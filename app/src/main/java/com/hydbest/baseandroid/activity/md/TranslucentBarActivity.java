package com.hydbest.baseandroid.activity.md;

import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageButton;

import com.hydbest.baseandroid.R;
import com.hydbest.baseandroid.util.SystemBarUtil;
import com.hydbest.baseandroid.view.view.CusNestScrollView;

import java.lang.reflect.Field;

public class TranslucentBarActivity extends AppCompatActivity implements CusNestScrollView.Callback {

    private Toolbar toolbar;
    private CusNestScrollView scollview;
    private boolean init;
    private int distance;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_translucent_bar);

        Window window = getWindow();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS | WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
            window.getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | View.SYSTEM_UI_FLAG_LAYOUT_STABLE);
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(Color.TRANSPARENT);
        } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            window.addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        }

        scollview = findViewById(R.id.scollview);
        scollview.setCallback(this);
        toolbar = findViewById(R.id.toolbar);
        toolbar.setNavigationIcon(R.drawable.icon_back);

        SystemBarUtil.setStatusBarColor(this,  Color.RED,toolbar);
        setSupportActionBar(toolbar);
        modifyToolbar();
    }

    private void modifyToolbar() {
        try {
            Field field = Toolbar.class.getDeclaredField("mNavButtonView");
            field.setAccessible(true);
            ImageButton o = (ImageButton) field.get(toolbar);
            Toolbar.LayoutParams params = (Toolbar.LayoutParams) o.getLayoutParams();
            if (params != null) {
                params.leftMargin = 20;
                o.setLayoutParams(params);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public void onScrollChange(int scrollX, int scrollY, int oldScrollX, int oldScrollY) {
        Log.i("csz", " " + scrollY + " " + oldScrollY);
        if (!init) {
            init = true;
            distance = scrollY;
            scollview.scrollTo(0, 0);
        } else {
            if (scrollY > distance) {
                scrollY = distance;
            }
            float alpha = scrollY * 1.0f / distance;
            int argb = Color.argb(alpha, ((Color.RED >> 16) & 0xff) * 1.f / 255, 0, 0);
            toolbar.setBackgroundColor(argb);
        }
    }
}
