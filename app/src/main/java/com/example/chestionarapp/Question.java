package com.example.chestionarapp;

import java.util.List;

public class Question {
    private String text;
    private List<String> options;
    private int correct;

    public String getText() { return text; }
    public List<String> getOptions() { return options; }
    public int getCorrect() { return correct; }
}
