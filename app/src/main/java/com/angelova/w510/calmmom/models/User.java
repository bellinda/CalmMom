package com.angelova.w510.calmmom.models;

import android.net.Uri;

import java.util.List;

/**
 * Created by W510 on 12.5.2018 г..
 */

public class User {

    private String name;
    private int age;
    private double currentHeight;
    private double currentWeight;
    private List<Illness> illnesses;
    private List<Surgery> surgeries;
    private List<FamilyHistory> familyHistories;
    private String profileImage;
    private String firstDayOfLastMenstruation;
    private boolean regularMenstruation;
    private int lengthOfMenstruation;
    private int durationOfMenstruation;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public double getCurrentHeight() {
        return currentHeight;
    }

    public void setCurrentHeight(double currentHeight) {
        this.currentHeight = currentHeight;
    }

    public double getCurrentWeight() {
        return currentWeight;
    }

    public void setCurrentWeight(double currentWeight) {
        this.currentWeight = currentWeight;
    }

    public List<Illness> getIllnesses() {
        return illnesses;
    }

    public void setIllnesses(List<Illness> illnesses) {
        this.illnesses = illnesses;
    }

    public List<Surgery> getSurgeries() {
        return surgeries;
    }

    public void setSurgeries(List<Surgery> surgeries) {
        this.surgeries = surgeries;
    }

    public List<FamilyHistory> getFamilyHistories() {
        return familyHistories;
    }

    public void setFamilyHistories(List<FamilyHistory> familyHistories) {
        this.familyHistories = familyHistories;
    }

    public String getProfileImage() {
        return profileImage;
    }

    public void setProfileImage(String profileImage) {
        this.profileImage = profileImage;
    }

    public String getFirstDayOfLastMenstruation() {
        return firstDayOfLastMenstruation;
    }

    public void setFirstDayOfLastMenstruation(String firstDayOfLastMenstruation) {
        this.firstDayOfLastMenstruation = firstDayOfLastMenstruation;
    }

    public boolean isRegularMenstruation() {
        return regularMenstruation;
    }

    public void setRegularMenstruation(boolean regularMenstruation) {
        this.regularMenstruation = regularMenstruation;
    }

    public int getLengthOfMenstruation() {
        return lengthOfMenstruation;
    }

    public void setLengthOfMenstruation(int lengthOfMenstruation) {
        this.lengthOfMenstruation = lengthOfMenstruation;
    }

    public int getDurationOfMenstruation() {
        return durationOfMenstruation;
    }

    public void setDurationOfMenstruation(int durationOfMenstruation) {
        this.durationOfMenstruation = durationOfMenstruation;
    }
}
