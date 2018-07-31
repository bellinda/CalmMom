package com.angelova.w510.calmmom.models;

import java.io.Serializable;

/**
 * Created by W510 on 31.7.2018 г..
 */

public class Question implements Serializable {

    private String text;
    private String answer;

    public Question() {

    }

    public Question(String text, String answer) {
        this.text = text;
        this.answer = answer;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getAnswer() {
        return answer;
    }

    public void setAnswer(String answer) {
        this.answer = answer;
    }
}
