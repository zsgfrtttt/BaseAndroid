package com.hydbest.baseandroid.view.viewgroup.calendar;

import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;

import java.util.Calendar;

/**
 * Created by csz on 2018/10/26.
 */

public class CalenderAdapter extends PagerAdapter {

    private ViewPager mViewPager;
    private Calendar mCalendar;

    public CalenderAdapter(ViewPager viewPager, Calendar calendar) {
        mViewPager = viewPager;
        mCalendar = calendar;
    }

    public void setChangeListener(final ChangeListener listener) {
        mViewPager.addOnPageChangeListener(new ViewPager.SimpleOnPageChangeListener(){
            @Override
            public void onPageSelected(int position) {
                if (listener!= null) {
                    Calendar calendar = getCalendarByPosition(position);

                    listener.onChange(calendar);
                }
            }
        });
    }

    @Override
    public int getCount() {
        return 500;
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        Calendar calendar = getCalendarByPosition(position);

        GridView gv = new GridView(container.getContext());
        gv.setNumColumns(7);
        gv.setSelector(new ColorDrawable(Color.TRANSPARENT));
        gv.setAdapter(new DateAdapter(container.getContext(), calendar));
        container.addView(gv, container.getLayoutParams());
        return gv;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        View view = (View) object;
        container.removeView(view);
    }

    @Override
    public void finishUpdate(ViewGroup container) {
        int position = mViewPager.getCurrentItem();
        /*if (position == 0) {
            position = mViewPager.getFristItem();
        } else if (position == getCount() - 1) {
            position = viewPager.getLastItem();
        }*/
        try {
            mViewPager.setCurrentItem(position, false);
        } catch (IllegalStateException e) {
        }
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }

    public Calendar getCalendarByPosition(int position){
        int zreo = getCount() / 2;
        Calendar calendar = (Calendar) mCalendar.clone();
        calendar.set(Calendar.MONTH, mCalendar.get(Calendar.MONTH) + position - zreo);
        return calendar;
    }

    interface ChangeListener {
        void onChange(Calendar calendar);
    }

}
