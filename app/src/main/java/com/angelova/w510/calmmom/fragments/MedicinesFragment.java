package com.angelova.w510.calmmom.fragments;

import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.content.res.ResourcesCompat;
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
import com.angelova.w510.calmmom.adapters.MedicinesAdapter;
import com.angelova.w510.calmmom.dialogs.IsAllowedMedDialog;
import com.angelova.w510.calmmom.dialogs.TakenMedicineDialog;
import com.angelova.w510.calmmom.models.Medicine;
import com.angelova.w510.calmmom.models.User;
import com.github.sundeepk.compactcalendarview.CompactCalendarView;
import com.github.sundeepk.compactcalendarview.domain.Event;
import com.melnykov.fab.FloatingActionButton;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Locale;

/**
 * Created by W510 on 31.8.2018 г..
 */

public class MedicinesFragment extends Fragment {

    private CompactCalendarView mCalendar;
    private TextView mMonthYearView;
    private TextView mNoDateSelectedView;
    private TextView mNoDataForTakenMedicinesView;
    private FloatingActionButton mAddTakenMedicineBtn;

    private List<Medicine> mMedicines = new ArrayList<>();
    private MedicinesAdapter mAdapter;
    private RecyclerView mRecyclerView;

    private Date mCurrentSelectedDate;

    private User mUser;
    private int pregnancyIndex;

