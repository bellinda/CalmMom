package com.angelova.w510.calmmom;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.SwitchCompat;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.angelova.w510.calmmom.dialogs.WarnDialog;
import com.angelova.w510.calmmom.models.Examination;
import com.angelova.w510.calmmom.models.ExaminationStatus;
import com.angelova.w510.calmmom.models.FamilyHistory;
import com.angelova.w510.calmmom.models.Measurement;
import com.angelova.w510.calmmom.models.Pregnancy;
import com.angelova.w510.calmmom.models.RiskFactor;
import com.angelova.w510.calmmom.models.Test;
import com.angelova.w510.calmmom.models.Tip;
import com.angelova.w510.calmmom.models.User;
import com.angelova.w510.calmmom.models.UserActivity;
import com.angelova.w510.calmmom.models.Weight;
import com.angelova.w510.calmmom.utils.PregnancyUtils;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;

public class InfoActivity extends AppCompatActivity {

    private CircleImageView mProfileImage;
    private TextView mProfileText;

    private EditText mInputName;
    private EditText mInputAge;
    private EditText mInputCurrHeight;
    private EditText mInputCurrWeight;
    //private EditText mInputFirstDayOfLastM;
    private LinearLayout mFirstDayOfMLayout;
    private TextView mFirstDayOfMText;
    private SwitchCompat mRegularSwitch;
    private EditText mInputMenstruationLength;
    private EditText mInputMenstruationDuration;
    private EditText mInfectiousDiseases;
    private EditText mOtherIllnesses;
    private EditText mSurgeries;
    private EditText mFamilyHistory;
    private EditText mFeaturesAndComplications;

    private CheckBox mSmokeChkBox;
    private CheckBox mAlcoholChkBox;
    private CheckBox mOverweightChkBox;
    private CheckBox mAgeChkBox;
    private CheckBox mUnderfeedingChkBox;
    private CheckBox mFoodAllChkBox;
    private CheckBox mMedAllChkBox;

    private SwitchCompat mFirstPregnancySwitch;
    private LinearLayout mPregnancyCountLayout;
    private LinearLayout mLastDeliveryLayout;
    private LinearLayout mLiveBornKidsLayout;
    private LinearLayout mStillbornKidsLayout;
    private LinearLayout mPrematureLayout;
    private LinearLayout mPosttermLayout;
    private EditText mPregnancyCountInput;
    private TextView mLastDeliveryDateInput;
    private EditText mLiveBornKidsInput;
    private EditText mStillbornKidsInput;
    private EditText mPrematureInput;
    private EditText mPosttermInput;
    private EditText mSterilityInput;

    private SwitchCompat mAbortionSwitch;
    private LinearLayout mDesiredAbortionsLayout;
    private LinearLayout mMiscarriagesLayout;
    private LinearLayout mMedicalAbortionsLayout;
    private EditText mDesiredInput;
    private EditText mMiscarriagesInput;
    private EditText mMedicalInput;

    private EditText mTakenMedicinesInput;
    private SwitchCompat mBloodGrIncompatibilitySwitch;
    private SwitchCompat mUnwantedPregnancySwitch;

    private Button mSubmitBtn;
    private ProgressBar mSubmitLoader;

    private User mUser;
    private String mUserEmail;
    private Pregnancy mPregnancy;
    private FirebaseFirestore mDb;
    FirebaseStorage mStorage;
    StorageReference mStorageReference;

    private Uri mFilePath;

    private boolean isLoading = false;
    final Calendar mLastDeliveryDate = Calendar.getInstance();
    final Calendar mFirstDayOfLastMDate = Calendar.getInstance();
    final Calendar mCurrentDate = Calendar.getInstance();

