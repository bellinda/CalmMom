package com.angelova.w510.calmmom.models;

/**
 * Created by W510 on 15.8.2018 г..
 */

public class Activity {

    private int duration;
    private ActivityType type;

    public Activity() {

    }

    public int getDuration() {
        return duration;
    }

    public void setDuration(int duration) {
        this.duration = duration;
    }

    public ActivityType getType() {
        return type;
    }

    public void setType(ActivityType type) {
        this.type = type;
    }
}
