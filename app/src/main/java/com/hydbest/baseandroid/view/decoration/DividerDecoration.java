package com.hydbest.baseandroid.view.decoration;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;

import androidx.recyclerview.widget.RecyclerView;
import android.util.Log;
import android.view.View;

/**
 * Created by csz on 2019/6/10.
 */

public class DividerDecoration extends RecyclerView.ItemDecoration {

    private Paint mPaint;
    private int mDiHeight = 10;

    public DividerDecoration(){
        mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mPaint.setDither(true);
        mPaint.setColor(Color.RED);
    }

    @Override
    public void onDraw(Canvas c, RecyclerView parent, RecyclerView.State state) {
        super.onDraw(c, parent, state);
        final int left = parent.getLeft();
        final int right = parent.getRight();
        for (int i = 0; i < parent.getChildCount(); i++) {
            final View childView = parent.getChildAt(i);
            final int bottom = childView.getTop();
            final int top = bottom - mDiHeight;
            c.drawRect(left, top, right, bottom, mPaint);
        }
    }

    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
        super.getItemOffsets(outRect, view, parent, state);
        int childAdapterPosition = parent.getChildAdapterPosition(view);
        Log.i("csz",childAdapterPosition + "   :    "+outRect.toString());
        if (childAdapterPosition != 0) {
            //为非第一个顶部留出空间
            outRect.top = mDiHeight ;
        }
    }
}
