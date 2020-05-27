package com.csz.runtime;

import android.app.Activity;
import android.app.Application;
import android.app.Application.ActivityLifecycleCallbacks;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

public class ActivityBuilder {
    private ActivityBuilder(){}
    private static final String SUFFIX = "Builder";

    public static final ActivityBuilder INSTANCE = new ActivityBuilder();

    private ActivityLifecycleCallbacks callbacks = new ActivityLifecycleCallbacks() {
        @Override
        public void onActivityCreated(@NonNull Activity activity, @Nullable Bundle savedInstanceState) {
            performInject(activity, savedInstanceState);
        }

        @Override
        public void onActivityStarted(@NonNull Activity activity) {

        }

        @Override
        public void onActivityResumed(@NonNull Activity activity) {

        }

        @Override
        public void onActivityPaused(@NonNull Activity activity) {

        }

        @Override
        public void onActivityStopped(@NonNull Activity activity) {

        }

        @Override
        public void onActivitySaveInstanceState(@NonNull Activity activity, @NonNull Bundle outState) {
            performSaveState(activity, outState);
        }

        @Override
        public void onActivityDestroyed(@NonNull Activity activity) {

        }
    };

    private void performInject(Activity activity, Bundle bundle) {
        try {
            Class.forName(activity.getClass().getName() + SUFFIX).getDeclaredMethod("inject", Activity.class, Bundle.class).invoke(null, activity, bundle);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void performSaveState(Activity activity, Bundle bundle) {
        try {
            Class.forName(activity.getClass().getName() + SUFFIX).getDeclaredMethod("saveState", Activity.class, Bundle.class).invoke(null, activity, bundle);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void startActivity(Context context, Intent intent) {
        if (!(context instanceof Activity)) {
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        }
        context.startActivity(intent);
    }

    public void init(Application app) {
        app.registerActivityLifecycleCallbacks(callbacks);
    }
}
