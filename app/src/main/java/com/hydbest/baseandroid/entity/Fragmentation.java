package com.hydbest.baseandroid.entity;

import com.chad.library.adapter.base.entity.MultiItemEntity;
import com.hydbest.baseandroid.adapter.ExpandableItemAdapter;

/**
 * Created by luoxw on 2016/8/10.
 */

public class Fragmentation implements MultiItemEntity {

    public Fragmentation(String content, Class<?> clas) {
        this.content = content;
        this.clz = clas;
    }

    public String content;
    public Class clz;

    @Override
    public int getItemType() {
        return ExpandableItemAdapter.TYPE_LEVEL_1;
    }
}