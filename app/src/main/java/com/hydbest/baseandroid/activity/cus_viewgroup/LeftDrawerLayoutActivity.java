package com.hydbest.baseandroid.activity.cus_viewgroup;

import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

import com.hydbest.baseandroid.R;
import com.hydbest.baseandroid.fragment.LeftMenuFragment;
import com.hydbest.baseandroid.view.viewdraghelper.DrawlerLayout;

public class LeftDrawerLayoutActivity extends AppCompatActivity {

    private LeftMenuFragment mMenuFragment;
    private DrawlerLayout mLeftDrawerLayout;
    private TextView mContentTv;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_left_drawer_layout);

        mLeftDrawerLayout = findViewById(R.id.id_drawerlayout);
        mContentTv =  findViewById(R.id.id_content_tv);

        FragmentManager fm = getSupportFragmentManager();
        mMenuFragment = (LeftMenuFragment) fm.findFragmentById(R.id.id_container_menu);
        if (mMenuFragment == null) {
            fm.beginTransaction().add(R.id.id_container_menu, mMenuFragment = new LeftMenuFragment()).commit();
        }

        mMenuFragment.setOnMenuItemSelectedListener(new LeftMenuFragment.OnMenuItemSelectedListener() {
            @Override
            public void menuItemSelected(String title) {
                mLeftDrawerLayout.closeDrawer();
                mContentTv.setText(title);
            }
        });

    }
}
