package cqk.usst.androidexame.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

/**
 * Created by 10033 on 2018/3/28.
 */

public class DatabaseHelper extends SQLiteOpenHelper {
    public static final String DB_NAME = "download.db";
    public static final int VERSION = 1;
    public static final String SQL_CREATE = "create table thread_info(" +
            "id integer primary key autoincrement," +
            "thread_id integer,url text,started integer,ended integer,finished integer)";
    public static final String SQL_DROP = "drop table if exists thread_info";


    public DatabaseHelper(Context context) {
        super(context, DB_NAME, null, VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        Log.i("db:", "-----create------");
        db.execSQL(SQL_CREATE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL(SQL_DROP);
        db.execSQL(SQL_CREATE);
        Log.i("db:", "-----upgrade------");
    }

    @Override
    public void onOpen(SQLiteDatabase db) {
        Log.i("db:", "-----open------");
        super.onOpen(db);
//        db.execSQL(SQL_DROP);
//        db.execSQL(SQL_CREATE);

    }
}
