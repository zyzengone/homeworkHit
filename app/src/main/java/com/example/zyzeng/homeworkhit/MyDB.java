package com.example.zyzeng.homeworkhit;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class MyDB extends SQLiteOpenHelper {
    public static final String TABLE_NAME_HW = "hw";
    public static final String COLUMN_NAME_ID = "_id";
    public static final String COLUMN_NAME_SUBJECT = "subject";
    public static final String COLUMN_NAME_CONTENT = "content";
    public static final String COLUMN_NAME_DATE = "date";
    public MyDB(Context context) {
        super(context, "database", null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        String sql = "create table "+ TABLE_NAME_HW +"("+COLUMN_NAME_ID+" integer primary key autoincrement,"+
                COLUMN_NAME_SUBJECT+ " varchar(20),"+COLUMN_NAME_CONTENT+" varchar(64),"+COLUMN_NAME_DATE+" varchar(32))";
        sqLiteDatabase.execSQL(sql);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

    }
}
