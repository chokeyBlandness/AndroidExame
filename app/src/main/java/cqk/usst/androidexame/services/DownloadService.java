package cqk.usst.androidexame.services;


import android.annotation.SuppressLint;
import android.app.Service;
import android.content.Intent;
import android.os.Environment;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.support.annotation.Nullable;
import android.util.Log;

import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.net.MalformedURLException;

import cqk.usst.androidexame.entities.FileInfo;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Created by 10033 on 2018/3/27.
 */

public class DownloadService extends Service {
    public static final String ACTION_START = "ACTION_START";
    public static final String ACTION_STOP = "ACTION_STOP";
    public static final String DOWNLOAD_PATH =
            Environment.getExternalStorageDirectory().getAbsolutePath() + "/downloads/";
    public static final int MSG_INIT = 0;
    public static final String ACTION_UPDATE = "ACTION_UPDATE";
    private DownloadTask downloadTask = null;

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        if (ACTION_START.equals(intent.getAction())) {
            FileInfo fileInfo = (FileInfo) intent.getSerializableExtra("fileInfo");
            Log.i("test", "start:" + fileInfo.toString());

            new InitThread(fileInfo).start();
        }else if (ACTION_STOP.equals(intent.getAction())) {
            FileInfo fileInfo = (FileInfo) intent.getSerializableExtra("fileInfo");
            Log.i("test", "stop:" + fileInfo.toString());
            if (downloadTask != null) {
                downloadTask.isPause = true;
            }
        }
        return super.onStartCommand(intent, flags, startId);
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }




    @SuppressLint("HandlerLeak")
    Handler downloadHandler=new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case DownloadService.MSG_INIT:
                    FileInfo fileInfo = (FileInfo) msg.obj;
                    Log.i("test", "Init:" + fileInfo.toString());//?
                    downloadTask = new DownloadTask(DownloadService.this,fileInfo);
                    downloadTask.download();
                    break;
            }
        }


    };


    public class InitThread extends Thread {
        private FileInfo fileInfo = null;

        public InitThread(FileInfo fileInfo) {
            this.fileInfo = fileInfo;
        }



        @Override
        public void run() {
            RandomAccessFile randomAccessFile = null;
            try {
                //connect file

                OkHttpClient okHttpClient = new OkHttpClient();
                Request request = new Request.Builder()
                        .url(fileInfo.getUrl())
                        .build();
                Response response = okHttpClient.newCall(request).execute();



                int length = -1;
                if (response.code() == 200) {
                    //get file length
                    length = (int) response.body().contentLength();
                }
                if (length < 0) {
                    return;
                }
                File dir = new File(DownloadService.DOWNLOAD_PATH);
                if (!dir.exists()) {
                    dir.mkdir();
                }

                //create file at local
                File file = new File(dir, fileInfo.getName());
                randomAccessFile = new RandomAccessFile(file, "rwd");

                //initialize file length
                randomAccessFile.setLength(length);
                fileInfo.setLength(length);
                downloadHandler.obtainMessage(DownloadService.MSG_INIT, fileInfo).sendToTarget();
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }finally {
                try {
                    randomAccessFile.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }


}
