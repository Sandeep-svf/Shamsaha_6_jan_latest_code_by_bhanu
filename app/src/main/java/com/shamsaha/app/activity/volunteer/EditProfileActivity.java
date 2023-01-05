package com.shamsaha.app.activity.volunteer;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.hardware.fingerprint.FingerprintManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
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
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.github.dhaval2404.imagepicker.ImagePicker;
import com.google.android.material.navigation.NavigationView;
import com.muddzdev.styleabletoast.StyleableToast;
import com.shamsaha.app.ApiModel.ChannelListModel;
import com.shamsaha.app.ApiModel.volunter.AdditionalLanguageModel;
import com.shamsaha.app.ApiModel.volunter.LanguageRequestModel;
import com.shamsaha.app.ApiModel.volunter.ProfilePic;
import com.shamsaha.app.ApiModel.volunter.ScheduleStatusModel;
import com.shamsaha.app.ApiModel.volunter.UpdateprofileModel;
import com.shamsaha.app.R;
import com.shamsaha.app.activity.volunteer.Resources.VolunterResourceActivity;
import com.shamsaha.app.activity.volunteer.VolEvent.VolEventsActivity;
import com.shamsaha.app.activity.volunteer.onduty.DutyShiftActivity1;
import com.shamsaha.app.adaptor.AdditionalLanguageSpinnerAdaptor;
import com.shamsaha.app.adaptor.CaseIDSpinnerAdaptor;
import com.shamsaha.app.api.api;
import com.shamsaha.app.retrofitAdaptor.retrofit;
import com.shamsaha.app.utils.ConstantsURL.baseURL;

import org.infideap.drawerbehavior.AdvanceDrawerLayout;

