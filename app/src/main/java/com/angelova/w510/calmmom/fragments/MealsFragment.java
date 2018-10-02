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
import com.angelova.w510.calmmom.dialogs.RecomWeeklyIntakeDialog;
import com.angelova.w510.calmmom.models.Meal;
import com.angelova.w510.calmmom.models.User;
import com.github.lzyzsd.circleprogress.ArcProgress;
import com.melnykov.fab.FloatingActionButton;

import java.text.DateFormat;
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
import java.util.Map;
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

    private int currentWeek = 0;
    private int currentDayOfWeek = 1;

    private int meatQuantityForWeek = 0;
    private int fishQuantityForWeek = 0;
    private int seaFoodQuantityForWeek = 0;
    private int fruitsAndVegsQuantityForWeek = 0;
    private int milkQuantityForWeek = 0;
    private int dairyQuantityForWeek = 0;
    private int eggsQuantityForWeek = 0;
    private int wholeGrainsQuantity = 0;
    private int beanFoodsQuantityForWeek = 0;

    private int meatPercent = 0;
    private int fishPercent = 0;
    private int seaFoodPercent = 0;
    private int fruitsAndVegsPercent = 0;
    private int milkPercent = 0;
    private int dairyPercent = 0;
    private int eggsPercent = 0;
    private int wholeGrainsPercent =  0;
    private int beanFoodsPercent = 0;

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
        currentWeek = currentPregnancyWeek;
        int daysOfCurrentWeek = (int) (getDaysSinceDate(mUser.getPregnancies().get(pregnancyIndex).getFirstDayOfLastMenstruation()) % 7 + 1);
        currentDayOfWeek = daysOfCurrentWeek;
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

                currentDayOfWeek = daysOfSelectedWeek;

                if (selectedPregnancyWeek != currentWeek) {
                    currentWeek = selectedPregnancyWeek;

                    calculateWeekPercentage(date, daysOfSelectedWeek);
                }
            }
        });

        mWeekDayView = (TextView) rootView.findViewById(R.id.week_day_view);
        mWeekDayView.setText(String.format(Locale.getDefault(), "%s %d, %s %d", getString(R.string.fragment_meals_week_title), currentPregnancyWeek, getString(R.string.fragment_meals_day_title), daysOfCurrentWeek));

        mArcProgress = (ArcProgress) rootView.findViewById(R.id.arc_progress);
        mArcProgress.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                RecomWeeklyIntakeDialog dialog = new RecomWeeklyIntakeDialog(getActivity(), getQuantitiesMap());
                dialog.show();
            }
        });

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
                            List<Meal> meals = new ArrayList<>(Arrays.asList(meal));
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

                        calculateWeekPercentage(horizontalCalendar.getSelectedDate(), currentDayOfWeek);
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

        calculateWeekPercentage(Calendar.getInstance(), daysOfCurrentWeek);

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

    private List<Meal> getMealsForDate(String date) {
        if (mUser.getPregnancies().get(pregnancyIndex).getMeals() != null) {
            return mUser.getPregnancies().get(pregnancyIndex).getMeals().get(date);
        }
        return new ArrayList<>();
    }

    private List<String> getAllDatesFromSelectedWeek(Calendar currentDate, int dayOfCurrentWeek) {
        List<String> datesInWeek = new ArrayList<>();
        DateFormat format = new SimpleDateFormat("dd MMM yyyy");
        currentDate.add(Calendar.DAY_OF_MONTH, 1 - dayOfCurrentWeek);

        for (int i = 0; i < 7; i++) {
            datesInWeek.add(format.format(currentDate.getTime()));
            currentDate.add(Calendar.DAY_OF_MONTH, 1);
        }
        return datesInWeek;
    }

    private int getQuantityForCategoryInWeek(List<String> datesInCurrentWeek, int category) {
        int quantity = 0;
        for (String date : datesInCurrentWeek) {
            List<Meal> currentDateMeals = getMealsForDate(date);
            if (currentDateMeals != null && currentDateMeals.size() > 0) {
                for (Meal meal : currentDateMeals) {
                    if (meal.getCategory() == category) {
                        quantity += meal.getQuantity();
                    }
                }
            }
        }
        return quantity;
    }

    private void calculateWeekPercentage(Calendar currentDate, int daysOfCurrentWeek) {
        List<String> datesInCurrentWeek = getAllDatesFromSelectedWeek(currentDate, daysOfCurrentWeek);
        meatQuantityForWeek = getQuantityForCategoryInWeek(datesInCurrentWeek, 6);
        fishQuantityForWeek = getQuantityForCategoryInWeek(datesInCurrentWeek, 7);
        seaFoodQuantityForWeek = getQuantityForCategoryInWeek(datesInCurrentWeek, 8);
        fruitsAndVegsQuantityForWeek = getQuantityForCategoryInWeek(datesInCurrentWeek, 0);
        milkQuantityForWeek = getQuantityForCategoryInWeek(datesInCurrentWeek, 2);
        dairyQuantityForWeek = getQuantityForCategoryInWeek(datesInCurrentWeek, 3);
        eggsQuantityForWeek = getQuantityForCategoryInWeek(datesInCurrentWeek, 4);
        wholeGrainsQuantity = getQuantityForCategoryInWeek(datesInCurrentWeek, 1);
        beanFoodsQuantityForWeek = getQuantityForCategoryInWeek(datesInCurrentWeek, 5);

        meatPercent = 0;
        fishPercent = 0;
        seaFoodPercent = 0;
        fruitsAndVegsPercent = 0;
        milkPercent = 0;
        dairyPercent = 0;
        eggsPercent = 0;
        wholeGrainsPercent =  0;
        beanFoodsPercent = 0;

        if (meatQuantityForWeek != 0) {
            meatPercent = (int)Math.round(((double)meatQuantityForWeek / 1750) * 100);
            if (meatPercent > 100) {
                meatPercent = 100;
            }
        }
        if (fishQuantityForWeek != 0) {
            fishPercent = (int)Math.round(((double)fishQuantityForWeek / 300) * 100);
        }
        if (seaFoodQuantityForWeek != 0) {
            seaFoodPercent = (int)Math.round(((double)seaFoodQuantityForWeek / 300) * 100);
        }
        if (fruitsAndVegsQuantityForWeek != 0) {
            fruitsAndVegsPercent = (int)Math.round(((double)fruitsAndVegsQuantityForWeek / 1500) * 100);
            if (fruitsAndVegsPercent > 100) {
                fruitsAndVegsPercent = 100;
            }
        }
        if (milkQuantityForWeek != 0) {
            milkPercent = (int)Math.round(((double)milkQuantityForWeek / 5000) * 100);
            if (milkPercent > 100) {
                milkPercent = 100;
            }
        }
        if (dairyQuantityForWeek != 0) {
            dairyPercent = (int)Math.round(((double)dairyQuantityForWeek / 1200) * 100);
            if (dairyPercent > 100) {
                dairyPercent = 100;
            }
        }
        if (eggsQuantityForWeek != 0) {
            eggsPercent = (int)Math.round(((double)eggsQuantityForWeek / 20) * 100);
            if (eggsPercent > 100) {
                eggsPercent = 100;
            }
        }
        if (wholeGrainsQuantity != 0) {
            wholeGrainsPercent = (int)Math.round(((double)wholeGrainsQuantity / 1000) * 100);
            if (wholeGrainsPercent > 100) {
                wholeGrainsPercent = 100;
            }
        }
        if (beanFoodsQuantityForWeek != 0) {
            beanFoodsPercent = (int)Math.round(((double)beanFoodsQuantityForWeek / 500) * 100);
            if (beanFoodsPercent > 100) {
                beanFoodsPercent = 100;
            }
        }

        int fishAndSeaFoodTotalPercent = fishPercent + seaFoodPercent > 100 ? 100 : fishPercent + seaFoodPercent;

        int totalPercent = (meatPercent + fishAndSeaFoodTotalPercent + fruitsAndVegsPercent + milkPercent + dairyPercent + eggsPercent + wholeGrainsPercent + beanFoodsPercent) / 8;
        mArcProgress.setProgress(totalPercent);
    }

    private Map<Integer, Integer> getQuantitiesMap() {
        Map<Integer, Integer> quantitiesMap = new HashMap<>();
        quantitiesMap.put(0, fruitsAndVegsQuantityForWeek);
        quantitiesMap.put(1, wholeGrainsQuantity);
        quantitiesMap.put(2, milkQuantityForWeek);
        quantitiesMap.put(3, dairyQuantityForWeek);
        quantitiesMap.put(4, eggsQuantityForWeek);
        quantitiesMap.put(5, beanFoodsQuantityForWeek);
        quantitiesMap.put(6, meatQuantityForWeek);
        quantitiesMap.put(7, fishQuantityForWeek);
        quantitiesMap.put(8, seaFoodQuantityForWeek);

        return quantitiesMap;
    }
}
