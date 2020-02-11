package com.hydbest.baseandroid.activity.other.syncAdapter;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.Nullable;

/**
 * @author csz
 */
public class SyncService extends Service {

    private static AuthenticatorSyncAdapter sAuthenticatorSyncAdapter;
    private static final Object sLock = new Object();

    @Override
    public void onCreate() {
        super.onCreate();
        synchronized (sLock){
            if (sAuthenticatorSyncAdapter == null){
                sAuthenticatorSyncAdapter = new AuthenticatorSyncAdapter(this,true);
            }
        }
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return sAuthenticatorSyncAdapter.getSyncAdapterBinder();
    }
}