import java.io.File;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.content.ContextCompat;
import androidx.core.view.GravityCompat;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class EditProfileActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private static final String TAG = "abc";
    boolean doubleBackToExitPressedOnce = false;
    private AdvanceDrawerLayout drawer;

    final Calendar myCalendar = Calendar.getInstance();

    private ImageView handBurger, back;

    private String vounter_id;
    private String profile;
    private String VolunteerName;
    private String total_rewards;
    private String phone;
    private String whatsapp;
    private String address;
    private String otherLanguage;
    private String dob;
    private String dutyStatus = "0";
    private String path;
    private File file;

    private EditText editName;
    private EditText editPhone;
    private EditText editWhatsapp;
    private EditText editAddress;
    private EditText editDob;
    private EditText editLang;

    private ImageView profilePic, iv_silver, iv_gold, iv_platinum;
    private Button btn_duty_status, Submit;
    private TextView name, rewardTv;

    private Dialog dialog;
    private FingerprintManager fingerprintManager = null;
    private DatePickerDialog.OnDateSetListener mDateSetListener;

    private boolean isResourceClicked = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);

        drawer = findViewById(R.id.drawer_layout);

        handBurger = findViewById(R.id.handBurger);
        back = findViewById(R.id.back);

        Submit = findViewById(R.id.button);

        name = findViewById(R.id.name);
        rewardTv = findViewById(R.id.rewardTv);

        iv_silver = findViewById(R.id.iv_silver);
        iv_gold = findViewById(R.id.iv_gold);
        iv_platinum = findViewById(R.id.iv_platinum);
        btn_duty_status = findViewById(R.id.btn_duty_status);

        profilePic = findViewById(R.id.profilePic);
        editName = findViewById(R.id.editName);
        editPhone = findViewById(R.id.editPhone);
        editWhatsapp = findViewById(R.id.editWhatsapp);
        editAddress = findViewById(R.id.editAddress);
        editDob = findViewById(R.id.editDob);
        editLang = findViewById(R.id.editLang);


        getData();
        hitStatusAPi();
        setView();

        editDob.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar calendar = Calendar.getInstance();
                int year = calendar.get(Calendar.YEAR);
                int month = calendar.get(Calendar.MONTH);
                int day = calendar.get(Calendar.DAY_OF_MONTH);

                DatePickerDialog dialog = new DatePickerDialog(
                        EditProfileActivity.this,
                        android.R.style.Theme_Holo_Light_Dialog_MinWidth,
                        mDateSetListener, year, month + 1, day);
                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                dialog.show();
            }
        });

        btn_duty_status.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onDuty();
            }
        });

        mDateSetListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                int mnth = month + 1;
                editDob.setText(year + "/" + mnth
                        + "/" + dayOfMonth);
            }
        };


        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawer, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        DatePickerDialog.OnDateSetListener date = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                myCalendar.set(Calendar.YEAR, year);
                myCalendar.set(Calendar.MONTH, month);
                myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);

            }
        };


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

        profilePic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ImagePicker.Companion.with(EditProfileActivity.this)
                        .crop()                    //Crop image(Optional), Check Customization for more option
                        .compress(1024)            //Final image size will be less than 1 MB(Optional)
                        .maxResultSize(800, 800)    //Final image resolution will be less than 1080 x 1080(Optional)
                        .saveDir(new File(Environment.getExternalStorageDirectory(), "ImagePicker"))
                        .start();


            }
        });

        Submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//
                try {
                    hitApiUplode();
                    hitApiImageUplode();
                    onBackPressed();
                } catch (Exception e) {
                    hitApiUplode();
                    onBackPressed();
                }
            }
        });

        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        ImageView navigationView_ = navigationView.getHeaderView(0).findViewById(R.id.nav_icon);
        TextView tv_Name = navigationView.getHeaderView(0).findViewById(R.id.tv_Name);
        Glide.with(EditProfileActivity.this).load(profile).placeholder(R.drawable.ic_smalllogo).into(navigationView_);
        tv_Name.setText(VolunteerName);
        drawer.setViewScale(Gravity.START, 0.9f);
        drawer.setRadius(Gravity.START, 35);
        drawer.setViewElevation(Gravity.START, 20);
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

    private void shiftRequest() {
        Intent i = new Intent(getApplicationContext(), ShiftRequestListActivity.class);
        i.putExtra("vounter_id", vounter_id);
        i.putExtra("profile", profile);
        i.putExtra("VolunteerName", VolunteerName);
        startActivity(i);
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

    private void upcomingSgift() {
        Intent i = new Intent(getApplicationContext(), UpcomingShiftActivity.class);
        i.putExtra("vounter_id", vounter_id);
        i.putExtra("profile", profile);
        i.putExtra("VolunteerName", VolunteerName);
        startActivity(i);
        drawer.closeDrawer(GravityCompat.START);
    }

    private void caseReport() {
        Intent i = new Intent(getApplicationContext(), CaseReportActivity.class);
        i.putExtra("vounter_id", vounter_id);
        i.putExtra("profile", profile);
        i.putExtra("VolunteerName", VolunteerName);
        startActivity(i);
        drawer.closeDrawer(GravityCompat.START);
    }

    String CaseID;
    String CaseID1;
    String Language;

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
                ramada.setVisibility(View.GONE);
                workstation.setVisibility(View.GONE);
                webView.setVisibility(View.VISIBLE);
                webView.loadUrl(baseURL.BaseURL_API + "services/ramada/?volunteer_id=" + vounter_id + "&case_id=" + CaseID);
            }
        });

        workstation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ramada.setVisibility(View.GONE);
                workstation.setVisibility(View.GONE);
                webView.setVisibility(View.VISIBLE);
                webView.loadUrl(baseURL.BaseURL_API + "services/wokstation?volunteer_id=" + vounter_id + "&case_id=" + CaseID);
            }
        });

        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        dialog.getWindow().setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);

        if (!EditProfileActivity.this.isFinishing() && !EditProfileActivity.this.isDestroyed()) {
            dialog.show();
        }
    }

    private void openLanguageDialog() {
        drawer.closeDrawer(GravityCompat.START);
        final android.app.AlertDialog.Builder alert;
        alert = new android.app.AlertDialog.Builder(EditProfileActivity.this);
        final LayoutInflater inflater = LayoutInflater.from(EditProfileActivity.this);
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

        spinnerApi(spinner, message);
        spinnerCaseApi1(case_id_spinner);

        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        sendRequest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                hitSendLanguageRequest(Language, CaseID1, message.getText().toString(), "Language");
                dialog.dismiss();
            }
        });

        if (!EditProfileActivity.this.isFinishing() && !EditProfileActivity.this.isDestroyed()) {
            dialog.show();
        }
    }

    private void openBackupDialog() {
        drawer.closeDrawer(GravityCompat.START);
        final android.app.AlertDialog.Builder alert;
        alert = new android.app.AlertDialog.Builder(EditProfileActivity.this);
        final LayoutInflater inflater = LayoutInflater.from(EditProfileActivity.this);
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
                if (police.isChecked()) {
                    support.append("Police, ");
                }
                if (Hospital.isChecked()) {
                    support.append("Hospital, ");
                }
                if (Court.isChecked()) {
                    support.append("Court");
                }
//                supportStr = new String()
//                for(String s : support){
//                    supportStr += s;
//                }
//                Log.d(TAG, "openBackupDialog: "+ support.toString());
//                Toast.makeText(VolunteerHome.this, "supportStr "+ support, Toast.LENGTH_SHORT).show();
                if (support.toString().isEmpty() || support.toString().equals("") || message.getText().toString().isEmpty() || message.getText().toString().equals("")) {
                    Toast.makeText(EditProfileActivity.this, "Fill the form", Toast.LENGTH_SHORT).show();
                } else {
                    hitSendLanguageRequest(" ", CaseID1, message.getText().toString(), support.toString());
                }


                dialog.dismiss();
            }
        });

        if (!EditProfileActivity.this.isFinishing() && !EditProfileActivity.this.isDestroyed()) {
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

                        ArrayAdapter<AdditionalLanguageSpinnerAdaptor> arrayAdapter = new ArrayAdapter<>(EditProfileActivity.this, android.R.layout.simple_spinner_item, location);
                        arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

                        spinner.setAdapter(arrayAdapter);

                        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                            @Override
                            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                                AdditionalLanguageSpinnerAdaptor spinnerAdaptor = (AdditionalLanguageSpinnerAdaptor) parent.getSelectedItem();
//                                Toast.makeText(VolunterResourceActivity.this, "ID : "+spinnerAdaptor.getId()+"\n" +
//                                        "\nLocation : "+spinnerAdaptor.getLocation(), Toast.LENGTH_SHORT).show();

                                editText.setText("There is an immediate language need in " + spinnerAdaptor.getLanguage() + " language. Can you back up XXXX and take over this case right now ?");

                                Language = spinnerAdaptor.getLanguage();
                            }

                            @Override
                            public void onNothingSelected(AdapterView<?> parent) {

                            }
                        });

                    }

                } else {
                    Toast.makeText(EditProfileActivity.this, "Something went wrong (OR) check your internet connection...!", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<List<AdditionalLanguageModel>> call, Throwable t) {
                Toast.makeText(EditProfileActivity.this, "Something went wrong (OR) check your internet connection...!\n" + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void spinnerCaseApi(Spinner spinner) {
        api api = retrofit.retrofit.create(api.class);
        Call<ChannelListModel> call = api.getChannelList();
        call.enqueue(new Callback<ChannelListModel>() {
            @Override
            public void onResponse(Call<ChannelListModel> call, Response<ChannelListModel> response) {
                if (response.isSuccessful() && response.code() == 200 && response.body() != null && response.body().getData() != null && response.body().getData().size() > 0) {
                    List<ChannelListModel.Datum> resData = response.body().getData();
                    List<CaseIDSpinnerAdaptor> channelData = new ArrayList<>();
                    for (ChannelListModel.Datum channel : resData) {
                        CaseIDSpinnerAdaptor adaptor = new CaseIDSpinnerAdaptor(channel.getCaseId());
                        channelData.add(adaptor);

                        ArrayAdapter<CaseIDSpinnerAdaptor> adaptorArrayAdapter = new ArrayAdapter<>(EditProfileActivity.this, android.R.layout.simple_spinner_item, channelData);
                        adaptorArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                        spinner.setAdapter(adaptorArrayAdapter);

                        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                            @Override
                            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                                CaseIDSpinnerAdaptor spinnerAdaptor = (CaseIDSpinnerAdaptor) adapterView.getSelectedItem();
                                CaseID = spinnerAdaptor.getLanguage();
                            }

                            @Override
                            public void onNothingSelected(AdapterView<?> adapterView) {

                            }
                        });

                    }

                } else {
                    Toast.makeText(EditProfileActivity.this, "Something went wrong (OR) check your internet connection...!", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<ChannelListModel> call, Throwable t) {
                Toast.makeText(EditProfileActivity.this, "Something went wrong (OR) check your internet connection...!", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void spinnerCaseApi1(Spinner spinner) {

        api api = retrofit.retrofit.create(api.class);
        Call<ChannelListModel> call = api.getChannelList();
        call.enqueue(new Callback<ChannelListModel>() {
            @Override
            public void onResponse(Call<ChannelListModel> call, Response<ChannelListModel> response) {
                if (response.isSuccessful() && response.code() == 200 && response.body() != null && response.body().getData() != null && response.body().getData().size() > 0) {
                    List<ChannelListModel.Datum> resData = response.body().getData();
                    List<CaseIDSpinnerAdaptor> channelData = new ArrayList<>();
                    for (ChannelListModel.Datum channel : resData) {
                        CaseIDSpinnerAdaptor adaptor = new CaseIDSpinnerAdaptor(channel.getCaseId());
                        channelData.add(adaptor);

                        ArrayAdapter<CaseIDSpinnerAdaptor> adaptorArrayAdapter = new ArrayAdapter<>(EditProfileActivity.this, android.R.layout.simple_spinner_item, channelData);
                        adaptorArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                        spinner.setAdapter(adaptorArrayAdapter);

                        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                            @Override
                            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                                CaseIDSpinnerAdaptor spinnerAdaptor = (CaseIDSpinnerAdaptor) adapterView.getSelectedItem();
                                CaseID1 = spinnerAdaptor.getLanguage();
                            }

                            @Override
                            public void onNothingSelected(AdapterView<?> adapterView) {

                            }
                        });

                    }

                } else {
                    Toast.makeText(EditProfileActivity.this, "Something went wrong (OR) check your internet connection...!", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<ChannelListModel> call, Throwable t) {
                Toast.makeText(EditProfileActivity.this, "Something went wrong (OR) check your internet connection...!", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void hitSendLanguageRequest(String Language, String caseID, String message, String support) {
        Log.d(TAG, "hitSendLanguageRequest: " + Language + "  " + caseID + "   " + message + "   " + support);
        api api = retrofit.retrofit.create(api.class);
        Call<LanguageRequestModel> call = api.LanguageRequest(Language, caseID, message, support);
        call.enqueue(new Callback<LanguageRequestModel>() {
            @Override
            public void onResponse(Call<LanguageRequestModel> call, Response<LanguageRequestModel> response) {
                if (response.isSuccessful()) {
                    assert response.body() != null;
                    if (response.body().getStatus().equals("valid")) {
                        Toast.makeText(getApplicationContext(), "Request Sent Successfully ", Toast.LENGTH_SHORT).show();
                    } else {
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
                StyleableToast.makeText(EditProfileActivity.this, t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
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

        Intent i = new Intent(getApplicationContext(), VolEventsActivity.class);
        i.putExtra("vounter_id", vounter_id);
        i.putExtra("profile", profile);
        i.putExtra("VolunteerName", VolunteerName);
        startActivity(i);
        drawer.closeDrawer(GravityCompat.START);
        drawer.closeDrawer(GravityCompat.START);

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

    private void onDuty() {
        Intent intent = new Intent(EditProfileActivity.this, DutyShiftActivity1.class);
        intent.putExtra("volID", vounter_id);
        intent.putExtra("volID1", "on_duty");
        startActivity(intent);
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
//        finish();
//        VolunterLogin();
    }

    private void VolunterLogin() {

        Intent i = new Intent(getApplicationContext(), loginActivity.class);
        startActivity(i);
        finish();
        drawer.closeDrawer(GravityCompat.START);

    }

    private void getData() {
        Intent intent = getIntent();
        vounter_id = intent.getStringExtra("vounter_id");
        profile = intent.getStringExtra("profile");
        VolunteerName = intent.getStringExtra("VolunteerName");
        total_rewards = intent.getStringExtra("total_rewards");
        phone = intent.getStringExtra("phone");
        whatsapp = intent.getStringExtra("whatsapp");
        address = intent.getStringExtra("address");
        otherLanguage = intent.getStringExtra("otherLanguage");
        dob = intent.getStringExtra("dob");
        dutyStatus = intent.getStringExtra("dutyStatus");
    }

    private void setView() {

        Glide.with(getApplicationContext()).load(profile).placeholder(R.drawable.ic_smalllogo).into(profilePic);

        if (dutyStatus.equals("1")) {
            btn_duty_status.setBackgroundResource(R.drawable.ic_duty_btn);
            btn_duty_status.setText("ON DUTY");
        } else {
            btn_duty_status.setVisibility(View.INVISIBLE);
        }

        name.setText(VolunteerName);
        editName.setText(VolunteerName);
        setRewards(Integer.parseInt(total_rewards));
        editPhone.setText(phone);
        editWhatsapp.setText(whatsapp);
        editAddress.setText(address);
        editDob.setText(dob);
        editLang.setText(otherLanguage);

    }

    private void setRewards(int rewards) {
        if (rewards >= 0 && rewards <= 6) {
            rewardTv.setText("Beginner ");
            iv_silver.setColorFilter(ContextCompat.getColor(this, R.color.colorLiteGray), android.graphics.PorterDuff.Mode.MULTIPLY);
            iv_gold.setColorFilter(ContextCompat.getColor(this, R.color.colorLiteGray), android.graphics.PorterDuff.Mode.MULTIPLY);
            iv_platinum.setColorFilter(ContextCompat.getColor(this, R.color.colorLiteGray), android.graphics.PorterDuff.Mode.MULTIPLY);
        } else if (rewards >= 7 && rewards <= 20) {

            rewardTv.setText("Silver ");
            iv_silver.setColorFilter(ContextCompat.getColor(this, R.color.colorPink), android.graphics.PorterDuff.Mode.MULTIPLY);
            iv_gold.setColorFilter(ContextCompat.getColor(this, R.color.colorLiteGray), android.graphics.PorterDuff.Mode.MULTIPLY);
            iv_platinum.setColorFilter(ContextCompat.getColor(this, R.color.colorLiteGray), android.graphics.PorterDuff.Mode.MULTIPLY);

        } else if (rewards >= 21 && rewards <= 30) {

            rewardTv.setText("Gold ");
            iv_silver.setColorFilter(ContextCompat.getColor(this, R.color.colorPink), android.graphics.PorterDuff.Mode.MULTIPLY);
            iv_gold.setColorFilter(ContextCompat.getColor(this, R.color.colorPink), android.graphics.PorterDuff.Mode.MULTIPLY);
            iv_platinum.setColorFilter(ContextCompat.getColor(this, R.color.colorLiteGray), android.graphics.PorterDuff.Mode.MULTIPLY);

        } else if (rewards > 30) {

            rewardTv.setText("Platinum ");
            iv_silver.setColorFilter(ContextCompat.getColor(this, R.color.colorPink), android.graphics.PorterDuff.Mode.MULTIPLY);
            iv_gold.setColorFilter(ContextCompat.getColor(this, R.color.colorPink), android.graphics.PorterDuff.Mode.MULTIPLY);
            iv_platinum.setColorFilter(ContextCompat.getColor(this, R.color.colorPink), android.graphics.PorterDuff.Mode.MULTIPLY);

        } else {
            iv_silver.setColorFilter(ContextCompat.getColor(this, R.color.colorLiteGray), android.graphics.PorterDuff.Mode.MULTIPLY);
            iv_gold.setColorFilter(ContextCompat.getColor(this, R.color.colorLiteGray), android.graphics.PorterDuff.Mode.MULTIPLY);
            iv_platinum.setColorFilter(ContextCompat.getColor(this, R.color.colorLiteGray), android.graphics.PorterDuff.Mode.MULTIPLY);
            StyleableToast.makeText(this, "Something went wrong\nContact Admin", Toast.LENGTH_SHORT, R.style.mytoast).show();

        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == Activity.RESULT_OK) {

            path = ImagePicker.Companion.getFilePath(data);
            Log.d("ddddddddddd", "path \n" + path);

        } else if (resultCode == ImagePicker.RESULT_ERROR) {

            Toast.makeText(this, ImagePicker.Companion.getError(data), Toast.LENGTH_SHORT).show();

        } else {
            Toast.makeText(this, "Task Cancelled", Toast.LENGTH_SHORT).show();
        }

    }

    private void hitApiImageUplode() {
        api api = retrofit.retrofit.create(api.class);
        file = new File(path);
        RequestBody requestBody = RequestBody.create(MediaType.parse("multipart/form-data"), file);
        MultipartBody.Part body = MultipartBody.Part.createFormData("profile_pic", file.getName(), requestBody);
        RequestBody volunteer_id1 = RequestBody.create(MediaType.parse("multipart/form-data"), vounter_id);
        Call<ProfilePic> call = api.Volunteer_pic_update(volunteer_id1, body);
        call.enqueue(new Callback<ProfilePic>() {
            @Override
            public void onResponse(Call<ProfilePic> call, Response<ProfilePic> response) {
                if (!response.isSuccessful()) {
                    Toast.makeText(EditProfileActivity.this, "response : " + response.errorBody(), Toast.LENGTH_SHORT).show();
                    Log.d("response", "response: " + response.errorBody());
                }
                Log.d("response", "response: " + response.body().getMessage());
                StyleableToast.makeText(EditProfileActivity.this, "Profile Pic Updated Successfully "
                        , Toast.LENGTH_LONG, R.style.mytoast).show();
            }

            @Override
            public void onFailure(Call<ProfilePic> call, Throwable t) {
                Log.d("respons", t.getMessage());
                StyleableToast.makeText(EditProfileActivity.this, t.getMessage() + "\n Check Internet Connection..!"
                        , Toast.LENGTH_LONG, R.style.mytoast).show();
            }
        });


    }


    private void hitApiUplode() {

        VolunteerName = editName.getText().toString();
        phone = editPhone.getText().toString();
        whatsapp = editWhatsapp.getText().toString();
        address = editAddress.getText().toString();
        dob = editDob.getText().toString();
        otherLanguage = editLang.getText().toString();

        api api = retrofit.retrofit.create(api.class);
        Call<UpdateprofileModel> call = api.volunteer_info_update(vounter_id, VolunteerName, phone, address, whatsapp, otherLanguage, dob);
        call.enqueue(new Callback<UpdateprofileModel>() {
            @Override
            public void onResponse(Call<UpdateprofileModel> call, Response<UpdateprofileModel> response) {
                if (response.isSuccessful()) {
                    assert response.body() != null;
                    StyleableToast.makeText(EditProfileActivity.this, response.body().getMessage(), Toast.LENGTH_SHORT, R.style.mytoast).show();
                    Log.d("SSSSSS", "res: " + response.body().getMessage());
                } else {
                    StyleableToast.makeText(EditProfileActivity.this, response.errorBody().toString(), Toast.LENGTH_SHORT, R.style.mytoast).show();
                    Log.d("SSSSSS", "res: " + response.errorBody().toString());
                }
            }

            @Override
            public void onFailure(Call<UpdateprofileModel> call, Throwable t) {
                StyleableToast.makeText(EditProfileActivity.this, t.getMessage(), Toast.LENGTH_SHORT, R.style.mytoast).show();
            }
        });

    }


    private void hitStatusAPi() {
//        showActivityIndicator("Loading Duty Status");
        api api = retrofit.retrofit.create(api.class);
        Call<ScheduleStatusModel> call = api.ScheduleStatusModel(vounter_id);
        call.enqueue(new Callback<ScheduleStatusModel>() {
            @Override
            public void onResponse(Call<ScheduleStatusModel> call, Response<ScheduleStatusModel> response) {
//                stopActivityIndicator();
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
//                stopActivityIndicator();
                Log.d("sssss", "err " + t.getMessage());
                StyleableToast.makeText(EditProfileActivity.this, t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void setDutyStatus(String dutyStatus) {
        if (dutyStatus.equals("1")) {
            btn_duty_status.setBackgroundResource(R.drawable.ic_duty_btn);
            btn_duty_status.setText("ON DUTY");
            btn_duty_status.setVisibility(View.VISIBLE);
        } else {
            btn_duty_status.setVisibility(View.GONE);
        }
    }

}