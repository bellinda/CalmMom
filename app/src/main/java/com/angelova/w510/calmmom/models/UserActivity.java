package com.angelova.w510.calmmom.models;

import java.io.Serializable;

/**
 * Created by W510 on 15.8.2018 г..
 */

public class UserActivity implements Serializable {

    private int duration;
    private ActivityType type;

    public UserActivity() {

    }

    public UserActivity(int duration, ActivityType type) {
        this.duration = duration;
        this.type = type;
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
