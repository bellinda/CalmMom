package com.angelova.w510.calmmom.fragments;

import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;

import com.angelova.w510.calmmom.R;
import com.angelova.w510.calmmom.models.User;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.concurrent.TimeUnit;

import devs.mulham.horizontalcalendar.HorizontalCalendar;
import devs.mulham.horizontalcalendar.HorizontalCalendarView;
import devs.mulham.horizontalcalendar.utils.HorizontalCalendarListener;

/**
 * Created by W510 on 16.9.2018 г..
 */

public class MealsFragment extends Fragment {

    private HorizontalCalendar horizontalCalendar;

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

            }
        });

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
}
