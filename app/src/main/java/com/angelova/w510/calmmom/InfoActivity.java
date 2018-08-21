package com.angelova.w510.calmmom;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
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
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.angelova.w510.calmmom.dialogs.AddIllnessDialog;
import com.angelova.w510.calmmom.dialogs.FamilyHistoryDialog;
import com.angelova.w510.calmmom.dialogs.SurgeriesDialog;
import com.angelova.w510.calmmom.dialogs.WarnDialog;
import com.angelova.w510.calmmom.models.Examination;
import com.angelova.w510.calmmom.models.ExaminationStatus;
import com.angelova.w510.calmmom.models.FamilyHistory;
import com.angelova.w510.calmmom.models.Illness;
import com.angelova.w510.calmmom.models.RiskFactor;
import com.angelova.w510.calmmom.models.Surgery;
import com.angelova.w510.calmmom.models.User;
import com.angelova.w510.calmmom.models.UserActivity;
import com.angelova.w510.calmmom.models.Weight;
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

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;

public class InfoActivity extends AppCompatActivity {

    private CircleImageView mProfileImage;
    private TextView mProfileText;
    private LinearLayout mIllnessesLayout;
    private LinearLayout mSurgeriesLayout;
    private LinearLayout mFHLayout;

    private EditText mInputName;
    private EditText mInputAge;
    private EditText mInputCurrHeight;
    private EditText mInputCurrWeight;
    private EditText mInputFirstDayOfLastM;
    private SwitchCompat mRegularSwitch;
    private EditText mInputMenstruationLength;
    private EditText mInputMenstruationDuration;

    private CheckBox mSmokeChkBox;
    private CheckBox mAlcoholChkBox;
    private CheckBox mOverweightChkBox;
    private CheckBox mAgeChkBox;
    private CheckBox mUnderfeedingChkBox;
    private CheckBox mFoodAllChkBox;
    private CheckBox mMedAllChkBox;

    private Button mSubmitBtn;
    private ProgressBar mSubmitLoader;

    private User mUser;
    private String mUserEmail;
    private FirebaseFirestore mDb;
    FirebaseStorage mStorage;
    StorageReference mStorageReference;

    private Uri mFilePath;

    private boolean isLoading = false;

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

        mIllnessesLayout = (LinearLayout) findViewById(R.id.illnesses_view);
        mIllnessesLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                List<Illness> illnesses = new ArrayList<>();
                if (mUser.getIllnesses() == null || mUser.getIllnesses().size() == 0) {
                    //adding a default item
                    illnesses.add(new Illness());
                } else {
                    illnesses = mUser.getIllnesses();
                }

