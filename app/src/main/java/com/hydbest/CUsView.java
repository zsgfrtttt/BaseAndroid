package com.hydbest;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.DashPathEffect;
import android.graphics.Paint;
import android.graphics.Path;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import androidx.annotation.Nullable;

public class CUsView extends View {

    private float firstX, firstY;
    private float secondX, secondY;
    private int centerY, startX, endX;

    private Paint mPaint;
    private Path mPath;

    private boolean mTouch;
    private int actionIndex = -1;

    public CUsView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        mPaint = new Paint();
        mPaint.setAntiAlias(true);
        mPaint.setDither(true);
        mPaint.setColor(Color.RED);
        mPaint.setStyle(Paint.Style.STROKE);
        mPaint.setStrokeWidth(5);

        mPath = new Path();
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        centerY = h / 2;
        startX = w / 4;
        endX = w * 3 / 4;

        firstX = startX;
        firstY = centerY - 200;
        secondX = endX;
        secondY = centerY - 200;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        mPath.reset();
        mPath.moveTo(startX, centerY);
        mPath.cubicTo(firstX, firstY, secondX, secondY, endX, centerY);
        canvas.drawPath(mPath, mPaint);

        mPath.reset();
        mPath.moveTo(startX, centerY);
        mPath.lineTo(firstX, firstY);
        mPath.lineTo(secondX, secondY);
        mPath.lineTo(endX, centerY);
        canvas.drawPath(mPath, mPaint);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {

        switch (event.getAction() & MotionEvent.ACTION_MASK) {
            case MotionEvent.ACTION_POINTER_DOWN: {
                int pointerCount = event.getPointerCount();
                if (pointerCount >= 2) {
                    actionIndex = -1;
                }
                mTouch = true;
                break;
            }
            case MotionEvent.ACTION_POINTER_UP: {
                int pointerCount = event.getPointerCount();
                if (pointerCount > 2) {
                    break;
                }
                actionIndex = event.getActionIndex();
                mTouch = false;
                break;
            }
            case MotionEvent.ACTION_MOVE:
                if (actionIndex == -1) {
                    firstX = event.getX(0);
                    firstY = event.getY(0);
                    if (mTouch) {
                        secondX = event.getX(1);
                        secondY = event.getY(1);
                    }
                } else if (actionIndex == 0) {
                    secondX = event.getX(0);
                    secondY = event.getY(0);
                } else {
                    firstX = event.getX(0);
                    firstY = event.getY(0);
                }
                break;
            case MotionEvent.ACTION_UP:
                actionIndex = -1;
                mTouch = false;
                break;
        }
        invalidate();
        return true;
    }
}
