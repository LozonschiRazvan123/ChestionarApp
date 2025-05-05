package com.example.chestionarapp;

import java.util.List;

public class Question {
    private String text;
    private String image;

    public String getImage() {
        return image;
    }

    private List<String> options;
    private List<Integer> correct;

    public String getText() {
        return text;
    }

    public List<String> getOptions() {
        return options;
    }

    public List<Integer> getCorrect() {
        return correct;
    }
}
