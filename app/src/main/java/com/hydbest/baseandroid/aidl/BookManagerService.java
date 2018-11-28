package com.hydbest.baseandroid.aidl;

import android.app.Service;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Binder;
import android.os.IBinder;
import android.os.RemoteException;
import android.support.annotation.Nullable;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by csz on 2018/11/28.
 */

public class BookManagerService extends Service {
    private static final String TAG = "guo.BookManagerService";

    private SQLiteDatabase database;
    private List<Book> mBookList = new ArrayList<>();

    private Binder mBinder = new IBookManager.Stub() {

        @Override
        public List<Book> getBookList() throws RemoteException {
            return mBookList;
        }

        @Override
        public void addBook(Book book) throws RemoteException {
            addBookIntoDB(book);
        }
    };

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return mBinder;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        Log.d(TAG, "service onCreate");

        queryBooksFromDB();
    }

    /**
     * 从数据库中获取Book列表，数据库操作{@link MyDatabaseHelper}
     */
    private void queryBooksFromDB() {
        Log.i(TAG, "service starting query Book from db.");
        MyDatabaseHelper dbHelper = new MyDatabaseHelper(this, "BookStore.db", null, 1);
        database = dbHelper.getReadableDatabase();
        Cursor cursor = database.query("Book", null, null, null, null, null, null);
        if (cursor.moveToFirst()) {
            do {
                int bookId = cursor.getInt(cursor.getColumnIndex("bookId"));
                String bookName = cursor.getString(cursor.getColumnIndex("bookName"));
                Book book = new Book(bookId, bookName);
                mBookList.add(book);
            } while (cursor.moveToNext());
        }
        cursor.close();
    }

    private void addBookIntoDB(Book book) {
        Log.i(TAG, "service starting add Book to db.");
        ContentValues values = new ContentValues();
        values.put("bookId", book.getBookId());
        values.put("bookName", book.getBookName());
        database.insert("Book", null, values);
    }
}

