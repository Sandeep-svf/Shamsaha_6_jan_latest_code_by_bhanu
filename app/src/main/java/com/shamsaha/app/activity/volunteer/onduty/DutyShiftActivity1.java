package com.shamsaha.app.activity.volunteer.onduty;

import android.Manifest;
import android.app.AlertDialog;
import android.app.NotificationManager;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.media.AudioManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.SystemClock;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.Chronometer;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.messaging.FirebaseMessaging;
import com.muddzdev.styleabletoast.StyleableToast;
import com.shamsaha.app.ApiModel.ChannelListModel;
import com.shamsaha.app.ApiModel.volunter.MessageModel;
import com.shamsaha.app.R;
import com.shamsaha.app.activity.CallHelper.Constants;
import com.shamsaha.app.activity.CallHelper.IncomingCallNotificationService;
import com.shamsaha.app.activity.CallHelper.SoundPoolManager;
import com.shamsaha.app.activity.ChatHelper.Application.TwilioChatApplication;
import com.shamsaha.app.activity.ChatHelper.ChanModel;
import com.shamsaha.app.activity.ChatHelper.Channel.ChannelManager;
import com.shamsaha.app.activity.ChatHelper.Channel.LoadChannelListener;
import com.shamsaha.app.activity.ChatHelper.Message.ChatClientManager;
import com.shamsaha.app.activity.ChatHelper.listener.TaskCompletionListener;
import com.shamsaha.app.activity.PublicPart.homeScreenActivity;
import com.shamsaha.app.activity.Victem.ChatActivity;
import com.shamsaha.app.adaptor.ChannelAdapter1;
import com.shamsaha.app.api.api;
import com.shamsaha.app.retrofitAdaptor.AccessTokenResponse;
import com.shamsaha.app.retrofitAdaptor.RetrofitInstance;
import com.shamsaha.app.retrofitAdaptor.ShamsahaService;
import com.shamsaha.app.retrofitAdaptor.retrofit;
import com.shamsaha.app.utils.ConstantsURL.baseURL;
import com.shamsaha.app.utils.SharedPreferencesUtils;
import com.twilio.audioswitch.AudioDevice;
import com.twilio.audioswitch.AudioSwitch;
import com.twilio.chat.CallbackListener;
import com.twilio.chat.Channel;
import com.twilio.chat.Channels;
import com.twilio.chat.ChatClient;
import com.twilio.chat.ChatClientListener;
import com.twilio.chat.ErrorInfo;
import com.twilio.chat.Members;
import com.twilio.chat.StatusListener;
import com.twilio.chat.User;
import com.twilio.voice.Call;
import com.twilio.voice.CallException;
import com.twilio.voice.CallInvite;
import com.twilio.voice.ConnectOptions;
import com.twilio.voice.RegistrationException;
import com.twilio.voice.RegistrationListener;
import com.twilio.voice.Voice;

import org.jetbrains.annotations.NotNull;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Set;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.lifecycle.Lifecycle;
import androidx.lifecycle.ProcessLifecycleOwner;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import kotlin.Unit;
import kotlin.jvm.functions.Function2;
import retrofit2.Callback;
import retrofit2.Response;

public class DutyShiftActivity1 extends AppCompatActivity implements ChatClientListener, View.OnClickListener {

    private static final String TAG = "CallActivity";
    //    private static final String identity = "Deva";
    private String identity = "Deva";
    private String vounter_id;
    private String profile;
    private String VolunteerName;
    private String callTo = "";

    private static final String TWILIO_ACCESS_TOKEN_SERVER_URL = baseURL.BaseURLTwilioVoice;
    private static final int MIC_PERMISSION_REQUEST_CODE = 1;
    private String accessToken;
    private AudioSwitch audioSwitch;
    private int savedVolumeControlStream;
    private MenuItem audioDeviceMenuItem;

    private CardView checkin;

    private ImageView iv_audio_mode;

    private boolean isReceiverRegistered = false;
    private boolean isReceiverRegistered1 = false;
    private DutyShiftActivity1.VoiceBroadcastReceiver voiceBroadcastReceiver;
    private DutyShiftActivity1.LocalBroadcastReceiver localBroadcastReceiver;
    HashMap<String, String> params = new HashMap<>();

    private CoordinatorLayout coordinatorLayout;
    private FloatingActionButton callActionFab;
    private FloatingActionButton hangupActionFab;
    private FloatingActionButton holdActionFab;
    private FloatingActionButton muteActionFab;
    private Chronometer chronometer;

    private NotificationManager notificationManager;
    private AlertDialog alertDialog;
    private CallInvite activeCallInvite;
    private Call activeCall;
    private int activeCallNotificationId;

    RegistrationListener registrationListener = registrationListener();
    Call.Listener callListener = callListener();

    private RecyclerView recyclerView;
    private String onDutyClicked = "";
    private LinearLayout layout_side_bar;
    private ChanModel chanModel;
    private static ChannelManager channelManager;
    private ProgressDialog progressDialog;
    private ChatClientManager chatClientManager;
    private Channels channelsObject;
    private static ChannelAdapter1 channelAdapter;
    public static final String ACTION_CHANNEL_CREATED = "channel_created";
    public static final String ACTION_CHANNEL = "channel";
    private String clickedChannelId = "";
    private ArrayList<ChannelListModel.Datum> channelList = new ArrayList<>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_duty_shift);

        Intent intent = getIntent();
        identity = intent.getStringExtra("volID");
        identity = SharedPreferencesUtils.getVolunteerID();
        onDutyClicked = intent.getStringExtra("volID1");
        vounter_id = intent.getStringExtra("vounter_id");
        profile = intent.getStringExtra("profile");
        VolunteerName = intent.getStringExtra("VolunteerName");