                AddIllnessDialog dialog = new AddIllnessDialog(InfoActivity.this, illnesses, new AddIllnessDialog.DialogClickListener() {
                    @Override
                    public void onSave(List<Illness> receivedIllnesses) {
                        mUser.setIllnesses(receivedIllnesses);
                    }
                });
                dialog.show();
            }
        });

        mSurgeriesLayout = (LinearLayout) findViewById(R.id.surgeries_view);
        mSurgeriesLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                List<Surgery> surgeries = new ArrayList<>();
                if (mUser.getSurgeries() == null || mUser.getSurgeries().size() == 0) {
                    surgeries.add(new Surgery());
                } else {
                    surgeries = mUser.getSurgeries();
                }

                SurgeriesDialog dialog = new SurgeriesDialog(InfoActivity.this, surgeries, new SurgeriesDialog.DialogClickListener() {
                    @Override
                    public void onSave(List<Surgery> receivedSurgeries) {
                        mUser.setSurgeries(receivedSurgeries);
                    }
                });
                dialog.show();
            }
        });

        mFHLayout = (LinearLayout) findViewById(R.id.family_history_view);
        mFHLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                List<FamilyHistory> fHistories = new ArrayList<>();
                if (mUser.getFamilyHistories() == null || mUser.getFamilyHistories().size() == 0) {
                    fHistories.add(new FamilyHistory());
                } else {
                    fHistories = eliminateEmptyFHItems(mUser.getFamilyHistories());
                }

                FamilyHistoryDialog dialog = new FamilyHistoryDialog(InfoActivity.this, fHistories, new FamilyHistoryDialog.DialogClickListener() {
                    @Override
                    public void onSave(List<FamilyHistory> fhs) {
                        mUser.setFamilyHistories(fhs);
                    }
                });
                dialog.show();
            }
        });

        mInputName = (EditText) findViewById(R.id.input_name);
        mInputAge = (EditText) findViewById(R.id.input_age);
        mInputCurrHeight = (EditText) findViewById(R.id.input_height);
        mInputCurrWeight = (EditText) findViewById(R.id.input_weight);
        mInputFirstDayOfLastM = (EditText) findViewById(R.id.input_menstruation);
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
                } else if (mInputFirstDayOfLastM.getText() == null || mInputFirstDayOfLastM.getText().toString().isEmpty()) {
                    showAlertDialogNow("Please input the first day of your last menstruation cycle", "Warning");
                } else if (mInputMenstruationLength.getText() == null || mInputMenstruationLength.getText().toString().isEmpty()) {
                    showAlertDialogNow("Please enter the length of your menstruation cycle", "Warning");
                } else if (mInputMenstruationDuration.getText() == null || mInputMenstruationDuration.getText().toString().isEmpty()) {
                    showAlertDialogNow("Please enter the average duration of your menstruation", "Warning");
                } else {
                    mUser.setName(mInputName.getText().toString());
                    mUser.setAge(Integer.parseInt(mInputAge.getText().toString()));
                    mUser.setCurrentHeight(Double.parseDouble(mInputCurrHeight.getText().toString()));
                    mUser.setCurrentWeight(Double.parseDouble(mInputCurrWeight.getText().toString()));
                    mUser.setFirstDayOfLastMenstruation(mInputFirstDayOfLastM.getText().toString());
                    mUser.setRegularMenstruation(mRegularSwitch.isChecked());
                    mUser.setLengthOfMenstruation(Integer.parseInt(mInputMenstruationLength.getText().toString()));
                    mUser.setDurationOfMenstruation(Integer.parseInt(mInputMenstruationDuration.getText().toString()));

                    List<Examination> mainExaminations = getListWithMainExaminations();
                    mUser.setExaminations(mainExaminations);

                    List<Weight> weights = new ArrayList<>();
                    weights.add(new Weight(Double.parseDouble(mInputCurrWeight.getText().toString()), 0));
                    mUser.setWeights(weights);

                    HashMap<String, List<UserActivity>> activities = getInitialUserActivities();
                    mUser.setActivities(activities);

                    List<RiskFactor> riskFactors = new ArrayList<RiskFactor>();
                    if (mSmokeChkBox.isChecked()) {
                        riskFactors.add(RiskFactor.Smoking);
                    }
                    if (mAlcoholChkBox.isChecked()) {
                        riskFactors.add(RiskFactor.Alcohol);
                    }
                    if (mOverweightChkBox.isChecked()) {
                        riskFactors.add(RiskFactor.Overweight);
                    }
                    if (mAgeChkBox.isChecked()) {
                        riskFactors.add(RiskFactor.Age);
                    }
                    if (mUnderfeedingChkBox.isChecked()) {
                        riskFactors.add(RiskFactor.UnderFeeding);
                    }
                    if (mFoodAllChkBox.isChecked()) {
                        riskFactors.add(RiskFactor.FoodAllergy);
                    }
                    if (mMedAllChkBox.isChecked()) {
                        riskFactors.add(RiskFactor.MedicinesAllergy);
                    }
                    mUser.setRiskFactors(riskFactors);

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
        mInputFirstDayOfLastM.setEnabled(false);
        mRegularSwitch.setClickable(false);
        mInputMenstruationLength.setEnabled(false);
        mInputMenstruationDuration.setEnabled(false);
        mIllnessesLayout.setClickable(false);
        mSurgeriesLayout.setClickable(false);
        mFHLayout.setClickable(false);
    }

    private void enableAllEditableFields() {
        isLoading = false;
        mInputName.setEnabled(true);
        mInputAge.setEnabled(true);
        mInputCurrHeight.setEnabled(true);
        mInputCurrWeight.setEnabled(true);
        mInputFirstDayOfLastM.setEnabled(true);
        mRegularSwitch.setClickable(true);
        mInputMenstruationLength.setEnabled(true);
        mInputMenstruationDuration.setEnabled(true);
        mIllnessesLayout.setClickable(true);
        mSurgeriesLayout.setClickable(true);
        mFHLayout.setClickable(true);
    }

    private List<Examination> getListWithMainExaminations() {
        List<Examination> examinations = new ArrayList<>();
        List<String> tests = Arrays.asList(getString(R.string.first_examination_test1), getString(R.string.first_examination_test2),
                getString(R.string.first_examination_test3), getString(R.string.first_examination_test4),
                getString(R.string.first_examination_test5), getString(R.string.first_examination_test6),
                getString(R.string.first_examination_test7));
        List<String> activities = Arrays.asList(getString(R.string.first_examination_act1), getString(R.string.first_examination_act2),
                getString(R.string.first_examination_act3), getString(R.string.first_examination_act4),
                getString(R.string.first_examination_act5), getString(R.string.first_examination_act6));

        Examination firstEx = new Examination("First, Second or Third Month", "", ExaminationStatus.CURRENT, tests, activities);
        examinations.add(firstEx);

        activities = Arrays.asList(getString(R.string.second_examination_act1), getString(R.string.second_examination_act2),
                getString(R.string.second_examination_act3), getString(R.string.second_examination_act4));
        Examination secondEx = new Examination("Fourth Month", "", ExaminationStatus.FUTURE, new ArrayList<String>(), activities);
        examinations.add(secondEx);

        tests = Arrays.asList(getString(R.string.third_examination_test1));
        activities = Arrays.asList(getString(R.string.third_examination_act1), getString(R.string.third_examination_act2), getString(R.string.third_examination_act3));
        Examination thirdEx = new Examination("Fifth Month", "", ExaminationStatus.FUTURE, tests, activities);
        examinations.add(thirdEx);

        activities = Arrays.asList(getString(R.string.fourth_examination_act1), getString(R.string.fourth_examination_act2), getString(R.string.fourth_examination_act3));
        Examination fourthEx = new Examination("Sixth Month", "", ExaminationStatus.FUTURE, new ArrayList<String>(), activities);
        examinations.add(fourthEx);

        activities = Arrays.asList(getString(R.string.fifth_examination_act1), getString(R.string.fifth_examination_act2), getString(R.string.fifth_examination_act3));
        Examination fifthEx = new Examination("Seventh Month", "", ExaminationStatus.FUTURE, new ArrayList<String>(), activities);
        examinations.add(fifthEx);

        tests = Arrays.asList(getString(R.string.sixth_examination_test1));
        activities = Arrays.asList(getString(R.string.sixth_examination_act1), getString(R.string.sixth_examination_act2), getString(R.string.sixth_examination_act3));
        Examination sixthEx = new Examination("Eighth Month", "", ExaminationStatus.FUTURE, tests, activities);
        examinations.add(sixthEx);

        tests = Arrays.asList(getString(R.string.seventh_examination_test1));
        activities = Arrays.asList(getString(R.string.seventh_examination_act1), getString(R.string.seventh_examination_act2),
                getString(R.string.seventh_examination_act3), getString(R.string.seventh_examination_act4));
        Examination seventhEx = new Examination("Ninth Month", "", ExaminationStatus.FUTURE, tests, activities);
        examinations.add(seventhEx);

        activities = Arrays.asList(getString(R.string.eighth_examination_act1), getString(R.string.eighth_examination_act2),
                getString(R.string.eighth_examination_act3), getString(R.string.eighth_examination_act4));
        Examination eighthEx = new Examination("Tenth Month", "", ExaminationStatus.FUTURE, new ArrayList<String>(), activities);
        examinations.add(eighthEx);

        return examinations;
    }

    private HashMap<String, List<UserActivity>> getInitialUserActivities() {
        HashMap<String, List<UserActivity>> activities = new HashMap<>();
        for (int i = 1; i <= 40; i++) {
            activities.put(Integer.toString(i), new ArrayList<UserActivity>());
        }
        return activities;
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
