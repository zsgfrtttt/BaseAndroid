package com.hydbest.baseandroid.view.viewgroup.keyboard;

import android.content.Context;
import android.inputmethodservice.KeyboardView;
import android.util.AttributeSet;

/**
 * Created by csz on 2019/4/28.
 */

public class SafeKeyboardView extends KeyboardView{
    public SafeKeyboardView(Context context, AttributeSet attrs) {
        this(context, attrs,0);
    }

    public SafeKeyboardView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

}
