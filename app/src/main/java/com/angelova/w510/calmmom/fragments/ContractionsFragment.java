package com.angelova.w510.calmmom.fragments;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.angelova.w510.calmmom.R;
import com.angelova.w510.calmmom.services.StopwatchService;
import com.github.ybq.android.spinkit.SpinKitView;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by W510 on 24.8.2018 г..
 */

public class ContractionsFragment extends Fragment {

    private TextView mTimerView;
    private SpinKitView mTimerAnimation;
    private Button mStartTimerBtn;
    private LinearLayout mSecondaryButtonsLayout;
    private Button mStopTimerBtn;
    private Button mContractionsBtn;

    SharedPreferences mPrefs;
    SharedPreferences.Editor mEditor;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_contractions, container, false);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = getActivity().getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(Color.parseColor("#324A5F"));
        }

        mTimerView = (TextView) rootView.findViewById(R.id.timer_view);

        mPrefs = PreferenceManager.getDefaultSharedPreferences(getActivity());
        mEditor = mPrefs.edit();

        mTimerAnimation = (SpinKitView) rootView.findViewById(R.id.timer_animation);
        mStartTimerBtn = (Button) rootView.findViewById(R.id.start_timer_btn);
        mSecondaryButtonsLayout = (LinearLayout) rootView.findViewById(R.id.secondary_button_layout);
        mStopTimerBtn = (Button) rootView.findViewById(R.id.stop_timer_btn);
        mContractionsBtn = (Button) rootView.findViewById(R.id.contraction_btn);

        mStartTimerBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mTimerAnimation.setVisibility(View.VISIBLE);
                mStartTimerBtn.setVisibility(View.GONE);
                mSecondaryButtonsLayout.setVisibility(View.VISIBLE);
                startTimer();
            }
        });

        mStopTimerBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mStartTimerBtn.setVisibility(View.VISIBLE);
                mTimerAnimation.setVisibility(View.GONE);
                mSecondaryButtonsLayout.setVisibility(View.GONE);
                mTimerView.setText("00:00:00");
                stopTimer();
            }
        });

        return rootView;
    }

    private void startTimer() {
        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("HH:mm:ss.SSS");

        String startTime = simpleDateFormat.format(calendar.getTime());
        mEditor.putString("data", startTime).commit();


        Intent intent_service = new Intent(getActivity(), StopwatchService.class);
        getActivity().startService(intent_service);
    }

    private void stopTimer() {
        Intent intent = new Intent(getActivity(), StopwatchService.class);
        getActivity().stopService(intent);

        mEditor.clear().commit();
    }

    private BroadcastReceiver broadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String time = intent.getStringExtra("time");
            mTimerView.setText(time);
        }
    };

    @Override
    public void onResume() {
        super.onResume();
        getActivity().registerReceiver(broadcastReceiver,new IntentFilter(StopwatchService.serviceReceiver));

    }

    @Override
    public void onPause() {
        super.onPause();
        getActivity().unregisterReceiver(broadcastReceiver);
    }
}
