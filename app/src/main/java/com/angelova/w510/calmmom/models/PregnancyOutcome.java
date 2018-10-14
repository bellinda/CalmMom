package com.angelova.w510.calmmom.models;

import java.io.Serializable;

public class PregnancyOutcome implements Serializable {

    private OutcomeType outcomeType;
    private String date;
    private String time;
    private String babySex;
    private String babyName;
    private String babyWeight;
    private int babyLength;
    private String complications;
    private int hoursOfLabour;
    private String comments;

    public PregnancyOutcome() {}

    public OutcomeType getOutcomeType() {
        return outcomeType;
    }

    public void setOutcomeType(OutcomeType outcomeType) {
        this.outcomeType = outcomeType;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getBabySex() {
        return babySex;
    }

    public void setBabySex(String babySex) {
        this.babySex = babySex;
    }

    public String getBabyName() {
        return babyName;
    }

    public void setBabyName(String babyName) {
        this.babyName = babyName;
    }

    public String getBabyWeight() {
        return babyWeight;
    }

    public void setBabyWeight(String babyWeight) {
        this.babyWeight = babyWeight;
    }

    public int getBabyLength() {
        return babyLength;
    }

    public void setBabyLength(int babyLength) {
        this.babyLength = babyLength;
    }

    public String getComplications() {
        return complications;
    }

    public void setComplications(String complications) {
        this.complications = complications;
    }

    public int getHoursOfLabour() {
        return hoursOfLabour;
    }

    public void setHoursOfLabour(int hoursOfLabour) {
        this.hoursOfLabour = hoursOfLabour;
    }

    public String getComments() {
        return comments;
    }

    public void setComments(String comments) {
        this.comments = comments;
    }
}
