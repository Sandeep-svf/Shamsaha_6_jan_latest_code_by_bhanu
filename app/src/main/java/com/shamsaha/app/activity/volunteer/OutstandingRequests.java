package com.shamsaha.app.activity.volunteer;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
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
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.material.navigation.NavigationView;
import com.muddzdev.styleabletoast.StyleableToast;
import com.shamsaha.app.ApiModel.ChannelListModel;
import com.shamsaha.app.ApiModel.volunter.AdditionalLanguageModel;
import com.shamsaha.app.ApiModel.volunter.HelpRequestModel;
import com.shamsaha.app.ApiModel.volunter.LanguageRequestModel;
import com.shamsaha.app.ApiModel.volunter.MessageModel;
import com.shamsaha.app.R;
import com.shamsaha.app.activity.ChatHelper.Application.TwilioChatApplication;
import com.shamsaha.app.activity.ChatHelper.ChanModel;
import com.shamsaha.app.activity.ChatHelper.Message.ChatClientManager;
import com.shamsaha.app.activity.ChatHelper.listener.TaskCompletionListener;
import com.shamsaha.app.activity.Victem.ChatActivity;
import com.shamsaha.app.activity.volunteer.Resources.VolunterResourceActivity;
import com.shamsaha.app.activity.volunteer.VolEvent.VolEventsActivity;
import com.shamsaha.app.adaptor.AdditionalLanguageSpinnerAdaptor;
import com.shamsaha.app.adaptor.CaseIDSpinnerAdaptor;
import com.shamsaha.app.adaptor.Volunteer.HelpRequestListAdaptor;
import com.shamsaha.app.adaptor.Volunteer.HelpRequestListAdaptor1;
import com.shamsaha.app.api.api;
import com.shamsaha.app.retrofitAdaptor.retrofit;
import com.shamsaha.app.utils.ConstantsURL.baseURL;
import com.twilio.chat.CallbackListener;
import com.twilio.chat.Channel;
import com.twilio.chat.Channels;
import com.twilio.chat.ErrorInfo;
import com.twilio.chat.Members;
import com.twilio.chat.StatusListener;

import org.infideap.drawerbehavior.AdvanceDrawerLayout;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.view.GravityCompat;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class OutstandingRequests extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

//    private static boolean menuStatus = true;
    public Button more_detail1;
    public boolean isResourceClicked = false;
//    ArrayList<ResourceCategory> CatModels = new ArrayList<>();
//    RecyclerView recyclerView;
//    ArrayAdapter<String> adapter;
    Spinner spinner;
//    List<String> data;
    private TextView type;
//    boolean doubleBackToExitPressedOnce = false;
//    private ResourceCategoryVolAdaptor CatAdaptor;
    private AdvanceDrawerLayout drawer;
    private ImageView handBurger, floatingActionButton2, back;
//    private CardView applyBtn, contactUS;

    private String vounter_id;
    private String profile;
    private String VolunteerName;

    private RecyclerView data_recycler;

    private Dialog dialog;
    private FingerprintManager fingerprintManager = null;
    ArrayList<HelpRequestModel> helpRequestModels = new ArrayList<>();

//
//    String email;
//    String contactNo;
//    String startDate;
//    String endDate;
    String Language;
//    String RequestMessage;
    private HelpRequestListAdaptor1 helpRequestListAdaptor;
    private String helpID;
    private ProgressDialog progressDialog;
    private ChatClientManager chatClientManager;
    private Channels channelsObject;
    private ChanModel chanModel;
    boolean doubleBackToExitPressedOnce = false;

    private void getData() {

        Intent intent = getIntent();
        vounter_id = intent.getStringExtra("vounter_id");
        profile = intent.getStringExtra("profile");
        VolunteerName = intent.getStringExtra("name");
//        total_rewards = intent.getStringExtra("total_rewards");
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        if (drawer.isDrawerOpen(GravityCompat.START)) {
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
        }
    }

    public BroadcastReceiver mMessageReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String caseID = intent.getStringExtra("caseID");
            helpID = intent.getStringExtra("id");
