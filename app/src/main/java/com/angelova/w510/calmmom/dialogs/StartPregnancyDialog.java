package com.angelova.w510.calmmom.dialogs;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.SwitchCompat;
import android.view.View;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.angelova.w510.calmmom.ProfileActivity;
import com.angelova.w510.calmmom.R;
import com.angelova.w510.calmmom.models.Examination;
import com.angelova.w510.calmmom.models.Pregnancy;
import com.angelova.w510.calmmom.models.RiskFactor;
import com.angelova.w510.calmmom.models.Tip;
import com.angelova.w510.calmmom.models.User;
import com.angelova.w510.calmmom.models.UserActivity;
import com.angelova.w510.calmmom.models.Weight;
import com.angelova.w510.calmmom.utils.PregnancyUtils;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

public class StartPregnancyDialog extends Dialog {

    private Context activity;
    private User mUser;
    private DialogClickListener listener;

    private LinearLayout mLastMenstruationDateLayout;
    private TextView mLastMenstruationValue;
    private EditText mCurrentWeightView;
    private SwitchCompat mRegularSwitch;
    private EditText mCycleLengthView;
    private EditText mDurationView;
    private SwitchCompat mUnwantedPregnancySwitch;
    private SwitchCompat mBloodIncompatibilitySwitch;
    private CheckBox mSmokeRisk;
    private CheckBox mAlcoholRisk;
    private CheckBox mOverweightRisk;
    private CheckBox mAgeRisk;
    private CheckBox mUnderfeedingRisk;
    private CheckBox mFoodAllergyRisk;
    private CheckBox mMedAllergyRisk;
    private TextView mCancelButton;
    private TextView mSaveButton;

    public interface DialogClickListener {
        void onSave(Pregnancy pregnancy, double currentWeight, boolean isRegular, int menLength, int menDuration, List<RiskFactor> riskFactors, List<Tip> customTips);
    }

    public StartPregnancyDialog(Activity activity, User user, DialogClickListener listener) {
        super(activity);
        this.activity = activity;
        this.mUser = user;
        this.listener = listener;
        this.setCancelable(false);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_start_pregnancy);

        mLastMenstruationDateLayout = (LinearLayout) findViewById(R.id.menstruation_date_layout);
        mLastMenstruationValue = (TextView) findViewById(R.id.last_men_date_text);
        mCurrentWeightView = (EditText) findViewById(R.id.current_weight_input);
        mRegularSwitch = (SwitchCompat) findViewById(R.id.regular_switch);
        mCycleLengthView = (EditText) findViewById(R.id.men_length_input);
        mDurationView = (EditText) findViewById(R.id.men_duration_input);
        mUnwantedPregnancySwitch = (SwitchCompat) findViewById(R.id.unwanted_switch);
        mBloodIncompatibilitySwitch = (SwitchCompat) findViewById(R.id.blood_incompatibility_switch);
        mSmokeRisk = (CheckBox) findViewById(R.id.factor_smoke);
        mAlcoholRisk = (CheckBox) findViewById(R.id.factor_alcohol);
        mOverweightRisk = (CheckBox) findViewById(R.id.factor_overweight);
        mAgeRisk = (CheckBox) findViewById(R.id.factor_age);
        mUnderfeedingRisk = (CheckBox) findViewById(R.id.factor_under_feeding);
        mFoodAllergyRisk = (CheckBox) findViewById(R.id.factor_food_allergy);
        mMedAllergyRisk = (CheckBox) findViewById(R.id.factor_med_allergy);
        mCancelButton = (TextView) findViewById(R.id.cancel_button);
        mSaveButton = (TextView) findViewById(R.id.save_button);

        mCurrentWeightView.setText(String.format(Locale.getDefault(), "%.0f", mUser.getCurrentWeight()));
        mRegularSwitch.setChecked(mUser.isRegularMenstruation());
        mCycleLengthView.setText(String.format(Locale.getDefault(), "%d", mUser.getLengthOfMenstruation()));
        mDurationView.setText(String.format(Locale.getDefault(), "%d", mUser.getDurationOfMenstruation()));
        mBloodIncompatibilitySwitch.setChecked(mUser.getPregnancies().get(mUser.getPregnancyConsecutiveId()).isBloodGroupIncompatibility());
        final List<RiskFactor> riskFactorList = mUser.getRiskFactors();
        if (riskFactorList.contains(RiskFactor.Smoking)) {
            mSmokeRisk.setChecked(true);
        }
        if (riskFactorList.contains(RiskFactor.Alcohol)) {
            mAlcoholRisk.setChecked(true);
        }
        if (riskFactorList.contains(RiskFactor.Overweight)) {
            mOverweightRisk.setChecked(true);
        }
        if (riskFactorList.contains(RiskFactor.Age)) {
            mAgeRisk.setChecked(true);
        }
        if (riskFactorList.contains(RiskFactor.UnderFeeding)) {
            mUnderfeedingRisk.setChecked(true);
        }
        if (riskFactorList.contains(RiskFactor.FoodAllergy)) {
            mFoodAllergyRisk.setChecked(true);
        }
        if (riskFactorList.contains(RiskFactor.MedicinesAllergy)) {
            mMedAllergyRisk.setChecked(true);
        }

        final Calendar currentDate = Calendar.getInstance();
        final Calendar date = Calendar.getInstance();

        final SimpleDateFormat sdf = new SimpleDateFormat("dd.MM.yyyy", Locale.getDefault());
        mLastMenstruationValue.setText(sdf.format(date.getTime()));

        mLastMenstruationDateLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DatePickerDialog datePickerDialog = new DatePickerDialog(activity, R.style.AppTheme_DialogThemeDark, new DatePickerDialog.OnDateSetListener() {

                    @Override
                    public void onDateSet(DatePicker datePicker, int year, int monthOfYear, int dayOfMonth) {
                        date.set(year, monthOfYear, dayOfMonth);
                        mLastMenstruationValue.setText(sdf.format(date.getTime()));
                    }
                }, date.get(Calendar.YEAR), date.get(Calendar.MONTH), date.get(Calendar.DATE));
                datePickerDialog.getDatePicker().setMaxDate(currentDate.getTimeInMillis());
                datePickerDialog.show();
            }
        });

        mCancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dismiss();
            }
        });

        mSaveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if ((mCurrentWeightView.getText() == null || mCurrentWeightView.getText().toString().isEmpty()) ||
                        (mCycleLengthView.getText() == null || mCycleLengthView.getText().toString().isEmpty()) ||
                        (mDurationView.getText() == null || mDurationView.getText().toString().isEmpty())) {
                    if (activity instanceof ProfileActivity) {
                        ((ProfileActivity) activity).showAlertDialogNow(activity.getString(R.string.dialog_start_pregnancy_not_all_data), activity.getString(R.string.dialog_start_pregnancy_not_all_data_title));
                    }
                } else {
                    Pregnancy pregnancy = new Pregnancy();
                    pregnancy.setFirstDayOfLastMenstruation(mLastMenstruationValue.getText().toString());
                    double currentWeight = Double.parseDouble(mCurrentWeightView.getText().toString());
                    Weight weight = new Weight();
                    weight.setValue(currentWeight);
                    weight.setWeek(0);
                    List<Weight> weights = new ArrayList<>();
                    weights.add(weight);
                    pregnancy.setWeights(weights);

                    HashMap<String, List<UserActivity>> activities = PregnancyUtils.getInitialUserActivities();
                    pregnancy.setActivities(activities);

                    List<Examination> mainExaminations = PregnancyUtils.getListWithMainExaminations(activity);
                    pregnancy.setExaminations(mainExaminations);

                    pregnancy.setUnwantedPregnancy(mUnwantedPregnancySwitch.isChecked());
                    pregnancy.setBloodGroupIncompatibility(mBloodIncompatibilitySwitch.isChecked());

                    Resources bgResources = getLocalizedResources(activity, new Locale("bg"));
                    Resources enResources = getLocalizedResources(activity, new Locale("en"));

                    List<Tip> customTips = new ArrayList<>();
                    List<RiskFactor> riskFactors = new ArrayList<>();
                    if (mSmokeRisk.isChecked()) {
                        riskFactors.add(RiskFactor.Smoking);
                        customTips.add(new Tip(null, enResources.getString(R.string.custom_tip_smoking), bgResources.getString(R.string.custom_tip_smoking), true, false));
                    }
                    if (mAlcoholRisk.isChecked()) {
                        riskFactors.add(RiskFactor.Alcohol);
                        customTips.add(new Tip(null, enResources.getString(R.string.custom_tip_alcohol), bgResources.getString(R.string.custom_tip_alcohol), true, false));
                    }
                    if (mOverweightRisk.isChecked()) {
                        riskFactors.add(RiskFactor.Overweight);
                        customTips.add(new Tip(null, enResources.getString(R.string.custom_tip_overweight), bgResources.getString(R.string.custom_tip_overweight), true, false));
                    }
                    if (mAgeRisk.isChecked()) {
                        riskFactors.add(RiskFactor.Age);
                        customTips.add(new Tip(null, enResources.getString(R.string.custom_tip_age), bgResources.getString(R.string.custom_tip_age), true, false));
                    }
                    if (mUnderfeedingRisk.isChecked()) {
                        riskFactors.add(RiskFactor.UnderFeeding);
                        customTips.add(new Tip(null, enResources.getString(R.string.custom_tip_underfeeding), bgResources.getString(R.string.custom_tip_underfeeding), true, false));
                    }
                    if (mFoodAllergyRisk.isChecked()) {
                        riskFactors.add(RiskFactor.FoodAllergy);
                    }
                    if (mMedAllergyRisk.isChecked()) {
                        riskFactors.add(RiskFactor.MedicinesAllergy);
                    }
                    if (mFoodAllergyRisk.isChecked() || mMedAllergyRisk.isChecked()) {
                        customTips.add(new Tip(null, enResources.getString(R.string.custom_tip_allergy), bgResources.getString(R.string.custom_tip_allergy), true, false));
                    }

                    if (mBloodIncompatibilitySwitch.isChecked()) {
                        customTips.add(new Tip(null, enResources.getString(R.string.custom_tip_blood_groups), bgResources.getString(R.string.custom_tip_blood_groups), true, false));
                    }

                    listener.onSave(pregnancy, currentWeight, mRegularSwitch.isChecked(), Integer.parseInt(mCycleLengthView.getText().toString()), Integer.parseInt(mDurationView.getText().toString()), riskFactors, customTips);
                    dismiss();
                }
            }
        });
    }

    @NonNull
    Resources getLocalizedResources(Context context, Locale desiredLocale) {
        Configuration conf = context.getResources().getConfiguration();
        conf = new Configuration(conf);
        conf.setLocale(desiredLocale);
        Context localizedContext = context.createConfigurationContext(conf);
        return localizedContext.getResources();
    }
}
