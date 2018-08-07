package com.angelova.w510.calmmom.fragments;

import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;

import com.angelova.w510.calmmom.R;
import com.angelova.w510.calmmom.chart_customizations.CustomFillFormatter;
import com.angelova.w510.calmmom.chart_customizations.CustomLineChartRenderer;
import com.angelova.w510.calmmom.models.User;
import com.angelova.w510.calmmom.models.Weight;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.components.Description;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter;
import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;
import com.github.mikephil.charting.listener.ChartTouchListener;
import com.github.mikephil.charting.listener.OnChartGestureListener;
import com.github.mikephil.charting.listener.OnChartValueSelectedListener;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Created by W510 on 4.8.2018 г..
 */

public class WeightFragment extends Fragment implements OnChartGestureListener, OnChartValueSelectedListener {

    private LineChart mChart;

    private User mUser;

    private List<Entry> mMinimumValues;
    private List<Entry> mMaximumValues;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_weight, container, false);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = getActivity().getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(Color.parseColor("#324A5F"));
        }

        mUser = (User) getArguments().getSerializable("user");

        mChart = (LineChart) rootView.findViewById(R.id.line_chart_weight);
        mChart.setOnChartGestureListener(this);
        mChart.setOnChartValueSelectedListener(this);

        mChart.setDragEnabled(true);
        mChart.setScaleEnabled(false);

        calculateAverageWeightGain();

        ArrayList<Entry> yValues = new ArrayList<>();

        yValues.add(new Entry(0, 65f));
        yValues.add(new Entry(1, 67f));
        yValues.add(new Entry(2, 69f));
        yValues.add(new Entry(5, 72f));
