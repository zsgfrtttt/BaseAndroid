package com.hydbest.baseandroid.view.viewgroup.calendar;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
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

public class DateAdapter extends ArrayAdapter<Date>{

    private Calendar mCalendar;
    private List<Date> mList;

    public DateAdapter(@NonNull Context context, Calendar calendar) {
        super(context, R.layout.item_date);
        this.mCalendar = calendar;
        init();
    }

    private void init() {
        Calendar calendar = (Calendar) mCalendar.clone();
        //重置到当月第一天  calendar.set(Calendar.DAY_OF_MONTH,1);
        calendar.add(Calendar.DAY_OF_MONTH, -calendar.get(Calendar.DAY_OF_MONTH) + 1);
        //当期月份
        int currentMonth = calendar.get(Calendar.MONTH);
        //当月第一天是周的第几天
        int dayOfWeek = calendar.get(Calendar.DAY_OF_WEEK);
        calendar.add(Calendar.DATE, -(dayOfWeek - 1));

        mList = new ArrayList<>();
        for (int i = 0; i < 42; i++) {
            //避免添加多一行
            if (calendar.get(Calendar.MONTH) != currentMonth && i % 7 == 0 && i > 7)
                break;
            mList.add(calendar.getTime());
            calendar.add(Calendar.DATE, 1);
        }
    }

    @Override
    public int getCount() {
        return mList == null ? 0 : mList.size();
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        Date item = mList.get(position);
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.item_date, parent, false);
        }
        TextView tv = convertView.findViewById(R.id.tv);
        tv.setText(item.getDate() + "");
        SimpleDateFormat format = new SimpleDateFormat("MMM");
        if (format.format(item).equals(format.format(mCalendar.getTime()))) {
            tv.setTextColor(parent.getResources().getColor(R.color.text));
        } else {
            tv.setTextColor(parent.getResources().getColor(R.color.light_text));
        }
        Date date = Calendar.getInstance().getTime();
        if (item.getDate() == date.getDate() && item.getMonth() == date.getMonth() && item.getYear() == date.getYear()){
            tv.setBackgroundResource(R.drawable.bg_stroke_circle);
        }else {
            tv.setBackgroundResource(R.drawable.bg_white_default);
        }
        return convertView;
    }
}
