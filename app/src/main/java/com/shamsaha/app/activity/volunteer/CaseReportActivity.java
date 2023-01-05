package com.shamsaha.app.activity.volunteer;

import android.annotation.TargetApi;
import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.hardware.fingerprint.FingerprintManager;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.webkit.PermissionRequest;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.material.navigation.NavigationView;
import com.muddzdev.styleabletoast.StyleableToast;
import com.shamsaha.app.ApiModel.ChannelListModel;
import com.shamsaha.app.ApiModel.volunter.AdditionalLanguageModel;
import com.shamsaha.app.ApiModel.volunter.LanguageRequestModel;
import com.shamsaha.app.ApiModel.volunter.ProfileInfoModel;
import com.shamsaha.app.ApiModel.volunter.ScheduleStatusModel;
import com.shamsaha.app.R;
import com.shamsaha.app.activity.volunteer.Resources.VolunterResourceActivity;
import com.shamsaha.app.activity.volunteer.VolEvent.VolEventsActivity;
import com.shamsaha.app.adaptor.AdditionalLanguageSpinnerAdaptor;
import com.shamsaha.app.adaptor.CaseIDSpinnerAdaptor;
import com.shamsaha.app.adaptor.ChannelAdapter1;
import com.shamsaha.app.adaptor.Volunteer.CaseListAdaptor;
import com.shamsaha.app.api.api;
import com.shamsaha.app.databinding.ActivityCaseReportFixBinding;
import com.shamsaha.app.preferences.Singleton;
import com.shamsaha.app.retrofitAdaptor.retrofit;
import com.shamsaha.app.utils.ConstantsURL.baseURL;
import com.shamsaha.app.utils.JsInterface;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.view.GravityCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
/**
 * Case Report from Menu
 * */
public class CaseReportActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener, OnItemClick {

    private ActivityCaseReportFixBinding binding;

    private static final String TAG = "abc";
    boolean doubleBackToExitPressedOnce = false;
    private String vounter_id;
    private String profile;
    private String dutyStatus = "0";
    private String VolunteerName;
    private CaseListAdaptor caseListAdaptor;

    private Dialog dialog;
    private FingerprintManager fingerprintManager = null;

    private ArrayList<ChannelListModel.Datum> channelList = new ArrayList<>();
    private static ChannelAdapter1 channelAdapter;

    private boolean isResourceClicked = false;
    private Dialog caseDialog;

    @Override
    public void onBackPressed() {

        //super.onBackPressed();
        return;

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityCaseReportFixBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        binding.back.setVisibility(View.GONE);
        //setContentView(R.layout.activity_case_report);

        //drawer = findViewById(R.id.drawer_layout);
        //handBurger = findViewById(R.id.handBurger);
        //profilePic = findViewById(R.id.profilePic);
        //btn_duty_status = findViewById(R.id.btn_duty_status);
        //back = findViewById(R.id.back);
        //back.setVisibility(View.GONE);
        //wv_case_report = findViewById(R.id.wv_case_report);

        getData();
        hitStatusAPi();
        hitProfileApi();
        hitHelpRequestApi();


        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, binding.drawerLayout, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        binding.drawerLayout.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        ImageView navigationView_ = navigationView.getHeaderView(0).findViewById(R.id.nav_icon);
        TextView tv_Name = navigationView.getHeaderView(0).findViewById(R.id.tv_Name);
        Glide.with(CaseReportActivity.this).load(profile).placeholder(R.drawable.ic_smalllogo).into(navigationView_);
        tv_Name.setText(VolunteerName);
        binding.drawerLayout.setViewScale(Gravity.START, 0.9f);
        binding.drawerLayout.setRadius(Gravity.START, 35);
        binding.drawerLayout.setViewElevation(Gravity.START, 20);

        binding.handBurger.setOnClickListener(v -> binding.drawerLayout.openDrawer(GravityCompat.START));

        binding.back.setOnClickListener(v -> super.onBackPressed());


//        wv_case_report = new WebView(this);

        WebSettings settings = binding.wvCaseReport.getSettings();

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

        binding.wvCaseReport.addJavascriptInterface(new JsInterface(message -> {
            Log.i(TAG, "SKATA ");
            runOnUiThread(() -> binding.back.setVisibility(View.VISIBLE));
           //binding.toggle.setVisibility(View.GONE);
        }),"Android");

        binding.wvCaseReport.setFitsSystemWindows(true);

//        wv_case_report.loadUrl(baseURL.BaseURL+"crreport.php?volunteer="+vounter_id+"case_id=");
        binding.wvCaseReport.loadUrl(baseURL.BaseURL+"apis/webview/casereport?case_id="+CaseID+"&volunteer="+vounter_id);

//        http://shamsaha.sayg.co/crreport.php
//        http://shamsaha.org/app/apis/webview/casereport?case_id=1223&volunteer=124
//        wv_case_report.loadUrl("https://demo.board.support/email.php");
//        wv_case_report.loadUrl("https://janus.conf.meetecho.com/videocalltest.html");
//        wv_case_report.loadUrl("https://messenger.complexcoder.com/");
//        wv_case_report.loadUrl("https://barcbahrain.sayg.bh/");
        binding.wvCaseReport.setLayerType(View.LAYER_TYPE_HARDWARE, null);

        binding.wvCaseReport.setWebViewClient(new WebViewClient() {

            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
            }

        });

