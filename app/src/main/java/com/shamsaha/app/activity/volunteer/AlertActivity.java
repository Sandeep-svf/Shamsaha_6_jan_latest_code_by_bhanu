package com.shamsaha.app.activity.volunteer;

import android.content.Intent;
import android.content.res.ColorStateList;
import android.net.Uri;
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
import android.widget.TextView;

import com.shamsaha.app.R;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

public class AlertActivity extends AppCompatActivity {

    private static final String TAG = "abc";
    private String txt, url, subj, type;
    private ImageView iv_download;
    private boolean value = false;
    private ConstraintLayout profileAct;
    private TextView textView4;
    private  WebView urlWebView ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_alert);

        urlWebView = findViewById(R.id.wv_alert_report);
        ImageView back = findViewById(R.id.back);
        TextView textView11 = findViewById(R.id.textView11);
        TextView textView14 = findViewById(R.id.textView14);
        ProgressBar progressBar3 = findViewById(R.id.progressBar3);
        profileAct = findViewById(R.id.profileAct);
        textView4 = findViewById(R.id.textView4);
        iv_download = findViewById(R.id.iv_download);

        Intent intent = getIntent();
        txt = intent.getStringExtra("getContentEn");
        url = intent.getStringExtra("getImage");
        subj = intent.getStringExtra("getSubjectEn");
        type = intent.getStringExtra("getType");
        value = intent.getBooleanExtra("boolean", false);

        if (value) {
            profileAct.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.colorPink)));
//            textView4.setText("Document");
            back.setVisibility(View.GONE);
            textView4.setVisibility(View.GONE);
        } else {
            profileAct.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.colorYellow)));
            textView4.setText(getResources().getString(R.string.admin_notes));
            back.setVisibility(View.VISIBLE);
            textView4.setVisibility(View.VISIBLE);
        }

        textView11.setText(subj);
        textView14.setText(txt);

        WebSettings settings = urlWebView.getSettings();
        settings.setJavaScriptEnabled(true);
        //settings.setAppCacheEnabled(false);
        int mode = WebSettings.LOAD_DEFAULT;
//        settings.setCacheMode(mode);
//        settings.setAppCachePath(getCacheDir().getPath());


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
        settings.setSupportZoom(true);
        settings.setBuiltInZoomControls(true);
        settings.setDisplayZoomControls(true);

        urlWebView.setFitsSystemWindows(true);


        if (type.equals("doc")) {
            Log.d("abc","url ===> "+url);
            urlWebView.loadUrl("https://docs.google.com/gview?embedded=true&url="
                    + url);
            Log.d("abc","url ===> "+url);
        } else if (type.equals("image")) {
            urlWebView.loadUrl(url);
        } else if (type.contains("pdf")) {
            Log.d("abc","url ===> "+url);
            urlWebView.loadUrl("https://docs.google.com/gview?embedded=true&url="
                    + url);

        }

        iv_download.setOnClickListener(v -> {
            Intent i = new Intent(Intent.ACTION_VIEW);
            Log.i(TAG, "onCreate: Url is: "+url);
            i.setData(Uri.parse(url));
            startActivity(i);
        });

//        urlWebView.loadUrl("http://shamsaha.sayg.co/uploads/announcement/about.png");
        urlWebView.setLayerType(View.LAYER_TYPE_HARDWARE, null);

        urlWebView.setWebViewClient(new WebViewClient() {

            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
//                Toast.makeText(AlertActivity.this, ""+url, Toast.LENGTH_SHORT).show();
                Log.d(TAG, "onPageFinished: "+url);
            }

        });

        urlWebView.setWebChromeClient(new WebChromeClient() {
            public void onProgressChanged(WebView view, int newProgress) {
                progressBar3.setVisibility(View.VISIBLE);
                if (newProgress == 100) {
                    progressBar3.setVisibility(View.GONE);
                }
                Log.d("mmmm", "ddd" + newProgress);
            }
        });

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        Log.d("abc", "onDestroy: ");
    }
}