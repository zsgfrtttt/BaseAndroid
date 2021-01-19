package com.hydbest.baseandroid.activity.jetpack;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.util.Log;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.hydbest.baseandroid.R;

import java.util.Deque;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.NavBackStackEntry;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

public class JetpackNavigationActivity extends AppCompatActivity {

    NavController controller;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //activity_jetpack_navigation

        setContentView(R.layout.activity_navigation_bottom);

        BottomNavigationView navigationView = findViewById(R.id.navi);
        controller = Navigation.findNavController(this, R.id.root);
        AppBarConfiguration configuration = new AppBarConfiguration.Builder(navigationView.getMenu()).build();
        //   NavigationUI.setupActionBarWithNavController(this,controller,configuration);
        NavigationUI.setupWithNavController(navigationView, controller);

    }
}
