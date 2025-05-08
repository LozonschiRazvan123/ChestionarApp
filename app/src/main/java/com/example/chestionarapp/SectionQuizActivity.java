package com.example.chestionarapp;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ListView;

import androidx.appcompat.app.AppCompatActivity;

public class SectionQuizActivity extends AppCompatActivity {

    String[] sections = {"Semne Rutiere", "Prioritate", "Semnale Politist"};
    String[] jsonFiles = {"Semne.json", "prioritate.json", "politist.json"};
    String[] legislationFiles = {"legislatie_semne.pdf", "prioritate.docx", "politist.docx"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_section_quiz);

        ListView sectionList = findViewById(R.id.sectionList);
        SectionQuizAdapter adapter = new SectionQuizAdapter(this, sections, jsonFiles, legislationFiles);
        sectionList.setAdapter(adapter);
    }
}
