package com.angelova.w510.calmmom.fragments;

import android.app.Activity;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.angelova.w510.calmmom.HealthStateActivity;
import com.angelova.w510.calmmom.R;
import com.angelova.w510.calmmom.chart_customizations.CustomMarkerView;
import com.angelova.w510.calmmom.dialogs.AddActivityDialog;
import com.angelova.w510.calmmom.models.ActivityType;
import com.angelova.w510.calmmom.models.User;
import com.angelova.w510.calmmom.models.UserActivity;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.formatter.IAxisValueFormatter;
import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.listener.OnChartValueSelectedListener;
import com.melnykov.fab.FloatingActionButton;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 * Created by W510 on 4.8.2018 г..
 */

public class ActivitiesFragment extends Fragment implements OnChartValueSelectedListener {

    private BarChart mChart;
    private FloatingActionButton mAddBtn;

    private ScrollView mActivitiesScroll;
    private View mWalkingBar;
    private TextView mWalkingDuration;
    private LinearLayout mWalkingBarLayout;
    private View mAerobicsBar;
    private TextView mAerobicsDuration;
    private LinearLayout mAerobicsBarLayout;
    private View mYogaBar;
    private TextView mYogaDuration;
    private LinearLayout mYogaBarLayout;
    private View mPilatesBar;
    private TextView mPilatesDuration;
    private LinearLayout mPilatesBarLayout;
    private View mSwimmingBar;
    private TextView mSwimmingDuration;
    private LinearLayout mSwimmingBarLayout;
    private View mDancingBar;
    private TextView mDancingDuration;
    private LinearLayout mDancingBarLayout;
    private View mSCBar;
    private TextView mSCDuration;
    private LinearLayout mSCBarLayout;
    private View mJoggingBar;
    private TextView mJoggingDuration;
    private LinearLayout mJoggingBarLayout;
    private View mOtherBar;
    private TextView mOtherDuration;
    private LinearLayout mOtherBarLayout;

    private User mUser;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_activities, container, false);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = getActivity().getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(Color.parseColor("#324A5F"));
        }
        mUser = (User) getArguments().getSerializable("user");

        mChart = (BarChart) rootView.findViewById(R.id.bar_chart_activities);
        mAddBtn = (FloatingActionButton) rootView.findViewById(R.id.add_activity_btn);
        mActivitiesScroll = (ScrollView) rootView.findViewById(R.id.activities_scroll);
        mWalkingBar = rootView.findViewById(R.id.walking_bar);
        mWalkingDuration = (TextView) rootView.findViewById(R.id.walking_duration);
        mWalkingBarLayout = (LinearLayout) rootView.findViewById(R.id.walking_bar_layout);
        mAerobicsBar = rootView.findViewById(R.id.aerobics_bar);
        mAerobicsDuration = (TextView) rootView.findViewById(R.id.aerobics_duration);
        mAerobicsBarLayout = (LinearLayout) rootView.findViewById(R.id.aerobics_bar_layout);
        mYogaBar = rootView.findViewById(R.id.yoga_bar);
        mYogaDuration = (TextView) rootView.findViewById(R.id.yoga_duration);
        mYogaBarLayout = (LinearLayout) rootView.findViewById(R.id.yoga_bar_layout);
        mPilatesBar = rootView.findViewById(R.id.pilates_bar);
        mPilatesDuration = (TextView) rootView.findViewById(R.id.pilates_duration);
        mPilatesBarLayout = (LinearLayout) rootView.findViewById(R.id.pilates_bar_layout);
        mSwimmingBar = rootView.findViewById(R.id.swimming_bar);
        mSwimmingDuration = (TextView) rootView.findViewById(R.id.swimming_duration);
        mSwimmingBarLayout = (LinearLayout) rootView.findViewById(R.id.swimming_bar_layout);
        mDancingBar = rootView.findViewById(R.id.dancing_bar);
        mDancingDuration = (TextView) rootView.findViewById(R.id.dancing_duration);
        mDancingBarLayout = (LinearLayout) rootView.findViewById(R.id.dancing_bar_layout);
        mSCBar = rootView.findViewById(R.id.sc_bar);
        mSCDuration = (TextView) rootView.findViewById(R.id.sc_duration);
        mSCBarLayout = (LinearLayout) rootView.findViewById(R.id.sc_bar_layout);
        mJoggingBar = rootView.findViewById(R.id.jogging_bar);
        mJoggingDuration = (TextView) rootView.findViewById(R.id.jogging_duration);
        mJoggingBarLayout = (LinearLayout) rootView.findViewById(R.id.jogging_bar_layout);
        mOtherBar = rootView.findViewById(R.id.other_bar);
        mOtherDuration = (TextView) rootView.findViewById(R.id.other_duration);
        mOtherBarLayout = (LinearLayout) rootView.findViewById(R.id.other_bar_layout);

        final List<String> labels = new ArrayList<>();
        for (int i = 0; i <= 40; i++) {
            labels.add(String.format("%s", i));
        }

