package com.hydbest.baseandroid.activity.jetpack;

import android.app.Application;
import android.os.Bundle;
import android.util.Log;

import com.hydbest.baseandroid.R;
import com.hydbest.baseandroid.databinding.ActivityLiveViewmodelBinding;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.LiveDataKt;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.SavedStateHandle;
import androidx.lifecycle.SavedStateViewModelFactory;
import androidx.lifecycle.ViewModelProvider;

public class LiveViewModelActivity extends AppCompatActivity {


    private LiveViewModel model;

    private ActivityLiveViewmodelBinding binding;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this,R.layout.activity_live_viewmodel);
        model = new ViewModelProvider(this,new SavedStateViewModelFactory(getApplication(),this)).get(LiveViewModel.class);
        binding.setModel(model);
        binding.setLifecycleOwner(this);

        model.getData().observe(this, new Observer<Integer>() {
            @Override
            public void onChanged(Integer i) {
                Log.i("csz","onChanged    " + i);
            }
        });
    }

    public final static class LiveViewModel extends AndroidViewModel {
        private static final String KEY = "key1";
        private SavedStateHandle handle;

        public LiveViewModel(@NonNull Application application,SavedStateHandle handle) {
            super(application);
            this.handle = handle;
        }

        public MutableLiveData<Integer> getData(){
            return handle.getLiveData(KEY,0);
        }

        public void interval(){
            new Thread(new Runnable() {
                @Override
                public void run() {
                    while (true){
                        add();
                        try {
                            Thread.sleep(1000);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                }
            }).start();
        }

        public void add(){
            //非ui thread
            getData().postValue(getData().getValue() + 1);
        }

        public void less(){
            getData().setValue(getData().getValue() - 1);
        }

        @Override
        protected void onCleared() { }
    }

    public final static class FragmentViewModel extends AndroidViewModel {
        private static final String KEY = "key1";
        private SavedStateHandle handle;

        public FragmentViewModel(@NonNull Application application,SavedStateHandle handle) {
            super(application);
            this.handle = handle;
        }

        public MutableLiveData<Integer> getData(){
            return handle.getLiveData(KEY,0);
        }

    }
}
