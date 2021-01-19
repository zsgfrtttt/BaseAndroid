package com.hydbest.baseandroid.util.network;

import android.net.ConnectivityManager;
import android.net.Network;
import android.os.Build;
import android.os.Handler;
import android.os.Message;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;

/**
 * @author caishuzhan
 */
@RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
public class NetworkCallbackImpl extends ConnectivityManager.NetworkCallback {

    private final Handler mObserveHandler;

    public NetworkCallbackImpl(@NonNull Handler observeHandler) {
        this.mObserveHandler = observeHandler;
    }

    @Override
    public void onAvailable(@NonNull Network network) {
        Message.obtain(mObserveHandler, NetworkStateManager.MSG_AVAILABLE, network).sendToTarget();
    }

    @Override
    public void onLost(@NonNull Network network) {
        Message.obtain(mObserveHandler, NetworkStateManager.MSG_LOST, network).sendToTarget();
    }
}
