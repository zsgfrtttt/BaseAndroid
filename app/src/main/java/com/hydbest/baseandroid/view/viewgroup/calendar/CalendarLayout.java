package com.hydbest.baseandroid.view.viewgroup.calendar;

import android.annotation.SuppressLint;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.hydbest.baseandroid.R;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * Created by csz on 2018/10/26.
 */

public class CalendarLayout extends LinearLayout implements View.OnClickListener {
    private ImageView ivPrev;
    private ImageView ivNext;
    private TextView tvDate;
    private GridView gvDate;

    private Calendar mCalendar;

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
        gvDate = findViewById(R.id.gv);

        ivPrev.setOnClickListener(this);
        ivNext.setOnClickListener(this);
        tvDate.setOnClickListener(this);
        mCalendar = Calendar.getInstance();
    }

    @SuppressLint("SimpleDateFormat")
    private void initCalendar() {
        tvDate.setText(new SimpleDateFormat("MMM yyyy").format(mCalendar.getTime()));

        Calendar calendar = (Calendar) mCalendar.clone();
        //重置到当月第一天
        calendar.add(Calendar.DAY_OF_MONTH, -calendar.get(Calendar.DAY_OF_MONTH) + 1);
        //当期月份
        int currentMonth = calendar.get(Calendar.MONTH);
        //当月第一天是周的第几天
        int dayOfWeek = calendar.get(Calendar.DAY_OF_WEEK);
        calendar.add(Calendar.DATE, -(dayOfWeek - 1));

        List<Date> cells = new ArrayList<>();
        for (int i = 0; i < 42; i++) {
            //避免添加多一行
            if (calendar.get(Calendar.MONTH) != currentMonth && i % 7 == 0 && i > 7)
                break;
            cells.add(calendar.getTime());
            calendar.add(Calendar.DATE, 1);
        }

        gvDate.setAdapter(new DateAdapter(getContext(), cells));
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
        mCalendar.set(Calendar.MONTH, mCalendar.get(Calendar.MONTH) + 1);
        initCalendar();
    }

    private void prevCalendar() {
        mCalendar.set(Calendar.MONTH, mCalendar.get(Calendar.MONTH) - 1);
        initCalendar();
    }

    private class DateAdapter extends ArrayAdapter<Date> {

        private List<Date> mList;

        public DateAdapter(@NonNull Context context, List<Date> list) {
            super(context, R.layout.item_date);
            this.mList = list;
        }

        @Override
        public int getCount() {
            return mList == null ? 0 : mList.size();
        }

        @NonNull
        @Override
        public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
            if (convertView == null) {
                convertView = LayoutInflater.from(getContext()).inflate(R.layout.item_date, parent, false);
            }
            ((TextView) convertView.findViewById(R.id.tv)).setText(mList.get(position).getDate() + "");
            return convertView;
        }
    }
}
