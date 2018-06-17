package com.angelova.w510.calmmom.models;

import java.io.Serializable;

/**
 * Created by W510 on 7.5.2018 г..
 */

public class Surgery implements Serializable {

    private String kind;
    private String referenceDate;
    private boolean successful;
    private String notes;

    public Surgery(String kind, String referenceDate, boolean successfull, String notes) {
        this.kind = kind;
        this.referenceDate = referenceDate;
        this.successful = successfull;
        this.notes = notes;
    }

    public Surgery() {
    }

    public String getKind() {
        return kind;
    }

    public void setKind(String kind) {
        this.kind = kind;
    }

    public String getReferenceDate() {
        return referenceDate;
    }

    public void setReferenceDate(String referenceDate) {
        this.referenceDate = referenceDate;
    }

    public boolean isSuccessful() {
        return successful;
    }

    public void setSuccessful(boolean successful) {
        this.successful = successful;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }
}
