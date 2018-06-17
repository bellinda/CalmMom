package com.angelova.w510.calmmom.models;

import java.io.Serializable;
import java.util.List;

/**
 * Created by W510 on 24.5.2018 г..
 */

public class Examination implements Serializable {

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
}
