package com.angelova.w510.calmmom.models;

import java.io.Serializable;
import java.util.List;

/**
 * Created by W510 on 6.5.2018 г..
 */

public class Illness implements Serializable {

    private String name;
    private String referenceDate;
    private String medicines;
    private String notes;

    public Illness(String name, String referenceDate, String medicines, String notes) {
        this.name = name;
        this.referenceDate = referenceDate;
        this.medicines = medicines;
        this.notes = notes;
    }

    public Illness() {
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getReferenceDate() {
        return referenceDate;
    }

    public void setReferenceDate(String referenceDate) {
        this.referenceDate = referenceDate;
    }

    public String getMedicines() {
        return medicines;
    }

    public void setMedicines(String medicines) {
        this.medicines = medicines;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }
}
