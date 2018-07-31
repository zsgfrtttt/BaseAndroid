package com.hydbest.baseandroid.view.view;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.RectF;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.support.annotation.Nullable;
import android.support.v7.widget.AppCompatImageView;
import android.util.AttributeSet;

/**
 * Created by csz on 2018/7/31.
 */

public class AdImageView extends AppCompatImageView{
    private RectF mBitmapRectF;
    private Bitmap mBitmap;

    private int mMinDy;
    private int mDy;


    public AdImageView(Context context) {
        this(context,null);
    }

    public AdImageView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs,0);
    }

    public AdImageView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {

    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);

        mMinDy = h;
        Drawable drawable = getDrawable();

        if (drawable == null) {
            return;
        }

        mBitmap = drawableToBitamp(drawable);
        mBitmapRectF = new RectF(0, 0, w,mBitmap.getHeight() * w / mBitmap.getWidth());

    }

    private Bitmap drawableToBitamp(Drawable drawable) {
        if (drawable instanceof BitmapDrawable) {
            BitmapDrawable bd = (BitmapDrawable) drawable;
            return bd.getBitmap();
        }
        int w = drawable.getIntrinsicWidth();
        int h = drawable.getIntrinsicHeight();
        Bitmap bitmap = Bitmap.createBitmap(w, h, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        drawable.setBounds(0, 0, w, h);
        drawable.draw(canvas);
        return bitmap;
    }

    public void setDy(int dy) {

        if (getDrawable() == null) {
            return;
        }
        mDy = dy - mMinDy;
        if (mDy <= 0) {
            mDy = 0;
        }
        if (mDy > mBitmapRectF.height() - mMinDy) {
            mDy = (int) (mBitmapRectF.height() - mMinDy);
        }
        invalidate();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        if (mBitmap == null) {
            return;
        }
        canvas.save();
        canvas.translate(0, -mDy);
        canvas.drawBitmap(mBitmap, null, mBitmapRectF, null);
        canvas.restore();
    }
}
