package com.hydbest.baseandroid.activity.Media;

import android.content.Context;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.ViewConfiguration;

public class GestureDetectorController implements GestureDetector.OnGestureListener {

    private GestureDetector mGestureDetector;
    private OnGestureListener mOnGestureListener;
    private Type mType = Type.SCROLL_NO;
    private int mTouchSlop;
    private int mWidth;

    public GestureDetectorController(Context context, OnGestureListener listener) {
        mWidth = context.getResources().getDisplayMetrics().widthPixels;
        mTouchSlop = new ViewConfiguration().getScaledTouchSlop();
        mGestureDetector = new GestureDetector(context, this);
        mOnGestureListener = listener;
    }

    @Override
    public boolean onDown(MotionEvent e) {
        mType = Type.SCROLL_NO;
        return false;
    }

    @Override
    public void onShowPress(MotionEvent e) {

    }

    @Override
    public boolean onSingleTapUp(MotionEvent e) {
        return false;
    }

    @Override
    public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
        float offsetX = e2.getX() - e1.getX();
        float offsetY = e2.getY() - e1.getY();
        if (mOnGestureListener != null) {
            if (mType != Type.SCROLL_NO) {
                switch (mType) {
                    case SCROLL_LEFT:
                        mOnGestureListener.onLeftVerticalScroll(distanceY);
                        break;
                    case SCROLL_RIGHT:
                        mOnGestureListener.onRightVerticalScroll(distanceY);
                        break;
                    case SCROLL_HORIZONTAL:
                        mOnGestureListener.onHorizontalScroll(distanceX);
                        break;
                }
                return false;
            }
            float x = Math.abs(offsetX);
            float y = Math.abs(offsetY);
            if ((x > mTouchSlop || y > mTouchSlop) && mType == Type.SCROLL_NO) {
                if (x > y) {
                    mOnGestureListener.onHorizontalScroll(offsetX);
                    mType = Type.SCROLL_HORIZONTAL;
                } else {
                    int slop = mWidth / 3;
                    if (e1.getX() < slop) {
                        mOnGestureListener.onLeftVerticalScroll(offsetY);
                        mType = Type.SCROLL_LEFT;
                    } else if (e1.getX() > slop * 2) {
                        mOnGestureListener.onRightVerticalScroll(offsetY);
                        mType = Type.SCROLL_RIGHT;
                    }
                }
            }
        }
        return false;
    }

    @Override
    public void onLongPress(MotionEvent e) {

    }

    @Override
    public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
        return false;
    }

    public boolean onTouchEvent(MotionEvent event) {
        return mGestureDetector.onTouchEvent(event);
    }

    enum Type {
        SCROLL_NO, SCROLL_LEFT, SCROLL_RIGHT, SCROLL_HORIZONTAL
    }

    public interface OnGestureListener {
        void onLeftVerticalScroll(float distance);

        void onRightVerticalScroll(float distance);

        void onHorizontalScroll(float distance);
    }
}
