package com.hydbest.baseandroid.view.view;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.LinearGradient;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.RectF;
import android.graphics.Shader;
import android.graphics.Xfermode;
import android.os.Handler;
import android.os.Message;
import androidx.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;

public class WaveView extends View {
    private static final int SAMPLE_SIZE = 128;
    private final Paint mPaint = new Paint();
    private final Path mFirstPath = new Path();
    private final Path mSecondPath = new Path();
    private final Path mCenterPath = new Path();
    private final RectF mRectF = new RectF();

    private float[] mSampleX;
    private float[] mX;
    private int mWidth;
    private int mHeight;
    private int mCenterHeight;
    private int mAmplitude;
    private float mOffset = 0.5F;

    private final Xfermode mXFermode = new PorterDuffXfermode(PorterDuff.Mode.SRC_IN);
    private final int mBackGroupColor = Color.rgb(24, 33, 41);
    private final int mCenterPathColor = Color.argb(64, 255, 255, 255);
    long mStartTime = System.currentTimeMillis();

    @SuppressLint("HandlerLeak")
    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            invalidate();
            mHandler.sendEmptyMessageDelayed(0, 16);
        }
    };

    public WaveView(Context context) {
        this(context, null);
    }

    public WaveView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public WaveView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        mPaint.setDither(true);
        mPaint.setAntiAlias(true);
    }

    @SuppressLint("DrawAllocation")
    @Override
    protected void onDraw(Canvas canvas) {
         super.onDraw(canvas);
        if (mSampleX == null) {
            mWidth = canvas.getWidth();
            mHeight = canvas.getHeight();

            mCenterHeight = mHeight >> 1;
            mAmplitude = mWidth >> 3;
            mSampleX = new float[SAMPLE_SIZE + 1];
            mX = new float[SAMPLE_SIZE + 1];

            for (int i = 0; i <= SAMPLE_SIZE; i++) {
                float mSlop = mWidth / (float) SAMPLE_SIZE;
                mSampleX[i] = mSlop * i;
                mX[i] = mSampleX[i] / mWidth * 4 - 2;
            }
        }

        canvas.drawColor(mBackGroupColor);
        mFirstPath.rewind();
        mSecondPath.rewind();
        mCenterPath.rewind();

        mFirstPath.moveTo(0, mCenterHeight);
        mSecondPath.moveTo(0, mCenterHeight);
        mCenterPath.moveTo(0, mCenterHeight);

        for (int i = 0; i <= SAMPLE_SIZE; i++) {
            float x = mSampleX[i];
            mOffset = (System.currentTimeMillis() - mStartTime)/500F;
            float y = (float) (mAmplitude * calculateValue(mX[i], mOffset));
            mFirstPath.lineTo(x, y + mCenterHeight);
            mSecondPath.lineTo(x, -y + mCenterHeight);
            mCenterPath.lineTo(x, (float) (0.5 * y + mCenterHeight));
        }

        mFirstPath.lineTo(mWidth, mCenterHeight);
        mSecondPath.lineTo(mWidth, mCenterHeight);
        mCenterPath.lineTo(mWidth, mCenterHeight);

        int layer = canvas.saveLayer(0, 0, mWidth, mHeight, null, Canvas.ALL_SAVE_FLAG);
        mPaint.setColor(Color.WHITE);
        mPaint.setStyle(Paint.Style.FILL);
        mPaint.setStrokeWidth(1);
        canvas.drawPath(mFirstPath, mPaint);
        canvas.drawPath(mSecondPath, mPaint);

        mPaint.setColor(Color.GREEN);
        mPaint.setXfermode(mXFermode);
        mRectF.set(0, mCenterHeight - mAmplitude, mWidth, mCenterHeight + mAmplitude);
        mPaint.setShader(new LinearGradient(0F, mCenterHeight + mAmplitude * 1.F, mWidth * 1.F, mCenterHeight - mAmplitude * 1.F, Color.BLUE, Color.GREEN, Shader.TileMode.CLAMP));
        canvas.drawRect(mRectF, mPaint);
        mPaint.setXfermode(null);
        mPaint.setShader(null);
        //canvas.restoreToCount(layer);

        mPaint.setStrokeWidth(3);
        mPaint.setStyle(Paint.Style.STROKE);
        mPaint.setColor(Color.BLUE);
        canvas.drawPath(mFirstPath, mPaint);
        mPaint.setColor(Color.GREEN);
        canvas.drawPath(mSecondPath, mPaint);
        mPaint.setColor(mCenterPathColor);
        canvas.drawPath(mCenterPath, mPaint);
        mHandler.sendEmptyMessageDelayed(0,16);
    }

    //0.5\left(\frac{4}{4+x^4}\right)^{2.5}\sin\left(0.75\pi x-0.5\pi\right)   0.5;
    private double calculateValue(float x, float offset) {
        double sin = Math.sin(0.75 * Math.PI * x - offset * Math.PI);
        double discrete = Math.pow(4 / (4 + Math.pow(x, 4)), 2.5);
        return (sin * discrete);
    }
}
