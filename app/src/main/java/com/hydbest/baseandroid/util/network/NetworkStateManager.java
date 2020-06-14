package com.hydbest.baseandroid.util.network;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.Network;
import android.net.NetworkInfo;
import android.net.NetworkRequest;
import android.os.Build;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

/**
 * @author csz
 */
public class NetworkStateManager {
    public static final int MSG_AVAILABLE = 0;
    public static final int MSG_LOST = 1;

    private static volatile NetworkStateManager sInstance;

    private NetworkStateManager() {
    }

    private ConnectivityManager mConnectivityManager;
    private List<NetworkObserver> mObserverList = new ArrayList<>();

    private boolean mIsRegiste;
    private ConnectivityManager.NetworkCallback mNetworkCallback;

    /**
     * 必须先注册callbak才有效
     *
     * @see #registeNetworkCallback()
     */
    private boolean mIsNetworkAvailable;
    private boolean mIsWifiAvailable;
    private int mNetworkType = -1;

    private Handler mObserveHandler = new Handler(Looper.getMainLooper()) {
        @Override
        public void handleMessage(@NonNull Message msg) {
            switch (msg.what) {
                case MSG_AVAILABLE:
                    notifyNetworkState((Network) msg.obj, true);
                    notifyObserver((Network) msg.obj, true);
                    break;
                case MSG_LOST:
                    notifyNetworkState((Network) msg.obj, false);
                    notifyObserver((Network) msg.obj, false);
                    break;
            }
        }
    };

    public static NetworkStateManager getInstance(Context context) {
        if (sInstance == null) {
            synchronized (NetworkStateManager.class) {
                if (sInstance == null) {
                    sInstance = new NetworkStateManager();
                    sInstance.initialize(context);
                }
            }
        }
        return sInstance;
    }

    public static NetworkStateManager withRegisteNetworkCallback(Context context) {
        NetworkStateManager instance = getInstance(context);
        instance.registeNetworkCallback();
        return instance;
    }

    private void initialize(Context context) {
        mConnectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
    }

    /**
     * 更新当前网络状态
     *
     * @param network
     * @param availavle
     */
    private void notifyNetworkState(Network network, boolean availavle) {
        NetworkInfo info = mConnectivityManager.getNetworkInfo(network);

        mIsNetworkAvailable = availavle;
        if (availavle && info != null && info.getType() == ConnectivityManager.TYPE_WIFI) {
            mIsWifiAvailable = true;
        } else {
            mIsWifiAvailable = false;
        }
        mNetworkType = availavle ? info.getType() : -1;
    }

    /**
     * 通知网络观察者网络状态发生变化
     *
     * @param network
     * @param availavle
     */
    private void notifyObserver(@Nullable Network network, boolean availavle) {
        mIsNetworkAvailable = availavle;
        NetworkInfo info = mConnectivityManager.getNetworkInfo(network);
        for (NetworkObserver observer : mObserverList) {
            if (availavle) {
                if (info != null && info.getType() == ConnectivityManager.TYPE_WIFI) {
                    mIsWifiAvailable = true;
                } else {
                    mIsWifiAvailable = false;
                }
                observer.onAvailable(info);
            } else {
                observer.onLost(info);
            }
        }
    }

    private synchronized void registeNetworkCallback() {
        if (mIsRegiste) {
            return;
        }
        mIsRegiste = true;
        mNetworkCallback = new NetworkCallbackImpl(mObserveHandler);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            mConnectivityManager.registerDefaultNetworkCallback(mNetworkCallback, mObserveHandler);
        } else {
            NetworkRequest request = new NetworkRequest.Builder().build();
            mConnectivityManager.registerNetworkCallback(request, mNetworkCallback);
        }
    }

    private synchronized void unRegisterNetworkCallback() {
        if (mIsRegiste) {
            mConnectivityManager.unregisterNetworkCallback(mNetworkCallback);
            mIsRegiste = false;
        }
    }

    public void addNetworkCallbackObserver(@NonNull NetworkObserver observer) {
        if (mIsRegiste) {
            mObserverList.add(observer);
        } else {
            registeNetworkCallback();
            mObserverList.add(observer);
        }
    }

    public void removeNetworkCallbackObserver(@NonNull NetworkObserver observer) {
        mObserverList.remove(observer);
    }

    public boolean isNetworkAvailable() {
        if (!mIsRegiste) {
            throw new RuntimeException("must registerNetworkCallback before.");
        }
        return mIsNetworkAvailable;
    }

    public boolean isWifiAvailable() {
        if (!mIsRegiste) {
            throw new RuntimeException("must registerNetworkCallback before.");
        }
        return mIsWifiAvailable;
    }

    public NetworkType getNetworkType() {
        if (!mIsRegiste) {
            throw new RuntimeException("must registerNetworkCallback before.");
        }
        return NetworkType.build(mNetworkType);
    }

    /**
     * 静态方法获取网络可用状态
     * @param context
     * @return
     */
    public static boolean isNetworkAvailable(Context context) {
        ConnectivityManager manager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            Network activeNetwork = manager.getActiveNetwork();
            return activeNetwork != null;
        } else {
            NetworkInfo info = manager.getActiveNetworkInfo();
            return info != null && info.isConnected();
        }
    }

    /**
     * 静态方法获取网络WIFI可用状态
     * @param context
     * @return
     */
    public static boolean isWifiAvailable(Context context) {
        ConnectivityManager manager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = manager.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
        return networkInfo != null && (networkInfo.getState() == NetworkInfo.State.CONNECTED || networkInfo.getState() == NetworkInfo.State.CONNECTING);
    }

    /**
     * 静态方法获取网络类型
     * @param context
     * @return
     */
    public static NetworkType getNetworkType(Context context){
        NetworkType networkType = null;
        ConnectivityManager manager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo info = manager.getActiveNetworkInfo();
        if (info != null){
            networkType = NetworkType.build(info.getType());
        }else {
            networkType = NetworkType.build(-1);
        }
        return networkType;
    }


}
