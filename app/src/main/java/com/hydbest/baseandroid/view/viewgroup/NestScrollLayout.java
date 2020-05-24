package com.hydbest.baseandroid.view.viewgroup;

import android.content.Context;
import androidx.annotation.Nullable;
import androidx.core.view.NestedScrollingParent;
import androidx.core.view.ViewCompat;
import android.util.AttributeSet;
import android.view.View;
import android.widget.LinearLayout;

public class NestScrollLayout extends LinearLayout implements NestedScrollingParent{

    private int mTopViewHeight;

    public NestScrollLayout(Context context) {
        this(context,null);
    }

    public NestScrollLayout(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs,0);
    }

    public NestScrollLayout(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    public boolean onStartNestedScroll(View child, View target, int nestedScrollAxes) {
        return (nestedScrollAxes & ViewCompat.SCROLL_AXIS_VERTICAL) != 0;
    }

    @Override
    public void onNestedPreScroll(View target, int dx, int dy, int[] consumed) {
        boolean hiddenTop = dy > 0 && getScrollY() < mTopViewHeight;
        boolean showTop = dy < 0 && getScrollY() > 0 && !ViewCompat.canScrollVertically(target, -1);

        if (hiddenTop || showTop)
        {
            scrollBy(0, dy);
            consumed[1] = dy;
        }
    }

    /**
     * onNestedFling你可以捕获对内部View的fling事件，如果return true则表示拦截掉内部View的事件。
     * @param target
     * @param velocityX
     * @param velocityY
     * @return
     */
    @Override
    public boolean onNestedPreFling(View target, float velocityX, float velocityY) {
        if (getScrollY() >= mTopViewHeight)
            return false;
        //fling((int) velocityY);
        return true;
    }
}
