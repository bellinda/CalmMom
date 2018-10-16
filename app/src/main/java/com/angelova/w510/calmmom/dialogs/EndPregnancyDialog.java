package com.angelova.w510.calmmom.dialogs;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.os.Bundle;
import android.support.v7.widget.AppCompatRadioButton;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;

import com.angelova.w510.calmmom.ProfileActivity;
import com.angelova.w510.calmmom.R;
import com.angelova.w510.calmmom.models.AbortionPurpose;
import com.angelova.w510.calmmom.models.OutcomeType;
import com.angelova.w510.calmmom.models.PregnancyOutcome;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

public class EndPregnancyDialog extends Dialog {

    private Context activity;
    private DialogClickListener listener;

    private Spinner mOutcomeTypeSpinner;
    private LinearLayout mCommentsLayout;
    private EditText mCommentsView;
    private LinearLayout mOutcomeDateLayout;
    private TextView mOutcomeDateView;
    private ScrollView mLiveBirthDataView;
    private LinearLayout mBirthDateLayout;
    private TextView mBirthDateView;
    private LinearLayout mBirthTimeLayout;
    private TextView mBirthTimeView;
    private AppCompatRadioButton mBoyRadioButton;
    private AppCompatRadioButton mGirlRadioButton;
    private EditText mBabyNameInput;
    private EditText mBabyWeightInput;
    private EditText mBabyLengthInput;
    private EditText mHoursInput;
    private EditText mComplicationsInput;
    private LinearLayout mAbortionPurposeLayout;
    private AppCompatRadioButton mDesiredAbortion;
    private AppCompatRadioButton mMedPurposeAbortion;
    private TextView mCancelBtn;
    private TextView mSaveBtn;

    public interface DialogClickListener {
        void onSave(PregnancyOutcome outcome);
    }

    public EndPregnancyDialog(Activity activity, DialogClickListener listener) {
        super(activity);
        this.activity = activity;
        this.listener = listener;
        this.setCancelable(false);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_end_pregnancy);

        mOutcomeTypeSpinner = (Spinner) findViewById(R.id.spinner);
        mCommentsLayout = (LinearLayout) findViewById(R.id.comments_layout);
        mCommentsView = (EditText) findViewById(R.id.comments_view);
        mOutcomeDateLayout = (LinearLayout) findViewById(R.id.outcome_date_layout);
        mOutcomeDateView = (TextView) findViewById(R.id.outcome_date_text);
        mLiveBirthDataView = (ScrollView) findViewById(R.id.live_birth_data);
        mBirthDateLayout = (LinearLayout) findViewById(R.id.birth_date_layout);
        mBirthDateView = (TextView) findViewById(R.id.birth_date_text);
        mBirthTimeLayout = (LinearLayout) findViewById(R.id.birth_time_layout);
        mBirthTimeView = (TextView) findViewById(R.id.birth_time_text);
        mBoyRadioButton = (AppCompatRadioButton) findViewById(R.id.sex_m);
        mGirlRadioButton = (AppCompatRadioButton) findViewById(R.id.sex_f);
        mBabyNameInput = (EditText) findViewById(R.id.baby_name_input);
        mBabyLengthInput = (EditText) findViewById(R.id.baby_length_input);
        mBabyWeightInput = (EditText) findViewById(R.id.baby_weight_input);
        mHoursInput = (EditText) findViewById(R.id.hours_of_labour_input);
        mComplicationsInput = (EditText) findViewById(R.id.complications_value);
        mAbortionPurposeLayout = (LinearLayout) findViewById(R.id.abortion_purpose_layout);
        mDesiredAbortion = (AppCompatRadioButton) findViewById(R.id.desired);
        mMedPurposeAbortion = (AppCompatRadioButton) findViewById(R.id.med_purpose);
        mCancelBtn = (TextView) findViewById(R.id.cancel_button);
        mSaveBtn = (TextView) findViewById(R.id.save_button);

        String[] outcomes = {
                "",
                activity.getString(R.string.dialog_end_pregnancy_outcome_abortion),
                activity.getString(R.string.dialog_end_pregnancy_outcome_miscarriage),
                activity.getString(R.string.dialog_end_pregnancy_outcome_live),
                activity.getString(R.string.dialog_end_pregnancy_outcome_still)
        };

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(activity, R.layout.custom_spinner_item, outcomes);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        mOutcomeTypeSpinner.setAdapter(adapter);

        mOutcomeTypeSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int position, long l) {
                if (position == 0) {

                } else if (position == 1 || position == 2 || position == 4) {
                    mCommentsLayout.setVisibility(View.VISIBLE);
                    mOutcomeDateLayout.setVisibility(View.VISIBLE);
                    mLiveBirthDataView.setVisibility(View.GONE);
                    if (position == 1) {
                        mAbortionPurposeLayout.setVisibility(View.VISIBLE);
                    }
                } else {
                    mCommentsLayout.setVisibility(View.GONE);
                    mOutcomeDateLayout.setVisibility(View.GONE);
                    mLiveBirthDataView.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        final Calendar currentDate = Calendar.getInstance();
        final Calendar date = Calendar.getInstance();

        final SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
        mBirthDateView.setText(sdf.format(date.getTime()));
        mOutcomeDateView.setText(sdf.format(date.getTime()));

        mBirthDateLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DatePickerDialog datePickerDialog = new DatePickerDialog(activity, R.style.AppTheme_DialogThemeDark, new DatePickerDialog.OnDateSetListener() {

                    @Override
                    public void onDateSet(DatePicker datePicker, int year, int monthOfYear, int dayOfMonth) {
                        date.set(year, monthOfYear, dayOfMonth);
                        mBirthDateView.setText(sdf.format(date.getTime()));
                    }
                }, date.get(Calendar.YEAR), date.get(Calendar.MONTH), date.get(Calendar.DATE));
                datePickerDialog.show();
            }
        });

        mBirthTimeLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new TimePickerDialog(activity, R.style.AppTheme_DialogThemeDark, new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                        date.set(Calendar.HOUR_OF_DAY, hourOfDay);
                        date.set(Calendar.MINUTE, minute);
                        SimpleDateFormat sdf = new SimpleDateFormat("hh:mm", Locale.getDefault());
                        mBirthTimeView.setText(sdf.format(date.getTime()));
                    }
                },date.get(Calendar.HOUR_OF_DAY), date.get(Calendar.MINUTE), false).show();
            }
        });

        mOutcomeDateLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DatePickerDialog datePickerDialog = new DatePickerDialog(activity, R.style.AppTheme_DialogThemeDark, new DatePickerDialog.OnDateSetListener() {

                    @Override
                    public void onDateSet(DatePicker datePicker, int year, int monthOfYear, int dayOfMonth) {
                        date.set(year, monthOfYear, dayOfMonth);
                        mOutcomeDateView.setText(sdf.format(date.getTime()));
                    }
                }, date.get(Calendar.YEAR), date.get(Calendar.MONTH), date.get(Calendar.DATE));
                datePickerDialog.show();
            }
        });

        mCancelBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dismiss();
            }
        });

        mSaveBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int outcomeType = mOutcomeTypeSpinner.getSelectedItemPosition();
                if (outcomeType == 0) {
                    ((ProfileActivity) activity).showAlertDialogNow(activity.getString(R.string.dialog_end_pregnancy_no_outcome),
                            activity.getString(R.string.dialog_end_pregnancy_missing_data_title));
                } else {
                    PregnancyOutcome outcome = new PregnancyOutcome();
                    outcome.setOutcomeType(OutcomeType.fromValue(outcomeType - 1));
                    if (outcomeType != 3) {
                        if (mAbortionPurposeLayout.getVisibility() == View.VISIBLE && !mDesiredAbortion.isChecked() && !mMedPurposeAbortion.isChecked()) {
                            ((ProfileActivity) activity).showAlertDialogNow(activity.getString(R.string.dialog_end_pregnancy_no_abortion_purpose), activity.getString(R.string.dialog_end_pregnancy_missing_data_title));
                        } else {
                            if (mCommentsView.getText() != null && !mCommentsView.getText().toString().isEmpty()) {
                                outcome.setComments(mCommentsView.getText().toString());
                            }
                            outcome.setDate(mOutcomeDateView.getText().toString());
                            if (outcomeType == 1) {

                                if (mDesiredAbortion.isChecked()) {
                                    outcome.setAbortionPurpose(AbortionPurpose.Desired);
                                } else {
                                    outcome.setAbortionPurpose(AbortionPurpose.MedicalEvidence);
                                }
                            }

                            listener.onSave(outcome);
                            dismiss();
                        }
                    } else  {
                        if (!mBoyRadioButton.isChecked() && !mGirlRadioButton.isChecked()) {
                            ((ProfileActivity) activity).showAlertDialogNow(activity.getString(R.string.dialog_end_pregnancy_no_sex), activity.getString(R.string.dialog_end_pregnancy_missing_data_title));
                        } else if (mBabyNameInput.getText() == null || mBabyNameInput.getText().toString().isEmpty()) {
                            ((ProfileActivity) activity).showAlertDialogNow(activity.getString(R.string.dialog_end_pregnancy_no_name), activity.getString(R.string.dialog_end_pregnancy_missing_data_title));
                        } else if (mBabyLengthInput.getText() == null || mBabyLengthInput.getText().toString().isEmpty()) {
                            ((ProfileActivity) activity).showAlertDialogNow(activity.getString(R.string.dialog_end_pregnancy_no_length), activity.getString(R.string.dialog_end_pregnancy_missing_data_title));
                        } else if (mBabyWeightInput.getText() == null || mBabyWeightInput.getText().toString().isEmpty()) {
                            ((ProfileActivity) activity).showAlertDialogNow(activity.getString(R.string.dialog_end_pregnancy_no_weight), activity.getString(R.string.dialog_end_pregnancy_missing_data_title));
                        } else {
                            outcome.setBabySex(mBoyRadioButton.isChecked() ? "m" : "f");
                            outcome.setBabyName(mBabyNameInput.getText().toString());
                            outcome.setBabyLength(Integer.parseInt(mBabyLengthInput.getText().toString()));
                            outcome.setBabyWeight(mBabyWeightInput.getText().toString());
                            if (mHoursInput.getText() != null && !mHoursInput.getText().toString().isEmpty()) {
                                outcome.setHoursOfLabour(Integer.parseInt(mHoursInput.getText().toString()));
                            }
                            if (mComplicationsInput.getText() != null && !mComplicationsInput.getText().toString().isEmpty()) {
                                outcome.setComplications(mComplicationsInput.getText().toString());
                            }
                            outcome.setDate(mBirthDateView.getText().toString());
                            outcome.setTime(mBirthTimeView.getText().toString());

                            listener.onSave(outcome);
                            dismiss();
                        }
                    }
                }
            }
        });
    }
}
