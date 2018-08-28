package com.angelova.w510.calmmom.models;

import java.io.Serializable;

/**
 * Created by W510 on 28.8.2018 г..
 */

public class Test implements Serializable {
    private String title;
    private boolean done;

    public Test() {
    }

    public Test (String title, boolean done) {
        this.title = title;
        this.done = done;
    }

    public Test(String title) {
        this.title = title;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public boolean isDone() {
        return done;
    }

    public void setDone(boolean done) {
        this.done = done;
    }
}
