package com.example.chestionarapp;

import android.os.Bundle;
import android.os.CountDownTimer;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class ExamActivity extends AppCompatActivity {
    private List<Question> allQuestions, examQuestions;
    private CountDownTimer timer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_exam);

        allQuestions = new ArrayList<>();
        allQuestions.addAll(QuestionLoader.loadQuestions(this, "Semne.json"));
        allQuestions.addAll(QuestionLoader.loadQuestions(this, "prioritate.json"));
        allQuestions.addAll(QuestionLoader.loadQuestions(this, "politist.json"));

        Collections.shuffle(allQuestions);
        examQuestions = allQuestions.subList(0, Math.min(26, allQuestions.size()));

        timer = new CountDownTimer(1800000, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {}
            @Override
            public void onFinish() {}
        }.start();
    }
}
