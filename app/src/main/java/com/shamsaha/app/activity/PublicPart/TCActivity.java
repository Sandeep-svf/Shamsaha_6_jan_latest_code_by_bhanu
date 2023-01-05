package com.shamsaha.app.activity.PublicPart;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.muddzdev.styleabletoast.StyleableToast;
import com.shamsaha.app.R;
import com.shamsaha.app.databinding.ActivityTCBinding;
import com.shamsaha.app.viewModels.TCViewModel;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

public class TCActivity extends AppCompatActivity {
    private ActivityTCBinding binding;
    private TCViewModel viewModel;
    public static String api_data = "Loading....!";
    private boolean language;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityTCBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        viewModel = new ViewModelProvider(this).get(TCViewModel.class);
        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.M && checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            requestPermissions(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1001);
        }




        hitApi();

        binding.webViewContainer.setVisibility(View.GONE);

        binding.button2.setOnClickListener(v -> {
            boolean stat = binding.termsCondition.isChecked();
            saveData(stat);
            if (binding.termsCondition.isChecked()) {
                Intent i = new Intent(getApplicationContext(), homeScreenActivity.class);
                startActivity(i);
                finish();
            } else {
                StyleableToast.makeText(TCActivity.this, "Please accept Terms & Conditions"
                        , Toast.LENGTH_LONG, R.style.mytoast).show();
            }
        });
        loadData();

        if(language){
            binding.termsCondition.setVisibility(View.GONE);
            binding.button2.setVisibility(View.GONE);

        }
        //updateView();
    }

    private void hitApi() {
       viewModel.hitApi();
       viewModel.getApi_data().observe(this, s -> {
           if (s!=null){
               binding.webViewContainer.setVisibility(View.VISIBLE);
               setwebView(s);
           }
       });
    }

    private void setwebView(String data) {
        binding.webViewContainer.setBackgroundColor(Color.TRANSPARENT);
//        webView.loadData(data, "text/html", "UTF-8");
        binding.webViewContainer.loadDataWithBaseURL(null,data, "text/html; charset=utf-8", "base64",null);

        Log.d("apidataaa", data);
    }

    public void saveData(boolean s) {
        SharedPreferences sharedPreferences = getSharedPreferences("Setting", MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean("T&C_Status", s);
        editor.apply();
    }

    public void loadData() {
        SharedPreferences pref = getSharedPreferences("Setting", Activity.MODE_PRIVATE);
        language = pref.getBoolean("T&C_Status", false);
        binding.termsCondition.setChecked(language);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

    }
}
