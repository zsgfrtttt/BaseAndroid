package com.hydbest.baseandroid.activity.cus_viewgroup;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;

import com.hydbest.baseandroid.R;
import com.hydbest.baseandroid.view.viewgroup.keyboard.SafeKeyboard;

/**
 * Created by csz on 2019/4/26.
 */

public class KeyboardActivity extends AppCompatActivity{
    private SafeKeyboard safeKeyboard;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_keyboard);

        EditText safeEdit = findViewById(R.id.safeEditText);
        LinearLayout keyboardContainer = findViewById(R.id.keyboardViewPlace);
        @SuppressLint("InflateParams") View view = LayoutInflater.from(this).inflate(R.layout.layout_keyboard_containor, null);
        safeKeyboard = new SafeKeyboard(getApplicationContext(), keyboardContainer, safeEdit,
                R.layout.layout_keyboard_containor, view.findViewById(R.id.safeKeyboardLetter).getId());
        safeKeyboard.setDelDrawable(this.getResources().getDrawable(R.drawable.icon_del));
        safeKeyboard.setLowDrawable(this.getResources().getDrawable(R.drawable.icon_capital_default));
        safeKeyboard.setUpDrawable(this.getResources().getDrawable(R.drawable.icon_capital_selected));
    }

    // 当点击返回键时, 如果软键盘正在显示, 则隐藏软键盘并是此次返回无效
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0) {
            if (safeKeyboard.isShow()) {
                safeKeyboard.hideKeyboard();
                return false;
            }
            return super.onKeyDown(keyCode, event);
        }
        return super.onKeyDown(keyCode, event);
    }
}
