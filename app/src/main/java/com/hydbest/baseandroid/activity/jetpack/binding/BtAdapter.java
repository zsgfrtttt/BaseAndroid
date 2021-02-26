package com.hydbest.baseandroid.activity.jetpack.binding;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.hydbest.baseandroid.BR;
import com.hydbest.baseandroid.R;
import com.hydbest.baseandroid.activity.jetpack.room.Student;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.databinding.ViewDataBinding;
import androidx.recyclerview.widget.RecyclerView;

public class BtAdapter extends RecyclerView.Adapter<BtAdapter.Holder> {

    private List<Student>  list;

    public BtAdapter(List<Student> list) {
        this.list = list;
    }

    @NonNull
    @Override
    public Holder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ViewDataBinding binding = DataBindingUtil.inflate((LayoutInflater) parent.getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE), R.layout.item_data_binding, parent, false);
        return new Holder(binding);
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    @Override
    public void onBindViewHolder(@NonNull Holder holder, int position) {
        holder.binding.setVariable(BR.student,list.get(position));
    }

    public class Holder extends RecyclerView.ViewHolder{

        ViewDataBinding binding;

        public Holder(@NonNull ViewDataBinding itemView) {
            super(itemView.getRoot());
            binding = itemView;
        }
    }
}
