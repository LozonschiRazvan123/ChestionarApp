package com.example.chestionarapp;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.core.content.FileProvider;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

public class SectionQuizAdapter extends BaseAdapter {
    private final Context context;
    private final String[] sections;
    private final String[] jsonFiles;
    private final String[] legislationFiles;

    public SectionQuizAdapter(Context context, String[] sections, String[] jsonFiles, String[] legislationFiles) {
        this.context = context;
        this.sections = sections;
        this.jsonFiles = jsonFiles;
        this.legislationFiles = legislationFiles;
    }

    @Override
    public int getCount() {
        return sections.length;
    }

    @Override
    public Object getItem(int position) {
        return sections[position];
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View itemView = LayoutInflater.from(context).inflate(R.layout.section_item, parent, false);

        TextView sectionName = itemView.findViewById(R.id.sectionName);
        Button btnQuiz = itemView.findViewById(R.id.btnQuiz);
        Button btnLegislation = itemView.findViewById(R.id.btnLegislation);

        sectionName.setText(sections[position]);

        btnQuiz.setOnClickListener(v -> {
            Intent intent = new Intent(context, QuizActivity.class);
            intent.putExtra("filename", jsonFiles[position]);
            context.startActivity(intent);
        });

        btnLegislation.setOnClickListener(v -> openPdfFromAssets(legislationFiles[position]));

        return itemView;
    }

    private void openPdfFromAssets(String assetFileName) {
        try {
            File outFile = new File(context.getCacheDir(), assetFileName);

            if (!outFile.exists()) {
                InputStream is = context.getAssets().open("imagini/" + assetFileName);
                FileOutputStream fos = new FileOutputStream(outFile);

                byte[] buffer = new byte[1024];
                int length;
                while ((length = is.read(buffer)) != -1) {
                    fos.write(buffer, 0, length);
                }

                fos.close();
                is.close();
            }

            Uri uri = FileProvider.getUriForFile(
                    context,
                    context.getPackageName() + ".provider",
                    outFile
            );

            Intent intent = new Intent(Intent.ACTION_VIEW);
            intent.setDataAndType(uri, "application/pdf");
            intent.setFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);

            context.startActivity(intent);

        } catch (IOException e) {
            e.printStackTrace();
            Toast.makeText(context, "Eroare la deschiderea fișierului.", Toast.LENGTH_SHORT).show();
        } catch (Exception e) {
            Toast.makeText(context, "Nicio aplicație nu poate deschide fișierul PDF.", Toast.LENGTH_SHORT).show();
        }
    }
}