//        callTo = intent.getStringExtra("volID");
//        Toast.makeText(this, "   "+identity, Toast.LENGTH_SHORT).show();
        Window window = getWindow();
        window.addFlags(WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED
                | WindowManager.LayoutParams.FLAG_TURN_SCREEN_ON
                | WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);

        coordinatorLayout = findViewById(R.id.coordinator_layout);
        callActionFab = findViewById(R.id.call_action_fab);
        hangupActionFab = findViewById(R.id.hangup_action_fab);
        holdActionFab = findViewById(R.id.hold_action_fab);
        muteActionFab = findViewById(R.id.mute_action_fab);
        chronometer = findViewById(R.id.chronometer);
        checkin = findViewById(R.id.checkin);
        iv_audio_mode = findViewById(R.id.iv_audio_mode);
        callActionFab.setOnClickListener(callActionFabClickListener());
        hangupActionFab.setOnClickListener(hangupActionFabClickListener());
        holdActionFab.setOnClickListener(holdActionFabClickListener());
        muteActionFab.setOnClickListener(muteActionFabClickListener());
        iv_audio_mode.setOnClickListener(audioSelectListener());
        notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        voiceBroadcastReceiver = new DutyShiftActivity1.VoiceBroadcastReceiver();
        localBroadcastReceiver = new DutyShiftActivity1.LocalBroadcastReceiver();
        registerReceiver();
        registerReceiver1();
        audioSwitch = new AudioSwitch(getApplicationContext());
        savedVolumeControlStream = getVolumeControlStream();
        setVolumeControlStream(AudioManager.STREAM_VOICE_CALL);

        chanModel = ChanModel.getInstance();
        channelManager = ChannelManager.getInstance();
        layout_side_bar = findViewById(R.id.layout_side_bar);
        recyclerView = findViewById(R.id.chat_channel_list);
        if (onDutyClicked == null) {
            setChatUI();
        } else {
            if (onDutyClicked.isEmpty()) {
                resetUI();
            } else {
                setChatUI();
            }
        }

        checkin.setOnClickListener(view -> {
            openCheckoutDialog();
        });

        handleIncomingCallIntent(getIntent());

        if (!checkPermissionForMicrophone()) {
            requestPermissionForMicrophone();
        } else {
            retrieveAccessToken();
        }