    private SimpleDateFormat dateFormatForMonth = new SimpleDateFormat("MMM yyyy", Locale.getDefault());

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_medicines, container, false);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = getActivity().getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(Color.parseColor("#283A4B"));
        }

        mUser = (User) getArguments().getSerializable("user");
        pregnancyIndex = mUser.getPregnancyConsecutiveId();

        mCalendar = (CompactCalendarView) rootView.findViewById(R.id.compactcalendar_view);
        mMonthYearView = (TextView) rootView.findViewById(R.id.month_year_view);
        mNoDateSelectedView = (TextView) rootView.findViewById(R.id.no_date_selected_view);
        mNoDataForTakenMedicinesView = (TextView) rootView.findViewById(R.id.no_medicines_taken_on_this_date_view);
        mAddTakenMedicineBtn = (FloatingActionButton) rootView.findViewById(R.id.add_taken_medicine_btn);

        mRecyclerView = (RecyclerView) rootView.findViewById(R.id.recyclerView);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false));
        mRecyclerView.setHasFixedSize(true);

        loadAllEventsOnTheCalendar();

        loadDataForCurrentDate();

        mCalendar.shouldSelectFirstDayOfMonthOnScroll(false);

        mMonthYearView.setText(dateFormatForMonth.format(mCalendar.getFirstDayOfCurrentMonth()));

        //set title on calendar scroll
        mCalendar.setListener(new CompactCalendarView.CompactCalendarViewListener() {
            @Override
            public void onDayClick(Date dateClicked) {
                mMedicines = new ArrayList<Medicine>();
                mCurrentSelectedDate = dateClicked;

                mMonthYearView.setText(dateFormatForMonth.format(dateClicked));
                mNoDateSelectedView.setVisibility(View.GONE);
                mAddTakenMedicineBtn.setVisibility(View.VISIBLE);

                List<Event> events = mCalendar.getEvents(dateClicked);
                if (events.size() > 0) {
                    mNoDataForTakenMedicinesView.setVisibility(View.GONE);

                    for (Event event : events) {
                        mMedicines.add((Medicine)event.getData());
                    }
                    Collections.sort(mMedicines);
                    mAdapter = new MedicinesAdapter(mMedicines, getAllowedMedicineTitles(), getActivity());
                    mRecyclerView.setAdapter(mAdapter);
                    mRecyclerView.setVisibility(View.VISIBLE);
                } else {
                    mRecyclerView.setVisibility(View.GONE);
                    mNoDataForTakenMedicinesView.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onMonthScroll(Date firstDayOfNewMonth) {
                mMonthYearView.setText(dateFormatForMonth.format(firstDayOfNewMonth));
            }
        });

        mAddTakenMedicineBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TakenMedicineDialog dialog = new TakenMedicineDialog(getActivity(), mCurrentSelectedDate, mUser.getPregnancies().get(pregnancyIndex).getAllowedMedicinesByDoctor(), new TakenMedicineDialog.DialogClickListener() {
                    @Override
                    public void onSave(Medicine medicine, boolean isInAllowedList) {
                        if (mUser.getPregnancies().get(pregnancyIndex).getTakenMedicines() == null) {
                            mUser.getPregnancies().get(pregnancyIndex).setTakenMedicines(new ArrayList<Medicine>());
                        }
                        mUser.getPregnancies().get(pregnancyIndex).getTakenMedicines().add(medicine);
                        ((HealthStateActivity) getActivity()).updateUserInDb(mUser);

                        addEventOnTheCalendar(medicine);
                        if (mNoDataForTakenMedicinesView.getVisibility() == View.VISIBLE) {
                            mMedicines = new ArrayList<Medicine>();
                        }
                        mMedicines.add(medicine);
                        Collections.sort(mMedicines);
                        if (mAdapter != null) {
                            if (mNoDataForTakenMedicinesView.getVisibility() == View.VISIBLE) {
                                mAdapter = new MedicinesAdapter(mMedicines, getAllowedMedicineTitles(), getActivity());
                                mRecyclerView.setAdapter(mAdapter);
                                mNoDataForTakenMedicinesView.setVisibility(View.GONE);
                                mRecyclerView.setVisibility(View.VISIBLE);
                            } else {
                                mAdapter.notifyDataSetChanged();
                            }
                        } else {
                            mAdapter = new MedicinesAdapter(mMedicines, getAllowedMedicineTitles(), getActivity());
                            mRecyclerView.setAdapter(mAdapter);
                            mNoDataForTakenMedicinesView.setVisibility(View.GONE);
                            mRecyclerView.setVisibility(View.VISIBLE);
                        }

                        if (!isInAllowedList) {
                            showAddInAllowedListDialog(medicine);
                        }
                    }
                });
                dialog.show();
            }
        });

        return rootView;
    }

    private void showAddInAllowedListDialog(Medicine medicine) {
        IsAllowedMedDialog dialog = new IsAllowedMedDialog(getActivity(), medicine, new IsAllowedMedDialog.DialogClickListener() {
            @Override
            public void addToAllowedList(Medicine medicine) {
                if (mUser.getPregnancies().get(pregnancyIndex).getAllowedMedicinesByDoctor() == null) {
                    mUser.getPregnancies().get(pregnancyIndex).setAllowedMedicinesByDoctor(new ArrayList<Medicine>());
                }
                mUser.getPregnancies().get(pregnancyIndex).getAllowedMedicinesByDoctor().add(medicine);
                ((HealthStateActivity) getActivity()).updateUserInDb(mUser);

                mAdapter = new MedicinesAdapter(mMedicines, getAllowedMedicineTitles(), getActivity());
                mRecyclerView.setAdapter(mAdapter);
            }
        });

        dialog.show();
    }

    private void loadAllEventsOnTheCalendar() {
        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat sdf = new SimpleDateFormat("dd MMM yyyy kk:mm", Locale.getDefault());

        if (mUser.getPregnancies().get(pregnancyIndex).getTakenMedicines() != null) {

            for (Medicine medicine : mUser.getPregnancies().get(pregnancyIndex).getTakenMedicines()) {
                try {
                    Date date = sdf.parse(String.format("%s %s", medicine.getTakenOn(), medicine.getTime()));
                    calendar.setTime(date);

                    Event ev1 = new Event(ResourcesCompat.getColor(getResources(), R.color.colorPrimaryTransparent, null), calendar.getTimeInMillis(), medicine);
                    mCalendar.addEvent(ev1);
                } catch (ParseException pe) {
                    pe.printStackTrace();
                }
            }
        }
    }

    private void loadDataForCurrentDate() {
        Calendar calendar = Calendar.getInstance();
        mMedicines = new ArrayList<Medicine>();
        mCurrentSelectedDate = calendar.getTime();

        mNoDateSelectedView.setVisibility(View.GONE);
        mAddTakenMedicineBtn.setVisibility(View.VISIBLE);

        List<Event> events = mCalendar.getEvents(mCurrentSelectedDate);
        if (events.size() > 0) {
            mNoDataForTakenMedicinesView.setVisibility(View.GONE);

            for (Event event : events) {
                mMedicines.add((Medicine)event.getData());
            }
            Collections.sort(mMedicines);
            mAdapter = new MedicinesAdapter(mMedicines, getAllowedMedicineTitles(), getActivity());
            mRecyclerView.setAdapter(mAdapter);
            mRecyclerView.setVisibility(View.VISIBLE);
        } else {
            mRecyclerView.setVisibility(View.GONE);
            mNoDataForTakenMedicinesView.setVisibility(View.VISIBLE);
        }
    }

    private void addEventOnTheCalendar(Medicine medicine) {
        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat sdf = new SimpleDateFormat("dd MMM yyyy kk:mm", Locale.getDefault());
        try {
            Date date = sdf.parse(String.format("%s %s", medicine.getTakenOn(), medicine.getTime()));
            calendar.setTime(date);

            Event ev1 = new Event(ResourcesCompat.getColor(getResources(), R.color.colorPrimaryTransparent, null), calendar.getTimeInMillis(), medicine);
            mCalendar.addEvent(ev1);
        } catch (ParseException pe) {
            pe.printStackTrace();
        }
    }

    private List<String> getAllowedMedicineTitles() {
        List<String> allowedMedicineTitles = new ArrayList<>();
        for (Medicine medicine : mUser.getPregnancies().get(pregnancyIndex).getAllowedMedicinesByDoctor()) {
            allowedMedicineTitles.add(medicine.getTitle());
        }
        return allowedMedicineTitles;
    }

    public void removeMedicineFromListAndCalendar(Medicine medicine) {
        mMedicines.remove(medicine);
        mAdapter.notifyDataSetChanged();

        Event event = getCurrentMedicineEvent(medicine);
        if (event != null) {
            mCalendar.removeEvent(event);
        }

        if (mMedicines.size() == 0) {
            mNoDataForTakenMedicinesView. setVisibility(View.VISIBLE);
            mRecyclerView.setVisibility(View.GONE);
        }
    }

    private Event getCurrentMedicineEvent(Medicine medicine) {
        SimpleDateFormat sdf = new SimpleDateFormat("dd MMM yyyy", Locale.getDefault());
        try {
            Date takenOn = sdf.parse(medicine.getTakenOn());
            for (Event event : mCalendar.getEvents(takenOn)) {
                if (event.getData().equals(medicine)) {
                    return event;
                }
            }
        } catch (ParseException pe) {
            pe.printStackTrace();
        }
        return null;
    }
}
