package com.gowhich.androidloadermanager.service;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.support.annotation.Nullable;

import com.gowhich.androidloadermanager.db.DBHelper;

/**
 * Created by durban126 on 16/9/28.
 */

public class PersonContentProvider extends ContentProvider {

    private DBHelper dbHelper;

    private final static UriMatcher URI_MATCHER = new UriMatcher(UriMatcher.NO_MATCH);
    private final static int PERSON = 1;
    private final static int PERSONS = 2;

    static {
        URI_MATCHER.addURI("com.gowhich.androidloadermanager.service.PersonContentProvider", "person", PERSONS);
        URI_MATCHER.addURI("com.gowhich.androidloadermanager.service.PersonContentProvider", "person/#", PERSON);
    }

    @Override
    public boolean onCreate() {
        dbHelper = new DBHelper(getContext());
        return false;
    }

    @Nullable
    @Override
    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {
        return null;
    }

    @Nullable
    @Override
    public String getType(Uri uri) {
        int flag = URI_MATCHER.match(uri);
        switch(flag){
            case PERSON:
                //单条记录
                return "vnd.android.cursor.item/person";
            case PERSONS:
                //多条记录
                return "vnd.android.cursor.item/persons";
        }

        return null;
    }

    @Nullable
    @Override
    public Uri insert(Uri uri, ContentValues values) {
        int flag = URI_MATCHER.match(uri);
        SQLiteDatabase sqLiteDatabase = dbHelper.getWritableDatabase();
        Uri uri1 = null;
        switch (flag){
            case PERSON:
                break;
            case PERSONS:
                long id = sqLiteDatabase.insert("person", null, values);
                uri1 = ContentUris.withAppendedId(uri, id);
                break;
        }

        System.out.println("======>" + uri1.toString());
        return uri1;
    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        int count = 0;
        int flag = URI_MATCHER.match(uri);
        SQLiteDatabase sqLiteDatabase = dbHelper.getWritableDatabase();
        switch (flag){
            case PERSON:
                long pid = ContentUris.parseId(uri);
                String whereCond = " pid = " + pid;
                if(selection != null && selection.equals("")){
                    whereCond += selection;
                }
                count = sqLiteDatabase.delete("person", whereCond, selectionArgs);
                break;
            case PERSONS:
                count = sqLiteDatabase.delete("person", selection, selectionArgs);
                break;
        }
        return count;
    }

    @Override
    public int update(Uri uri, ContentValues values, String selection, String[] selectionArgs) {
        int count = 0;
        int flag = URI_MATCHER.match(uri);
        SQLiteDatabase sqLiteDatabase = dbHelper.getWritableDatabase();
        switch (flag){
            case PERSON:
                long id = ContentUris.parseId(uri);
                String whereCond = " pid = " + id;
                if(selection != null && selection.equals("")){
                    whereCond += selection;
                }
                sqLiteDatabase.update("person", values, whereCond, selectionArgs);
                break;
            case PERSONS:
                sqLiteDatabase.update("person", values, selection, selectionArgs);
                break;
        }
        return count;
    }
}
