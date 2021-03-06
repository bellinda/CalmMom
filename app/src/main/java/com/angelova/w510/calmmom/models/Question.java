package com.angelova.w510.calmmom.models;

import java.io.Serializable;

/**
 * Created by W510 on 31.7.2018 г..
 */

public class Question implements Serializable {

    private String text;
    private String answer;
    private boolean isNew;
    private boolean isEditingQuestion;
    private boolean isEditingAnswer;

    public Question() {

    }

    public Question(String text, String answer, boolean isNew) {
        this.text = text;
        this.answer = answer;
        this.isNew = isNew;
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

    public boolean isNew() {
        return isNew;
    }

    public void setNew(boolean isNew) {
        this.isNew = isNew;
    }

    public boolean isEditingQuestion() {
        return isEditingQuestion;
    }

    public void setEditingQuestion(boolean isEditingQuestion) {
        this.isEditingQuestion = isEditingQuestion;
    }

    public boolean isEditingAnswer() {
        return isEditingAnswer;
    }

    public void setEditingAnswer(boolean isEditingAnswer) {
        this.isEditingAnswer = isEditingAnswer;
    }
}
