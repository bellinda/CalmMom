package com.angelova.w510.calmmom.models;

import java.io.Serializable;

/**
 * Created by W510 on 26.8.2018 г..
 */

public class Kick implements Serializable {
    private String date;
    private String duration;
    private int count;

    public Kick (String date, String duration, int count) {
        this.date = date;
        this.duration = duration;
        this.count = count;
    }

    public Kick () {}

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getDuration() {
        return duration;
    }

    public void setDuration(String duration) {
        this.duration = duration;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }
}
