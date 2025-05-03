package com.example.chestionarapp;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import com.example.chestionarapp.SectionQuizActivity;


import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class MainActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button sectionQuizBtn = findViewById(R.id.sectionQuizBtn);
        Button examBtn = findViewById(R.id.examBtn);
        Button legislationBtn = findViewById(R.id.legislationBtn);

        sectionQuizBtn.setOnClickListener(v -> {
            Intent intent = new Intent(this, SectionQuizActivity.class);
            startActivity(intent);
        });

        examBtn.setOnClickListener(v -> {
            Intent intent = new Intent(this, ExamActivity.class);
            startActivity(intent);
        });

        legislationBtn.setOnClickListener(v -> {
            Intent intent = new Intent(this, LegislationActivity.class);
            startActivity(intent);
        });
    }
}
