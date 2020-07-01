package com.hydbest.baseandroid.activity.jetpack;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.hydbest.baseandroid.R;
import com.hydbest.baseandroid.activity.jetpack.room.Student;
import com.hydbest.baseandroid.activity.jetpack.room.StudentDao;
import com.hydbest.baseandroid.activity.jetpack.room.StudentDatabase;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Observer;
import androidx.paging.DataSource;
import androidx.paging.LivePagedListBuilder;
import androidx.paging.PageKeyedDataSource;
import androidx.paging.PagedList;
import androidx.paging.PagedListAdapter;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class PagingActivity extends AppCompatActivity {

    StudentDao dao;
    PageAdapter adapter;
    RecyclerView rv;

    LiveData data;
    Executor executor = Executors.newSingleThreadExecutor();

 /*   private MutableLiveData<Integer> mu = new MutableLiveData<>(0);
    private LiveData<String> text = Transformations.switchMap(mu, new Function<Integer, LiveData<String>>() {
        @Override
        public LiveData<String> apply(Integer input) {
            Log.i("csz","input  " + input);
            MutableLiveData<String> mutableLiveData = new MutableLiveData();
            mutableLiveData.setValue("input  " + input);
            return mutableLiveData;
        }
    });
*/
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_paging);
        dao = StudentDatabase.getInstance(this).getStudentDao();

        adapter = new PageAdapter();

        /* data = new LivePagedListBuilder<>(dao.queryAllData(),2).build(); */
        data = new LivePagedListBuilder(new PageKeyDataSourceFactory(),1).build();

        data.observe(this, new Observer<PagedList<Student>>() {
            @Override
            public void onChanged(PagedList<Student> students) {
                adapter.submitList(students);
            }
        });


        rv = findViewById(R.id.rv);
        rv.setLayoutManager(new LinearLayoutManager(this));
        rv.setAdapter(adapter);

    }

    public void add(View view) {
        final List<Student> list = new ArrayList<>();
        for (int i = 0; i < 100; i++) {
            list.add(new Student("cai " + i,i));
        }
        executor.execute(new Runnable() {
            @Override
            public void run() {
                dao.insertAll(list.toArray(new Student[0]));
            }
        });
    }

    public void reset(View view) {
       executor.execute(new Runnable() {
            @Override
            public void run() {
                dao.deleteAll();
            }
        });
    }


    class PageAdapter extends PagedListAdapter<Student, PageAdapter.MyHolder>{


        protected PageAdapter() {
            super(new DiffUtil.ItemCallback<Student>() {
                @Override
                public boolean areItemsTheSame(@NonNull Student oldItem, @NonNull Student newItem) {
                    return oldItem.getId() == newItem.getId();
                }

                @Override
                public boolean areContentsTheSame(@NonNull Student oldItem, @NonNull Student newItem) {
                    return oldItem.getName().equals(newItem.getName())
                            && oldItem.getAge() == newItem.getAge()
                            && oldItem.isBoy() == newItem.isBoy();
                }
            });
        }

        @NonNull
        @Override
        public MyHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(android.R.layout.simple_list_item_1,parent,false);
            return new MyHolder(view);
        }

        @SuppressLint("SetTextI18n")
        @Override
        public void onBindViewHolder(@NonNull MyHolder holder, int position) {
            Student item = getItem(position);
            if (item == null){
                holder.tv.setText("加载中");
            }else{
                holder.tv.setText(item.getName() +"      " + item.getAge());
            }
        }

        class MyHolder extends RecyclerView.ViewHolder{
            private TextView tv;

            public MyHolder(@NonNull View itemView) {
                super(itemView);
                tv = itemView.findViewById(android.R.id.text1);
            }
        }
    }


    class PageKeyDataSource extends PageKeyedDataSource<Integer,Student>{

        private int page = 1;
        private int pageSize = 10;

        @Override
        public void loadInitial(@NonNull LoadInitialParams<Integer> params, @NonNull LoadInitialCallback<Integer, Student> callback) {
            page = 1;
            callback.onResult( dao.findOnePage(page, pageSize),null,2);
        }

        @Override
        public void loadAfter(@NonNull LoadParams<Integer> params, @NonNull LoadCallback<Integer, Student> callback) {
            try {
                Thread.sleep(2000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            callback.onResult(dao.findOnePage(params.key, pageSize),params.key +1);
        }

        @Override
        public void loadBefore(@NonNull LoadParams<Integer> params, @NonNull LoadCallback<Integer, Student> callback) {

        }
    }

    class PageKeyDataSourceFactory extends DataSource.Factory<Integer,Student>{

        @NonNull
        @Override
        public DataSource<Integer, Student> create() {
            return new PageKeyDataSource();
        }
    }
}
