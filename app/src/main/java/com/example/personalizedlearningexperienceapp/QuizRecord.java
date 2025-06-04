package com.example.personalizedlearningexperienceapp;

public class QuizRecord {
    private String question;
    private String[] options;
    private String useranswer;
    private String correctanswer;
    private String username;


    // Getters
    public String getUsername() { return username; }
    public String getQuestion() { return question; }
    public String[] getOptions() { return options; }
    public String getUseranswer() { return useranswer; }
    public String getCorrectanswer() { return correctanswer; }

    // Setters
    public void setUsername(String username) {
        this.username = username;
    }

}
