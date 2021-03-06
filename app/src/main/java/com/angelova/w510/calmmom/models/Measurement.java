package com.angelova.w510.calmmom.models;

import java.io.Serializable;

/**
 * Created by W510 on 28.8.2018 г..
 */

public class Measurement implements Serializable {

    private String title;
    private String titleEn;
    private boolean done;

    public Measurement() {}

    public Measurement(String title, String titleEn) {
        this.title = title;
        this.titleEn = titleEn;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getTitleEn() {
        return titleEn;
    }

    public void setTitleEn(String titleEn) {
        this.titleEn = titleEn;
    }

    public boolean isDone() {
        return done;
    }

    public void setDone(boolean done) {
        this.done = done;
    }
}
