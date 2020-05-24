package com.hydbest.baseandroid.entity;

import com.chad.library.adapter.base.entity.MultiItemEntity;
import com.chad.library.adapter.base.entity.node.BaseExpandNode;
import com.chad.library.adapter.base.entity.node.BaseNode;
import com.hydbest.baseandroid.adapter.ExpandableItemAdapter;

import org.jetbrains.annotations.Nullable;

import java.util.List;

/**
 * Created by luoxw on 2016/8/10.
 */

public class Fragmentation extends BaseExpandNode implements MultiItemEntity {

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

    @Nullable
    @Override
    public List<BaseNode> getChildNode() {
        return null;
    }
}