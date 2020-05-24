package com.hydbest.baseandroid.activity.md;

import android.animation.Animator;
import android.animation.ObjectAnimator;
import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.LayoutAnimationController;
import android.widget.Button;

import com.chad.library.adapter.base.animation.BaseAnimation;
import com.hydbest.baseandroid.R;
import com.hydbest.baseandroid.adapter.TextAdapter;

import org.jetbrains.annotations.NotNull;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by csz on 2019/5/22.
 */

public class RecycleViewAnimActivity extends AppCompatActivity {
    @BindView(R.id.rv)
    RecyclerView rv;
    @BindView(R.id.btn_reset)
    Button btnReset;

    TextAdapter adapter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recycle_anim);
        ButterKnife.bind(this);
        initView();
    }

    private void initView() {
        adapter = new TextAdapter(null);
        rv.setLayoutManager(new LinearLayoutManager(this));
        rv.setAdapter(adapter);
        Animation animation= AnimationUtils.loadAnimation(this, R.anim.adapter_item_enter);
        LayoutAnimationController lac=new LayoutAnimationController(animation);
        lac.setOrder(LayoutAnimationController.ORDER_NORMAL);
        lac.setDelay(0.1f);
        rv.setLayoutAnimation(lac);
    }

    @OnClick({R.id.btn_reset,R.id.btn_add,R.id.btn_remove})
    public void onClick(View view){
        if (view.getId() == R.id.btn_reset){
            adapter = new TextAdapter(null);
            rv.setAdapter(adapter);
            rv.setAdapter(adapter);
            rv.startLayoutAnimation();
        }else if (view.getId() == R.id.btn_add){
            adapter.addData(new TextAdapter.Bean(adapter.getItemCount() + "",""));
            rv.scrollToPosition(adapter.getItemCount() - 1);
        }else if (view.getId() == R.id.btn_remove){
            adapter.remove(adapter.getItemCount() -1);
        }

    }

    public class ScaleInAnimation implements BaseAnimation {
        private static final float DEFAULT_SCALE_FROM = .5f;
        private final float mFrom;

        public ScaleInAnimation() {
            this(DEFAULT_SCALE_FROM);
        }

        public ScaleInAnimation(float from) {
            mFrom = from;
        }

        @NotNull
        @Override
        public Animator[] animators(@NotNull View view) {
            ObjectAnimator scaleX = ObjectAnimator.ofFloat(view, "scaleX", mFrom, 1f);
            ObjectAnimator scaleY = ObjectAnimator.ofFloat(view, "scaleY", mFrom, 1f);
            return new ObjectAnimator[]{scaleX, scaleY};
        }
    }
}
