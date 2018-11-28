// IBookManager.aidl
package com.hydbest.baseandroid.aidl;
import com.hydbest.baseandroid.aidl.Book;

// Declare any non-default types here with import statements

//在AIDL文件中用到了自定义的Parcelable对象时，必须新建一个和该对象同名的.aidl文件

interface IBookManager {
        /*
         * 向客户端提供获取图书列表接口
         */
        List<Book> getBookList();

        /*
         * 向客户端提供添加图书接口  in：输入   out:输出   inout：输入输出
         */
        void addBook(in Book book);
}
