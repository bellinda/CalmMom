package com.angelova.w510.calmmom.models;

/**
 * Created by W510 on 7.5.2018 г..
 */

public class FamilyHistory {

    private String type;
    private String notes;

    public FamilyHistory(String type, String notes) {
        this.type = type;
        this.notes = notes;
    }

    public FamilyHistory() {
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }
}
