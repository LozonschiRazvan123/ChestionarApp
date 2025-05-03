package com.example.chestionarapp;

import android.os.Bundle;
import android.webkit.WebView;

import androidx.appcompat.app.AppCompatActivity;

public class LegislationActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_legislation);

        WebView webView = findViewById(R.id.webView);
        webView.loadUrl("file:///android_asset/legislatie.html");
    }
}
