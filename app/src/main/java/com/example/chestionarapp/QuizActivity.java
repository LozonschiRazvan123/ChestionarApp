package com.example.chestionarapp;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.List;

public class QuizActivity extends AppCompatActivity {

    private TextView questionText, progressText;
    private Button[] buttons;
    private Button btnNext, btnBack, btnGoToSections;

    private List<Question> questions;
    private int currentIndex = 0;
    private int score = 0;
    private boolean answered = false;
    private List<Integer> userAnswers = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quiz);

        // Legăm componentele din layout
        progressText = findViewById(R.id.progressText);
        questionText = findViewById(R.id.questionText);
        buttons = new Button[]{
                findViewById(R.id.optionA),
                findViewById(R.id.optionB),
                findViewById(R.id.optionC)
        };
        btnNext = findViewById(R.id.btnNext);
        btnBack = findViewById(R.id.btnBack);
        btnGoToSections = findViewById(R.id.btnGoToSections);

        // Încărcăm întrebările din JSON
        String filename = getIntent().getStringExtra("filename");
        questions = QuestionLoader.loadQuestions(this, filename);

        if (questions == null || questions.isEmpty()) {
            finish();
            return;
        }

        for (int i = 0; i < questions.size(); i++) {
            userAnswers.add(-1);
        }

        showQuestion();

        // Click pe opțiuni de răspuns
        for (int i = 0; i < buttons.length; i++) {
            int finalI = i;
            buttons[i].setOnClickListener(v -> {
                if (!answered) {
                    answered = true;
                    userAnswers.set(currentIndex, finalI);
                    checkAnswer(finalI);
                    btnNext.setEnabled(true);
                }
            });
        }

        // Următoarea întrebare
        btnNext.setOnClickListener(v -> {
            if (currentIndex < questions.size() - 1) {
                currentIndex++;
                showQuestion();
            } else {
                Intent intent = new Intent(this, ResultActivity.class);
                intent.putExtra("score", score);
                intent.putExtra("total", questions.size());
                startActivity(intent);
                finish();
            }
        });

        // Înapoi la întrebarea precedentă
        btnBack.setOnClickListener(v -> {
            if (currentIndex > 0) {
                currentIndex--;
                showQuestion();
            }
        });

        // Înapoi la secțiunile de chestionare
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

        for (int i = 0; i < buttons.length; i++) {
            buttons[i].setText(q.getOptions().get(i));
            buttons[i].setBackgroundColor(getResources().getColor(android.R.color.darker_gray));
            buttons[i].setEnabled(true);
        }

        btnBack.setVisibility(currentIndex == 0 ? Button.GONE : Button.VISIBLE);
        btnNext.setEnabled(false);
        answered = false;

        int savedAnswer = userAnswers.get(currentIndex);
        if (savedAnswer != -1) {
            answered = true;
            checkAnswer(savedAnswer);
            if (currentIndex < questions.size())
                btnNext.setEnabled(true);
        }
    }

    private void checkAnswer(int selectedIndex) {
        Question q = questions.get(currentIndex);
        List<Integer> correctList = q.getCorrect();

        for (int i = 0; i < buttons.length; i++) {
            buttons[i].setEnabled(false);
            if (correctList.contains(i)) {
                buttons[i].setBackgroundColor(getResources().getColor(android.R.color.holo_green_light));
            } else if (i == selectedIndex) {
                buttons[i].setBackgroundColor(getResources().getColor(android.R.color.holo_red_light));
            }
        }

        if (correctList.contains(selectedIndex)) {
            score++;
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();

        // Blocăm butonul hardware pentru a rămâne în quiz
    }
}
