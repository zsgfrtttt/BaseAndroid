package com.hydbest.baseandroid.activity.cus_viewgroup;

import android.os.Bundle;
import androidx.annotation.Nullable;
import com.google.android.material.navigation.NavigationView;
import com.hydbest.baseandroid.R;
import com.hydbest.baseandroid.activity.jetpack.A;

import androidx.drawerlayout.widget.DrawerLayout;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.util.Log;
import android.view.MenuItem;

/**
 * Created by csz on 2018/8/6.
 */

public class NavigationActivity extends AppCompatActivity{

    private DrawerLayout mDrawerLayout;
    private NavigationView mNavigationView;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(com.hydbest.baseandroid.R.layout.activity_navigation);

        mDrawerLayout = (DrawerLayout) findViewById(com.hydbest.baseandroid.R.id.id_drawer_layout);
        mNavigationView = (NavigationView) findViewById(com.hydbest.baseandroid.R.id.id_nv_menu);

        Toolbar toolbar = (Toolbar) findViewById(com.hydbest.baseandroid.R.id.id_toolbar);
        setSupportActionBar(toolbar);

        final ActionBar ab = getSupportActionBar();
        ab.setHomeAsUpIndicator(R.drawable.brvah_sample_footer_loading);
        ab.setDisplayHomeAsUpEnabled(true);

        setupDrawerContent(mNavigationView);

        A.B data = getIntent().getParcelableExtra("data");
        Log.i("csz","B : " + data.getIndex());
    }

    private void setupDrawerContent(NavigationView navigationView)
    {
        navigationView.setNavigationItemSelectedListener(

                new NavigationView.OnNavigationItemSelectedListener()
                {

                    private MenuItem mPreMenuItem;

                    @Override
                    public boolean onNavigationItemSelected(MenuItem menuItem)
                    {
                        if (mPreMenuItem != null) mPreMenuItem.setChecked(false);
                        menuItem.setChecked(true);
                        mDrawerLayout.closeDrawers();
                        mPreMenuItem = menuItem;
                        return true;
                    }
                });
    }


}
