package com.angelova.w510.calmmom.models;

/**
 * Created by W510 on 15.8.2018 г..
 */

public enum ActivityType {
    Yoga(0),
    Jogging(1),
    Pilates(2),
    Swimming(2),
    StationaryB(2),
    Walking(2),
    Dancing(2),
    Other(2);

    private final int type;

    ActivityType(int type)
    {
        this.type = type;
    }

    private int getType() {
        return type;
    }

    public static ActivityType fromValue(int value) {
        for (ActivityType activityType : ActivityType.values()) {
            if (activityType.getType() == value) {
                return activityType;
            }
        }
        return null;
    }
}
