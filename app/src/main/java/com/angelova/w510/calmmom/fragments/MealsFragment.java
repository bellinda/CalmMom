package com.angelova.w510.calmmom.fragments;

import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

import com.angelova.w510.calmmom.HealthStateActivity;
import com.angelova.w510.calmmom.R;
import com.angelova.w510.calmmom.adapters.MealsTimelineAdapter;
import com.angelova.w510.calmmom.dialogs.AddMealDialog;
import com.angelova.w510.calmmom.models.Meal;
import com.angelova.w510.calmmom.models.User;
import com.github.lzyzsd.circleprogress.ArcProgress;
import com.melnykov.fab.FloatingActionButton;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.TimeUnit;

import devs.mulham.horizontalcalendar.HorizontalCalendar;
import devs.mulham.horizontalcalendar.utils.HorizontalCalendarListener;

/**
 * Created by W510 on 16.9.2018 г..
 */

public class MealsFragment extends Fragment {

    private HorizontalCalendar horizontalCalendar;
    private TextView mWeekDayView;
    private ArcProgress mArcProgress;
    private FloatingActionButton mAddBtn;
    private TextView mNoDataView;
    private MealsTimelineAdapter mMealsAdapter;
    private RecyclerView mRecyclerView;

    private List<Meal> meals;

