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
    private boolean regularMenstruation;
    private int lengthOfMenstruation;
    private int durationOfMenstruation;
    private List<RiskFactor> riskFactors;
    private int pregnancyCount;
    private String lastDeliveryDate;
    private int liveBornKids;
    private int stillbornKids;
    private int prematureKids;
    private int posttermKids;
    private String sterilityDiagnosis;
    private int desiredAbortions;
    private int miscarriages;
    private int abortionsOnMedEvidence;
    private String takenMedicines;
    private List<Pregnancy> pregnancies;
    private int pregnancyConsecutiveId;

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

    public List<RiskFactor> getRiskFactors() {
        return riskFactors;
    }

    public void setRiskFactors(List<RiskFactor> riskFactors) {
        this.riskFactors = riskFactors;
    }

    public int getPregnancyCount() {
        return pregnancyCount;
    }

    public void setPregnancyCount(int pregnancyCount) {
        this.pregnancyCount = pregnancyCount;
    }

    public String getLastDeliveryDate() {
        return lastDeliveryDate;
    }

    public void setLastDeliveryDate(String lastDeliveryDate) {
        this.lastDeliveryDate = lastDeliveryDate;
    }

    public int getLiveBornKids() {
        return liveBornKids;
    }

    public void setLiveBornKids(int liveBornKids) {
        this.liveBornKids = liveBornKids;
    }

    public int getStillbornKids() {
        return stillbornKids;
    }

    public void setStillbornKids(int stillbornKids) {
        this.stillbornKids = stillbornKids;
    }

    public int getPrematureKids() {
        return prematureKids;
    }

    public void setPrematureKids(int prematureKids) {
        this.prematureKids = prematureKids;
    }

    public int getPosttermKids() {
        return posttermKids;
    }

    public void setPosttermKids(int posttermKids) {
        this.posttermKids = posttermKids;
    }

    public String getSterilityDiagnosis() {
        return sterilityDiagnosis;
    }

    public void setSterilityDiagnosis(String sterilityDiagnosis) {
        this.sterilityDiagnosis = sterilityDiagnosis;
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

    public List<Pregnancy> getPregnancies() {
        return pregnancies;
    }

    public void setPregnancies(List<Pregnancy> pregnancies) {
        this.pregnancies = pregnancies;
    }

    public int getPregnancyConsecutiveId() {
        return pregnancyConsecutiveId;
    }

    public void setPregnancyConsecutiveId(int pregnancyConsecutiveId) {
        this.pregnancyConsecutiveId = pregnancyConsecutiveId;
    }
}
