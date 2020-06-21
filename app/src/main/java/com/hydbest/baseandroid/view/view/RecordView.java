package com.hydbest.baseandroid.view.view;

import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.View;
import android.view.animation.LinearInterpolator;

import com.hydbest.baseandroid.R;

import androidx.annotation.Nullable;

public class RecordView extends View {

    private Paint mPaint;
    private int mPadding = 16;
    private int mStrokeWidth = 8;
    private int mWidth;
    private int mInnerRadio;
    private float mProgress;
    private Bitmap mBitmap;

    private ValueAnimator valueAnimator;

    public RecordView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int width = MeasureSpec.getSize(widthMeasureSpec);
        int height = MeasureSpec.getSize(heightMeasureSpec);
        mWidth = Math.min(width, height);
        mInnerRadio = mWidth / 2 - mPadding;
        setMeasuredDimension(mWidth, mWidth);
    }

    private void init() {
        mBitmap = BitmapFactory.decodeResource(getResources(), R.drawable.icon_del);
        mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mPaint.setDither(true);
        mPaint.setStyle(Paint.Style.FILL);
        mPaint.setColor(Color.LTGRAY);

        valueAnimator = ValueAnimator.ofFloat(mProgress,1.f).setDuration(mProgress > 0.5 ? 16000 : (mProgress > 0.25 ? 8000 : 4000));
        valueAnimator.setInterpolator(new LinearInterpolator());
        valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                mProgress = (float) animation.getAnimatedValue();
                invalidate();
            }
        });
        valueAnimator.start();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        drawInner(canvas);
        drawCircle(canvas);
        drawBitmap(canvas);
    }

    private void drawInner(Canvas canvas) {
        mPaint.setStyle(Paint.Style.FILL);
        canvas.drawCircle(mWidth / 2, mWidth / 2, mInnerRadio, mPaint);
    }

    private void drawCircle(Canvas canvas) {
        mPaint.setStyle(Paint.Style.STROKE);
        mPaint.setStrokeCap(Paint.Cap.ROUND);
        mPaint.setStrokeWidth(mStrokeWidth);
        canvas.drawArc(new RectF(mStrokeWidth / 2, mStrokeWidth / 2, mWidth - mStrokeWidth / 2, mWidth - mStrokeWidth / 2), -90, mProgress * 360, false, mPaint);
    }

    private void drawBitmap(Canvas canvas) {
        int width = mBitmap.getWidth();
        int height = mBitmap.getHeight();
        float pixov = height * 1.f /width;
        canvas.drawBitmap(mBitmap, null,new RectF(mWidth / 4 , (mWidth - mWidth/2  * pixov) /2,mWidth * 3 /4, mWidth - (mWidth - mWidth/2  * pixov) /2), mPaint);
    }

    public void setProgress(float progress) {
        this.mProgress = progress;
        invalidate();
    }

    public void startAnim(){
        if (!valueAnimator.isRunning()) {
            valueAnimator.start();
        }
    }

}
