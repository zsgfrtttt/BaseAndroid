package com.hydbest.baseandroid.aidl;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.View;

import com.hydbest.baseandroid.R;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by csz on 2018/11/28.
 */

public class BookActivity extends AppCompatActivity {

    @BindView(R.id.rv)
    RecyclerView mRv;
    private SQLiteDatabase database;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_book);
        ButterKnife.bind(this);

        MyDatabaseHelper dbHelper = new MyDatabaseHelper(this, "BookStore.db", null, 1);
        database = dbHelper.getReadableDatabase();

        //如果数据库为空就向数据库添加几个数据
        if (!database.query("Book", null, null, null, null, null, null).moveToFirst()) {
            addBooksToDB();
        }
        refresh(null);
    }

    private void addBooksToDB() {
        ContentValues values = new ContentValues();
        values.put("bookId", 0);
        values.put("bookName", "玉女真经");
        database.insert("Book", null, values);
        values.clear();

        values.put("bookId", 1);
        values.put("bookName", "辟邪剑法");
        database.insert("Book", null, values);
        values.clear();

        values.put("bookId", 2);
        values.put("bookName", "如来神掌");
        database.insert("Book", null, values);
        values.clear();

        values.put("bookId", 3);
        values.put("bookName", "降龙十八掌");
        database.insert("Book", null, values);
        values.clear();

        values.put("bookId", 4);
        values.put("bookName", "九阴真经");
        database.insert("Book", null, values);
        values.clear();

        values.put("bookId", 5);
        values.put("bookName", "九阳神功");
        database.insert("Book", null, values);
        values.clear();

        values.put("bookId", 6);
        values.put("bookName", "打狗棒法");
        database.insert("Book", null, values);
        values.clear();

        values.put("bookId", 7);
        values.put("bookName", "乾坤大挪移");
        database.insert("Book", null, values);
        values.clear();

        values.put("bookId", 8);
        values.put("bookName", "易筋经");
        database.insert("Book", null, values);
        values.clear();

        values.put("bookId", 9);
        values.put("bookName", "六脉神剑");
        database.insert("Book", null, values);
        values.clear();

        values.put("bookId", 10);
        values.put("bookName", "黯然销魂掌");
        database.insert("Book", null, values);
        values.clear();

        values.put("bookId", 11);
        values.put("bookName", "九阴白骨爪");
        database.insert("Book", null, values);
        values.clear();

        values.put("bookId", 12);
        values.put("bookName", "独孤九剑");
        database.insert("Book", null, values);
    }

    public void refresh(View view) {
        List list = new ArrayList();
        Cursor cursor = database.query("Book", null, null, null, null, null, null);
        if (cursor != null) {
            if (cursor.moveToFirst()) {
                do {
                    int bookId = cursor.getInt(cursor.getColumnIndex("bookId"));
                    String bookName = cursor.getString(cursor.getColumnIndex("bookName"));
                    Book book = new Book(bookId, bookName);
                    list.add(book);
                } while (cursor.moveToNext());
            }
            cursor.close();
        }

        BookAdapter adapter = new BookAdapter(list);
        mRv.setLayoutManager(new LinearLayoutManager(this));
        mRv.setAdapter(adapter);
    }

    public void startProcess(View view){
        startActivity(new Intent(this,AddBookActivity.class));
    }
}
