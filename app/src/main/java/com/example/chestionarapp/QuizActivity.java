package com.example.chestionarapp;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import java.util.List;

public class QuizActivity extends AppCompatActivity {
    private List<Question> questions;
    private int currentIndex = 0;
    private int score = 0;
    private TextView questionText, scoreText;
    private Button[] optionButtons = new Button[3];
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quiz);

        questionText = findViewById(R.id.questionText);
        optionButtons[0] = findViewById(R.id.optionA);
        optionButtons[1] = findViewById(R.id.optionB);
        optionButtons[2] = findViewById(R.id.optionC);
        scoreText = findViewById(R.id.scoreText);

        String section = getIntent().getStringExtra("section");
        questions = QuestionLoader.loadQuestions(this, section.toLowerCase() + ".json");

        showQuestion();
    }

    private void showQuestion() {
        if (currentIndex >= questions.size()) {
            Intent resultIntent = new Intent(this, ResultActivity.class);
            resultIntent.putExtra("score", score);
            resultIntent.putExtra("total", questions.size());
            startActivity(resultIntent);
            finish();
            return;
        }

        Question q = questions.get(currentIndex);
        questionText.setText(q.getText());
        for (int i = 0; i < 3; i++) {
            int index = i;
            optionButtons[i].setText(q.getOptions().get(i));
            optionButtons[i].setOnClickListener(v -> {
                if (index == q.getCorrect()) score++;
                currentIndex++;
                showQuestion();
            });
        }

        scoreText.setText("Scor: " + score);
    }
}
