package com.example.chestionarapp;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.List;

public class QuizActivity extends AppCompatActivity {

    private TextView questionText, progressText;
    private CheckBox[] options;
    private Button btnNext, btnBack, btnGoToSections, btnCheckAnswer, btnSkip;

    private List<Question> questions;
    private int currentIndex = 0;
    private int score = 0;
    private List<List<Integer>> userAnswers = new ArrayList<>();
    private boolean answered = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quiz);

        progressText = findViewById(R.id.progressText);
        questionText = findViewById(R.id.questionText);
        options = new CheckBox[]{
                findViewById(R.id.optionA),
                findViewById(R.id.optionB),
                findViewById(R.id.optionC)
        };
        btnCheckAnswer = findViewById(R.id.btnCheckAnswer);
        btnNext = findViewById(R.id.btnNext);
        btnBack = findViewById(R.id.btnBack);
        btnGoToSections = findViewById(R.id.btnGoToSections);
        btnSkip = findViewById(R.id.btnSkip);

        String filename = getIntent().getStringExtra("filename");
        questions = QuestionLoader.loadQuestions(this, filename);

        if (questions == null || questions.isEmpty()) {
            finish();
            return;
        }

        for (int i = 0; i < questions.size(); i++) {
            userAnswers.add(new ArrayList<>());
        }

        showQuestion();

        btnCheckAnswer.setOnClickListener(v -> {
            if (answered) return;

            List<Integer> selected = new ArrayList<>();
            for (int i = 0; i < options.length; i++) {
                if (options[i].isChecked()) {
                    selected.add(i);
                }
            }

            userAnswers.set(currentIndex, selected);
            List<Integer> correct = questions.get(currentIndex).getCorrect();

            for (int i = 0; i < options.length; i++) {
                options[i].setEnabled(false);
                if (correct.contains(i)) {
                    options[i].setTextColor(getResources().getColor(android.R.color.holo_green_dark));
                } else if (selected.contains(i)) {
                    options[i].setTextColor(getResources().getColor(android.R.color.holo_red_dark));
                }
            }

            if (selected.containsAll(correct) && correct.containsAll(selected)) {
                score++;
            }

            btnCheckAnswer.setEnabled(false);
            btnNext.setEnabled(true);
            answered = true;
        });

        btnNext.setOnClickListener(v -> {
            if (currentIndex < questions.size() - 1) {
                currentIndex++;
                showQuestion();
            } else {
                goToFirstSkipped();
            }
        });

        btnBack.setOnClickListener(v -> {
            if (currentIndex > 0) {
                currentIndex--;
                showQuestion();
            }
        });

        btnGoToSections.setOnClickListener(v -> {
            Intent intent = new Intent(this, SectionQuizActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
            finish();
        });

        btnSkip.setOnClickListener(v -> {
            userAnswers.set(currentIndex, new ArrayList<>()); // păstrează ca "sărită"
            if (currentIndex < questions.size() - 1) {
                currentIndex++;
                showQuestion();
            } else {
                goToFirstSkipped();
            }
        });
    }

    private void showQuestion() {
        Question q = questions.get(currentIndex);
        questionText.setText(q.getText());
        progressText.setText("Întrebarea " + (currentIndex + 1) + " din " + questions.size());

        for (int i = 0; i < options.length; i++) {
            options[i].setText(q.getOptions().get(i));
            options[i].setEnabled(true);
            options[i].setChecked(false);
            options[i].setTextColor(getResources().getColor(android.R.color.black));
        }

        btnNext.setEnabled(false);
        btnCheckAnswer.setEnabled(true);
        btnBack.setVisibility(currentIndex == 0 ? View.GONE : View.VISIBLE);
        answered = false;
    }

    private void goToFirstSkipped() {
        for (int i = 0; i < userAnswers.size(); i++) {
            if (userAnswers.get(i).isEmpty()) {
                currentIndex = i;
                showQuestion();
                return;
            }
        }
        showResults();
    }

    private void showResults() {
        Intent intent = new Intent(this, ResultActivity.class);
        intent.putExtra("score", score);
        intent.putExtra("total", questions.size());
        startActivity(intent);
        finish();
    }
}
