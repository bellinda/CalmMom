package com.angelova.w510.calmmom.fragments;

import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.constraint.ConstraintLayout;
import android.support.constraint.ConstraintSet;
import android.support.v4.app.Fragment;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.angelova.w510.calmmom.KicksAndContractionsActivity;
import com.angelova.w510.calmmom.R;
import com.angelova.w510.calmmom.adapters.KicksAdapter;
import com.angelova.w510.calmmom.models.Kick;
import com.angelova.w510.calmmom.models.User;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import pl.bclogic.pulsator4droid.library.PulsatorLayout;

/**
 * Created by W510 on 24.8.2018 г..
 */

public class KicksFragment extends Fragment {

    private ConstraintLayout mConstraintLayout;
    private CardView mCardView;
    private Button mCountBtn;
    private LinearLayout mKickBtn;
    private TextView mCountText;
    private PulsatorLayout mPulsator;
    private RecyclerView mRecyclerView;
    private KicksAdapter mAdapter;
    private List<Kick> mDataList = new ArrayList<>();
    private long startTimeMilliseconds;
    private String currentDate;
    private int counter = 0;

    private boolean isCounting = false;

    private User mUser;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_kicks, container, false);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = getActivity().getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(Color.parseColor("#324A5F"));
        }

        mUser = (User) getArguments().getSerializable("user");

        SimpleDateFormat sdf = new SimpleDateFormat("dd MMM yyyy");
        currentDate = sdf.format(Calendar.getInstance().getTime());

        mConstraintLayout = (ConstraintLayout) rootView.findViewById(R.id.constraint_layout);
        mCardView = (CardView) rootView.findViewById(R.id.card_view);
        mCountBtn = (Button) rootView.findViewById(R.id.counting_btn);
        mKickBtn = (LinearLayout) rootView.findViewById(R.id.kick_btn);
        mCountText = (TextView) rootView.findViewById(R.id.counter_text);
        mRecyclerView = (RecyclerView) rootView.findViewById(R.id.kicks_list);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false));
        mRecyclerView.setHasFixedSize(true);
        if (mUser.getPregnancies().get(mUser.getPregnancyConsecutiveId()).getKicks() != null) {
            mDataList.addAll(mUser.getPregnancies().get(mUser.getPregnancyConsecutiveId()).getKicks());
        }
        mAdapter = new KicksAdapter(mDataList, getActivity());
        mRecyclerView.setAdapter(mAdapter);

        mPulsator = (PulsatorLayout) rootView.findViewById(R.id.pulsator);

        mCountBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isCounting) {
                    if (counter != 0) {
                        saveKickItem();
                    }

                    mPulsator.setVisibility(View.GONE);
                    mKickBtn.setVisibility(View.GONE);
                    mCountText.setVisibility(View.GONE);
                    mCountBtn.setText(getString(R.string.fragment_kicks_start));
                    isCounting = false;
                    counter = 0;
                    mCountText.setText("0/10");

                    ConstraintSet constraintSet = new ConstraintSet();
                    constraintSet.clone(mConstraintLayout);
                    constraintSet.connect(mCountBtn.getId(), ConstraintSet.RIGHT, R.id.large_btn_right_guideline,ConstraintSet.LEFT);
                    constraintSet.connect(mCountBtn.getId(), ConstraintSet.LEFT, R.id.large_btn_guideline,ConstraintSet.RIGHT);
                    constraintSet.connect(mCountBtn.getId(), ConstraintSet.TOP, R.id.large_btn_top_guideline,ConstraintSet.BOTTOM);
                    constraintSet.connect(mCountBtn.getId(), ConstraintSet.BOTTOM, R.id.large_btn_bottom_guideline,ConstraintSet.TOP);

                    constraintSet.connect(mCardView.getId(), ConstraintSet.RIGHT, R.id.card_right_guideline,ConstraintSet.LEFT);
                    constraintSet.connect(mCardView.getId(), ConstraintSet.LEFT, R.id.card_left_guideline,ConstraintSet.RIGHT);
                    constraintSet.connect(mCardView.getId(), ConstraintSet.TOP, R.id.card_top_guideline,ConstraintSet.BOTTOM);
                    constraintSet.connect(mCardView.getId(), ConstraintSet.BOTTOM, R.id.card_bottom_guideline,ConstraintSet.TOP);
                    constraintSet.applyTo(mConstraintLayout);

                    mCountBtn.setTextSize(18);
                } else {
                    if (!mPulsator.isStarted()) {
                        mPulsator.start();
                    }
                    mPulsator.setVisibility(View.VISIBLE);
                    mKickBtn.setVisibility(View.VISIBLE);
                    mCountText.setVisibility(View.VISIBLE);
                    mCountBtn.setText(getString(R.string.fragment_kicks_stop));
                    startTimeMilliseconds = Calendar.getInstance().getTimeInMillis();
                    isCounting = true;

                    ConstraintSet constraintSet = new ConstraintSet();
                    constraintSet.clone(mConstraintLayout);
                    constraintSet.connect(mCountBtn.getId(), ConstraintSet.RIGHT, R.id.btn_right_guideline,ConstraintSet.LEFT);
                    constraintSet.connect(mCountBtn.getId(), ConstraintSet.LEFT, R.id.btn_guideline,ConstraintSet.RIGHT);
                    constraintSet.connect(mCountBtn.getId(), ConstraintSet.TOP, R.id.btn_top_guideline,ConstraintSet.BOTTOM);
                    constraintSet.connect(mCountBtn.getId(), ConstraintSet.BOTTOM, R.id.btn_bottom_guideline,ConstraintSet.TOP);

                    constraintSet.connect(mCardView.getId(), ConstraintSet.RIGHT, R.id.card_right_guideline,ConstraintSet.LEFT);
                    constraintSet.connect(mCardView.getId(), ConstraintSet.LEFT, R.id.card_left_guideline,ConstraintSet.RIGHT);
                    constraintSet.connect(mCardView.getId(), ConstraintSet.TOP, R.id.card_top_guideline,ConstraintSet.BOTTOM);
                    constraintSet.connect(mCardView.getId(), ConstraintSet.BOTTOM, R.id.card_bottom_guideline,ConstraintSet.TOP);
                    constraintSet.applyTo(mConstraintLayout);

                    mCountBtn.setTextSize(16);
                }
            }
        });

        mKickBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                counter++;
                mCountText.setText(counter + "/10");
                if (counter == 10) {
                    saveKickItem();
                    counter = 0;
                    mCountText.setText("0/10");
                    startTimeMilliseconds = Calendar.getInstance().getTimeInMillis();
                }
            }
        });

        return rootView;
    }

    private void saveKickItem() {
        long endTimeInMilliseconds = Calendar.getInstance().getTimeInMillis();
        String timeInMinutes = getTimeInMinutes(startTimeMilliseconds, endTimeInMilliseconds);
        System.out.println("TIME = " + timeInMinutes);
        Kick kick = new Kick(currentDate, timeInMinutes, counter);
        mDataList.add(0, kick);
        mAdapter.notifyDataSetChanged();

        ((KicksAndContractionsActivity)getActivity()).updateKicksInDb(mDataList);

        mUser.getPregnancies().get(mUser.getPregnancyConsecutiveId()).setKicks(mDataList);
    }

    private String getTimeInMinutes(long startTime, long endTime) {
        long diff = endTime - startTime;
        int timeInSeconds = (int) (diff / 1000);
        int minutes = timeInSeconds / 60;
        int seconds = timeInSeconds % 60;
        return String.format("%02d:%02d min", minutes, seconds);
    }
}