//        checkTwilioClient();
    }


    private void openCheckoutDialog() {
        final android.app.AlertDialog.Builder alert;
        alert = new android.app.AlertDialog.Builder(DutyShiftActivity1.this);
        final LayoutInflater inflater = LayoutInflater.from(DutyShiftActivity1.this);
        View alertLayout = inflater.inflate(R.layout.checkout_dialog, null);
        ImageView closeImg = alertLayout.findViewById(R.id.closeBtn);
        Button btnYes = alertLayout.findViewById(R.id.btn_yes);
        Button btnNo = alertLayout.findViewById(R.id.btn_no);
        alert.setView(alertLayout);
        alert.setCancelable(false);
        final android.app.AlertDialog dialog = alert.create();
        dialog.setCanceledOnTouchOutside(false);
        closeImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        btnYes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                hitCheckoutAPI(vounter_id, "CheckOut");
//                onBackPressed();
//                Intent intent = new Intent(DutyShiftActivity1.this, VolunteerHome.class);
//                intent.putExtra("vounter_id", vounter_id);
//                intent.putExtra("profile", profile);
//                intent.putExtra("VolunteerName", VolunteerName);
//                startActivity(intent);
//                finish();
            }
        });
        btnNo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        if (!DutyShiftActivity1.this.isFinishing() && !DutyShiftActivity1.this.isDestroyed()) {
            dialog.show();
        }
    }

    private void changeCheckINStatus() {
        SharedPreferences sharedPreferences = getSharedPreferences("Setting", MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean("CheckIN", true);
        editor.apply();
    }

    public void hitCheckoutAPI(String vounter_id, String status) {
        showActivityIndicator("Loading");
        api api = retrofit.retrofit.create(api.class);
        retrofit2.Call<MessageModel> checkin = api.checkin(vounter_id, status);
        checkin.enqueue(new Callback<MessageModel>() {
            @Override
            public void onResponse(retrofit2.Call<MessageModel> call, Response<MessageModel> response) {
                if (response.isSuccessful() && response.code() == 200 && response.body() != null) {
                    if (response.body().getStatus().equals("valid")) {
                        stopActivityIndicator();
                        changeCheckINStatus();
                        onBackPressed();
                    } else {
                        stopActivityIndicator();
                        Toast.makeText(DutyShiftActivity1.this, " " + response.body().getMessage() + " ", Toast.LENGTH_SHORT).show();
                    }
                }
            }

            @Override
            public void onFailure(retrofit2.Call<MessageModel> call, Throwable t) {
                stopActivityIndicator();
                Toast.makeText(DutyShiftActivity1.this, "" + t, Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void getChannelsList() {
        showActivityIndicator(getResources().getString(R.string.loading_channels_message));
        api api = retrofit.retrofit.create(api.class);
        retrofit2.Call<ChannelListModel> channelListModelCall = api.getChannelList();
        channelListModelCall.enqueue(new Callback<ChannelListModel>() {
            @Override
            public void onResponse(retrofit2.Call<ChannelListModel> call, Response<ChannelListModel> response) {
                if (response.isSuccessful() && response.code() == 200 && response.body() != null && response.body().getData() != null && response.body().getData().size() > 0) {
                    RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(DutyShiftActivity1.this);
                    recyclerView.setLayoutManager(layoutManager);
                    channelList.clear();
                    channelList.addAll(response.body().getData());
                    ArrayList<ChannelListModel.Datum> filterList = new ArrayList<>();
                    ArrayList<ChannelListModel.Datum> finalList = new ArrayList<>();
                    long twoDays = 60 * 60 * 48 * 1000L;
                    Date twoDayDate = new Date();
                    twoDayDate.setTime(twoDays);
                    for (ChannelListModel.Datum datum : response.body().getData()) {
                        /*Log.i("CASES", "Language: "+datum.getLanguage());
                        Log.i("CASES", "Volunteer Language: "+SharedPreferencesUtils.getVolunteerLanguage());
                        Log.i("CASES", "Item Connection type: "+datum.getConnection_type());*/
                        if (datum.getLanguage().equalsIgnoreCase(SharedPreferencesUtils.getVolunteerLanguage())) {
                            filterList.add(datum);
                            Log.i("CASES", "FilterList Item: " + datum.getLanguage());
                            Log.i("CASES", "FilterList Item Connection type: " + datum.getConnection_type());
                            Log.i("CASES", "FilterList Item Reported Date: " + datum.getReported_date());
                            Log.i("CASES", "STATUS: " + datum.getStatus());
                        }
                    }
                    Log.i("FINAL", "Filter size: " + filterList.size());

                    Handler handler = new Handler();
                    handler.post(() -> {
                        for (ChannelListModel.Datum datum : filterList) {
                            if (datum.getReported_date() != null) {
                                Date current = new Date(System.currentTimeMillis());
                                SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
                                try {
                                    Date date = simpleDateFormat.parse(datum.getReported_date());
                                    long datumDateInMillis = date.getTime();
                                    Log.i("EXTRACTED", "Time : " + datumDateInMillis);
                                    Log.i("EXTRACTED", "Difference: " + (current.getTime() - datumDateInMillis));
                                    Log.i("EXTRACTED", "TWODAYS: " + twoDays);
                                    if (!((current.getTime() - datumDateInMillis) >= twoDays)) {
                                        finalList.add(datum);
                                    }
                                } catch (ParseException e) {
                                    e.printStackTrace();
                                }
                            } else {
                                finalList.add(datum);
                            }
                        }
                        Log.i("FINAL", "size: " + finalList.size());

                        channelAdapter = new ChannelAdapter1(DutyShiftActivity1.this, finalList, DutyShiftActivity1.this);
                        recyclerView.setAdapter(channelAdapter);
                    });
                }
                runOnUiThread(() -> {
                    //channelAdapter.notifyDataSetChanged();
                    stopActivityIndicator();
                });
            }

            @Override
            public void onFailure(retrofit2.Call<ChannelListModel> call, Throwable t) {
            }
        });
    }

    private void setChatUI() {
        iv_audio_mode.setVisibility(View.GONE);
        chronometer.setVisibility(View.GONE);
        callActionFab.setVisibility(View.GONE);
        layout_side_bar.setVisibility(View.GONE);
        recyclerView.setVisibility(View.VISIBLE);
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        if (intent.getAction() == ACTION_CHANNEL_CREATED) {
            Log.e("jfbdskjn", "bdfbsfj");
        } else {
            handleIncomingCallIntent(intent);
        }
    }

    private class LocalBroadcastReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if (action.equals(ACTION_CHANNEL_CREATED)) {
                Log.e("jfbdskjn", "bdfbsfj");
            }
        }
    }


    private RegistrationListener registrationListener() {
        return new RegistrationListener() {
            @Override
            public void onRegistered(@NonNull String accessToken, @NonNull String fcmToken) {
                Log.d(TAG, "Successfully registered FCM " + fcmToken);
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
                Snackbar.make(coordinatorLayout, message, Snackbar.LENGTH_LONG).show();
            }
        };
    }

    private Call.Listener callListener() {
        return new Call.Listener() {
            /*
             * This callback is emitted once before the Call.Listener.onConnected() callback when
             * the callee is being alerted of a Call. The behavior of this callback is determined by
             * the answerOnBridge flag provided in the Dial verb of your TwiML application
             * associated with this client. If the answerOnBridge flag is false, which is the
             * default, the Call.Listener.onConnected() callback will be emitted immediately after
             * Call.Listener.onRinging(). If the answerOnBridge flag is true, this will cause the
             * call to emit the onConnected callback only after the call is answered.
             * See answeronbridge for more details on how to use it with the Dial TwiML verb. If the
             * twiML response contains a Say verb, then the call will emit the
             * Call.Listener.onConnected callback immediately after Call.Listener.onRinging() is
             * raised, irrespective of the value of answerOnBridge being set to true or false
             */
            @Override
            public void onRinging(@NonNull Call call) {
                Log.d(TAG, "Ringing");
                /*
                 * When [answerOnBridge](https://www.twilio.com/docs/voice/twiml/dial#answeronbridge)
                 * is enabled in the <Dial> TwiML verb, the caller will not hear the ringback while
                 * the call is ringing and awaiting to be accepted on the callee's side. The application
                 * can use the `SoundPoolManager` to play custom audio files between the
                 * `Call.Listener.onRinging()` and the `Call.Listener.onConnected()` callbacks.
                 */
                if (Constants.playCustomRingback) {
                    SoundPoolManager.getInstance(DutyShiftActivity1.this).playRinging();
                }
            }

            @Override
            public void onConnectFailure(@NonNull Call call, @NonNull CallException error) {
                audioSwitch.deactivate();
                if (Constants.playCustomRingback) {
                    SoundPoolManager.getInstance(DutyShiftActivity1.this).stopRinging();
                }
                Log.d(TAG, "Connect failure");
                String message = String.format(
                        Locale.US,
                        "Call Error: %d, %s",
                        error.getErrorCode(),
                        error.getMessage());
                Log.e(TAG, message);
                Snackbar.make(coordinatorLayout, message, Snackbar.LENGTH_LONG).show();
                resetUI();
            }

            @Override
            public void onConnected(@NonNull Call call) {
                audioSwitch.activate();
                if (Constants.playCustomRingback) {
                    SoundPoolManager.getInstance(DutyShiftActivity1.this).stopRinging();
                }
                Log.d(TAG, "Connected");
                activeCall = call;
            }

            @Override
            public void onReconnecting(@NonNull Call call, @NonNull CallException callException) {
                Log.d(TAG, "onReconnecting");
            }

            @Override
            public void onReconnected(@NonNull Call call) {
                Log.d(TAG, "onReconnected");
            }

            @Override
            public void onDisconnected(@NonNull Call call, CallException error) {
                audioSwitch.deactivate();
                if (Constants.playCustomRingback) {
                    SoundPoolManager.getInstance(DutyShiftActivity1.this).stopRinging();
                    Toast.makeText(DutyShiftActivity1.this, "---------", Toast.LENGTH_SHORT).show();
                }
                Log.d(TAG, "Disconnected");
                if (error != null) {
                    String message = String.format(
                            Locale.US,
                            "Call Error: %d, %s",
                            error.getErrorCode(),
                            error.getMessage());
                    Log.e(TAG, message);
                    Snackbar.make(coordinatorLayout, message, Snackbar.LENGTH_LONG).show();
                }
                home();
                resetUI();
            }

            /*
             * currentWarnings: existing quality warnings that have not been cleared yet
             * previousWarnings: last set of warnings prior to receiving this callback
             *
             * Example:
             *   - currentWarnings: { A, B }
             *   - previousWarnings: { B, C }
             *
             * Newly raised warnings = currentWarnings - intersection = { A }
             * Newly cleared warnings = previousWarnings - intersection = { C }
             */
            public void onCallQualityWarningsChanged(@NonNull Call call,
                                                     @NonNull Set<Call.CallQualityWarning> currentWarnings,
                                                     @NonNull Set<Call.CallQualityWarning> previousWarnings) {
                if (previousWarnings.size() > 1) {
                    Set<Call.CallQualityWarning> intersection = new HashSet<>(currentWarnings);
                    currentWarnings.removeAll(previousWarnings);
                    intersection.retainAll(previousWarnings);
                    previousWarnings.removeAll(intersection);
                }

                String message = String.format(
                        Locale.US,
                        "Newly raised warnings: " + currentWarnings + " Clear warnings " + previousWarnings);
                Log.e(TAG, message);
                Snackbar.make(coordinatorLayout, message, Snackbar.LENGTH_LONG).show();
            }
        };
    }

    private void setCallUI() {
        callActionFab.hide();
        hangupActionFab.show();
        holdActionFab.show();
        muteActionFab.show();
        chronometer.setVisibility(View.VISIBLE);
        chronometer.setBase(SystemClock.elapsedRealtime());
        chronometer.start();
    }

    private void resetUI() {
        recyclerView.setVisibility(View.GONE);
        callActionFab.show();
        muteActionFab.setImageDrawable(ContextCompat.getDrawable(DutyShiftActivity1.this, R.drawable.ic_mic_off_black_24dp));
        holdActionFab.hide();
        holdActionFab.setBackgroundTintList(ColorStateList
                .valueOf(ContextCompat.getColor(this, R.color.colorAccent)));
        muteActionFab.hide();
        hangupActionFab.hide();
        chronometer.setVisibility(View.INVISIBLE);
        chronometer.stop();
    }

    @Override
    protected void onResume() {
        super.onResume();
        registerReceiver();
        registerReceiver1();
        getChannelsList();
    }

    @Override
    protected void onPause() {
        super.onPause();
        unregisterReceiver();
        unregisterReceiver1();
    }

    @Override
    public void onDestroy() {
        /*
         * Tear down audio device management and restore previous volume stream
         */
        if (chatClientManager != null) {
            new Handler().post(new Runnable() {
                @Override
                public void run() {
                    // chatClientManager.shutdown();
                    //TwilioChatApplication.get().getChatClientManager().setChatClient(null);
                }
            });
        }

        audioSwitch.stop();
        setVolumeControlStream(savedVolumeControlStream);
        SoundPoolManager.getInstance(this).release();
        retrieveAccessToken();

        super.onDestroy();
    }

    private void handleIncomingCallIntent(Intent intent) {
        if (intent != null && intent.getAction() != null) {
            String action = intent.getAction();
            activeCallInvite = intent.getParcelableExtra(Constants.INCOMING_CALL_INVITE);
            activeCallNotificationId = intent.getIntExtra(Constants.INCOMING_CALL_NOTIFICATION_ID, 0);

            switch (action) {
                case Constants.ACTION_INCOMING_CALL:
                    handleIncomingCall();
                    break;
                case Constants.ACTION_INCOMING_CALL_NOTIFICATION:
                    showIncomingCallDialog();
                    break;
                case Constants.ACTION_CANCEL_CALL:
                    handleCancel();
                    break;
                case Constants.ACTION_FCM_TOKEN:
                    retrieveAccessToken();
                    break;
                case Constants.ACTION_ACCEPT:
                    answer();
                    break;
                default:
                    break;
            }
        }
    }

    private void handleIncomingCall() {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.O) {
            showIncomingCallDialog();
        } else {
            if (isAppVisible()) {
                showIncomingCallDialog();
            }
        }
    }

    private void handleCancel() {
        if (alertDialog != null && alertDialog.isShowing()) {
            SoundPoolManager.getInstance(this).stopRinging();
            alertDialog.cancel();
        }
    }

    private void registerReceiver1() {
        if (!isReceiverRegistered1) {
            IntentFilter intentFilter = new IntentFilter();
            intentFilter.addAction(ACTION_CHANNEL_CREATED);
            LocalBroadcastManager.getInstance(this).registerReceiver(
                    localBroadcastReceiver, intentFilter);
            isReceiverRegistered1 = true;
        }
    }

    private void unregisterReceiver1() {
        LocalBroadcastManager.getInstance(this).unregisterReceiver(localBroadcastReceiver);
        isReceiverRegistered1 = false;
    }

    private void registerReceiver() {
        if (!isReceiverRegistered) {
            IntentFilter intentFilter = new IntentFilter();
            intentFilter.addAction(Constants.ACTION_INCOMING_CALL);
            intentFilter.addAction(Constants.ACTION_CANCEL_CALL);
            intentFilter.addAction(Constants.ACTION_FCM_TOKEN);
            LocalBroadcastManager.getInstance(this).registerReceiver(
                    voiceBroadcastReceiver, intentFilter);
            isReceiverRegistered = true;
        }
    }

    private void unregisterReceiver() {
        if (isReceiverRegistered) {
            LocalBroadcastManager.getInstance(this).unregisterReceiver(voiceBroadcastReceiver);
            isReceiverRegistered = false;
        }
    }

    private class VoiceBroadcastReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if (action != null && (action.equals(Constants.ACTION_INCOMING_CALL) || action.equals(Constants.ACTION_CANCEL_CALL))) {
                /*
                 * Handle the incoming or cancelled call invite
                 */
                handleIncomingCallIntent(intent);
            }
        }
    }

    private DialogInterface.OnClickListener answerCallClickListener() {
        return new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Log.d(TAG, "Clicked accept");
                Intent acceptIntent = new Intent(DutyShiftActivity1.this.getApplicationContext(), IncomingCallNotificationService.class);
                acceptIntent.setAction(Constants.ACTION_ACCEPT);
                acceptIntent.putExtra(Constants.INCOMING_CALL_INVITE, activeCallInvite);
                acceptIntent.putExtra(Constants.INCOMING_CALL_NOTIFICATION_ID, activeCallNotificationId);
                Log.d(TAG, "Clicked accept startService");
                DutyShiftActivity1.this.startService(acceptIntent);
            }
        };
    }

    private DialogInterface.OnClickListener callClickListener() {
        return new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                // Place a call
                EditText contact = ((AlertDialog) dialog).findViewById(R.id.contact);
                params.put("to", contact.getText().toString());
                ConnectOptions connectOptions = new ConnectOptions.Builder(accessToken)
                        .params(params)
                        .build();
                activeCall = Voice.connect(DutyShiftActivity1.this, connectOptions, callListener);
                DutyShiftActivity1.this.setCallUI();
                alertDialog.dismiss();
            }
        };
    }

    private DialogInterface.OnClickListener cancelCallClickListener() {
        return new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                SoundPoolManager.getInstance(DutyShiftActivity1.this).stopRinging();
                if (activeCallInvite != null) {
                    Intent intent = new Intent(DutyShiftActivity1.this, IncomingCallNotificationService.class);
                    intent.setAction(Constants.ACTION_REJECT);
                    intent.putExtra(Constants.INCOMING_CALL_INVITE, activeCallInvite);
                    DutyShiftActivity1.this.startService(intent);
                }
                if (alertDialog != null && alertDialog.isShowing()) {
                    alertDialog.dismiss();
                }
            }
        };
    }

    public static AlertDialog createIncomingCallDialog(
            Context context,
            CallInvite callInvite,
            DialogInterface.OnClickListener answerCallClickListener,
            DialogInterface.OnClickListener cancelClickListener) {
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(context);
        alertDialogBuilder.setIcon(R.drawable.ic_call_black_24dp);
        alertDialogBuilder.setTitle("Incoming Call");
        alertDialogBuilder.setPositiveButton("Accept", answerCallClickListener);
        alertDialogBuilder.setNegativeButton("Reject", cancelClickListener);
//        alertDialogBuilder.setMessage(callInvite.getFrom() + " is calling with " + callInvite.getCallerInfo().isVerified() + " status");
        return alertDialogBuilder.create();
    }

    private void registerForCallInvites() {
        FirebaseMessaging.getInstance().getToken().addOnSuccessListener(this, fcmToken -> {
            Log.i(TAG, "Registering with FCM" + fcmToken);
            Voice.register(accessToken, Voice.RegistrationChannel.FCM, fcmToken, registrationListener);
            SharedPreferencesUtils.saveAccessToken(accessToken);
            SharedPreferencesUtils.saveFCMToken(fcmToken);
        });


      /*  FirebaseInstallations.getInstance().getId().addOnSuccessListener(this, new OnSuccessListener<String>() {
            @Override
            public void onSuccess(@NonNull String s) {
                String fcmToken = FirebaseMessaging.getInstance().getToken().toString();
                Log.i(TAG, "Registering with FCM");
                Voice.register(accessToken, Voice.RegistrationChannel.FCM, fcmToken, registrationListener);
            }
        });*/ /*{
            @Override
            public void onSuccess(InstanceIdResult instanceIdResult) {
                String fcmToken = instanceIdResult.getToken();
                Log.i(TAG, "Registering with FCM");
                Voice.register(accessToken, Voice.RegistrationChannel.FCM, fcmToken, registrationListener);
            }
        });*/

        //callTOpreson();

    }

    private View.OnClickListener audioSelectListener() {
        return view -> showAudioDevices();
    }

    private View.OnClickListener callActionFabClickListener() {
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                alertDialog = createCallDialog(MainActivity.this.callClickListener(), MainActivity.this.cancelCallClickListener(), MainActivity.this);
//                alertDialog.show();
//                params.put("to", "Rajan");
                params.put("to", callTo);
                ConnectOptions connectOptions = new ConnectOptions.Builder(accessToken)
                        .params(params)
                        .build();
                activeCall = Voice.connect(DutyShiftActivity1.this, connectOptions, callListener);
                DutyShiftActivity1.this.setCallUI();

            }
        };
    }

    private void home() {

        Intent i = new Intent(getApplicationContext(), homeScreenActivity.class);
        startActivity(i);
        finish();

    }

    private View.OnClickListener hangupActionFabClickListener() {
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SoundPoolManager.getInstance(DutyShiftActivity1.this).playDisconnect();
                DutyShiftActivity1.this.resetUI();
                DutyShiftActivity1.this.disconnect();
//                onBackPressed();
            }
        };
    }

    private View.OnClickListener holdActionFabClickListener() {
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DutyShiftActivity1.this.hold();
            }
        };
    }

    private View.OnClickListener muteActionFabClickListener() {
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DutyShiftActivity1.this.mute();
            }
        };
    }

    private void answer() {
        SoundPoolManager.getInstance(this).stopRinging();
        activeCallInvite.accept(this, callListener);
        notificationManager.cancel(activeCallNotificationId);
        setCallUI();
        if (alertDialog != null && alertDialog.isShowing()) {
            alertDialog.dismiss();
        }
    }

    private void disconnect() {
        if (activeCall != null) {
            activeCall.disconnect();
            activeCall = null;
        }
    }

    private void hold() {
        if (activeCall != null) {
            boolean hold = !activeCall.isOnHold();
            activeCall.hold(hold);
            applyFabState(holdActionFab, hold);
        }
    }

    private void mute() {
        if (activeCall != null) {
            boolean mute = !activeCall.isMuted();
            if (!mute) {
                muteActionFab.setImageDrawable(ContextCompat.getDrawable(DutyShiftActivity1.this, R.drawable.ic_mic_off_black_24dp));
            } else {
                muteActionFab.setImageDrawable(ContextCompat.getDrawable(DutyShiftActivity1.this, R.drawable.ic_mic_white_24dp));
            }
            activeCall.mute(mute);
            applyFabState(muteActionFab, mute);
        }
    }

    private void applyFabState(FloatingActionButton button, boolean enabled) {
        // Set fab as pressed when call is on hold
        ColorStateList colorStateList = enabled ?
                ColorStateList.valueOf(ContextCompat.getColor(this,
                        R.color.colorPrimaryDark)) :
                ColorStateList.valueOf(ContextCompat.getColor(this,
                        R.color.colorAccent));
        button.setBackgroundTintList(colorStateList);
    }

    private boolean checkPermissionForMicrophone() {
        int resultMic = ContextCompat.checkSelfPermission(this, Manifest.permission.RECORD_AUDIO);
        return resultMic == PackageManager.PERMISSION_GRANTED;
    }

    private void requestPermissionForMicrophone() {
        if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.RECORD_AUDIO)) {
            Snackbar.make(coordinatorLayout,
                    "Microphone permissions needed. Please allow in your application settings.",
                    Snackbar.LENGTH_LONG).show();
        } else {
            ActivityCompat.requestPermissions(
                    this,
                    new String[]{Manifest.permission.RECORD_AUDIO},
                    MIC_PERMISSION_REQUEST_CODE);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        /*
         * Check if microphone permissions is granted
         */
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == MIC_PERMISSION_REQUEST_CODE && permissions.length > 0) {
            if (grantResults[0] != PackageManager.PERMISSION_GRANTED) {
                Snackbar.make(coordinatorLayout,
                        "Microphone permissions needed. Please allow in your application settings.",
                        Snackbar.LENGTH_LONG).show();
            } else {
                retrieveAccessToken();
            }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu, menu);
        audioDeviceMenuItem = menu.findItem(R.id.menu_audio_device);

        /*
         * Start the audio device selector after the menu is created and update the icon when the
         * selected audio device changes.
         */
        audioSwitch.start(new Function2<List<? extends AudioDevice>, AudioDevice, Unit>() {
            @Override
            public Unit invoke(List<? extends AudioDevice> audioDevices, AudioDevice audioDevice) {
                DutyShiftActivity1.this.updateAudioDeviceIcon(audioDevice);
                return Unit.INSTANCE;
            }
        });

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.menu_audio_device) {
            showAudioDevices();
            return true;
        }
        return false;
    }

    private void showAudioDevices() {
        AudioDevice selectedDevice = audioSwitch.getSelectedAudioDevice();
        final List<AudioDevice> availableAudioDevices = audioSwitch.getAvailableAudioDevices();

        if (selectedDevice != null) {
            int selectedDeviceIndex = availableAudioDevices.indexOf(selectedDevice);

            ArrayList<String> audioDeviceNames = new ArrayList<>();
            for (AudioDevice a : availableAudioDevices) {
                audioDeviceNames.add(a.getName());
            }

            new AlertDialog.Builder(this)
                    .setTitle(R.string.select_device)
                    .setSingleChoiceItems(
                            audioDeviceNames.toArray(new CharSequence[0]),
                            selectedDeviceIndex,
                            new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int index) {
                                    dialog.dismiss();
                                    AudioDevice selectedAudioDevice = availableAudioDevices.get(index);
                                    DutyShiftActivity1.this.updateAudioDeviceIcon(selectedAudioDevice);
                                    audioSwitch.selectDevice(selectedAudioDevice);
                                }
                            }).create().show();
        }
    }

    private void updateAudioDeviceIcon(AudioDevice selectedAudioDevice) {
        int audioDeviceMenuIcon = R.drawable.ic_phonelink_ring_white_24dp;

        if (selectedAudioDevice instanceof AudioDevice.BluetoothHeadset) {
            audioDeviceMenuIcon = R.drawable.ic_bluetooth_white_24dp;
        } else if (selectedAudioDevice instanceof AudioDevice.WiredHeadset) {
            audioDeviceMenuIcon = R.drawable.ic_headset_mic_white_24dp;
        } else if (selectedAudioDevice instanceof AudioDevice.Earpiece) {
            audioDeviceMenuIcon = R.drawable.ic_phonelink_ring_white_24dp;
        } else if (selectedAudioDevice instanceof AudioDevice.Speakerphone) {
            audioDeviceMenuIcon = R.drawable.ic_volume_up_white_24dp;
        }

        String a = "iv_audio_mode" + iv_audio_mode.toString();
        //audioDeviceMenuItem.setIcon(audioDeviceMenuIcon);
        if (!this.isFinishing()) {
            Glide.with(DutyShiftActivity1.this).load(audioDeviceMenuIcon).into(iv_audio_mode);
        }

    }

