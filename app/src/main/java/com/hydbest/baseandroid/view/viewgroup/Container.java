package com.hydbest.baseandroid.view.viewgroup;

import android.content.Context;
import android.support.v4.view.MotionEventCompat;
import android.support.v4.widget.ViewDragHelper;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.RelativeLayout;

public class Container extends RelativeLayout {

    private ViewDragHelper mHelper;

    public Container(Context context) {
        this(context,null);
    }

    public Container(Context context, AttributeSet attrs) {
        this(context, attrs,0);
    }

    public Container(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public void setHelper(ViewDragHelper helper){
        this.mHelper = helper;
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
       // boolean shouldInterceptTouchEvent = mHelper.shouldInterceptTouchEvent(ev);
        if (ev.getAction() == MotionEvent.ACTION_UP) mHelper.cancel();
        return false;
       // return false
    }

    @Override
    public void computeScroll() {
        if (mHelper.continueSettling(true)) {
            invalidate();
        }
    }
}
