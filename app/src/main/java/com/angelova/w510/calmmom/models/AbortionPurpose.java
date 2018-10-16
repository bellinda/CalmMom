package com.angelova.w510.calmmom.models;

public enum AbortionPurpose {

    Desired(0),
    MedicalEvidence(1);

    private final int type;

    AbortionPurpose(int type)
    {
        this.type = type;
    }

    private int getType() {
        return type;
    }

    public static AbortionPurpose fromValue(int value) {
        for (AbortionPurpose purpose : AbortionPurpose.values()) {
            if (purpose.getType() == value) {
                return purpose;
            }
        }
        return null;
    }
}
