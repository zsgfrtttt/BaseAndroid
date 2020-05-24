package com.hydbest.baseandroid.fragment;


import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.viewpager2.adapter.FragmentStateAdapter;
import androidx.viewpager2.widget.ViewPager2;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;
import com.hydbest.baseandroid.R;
import com.hydbest.baseandroid.databinding.FragmentHomeBinding;
import com.hydbest.baseandroid.fragment.viewpager.PlusOneFragment;
import com.hydbest.baseandroid.fragment.viewpager.PlusThreeFragment;
import com.hydbest.baseandroid.fragment.viewpager.PlusTwoFragment;

import java.io.FileNotFoundException;


/**
 * A simple {@link Fragment} subclass.
 */
public class HomeFragment extends Fragment {


    public HomeFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        FragmentHomeBinding binding = FragmentHomeBinding.inflate(getLayoutInflater());
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ViewPager2 vp  = view.findViewById(R.id.vp);
        TabLayout tabLayout = view.findViewById(R.id.tab);
        final Fragment[] fragments =  new Fragment[]{new PlusOneFragment(),new PlusTwoFragment(),new PlusThreeFragment()};

        vp.setAdapter(new FragmentStateAdapter(requireActivity()) {
            @NonNull
            @Override
            public Fragment createFragment(int position) {
                return fragments[position];
            }

            @Override
            public int getItemCount() {
                return fragments.length;
            }
        });

       new TabLayoutMediator(tabLayout, vp, new TabLayoutMediator.TabConfigurationStrategy() {
            @Override
            public void onConfigureTab(@NonNull TabLayout.Tab tab, int position) {
                switch (position) {
                    case 0:
                        tab.setText("one");
                        break;
                    case 1:
                        tab.setText("two");
                        break;
                    case 2:
                        tab.setText("three");
                        break;
                }

            }
        }).attach();



    }
}
