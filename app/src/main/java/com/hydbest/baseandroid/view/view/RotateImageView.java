package com.hydbest.baseandroid.view.view;

import android.content.Context;
import android.graphics.Matrix;
import android.support.v7.widget.AppCompatImageView;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.ImageView;

/**
 * Math.toDegrees (Math.atan (1))
 */
public class RotateImageView extends AppCompatImageView {

    private int mPointCount;
    private float mDegrees = -1000;
    private Matrix mMatrix;
    private boolean canRotate;

    public RotateImageView(Context context, AttributeSet attrs) {
        super(context, attrs);
        setScaleType(ImageView.ScaleType.MATRIX);
        mMatrix = new Matrix();
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        int pointCount = event.getPointerCount();
        float x1 = 0, y1 = 0, x2 = 0, y2 = 0;

        if (pointCount == mPointCount && pointCount >= 2) {
            x1 = event.getX(0);
            x2 = event.getX(1);
            y1 = event.getY(0);
            y2 = event.getY(1);
            float degrees = rotation(event);
            if (canRotate){
                mMatrix.postRotate(degrees - mDegrees,( x1-x2)/2 + x2, (y1-y2) / 2  + y2);
                setImageMatrix(mMatrix);
            } else{
                canRotate = true;
            }
            mDegrees = degrees;
        } else {
            mPointCount = pointCount;
            mDegrees = -1000;
            canRotate = false;
        }

        return true;
    }


    // 取旋转角度
    private float rotation(MotionEvent event) {
        double delta_x = (event.getX(0) - event.getX(1));
        double delta_y = (event.getY(0) - event.getY(1));
        double radians = Math.atan2(delta_y, delta_x);
        return (float) Math.toDegrees(radians);
    }


}
