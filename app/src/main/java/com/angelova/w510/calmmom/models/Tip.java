package com.angelova.w510.calmmom.models;

import android.support.annotation.NonNull;

/**
 * Created by W510 on 20.8.2018 г..
 */

public class Tip implements Comparable<Tip> {

    private String week;
    private String content;

    public Tip() {}

    public String getWeek() {
        return week;
    }

    public void setWeek(String week) {
        this.week = week;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    @Override
    public int compareTo(@NonNull Tip o) {
        if (Integer.parseInt(this.week) > Integer.parseInt(o.getWeek())) {
            return -1;
        } else if (Integer.parseInt(this.week) < Integer.parseInt(o.getWeek())) {
            return 1;
        }
        return 0;
    }
}
