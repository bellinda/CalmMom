package com.angelova.w510.calmmom.models;

import android.support.annotation.NonNull;

import java.io.Serializable;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

/**
 * Created by W510 on 26.9.2018 г..
 */

public class Meal implements Serializable, Comparable<Meal> {
    private String title;
    private int quantity;
    private String date;
    private String time;
    private int category;
    private boolean dangerous;
    private List<Integer> dangerIndexes;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public int getCategory() {
        return category;
    }

    public void setCategory(int category) {
        this.category = category;
    }

    public boolean isDangerous() {
        return dangerous;
    }

    public void setDangerous(boolean dangerous) {
        this.dangerous = dangerous;
    }

    public List<Integer> getDangerIndexes() {
        return dangerIndexes;
    }

    public void setDangerIndexes(List<Integer> dangerIndexes) {
        this.dangerIndexes = dangerIndexes;
    }

    @Override
    public int compareTo(@NonNull Meal meal) {
        SimpleDateFormat sdf = new SimpleDateFormat("dd MMM yyyy kk:mm", Locale.getDefault());

        try {
            Date currentItemDate = sdf.parse(String.format("%s %s", this.getDate(), this.getTime()));
            Date otherItemDate = sdf.parse(String.format("%s %s", meal.getDate(), meal.getTime()));

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
