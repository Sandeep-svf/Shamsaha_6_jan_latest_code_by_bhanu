package com.shamsaha.app.activity.volunteer;

import android.annotation.TargetApi;
import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.hardware.fingerprint.FingerprintManager;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.webkit.PermissionRequest;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.material.navigation.NavigationView;
import com.muddzdev.styleabletoast.StyleableToast;
import com.shamsaha.app.ApiModel.volunter.ProfileInfoModel;
import com.shamsaha.app.ApiModel.volunter.ScheduleStatusModel;
import com.shamsaha.app.R;
import com.shamsaha.app.activity.PublicPart.event.eventsActivity;
import com.shamsaha.app.api.api;
import com.shamsaha.app.retrofitAdaptor.retrofit;
import com.shamsaha.app.utils.ConstantsURL.baseURL;
import com.shamsaha.app.utils.JsInterface;

import org.infideap.drawerbehavior.AdvanceDrawerLayout;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
/**
 * CAse report from inside the chat
 * */
public class ChatCaseReportActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    boolean doubleBackToExitPressedOnce = false;
    private String vounter_id;
    private String case_id;
    private String profile;
    private String dutyStatus = "0";
    private String VolunteerName;
    private ImageView handBurger, profilePic, back;
    private AdvanceDrawerLayout drawer;
    private Button btn_duty_status;
    private WebView wv_case_report;

    private Dialog dialog;
    private FingerprintManager fingerprintManager = null;

    @Override
    public void onBackPressed() {
        return;

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_call_case_report);

        drawer = findViewById(R.id.drawer_layout);
        handBurger = findViewById(R.id.handBurger);
        profilePic = findViewById(R.id.profilePic);
        btn_duty_status = findViewById(R.id.btn_duty_status);
        back = findViewById(R.id.back);
        wv_case_report = findViewById(R.id.wv_case_report);

        getData();

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawer, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        ImageView navigationView_ = navigationView.getHeaderView(0).findViewById(R.id.nav_icon);
        TextView tv_Name = navigationView.getHeaderView(0).findViewById(R.id.tv_Name);
        Glide.with(ChatCaseReportActivity.this).load(profile).placeholder(R.drawable.ic_smalllogo).into(navigationView_);
        tv_Name.setText(VolunteerName);
        drawer.setViewScale(Gravity.START, 0.9f);
        drawer.setRadius(Gravity.START, 35);
        drawer.setViewElevation(Gravity.START, 20);

//        wv_case_report = new WebView(this);

        WebSettings settings = wv_case_report.getSettings();

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
        settings.setAllowFileAccess(true);
        settings.setAllowContentAccess(true);
        settings.setDomStorageEnabled(true);

        wv_case_report.addJavascriptInterface(new JsInterface(message -> {
            runOnUiThread(() -> back.setVisibility(View.VISIBLE));
        }),"Android");

        wv_case_report.setFitsSystemWindows(true);

        wv_case_report.loadUrl(baseURL.BaseURL + "apis/webview/casereport?case_id="+case_id+"&volunteer="+vounter_id);
