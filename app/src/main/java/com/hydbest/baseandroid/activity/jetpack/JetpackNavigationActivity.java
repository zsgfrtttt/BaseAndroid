package com.hydbest.baseandroid.activity.jetpack;

import android.os.Bundle;
import android.util.Log;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.hydbest.baseandroid.R;

import java.util.Arrays;
import java.util.List;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;
import androidx.recyclerview.widget.PagerSnapHelper;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

public class JetpackNavigationActivity extends AppCompatActivity {

    NavController controller;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //activity_jetpack_navigation

        setContentView(R.layout.activity_navigation_bottom);

        BottomNavigationView navigationView = findViewById(R.id.navi);
         controller = Navigation.findNavController(this,R.id.root);
        AppBarConfiguration configuration = new AppBarConfiguration.Builder(navigationView.getMenu()).build();
     //   NavigationUI.setupActionBarWithNavController(this,controller,configuration);
        NavigationUI.setupWithNavController(navigationView,controller);

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();


    }
}
