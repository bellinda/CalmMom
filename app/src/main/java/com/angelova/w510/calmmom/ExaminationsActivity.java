package com.angelova.w510.calmmom;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Build;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.Window;
import android.view.WindowManager;

import com.angelova.w510.calmmom.dialogs.WarnDialog;
import com.angelova.w510.calmmom.dialogs.YesNoDialog;
import com.angelova.w510.calmmom.fragments.ExaminationsFragment;
import com.angelova.w510.calmmom.fragments.QuestionsFragment;
import com.angelova.w510.calmmom.models.Examination;
import com.angelova.w510.calmmom.models.ExaminationStatus;
import com.angelova.w510.calmmom.models.Question;
import com.angelova.w510.calmmom.models.User;
import com.angelova.w510.calmmom.receivers.NextExaminationReceiver;
import com.angelova.w510.calmmom.utils.DateTimeUtils;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.gson.Gson;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class ExaminationsActivity extends AppCompatActivity {

    private FragmentManager fragmentManager;

    private User mUser;
    private String mUserEmail;
    private FirebaseFirestore mDb;
    FirebaseStorage mStorage;
    StorageReference mStorageReference;

    private int pregnancyIndex;

    SharedPreferences mPrefs;
    SharedPreferences.Editor mEditor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_examinations);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(Color.parseColor("#324A5F"));
        }

        mPrefs = PreferenceManager.getDefaultSharedPreferences(this);
        mEditor = mPrefs.edit();

        mUser = new User();
        mDb = FirebaseFirestore.getInstance();
        mStorage = FirebaseStorage.getInstance();
        mStorageReference = mStorage.getReference();
        mUserEmail = getIntent().getStringExtra("email");
        final DocumentReference userRef = mDb.collection("users").document(mUserEmail);
        userRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {

                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document != null) {
                        //The user exists...
                        mUser = document.toObject(User.class);

                        pregnancyIndex = mUser.getPregnancyConsecutiveId();

                        fragmentManager = getSupportFragmentManager();
                        FragmentTransaction transaction = fragmentManager.beginTransaction();
                        Bundle bundle = new Bundle();
                        bundle.putSerializable("user", mUser);
                        bundle.putString("email", mUserEmail);
                        // set Fragmentclass Arguments
                        ExaminationsFragment examinationsFragment = new ExaminationsFragment();
                        examinationsFragment.setArguments(bundle);
                        transaction.replace(R.id.content, examinationsFragment).commit();
                    } else {
                        //The user doesn't exist...
                        //createUser(email);
                    }

                }
            }
        });

        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
    }

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            FragmentTransaction transaction = fragmentManager.beginTransaction();

            switch (item.getItemId()) {
                case R.id.navigation_examinations:
                    Bundle bundle = new Bundle();
                    bundle.putSerializable("user", mUser);
                    bundle.putString("email", mUserEmail);
                    // set Fragmentclass Arguments
                    ExaminationsFragment examinationsFragment = new ExaminationsFragment();
                    examinationsFragment.setArguments(bundle);
                    transaction.replace(R.id.content, examinationsFragment).commit();
                    if(getSupportActionBar() != null) {
                        //getSupportActionBar().setTitle(R.string.title_upload);
//                        toolbarTitle.setText(R.string.title_upload);
                        getSupportActionBar().setDisplayHomeAsUpEnabled(false);
                    }
                    return true;
                case R.id.navigation_questions:
                    Bundle bundleQ = new Bundle();
                    bundleQ.putSerializable("user", mUser);
                    QuestionsFragment questionsFragment = new QuestionsFragment();
                    questionsFragment.setArguments(bundleQ);
                    transaction.replace(R.id.content, questionsFragment).commit();
                    if(getSupportActionBar() != null) {
                        //getSupportActionBar().setTitle(R.string.title_documents);
//                        toolbar.setVisibility(View.VISIBLE);
//                        toolbarTitle.setText(R.string.title_waiting);
                        getSupportActionBar().setDisplayHomeAsUpEnabled(false);
                        //change items in toolbar
                        //WAITING_DOCUMENT_MENU_ITEM_STATE = 0;
                        //invalidateOptionsMenu();
                    }
                    return true;
            }
            return false;
        }

    };

    public void updateExaminationsInDb(List<Examination> examinations) {
        ObjectMapper m = new ObjectMapper();
        mUser.getPregnancies().get(pregnancyIndex).setExaminations(examinations);
        Map<String,Object> user = m.convertValue(mUser, Map.class);

        final DocumentReference userRef = mDb.collection("users").document(mUserEmail);
        userRef.update(user)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        System.out.println("DocumentSnapshot successfully updated!");
                        Gson gson = new Gson();
                        String userJson = gson.toJson(mUser);
                        mEditor.putString("user", userJson);
                        mEditor.apply();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        System.out.println("Error updating document " + e);
                    }
                });
    }

    public void openExaminationDetailsActivity(Examination examination) {
        Intent intent = new Intent(ExaminationsActivity.this, ExaminationDetailsActivity.class);
        intent.putExtra("examination", examination);
        intent.putExtra("user", mUser);
        intent.putExtra("email", mUserEmail);
        startActivity(intent);
        finish();
    }

    public void updateQuestionsInDb(List<Question> questions) {
        ObjectMapper m = new ObjectMapper();
        mUser.getPregnancies().get(pregnancyIndex).setQuestions(questions);
        Map<String, Object> user = m.convertValue(mUser, Map.class);

        final DocumentReference userRef = mDb.collection("users").document(mUserEmail);
        userRef.update(user)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        System.out.println("DocumentSnapshot successfully updated!");
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        System.out.println("Error updating document " + e);
                    }
                });
    }

    public void updateQuestionsList(final Question question) {
        YesNoDialog dialog = new YesNoDialog(ExaminationsActivity.this, getString(R.string.delete_question_text), getString(R.string.dialog_yes_no_positive), getString(R.string.dialog_yes_no_negative), new YesNoDialog.ButtonClickListener() {
            @Override
            public void onPositiveButtonClick() {
                Fragment f = getSupportFragmentManager().findFragmentById(R.id.content);
                if (f instanceof QuestionsFragment) {
                    ((QuestionsFragment) f).removeItemFromListAndUpdateDb(question);
                }
            }

            @Override
            public void onNegativeButtonClick() {

            }
        });
        dialog.show();
    }

    public void scheduleNotificationForNextExamination(List<Examination> examinations) {
        String nextExaminationDate = getNextExaminationDate(examinations);
        if (nextExaminationDate != null) {
            String date = DateTimeUtils.parseDateTime(nextExaminationDate, "yyyy-MM-dd HH:mm", "dd MMM yyyy");
            String time = DateTimeUtils.parseDateTime(nextExaminationDate, "yyyy-MM-dd HH:mm", "HH:mm");
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.getDefault());
            try {
                Date exDate = sdf.parse(nextExaminationDate);
                Calendar calendar = Calendar.getInstance();
                calendar.setTime(exDate);
                calendar.set(Calendar.HOUR, 10);
                calendar.set(Calendar.HOUR_OF_DAY, 10);
                calendar.set(Calendar.MINUTE, 0);
                calendar.set(Calendar.SECOND, 0);

                long when = calendar.getTime().getTime() - (24 * 60 * 60 * 1000); //System.currentTimeMillis() + 60000L;

                AlarmManager am = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
                Intent intent = new Intent(this, NextExaminationReceiver.class);
                intent.putExtra("nextExamination", String.format(getString(R.string.examination_next_date), date, time));
                PendingIntent pendingIntent = PendingIntent.getBroadcast(this, 0, intent, 0);
                am.set(AlarmManager.RTC_WAKEUP, when, pendingIntent);
            } catch (ParseException pe) {
                pe.printStackTrace();
            }
        }
    }

    private String getNextExaminationDate(List<Examination> examinations) {
        for (Examination examination : examinations) {
            if (examination.getStatus() == ExaminationStatus.CURRENT) {
                return examination.getDate();
            }
        }
        return null;
    }

    public void showYesNoDialogNow(String title, String positiveBtnTitle, String negativeBtnTitle, YesNoDialog.ButtonClickListener listener) {
        YesNoDialog dialog = new YesNoDialog(this, title, positiveBtnTitle, negativeBtnTitle, listener);
        dialog.show();
    }

    public void showWarnDialogNow(String title, String message) {
        WarnDialog dialog = new WarnDialog(this, title, message, new WarnDialog.DialogClickListener() {
            @Override
            public void onClick() {

            }
        });
        dialog.show();
    }

}
