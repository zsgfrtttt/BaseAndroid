package com.hydbest.baseandroid.view.view;

import android.animation.Animator;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.widget.RelativeLayout;

import com.hydbest.baseandroid.R;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class RippleView extends RelativeLayout {

    private Paint mPaint;
    private int rippleColor;
    private int strokeWidth;
    private int radius;
    private int type;

    private AnimatorSet animatorSet;
    private List<View> views;

    private boolean isRunning;

    public RippleView(Context context) {
        this(context, null);
    }

    public RippleView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public RippleView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs);
    }

    private void init(Context context, AttributeSet attrs) {
        mPaint = new Paint();
        mPaint.setDither(true);
        mPaint.setAntiAlias(true);

        views = new ArrayList<>();

        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.ripple_view);
        rippleColor = typedArray.getColor(typedArray.getIndex(R.styleable.ripple_view_rippleColor), Color.RED);
        strokeWidth = typedArray.getInteger(typedArray.getIndex(R.styleable.ripple_view_stroke_width), 0);
        radius = typedArray.getInteger(typedArray.getIndex(R.styleable.ripple_view_radius), 4);
        type = typedArray.getInt(typedArray.getIndex(R.styleable.ripple_view_ripple_type), 0);
        typedArray.recycle();

        if (type == 0) {
            mPaint.setStyle(Paint.Style.FILL);
        } else {
            mPaint.setStyle(Paint.Style.STROKE);
        }
        mPaint.setColor(rippleColor);
        mPaint.setStrokeWidth(strokeWidth);


        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(radius + strokeWidth, radius + strokeWidth);
        params.addRule(CENTER_IN_PARENT, TRUE);

        float maxScale = 10f;
        int duration = 3500;
        int delay = 3500 / 4;

        List<Animator> list = new ArrayList<>();
        for (int i = 0; i < 4; i++) {
            RippleAnimationView rippleAnimationView = new RippleAnimationView(context);
            rippleAnimationView.setVisibility(INVISIBLE);
            addView(rippleAnimationView, params);
            views.add(rippleAnimationView);

            ObjectAnimator scaleX = ObjectAnimator.ofFloat(rippleAnimationView, "scaleX", 1.f, maxScale);
            scaleX.setRepeatCount(ValueAnimator.INFINITE);
            scaleX.setRepeatMode(ValueAnimator.RESTART);
            scaleX.setStartDelay(i * delay);
            list.add(scaleX);

            ObjectAnimator scaleY = ObjectAnimator.ofFloat(rippleAnimationView, "scaleY", 1.f, maxScale);
            scaleY.setRepeatCount(ValueAnimator.INFINITE);
            scaleY.setRepeatMode(ValueAnimator.RESTART);
            scaleY.setStartDelay(i * delay);
            list.add(scaleY);

            ObjectAnimator alpha = ObjectAnimator.ofFloat(rippleAnimationView, "alpha", 1.f, 0f);
            alpha.setRepeatCount(ValueAnimator.INFINITE);
            alpha.setRepeatMode(ValueAnimator.RESTART);
            alpha.setStartDelay(i * delay);
            list.add(alpha);
        }

        animatorSet = new AnimatorSet();
        animatorSet.playTogether(list);
        animatorSet.setDuration(duration);
        animatorSet.setInterpolator(new AccelerateDecelerateInterpolator());
    }

    public void startAnim(){
        if (!isRunning){
            isRunning = true;
            for (View view : views) {
                view.setVisibility(VISIBLE);
            }
            animatorSet.start();
        }
    }

    public void stopAnim(){
        if (isRunning){
            isRunning = false;
            for (View view : views) {
                view.setVisibility(INVISIBLE);
            }
            animatorSet.end();
        }
    }

    public boolean isRunning() {
        return isRunning;
    }

    class RippleAnimationView extends View {

        public RippleAnimationView(Context context) {
            this(context, null);
        }

        public RippleAnimationView(Context context, @Nullable AttributeSet attrs) {
            this(context, attrs, 0);
        }

        public RippleAnimationView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
            super(context, attrs, defStyleAttr);
        }


        @Override
        protected void onDraw(Canvas canvas) {
            super.onDraw(canvas);
            int width = Math.min(getWidth(), getHeight()) / 2 - strokeWidth / 2;
            canvas.drawCircle(getWidth()/2,getHeight()/2,width  ,mPaint);
        }
    }
}
