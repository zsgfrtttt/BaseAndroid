package com.hydbest.baseandroid.aidl;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

/**
 * Created by csz on 2018/11/28.
 */

public class MyDatabaseHelper extends SQLiteOpenHelper {

    private static final String TAG = "long.MyDatabaseHelper";

    private static final String CREATE_BOOK = "create table Book (" +
            "id integer primary key autoincrement," +
            "bookId integer," +
            "bookName text)";

    public MyDatabaseHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL(CREATE_BOOK);
        Log.i(TAG, "Book database create succeeded!");
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

    }
}