        binding.wvCaseReport.setWebChromeClient(new WebChromeClient() {
            // Grant permissions for cam
            @Override
            public void onPermissionRequest(final PermissionRequest request) {
                CaseReportActivity.this.runOnUiThread(new Runnable() {
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

            case R.id.services:
                openServicesDialog();
                return true;

            case R.id.caseReport:
                caseReport();
                return true;

            case R.id.resources:
                resource();
                return true;

            case R.id.myShift:
                upcomingSgift();
                return true;

            case R.id.help:
//                openBackupDialog();
                if (!isResourceClicked) {
                    NavigationView nv = findViewById(R.id.nav_view);
                    Menu m = nv.getMenu();
                    int id = item.getItemId();
                    m.findItem(R.id.languageSupport).setVisible(true);
                    m.findItem(R.id.backUpSupport).setVisible(true);
                    isResourceClicked = true;
                } else {
                    NavigationView nv = findViewById(R.id.nav_view);
                    Menu m = nv.getMenu();
                    int id = item.getItemId();
                    m.findItem(R.id.languageSupport).setVisible(false);
                    m.findItem(R.id.backUpSupport).setVisible(false);
                    isResourceClicked = false;
                }
                return true;

            case R.id.languageSupport:
                openLanguageDialog();
                return true;

            case R.id.backUpSupport:
                openBackupDialog();
                return true;


            case R.id.logout:
                VolunterLogin();
                return true;

            case R.id.shiftRequest:
                binding.drawerLayout.closeDrawer(GravityCompat.START);
                shiftRequest();
                return true;

            case R.id.settings:
                binding.drawerLayout.closeDrawer(GravityCompat.START);
                settingsDialog();
                return true;

            case R.id.outstandingRequests:
                binding.drawerLayout.closeDrawer(GravityCompat.START);
                outstandingRequests();
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void outstandingRequests() {
        Intent i = new Intent(getApplicationContext(), OutstandingRequests.class);
        i.putExtra("vounter_id", vounter_id);
        i.putExtra("profile", profile);
        i.putExtra("VolunteerName", VolunteerName);
        startActivity(i);
        finish();
        binding.drawerLayout.closeDrawer(GravityCompat.START);
    }

    private void resource() {
        Intent i = new Intent(getApplicationContext(), VolunterResourceActivity.class);
        i.putExtra("vounter_id", vounter_id);
        i.putExtra("profile", profile);
        i.putExtra("VolunteerName", VolunteerName);
        startActivity(i);
        binding.drawerLayout.closeDrawer(GravityCompat.START);
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
        binding.drawerLayout.closeDrawer(GravityCompat.START);

    }

    private void VolunteerHome() {

        Intent i = new Intent(getApplicationContext(), VolunteerHome.class);
        i.putExtra("vounter_id", vounter_id);
        i.putExtra("profile", profile);
        i.putExtra("VolunteerName", VolunteerName);
        startActivity(i);
        finish();
        binding.drawerLayout.closeDrawer(GravityCompat.START);

    }

    private void profile() {

        Intent i = new Intent(getApplicationContext(), ProfileActivity.class);
        i.putExtra("vounter_id", vounter_id);
        i.putExtra("profile", profile);
        i.putExtra("VolunteerName", VolunteerName);
        startActivity(i);
        finish();
        binding.drawerLayout.closeDrawer(GravityCompat.START);

    }

    private void Shift() {

        Intent i = new Intent(getApplicationContext(), ShiftActivity.class);
        i.putExtra("vounter_id", vounter_id);
        i.putExtra("profile", profile);
        i.putExtra("name", VolunteerName);
        startActivity(i);
        finish();
        binding.drawerLayout.closeDrawer(GravityCompat.START);

    }

    private void event() {

        Intent i = new Intent(getApplicationContext(), VolEventsActivity.class);
        startActivity(i);
        finish();
        binding.drawerLayout.closeDrawer(GravityCompat.START);

    }

    private void getData() {

        Intent intent = getIntent();
        vounter_id = intent.getStringExtra("vounter_id");
        profile = intent.getStringExtra("profile");
        VolunteerName = intent.getStringExtra("VolunteerName");
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
                StyleableToast.makeText(CaseReportActivity.this, t.getMessage(), Toast.LENGTH_SHORT).show();

            }
        });
    }

    private void setDutyStatus(String dutyStatus) {
        if (dutyStatus.equals("1")) {
            binding.btnDutyStatus.setBackgroundResource(R.drawable.ic_duty_btn);
            binding.btnDutyStatus.setText("ON DUTY");
        } else {
            binding.btnDutyStatus.setVisibility(View.INVISIBLE);
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
        Glide.with(getApplicationContext()).load(profile).placeholder(R.drawable.ic_smalllogo).into(binding.profilePic);
    }



    private void shiftRequest() {
        Intent i = new Intent(getApplicationContext(), ShiftRequestListActivity.class);
        i.putExtra("vounter_id", vounter_id);
        i.putExtra("profile", profile);
        i.putExtra("VolunteerName", VolunteerName);
        startActivity(i);
        binding.drawerLayout.closeDrawer(GravityCompat.START);
    }

    private void upcomingSgift() {
        Intent i = new Intent(getApplicationContext(), UpcomingShiftActivity.class);
        i.putExtra("vounter_id", vounter_id);
        i.putExtra("profile", profile);
        i.putExtra("VolunteerName", VolunteerName);
        startActivity(i);
        binding.drawerLayout.closeDrawer(GravityCompat.START);
    }

    private void caseReport() {
        Intent i = new Intent(getApplicationContext(), CaseReportActivity.class);
        i.putExtra("vounter_id", vounter_id);
        i.putExtra("profile", profile);
        i.putExtra("VolunteerName", VolunteerName);
        startActivity(i);
        binding.drawerLayout.closeDrawer(GravityCompat.START);
    }

    String CaseID ;
    String CaseID1 ;
    String Language ;

    public void openServicesDialog() {
        binding.drawerLayout.closeDrawer(GravityCompat.START);
        dialog = new Dialog(this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(false);
        dialog.setContentView(R.layout.services_dialog);
        ImageView closeImg2 = dialog.findViewById(R.id.closeBtn2);
        ConstraintLayout body2 = dialog.findViewById(R.id.body2);
        Spinner case_id_spinner = dialog.findViewById(R.id.case_id_spinner);
        CardView ramada = dialog.findViewById(R.id.ramada);
        CardView workstation = dialog.findViewById(R.id.workstation);
        WebView webView = dialog.findViewById(R.id.webview);
        closeImg2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        WebSettings settings = webView.getSettings();
        settings.setJavaScriptEnabled(true);
        //settings.setAppCacheEnabled(true);
        settings.setDomStorageEnabled(true);
        int mode = WebSettings.LOAD_DEFAULT;
        settings.setCacheMode(mode);
        //settings.setAppCachePath(getCacheDir().getPath());

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            settings.setSafeBrowsingEnabled(true); // api 26
        }
        settings.setUseWideViewPort(true);
        settings.setLoadWithOverviewMode(true);
        webView.clearCache(true);
        webView.getSettings().setJavaScriptEnabled(true);
        webView.getSettings().setJavaScriptCanOpenWindowsAutomatically(true);

        settings.setDomStorageEnabled(true);
        settings.setSupportMultipleWindows(true);
        settings.setLoadWithOverviewMode(true);
        settings.setAllowContentAccess(true);
        settings.setGeolocationEnabled(true);
        settings.setAllowFileAccess(true);
        settings.setAllowContentAccess(true);
        settings.setDomStorageEnabled(true);

        settings.setSupportZoom(false);
        settings.setBuiltInZoomControls(true);
        settings.setDisplayZoomControls(true);

        settings.setBlockNetworkImage(false);
        settings.setLoadsImagesAutomatically(true);

        webView.setFitsSystemWindows(true);

        spinnerCaseApi(case_id_spinner);

        ramada.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String url = baseURL.BaseURL_API+"services/ramada/?volunteer_id=" + vounter_id + "&case_id=" + CaseID;
//                ramada.setVisibility(View.GONE);
//                workstation.setVisibility(View.GONE);
//                webView.setVisibility(View.VISIBLE);
//                webView.loadUrl(url);
                Intent i = new Intent(CaseReportActivity.this,WebViewActivity.class);
                i.putExtra("type","volunteer");
                i.putExtra("url",url);
                startActivity(i);

            }
        });

        workstation.setOnClickListener(v -> {
//            ramada.setVisibility(View.GONE);
//            workstation.setVisibility(View.GONE);
//            webView.setVisibility(View.VISIBLE);
//            webView.loadUrl(baseURL.BaseURL_API+"services/wokstation?volunteer_id=" + vounter_id + "&case_id=" + CaseID);
            Intent i = new Intent(CaseReportActivity.this,WebViewActivity.class);
            i.putExtra("type","volunteer");
            i.putExtra("url",baseURL.BaseURL_API+"services/wokstation?volunteer_id=" + vounter_id + "&case_id=" + CaseID);
            startActivity(i);

        });
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        dialog.getWindow().setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);

