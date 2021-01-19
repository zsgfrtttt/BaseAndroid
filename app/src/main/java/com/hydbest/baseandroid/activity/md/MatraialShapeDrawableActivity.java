package com.hydbest.baseandroid.activity.md;

import android.animation.ValueAnimator;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.ColorStateListDrawable;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.material.animation.AnimationUtils;
import com.google.android.material.shape.MaterialShapeDrawable;
import com.google.android.material.shape.ShapeAppearanceModel;
import com.hydbest.baseandroid.R;
import com.hydbest.baseandroid.drawable.ExMaterialShapeDrawable;
import com.hydbest.baseandroid.view.RadiusTreatment;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserFactory;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.ViewUtils;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.core.view.ViewCompat;

public class MatraialShapeDrawableActivity extends AppCompatActivity {

    private TextView tv;
    private ImageView iv;
    private CoordinatorLayout ly_bot;
    private boolean lefted;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.matrail_shape_drawable);

        initShadow();
    }

    private void initShadow() {
        tv = findViewById(R.id.tv);
        iv = findViewById(R.id.iv);
        ly_bot = findViewById(R.id.ly_bot);

        int size = getResources().getDimensionPixelSize(R.dimen.dp_40);
        ShapeAppearanceModel model = new ShapeAppearanceModel.Builder().setTopEdge(new RadiusTreatment(size)).build();
        MaterialShapeDrawable drawable2 = new MaterialShapeDrawable(model);
        drawable2.setTint(Color.GRAY);
        ViewCompat.setBackground(ly_bot, drawable2);

        Drawable drawable = iv.getBackground();
        if (drawable instanceof ColorDrawable) {
            ColorDrawable background = (ColorDrawable) drawable;
            MaterialShapeDrawable materialShapeDrawable = new ExMaterialShapeDrawable();
            materialShapeDrawable.setFillColor(ColorStateList.valueOf(background.getColor()));
            materialShapeDrawable.initializeElevationOverlay(this);
            //  ViewCompat.setBackground(iv, materialShapeDrawable);
        }
    }

    public void start(View view) {
        startLiftOnScrollElevationOverlayAnimation(lefted = !lefted);
        iv.setSelected(lefted = !lefted);
    }

    private void startLiftOnScrollElevationOverlayAnimation(boolean lifted) {
        ValueAnimator elevationOverlayAnimator = null;
        float appBarElevation = getResources().getDimension(R.dimen.design_appbar_elevation);
        float fromElevation = lifted ? 0 : appBarElevation;
        float toElevation = lifted ? appBarElevation : 0;
        if (elevationOverlayAnimator != null) {
            elevationOverlayAnimator.cancel();
        }
        elevationOverlayAnimator = ValueAnimator.ofFloat(fromElevation, toElevation);
        elevationOverlayAnimator.setDuration(getResources().getInteger(R.integer.app_bar_elevation_anim_duration));
        elevationOverlayAnimator.setInterpolator(AnimationUtils.LINEAR_INTERPOLATOR);
        elevationOverlayAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(@NonNull ValueAnimator valueAnimator) {
                if (Build.VERSION.SDK_INT >= 21) {
                    iv.setElevation((float) valueAnimator.getAnimatedValue());
                } else {
                    @NonNull final MaterialShapeDrawable background = (MaterialShapeDrawable) iv.getBackground();
                    background.setElevation((float) valueAnimator.getAnimatedValue());
                }
            }
        });
        elevationOverlayAnimator.start();
    }

}
