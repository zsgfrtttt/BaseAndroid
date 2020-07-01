package com.hydbest.baseandroid.activity.cus_viewgroup;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.TextView;

import com.hydbest.baseandroid.R;
import com.hydbest.baseandroid.fragment.LeftMenuFragment;
import com.hydbest.baseandroid.view.viewdraghelper.DrawlerLayout;
import com.hydbest.baseandroid.view.viewgroup.Container;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.customview.widget.ViewDragHelper;
import androidx.fragment.app.FragmentManager;

public class LeftDrawerLayoutActivity extends AppCompatActivity {

    private LeftMenuFragment mMenuFragment;
    private DrawlerLayout mLeftDrawerLayout;
    private TextView mContentTv;

    private android.widget.ImageView mIvDrag;
    private Container mContainerView;
    private ViewDragHelper mHelper;


    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_left_drawer_layout);

        mLeftDrawerLayout = findViewById(R.id.id_drawerlayout);
        mContentTv = findViewById(R.id.id_content_tv);
        mIvDrag = findViewById(R.id.iv_drag);
        mContainerView = findViewById(R.id.layout_container);

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

        mIvDrag.post(new Runnable() {
            @Override
            public void run() {
                mHelper = ViewDragHelper.create(mContainerView, 1.f, new DragCallBack());
                mContainerView.setHelper(mHelper);
            }
        });

        mIvDrag.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_DOWN){
                    Log.i("csz","ev:dowb");
                }else if (event.getAction() == MotionEvent.ACTION_MOVE){
                    Log.i("csz","ev:move");
                }else if (event.getAction() == MotionEvent.ACTION_UP){
                    Log.i("csz","ev:up");
                }

                mHelper.processTouchEvent(event);
                return true;
            }
        });
    }

    private class DragCallBack extends ViewDragHelper.Callback {

        @Override
        public boolean  tryCaptureView(@NonNull View view, int i) {
            boolean flag = view == mIvDrag;
            Log.i("csz",view.getClass().getName()+"   iii   "+flag);
            return flag;
        }

        @Override  //控制child在水平方向上的位置，不限制边界返回left即可
        public int clampViewPositionHorizontal(View child, int left, int dx) {
            if (left < 0) {
                //限制左边界
                left = 0;
            } else if (left > (mContainerView.getMeasuredWidth() - child.getMeasuredWidth())) {
                //限制右边界
                left = mContainerView.getMeasuredWidth() - child.getMeasuredWidth();
            }
            return left;
        }

        @Override  //控制child在竖直方向上的位置，不限制边界返回top即可
        public int clampViewPositionVertical(View child, int top, int dy) {
            if (top < 0) {
                //限制上边界
                top = 0;
            } else if (top > (mContainerView.getMeasuredHeight() - child.getMeasuredHeight())) {
                //限制下边界
                top = mContainerView.getMeasuredHeight() - child.getMeasuredHeight();
            }
            return top;
        }

        @Override  //不重写该方法默认返回0，返回0时若还设置了点击事件则水平方向不能移动
        public int getViewHorizontalDragRange(View child) {
            return mContainerView.getMeasuredWidth() - child.getMeasuredWidth();
        }

        @Override  //不重写该方法默认返回0，返回0时若还设置了点击事件则竖直方向不能移动
        public int getViewVerticalDragRange(View child) {
            return mContainerView.getMeasuredHeight() - child.getMeasuredHeight();
        }

        @Override
        public void onViewPositionChanged(@NonNull View changedView, int left, int top, int dx, int dy) {
            super.onViewPositionChanged(changedView, left, top, dx, dy);
        }

        @Override   //处理手指释放的事件
        public void onViewReleased(View releasedChild, float xvel, float yvel) {
            super.onViewReleased(releasedChild, xvel, yvel);
            Log.i("csz","release");
            if (releasedChild == mIvDrag) {
                final int childWidth = releasedChild.getWidth();
                float offset = (childWidth + releasedChild.getLeft()) * 1.0f / childWidth;
                mHelper.settleCapturedViewAt(xvel > 0 || xvel == 0 && offset > 0.5f ? 0 : mContainerView.getWidth()-mIvDrag.getWidth(), releasedChild.getTop());
               mContainerView.invalidate();
//                mHelper.smoothSlideViewTo(releasedChild, mContainerView.getMeasuredWidth() - releasedChild.getMeasuredWidth(), releasedChild.getTop()); //平滑移动到最右边
//                ViewCompat.postInvalidateOnAnimation(mContainerView); //刷新布局
            }
        }

    }


}
