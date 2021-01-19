package com.hydbest.baseandroid;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;

import com.hydbest.baseandroid.fragment.HomeFragmentArgs;

public class HomeSkipFragment extends Fragment {

    public HomeSkipFragment() { }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            HomeFragmentArgs homeFragmentArgs = HomeFragmentArgs.fromBundle(getArguments());
            int age = homeFragmentArgs.getAge();
            String name = homeFragmentArgs.getName();
            Log.i("csz","tag   "  + age   +"    " + name);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        setHasOptionsMenu(true);
        return inflater.inflate(R.layout.fragment_home_skip, container, false);
    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.menu_skip,menu);
    }
}