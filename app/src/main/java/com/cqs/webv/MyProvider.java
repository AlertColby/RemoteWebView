package com.cqs.webv;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;

/**
 * 作者:  陈庆松
 * 创建时间: 2020\12\4 0004 11:59
 * 邮箱:chen510470614@163.com
 */
public class MyProvider extends ContentProvider {

    private SQLiteDatabase mDb;

    @Override
    public boolean onCreate() {
        mDb = new ContentSqlHelper(getContext()).getWritableDatabase();
        return false;
    }

    public String getTableName(Uri uri){
        return null;
    }

    @Override
    public Cursor query( Uri uri,  String[] projection,  String selection,  String[] selectionArgs,  String sortOrder) {
        //这里需要验证uri和参数，但是这里只做示例，没写
        return mDb.query(getTableName(uri), projection, selection, selectionArgs,null,null,sortOrder,null);
    }

    
    @Override
    public String getType( Uri uri) {
        return null;
    }

    
    @Override
    public Uri insert( Uri uri,  ContentValues values) {
        return null;
    }

    @Override
    public int delete( Uri uri,  String selection,  String[] selectionArgs) {
        return 0;
    }

    @Override
    public int update( Uri uri,  ContentValues values,  String selection,  String[] selectionArgs) {
        return 0;
    }
}
