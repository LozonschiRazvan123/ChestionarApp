package com.example.chestionarapp;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.chestionarapp.QuizActivity;
import com.example.chestionarapp.R;
public class SectionQuizActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_section_quiz);

        ListView sectionList = findViewById(R.id.sectionList);
        String[] sections = {"Semne", "Prioritate", "Conducere"};

        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, R.layout.section_item, R.id.sectionName, sections);
        sectionList.setAdapter(adapter);


        sectionList.setOnItemClickListener((parent, view, position, id) -> {
            String sectionFile = sections[position].toLowerCase() + ".json";
            Intent intent = new Intent(this, QuizActivity.class);
            intent.putExtra("filename", sectionFile);
            startActivity(intent);
        });
    }
}
