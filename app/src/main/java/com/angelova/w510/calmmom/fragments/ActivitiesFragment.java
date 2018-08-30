package com.angelova.w510.calmmom.fragments;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.graphics.Point;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.util.DisplayMetrics;
import android.view.Display;
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
import java.util.HashMap;
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
    private int pregnancyIndex;

    private int deviceWidth;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_activities, container, false);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = getActivity().getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(Color.parseColor("#324A5F"));
        }
        mUser = (User) getArguments().getSerializable("user");
        pregnancyIndex = mUser.getPregnancyConsecutiveId();

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


        WindowManager wm = (WindowManager) getContext().getSystemService(Context.WINDOW_SERVICE);
        Display display = wm.getDefaultDisplay();
        if(android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR2){
            Point size = new Point();
            display.getSize(size);
            deviceWidth = size.x;
        } else {
            deviceWidth = display.getWidth();
        }

        final List<String> labels = new ArrayList<>();
        for (int i = 0; i <= 40; i++) {
            labels.add(String.format("%s", i));
        }

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

        BarDataSet dataSet = new BarDataSet(entries, getString(R.string.fragment_activities_duration));
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

        mChart.setData(data);

        CustomMarkerView mv = new CustomMarkerView(getActivity(), R.layout.marker_content);
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
                AddActivityDialog dialog = new AddActivityDialog(getActivity(), mUser.getPregnancies().get(pregnancyIndex).getActivities(), new AddActivityDialog.DialogClickListener() {
                    @Override
                    public void onSave(String week, List<UserActivity> activities) {
                        mUser.getPregnancies().get(pregnancyIndex).getActivities().put(week, activities);
                        ((HealthStateActivity) getActivity()).updateUserInDb(mUser);
                        updateChart();
                        mActivitiesScroll.setVisibility(View.INVISIBLE);
                    }
                });
                dialog.show();
            }
        });

        return rootView;
    }

    private ArrayList<BarEntry> getBarEntriesFromUserActivities() {
        ArrayList<BarEntry> entries = new ArrayList<>();
        HashMap<String, List<UserActivity>> activities = mUser.getPregnancies().get(pregnancyIndex).getActivities();
        for (String key : activities.keySet()) {
            int activitiesDuration = getTotalActivitiesDuration(activities.get(key));
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

        int widthMeasureSpec = View.MeasureSpec.makeMeasureSpec(deviceWidth, View.MeasureSpec.AT_MOST);
        int heightMeasureSpec = View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED);
        durationView.measure(widthMeasureSpec, heightMeasureSpec);

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

    private void updateChart() {
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

        BarDataSet dataSet = new BarDataSet(entries, getString(R.string.fragment_activities_duration));
        dataSet.setColor(ContextCompat.getColor(getActivity(), R.color.colorPrimaryTransparent));
        dataSet.setDrawValues(false);

        BarData data = new BarData(dataSet);

        mChart.setData(data);
        mChart.highlightValues(null);
        mChart.invalidate();
    }

    @Override
    public void onValueSelected(Entry e, Highlight h) {
        setToZeroBarsWidth();
        mActivitiesScroll.setVisibility(View.VISIBLE);

        List<UserActivity> activities = mUser.getPregnancies().get(pregnancyIndex).getActivities().get(Integer.toString((int)e.getX()));
        setAllDurationsToZero();
        if (activities.size() > 0) {
            int longestDuration = 0;
            ActivityType longestDurationType = ActivityType.Other;

            for (UserActivity act : activities) {
                if (act.getDuration() > longestDuration) {
                    longestDuration = act.getDuration();
                    longestDurationType = act.getType();
                }

                setDurationAsTextByType(act);
            }

            int maxBarWidth = getMaxBarWidthByType(longestDurationType);

            for (UserActivity act : activities) {
                float partFromLongestDuration = ((float) act.getDuration()) / ((float) longestDuration);
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
