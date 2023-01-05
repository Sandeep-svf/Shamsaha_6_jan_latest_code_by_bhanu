package com.shamsaha.app.activity.PublicPart.event;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.ProgressBar;

import com.shamsaha.app.R;
import com.shamsaha.app.utils.ConstantsURL.baseURL;

import androidx.appcompat.app.AppCompatActivity;

public class EventPaymentActivity extends AppCompatActivity {

    ImageView back;
    WebView webView;
    ProgressBar progressBar3,progressBar5;
    String id,type;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pay_ment);

        back = findViewById(R.id.back);
        webView = findViewById(R.id.webViewContainer);
        progressBar5 = findViewById(R.id.progressBar5);

        Intent intent = getIntent();
        id = intent.getStringExtra("id");
        type = intent.getStringExtra("type");

        progressBar5.setVisibility(View.GONE);

        WebSettings settings = webView.getSettings();

        settings.setJavaScriptEnabled(true);
        //settings.setAppCacheEnabled(true);

        int mode = WebSettings.LOAD_DEFAULT;
        settings.setCacheMode(mode);
        //settings.setAppCachePath(getCacheDir().getPath());


        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            settings.setSafeBrowsingEnabled(true); // api 26
        }

        settings.setUseWideViewPort(true);
        settings.setLoadWithOverviewMode(true);
        settings.setJavaScriptCanOpenWindowsAutomatically(true);

        settings.setDomStorageEnabled(true);
        settings.setSupportMultipleWindows(true);
        settings.setLoadWithOverviewMode(true);
        settings.setAllowContentAccess(true);
        settings.setGeolocationEnabled(true);
        settings.setAllowFileAccess(true);
        settings.setAllowContentAccess(true);
        settings.setDomStorageEnabled(true);

        settings.setSupportZoom(true);
        settings.setBuiltInZoomControls(true);
        settings.setDisplayZoomControls(true);

        settings.setBlockNetworkImage(false);
        settings.setLoadsImagesAutomatically(true);

        webView.setFitsSystemWindows(true);

        String baseUrl = baseURL.BaseURL_API;
        String encorded_data = null;
//        try {
//            encorded_data = URLEncoder.encode(URL,"UTF-8");
//        } catch (UnsupportedEncodingException e) {
//            e.printStackTrace();
//        }
//        webView.postUrl(baseUrl, URL.getBytes());
        webView.loadUrl(baseUrl+"webview/event_pay?reg_id="+id+"&&payType="+type);
//        wv_case_report.loadUrl("https://demo.board.support/email.php");
//        wv_case_report.loadUrl("https://janus.conf.meetecho.com/videocalltest.html");
//        wv_case_report.loadUrl("https://messenger.complexcoder.com/");
//        webView.loadUrl("https://barcbahrain.sayg.bh/");
        webView.setLayerType(View.LAYER_TYPE_HARDWARE, null);

        webView.setWebViewClient(new WebViewClient(){
            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                super.onPageStarted(view, url, favicon);
//                Toast.makeText(PayMentActivity.this, url, Toast.LENGTH_SHORT).show();
                Log.d("nnnnnnnnnn",url);
            }
        });

        webView.setWebChromeClient(new WebChromeClient(){
            public void onProgressChanged(WebView view, int newProgress) {
                progressBar5.setVisibility(View.VISIBLE);
                if(newProgress == 100){
                    progressBar5.setVisibility(View.GONE);
                }
                Log.d("mmmm","ddd"+newProgress);
            }
        });

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

    }
}