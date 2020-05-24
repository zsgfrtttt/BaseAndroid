package com.hydbest.baseandroid.aidl;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.os.RemoteException;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import android.view.View;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.hydbest.baseandroid.R;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by csz on 2018/11/28.
 */

public class AddBookActivity extends AppCompatActivity {

    private static final String SERVICE_PACKAGE = "com.hydbest.baseandroid";
    private static final String SERVICE_ACTION = "BookManagerService";

    @BindView(R.id.edit_book_id)
    EditText mEditBookId;
    @BindView(R.id.edit_book_name)
    AutoCompleteTextView mEditBookName;
    @BindView(R.id.button_ok)
    Button mButtonOk;
    @BindView(R.id.button_cancel)
    Button mButtonCancel;

    private IBookManager mIBookManager;

    ServiceConnection connection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName componentName, IBinder iBinder) {
            //如果客户端和服务端在同一个进程下，那么asInterface()将返回Stub对象本身，否则返回Stub.Proxy对象(会走底层binder)。
            mIBookManager = IBookManager.Stub.asInterface(iBinder);
            Toast.makeText(AddBookActivity.this,"连接服务器成功",Toast.LENGTH_SHORT).show();
        }

        @Override
        public void onServiceDisconnected(ComponentName componentName) {
            Toast.makeText(AddBookActivity.this, "onServiceDisconnected!", Toast.LENGTH_SHORT).show();
        }
    };

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_book);
        ButterKnife.bind(this);
    }

    @OnClick({R.id.button_ok, R.id.button_cancel,R.id.btn_book_list})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.btn_book_list:
                requestBookList();
                break;
            case R.id.button_ok:
                addBook(Integer.parseInt(mEditBookId.getText().toString().trim()),mEditBookName.getText().toString().trim());
                break;
            default:
                break;
        }
    }

    /**
     * 添加Book{@link Book}
     */
    private void addBook(int bookId, String bookName) {
        if (mIBookManager == null) {
            return;
        }
        try {
            Book book = new Book(bookId, bookName);
            mIBookManager.addBook(book);
            Toast.makeText(this, "Add succeed", Toast.LENGTH_SHORT).show();
        } catch (RemoteException e) {
            e.printStackTrace();
            Toast.makeText(this, "添加失败", Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * 获取Book列表
     */
    private void requestBookList() {
        bindService();
    }

    /**
     * 连接远程服务
     */
    private void bindService() {
        Intent intent = new Intent();
        intent.setPackage(SERVICE_PACKAGE);
        intent.setAction(SERVICE_ACTION);
        bindService(intent, connection, Context.BIND_AUTO_CREATE);
    }
}
