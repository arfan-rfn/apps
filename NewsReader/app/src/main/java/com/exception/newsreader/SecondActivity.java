package com.exception.newsreader;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Toast;

public class SecondActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_second);

        WebView webView = findViewById(R.id.webView);

        webView.getSettings().setJavaScriptEnabled(true);

        webView.setWebViewClient(new WebViewClient());

        Intent intent = getIntent();
        String url  = intent.getStringExtra("url");

        if (url.equals("")){
            webView.loadUrl("http://www.google.com");
        }else {
            webView.loadUrl(url);
            Toast.makeText(getApplicationContext(), url, Toast.LENGTH_SHORT).show();
        }
    }
}
