package com.origin.wottopark;

import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class MainwebviewActivity extends AppCompatActivity {
    private WebView mainwebView;
    private WebSettings webSettings;
    private WebViewClient webviewClient;
    private ImageButton homeButton;
    String URL;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mainwebview);
        mainwebView = (WebView) findViewById(R.id.webview_main);
        homeButton = (ImageButton)findViewById(R.id.img_navwebview);
        webviewClient = new WebViewClient();
        mainwebView.setWebViewClient(webviewClient);
        webSettings = mainwebView.getSettings();
        webSettings.setJavaScriptEnabled(true);
        URL = getIntent().getStringExtra("webviewurl");

        mainwebView.loadUrl(URL);

        homeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Gohome_func();
            }
        });
    }
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if ((keyCode == KeyEvent.KEYCODE_BACK) && this.mainwebView.canGoBack()) {
            this.mainwebView.goBack();
            return true;
        }

        return super.onKeyDown(keyCode, event);
    }
    private void Gohome_func(){
        Intent intent =  new Intent(MainwebviewActivity.this,MainActivity.class);
        startActivity(intent);
    }

}
