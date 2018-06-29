package com.angelova.w510.calmmom.models;

import android.support.annotation.NonNull;

import java.io.Serializable;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

/**
 * Created by W510 on 24.5.2018 г..
 */

public class Examination implements Serializable, Comparable<Examination> {

    private String date;
    private ExaminationStatus status;
    private String title;
    private List<String> tests;
    private List<String> activities;

    public Examination() {

    }

    public Examination(String title, String date, ExaminationStatus status) {
        this.title = title;
        this.date = date;
        this.status = status;
    }

    public Examination(String title, String date, ExaminationStatus status, List<String> tests, List<String> activities) {
        this.title = title;
        this.date = date;
        this.status = status;
        this.tests = tests;
        this.activities = activities;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public ExaminationStatus getStatus() {
        return status;
    }

    public void setStatus(ExaminationStatus status) {
        this.status = status;
    }

    public List<String> getTests() {
        return tests;
    }

    public void setTests(List<String> tests) {
        this.tests = tests;
    }

    public List<String> getActivities() {
        return activities;
    }

    public void setActivities(List<String> activities) {
        this.activities = activities;
    }

    @Override
    public int compareTo(@NonNull Examination examination) {
        String serverDateFormat = "yyyy-MM-dd HH:mm";
        SimpleDateFormat formatter = new SimpleDateFormat(serverDateFormat, Locale.UK);
        if (getDate().isEmpty()) {
            return 1;
        } else if (examination.getDate().isEmpty()) {
            return -1;
        }
        try {
            Date date = formatter.parse(getDate());
            Date examinationDate = formatter.parse(examination.getDate());
            return date.compareTo(examinationDate);
        } catch (ParseException pe) {
            pe.printStackTrace();
        }
        return 0;
    }
}