//        http://shamsaha.sayg.co/crreport.php
//        wv_case_report.loadUrl("https://demo.board.support/email.php");
//        wv_case_report.loadUrl("https://janus.conf.meetecho.com/videocalltest.html");
//        wv_case_report.loadUrl("https://messenger.complexcoder.com/");
//        wv_case_report.loadUrl("https://barcbahrain.sayg.bh/");
        wv_case_report.setLayerType(View.LAYER_TYPE_HARDWARE, null);

        wv_case_report.setWebViewClient(new WebViewClient() {

            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
            }

        });

        wv_case_report.setWebChromeClient(new WebChromeClient() {
            // Grant permissions for cam
            @Override
            public void onPermissionRequest(final PermissionRequest request) {
                ChatCaseReportActivity.this.runOnUiThread(new Runnable() {
                    @TargetApi(Build.VERSION_CODES.M)
                    @Override
                    public void run() {

                        if (request.getOrigin().toString().equals("file:///")) {
                            request.grant(request.getResources());
                        } else {
                            request.deny();
                        }
                    }
                });
            }


        });

        back.setOnClickListener(view -> super.onBackPressed());

    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {


        switch (item.getItemId()) {
            case R.id.Home:
                VolunteerHome();
                return true;

            case R.id.profile:
                profile();
                return true;

            case R.id.schedule:
                Shift();
                return true;

            case R.id.events:
                event();
                return true;

            case R.id.logout:
                VolunterLogin();
                return true;

            case R.id.settings:
                drawer.closeDrawer(GravityCompat.START);
                settingsDialog();
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void settingsDialog() {
        dialog = new Dialog(this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(false);
        dialog.setContentView(R.layout.dialog_setting);
        ImageView i = dialog.findViewById(R.id.iv_dialog_close);
        Switch switch1 = dialog.findViewById(R.id.switch1);
        LinearLayout ll_change_password = dialog.findViewById(R.id.ll_change_password);

        boolean a = loadData();

        if (a) {
            switch1.setChecked(true);
        } else {
            switch1.setChecked(false);
        }

        i.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        switch1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean on = ((Switch) v).isChecked();
                if (on) {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                        if (!fingerprintManager.isHardwareDetected()) {
                            // Device doesn't support fingerprint authentication
                            StyleableToast.makeText(getApplicationContext(),
                                    "Device doesn't support fingerprint authentication"
                                    , Toast.LENGTH_LONG, R.style.mytoast).show();
                            switch1.setChecked(false);
                        } else if (!fingerprintManager.hasEnrolledFingerprints()) {
                            // User hasn't enrolled any fingerprints to authenticate with
                            StyleableToast.makeText(getApplicationContext(),
                                    "Device hasn't enrolled any fingerprints to authenticate with"
                                    , Toast.LENGTH_LONG, R.style.mytoast).show();
                            switch1.setChecked(false);
                        } else {
                            // Everything is ready for fingerprint authentication
                            saveData(true);
                        }
                    }
                } else {
                    saveData(false);
                }
            }
        });

        ll_change_password.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                changePassword();
            }
        });

        dialog.show();
        dialog.getWindow().setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);

    }

    public boolean loadData() {

        SharedPreferences pref = getSharedPreferences("Setting", Activity.MODE_PRIVATE);
        boolean Biometric = pref.getBoolean("Biometric", false);
        return Biometric;

    }

    public void saveData(boolean s) {

        SharedPreferences sharedPreferences = getSharedPreferences("Setting", MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean("Biometric", s);
        editor.apply();

    }

    private void changePassword() {

        Intent i = new Intent(getApplicationContext(), ChangePasswordActivity.class);
        i.putExtra("vounter_id", vounter_id);
        i.putExtra("profile", profile);
        i.putExtra("VolunteerName", VolunteerName);
        startActivity(i);

    }

    private void VolunterLogin() {

        Intent i = new Intent(getApplicationContext(), loginActivity.class);
        startActivity(i);
        finish();
        drawer.closeDrawer(GravityCompat.START);

    }

    private void VolunteerHome() {

        Intent i = new Intent(getApplicationContext(), VolunteerHome.class);
        i.putExtra("vounter_id", vounter_id);
        i.putExtra("profile", profile);
        i.putExtra("VolunteerName", VolunteerName);
        startActivity(i);
        finish();
        drawer.closeDrawer(GravityCompat.START);

    }

    private void profile() {

        Intent i = new Intent(getApplicationContext(), ProfileActivity.class);
        i.putExtra("vounter_id", vounter_id);
        i.putExtra("profile", profile);
        i.putExtra("VolunteerName", VolunteerName);
        startActivity(i);
        finish();
        drawer.closeDrawer(GravityCompat.START);

    }

    private void Shift() {

        Intent i = new Intent(getApplicationContext(), ShiftActivity.class);
        i.putExtra("vounter_id", vounter_id);
        i.putExtra("profile", profile);
        i.putExtra("name", VolunteerName);
        startActivity(i);
        finish();
        drawer.closeDrawer(GravityCompat.START);

    }

    private void event() {

        Intent i = new Intent(getApplicationContext(), eventsActivity.class);
        startActivity(i);
        finish();
        drawer.closeDrawer(GravityCompat.START);

    }

    private void getData() {

        Intent intent = getIntent();
        vounter_id = intent.getStringExtra("volId");
        case_id = intent.getStringExtra("caseId");
//        total_rewards = intent.getStringExtra("total_rewards");
    }

    private void hitStatusAPi() {
        api api = retrofit.retrofit.create(api.class);
        Call<ScheduleStatusModel> call = api.ScheduleStatusModel(vounter_id);
        call.enqueue(new Callback<ScheduleStatusModel>() {
            @Override
            public void onResponse(Call<ScheduleStatusModel> call, Response<ScheduleStatusModel> response) {
                if (response.isSuccessful()) {
                    assert response.body() != null;
                    dutyStatus = response.body().getMessage();
                    setDutyStatus(dutyStatus);
                } else {
                    Log.d("sssss", "err : " + response.errorBody());
                }
            }

            @Override
            public void onFailure(Call<ScheduleStatusModel> call, Throwable t) {
                Log.d("sssss", "err " + t.getMessage());
                StyleableToast.makeText(ChatCaseReportActivity.this, t.getMessage(), Toast.LENGTH_SHORT).show();

            }
        });
    }

    private void setDutyStatus(String dutyStatus) {
        if (dutyStatus.equals("1")) {
            btn_duty_status.setBackgroundResource(R.drawable.ic_duty_btn);
            btn_duty_status.setText("ON DUTY");
        } else {
            btn_duty_status.setVisibility(View.INVISIBLE);
        }
    }

    private void hitProfileApi() {
        api api = retrofit.retrofit.create(api.class);
        Call<List<ProfileInfoModel>> call = api.volunteer_info(vounter_id);
        Log.d("sssss", "vounter_id : " + vounter_id);
        call.enqueue(new Callback<List<ProfileInfoModel>>() {
            @Override
            public void onResponse(Call<List<ProfileInfoModel>> call, Response<List<ProfileInfoModel>> response) {
                if (response.isSuccessful()) {
                    assert response.body() != null;
                    List<ProfileInfoModel> pro = response.body();
                    for (ProfileInfoModel infoModel : pro) {
                        VolunteerName = infoModel.getVname();
                        profile = infoModel.getProfilePic();
                    }
                    setView(profile);
                } else {
                    Log.d("sssss", "err " + response.errorBody());
                }
            }

            @Override
            public void onFailure(Call<List<ProfileInfoModel>> call, Throwable t) {
                Log.d("sssss", "err " + t.getMessage());
            }
        });
    }

    private void setView(String profile) {
        Glide.with(getApplicationContext()).load(profile).placeholder(R.drawable.ic_smalllogo).into(profilePic);
    }


}