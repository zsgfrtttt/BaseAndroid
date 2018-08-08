package com.hydbest.baseandroid.view.view;

import android.content.Context;
import android.support.annotation.Nullable;
import android.support.v4.view.MotionEventCompat;
import android.support.v4.view.ViewCompat;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewConfiguration;

/**
 * Created by csz on 2018/8/8.
 */

public class MultiTouchView extends View {
    private float downX;
    private float downY;
    private int downPointerId;
    private int downPointerId2;

    private double mStartLen;

    public MultiTouchView(Context context) {
        this(context, null);
    }

    public MultiTouchView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public MultiTouchView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        int action = event.getActionMasked();
        switch (action) {
            case MotionEvent.ACTION_DOWN:
                Log.i("csz", "down");
                downX = event.getX();
                downY = event.getY();
                int index = event.getActionIndex();
                downPointerId = event.getPointerId(index);
                return true;
            case MotionEvent.ACTION_POINTER_DOWN:
                Log.i("csz", "pointer_down");
                int index2 = event.getActionIndex();
                Log.i("csz","jjj"+event.getPointerCount());
                if (event.getPointerCount() > 2) {
                    break;
                }else if (downPointerId == -1){
                    downPointerId = event.getPointerId(index2);
                }else {
                    downPointerId2 = event.getPointerId(index2);
                }

                float pointX1 = event.getX(downPointerId);
                float pointX2 = event.getX(downPointerId2);
                float pointY1 = event.getY(downPointerId);
                float pointY2 = event.getY(downPointerId2);

                mStartLen = Math.sqrt(Math.pow(pointX1 - pointX2, 2) + Math.pow( pointY1 - pointY2 ,2));
                break;
            case MotionEvent.ACTION_UP:
                Log.i("csz", "up");
                break;
            case MotionEvent.ACTION_POINTER_UP:
                Log.i("csz", "pointer_up   "+event.getPointerCount());
                if (event.getPointerCount()==2) {
                    int indexUp = event.getActionIndex();
                    int upPointerId = event.getPointerId(indexUp);
                    if (upPointerId == downPointerId) {
                        downPointerId = -1;
                        Log.i("csz", "kkkkkkkkkkkkkkkkkkkkkk");
                    }
                }else{
                    downPointerId = event.getPointerId(0);
                    downPointerId2 =event.getPointerId(1);
                }
                break;
            case MotionEvent.ACTION_MOVE:
                int downIndex = event.findPointerIndex(downPointerId);
                int pointerCount = event.getPointerCount();
                if (pointerCount >= 2) {
                    float movePointX1 = event.getX(downPointerId);
                    float movePointX2 = event.getX(downPointerId2);
                    float movePointY1 = event.getY(downPointerId);
                    float movePointY2 = event.getY(downPointerId2);

                    double moveLen = Math.sqrt(Math.pow(movePointX1 - movePointX2, 2) + Math.pow( movePointY1 - movePointY2 ,2));
                    if (Math.abs(moveLen - mStartLen) > ViewConfiguration.getTouchSlop()){
                        setScaleX((float) (moveLen/mStartLen));
                        setScaleY((float) (moveLen/mStartLen));
                    }
                }
                /*if (downIndex != -1) {
                    //如果之前所标记的触点被抬起,怎最先按下的手指会被设置成该触点
                    Log.i("csz", "moveX:" + event.getX(downIndex) + "   " + downIndex);
                }*/
                break;
        }
        return super.onTouchEvent(event);
    }
}
