package com.angelova.w510.calmmom.models;

public enum OutcomeType {

    Abortion(0),
    Miscarriage(1),
    LiveBirth(2),
    StillBirth(3);

    private final int type;

    OutcomeType(int type)
    {
        this.type = type;
    }

    private int getType() {
        return type;
    }

    public static OutcomeType fromValue(int value) {
        for (OutcomeType type : OutcomeType.values()) {
            if (type.getType() == value) {
                return type;
            }
        }
        return null;
    }
}
