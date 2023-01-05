package com.shamsaha.app.activity.volunteer;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.hardware.fingerprint.FingerprintManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
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
import com.makeramen.roundedimageview.RoundedImageView;
import com.muddzdev.styleabletoast.StyleableToast;
import com.shamsaha.app.ApiModel.ChannelListModel;
import com.shamsaha.app.ApiModel.volunter.AdditionalLanguageModel;
import com.shamsaha.app.ApiModel.volunter.LanguageRequestModel;
import com.shamsaha.app.ApiModel.volunter.ProfileInfoModel;
import com.shamsaha.app.ApiModel.volunter.ScheduleStatusModel;
import com.shamsaha.app.ApiModel.volunter.UpcomingShift;
import com.shamsaha.app.R;
import com.shamsaha.app.activity.volunteer.Resources.VolunterResourceActivity;
import com.shamsaha.app.activity.volunteer.VolEvent.VolEventsActivity;
import com.shamsaha.app.adaptor.AdditionalLanguageSpinnerAdaptor;
import com.shamsaha.app.adaptor.CaseIDSpinnerAdaptor;
import com.shamsaha.app.api.api;
import com.shamsaha.app.retrofitAdaptor.retrofit;
import com.shamsaha.app.utils.ConstantsURL.baseURL;
import com.shamsaha.app.utils.dateConversion;

