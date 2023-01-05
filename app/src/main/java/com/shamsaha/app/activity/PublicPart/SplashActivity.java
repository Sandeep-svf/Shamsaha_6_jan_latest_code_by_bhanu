package com.shamsaha.app.activity.PublicPart;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;

import com.google.firebase.messaging.FirebaseMessaging;
import com.shamsaha.app.R;
import com.shamsaha.app.activity.ChatHelper.Application.TwilioChatApplication;
import com.shamsaha.app.activity.general.PinActivity;
import com.shamsaha.app.activity.volunteer.VolunteerHome;
import com.shamsaha.app.databinding.ActivitySplashBinding;
import com.shamsaha.app.utils.ConstantsURL.baseURL;
import com.shamsaha.app.utils.SharedPreferencesUtils;
import com.shamsaha.app.viewModels.SplashViewModel;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

public class SplashActivity extends AppCompatActivity {
    private ActivitySplashBinding binding;
    Animation animFadeIn;
    private boolean termsCondition;
    private boolean havePIN;
    private SplashViewModel viewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivitySplashBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        initialize();
        hitapi();
        loadData();
        setUpHandler();
        setAnimFadeIn();
    }

    private void initialize() {
        if (TwilioChatApplication.getSelectedLanguage().isEmpty()) {
            baseURL.LANGUAGE_CODE = "en";
        } else {
            baseURL.LANGUAGE_CODE = TwilioChatApplication.getSelectedLanguage();
            TwilioChatApplication.setLocale(SplashActivity.this, baseURL.LANGUAGE_CODE, true);
        }

        FirebaseMessaging.getInstance().subscribeToTopic("all");

        viewModel = new ViewModelProvider(this).get(SplashViewModel.class);
    }

    private void setUpHandler() {
        new Handler().postDelayed(() -> {

            if (termsCondition && havePIN) {
                pin();
            } else if (termsCondition) {
                if (SharedPreferencesUtils.getIsLoggedIn()) {
                    volunteerHome();
                } else
                    home();
            } else if (!havePIN) {
                tc();
            }
        }, 1000); // wait for 5 seconds
    }

    private void volunteerHome() {
        Intent i = new Intent(getApplicationContext(), VolunteerHome.class);
        startActivity(i);
        overridePendingTransition(R.anim.fade_in_zero, R.anim.fade_out_zero);
        finish();
    }

    public void loadData() {
        SharedPreferences pref = getSharedPreferences("Setting", Activity.MODE_PRIVATE);
        boolean language = pref.getBoolean("T&C_Status", false);
        havePIN = pref.getBoolean("PIN_Status", false);
        termsCondition = language;
    }

    private void pin() {
        Intent i = new Intent(getApplicationContext(), PinActivity.class);
        startActivity(i);
        overridePendingTransition(R.anim.fade_in_zero, R.anim.fade_out_zero);
        finish();
    }

    private void home() {
        Intent i = new Intent(getApplicationContext(), homeScreenActivity.class);
        startActivity(i);
        overridePendingTransition(R.anim.fade_in_zero, R.anim.fade_out_zero);
        finish();
    }


    private void tc() {
        Intent i = new Intent(getApplicationContext(), TCActivity.class);
        startActivity(i);
        finish();
    }

    private String uniqueDeviceId() {
        //return Settings.Secure.getString(this.getContentResolver(), Settings.Secure.ANDROID_ID);
        return TwilioChatApplication.get().getSettings().getString("uniqueDeviceId", "");
    }

    private void hitapi() {
        viewModel.hitApi(uniqueDeviceId());
    }

    private void setAnimFadeIn() {
        animFadeIn = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.move_up);
        binding.logo.startAnimation(animFadeIn);
    }

    /*public void saveData(boolean s) {
        SharedPreferences sharedPreferences = getSharedPreferences("Setting", MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean("PIN_Status", s);
        editor.apply();
    }*/


   /* private void ShiftList() {
        Intent i = new Intent(getApplicationContext(), ShiftRequestListActivity.class);
        startActivity(i);
        overridePendingTransition(R.anim.fade_in_zero, R.anim.fade_out_zero);
        finish();
    }*/


}