    private User mUser;
    private int pregnancyIndex;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_meals, container, false);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = getActivity().getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(Color.parseColor("#283A4B"));
        }

        mUser = (User) getArguments().getSerializable("user");
        pregnancyIndex = mUser.getPregnancyConsecutiveId();

        int currentPregnancyWeek = (int) (getDaysSinceDate(mUser.getPregnancies().get(pregnancyIndex).getFirstDayOfLastMenstruation()) / 7 + 1);
        int daysOfCurrentWeek = (int) (getDaysSinceDate(mUser.getPregnancies().get(pregnancyIndex).getFirstDayOfLastMenstruation()) % 7 + 1);
        int monthsGone = currentPregnancyWeek / 4;
        int monthsUpcoming = 10 - monthsGone;

        Calendar endDate = Calendar.getInstance();
        endDate.add(Calendar.MONTH, monthsUpcoming);
        int lastDayOfEndMonth = endDate.getActualMaximum(Calendar.DAY_OF_MONTH);
        endDate.set(Calendar.DATE, lastDayOfEndMonth);
        Calendar startDate = Calendar.getInstance();
        startDate.add(Calendar.MONTH, 0 - monthsGone);
        startDate.set(Calendar.DATE, 1);

        horizontalCalendar = new HorizontalCalendar.Builder(rootView, R.id.calendarView)
                .range(startDate, endDate)
                .datesNumberOnScreen(7)
                .build();

        horizontalCalendar.setCalendarListener(new HorizontalCalendarListener() {
            @Override
            public void onDateSelected(Calendar date, int position) {
                int selectedPregnancyWeek = (int) (getDaysBetweenTwoDates(mUser.getPregnancies().get(pregnancyIndex).getFirstDayOfLastMenstruation(), date.getTime()) / 7 + 1);
                int daysOfSelectedWeek = (int) (getDaysBetweenTwoDates(mUser.getPregnancies().get(pregnancyIndex).getFirstDayOfLastMenstruation(), date.getTime()) % 7 + 1);
                mWeekDayView.setText(String.format(Locale.getDefault(), "%s %d, %s %d", getString(R.string.fragment_meals_week_title), selectedPregnancyWeek, getString(R.string.fragment_meals_day_title), daysOfSelectedWeek));

                getMealsForDate(date.getTime());
                if (meals.size() > 0) {
                    Collections.sort(meals);
                    mMealsAdapter = new MealsTimelineAdapter(meals);
                    mRecyclerView.setAdapter(mMealsAdapter);

                    mNoDataView.setVisibility(View.GONE);
                    mRecyclerView.setVisibility(View.VISIBLE);
                } else {
                    mRecyclerView.setVisibility(View.GONE);
                    mNoDataView.setVisibility(View.VISIBLE);
                }
            }
        });

        mWeekDayView = (TextView) rootView.findViewById(R.id.week_day_view);
        mWeekDayView.setText(String.format(Locale.getDefault(), "%s %d, %s %d", getString(R.string.fragment_meals_week_title), currentPregnancyWeek, getString(R.string.fragment_meals_day_title), daysOfCurrentWeek));

        mArcProgress = (ArcProgress) rootView.findViewById(R.id.arc_progress);

        mAddBtn = (FloatingActionButton) rootView.findViewById(R.id.add_meal_btn);
        mAddBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AddMealDialog addMealDialog = new AddMealDialog(getActivity(), horizontalCalendar.getSelectedDate().getTime(), new AddMealDialog.DialogClickListener() {
                    @Override
                    public void onSave(Meal meal) {
                        if (mUser.getPregnancies().get(pregnancyIndex).getMeals() == null) {
                            mUser.getPregnancies().get(pregnancyIndex).setMeals(new HashMap<String, List<Meal>>());
                        }

                        if (mUser.getPregnancies().get(pregnancyIndex).getMeals().get(meal.getDate()) == null) {
                            List<Meal> meals = Arrays.asList(meal);
                            mUser.getPregnancies().get(pregnancyIndex).getMeals().put(meal.getDate(), meals);
                        } else {
                            mUser.getPregnancies().get(pregnancyIndex).getMeals().get(meal.getDate()).add(meal);
                        }
                        ((HealthStateActivity) getActivity()).updateUserInDb(mUser);

                        getMealsForDate(horizontalCalendar.getSelectedDate().getTime());
                        if (meals.size() > 0) {
                            Collections.sort(meals);
                            mMealsAdapter = new MealsTimelineAdapter(meals);
                            mRecyclerView.setAdapter(mMealsAdapter);

                            mNoDataView.setVisibility(View.GONE);
                            mRecyclerView.setVisibility(View.VISIBLE);
                        } else {
                            mRecyclerView.setVisibility(View.GONE);
                            mNoDataView.setVisibility(View.VISIBLE);
                        }
                    }
                });
                addMealDialog.show();
            }
        });

        mNoDataView = (TextView) rootView.findViewById(R.id.no_data_view);
        mRecyclerView = (RecyclerView) rootView.findViewById(R.id.recyclerView);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false));
        mRecyclerView.setHasFixedSize(true);

        getMealsForCurrentDate();
        if (meals.size() > 0) {
            Collections.sort(meals);
            mMealsAdapter = new MealsTimelineAdapter(meals);
            mRecyclerView.setAdapter(mMealsAdapter);

            mNoDataView.setVisibility(View.GONE);
            mRecyclerView.setVisibility(View.VISIBLE);
        } else {
            mRecyclerView.setVisibility(View.GONE);
            mNoDataView.setVisibility(View.VISIBLE);
        }

        return rootView;
    }

    private long getDaysSinceDate(String startDate) {
        long daysDiff = 0;
        Calendar cal = Calendar.getInstance();
        SimpleDateFormat sdf = new SimpleDateFormat("dd.MM.yyyy", Locale.ENGLISH);
        try {
            cal.setTime(sdf.parse(startDate));
            Calendar cal1 = Calendar.getInstance();
            String formatted = sdf.format(cal1.getTime());
            cal1.setTime(sdf.parse(formatted));

            long msDiff = cal1.getTimeInMillis() - cal.getTimeInMillis();
            daysDiff = TimeUnit.MILLISECONDS.toDays(msDiff);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return daysDiff;
    }

    private long getDaysBetweenTwoDates(String startDate, Date endDate) {
        long daysDiff = 0;
        Calendar cal = Calendar.getInstance();
        SimpleDateFormat sdf = new SimpleDateFormat("dd.MM.yyyy", Locale.ENGLISH);
        try {
            cal.setTime(sdf.parse(startDate));
            Calendar cal1 = Calendar.getInstance();
            cal1.setTime(endDate);

            long msDiff = cal1.getTimeInMillis() - cal.getTimeInMillis();
            daysDiff = TimeUnit.MILLISECONDS.toDays(msDiff);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return daysDiff;
    }

    private void getMealsForCurrentDate() {
        meals = new ArrayList<>();
        if (mUser.getPregnancies().get(pregnancyIndex).getMeals() != null) {
            SimpleDateFormat sdf = new SimpleDateFormat("dd MMM yyyy", Locale.getDefault());
            //the selected date on opening the fragment is always the current date
            String date = sdf.format(Calendar.getInstance().getTime());
            List<Meal> mealsForCurrentDate = mUser.getPregnancies().get(pregnancyIndex).getMeals().get(date);
            if (mealsForCurrentDate != null) {
                meals = mealsForCurrentDate;
            }
        }
    }

    private void getMealsForDate(Date date) {
        meals = new ArrayList<>();
        if (mUser.getPregnancies().get(pregnancyIndex).getMeals() != null) {
            SimpleDateFormat sdf = new SimpleDateFormat("dd MMM yyyy", Locale.getDefault());
            String dateAsString = sdf.format(date);
            List<Meal> mealsForCurrentDate = mUser.getPregnancies().get(pregnancyIndex).getMeals().get(dateAsString);
            if (mealsForCurrentDate != null) {
                meals = mealsForCurrentDate;
            }
        }
    }
}
