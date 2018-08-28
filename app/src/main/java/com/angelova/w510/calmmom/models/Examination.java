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
    private List<Test> tests;
    private List<Measurement> activities;
    private List<ExaminationDocument> documents;
    private List<ExaminationDocument> images;

    public Examination() {

    }

    public Examination(String title, String date, ExaminationStatus status) {
        this.title = title;
        this.date = date;
        this.status = status;
    }

    public Examination(String title, String date, ExaminationStatus status, List<Test> tests, List<Measurement> activities) {
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

    public List<Test> getTests() {
        return tests;
    }

    public void setTests(List<Test> tests) {
        this.tests = tests;
    }

    public List<Measurement> getActivities() {
        return activities;
    }

    public void setActivities(List<Measurement> activities) {
        this.activities = activities;
    }

    public List<ExaminationDocument> getDocuments() {
        return documents;
    }

    public void setDocuments(List<ExaminationDocument> documents) {
        this.documents = documents;
    }

    public List<ExaminationDocument> getImages() {
        return images;
    }

    public void setImages(List<ExaminationDocument> images) {
        this.images = images;
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
