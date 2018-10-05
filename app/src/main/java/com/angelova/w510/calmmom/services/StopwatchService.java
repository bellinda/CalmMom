package com.angelova.w510.calmmom.services;

import android.app.Service;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Handler;
import android.os.IBinder;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.util.Log;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.TimeUnit;

public class StopwatchService extends Service {

    public static String serviceReceiver = "com.angelova.w510.calmmom.stopwatch.service.receiver";

    private Handler mHandler = new Handler();
    Calendar calendar;
    SimpleDateFormat simpleDateFormat;
    String strDate;
    Date startTime, currentTime;
    SharedPreferences prefs;
    SharedPreferences.Editor mEditor;

    private Timer mTimer = null;
    public static final long NOTIFY_INTERVAL = 1000;
    Intent intent;

    @Override
    public void onCreate() {
        super.onCreate();

        prefs = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        mEditor = prefs.edit();
        calendar = Calendar.getInstance();
        simpleDateFormat = new SimpleDateFormat("HH:mm:ss.SSS");

        try {
            startTime = simpleDateFormat.parse(prefs.getString("data", ""));
        } catch (ParseException pe) {
            pe.printStackTrace();
        }

        mTimer = new Timer();
        mTimer.scheduleAtFixedRate(new TimeDisplayTimerTask(), 5, NOTIFY_INTERVAL);
        intent = new Intent(serviceReceiver);
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    public void twoDatesBetweenTime() {
        try {
            currentTime = simpleDateFormat.parse(strDate);

            long diff = currentTime.getTime() - startTime.getTime();
            long milliseconds = diff % 1000;
            long seconds = (diff / 1000) % 60;
            long minutes = (diff / 1000) / 60;
            long hours = minutes / 60;

            String timerValue = String.format(Locale.getDefault(), "%02d:%02d:%02d", hours, minutes, seconds);
            Log.e("TIME", timerValue);

            fn_update(timerValue);

            if (hours == 1) {
                mEditor.putBoolean("finish", true).commit();
                mTimer.cancel();
            }
        } catch (Exception ex) {
            mTimer.cancel();
            mTimer.purge();
        }

//            int int_hours = Integer.valueOf(prefs.getString("hours", ""));
//
//            long int_timer = TimeUnit.HOURS.toMillis(int_hours);
//            long long_hours = int_timer - diff;
//            long diffSeconds2 = long_hours / 1000 % 60;
//            long diffMinutes2 = long_hours / (60 * 1000) % 60;
//            long diffHours2 = long_hours / (60 * 60 * 1000) % 24;
//
//
//            if (long_hours > 0) {
//                String str_testing = diffHours2 + ":" + diffMinutes2 + ":" + diffSeconds2;
//
//                Log.e("TIME", str_testing);
//
//                fn_update(str_testing);
//            } else {
//                mEditor.putBoolean("finish", true).commit();
//                mTimer.cancel();
//            }
//        }catch (Exception e){
//            mTimer.cancel();
//            mTimer.purge();
//
//
//        }
//
//        return "";

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mTimer.cancel();
        mTimer.purge();
        Log.e("Service finish","Finish");
    }

    private void fn_update(String str_time){

        intent.putExtra("time",str_time);
        sendBroadcast(intent);
    }

    class TimeDisplayTimerTask extends TimerTask {

        @Override
        public void run() {
            mHandler.post(new Runnable() {

                @Override
                public void run() {

                    calendar = Calendar.getInstance();
                    simpleDateFormat = new SimpleDateFormat("HH:mm:ss.SSS");
                    strDate = simpleDateFormat.format(calendar.getTime());
                    Log.e("strDate", strDate);
                    twoDatesBetweenTime();

                }

            });
        }

    }
}
