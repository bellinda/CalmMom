package com.angelova.w510.calmmom.models;

/**
 * Created by W510 on 15.8.2018 г..
 */

public enum ActivityType {
    Yoga("Yoga"),
    Jogging("Jogging"),
    Pilates("Pilates"),
    Swimming("Swimming"),
    StationaryB("Stationary Cycling"),
    Aerobics("Aerobics"),
    Walking("Walking"),
    Dancing("Dancing"),
    Other("Other");

    private final String type;

    ActivityType(String type)
    {
        this.type = type;
    }

    private String getType() {
        return type;
    }

    public static ActivityType fromValue(String value) {
        for (ActivityType activityType : ActivityType.values()) {
            if (activityType.getType().equals(value)) {
                return activityType;
            }
        }
        return null;
    }
}
