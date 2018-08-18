package com.angelova.w510.calmmom.fragments;

import android.app.Activity;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;

import com.angelova.w510.calmmom.HealthStateActivity;
import com.angelova.w510.calmmom.R;
import com.angelova.w510.calmmom.chart_customizations.CustomMarkerView;
import com.angelova.w510.calmmom.dialogs.AddActivityDialog;
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

    @Override
    public void onValueSelected(Entry e, Highlight h) {

    }

    @Override
    public void onNothingSelected() {

    }
}
