package com.angelova.w510.calmmom.dialogs;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ScrollView;
import android.widget.Spinner;
import android.widget.TextView;

import com.angelova.w510.calmmom.R;
import com.angelova.w510.calmmom.models.ActivityType;
import com.angelova.w510.calmmom.models.UserActivity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by W510 on 17.8.2018 г..
 */

public class AddActivityDialog extends Dialog {

    private Context activity;
    private DialogClickListener listener;
    private HashMap<String, List<UserActivity>> userActivities;
    private Spinner mWeeksSpinner;
    private ScrollView mActivitiesDetailsScroll;
    private EditText mWalkingHours;
    private EditText mWalkingMinutes;
    private EditText mAerobicsHours;
    private EditText mAerobicsMinutes;
    private EditText mYogaHours;
    private EditText mYogaMinutes;
    private EditText mPilatesHours;
    private EditText mPilatesMinutes;
    private EditText mSwimmingHours;
    private EditText mSwimmingMinutes;
    private EditText mDancingHours;
    private EditText mDancingMinutes;
    private EditText mSCHours;
    private EditText mSCMinutes;
    private EditText mJoggingHours;
    private EditText mJoggingMinutes;
    private EditText mOtherHours;
    private EditText mOtherMinutes;
    private TextView mCancelBtn;
    private TextView mSaveBtn;

    public interface DialogClickListener {
        void onSave(String week, List<UserActivity> activities);
    }

    public AddActivityDialog(Activity activity, HashMap<String, List<UserActivity>> userActivities, DialogClickListener listener) {
        super(activity);
        this.activity = activity;
        this.listener = listener;
        this.userActivities = userActivities;
        this.setCancelable(false);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_add_activity);

        mWeeksSpinner = (Spinner) findViewById(R.id.spinner);
        mActivitiesDetailsScroll = (ScrollView) findViewById(R.id.activities_scroll);
        mWalkingHours = (EditText) findViewById(R.id.walking_hours_value);
        mWalkingMinutes = (EditText) findViewById(R.id.walking_mins_value);
        mAerobicsHours = (EditText) findViewById(R.id.aerobics_hours_value);
        mAerobicsMinutes = (EditText) findViewById(R.id.aerobics_mins_value);
        mYogaHours = (EditText) findViewById(R.id.yoga_hours_value);
        mYogaMinutes = (EditText) findViewById(R.id.yoga_mins_value);
        mPilatesHours = (EditText) findViewById(R.id.pilates_hours_value);
        mPilatesMinutes = (EditText) findViewById(R.id.pilates_mins_value);
        mSwimmingHours = (EditText) findViewById(R.id.swimming_hours_value);
        mSwimmingMinutes = (EditText) findViewById(R.id.swimming_mins_value);
        mDancingHours = (EditText) findViewById(R.id.dancing_hours_value);
        mDancingMinutes = (EditText) findViewById(R.id.dancing_mins_value);
        mSCHours = (EditText) findViewById(R.id.sc_hours_value);
        mSCMinutes = (EditText) findViewById(R.id.sc_mins_value);
        mJoggingHours = (EditText) findViewById(R.id.jogging_hours_value);
        mJoggingMinutes = (EditText) findViewById(R.id.jogging_mins_value);
        mOtherHours = (EditText) findViewById(R.id.other_hours_value);
        mOtherMinutes = (EditText) findViewById(R.id.other_mins_value);
        mCancelBtn = (TextView) findViewById(R.id.cancel_button);
        mSaveBtn = (TextView) findViewById(R.id.save_button);

        String[] weeks = new String[41];
        weeks[0] = "";
        for (int i = 1; i <= 40; i++) {
            weeks[i] = Integer.toString(i);
        }
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(activity, R.layout.custom_spinner_item, weeks);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        mWeeksSpinner.setAdapter(adapter);

        mWeeksSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String selectedItem = parent.getItemAtPosition(position).toString();
                if (!selectedItem.isEmpty()) {
                    mActivitiesDetailsScroll.setVisibility(View.VISIBLE);

                    List<UserActivity> userActivitiesThisWeek = userActivities.get(selectedItem);
                    for (UserActivity act : userActivitiesThisWeek) {
                        int hours = act.getDuration() / 60;
                        int minutes = act.getDuration() % 60;
                        if (act.getType() == ActivityType.Walking) {
                            mWalkingHours.setText(hours);
                            mWalkingMinutes.setText(minutes);
                        } else if (act.getType() == ActivityType.Aerobics) {
                            mAerobicsHours.setText(hours);
                            mAerobicsHours.setText(minutes);
                        } else if (act.getType() == ActivityType.Yoga) {
                            mYogaHours.setText(hours);
                            mYogaMinutes.setText(minutes);
                        } else if (act.getType() == ActivityType.Pilates) {
                            mPilatesHours.setText(hours);
                            mPilatesMinutes.setText(minutes);
                        } else if (act.getType() == ActivityType.Swimming) {
                            mSwimmingHours.setText(hours);
                            mSwimmingMinutes.setText(minutes);
                        } else if (act.getType() == ActivityType.Dancing) {
                            mDancingHours.setText(hours);
                            mDancingMinutes.setText(minutes);
                        } else if (act.getType() == ActivityType.Jogging) {
                            mJoggingHours.setText(hours);
                            mJoggingMinutes.setText(minutes);
                        } else if (act.getType() == ActivityType.StationaryB) {
                            mSCHours.setText(hours);
                            mSCMinutes.setText(minutes);
                        } else if (act.getType() == ActivityType.Other) {
                            mOtherHours.setText(hours);
                            mOtherMinutes.setText(minutes);
                        }
                    }
                } else {
                    mActivitiesDetailsScroll.setVisibility(View.GONE);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        mCancelBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });

        mSaveBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String selectedWeek = mWeeksSpinner.getSelectedItem().toString();
                List<UserActivity> userActivitiesThisWeek = new ArrayList<UserActivity>();

                int walkingDuration = 0;
                if (mWalkingHours.getText() != null && !mWalkingHours.getText().toString().isEmpty()) {
                    walkingDuration += Integer.parseInt(mWalkingHours.getText().toString()) * 60;
                }
                if (mWalkingMinutes.getText() != null && !mWalkingMinutes.getText().toString().isEmpty()) {
                    walkingDuration += Integer.parseInt(mWalkingMinutes.getText().toString());
                }
                userActivitiesThisWeek.add(new UserActivity(walkingDuration, ActivityType.Walking));

                int aerobicsDuration = 0;
                if (mAerobicsHours.getText() != null && !mAerobicsHours.getText().toString().isEmpty()) {
                    aerobicsDuration += Integer.parseInt(mAerobicsHours.getText().toString()) * 60;
                }
                if (mAerobicsMinutes.getText() != null && !mAerobicsMinutes.getText().toString().isEmpty()) {
                    aerobicsDuration += Integer.parseInt(mAerobicsMinutes.getText().toString());
                }
                userActivitiesThisWeek.add(new UserActivity(aerobicsDuration, ActivityType.Aerobics));

                int yogaDuration = 0;
                if (mYogaHours.getText() != null && !mYogaHours.getText().toString().isEmpty()) {
                    yogaDuration += Integer.parseInt(mYogaHours.getText().toString()) * 60;
                }
                if (mYogaMinutes.getText() != null && !mYogaMinutes.getText().toString().isEmpty()) {
                    yogaDuration += Integer.parseInt(mYogaMinutes.getText().toString());
                }
                userActivitiesThisWeek.add(new UserActivity(yogaDuration, ActivityType.Yoga));

                int pilatesDuration = 0;
                if (mPilatesHours.getText() != null && !mPilatesHours.getText().toString().isEmpty()) {
                    pilatesDuration += Integer.parseInt(mPilatesHours.getText().toString()) * 60;
                }
                if (mPilatesMinutes.getText() != null && !mPilatesMinutes.getText().toString().isEmpty()) {
                    pilatesDuration += Integer.parseInt(mPilatesMinutes.getText().toString());
                }
                userActivitiesThisWeek.add(new UserActivity(pilatesDuration, ActivityType.Pilates));

                int swimmingDuration = 0;
                if (mSwimmingHours.getText() != null && !mSwimmingHours.getText().toString().isEmpty()) {
                    swimmingDuration += Integer.parseInt(mSwimmingHours.getText().toString()) * 60;
                }
                if (mSwimmingMinutes.getText() != null && !mSwimmingMinutes.getText().toString().isEmpty()) {
                    swimmingDuration += Integer.parseInt(mSwimmingMinutes.getText().toString());
                }
                userActivitiesThisWeek.add(new UserActivity(swimmingDuration, ActivityType.Swimming));

                int dancingDuration = 0;
                if (mDancingHours.getText() != null && !mDancingHours.getText().toString().isEmpty()) {
                    dancingDuration += Integer.parseInt(mDancingHours.getText().toString()) * 60;
                }
                if (mDancingMinutes.getText() != null && !mDancingMinutes.getText().toString().isEmpty()) {
                    dancingDuration += Integer.parseInt(mDancingMinutes.getText().toString());
                }
                userActivitiesThisWeek.add(new UserActivity(dancingDuration, ActivityType.Dancing));

                int joggingDuration = 0;
                if (mJoggingHours.getText() != null && !mJoggingHours.getText().toString().isEmpty()) {
                    joggingDuration += Integer.parseInt(mJoggingHours.getText().toString()) * 60;
                }
                if (mJoggingMinutes.getText() != null && !mJoggingMinutes.getText().toString().isEmpty()) {
                    joggingDuration += Integer.parseInt(mJoggingMinutes.getText().toString());
                }
                userActivitiesThisWeek.add(new UserActivity(joggingDuration, ActivityType.Jogging));

                int scDuration = 0;
                if (mSCHours.getText() != null && !mSCHours.getText().toString().isEmpty()) {
                    scDuration += Integer.parseInt(mSCHours.getText().toString()) * 60;
                }
                if (mSCMinutes.getText() != null && !mSCMinutes.getText().toString().isEmpty()) {
                    scDuration += Integer.parseInt(mSCMinutes.getText().toString());
                }
                userActivitiesThisWeek.add(new UserActivity(scDuration, ActivityType.StationaryB));

                int otherDuration = 0;
                if (mOtherHours.getText() != null && !mOtherHours.getText().toString().isEmpty()) {
                    otherDuration += Integer.parseInt(mOtherHours.getText().toString()) * 60;
                }
                if (mOtherMinutes.getText() != null && !mOtherMinutes.getText().toString().isEmpty()) {
                    otherDuration += Integer.parseInt(mOtherMinutes.getText().toString());
                }
                userActivitiesThisWeek.add(new UserActivity(otherDuration, ActivityType.Other));

                listener.onSave(selectedWeek, userActivitiesThisWeek);
                dismiss();
            }
        });
    }
}
