package com.example.zyzeng.homeworkhit.activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.webkit.WebSettings;
import android.webkit.WebView;
import com.example.zyzeng.homeworkhit.R;


public class ChatActivity extends AppCompatActivity {
    private WebView mWebView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);
//        mWebView = findViewById(R.id.web);
//        mWebView.getSettings().setJavaScriptEnabled(true);// 支持js
//        mWebView.setWebViewClient(new WebViewClient());//防止加载网页时调起系统浏览器
//        mWebView.loadUrl("file:///android_asset/index.html");
        mWebView = (WebView) findViewById(R.id.web);
        //mWebView.loadUrl("http://104.243.30.196:8080/chatroom_war/");
        mWebView.loadUrl("file:///android_asset/index.html");
//        mWebView.setWebChromeClient(webChromeClient);
//        mWebView.setWebViewClient(webViewClient);
        WebSettings webSettings=mWebView.getSettings();
        webSettings.setJavaScriptEnabled(true);//允许使用js
    }
}