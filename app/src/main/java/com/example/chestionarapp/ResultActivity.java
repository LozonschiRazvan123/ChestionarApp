package com.example.chestionarapp;

import android.os.Bundle;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class ResultActivity extends AppCompatActivity {

    private TextView resultMessageText, resultScoreText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_result);

        resultMessageText = findViewById(R.id.resultMessageText);
        resultScoreText = findViewById(R.id.resultScoreText);

        int score = getIntent().getIntExtra("score", 0);
        int total = getIntent().getIntExtra("total", 26);

        resultScoreText.setText("Ai răspuns corect la " + score + " din " + total + " întrebări.");

        if (score >= 22) {
            resultMessageText.setText("🎉 Felicitări, ai fost admis!");
        } else {
            resultMessageText.setText("❌ Din păcate, nu ai fost admis.");
        }
    }
}
