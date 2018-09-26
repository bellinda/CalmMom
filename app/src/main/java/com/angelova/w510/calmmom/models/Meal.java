package com.angelova.w510.calmmom.models;

import java.util.Date;

/**
 * Created by W510 on 26.9.2018 г..
 */

public class Meal {
    private String title;
    private Date date;
    private String time;
    private String category;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }
}
