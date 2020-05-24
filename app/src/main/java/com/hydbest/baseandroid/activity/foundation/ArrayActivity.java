package com.hydbest.baseandroid.activity.foundation;

import android.os.Build;
import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.collection.LongSparseArray;
import androidx.appcompat.app.AppCompatActivity;
import android.util.Log;
import android.util.SparseArray;
import android.util.SparseLongArray;
import android.view.View;

import com.hydbest.baseandroid.R;

/**
 * Created by csz on 2018/9/26.
 */

public class ArrayActivity extends AppCompatActivity {

    private SparseArray mSparseArray;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_array);
    }

    public void sparseArray(View view) {
        /**
         * key会从小到大进行排序
         */
        mSparseArray = new SparseArray();
        mSparseArray.put(5, "5");
        mSparseArray.put(6, "6");
        mSparseArray.put(8, "8");
        mSparseArray.put(3, "3");
        mSparseArray.put(2, "2");

        int index = mSparseArray.indexOfKey(8);
        Log.i("csz", "index:" + index);

        int val = mSparseArray.indexOfValue("6");
        Log.i("csz", "indexOfval:" + val);

        mSparseArray.removeAt(0);

        mSparseArray.setValueAt(0, "0");

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            mSparseArray.removeAtRange(0, 2);
        }

        String val1 = (String) mSparseArray.valueAt(1);
        Log.i("csz", "val1:" + val1+"   toString:"+val1.toString());
    }

    public void arrayMap(View view) {
        /**
         * key为Long类型,避免溢出
         */
        LongSparseArray  longSparseArray;

        /**
         * val为long类型
         */
        SparseLongArray sparseLongArray;
    }
}
