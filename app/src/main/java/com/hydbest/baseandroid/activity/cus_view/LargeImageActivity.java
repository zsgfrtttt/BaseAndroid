
package com.hydbest.baseandroid.activity.cus_view;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.hydbest.baseandroid.R;
import com.hydbest.baseandroid.view.view.LargeImageView;

import java.io.IOException;
import java.io.InputStream;

public class LargeImageActivity extends AppCompatActivity {
    private LargeImageView mLargeImageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_large_img);

        mLargeImageView = (LargeImageView) findViewById(R.id.id_largetImageview);
        try {

            InputStream inputStream = getAssets().open("qm.jpg");
            mLargeImageView.setInputStream(inputStream);
        } catch (IOException e) {
            e.printStackTrace();
        }


    }
}

