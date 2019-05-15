package com.hydbest.baseandroid.view.view;

import android.animation.ValueAnimator;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.view.animation.LinearInterpolator;

import com.hydbest.baseandroid.R;

/**
 * Created by csz on 2019/5/15.
 */

public class AliPayLoadingView extends View {

    private static final int DEFAULT_STROKE_WIDTH = 12;
    private static final int DEFAULT_CIRCLE_COLOR = Color.GRAY;
    private static final int DEFAULT_ROTATE_COLOR = Color.BLUE;
    private static final int DEFAULT_ROTATE_DURATION = 4000;

    private Paint mPaint;
    private int mCirlceColor = DEFAULT_CIRCLE_COLOR;
    private int mRotateColor = DEFAULT_ROTATE_COLOR;
    private int mStrokeWidth = DEFAULT_STROKE_WIDTH;
    private int mRotateDuration = DEFAULT_ROTATE_DURATION;

    private Bitmap mBitmap;
    private int cx,cy;
    private ValueAnimator mRotateAnim;

    public AliPayLoadingView(Context context) {
        this(context,null);
    }

    public AliPayLoadingView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs,0);
    }

    public AliPayLoadingView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context,attrs);
        initPaint();
    }

    private void init(Context context, AttributeSet attrs){
        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.AliPayLoadingView);
        mCirlceColor = typedArray.getColor(R.styleable.AliPayLoadingView_circleColor, mCirlceColor);
        mRotateColor = typedArray.getColor(R.styleable.AliPayLoadingView_rotateColor,mRotateColor);
        mStrokeWidth = typedArray.getDimensionPixelSize(R.styleable.AliPayLoadingView_strokeWidth,mStrokeWidth);
        int offset = typedArray.getDimensionPixelOffset(R.styleable.AliPayLoadingView_strokeWidth,mStrokeWidth);
        int dimension = (int) typedArray.getDimension(R.styleable.AliPayLoadingView_strokeWidth,mStrokeWidth);
        mRotateDuration = typedArray.getInteger(R.styleable.AliPayLoadingView_rotateDuration,mRotateDuration);
        Log.i("csz","PixelSize:" + mStrokeWidth + "   offset:"+offset + "   dimension:"+dimension);

        typedArray.recycle();
    }

    private void initPaint() {
        mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mPaint.setDither(true);
        mPaint.setStrokeWidth(mStrokeWidth);
        mPaint.setStyle(Paint.Style.STROKE);
        mPaint.setColor(mCirlceColor);
        mPaint.setStrokeCap(Paint.Cap.ROUND);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        createLoadingBitmap(w - getPaddingLeft() - getPaddingRight()  ,h - getPaddingTop() - getPaddingBottom());
        startRotate();
    }

    private void createLoadingBitmap(int realwidth, int realheight) {
        float radius = Math.min(realwidth ,realheight)/2 - mStrokeWidth / 2;
        float length = Math.min(realwidth ,realheight);
        cx = (int) (getPaddingLeft() + (realwidth - length)/2 + length/2);
        cy = (int) (getPaddingTop() + (realheight - length)/2 + length/2);
        Bitmap bitmap = Bitmap.createBitmap(getWidth(),getHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        canvas.drawCircle(cx,cy,radius,mPaint);

        mPaint.setColor(mRotateColor);
        canvas.drawArc(new RectF(cx-radius,cy-radius,cx+radius,cy+radius),
                0,40,false,mPaint);

        this.mBitmap = bitmap;
    }

    private void startRotate() {
        mRotateAnim = ValueAnimator.ofFloat(0,360.f).setDuration(mRotateDuration);
        mRotateAnim.setInterpolator(new LinearInterpolator());
        mRotateAnim.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                Float val = (Float) animation.getAnimatedValue();
                setRotation(val);
            }
        });
        mRotateAnim.setRepeatCount(ValueAnimator.INFINITE);
        mRotateAnim.start();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        canvas.drawBitmap(mBitmap,0,0,null);
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        mBitmap.recycle();
        mRotateAnim.cancel();
    }
}
