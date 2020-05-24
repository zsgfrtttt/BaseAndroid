package com.hydbest.baseandroid.entity;

import com.chad.library.adapter.base.entity.MultiItemEntity;
import com.chad.library.adapter.base.entity.node.BaseExpandNode;
import com.chad.library.adapter.base.entity.node.BaseNode;
import com.hydbest.baseandroid.adapter.ExpandableItemAdapter;

import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by luoxw on 2016/8/10.
 */
public class Level0Item extends BaseExpandNode implements MultiItemEntity {
    public String title;
    public String subTitle;
    private List<BaseNode> list = new ArrayList<>();

    public Level0Item(String title, String subTitle) {
        this.subTitle = subTitle;
        this.title = title;
        setExpanded(false);
    }

    @Override
    public int getItemType() {
        return ExpandableItemAdapter.TYPE_LEVEL_0;
    }

    @Nullable
    @Override
    public List<BaseNode> getChildNode() {
        return list;
    }

    public void addSubItem(BaseNode item) {
        list.add(item);
    }
}
