// ExamActivity.java
package com.example.chestionarapp;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class ExamActivity extends AppCompatActivity {
    private ImageView examQuestionImage;
    private TextView examQuestionText, examTimerText, correctCount, wrongCount, progressText;
    private CheckBox[] examOptions;
    private Button btnCheckExamAnswer, btnExamNext, btnSkip, btnCheatPass;

    private List<Question> examQuestions;
    private int currentExamIndex = 0;
    private int correctExamAnswers = 0;
    private int wrongExamAnswers = 0;
    private List<List<Integer>> examUserAnswers = new ArrayList<>();
    private boolean examAnswered = false;

    private CountDownTimer examTimer;
    private static final long EXAM_TIME_MS = 30 * 60 * 1000; // 30 minute

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_exam);

        examQuestionImage = findViewById(R.id.examQuestionImage);
        examQuestionText = findViewById(R.id.examQuestionText);
        examTimerText = findViewById(R.id.examTimerText);

        correctCount = findViewById(R.id.correctCount);
        wrongCount = findViewById(R.id.wrongCount);
        progressText = findViewById(R.id.progressText);

        examOptions = new CheckBox[]{
                findViewById(R.id.examOptionA),
                findViewById(R.id.examOptionB),
                findViewById(R.id.examOptionC)
        };

        btnCheckExamAnswer = findViewById(R.id.btnCheckExamAnswer);
        btnExamNext = findViewById(R.id.btnExamNext);
        btnSkip = findViewById(R.id.btnSkip);
        btnCheatPass = findViewById(R.id.btnCheatPass);

        examQuestions = new ArrayList<>();
        examQuestions.addAll(QuestionLoader.loadQuestions(this, "Semne.json"));
        examQuestions.addAll(QuestionLoader.loadQuestions(this, "prioritate.json"));
        examQuestions.addAll(QuestionLoader.loadQuestions(this, "politist.json"));

        Collections.shuffle(examQuestions);
        examQuestions = examQuestions.subList(0, Math.min(26, examQuestions.size()));

        for (int i = 0; i < examQuestions.size(); i++) {
            examUserAnswers.add(new ArrayList<>());
        }

        startExamTimer();
        showExamQuestion();

        btnCheckExamAnswer.setOnClickListener(v -> {
            if (examAnswered) return;

            List<Integer> selected = new ArrayList<>();
            for (int i = 0; i < examOptions.length; i++) {
                if (examOptions[i].isChecked()) {
                    selected.add(i);
                }
            }

            if (selected.isEmpty()) {
                Toast.makeText(this, "Selectează cel puțin un răspuns!", Toast.LENGTH_SHORT).show();
                return;
            }

            examUserAnswers.set(currentExamIndex, selected);
            List<Integer> correct = examQuestions.get(currentExamIndex).getCorrect();

            for (int i = 0; i < examOptions.length; i++) {
                examOptions[i].setEnabled(false);
                if (correct.contains(i)) {
                    examOptions[i].setTextColor(getResources().getColor(android.R.color.holo_green_dark));
                } else if (selected.contains(i)) {
                    examOptions[i].setTextColor(getResources().getColor(android.R.color.holo_red_dark));
                }
            }

            if (selected.containsAll(correct) && correct.containsAll(selected)) {
                correctExamAnswers++;
            } else {
                wrongExamAnswers++;
            }

            updateHeader();

            btnCheckExamAnswer.setEnabled(false);
            btnExamNext.setEnabled(true);
            examAnswered = true;

            if (wrongExamAnswers >= 5) {
                Toast.makeText(this, "Ai greșit de 5 ori. Testul se oprește!", Toast.LENGTH_LONG).show();
                finishExam();
            }
        });

        btnExamNext.setOnClickListener(v -> {
            if (currentExamIndex < examQuestions.size() - 1) {
                currentExamIndex++;
                showExamQuestion();
            } else {
                finishExam();
            }
        });

        btnSkip.setOnClickListener(v -> {
            if (currentExamIndex < examQuestions.size() - 1) {
                currentExamIndex++;
                showExamQuestion();
            } else {
                finishExam();
            }
        });

        btnCheatPass.setOnClickListener(v -> {
            correctExamAnswers = 22;
            Toast.makeText(this, "Scor setat la 22 (Cheat)!", Toast.LENGTH_SHORT).show();
            finishExam();
        });
    }

    private void startExamTimer() {
        examTimer = new CountDownTimer(EXAM_TIME_MS, 1000) {
            public void onTick(long millisUntilFinished) {
                long minutes = millisUntilFinished / 60000;
                long seconds = (millisUntilFinished % 60000) / 1000;
                examTimerText.setText(String.format("Timp rămas: %02d:%02d", minutes, seconds));
            }

            public void onFinish() {
                Toast.makeText(ExamActivity.this, "Timpul a expirat!", Toast.LENGTH_SHORT).show();
                finishExam();
            }
        }.start();
    }

    private void showExamQuestion() {
        Question q = examQuestions.get(currentExamIndex);
        examQuestionText.setText(q.getText());
        progressText.setText("Întrebarea " + (currentExamIndex + 1) + " din " + examQuestions.size());

        for (int i = 0; i < examOptions.length; i++) {
            examOptions[i].setText(q.getOptions().get(i));
            examOptions[i].setEnabled(true);
            examOptions[i].setChecked(false);
            examOptions[i].setTextColor(getResources().getColor(android.R.color.black));
        }

        if (q.getImage() != null && !q.getImage().isEmpty()) {
            try {
                InputStream is = getAssets().open(q.getImage());
                Drawable drawable = Drawable.createFromStream(is, null);
                examQuestionImage.setImageDrawable(drawable);
                examQuestionImage.setVisibility(View.VISIBLE);
            } catch (IOException e) {
                examQuestionImage.setVisibility(View.GONE);
            }
        } else {
            examQuestionImage.setVisibility(View.GONE);
        }

        btnCheckExamAnswer.setEnabled(true);
        btnExamNext.setEnabled(false);
        examAnswered = false;
        updateHeader();
    }

    private void updateHeader() {
        correctCount.setText("✔ " + correctExamAnswers);
        wrongCount.setText("✖ " + wrongExamAnswers);
    }

    private void finishExam() {
        if (examTimer != null) examTimer.cancel();
        Intent intent = new Intent(this, ResultActivity.class);
        intent.putExtra("score", correctExamAnswers);
        intent.putExtra("total", examQuestions.size());
        startActivity(intent);
        finish();
    }
}
