package com.hydbest.baseandroid.fragment.viewpager;


import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.hydbest.baseandroid.R;
import com.hydbest.baseandroid.fragment.HomeFragmentArgs;

import androidx.annotation.NonNull;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

/**
 * A fragment with a Google +1 button.
 */
public class PlusOneFragment extends Fragment implements View.OnClickListener {

    // The request code must be 0 or greater.
    private static final int PLUS_ONE_REQUEST_CODE = 0;
    // The URL to +1.  Must be a valid URL.
    private final String PLUS_ONE_URL = "http://developer.android.com";
    private Button mPlusOneButton;

    public PlusOneFragment() { }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_plus_one, container, false);
        mPlusOneButton = (Button) view.findViewById(R.id.plus_one_button);
        mPlusOneButton.setOnClickListener(this);

        view.findViewById(R.id.deep).setOnClickListener(this);
        return view;
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.plus_one_button){
            HomeFragmentArgs args = new HomeFragmentArgs.Builder().setAge(10).setName("caishuying").build();
            Navigation.findNavController(v).navigate(R.id.action_homeFragment_to_homeSkipFragment,args.toBundle());
          //  Navigation.createNavigateOnClickListener(R.id.action_homeFragment_to_homeSkipFragment);
        } else if (v.getId() == R.id.deep){
            deeplinkByNotification();
        }
    }

    private void deeplinkByNotification() {
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel("id","test", NotificationManager.IMPORTANCE_DEFAULT);
            channel.setDescription("desc");
            NotificationManager systemService = getActivity().getSystemService(NotificationManager.class);
            systemService.createNotificationChannel(channel);
        }

        NotificationCompat.Builder builder = new NotificationCompat.Builder(getContext(),"id")
                .setContentTitle("deeplink_title")
                .setContentText("deeplink_text")
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                .setContentIntent(getPendingIntent())
                .setSmallIcon(R.drawable.icon_full);
        NotificationManagerCompat.from(getContext()).notify(1,builder.build());
    }

    private PendingIntent getPendingIntent() {
        Bundle bundle = new Bundle();
        bundle.putString("name","this is param name");
        bundle.putInt("age",908);
        return Navigation.findNavController(getActivity(),R.id.deep)
                .createDeepLink()
                .setGraph(R.navigation.navigation_home)
                .setDestination(R.id.homeSkipFragment)
                .setArguments(bundle)
                .createPendingIntent();
    }

}