//            Toast.makeText(context, "Mani" + caseID, Toast.LENGTH_SHORT).show();
            hithelpAcceptAPI(helpID);
            checkTwilioClient(caseID);
        }
    };

    private void showActivityIndicator(final String message) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                progressDialog = new ProgressDialog(OutstandingRequests.this);
                progressDialog.setMessage(message);
                progressDialog.show();
                progressDialog.setCanceledOnTouchOutside(false);
                progressDialog.setCancelable(false);
            }
        });
    }

    private void checkTwilioClient(String caseID) {
        try {
            showActivityIndicator(getResources().getString(R.string.loading_channels_message));
            chatClientManager = TwilioChatApplication.get().getChatClientManager();
            initializeClient(caseID);
        } catch (Exception e) {
            Log.d("abc", "checkTwilioClient: " + e.getMessage());
        }

    }


    private void initializeClient(String caseID) {
        //reg vol
        chatClientManager.connectClient(vounter_id, new TaskCompletionListener<Void, String>() {
            @Override
            public void onSuccess(Void aVoid) {
                populateChannels(caseID);
            }

            @Override
            public void onError(String errorMessage) {
                stopActivityIndicator();
                StyleableToast.makeText(OutstandingRequests.this, "Client connection error: " + errorMessage + "\n Check Internet Connection..!"
                        , Toast.LENGTH_LONG, R.style.mytoast).show();
            }
        });
    }

    private void initializeClientCase(String caseID) {
        chatClientManager.connectClient(caseID, new TaskCompletionListener<Void, String>() {
            @Override
            public void onSuccess(Void aVoid) {
                populateChannels1(caseID);
            }

            @Override
            public void onError(String errorMessage) {
                stopActivityIndicator();
                StyleableToast.makeText(OutstandingRequests.this, "Client connection error: " + errorMessage + "\n Check Internet Connection..!"
                        , Toast.LENGTH_LONG, R.style.mytoast).show();
            }
        });
    }

    private void stopActivityIndicator() {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (progressDialog.isShowing()) {
                    progressDialog.dismiss();
                }
            }
        });
    }

    private void populateChannels(String caseID) {
        channelsObject = chatClientManager.getChatClient().getChannels();
        channelsObject.getChannel(caseID, new CallbackListener<Channel>() {
            @Override
            public void onSuccess(Channel channel) {
                Log.d("abc", "onSuccess: " + channel.getStatus());
                if (channel.getStatus() == Channel.ChannelStatus.JOINED) {
                    Log.d("abc", "onSuccess: True"+channel.getUniqueName());
                    chanModel.setChannel(channel);
//                            messagesObject = channel.getMessages();
//                            Toast.makeText(VolunteerHome.this, ""+messagesObject.getLastConsumedMessageIndex(), Toast.LENGTH_SHORT).show();
                    if (channel.getFriendlyName().equals(chanModel.getChannel().getFriendlyName())) {
                        Intent intent = new Intent(OutstandingRequests.this, ChatActivity.class);
                        intent.putExtra("volID", vounter_id);
                        intent.putExtra("caseID", "");
                        startActivity(intent);
                        stopActivityIndicator();
                    }
//                    stopActivityIndicator();
                } else {
                    Log.d("abc", "onSuccess: false");
                    chanModel.setChannel(channel);
                    if (channel.getFriendlyName().equals(chanModel.getChannel().getFriendlyName())) {
                        Intent intent = new Intent(OutstandingRequests.this, ChatActivity.class);
                        intent.putExtra("volID", vounter_id);
                        intent.putExtra("caseID", "");
                        startActivity(intent);
                        stopActivityIndicator();
                    }
//                    stopActivityIndicator();
                }
            }

            @Override
            public void onError(ErrorInfo errorInfo) {
                super.onError(errorInfo);
//                        Toast.makeText(VolunteerHome.this, "errorInfo " + errorInfo, Toast.LENGTH_SHORT).show();
                initializeClientCase(caseID);
            }
        });
    }


    private void populateChannels1(String caseID) {
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(OutstandingRequests.this, "", Toast.LENGTH_SHORT).show();
                channelsObject = chatClientManager.getChatClient().getChannels();
                channelsObject.getChannel(caseID, new CallbackListener<Channel>() {
                    @Override
                    public void onSuccess(Channel channel) {
                        Log.d("abc", "onSuccess: " + channel.getStatus());
                        chanModel.setChannel(channel);
                        Members membersObject = channel.getMembers();
                        if(membersObject != null){
                            membersObject.addByIdentity(vounter_id, new StatusListener() {
                                @Override
                                public void onSuccess() {
                                    Log.d("abc", "onSuccess: " + chanModel.getChannel().getMembers().getMembersList());
                                    stopActivityIndicator();
                                    initializeClient(caseID);
                                }
                                @Override
                                public void onError(ErrorInfo errorInfo) {
                                    super.onError(errorInfo);
                                    Log.d("abc", "onSuccess: " + errorInfo.getMessage());
                                    stopActivityIndicator();
                                }
                            });
                        }else{
                            Toast.makeText(OutstandingRequests.this, "Something went wrong, try again", Toast.LENGTH_SHORT).show();
                            stopActivityIndicator();
                        }

                    }

                    @Override
                    public void onError(ErrorInfo errorInfo) {
                        super.onError(errorInfo);
                        Toast.makeText(OutstandingRequests.this, "errorInfo " + errorInfo, Toast.LENGTH_SHORT).show();
//                refreshChannels(caseID);
//                Log.d(TAG, "onError: "+channelManager.getChannels());
//                stopActivityIndicator();
                    }
                });
            }
        },3000);

    }

    @Override
    public void onDestroy() {
        /*
         * Tear down audio device management and restore previous volume stream
         */
        if (chatClientManager != null) {
            new Handler().post(() -> {
                //chatClientManager.shutdown();
                //TwilioChatApplication.get().getChatClientManager().setChatClient(null);
            });
        }
        super.onDestroy();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_volunter_resource);

        LocalBroadcastManager.getInstance(this).registerReceiver(mMessageReceiver,
                new IntentFilter("custom-message"));

        getData();

        initialize();
        //hitResourceLocationApi();
        nav();
        chanModel = ChanModel.getInstance();
        spinner.setVisibility(View.GONE);
        type.setText("Outstanding Requests");
        floatingActionButton2.setVisibility(View.GONE);
        data_recycler.setLayoutManager(new LinearLayoutManager(this));
        hitApiResourceCategory();

        handBurger.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("WrongConstant")
            @Override
            public void onClick(View v) {
                drawer.openDrawer(Gravity.START);
            }
        });


    }

    private void nav() {
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawer, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        ImageView navigationView_ = navigationView.getHeaderView(0).findViewById(R.id.nav_icon);
        TextView tv_Name = navigationView.getHeaderView(0).findViewById(R.id.tv_Name);
        Glide.with(OutstandingRequests.this).load(profile).placeholder(R.drawable.ic_smalllogo).into(navigationView_);
        tv_Name.setText(VolunteerName);
//        navigationView.hes
        drawer.setViewScale(Gravity.START, 0.9f);
        drawer.setRadius(Gravity.START, 35);
        drawer.setViewElevation(Gravity.START, 20);
    }

    private void initialize() {
        drawer = findViewById(R.id.drawer_layout);
        handBurger = findViewById(R.id.handBurger3);
        type = findViewById(R.id.type);
        more_detail1 = findViewById(R.id.more_detail1);
        floatingActionButton2 = findViewById(R.id.floatingActionButton2);
        spinner = findViewById(R.id.spinner);
        back = findViewById(R.id.back);
        data_recycler = findViewById(R.id.data_recycler);
    }

    private void hitApiResourceCategory() {
        api api = retrofit.retrofit.create(com.shamsaha.app.api.api.class);
        Call<HelpRequestModel> call = api.helpvictimlist();
        call.enqueue(new Callback<HelpRequestModel>() {
            @Override
            public void onResponse(@NotNull Call<HelpRequestModel> call, @NotNull Response<HelpRequestModel> response) {
                if (response.isSuccessful() && response.body()!=null) {
                    if (response.body().getData().size() != 0) {
                        List<HelpRequestModel.Datum> newHelpRequestModels = response.body().getData();

                        for (HelpRequestModel.Datum newHelpRequestModel:response.body().getData()){
                            newHelpRequestModel.setMessage(response.body().getMessage());
                        }
                        //helpRequestModels = new ArrayList<>(response.body());
                        helpRequestListAdaptor = new HelpRequestListAdaptor1(newHelpRequestModels, OutstandingRequests.this,  vounter_id);
                        data_recycler.setAdapter(helpRequestListAdaptor);
                    }else {
                        Log.d("Outstanding requests", "onResponse: HelpVictimListRequest was empty");
                    }

                } else {
                    Toast.makeText(OutstandingRequests.this, "err : " + response.errorBody(), Toast.LENGTH_SHORT).show();
                    Log.d("ressss", "err : " + response.errorBody());
                }
            }

            @Override
            public void onFailure(Call<HelpRequestModel> call, Throwable t) {
//                stopActivityIndicator();
                Log.d("ressss", "err : " + t.getMessage());
                Toast.makeText(OutstandingRequests.this, "err : " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }


    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.

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
        //drawer.closeDrawer(GravityCompat.START);
        //return true;
    }

    private void caseReport() {
        Intent i = new Intent(getApplicationContext(), CaseReportActivity.class);
        i.putExtra("vounter_id", vounter_id);
        i.putExtra("profile", profile);
        i.putExtra("VolunteerName", VolunteerName);
        startActivity(i);
        drawer.closeDrawer(GravityCompat.START);
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


    private void upcomingSgift() {
        Intent i = new Intent(getApplicationContext(), UpcomingShiftActivity.class);
        i.putExtra("vounter_id", vounter_id);
        i.putExtra("profile", profile);
        i.putExtra("VolunteerName", VolunteerName);
        startActivity(i);
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

    String CaseID;
    String CaseID1;

    private void openHelpListDialog(HelpRequestListAdaptor helpRequestListAdaptor, Dialog dialog1) {
        drawer.closeDrawer(GravityCompat.START);
        dialog1.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog1.setCancelable(false);
        dialog1.setContentView(R.layout.help_request_list_dialog);
        ImageView closeImg2 = dialog1.findViewById(R.id.closeBtn2);
        RecyclerView rv_helplist = dialog1.findViewById(R.id.rv_helplist);
        LinearLayout list_items = dialog1.findViewById(R.id.list_items);
        LinearLayout accept = dialog1.findViewById(R.id.accept);
        list_items.setVisibility(View.VISIBLE);
        accept.setVisibility(View.GONE);
        rv_helplist.setLayoutManager(new LinearLayoutManager(this));
        rv_helplist.setAdapter(helpRequestListAdaptor);
        closeImg2.setOnClickListener(v -> dialog1.dismiss());
        dialog1.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog1.getWindow().setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);
        if (!OutstandingRequests.this.isFinishing() && !OutstandingRequests.this.isDestroyed()) {
            dialog1.show();
        }
    }

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
        ProgressBar progressBar5 = dialog.findViewById(R.id.progressBar5);
        CardView workstation = dialog.findViewById(R.id.workstation);
        WebView webView = dialog.findViewById(R.id.webview);
        progressBar5.setVisibility(View.GONE);
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
       // settings.setAppCachePath(getCacheDir().getPath());

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
                Intent i = new Intent(OutstandingRequests.this,WebViewActivity.class);
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
            Intent i = new Intent(OutstandingRequests.this,WebViewActivity.class);
            i.putExtra("type","volunteer");
            i.putExtra("url",baseURL.BaseURL_API+"services/wokstation?volunteer_id=" + vounter_id + "&case_id=" + CaseID);
            startActivity(i);

        });

        webView.setWebViewClient(new WebViewClient(){
            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                super.onPageStarted(view, url, favicon);
//                Toast.makeText(PayMentActivity.this, url, Toast.LENGTH_SHORT).show();
                Log.d("nnnnnnnnnn",url);
            }
        });


        webView.setWebChromeClient(new WebChromeClient(){
            public void onProgressChanged(WebView view, int newProgress) {
                progressBar5.setVisibility(View.VISIBLE);
                if(newProgress == 100){
                    progressBar5.setVisibility(View.GONE);
                }
                Log.d("mmmm","ddd"+newProgress);
            }
        });


        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        dialog.getWindow().setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.MATCH_PARENT);

        if (!OutstandingRequests.this.isFinishing() && !OutstandingRequests.this.isDestroyed()) {
            dialog.show();
        }
    }

    private void openLanguageDialog() {
        drawer.closeDrawer(GravityCompat.START);
        final android.app.AlertDialog.Builder alert;
        alert = new android.app.AlertDialog.Builder(OutstandingRequests.this);
        final LayoutInflater inflater = LayoutInflater.from(OutstandingRequests.this);
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

        if (!OutstandingRequests.this.isFinishing() && !OutstandingRequests.this.isDestroyed()) {
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

                        ArrayAdapter<AdditionalLanguageSpinnerAdaptor> arrayAdapter = new ArrayAdapter<>(OutstandingRequests.this, android.R.layout.simple_spinner_item, location);
                        arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

                        spinner.setAdapter(arrayAdapter);

                        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                            @Override
                            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                                AdditionalLanguageSpinnerAdaptor spinnerAdaptor = (AdditionalLanguageSpinnerAdaptor) parent.getSelectedItem();
//                                Toast.makeText(VolunterResourceActivity.this, "ID : "+spinnerAdaptor.getId()+"\n" +
//                                        "\nLocation : "+spinnerAdaptor.getLocation(), Toast.LENGTH_SHORT).show();

                                editText.setText("There is an immediate language need in " + spinnerAdaptor.getLanguage() +
                                        " language. Can you back up "+CaseID1+" and take over this case right now ?");

                                Language = spinnerAdaptor.getLanguage();
                            }

                            @Override
                            public void onNothingSelected(AdapterView<?> parent) {

                            }
                        });

                    }

                } else {
                    Toast.makeText(OutstandingRequests.this, "Something went wrong (OR) check your internet connection...!", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<List<AdditionalLanguageModel>> call, Throwable t) {
                Toast.makeText(OutstandingRequests.this, "Something went wrong (OR) check your internet connection...!\n" + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

//    private void hitApiTimer(){
//        api api = retrofit.retrofit.create(api.class);
//        Call<MessageModel> call = api.timer(vounter_id);
//        call.enqueue(new Callback<MessageModel>() {
//            @Override
//            public void onResponse(Call<MessageModel> call, Response<MessageModel> response) {
//                if (response.isSuccessful() && response.code() == 200 && response.body() != null && response.body().getMessage() != null && response.body().getMessage().length() > 0) {
//                    if(response.body().getStatus().equals("valid")){
//                        int sec = Integer.parseInt(response.body().getMessage().toString());
//                        timer(sec,timerTV,timeCard);
//                    }
//                }
//            }
//
//            @Override
//            public void onFailure(Call<MessageModel> call, Throwable t) {
//                Toast.makeText(OutstandingRequests.this, t.getMessage(), Toast.LENGTH_SHORT).show();
//            }
//        });
//    }

    private void hithelpAcceptAPI(String id){
        api api = retrofit.retrofit.create(api.class);
        Call<MessageModel> call = api.helpvictimAccept(id);
        call.enqueue(new Callback<MessageModel>() {
            @Override
            public void onResponse(Call<MessageModel> call, Response<MessageModel> response) {
                if (response.isSuccessful() && response.code() == 200 && response.body() != null && response.body().getMessage() != null && response.body().getMessage().length() > 0) {
//                    if(response.body().getStatus().equals("valid")){
//                        int sec = Integer.parseInt(response.body().getMessage().toString());
//                        timer(sec,timerTV,timeCard);
//                    }
                }
            }

            @Override
            public void onFailure(Call<MessageModel> call, Throwable t) {
                Toast.makeText(OutstandingRequests.this, t.getMessage(), Toast.LENGTH_SHORT).show();
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

                        ArrayAdapter<CaseIDSpinnerAdaptor> adaptorArrayAdapter = new ArrayAdapter<>(OutstandingRequests.this, android.R.layout.simple_spinner_item, channelData);
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
                    Toast.makeText(OutstandingRequests.this, "Something went wrong (OR) check your internet connection...!", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<ChannelListModel> call, Throwable t) {
                Toast.makeText(OutstandingRequests.this, "Something went wrong (OR) check your internet connection...!", Toast.LENGTH_SHORT).show();
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

                        ArrayAdapter<CaseIDSpinnerAdaptor> adaptorArrayAdapter = new ArrayAdapter<>(OutstandingRequests.this, android.R.layout.simple_spinner_item, channelData);
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
                    Toast.makeText(OutstandingRequests.this, "Something went wrong (OR) check your internet connection...!", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<ChannelListModel> call, Throwable t) {
                Toast.makeText(OutstandingRequests.this, "Something went wrong (OR) check your internet connection...!", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void openBackupDialog() {
        drawer.closeDrawer(GravityCompat.START);
        final android.app.AlertDialog.Builder alert;
        alert = new android.app.AlertDialog.Builder(OutstandingRequests.this);
        final LayoutInflater inflater = LayoutInflater.from(OutstandingRequests.this);
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
//                Toast.makeText(OutstandingRequests.this, "supportStr "+ support, Toast.LENGTH_SHORT).show();
                if (support.toString().isEmpty() || support.toString().equals("") || message.getText().toString().isEmpty() || message.getText().toString().equals("")) {
                    Toast.makeText(OutstandingRequests.this, "Fill the form", Toast.LENGTH_SHORT).show();
                } else {
                    hitSendLanguageRequest("", CaseID1, message.getText().toString(), support.toString());
                }


                dialog.dismiss();
            }
        });

        if (!OutstandingRequests.this.isFinishing() && !OutstandingRequests.this.isDestroyed()) {
            dialog.show();
        }
    }

    private void hitSendLanguageRequest(String Language, String caseID, String message, String support) {
        Log.d("abc", "hitSendLanguageRequest: " + Language + "  " + caseID + "   " + message + "   " + support);
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
                    Log.d("abc", "response Status : " + response.body().getStatus());
                } else {
                    Log.d("abc", "err : " + response.errorBody());
                    Toast.makeText(getApplicationContext(), response.errorBody().toString(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<LanguageRequestModel> call, Throwable t) {
                Log.d("sssss", "err " + t.getMessage());
                StyleableToast.makeText(OutstandingRequests.this, t.getMessage(), Toast.LENGTH_SHORT).show();
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
        i.putExtra("vounter_id",vounter_id);
        i.putExtra("profile",profile);
        i.putExtra("VolunteerName",VolunteerName);
        startActivity(i);
        drawer.closeDrawer(GravityCompat.START);
        drawer.closeDrawer(GravityCompat.START);

    }

    private void VolunterLogin() {

        Intent i = new Intent(getApplicationContext(), loginActivity.class);
        startActivity(i);
        finish();
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
        i.putExtra("vounter_id",vounter_id);
        i.putExtra("profile",profile);
        i.putExtra("VolunteerName",VolunteerName);
        startActivity(i);
//        finish();
//        VolunterLogin();
    }
}