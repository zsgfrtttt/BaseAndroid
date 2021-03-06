package com.hydbest.baseandroid.fragment.viewpager;


import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.hydbest.baseandroid.R;

import androidx.fragment.app.Fragment;

/**
 * A fragment with a Google +1 button.
 */
public class PlusTwoFragment extends Fragment {

    // The request code must be 0 or greater.
    private static final int PLUS_ONE_REQUEST_CODE = 0;
    // The URL to +1.  Must be a valid URL.
    private final String PLUS_ONE_URL = "http://developer.android.com";
    private Button mPlusOneButton;


    public PlusTwoFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_plus_two, container, false);

        //Find the +1 button
        mPlusOneButton = (Button) view.findViewById(R.id.plus_one_button);

        return view;
    }

}
