package com.hydbest.baseandroid.activity.foundation;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.gifdecoder.GifDecoder;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.load.resource.gif.GifDrawable;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.Target;
import com.hydbest.baseandroid.R;
import com.hydbest.baseandroid.activity.other.AndroidAdvanceActivity;

import java.lang.reflect.Field;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by csz on 2018/11/22.
 */

public class GifActivity extends AppCompatActivity {

    @BindView(R.id.iv)
    ImageView mIv;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gif);
        ButterKnife.bind(this);

        loadGif();
    }

    private void loadGif() {
        Glide.with(this).load(R.drawable.icon_gif).apply(new RequestOptions().diskCacheStrategy(DiskCacheStrategy.NONE)).listener(new RequestListener<Drawable>() {
            @Override
            public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                return false;
            }

            @Override
            public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                // 计算动画时长
                GifDrawable drawable = (GifDrawable) resource;
                ((GifDrawable) resource).setLoopCount(1);
                Class<GifDrawable> gifDrawableClass = (Class<GifDrawable>) resource.getClass();
                try {
                    Field stateField = gifDrawableClass.getDeclaredField("state");
                    stateField.setAccessible(true);
                    Class gifStateClass = stateField.get(drawable).getClass();

                    Field frameLoaderField = gifStateClass.getDeclaredField("frameLoader");
                    frameLoaderField.setAccessible(true);
                    Object frameLoaderObj = frameLoaderField.get(stateField.get(drawable));

                    Field gifDecoder = frameLoaderObj.getClass().getDeclaredField("gifDecoder");
                    gifDecoder.setAccessible(true);
                    GifDecoder decoder = (GifDecoder) gifDecoder.get(frameLoaderObj);

                    //  GifDecoder decoder = drawable.startFromFirstFrame();
                    long duration = 0;
                    for (int i = 0; i < drawable.getFrameCount(); i++) {
                        duration += decoder.getDelay(i);
                    }

                    new Handler(Looper.getMainLooper()).postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            startActivity(new Intent(GifActivity.this,AndroidAdvanceActivity.class));
                        }
                    },duration);
                } catch (NoSuchFieldException e) {
                    e.printStackTrace();
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                }

                return false;
            }

        }).into(mIv);
    }
}
