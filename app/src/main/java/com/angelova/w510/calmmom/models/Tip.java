package com.angelova.w510.calmmom.models;

import android.support.annotation.NonNull;

import java.io.Serializable;

/**
 * Created by W510 on 20.8.2018 г..
 */

public class Tip implements Serializable, Comparable<Tip> {

    private String week;
    private String content;
    private String bgContent;
    private boolean isCustom;
    private boolean isDone;

    public Tip() {}

    public Tip(String week, String content, String bgContent, boolean isCustom, boolean isDone) {
        this.week = week;
        this.content = content;
        this.bgContent = bgContent;
        this.isCustom = isCustom;
        this.isDone = isDone;
    }

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

    public String getBgContent() {
        return bgContent;
    }

    public void setBgContent(String bgContent) {
        this.bgContent = bgContent;
    }

    public boolean isCustom() {
        return isCustom;
    }

    public void setCustom(boolean isCustom) {
        this.isCustom = isCustom;
    }

    public boolean isDone() {
        return isDone;
    }

    public void setDone(boolean done) {
        isDone = done;
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