//        yValues.add(new Entry(4, 50f));
//        yValues.add(new Entry(5, 60f));
//        yValues.add(new Entry(6, 65f));

        ArrayList<Entry> yValues2 = new ArrayList<>();

        yValues2.add(new Entry(0, 30f));
        yValues2.add(new Entry(1, 40f));
        yValues2.add(new Entry(2, 45f));
        yValues2.add(new Entry(3, 50f));
        yValues2.add(new Entry(4, 55f));
        yValues2.add(new Entry(6, 65f));

        ArrayList<Entry> yValues3 = new ArrayList<>();

        yValues3.add(new Entry(0, 35f));
        yValues3.add(new Entry(1, 50f));
        yValues3.add(new Entry(2, 55f));
        yValues3.add(new Entry(3, 65f));
        yValues3.add(new Entry(4, 70f));
        yValues3.add(new Entry(5, 80f));
        yValues3.add(new Entry(6, 95f));

        final ArrayList<String> xValues = new ArrayList<>();
        xValues.add("0");
        xValues.add("5");
        xValues.add("10");
        xValues.add("15");
        xValues.add("20");
        xValues.add("25");
        xValues.add("30");
        xValues.add("35");
        xValues.add("40");

        List<Entry> userValues = getWeightValuesFromUser();
        LineDataSet set1 = new LineDataSet(userValues, "Data Set 1");
        LineDataSet set2 = new LineDataSet(mMinimumValues, "Minimum values for the current BMI");
        LineDataSet set3 = new LineDataSet(mMaximumValues, "Maximum values for the current BMI");

        set1.setFillAlpha(110);

        //to make the smooth line as the graph is adrapt change so smooth curve
        set1.setMode(LineDataSet.Mode.CUBIC_BEZIER);
        set2.setMode(LineDataSet.Mode.LINEAR);
        set3.setMode(LineDataSet.Mode.LINEAR);
        //to enable the cubic density : if 1 then it will be sharp curve
        set1.setCubicIntensity(0.2f);
        //set2.setCubicIntensity(0.2f);

        //to fill the below of smooth line in graph
        set1.setDrawFilled(true);
        set2.setDrawFilled(true);
        set3.setDrawFilled(true);
        set1.setFillColor(Color.parseColor("#324A5F"));
        set2.setFillColor(Color.parseColor("#fa7665"));
        set3.setFillColor(Color.parseColor("#fa7665"));
        //set the transparency
        set1.setFillAlpha(80);
        set2.setFillAlpha(0);
        set3.setFillAlpha(50);

        set1.setColor(Color.parseColor("#324A5F"));
        set2.setColor(Color.parseColor("#fa7665"));
        set3.setColor(Color.parseColor("#fa7665"));

        //set the gradiant then the above draw fill color will be replace
        Drawable drawable = ContextCompat.getDrawable(getContext(), R.drawable.chart_gradient);
        //set1.setFillDrawable(drawable);
        //set2.setFillDrawable(drawable);

        //set legend disable or enable to hide {the left down corner name of graph}
        Legend legend = mChart.getLegend();
        legend.setEnabled(false);

        //to remove the cricle from the graph
        set1.setDrawCircles(false);
        set2.setDrawCircles(false);
        set3.setDrawCircles(false);

        set2.setDrawValues(false);
        set3.setDrawValues(false);

        ArrayList<ILineDataSet> dataSets = new ArrayList<>();
        dataSets.add(set1);
        dataSets.add(set2);
        dataSets.add(set3);

        LineData data = new LineData(dataSets);

        mChart.setData(data);
        mChart.animateX(500);

        XAxis xAxis = mChart.getXAxis();
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setDrawGridLines(true);
        xAxis.setTextSize(10f);


        xAxis.setValueFormatter(new IndexAxisValueFormatter() {
            @Override
            public String getFormattedValue(float value, AxisBase axis) {
                int index = (int) value;
                return xValues.get(index);
            }
        });

        set3.setFillFormatter(new CustomFillFormatter(set2));

        mChart.setRenderer(new CustomLineChartRenderer(mChart, mChart.getAnimator(), mChart.getViewPortHandler()));

        mChart.getAxisRight().setEnabled(false);
        mChart.setScaleEnabled(true);
        Description description = new Description();
        description.setText(getString(R.string.fragment_weight_chart_description));
        mChart.setDescription(description);

        return rootView;
    }

    @Override
    public void onChartGestureStart(MotionEvent me, ChartTouchListener.ChartGesture lastPerformedGesture) {

    }

    @Override
    public void onChartGestureEnd(MotionEvent me, ChartTouchListener.ChartGesture lastPerformedGesture) {

    }

    @Override
    public void onChartLongPressed(MotionEvent me) {

    }

    @Override
    public void onChartDoubleTapped(MotionEvent me) {

    }

    @Override
    public void onChartSingleTapped(MotionEvent me) {

    }

    @Override
    public void onChartFling(MotionEvent me1, MotionEvent me2, float velocityX, float velocityY) {

    }

    @Override
    public void onChartScale(MotionEvent me, float scaleX, float scaleY) {

    }

    @Override
    public void onChartTranslate(MotionEvent me, float dX, float dY) {

    }

    //data source: https://www.babycentre.co.uk/a554810/weight-gain-in-pregnancy
    private void calculateAverageWeightGain() {
        double height = mUser.getCurrentHeight();
        double weightBeforePregnancy = mUser.getCurrentWeight();
        double bmi = weightBeforePregnancy / (height * height);

        mMinimumValues = new ArrayList<>();
        mMaximumValues = new ArrayList<>();
        if (bmi < 18.5) {
            //underweight
            mMinimumValues.add(new Entry(0, (float)weightBeforePregnancy));
            mMinimumValues.add(new Entry(1, (float)(weightBeforePregnancy + 0.625)));
            mMinimumValues.add(new Entry(2, (float)(weightBeforePregnancy + 1.25)));
            mMinimumValues.add(new Entry(3, (float)(weightBeforePregnancy + 2.5)));
            mMinimumValues.add(new Entry(4, (float)(weightBeforePregnancy + 5)));
            mMinimumValues.add(new Entry(5, (float)(weightBeforePregnancy + 7.5)));
            mMinimumValues.add(new Entry(6, (float)(weightBeforePregnancy + 10)));
            mMinimumValues.add(new Entry(7, (float)(weightBeforePregnancy + 12.5)));
            mMinimumValues.add(new Entry(8, (float)(weightBeforePregnancy + 15)));

            mMaximumValues.add(new Entry(0, (float)weightBeforePregnancy));
            mMaximumValues.add(new Entry(1, (float)(weightBeforePregnancy + 0.75)));
            mMaximumValues.add(new Entry(2, (float)(weightBeforePregnancy + 1.5)));
            mMaximumValues.add(new Entry(3, (float)(weightBeforePregnancy + 3)));
            mMaximumValues.add(new Entry(4, (float)(weightBeforePregnancy + 6)));
            mMaximumValues.add(new Entry(5, (float)(weightBeforePregnancy + 9)));
            mMaximumValues.add(new Entry(6, (float)(weightBeforePregnancy + 12)));
            mMaximumValues.add(new Entry(7, (float)(weightBeforePregnancy + 15)));
            mMaximumValues.add(new Entry(8, (float)(weightBeforePregnancy + 18)));
        } else if (bmi < 24.9) {
            //normal
            mMinimumValues.add(new Entry(0, (float)weightBeforePregnancy));
            mMinimumValues.add(new Entry(1, (float)(weightBeforePregnancy + 0.2)));
            mMinimumValues.add(new Entry(2, (float)(weightBeforePregnancy + 0.5)));
            mMinimumValues.add(new Entry(3, (float)(weightBeforePregnancy + 1.5)));
            mMinimumValues.add(new Entry(4, (float)(weightBeforePregnancy + 3.5)));
            mMinimumValues.add(new Entry(5, (float)(weightBeforePregnancy + 5.5)));
            mMinimumValues.add(new Entry(6, (float)(weightBeforePregnancy + 7.5)));
            mMinimumValues.add(new Entry(7, (float)(weightBeforePregnancy + 9.5)));
            mMinimumValues.add(new Entry(8, (float)(weightBeforePregnancy + 11.5)));

            mMaximumValues.add(new Entry(0, (float)weightBeforePregnancy));
            mMaximumValues.add(new Entry(1, (float)(weightBeforePregnancy + 1.25)));
            mMaximumValues.add(new Entry(2, (float)(weightBeforePregnancy + 2.5)));
            mMaximumValues.add(new Entry(3, (float)(weightBeforePregnancy + 3.5)));
            mMaximumValues.add(new Entry(4, (float)(weightBeforePregnancy + 6)));
            mMaximumValues.add(new Entry(5, (float)(weightBeforePregnancy + 8.5)));
            mMaximumValues.add(new Entry(6, (float)(weightBeforePregnancy + 11)));
            mMaximumValues.add(new Entry(7, (float)(weightBeforePregnancy + 13.5)));
            mMaximumValues.add(new Entry(8, (float)(weightBeforePregnancy + 16)));
        } else if (bmi < 29.9) {
            //overweight
            mMinimumValues.add(new Entry(0, (float)weightBeforePregnancy));
            mMinimumValues.add(new Entry(1, (float)(weightBeforePregnancy + 0.67)));
            mMinimumValues.add(new Entry(2, (float)(weightBeforePregnancy + 1.3)));
            mMinimumValues.add(new Entry(3, (float)(weightBeforePregnancy + 2)));
            mMinimumValues.add(new Entry(4, (float)(weightBeforePregnancy + 3)));
            mMinimumValues.add(new Entry(5, (float)(weightBeforePregnancy + 4)));
            mMinimumValues.add(new Entry(6, (float)(weightBeforePregnancy + 5)));
            mMinimumValues.add(new Entry(7, (float)(weightBeforePregnancy + 6)));
            mMinimumValues.add(new Entry(8, (float)(weightBeforePregnancy + 7)));

            mMaximumValues.add(new Entry(0, (float)weightBeforePregnancy));
            mMaximumValues.add(new Entry(1, (float)(weightBeforePregnancy + 1.45)));
            mMaximumValues.add(new Entry(2, (float)(weightBeforePregnancy + 2.8)));
            mMaximumValues.add(new Entry(3, (float)(weightBeforePregnancy + 4)));
            mMaximumValues.add(new Entry(4, (float)(weightBeforePregnancy + 5.5)));
            mMaximumValues.add(new Entry(5, (float)(weightBeforePregnancy + 7)));
            mMaximumValues.add(new Entry(6, (float)(weightBeforePregnancy + 8.5)));
            mMaximumValues.add(new Entry(7, (float)(weightBeforePregnancy + 10)));
            mMaximumValues.add(new Entry(8, (float)(weightBeforePregnancy + 11.5)));
        } else {
            //obese
            mMinimumValues.add(new Entry(0, (float)weightBeforePregnancy));
            mMinimumValues.add(new Entry(1, (float)(weightBeforePregnancy + 0.25)));
            mMinimumValues.add(new Entry(2, (float)(weightBeforePregnancy + 0.5)));
            mMinimumValues.add(new Entry(3, (float)(weightBeforePregnancy + 1)));
            mMinimumValues.add(new Entry(4, (float)(weightBeforePregnancy + 2)));
            mMinimumValues.add(new Entry(5, (float)(weightBeforePregnancy + 3)));
            mMinimumValues.add(new Entry(6, (float)(weightBeforePregnancy + 4)));
            mMinimumValues.add(new Entry(7, (float)(weightBeforePregnancy + 5)));
            mMinimumValues.add(new Entry(8, (float)(weightBeforePregnancy + 6)));

            mMaximumValues.add(new Entry(0, (float)weightBeforePregnancy));
            mMaximumValues.add(new Entry(1, (float)(weightBeforePregnancy + 0.375)));
            mMaximumValues.add(new Entry(2, (float)(weightBeforePregnancy + 0.75)));
            mMaximumValues.add(new Entry(3, (float)(weightBeforePregnancy + 1.5)));
            mMaximumValues.add(new Entry(4, (float)(weightBeforePregnancy + 3)));
            mMaximumValues.add(new Entry(5, (float)(weightBeforePregnancy + 4.5)));
            mMaximumValues.add(new Entry(6, (float)(weightBeforePregnancy + 6)));
            mMaximumValues.add(new Entry(7, (float)(weightBeforePregnancy + 7.5)));
            mMaximumValues.add(new Entry(8, (float)(weightBeforePregnancy + 9)));
        }
    }

    private List<Entry> getWeightValuesFromUser() {
        List<Entry> values = new ArrayList<>();
        List<Weight> weights = mUser.getWeights();
        if (weights == null) {
            values.add(new Entry(0, (float)mUser.getCurrentWeight()));
        } else {
            Collections.sort(weights);
            for (Weight w : weights) {
                int index = getIndexByWeek(w.getWeek());
                values.add(new Entry(index, (float) w.getValue()));
            }
        }

        return values;
    }

    private int getIndexByWeek(int week) {
        switch (week) {
            case 0:
                return 0;
            case 5:
                return 1;
            case 10:
                return 2;
            case 15:
                return 3;
            case 20:
                return 4;
            case 25:
                return 5;
            case 30:
                return 6;
            case 35:
                return 7;
            case 40:
                return 8;
            default:
                return 0;
        }
    }

    @Override
    public void onValueSelected(Entry e, Highlight h) {
        System.out.println("SELECTED " + e.getX() + " " + e.getY());
    }

    @Override
    public void onNothingSelected() {

    }
}