    private static final int SELECT_FILE = 1023;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_info);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(Color.parseColor("#324A5F"));
        }

        initializeActivity();
    }

    private void initializeActivity() {
        mUser = new User();
        mDb = FirebaseFirestore.getInstance();
        mStorage = FirebaseStorage.getInstance();
        mStorageReference = mStorage.getReference();
        mUserEmail = getIntent().getStringExtra("email");

        mPregnancy = new Pregnancy();

        mProfileImage = (CircleImageView) findViewById(R.id.profile_image);
        mProfileText = (TextView) findViewById(R.id.profile_text);

        mProfileImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(ActivityCompat.checkSelfPermission(InfoActivity.this, android.Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions(InfoActivity.this,
                            new String[]{android.Manifest.permission.READ_EXTERNAL_STORAGE}, SELECT_FILE);
                }
                else {
                    openGallery();
                }
            }
        });

        mInputName = (EditText) findViewById(R.id.input_name);
        mInputAge = (EditText) findViewById(R.id.input_age);
        mInputCurrHeight = (EditText) findViewById(R.id.input_height);
        mInputCurrWeight = (EditText) findViewById(R.id.input_weight);
        mFirstDayOfMLayout = (LinearLayout) findViewById(R.id.menstruation_layout);
        mFirstDayOfMText = (TextView) findViewById(R.id.menstruation_text);
        mFirstDayOfMLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DatePickerDialog datePickerDialog = new DatePickerDialog(InfoActivity.this, R.style.AppTheme_DialogThemeDark, new DatePickerDialog.OnDateSetListener() {

                    @Override
                    public void onDateSet(DatePicker datePicker, int year, int monthOfYear, int dayOfMonth) {
                        mFirstDayOfLastMDate.set(year, monthOfYear, dayOfMonth);
                        SimpleDateFormat sdf = new SimpleDateFormat("dd.MM.yyyy");
                        mFirstDayOfMText.setText(sdf.format(mFirstDayOfLastMDate.getTime()));
                        mPregnancy.setFirstDayOfLastMenstruation(mFirstDayOfMText.getText().toString());
                    }
                }, mFirstDayOfLastMDate.get(Calendar.YEAR), mFirstDayOfLastMDate.get(Calendar.MONTH), mFirstDayOfLastMDate.get(Calendar.DATE));
                datePickerDialog.getDatePicker().setMaxDate(mCurrentDate.getTimeInMillis());
                datePickerDialog.show();
            }
        });
        mRegularSwitch = (SwitchCompat) findViewById(R.id.regular_switch);
        mInputMenstruationLength = (EditText) findViewById(R.id.input_menstruation_length);
        mInputMenstruationDuration = (EditText) findViewById(R.id.input_menstruation_duration);
        mSmokeChkBox = (CheckBox) findViewById(R.id.factor_smoke);
        mAlcoholChkBox = (CheckBox) findViewById(R.id.factor_alcohol);
        mOverweightChkBox = (CheckBox) findViewById(R.id.factor_overweight);
        mAgeChkBox = (CheckBox) findViewById(R.id.factor_age);
        mUnderfeedingChkBox = (CheckBox) findViewById(R.id.factor_under_feeding);
        mFoodAllChkBox = (CheckBox) findViewById(R.id.factor_food_allergy);
        mMedAllChkBox = (CheckBox) findViewById(R.id.factor_med_allergy);
        mFirstPregnancySwitch = (SwitchCompat) findViewById(R.id.first_pregnancy_switch);
        mPregnancyCountLayout = (LinearLayout) findViewById(R.id.cons_pregnancy_layout);
        mLastDeliveryLayout = (LinearLayout) findViewById(R.id.last_delivery_layout);
        mLiveBornKidsLayout = (LinearLayout) findViewById(R.id.live_kids_layout);
        mStillbornKidsLayout = (LinearLayout) findViewById(R.id.lstillborn_kids_layout);
        mPrematureLayout = (LinearLayout) findViewById(R.id.premature_layout);
        mPosttermLayout = (LinearLayout) findViewById(R.id.postmature_layout);
        mPregnancyCountInput = (EditText) findViewById(R.id.consequtive_pregnancy);
        mLastDeliveryDateInput = (TextView) findViewById(R.id.last_delivery_text);
        mLiveBornKidsInput = (EditText) findViewById(R.id.live_kids_count);
        mStillbornKidsInput = (EditText) findViewById(R.id.stillborn_kids_count);
        mPrematureInput = (EditText) findViewById(R.id.premature_kids_count);
        mPosttermInput = (EditText) findViewById(R.id.postmature_kids_count);
        mFirstPregnancySwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    mPregnancyCountLayout.setVisibility(View.GONE);
                    mLastDeliveryLayout.setVisibility(View.GONE);
                    mLiveBornKidsLayout.setVisibility(View.GONE);
                    mStillbornKidsLayout.setVisibility(View.GONE);
                    mPrematureLayout.setVisibility(View.GONE);
                    mPosttermLayout.setVisibility(View.GONE);
                } else {
                    mPregnancyCountLayout.setVisibility(View.VISIBLE);
                    mLastDeliveryLayout.setVisibility(View.VISIBLE);
                    mLiveBornKidsLayout.setVisibility(View.VISIBLE);
                    mStillbornKidsLayout.setVisibility(View.VISIBLE);
                    mPrematureLayout.setVisibility(View.VISIBLE);
                    mPosttermLayout.setVisibility(View.VISIBLE);
                }
            }
        });
        mLastDeliveryLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DatePickerDialog datePickerDialog = new DatePickerDialog(InfoActivity.this, R.style.AppTheme_DialogThemeDark, new DatePickerDialog.OnDateSetListener() {

                    @Override
                    public void onDateSet(DatePicker datePicker, int year, int monthOfYear, int dayOfMonth) {
                        mLastDeliveryDate.set(year, monthOfYear, dayOfMonth);
                        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
                        mLastDeliveryDateInput.setText(sdf.format(mLastDeliveryDate.getTime()));
                        mUser.setLastDeliveryDate(mLastDeliveryDateInput.getText().toString());
                    }
                }, mLastDeliveryDate.get(Calendar.YEAR), mLastDeliveryDate.get(Calendar.MONTH), mLastDeliveryDate.get(Calendar.DATE));
                datePickerDialog.getDatePicker().setMaxDate(mCurrentDate.getTimeInMillis());
                datePickerDialog.show();
            }
        });
        mSterilityInput = (EditText) findViewById(R.id.input_sterility);
        mAbortionSwitch = (SwitchCompat) findViewById(R.id.abortion_switch);
        mDesiredAbortionsLayout = (LinearLayout) findViewById(R.id.desired_layout);
        mMiscarriagesLayout = (LinearLayout) findViewById(R.id.miscarriages_layout);
        mMedicalAbortionsLayout = (LinearLayout) findViewById(R.id.medical_layout);
        mDesiredInput = (EditText) findViewById(R.id.desired_count);
        mMiscarriagesInput = (EditText) findViewById(R.id.miscarriages_count);
        mMedicalInput = (EditText) findViewById(R.id.medical_count);
        mAbortionSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    mDesiredAbortionsLayout.setVisibility(View.VISIBLE);
                    mMiscarriagesLayout.setVisibility(View.VISIBLE);
                    mMedicalAbortionsLayout.setVisibility(View.VISIBLE);
                } else {
                    mDesiredAbortionsLayout.setVisibility(View.GONE);
                    mMiscarriagesLayout.setVisibility(View.GONE);
                    mMedicalAbortionsLayout.setVisibility(View.GONE);
                }
            }
        });

        mInfectiousDiseases = (EditText) findViewById(R.id.input_infectious_diseases);
        mOtherIllnesses = (EditText) findViewById(R.id.input_other_illnesses);
        mSurgeries = (EditText) findViewById(R.id.input_surgeries);
        mFamilyHistory = (EditText) findViewById(R.id.input_family_history);
        mFeaturesAndComplications = (EditText) findViewById(R.id.input_features_complications);
        mTakenMedicinesInput = (EditText) findViewById(R.id.input_medicines);
        mBloodGrIncompatibilitySwitch = (SwitchCompat) findViewById(R.id.blood_group_incomp_switch);
        mUnwantedPregnancySwitch = (SwitchCompat) findViewById(R.id.unwanted_switch);

        mSubmitBtn = (Button) findViewById(R.id.submit_btn);
        mSubmitLoader = (ProgressBar) findViewById(R.id.submit_loader);
        mSubmitBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mInputName.getText() == null || mInputName.getText().toString().isEmpty()) {
                    showAlertDialogNow("Please input your name", "Warning");
                } else if (mInputAge.getText() == null || mInputAge.getText().toString().isEmpty()) {
                    showAlertDialogNow("Please input your age", "Warning");
                } else if (mInputCurrHeight.getText() == null || mInputCurrHeight.getText().toString().isEmpty()) {
                    showAlertDialogNow("Please input your current height", "Warning");
                } else if (mInputCurrWeight.getText() == null || mInputCurrWeight.getText().toString().isEmpty()) {
                    showAlertDialogNow("Please input your current weight", "Warning");
                } else if (mFirstDayOfMText.getText() == null || mFirstDayOfMText.getText().toString().isEmpty()) {
                    showAlertDialogNow("Please input the first day of your last menstruation cycle", "Warning");
                } else if (mInputMenstruationLength.getText() == null || mInputMenstruationLength.getText().toString().isEmpty()) {
                    showAlertDialogNow("Please enter the length of your menstruation cycle", "Warning");
                } else if (mInputMenstruationDuration.getText() == null || mInputMenstruationDuration.getText().toString().isEmpty()) {
                    showAlertDialogNow("Please enter the average duration of your menstruation", "Warning");
                } else {
                    List<Tip> customTips = new ArrayList<>();
                    mUser.setName(mInputName.getText().toString());
                    mUser.setAge(Integer.parseInt(mInputAge.getText().toString()));
                    mUser.setCurrentHeight(Double.parseDouble(mInputCurrHeight.getText().toString()));
                    mUser.setCurrentWeight(Double.parseDouble(mInputCurrWeight.getText().toString()));
                    mUser.setRegularMenstruation(mRegularSwitch.isChecked());
                    mUser.setLengthOfMenstruation(Integer.parseInt(mInputMenstruationLength.getText().toString()));
                    mUser.setDurationOfMenstruation(Integer.parseInt(mInputMenstruationDuration.getText().toString()));

                    List<Examination> mainExaminations = PregnancyUtils.getListWithMainExaminations(InfoActivity.this);
                    mPregnancy.setExaminations(mainExaminations);

                    List<Weight> weights = new ArrayList<>();
                    weights.add(new Weight(Double.parseDouble(mInputCurrWeight.getText().toString()), 0));
                    mPregnancy.setWeights(weights);

                    HashMap<String, List<UserActivity>> activities = PregnancyUtils.getInitialUserActivities();
                    mPregnancy.setActivities(activities);

                    List<RiskFactor> riskFactors = new ArrayList<RiskFactor>();
                    if (mSmokeChkBox.isChecked()) {
                        riskFactors.add(RiskFactor.Smoking);
                        customTips.add(new Tip(null, getString(R.string.custom_tip_smoking), getLocalizedResources(InfoActivity.this, new Locale("bg")).getString(R.string.custom_tip_smoking), true, false));
                    }
                    if (mAlcoholChkBox.isChecked()) {
                        riskFactors.add(RiskFactor.Alcohol);
                        customTips.add(new Tip(null, getString(R.string.custom_tip_alcohol), getLocalizedResources(InfoActivity.this, new Locale("bg")).getString(R.string.custom_tip_alcohol), true, false));
                    }
                    if (mOverweightChkBox.isChecked()) {
                        riskFactors.add(RiskFactor.Overweight);
                        customTips.add(new Tip(null, getString(R.string.custom_tip_overweight), getLocalizedResources(InfoActivity.this, new Locale("bg")).getString(R.string.custom_tip_overweight), true, false));
                    }
                    if (mAgeChkBox.isChecked()) {
                        riskFactors.add(RiskFactor.Age);
                        customTips.add(new Tip(null, getString(R.string.custom_tip_age), getLocalizedResources(InfoActivity.this, new Locale("bg")).getString(R.string.custom_tip_age), true, false));
                    }
                    if (mUnderfeedingChkBox.isChecked()) {
                        riskFactors.add(RiskFactor.UnderFeeding);
                        customTips.add(new Tip(null, getString(R.string.custom_tip_underfeeding), getLocalizedResources(InfoActivity.this, new Locale("bg")).getString(R.string.custom_tip_underfeeding), true, false));
                    }
                    if (mFoodAllChkBox.isChecked()) {
                        riskFactors.add(RiskFactor.FoodAllergy);
                    }
                    if (mMedAllChkBox.isChecked()) {
                        riskFactors.add(RiskFactor.MedicinesAllergy);
                    }
                    if (mFoodAllChkBox.isChecked() || mMedAllChkBox.isChecked()) {
                        customTips.add(new Tip(null, getString(R.string.custom_tip_allergy), getLocalizedResources(InfoActivity.this, new Locale("bg")).getString(R.string.custom_tip_allergy), true, false));
                    }
                    mUser.setRiskFactors(riskFactors);

                    if (!mFirstPregnancySwitch.isChecked()) {
                        mPregnancy.setFirstPregnancy(false);
                        if (mPregnancyCountInput.getText() != null && !mPregnancyCountInput.getText().toString().isEmpty()) {
                            mUser.setPregnancyCount(Integer.parseInt(mPregnancyCountInput.getText().toString()));
                        }
                        if (mLiveBornKidsInput.getText() != null && !mLiveBornKidsInput.getText().toString().isEmpty()) {
                            mUser.setLiveBornKids(Integer.parseInt(mLiveBornKidsInput.getText().toString()));
                        }
                        if (mStillbornKidsInput.getText() != null && !mStillbornKidsInput.getText().toString().isEmpty()) {
                            mUser.setStillbornKids(Integer.parseInt(mStillbornKidsInput.getText().toString()));
                        }
                        if (mPrematureInput.getText() != null && !mPrematureInput.getText().toString().isEmpty()) {
                            mUser.setPrematureKids(Integer.parseInt(mPrematureInput.getText().toString()));
                        }
                        if (mPosttermInput.getText() != null && !mPosttermInput.getText().toString().isEmpty()) {
                            mUser.setPosttermKids(Integer.parseInt(mPosttermInput.getText().toString()));
                        }
                    } else {
                        mPregnancy.setFirstPregnancy(true);
                    }

                    if (mSterilityInput.getText() != null && !mSterilityInput.getText().toString().isEmpty()) {
                        mUser.setSterilityDiagnosis(mSterilityInput.getText().toString());
                    }

                    if (mAbortionSwitch.isChecked()) {
                        if (mDesiredInput.getText() != null && !mDesiredInput.getText().toString().isEmpty()) {
                            mUser.setDesiredAbortions(Integer.parseInt(mDesiredInput.getText().toString()));
                        }

                        if (mMiscarriagesInput.getText() != null && !mMiscarriagesInput.getText().toString().isEmpty()) {
                            mUser.setMiscarriages(Integer.parseInt(mMiscarriagesInput.getText().toString()));
                        }

                        if (mMedicalInput.getText() != null && !mMedicalInput.getText().toString().isEmpty()) {
                            mUser.setAbortionsOnMedEvidence(Integer.parseInt(mMedicalInput.getText().toString()));
                        }
                    }

                    if (mAbortionSwitch.isChecked() || (!mPregnancy.isFirstPregnancy() && mUser.getStillbornKids() > 0) || mUser.getComplicationsOtherPregnancies() != null) {
                        customTips.add(new Tip(null, getString(R.string.custom_tip_pregnancies_history), getLocalizedResources(InfoActivity.this, new Locale("bg")).getString(R.string.custom_tip_pregnancies_history), true, false));
                    }

                    if (mInfectiousDiseases.getText() != null && !mInfectiousDiseases.getText().toString().isEmpty()) {
                        mUser.setInfectiousDiseases(mInfectiousDiseases.getText().toString());
                    }

                    if (mOtherIllnesses.getText() != null && !mOtherIllnesses.getText().toString().isEmpty()) {
                        mUser.setOtherIllnesses(mOtherIllnesses.getText().toString());
                    }

                    if (mSurgeries.getText() != null && !mSurgeries.getText().toString().isEmpty()) {
                        mUser.setSurgeries(mSurgeries.getText().toString());
                    }

                    if (mFamilyHistory.getText() != null && !mFamilyHistory.getText().toString().isEmpty()) {
                        mUser.setFamilyHistories(mFamilyHistory.getText().toString());
                        customTips.add(new Tip(null, getString(R.string.custom_tip_family_history), getLocalizedResources(InfoActivity.this, new Locale("bg")).getString(R.string.custom_tip_family_history), true, false));
                    }

                    if (mFeaturesAndComplications.getText() != null && !mFeaturesAndComplications.getText().toString().isEmpty()) {
                        mUser.setComplicationsOtherPregnancies(mFeaturesAndComplications.getText().toString());
                    }

                    if (mTakenMedicinesInput.getText() != null && !mTakenMedicinesInput.getText().toString().isEmpty()) {
                        mUser.setTakenMedicines(mTakenMedicinesInput.getText().toString());
                    }

                    if (mBloodGrIncompatibilitySwitch.isChecked()) {
                        mPregnancy.setBloodGroupIncompatibility(true);
                        customTips.add(new Tip(null, getString(R.string.custom_tip_blood_groups), getLocalizedResources(InfoActivity.this, new Locale("bg")).getString(R.string.custom_tip_blood_groups), true, false));
                    }

                    if (mUnwantedPregnancySwitch.isChecked()) {
                        mPregnancy.setUnwantedPregnancy(true);
                    }

                    mUser.setCustomTips(customTips);
                    mUser.setPregnancyConsecutiveId(0);
                    List<Pregnancy> pregnancies = new ArrayList<Pregnancy>();
                    pregnancies.add(mPregnancy);
                    mUser.setPregnancies(pregnancies);

                    mSubmitBtn.setVisibility(View.GONE);
                    mSubmitLoader.setVisibility(View.VISIBLE);
                    disableAllEditableFieldsOnSubmit();
                    if (mFilePath != null) {
                        uploadUserPhoto();
                    } else {
                        updateUser();
                    }
                }
            }
        });
    }

    private void uploadUserPhoto() {
        final StorageReference ref = mStorageReference.child("images/" + mFilePath.getLastPathSegment());
        UploadTask uploadTask = ref.putFile(mFilePath);

        Task<Uri> urlTask = uploadTask.continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
            @Override
            public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {
                if (!task.isSuccessful()) {
                    throw task.getException();
                }

                // Continue with the task to get the download URL
                return ref.getDownloadUrl();
            }
        }).addOnCompleteListener(new OnCompleteListener<Uri>() {
            @Override
            public void onComplete(@NonNull Task<Uri> task) {
                if (task.isSuccessful()) {
                    Uri downloadUri = task.getResult();
                    mUser.setProfileImage(downloadUri.toString());

                    updateUser();
                } else {
                    // Handle failures
                    // ...
                    showAlertDialogNow("Failed to upload profile image. Please try again", "Warning");
                    enableAllEditableFields();
                }
            }
        });
    }


    private void updateUser() {
        ObjectMapper m = new ObjectMapper();
        Map<String,Object> user = m.convertValue(mUser, Map.class);

        mDb.collection("users").document(mUserEmail).set(user)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        System.out.println("DocumentSnapshot successfully written!");
                        mSubmitLoader.setVisibility(View.GONE);
                        mSubmitBtn.setVisibility(View.VISIBLE);

                        showAlertDialogNow("Your data is saved successfully", "User data", new WarnDialog.DialogClickListener() {
                            @Override
                            public void onClick() {
                                Intent intent = new Intent(InfoActivity.this, MainActivity.class);
                                intent.putExtra("email", mUserEmail);
                                intent.putExtra("user", mUser);
                                startActivity(intent);
                                finish();
                            }
                        });
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        System.out.println("Error writing document " + e.getMessage());
                        mSubmitLoader.setVisibility(View.GONE);
                        mSubmitBtn.setVisibility(View.VISIBLE);
                        enableAllEditableFields();

                        showAlertDialogNow(e.getMessage(), "User data");
                    }
                });
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {
            case SELECT_FILE: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    openGallery();
                } else {
                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.
                }
                return;
            }
        }
    }

    private void openGallery() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);//
        startActivityForResult(Intent.createChooser(intent, "Select File"), SELECT_FILE);
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == SELECT_FILE)
                mFilePath = data.getData();
                onSelectFromGalleryResult(data);
        }
    }

    private void onSelectFromGalleryResult(Intent data) {
        if (data != null) {
            Uri selectedImage = data.getData();
            String[] filePathColumn = { MediaStore.Images.Media.DATA };

            Cursor cursor = getContentResolver().query(selectedImage,
                    filePathColumn, null, null, null);
            cursor.moveToFirst();

            int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
            String picturePath = cursor.getString(columnIndex);
            cursor.close();
            Bitmap d = BitmapFactory.decodeFile(picturePath);
            int nh = (int) ( d.getHeight() * (512.0 / d.getWidth()) );
            Bitmap scaledImage = Bitmap.createScaledBitmap(d, 512, nh, true);
            mProfileImage.setImageBitmap(scaledImage);
            mProfileText.setVisibility(View.GONE);
        }
    }

    private void disableAllEditableFieldsOnSubmit() {
        isLoading = true;
        mInputName.setEnabled(false);
        mInputAge.setEnabled(false);
        mInputCurrHeight.setEnabled(false);
        mInputCurrWeight.setEnabled(false);
        mSmokeChkBox.setEnabled(false);
        mAlcoholChkBox.setEnabled(false);
        mOverweightChkBox.setEnabled(false);
        mAgeChkBox.setEnabled(false);
        mUnderfeedingChkBox.setEnabled(false);
        mFoodAllChkBox.setEnabled(false);
        mMedAllChkBox.setEnabled(false);
        mUnwantedPregnancySwitch.setClickable(false);
        mBloodGrIncompatibilitySwitch.setClickable(false);
        mInfectiousDiseases.setEnabled(false);
        mFamilyHistory.setEnabled(false);
        mSurgeries.setEnabled(false);
        mOtherIllnesses.setEnabled(false);
        mFeaturesAndComplications.setEnabled(false);
        mFirstPregnancySwitch.setClickable(false);
        mLastDeliveryLayout.setClickable(false);
        mLiveBornKidsInput.setEnabled(false);
        mStillbornKidsInput.setEnabled(false);
        mPrematureInput.setEnabled(false);
        mPosttermInput.setEnabled(false);
        mSterilityInput.setEnabled(false);
        mAbortionSwitch.setClickable(false);
        mDesiredInput.setEnabled(false);
        mMiscarriagesInput.setEnabled(false);
        mMedicalInput.setEnabled(false);
        mTakenMedicinesInput.setEnabled(false);
        mFirstDayOfMLayout.setClickable(false);
        mRegularSwitch.setClickable(false);
        mInputMenstruationLength.setEnabled(false);
        mInputMenstruationDuration.setEnabled(false);
    }

    private void enableAllEditableFields() {
        isLoading = false;
        mInputName.setEnabled(true);
        mInputAge.setEnabled(true);
        mInputCurrHeight.setEnabled(true);
        mInputCurrWeight.setEnabled(true);
        mSmokeChkBox.setEnabled(true);
        mAlcoholChkBox.setEnabled(true);
        mOverweightChkBox.setEnabled(true);
        mAgeChkBox.setEnabled(true);
        mUnderfeedingChkBox.setEnabled(true);
        mFoodAllChkBox.setEnabled(true);
        mMedAllChkBox.setEnabled(true);
        mUnwantedPregnancySwitch.setClickable(true);
        mBloodGrIncompatibilitySwitch.setClickable(true);
        mInfectiousDiseases.setEnabled(true);
        mFamilyHistory.setEnabled(true);
        mSurgeries.setEnabled(true);
        mOtherIllnesses.setEnabled(true);
        mFeaturesAndComplications.setEnabled(true);
        mFirstPregnancySwitch.setClickable(true);
        mLastDeliveryLayout.setClickable(true);
        mLiveBornKidsInput.setEnabled(true);
        mStillbornKidsInput.setEnabled(true);
        mPrematureInput.setEnabled(true);
        mPosttermInput.setEnabled(true);
        mSterilityInput.setEnabled(true);
        mAbortionSwitch.setClickable(true);
        mDesiredInput.setEnabled(true);
        mMiscarriagesInput.setEnabled(true);
        mMedicalInput.setEnabled(true);
        mTakenMedicinesInput.setEnabled(true);
        mFirstDayOfMLayout.setClickable(true);
        mRegularSwitch.setClickable(true);
        mInputMenstruationLength.setEnabled(true);
        mInputMenstruationDuration.setEnabled(true);
    }

    @NonNull
    Resources getLocalizedResources(Context context, Locale desiredLocale) {
        Configuration conf = context.getResources().getConfiguration();
        conf = new Configuration(conf);
        conf.setLocale(desiredLocale);
        Context localizedContext = context.createConfigurationContext(conf);
        return localizedContext.getResources();
    }

    private void showAlertDialogNow(String message, String title) {
        WarnDialog warning = new WarnDialog(this, title, message, new WarnDialog.DialogClickListener() {
            @Override
            public void onClick() {
                mSubmitLoader.setVisibility(View.GONE);
                mSubmitBtn.setVisibility(View.VISIBLE);
            }
        });
        warning.show();
    }

    private void showAlertDialogNow(String message, String title, WarnDialog.DialogClickListener listener) {
        WarnDialog warnDialog = new WarnDialog(this, title, message, listener);
        warnDialog.show();
    }

    @Override
    public void onBackPressed() {
        if (!isLoading) {
            super.onBackPressed();
        }
    }

    private List<FamilyHistory> eliminateEmptyFHItems(List<FamilyHistory> fHistories) {
        List<FamilyHistory> notEmptyFHs = new ArrayList<>();
        for (FamilyHistory fh : fHistories) {
            if (fh.getType() != null && !fh.getType().isEmpty()) {
                notEmptyFHs.add(fh);
            }
        }
        return notEmptyFHs;
    }
}
