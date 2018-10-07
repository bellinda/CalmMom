package com.angelova.w510.calmmom.models;

import java.io.Serializable;

public class Answer implements Serializable {

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
}