//    private static AlertDialog createCallDialog(final DialogInterface.OnClickListener callClickListener,
//                                                final DialogInterface.OnClickListener cancelClickListener,
//                                                final Activity activity) {
//        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(activity);
//
//        alertDialogBuilder.setIcon(R.drawable.ic_call_black_24dp);
//        alertDialogBuilder.setTitle("Call");
//        alertDialogBuilder.setPositiveButton("Call", callClickListener);
//        alertDialogBuilder.setNegativeButton("Cancel", cancelClickListener);
//        alertDialogBuilder.setCancelable(false);
//
//        LayoutInflater li = LayoutInflater.from(activity);
//        View dialogView = li.inflate(
//                R.layout.dialog_call,
//                (ViewGroup) activity.findViewById(android.R.id.content),
//                false);
//        final EditText contact = dialogView.findViewById(R.id.contact);
//        contact.setHint(R.string.callee);
//        alertDialogBuilder.setView(dialogView);
//
//        return alertDialogBuilder.create();
//
//    }

    private void showIncomingCallDialog() {
        SoundPoolManager.getInstance(this).playRinging();
        if (activeCallInvite != null) {
            alertDialog = createIncomingCallDialog(DutyShiftActivity1.this,
                    activeCallInvite,
                    answerCallClickListener(),
                    cancelCallClickListener());
            alertDialog.show();
        }
    }

    private boolean isAppVisible() {
        return ProcessLifecycleOwner
                .get()
                .getLifecycle()
                .getCurrentState()
                .isAtLeast(Lifecycle.State.STARTED);
    }


    private void retrieveAccessToken() {
/*//        Toast.makeText(this, "   " + identity, Toast.LENGTH_SHORT).show();
        Ion.with(this).load(TWILIO_ACCESS_TOKEN_SERVER_URL + "?identity=" + identity)
                .asString()
                .setCallback(new FutureCallback<String>() {
                    @Override
                    public void onCompleted(Exception e, String accessToken) {
                        if (e == null) {
                            Log.d(TAG, "Access token: " + accessToken);
                            DutyShiftActivity1.this.accessToken = accessToken;
                            DutyShiftActivity1.this.registerForCallInvites();
                            audioSwitch.start(new Function2<List<? extends AudioDevice>, AudioDevice, Unit>() {
                                @Override
                                public Unit invoke(List<? extends AudioDevice> audioDevices, AudioDevice audioDevice) {
                                    DutyShiftActivity1.this.updateAudioDeviceIcon(audioDevice);
                                    return Unit.INSTANCE;
                                }
                            });

                        } else {
                            Snackbar.make(coordinatorLayout,
                                    "Error retrieving access token. Unable to make calls",
                                    Snackbar.LENGTH_LONG).show();
                        }
                    }
                });*/
        ShamsahaService digitalService = RetrofitInstance.getRetrofitInstance().getDigitalService();
        retrofit2.Call<AccessTokenResponse> accessTokenCall = digitalService.getAccessToken(identity);
        accessTokenCall.enqueue(new Callback<AccessTokenResponse>() {
            @Override
            public void onResponse(@NotNull retrofit2.Call<AccessTokenResponse> call, @NotNull Response<AccessTokenResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    accessToken = response.body().getToken();
                    registerForCallInvites();
                    audioSwitch.start((audioDevices, audioDevice) -> {
                        updateAudioDeviceIcon(audioDevice);
                        return Unit.INSTANCE;
                    });
                } else {
                    Snackbar.make(coordinatorLayout,
                            "Error retrieving access token. Unable to make calls",
                            Snackbar.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(@NotNull retrofit2.Call<AccessTokenResponse> call, @NotNull Throwable t) {
                Log.e(TAG, "onFailure: ", t);
            }
        });


    }

    private void callTOpreson() {
        params.put("to", callTo);
        ConnectOptions connectOptions = new ConnectOptions.Builder(accessToken)
                .params(params)
                .build();
        activeCall = Voice.connect(DutyShiftActivity1.this, connectOptions, callListener);
        DutyShiftActivity1.this.setCallUI();
    }

    private void showActivityIndicator(final String message) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                progressDialog = new ProgressDialog(DutyShiftActivity1.this);
                progressDialog.setMessage(message);
                progressDialog.show();
                progressDialog.setCanceledOnTouchOutside(false);
                progressDialog.setCancelable(false);
            }
        });
    }


    private void checkTwilioClient() {
        showActivityIndicator(getResources().getString(R.string.loading_channels_message));
        chatClientManager = TwilioChatApplication.get().getChatClientManager();
        //if (chatClientManager.getChatClient() == null) {
        initializeClient();
//        } else {
//            populateChannels();
//        }
    }

    private void initializeClient() {
        chatClientManager.connectClient(identity, new TaskCompletionListener<Void, String>() {
            @Override
            public void onSuccess(Void aVoid) {
                chatClientManager.getChatClient().registerFCMToken(new ChatClient.FCMToken(SharedPreferencesUtils.getFCMToken()), new StatusListener() {
                    @Override
                    public void onSuccess() {
                        Log.d("Register FCM to client", "onSuccess: Successful");
                    }
                });
                populateChannels();
            }

            @Override
            public void onError(String errorMessage) {
                stopActivityIndicator();
                StyleableToast.makeText(DutyShiftActivity1.this, "Client connection error: " + errorMessage + "\n Check Internet Connection..!"
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

    private void dutyStatusAvailableAPI() {
        api api = retrofit.retrofit.create(com.shamsaha.app.api.api.class);
        retrofit2.Call<MessageModel> call = api.volunteerAvailability(identity, "Available");
        call.enqueue(new Callback<MessageModel>() {
            @Override
            public void onResponse(retrofit2.Call<MessageModel> call, Response<MessageModel> response) {
                if (response.isSuccessful() && (response.code() == 200)) {
                    assert response.body() != null;
                    if (response.body().getStatus().equals("valid")) {
                        stopActivityIndicator();
                        Intent intent = new Intent(DutyShiftActivity1.this, ChatActivity.class);
                        intent.putExtra("volID", identity);
                        intent.putExtra("caseID", "");
                        startActivity(intent);
                    } else {
                        stopActivityIndicator();
                        Toast.makeText(DutyShiftActivity1.this, response.body().getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }
            }

            @Override
            public void onFailure(retrofit2.Call<MessageModel> call, Throwable t) {
                Toast.makeText(DutyShiftActivity1.this, t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void populateChannels() {
        final Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                channelManager.setChannelListener(DutyShiftActivity1.this);
                channelsObject = chatClientManager.getChatClient().getChannels();
                channelsObject.getChannel(clickedChannelId, new CallbackListener<Channel>() {
                    @Override
                    public void onSuccess(Channel channel) {
                        Log.d(TAG, "onSuccess: " + channel.getStatus());
                        if (channel.getStatus() == Channel.ChannelStatus.JOINED) {
                            Log.d(TAG, "onSuccess: True");
                            //Toast.makeText(DutyShiftActivity1.this, "true", Toast.LENGTH_SHORT).show();
//                            stopActivityIndicator();
                            chanModel.setChannel(channel);
                            if (channel.getFriendlyName().equals(chanModel.getChannel().getFriendlyName())) {
                                dutyStatusAvailableAPI();
                            }
                        } else {
                            Log.d(TAG, "onSuccess: false");
                            Toast.makeText(DutyShiftActivity1.this, "false " + channel.getStatus(), Toast.LENGTH_SHORT).show();
                            chanModel.setChannel(channel);
                            if (channel.getFriendlyName().equals(chanModel.getChannel().getFriendlyName())) {
                                dutyStatusAvailableAPI();
                            }
//                            stopActivityIndicator();
                        }
                    }

                    @Override
                    public void onError(ErrorInfo errorInfo) {
                        super.onError(errorInfo);
                        initializeClientCase(clickedChannelId);
                       // Toast.makeText(DutyShiftActivity1.this, "" + errorInfo.getMessage(), Toast.LENGTH_SHORT).show();
                        Toast.makeText(DutyShiftActivity1.this, R.string.error_message, Toast.LENGTH_SHORT).show();
                        //Log.e("test_sam","start"+errorInfo.getMessage()+"ok");

                    }
                });
            }
        }, 2000);
//        channelManager.populateChannels(channels -> {
//            channelsObject = chatClientManager.getChatClient().getChannels();
//            channelsObject.getChannel(clickedChannelId, new CallbackListener<Channel>() {
//                @Override
//                public void onSuccess(Channel channel) {
//                    Log.d(TAG, "onSuccess: "+channel.getStatus());
//                    if(channel.getStatus() == Channel.ChannelStatus.JOINED){
//                        Log.d(TAG, "onSuccess: True");
//                        Toast.makeText(DutyShiftActivity1.this, "true", Toast.LENGTH_SHORT).show();
//                        stopActivityIndicator();
//                        chanModel.setChannel(channel);
//                        if(channel.getFriendlyName().equals(chanModel.getChannel().getFriendlyName())){
//                            Intent intent = new Intent(DutyShiftActivity1.this, ChatActivity.class);
//                            intent.putExtra("volID", identity);
//                            intent.putExtra("caseID", "");
//                            startActivity(intent);
//                        }
//                    }else{
//                        Log.d(TAG, "onSuccess: false");
//                        Toast.makeText(DutyShiftActivity1.this, "false", Toast.LENGTH_SHORT).show();
//                        stopActivityIndicator();
//                    }
//                }
//            });
////            if (channels != null) {
////                int i;
////                for (i = 0; i < channels.size(); i++) {
////                    if (channels.get(i).getFriendlyName().equals(clickedChannelId)) {
////                        Log.d(TAG, "populateChannels: "+channels.get(i).getStatus());
////                        setChannel(i);
////                    }
////                }
////                runOnUiThread(this::stopActivityIndicator);
////            } else {
////                stopActivityIndicator();
////            }
////            channelsObject = chatClientManager.getChatClient().getChannels();
////            channelsObject.getChannel(clickedChannelId, new CallbackListener<Channel>() {
////                @Override
////                public void onSuccess(Channel channel) {
////                    Log.d("basto", "populateChannels: "+ channel.getFriendlyName());
////                    Log.d("basto", "populateChannels: "+ channel.getUniqueName());
//////                    chanModel.setChannel(channel);
//////                    Intent intent = new Intent(DutyShiftActivity1.this, ChatActivity.class);
//////                    intent.putExtra("volID", identity);
//////                    intent.putExtra("caseID", "");
//////                    startActivity(intent);
//////                    stopActivityIndicator();
////                    setChannel(channel);
////                }
////
////            });
//
//        });
    }

    private void populateChannels1(String caseID) {
        channelsObject = chatClientManager.getChatClient().getChannels();
        channelsObject.getChannel(caseID, new CallbackListener<Channel>() {
            @Override
            public void onSuccess(Channel channel) {
                Log.d(TAG, "onSuccess: " + channel.getStatus());
                chanModel.setChannel(channel);

                final Handler handler = new Handler();
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        Members membersObject = chanModel.getChannel().getMembers();
                        membersObject.addByIdentity(identity, new StatusListener() {
                            @Override
                            public void onSuccess() {
                                Log.d(TAG, "onSuccess: " + chanModel.getChannel().getMembers().getMembersList());
//                                stopActivityIndicator();
                                initializeClient();
                            }

                            @Override
                            public void onError(ErrorInfo errorInfo) {
                                super.onError(errorInfo);
                                Log.d(TAG, "onSuccess: " + errorInfo.getMessage());
                                stopActivityIndicator();
                            }
                        });
                    }
                }, 3000);

            }

            @Override
            public void onError(ErrorInfo errorInfo) {
                super.onError(errorInfo);
               // Toast.makeText(DutyShiftActivity1.this, "errorInfo " + errorInfo, Toast.LENGTH_SHORT).show();
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
                StyleableToast.makeText(DutyShiftActivity1.this, "Client connection error: " + errorMessage + "\n Check Internet Connection..!"
                        , Toast.LENGTH_LONG, R.style.mytoast).show();
            }
        });
    }


    @Override
    public void onChannelJoined(Channel channel) {

    }

    @Override
    public void onChannelInvited(Channel channel) {
        Log.e("fdnjkf", "doisjfoi");
    }

    @Override
    public void onChannelAdded(Channel channel) {
        refreshChannels();
        Toast.makeText(this, "New Channel Added", Toast.LENGTH_SHORT).show();
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

    private void refreshChannels() {
        channelManager.populateChannels(new LoadChannelListener() {
            @Override
            public void onChannelsFinishedLoading(final List<Channel> channels) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        channelAdapter.notifyDataSetChanged();
                    }
                });
            }
        });
    }

    private void setChannel(final int position) {

        List<Channel> channels = channelManager.getChannels();
        if (channels == null) {
            return;
        }
//        final Channel currentChannel = chatFragment.getCurrentChannel();
        final Channel selectedChannel = channels.get(position);

//        if (currentChannel != null && currentChannel.getSid().contentEquals(selectedChannel.getSid())) {
////            drawer.closeDrawer(GravityCompat.START);
//            return;
//        }
//
//        if(selectedChannel != null){
//            showActivityIndicator("Joining " + selectedChannel.getFriendlyName() + " channel");
//            if(currentChannel != null && currentChannel.getStatus() == Channel.ChannelStatus.JOINED){
//                channelManager.leaveChannelWithHandler(currentChannel, new StatusListener() {
//                    @Override
//                    public void onSuccess() {
//                        joinChannel(selectedChannel);
//                    }
//
//                    @Override
//                    public void onError(ErrorInfo errorInfo) {
//                        stopActivityIndicator();
//                    }
//                });
//                return;
//            }
//
//            joinChannel(selectedChannel);
//            stopActivityIndicator();
//        }else {
//            stopActivityIndicator();
//            showAlertWithMessage(getStringResource(R.string.generic_error));
//            Log.e(TwilioChatApplication.TAG,"Selected channel out of range");
//        }
        Log.d("mmmmm", "\nres:------------------------------------------- ");
        Log.d("mmmmm", "res: " + selectedChannel.getFriendlyName());
        Log.d("mmmmm", "res: " + selectedChannel.getDateCreated());
        Log.d("mmmmm", "res: " + selectedChannel.getSid());
        Log.d("mmmmm", "res: " + selectedChannel.getUniqueName());
        Log.d("mmmmm", "res:------------------------------------------- \n");
//joinChannel(selectedChannel);

        chanModel.setChannel(selectedChannel);
        Log.d(TAG, "setChannel: " + selectedChannel.getStatus());
        Intent intent = new Intent(DutyShiftActivity1.this, ChatActivity.class);
        intent.putExtra("volID", identity);
        intent.putExtra("caseID", "");
        startActivity(intent);

    }

    public static boolean isNullOrEmpty(String str) {
        if (str != null && !str.isEmpty())
            return false;
        return true;
    }

    @Override
    public void onClick(View v) {
        int position = Integer.parseInt(v.getTag().toString());
        if (v.getId() == R.id.cv_service) {
            if (isNullOrEmpty(channelList.get(position).getScreenName())) {

            } else {
                clickedChannelId = channelList.get(position).getCaseId();
                checkTwilioClient();
            }

        }
    }

    public static void onNewVictimChannelAdded(Channel channel) {
        Toast.makeText(TwilioChatApplication.get(), "Channel created!!", Toast.LENGTH_SHORT).show();
        channelManager.populateChannels(new LoadChannelListener() {
            @Override
            public void onChannelsFinishedLoading(List<Channel> channels) {
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        channelAdapter.notifyDataSetChanged();
                    }
                });
            }
        });
    }
}


