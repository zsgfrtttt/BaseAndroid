package com.hydbest.baseandroid.aidl

import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.ServiceConnection
import android.os.Bundle
import android.os.IBinder
import android.os.RemoteException
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.hydbest.baseandroid.R
import kotlinx.android.synthetic.main.activity_add_book.*

/**
 * Created by csz on 2018/11/28.
 */
class AddBookActivity : AppCompatActivity() {

    private var mIBookManager: IBookManager? = null
    var connection: ServiceConnection = object : ServiceConnection {
        override fun onServiceConnected(componentName: ComponentName, iBinder: IBinder) {
            //如果客户端和服务端在同一个进程下，那么asInterface()将返回Stub对象本身，否则返回Stub.Proxy对象(会走底层binder)。
            mIBookManager = IBookManager.Stub.asInterface(iBinder)
            Toast.makeText(this@AddBookActivity, "连接服务器成功", Toast.LENGTH_SHORT).show()
        }

        override fun onServiceDisconnected(componentName: ComponentName) {
            Toast.makeText(this@AddBookActivity, "onServiceDisconnected!", Toast.LENGTH_SHORT).show()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_book)
        initEvent()
    }

    private fun initEvent() {
        button_ok.setOnClickListener {
            addBook(edit_book_id.text.toString().trim { it <= ' ' }.toInt(), edit_book_name.text.toString().trim { it <= ' ' })
        }
        btn_book_list.setOnClickListener {
            requestBookList()
        }
    }

    /**
     * 添加Book[Book]
     */
    private fun addBook(bookId: Int, bookName: String) {
        if (mIBookManager == null) {
            return
        }
        try {
            val book = Book(bookId, bookName)
            mIBookManager!!.addBook(book)
            Toast.makeText(this, "Add succeed", Toast.LENGTH_SHORT).show()
        } catch (e: RemoteException) {
            e.printStackTrace()
            Toast.makeText(this, "添加失败", Toast.LENGTH_SHORT).show()
        }
    }

    /**
     * 获取Book列表
     */
    private fun requestBookList() {
        bindService()
    }

    /**
     * 连接远程服务
     */
    private fun bindService() {
        val intent = Intent()
        intent.setPackage(SERVICE_PACKAGE)
        intent.action = SERVICE_ACTION
        bindService(intent, connection, Context.BIND_AUTO_CREATE)
    }

    companion object {
        private const val SERVICE_PACKAGE = "com.hydbest.baseandroid"
        private const val SERVICE_ACTION = "BookManagerService"
    }
}