//        ArrayList<BarEntry> entries = new ArrayList<>();
//        entries.add(new BarEntry(1f, 80));
//        entries.add(new BarEntry(2f, 20));
//        entries.add(new BarEntry(3f, 15));
//        entries.add(new BarEntry(4f, 40));
//        entries.add(new BarEntry(5f, 60));
//        entries.add(new BarEntry(6f, 10));
//        entries.add(new BarEntry(7f, 0));
//        entries.add(new BarEntry(8f, 0));
//        entries.add(new BarEntry(9f, 0));
//        entries.add(new BarEntry(10f, 0));
//        entries.add(new BarEntry(11f, 0));
//        entries.add(new BarEntry(12f, 0));
//        entries.add(new BarEntry(13f, 0));
//        entries.add(new BarEntry(14f, 80));
//        entries.add(new BarEntry(15f, 0));
//        entries.add(new BarEntry(16f, 0));
//        entries.add(new BarEntry(17f, 0));
//        entries.add(new BarEntry(18f, 0));
//        entries.add(new BarEntry(19f, 0));
//        entries.add(new BarEntry(20f, 0));
//        entries.add(new BarEntry(21f, 0));
//        entries.add(new BarEntry(22f, 0));
//        entries.add(new BarEntry(23f, 0));
//        entries.add(new BarEntry(24f, 0));
//        entries.add(new BarEntry(25f, 0));
//        entries.add(new BarEntry(26f, 0));
//        entries.add(new BarEntry(27f, 0));
//        entries.add(new BarEntry(28f, 0));
//        entries.add(new BarEntry(29f, 0));
//        entries.add(new BarEntry(30f, 0));
//        entries.add(new BarEntry(31f, 0));
//        entries.add(new BarEntry(32f, 0));
//        entries.add(new BarEntry(33f, 0));
//        entries.add(new BarEntry(34f, 0));
//        entries.add(new BarEntry(35f, 0));
//        entries.add(new BarEntry(36f, 0));
//        entries.add(new BarEntry(37f, 0));
//        entries.add(new BarEntry(38f, 0));
//        entries.add(new BarEntry(39f, 0));
//        entries.add(new BarEntry(40f, 0));

        ArrayList<BarEntry> entries = getBarEntriesFromUserActivities();
        Collections.sort(entries, new Comparator<BarEntry>() {
            @Override
            public int compare(BarEntry o1, BarEntry o2) {
                if (o1.getX() > o2.getX()) {
                    return 1;
                } else if (o1.getX() < o2.getX()) {
                    return -1;
                } else {
                    return 0;
                }
            }
        });

        BarDataSet dataSet = new BarDataSet(entries, "Activity Duration");
        dataSet.setColor(ContextCompat.getColor(getActivity(), R.color.colorPrimaryTransparent));
        dataSet.setDrawValues(false);

        XAxis xAxis = mChart.getXAxis();
        xAxis.setValueFormatter(new IAxisValueFormatter() {
            @Override
            public String getFormattedValue(float value, AxisBase axis) {
                return labels.get((int) value);
            }
        });
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);

        BarData data = new BarData(dataSet);

        mChart.setData(data); // set the data and list of labels into chart

        CustomMarkerView mv = new CustomMarkerView(getActivity(), R.layout.marker_content);
        // set the marker to the chart
        mChart.setMarker(mv);
        mChart.setDrawMarkers(true);
        mChart.getAxisRight().setEnabled(false);

        mChart.setVisibleXRangeMaximum(12);
        mChart.animateX(500);
        mChart.getAxisLeft().setAxisMinimum(0);

        mChart.setOnChartValueSelectedListener(this);

        mAddBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AddActivityDialog dialog = new AddActivityDialog(getActivity(), mUser.getActivities(), new AddActivityDialog.DialogClickListener() {
                    @Override
                    public void onSave(String week, List<UserActivity> activities) {
                        mUser.getActivities().put(week, activities);
                        ((HealthStateActivity) getActivity()).updateUserInDb(mUser);
                        //TODO: update UI
                    }
                });
                dialog.show();
            }
        });

        return rootView;
    }

    private ArrayList<BarEntry> getBarEntriesFromUserActivities() {
        ArrayList<BarEntry> entries = new ArrayList<>();
        for (String key : mUser.getActivities().keySet()) {
            int activitiesDuration = getTotalActivitiesDuration(mUser.getActivities().get(key));
            entries.add(new BarEntry(Float.parseFloat(key), activitiesDuration));
        }
        return entries;
    }

    private int getTotalActivitiesDuration(List<UserActivity> activities) {
        int totalDuration = 0;
        if (activities != null && activities.size() > 0) {
            for (UserActivity activity : activities) {
                totalDuration += activity.getDuration();
            }
        }
        return totalDuration;
    }

    private int getMaxBarWidth(TextView durationView, LinearLayout wholeBarLayout) {
        int textViewWidth = durationView.getMeasuredWidth();
        int wholeLayoutWidth = wholeBarLayout.getMeasuredWidth();
        return wholeLayoutWidth - textViewWidth;
    }

    private int getMaxBarWidthByType(ActivityType type) {
        switch (type) {
            case Walking:
                return getMaxBarWidth(mWalkingDuration, mWalkingBarLayout);
            case Aerobics:
                return getMaxBarWidth(mAerobicsDuration, mAerobicsBarLayout);
            case Yoga:
                return getMaxBarWidth(mYogaDuration, mYogaBarLayout);
            case Pilates:
                return getMaxBarWidth(mPilatesDuration, mPilatesBarLayout);
            case Swimming:
                return getMaxBarWidth(mSwimmingDuration, mSwimmingBarLayout);
            case Dancing:
                return getMaxBarWidth(mDancingDuration, mDancingBarLayout);
            case StationaryB:
                return getMaxBarWidth(mSCDuration, mSCBarLayout);
            case Jogging:
                return getMaxBarWidth(mJoggingDuration, mJoggingBarLayout);
            case Other:
                return getMaxBarWidth(mOtherDuration, mOtherBarLayout);
            default:
                return 0;
        }
    }

    private void setDurationAsTextByType(UserActivity act) {
        String durationAsText = String.format("%02d:%02d %s", act.getDuration() / 60, act.getDuration() % 60, getString(R.string.fragment_activities_hours));
        switch (act.getType()) {
            case Walking:
                mWalkingDuration.setText(durationAsText);
                break;
            case Aerobics:
                mAerobicsDuration.setText(durationAsText);
                break;
            case Yoga:
                mYogaDuration.setText(durationAsText);
                break;
            case Pilates:
                mPilatesDuration.setText(durationAsText);
                break;
            case Swimming:
                mSwimmingDuration.setText(durationAsText);
                break;
            case Dancing:
                mDancingDuration.setText(durationAsText);
                break;
            case StationaryB:
                mSCDuration.setText(durationAsText);
                break;
            case Jogging:
                mJoggingDuration.setText(durationAsText);
                break;
            case Other:
                mOtherDuration.setText(durationAsText);
                break;
        }
    }

    private void setParamsToViewByType(LinearLayout.LayoutParams params, ActivityType type) {
        switch (type) {
            case Walking:
                mWalkingBar.setLayoutParams(params);
                break;
            case Aerobics:
                mAerobicsBar.setLayoutParams(params);
                break;
            case Yoga:
                mYogaBar.setLayoutParams(params);
                break;
            case Pilates:
                mPilatesBar.setLayoutParams(params);
                break;
            case Swimming:
                mSwimmingBar.setLayoutParams(params);
                break;
            case Dancing:
                mDancingBar.setLayoutParams(params);
                break;
            case StationaryB:
                mSCBar.setLayoutParams(params);
                break;
            case Jogging:
                mJoggingBar.setLayoutParams(params);
                break;
            case Other:
                mOtherBar.setLayoutParams(params);
                break;
        }
    }

    private void setToZeroBarsWidth() {
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(0, (int) getResources().getDimension(R.dimen.details_color_bar_height));
        mWalkingBar.setLayoutParams(params);
        mAerobicsBar.setLayoutParams(params);
        mYogaBar.setLayoutParams(params);
        mPilatesBar.setLayoutParams(params);
        mSwimmingBar.setLayoutParams(params);
        mDancingBar.setLayoutParams(params);
        mSCBar.setLayoutParams(params);
        mJoggingBar.setLayoutParams(params);
        mOtherBar.setLayoutParams(params);
    }

    private void setAllDurationsToZero() {
        String zeroDurationAsText = String.format("00:00 %s", getString(R.string.fragment_activities_hours));
        mWalkingDuration.setText(zeroDurationAsText);
        mAerobicsDuration.setText(zeroDurationAsText);
        mYogaDuration.setText(zeroDurationAsText);
        mPilatesDuration.setText(zeroDurationAsText);
        mSwimmingDuration.setText(zeroDurationAsText);
        mDancingDuration.setText(zeroDurationAsText);
        mSCDuration.setText(zeroDurationAsText);
        mJoggingDuration.setText(zeroDurationAsText);
        mOtherDuration.setText(zeroDurationAsText);
    }

    @Override
    public void onValueSelected(Entry e, Highlight h) {
        setToZeroBarsWidth();
        mActivitiesScroll.setVisibility(View.VISIBLE);

        List<UserActivity> activities = mUser.getActivities().get(Integer.toString((int)e.getX()));
        if (activities.size() == 0) {
            setAllDurationsToZero();
        } else {
            int longestDuration = 0;
            ActivityType longestDurationType = ActivityType.Other;

            for (UserActivity act : activities) {
                if (act.getDuration() > longestDuration) {
                    longestDuration = act.getDuration();
                    longestDurationType = act.getType();
                }

                setDurationAsTextByType(act);
            }

            for (UserActivity act : activities) {
                float partFromLongestDuration = ((float) act.getDuration()) / ((float) longestDuration);
                int maxBarWidth = getMaxBarWidthByType(longestDurationType);
                float scale = getResources().getDisplayMetrics().density;
                int dpAsPixels = (int) (5 * scale + 0.5f);
                LinearLayout.LayoutParams params = new LinearLayout.LayoutParams((int) (maxBarWidth * partFromLongestDuration) - dpAsPixels, (int) getResources().getDimension(R.dimen.details_color_bar_height));
                params.setMargins(0, 0, dpAsPixels, 0);
                setParamsToViewByType(params, act.getType());
            }
        }
    }

    @Override
    public void onNothingSelected() {
        setToZeroBarsWidth();
        mActivitiesScroll.setVisibility(View.INVISIBLE);
    }
}
