package com.hydbest.baseandroid.fragment;

import android.app.Application;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.hydbest.baseandroid.R;
import com.hydbest.baseandroid.databinding.FragmentFirstBinding;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.SavedStateHandle;
import androidx.lifecycle.SavedStateViewModelFactory;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.NavigationUI;


public class FirstFragment extends Fragment {

    private FragmentFirstBinding binding;
    private FirstViewModel model;

    public FirstFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        model = new ViewModelProvider(this,new SavedStateViewModelFactory(getActivity().getApplication(),this)).get(FirstViewModel.class);
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_first, container, false);
        binding.setModel(model);
        binding.setLifecycleOwner(this);
        return binding.getRoot();
    }


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        binding.btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                model.Skip(v);
            }
        });
        try {
            NavController controller = Navigation.findNavController(binding.root);
            //   AppBarConfiguration configuration = new AppBarConfiguration.Builder(controller.getGraph()).build();
            //    NavigationUI.setupActionBarWithNavController((AppCompatActivity) requireActivity(),controller,configuration);
            NavigationUI.setupWithNavController(binding.navi,controller);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static final class FirstViewModel extends AndroidViewModel{

        private SavedStateHandle handle;

        public FirstViewModel(@NonNull Application application, SavedStateHandle handle) {
            super(application);
            this.handle = handle;
        }

        public void Skip(View view){
            Navigation.findNavController(view).navigate(R.id.action_firstFragment_to_secondFragment);
        }
    }
}
