package com.example.chestionarapp;

import android.os.Bundle;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.apache.poi.xwpf.usermodel.XWPFParagraph;
import java.io.InputStream;
import java.util.List;

public class LegislationViewActivity extends AppCompatActivity {

    TextView legislationText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_legislation_view);

        legislationText = findViewById(R.id.legislationText);

        String filename = getIntent().getStringExtra("docx");

        if (filename != null) {
            displayDocxFromAssets(filename);
        }
    }

    private void displayDocxFromAssets(String filename) {
        try {
            InputStream is = getAssets().open(filename);
            XWPFDocument document = new XWPFDocument(is);
            List<XWPFParagraph> paragraphs = document.getParagraphs();

            StringBuilder content = new StringBuilder();
            for (XWPFParagraph p : paragraphs) {
                content.append(p.getText()).append("\n\n");
            }

            legislationText.setText(content.toString());
            is.close();
        } catch (Exception e) {
            legislationText.setText("Eroare la citirea documentului: " + e.getMessage());
        }
    }
}