import org.infideap.drawerbehavior.AdvanceDrawerLayout;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.content.ContextCompat;
import androidx.core.view.GravityCompat;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ProfileActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private static final String TAG = "abc";
    boolean doubleBackToExitPressedOnce = false;
    private AdvanceDrawerLayout drawer;
    private ImageView handBurger, profilePic, iv_silver, iv_gold, iv_platinum, imageView18;
    private TextView name, rewardTv, tv_deate, tv_language, tv_time, tv_upcoming_shifts;
    private LinearLayout ll_reward;

    private ImageView iv_contact;
    private LinearLayout ll_phone, ll_whatsapp, ll_email;
    private TextView tv_phoneNumber, tv_whatsapp, tv_email;
    private CardView cv_contact;
    private boolean isContactClicked = false;

    private CardView cv_address;
    private LinearLayout ll_address;
    private ImageView Address_iv;
    private TextView tv_Address;
    private boolean isAddressClicked = false;

    private TextView passport_tv;

    private CardView cv_language;
    private LinearLayout ll_Official, ll_additional;
    private ImageView Language_iv;
    private TextView tv_o_language, tv_o_additional;
    private boolean isLanguageClicked = false;


    private CardView cv_date;
    private CardView cardView;
    private LinearLayout ll_doj, ll_dob;
    private ImageView Date_iv;
    private TextView tv_doj_date, tv_o_dob;
    private boolean isDateClicked = false;

    private String vounter_id;
    private String profile;
    private String VolunteerName;
    private String total_rewards;

    private Button btn_duty_status;

    private String phone = "+xxx-xxxxxx";
    private String whatsapp = "+xxx-xxxxxx";
    private String email = "abcd@abc.com";
    private String address = "Loading...!";
    private String passport = "xxx xxx xxx";
    private String officialLanguage = "Loading...!";
    private String otherLanguage = "Loading...!";
    private String doj = "Loading...!";
    private String dob = "Loading...!";
    private String dutyStatus = "0";

    private RoundedImageView iv_updatedShift;


    private Dialog dialog;

    private FingerprintManager fingerprintManager = null;

    boolean isResourceClicked = false;


    @Override
    public void onBackPressed() {

        if (drawer.isDrawerOpen(GravityCompat.START)) {
            //handBurger.setImageResource(R.drawable.ic_arrow_back_black_24dp);
            drawer.closeDrawer(GravityCompat.START);
        } else {

            if (doubleBackToExitPressedOnce) {
                super.onBackPressed();
                return;
            }

            this.doubleBackToExitPressedOnce = true;
            Toast.makeText(this, "Please click BACK again to exit", Toast.LENGTH_SHORT).show();

            new Handler().postDelayed(new Runnable() {

                @Override
                public void run() {
                    doubleBackToExitPressedOnce = false;
                }
            }, 2000);
            //super.onBackPressed();
        }

    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);


        drawer = findViewById(R.id.drawer_layout);
        handBurger = findViewById(R.id.handBurger);
        profilePic = findViewById(R.id.profilePic);
        name = findViewById(R.id.name);
        iv_silver = findViewById(R.id.iv_silver);
        iv_gold = findViewById(R.id.iv_gold);
        iv_platinum = findViewById(R.id.iv_platinum);
        rewardTv = findViewById(R.id.rewardTv);
        ll_reward = findViewById(R.id.ll_reward);

        //Contact
        iv_contact = findViewById(R.id.iv_contact);
        ll_phone = findViewById(R.id.ll_phone);
        ll_whatsapp = findViewById(R.id.ll_whatsapp);
        ll_email = findViewById(R.id.ll_email);
        tv_phoneNumber = findViewById(R.id.tv_phoneNumber);
        tv_whatsapp = findViewById(R.id.tv_whatsapp);
        tv_email = findViewById(R.id.tv_email);
        cv_contact = findViewById(R.id.cv_contact);

        //Address
        cv_address = findViewById(R.id.cv_address);
        ll_address = findViewById(R.id.ll_address);
        Address_iv = findViewById(R.id.Address_iv);
        tv_Address = findViewById(R.id.tv_Address);

        passport_tv = findViewById(R.id.passport_tv);

        //Language
        cv_language = findViewById(R.id.cv_language);
        ll_Official = findViewById(R.id.ll_Official);
        ll_additional = findViewById(R.id.ll_additional);
        Language_iv = findViewById(R.id.Language_iv);
        tv_o_language = findViewById(R.id.tv_o_language);
        tv_o_additional = findViewById(R.id.tv_o_additional);


        //Date
        cv_date = findViewById(R.id.cv_date);
        cardView = findViewById(R.id.cardView);
        ll_doj = findViewById(R.id.ll_doj);
        ll_dob = findViewById(R.id.ll_dob);
        Date_iv = findViewById(R.id.Date_iv);
        tv_doj_date = findViewById(R.id.tv_doj_date);
        tv_o_dob = findViewById(R.id.tv_o_dob);

        imageView18 = findViewById(R.id.imageView18);

        btn_duty_status = findViewById(R.id.btn_duty_status);

        iv_updatedShift = findViewById(R.id.iv_updatedShift);
        tv_deate = findViewById(R.id.tv_deate);
        tv_language = findViewById(R.id.tv_language);
        tv_time = findViewById(R.id.tv_time);
        tv_upcoming_shifts = findViewById(R.id.upcoming_shifts);


        getData();
        hitProfileApi();
        hitStatusAPi();
        hitUpcommingShift();
        setView();
        contactClose();
        addressClose();
        languageClose();
        dateClose();

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            fingerprintManager = (FingerprintManager) this.getSystemService(Context.FINGERPRINT_SERVICE);
        }

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawer, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        handBurger.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                drawer.openDrawer(GravityCompat.START);

            }
        });

        ll_reward.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                openDialog();

            }
        });

        cv_contact.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (!isContactClicked) {
                    contactOpen();
                    isContactClicked = true;
                } else {
                    contactClose();
                    isContactClicked = false;
                }


            }
        });

        cv_address.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (!isAddressClicked) {
                    addressOpen();
                    isAddressClicked = true;
                } else {
                    addressClose();
                    isAddressClicked = false;
                }


            }
        });

        cv_language.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (!isLanguageClicked) {
                    languageOpen();
                    isLanguageClicked = true;
                } else {
                    languageClose();
                    isLanguageClicked = false;
                }


            }
        });

        iv_updatedShift.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                upcomingSgift();
            }
        });

        cv_date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (!isDateClicked) {
                    dateOpen();
                    isDateClicked = true;
                } else {
                    dateClose();
                    isDateClicked = false;
                }


            }
        });


        imageView18.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(ProfileActivity.this, EditProfileActivity.class);
                intent.putExtra("vounter_id", vounter_id);
                intent.putExtra("profile", profile);
                intent.putExtra("VolunteerName", VolunteerName);
                intent.putExtra("total_rewards", total_rewards);
                intent.putExtra("phone", phone);
                intent.putExtra("whatsapp", whatsapp);
                intent.putExtra("address", address);
                intent.putExtra("otherLanguage", otherLanguage);
                intent.putExtra("dob", dob);
                intent.putExtra("dutyStatus", dutyStatus);
                startActivity(intent);

            }
        });

        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        ImageView navigationView_ = navigationView.getHeaderView(0).findViewById(R.id.nav_icon);
        TextView tv_Name = navigationView.getHeaderView(0).findViewById(R.id.tv_Name);
        Glide.with(ProfileActivity.this).load(profile).placeholder(R.drawable.ic_smalllogo).into(navigationView_);
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


    private void caseReport() {
        Intent i = new Intent(getApplicationContext(), CaseReportActivity.class);
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
                String url = baseURL.BaseURL_API + "services/ramada/?volunteer_id=" + vounter_id + "&case_id=" + CaseID;
                ramada.setVisibility(View.GONE);
                workstation.setVisibility(View.GONE);
                webView.setVisibility(View.VISIBLE);
                webView.loadUrl(url);
            }
        });

        workstation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String url = baseURL.BaseURL_API + "services/wokstation?volunteer_id=" + vounter_id + "&case_id=" + CaseID;
                ramada.setVisibility(View.GONE);
                workstation.setVisibility(View.GONE);
                webView.setVisibility(View.VISIBLE);
                webView.loadUrl(url);
            }
        });

        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        dialog.getWindow().setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);

        if (!ProfileActivity.this.isFinishing() && !ProfileActivity.this.isDestroyed()) {
            dialog.show();
        }
    }

    private void openLanguageDialog() {
        drawer.closeDrawer(GravityCompat.START);
        final android.app.AlertDialog.Builder alert;
        alert = new android.app.AlertDialog.Builder(ProfileActivity.this);
        final LayoutInflater inflater = LayoutInflater.from(ProfileActivity.this);
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

        if (!ProfileActivity.this.isFinishing() && !ProfileActivity.this.isDestroyed()) {
            dialog.show();
        }
    }

    private void openBackupDialog() {
        drawer.closeDrawer(GravityCompat.START);
        final android.app.AlertDialog.Builder alert;
        alert = new android.app.AlertDialog.Builder(ProfileActivity.this);
        final LayoutInflater inflater = LayoutInflater.from(ProfileActivity.this);
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
                    Toast.makeText(ProfileActivity.this, "Fill the form", Toast.LENGTH_SHORT).show();
                } else {
                    hitSendLanguageRequest(" ", CaseID1, message.getText().toString(), support.toString());
                }


                dialog.dismiss();
            }
        });

        if (!ProfileActivity.this.isFinishing() && !ProfileActivity.this.isDestroyed()) {
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

                        ArrayAdapter<AdditionalLanguageSpinnerAdaptor> arrayAdapter = new ArrayAdapter<>(ProfileActivity.this, android.R.layout.simple_spinner_item, location);
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
                    Toast.makeText(ProfileActivity.this, "Something went wrong (OR) check your internet connection...!", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<List<AdditionalLanguageModel>> call, Throwable t) {
                Toast.makeText(ProfileActivity.this, "Something went wrong (OR) check your internet connection...!\n" + t.getMessage(), Toast.LENGTH_SHORT).show();
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

                        ArrayAdapter<CaseIDSpinnerAdaptor> adaptorArrayAdapter = new ArrayAdapter<>(ProfileActivity.this, android.R.layout.simple_spinner_item, channelData);
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
                    Toast.makeText(ProfileActivity.this, "Something went wrong (OR) check your internet connection...!", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<ChannelListModel> call, Throwable t) {
                Toast.makeText(ProfileActivity.this, "Something went wrong (OR) check your internet connection...!", Toast.LENGTH_SHORT).show();
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

                        ArrayAdapter<CaseIDSpinnerAdaptor> adaptorArrayAdapter = new ArrayAdapter<>(ProfileActivity.this, android.R.layout.simple_spinner_item, channelData);
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
                    Toast.makeText(ProfileActivity.this, "Something went wrong (OR) check your internet connection...!", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<ChannelListModel> call, Throwable t) {
                Toast.makeText(ProfileActivity.this, "Something went wrong (OR) check your internet connection...!", Toast.LENGTH_SHORT).show();
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
                StyleableToast.makeText(ProfileActivity.this, t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void shiftRequest() {
        Intent i = new Intent(getApplicationContext(), ShiftRequestListActivity.class);
        i.putExtra("vounter_id", vounter_id);
        i.putExtra("profile", profile);
        i.putExtra("VolunteerName", VolunteerName);
        startActivity(i);
        drawer.closeDrawer(GravityCompat.START);
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

    private void upcomingSgift() {

        Intent i = new Intent(getApplicationContext(), UpcomingShiftActivity.class);
        i.putExtra("vounter_id", vounter_id);
        i.putExtra("profile", profile);
        i.putExtra("name", VolunteerName);
        startActivity(i);
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


    private void getData() {
        Intent intent = getIntent();
        vounter_id = intent.getStringExtra("vounter_id");
        profile = intent.getStringExtra("profile");
        VolunteerName = intent.getStringExtra("VolunteerName");
        total_rewards = intent.getStringExtra("total_rewards");
    }

    private void setView() {

        Glide.with(getApplicationContext()).load(profile).placeholder(R.drawable.ic_smalllogo).into(profilePic);

        if (dutyStatus.equals("1")) {
            btn_duty_status.setBackgroundResource(R.drawable.ic_duty_btn);
            btn_duty_status.setText("ON DUTY");
        } else {
            btn_duty_status.setVisibility(View.INVISIBLE);
        }

        tv_phoneNumber.setText(phone);
        tv_whatsapp.setText(whatsapp);
        tv_email.setText(email);

        tv_Address.setText(address);

        tv_o_language.setText(officialLanguage);
        tv_o_additional.setText(otherLanguage);
        tv_doj_date.setText(doj);
        tv_o_dob.setText(dob);
        passport_tv.setText(passport);


        name.setText(VolunteerName);
        //setRewards(total_rewards);


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
                        phone = infoModel.getVmobile();
                        whatsapp = infoModel.getWhatsapp();
                        email = infoModel.getVemail();
                        address = infoModel.getAddress();
                        passport = infoModel.getPassportRCpr();
                        officialLanguage = infoModel.getShiftLanguage();
                        otherLanguage = infoModel.getLanguageKnown();
                        doj = infoModel.getDoj();
                        dob = infoModel.getDob();
                    }

                    setView();
                    setRewards(Integer.parseInt(total_rewards));


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

    private void hitStatusAPi() {

        api api = retrofit.retrofit.create(api.class);
        Call<ScheduleStatusModel> call = api.ScheduleStatusModel(vounter_id);
        call.enqueue(new Callback<ScheduleStatusModel>() {
            @Override
            public void onResponse(Call<ScheduleStatusModel> call, Response<ScheduleStatusModel> response) {
                if (response.isSuccessful()) {
                    assert response.body() != null;
                    dutyStatus = response.body().getMessage();
                    setView();
                } else {
                    Log.d("sssss", "err : " + response.errorBody());
                }
            }

            @Override
            public void onFailure(Call<ScheduleStatusModel> call, Throwable t) {
                Log.d("sssss", "err " + t.getMessage());
            }
        });

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

    private void openDialog() {
        dialog = new Dialog(this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(false);
        dialog.setContentView(R.layout.reward_dialog);
        ImageView i = dialog.findViewById(R.id.iv_dialog_close);
        i.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        dialog.show();
        dialog.getWindow().setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);
    }

    private void contactClose() {
        //iv_contact.setBackgroundResource();
        Glide.with(getApplicationContext()).load(R.drawable.ic_down_aero).into(iv_contact);
        ll_email.setVisibility(View.GONE);
        ll_whatsapp.setVisibility(View.GONE);
        ll_phone.setVisibility(View.GONE);
    }

    private void contactOpen() {

        addressClose();
        languageClose();
        dateClose();

        Glide.with(getApplicationContext()).load(R.drawable.ic_icon_awesome_minus).into(iv_contact);
        //iv_contact.setBackgroundResource(R.drawable.ic_icon_awesome_minus);
        ll_email.setVisibility(View.VISIBLE);
        ll_whatsapp.setVisibility(View.VISIBLE);
        ll_phone.setVisibility(View.VISIBLE);


    }

    private void addressClose() {
        //iv_contact.setBackgroundResource();
        Glide.with(getApplicationContext()).load(R.drawable.ic_down_aero).into(Address_iv);
        ll_address.setVisibility(View.GONE);
    }

    private void addressOpen() {
        contactClose();
        languageClose();
        dateClose();
        Glide.with(getApplicationContext()).load(R.drawable.ic_icon_awesome_minus).into(Address_iv);
        ll_address.setVisibility(View.VISIBLE);
    }

    private void languageClose() {

        Glide.with(getApplicationContext()).load(R.drawable.ic_down_aero).into(Language_iv);
        ll_Official.setVisibility(View.GONE);
        ll_additional.setVisibility(View.GONE);

    }

    private void languageOpen() {
        addressClose();
        contactClose();
        dateClose();
        Glide.with(getApplicationContext()).load(R.drawable.ic_icon_awesome_minus).into(Language_iv);
        ll_Official.setVisibility(View.VISIBLE);
        ll_additional.setVisibility(View.VISIBLE);

    }

    private void dateClose() {


        Glide.with(getApplicationContext()).load(R.drawable.ic_down_aero).into(Date_iv);
        ll_doj.setVisibility(View.GONE);
        ll_dob.setVisibility(View.GONE);

    }

    private void dateOpen() {

        addressClose();
        languageClose();
        contactClose();
        Glide.with(getApplicationContext()).load(R.drawable.ic_icon_awesome_minus).into(Date_iv);
        ll_doj.setVisibility(View.VISIBLE);
        ll_dob.setVisibility(View.VISIBLE);

    }

    @Override
    protected void onResume() {
        super.onResume();
        hitProfileApi();
    }

    private void hitUpcommingShift() {
//        showActivityIndicator("Loading Upcoming Shift");
        api api = retrofit.retrofit.create(com.shamsaha.app.api.api.class);
        Call<List<UpcomingShift>> call = api.Volunteer_upcoming_shift(vounter_id);
        call.enqueue(new Callback<List<UpcomingShift>>() {
            @Override
            public void onResponse(Call<List<UpcomingShift>> call, Response<List<UpcomingShift>> response) {
//                stopActivityIndicator();
                if (response.isSuccessful()) {
                    List<UpcomingShift> body = response.body();
                    for (UpcomingShift s : body) {
                        cardView.setVisibility(View.VISIBLE);
                        String pic = s.getImage();
                        String sDate = s.getDate();
                        String BackColor = s.getColor();
                        dateConversion d = new dateConversion();
                        try {
                            String dd = d.dateConversion(sDate, "yyyy-MM-dd");
                            dd = dd.replace("-", " ");
                            tv_deate.setText(dd);
                        } catch (ParseException e) {
                            e.printStackTrace();
                        }
                        tv_language.setText(s.getShiftLanguage());
                        tv_time.setText(s.getShiftTime());
                        Glide.with(getApplicationContext()).load(pic).into(iv_updatedShift);

                        if (BackColor.equals("light")) {
                            tv_deate.setTextColor(Color.parseColor("#ffffff"));
                            tv_language.setTextColor(Color.parseColor("#ffffff"));
                            tv_time.setTextColor(Color.parseColor("#ffffff"));
                            tv_upcoming_shifts.setTextColor(Color.parseColor("#ffffff"));
                        }
                    }
                } else {
                    Toast.makeText(ProfileActivity.this, "err: " + response.errorBody(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<List<UpcomingShift>> call, Throwable t) {
//                stopActivityIndicator();
//                Toast.makeText(VolunteerHome.this, "err  : " + t.getMessage(), Toast.LENGTH_SHORT).show();
                cardView.setVisibility(View.GONE);
            }
        });
    }

//    private void hitUpcommingShift() {
//
//        api api = retrofit.retrofit.create(api.class);
//        Call<List<UpcomingShift>> call = api.Volunteer_upcoming_shift(vounter_id);
//        call.enqueue(new Callback<List<UpcomingShift>>() {
//            @Override
//            public void onResponse(Call<List<UpcomingShift>> call, Response<List<UpcomingShift>> response) {
//                if (response.isSuccessful()) {
//                    List<UpcomingShift> body = response.body();
//                    for (UpcomingShift s : body) {
//                        cardView.setVisibility(View.VISIBLE);
//                        String pic = s.getImage();
//                        String sDate = s.getDate();
//
//                        dateConversion d = new dateConversion();
//                        try {
//                            String dd = d.dateConversion(sDate, "yyyy-MM-dd");
//                            dd= dd.replace("-"," ");
//                            tv_deate.setText(dd);
//                            // Toast.makeText(VolunteerHome.this, "date " + dd, Toast.LENGTH_SHORT).show();
//                        } catch (ParseException e) {
//                            e.printStackTrace();
//                        }
//
//                        tv_language.setText(s.getShiftLanguage());
//                        tv_time.setText(s.getShiftTime());
//
//                        Glide.with(getApplicationContext()).load(pic).placeholder(R.drawable.ic_smalllogo).into(iv_updatedShift);
//
//                    }
//
//                } else {
//                    Toast.makeText(getApplicationContext(), "err: " + response.errorBody(), Toast.LENGTH_SHORT).show();
//                }
//            }
//
//            @Override
//            public void onFailure(Call<List<UpcomingShift>> call, Throwable t) {
////                Toast.makeText(getApplicationContext(), "err  : " + t.getMessage(), Toast.LENGTH_SHORT).show();
//                cardView.setVisibility(View.GONE);
//            }
//        });
//    }

    private void settingsDialog() {
        dialog = new Dialog(this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(false);
        dialog.setContentView(R.layout.dialog_setting);
        ImageView i = dialog.findViewById(R.id.iv_dialog_close);
        Switch switch1 = dialog.findViewById(R.id.switch1);

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


}