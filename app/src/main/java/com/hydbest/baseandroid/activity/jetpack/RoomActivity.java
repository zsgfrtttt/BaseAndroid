package com.hydbest.baseandroid.activity.jetpack;

import android.app.Application;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.hydbest.baseandroid.R;
import com.hydbest.baseandroid.activity.jetpack.room.Student;
import com.hydbest.baseandroid.activity.jetpack.room.StudentDao;
import com.hydbest.baseandroid.activity.jetpack.room.StudentDatabase;

import java.util.List;
import java.util.Random;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.SavedStateHandle;
import androidx.lifecycle.SavedStateViewModelFactory;
import androidx.lifecycle.ViewModelProvider;

public class RoomActivity extends AppCompatActivity {

    private TextView tv;

    StudentDatabase stuDb;
    StudentDao studentDao;

    private Executor threadPool = Executors.newFixedThreadPool(1);
    private RoomViewModel roomViewModel;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_room);
        tv = findViewById(R.id.tv);
        stuDb = StudentDatabase.getInstance(this);
        studentDao = stuDb.getStudentDao();
        studentDao.queryAll1();

        observe();

        roomViewModel = new ViewModelProvider(this, new SavedStateViewModelFactory(getApplication(), this)).get(RoomViewModel.class);
        roomViewModel.getStudentList().observe(this, new Observer<List<Student>>() {
            @Override
            public void onChanged(List<Student> students) {
                Log.i("csz", Thread.currentThread().getName() + "    students changed    " + students);
            }
        });
    }

    public void insert(View view) {
        threadPool.execute(new Runnable() {
            @Override
            public void run() {
                Student stu = new Student("caishuzhan", 20);
                long id = studentDao.insertOne(stu);
            }
        });
    }

    public void insertAll(View view) {
        threadPool.execute(new Runnable() {
            @Override
            public void run() {
                Student stu = new Student("csz", 30);
                Student stu1 = new Student("csz111", 40);
                studentDao.insertAll(stu, stu1);
            }
        });
    }

    public void deltete(View view) {
        threadPool.execute(new Runnable() {
            @Override
            public void run() {
                Student stu = new Student(1, null, 0);
                studentDao.deleteOne(stu);
            }
        });
    }

    public void deleteAll(View view) {
        threadPool.execute(new Runnable() {
            @Override
            public void run() {
                Student stu = new Student(2, null, 0);
                Student stu1 = new Student(3, null, 0);
                studentDao.deleteAll(stu, stu1);
            }
        });
    }

    public void deleteAllTrue(View view) {
        threadPool.execute(new Runnable() {
            @Override
            public void run() {
                studentDao.deleteAll();
            }
        });
    }

    public void updateOne(View view) {
        threadPool.execute(new Runnable() {
            @Override
            public void run() {
                Student stu = new Student(23, "ccsszz", new Random().nextInt(10000));
                studentDao.updateOne(stu);
            }
        });
    }

    public void updateAll(View view) {
        threadPool.execute(new Runnable() {
            @Override
            public void run() {
                Student stu = new Student(12, "caishuzhan", 90);
                Student stu1 = new Student(13, "csz", 1000);
                studentDao.updateAll(stu, stu1);
            }
        });
    }

    public void findByIds(View view) {
        threadPool.execute(new Runnable() {
            @Override
            public void run() {
                List<Student> list = studentDao.queryAllByIds(new int[]{11, 12, 13, 14});
            }
        });
    }

    public void findByName(View view) {
        threadPool.execute(new Runnable() {
            @Override
            public void run() {
                Student stu = studentDao.findByName("csz");
            }
        });
    }

    private void observe() {
        studentDao.queryAll().observe(this, new Observer<List<Student>>() {
            @Override
            public void onChanged(List<Student> students) {
                tv.setText("");
                for (Student student : students) {
                    tv.append(student.toString() + "\n");
                }
            }
        });
    }

    /**
     * 通过viewmodel隔离数据库的操作
     */
    public static class RoomViewModel extends AndroidViewModel {

        private static final String KEY = "key1";
        private SavedStateHandle handle;

        private StudentDao studentDao;
        private LiveData<List<Student>> liveStudentList;

        public RoomViewModel(@NonNull Application application, SavedStateHandle handle) {
            super(application);
            this.handle = handle;
            this.studentDao = StudentDatabase.getInstance(application).getStudentDao();
            this.liveStudentList = this.studentDao.queryAll();
        }

        public LiveData<List<Student>> getStudentList() {
            return liveStudentList;
        }

    }

}
