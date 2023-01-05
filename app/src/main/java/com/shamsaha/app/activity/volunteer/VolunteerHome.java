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
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;
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
import com.makeramen.roundedimageview.RoundedImageView;
import com.muddzdev.styleabletoast.StyleableToast;
import com.shamsaha.app.ApiModel.ChannelListModel;
import com.shamsaha.app.ApiModel.volunter.AdditionalLanguageModel;
import com.shamsaha.app.ApiModel.volunter.Announcement;
import com.shamsaha.app.ApiModel.volunter.HelpRequestModel;
import com.shamsaha.app.ApiModel.volunter.LanguageRequestModel;
import com.shamsaha.app.ApiModel.volunter.MessageModel;
import com.shamsaha.app.ApiModel.volunter.ModModel;
import com.shamsaha.app.ApiModel.volunter.ProfileInfoModel;
import com.shamsaha.app.ApiModel.volunter.ScheduleStatusModel;
import com.shamsaha.app.ApiModel.volunter.UpcomingShift;
import com.shamsaha.app.R;
import com.shamsaha.app.activity.CallHelper.fcm.RegistrationServiceVolunteer;
import com.shamsaha.app.activity.ChatHelper.Application.TwilioChatApplication;
import com.shamsaha.app.activity.ChatHelper.ChanModel;
import com.shamsaha.app.activity.ChatHelper.Channel.ChannelManager;
import com.shamsaha.app.activity.ChatHelper.Message.ChatClientManager;
import com.shamsaha.app.activity.ChatHelper.listener.TaskCompletionListener;
import com.shamsaha.app.activity.Victem.ChatActivity;
import com.shamsaha.app.activity.volunteer.Resources.VolunterResourceActivity;
import com.shamsaha.app.activity.volunteer.VolEvent.VolEventsActivity;
import com.shamsaha.app.activity.volunteer.onduty.DutyShiftActivity1;
import com.shamsaha.app.adaptor.AdditionalLanguageSpinnerAdaptor;
import com.shamsaha.app.adaptor.CaseIDSpinnerAdaptor;
import com.shamsaha.app.adaptor.Volunteer.HelpRequestListAdaptor;
import com.shamsaha.app.adaptor.Volunteer.anouncementAdaptor;
import com.shamsaha.app.api.api;
import com.shamsaha.app.preferences.Singleton;
import com.shamsaha.app.retrofitAdaptor.retrofit;
import com.shamsaha.app.utils.ConstantsURL.baseURL;
import com.shamsaha.app.utils.SharedPreferencesUtils;
import com.shamsaha.app.utils.dateConversion;
import com.twilio.chat.CallbackListener;
import com.twilio.chat.Channel;
import com.twilio.chat.Channels;
import com.twilio.chat.ChatClient;
import com.twilio.chat.ChatClientListener;
import com.twilio.chat.ErrorInfo;
import com.twilio.chat.Members;
import com.twilio.chat.Messages;
import com.twilio.chat.StatusListener;
import com.twilio.chat.User;
import com.twilio.voice.RegistrationException;
import com.twilio.voice.UnregistrationListener;
import com.twilio.voice.Voice;

