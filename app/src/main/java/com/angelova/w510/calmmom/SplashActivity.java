package com.angelova.w510.calmmom;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.support.constraint.ConstraintLayout;
import android.support.constraint.ConstraintSet;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.transition.ChangeBounds;
import android.transition.Transition;
import android.transition.TransitionManager;
import android.view.View;
import android.view.animation.AnticipateOvershootInterpolator;
import android.widget.ProgressBar;

import java.util.Locale;

public class SplashActivity extends AppCompatActivity {

    private ProgressBar mProgress;
    private ConstraintLayout mMainLayout;

    // Splash screen timer
    private static int SPLASH_TIME_OUT = 500;
    private static int OPEN_LOGIN_TIME_OUT = 2000;
    private ConstraintSet constraintSet = new ConstraintSet();

    private static final String SHARED_PREFS_LANGUAGE = "language";
    private static final String LANGUAGE_EN = "en";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        mMainLayout = (ConstraintLayout) findViewById(R.id.main_layout);
        mProgress = (ProgressBar) findViewById(R.id.progress);

        SharedPreferences appPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        final String currentLanguage = appPreferences.getString(SHARED_PREFS_LANGUAGE, LANGUAGE_EN);

        final Handler handler = new Handler();
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                showComponents();
                Runnable openLoginRunnable = new Runnable() {
                    @Override
                    public void run() {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                mProgress.setVisibility(View.GONE);
                            }
                        });

                        updateResources(SplashActivity.this, currentLanguage);

                        Intent intent = new Intent(SplashActivity.this, LoginActivity.class);
                        startActivity(intent);
                        finish();
                    }
                };
                handler.postDelayed(openLoginRunnable, OPEN_LOGIN_TIME_OUT);
            }
        };
        handler.postDelayed(runnable, SPLASH_TIME_OUT);
    }

    private void showComponents() {

        constraintSet.clone(this, R.layout.activity_splash_alt);

        Transition transition = new ChangeBounds();
        transition.setInterpolator(new AnticipateOvershootInterpolator(1.0f));
        transition.setDuration(1000);
        transition.addListener(new Transition.TransitionListener() {
            @Override
            public void onTransitionStart(Transition transition) {

            }

            @Override
            public void onTransitionEnd(Transition transition) {
                mProgress.setVisibility(View.VISIBLE);
            }

            @Override
            public void onTransitionCancel(Transition transition) {

            }

            @Override
            public void onTransitionPause(Transition transition) {

            }

            @Override
            public void onTransitionResume(Transition transition) {

            }
        });

        TransitionManager.beginDelayedTransition(mMainLayout, transition);
        constraintSet.applyTo(mMainLayout);
    }

    private void updateResources(Context context, String language) {
        Locale locale = new Locale(language);
        Locale.setDefault(locale);

        Resources resources = context.getResources();

        Configuration configuration = resources.getConfiguration();
        configuration.locale = locale;

        resources.updateConfiguration(configuration, resources.getDisplayMetrics());
    }
}
