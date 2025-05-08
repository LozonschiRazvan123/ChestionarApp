package com.example.chestionarapp;

import android.os.Bundle;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import androidx.appcompat.app.AppCompatActivity;

public class LegislationActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_legislation);

        WebView webView = findViewById(R.id.legislationWebView);
        webView.getSettings().setJavaScriptEnabled(true);
        webView.setWebViewClient(new WebViewClient());

        String section = getIntent().getStringExtra("section");

        String url;
        switch (section) {
            case "Semne Rutiere":
                url = "https://example.com/semnlegislatie_semne.docx";
                break;
            case "Prioritate":
                url = "https://example.com/prioritate.docx";
                break;
            case "Semnale Politist":
                url = "https://example.com/politist.docx";
                break;
            default:
                url = "https://example.com/default.docx";
        }

        String googleDocsUrl = "https://docs.google.com/gview?embedded=true&url=" + url;
        webView.loadUrl(googleDocsUrl);
    }
}
