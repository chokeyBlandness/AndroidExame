package cqk.usst.androidexame.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;


import java.util.ArrayList;
import java.util.List;

import cqk.usst.androidexame.entities.ThreadInfo;

/**
 * Created by 10033 on 2018/3/28.
 */

public class ThreadDAOImpl implements ThreadDAO {

    private DatabaseHelper databaseHelper = null;

    public ThreadDAOImpl(Context context) {
        this.databaseHelper = new DatabaseHelper(context);
    }

    @Override
    public void insertThread(ThreadInfo threadInfo) {
        SQLiteDatabase sqLiteDatabase = databaseHelper.getWritableDatabase();
//        sqLiteDatabase.execSQL(
//                "insert into thread_info(thread_id,url,started,ended,finished) values(?,?,?,?,?)",
//                new Object[]{threadInfo.getId(),threadInfo.getUrl(),threadInfo.getStart(),
//                threadInfo.getEnded(),threadInfo.getFinished()});
        ContentValues contentValues = new ContentValues();
        contentValues.put("thread_id",threadInfo.getId());
        contentValues.put("url",threadInfo.getUrl());
        contentValues.put("started",threadInfo.getStart());
        contentValues.put("ended",threadInfo.getEnded());
        contentValues.put("finished",threadInfo.getFinished());
        sqLiteDatabase.insert("thread_info",null,contentValues);
        sqLiteDatabase.close();
    }

    @Override
    public void deleteThread(String url, int thread_id) {
        SQLiteDatabase sqLiteDatabase = databaseHelper.getWritableDatabase();
//        sqLiteDatabase.execSQL(
//                "delete from thread_info where url= ? and thread_id=?",
//                new Object[]{url,thread_id});
        ContentValues contentValues = new ContentValues();
        contentValues.put("url",url);
        contentValues.put("thread_id",thread_id);
        sqLiteDatabase.delete("thread_info","url= ? and thread_id=?",
                new String[]{url, String.valueOf(thread_id)});
        sqLiteDatabase.close();
    }

    @Override
    public void updataThread(String url, int thread_id, int finished) {
        SQLiteDatabase sqLiteDatabase = databaseHelper.getWritableDatabase();
//        sqLiteDatabase.execSQL(
//                "update thread_info set finished=? where url=? and thread_id=?",
//                new Object[]{finished,url,thread_id});
        ContentValues contentValues = new ContentValues();
        contentValues.put("finished",finished);
        sqLiteDatabase.update("thread_info",contentValues,"url=? and thread_id=?",
                new String[]{url, String.valueOf(thread_id)});
        sqLiteDatabase.close();
    }

    @Override
    public List<ThreadInfo> getThreads(String url) {
        List<ThreadInfo> threadInfos = new ArrayList<ThreadInfo>();
        SQLiteDatabase sqLiteDatabase = databaseHelper.getWritableDatabase();
        Cursor cursor=sqLiteDatabase.rawQuery("select * from thread_info where url=?",new String[]{url});
        while (cursor.moveToNext()) {
            ThreadInfo threadInfo = new ThreadInfo();
            threadInfo.setId(cursor.getInt(cursor.getColumnIndex("thread_id")));
            threadInfo.setUrl(cursor.getString(cursor.getColumnIndex("url")));
            threadInfo.setStart(cursor.getInt(cursor.getColumnIndex("started")));
            threadInfo.setEnded(cursor.getInt(cursor.getColumnIndex("ended")));
            threadInfo.setFinished(cursor.getInt(cursor.getColumnIndex("finished")));
            threadInfos.add(threadInfo);
        }
        cursor.close();
        sqLiteDatabase.close();
        return threadInfos;
    }

    @Override
    public boolean isExists(String url, int thread_id) {
        boolean isExist;
        SQLiteDatabase sqLiteDatabase = databaseHelper.getWritableDatabase();
        Cursor cursor=sqLiteDatabase.rawQuery("select * from thread_info where url=? and thread_id=?",
                new String[]{url, thread_id + ""});
        isExist = cursor.moveToNext();
        cursor.close();
        sqLiteDatabase.close();
        return isExist;
    }
}