//    private void populateChannels() {
//        try{
//            channelManager.setChannelListener(DutyShiftActivity1.this);
//            channelsObject = chatClientManager.getChatClient().getChannels();
//            channelsObject.getChannel(clickedChannelId, new CallbackListener<Channel>() {
//                @Override
//                public void onSuccess(Channel channel) {
//                    Log.d("basto", "populateChannels: "+ channel.getFriendlyName());
//                    Log.d("basto", "populateChannels: "+ channel.getUniqueName());
////                    chanModel.setChannel(channel);
////                    Intent intent = new Intent(DutyShiftActivity1.this, ChatActivity.class);
////                    intent.putExtra("volID", identity);
////                    intent.putExtra("caseID", "");
////                    startActivity(intent);
////                    stopActivityIndicator();
//                    setChannel(channel);
//                }
//
//            });
////            runOnUiThread(this::stopActivityIndicator);
//        }catch (Exception e){
//            stopActivityIndicator();
//            Log.d("basto", "Exception: "+ e);
//        }
//
////        channelManager.setChannelListener(DutyShiftActivity1.this);
////        channelManager.populateChannels(channels -> {
////            Log.d("basto", "populateChannels: "+clickedChannelId);
////            Log.d("basto", "populateChannels: "+ channels.size());
////
////
////            if (channels != null) {
////                for (int i = 0; i < channels.size(); i++) {
////                    Log.d("basto", i+"populateChannels: "+channels.get(i).getFriendlyName());
////                    if (channels.get(i).getFriendlyName().equals(clickedChannelId)) {
////                        setChannel(i);
////                    }
////                }
////                runOnUiThread(this::stopActivityIndicator);
////            } else {
////                stopActivityIndicator();
////
////            }
////        });
//    }