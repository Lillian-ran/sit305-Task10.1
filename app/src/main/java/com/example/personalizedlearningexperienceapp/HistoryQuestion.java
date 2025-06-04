package com.example.personalizedlearningexperienceapp;

public class HistoryQuestion {
    public String question;
    public String[] options;
    public int correctAnswerIndex;
    public int userAnswerIndex;

    public HistoryQuestion(String question, String[] options, int correct, int user) {
        this.question = question;
        this.options = options;
        this.correctAnswerIndex = correct;
        this.userAnswerIndex = user;
    }
}
