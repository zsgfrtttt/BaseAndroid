package com.hydbest.baseandroid.activity.cus_viewgroup;

import android.os.Bundle;

import com.hydbest.baseandroid.R;
import com.hydbest.baseandroid.fragment.FirstFragment;
import com.hydbest.baseandroid.fragment.LeftMenuFragment;
import com.hydbest.baseandroid.fragment.SecondFragment;
import com.indicator.library.IndicatorAdapter;
import com.indicator.library.IndicatorContainer;

import java.util.Arrays;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;

public class PagerIndicatorActivity extends AppCompatActivity {

    ViewPager vp;
    IndicatorContainer wrapper;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pager_indicator1);
        vp = findViewById(R.id.vp);
        wrapper = findViewById(R.id.indicator_wrap);
        initVp();
        initWrap();
    }

    private void initWrap() {
        wrapper.bind(vp).setAdapter(new IndicatorAdapter() {

            @Override
            public List<String> getTitle() {
                return Arrays.asList("HomeFragment","SecondFragment","FirstFragment","Fragment","HomeFragment");
            }
        });
    }

    private void initVp() {
        final  List<Fragment> list = Arrays.asList(new SecondFragment(),new SecondFragment(),new FirstFragment(),new LeftMenuFragment(),new SecondFragment());
        vp.setAdapter(new FragmentPagerAdapter(getSupportFragmentManager()) {
            @NonNull
            @Override
            public Fragment getItem(int position) {
                return list.get(position);
            }

            @Override
            public int getCount() {
                return list.size();
            }
        });
    }

}
