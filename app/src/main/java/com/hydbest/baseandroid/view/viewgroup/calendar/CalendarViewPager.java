package com.hydbest.baseandroid.view.viewgroup.calendar;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;

/**
 * Created by csz on 2018/10/26.
 */

public class CalendarViewPager extends ViewPager{

    public CalendarViewPager(@NonNull Context context) {
        this(context,null);
    }

    public CalendarViewPager(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public void setAdapter(@Nullable PagerAdapter adapter) {
        super.setAdapter(adapter);
        setCurrentItem(adapter.getCount()/2,false);
    }
}
