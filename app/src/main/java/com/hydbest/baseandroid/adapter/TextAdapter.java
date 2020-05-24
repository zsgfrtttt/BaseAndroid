package com.hydbest.baseandroid.adapter;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.viewholder.BaseViewHolder;
import com.hydbest.baseandroid.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by csz on 2018/11/5.
 */

public class TextAdapter extends BaseQuickAdapter<TextAdapter.Bean, BaseViewHolder> {

    public TextAdapter(List data) {
        super(R.layout.item_text, data);
        data = new ArrayList();
        for (int i = 0; i < 20; i++) {
            data.add(new Bean("i:"+i,null));
        }
        setNewData(data);
    }

    @Override
    protected void convert(BaseViewHolder helper, Bean item) {
        helper.setText(android.R.id.text1, item.title);
        helper.setImageResource(R.id.iv,R.drawable.a);
        helper.setVisible(R.id.iv,false);
        addChildClickViewIds(R.id.item);
    }

    public static class Bean{
        public String title;
        public String url;

        public Bean(String title, String url) {
            this.title = title;
            this.url = url;
        }
    }

}
