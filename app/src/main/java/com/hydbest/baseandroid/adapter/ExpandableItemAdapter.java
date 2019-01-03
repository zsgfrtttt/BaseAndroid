package com.hydbest.baseandroid.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.View;

import com.chad.library.adapter.base.BaseMultiItemQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.chad.library.adapter.base.entity.MultiItemEntity;

import com.hydbest.baseandroid.entity.Fragmentation;
import com.hydbest.baseandroid.entity.Level0Item;

import java.util.List;

/**
 * Created by luoxw on 2016/8/9.
 */
public class ExpandableItemAdapter extends BaseMultiItemQuickAdapter<MultiItemEntity, BaseViewHolder> {
    private static final String TAG = ExpandableItemAdapter.class.getSimpleName();

    public static final int TYPE_LEVEL_0 = 0;
    public static final int TYPE_LEVEL_1 = 1;
    public static final int TYPE_PERSON = 2;

    /**
     * Same as QuickAdapter#QuickAdapter(Context,int) but with
     * some initialization data.
     *
     * @param data A new list is created out of this one to avoid mutable list
     */
    public ExpandableItemAdapter(List<MultiItemEntity> data) {
        super(data);
        addItemType(TYPE_LEVEL_0, com.hydbest.baseandroid.R.layout.item_expandable_lv0);
        addItemType(TYPE_LEVEL_1, com.hydbest.baseandroid.R.layout.item_expandable_lv1);
    }


    @Override
    protected void convert(final BaseViewHolder holder, final MultiItemEntity item) {
        switch (holder.getItemViewType()) {
            case TYPE_LEVEL_0:
                switch (holder.getLayoutPosition() %
                        3) {
                    case 0:
                        holder.setImageResource(com.hydbest.baseandroid.R.id.iv_head, com.hydbest.baseandroid.R.mipmap.dice_action_0);
                        break;
                    case 1:
                        holder.setImageResource(com.hydbest.baseandroid.R.id.iv_head, com.hydbest.baseandroid.R.mipmap.dice_action_1);
                        break;
                    case 2:
                        holder.setImageResource(com.hydbest.baseandroid.R.id.iv_head, com.hydbest.baseandroid.R.mipmap.dice_action_2);
                        break;
                }
                final Level0Item lv0 = (Level0Item) item;
                holder.setText(com.hydbest.baseandroid.R.id.title, lv0.title)
                        .setText(com.hydbest.baseandroid.R.id.sub_title, lv0.subTitle)
                        .setImageResource(com.hydbest.baseandroid.R.id.iv, lv0.isExpanded() ? com.hydbest.baseandroid.R.mipmap.arrow_b : com.hydbest.baseandroid.R.mipmap.arrow_r);
                holder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        int pos = holder.getAdapterPosition();
                        if (lv0.isExpanded()) {
                            collapse(pos);
                        } else {
                            expand(pos);
                        }
                    }
                });
                break;
            case TYPE_LEVEL_1:
                final Fragmentation fragmentation = (Fragmentation) item;
                holder.setText(com.hydbest.baseandroid.R.id.tv, fragmentation.content.split("-")[0]);
                holder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (fragmentation.clz != null) {
                            Context context = holder.itemView.getContext();
                            context.startActivity(new Intent(context, fragmentation.clz));
                        }
                    }
                });
                break;
        }
    }
}
