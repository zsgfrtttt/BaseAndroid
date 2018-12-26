package com.hydbest.baseandroid.activity.cus_viewgroup;

import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.hydbest.baseandroid.R;
import com.hydbest.baseandroid.view.transformer.AlphaPageTransformer;
import com.hydbest.baseandroid.view.transformer.RotateDownPageTransformer;
import com.hydbest.baseandroid.view.transformer.RotateUpPageTransformer;
import com.hydbest.baseandroid.view.transformer.RotateYTransformer;
import com.hydbest.baseandroid.view.transformer.ScaleInTransformer;

/**
 * Created by csz on 2018/7/24.
 */

public class ViewpageActivity extends AppCompatActivity {
    private ViewPager mViewPager;
    private PagerAdapter mAdapter;

    int[] imgRes = {R.mipmap.a, R.mipmap.b, R.mipmap.c, R.mipmap.d};
    ViewPager.PageTransformer[] mTransformer = new ViewPager.PageTransformer[]{new AlphaPageTransformer(0.1f,new RotateUpPageTransformer()), new RotateDownPageTransformer()
            , new RotateUpPageTransformer(), new RotateYTransformer(), new ScaleInTransformer()};
    int mIndex = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_viewpage);

        mViewPager = (ViewPager) findViewById(R.id.id_viewpager);
        //设置Page间间距
        mViewPager.setPageMargin(20);
        //设置缓存的页面数量
        mViewPager.setOffscreenPageLimit(3);
        mViewPager.setPageTransformer(false, new RotateDownPageTransformer());
        mViewPager.setAdapter(mAdapter = new PagerAdapter() {
            @Override
            public Object instantiateItem(ViewGroup container, int position) {
                ImageView view = new ImageView(ViewpageActivity.this);
                view.setScaleType(ImageView.ScaleType.CENTER_INSIDE);
                view.setImageResource(imgRes[position]);
                container.addView(view, ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
                return view;
            }

            @Override
            public void destroyItem(ViewGroup container, int position, Object object) {
                container.removeView((View) object);
            }

            @Override
            public int getCount() {
                return imgRes.length;
            }

            @Override
            public boolean isViewFromObject(View view, Object o) {
                return view == o;
            }
        });

    }

    public void change(View view) {
        mViewPager.setPageTransformer(false, mTransformer[mIndex]);
        mViewPager.setAdapter(mAdapter);
        ((TextView) view).setText(mTransformer[mIndex].getClass().getSimpleName());
        mIndex++;
        if (mIndex == mTransformer.length)
            mIndex = 0;
    }
}
