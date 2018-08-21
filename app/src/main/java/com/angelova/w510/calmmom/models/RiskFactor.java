package com.angelova.w510.calmmom.models;

/**
 * Created by W510 on 20.8.2018 г..
 */

public enum RiskFactor {

    Smoking("Smoking"),
    Alcohol("Drinking Alcohol"),
    Overweight("Overweight"),
    Age("Age"),
    UnderFeeding("Underfeeding"),
    FoodAllergy("Food Allergy"),
    MedicinesAllergy("Medicines Allergy");

    private final String type;

    RiskFactor(String type)
    {
        this.type = type;
    }

    private String getType() {
        return type;
    }

    public static RiskFactor fromValue(String value) {
        for (RiskFactor riskFactor : RiskFactor.values()) {
            if (riskFactor.getType().equals(value)) {
                return riskFactor;
            }
        }
        return null;
    }
}
