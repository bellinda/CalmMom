package com.angelova.w510.calmmom.models;

import android.support.annotation.NonNull;

import java.io.Serializable;

public class Contraction implements Serializable {

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
}
