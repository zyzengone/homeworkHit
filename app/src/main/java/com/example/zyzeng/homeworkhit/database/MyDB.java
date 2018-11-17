package com.example.zyzeng.homeworkhit.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.support.annotation.Nullable;
import android.util.Log;

import java.util.HashMap;

import static android.content.ContentValues.TAG;

public class MyDB extends SQLiteOpenHelper {
    public static final String TABLE_NAME_HW = "hw";
    public static final String TABLE_NAME_FINISH ="finish";
    
    public static final String COLUMN_NAME_ID = "_id";
    public static final String COLUMN_NAME_SUBJECT = "subject";
    public static final String COLUMN_NAME_CONTENT = "content";
    public static final String COLUMN_NAME_DATE = "date";
    //用户表名
    public static final String TABLE_NAME_LOGIN = "login";
    // 列名
    private static final String KEY_ID = "id";
    private static final String KEY_NAME = "name";
    private static final String KEY_EMAIL = "email";
    private static final String KEY_UID = "uid";
    private static final String KEY_CREATED_AT = "created_at";

    public MyDB(@Nullable Context context, @Nullable String name, @Nullable SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    public MyDB(Context context) {
        super(context, "database", null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        String sql1 = "create table "+ TABLE_NAME_HW +"("+COLUMN_NAME_ID+" integer primary key autoincrement,"+
                COLUMN_NAME_SUBJECT+ " varchar(20),"+COLUMN_NAME_CONTENT+" varchar(64),"+COLUMN_NAME_DATE+" varchar(32))";
        sqLiteDatabase.execSQL(sql1);
        String sql2 = "create table "+ TABLE_NAME_FINISH +"("+COLUMN_NAME_ID+" integer primary key autoincrement,"+
                COLUMN_NAME_SUBJECT+ " varchar(20),"+COLUMN_NAME_CONTENT+" varchar(64),"+COLUMN_NAME_DATE+" varchar(32))";
        String sql3 = "CREATE TABLE " + TABLE_NAME_LOGIN + "("
                + KEY_ID + " INTEGER PRIMARY KEY," + KEY_NAME + " TEXT,"
                + KEY_EMAIL + " TEXT UNIQUE," + KEY_UID + " TEXT,"
                + KEY_CREATED_AT + " TEXT" + ")";
        sqLiteDatabase.execSQL(sql3);
        sqLiteDatabase.execSQL(sql2);
    }
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // 如果表存在则删除表
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME_LOGIN);

        // 重新创建表
        onCreate(db);
    }

    /**
     * Storing user details in database
     * */
    public void addUser(String name, String email, String uid, String created_at) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_NAME, name); // 名字
        values.put(KEY_EMAIL, email); // 邮箱
        values.put(KEY_UID, uid); // 唯一id,用于标示用户
        values.put(KEY_CREATED_AT, created_at); // 创建时间

        // 插入用户信息
        long id = db.insert(TABLE_NAME_LOGIN, null, values);
        db.close();

        Log.d(TAG, "New user inserted into sqlite: " + id);
    }

    /**
     * 从数据库获取用户信息
     * */
    public HashMap<String, String> getUserDetails() {
        HashMap<String, String> user = new HashMap<String, String>();
        String selectQuery = "SELECT  * FROM " + TABLE_NAME_LOGIN;

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
        cursor.moveToFirst();
        if (cursor.getCount() > 0) {
            user.put("name", cursor.getString(1));
            user.put("email", cursor.getString(2));
            user.put("uid", cursor.getString(3));
            user.put("created_at", cursor.getString(4));
        }
        cursor.close();
        db.close();
        Log.d(TAG, "Fetching user from Sqlite: " + user.toString());

        return user;
    }

    /**
     * 获取用户的登陆状态,如果表中没有数据说明用户没有登陆
     * */
    public int getRowCount() {
        String countQuery = "SELECT  * FROM " + TABLE_NAME_LOGIN;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(countQuery, null);
        int rowCount = cursor.getCount();
        db.close();
        cursor.close();

        // return row count
        return rowCount;
    }

    /**
     * 从数据库中删除所有信息
     * */
    public void deleteUsers() {
        SQLiteDatabase db = this.getWritableDatabase();
        // 删除所有列
        db.delete(TABLE_NAME_LOGIN, null, null);
        db.close();

        Log.d(TAG, "Deleted all user info from sqlite");
    }


}
