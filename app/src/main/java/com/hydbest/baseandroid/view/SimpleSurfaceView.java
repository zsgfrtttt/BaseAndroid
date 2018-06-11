package com.hydbest.baseandroid.view;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.widget.FrameLayout;

import com.hydbest.baseandroid.R;

/**
 * Created by csz on 2018/6/11.
 */

public class SimpleSurfaceView extends FrameLayout {

    private SurfaceView mSurfaceView;
    private SurfaceHolder mSurfaceHolder;

    private Paint mPaint;

    public SimpleSurfaceView(@NonNull Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
        init();
    }

    public SimpleSurfaceView(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        View.inflate(getContext(), R.layout.view_simple_surface, this);
        mSurfaceView = findViewById(R.id.surface);
        mSurfaceHolder = mSurfaceView.getHolder();
        mSurfaceHolder.addCallback(new SurfaceHolder.Callback() {
            @Override
            public void surfaceCreated(SurfaceHolder holder) {
                // 锁定整个SurfaceView
                Canvas canvas = holder.lockCanvas();
                // 绘制背景
                Bitmap back = BitmapFactory.decodeResource(getContext().getResources(), R.mipmap.ic_launcher);
                // 绘制背景
                canvas.drawBitmap(back, 0, 0, null);
                // 绘制完成，释放画布，提交修改
                holder.unlockCanvasAndPost(canvas);
                // 重新锁一次，"持久化"上次所绘制的内容
                holder.lockCanvas(new Rect(0, 0, 0, 0));
                holder.unlockCanvasAndPost(canvas);
            }

            @Override
            public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {

            }

            @Override
            public void surfaceDestroyed(SurfaceHolder holder) {

            }
        });

        initPaint();
    }

    private void initPaint() {
        mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mPaint.setStrokeWidth(10);
        mPaint.setColor(Color.RED);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        // return super.onTouchEvent(event);
        if (event.getAction() == MotionEvent.ACTION_DOWN) {
            int cx = (int) event.getX();
            int cy = (int) event.getY();
            // 锁定SurfaceView的局部区域，只更新局部内容
            Canvas canvas = mSurfaceHolder.lockCanvas(new Rect(cx - 50,
                    cy - 50, cx + 50, cy + 50));
            // 保存canvas的当前状态
            canvas.save();
            // 旋转画布
            canvas.rotate(30, cx, cy);
            mPaint.setColor(Color.RED);
            // 绘制红色方块
            canvas.drawRect(cx - 40, cy - 40, cx, cy, mPaint);
            // 恢复Canvas之前的保存状态
            canvas.restore();
            mPaint.setColor(Color.GREEN);
            // 绘制绿色方块
            canvas.drawRect(cx, cy, cx + 40, cy + 40, mPaint);
            // 绘制完成，释放画布，提交修改
            mSurfaceHolder.unlockCanvasAndPost(canvas);
        }
        return false;
    }
}
