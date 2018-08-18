package com.angelova.w510.calmmom.models;

import android.support.annotation.NonNull;

import java.io.Serializable;

/**
 * Created by W510 on 7.8.2018 г..
 */

public class Weight implements Comparable<Weight>, Serializable {
    private double value;
    private int week;

    public Weight() {

    }

    public Weight(double value, int week) {
        this.value = value;
        this.week = week;
    }

    public double getValue() {
        return value;
    }

    public void setValue(double value) {
        this.value = value;
    }

    public int getWeek() {
        return week;
    }

    public void setWeek(int week) {
        this.week = week;
    }

    @Override
    public int compareTo(@NonNull Weight o) {
        if (this.week == o.week) {
            return 0;
        } else if (this.week < o.week) {
            return -1;
        } else {
            return 1;
        }
    }
}
