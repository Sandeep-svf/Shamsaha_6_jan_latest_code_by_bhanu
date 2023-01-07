package com.shamsaha.app.activity;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.webkit.WebView;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.shamsaha.app.ApiModel.Termsncondition;
import com.shamsaha.app.R;
import com.shamsaha.app.api.api;
import com.shamsaha.app.retrofitAdaptor.retrofit;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class TermsCondition {
    Animation animFadein;
    Dialog dialog;


    public void openDialog(Context context) {
        dialog = new Dialog(context);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(false);
        dialog.setContentView(R.layout.terms);
        ImageView i = dialog.findViewById(R.id.closeBtn2);
        ProgressBar progressBar = dialog.findViewById(R.id.progressBar6);
        WebView webView = dialog.findViewById(R.id.webViewContainer);
        progressBar.setVisibility(View.VISIBLE);
        hitApi(context,webView,progressBar);
        i.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.show();
        dialog.getWindow().setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);
        dialog.getWindow().getAttributes().windowAnimations = R.anim.fade_in;
    }



    private void hitApi(Context context, WebView webView,ProgressBar progressBar) {
        api api = retrofit.retrofit.create(api.class);
        Call<List<Termsncondition>> getInvolved = api.termsnconditionsData();
        getInvolved.enqueue(new Callback<List<Termsncondition>>() {
            @Override
            public void onResponse(Call<List<Termsncondition>> call, Response<List<Termsncondition>> response) {

                List<Termsncondition> data = response.body();
                progressBar.setVisibility(View.GONE);
                for (Termsncondition l : data) {
                    //Toast.makeText(MainActivity.this, l.getSite_logo(), Toast.LENGTH_SHORT).show();
                    setwebView(l.getContentEn(),webView,context);
                    //aboutTxt.setText(l.getContentEn());
                }
            }

            @Override
            public void onFailure(Call<List<Termsncondition>> call, Throwable t) {
                Toast.makeText(context, t.toString(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void setwebView(String data, WebView webView,Context context) {
        animFadein = AnimationUtils.loadAnimation(context, R.anim.fade_in);
        webView.setBackgroundColor(Color.TRANSPARENT);
        webView.setAnimation(animFadein);
        webView.loadDataWithBaseURL(null,data, "text/html; charset=utf-8", "base64",null);
//        webView.setAnimation(animFadein);
        Log.d("apidataa", data);
    }

}
