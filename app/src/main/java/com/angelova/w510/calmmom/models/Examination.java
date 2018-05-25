package com.angelova.w510.calmmom.models;

/**
 * Created by W510 on 24.5.2018 г..
 */

public class Examination {

    private String date;
    private ExaminationStatus status;
    private String title;

    public Examination(String title, String date, ExaminationStatus status) {
        this.title = title;
        this.date = date;
        this.status = status;
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
}
