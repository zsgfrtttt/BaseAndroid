package com.hydbest.baseandroid.activity.jetpack.paging;

import android.annotation.SuppressLint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import com.hydbest.baseandroid.activity.jetpack.room.Student;
import androidx.annotation.NonNull;
import androidx.paging.PagedListAdapter;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.RecyclerView;

public class CommonAdapter extends PagedListAdapter<Student, CommonAdapter.MyHolder> {

    public CommonAdapter() {
        super(new DiffUtil.ItemCallback<Student>() {
            @Override
            public boolean areItemsTheSame(@NonNull Student oldItem, @NonNull Student newItem) {
                return oldItem.getId() == newItem.getId();
            }

            @Override
            public boolean areContentsTheSame(@NonNull Student oldItem, @NonNull Student newItem) {
                return oldItem.getName().equals(newItem.getName()) && oldItem.getAge() == newItem.getAge() && oldItem.isBoy() == newItem.isBoy();
            }
        });
    }

    @NonNull
    @Override
    public CommonAdapter.MyHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(android.R.layout.simple_list_item_1, parent, false);
        return new CommonAdapter.MyHolder(view);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull CommonAdapter.MyHolder holder, int position) {
        Student item = getItem(position);
        if (item == null) {
            holder.tv.setText("加载中");
        } else {
            holder.tv.setText(item.getName() + "      " + item.getAge());
        }
    }

    class MyHolder extends RecyclerView.ViewHolder {
        private TextView tv;

        public MyHolder(@NonNull View itemView) {
            super(itemView);
            tv = itemView.findViewById(android.R.id.text1);
        }
    }
}
