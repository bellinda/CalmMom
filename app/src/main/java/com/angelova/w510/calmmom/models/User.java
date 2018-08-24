package com.angelova.w510.calmmom.models;

import java.io.Serializable;
import java.util.HashMap;
import java.util.List;

/**
 * Created by W510 on 12.5.2018 г..
 */

public class User implements Serializable {

    private String name;
    private int age;
    private double currentHeight;
    private double currentWeight;
    private String infectiousDiseases;
    private String otherIllnesses;
    private String surgeries;
    private String familyHistories;
    private String complicationsOtherPregnancies;
    private String profileImage;
    private String firstDayOfLastMenstruation;
    private boolean regularMenstruation;
    private int lengthOfMenstruation;
    private int durationOfMenstruation;
    private List<Examination> examinations;
    private List<Question> questions;
    private List<Weight> weights;
    private HashMap<String, List<UserActivity>> activities;
    private List<RiskFactor> riskFactors;
    private int desiredAbortions;
    private int miscarriages;
    private int abortionsOnMedEvidence;
    private String takenMedicines;
    private boolean bloodGroupIncompatibility;
    private boolean unwantedPregnancy;

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

    public String getInfectiousDiseases() {
        return infectiousDiseases;
    }

    public void setInfectiousDiseases(String infectiousDiseases) {
        this.infectiousDiseases = infectiousDiseases;
    }

    public String getOtherIllnesses() {
        return otherIllnesses;
    }

    public void setOtherIllnesses(String illnesses) {
        this.otherIllnesses = illnesses;
    }

    public String getSurgeries() {
        return surgeries;
    }

    public void setSurgeries(String surgeries) {
        this.surgeries = surgeries;
    }

    public String getFamilyHistories() {
        return familyHistories;
    }

    public void setFamilyHistories(String familyHistories) {
        this.familyHistories = familyHistories;
    }

    public String getComplicationsOtherPregnancies() {
        return complicationsOtherPregnancies;
    }

    public void setComplicationsOtherPregnancies(String complicationsOtherPregnancies) {
        this.complicationsOtherPregnancies = complicationsOtherPregnancies;
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

    public List<Examination> getExaminations() {
        return examinations;
    }

    public void setExaminations(List<Examination> examinations) {
        this.examinations = examinations;
    }

    public List<Question> getQuestions() {
        return questions;
    }

    public void setQuestions(List<Question> questions) {
        this.questions = questions;
    }

    public List<Weight> getWeights() {
        return weights;
    }

    public void setWeights(List<Weight> weights) {
        this.weights = weights;
    }

    public HashMap<String, List<UserActivity>> getActivities() {
        return activities;
    }

    public void setActivities(HashMap<String, List<UserActivity>> activities) {
        this.activities = activities;
    }

    public List<RiskFactor> getRiskFactors() {
        return riskFactors;
    }

    public void setRiskFactors(List<RiskFactor> riskFactors) {
        this.riskFactors = riskFactors;
    }

    public int getDesiredAbortions() {
        return desiredAbortions;
    }

    public void setDesiredAbortions(int desiredAbortions) {
        this.desiredAbortions = desiredAbortions;
    }

    public int getMiscarriages() {
        return miscarriages;
    }

    public void setMiscarriages(int miscarriages) {
        this.miscarriages = miscarriages;
    }

    public int getAbortionsOnMedEvidence() {
        return abortionsOnMedEvidence;
    }

    public void setAbortionsOnMedEvidence(int abortionsOnMedEvidence) {
        this.abortionsOnMedEvidence = abortionsOnMedEvidence;
    }

    public String getTakenMedicines() {
        return takenMedicines;
    }

    public void setTakenMedicines(String takenMedicines) {
        this.takenMedicines = takenMedicines;
    }

    public boolean isBloodGroupIncompatibility() {
        return bloodGroupIncompatibility;
    }

    public void setBloodGroupIncompatibility(boolean bloodGroupIncompatibility) {
        this.bloodGroupIncompatibility = bloodGroupIncompatibility;
    }

    public boolean isUnwantedPregnancy() {
        return unwantedPregnancy;
    }

    public void setUnwantedPregnancy(boolean unwantedPregnancy) {
        this.unwantedPregnancy = unwantedPregnancy;
    }
}
