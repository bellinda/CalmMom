package com.angelova.w510.calmmom;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.graphics.Color;
import android.os.Build;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.TypedValue;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.LinearInterpolator;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.angelova.w510.calmmom.dialogs.LoadingDialog;
import com.angelova.w510.calmmom.dialogs.StartPregnancyDialog;
import com.angelova.w510.calmmom.models.BabySize;
import com.angelova.w510.calmmom.models.OutcomeType;
import com.angelova.w510.calmmom.models.Pregnancy;
import com.angelova.w510.calmmom.models.PregnancyOutcome;
import com.angelova.w510.calmmom.models.RiskFactor;
import com.angelova.w510.calmmom.models.Tip;
import com.angelova.w510.calmmom.models.User;
import com.angelova.w510.calmmom.models.UserActivity;
import com.angelova.w510.calmmom.models.Weight;
import com.angelova.w510.calmmom.services.StopwatchService;
import com.angelova.w510.calmmom.utils.PregnancyUtils;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.gson.Gson;
import com.melnykov.fab.FloatingActionButton;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.concurrent.TimeUnit;

public class MainActivity extends AppCompatActivity {

    private LinearLayout mExaminationsItem;
    private LinearLayout mHealthStateItem;
    private LinearLayout mTipsItem;
    private LinearLayout mKicksAndContractionsItem;

    private TextView mCurrentDateView;
    private TextView mCurrentWeekView;
    private TextView mBabySizeView;
    private ImageView mSizeImage;
    private TextView mSizeText;
    private LinearLayout mSizeImageLayout;
    private TextView mTooSmallSizeText;
    private View mSeparatorView;
    private Button mStartPregnancyBtn;
    private LinearLayout mPregnancyImage;

    private FloatingActionButton mProfileBtn;

    private User mUser;
    private String mUserEmail;
    private BabySize mCurrentBabySize;
    private int mCurrentPregnancyWeek;
    private FirebaseFirestore mDb;

    SharedPreferences mPrefs;
    SharedPreferences.Editor mEditor;

