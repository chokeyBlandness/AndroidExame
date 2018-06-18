package cqk.usst.androidexame.services;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;


import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.RandomAccessFile;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.util.List;

import cqk.usst.androidexame.database.ThreadDAO;
import cqk.usst.androidexame.database.ThreadDAOImpl;
import cqk.usst.androidexame.entities.FileInfo;
import cqk.usst.androidexame.entities.ThreadInfo;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Created by 10033 on 2018/3/28.
 */

public class DownloadTask {
    private Context context = null;
    private FileInfo fileInfo = null;
    private ThreadDAO threadDAO = null;
    private int finished = 0;
    public boolean isPause = false;

    public DownloadTask(Context context, FileInfo fileInfo) {
        this.context = context;
        this.fileInfo = fileInfo;
        this.threadDAO = new ThreadDAOImpl(context);
    }

    public void download() {
        List<ThreadInfo> threadInfos = threadDAO.getThreads(fileInfo.getUrl());
        ThreadInfo threadInfo = null;
        if (threadInfos.size() == 0) {
            threadInfo = new ThreadInfo(0, fileInfo.getUrl(), 0, fileInfo.getLength(), 0);
        } else {
            threadInfo = threadInfos.get(0);
        }

        new DownloadThread(threadInfo).start();
    }


    public class DownloadThread extends Thread {
        private ThreadInfo threadInfo = null;

        public DownloadThread(ThreadInfo threadInfo) {
            this.threadInfo = threadInfo;
        }

        @Override
        public void run() {

            if (!threadDAO.isExists(threadInfo.getUrl(), threadInfo.getId())) {
                threadDAO.insertThread(threadInfo);
            }
            RandomAccessFile randomAccessFile = null;
            InputStream inputStream = null;

            try {
//                URL url = new URL(threadInfo.getUrl());
//                httpURLConnection = (HttpURLConnection) url.openConnection();
//                httpURLConnection.setConnectTimeout(5000);
//                httpURLConnection.setRequestMethod("GET");


                int start = threadInfo.getStart() + threadInfo.getFinished();
                //httpURLConnection.setRequestProperty("Range","bytes="+start+"-"+threadInfo.getEnded());

                OkHttpClient okHttpClient = new OkHttpClient();
                Request request = new Request.Builder()
                        .addHeader("Range","bytes="+start+"-"+threadInfo.getEnded())
                        .url(threadInfo.getUrl())
                        .build();
                Response response = okHttpClient.newCall(request).execute();

                File file = new File(DownloadService.DOWNLOAD_PATH, fileInfo.getName());
                randomAccessFile = new RandomAccessFile(file, "rwd");
                randomAccessFile.seek(start);

                Intent intent = new Intent(DownloadService.ACTION_UPDATE);
                finished += threadInfo.getFinished();

                if (response.code()==206) {//HTTPStatus.SC_PARTIAL_CONTENT
                    //inputStream = httpURLConnection.getInputStream();
                    inputStream = response.body().byteStream();
                    byte[] buffer = new byte[1024 * 4];
                    int length = -1;

                    long time = System.currentTimeMillis();

                    while ((length = inputStream.read(buffer)) != -1) {
                        randomAccessFile.write(buffer, 0, length);

                        finished += length;

                        if (System.currentTimeMillis() - time > 200) {
                            time = System.currentTimeMillis();
                            intent.putExtra("finished", finished * 100 / fileInfo.getLength());
                            context.sendBroadcast(intent);
                        }
                        if (finished == fileInfo.getLength()) {
                            intent.putExtra("finished", 100);
                            context.sendBroadcast(intent);
                        }

                        if (isPause) {
                            threadDAO.updataThread(threadInfo.getUrl(), threadInfo.getId(), finished);
                            return;
                        }
                    }

                    threadDAO.deleteThread(threadInfo.getUrl(), threadInfo.getId());
                }
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }finally {
                try {
                    inputStream.close();
                    randomAccessFile.close();
                    //httpURLConnection.disconnect();
                } catch (IOException e) {
                    e.printStackTrace();
                }

            }
        }
    }


}
