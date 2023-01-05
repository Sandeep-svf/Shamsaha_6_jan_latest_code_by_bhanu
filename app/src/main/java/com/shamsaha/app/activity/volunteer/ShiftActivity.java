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
import android.widget.ProgressBar;
import android.widget.ScrollView;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.applandeo.materialcalendarview.CalendarView;
import com.applandeo.materialcalendarview.EventDay;
import com.applandeo.materialcalendarview.listeners.OnCalendarPageChangeListener;
import com.applandeo.materialcalendarview.listeners.OnDayClickListener;
import com.bumptech.glide.Glide;
import com.google.android.material.navigation.NavigationView;
import com.google.gson.JsonObject;
import com.muddzdev.styleabletoast.StyleableToast;
import com.shamsaha.app.ApiModel.ChannelListModel;
import com.shamsaha.app.ApiModel.volunter.AdditionalLanguageModel;
import com.shamsaha.app.ApiModel.volunter.LanguageRequestModel;
import com.shamsaha.app.ApiModel.volunter.MyShift;
import com.shamsaha.app.ApiModel.volunter.OnDateShift;
import com.shamsaha.app.ApiModel.volunter.OpenShift;
import com.shamsaha.app.ApiModel.volunter.ProfileInfoModel;
import com.shamsaha.app.ApiModel.volunter.ScheduleStatusModel;
import com.shamsaha.app.ApiModel.volunter.SheduleListModel;
import com.shamsaha.app.ApiModel.volunter.UpcomingShift;
import com.shamsaha.app.R;
import com.shamsaha.app.activity.volunteer.Resources.VolunterResourceActivity;
import com.shamsaha.app.activity.volunteer.VolEvent.VolEventsActivity;
import com.shamsaha.app.activity.volunteer.onduty.DutyShiftActivity1;
import com.shamsaha.app.adaptor.AdditionalLanguageSpinnerAdaptor;
import com.shamsaha.app.adaptor.CaseIDSpinnerAdaptor;
import com.shamsaha.app.adaptor.Volunteer.CommonItemSpaceDecoration;
import com.shamsaha.app.adaptor.Volunteer.OnDateShiftAdaptor;
import com.shamsaha.app.adaptor.Volunteer.ShiftListParentAdaptor;
import com.shamsaha.app.adaptor.Volunteer.UpcomingShiftAdaptor;
import com.shamsaha.app.api.api;
import com.shamsaha.app.retrofitAdaptor.retrofit;
import com.shamsaha.app.utils.ConstantsURL.baseURL;
import com.shamsaha.app.utils.DateToDateConversion;