        if (!CaseReportActivity.this.isFinishing() && !CaseReportActivity.this.isDestroyed()) {
            dialog.show();
        }
    }
    private void openLanguageDialog() {
        binding.drawerLayout.closeDrawer(GravityCompat.START);
        final android.app.AlertDialog.Builder alert;
        alert = new android.app.AlertDialog.Builder(CaseReportActivity.this);
        final LayoutInflater inflater = LayoutInflater.from(CaseReportActivity.this);
        View alertLayout = inflater.inflate(R.layout.language_support_dialog, null);
        ImageView closeImg2 = alertLayout.findViewById(R.id.closeBtn2);
        Spinner spinner = alertLayout.findViewById(R.id.spinner2);
        Spinner case_id_spinner = alertLayout.findViewById(R.id.case_id_spinner);
        EditText message = alertLayout.findViewById(R.id.editTextTextMultiLine);
        CardView sendRequest = alertLayout.findViewById(R.id.sendRequest);

        alert.setView(alertLayout);
        alert.setCancelable(false);
        final android.app.AlertDialog dialog = alert.create();
        dialog.setCanceledOnTouchOutside(false);
        closeImg2.setOnClickListener(v -> dialog.dismiss());

        spinnerApi(spinner,message);
        spinnerCaseApi1(case_id_spinner);

        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        sendRequest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                hitSendLanguageRequest(Language,CaseID1,message.getText().toString(),"Language");
                dialog.dismiss();
            }
        });

        if (!CaseReportActivity.this.isFinishing() && !CaseReportActivity.this.isDestroyed()) {
            dialog.show();
        }
    }
    private void openBackupDialog() {
        binding.drawerLayout.closeDrawer(GravityCompat.START);
        final android.app.AlertDialog.Builder alert;
        alert = new android.app.AlertDialog.Builder(CaseReportActivity.this);
        final LayoutInflater inflater = LayoutInflater.from(CaseReportActivity.this);
        View alertLayout = inflater.inflate(R.layout.backup_support_dialog, null);
        ImageView closeImg2 = alertLayout.findViewById(R.id.closeBtn2);
        Spinner case_id_spinner = alertLayout.findViewById(R.id.case_id_spinner);
        EditText message = alertLayout.findViewById(R.id.editTextTextMultiLine);
        CardView sendRequest = alertLayout.findViewById(R.id.sendRequest);
        CheckBox police = alertLayout.findViewById(R.id.Police);
        CheckBox Hospital = alertLayout.findViewById(R.id.Hospital);
        CheckBox Court = alertLayout.findViewById(R.id.Court);

//        List<String> support = new ArrayList<String>();

        alert.setView(alertLayout);
        alert.setCancelable(false);
        final android.app.AlertDialog dialog = alert.create();
        dialog.setCanceledOnTouchOutside(false);
        closeImg2.setOnClickListener(v -> dialog.dismiss());

        spinnerCaseApi1(case_id_spinner);
        StringBuilder support = new StringBuilder();

        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        sendRequest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(police.isChecked()){
                    support.append("Police, ");
                }
                if(Hospital.isChecked()){
                    support.append("Hospital, ");
                }
                if(Court.isChecked()){
                    support.append("Court");
                }
//                supportStr = new String()
//                for(String s : support){
//                    supportStr += s;
//                }
//                Log.d(TAG, "openBackupDialog: "+ support.toString());
//                Toast.makeText(VolunteerHome.this, "supportStr "+ support, Toast.LENGTH_SHORT).show();
                if(support.toString().isEmpty()||support.toString().equals("")||message.getText().toString().isEmpty()||message.getText().toString().equals("")){
                    Toast.makeText(CaseReportActivity.this, "Fill the form", Toast.LENGTH_SHORT).show();
                }else {
                    hitSendLanguageRequest(" ",CaseID1,message.getText().toString(),support.toString());
                }



                dialog.dismiss();
            }
        });

        if (!CaseReportActivity.this.isFinishing() && !CaseReportActivity.this.isDestroyed()) {
            dialog.show();
        }
    }
    private void spinnerApi(Spinner spinner, EditText editText) {
        api api = retrofit.retrofit.create(api.class);
        Call<List<AdditionalLanguageModel>> resource = api.getAdditionalLanguage();
        resource.enqueue(new Callback<List<AdditionalLanguageModel>>() {
            @Override
            public void onResponse(Call<List<AdditionalLanguageModel>> call, Response<List<AdditionalLanguageModel>> response) {
                if (response.isSuccessful()) {
                    List<AdditionalLanguageModel> resData = response.body();
                    List<AdditionalLanguageSpinnerAdaptor> location = new ArrayList<>();
                    for (AdditionalLanguageModel resource_location : resData) {
                        AdditionalLanguageSpinnerAdaptor adaptor = new AdditionalLanguageSpinnerAdaptor(resource_location.getLname(), resource_location.getWclId());
                        location.add(adaptor);

                        ArrayAdapter<AdditionalLanguageSpinnerAdaptor> arrayAdapter = new ArrayAdapter<>(CaseReportActivity.this, android.R.layout.simple_spinner_item, location);
                        arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

                        spinner.setAdapter(arrayAdapter);

                        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                            @Override
                            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                                AdditionalLanguageSpinnerAdaptor spinnerAdaptor = (AdditionalLanguageSpinnerAdaptor) parent.getSelectedItem();
//                                Toast.makeText(VolunterResourceActivity.this, "ID : "+spinnerAdaptor.getId()+"\n" +
//                                        "\nLocation : "+spinnerAdaptor.getLocation(), Toast.LENGTH_SHORT).show();

                                editText.setText("There is an immediate language need in "+ spinnerAdaptor.getLanguage() +" language. Can you back up XXXX and take over this case right now ?");

                                Language = spinnerAdaptor.getLanguage();
                            }

                            @Override
                            public void onNothingSelected(AdapterView<?> parent) {

                            }
                        });

                    }

                } else {
                    Toast.makeText(CaseReportActivity.this, "Something went wrong (OR) check your internet connection...!", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<List<AdditionalLanguageModel>> call, Throwable t) {
                Toast.makeText(CaseReportActivity.this, "Something went wrong (OR) check your internet connection...!\n"+t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
    private void spinnerCaseApi(Spinner spinner) {
        api api = retrofit.retrofit.create(api.class);
        Call<ChannelListModel> call = api.getChannelList();
        call.enqueue(new Callback<ChannelListModel>() {
            @Override
            public void onResponse(Call<ChannelListModel> call, Response<ChannelListModel> response) {
                if(response.isSuccessful() && response.code() == 200 && response.body() != null && response.body().getData() != null && response.body().getData().size() > 0){
                    List<ChannelListModel.Datum> resData = response.body().getData();
                    List<CaseIDSpinnerAdaptor> channelData = new ArrayList<>();
                    for(ChannelListModel.Datum channel: resData){
                        CaseIDSpinnerAdaptor adaptor = new CaseIDSpinnerAdaptor(channel.getCaseId());
                        channelData.add(adaptor);

                        ArrayAdapter<CaseIDSpinnerAdaptor> adaptorArrayAdapter = new ArrayAdapter<>(CaseReportActivity.this, android.R.layout.simple_spinner_item, channelData);
                        adaptorArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                        spinner.setAdapter(adaptorArrayAdapter);

                        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                            @Override
                            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                                CaseIDSpinnerAdaptor spinnerAdaptor =  (CaseIDSpinnerAdaptor) adapterView.getSelectedItem();
                                CaseID = spinnerAdaptor.getLanguage();
                            }

                            @Override
                            public void onNothingSelected(AdapterView<?> adapterView) {

                            }
                        });

                    }

                }else{
                    Toast.makeText(CaseReportActivity.this, "Something went wrong (OR) check your internet connection...!", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<ChannelListModel> call, Throwable t) {
                Toast.makeText(CaseReportActivity.this, "Something went wrong (OR) check your internet connection...!", Toast.LENGTH_SHORT).show();
            }
        });
    }
    private void spinnerCaseApi1(Spinner spinner) {

        api api = retrofit.retrofit.create(api.class);
        Call<ChannelListModel> call = api.getChannelList();
        call.enqueue(new Callback<ChannelListModel>() {
            @Override
            public void onResponse(Call<ChannelListModel> call, Response<ChannelListModel> response) {
                if(response.isSuccessful() && response.code() == 200 && response.body() != null && response.body().getData() != null && response.body().getData().size() > 0){
                    List<ChannelListModel.Datum> resData = response.body().getData();
                    List<CaseIDSpinnerAdaptor> channelData = new ArrayList<>();
                    for(ChannelListModel.Datum channel: resData){
                        CaseIDSpinnerAdaptor adaptor = new CaseIDSpinnerAdaptor(channel.getCaseId());
                        channelData.add(adaptor);

                        ArrayAdapter<CaseIDSpinnerAdaptor> adaptorArrayAdapter = new ArrayAdapter<>(CaseReportActivity.this, android.R.layout.simple_spinner_item, channelData);
                        adaptorArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                        spinner.setAdapter(adaptorArrayAdapter);

                        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                            @Override
                            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                                CaseIDSpinnerAdaptor spinnerAdaptor =  (CaseIDSpinnerAdaptor) adapterView.getSelectedItem();
                                CaseID1 = spinnerAdaptor.getLanguage();
                            }

                            @Override
                            public void onNothingSelected(AdapterView<?> adapterView) {

                            }
                        });

                    }

                }else{
                    Toast.makeText(CaseReportActivity.this, "Something went wrong (OR) check your internet connection...!", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<ChannelListModel> call, Throwable t) {
                Toast.makeText(CaseReportActivity.this, "Something went wrong (OR) check your internet connection...!", Toast.LENGTH_SHORT).show();
            }
        });
    }
    private void hitSendLanguageRequest(String Language, String caseID, String message,String support) {
        Log.d(TAG, "hitSendLanguageRequest: "+Language+"  "+caseID+ "   "+message+ "   "+support);
        api api = retrofit.retrofit.create(api.class);
        Call<LanguageRequestModel> call = api.LanguageRequest(Language,caseID,message,support);
        call.enqueue(new Callback<LanguageRequestModel>() {
            @Override
            public void onResponse(Call<LanguageRequestModel> call, Response<LanguageRequestModel> response) {
                if (response.isSuccessful()) {
                    assert response.body() != null;
                    if(response.body().getStatus().equals("valid")){
                        Toast.makeText(getApplicationContext(), "Request Sent Successfully ", Toast.LENGTH_SHORT).show();
                    }else {
                        Toast.makeText(getApplicationContext(), "Request failed ", Toast.LENGTH_SHORT).show();
                    }
                    Log.d(TAG, "response Status : " + response.body().getStatus());
                } else {
                    Log.d(TAG, "err : " + response.errorBody());
                    Toast.makeText(getApplicationContext(), response.errorBody().toString(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<LanguageRequestModel> call, Throwable t) {
                Log.d("sssss", "err " + t.getMessage());
                StyleableToast.makeText(CaseReportActivity.this, t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void hitHelpRequestApi() {
//        showActivityIndicator("Loading Announcements");

        caseDialog = new Dialog(this);

        api api = retrofit.retrofit.create(api.class);
        Call<ChannelListModel> call = api.getChannelList();

        call.enqueue(new Callback<ChannelListModel>() {
            @Override
            public void onResponse(Call<ChannelListModel> call, Response<ChannelListModel> response) {
                if(response.isSuccessful()){
                    channelList.clear();
                    channelList.addAll(response.body().getData());
                    caseListAdaptor = new CaseListAdaptor(CaseReportActivity.this,channelList,CaseReportActivity.this);
                    openHelpListDialog(caseListAdaptor,caseDialog);
                }else {
                    Log.d("ressss", "err : " + response.errorBody());
                }
            }

            @Override
            public void onFailure(Call<ChannelListModel> call, Throwable t) {
                Log.d("ressss", "err : " + t);
            }
        });

//        api api = retrofit.retrofit.create(api.class);
//        Call<List<HelpRequestModel>> call = api.helpvictimlist();
//        call.enqueue(new Callback<List<HelpRequestModel>>() {
//            @Override
//            public void onResponse(Call<List<HelpRequestModel>> call, Response<List<HelpRequestModel>> response) {
//                if (response.isSuccessful()) {
//                    if (response.body().size() != 0) {
//                        helpRequestModels = new ArrayList<>(response.body());
//                        dialog1 = new Dialog(VolunteerHome.this);
//                        helpRequestListAdaptor = new HelpRequestListAdaptor(helpRequestModels, VolunteerHome.this, dialog1, vounter_id);
//                        openHelpListDialog(helpRequestListAdaptor, dialog1);
//                    }
//
//                } else {
//                    Toast.makeText(CaseReportActivity.this, "err : " + response.errorBody(), Toast.LENGTH_SHORT).show();
//                    Log.d("ressss", "err : " + response.errorBody());
//                }
//            }
//
//            @Override
//            public void onFailure(Call<List<HelpRequestModel>> call, Throwable t) {
////                stopActivityIndicator();
//                Log.d("ressss", "err : " + t.getMessage());
//                Toast.makeText(CaseReportActivity.this, "err : " + t.getMessage(), Toast.LENGTH_SHORT).show();
//            }
//        });
    }

    private void openHelpListDialog(CaseListAdaptor caseListAdaptor, Dialog dialog1) {
        binding.drawerLayout.closeDrawer(GravityCompat.START);
        dialog1.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog1.setCancelable(false);
        dialog1.setContentView(R.layout.help_request_list_dialog);
        ImageView closeImg2 = dialog1.findViewById(R.id.closeBtn2);
        RecyclerView rv_helplist = dialog1.findViewById(R.id.rv_helplist);
        LinearLayout list_items = dialog1.findViewById(R.id.list_items);
        LinearLayout accept = dialog1.findViewById(R.id.accept);
        TextView status2 = dialog1.findViewById(R.id.status2);
        list_items.setVisibility(View.VISIBLE);
        status2.setVisibility(View.GONE);
        accept.setVisibility(View.GONE);
        rv_helplist.setLayoutManager(new LinearLayoutManager(this));
        rv_helplist.setAdapter(caseListAdaptor);
        closeImg2.setVisibility(View.GONE);
        closeImg2.setOnClickListener(v -> {
            dialog1.dismiss();
            Singleton.get().setDialogDisplay(false);
        });
        dialog1.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog1.getWindow().setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);
        if (!CaseReportActivity.this.isFinishing() && !CaseReportActivity.this.isDestroyed()) {
            dialog1.show();
        }
    }

    @Override
    public void onClick(String value) {
//        Toast.makeText(this, "value "+value, Toast.LENGTH_SHORT).show();
//        CaseID = value;
        //todo onClick open status dialog
        binding.wvCaseReport.loadUrl(baseURL.BaseURL+"apis/webview/casereport?case_id="+value+"&volunteer="+vounter_id);
//        Toast.makeText(this, baseURL.BaseURL+"apis/webview/casereport?case_id="+value+"&volunteer="+vounter_id, Toast.LENGTH_SHORT).show();
        caseDialog.dismiss();
    }
}