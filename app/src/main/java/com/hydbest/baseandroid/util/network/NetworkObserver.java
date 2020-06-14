package com.hydbest.baseandroid.util.network;

import android.net.NetworkInfo;

import androidx.annotation.Nullable;

public interface NetworkObserver {

    void onAvailable(@Nullable NetworkInfo info);

    void onLost(@Nullable NetworkInfo info);

}
