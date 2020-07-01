package com.hydbest.baseandroid.activity.other.syncAdapter;

import android.accounts.Account;
import android.content.ContentResolver;
import android.content.SyncRequest;
import android.os.Build;
import android.os.Bundle;
import android.view.View;

import com.hydbest.baseandroid.R;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;


/**
 * @author csz
 */
public class SyncAdapterActivity extends AppCompatActivity {

    private static final String AUTHORITY = "com.csz.syncAdapter";
    private static final long SYNC_INTERVAL = 60 * 60 * 2;
    private static final long FLEX_TIME = 60 * 5;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sync_adapter);
    }

    /**
     * 手动同步
     *
     * @param view
     */
    public void syncData(View view) {
        AuthenticatorSyncAdapter.syncImmediately(this);
    }

    /**
     * 定时同步
     *
     * @param view
     */
    public void periodicSync(View view) {
        Account account = AuthenticatorSyncAdapter.getSyncAccount(this);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            SyncRequest request = new SyncRequest.Builder()
                    .syncPeriodic(SYNC_INTERVAL, FLEX_TIME)
                    .setSyncAdapter(account, AUTHORITY)
                    .setExtras(Bundle.EMPTY).build();
            ContentResolver.requestSync(request);
        } else {
            ContentResolver.addPeriodicSync(account, AUTHORITY, Bundle.EMPTY, SYNC_INTERVAL);
        }
        ContentResolver.setSyncAutomatically(account, AUTHORITY, true);
    }
}
