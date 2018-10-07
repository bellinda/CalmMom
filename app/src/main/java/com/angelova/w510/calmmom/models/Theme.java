package com.angelova.w510.calmmom.models;

import java.io.Serializable;
import java.util.List;

public class Theme implements Serializable {

    private String author;
    private String title;
    private String titleEn;
    private String content;
    private String contentEn;
    private List<Answer> answers;
    private String submittedOn;
    private String lastAnsweredOn;

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
}
