package com.hydbest.baseandroid.activity.foundation;

import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.hydbest.baseandroid.R;
import com.hydbest.baseandroid.util.network.NetworkStateManager;
import com.hydbest.baseandroid.util.network.NetworkType;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

public class NetworkObserveActivity extends AppCompatActivity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_network_observe);
        NetworkStateManager.withRegisteNetworkCallback(this);
    }

    public void test(View view) {
        boolean networkAvailable = NetworkStateManager.isNetworkAvailable(this);
        boolean wifiAvailable = NetworkStateManager.isWifiAvailable(this);
        NetworkType networkType = NetworkStateManager.getNetworkType(this);
        Log.i("csz", networkAvailable + "   " + wifiAvailable + "   " + networkType.getDesc());
    }
}
