package com.hydbest.baseandroid.fragment;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SeekBar;

import com.hydbest.baseandroid.R;
import com.hydbest.baseandroid.activity.jetpack.LiveViewModelActivity;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.SavedStateViewModelFactory;
import androidx.lifecycle.ViewModelProvider;

public class FragmentTop extends Fragment implements SeekBar.OnSeekBarChangeListener {

    private SeekBar seekBar;
    private LiveViewModelActivity.FragmentViewModel model;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate( R.layout.fragment_fragment_top,container,false);
    }

    @SuppressLint("FragmentLiveDataObserve")
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        seekBar = view.findViewById(R.id.seek);
        seekBar.setOnSeekBarChangeListener(this);
        model = new ViewModelProvider(getActivity(), new SavedStateViewModelFactory(getActivity().getApplication(), this)).get(LiveViewModelActivity.FragmentViewModel.class);

        MutableLiveData<Integer> liveData = model.getData();
        liveData.observe(this, new Observer<Integer>() {
            @Override
            public void onChanged(Integer integer) {
                seekBar.setProgress(integer);
            }
        });
        Log.i("csz","FragmentTop    " + model);
    }

    @Override
    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
        model.getData().setValue(progress);
    }

    @Override
    public void onStartTrackingTouch(SeekBar seekBar) {

    }

    @Override
    public void onStopTrackingTouch(SeekBar seekBar) {

    }
}
