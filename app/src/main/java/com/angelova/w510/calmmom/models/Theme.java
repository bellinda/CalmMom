package com.angelova.w510.calmmom.models;

import android.support.annotation.NonNull;

import com.fasterxml.jackson.annotation.JsonIgnore;

import java.io.Serializable;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class Theme implements Serializable, Comparable<Theme> {

    private String author;
    private String title;
    private String titleEn;
    private String content;
    private String contentEn;
    private List<Answer> answers;
    private String submittedOn;
    private String lastAnsweredOn;
    @JsonIgnore
    private String dbId;

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
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

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getContentEn() {
        return contentEn;
    }

    public void setContentEn(String contentEn) {
        this.contentEn = contentEn;
    }

    public List<Answer> getAnswers() {
        return answers;
    }

    public void setAnswers(List<Answer> answers) {
        this.answers = answers;
    }

    public String getSubmittedOn() {
        return submittedOn;
    }

    public void setSubmittedOn(String submittedOn) {
        this.submittedOn = submittedOn;
    }

    public String getLastAnsweredOn() {
        return lastAnsweredOn;
    }

    public void setLastAnsweredOn(String lastAnsweredOn) {
        this.lastAnsweredOn = lastAnsweredOn;
    }

    public String getDbId() {
        return dbId;
    }

    public void setDbId(String dbId) {
        this.dbId = dbId;
    }

    @Override
    public int compareTo(@NonNull Theme theme) {
        SimpleDateFormat sdf = new SimpleDateFormat("dd MMM yyyy, HH:mm", Locale.getDefault());

        try {
            Date currentItemDate = sdf.parse(this.getLastAnsweredOn());
            Date otherItemDate = sdf.parse(theme.getLastAnsweredOn());

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
