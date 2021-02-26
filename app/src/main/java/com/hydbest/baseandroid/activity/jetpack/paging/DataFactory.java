package com.hydbest.baseandroid.activity.jetpack.paging;

import android.util.Log;

import com.hydbest.baseandroid.activity.jetpack.room.Student;

import java.util.ArrayList;
import java.util.List;

public class DataFactory {

    public static List<Student> students = new ArrayList<>();

    static {
        for (int i = 0; i < 100; i++) {
            students.add(new Student("name " + i, i));
        }
    }

    public static List<Student> fetchData(int start, int count) {
        if (start > students.size() - 1) return new ArrayList<>();
        List<Student> result = new ArrayList<>();
        for (int i = start; i < students.size(); i++, count--) {
            if (count <= 0) break;
            result.add(students.get(i));
        }
        return result;
    }
}
