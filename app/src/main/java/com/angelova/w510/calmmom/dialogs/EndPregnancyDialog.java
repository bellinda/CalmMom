package com.angelova.w510.calmmom.dialogs;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.v7.widget.AppCompatRadioButton;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.Spinner;
import android.widget.TextView;

import com.angelova.w510.calmmom.ProfileActivity;
import com.angelova.w510.calmmom.R;
import com.angelova.w510.calmmom.models.OutcomeType;
import com.angelova.w510.calmmom.models.PregnancyOutcome;

import java.text.SimpleDateFormat;
import java.util.Locale;

public class EndPregnancyDialog extends Dialog {

    private Context activity;
    private DialogClickListener listener;

    private Spinner mOutcomeTypeSpinner;
    private LinearLayout mCommentsLayout;
    private EditText mCommentsView;
    private ScrollView mLiveBirthDataView;
    private AppCompatRadioButton mBoyRadioButton;
    private AppCompatRadioButton mGirlRadioButton;
    private EditText mBabyNameInput;
    private EditText mBabyWeightInput;
    private EditText mBabyLengthInput;
    private EditText mHoursInput;
    private EditText mComplicationsInput;
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
        mLiveBirthDataView = (ScrollView) findViewById(R.id.live_birth_data);
        mBoyRadioButton = (AppCompatRadioButton) findViewById(R.id.sex_m);
        mGirlRadioButton = (AppCompatRadioButton) findViewById(R.id.sex_f);
        mBabyNameInput = (EditText) findViewById(R.id.baby_name_input);
        mBabyLengthInput = (EditText) findViewById(R.id.baby_length_input);
        mBabyWeightInput = (EditText) findViewById(R.id.baby_weight_input);
        mHoursInput = (EditText) findViewById(R.id.hours_of_labour_input);
        mComplicationsInput = (EditText) findViewById(R.id.complications_value);
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
                    mLiveBirthDataView.setVisibility(View.GONE);
                } else {
                    mCommentsLayout.setVisibility(View.GONE);
                    mLiveBirthDataView.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

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
                    ((ProfileActivity) activity).showAlertDialogNow("Please select the pregnancy outcome", "Warning");
                } else {
                    PregnancyOutcome outcome = new PregnancyOutcome();
                    outcome.setOutcomeType(OutcomeType.fromValue(outcomeType - 1));
                    if (outcomeType != 3) {
                        if (mCommentsView.getText() != null && !mCommentsView.getText().toString().isEmpty()) {
                            outcome.setComments(mCommentsView.getText().toString());
                        }
                    } else  {
                        if (!mBoyRadioButton.isChecked() && !mGirlRadioButton.isChecked()) {
                            ((ProfileActivity) activity).showAlertDialogNow("Please select the sex of the baby", "Warning");
                        } else if (mBabyNameInput.getText() == null || mBabyNameInput.getText().toString().isEmpty()) {
                            ((ProfileActivity) activity).showAlertDialogNow("Please enter the name of the baby", "Warning");
                        } else if (mBabyLengthInput.getText() == null || mBabyLengthInput.getText().toString().isEmpty()) {
                            ((ProfileActivity) activity).showAlertDialogNow("Please enter the length of the baby", "Warning");
                        } else if (mBabyWeightInput.getText() == null || mBabyWeightInput.getText().toString().isEmpty()) {
                            ((ProfileActivity) activity).showAlertDialogNow("Please enter the weight of the bay", "Warning");
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
                        }
                    }
                }
            }
        });
    }
}
