package com.hydbest.baseandroid.activity.md;

import android.graphics.Color;
import android.graphics.Outline;
import android.graphics.Paint;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.shapes.RoundRectShape;
import android.os.Build;
import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewOutlineProvider;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;

import com.hydbest.baseandroid.R;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by csz on 2019/5/23.
 */

public class ViewOutlineActivity extends AppCompatActivity implements SeekBar.OnSeekBarChangeListener {

    @BindView(R.id.iv)
    ImageView iv;
    @BindView(R.id.tv)
    TextView tv;

    @BindView(R.id.seek_ele)
    SeekBar seek_ele;
    @BindView(R.id.seek_z)
    SeekBar seek_z;
    @BindView(R.id.seek_coner)
    SeekBar seek_coner;

    @BindView(R.id.v_coner)
    View vConner;


    boolean isStart;
    private int startWidth;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_outline);
        ButterKnife.bind(this);

        seek_ele.setOnSeekBarChangeListener(this);
        seek_z.setOnSeekBarChangeListener(this);
        seek_coner.setOnSeekBarChangeListener(this);
        seek_coner.post(new Runnable() {
            @Override
            public void run() {
                int radius  = vConner.getHeight()/2;
                setDrawable(radius);
            }
        });
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @OnClick({R.id.btn})
    void onClick(View view){
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            iv.setOutlineProvider(new ViewOutlineProvider() {
                @Override
                public void getOutline(View view, Outline outline) {
                    final int margin = Math.min(view.getWidth(), view.getHeight()) / 10;
                    //阴影的透明度
                    outline.setAlpha(0.5f);
                    outline.setRoundRect(margin, margin, view.getWidth() - margin,        view.getHeight() - margin, (view.getWidth() - 2 * margin)/2);
                }
            });
            iv.setClipToOutline(true);
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
        if (seekBar == seek_ele){
            iv.setElevation(progress);
        }else if (seekBar == seek_z) {
            iv.setTranslationZ(progress);
        } else {
            if (!isStart){
                startWidth = vConner.getWidth();
                isStart = true;
            }
            int offset = progress * 200 / seekBar.getMax()  ;
            ViewGroup.LayoutParams params = vConner.getLayoutParams();
            params.width = startWidth + offset;
            vConner.setLayoutParams(params);

            int radius  = vConner.getHeight()/2;
            int outRadius = radius * (seekBar.getMax() - progress) / seekBar.getMax();

            setDrawable(outRadius);
        }
    }

    private void setDrawable(int outRadius){
        float[] outerRadii = {outRadius, outRadius, outRadius, outRadius, outRadius, outRadius, outRadius, outRadius};
        float[] innerRadii = {outRadius, outRadius, outRadius, outRadius, outRadius, outRadius, outRadius, outRadius};
        RoundRectShape roundRectShape = new RoundRectShape(outerRadii, null, innerRadii);
        ShapeDrawable drawable = new ShapeDrawable(roundRectShape);
        drawable.getPaint().setColor(Color.RED);
        drawable.getPaint().setStyle(Paint.Style.FILL);
        vConner.setBackgroundDrawable(drawable);
    }

    @Override
    public void onStartTrackingTouch(SeekBar seekBar) {

    }

    @Override
    public void onStopTrackingTouch(SeekBar seekBar) {

    }
}
