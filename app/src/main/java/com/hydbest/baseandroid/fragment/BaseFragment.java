package com.hydbest.baseandroid.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;

/**
 * Created by csz on 2018/8/7.
 */

public class BaseFragment extends Fragment{
    private static final String STATE_SAVE_IS_HIDDEN = "STATE_SAVE_IS_HIDDEN";

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //回复Fragment的可见状态
        if (savedInstanceState != null) {
            boolean isSupportHidden = savedInstanceState.getBoolean(STATE_SAVE_IS_HIDDEN);
            FragmentTransaction ft = getFragmentManager().beginTransaction();
            if (isSupportHidden) {
                ft.hide(this);
            } else {
                ft.show(this);
            }
            ft.commit();
        }

        /**
         * 24.0.0+已修复
         * @See Activity#OnCreate
         * <p>在activity启动时调用，避免内存重启导致加载重复的fragment</p>
         */
        /*TargetFragment targetFragment;
        HideFragment hideFragment;

        if (savedInstanceState != null) {  // “内存重启”时调用
            targetFragment = getFragmentManager().findFragmentByTag(TargetFragment.class.getName);
            hideFragment = getFragmentManager().findFragmentByTag(HideFragment.class.getName);
            // 解决重叠问题
            getFragmentManager().beginTransaction()
                    .show(targetFragment)
                    .hide(hideFragment)
                    .commit();
        }else{  // 正常时
            targetFragment = TargetFragment.newInstance();
            hideFragment = HideFragment.newInstance();

            getFragmentManager().beginTransaction()
                    .add(R.id.container, targetFragment, targetFragment.getClass().getName())
                    .add(R.id,container,hideFragment,hideFragment.getClass().getName())
                    .hide(hideFragment)
                    .commit();
        }*/

    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putBoolean(STATE_SAVE_IS_HIDDEN, isHidden());
    }
}
