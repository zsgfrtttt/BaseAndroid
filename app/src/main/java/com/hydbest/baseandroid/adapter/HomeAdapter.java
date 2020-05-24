package com.hydbest.baseandroid.adapter;

import android.view.View;

import com.chad.library.adapter.base.BaseMultiItemQuickAdapter;
import com.chad.library.adapter.base.entity.MultiItemEntity;
import com.chad.library.adapter.base.viewholder.BaseViewHolder;
import com.hydbest.baseandroid.R;

import java.util.List;

public class HomeAdapter extends BaseMultiItemQuickAdapter<MultiItemEntity, BaseViewHolder> {
    private static final int TYPE_TITLE = 0;
    private static final int TYPE_CONTENT = 1;

    public HomeAdapter(List<MultiItemEntity> data) {
        super(data);
        addItemType(TYPE_TITLE, R.layout.item_home_title);
        addItemType(TYPE_CONTENT, R.layout.item_home_content);
    }

    @Override
    protected void convert(final BaseViewHolder holder, final MultiItemEntity item) {
        switch (holder.getItemViewType()) {
            case TYPE_TITLE:
                holder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        int pos = holder.getAdapterPosition();

                    }});
                break;
            case TYPE_CONTENT:

                break;
        }

    }
}
