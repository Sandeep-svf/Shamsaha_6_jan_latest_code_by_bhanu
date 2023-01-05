package com.shamsaha.app.activity.volunteer;

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
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
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
import com.shamsaha.app.ApiModel.volunter.MessageModel;
import com.shamsaha.app.ApiModel.volunter.ProfileInfoModel;
import com.shamsaha.app.ApiModel.volunter.ScheduleStatusModel;
import com.shamsaha.app.ApiModel.volunter.ShiftRequestMode;
import com.shamsaha.app.R;
import com.shamsaha.app.activity.volunteer.Resources.VolunterResourceActivity;
import com.shamsaha.app.activity.volunteer.VolEvent.VolEventsActivity;
import com.shamsaha.app.adaptor.AdditionalLanguageSpinnerAdaptor;
import com.shamsaha.app.adaptor.CaseIDSpinnerAdaptor;
import com.shamsaha.app.adaptor.Volunteer.ShiftRequestListAdaptor;
import com.shamsaha.app.api.api;
import com.shamsaha.app.retrofitAdaptor.retrofit;
import com.shamsaha.app.utils.ConstantsURL.baseURL;

import org.infideap.drawerbehavior.AdvanceDrawerLayout;

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

public class ShiftRequestListActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    private static final String TAG = "abc";
    boolean doubleBackToExitPressedOnce = false;
    ArrayList<ShiftRequestMode> dataModel = new ArrayList<>();
    private AdvanceDrawerLayout drawer;
    private String vounter_id;
    private ImageView handBurger, profilePic, back, iv_gold, iv_platinum;
    private RecyclerView recyclerview;
    private String dutyStatus = "0";
    private Button btn_duty_status;
    private String VolunteerName;
    private String total_rewards;
    private String profile;
    private TextView name, rewardTv;
    private Dialog dialog;
    private LinearLayout ll_reward;
    private FingerprintManager fingerprintManager = null;
    private ShiftRequestListAdaptor shiftRequestListAdaptor;
    private boolean isResourceClicked = false;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shift_list);

        getData();
        hitStatusAPi();
        hitProfileApi();

        drawer = findViewById(R.id.drawer_layout);
        handBurger = findViewById(R.id.handBurger);
        btn_duty_status = findViewById(R.id.btn_duty_status);
        profilePic = findViewById(R.id.profilePic);
        back = findViewById(R.id.back);
        recyclerview = findViewById(R.id.recyclerview);

        recyclerview.setLayoutManager(new LinearLayoutManager(this));

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawer, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        ImageView navigationView_ = navigationView.getHeaderView(0).findViewById(R.id.nav_icon);
        TextView tv_Name = navigationView.getHeaderView(0).findViewById(R.id.tv_Name);
        Glide.with(ShiftRequestListActivity.this).load(profile).placeholder(R.drawable.ic_smalllogo).into(navigationView_);
        tv_Name.setText(VolunteerName);
        drawer.setViewScale(Gravity.START, 0.9f);
        drawer.setRadius(Gravity.START, 35);
        drawer.setViewElevation(Gravity.START, 20);

        handBurger.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                drawer.openDrawer(GravityCompat.START);

            }
        });

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                onBackPressed();

            }
        });

        hitShiftRequestApi(vounter_id);

    }


    private void hitShiftRequestApi(String volunteer_id) {
        api api = retrofit.retrofit.create(api.class);
        Call<List<ShiftRequestMode>> call = api.request_list(volunteer_id);
        call.enqueue(new Callback<List<ShiftRequestMode>>() {
            @Override
            public void onResponse(@NonNull Call<List<ShiftRequestMode>> call, @NonNull Response<List<ShiftRequestMode>> response) {
                if (response.isSuccessful() && response.body()!=null) {
                    List<ShiftRequestMode> requestModes = response.body();
                    Log.i(TAG, "onResponse: "+requestModes.get(0).getStatus());
                    if (requestModes.get(0).getStatus().equals("valid")){
                          dataModel = new ArrayList<>(response.body());
                    shiftRequestListAdaptor = new ShiftRequestListAdaptor(dataModel,ShiftRequestListActivity.this,volunteer_id);
                    }else {
                        shiftRequestListAdaptor = new ShiftRequestListAdaptor(new ArrayList<>(),ShiftRequestListActivity.this,volunteer_id);
                    }
                    recyclerview.setAdapter(shiftRequestListAdaptor);

                }
            }

            @Override
            public void onFailure(Call<List<ShiftRequestMode>> call, Throwable t) {
                api api = retrofit.retrofit.create(api.class);
                Call<MessageModel> callMessage = api.request_list_message(volunteer_id);
                callMessage.enqueue(new Callback<MessageModel>() {
                    @Override
                    public void onResponse(Call<MessageModel> call, Response<MessageModel> response) {
                        StyleableToast.makeText(ShiftRequestListActivity.this, response.body().getMessage(), Toast.LENGTH_LONG, R.style.mytoast).show();
                    }

                    @Override
                    public void onFailure(Call<MessageModel> call, Throwable te) {
                        StyleableToast.makeText(ShiftRequestListActivity.this, t.getMessage () + "\n Check Internet Connection..!"
                                , Toast.LENGTH_LONG, R.style.mytoast).show();
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
                drawer.closeDrawer(GravityCompat.START);
                shiftRequest();
                return true;

            case R.id.settings:
                drawer.closeDrawer(GravityCompat.START);
                settingsDialog();
                return true;

            case R.id.outstandingRequests:
                drawer.closeDrawer(GravityCompat.START);
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
        drawer.closeDrawer(GravityCompat.START);
    }

    private void resource() {
        Intent i = new Intent(getApplicationContext(), VolunterResourceActivity.class);
        i.putExtra("vounter_id", vounter_id);
        i.putExtra("profile", profile);
        i.putExtra("VolunteerName", VolunteerName);
        startActivity(i);
        drawer.closeDrawer(GravityCompat.START);
    }

    String CaseID ;
    String CaseID1 ;
    String Language ;

    public void openServicesDialog() {
        drawer.closeDrawer(GravityCompat.START);
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
                String url = baseURL.BaseURL_API+"services/ramada/?volunteer_id=" + vounter_id + "&case_id=" +CaseID;
                ramada.setVisibility(View.GONE);
                workstation.setVisibility(View.GONE);
                webView.setVisibility(View.VISIBLE);
                webView.loadUrl(url);
            }
        });

        workstation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String url = baseURL.BaseURL_API+"services/wokstation?volunteer_id=" + vounter_id + "&case_id=" +CaseID;
                ramada.setVisibility(View.GONE);
                workstation.setVisibility(View.GONE);
                webView.setVisibility(View.VISIBLE);
                webView.loadUrl(url);
            }
        });

        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        dialog.getWindow().setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);

        if (!ShiftRequestListActivity.this.isFinishing() && !ShiftRequestListActivity.this.isDestroyed()) {
            dialog.show();
        }
    }
    private void openLanguageDialog() {
        drawer.closeDrawer(GravityCompat.START);
        final android.app.AlertDialog.Builder alert;
        alert = new android.app.AlertDialog.Builder(ShiftRequestListActivity.this);
        final LayoutInflater inflater = LayoutInflater.from(ShiftRequestListActivity.this);
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

        if (!ShiftRequestListActivity.this.isFinishing() && !ShiftRequestListActivity.this.isDestroyed()) {
            dialog.show();
        }
    }
    private void openBackupDialog() {
        drawer.closeDrawer(GravityCompat.START);
        final android.app.AlertDialog.Builder alert;
        alert = new android.app.AlertDialog.Builder(ShiftRequestListActivity.this);
        final LayoutInflater inflater = LayoutInflater.from(ShiftRequestListActivity.this);
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
                    Toast.makeText(ShiftRequestListActivity.this, "Fill the form", Toast.LENGTH_SHORT).show();
                }else {
                    hitSendLanguageRequest(" ",CaseID1,message.getText().toString(),support.toString());
                }



                dialog.dismiss();
            }
        });

        if (!ShiftRequestListActivity.this.isFinishing() && !ShiftRequestListActivity.this.isDestroyed()) {
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

                        ArrayAdapter<AdditionalLanguageSpinnerAdaptor> arrayAdapter = new ArrayAdapter<>(ShiftRequestListActivity.this, android.R.layout.simple_spinner_item, location);
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
                    Toast.makeText(ShiftRequestListActivity.this, "Something went wrong (OR) check your internet connection...!", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<List<AdditionalLanguageModel>> call, Throwable t) {
                Toast.makeText(ShiftRequestListActivity.this, "Something went wrong (OR) check your internet connection...!\n"+t.getMessage(), Toast.LENGTH_SHORT).show();
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

                        ArrayAdapter<CaseIDSpinnerAdaptor> adaptorArrayAdapter = new ArrayAdapter<>(ShiftRequestListActivity.this, android.R.layout.simple_spinner_item, channelData);
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
                    Toast.makeText(ShiftRequestListActivity.this, "Something went wrong (OR) check your internet connection...!", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<ChannelListModel> call, Throwable t) {
                Toast.makeText(ShiftRequestListActivity.this, "Something went wrong (OR) check your internet connection...!", Toast.LENGTH_SHORT).show();
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

                        ArrayAdapter<CaseIDSpinnerAdaptor> adaptorArrayAdapter = new ArrayAdapter<>(ShiftRequestListActivity.this, android.R.layout.simple_spinner_item, channelData);
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
                    Toast.makeText(ShiftRequestListActivity.this, "Something went wrong (OR) check your internet connection...!", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<ChannelListModel> call, Throwable t) {
                Toast.makeText(ShiftRequestListActivity.this, "Something went wrong (OR) check your internet connection...!", Toast.LENGTH_SHORT).show();
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
                StyleableToast.makeText(ShiftRequestListActivity.this, t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
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

    private void settingsDialog() {
        dialog = new Dialog(this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(false);
        dialog.setContentView(R.layout.dialog_setting);
        ImageView i = dialog.findViewById(R.id.iv_dialog_close);
        Switch switch1 = dialog.findViewById(R.id.switch1);
        LinearLayout ll_change_password = dialog.findViewById(R.id.ll_change_password);

        boolean a = loadData();

        if(a){
            switch1.setChecked(true);
        }else {
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
                if(on){

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

                }else {
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


    private void VolunterLogin() {
        Intent i = new Intent(getApplicationContext(), loginActivity.class);
        startActivity(i);
        finish();
        drawer.closeDrawer(GravityCompat.START);
    }

    private void changePassword() {
        Intent i = new Intent(getApplicationContext(), ChangePasswordActivity.class);
        i.putExtra("vounter_id",vounter_id);
        i.putExtra("profile",profile);
        i.putExtra("VolunteerName",VolunteerName);
        startActivity(i);
//        finish();
//        VolunterLogin();
    }

    private void VolunteerHome() {
        Intent i = new Intent(getApplicationContext(), VolunteerHome.class);
        i.putExtra("vounter_id",vounter_id);
        i.putExtra("profile",profile);
        i.putExtra("VolunteerName",VolunteerName);
        startActivity(i);
        finish();
        drawer.closeDrawer(GravityCompat.START);
    }

    private void profile() {
        Intent i = new Intent(getApplicationContext(), ProfileActivity.class);
        i.putExtra("vounter_id",vounter_id);
        i.putExtra("profile",profile);
        i.putExtra("VolunteerName",VolunteerName);
        startActivity(i);
        finish();
        drawer.closeDrawer(GravityCompat.START);
    }

    private void Shift() {
        Intent i = new Intent(getApplicationContext(), ShiftActivity.class);
        i.putExtra("vounter_id",vounter_id);
        i.putExtra("profile",profile);
        i.putExtra("name",VolunteerName);
        startActivity(i);
        finish();
        drawer.closeDrawer(GravityCompat.START);
    }

    private void event() {
        Intent i = new Intent(getApplicationContext(), VolEventsActivity.class);
        i.putExtra("vounter_id",vounter_id);
        i.putExtra("profile",profile);
        i.putExtra("VolunteerName",VolunteerName);
        startActivity(i);
        finish();
        drawer.closeDrawer(GravityCompat.START);
        drawer.closeDrawer(GravityCompat.START);
    }

    private void upcomingSgift() {
        Intent i = new Intent(getApplicationContext(), UpcomingShiftActivity.class);
        i.putExtra("vounter_id",vounter_id);
        i.putExtra("profile",profile);
        i.putExtra("VolunteerName",VolunteerName);
        startActivity(i);
        drawer.closeDrawer(GravityCompat.START);
    }

    private void shiftRequest() {
        Intent i = new Intent(getApplicationContext(), ShiftRequestListActivity.class);
        i.putExtra("vounter_id",vounter_id);
        i.putExtra("profile",profile);
        i.putExtra("VolunteerName",VolunteerName);
        startActivity(i);
        drawer.closeDrawer(GravityCompat.START);
    }

    private void caseReport() {
        Intent i = new Intent(getApplicationContext(), CaseReportActivity.class);
        i.putExtra("vounter_id",vounter_id);
        i.putExtra("profile",profile);
        i.putExtra("VolunteerName",VolunteerName);
        startActivity(i);
        drawer.closeDrawer(GravityCompat.START);
    }

    private void getData() {

        Intent intent = getIntent();
        vounter_id = intent.getStringExtra("vounter_id");
        profile = intent.getStringExtra("profile");
        VolunteerName = intent.getStringExtra("VolunteerName");
        total_rewards = intent.getStringExtra("total_rewards");
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
                StyleableToast.makeText(ShiftRequestListActivity.this, t.getMessage(), Toast.LENGTH_SHORT).show();

            }
        });
    }

    private void setDutyStatus(String dutyStatus) {
        if (dutyStatus.equals("1")) {
            btn_duty_status.setBackgroundResource(R.drawable.ic_duty_btn);
            btn_duty_status.setText("ON DUTY");
            btn_duty_status.setVisibility(View.VISIBLE);
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
                        total_rewards = infoModel.getTotalRewards();
                        profile = infoModel.getProfilePic();
                    }
                    setView(profile, VolunteerName, total_rewards);
//                    setRewards(total_rewards);
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

    private void setView(String profile, String VolunteerName, String total_rewards) {
        //hitProfileApi();
        Glide.with(getApplicationContext()).load(profile).placeholder(R.drawable.ic_smalllogo).into(profilePic);
//        name.setText(VolunteerName);
//        setRewards(total_rewards);
    }


}