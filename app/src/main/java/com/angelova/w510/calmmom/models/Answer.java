package com.angelova.w510.calmmom.models;

import android.support.annotation.NonNull;

import java.io.Serializable;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class Answer implements Serializable, Comparable<Answer> {

    private String author;
    private String submittedOn;
    private String content;

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getSubmittedOn() {
        return submittedOn;
    }

    public void setSubmittedOn(String submittedOn) {
        this.submittedOn = submittedOn;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    @Override
    public int compareTo(@NonNull Answer answer) {
        SimpleDateFormat sdf = new SimpleDateFormat("dd.MM.yyyy, HH:mm", Locale.getDefault());

        try {
            Date currentItemDate = sdf.parse(this.getSubmittedOn());
            Date otherItemDate = sdf.parse(answer.getSubmittedOn());

            if (currentItemDate.after(otherItemDate)) {
                return 1;
            } else if (currentItemDate.before(otherItemDate)) {
                return -1;
            }
        } catch (ParseException pe) {
            pe.printStackTrace();
        }
        return 0;
    }
}
