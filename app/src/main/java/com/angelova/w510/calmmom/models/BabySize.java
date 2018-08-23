package com.angelova.w510.calmmom.models;

/**
 * Created by W510 on 23.8.2018 г..
 */

public class BabySize {
    private int week;
    private float weight;
    private float length;

    public BabySize() {}

    public BabySize(int week, float weight, float length) {
        this.week = week;
        this.weight = weight;
        this.length = length;
    }

    public int getWeek() {
        return week;
    }

    public void setWeek(int week) {
        this.week = week;
    }

    public float getWeight() {
        return weight;
    }

    public void setWeight(float weight) {
        this.weight = weight;
    }

    public float getLength() {
        return length;
    }

    public void setLength(float length) {
        this.length = length;
    }
}
