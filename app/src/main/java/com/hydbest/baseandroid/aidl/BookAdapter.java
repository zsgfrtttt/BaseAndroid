package com.hydbest.baseandroid.aidl;

import android.support.annotation.Nullable;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;

import java.util.List;

/**
 * Created by csz on 2018/11/28.
 */

public class BookAdapter extends BaseQuickAdapter<Book, BaseViewHolder> {
    public BookAdapter(@Nullable List<Book> data) {
        super(android.R.layout.simple_list_item_1, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, Book item) {
        helper.setText(android.R.id.text1, item.getBookId() + "  " + item.getBookName());
    }
}
