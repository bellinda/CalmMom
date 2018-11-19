package com.angelova.w510.calmmom.models;

import android.support.annotation.NonNull;

import com.angelova.w510.calmmom.utils.DateTimeUtils;

import org.apache.commons.lang3.LocaleUtils;

import java.io.Serializable;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class Contraction implements Serializable, Comparable<Contraction> {

    private String date;
    private String startTime;
    private String endTime;
    private String duration;
    private boolean painful;
    private int painGrade;

    public Contraction() {}

    public Contraction(String date, String startTime, String endTime, String duration, boolean painful, int painGrade) {
        this.date = date;
        this.startTime = startTime;
        this.endTime = endTime;
        this.duration = duration;
        this.painful = painful;
        this.painGrade = painGrade;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getStartTime() {
        return startTime;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    public String getEndTime() {
        return endTime;
    }

    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }

    public String getDuration() {
        return duration;
    }

    public void setDuration(String duration) {
        this.duration = duration;
    }

    public boolean isPainful() {
         return painful;
    }

    public void setPainful(boolean painful) {
        this.painful = painful;
    }

    public int getPainGrade() {
        return painGrade;
    }

    public void setPainGrade(int painGrade) {
        this.painGrade = painGrade;
    }

    @Override
    public int compareTo(@NonNull Contraction contraction) {
        SimpleDateFormat sdfEn = new SimpleDateFormat("dd MMM yyyy, HH:mm:ss", Locale.US);
        SimpleDateFormat sdf2En = new SimpleDateFormat("dd.MM.yyyy, HH:mm:ss", Locale.US);
        SimpleDateFormat sdfBg = new SimpleDateFormat("dd MMM yyyy, HH:mm:ss", LocaleUtils.toLocale("bg"));
        SimpleDateFormat sdf2Bg = new SimpleDateFormat("dd.MM.yyyy, HH:mm:ss", LocaleUtils.toLocale("bg"));

        try {
            Date currentItemDate;
            Date otherItemDate;
            if (DateTimeUtils.isDateInEn(this.getDate())) {
                currentItemDate = sdf2En.parse(sdf2En.format(sdfEn.parse(this.getDate() + ", " + this.getStartTime())));
            } else {
                currentItemDate = sdf2Bg.parse(sdf2Bg.format(sdfBg.parse(this.getDate() + ", " + this.getStartTime())));
            }
            if (DateTimeUtils.isDateInEn(contraction.getDate())) {
                otherItemDate = sdf2En.parse(sdf2En.format(sdfEn.parse(contraction.getDate() + ", " + contraction.getStartTime())));
            } else {
                otherItemDate = sdf2Bg.parse(sdf2Bg.format(sdfBg.parse(contraction.getDate() + ", " + contraction.getStartTime())));
            }

            if (currentItemDate.after(otherItemDate)) {
                return -1;
            } else if (currentItemDate.before(otherItemDate)) {
                return 1;
            }
        } catch (ParseException pe) {
            pe.printStackTrace();
        }
        return 0;
    }
}
