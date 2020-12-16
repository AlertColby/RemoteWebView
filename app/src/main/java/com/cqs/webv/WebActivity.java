package com.cqs.webv;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.webkit.WebView;

public class WebActivity extends AppCompatActivity {

    private WebView webview;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_web);
        webview = findViewById(R.id.webview);
    }

    public void monitorCrash(View view){
        throw new RuntimeException("123");
    }

    public void testLog(View view){
    }
}
