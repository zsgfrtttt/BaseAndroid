package com.hydbest.baseandroid.activity.foundation;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.accessibility.AccessibilityEvent;

import com.hydbest.baseandroid.util.AccessibilityDelegateWrapper;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

/**
 * 为每个View设置AccessibilityDelegate，监听各种事件
 */
public class AutoTrackActivity extends AppCompatActivity {

    @Override
    public Object getSystemService(@NonNull String name) {
        Object object = super.getSystemService(name);
        if (traceEnable() && object instanceof LayoutInflater) {
            return new TrackLayoutInflater((LayoutInflater) object, this);
        }
        return object;
    }

    public boolean traceEnable() {
        return true;
    }

    public static class TrackLayoutInflater extends LayoutInflater {
        private final LayoutInflater mProxy;

        public TrackLayoutInflater(LayoutInflater proxy, Context context) {
            super(proxy, context);
            this.mProxy = proxy;
        }

        @Override
        public LayoutInflater cloneInContext(Context newContext) {
            return new TrackLayoutInflater(mProxy, newContext);
        }

        @Override
        public View inflate(int resource, @Nullable ViewGroup root) {
            View view = mProxy.inflate(resource, root);
            View content = view.findViewById(android.R.id.content);
            if (content instanceof ViewGroup) {
                flatViews((ViewGroup) content);
            }
            return view;
        }

        private static void bindAccessibilityDelegate(View view) {
            if (view != null) {
                view.setAccessibilityDelegate(AccessibilityDelegateWrapper.create(view, new View.AccessibilityDelegate() {
                    @Override
                    public void sendAccessibilityEvent(View host, int eventType) {
                        if (eventType == AccessibilityEvent.TYPE_VIEW_CLICKED) {
                            Log.i("csz", host.getClass().getCanonicalName() + "  click");
                        }else if (eventType == AccessibilityEvent.TYPE_VIEW_LONG_CLICKED){
                            Log.i("csz", host.getClass().getCanonicalName() + "  longClick");
                        }
                        super.sendAccessibilityEvent(host, eventType);
                    }
                }));
            }
        }

        private void flatViews(ViewGroup group) {
            for (int i = 0; i < group.getChildCount(); i++) {
                View child = group.getChildAt(i);
                bindAccessibilityDelegate(child);
                if (child instanceof ViewGroup){
                    flatViews((ViewGroup) child);
                }
            }
        }
    }


}