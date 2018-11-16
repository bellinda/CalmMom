package com.angelova.w510.calmmom;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.preference.PreferenceManager;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.DatePicker;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.TextView;

import com.angelova.w510.calmmom.dialogs.EndPregnancyDialog;
import com.angelova.w510.calmmom.dialogs.ListPregnanciesDialog;
import com.angelova.w510.calmmom.dialogs.StartPregnancyDialog;
import com.angelova.w510.calmmom.dialogs.WarnDialog;
import com.angelova.w510.calmmom.models.AbortionPurpose;
import com.angelova.w510.calmmom.models.Examination;
import com.angelova.w510.calmmom.models.ExaminationStatus;
import com.angelova.w510.calmmom.models.Measurement;
import com.angelova.w510.calmmom.models.OutcomeType;
import com.angelova.w510.calmmom.models.Pregnancy;
import com.angelova.w510.calmmom.models.PregnancyOutcome;
import com.angelova.w510.calmmom.models.RiskFactor;
import com.angelova.w510.calmmom.models.Test;
import com.angelova.w510.calmmom.models.Tip;
import com.angelova.w510.calmmom.models.User;
import com.bumptech.glide.Glide;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;

import static java.util.Calendar.DATE;

public class ProfileActivity extends AppCompatActivity {

    private CircleImageView mImageView;
    private TextView mAddImageText;
    private TextView mNameTextView;
    private TextView mPregnancyIndex;
    private Button mChoosePregnancyBtn;
    private Button mForumBtn;
    private LinearLayout mDDLayout;
    private LinearLayout mEstimatedDDLayout;
    private TextView mDeliveryDate;
    private RadioButton mEnLanguage;
    private RadioButton mBgLanguage;
    private Button mLogoutBtn;
    private Button mEndPregnancyBtn;
    private Button mStartPregnancyBtn;

    private User mUser;
    private String mUserEmail;

    private FirebaseFirestore mDb;
    FirebaseStorage mStorage;
    StorageReference mStorageReference;

    private Uri mFilePath;
    private static final int SELECT_FILE = 1023;

    final Calendar mExpectedDeliveryDate = Calendar.getInstance();

    private ListPregnanciesDialog choosePregnancyDialog;

    SharedPreferences mPrefs;
    SharedPreferences.Editor mEditor;