import org.infideap.drawerbehavior.AdvanceDrawerLayout;
import org.jetbrains.annotations.NotNull;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.content.ContextCompat;
import androidx.core.view.GravityCompat;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class VolunteerHome extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, ChatClientListener {

    private static final String TAG = "abc";
    boolean doubleBackToExitPressedOnce = false;
    ArrayList<Announcement> announcementsModel = new ArrayList<>();
    ArrayList<HelpRequestModel> helpRequestModels = new ArrayList<>();
    private String vounter_id;
    private String profile;
    private String VolunteerName;
    private String total_rewards;
    private String password_login_first = "No";
    private String dutyStatus = "0";
    private ImageView handBurger, profilePic, iv_silver, iv_gold, iv_platinum;
    private TextView name, rewardTv, tv_deate, tv_language, tv_time, tv_upcoming_shifts, modNameTv;
    private LinearLayout ll_reward, ll_case_report, ll_resource;
    private Button btn_duty_status;
    private AdvanceDrawerLayout drawer;
    private RoundedImageView iv_updatedShift;
    private RecyclerView rr_announcement;
    private anouncementAdaptor anouncementAdaptor;
    private HelpRequestListAdaptor helpRequestListAdaptor;
    private Dialog dialog;
    private Dialog dialog1;
    private FingerprintManager fingerprintManager = null;
    private CardView cardView, cardViewMod;
    private ProgressDialog progressDialog;
    boolean isResourceClicked = false;


    String email;
    String contactNo;
    String startDate;
    String endDate;
    String Language;
    String RequestMessage;

    private boolean isHaveAlert = false;
    private ArrayList<ChannelListModel.Datum> channelList = new ArrayList<>();
    private ChatClientManager chatClientManager;
    private ChatClient chatClient;
    private String identity = "";
    private ChannelManager channelManager;
    private Channels channelsObject;
    private ChanModel chanModel;
    Messages messagesObject;
    private TextView timerTV;
    private CardView timeCard;
    String helpID;
    Singleton singleton;

    Dialog dutyDialog;
    boolean CheckIN;
    UnregistrationListener registrationListener = registrationListener();


    @Override
    public void onBackPressed() {
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

    @SuppressLint("WrongConstant")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_volunteer_home);

        TwilioChatApplication.setLocale(VolunteerHome.this, "en", false);
        LocalBroadcastManager.getInstance(this).registerReceiver(mMessageReceiver,
                new IntentFilter("custom-message"));
        channelManager = ChannelManager.getInstance();

        dutyDialog = new Dialog(this);
//        openDutyTimer(dutyDialog,3000);

        drawer = findViewById(R.id.drawer_layout);
        handBurger = findViewById(R.id.handBurger);
        profilePic = findViewById(R.id.profilePic);
        name = findViewById(R.id.name);
        iv_silver = findViewById(R.id.iv_silver);
        iv_gold = findViewById(R.id.iv_gold);
        iv_platinum = findViewById(R.id.iv_platinum);
        rewardTv = findViewById(R.id.rewardTv);
        ll_reward = findViewById(R.id.ll_reward);
        btn_duty_status = findViewById(R.id.btn_duty_status);
        rr_announcement = findViewById(R.id.rr_announcement);
        iv_updatedShift = findViewById(R.id.iv_updatedShift);
        tv_deate = findViewById(R.id.tv_deate);
        tv_language = findViewById(R.id.tv_language);
        tv_time = findViewById(R.id.tv_time);
        ll_case_report = findViewById(R.id.ll_case_report);
        ll_resource = findViewById(R.id.ll_resource);
        tv_upcoming_shifts = findViewById(R.id.tv_upcoming_shifts);
        cardView = findViewById(R.id.cardView);
        cardViewMod = findViewById(R.id.cardViewMod);
        modNameTv = findViewById(R.id.modNameTv);
        timerTV = findViewById(R.id.timerTV);
        timeCard = findViewById(R.id.timeCard);
        chanModel = ChanModel.getInstance();

//        channelManager = ChannelManager.getInstance();

//        new CountDownTimer(3000,1000){
//            public void onTick(long millisUntilFinished){
//                Log.d(TAG, "onTick: "+millisUntilFinished);
//            }
//            public  void onFinish(){
//                Log.d(TAG, "onFinish: ");
//            }
//        };

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            fingerprintManager = (FingerprintManager) this.getSystemService(Context.FINGERPRINT_SERVICE);
        }

        showActivityIndicator("Loading");
        getData();
        hitFirstLogin();
        hitMod();
        hitStatusAPi();
        hitUpcommingShift();
        hitProfileApi();
        hitAnnouncementApi();
        registerToServer();
//
        stopActivityIndicator();
        hitApiTimer();

        if (Singleton.get().isDialogDisplay())
            hitHelpRequestApi();

//        timer(30000,timerTV,timeCard);

        if (isHaveAlert) {
            alert("There is an immediate language need in Malayalam language. Can you back up XXXX and take over this case right now ?");
            alert("There is an immediate");
        }

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawer, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        rr_announcement.setLayoutManager(new LinearLayoutManager(this));

        handBurger.setOnClickListener(v -> drawer.openDrawer(Gravity.START));

