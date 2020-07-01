package com.hydbest.baseandroid.aidl

import android.annotation.SuppressLint
import android.content.ContentValues
import android.content.Intent
import android.database.sqlite.SQLiteDatabase
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.hydbest.baseandroid.R
import com.hydbest.baseandroid.aidl.AddBookActivity
import kotlinx.android.synthetic.main.activity_book.*
import java.util.*

/**
 * Created by csz on 2018/11/28.
 */
class BookActivity : AppCompatActivity() {

    private var database: SQLiteDatabase? = null
    @SuppressLint("Recycle")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_book)
        val dbHelper = MyDatabaseHelper(this, "BookStore.db", null, 1)
        database = dbHelper.readableDatabase

        //如果数据库为空就向数据库添加几个数据
        if (!database!!.query("Book", null, null, null, null, null, null).moveToFirst()) {
            addBooksToDB()
        }
        refresh(null)
    }

    private fun addBooksToDB() {
        val values = ContentValues()
        values.put("bookId", 0)
        values.put("bookName", "玉女真经")
        database!!.insert("Book", null, values)
        values.clear()
        values.put("bookId", 1)
        values.put("bookName", "辟邪剑法")
        database!!.insert("Book", null, values)
        values.clear()
        values.put("bookId", 2)
        values.put("bookName", "如来神掌")
        database!!.insert("Book", null, values)
        values.clear()
        values.put("bookId", 3)
        values.put("bookName", "降龙十八掌")
        database!!.insert("Book", null, values)
        values.clear()
        values.put("bookId", 4)
        values.put("bookName", "九阴真经")
        database!!.insert("Book", null, values)
        values.clear()
        values.put("bookId", 5)
        values.put("bookName", "九阳神功")
        database!!.insert("Book", null, values)
        values.clear()
        values.put("bookId", 6)
        values.put("bookName", "打狗棒法")
        database!!.insert("Book", null, values)
        values.clear()
        values.put("bookId", 7)
        values.put("bookName", "乾坤大挪移")
        database!!.insert("Book", null, values)
        values.clear()
        values.put("bookId", 8)
        values.put("bookName", "易筋经")
        database!!.insert("Book", null, values)
        values.clear()
        values.put("bookId", 9)
        values.put("bookName", "六脉神剑")
        database!!.insert("Book", null, values)
        values.clear()
        values.put("bookId", 10)
        values.put("bookName", "黯然销魂掌")
        database!!.insert("Book", null, values)
        values.clear()
        values.put("bookId", 11)
        values.put("bookName", "九阴白骨爪")
        database!!.insert("Book", null, values)
        values.clear()
        values.put("bookId", 12)
        values.put("bookName", "独孤九剑")
        database!!.insert("Book", null, values)
    }

    fun refresh(view: View?) {
        val list = ArrayList<Book>()
        val cursor = database!!.query("Book", null, null, null, null, null, null)
        if (cursor != null) {
            if (cursor.moveToFirst()) {
                do {
                    val bookId = cursor.getInt(cursor.getColumnIndex("bookId"))
                    val bookName = cursor.getString(cursor.getColumnIndex("bookName"))
                    val book = Book(bookId, bookName)
                    list.add(book)
                } while (cursor.moveToNext())
            }
            cursor.close()
        }
        val adapter = BookAdapter(list)
        rv.layoutManager = LinearLayoutManager(this)
        rv.adapter = adapter
    }

    fun startProcess(view: View?) {
        startActivity(Intent(this, AddBookActivity::class.java))
    }
}