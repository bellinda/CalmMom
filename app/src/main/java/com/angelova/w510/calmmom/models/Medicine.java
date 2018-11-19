package com.angelova.w510.calmmom.models;

import android.support.annotation.NonNull;

import java.io.Serializable;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

/**
 * Created by W510 on 31.8.2018 г..
 */

public class Medicine implements Serializable, Comparable<Medicine> {

    private String title;
    private String takenOn;
    private String time;
    private String comments;

    public Medicine() {

    }

    public Medicine(String title, String takenOn, String time, String comments) {
        this.title = title;
        this.takenOn = takenOn;
        this.time = time;
        this.comments = comments;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getTakenOn() {
        return takenOn;
    }

    public void setTakenOn(String takenOn) {
        this.takenOn = takenOn;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getComments() {
        return comments;
    }

    public void setComments(String comments) {
        this.comments = comments;
    }

    @Override
    public int compareTo(@NonNull Medicine m) {
        SimpleDateFormat sdf = new SimpleDateFormat("kk:mm", Locale.getDefault());
        try {
            Date currentItemDate = sdf.parse(this.getTime());
            Date otherItemDate = sdf.parse(m.getTime());

            if (currentItemDate.after(otherItemDate)) {
                return -1;
            } else if (currentItemDate.before(otherItemDate)) {
                return 1;
            }
        } catch (ParseException pe) {
            pe.printStackTrace();
        }
        return 0;
    }
}
