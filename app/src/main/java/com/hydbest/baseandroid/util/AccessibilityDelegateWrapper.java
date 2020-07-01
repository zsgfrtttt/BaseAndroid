package com.hydbest.baseandroid.util;

import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.view.accessibility.AccessibilityEvent;
import android.view.accessibility.AccessibilityNodeInfo;
import android.view.accessibility.AccessibilityNodeProvider;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

public class AccessibilityDelegateWrapper extends View.AccessibilityDelegate {

    private final List<View.AccessibilityDelegate> mProxy = new ArrayList<>();

    public AccessibilityDelegateWrapper(@NonNull View view, @NonNull View.AccessibilityDelegate proxy) {
        View.AccessibilityDelegate delegate = null;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.Q) {
            delegate = view.getAccessibilityDelegate();
        }
        if (delegate != null) {
            mProxy.add(delegate);
        }
        mProxy.add(proxy);
    }

    public static final AccessibilityDelegateWrapper create(@NonNull View view, @NonNull View.AccessibilityDelegate delegate) {
        return new AccessibilityDelegateWrapper(view, delegate);
    }

    @Override
    public void sendAccessibilityEvent(View host, int eventType) {
        for (View.AccessibilityDelegate delegate : mProxy) {
            delegate.sendAccessibilityEvent(host, eventType);
        }
        super.sendAccessibilityEvent(host, eventType);
    }

    @Override
    public boolean performAccessibilityAction(View host, int action, Bundle args) {
        for (View.AccessibilityDelegate delegate : mProxy) {
            delegate.performAccessibilityAction(host, action, args);
        }
        return super.performAccessibilityAction(host, action, args);
    }

    @Override
    public void sendAccessibilityEventUnchecked(View host, AccessibilityEvent event) {
        for (View.AccessibilityDelegate delegate : mProxy) {
            delegate.sendAccessibilityEventUnchecked(host, event);
        }
        super.sendAccessibilityEventUnchecked(host, event);
    }

    @Override
    public boolean dispatchPopulateAccessibilityEvent(View host, AccessibilityEvent event) {
        for (View.AccessibilityDelegate delegate : mProxy) {
            delegate.dispatchPopulateAccessibilityEvent(host, event);
        }
        return super.dispatchPopulateAccessibilityEvent(host, event);
    }

    @Override
    public void onPopulateAccessibilityEvent(View host, AccessibilityEvent event) {
        for (View.AccessibilityDelegate delegate : mProxy) {
            delegate.onPopulateAccessibilityEvent(host, event);
        }
        super.onPopulateAccessibilityEvent(host, event);
    }

    @Override
    public void onInitializeAccessibilityEvent(View host, AccessibilityEvent event) {
        for (View.AccessibilityDelegate delegate : mProxy) {
            delegate.onInitializeAccessibilityEvent(host, event);
        }
        super.onInitializeAccessibilityEvent(host, event);
    }

    @Override
    public void onInitializeAccessibilityNodeInfo(View host, AccessibilityNodeInfo info) {
        for (View.AccessibilityDelegate delegate : mProxy) {
            delegate.onInitializeAccessibilityNodeInfo(host, info);
        }
        super.onInitializeAccessibilityNodeInfo(host, info);
    }

    @Override
    public void addExtraDataToAccessibilityNodeInfo(@NonNull View host, @NonNull AccessibilityNodeInfo info, @NonNull String extraDataKey, @Nullable Bundle arguments) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            for (View.AccessibilityDelegate delegate : mProxy) {
                delegate.addExtraDataToAccessibilityNodeInfo(host, info, extraDataKey, arguments);
            }
        }
        super.addExtraDataToAccessibilityNodeInfo(host, info, extraDataKey, arguments);
    }

    @Override
    public boolean onRequestSendAccessibilityEvent(ViewGroup host, View child, AccessibilityEvent event) {
        for (View.AccessibilityDelegate delegate : mProxy) {
            delegate.onRequestSendAccessibilityEvent(host, child, event);
        }
        return super.onRequestSendAccessibilityEvent(host, child, event);
    }

    @Override
    public AccessibilityNodeProvider getAccessibilityNodeProvider(View host) {
        for (View.AccessibilityDelegate delegate : mProxy) {
            delegate.getAccessibilityNodeProvider(host);
        }
        return super.getAccessibilityNodeProvider(host);
    }
}
