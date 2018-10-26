package com.hydbest.baseandroid.view.viewgroup.calendar;

import android.annotation.SuppressLint;
import android.content.Context;
import android.support.annotation.Nullable;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.hydbest.baseandroid.R;

import java.text.SimpleDateFormat;
import java.util.Calendar;

/**
 * Created by csz on 2018/10/26.
 */

public class CalendarLayout extends LinearLayout implements View.OnClickListener {
    private ImageView ivPrev;
    private ImageView ivNext;
    private TextView tvDate;
    private CalendarViewPager vp;

    private Calendar mCalendar;
    private CalenderAdapter mCalenderAdapter;

    public CalendarLayout(Context context) {
        this(context, null);
    }

    public CalendarLayout(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public CalendarLayout(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initViews();
        initCalendar();
    }

    private void initViews() {
        LayoutInflater.from(getContext()).inflate(R.layout.layout_calendar, this, true);
        ivPrev = findViewById(R.id.iv_prev);
        ivNext = findViewById(R.id.iv_next);
        tvDate = findViewById(R.id.tv_date);
        vp = findViewById(R.id.vp);

        ivPrev.setOnClickListener(this);
        ivNext.setOnClickListener(this);
        tvDate.setOnClickListener(this);
        mCalendar = Calendar.getInstance();
    }

    @SuppressLint("SimpleDateFormat")
    private void initCalendar() {
        mCalenderAdapter = new CalenderAdapter(vp, (Calendar) mCalendar.clone());
        mCalenderAdapter.setChangeListener(new CalenderAdapter.ChangeListener() {
            @Override
            public void onChange(Calendar calendar) {
                tvDate.setText(new SimpleDateFormat("MMM yyyy").format(calendar.getTime()));
            }
        });
        vp.setAdapter(mCalenderAdapter);
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.iv_prev) {
            prevCalendar();
        } else if (v.getId() == R.id.iv_next) {
            nextCalendar();
        } else if (v.getId() == R.id.tv_date) {
            //TODO 选择年份
        }
    }

    private void nextCalendar() {
        vp.setCurrentItem(vp.getCurrentItem() + 1);
    }

    private void prevCalendar() {
        vp.setCurrentItem(vp.getCurrentItem() - 1);
    }


}