import org.infideap.drawerbehavior.AdvanceDrawerLayout;
import org.jetbrains.annotations.NotNull;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.content.ContextCompat;
import androidx.core.view.GravityCompat;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.PagerSnapHelper;
import androidx.recyclerview.widget.RecyclerView;
import de.hdodenhof.circleimageview.CircleImageView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ShiftActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private static final String TAG = "ShiftActivity";
    List<EventDay> events = new ArrayList<>();
    CalendarView calendarView;

    ArrayList<OnDateShift> dataModel = new ArrayList<>();
    List<SheduleListModel> sectionListData = new ArrayList<>();
    List<SheduleListModel> sectionListDataDay = new ArrayList<>();
    List<SheduleListModel> sectionListDataNight = new ArrayList<>();
    ArrayList<UpcomingShift> upcomingShifts = new ArrayList<>();

    private UpcomingShiftAdaptor upcomingShiftAdaptor;


    boolean doubleBackToExitPressedOnce = false;
    RecyclerView recyclerview, recyclerViewList;
    private OnDateShiftAdaptor shiftAdaptor;
    private AdvanceDrawerLayout drawer;
    private String vounter_id;
    private Dialog dialog;
    private CircleImageView profilePic;
    private ImageView handBurger;
    private Button btn_duty_status;
    private String dutyStatus = "0";
    private ScrollView scrollView;
    private String profile, VolunteerName;
    private Switch sw_mode, sw_shift;
    private Boolean sw;
    private LinearLayout ll_legend, shift_switch;
    private ProgressBar progressBar2;
    private TextView tv_all_shifts, tv_day_shift, tv_night_shift;
    private ImageView iv_all_shifts, iv_day_shift, iv_night_shift;
    private CardView cv_filter;
    private String currentDate;
    private FingerprintManager fingerprintManager = null;
    private boolean isResourceClicked = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shift);

        progressBar2 = findViewById(R.id.progressBar2);
        progressBar2.setVisibility(View.GONE);

        drawer = findViewById(R.id.drawer_layout);
        calendarView = findViewById(R.id.calendarView);
        recyclerview = findViewById(R.id.recyclerView);
        profilePic = findViewById(R.id.profilePic);
        handBurger = findViewById(R.id.handBurger);
        btn_duty_status = findViewById(R.id.btn_duty_status);
        scrollView = findViewById(R.id.scrollView);
        sw_mode = findViewById(R.id.sw_mode);
        ll_legend = findViewById(R.id.ll_legend);
        tv_all_shifts = findViewById(R.id.tv_all_shifts);
        tv_day_shift = findViewById(R.id.tv_day_shift);
        tv_night_shift = findViewById(R.id.tv_night_shift);

        shift_switch = findViewById(R.id.shift_switch);
        sw_shift = findViewById(R.id.sw_shift);

        iv_all_shifts = findViewById(R.id.iv_all_shifts);
        iv_day_shift = findViewById(R.id.iv_day_shift);
        iv_night_shift = findViewById(R.id.iv_night_shift);
        recyclerViewList = findViewById(R.id.recyclerViewList);
        cv_filter = findViewById(R.id.cv_filter);


        iv_day_shift.setVisibility(View.GONE);
        iv_night_shift.setVisibility(View.GONE);

        getData();
        hitStatusAPi();
        hitProfileApi();

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            fingerprintManager = (FingerprintManager)this.getSystemService(Context.FINGERPRINT_SERVICE);
        }

        recyclerview.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        new PagerSnapHelper().attachToRecyclerView(recyclerview);

        recyclerViewList.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL));
        recyclerViewList.setLayoutManager(new LinearLayoutManager(this));

        Date date = Calendar.getInstance().getTime();
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
        currentDate = df.format(date);

        Log.d(TAG, "onCreate: " + currentDate);


        if (!sw_mode.isChecked()) {
            calendarView.setVisibility(View.VISIBLE);

//            HitOpenShiftApi(currentDate);
            HitMyShiftApi(currentDate);
            hitShiftOnDateApi(currentDate);
            calendarView.setOnDayClickListener(new OnDayClickListener() {
                @Override
                public void onDayClick(EventDay eventDay) {
                    Calendar clickedDayCalendar = eventDay.getCalendar();

                    Date d = clickedDayCalendar.getTime();
                    SimpleDateFormat format1 = new SimpleDateFormat("yyyy-MM-dd");
                    String date1 = format1.format(d);
                    //Toast.makeText(MainActivity.this, ""+date1, Toast.LENGTH_SHORT).show();
                    hitShiftOnDateApi(date1);

                    scrollView.scrollBy(0, scrollView.getBottom());

                }
            });

        }

        btn_duty_status.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onDuty();
            }
        });

        sw_mode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean on = ((Switch) v).isChecked();
                if (on) {
                    calendarView.setVisibility(View.GONE);
                    recyclerview.setVisibility(View.GONE);
                    ll_legend.setVisibility(View.GONE);
                    recyclerViewList.setVisibility(View.VISIBLE);
                    cv_filter.setVisibility(View.VISIBLE);
                    hitShiftListApi(currentDate);
                    shift_switch.setVisibility(View.VISIBLE);

                    sw_shift.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            boolean on = ((Switch) v).isChecked();
                            if(on){
                                HitMyShiftApi();
                                cv_filter.setVisibility(View.GONE);
                            }else {
                                hitShiftListApi(currentDate);
                                cv_filter.setVisibility(View.VISIBLE);

                            }
                        }
                    });


                } else {
                    calendarView.setVisibility(View.VISIBLE);
                    recyclerview.setVisibility(View.VISIBLE);
                    ll_legend.setVisibility(View.VISIBLE);
                    recyclerViewList.setVisibility(View.GONE);
                    cv_filter.setVisibility(View.GONE);
                    shift_switch.setVisibility(View.INVISIBLE);
                    HitOpenShiftApi(currentDate);
                    HitMyShiftApi(currentDate);
                    hitShiftOnDateApi(currentDate);

                    calendarView.setOnDayClickListener(new OnDayClickListener() {
                        @Override
                        public void onDayClick(EventDay eventDay) {
                            Calendar clickedDayCalendar = eventDay.getCalendar();

                            Date d = clickedDayCalendar.getTime();
                            SimpleDateFormat format1 = new SimpleDateFormat("yyyy-MM-dd");
                            String date1 = format1.format(d);
                            //Toast.makeText(MainActivity.this, ""+date1, Toast.LENGTH_SHORT).show();
                            hitShiftOnDateApi(date1);

                            scrollView.scrollBy(0, scrollView.getBottom());

                        }
                    });
                }
            }
        });

        tv_all_shifts.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                all_shifts();
            }
        });

        tv_day_shift.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                day_shifts();
            }
        });

        tv_night_shift.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                night_shifts();
            }
        });

        calendarView.setOnPreviousPageChangeListener(new OnCalendarPageChangeListener() {
            @Override
            public void onChange() {
                Calendar calendar = calendarView.getCurrentPageDate();
                calendar.add(Calendar.DATE, 1);
                Date date1 = calendar.getTime();
                DateToDateConversion d = new DateToDateConversion();
                SimpleDateFormat format1 = new SimpleDateFormat("yyyy-MM-dd");
                String dateS = format1.format(date1);
                try {
                    String dateRes = d.dateConversion(dateS, "yyyy-MM-dd");
                    Log.d(TAG, "onChange: " + dateRes);
//                    HitOpenShiftApi(dateRes);
                    HitMyShiftApi(dateRes);

                } catch (ParseException e) {
                    e.printStackTrace();
                }
            }
        });

        calendarView.setOnForwardPageChangeListener(new OnCalendarPageChangeListener() {
            @Override
            public void onChange() {
                Calendar calendar = calendarView.getCurrentPageDate();
                calendar.add(Calendar.DATE, 1);
                Date date1 = calendar.getTime();
                DateToDateConversion d = new DateToDateConversion();
                SimpleDateFormat format1 = new SimpleDateFormat("yyyy-MM-dd");
                String dateS = format1.format(date1);
                try {
                    String dateRes = d.dateConversion(dateS, "yyyy-MM-dd");
                    Log.d(TAG, "onChange: " + dateRes);
//                    HitOpenShiftApi(dateRes);
                    HitMyShiftApi(dateRes);

                } catch (ParseException e) {
                    e.printStackTrace();
                }

                //Log.d(TAG, "onChange: "+date1.toString());
            }
        });


        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawer, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        ImageView navigationView_ = navigationView.getHeaderView(0).findViewById(R.id.nav_icon);
        TextView tv_Name = navigationView.getHeaderView(0).findViewById(R.id.tv_Name);
        Glide.with(ShiftActivity.this).load(profile).placeholder(R.drawable.ic_smalllogo).into(navigationView_);
        tv_Name.setText(VolunteerName);
