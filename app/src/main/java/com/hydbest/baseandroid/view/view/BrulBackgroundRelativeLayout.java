package com.hydbest.baseandroid.view.view;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.LayerDrawable;
import android.util.AttributeSet;
import android.view.animation.AccelerateInterpolator;
import android.widget.RelativeLayout;

public class BrulBackgroundRelativeLayout extends RelativeLayout {
    private LayerDrawable mLayerDrawable;
    private Drawable[] mDrawable;

    private ValueAnimator mAlphaAnimation;

    public BrulBackgroundRelativeLayout(Context context) {
        this(context, null);
    }

    public BrulBackgroundRelativeLayout(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public BrulBackgroundRelativeLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(null);
    }

    private void init(Drawable drawable) {
        boolean isBackground;
        isBackground = drawable != null;
        Drawable background = getBackground();
        if (background == null && !isBackground) return;
        mDrawable = new Drawable[]{isBackground ? drawable : background, isBackground ? drawable : background};
        mLayerDrawable = new LayerDrawable(mDrawable);
        setBackground(mLayerDrawable);
        mLayerDrawable.setId(0, 0);
        mLayerDrawable.setId(1, 1);

        mAlphaAnimation = ValueAnimator.ofFloat(0f, 1f).setDuration(500);
        mAlphaAnimation.setInterpolator(new AccelerateInterpolator());
        mAlphaAnimation.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                float value = (float) animation.getAnimatedValue();
                mDrawable[1].setAlpha((int) (value * 255));
            }
        });
        mAlphaAnimation.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                mDrawable[0] = mDrawable[1];
                mLayerDrawable.setDrawableByLayerId(0, mDrawable[0]);
            }

            @Override
            public void onAnimationStart(Animator animation) {
                mLayerDrawable.setDrawableByLayerId(1, mDrawable[1]);
            }
        });
        if (isBackground){
            mAlphaAnimation.start();
        }
    }

    public void setForeground(Drawable drawable) {
        if (getBackground() != null) {
            mDrawable[1] = drawable;
            mAlphaAnimation.start();
        } else {
            init(drawable);
        }

    }
}
