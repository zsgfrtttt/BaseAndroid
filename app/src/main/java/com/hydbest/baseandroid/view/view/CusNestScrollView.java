package com.hydbest.baseandroid.view.view;

import android.content.Context;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.widget.NestedScrollView;
import android.util.AttributeSet;

public class CusNestScrollView extends NestedScrollView {

    private Callback mCallback;

    public void setCallback(Callback mCallback) {
        this.mCallback = mCallback;
    }

    public CusNestScrollView(@NonNull Context context) {
        this(context, null);
    }

    public CusNestScrollView(@NonNull Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public CusNestScrollView(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        setOnScrollChangeListener(new OnScrollChangeListener() {
            @Override
            public void onScrollChange(NestedScrollView v, int scrollX, int scrollY, int oldScrollX, int oldScrollY) {
                if (mCallback != null){
                    mCallback.onScrollChange(scrollX,scrollY,oldScrollX,oldScrollY);
                }
            }
        });
    }

    public interface Callback {
        void onScrollChange(int scrollX, int scrollY, int oldScrollX, int oldScrollY);
    }
}