//        navigationView.hes
        drawer.setViewScale(Gravity.START, 0.9f);
        drawer.setRadius(Gravity.START, 35);
        drawer.setViewElevation(Gravity.START, 20);


        handBurger.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                drawer.openDrawer(GravityCompat.START);

            }
        });


    }

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

            default:
                return super.onOptionsItemSelected(item);
        }
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

        if (!ShiftActivity.this.isFinishing() && !ShiftActivity.this.isDestroyed()) {
            dialog.show();
        }
    }
    private void openLanguageDialog() {
        drawer.closeDrawer(GravityCompat.START);
        final android.app.AlertDialog.Builder alert;
        alert = new android.app.AlertDialog.Builder(ShiftActivity.this);
        final LayoutInflater inflater = LayoutInflater.from(ShiftActivity.this);
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

        if (!ShiftActivity.this.isFinishing() && !ShiftActivity.this.isDestroyed()) {
            dialog.show();
        }
    }
    private void openBackupDialog() {
        drawer.closeDrawer(GravityCompat.START);
        final android.app.AlertDialog.Builder alert;
        alert = new android.app.AlertDialog.Builder(ShiftActivity.this);
        final LayoutInflater inflater = LayoutInflater.from(ShiftActivity.this);
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
                    Toast.makeText(ShiftActivity.this, "Fill the form", Toast.LENGTH_SHORT).show();
                }else {
                    hitSendLanguageRequest(" ",CaseID1,message.getText().toString(),support.toString());
                }



                dialog.dismiss();
            }
        });

        if (!ShiftActivity.this.isFinishing() && !ShiftActivity.this.isDestroyed()) {
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

                        ArrayAdapter<AdditionalLanguageSpinnerAdaptor> arrayAdapter = new ArrayAdapter<>(ShiftActivity.this, android.R.layout.simple_spinner_item, location);
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
                    Toast.makeText(ShiftActivity.this, "Something went wrong (OR) check your internet connection...!", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<List<AdditionalLanguageModel>> call, Throwable t) {
                Toast.makeText(ShiftActivity.this, "Something went wrong (OR) check your internet connection...!\n"+t.getMessage(), Toast.LENGTH_SHORT).show();
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

                        ArrayAdapter<CaseIDSpinnerAdaptor> adaptorArrayAdapter = new ArrayAdapter<>(ShiftActivity.this, android.R.layout.simple_spinner_item, channelData);
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
                    Toast.makeText(ShiftActivity.this, "Something went wrong (OR) check your internet connection...!", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<ChannelListModel> call, Throwable t) {
                Toast.makeText(ShiftActivity.this, "Something went wrong (OR) check your internet connection...!", Toast.LENGTH_SHORT).show();
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

                        ArrayAdapter<CaseIDSpinnerAdaptor> adaptorArrayAdapter = new ArrayAdapter<>(ShiftActivity.this, android.R.layout.simple_spinner_item, channelData);
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
                    Toast.makeText(ShiftActivity.this, "Something went wrong (OR) check your internet connection...!", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<ChannelListModel> call, Throwable t) {
                Toast.makeText(ShiftActivity.this, "Something went wrong (OR) check your internet connection...!", Toast.LENGTH_SHORT).show();
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
                StyleableToast.makeText(ShiftActivity.this, t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void shiftRequest() {
        Intent i = new Intent(getApplicationContext(), ShiftRequestListActivity.class);
        i.putExtra("vounter_id",vounter_id);
        i.putExtra("profile",profile);
        i.putExtra("VolunteerName",VolunteerName);
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

    private void event() {

        Intent i = new Intent(getApplicationContext(), VolEventsActivity.class);
        i.putExtra("vounter_id",vounter_id);
        i.putExtra("profile",profile);
        i.putExtra("VolunteerName",VolunteerName);
        startActivity(i);
        drawer.closeDrawer(GravityCompat.START);
        drawer.closeDrawer(GravityCompat.START);

    }


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

    private void getData() {

        Intent intent = getIntent();
        vounter_id = intent.getStringExtra("vounter_id");
        profile = intent.getStringExtra("profile");
        VolunteerName = intent.getStringExtra("name");
//        total_rewards = intent.getStringExtra("total_rewards");
    }

    private void HitOpenShiftApi(String date) {
        progressBar2.setVisibility(View.VISIBLE);
        api apiInstance = retrofit.retrofit.create(api.class);
        Call<List<OpenShift>> call = apiInstance.Open_shifts_by_year(date);
        call.enqueue(new Callback<List<OpenShift>>() {
            @Override
            public void onResponse(@NotNull Call<List<OpenShift>> call, Response<List<OpenShift>> response) {
                assert response.body() != null;
                List<OpenShift> dataModels = response.body();

                for (OpenShift model : dataModels) {
                    avidate(model.getDate());
                }
                calendarView.setEvents(events);
//                HitMyShiftApi(date);
                progressBar2.setVisibility(View.GONE);
            }

            @Override
            public void onFailure(Call<List<OpenShift>> call, Throwable t) {
                progressBar2.setVisibility(View.GONE);
                Log.d("dateresult", "err : " + t.getMessage());
            }
        });

    }

    private void HitMyShiftApi(String Date) {
        progressBar2.setVisibility(View.VISIBLE);
        api apiInstance = retrofit.retrofit.create(api.class);
        Call<List<MyShift>> call = apiInstance.myShift(vounter_id, Date);
        call.enqueue(new Callback<List<MyShift>>() {
            @Override
            public void onResponse(Call<List<MyShift>> call, Response<List<MyShift>> response) {
                if (response.isSuccessful()) {
                    assert response.body() != null;
                    List<MyShift> dataModels = response.body();
                    for (MyShift model : dataModels) {
                        //Log.d("dateresult","res : "+model.getDate());
                        setdate(model.getDate());
                    }
                    calendarView.setEvents(events);
                    progressBar2.setVisibility(View.GONE);
                    HitOpenShiftApi(Date);
                } else {
                    progressBar2.setVisibility(View.GONE);
                    Log.d("Response", "onResponse: ");
                }
            }

            @Override
            public void onFailure(Call<List<MyShift>> call, Throwable t) {
                progressBar2.setVisibility(View.GONE);
                Log.d("dateresult", "err : " + t.getMessage());
                HitOpenShiftApi(Date);
            }
        });

    }

    private void avidate(String date) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        Date dateTest = null;
        Calendar calendar = Calendar.getInstance();
        try {
            dateTest = sdf.parse(date);
            calendar.setTime(dateTest);
            events.add(new EventDay(calendar, R.drawable.ic_round_yellow));
            Log.d("aaaaaaa", "mm");
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }

    private void setdate(String date) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        Date dateTest = null;
        Calendar calendar = Calendar.getInstance();
        try {
            dateTest = sdf.parse(date);
            calendar.setTime(dateTest);
            events.add(new EventDay(calendar, R.drawable.ic_round));
            Log.d("aaaaaaa", "mm");
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }

    private void hitShiftOnDateApi(String date) {
        progressBar2.setVisibility(View.VISIBLE);
        api apiInstance = retrofit.retrofit.create(api.class);
        Call<List<OnDateShift>> call = apiInstance.Shift_on_date(date);
        call.enqueue(new Callback<List<OnDateShift>>() {
            @Override
            public void onResponse(Call<List<OnDateShift>> call, Response<List<OnDateShift>> response) {
                if (response.isSuccessful()) {
                    assert response.body() != null;
                    dataModel = new ArrayList<>(response.body());
                    shiftAdaptor = new OnDateShiftAdaptor(dataModel, ShiftActivity.this, vounter_id);
                    recyclerview.setAdapter(shiftAdaptor);
                    final int radius = getResources().getDimensionPixelSize(R.dimen.round);
                    final int dotsHeight = getResources().getDimensionPixelSize(R.dimen.height);
                    final int color = ContextCompat.getColor(ShiftActivity.this, R.color.colorGray);
                    recyclerview.addItemDecoration(new CommonItemSpaceDecoration(radius, radius * 4, dotsHeight, color, color));
                    assert response.body() != null;
                    List<OnDateShift> dataModels = response.body();

                    for (OnDateShift model : dataModels) {
                        Log.d("dateresult", "res : " + model.getVolunteerAssign());
                    }
                } else {
                    Log.d("response", "response");
                }
                progressBar2.setVisibility(View.GONE);
            }

            @Override
            public void onFailure(Call<List<OnDateShift>> call, Throwable t) {
                progressBar2.setVisibility(View.GONE);
                Log.d("dateresult", "err : " + t.getMessage());
            }
        });
    }

    private void hitProfileApi() {
        progressBar2.setVisibility(View.VISIBLE);
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
                        profile = infoModel.getProfilePic();
                        Glide.with(ShiftActivity.this).load(infoModel.getProfilePic()).placeholder(R.drawable.ic_smalllogo).into(profilePic);
                    }
                    progressBar2.setVisibility(View.GONE);
                } else {
                    progressBar2.setVisibility(View.GONE);
                    Log.d("sssss", "err " + response.errorBody());
                }
            }

            @Override
            public void onFailure(Call<List<ProfileInfoModel>> call, Throwable t) {
                progressBar2.setVisibility(View.GONE);
                Log.d("sssss", "err " + t.getMessage());
            }
        });
    }

    private void hitStatusAPi() {
        progressBar2.setVisibility(View.VISIBLE);
        api api = retrofit.retrofit.create(api.class);
        Call<ScheduleStatusModel> call = api.ScheduleStatusModel(vounter_id);
        call.enqueue(new Callback<ScheduleStatusModel>() {
            @Override
            public void onResponse(Call<ScheduleStatusModel> call, Response<ScheduleStatusModel> response) {
                if (response.isSuccessful()) {
                    assert response.body() != null;
                    dutyStatus = response.body().getMessage();
                    setDutyStatus(dutyStatus);
                    progressBar2.setVisibility(View.GONE);
                } else {
                    progressBar2.setVisibility(View.GONE);
                    Log.d("sssss", "err : " + response.errorBody());
                }
            }

            @Override
            public void onFailure(Call<ScheduleStatusModel> call, Throwable t) {
                progressBar2.setVisibility(View.GONE);
                Log.d("sssss", "err " + t.getMessage());
                StyleableToast.makeText(getApplicationContext(), t.getMessage(), Toast.LENGTH_SHORT).show();
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

    private void all_shifts() {
        iv_all_shifts.setVisibility(View.VISIBLE);
        iv_day_shift.setVisibility(View.GONE);
        iv_night_shift.setVisibility(View.GONE);
        hitShiftListApi(currentDate);
    }

    private void day_shifts() {
        iv_day_shift.setVisibility(View.VISIBLE);
        iv_all_shifts.setVisibility(View.GONE);
        iv_night_shift.setVisibility(View.GONE);
        hitShiftListApiMorning(currentDate);
    }

    private void night_shifts() {
        iv_night_shift.setVisibility(View.VISIBLE);
        iv_all_shifts.setVisibility(View.GONE);
        iv_day_shift.setVisibility(View.GONE);
        hitShiftListApiNight(currentDate);
    }

    private void hitShiftListApi(String date) {
        progressBar2.setVisibility(View.VISIBLE);
        api api = retrofit.retrofit.create(api.class);
        Call<JsonObject> call = api.Upcoming_open_shift(date);
        call.enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                if (response.isSuccessful()) {

                    Log.d(TAG, "onResponse: \n" + response.body());
                    try {
                        JSONObject date = new JSONObject(String.valueOf(response.body()));
                        Iterator keys = date.keys();
                        while (keys.hasNext()) {
                            Object key = keys.next();
                            JSONArray value = date.getJSONArray((String) key);
                            List<JSONArray> jsonData = new ArrayList<>();
                            jsonData.add(value);
                            Log.d("Results___", "res " + value);
                            sectionListData.add(new SheduleListModel((String) key, jsonData));
                        }

                        Log.d(TAG, "initData: " + sectionListData);
                        ShiftListParentAdaptor shiftListParentAdaptor = new ShiftListParentAdaptor(sectionListData, ShiftActivity.this, vounter_id);
                        recyclerViewList.setAdapter(shiftListParentAdaptor);
                        progressBar2.setVisibility(View.GONE);

                    } catch (JSONException e) {
                        progressBar2.setVisibility(View.GONE);
                        Toast.makeText(ShiftActivity.this, "err" + e.getMessage(), Toast.LENGTH_SHORT).show();
                        e.printStackTrace();
                        Log.d(TAG, "onResponse: " + e.getMessage());
                    }

                } else {
                    progressBar2.setVisibility(View.GONE);
                    Log.d("Results___", "res " + response.errorBody());
                }
            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {
                progressBar2.setVisibility(View.GONE);
                Log.d("Results___", "res " + t.getMessage());
            }
        });
    }

    private void hitShiftListApiMorning(String date) {
        progressBar2.setVisibility(View.VISIBLE);
        api api = retrofit.retrofit.create(api.class);
        Call<JsonObject> call = api.Upcoming_open_shift_wise(date, "morning shift");
        call.enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                if (response.isSuccessful()) {

                    Log.d(TAG, "onResponse: \n" + response.body());
                    try {
                        JSONObject date = new JSONObject(String.valueOf(response.body()));
                        Iterator keys = date.keys();
                        while (keys.hasNext()) {
                            Object key = keys.next();
                            JSONArray value = date.getJSONArray((String) key);
                            List<JSONArray> jsonData = new ArrayList<>();
                            jsonData.add(value);
                            Log.d("Results___", "res " + value);
                            sectionListDataDay.add(new SheduleListModel((String) key, jsonData));
                        }

                        Log.d(TAG, "initData: " + sectionListData);
                        ShiftListParentAdaptor shiftListParentAdaptor = new ShiftListParentAdaptor(sectionListDataDay, ShiftActivity.this, vounter_id);
                        recyclerViewList.setAdapter(shiftListParentAdaptor);
                        progressBar2.setVisibility(View.GONE);

                    } catch (JSONException e) {
                        progressBar2.setVisibility(View.GONE);
                        Toast.makeText(ShiftActivity.this, "err" + e.getMessage(), Toast.LENGTH_SHORT).show();
                        e.printStackTrace();
                        Log.d(TAG, "onResponse: " + e.getMessage());
                    }

                } else {
                    progressBar2.setVisibility(View.GONE);
                    Log.d("Results___", "res " + response.errorBody());
                }
            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {
                progressBar2.setVisibility(View.GONE);
                Log.d("Results___", "res " + t.getMessage());
            }
        });
    }

    private void hitShiftListApiNight(String date) {
        progressBar2.setVisibility(View.VISIBLE);
        api api = retrofit.retrofit.create(api.class);
        Call<JsonObject> call = api.Upcoming_open_shift_wise(date, "Evening Shift");
        call.enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                if (response.isSuccessful()) {

                    Log.d("TAG", "onResponse: \n" + response.body());
                    try {
                        JSONObject date = new JSONObject(String.valueOf(response.body()));
                        Iterator keys = date.keys();
                        while (keys.hasNext()) {
                            Object key = keys.next();
                            JSONArray value = date.getJSONArray((String) key);
                            List<JSONArray> jsonData = new ArrayList<>();
                            jsonData.add(value);
                            Log.d("Results___", "res " + value);
                            sectionListDataNight.add(new SheduleListModel((String) key, jsonData));
                        }

                        Log.d(TAG, "initData: " + sectionListData);
                        ShiftListParentAdaptor shiftListParentAdaptor = new ShiftListParentAdaptor(sectionListDataNight, ShiftActivity.this, vounter_id);
                        recyclerViewList.setAdapter(shiftListParentAdaptor);
                        progressBar2.setVisibility(View.GONE);

                    } catch (JSONException e) {
                        progressBar2.setVisibility(View.GONE);
                        Toast.makeText(ShiftActivity.this, "err" + e.getMessage(), Toast.LENGTH_SHORT).show();
                        e.printStackTrace();
                        Log.d(TAG, "onResponse: " + e.getMessage());
                    }

                } else {
                    progressBar2.setVisibility(View.GONE);
                    Log.d("Results___", "res " + response.errorBody());
                }
            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {
                progressBar2.setVisibility(View.GONE);
                Log.d("Results___", "res " + t.getMessage());
            }
        });
    }

    private void HitMyShiftApi() {
        api apiInstance = retrofit.retrofit.create(api.class);
        Call<List<UpcomingShift>> call = apiInstance.Volunteer_upcoming_shift_list(vounter_id);
        call.enqueue(new Callback<List<UpcomingShift>>() {
            @Override
            public void onResponse(Call<List<UpcomingShift>> call, Response<List<UpcomingShift>> response) {
                assert response.body() != null;
                List<UpcomingShift> dataModels = response.body();
                upcomingShifts = new ArrayList<>(response.body());
                upcomingShiftAdaptor = new UpcomingShiftAdaptor(upcomingShifts, ShiftActivity.this);
                recyclerViewList.setAdapter(upcomingShiftAdaptor);
            }

            @Override
            public void onFailure(Call<List<UpcomingShift>> call, Throwable t) {
                Log.d("dateresult", "err : " + t.getMessage());
            }
        });
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

    private void changePassword() {
        Intent i = new Intent(getApplicationContext(), ChangePasswordActivity.class);
        i.putExtra("vounter_id",vounter_id);
        i.putExtra("profile",profile);
        i.putExtra("VolunteerName",VolunteerName);
        startActivity(i);
//        finish();
//        VolunterLogin();
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

    private void onDuty() {
        Intent intent = new Intent(ShiftActivity.this, DutyShiftActivity1.class);
        intent.putExtra("volID", vounter_id);
        intent.putExtra("volID1", "on_duty");
        startActivity(intent);
    }

}