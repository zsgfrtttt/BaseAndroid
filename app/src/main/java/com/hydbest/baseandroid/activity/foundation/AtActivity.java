package com.hydbest.baseandroid.activity.foundation;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.EditText;

import com.hydbest.baseandroid.R;
import com.hydbest.baseandroid.activity.integrate.span.DataBindingSpan;
import com.hydbest.baseandroid.activity.integrate.span.impl.SpannableData;
import com.hydbest.baseandroid.helper.KeyCodeDeleteHelper;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by csz on 2018/12/20.
 */

public class AtActivity extends AppCompatActivity{

    @BindView(R.id.et)
    EditText mEditText;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_at);
        ButterKnife.bind(this);
        mEditText.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (keyCode == KeyEvent.KEYCODE_DEL && event.getAction() == KeyEvent.ACTION_DOWN) {
                    return KeyCodeDeleteHelper.onDelDown(mEditText.getText());
                }
                return false;
            }
        });
        //取数据
        DataBindingSpan[] spans = mEditText.getText().getSpans(0, mEditText.getText().length(), DataBindingSpan.class);
        if (spans!= null){
            for (DataBindingSpan span : spans) {
                Log.i("csz","spans:"+span.bindingData());
            }

        }

    }

    public void add(View view){
        SpannableData span =  new SpannableData("caishuzhan");
        mEditText.getText().append(span.spannedText());
    }

    public void less(View view){

    }
}
