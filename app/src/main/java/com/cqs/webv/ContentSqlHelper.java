package com.cqs.webv;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * 作者:  陈庆松
 * 创建时间: 2020\12\4 0004 14:19
 * 邮箱:chen510470614@163.com
 */
public class ContentSqlHelper extends SQLiteOpenHelper {

    private static final String DB_NAME = "content.db";
    private static final int DB_VERSION = 1;

    public static final String BOOK_TABLE_NAME = "book";
    public static final String USER_TABLE_NAME = "user";

    private String CREATE_BOOK_TABLE = "CREATE TABLE IF NOT EXISTS " +
            BOOK_TABLE_NAME + "(_id INTEGER PRIMARY KEY, name TEXT)";
    private String CREATE_USER_TABLE = "CREATE TABLE IF NOT EXISTS " +
            USER_TABLE_NAME + "(_id INTEGER PRIMARY KEY, name TEXT, sex INT)";

    public ContentSqlHelper(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_BOOK_TABLE);
        db.execSQL(CREATE_USER_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        //TODO IGNORE
    }
}
