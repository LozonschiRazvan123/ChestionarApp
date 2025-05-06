package com.example.chestionarapp;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

public class QuizActivity extends AppCompatActivity {
    private ImageView questionImage;
    private TextView questionText, progressText, correctCount, wrongCount;
    private CheckBox[] options;
    private Button btnNext, btnBack, btnGoToSections, btnCheckAnswer, btnSkip;

    private List<Question> questions;
    private int currentIndex = 0;
    private int score = 0;
    private int correctAnswers = 0;
    private int wrongAnswers = 0;
    private List<List<Integer>> userAnswers = new ArrayList<>();
    private boolean answered = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quiz);

        // Legături UI
        questionImage = findViewById(R.id.questionImage);
        questionText = findViewById(R.id.questionText);
        progressText = findViewById(R.id.progressText);
        correctCount = findViewById(R.id.correctCount);
        wrongCount = findViewById(R.id.wrongCount);
        options = new CheckBox[]{
                findViewById(R.id.optionA),
                findViewById(R.id.optionB),
                findViewById(R.id.optionC)
        };
        btnCheckAnswer = findViewById(R.id.btnCheckAnswer);
        btnNext = findViewById(R.id.btnNext);
        btnBack = findViewById(R.id.btnBack);
        btnSkip = findViewById(R.id.btnSkip);
        btnGoToSections = findViewById(R.id.sectionQuizBtn);

        // Încărcare întrebări
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
                    options[i].setTextColor(getResources().getColor(android.R.color.holo_red_light));
                }
            }

            if (selected.containsAll(correct) && correct.containsAll(selected)) {
                correctAnswers++;
                score++;
            } else {
                wrongAnswers++;
            }

            correctCount.setText("✔ " + correctAnswers);
            wrongCount.setText("✖ " + wrongAnswers);

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

        btnSkip.setOnClickListener(v -> {
            userAnswers.set(currentIndex, new ArrayList<>());
            if (currentIndex < questions.size() - 1) {
                currentIndex++;
                showQuestion();
            } else {
                goToFirstSkipped();
            }
        });

        btnGoToSections.setOnClickListener(v -> {
            Intent intent = new Intent(this, SectionQuizActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
            finish();
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

        // Afișare imagine
        if (q.getImage() != null && !q.getImage().isEmpty()) {
            try {
                InputStream is = getAssets().open(q.getImage());
                Drawable d = Drawable.createFromStream(is, null);
                questionImage.setImageDrawable(d);
                questionImage.setVisibility(View.VISIBLE);
            } catch (IOException e) {
                questionImage.setVisibility(View.GONE);
            }
        } else {
            questionImage.setVisibility(View.GONE);
        }

        btnNext.setEnabled(false);
        btnCheckAnswer.setEnabled(true);
        btnBack.setVisibility(currentIndex == 0 ? View.GONE : View.VISIBLE);
        btnNext.setVisibility(currentIndex == questions.size() - 1 ? View.GONE : View.VISIBLE);
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