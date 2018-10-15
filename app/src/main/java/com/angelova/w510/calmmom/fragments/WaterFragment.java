package com.angelova.w510.calmmom.fragments;

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
import android.widget.ImageView;
import android.widget.TextView;

import com.angelova.w510.calmmom.HealthStateActivity;
import com.angelova.w510.calmmom.R;
import com.angelova.w510.calmmom.models.User;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.TimeUnit;

import devs.mulham.horizontalcalendar.HorizontalCalendar;
import devs.mulham.horizontalcalendar.utils.HorizontalCalendarListener;

public class WaterFragment extends Fragment {

    private HorizontalCalendar horizontalCalendar;
    private TextView mWeekDayView;
    private TextView mRemoveBtn;
    private TextView mAddBtn;
    private ImageView mWaterGraphic;
    private TextView mInnerGlassesCount;
    private TextView mGlassesCountFromRecommended;

    private User mUser;
    private int pregnancyIndex;

    private int currentGlassesCount = 0;
    private String selectedDate;
    private SimpleDateFormat sdf;

    private Date endedPregnancyDate = null;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_water, container, false);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = getActivity().getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(Color.parseColor("#283A4B"));
        }

        mUser = (User) getArguments().getSerializable("user");
        pregnancyIndex = mUser.getPregnancyConsecutiveId();

        sdf = new SimpleDateFormat("dd MMM yyyy", Locale.getDefault());

        int currentPregnancyWeek = (int) (getDaysSinceDate(mUser.getPregnancies().get(pregnancyIndex).getFirstDayOfLastMenstruation()) / 7 + 1);
        int daysOfCurrentWeek = (int) (getDaysSinceDate(mUser.getPregnancies().get(pregnancyIndex).getFirstDayOfLastMenstruation()) % 7 + 1);
        int monthsGone = currentPregnancyWeek / 4;
        int monthsUpcoming = 10 - monthsGone;

        if (mUser.getPregnancies().get(pregnancyIndex).getPregnancyOutcome() != null) {
            SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
            try {
                endedPregnancyDate = sdf.parse(mUser.getPregnancies().get(pregnancyIndex).getPregnancyOutcome().getDate());
            } catch (ParseException pe) {
                pe.printStackTrace();
            }
        }

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
                if (endedPregnancyDate == null || !date.getTime().after(endedPregnancyDate)) {
                    mWeekDayView.setVisibility(View.VISIBLE);

                    int selectedPregnancyWeek = (int) (getDaysBetweenTwoDates(mUser.getPregnancies().get(pregnancyIndex).getFirstDayOfLastMenstruation(), date.getTime()) / 7 + 1);
                    int daysOfSelectedWeek = (int) (getDaysBetweenTwoDates(mUser.getPregnancies().get(pregnancyIndex).getFirstDayOfLastMenstruation(), date.getTime()) % 7 + 1);
                    mWeekDayView.setText(String.format(Locale.getDefault(), "%s %d, %s %d", getString(R.string.fragment_meals_week_title), selectedPregnancyWeek, getString(R.string.fragment_meals_day_title), daysOfSelectedWeek));

                    selectedDate = sdf.format(date.getTime());

                    currentGlassesCount = getWaterIntakeForDate(date);
                    renderDataAfterCountChange();
                } else {
                    mWeekDayView.setVisibility(View.GONE);
                }

            }
        });

        mWeekDayView = (TextView) rootView.findViewById(R.id.week_day_view);
        mWeekDayView.setText(String.format(Locale.getDefault(), "%s %d, %s %d", getString(R.string.fragment_meals_week_title), currentPregnancyWeek, getString(R.string.fragment_meals_day_title), daysOfCurrentWeek));

        mRemoveBtn = (TextView) rootView.findViewById(R.id.remove_btn);
        mAddBtn = (TextView) rootView.findViewById(R.id.add_btn);
        mWaterGraphic = (ImageView) rootView.findViewById(R.id.water_graphic);
        mInnerGlassesCount = (TextView) rootView.findViewById(R.id.glasses_count);
        mGlassesCountFromRecommended = (TextView) rootView.findViewById(R.id.glasses_count_view);

        mRemoveBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (currentGlassesCount > 0) {
                    currentGlassesCount--;
                    updateUserWaterIntake();
                    renderDataAfterCountChange();
                }
            }
        });

        mAddBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                currentGlassesCount++;
                updateUserWaterIntake();
                renderDataAfterCountChange();
            }
        });

        //initial selected date is current date
        selectedDate = sdf.format(Calendar.getInstance().getTime());
        currentGlassesCount = getWaterIntakeForCurrentDay();
        renderDataAfterCountChange();

        if (mUser.getPregnancies().get(pregnancyIndex).getPregnancyOutcome() != null) {
            mAddBtn.setVisibility(View.GONE);
            mRemoveBtn.setVisibility(View.GONE);
            mWeekDayView.setVisibility(View.GONE);
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

    private int getWaterIntakeForCurrentDay() {
        if (mUser.getPregnancies().get(pregnancyIndex).getWaterIntakes() != null) {
            SimpleDateFormat sdf = new SimpleDateFormat("dd MMM yyyy", Locale.getDefault());
            //the selected date on opening the fragment is always the current date
            String date = sdf.format(Calendar.getInstance().getTime());
            if (mUser.getPregnancies().get(pregnancyIndex).getWaterIntakes().get(date) != null) {
                return mUser.getPregnancies().get(pregnancyIndex).getWaterIntakes().get(date);
            }
        }
        return 0;
    }

    private int getWaterIntakeForDate(Calendar date) {
        if (mUser.getPregnancies().get(pregnancyIndex).getWaterIntakes() != null) {
            SimpleDateFormat sdf = new SimpleDateFormat("dd MMM yyyy", Locale.getDefault());
            String formattedDate = sdf.format(date.getTime());
            if (mUser.getPregnancies().get(pregnancyIndex).getWaterIntakes().get(formattedDate) != null) {
                return mUser.getPregnancies().get(pregnancyIndex).getWaterIntakes().get(formattedDate);
            }
        }
        return 0;
    }

    private void renderDataAfterCountChange() {
        mInnerGlassesCount.setText(String.format(Locale.getDefault(), "%d", currentGlassesCount));
        if (currentGlassesCount > 5) {
            mInnerGlassesCount.setTextColor(ContextCompat.getColor(getActivity(), R.color.colorAccent));
        } else {
            mInnerGlassesCount.setTextColor(ContextCompat.getColor(getActivity(), R.color.colorContrast));
        }
        mGlassesCountFromRecommended.setText(String.format(Locale.getDefault(), "%d/8", currentGlassesCount));

        if (currentGlassesCount < 1) {
            mWaterGraphic.setImageResource(R.drawable.water_0);
        } else if (currentGlassesCount < 4) {
            mWaterGraphic.setImageResource(R.drawable.water_25);
        } else if (currentGlassesCount < 6) {
            mWaterGraphic.setImageResource(R.drawable.water_50);
        } else if (currentGlassesCount < 8) {
            mWaterGraphic.setImageResource(R.drawable.water_75);
        } else {
            mWaterGraphic.setImageResource(R.drawable.water_100);
        }
    }

    private void updateUserWaterIntake() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                if (mUser.getPregnancies().get(pregnancyIndex).getWaterIntakes() == null) {
                    mUser.getPregnancies().get(pregnancyIndex).setWaterIntakes(new HashMap<String, Integer>());
                }

                mUser.getPregnancies().get(pregnancyIndex).getWaterIntakes().put(selectedDate, currentGlassesCount);
                ((HealthStateActivity) getActivity()).updateUserInDb(mUser);
            }
        }).run();
    }
}