    public static final String INTENT_EXTRA_CHANGE_LANGUAGE = "language_change";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(Color.parseColor("#324A5F"));
        }

        mPrefs = PreferenceManager.getDefaultSharedPreferences(this);
        mEditor = mPrefs.edit();

        mUser = (User) getIntent().getSerializableExtra("user");
        mUserEmail = getIntent().getStringExtra("email");

        mDb = FirebaseFirestore.getInstance();
        mStorage = FirebaseStorage.getInstance();
        mStorageReference = mStorage.getReference();

        mImageView = (CircleImageView) findViewById(R.id.profile_image);
        mAddImageText = (TextView) findViewById(R.id.profile_text);
        mNameTextView = (TextView) findViewById(R.id.name_field);
        mPregnancyIndex = (TextView) findViewById(R.id.pregnancy_count);
        mChoosePregnancyBtn = (Button) findViewById(R.id.other_pregnancies_btn);
        mForumBtn = (Button) findViewById(R.id.forum_btn);
        mDDLayout = (LinearLayout) findViewById(R.id.dd_view);
        mDeliveryDate = (TextView) findViewById(R.id.delivery_date);
        mEstimatedDDLayout = (LinearLayout) findViewById(R.id.delivery_date_layout);
        mEnLanguage = (RadioButton) findViewById(R.id.lang_en);
        mBgLanguage = (RadioButton) findViewById(R.id.lang_bg);
        mLogoutBtn = (Button) findViewById(R.id.log_out_btn);
        mEndPregnancyBtn = (Button) findViewById(R.id.end_pregnancy_btn);
        mStartPregnancyBtn = (Button) findViewById(R.id.start_pregnancy_btn);

        if (mUser.getProfileImage() != null && !mUser.getProfileImage().isEmpty()) {
            mAddImageText.setVisibility(View.GONE);
            Glide.with(getApplicationContext()).load(mUser.getProfileImage()).into(mImageView);
        }

        choosePregnancyDialog = new ListPregnanciesDialog(ProfileActivity.this, mUser.getPregnancies(), new ListPregnanciesDialog.DialogClickListener() {
            @Override
            public void onItemClicked(Pregnancy pregnancy) {
                int pregnancyIndex = mUser.getPregnancies().indexOf(pregnancy);
                if (pregnancyIndex == mUser.getPregnancyConsecutiveId()) {
                    showAlertDialogNow(getString(R.string.dialog_list_pregnancies_same_pregnancy), getString(R.string.dialog_list_pregnancies_same_pregnancy_title));
                } else {
                    mUser.setPregnancyConsecutiveId(pregnancyIndex);
                    updateUser();
                    mEditor.putBoolean("shouldReload", true);
                    mEditor.apply();
                    choosePregnancyDialog.dismiss();
                    //showAlertDialogNow("The pregnancy is switched. Go to the main menu to observe the data for it.", "Warning");
                    onBackPressed();
                }
            }
        });

        mImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(ActivityCompat.checkSelfPermission(ProfileActivity.this, android.Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions(ProfileActivity.this,
                            new String[]{android.Manifest.permission.READ_EXTERNAL_STORAGE}, SELECT_FILE);
                }
                else {
                    openGallery();
                }
            }
        });

        mNameTextView.setText(mUser.getName());
        if (mUser.getPregnancies().get(mUser.getPregnancyConsecutiveId()).isFirstPregnancy()) {
            mPregnancyIndex.setText(getString(R.string.activity_profile_first_pregnancy));
        } else {
            if (mUser.getPregnancyConsecutiveId() != mUser.getPregnancies().size() - 1) {
                mPregnancyIndex.setVisibility(View.GONE);
            } else {
                int pregnanciesCount = mUser.getPregnancyCount();
                if (pregnanciesCount == 2) {
                    mPregnancyIndex.setText(getString(R.string.activity_profile_second_pregnancy));
                } else if (pregnanciesCount == 3) {
                    mPregnancyIndex.setText(getString(R.string.activity_profile_third_pregnancy));
                } else {
                    mPregnancyIndex.setText(String.format(Locale.getDefault(), getString(R.string.activity_profile_other_pregnancy), pregnanciesCount));
                }
            }
        }

        mChoosePregnancyBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mUser.getPregnancies().get(mUser.getPregnancyConsecutiveId()).isFirstPregnancy() || mUser.getPregnancies().size() == 1) {
                    WarnDialog dialog = new WarnDialog(ProfileActivity.this, getString(R.string.dialog_list_pregnancies_only_one_title), getString(R.string.dialog_list_pregnancies_only_one), new WarnDialog.DialogClickListener() {
                        @Override
                        public void onClick() {

                        }
                    });
                    dialog.show();
                } else {
                    choosePregnancyDialog.show();
                }
            }
        });

        mForumBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ProfileActivity.this, ForumActivity.class);
                intent.putExtra("user", mUser);
                intent.putExtra("email", mUserEmail);
                startActivity(intent);
            }
        });

        try {
            SimpleDateFormat sdf = new SimpleDateFormat("dd.MM.yyyy", Locale.getDefault());

            if (mUser.getPregnancies().get(mUser.getPregnancyConsecutiveId()).getEstimatedDeliveryDate() == null || mUser.getPregnancies().get(mUser.getPregnancyConsecutiveId()).getEstimatedDeliveryDate().isEmpty()) {
                Date firstDayOfLM = sdf.parse(mUser.getPregnancies().get(mUser.getPregnancyConsecutiveId()).getFirstDayOfLastMenstruation());
                Date deliveryDate = new Date(firstDayOfLM.getTime() + 280 * 24 * 3600 * 1000l);
                mDeliveryDate.setText(sdf.format(deliveryDate));
                mExpectedDeliveryDate.setTime(deliveryDate);
            } else {
                mDeliveryDate.setText(mUser.getPregnancies().get(mUser.getPregnancyConsecutiveId()).getEstimatedDeliveryDate());
                mExpectedDeliveryDate.setTime(sdf.parse(mUser.getPregnancies().get(mUser.getPregnancyConsecutiveId()).getEstimatedDeliveryDate()));
            }
        } catch (ParseException pe) {
            pe.printStackTrace();
        }

        mDDLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DatePickerDialog datePickerDialog = new DatePickerDialog(ProfileActivity.this, R.style.AppTheme_DialogThemeDark, new DatePickerDialog.OnDateSetListener() {

                    @Override
                    public void onDateSet(DatePicker datePicker, int year, int monthOfYear, int dayOfMonth) {
                        mExpectedDeliveryDate.set(year, monthOfYear, dayOfMonth);
                        SimpleDateFormat sdf = new SimpleDateFormat("dd.MM.yyyy", Locale.getDefault());
                        mDeliveryDate.setText(sdf.format(mExpectedDeliveryDate.getTime()));
                        mUser.getPregnancies().get(mUser.getPregnancyConsecutiveId()).setEstimatedDeliveryDate(mDeliveryDate.getText().toString());
                        String firstDayOfLastMenstruation = getFirstDayOfLastMenstruation();
                        mUser.getPregnancies().get(mUser.getPregnancyConsecutiveId()).setFirstDayOfLastMenstruation(firstDayOfLastMenstruation);
                        updateUser();
                        mEditor.putBoolean("shouldReload", true).commit();
                    }
                }, mExpectedDeliveryDate.get(Calendar.YEAR), mExpectedDeliveryDate.get(Calendar.MONTH), mExpectedDeliveryDate.get(DATE));
                datePickerDialog.getDatePicker().setMinDate(Calendar.getInstance().getTimeInMillis());
                datePickerDialog.show();
            }
        });

        if (Locale.getDefault().getLanguage().equals("en")) {
            mEnLanguage.setOnCheckedChangeListener(null);
            mEnLanguage.setChecked(true);
            mEnLanguage.setOnCheckedChangeListener(mEnCheckListener);
            mBgLanguage.setOnCheckedChangeListener(mBgCheckListener);
        } else {
            mBgLanguage.setOnCheckedChangeListener(null);
            mBgLanguage.setChecked(true);
            mBgLanguage.setOnCheckedChangeListener(mBgCheckListener);
            mEnLanguage.setOnCheckedChangeListener(mEnCheckListener);
        }

        mLogoutBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mEditor.clear();
                mEditor.apply();
                FirebaseAuth.getInstance().signOut();
                Intent loginPageIntent = new Intent(getApplicationContext(), LoginActivity.class);
                loginPageIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(loginPageIntent);
                finishAffinity();
            }
        });

        mEndPregnancyBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                EndPregnancyDialog dialog = new EndPregnancyDialog(ProfileActivity.this, new EndPregnancyDialog.DialogClickListener() {
                    @Override
                    public void onSave(PregnancyOutcome outcome) {
                        mUser.getPregnancies().get(mUser.getPregnancyConsecutiveId()).setPregnancyOutcome(outcome);
                        if (outcome.getOutcomeType() == OutcomeType.Abortion) {
                            if (outcome.getAbortionPurpose() == AbortionPurpose.Desired) {
                                mUser.setDesiredAbortions(mUser.getDesiredAbortions() + 1);
                            } else {
                                mUser.setAbortionsOnMedEvidence(mUser.getAbortionsOnMedEvidence() + 1);
                            }
                        } else if (outcome.getOutcomeType() == OutcomeType.Miscarriage) {
                            mUser.setMiscarriages(mUser.getMiscarriages() + 1);
                        } else if (outcome.getOutcomeType() == OutcomeType.StillBirth) {
                            mUser.setStillbornKids(mUser.getStillbornKids() + 1);
                        } else {
                            mUser.setLiveBornKids(mUser.getLiveBornKids() + 1);
                            mUser.setLastDeliveryDate(outcome.getDate());
                        }

                        List<Examination> examinations = mUser.getPregnancies().get(mUser.getPregnancyConsecutiveId()).getExaminations();
                        for(int i = examinations.size() - 1; i >= 0; i--) {
                            if (examinations.get(i).getStatus() == ExaminationStatus.FUTURE || examinations.get(i).getStatus() == ExaminationStatus.CURRENT) {
                                examinations.remove(i);
                            }
                        }

                        Resources enResources = getLocalizedResources(ProfileActivity.this, new Locale("en"));
                        Resources bgResources = getLocalizedResources(ProfileActivity.this, new Locale("bg"));

                        List<Measurement> activities = Arrays.asList(new Measurement(bgResources.getString(R.string.seven_days_after_birth_examination_act1), enResources.getString(R.string.seven_days_after_birth_examination_act1)),
                                new Measurement(bgResources.getString(R.string.seven_days_after_birth_examination_act2), enResources.getString(R.string.seven_days_after_birth_examination_act2)),
                                new Measurement(bgResources.getString(R.string.seven_days_after_birth_examination_act3), enResources.getString(R.string.seven_days_after_birth_examination_act3)),
                                new Measurement(bgResources.getString(R.string.seven_days_after_birth_examination_act4), enResources.getString(R.string.seven_days_after_birth_examination_act4)));
                        Examination sevenDaysAfterBirthEx = new Examination(enResources.getString(R.string.seven_days_after_birth_title), bgResources.getString(R.string.seven_days_after_birth_title), "", ExaminationStatus.FUTURE, new ArrayList<Test>(), activities);
                        examinations.add(sevenDaysAfterBirthEx);

                        activities = Arrays.asList(new Measurement(bgResources.getString(R.string.forty_days_after_birth_exmaination_act1), enResources.getString(R.string.forty_days_after_birth_exmaination_act1)),
                                new Measurement(bgResources.getString(R.string.forty_days_after_birth_exmaination_act2), enResources.getString(R.string.forty_days_after_birth_exmaination_act2)),
                                new Measurement(bgResources.getString(R.string.forty_days_after_birth_exmaination_act3), enResources.getString(R.string.forty_days_after_birth_exmaination_act3)),
                                new Measurement(bgResources.getString(R.string.forty_days_after_birth_exmaination_act4), enResources.getString(R.string.forty_days_after_birth_exmaination_act4)));
                        List<Test> tests = Arrays.asList(new Test(bgResources.getString(R.string.forty_days_after_birth_examination_test1), enResources.getString(R.string.forty_days_after_birth_examination_test1)));
                        Examination fortyDaysAfterBirthEx = new Examination(enResources.getString(R.string.forty_days_after_birth_title), bgResources.getString(R.string.forty_days_after_birth_title), "", ExaminationStatus.FUTURE, tests, activities);
                        examinations.add(fortyDaysAfterBirthEx);
                        mUser.getPregnancies().get(mUser.getPregnancyConsecutiveId()).setExaminations(examinations);

                        mUser.setCustomTips(new ArrayList<Tip>());

                        updateUser();
                        mEditor.putBoolean("shouldReload", true).commit();
                    }
                });
                dialog.show();
            }
        });

        mStartPregnancyBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                StartPregnancyDialog dialog = new StartPregnancyDialog(ProfileActivity.this, mUser, new StartPregnancyDialog.DialogClickListener() {
                    @Override
                    public void onSave(Pregnancy pregnancy, double currentWeight, boolean isRegular, int menLength, int menDuration, List<RiskFactor> riskFactors, List<Tip> customTips) {
                        mUser.setCurrentWeight(currentWeight);
                        mUser.setRegularMenstruation(isRegular);
                        mUser.setLengthOfMenstruation(menLength);
                        mUser.setDurationOfMenstruation(menDuration);
                        mUser.setRiskFactors(riskFactors);
                        mUser.getPregnancies().add(pregnancy);
                        mUser.setPregnancyCount(mUser.getPregnancyCount() + 1);
                        mUser.setPregnancyConsecutiveId(mUser.getPregnancyConsecutiveId() + 1);
                        if (mUser.getStillbornKids() > 0 || mUser.getDesiredAbortions() > 0 || mUser.getAbortionsOnMedEvidence() > 0 || mUser.getMiscarriages() > 0 || mUser.getComplicationsOtherPregnancies() != null) {
                            customTips.add(new Tip(null, getLocalizedResources(ProfileActivity.this, new Locale("en")).getString(R.string.custom_tip_pregnancies_history), getLocalizedResources(ProfileActivity.this, new Locale("bg")).getString(R.string.custom_tip_pregnancies_history), true, false));
                        }
                        if (mUser.getFamilyHistories() != null) {
                            customTips.add(new Tip(null, getLocalizedResources(ProfileActivity.this, new Locale("en")).getString(R.string.custom_tip_family_history), getLocalizedResources(ProfileActivity.this, new Locale("bg")).getString(R.string.custom_tip_family_history), true, false));
                        }
                        mUser.setCustomTips(customTips);

                        updateUser();
                        mEditor.putBoolean("shouldReload", true).commit();

                        try {
                            SimpleDateFormat sdf = new SimpleDateFormat("dd.MM.yyyy", Locale.getDefault());

                            if (mUser.getPregnancies().get(mUser.getPregnancyConsecutiveId()).getEstimatedDeliveryDate() == null || mUser.getPregnancies().get(mUser.getPregnancyConsecutiveId()).getEstimatedDeliveryDate().isEmpty()) {
                                Date firstDayOfLM = sdf.parse(mUser.getPregnancies().get(mUser.getPregnancyConsecutiveId()).getFirstDayOfLastMenstruation());
                                Date deliveryDate = new Date(firstDayOfLM.getTime() + 280 * 24 * 3600 * 1000l);
                                mDeliveryDate.setText(sdf.format(deliveryDate));
                                mExpectedDeliveryDate.setTime(deliveryDate);
                            } else {
                                mDeliveryDate.setText(mUser.getPregnancies().get(mUser.getPregnancyConsecutiveId()).getEstimatedDeliveryDate());
                                mExpectedDeliveryDate.setTime(sdf.parse(mUser.getPregnancies().get(mUser.getPregnancyConsecutiveId()).getEstimatedDeliveryDate()));
                            }
                        } catch (ParseException pe) {
                            pe.printStackTrace();
                        }

                        int pregnanciesCount = mUser.getPregnancyCount();
                        if (pregnanciesCount == 2) {
                            mPregnancyIndex.setText(getString(R.string.activity_profile_second_pregnancy));
                        } else {
                            mPregnancyIndex.setText(String.format(Locale.getDefault(), getString(R.string.activity_profile_other_pregnancy), pregnanciesCount));
                        }

                        mEstimatedDDLayout.setVisibility(View.VISIBLE);
                        mEndPregnancyBtn.setVisibility(View.VISIBLE);
                        mStartPregnancyBtn.setVisibility(View.GONE);
                    }
                });
                dialog.show();
            }
        });

        if (mUser.getPregnancies().get(mUser.getPregnancyConsecutiveId()).getPregnancyOutcome() != null) {
            mEstimatedDDLayout.setVisibility(View.GONE);
            mEndPregnancyBtn.setVisibility(View.GONE);
            mStartPregnancyBtn.setVisibility(View.VISIBLE);
        }
    }

    CompoundButton.OnCheckedChangeListener mEnCheckListener = new CompoundButton.OnCheckedChangeListener() {
        @Override
        public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {
            if (isChecked) {
                updateResources(ProfileActivity.this, "en");
                mUser.setApplicationLanguage("en");
                updateUser();
            }
        }
    };

    CompoundButton.OnCheckedChangeListener mBgCheckListener = new CompoundButton.OnCheckedChangeListener() {
        @Override
        public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {
            if (isChecked) {
                updateResources(ProfileActivity.this, "bg");
                mUser.setApplicationLanguage("bg");
                updateUser();
            }
        }
    };

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
            if (d != null) {
                int nh = (int) (d.getHeight() * (512.0 / d.getWidth()));
                Bitmap scaledImage = Bitmap.createScaledBitmap(d, 512, nh, true);
                mImageView.setImageBitmap(scaledImage);
                mAddImageText.setVisibility(View.GONE);

                uploadUserPhoto();
            }
        }
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
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        System.out.println("Error writing document " + e.getMessage());
                    }
                });
    }

    private String getFirstDayOfLastMenstruation() {
        Date expectedDeliveryDate = mExpectedDeliveryDate.getTime();
        Date firstDayOfLM = new Date(expectedDeliveryDate.getTime() - 280 * 24 * 3600 * 1000l);
        SimpleDateFormat sdf = new SimpleDateFormat("dd.MM.yyyy", Locale.getDefault());
        return sdf.format(firstDayOfLM);
    }

    private void updateResources(Context context, String language) {
        Locale locale = new Locale(language);
        Locale.setDefault(locale);

        Resources resources = context.getResources();

        Configuration configuration = resources.getConfiguration();
        configuration.locale = locale;

        resources.updateConfiguration(configuration, resources.getDisplayMetrics());

        mEditor.putBoolean("shouldUpdateLanguage", true).commit();
        finish();
        startActivity(getIntent());
    }

    @NonNull
    Resources getLocalizedResources(Context context, Locale desiredLocale) {
        Configuration conf = context.getResources().getConfiguration();
        conf = new Configuration(conf);
        conf.setLocale(desiredLocale);
        Context localizedContext = context.createConfigurationContext(conf);
        return localizedContext.getResources();
    }

    public void showAlertDialogNow(String message, String title) {
        WarnDialog warning = new WarnDialog(this, title, message, new WarnDialog.DialogClickListener() {
            @Override
            public void onClick() {
            }
        });
        warning.show();
    }
}
