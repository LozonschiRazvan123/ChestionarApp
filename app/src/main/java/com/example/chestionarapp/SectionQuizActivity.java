package com.example.chestionarapp;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import androidx.appcompat.app.AppCompatActivity;

public class SectionQuizActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_section_quiz);

        ListView sectionList = findViewById(R.id.sectionList);

        // Afișăm denumirile secțiunilor în interfață
        String[] sections = {"Semne Rutiere", "Prioritate", "Semnale Politist"};

        // Adapter pentru a popula listView-ul cu layout personalizat
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, R.layout.section_item, R.id.sectionName, sections);
        sectionList.setAdapter(adapter);

        sectionList.setOnItemClickListener((parent, view, position, id) -> {
            String sectionFile;
            switch (sections[position]) {
                case "Semne Rutiere":
                    sectionFile = "Semne.json";
                    break;
                case "Prioritate":
                    sectionFile = "prioritate.json";
                    break;
                case "Semnale Politist":
                    sectionFile = "politist.json";
                    break;
                default:
                    sectionFile = "semne.json";
            }

            // Deschide QuizActivity cu fișierul potrivit
            Intent intent = new Intent(this, QuizActivity.class);
            intent.putExtra("filename", sectionFile);
            startActivity(intent);
        });
    }
}