    LoadingDialog mLoadingDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(Color.parseColor("#324A5F"));
        }

        mUser = (User) getIntent().getSerializableExtra("user");
        mUserEmail = getIntent().getStringExtra("email");
        mDb = FirebaseFirestore.getInstance();

        mPrefs = PreferenceManager.getDefaultSharedPreferences(this);
        mEditor = mPrefs.edit();

        mCurrentDateView = (TextView) findViewById(R.id.current_date_view);
        SimpleDateFormat sdf = new SimpleDateFormat("dd MMM yyyy", Locale.getDefault());
        mCurrentDateView.setText(sdf.format(Calendar.getInstance().getTime()));

        mCurrentWeekView = (TextView) findViewById(R.id.current_week_view);
        mCurrentPregnancyWeek = (int) (getDaysSinceDate(mUser.getPregnancies().get(mUser.getPregnancyConsecutiveId()).getFirstDayOfLastMenstruation()) / 7 + 1);
        mCurrentWeekView.setText(String.format("%d %s", mCurrentPregnancyWeek, getString(R.string.main_activity_weeks)));

        mBabySizeView = (TextView) findViewById(R.id.baby_size_view);
        mSizeImage = (ImageView) findViewById(R.id.size_image);
        mSizeText = (TextView) findViewById(R.id.size_text);
        mSizeImageLayout = (LinearLayout) findViewById(R.id.size_image_layout);
        mTooSmallSizeText = (TextView) findViewById(R.id.too_small_view);
        mSeparatorView = findViewById(R.id.separator);
        mStartPregnancyBtn = (Button) findViewById(R.id.start_pregnancy_btn);
        mPregnancyImage = (LinearLayout) findViewById(R.id.pregnancy_image);

        if (mUser.getPregnancies().get(mUser.getPregnancyConsecutiveId()).getPregnancyOutcome() == null) {
            getSizes();
            setImageAndTextBasedOnWeek();
        } else {
            PregnancyOutcome outcome = mUser.getPregnancies().get(mUser.getPregnancyConsecutiveId()).getPregnancyOutcome();
            loadOutcomeData(outcome);
        }

        mExaminationsItem = (LinearLayout) findViewById(R.id.examinations_layout);

        mExaminationsItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, ExaminationsActivity.class);
                intent.putExtra("email", getIntent().getStringExtra("email"));
                startActivity(intent);
                //finish();
            }
        });

        mHealthStateItem = (LinearLayout) findViewById(R.id.health_state_layout);

        mHealthStateItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, HealthStateActivity.class);
                intent.putExtra("email", getIntent().getStringExtra("email"));
                startActivity(intent);
            }
        });

        mTipsItem = (LinearLayout) findViewById(R.id.tip_layout);

        mTipsItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, TipsActivity.class);
                intent.putExtra("user", mUser);
                intent.putExtra("email", getIntent().getStringExtra("email"));
                startActivity(intent);
            }
        });

        mKicksAndContractionsItem = (LinearLayout) findViewById(R.id.kicks_contractions_layout);

        mKicksAndContractionsItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, KicksAndContractionsActivity.class);
                intent.putExtra("user", mUser);
                intent.putExtra("email", getIntent().getStringExtra("email"));
                startActivity(intent);
            }
        });

        mProfileBtn = (FloatingActionButton) findViewById(R.id.profile_btn);
        mProfileBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, ProfileActivity.class);
                intent.putExtra("user", mUser);
                intent.putExtra("email", getIntent().getStringExtra("email"));
                startActivity(intent);
            }
        });

        if (mPrefs.getBoolean("shouldReload", false)) {
            getUserData();
            mEditor.remove("shouldReload").commit();
        }

        if (mUser.getPregnancies().get(mUser.getPregnancyConsecutiveId()).getPregnancyOutcome() == null) {

            if (hasNotDoneTips()) {
                blinkTipsButton();
            }

            Pregnancy currentPregnancy = mUser.getPregnancies().get(mUser.getPregnancyConsecutiveId());
            Weight currentWeekWeight = getWeightByWeek(currentPregnancy, mCurrentPregnancyWeek);
            Weight previousWeekWeight = getWeightByWeek(currentPregnancy, mCurrentPregnancyWeek - 5);
            if (currentWeekWeight != null && previousWeekWeight != null) {
                List<UserActivity> currentWeekActivities = currentPregnancy.getActivities().get(Integer.toString(mCurrentPregnancyWeek));
                if ((currentWeekActivities == null || currentWeekActivities.size() == 0) && currentWeekWeight.getValue() - previousWeekWeight.getValue() > 0) {
                    if (!isNotEnoughActivitiesTipPresent()) {
                        mUser.getCustomTips().add(new Tip(null, getString(R.string.custom_tip_not_enough_activities), getLocalizedResources(MainActivity.this, new Locale("bg")).getString(R.string.custom_tip_not_enough_activities), true, false));
                        updateUser();
                    }
                } else {
                    removeNotEnoughActivitiesTipIfPresent();
                }
            } else {
                removeNotEnoughActivitiesTipIfPresent();
            }
        }

        mStartPregnancyBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                StartPregnancyDialog dialog = new StartPregnancyDialog(MainActivity.this, mUser, new StartPregnancyDialog.DialogClickListener() {
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
                            customTips.add(new Tip(null, getString(R.string.custom_tip_pregnancies_history), getLocalizedResources(MainActivity.this, new Locale("bg")).getString(R.string.custom_tip_pregnancies_history), true, false));
                        }
                        if (mUser.getFamilyHistories() != null) {
                            customTips.add(new Tip(null, getString(R.string.custom_tip_family_history), getLocalizedResources(MainActivity.this, new Locale("bg")).getString(R.string.custom_tip_family_history), true, false));
                        }
                        mUser.setCustomTips(customTips);

                        updateUser();

                        mLoadingDialog = new LoadingDialog(MainActivity.this, getString(R.string.main_activity_reloading_msg));
                        mLoadingDialog.show();
                        getUserData();
                    }
                });
                dialog.show();
            }
        });
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

    private void getSizes() {
        mDb.collection("sizes").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    ObjectMapper mapper = new ObjectMapper();
                    for (QueryDocumentSnapshot document : task.getResult()) {
                        Map<String, Object> sizes = document.getData();
                        if (mCurrentPregnancyWeek >= 40) {
                            mCurrentBabySize = mapper.convertValue(sizes.get("40"), BabySize.class);
                        } else if (mCurrentPregnancyWeek <= 0) {
                            mCurrentBabySize = mapper.convertValue(sizes.get("1"), BabySize.class);
                        } else {
                            mCurrentBabySize = mapper.convertValue(sizes.get(Integer.toString(mCurrentPregnancyWeek)), BabySize.class);
                        }
                    }
                    if (mCurrentBabySize != null) {
                        mBabySizeView.setText(String.format(getString(R.string.main_activity_size_text), Float.toString(mCurrentBabySize.getWeight()), Float.toString(mCurrentBabySize.getLength())));
                    } else {
                        mBabySizeView.setText("");
                    }
                    if (mLoadingDialog != null) {
                        mLoadingDialog.dismiss();
                    }
                }
            }
        });
    }

    private void getUserData() {
        final DocumentReference userRef = mDb.collection("users").document(mUserEmail);
        userRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {

                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document != null) {
                        mUser = document.toObject(User.class);
                        reloadData();
                    } else {
                        //The user doesn't exist...
                        //createUser(email);
                    }

                }
            }
        });
    }

    private void reloadData() {
        if (mUser.getPregnancies().get(mUser.getPregnancyConsecutiveId()).getPregnancyOutcome() == null) {
            mCurrentPregnancyWeek = (int) (getDaysSinceDate(mUser.getPregnancies().get(mUser.getPregnancyConsecutiveId()).getFirstDayOfLastMenstruation()) / 7 + 1);
            mCurrentWeekView.setText(String.format(Locale.getDefault(), "%d %s", mCurrentPregnancyWeek, getString(R.string.main_activity_weeks)));
            mCurrentDateView.setVisibility(View.VISIBLE);
            getSizes();
            setImageAndTextBasedOnWeek();
            if (mStartPregnancyBtn.getVisibility() == View.VISIBLE) {
                mSizeImageLayout.setVisibility(View.VISIBLE);
                mSizeText.setVisibility(View.VISIBLE);
                mCurrentDateView.setVisibility(View.VISIBLE);
                mCurrentWeekView.setVisibility(View.VISIBLE);
                mSeparatorView.setVisibility(View.VISIBLE);
                mBabySizeView.setVisibility(View.VISIBLE);
                mSizeImage.setVisibility(View.VISIBLE);
                mStartPregnancyBtn.setVisibility(View.GONE);
                mPregnancyImage.setVisibility(View.GONE);
            }
        } else {
            PregnancyOutcome outcome = mUser.getPregnancies().get(mUser.getPregnancyConsecutiveId()).getPregnancyOutcome();
            loadOutcomeData(outcome);
        }
    }

    private void loadOutcomeData(PregnancyOutcome outcome) {
        if (outcome.getOutcomeType() == OutcomeType.LiveBirth) {
            mSizeImageLayout.setVisibility(View.VISIBLE);
            mSizeText.setVisibility(View.VISIBLE);
            mTooSmallSizeText.setVisibility(View.GONE);
            mCurrentDateView.setVisibility(View.GONE);
            mStartPregnancyBtn.setVisibility(View.GONE);
            mPregnancyImage.setVisibility(View.GONE);

            mCurrentWeekView.setText(String.format(getString(R.string.main_activity_gave_birth), outcome.getDate(), outcome.getTime()));
            mBabySizeView.setText(String.format(getString(R.string.main_activity_baby_size), outcome.getBabyLength(), outcome.getBabyWeight()));

            if (outcome.getBabySex().equals("m")) {
                mSizeImage.setBackground(ContextCompat.getDrawable(MainActivity.this, R.drawable.ic_baby_boy));
            } else {
                mSizeImage.setBackground(ContextCompat.getDrawable(MainActivity.this, R.drawable.ic_baby_girl));
            }
            mSizeText.setText(outcome.getBabyName());
            mSizeText.setTextSize(TypedValue.COMPLEX_UNIT_SP, 18);
        } else {
            mSizeImageLayout.setVisibility(View.GONE);
            mSizeText.setVisibility(View.GONE);
            mTooSmallSizeText.setVisibility(View.GONE);
            mCurrentDateView.setVisibility(View.VISIBLE);
            mCurrentWeekView.setVisibility(View.GONE);
            mSeparatorView.setVisibility(View.GONE);
            mBabySizeView.setVisibility(View.GONE);
            mSizeImage.setVisibility(View.GONE);
            mStartPregnancyBtn.setVisibility(View.VISIBLE);
            mPregnancyImage.setVisibility(View.VISIBLE);

            mStartPregnancyBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                }
            });
        }

        if (mLoadingDialog != null) {
            mLoadingDialog.dismiss();
        }
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

    private boolean hasNotDoneTips() {
        if (mUser.getCustomTips() == null) {
            return false;
        }
        for (Tip tip : mUser.getCustomTips()) {
            if (!tip.isDone()) {
                return true;
            }
        }
        return false;
    }

    private void blinkTipsButton() {
        Animation animation = new AlphaAnimation(1, 0);
        animation.setDuration(1000);
        animation.setInterpolator(new LinearInterpolator());
        animation.setRepeatCount(Animation.INFINITE);
        animation.setRepeatMode(Animation.REVERSE);
        mTipsItem.startAnimation(animation);
    }

    private Weight getWeightByWeek(Pregnancy pregnancy, int week) {
        for (Weight weight : pregnancy.getWeights()) {
            if (weight.getWeek() == week) {
                return weight;
            }
        }
        return null;
    }

    private void removeNotEnoughActivitiesTipIfPresent() {
        if (mUser.getCustomTips() != null) {
            for (int i = 0; i < mUser.getCustomTips().size(); i++) {
                Tip tip = mUser.getCustomTips().get(i);
                if (tip.getContent().equals(getString(R.string.custom_tip_not_enough_activities))) {
                    mUser.getCustomTips().remove(i);
                    updateUser();
                    break;
                }
            }
        }
    }

    private boolean isNotEnoughActivitiesTipPresent() {
        if (mUser.getCustomTips() != null) {
            for (int i = 0; i < mUser.getCustomTips().size(); i++) {
                Tip tip = mUser.getCustomTips().get(i);
                if (tip.getContent().equals(getString(R.string.custom_tip_not_enough_activities))) {
                    return true;
                }
            }
        }
        return false;
    }

    private void setImageAndTextBasedOnWeek() {
        mSizeImageLayout.setVisibility(View.VISIBLE);
        mSizeText.setVisibility(View.VISIBLE);
        mTooSmallSizeText.setVisibility(View.GONE);

        switch (mCurrentPregnancyWeek) {
            case 1:
            case 2:
            case 3:
                mSizeImageLayout.setVisibility(View.GONE);
                mSizeText.setVisibility(View.GONE);
                mTooSmallSizeText.setVisibility(View.VISIBLE);
                break;
            case 4:
                mSizeImage.setBackground(ContextCompat.getDrawable(MainActivity.this, R.drawable.ic_apple_seeds));
                mSizeText.setText(getString(R.string.size_apple_seeds));
                break;
            case 5:
                mSizeImage.setBackground(ContextCompat.getDrawable(MainActivity.this, R.drawable.ic_black_pepper_corn));
                mSizeText.setText(getString(R.string.size_black_pepper_corn));
                break;
            case 6:
                mSizeImage.setBackground(ContextCompat.getDrawable(MainActivity.this, R.drawable.ic_pea));
                mSizeText.setText(getString(R.string.size_pea));
                break;
            case 7:
                mSizeImage.setBackground(ContextCompat.getDrawable(MainActivity.this, R.drawable.ic_blueberry));
                mSizeText.setText(getString(R.string.size_blueberry));
                break;
            case 8:
                mSizeImage.setBackground(ContextCompat.getDrawable(MainActivity.this, R.drawable.ic_olive));
                mSizeText.setText(getString(R.string.size_olive));
                break;
            case 9:
                mSizeImage.setBackground(ContextCompat.getDrawable(MainActivity.this, R.drawable.ic_raspberry));
                mSizeText.setText(getString(R.string.size_raspberry));
                break;
            case 10:
                mSizeImage.setBackground(ContextCompat.getDrawable(MainActivity.this, R.drawable.ic_strawberry));
                mSizeText.setText(getString(R.string.size_strawberry));
                break;
            case 11:
                mSizeImage.setBackground(ContextCompat.getDrawable(MainActivity.this, R.drawable.ic_apricote));
                mSizeText.setText(getString(R.string.size_apricot));
                break;
            case 12:
                mSizeImage.setBackground(ContextCompat.getDrawable(MainActivity.this, R.drawable.ic_fig));
                mSizeText.setText(getString(R.string.size_fig));
                break;
            case 13:
                mSizeImage.setBackground(ContextCompat.getDrawable(MainActivity.this, R.drawable.ic_plum));
                mSizeText.setText(getString(R.string.size_plum));
                break;
            case 14:
                mSizeImage.setBackground(ContextCompat.getDrawable(MainActivity.this, R.drawable.ic_lemon));
                mSizeText.setText(getString(R.string.size_lemon));
                break;
            case 15:
                mSizeImage.setBackground(ContextCompat.getDrawable(MainActivity.this, R.drawable.ic_peach));
                mSizeText.setText(getString(R.string.size_peach));
                break;
            case 16:
                mSizeImage.setBackground(ContextCompat.getDrawable(MainActivity.this, R.drawable.ic_orange));
                mSizeText.setText(getString(R.string.size_orange));
                break;
            case 17:
                mSizeImage.setBackground(ContextCompat.getDrawable(MainActivity.this, R.drawable.ic_avocado));
                mSizeText.setText(getString(R.string.size_avocado));
                break;
            case 18:
                mSizeImage.setBackground(ContextCompat.getDrawable(MainActivity.this, R.drawable.ic_onion));
                mSizeText.setText(getString(R.string.size_onion));
                break;
            case 19:
                mSizeImage.setBackground(ContextCompat.getDrawable(MainActivity.this, R.drawable.ic_pomegranate));
                mSizeText.setText(getString(R.string.size_pomegranate));
                break;
            case 20:
                mSizeImage.setBackground(ContextCompat.getDrawable(MainActivity.this, R.drawable.ic_beef_tomato));
                mSizeText.setText(getString(R.string.size_beef_tomato));
                break;
            case 21:
                mSizeImage.setBackground(ContextCompat.getDrawable(MainActivity.this, R.drawable.ic_pepper));
                mSizeText.setText(getString(R.string.size_pepper));
                break;
            case 22:
                mSizeImage.setBackground(ContextCompat.getDrawable(MainActivity.this, R.drawable.ic_sweet_potatoe));
                mSizeText.setText(getString(R.string.size_sweet_potato));
                break;
            case 23:
                mSizeImage.setBackground(ContextCompat.getDrawable(MainActivity.this, R.drawable.ic_mango));
                mSizeText.setText(getString(R.string.size_mango));
                break;
            case 24:
                mSizeImage.setBackground(ContextCompat.getDrawable(MainActivity.this, R.drawable.ic_papaya));
                mSizeText.setText(getString(R.string.size_papaya));
                break;
            case 25:
                mSizeImage.setBackground(ContextCompat.getDrawable(MainActivity.this, R.drawable.ic_grapefruit));
                mSizeText.setText(getString(R.string.size_grapefruit));
                break;
            case 26:
                mSizeImage.setBackground(ContextCompat.getDrawable(MainActivity.this, R.drawable.ic_swede));
                mSizeText.setText(getString(R.string.size_swede));
                break;
            case 27:
                mSizeImage.setBackground(ContextCompat.getDrawable(MainActivity.this, R.drawable.ic_broccoli_head));
                mSizeText.setText(getString(R.string.size_broccoli_head));
                break;
            case 28:
                mSizeImage.setBackground(ContextCompat.getDrawable(MainActivity.this, R.drawable.ic_canteloupe_melon));
                mSizeText.setText(getString(R.string.size_canteloupe_melon));
                break;
            case 29:
                mSizeImage.setBackground(ContextCompat.getDrawable(MainActivity.this, R.drawable.ic_cauliflower));
                mSizeText.setText(getString(R.string.size_cauliflower));
                break;
            case 30:
                mSizeImage.setBackground(ContextCompat.getDrawable(MainActivity.this, R.drawable.ic_iceberg_lettuce));
                mSizeText.setText(getString(R.string.size_iceberg_lettuce));
                break;
            case 31:
                mSizeImage.setBackground(ContextCompat.getDrawable(MainActivity.this, R.drawable.ic_aubergine));
                mSizeText.setText(getString(R.string.size_aubergine));
                break;
            case 32:
                mSizeImage.setBackground(ContextCompat.getDrawable(MainActivity.this, R.drawable.ic_coconut));
                mSizeText.setText(getString(R.string.size_coconut));
                break;
            case 33:
                mSizeImage.setBackground(ContextCompat.getDrawable(MainActivity.this, R.drawable.ic_butternut_squash));
                mSizeText.setText(getString(R.string.size_butternut_squash));
                break;
            case 34:
                mSizeImage.setBackground(ContextCompat.getDrawable(MainActivity.this, R.drawable.ic_cabbage));
                mSizeText.setText(getString(R.string.size_cabbage));
                break;
            case 35:
                mSizeImage.setBackground(ContextCompat.getDrawable(MainActivity.this, R.drawable.ic_pineapple));
                mSizeText.setText(getString(R.string.size_pineapple));
                break;
            case 36:
                mSizeImage.setBackground(ContextCompat.getDrawable(MainActivity.this, R.drawable.ic_bananas));
                mSizeText.setText(getString(R.string.size_bananas));
                break;
            case 37:
                mSizeImage.setBackground(ContextCompat.getDrawable(MainActivity.this, R.drawable.ic_melon));
                mSizeText.setText(getString(R.string.size_melon));
                break;
            case 38:
                mSizeImage.setBackground(ContextCompat.getDrawable(MainActivity.this, R.drawable.ic_marrow));
                mSizeText.setText(getString(R.string.size_marrow));
                break;
            case 39:
                mSizeImage.setBackground(ContextCompat.getDrawable(MainActivity.this, R.drawable.ic_pumpkin));
                mSizeText.setText(getString(R.string.size_pumpkin));
                break;
            case 40:
                mSizeImage.setBackground(ContextCompat.getDrawable(MainActivity.this, R.drawable.ic_watermelon));
                mSizeText.setText(getString(R.string.size_watermelon));
                break;
        }
    }

    @NonNull
    Resources getLocalizedResources(Context context, Locale desiredLocale) {
        Configuration conf = context.getResources().getConfiguration();
        conf = new Configuration(conf);
        conf.setLocale(desiredLocale);
        Context localizedContext = context.createConfigurationContext(conf);
        return localizedContext.getResources();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

        System.out.println("DESTROYING");
        Intent intent = new Intent(this, StopwatchService.class);
        stopService(intent);

        mEditor.clear().commit();
    }

    @Override
    protected void onPause() {
        super.onPause();

        if (mTipsItem.getAnimation() != null) {
            mTipsItem.clearAnimation();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();

        if (mPrefs.getBoolean("shouldReload", false)) {
            mLoadingDialog = new LoadingDialog(MainActivity.this, getString(R.string.main_activity_reloading_msg));
            mLoadingDialog.show();
            getUserData();
            mEditor.remove("shouldReload").commit();
        } else if (mPrefs.getBoolean("shouldUpdateLanguage", false)) {
            mEditor.remove("shouldUpdateLanguage").commit();
            finish();
            startActivity(getIntent());
        } else if (!mPrefs.getString("user", "").isEmpty()) {
            Gson gson = new Gson();
            String userJson = mPrefs.getString("user", "");
            mUser = gson.fromJson(userJson, User.class);
            mEditor.remove("user").commit();
        }
    }

    @Override
    public void onBackPressed() {

    }
}
