package com.angelova.w510.calmmom.models;

import java.io.Serializable;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by W510 on 29.8.2018 г..
 */

public class Pregnancy implements Serializable {

    private String firstDayOfLastMenstruation;
    private String estimatedDeliveryDate;
    private List<Examination> examinations;
    private List<Question> questions;
    private List<Weight> weights;
    private HashMap<String, List<UserActivity>> activities;
    private List<Kick> kicks;
    private List<Contraction> contractions;
    private boolean firstPregnancy;
    private boolean bloodGroupIncompatibility;
    private boolean unwantedPregnancy;
    private List<Medicine> takenMedicines;
    private List<Medicine> allowedMedicinesByDoctor;
    private Map<String, List<Meal>> meals;
    private Map<String, Integer> waterIntakes;

    public String getFirstDayOfLastMenstruation() {
        return firstDayOfLastMenstruation;
    }

    public void setFirstDayOfLastMenstruation(String firstDayOfLastMenstruation) {
        this.firstDayOfLastMenstruation = firstDayOfLastMenstruation;
    }

    public String getEstimatedDeliveryDate() {
        return estimatedDeliveryDate;
    }

    public void setEstimatedDeliveryDate(String estimatedDeliveryDate) {
        this.estimatedDeliveryDate = estimatedDeliveryDate;
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

    public List<Kick> getKicks() {
        return kicks;
    }

    public void setKicks(List<Kick> kicks) {
        this.kicks = kicks;
    }

    public List<Contraction> getContractions() {
        return contractions;
    }

    public void setContractions(List<Contraction> contractions) {
        this.contractions = contractions;
    }

    public boolean isFirstPregnancy() {
        return firstPregnancy;
    }

    public void setFirstPregnancy(boolean firstPregnancy) {
        this.firstPregnancy = firstPregnancy;
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

    public List<Medicine> getTakenMedicines() {
        return takenMedicines;
    }

    public void setTakenMedicines(List<Medicine> takenMedicines) {
        this.takenMedicines = takenMedicines;
    }

    public List<Medicine> getAllowedMedicinesByDoctor() {
        return allowedMedicinesByDoctor;
    }

    public void setAllowedMedicinesByDoctor(List<Medicine> allowedMedicinesByDoctor) {
        this.allowedMedicinesByDoctor = allowedMedicinesByDoctor;
    }

    public Map<String, List<Meal>> getMeals() {
        return meals;
    }

    public void setMeals(Map<String, List<Meal>> meals) {
        this.meals = meals;
    }

    public Map<String, Integer> getWaterIntakes() {
        return waterIntakes;
    }

    public void setWaterIntakes(Map<String, Integer> waterIntakes) {
        this.waterIntakes = waterIntakes;
    }
}
