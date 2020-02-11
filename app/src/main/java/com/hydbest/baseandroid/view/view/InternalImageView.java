package com.hydbest.baseandroid.view.view;

import android.content.Context;
import android.graphics.Matrix;
import android.graphics.RectF;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.support.v7.widget.AppCompatImageView;
import android.util.AttributeSet;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.ScaleGestureDetector;
import android.view.ViewConfiguration;
import android.view.ViewTreeObserver;
import android.widget.ImageView;


public class InternalImageView extends AppCompatImageView implements ViewTreeObserver.OnGlobalLayoutListener, ScaleGestureDetector.OnScaleGestureListener {

    private boolean mLayout;

    private float mScale;
    private float mMaxScale;
    private float mMinScale;

    private Matrix mMatrix;
    private ScaleGestureDetector mScaleGestureDetector;
    private GestureDetector mGestureDetector;

    private int mPointerCount;
    private float mLastTouchX;
    private float mLastTouchY;
    private float mTouchSlop;
    private boolean mScaleing;

    public InternalImageView(Context context, AttributeSet attrs) {
        super(context, attrs);
        setScaleType(ImageView.ScaleType.MATRIX);
        mMatrix = new Matrix();
        mScaleGestureDetector = new ScaleGestureDetector(context, this);
        mTouchSlop = ViewConfiguration.get(context).getScaledTouchSlop();
        mGestureDetector = new GestureDetector(context, new GestureDetector.SimpleOnGestureListener() {
            @Override
            public boolean onDoubleTap(MotionEvent e) {
                float scale = getScale();
                float x = e.getX();
                float y = e.getY();
                if (scale >= mMinScale) {
                    if (!mScaleing) {
                        post(new ScaleRunnable(scale, mScale, x, y));
                        mScaleing = true;
                    }
                }
                if (scale < mMinScale) {
                    if (!mScaleing) {
                        post(new ScaleRunnable(scale, mMinScale, x, y));
                        mScaleing = true;
                    }
                }

                return true;
            }
        });
    }

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        getViewTreeObserver().addOnGlobalLayoutListener(this);
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
            getViewTreeObserver().removeOnGlobalLayoutListener(this);
        } else{
            getViewTreeObserver().removeGlobalOnLayoutListener(this);
        }
    }

    @Override
    public void onGlobalLayout() {
        if (!mLayout) {
            Drawable d = getDrawable();
            if (d == null) return;
            int width = getWidth();
            int height = getHeight();

            int dw = d.getIntrinsicWidth();
            int dh = d.getIntrinsicHeight();
            /**
             * 缩放，确保图片在view内部
             */
            float scale = 1.0f;
            if (dw > width && dh <= width) {
                scale = width * 1.0f / dw;
            }
            if (dh > height && dw <= width) {
                scale = height * 1.0f / dh;
            }
            if (dw > width && dh > height) {
                scale = Math.min(width * 1.0f / dw, height * 1.0f / dh);
            }
            if (dw < width && dh < height) {
                scale = Math.min(width * 1.0f / dw, height * 1.0f / dh);
            }
            mScale = scale;
            mMaxScale = scale * 4;
            mMinScale = scale * 2;

            int dx = width / 2 - dw / 2;
            int dy = height / 2 - dh / 2;
            mMatrix.postTranslate(dx, dy);
            mMatrix.postScale(scale, scale, width / 2, height / 2);
            setImageMatrix(mMatrix);

            mLayout = true;
        }
    }

    private float getScale() {
        float[] values = new float[9];
        mMatrix.getValues(values);
        return values[Matrix.MSCALE_X];
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (mGestureDetector.onTouchEvent(event)) {
            return true;
        }
        mScaleGestureDetector.onTouchEvent(event);
        int pointerCount = event.getPointerCount();
        int x = 0, y = 0;
        for (int i = 0; i < pointerCount; i++) {
            x += event.getX(i);
            y += event.getY(i);
        }
        x /= pointerCount;
        y /= pointerCount;

        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN: {
                RectF rectF = getScaleRectF();
                if (rectF.width() > getWidth() + 0.01 || rectF.height() > getHeight() + 0.01) {
                    getParent().requestDisallowInterceptTouchEvent(true);
                }
                break;
            }
            case MotionEvent.ACTION_MOVE: {
                RectF rectF = getScaleRectF();
                if (rectF.width() > getWidth() + 0.01 || rectF.height() > getHeight() + 0.01) {
                    getParent().requestDisallowInterceptTouchEvent(true);
                }
                float dx = x - mLastTouchX;
                float dy = y - mLastTouchY;
                if (isMoveAction(dx, dy) && isCanDrag(pointerCount)) {
                    mMatrix.postTranslate(dx, dy);
                    checkBorderScale();
                    setImageMatrix(mMatrix);
                }
                break;
            }
            case MotionEvent.ACTION_UP:
            case MotionEvent.ACTION_CANCEL:
                mPointerCount = 0;
                break;
        }
        mLastTouchX = x;
        mLastTouchY = y;
        mPointerCount = pointerCount;
        return true;
    }

    private boolean isCanDrag(int pointerCount) {
        return mPointerCount == pointerCount;
    }

    private boolean isMoveAction(float dx, float dy) {
        return Math.sqrt(dx * dx + dy * dy) > mTouchSlop;
    }

    @Override
    public boolean onScale(ScaleGestureDetector detector) {
        if (getDrawable() == null) return true;
        float scaleFactor = detector.getScaleFactor();
        float scale = getScale();
        if ((scale < mMaxScale && scaleFactor > 1.0f) || (scale > mScale && scaleFactor < 1.0f)) {
            if (scale * scaleFactor > mMaxScale) {
                scaleFactor = mMaxScale / scale;
            }
            if (scale * scaleFactor < mScale) {
                scaleFactor = mScale / scale;
            }

            mMatrix.postScale(scaleFactor, scaleFactor, detector.getFocusX(), detector.getFocusY());

            checkBorderScale();
            setImageMatrix(mMatrix);

        }
        return true;
    }

    private void checkBorderScale() {
        RectF rectF = getScaleRectF();
        if (rectF == null) return;
        float dx = 0, dy = 0;
        int width = getWidth();
        int height = getHeight();
        if (rectF.width() >= width) {
            if (rectF.left > 0) {
                dx = -rectF.left;
            }
            if (rectF.right < width) {
                dx = width - rectF.right;
            }
        }
        if (rectF.height() >= height) {
            if (rectF.top > 0) {
                dy = -rectF.top;
            }
            if (rectF.bottom < height) {
                dy = height - rectF.bottom;
            }
        }

        if (rectF.width() < width) {
            dx = width / 2f - rectF.right + rectF.width() / 2f;
        }
        if (rectF.height() < height) {
            dy = height / 2f - rectF.bottom + rectF.height() / 2f;
        }
        mMatrix.postTranslate(dx, dy);
    }

    private RectF getScaleRectF() {
        Drawable drawable = getDrawable();
        RectF rectF = null;
        if (drawable != null) {
            rectF = new RectF(0, 0, drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight());
            mMatrix.mapRect(rectF);
        }
        return rectF;
    }

    @Override
    public boolean onScaleBegin(ScaleGestureDetector detector) {
        return true;
    }

    @Override
    public void onScaleEnd(ScaleGestureDetector detector) {

    }

    private class ScaleRunnable implements Runnable {
        private float target;
        private float x;
        private float y;
        private float ZOOM_BIG = 1.06f;
        private float ZOOM_SMALL = 0.94f;
        private float mZoom;

        public ScaleRunnable(float scale, float target, float x, float y) {
            this.target = target;
            this.x = x;
            this.y = y;
            if (scale > target) {
                mZoom = ZOOM_SMALL;
            } else {
                mZoom = ZOOM_BIG;
            }
        }

        @Override
        public void run() {
            float scale = getScale();
            if ((scale * mZoom >= target && mZoom > 1.0f) || (scale * mZoom <= target && mZoom < 1.0f)) {
                mZoom = target / scale;
                mMatrix.postScale(mZoom, mZoom, x, y);
                checkBorderScale();
                setImageMatrix(mMatrix);
                mScaleing = false;
            } else {
                mMatrix.postScale(mZoom, mZoom, x, y);
                checkBorderScale();
                setImageMatrix(mMatrix);
                postDelayed(this, 10);
            }

        }
    }
}