//        profilePic.setOnClickListener(v -> checkTwilioClient("CI000894"));

        btn_duty_status.setOnClickListener(view -> onDuty(vounter_id));

        cardViewMod.setOnClickListener(view -> modDialog(email, contactNo, startDate, endDate));

        ll_reward.setOnClickListener(v -> openDialog());

        ll_case_report.setOnClickListener(v -> caseReport());

        iv_updatedShift.setOnClickListener(v -> upcomingSgift());

        ll_resource.setOnClickListener(v -> resource());

        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        ImageView navigationView_ = navigationView.getHeaderView(0).findViewById(R.id.nav_icon);
        TextView tv_Name = navigationView.getHeaderView(0).findViewById(R.id.tv_Name);
        Glide.with(VolunteerHome.this).load(profile).placeholder(R.drawable.ic_smalllogo).into(navigationView_);
        tv_Name.setText(VolunteerName);
        drawer.setViewScale(Gravity.START, 0.9f);
        drawer.setRadius(Gravity.START, 35);
        drawer.setViewElevation(Gravity.START, 20);

        ImageView imageView1 = navigationView.getHeaderView(0).findViewById(R.id.imageView24);

        TextView textView = navigationView.getHeaderView(0).findViewById(R.id.textView13);

        textView.setVisibility(View.GONE);

        imageView1.setVisibility(View.GONE);
    }

    private void registerToServer() {
        Intent intent = new Intent(getBaseContext(), RegistrationServiceVolunteer.class);
        intent.putExtra("volID", vounter_id);
        intent.putExtra("caseID", "");
        startService(intent);

    }

    @Override
    public void onDestroy() {
        /*
         * Tear down audio device management and restore previous volume stream
         */
        if (chatClientManager != null) {
            new Handler().post(() -> {
                chatClientManager.shutdown();
                TwilioChatApplication.get().getChatClientManager().setChatClient(null);
            });
        }
        super.onDestroy();
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
                hitCheckoutAPI(vounter_id, "CheckOut");
                //VolunterLogin();
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

    private void VolunteerHome() {
        Intent i = new Intent(getApplicationContext(), VolunteerHome.class);
        i.putExtra("vounter_id", vounter_id);
        i.putExtra("profile", profile);
        i.putExtra("VolunteerName", VolunteerName);
        startActivity(i);
        finish();
        drawer.closeDrawer(GravityCompat.START);
    }


    private void alert(String message) {
        final android.app.AlertDialog.Builder alert;
        alert = new android.app.AlertDialog.Builder(VolunteerHome.this);
        final LayoutInflater inflater = LayoutInflater.from(VolunteerHome.this);
        View alertLayout = inflater.inflate(R.layout.volunteer_alert_dialog, null);
        ImageView closeImg1 = alertLayout.findViewById(R.id.closeBtn1);
        TextView messageText = alertLayout.findViewById(R.id.messageText);
        Button done = alertLayout.findViewById(R.id.btn_yes);
        alert.setView(alertLayout);
        alert.setCancelable(false);
        final android.app.AlertDialog dialog = alert.create();
        messageText.setText(message);
        dialog.setCanceledOnTouchOutside(false);
        closeImg1.setOnClickListener(v -> dialog.dismiss());
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        if (!VolunteerHome.this.isFinishing() && !VolunteerHome.this.isDestroyed()) {
            dialog.show();
        }

    }

    private void VolunterLogin() {
        Intent i = new Intent(getApplicationContext(), loginActivity.class);
        startActivity(i);
        finish();
        drawer.closeDrawer(GravityCompat.START);
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

    public BroadcastReceiver mMessageReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String caseID = intent.getStringExtra("caseID");
            helpID = intent.getStringExtra("id");
            Toast.makeText(context, "Mani" + caseID, Toast.LENGTH_SHORT).show();
            hithelpAcceptAPI(helpID);
            checkTwilioClient(caseID);
        }
    };


    private void checkTwilioClient(String caseID) {
        try {
            showActivityIndicator(getResources().getString(R.string.loading_channels_message));
            chatClientManager = TwilioChatApplication.get().getChatClientManager();
            initializeClient(caseID);
        } catch (Exception e) {
            Log.d(TAG, "checkTwilioClient: " + e.getMessage());
        }

    }

    private void initializeClient(String caseID) {
        //reg vol
        chatClientManager.connectClient(vounter_id, new TaskCompletionListener<Void, String>() {
            @Override
            public void onSuccess(Void aVoid) {
                chatClientManager.getChatClient().registerFCMToken(new ChatClient.FCMToken(SharedPreferencesUtils.getFCMToken()), new StatusListener() {
                    @Override
                    public void onSuccess() {
                        Log.d("Register FCM to client", "onSuccess: Successful");
                    }
                });
                populateChannels(caseID);
            }

            @Override
            public void onError(String errorMessage) {
                stopActivityIndicator();
                StyleableToast.makeText(VolunteerHome.this, "Client connection error: " + errorMessage + "\n Check Internet Connection..!"
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
                StyleableToast.makeText(VolunteerHome.this, "Client connection error: " + errorMessage + "\n Check Internet Connection..!"
                        , Toast.LENGTH_LONG, R.style.mytoast).show();
            }
        });
    }

    private void populateChannels(String caseID) {
        channelsObject = chatClientManager.getChatClient().getChannels();
        channelsObject.getChannel(caseID, new CallbackListener<Channel>() {
            @Override
            public void onSuccess(Channel channel) {
                Log.d(TAG, "onSuccess: " + channel.getStatus());
                if (channel.getStatus() == Channel.ChannelStatus.JOINED) {
                    Log.d(TAG, "onSuccess: True");
                    chanModel.setChannel(channel);
//                            messagesObject = channel.getMessages();
//                            Toast.makeText(VolunteerHome.this, ""+messagesObject.getLastConsumedMessageIndex(), Toast.LENGTH_SHORT).show();
                    if (channel.getFriendlyName().equals(chanModel.getChannel().getFriendlyName())) {
                        Intent intent = new Intent(VolunteerHome.this, ChatActivity.class);
                        intent.putExtra("volID", vounter_id);
                        intent.putExtra("caseID", "");
                        startActivity(intent);
                        stopActivityIndicator();
                    }
//                    stopActivityIndicator();
                } else {
                    Log.d(TAG, "onSuccess: false");
                    chanModel.setChannel(channel);
                    if (channel.getFriendlyName().equals(chanModel.getChannel().getFriendlyName())) {
                        Intent intent = new Intent(VolunteerHome.this, ChatActivity.class);
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
                Toast.makeText(VolunteerHome.this, "", Toast.LENGTH_SHORT).show();
                channelsObject = chatClientManager.getChatClient().getChannels();
                channelsObject.getChannel(caseID, new CallbackListener<Channel>() {
                    @Override
                    public void onSuccess(Channel channel) {
                        Log.d(TAG, "onSuccess: " + channel.getStatus());
                        chanModel.setChannel(channel);
                        Members membersObject = channel.getMembers();
                        if (membersObject != null) {
                            membersObject.addByIdentity(vounter_id, new StatusListener() {
                                @Override
                                public void onSuccess() {
                                    Log.d(TAG, "onSuccess: " + chanModel.getChannel().getMembers().getMembersList());
                                    stopActivityIndicator();
                                    initializeClient(caseID);
                                }

                                @Override
                                public void onError(ErrorInfo errorInfo) {
                                    super.onError(errorInfo);
                                    Log.d(TAG, "onSuccess: " + errorInfo.getMessage());
                                    stopActivityIndicator();
                                }
                            });
                        } else {
                            Toast.makeText(VolunteerHome.this, "Something went wrong, try again", Toast.LENGTH_SHORT).show();
                            stopActivityIndicator();
                        }

                    }

                    @Override
                    public void onError(ErrorInfo errorInfo) {
                        super.onError(errorInfo);
                        Toast.makeText(VolunteerHome.this, "errorInfo " + errorInfo, Toast.LENGTH_SHORT).show();
//                refreshChannels(caseID);
//                Log.d(TAG, "onError: "+channelManager.getChannels());
//                stopActivityIndicator();
                    }
                });
            }
        }, 3000);

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
        finish();
        drawer.closeDrawer(GravityCompat.START);
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

    private void caseReport() {
        Intent i = new Intent(getApplicationContext(), CaseReportActivity.class);
        i.putExtra("vounter_id", vounter_id);
        i.putExtra("profile", profile);
        i.putExtra("VolunteerName", VolunteerName);
        startActivity(i);
        drawer.closeDrawer(GravityCompat.START);
    }

    private void changeCheckINStatus() {
        SharedPreferences sharedPreferences = getSharedPreferences("Setting", MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean("CheckIN", true);
        editor.apply();
    }

    public void getCheckInStatus() {
        SharedPreferences pref = getSharedPreferences("Setting", Activity.MODE_PRIVATE);
        CheckIN = pref.getBoolean("CheckIN", false);
    }

    public void hitCheckoutAPI(String vounter_id, String status) {
        showActivityIndicator("Loading");
        api api = retrofit.retrofit.create(api.class);
        retrofit2.Call<MessageModel> checkin = api.checkin(vounter_id, status);
        checkin.enqueue(new Callback<MessageModel>() {
            @Override
            public void onResponse(retrofit2.Call<MessageModel> call, Response<MessageModel> response) {
                if (response.isSuccessful() && response.code() == 200 && response.body() != null) {
                    Log.i(TAG, "onResponse: Status : "+response.body().getStatus());
                    if (response.body().getStatus().equals("valid")) {
                        stopActivityIndicator();
                        changeCheckINStatus();
                        if (SharedPreferencesUtils.getAccessToken()!=null){
                            Voice.unregister(SharedPreferencesUtils.getAccessToken(), Voice.RegistrationChannel.FCM, SharedPreferencesUtils.getFCMToken(), registrationListener);
                        }
                        ChatClient client = TwilioChatApplication.get().getChatClientManager().getChatClient();
                        if (client != null) {
                            client.unregisterFCMToken(new ChatClient.FCMToken(SharedPreferencesUtils.getFCMToken()), new StatusListener() {
                                @Override
                                public void onSuccess() {
                                    Log.d(TAG, "onSuccess: UnRegistered from chat successfully");
                                    client.shutdown();
                                    TwilioChatApplication.get().getChatClientManager().setChatClient(null);
                                }
                            });
                        }
                        SharedPreferencesUtils.clearSharedPreferences();
                        VolunterLogin();
                    } else {
                        stopActivityIndicator();
                        SharedPreferencesUtils.clearSharedPreferences();
                        VolunterLogin();
                        //Toast.makeText(VolunteerHome.this, "Status Invalid you have not pressed the green button  " + response.body().getMessage() + " ", Toast.LENGTH_SHORT).show();
                    }
                }
            }

            @Override
            public void onFailure(retrofit2.Call<MessageModel> call, Throwable t) {
                stopActivityIndicator();
                Toast.makeText(VolunteerHome.this, "" + t, Toast.LENGTH_SHORT).show();
            }
        });
    }


    private void getData() {
        Intent intent = getIntent();
        vounter_id = intent.getStringExtra("vounter_id");
        if (vounter_id == null) {
            vounter_id = SharedPreferencesUtils.getVolunteerID();
        } else if (SharedPreferencesUtils.getVolunteerID() == null) {
            SharedPreferencesUtils.saveVolunteerID(vounter_id);
        }
        profile = intent.getStringExtra("profile");
        if (SharedPreferencesUtils.getVolunteerProfile() == null) {
            SharedPreferencesUtils.saveVolunteerProfile(profile);
        } else if (profile == null) {
            profile = SharedPreferencesUtils.getVolunteerProfile();
        }
        VolunteerName = intent.getStringExtra("VolunteerName");
        if (SharedPreferencesUtils.getVolunteerName() == null) {
            SharedPreferencesUtils.saveVolunteerName(VolunteerName);
        } else if (VolunteerName == null) {
            VolunteerName = SharedPreferencesUtils.getVolunteerName();
        }
        //password_login_first = intent.getStringExtra("password_login_first");
    }

    private void setDutyStatus(String dutyStatus) {
        if (dutyStatus.equals("1")) {
            btn_duty_status.setBackgroundResource(R.drawable.ic_duty_btn);
            btn_duty_status.setText("ON DUTY");
            btn_duty_status.setVisibility(View.VISIBLE);
            checkInApi(vounter_id, "CheckIn");
        } else {
            btn_duty_status.setVisibility(View.GONE);
            //checkInApi(vounter_id, "CheckIn");
        }
    }

    private void setView(String profile, String VolunteerName, String total_rewards) {
        //hitProfileApi();
        Glide.with(getApplicationContext()).load(profile).placeholder(R.drawable.ic_smalllogo).into(profilePic);
        name.setText(VolunteerName);
        setRewards(Integer.parseInt(total_rewards));
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

                    Log.e("sssss", "err : " + dutyStatus+"  "+vounter_id);
                } else {
                    Log.d("sssss", "err : " + response.errorBody());
                }
            }

            @Override
            public void onFailure(Call<ScheduleStatusModel> call, Throwable t) {
//                stopActivityIndicator();
                Log.d("sssss", "err " + t.getMessage());
                StyleableToast.makeText(VolunteerHome.this, t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void hitFirstLogin() {
        api api = retrofit.retrofit.create(api.class);
        Call<MessageModel> call = api.firstLogin(vounter_id);
        call.enqueue(new Callback<MessageModel>() {
            @Override
            public void onResponse(Call<MessageModel> call, Response<MessageModel> response) {
                if (response.isSuccessful()) {
                    password_login_first = response.body().getMessage();
                    Log.d(password_login_first, "onResponse: " + password_login_first);
                    if (password_login_first.equals("Yes")) {
                        changePassword();
                    }
                } else {
                    Log.d("sssss", "err : " + response.errorBody());
                }
            }

            @Override
            public void onFailure(Call<MessageModel> call, Throwable t) {
                Log.d("sssss", "err : " + t.getMessage());
            }
        });
    }

    private void hitMod() {
        api api = retrofit.retrofit.create(api.class);
        Call<ModModel> call = api.modAPI();
        call.enqueue(new Callback<ModModel>() {
            @Override
            public void onResponse(Call<ModModel> call, Response<ModModel> response) {
                if (response.isSuccessful()) {
                    modNameTv.setText(response.body().getName());
                    email = response.body().getEmail();
                    contactNo = response.body().getContactNo();
                    startDate = response.body().getStartDate();
                    endDate = response.body().getEndDate();
                } else {
                    Log.d("sssss", "err : " + response.errorBody());
                }
            }

            @Override
            public void onFailure(Call<ModModel> call, Throwable t) {
                Log.d("sssss", "err : " + t.getMessage());
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

    private void hitProfileApi() {
//        showActivityIndicator("Loading Profile");
        api api = retrofit.retrofit.create(api.class);
        Call<List<ProfileInfoModel>> call = api.volunteer_info(vounter_id);
        Log.d("sssss", "vounter_id : " + vounter_id);
        call.enqueue(new Callback<List<ProfileInfoModel>>() {
            @Override
            public void onResponse(Call<List<ProfileInfoModel>> call, Response<List<ProfileInfoModel>> response) {
//                stopActivityIndicator();
                if (response.isSuccessful()) {
                    assert response.body() != null;
                    List<ProfileInfoModel> pro = response.body();
                    for (ProfileInfoModel infoModel : pro) {
                        VolunteerName = infoModel.getVname();
                        total_rewards = infoModel.getTotalRewards();
                        profile = infoModel.getProfilePic();
                    }
                    setView(profile, VolunteerName, total_rewards);
                    setRewards(Integer.parseInt(total_rewards));
                } else {
                    Log.d("sssss", "err " + response.errorBody());
                }
            }

            @Override
            public void onFailure(Call<List<ProfileInfoModel>> call, Throwable t) {
//                stopActivityIndicator();
                Log.d("sssss", "err " + t.getMessage());
            }
        });
    }

    private void hitAnnouncementApi() {
//        showActivityIndicator("Loading Announcements");
        api api = retrofit.retrofit.create(api.class);
        Call<List<Announcement>> call = api.Admin_notes1();
        call.enqueue(new Callback<List<Announcement>>() {
            @Override
            public void onResponse(Call<List<Announcement>> call, Response<List<Announcement>> response) {
//                stopActivityIndicator();
                if (response.isSuccessful()) {
                    announcementsModel = new ArrayList<>(response.body());
                    anouncementAdaptor = new anouncementAdaptor(announcementsModel, VolunteerHome.this);
                    rr_announcement.setAdapter(anouncementAdaptor);
                } else {
                    Toast.makeText(VolunteerHome.this, "err : " + response.errorBody(), Toast.LENGTH_SHORT).show();
                    Log.d("ADMIN NOTES", "err : " + response.errorBody());
                }
            }

            @Override
            public void onFailure(Call<List<Announcement>> call, Throwable t) {
//                stopActivityIndicator();
                Log.d("ADMIN NOTES", "err : " + t.getMessage());
                Toast.makeText(VolunteerHome.this, "err : " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void hitHelpRequestApi() {
//        showActivityIndicator("Loading Announcements");
        api api = retrofit.retrofit.create(api.class);
        Call<HelpRequestModel> call = api.helpvictimlist();
        call.enqueue(new Callback<HelpRequestModel>() {
            @Override
            public void onResponse(@NotNull Call<HelpRequestModel> call, @NotNull Response<HelpRequestModel> response) {
                if (response.isSuccessful() && response.body() != null) {
                    if (response.body().getData().size() != 0) {
                        List<HelpRequestModel.Datum> newHelpRequestModels = response.body().getData();
                        for (HelpRequestModel.Datum newHelpRequestModel : response.body().getData()) {
                            newHelpRequestModel.setMessage(response.body().getMessage());
                        }
                        //helpRequestModels = new ArrayList<>(response.body().getData());
                        dialog1 = new Dialog(VolunteerHome.this);
                        helpRequestListAdaptor = new HelpRequestListAdaptor(newHelpRequestModels, VolunteerHome.this, dialog1, vounter_id);
                        openHelpListDialog(helpRequestListAdaptor, dialog1);
                    } else {
                        Log.d(TAG, "onResponse: HelpVictimResponse was empty");
                    }

                } else {
                    Toast.makeText(VolunteerHome.this, "err : " + response.errorBody(), Toast.LENGTH_SHORT).show();
                    Log.d("HelpVictimList", "err : " + response.errorBody());
                }
            }

            @Override
            public void onFailure(@NotNull Call<HelpRequestModel> call, @NotNull Throwable t) {
//                stopActivityIndicator();
                Log.d("HelpVictimList", "err : " + t.getMessage());
                Toast.makeText(VolunteerHome.this, "err : " + t.getMessage(), Toast.LENGTH_SHORT).show();
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
                StyleableToast.makeText(VolunteerHome.this, t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void resource() {
        Intent i = new Intent(getApplicationContext(), VolunterResourceActivity.class);
        i.putExtra("vounter_id", vounter_id);
        i.putExtra("profile", profile);
        i.putExtra("VolunteerName", VolunteerName);
        startActivity(i);
        drawer.closeDrawer(GravityCompat.START);
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
                    Toast.makeText(VolunteerHome.this, "err: " + response.errorBody(), Toast.LENGTH_SHORT).show();
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

    private void modDialog(String email, String contactNo, String startDate, String endDate) {
        dialog = new Dialog(this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(false);
        dialog.setContentView(R.layout.mod_dialog);
        ImageView i = dialog.findViewById(R.id.iv_dialog_close);
        TextView tv_from = dialog.findViewById(R.id.tv_from);
        TextView tv_to = dialog.findViewById(R.id.tv_to);
        TextView tv_email = dialog.findViewById(R.id.tv_email);
        TextView tv_phone = dialog.findViewById(R.id.tv_phone);

        tv_email.setText(email);
        tv_from.setText(startDate);
        tv_to.setText(endDate);
        tv_phone.setText(contactNo);

        tv_email.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent emailIntent = new Intent(Intent.ACTION_SENDTO, Uri.fromParts(
                        "mailto", email, null));
                emailIntent.putExtra(Intent.EXTRA_SUBJECT, "Subject");
                emailIntent.putExtra(Intent.EXTRA_TEXT, "Body");
                startActivity(Intent.createChooser(emailIntent, "Send Email"));
            }
        });

        tv_phone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_DIAL);
                intent.setData(Uri.parse("tel:" + contactNo));
                startActivity(intent);
            }
        });

        i.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        dialog.show();
        dialog.getWindow().setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);
    }

    private void onDuty(String vounter_id) {
        showActivityIndicator("Loading");
        dutyStatusAvailableAPI(vounter_id);
    }

    private void dutyStatusAvailableAPI(String vounter_id) {
        api api = retrofit.retrofit.create(com.shamsaha.app.api.api.class);
        Call<MessageModel> call = api.volunteerAvailability(vounter_id, "Available");
        call.enqueue(new Callback<MessageModel>() {
            @Override
            public void onResponse(Call<MessageModel> call, Response<MessageModel> response) {
                if (response.isSuccessful() && (response.code() == 200)) {
                    assert response.body() != null;
                    if (response.body().getStatus().equals("valid")) {
                        stopActivityIndicator();
                        Intent intent = new Intent(VolunteerHome.this, DutyShiftActivity1.class);
                        intent.putExtra("volID", vounter_id);
                        intent.putExtra("volID1", "on_duty");
                        intent.putExtra("vounter_id", vounter_id);
                        intent.putExtra("profile", profile);
                        intent.putExtra("VolunteerName", VolunteerName);
                        startActivity(intent);
                    } else {
                        stopActivityIndicator();
                        Toast.makeText(VolunteerHome.this, response.body().getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }
            }

            @Override
            public void onFailure(Call<MessageModel> call, Throwable t) {

            }
        });
    }

    private void checkInApi(String volunteer_id, String status) {
        showActivityIndicator("Loading");
        api api = retrofit.retrofit.create(api.class);
        retrofit2.Call<MessageModel> checkin = api.checkin(volunteer_id, status);
        checkin.enqueue(new Callback<MessageModel>() {
            @Override
            public void onResponse(@NonNull Call<MessageModel> call, @NonNull Response<MessageModel> response) {
                if (response.isSuccessful() && response.code() == 200 && response.body() != null) {
                    if (response.body().getStatus().equals("valid")) {
                        Log.d(TAG, "onResponse: Check In is Valid");
                        stopActivityIndicator();
                        changeCheckINStatus();
                    } else {
                        stopActivityIndicator();
                        Toast.makeText(VolunteerHome.this, " " + response.body().getMessage() + " ", Toast.LENGTH_SHORT).show();
                    }
                }
            }

            @Override
            public void onFailure(@NonNull Call<MessageModel> call, @NonNull Throwable t) {
                stopActivityIndicator();
                Toast.makeText(VolunteerHome.this, "" + t, Toast.LENGTH_SHORT).show();
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

    public void saveData(boolean s) {
        SharedPreferences sharedPreferences = getSharedPreferences("Setting", MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean("Biometric", s);
        editor.apply();
    }

    public boolean loadData() {
        SharedPreferences pref = getSharedPreferences("Setting", Activity.MODE_PRIVATE);
        boolean Biometric = pref.getBoolean("Biometric", false);
        return Biometric;
    }

    private void showActivityIndicator(final String message) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                progressDialog = new ProgressDialog(VolunteerHome.this);
                progressDialog.setMessage(message);
                progressDialog.show();
                progressDialog.setCanceledOnTouchOutside(false);
                progressDialog.setCancelable(false);
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
        closeImg2.setOnClickListener(v -> {
            dialog1.dismiss();
            Singleton.get().setDialogDisplay(false);
        });
        dialog1.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog1.getWindow().setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);
        if (!VolunteerHome.this.isFinishing() && !VolunteerHome.this.isDestroyed()) {
            dialog1.show();
        }
    }

    public void timer(int sec, TextView Timer, CardView timeCard) {
        timeCard.setVisibility(View.VISIBLE);
        try {
            new CountDownTimer(sec, 1000) {
                public void onTick(long millisUntilFinished) {
                    Timer.setText(" " + millisUntilFinished / 60000 + ":" + millisUntilFinished % 60000 / 1000 + " ");
                }

                public void onFinish() {
//                    Timer.setText("done!");
                    hitStatusAPi();
                    timeCard.setVisibility(View.GONE);
                }
            }.start();
        } catch (Exception e) {
            Log.d(TAG, "openDutyTimer: " + e);
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
        CardView taxi = dialog.findViewById(R.id.taxi);
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
//                ramada.setVisibility(View.GONE);
//                workstation.setVisibility(View.GONE);
//                webView.setVisibility(View.VISIBLE);
//                webView.loadUrl(url);
                Intent i = new Intent(VolunteerHome.this, WebViewActivity.class);
                i.putExtra("type", "volunteer");
                i.putExtra("url", url);
                startActivity(i);

            }
        });

        workstation.setOnClickListener(v -> {
//            ramada.setVisibility(View.GONE);
//            workstation.setVisibility(View.GONE);
//            webView.setVisibility(View.VISIBLE);
//            webView.loadUrl(baseURL.BaseURL_API+"services/wokstation?volunteer_id=" + vounter_id + "&case_id=" + CaseID);
            Intent i = new Intent(VolunteerHome.this, WebViewActivity.class);
            i.putExtra("type", "volunteer");
            i.putExtra("url", baseURL.BaseURL_API + "services/wokstation?volunteer_id=" + vounter_id + "&case_id=" + CaseID);
            startActivity(i);

        });
        taxi.setOnClickListener(view -> {
            Intent i = new Intent(VolunteerHome.this, WebViewActivity.class);
            i.putExtra("type", "volunteer");
            i.putExtra("url", baseURL.BaseURL_API + "services/bahrainTaxi?volunteer_id=" + vounter_id + "&case_id=" + CaseID);
            startActivity(i);
        });



        webView.setWebViewClient(new WebViewClient() {
            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                super.onPageStarted(view, url, favicon);
//                Toast.makeText(PayMentActivity.this, url, Toast.LENGTH_SHORT).show();
                Log.d("nnnnnnnnnn", url);
            }
        });


        webView.setWebChromeClient(new WebChromeClient() {
            public void onProgressChanged(WebView view, int newProgress) {
                progressBar5.setVisibility(View.VISIBLE);
                if (newProgress == 100) {
                    progressBar5.setVisibility(View.GONE);
                }
                Log.d("mmmm", "ddd" + newProgress);
            }
        });


        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        dialog.getWindow().setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.MATCH_PARENT);

        if (!VolunteerHome.this.isFinishing() && !VolunteerHome.this.isDestroyed()) {
            dialog.show();
        }
    }

    private void openLanguageDialog() {
        drawer.closeDrawer(GravityCompat.START);
        final android.app.AlertDialog.Builder alert;
        alert = new android.app.AlertDialog.Builder(VolunteerHome.this);
        final LayoutInflater inflater = LayoutInflater.from(VolunteerHome.this);
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

        if (!VolunteerHome.this.isFinishing() && !VolunteerHome.this.isDestroyed()) {
            dialog.show();
        }
    }

    private void openBackupDialog() {
        drawer.closeDrawer(GravityCompat.START);
        final android.app.AlertDialog.Builder alert;
        alert = new android.app.AlertDialog.Builder(VolunteerHome.this);
        final LayoutInflater inflater = LayoutInflater.from(VolunteerHome.this);
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
                    Toast.makeText(VolunteerHome.this, "Fill the form", Toast.LENGTH_SHORT).show();
                } else {
                    hitSendLanguageRequest("", CaseID1, message.getText().toString(), support.toString());
                }


                dialog.dismiss();
            }
        });

        if (!VolunteerHome.this.isFinishing() && !VolunteerHome.this.isDestroyed()) {
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

                        ArrayAdapter<AdditionalLanguageSpinnerAdaptor> arrayAdapter = new ArrayAdapter<>(VolunteerHome.this, android.R.layout.simple_spinner_item, location);
                        arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

                        spinner.setAdapter(arrayAdapter);

                        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                            @Override
                            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                                AdditionalLanguageSpinnerAdaptor spinnerAdaptor = (AdditionalLanguageSpinnerAdaptor) parent.getSelectedItem();
//                                Toast.makeText(VolunterResourceActivity.this, "ID : "+spinnerAdaptor.getId()+"\n" +
//                                        "\nLocation : "+spinnerAdaptor.getLocation(), Toast.LENGTH_SHORT).show();

                                editText.setText("There is an immediate language need in " + spinnerAdaptor.getLanguage() +
                                        " language. Can you back up " + CaseID1 + " and take over this case right now ?");

                                Language = spinnerAdaptor.getLanguage();
                            }

                            @Override
                            public void onNothingSelected(AdapterView<?> parent) {

                            }
                        });

                    }

                } else {
                    Toast.makeText(VolunteerHome.this, "Something went wrong (OR) check your internet connection...!", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<List<AdditionalLanguageModel>> call, Throwable t) {
                Toast.makeText(VolunteerHome.this, "Something went wrong (OR) check your internet connection...!\n" + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void hitApiTimer() {
        api api = retrofit.retrofit.create(api.class);
        Call<MessageModel> call = api.timer(vounter_id);
        call.enqueue(new Callback<MessageModel>() {
            @Override
            public void onResponse(Call<MessageModel> call, Response<MessageModel> response) {
                if (response.isSuccessful() && response.code() == 200 && response.body() != null && response.body().getMessage() != null && response.body().getMessage().length() > 0) {
                    if (response.body().getStatus().equals("valid")) {
                        int sec = Integer.parseInt(response.body().getMessage().toString());
                        timer(sec, timerTV, timeCard);
                    }
                }
            }

            @Override
            public void onFailure(Call<MessageModel> call, Throwable t) {
                Toast.makeText(VolunteerHome.this, t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void hithelpAcceptAPI(String id) {
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
                Toast.makeText(VolunteerHome.this, t.getMessage(), Toast.LENGTH_SHORT).show();
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
                        if (channel.getScreenName() != null) {
                            CaseIDSpinnerAdaptor adaptor = new CaseIDSpinnerAdaptor(channel.getCaseId());
                            channelData.add(adaptor);
                        }

                        ArrayAdapter<CaseIDSpinnerAdaptor> adaptorArrayAdapter = new ArrayAdapter<>(VolunteerHome.this, android.R.layout.simple_spinner_item, channelData);
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
                    Toast.makeText(VolunteerHome.this, "Something went wrong (OR) check your internet connection...!", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<ChannelListModel> call, Throwable t) {
                Toast.makeText(VolunteerHome.this, "Something went wrong (OR) check your internet connection...!", Toast.LENGTH_SHORT).show();
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
                        if (channel.getScreenName() != null) {
                            CaseIDSpinnerAdaptor adaptor = new CaseIDSpinnerAdaptor(channel.getCaseId());
                            channelData.add(adaptor);
                        }
                        ArrayAdapter<CaseIDSpinnerAdaptor> adaptorArrayAdapter = new ArrayAdapter<>(VolunteerHome.this, android.R.layout.simple_spinner_item, channelData);
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
                    Toast.makeText(VolunteerHome.this, "Something went wrong (OR) check your internet connection...!", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<ChannelListModel> call, Throwable t) {
                Toast.makeText(VolunteerHome.this, "Something went wrong (OR) check your internet connection...!", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public void onChannelJoined(Channel channel) {

    }

    @Override
    public void onChannelInvited(Channel channel) {

    }

    @Override
    public void onChannelAdded(Channel channel) {

    }

    @Override
    public void onChannelUpdated(Channel channel, Channel.UpdateReason updateReason) {

    }

    @Override
    public void onChannelDeleted(Channel channel) {

    }

    @Override
    public void onChannelSynchronizationChange(Channel channel) {

    }

    @Override
    public void onError(ErrorInfo errorInfo) {

    }

    @Override
    public void onUserUpdated(User user, User.UpdateReason updateReason) {

    }

    @Override
    public void onUserSubscribed(User user) {

    }

    @Override
    public void onUserUnsubscribed(User user) {

    }

    @Override
    public void onClientSynchronization(ChatClient.SynchronizationStatus synchronizationStatus) {

    }

    @Override
    public void onNewMessageNotification(String s, String s1, long l) {
    }

    @Override
    public void onAddedToChannelNotification(String s) {

    }

    @Override
    public void onInvitedToChannelNotification(String s) {

    }

    @Override
    public void onRemovedFromChannelNotification(String s) {

    }

    @Override
    public void onNotificationSubscribed() {

    }

    @Override
    public void onNotificationFailed(ErrorInfo errorInfo) {

    }

    @Override
    public void onConnectionStateChange(ChatClient.ConnectionState connectionState) {

    }

    @Override
    public void onTokenExpired() {

    }

    @Override
    public void onTokenAboutToExpire() {

    }

    private UnregistrationListener registrationListener() {
        return new UnregistrationListener() {
            @Override
            public void onUnregistered(@NonNull String accessToken, @NonNull String fcmToken) {
                Log.d(TAG, "Successfully Unregistered FCM " + fcmToken);
            }

            @Override
            public void onError(@NonNull RegistrationException error,
                                @NonNull String accessToken,
                                @NonNull String fcmToken) {
                String message = String.format(
                        Locale.US,
                        "Registration Error: %d, %s",
                        error.getErrorCode(),
                        error.getMessage());
                Log.e(TAG, message);
                //Snackbar.make(line, message, Snackbar.LENGTH_LONG).show();
                Toast.makeText(VolunteerHome.this, message, Toast.LENGTH_SHORT).show();
            }
        };
    }
}