package com.shamsaha.app.activity.volunteer;

import android.content.Intent;
import android.graphics.Bitmap;
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
import com.shamsaha.app.activity.PublicPart.homeScreenActivity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.content.ContextCompat;

public class WebViewActivity extends AppCompatActivity {

    private ProgressBar progressBar;
    private WebView webView;
    private ImageView back;
    private ConstraintLayout body;

    String url ;
    String type ;

    private void getData() {
        Intent intent = getIntent();
        url = intent.getStringExtra("url");
        type = intent.getStringExtra("type");
        //password_login_first = intent.getStringExtra("password_login_first");
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_web_view);

        webView = findViewById(R.id.webViewContainer);
        progressBar = findViewById(R.id.progressBar8);
        back = findViewById(R.id.back);
        body = findViewById(R.id.body);

        getData();

        if(type.equals("victim")){
            body.setBackgroundColor(ContextCompat.getColor(this, R.color.colorGray));
        }

        progressBar.setVisibility(View.GONE);

        WebSettings settings = webView.getSettings();
        settings.setJavaScriptEnabled(true);
       // settings.setAppCacheEnabled(true);

        int mode = WebSettings.LOAD_DEFAULT;
        settings.setCacheMode(mode);
        //settings.setAppCachePath(getCacheDir().getPath());

        back.setOnClickListener(view -> {
            if(type.equals("victim")){
                Intent i = new Intent(WebViewActivity.this, homeScreenActivity.class);
                startActivity(i);
                finish();
            }else{
                onBackPressed();
            }
        });

//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
//            settings.setSafeBrowsingEnabled(true); // api 26
//        }
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
        settings.setDisplayZoomControls(false);

        settings.setBlockNetworkImage(false);
        settings.setLoadsImagesAutomatically(true);

        webView.setFitsSystemWindows(true);
        webView.setLayerType(View.LAYER_TYPE_HARDWARE, null);

        webView.loadUrl(url);

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
                progressBar.setVisibility(View.VISIBLE);
                if(newProgress == 100){
                    progressBar.setVisibility(View.GONE);
                }
                Log.d("mmmm","ddd"+newProgress);
            }
        });

    }
}