package com.shamsaha.app.activity.Victem;

import android.Manifest;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Environment;
import android.os.SystemClock;
import android.os.Vibrator;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.webkit.MimeTypeMap;
import android.webkit.WebView;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.Chronometer;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.github.dhaval2404.imagepicker.ImagePicker;
import com.muddzdev.styleabletoast.StyleableToast;
import com.nbsp.materialfilepicker.MaterialFilePicker;
import com.nbsp.materialfilepicker.ui.FilePickerActivity;
import com.quickblox.ui.kit.chatmessage.adapter.QBMessagesAdapter;
import com.quickblox.ui.kit.chatmessage.adapter.media.SingleMediaManager;
import com.quickblox.ui.kit.chatmessage.adapter.media.recorder.AudioRecorder;
import com.quickblox.ui.kit.chatmessage.adapter.media.recorder.exceptions.MediaRecorderException;
import com.quickblox.ui.kit.chatmessage.adapter.media.recorder.listeners.QBMediaRecordListener;
import com.quickblox.ui.kit.chatmessage.adapter.media.recorder.view.QBRecordAudioButton;
import com.shamsaha.app.ApiModel.ChannelStatusModel;
import com.shamsaha.app.ApiModel.volunter.AdditionalLanguageModel;
import com.shamsaha.app.ApiModel.volunter.LanguageRequestModel;
import com.shamsaha.app.ApiModel.volunter.MessageModel;
import com.shamsaha.app.R;
import com.shamsaha.app.activity.CallHelper.videocall.VideoInviteActivity;
import com.shamsaha.app.activity.ChatHelper.Application.TwilioChatApplication;
import com.shamsaha.app.activity.ChatHelper.ChanModel;
import com.shamsaha.app.activity.ChatHelper.Channel.ChannelManager;
import com.shamsaha.app.activity.ChatHelper.Message.ChatClientManager;
import com.shamsaha.app.activity.ChatHelper.listener.TaskCompletionListener;
import com.shamsaha.app.activity.volunteer.ChatCaseReportActivity;
import com.shamsaha.app.activity.volunteer.WebViewActivity;
import com.shamsaha.app.adaptor.AdditionalLanguageSpinnerAdaptor;
import com.shamsaha.app.adaptor.ChatAdaptor;
import com.shamsaha.app.adaptor.MessageAdapter;
import com.shamsaha.app.api.api;
import com.shamsaha.app.retrofitAdaptor.retrofit;
import com.shamsaha.app.utils.ConstantsURL.baseURL;
import com.shamsaha.app.utils.SharedPreferencesUtils;
import com.twilio.chat.CallbackListener;
import com.twilio.chat.Channel;
import com.twilio.chat.ChannelListener;
import com.twilio.chat.ChatClient;
import com.twilio.chat.ErrorInfo;
import com.twilio.chat.Member;
import com.twilio.chat.Message;
import com.twilio.chat.Messages;
import com.twilio.chat.ProgressListener;
import com.twilio.chat.StatusListener;

import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.regex.Pattern;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ChatActivity extends AppCompatActivity implements ChannelListener, LocationListener {

    private static final String TAG = "ChatActivity";
    Messages messagesObject;
    MessageAdapter messageAdapter;
    Button sendButton;
    ListView messagesListView;
    EditText messageTextEdit;
    ImageView IV_file, iv_audio, iv_image, iv_location, iv_attach_close, iv_attach, imageView35;
    ChatAdaptor chatAdaptor;
    RecyclerView recyclerView;
    Channel currentChannel;
    ChannelManager channelManager;
    String Language;
    boolean languageSupport = true;
    Dialog dialog;

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.chat_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        return super.onOptionsItemSelected(item);
    }

    private ChanModel chanModel;
    private String path;
    boolean doubleBackToExitPressedOnce = false;


    @Override
    public void onBackPressed() {
        if (!SharedPreferencesUtils.getIsLoggedIn()) {
            createLogoutAlert();
        } else {
            //dutyStatusAvailableAPI();
            super.onBackPressed();
        }
         /*if (identity.isEmpty() && !caseId.isEmpty()) {
            createLogoutAlert();
        }*/
    }

    private void dutyStatusAvailableAPI() {
        api api = retrofit.retrofit.create(com.shamsaha.app.api.api.class);
        Call<MessageModel> call = api.volunteerAvailability(identity, "Available");
        call.enqueue(new Callback<MessageModel>() {
            @Override
            public void onResponse(Call<MessageModel> call, Response<MessageModel> response) {
                if (response.isSuccessful() && (response.code() == 200)) {
                    assert response.body() != null;
                    if (response.body().getStatus().equals("valid")) {
//                        stopActivityIndicator();
                    } else {
//                        stopActivityIndicator();
                        Toast.makeText(ChatActivity.this, response.body().getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }
            }

            @Override
            public void onFailure(Call<MessageModel> call, Throwable t) {
                Toast.makeText(ChatActivity.this, t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private File file;
    private ImageView imageView5, video_call;
    private CardView cv_attach_dialog;
    boolean cardVisible = false;
    private LinearLayout ll_image, ll_audio, ll_file, ll_location;
    List<Message> messagesAdaptorChanges = new ArrayList<>();
    private String identity = "Deva";
    private String caseId = "Deva";
    private String vId = "";
    private ProgressDialog progressDialog;
    private QBRecordAudioButton recordButton;
    private Chronometer recordChronometer;
    private Vibrator vibro;
    private TextView audioRecordTextView, custom_message, exit;
    private ImageView bucketView;
    private AudioRecorder audioRecorder;
    private LinearLayout audioLayout, relSendMessage, customLL;
    private String[] permissions = {Manifest.permission.RECORD_AUDIO, Manifest.permission.WRITE_EXTERNAL_STORAGE};
    private static final int REQUEST_RECORD_AUDIO_WRITE_EXTERNAL_STORAGE_PERMISSIONS = 200;
    public static final int MY_PERMISSIONS_REQUEST_LOCATION = 100;
    private SingleMediaManager mediaManager;
    private QBMessagesAdapter chatAdapter1;
    private boolean permissionToRecordAccepted = false;
    Location location;
    MediaPlayer mPlayer;
    boolean nonEmpty = false;
    double lat, lon, uncertainity;
    String messageHeader = "";
    List<Member> memberList = new ArrayList<>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);
        chanModel = ChanModel.getInstance();
//        this.currentChannel = chanModel.getChannel();
        //joinChannel(chanModel.getChannel());
        Log.i(TAG, "onCreate: channel: " + chanModel.getChannel());
//        joinChannel(chanModel.getChannel());
        mPlayer = MediaPlayer.create(ChatActivity.this, R.raw.notification);
        channelManager = ChannelManager.getInstance();
        messageAdapter = new MessageAdapter(this);
        sendButton = findViewById(R.id.btn_send);
        IV_file = findViewById(R.id.IV_file);
        ll_file = findViewById(R.id.ll_file);
        ll_audio = findViewById(R.id.ll_audio);
        iv_audio = findViewById(R.id.iv_audio);
        ll_image = findViewById(R.id.ll_image);
        ll_location = findViewById(R.id.ll_location);
        iv_image = findViewById(R.id.iv_image);
        video_call = findViewById(R.id.video_call);
        iv_location = findViewById(R.id.iv_location);
        iv_attach_close = findViewById(R.id.iv_attach_close);
        imageView35 = findViewById(R.id.imageView35);
        iv_attach = findViewById(R.id.iv_attach);
        cv_attach_dialog = findViewById(R.id.cv_attach_dialog);
        recordButton = (QBRecordAudioButton) findViewById(R.id.button_chat_record_audio);
        recordChronometer = (Chronometer) findViewById(R.id.chat_audio_record_chronometer);
        recordButton.setRecordTouchListener(new RecordTouchListenerImpl());
        bucketView = (ImageView) findViewById(R.id.chat_audio_record_bucket_imageview);
        audioRecordTextView = (TextView) findViewById(R.id.chat_audio_record_textview);
        vibro = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
        audioLayout = (LinearLayout) findViewById(R.id.layout_chat_audio_container);
        relSendMessage = findViewById(R.id.relSendMessage);
        Intent intent = getIntent();
        identity = intent.getStringExtra("volID");
        caseId = intent.getStringExtra("caseID");
        //caseId = chanModel.getChannel().getFriendlyName();

        ChatClient chatClient = TwilioChatApplication.get().getChatClientManager().getChatClient();
        if (chatClient == null) {
            ChatClientManager chatClientManager = new ChatClientManager(this.getApplicationContext());
            if (SharedPreferencesUtils.getIsLoggedIn()) {
                chatClientManager.connectClient(SharedPreferencesUtils.getVolunteerID(), new TaskCompletionListener<Void, String>() {
                    @Override
                    public void onSuccess(Void unused) {
                        chatClientManager.getChatClient().registerFCMToken(new ChatClient.FCMToken(SharedPreferencesUtils.getFCMToken()), new StatusListener() {
                            @Override
                            public void onSuccess() {
                                Log.d("Register FCM Token", "onSuccess: Successful");
                            }
                        });
                        chatClientManager.getChatClient().getChannels().getChannel(caseId, new CallbackListener<Channel>() {
                            @Override
                            public void onSuccess(Channel channel) {
                                chanModel.setChannel(channel);
                                joinChannel(channel);
                            }
                        });
                    }

                    @Override
                    public void onError(String s) {
                    }
                });
            } else {
                chatClientManager.connectClient(caseId, new TaskCompletionListener<Void, String>() {
                    @Override
                    public void onSuccess(Void unused) {
                        chatClientManager.getChatClient().registerFCMToken(new ChatClient.FCMToken(SharedPreferencesUtils.getFCMToken()), new StatusListener() {
                            @Override
                            public void onSuccess() {
                                Log.d("Register FCM Token", "onSuccess: Successful");
                                Log.i(TAG, "onSuccess: From chat activity registered");
                            }
                        });
                        chatClientManager.getChatClient().getChannels().getChannel(caseId, new CallbackListener<Channel>() {
                            @Override
                            public void onSuccess(Channel channel) {
                                chanModel.setChannel(channel);
                                joinChannel(channel);
                            }
                        });
                    }

                    @Override
                    public void onError(String s) {
                    }
                });
            }
        } else {
            joinChannel(chanModel.getChannel());
        }



        /*if (SharedPreferencesUtils.getIsLoggedIn()){
         *//*if (chanModel.getChannel() !=null){

            }*//*
            ChatClient chatclient = TwilioChatApplication.get().getChatClientManager().getChatClient();
            //if client dead re create it
            if (chatclient== null) {
                TwilioChatApplication.get().getChatClientManager().connectClient(SharedPreferencesUtils.getVolunteerID(), new TaskCompletionListener<Void, String>() {
                    @Override
                    public void onSuccess(Void unused) {
                        TwilioChatApplication.get().getChatClientManager().getChatClient().getChannels().getChannel(caseId, new CallbackListener<Channel>() {
                            @Override
                            public void onSuccess(Channel channel) {
                                chanModel.setChannel(channel);
                                //memberList = chanModel.getChannel().getMembers().getMembersList();
                                joinChannel(channel);
                            }
                        });
                    }

                    @Override
                    public void onError(String s) {
                        Log.i(TAG, "onError: Client not connected");
                    }
                });
            }

            //joinFromNotification();
        }else {
            joinChannel(chanModel.getChannel());
            //memberList = chanModel.getChannel().getMembers().getMembersList();
        }*/
        vId = intent.getStringExtra("vId");
        messageTextEdit = findViewById(R.id.editTextMessage);
        recyclerView = findViewById(R.id.recyclerView);
        custom_message = findViewById(R.id.custom_message);
        customLL = findViewById(R.id.customLL);
        exit = findViewById(R.id.exit);
        final LinearLayoutManager linearLayoutManager = new LinearLayoutManager(ChatActivity.this);
        linearLayoutManager.setStackFromEnd(true);
        recyclerView.setLayoutManager(linearLayoutManager);
        checkLocationPermission();
        requestPermission();
        initAudioRecorder();

        if (identity.isEmpty() && !caseId.isEmpty()) {
            imageView35.setVisibility(View.GONE);
            customLL.setVisibility(View.GONE);
        } else {
            exit.setVisibility(View.GONE);
        }
        messageTextEdit.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (s.length() == 0) {
                    sendButton.setVisibility(View.GONE);
                    recordButton.setVisibility(View.VISIBLE);
                } else {
                    sendButton.setVisibility(View.VISIBLE);
                    recordButton.setVisibility(View.GONE);
                }
            }
        });

        sendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    sendMessage();
                    sendButton.setVisibility(View.GONE);
                    recordButton.setVisibility(View.VISIBLE);
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }
            }
        });

        exit.setOnClickListener(view -> {
            if (!SharedPreferencesUtils.getIsLoggedIn()) {
                createLogoutAlert();
            } else {
                onBackPressed();
            }
            /*if (identity.isEmpty() && !caseId.isEmpty()) {
                createLogoutAlert();
            } else {
                onBackPressed();
            }*/
        });

        iv_attach.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cv_attach_dialog.setVisibility(View.VISIBLE);
                iv_attach.setVisibility(View.GONE);
            }
        });

        iv_attach_close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cv_attach_dialog.setVisibility(View.GONE);
                iv_attach.setVisibility(View.VISIBLE);
            }
        });

        ll_image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cv_attach_dialog.setVisibility(View.GONE);
                iv_attach.setVisibility(View.VISIBLE);
                ImagePicker.Companion.with(ChatActivity.this)
                        .crop()                    //Crop image(Optional), Check Customization for more option
                        .compress(1024)            //Final image size will be less than 1 MB(Optional)
                        .maxResultSize(800, 800)    //Final image resolution will be less than 1080 x 1080(Optional)
                        .saveDir(new File(Environment.getExternalStorageDirectory(), "ImagePicker"))
                        .start(2000);
            }
        });

        custom_message.setOnClickListener(view -> {
            openCustomDialog();
        });

        ll_file.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cv_attach_dialog.setVisibility(View.GONE);
                iv_attach.setVisibility(View.VISIBLE);
                selectDocument();
            }
        });

        ll_audio.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cv_attach_dialog.setVisibility(View.GONE);
                iv_attach.setVisibility(View.VISIBLE);
                selectAudio();
            }
        });

        ll_location.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cv_attach_dialog.setVisibility(View.GONE);
                iv_attach.setVisibility(View.VISIBLE);
                createAlertDialog();
            }
        });

        video_call.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent1 = new Intent(ChatActivity.this, VideoInviteActivity.class);
                String identityToInvite;
                memberList = chanModel.getChannel().getMembers().getMembersList();
                for (Member member : memberList) {
                    if (member.getIdentity().startsWith("SV")) {
                        identityToInvite = member.getIdentity();
                        SharedPreferencesUtils.saveIdentityToInviteForVideoCall(identityToInvite);
                    }
                }
                if (!SharedPreferencesUtils.getIsLoggedIn()) {
                    intent1.putExtra("volID", SharedPreferencesUtils.getIdentityToInviteForVideoCall());
                } else {
                    identityToInvite = chanModel.getChannel().getFriendlyName();
                    SharedPreferencesUtils.saveIdentityToInviteForVideoCall(identityToInvite);
                    intent1.putExtra("volID", SharedPreferencesUtils.getIdentityToInviteForVideoCall());
                }
                intent1.putExtra("caseID", chanModel.getChannel().getFriendlyName());
                //intent1.putExtra("volID", identity);
                Log.i(TAG, "onClick: Identity to register for video call is : " + SharedPreferencesUtils.getIdentityToInviteForVideoCall());
                //intent1.putExtra("caseID", chanModel.getChannel().getFriendlyName());
                startActivity(intent1);
            }
        });

        imageView35.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                createOptionsDialog();
            }
        });

        recyclerView.addOnLayoutChangeListener(new View.OnLayoutChangeListener() {
            @Override
            public void onLayoutChange(View v, int left, int top, int right, int bottom, int oldLeft, int oldTop, int oldRight, int oldBottom) {
                if (bottom < oldBottom) {
                    recyclerView.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            recyclerView.scrollToPosition(messagesAdaptorChanges.size() - 1);
                        }
                    }, 100);
                }
            }
        });

    }

    private void createAlertDialog() {
        final android.app.AlertDialog.Builder alert;
        alert = new android.app.AlertDialog.Builder(ChatActivity.this);
        final LayoutInflater inflater = LayoutInflater.from(ChatActivity.this);
        View alertLayout = inflater.inflate(R.layout.client_contact_container_cpy1, null);
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
                shareLocationMessage();
                dialog.dismiss();
            }
        });
        btnNo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        if (!ChatActivity.this.isFinishing() && !ChatActivity.this.isDestroyed()) {
            dialog.show();
        }
    }

    private void createLogoutAlert() {
        final android.app.AlertDialog.Builder alert;
        alert = new android.app.AlertDialog.Builder(ChatActivity.this);
        final LayoutInflater inflater = LayoutInflater.from(ChatActivity.this);
        View alertLayout = inflater.inflate(R.layout.client_contact_container_cpy2, null);
        ImageView closeImg = alertLayout.findViewById(R.id.closeBtn);
        Button btnYes = alertLayout.findViewById(R.id.btn_yes);
        Button btnNo = alertLayout.findViewById(R.id.btn_no);
        alert.setView(alertLayout);
        alert.setCancelable(false);
        final android.app.AlertDialog dialog = alert.create();
        dialog.setCanceledOnTouchOutside(false);
        closeImg.setOnClickListener(v -> dialog.dismiss());
        btnYes.setOnClickListener(v -> {
            dialog.dismiss();
            //TODO: Exit
            Intent i = new Intent(ChatActivity.this, WebViewActivity.class);
            i.putExtra("type", "victim");
            i.putExtra("url", "https://shamsaha.org/app/apis/webview/feedback?case_id=" + caseId + "&volunteer_id=" + vId);
            startActivity(i);
            currentChannel.leave(new StatusListener() {
                @Override
                public void onSuccess() {
                    finish();
                }
            });

        });
        btnNo.setOnClickListener(v -> dialog.dismiss());
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        if (!ChatActivity.this.isFinishing() && !ChatActivity.this.isDestroyed()) {
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

                        ArrayAdapter<AdditionalLanguageSpinnerAdaptor> arrayAdapter = new ArrayAdapter<>(ChatActivity.this, android.R.layout.simple_spinner_item, location);
                        arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

                        spinner.setAdapter(arrayAdapter);

                        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                            @Override
                            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                                AdditionalLanguageSpinnerAdaptor spinnerAdaptor = (AdditionalLanguageSpinnerAdaptor) parent.getSelectedItem();
//                                Toast.makeText(VolunterResourceActivity.this, "ID : "+spinnerAdaptor.getId()+"\n" +
//                                        "\nLocation : "+spinnerAdaptor.getLocation(), Toast.LENGTH_SHORT).show();

                                editText.setText("There is an immediate language need in " + spinnerAdaptor.getLanguage() + " language. Can you back up " + chanModel.getChannel().getUniqueName() + " and take over this case right now ?");

                                Language = spinnerAdaptor.getLanguage();
                            }

                            @Override
                            public void onNothingSelected(AdapterView<?> parent) {

                            }
                        });

                    }

                } else {
                    Toast.makeText(ChatActivity.this, "Something went wrong (OR) check your internet connection...!", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<List<AdditionalLanguageModel>> call, Throwable t) {
                Toast.makeText(ChatActivity.this, "Something went wrong (OR) check your internet connection...!\n" + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }


    private void createOptionsDialog() {
        dialog = new Dialog(ChatActivity.this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(false);
        dialog.setContentView(R.layout.client_contact_container_cpy3);
//        final android.app.AlertDialog.Builder alert;
//        alert = new android.app.AlertDialog.Builder(ChatActivity.this);
//        final LayoutInflater inflater = LayoutInflater.from(ChatActivity.this);
//        View alertLayout = inflater.inflate(R.layout.client_contact_container_cpy3, null);
        ImageView closeImg = dialog.findViewById(R.id.closeBtn);
        ImageView closeImg1 = dialog.findViewById(R.id.closeBtn1);
        ImageView closeImg2 = dialog.findViewById(R.id.closeBtn2);
        ImageView closeImg3 = dialog.findViewById(R.id.closeBtn3);
        CardView cvService = dialog.findViewById(R.id.cv_service);
        CardView cvHelp = dialog.findViewById(R.id.cv_help);
        CardView cvStatus = dialog.findViewById(R.id.cv_status);
        ConstraintLayout body = dialog.findViewById(R.id.body);
        ConstraintLayout body1 = dialog.findViewById(R.id.body1);
        ConstraintLayout body2 = dialog.findViewById(R.id.body2);
        ConstraintLayout SupportMenu = dialog.findViewById(R.id.SupportMenu);
        RadioGroup radioGroup = dialog.findViewById(R.id.radio_group);
        CardView ramada = dialog.findViewById(R.id.ramada);
        CardView workstation = dialog.findViewById(R.id.workstation);
        WebView webview = dialog.findViewById(R.id.webview);
        Button done = dialog.findViewById(R.id.btn_yes);
        Button btnSupport = dialog.findViewById(R.id.btnSupport);
        LinearLayout backupSupportBody = dialog.findViewById(R.id.backupSupportBody);
        LinearLayout languageSupportBody = dialog.findViewById(R.id.languageSupportBody);
        RadioButton backupButton = dialog.findViewById(R.id.backupButton);
        RadioButton languageButton = dialog.findViewById(R.id.languageButton);
        Spinner spinner = dialog.findViewById(R.id.spinner2);
        EditText langMessage = dialog.findViewById(R.id.editTextTextMultiLine);
        EditText backupMessage = dialog.findViewById(R.id.backupMessage);
        CheckBox police = dialog.findViewById(R.id.Police);
        CheckBox Hospital = dialog.findViewById(R.id.Hospital);
        CheckBox Court = dialog.findViewById(R.id.Court);

        spinnerApi(spinner, langMessage);

//        if(languageSupport){
//            backupSupportBody.setVisibility(View.GONE);
//            languageSupportBody.setVisibility(View.VISIBLE);
//        }else{
//            backupSupportBody.setVisibility(View.VISIBLE);
//            languageSupportBody.setVisibility(View.GONE);
//        }


        body.setVisibility(View.VISIBLE);
        body1.setVisibility(View.GONE);
        body2.setVisibility(View.GONE);
        SupportMenu.setVisibility(View.GONE);
//        final android.app.AlertDialog dialog = alert.create();
        dialog.setCanceledOnTouchOutside(false);
        closeImg.setOnClickListener(v -> dialog.dismiss());
        closeImg1.setOnClickListener(v -> dialog.dismiss());
        closeImg2.setOnClickListener(v -> dialog.dismiss());
        closeImg3.setOnClickListener(v -> dialog.dismiss());
        cvService.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                body.setVisibility(View.GONE);
                body2.setVisibility(View.VISIBLE);

            }
        });
        cvHelp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                body.setVisibility(View.GONE);
                body2.setVisibility(View.GONE);
                SupportMenu.setVisibility(View.VISIBLE);
//                languageButton.setActivated(true);
//                if(languageSupport){
//                    backupSupportBody.setVisibility(View.GONE);
//                    languageSupportBody.setVisibility(View.VISIBLE);
//                }else{
//                    backupSupportBody.setVisibility(View.VISIBLE);
//                    languageSupportBody.setVisibility(View.GONE);
//                }

            }
        });

        if (languageButton.isChecked()) {
            languageSupportBody.setVisibility(View.VISIBLE);
            backupSupportBody.setVisibility(View.GONE);
//            Toast.makeText(this, "languageButton", Toast.LENGTH_SHORT).show();
        }

        backupButton.setOnClickListener(view -> {
            if (backupButton.isChecked()) {
                backupSupportBody.setVisibility(View.VISIBLE);
                languageSupportBody.setVisibility(View.GONE);
            } else {
                backupSupportBody.setVisibility(View.GONE);
                languageSupportBody.setVisibility(View.VISIBLE);
            }
//            Toast.makeText(this, "Lang", Toast.LENGTH_SHORT).show();
        });

        languageButton.setOnClickListener(view -> {
            if (languageButton.isChecked()) {
                backupSupportBody.setVisibility(View.GONE);
                languageSupportBody.setVisibility(View.VISIBLE);
            } else {
                backupSupportBody.setVisibility(View.VISIBLE);
                languageSupportBody.setVisibility(View.GONE);
            }
//            Toast.makeText(this, "Lang", Toast.LENGTH_SHORT).show();
        });

        btnSupport.setOnClickListener(view -> {
            if (languageButton.isChecked()) {
                hitSendLanguageRequest(Language, chanModel.getChannel().getUniqueName(), langMessage.getText().toString(), "Language");
            }
            if (backupButton.isChecked()) {
                StringBuilder support = new StringBuilder();

                if (police.isChecked()) {
                    support.append("Police, ");
                }
                if (Hospital.isChecked()) {
                    support.append("Hospital, ");
                }
                if (Court.isChecked()) {
                    support.append("Court");
                }
                if (support.toString().isEmpty() || support.toString().equals("") || backupMessage.getText().toString().isEmpty() || backupMessage.getText().toString().equals("")) {
                    Toast.makeText(ChatActivity.this, "Fill the form", Toast.LENGTH_SHORT).show();
                } else {
                    hitSendLanguageRequest("", chanModel.getChannel().getUniqueName(), backupMessage.getText().toString(), support.toString());
                }
            }
            dialog.dismiss();
        });


        cvStatus.setOnClickListener(v -> {
            body.setVisibility(View.GONE);
            body1.setVisibility(View.VISIBLE);
        });

        done.setOnClickListener(v -> {
            int status;
            int selectedId = radioGroup.getCheckedRadioButtonId();
            RadioButton radioButton = radioGroup.findViewById(selectedId);
            if (radioButton.getText() != null) {
                if (radioButton.getText().toString().equalsIgnoreCase(getString(R.string.case_closed))) {
                    status = 2;
                } else {
                    status = 1;
                }
                showActivityIndicator(getResources().getString(R.string.please_wait));
                api api = retrofit.retrofit.create(api.class);
                Call<ChannelStatusModel> call = api.postCaseStatus(chanModel.getChannel().getFriendlyName(), identity, "", chanModel.getChannel().getFriendlyName().replace("CI", ""), String.valueOf(status));
                call.enqueue(new Callback<ChannelStatusModel>() {
                    @Override
                    public void onResponse(Call<ChannelStatusModel> call, Response<ChannelStatusModel> response) {
                        stopActivityIndicator();
                        dialog.dismiss();
                        Intent intent = new Intent(ChatActivity.this, ChatCaseReportActivity.class);
                        intent.putExtra("volId", identity);
                        intent.putExtra("caseId", chanModel.getChannel().getFriendlyName());
                        startActivity(intent);
                        finish();
                    }

                    @Override
                    public void onFailure(Call<ChannelStatusModel> call, Throwable t) {
                        stopActivityIndicator();
                    }
                });
            }
        });

        ramada.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String url = baseURL.BaseURL_API + "services/ramada/?volunteer_id=" + identity + "&case_id=" + chanModel.getChannel().getFriendlyName();
//                ramada.setVisibility(View.GONE);
//                workstation.setVisibility(View.GONE);
//                webview.setVisibility(View.VISIBLE);
//                webview.loadUrl(baseURL.BaseURL_API + "services/ramada?volunteer_id=" + identity + "&&case_id=" + chanModel.getChannel().getFriendlyName());
                Intent i = new Intent(ChatActivity.this, WebViewActivity.class);
                i.putExtra("type", "volunteer");
                i.putExtra("url", url);
                startActivity(i);

            }
        });

        workstation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(ChatActivity.this, WebViewActivity.class);
                i.putExtra("type", "volunteer");
                i.putExtra("url", baseURL.BaseURL_API + "services/wokstation?volunteer_id=" + identity + "&case_id=" + chanModel.getChannel().getFriendlyName());
                startActivity(i);
//                ramada.setVisibility(View.GONE);
//                workstation.setVisibility(View.GONE);
//                webview.setVisibility(View.VISIBLE);
//                webview.loadUrl(baseURL.BaseURL_API + "services/wokstation?volunteer_id=" + identity + "&&case_id=" + chanModel.getChannel().getFriendlyName());
            }
        });

//        dialog.show();
        dialog.getWindow().setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        if (!ChatActivity.this.isFinishing() && !ChatActivity.this.isDestroyed()) {
            dialog.show();
        }

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
                        Toast.makeText(getApplicationContext(), " " + response.body().getMessage() + " ", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(getApplicationContext(), " " + response.body().getMessage() + " ", Toast.LENGTH_SHORT).show();
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
                StyleableToast.makeText(ChatActivity.this, t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }


    private void shareLocationMessage() {
        StringBuffer message = new StringBuffer();
        message.append("Current Location");
        message.append("\n\nhttps://maps.google.com/maps?q=loc:" + lat + "," + lon);
        Message.Options messageOptions = Message.options().withBody(message.toString());
        this.messagesObject.sendMessage(messageOptions, null);
    }

    private void selectDocument() {
        new MaterialFilePicker()
                .withActivity(ChatActivity.this)
                .withCloseMenu(true)
                .withRequestCode(1000)
                .withHiddenFiles(true)
                .withFilter(Pattern.compile(".*\\.(txt|docx|pdf|apk|xml|mp4|xlsx|ppt)$"))
                .withTitle("select Document")
                .start();
    }

    private void selectAudio() {
        new MaterialFilePicker()
                .withActivity(ChatActivity.this)
                .withCloseMenu(true)
                .withRequestCode(3000)
                .withHiddenFiles(true)
                .withFilter(Pattern.compile(".*\\.(mp3)$"))
                .withTitle("select Audio")
                .start();
    }

    public void initAudioRecorder() {
        audioRecorder = AudioRecorder.newBuilder()
                // Required
                .useInBuildFilePathGenerator(this)
                .setDuration(10)
                .build();
        // Optional
//                .setDuration(10)
//                .setAudioSource(MediaRecorder.AudioSource.MIC)
//                .setOutputFormat(MediaRecorder.OutputFormat.MPEG_4)
//                .setAudioEncoder(MediaRecorder.AudioEncoder.AAC)
//                .setAudioSamplingRate(44100)
//                .setAudioChannels(CHANNEL_STEREO)
//                .setAudioEncodingBitRate(96000)
        audioRecorder.setMediaRecordListener(new QBMediaRecordListenerImpl());
    }

    public void requestPermission() {
        ActivityCompat.requestPermissions(this, permissions, REQUEST_RECORD_AUDIO_WRITE_EXTERNAL_STORAGE_PERMISSIONS);
    }

    private void joinChannel(final Channel selectedChannel) {
        if (!(selectedChannel == null)) {
//            Toast.makeText(this, "1" + selectedChannel.getUniqueName(), Toast.LENGTH_SHORT).show();
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    setCurrentChannel(selectedChannel, new StatusListener() {
                        @Override
                        public void onSuccess() {
                            //memberList = chanModel.getChannel().getMembers().getMembersList();
//                            Toast.makeText(ChatActivity.this, "joinChannel onSuccess", Toast.LENGTH_SHORT).show();
                        }

                        @Override
                        public void onError(ErrorInfo errorInfo) {
//                            Toast.makeText(ChatActivity.this, "" + errorInfo, Toast.LENGTH_SHORT).show();
                            joinChannel(selectedChannel);
                        }
                    });
                }
            });
        } else {
//            Toast.makeText(this, "empty", Toast.LENGTH_SHORT).show();
        }

    }

    private void sendMessage() throws FileNotFoundException {
        String messageText = messageTextEdit.getText().toString();
        if (messageText.length() == 0) {
            return;
        }
        messageTextEdit.setText("");
        Message.Options messageOptions = Message.options().withBody(messageText);
        if (messagesObject != null) {
            this.messagesObject.sendMessage(messageOptions, null);
        }
    }

    private void sendMessage(String message) throws FileNotFoundException {
        if (message.length() == 0) {
            return;
        }
        messageTextEdit.setText("");
        Message.Options messageOptions = Message.options().withBody(message);
        if (messagesObject != null) {
            this.messagesObject.sendMessage(messageOptions, null);
        }
//        file = new File(path);
//
//        // Messages messagesObject;
//        messagesObject.sendMessage(
//                Message.options()
//                        .withMedia(new FileInputStream(path), "image/jpeg")
//                        .withMediaFileName("image.jpg")
//                        .withMediaProgressListener(new ProgressListener() {
//                            @Override
//                            public void onStarted() {
////                                Timber.d("Upload started");
//                                Log.d(TAG, "Upload started");
//                            }
//
//                            @Override
//                            public void onProgress(long bytes) {
////                                Timber.d("Uploaded " + bytes + " bytes");
//
//                                Log.d(TAG, "Uploaded " + bytes + " bytes");
//
//                            }
//
//                            @Override
//                            public void onCompleted(String mediaSid) {
////                                Timber.d("Upload completed");
//                                Log.d(TAG, "Upload completed");
//                            }
//                        }),
//                new CallbackListener<Message>() {
//                    @Override
//                    public void onSuccess(Message msg) {
////                        Timber.d("Successfully sent MEDIA message");
//                        Log.d(TAG, "Successfully sent MEDIA message");
//                    }
//
//                    @Override
//                    public void onError(ErrorInfo error) {
////                        Timber.e("Error sending MEDIA message");
//                        Log.d(TAG, "Error sending MEDIA message");
//                    }
//                });
//


    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        try {
            if (requestCode == 1000 && resultCode == RESULT_OK) {
                path = data.getStringExtra(FilePickerActivity.RESULT_FILE_PATH);
                file = new File(path);
                String extension = getFileExtension(file);
                String mimeType = getMimeType(path);
                sendMediaMessage(extension, mimeType);
            } else if (requestCode == 2000 && resultCode == RESULT_OK) {
                path = ImagePicker.Companion.getFilePath(data);
                file = new File(path);
                String extension = getFileExtension(file);
                String mimeType = getMimeType(path);
                sendMediaMessage(extension, mimeType);
            } else if (requestCode == 3000 && resultCode == RESULT_OK) {
                path = data.getStringExtra(FilePickerActivity.RESULT_FILE_PATH);
                file = new File(path);
                String extension = getFileExtension(file);
                String mimeType = getMimeType(path);
                sendMediaMessage(extension, mimeType);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void sendMediaMessage(String extension, String mimeType) {
        try {
            messagesObject.sendMessage(
                    Message.options()
                            .withMedia(new FileInputStream(path), mimeType)
                            .withMediaFileName("media" + extension)
                            .withMediaProgressListener(new ProgressListener() {
                                @Override
                                public void onStarted() {
                                    showActivityIndicator(getResources().getString(R.string.upload_started));
                                    Log.d(TAG, "Upload started");
                                }

                                @Override
                                public void onProgress(long bytes) {
                                    Log.d(TAG, "Uploaded " + bytes + " bytes");

                                }

                                @Override
                                public void onCompleted(String mediaSid) {
                                    Log.d(TAG, "Upload completed");
                                }
                            }),
                    new CallbackListener<Message>() {
                        @Override
                        public void onSuccess(Message msg) {
                            stopActivityIndicator();
                            Log.d(TAG, "Successfully sent MEDIA message");
                        }

                        @Override
                        public void onError(ErrorInfo error) {
                            Log.d(TAG, "Error sending MEDIA message");
                        }
                    });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public void setCurrentChannel(@NotNull Channel currentChannel, final StatusListener handler) {
//        Toast.makeText(this, ""+currentChannel.getUniqueName(), Toast.LENGTH_SHORT).show();q
        if (!currentChannel.equals(this.currentChannel)) {
            this.currentChannel = currentChannel;
            this.currentChannel.addListener(this);
            Log.d(TAG, "setCurrentChannel Channel.ChannelStatus : " + Channel.ChannelStatus.JOINED.toString());
            //currentChannel.addListener(this);
            if (currentChannel.getStatus() == Channel.ChannelStatus.JOINED) {
//                    Toast.makeText(this, "\uD83D\uDE04", Toast.LENGTH_SHORT).show();
                loadMessages(currentChannel, handler);
            } else {
                currentChannel.join(new StatusListener() {
                    @Override
                    public void onSuccess() {
                        loadMessages(currentChannel, handler);
                    }

                    @Override
                    public void onError(ErrorInfo errorInfo) {
                        Log.d(TAG, "onError: " + errorInfo + " " + errorInfo.getCode());
//                        Toast.makeText(ChatActivity.this, errorInfo.getMessage(), Toast.LENGTH_SHORT).show();
                        if (errorInfo.getMessage().equals("Member already exists")) {
                            loadMessages(currentChannel, handler);
                        }
                    }
                });
            }
        }

    }

    private void loadMessages(@NotNull Channel currentChannel, final StatusListener handler) {

        messagesObject = currentChannel.getMessages();
        if (messagesObject != null) {
            messagesObject.getLastMessages(100, new CallbackListener<List<Message>>() {
                @Override
                public void onSuccess(List<Message> messageList) {
                    Log.d(TAG, "onSuccess: " + messageList.size());
//                        Toast.makeText(ChatActivity.this, ""+messageList.size(), Toast.LENGTH_SHORT).show();
                    messagesAdaptorChanges.addAll(messageList);
                    messageAdapter.setMessages(messagesAdaptorChanges);
                    setMessageInputEnabled(true);
                    if (messageTextEdit != null)
                        messageTextEdit.requestFocus();

                    chatAdapter1 = new QBMessagesAdapter(ChatActivity.this, messagesAdaptorChanges);
                    mediaManager = chatAdapter1.getMediaManagerInstance();
                    String author = "";
                    if (!SharedPreferencesUtils.getIsLoggedIn()) {
                        author = caseId;
                    } else if (SharedPreferencesUtils.getIsLoggedIn()) {
                        author = identity;
                        Log.i(TAG, "onSuccess: author in chat is :"+author);
                    }
                    chatAdaptor = new ChatAdaptor(ChatActivity.this, messagesAdaptorChanges, author);
                    recyclerView.setAdapter(chatAdaptor);
                    handler.onSuccess();
                }

                @Override
                public void onError(ErrorInfo errorInfo) {
                    super.onError(errorInfo);
                    Toast.makeText(ChatActivity.this, "" + errorInfo, Toast.LENGTH_SHORT).show();
                }
            });
        } else {
//            Toast.makeText(ChatActivity.this, " Try again later ", Toast.LENGTH_SHORT).show();
//            i++;
            loadMessages(currentChannel, handler);
        }
//        Toast.makeText(this, "i "+i, Toast.LENGTH_SHORT).show();
    }

    private void setMessageInputEnabled(final boolean enabled) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (sendButton != null)
                    sendButton.setEnabled(enabled);
                if (messageTextEdit != null)
                    messageTextEdit.setEnabled(enabled);
            }
        });
    }

    private void openCustomDialog() {
        final android.app.AlertDialog.Builder alert;
        alert = new android.app.AlertDialog.Builder(ChatActivity.this);
        final LayoutInflater inflater = LayoutInflater.from(ChatActivity.this);
        View alertLayout = inflater.inflate(R.layout.custom_messages, null);
        ImageView closeImg = alertLayout.findViewById(R.id.closeBtn2);
        alert.setView(alertLayout);
        alert.setCancelable(false);
        final android.app.AlertDialog dialog = alert.create();
        TextView messageText1 = alertLayout.findViewById(R.id.message1);
        TextView messageText2 = alertLayout.findViewById(R.id.message2);
        TextView messageText1A = alertLayout.findViewById(R.id.message11);
        TextView messageText2A = alertLayout.findViewById(R.id.message3);
        dialog.setCanceledOnTouchOutside(false);
        closeImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        messageText1.setOnClickListener(view -> {
            try {
                sendMessage(messageText1.getText().toString());
                dialog.dismiss();
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
        });

        messageText1A.setOnClickListener(view -> {
            try {
                sendMessage(messageText1A.getText().toString());
                dialog.dismiss();
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
        });

        messageText2A.setOnClickListener(view -> {
            try {
                sendMessage(messageText2A.getText().toString());
                dialog.dismiss();
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
        });

        messageText2.setOnClickListener(view -> {
            try {
                sendMessage(messageText2.getText().toString());
                dialog.dismiss();
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
        });
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        if (!ChatActivity.this.isFinishing() && !ChatActivity.this.isDestroyed()) {
            dialog.show();
        }
    }


    @Override
    public void onMessageAdded(Message message) {
        messagesAdaptorChanges.add(message);
        chatAdaptor.notifyItemInserted(messagesAdaptorChanges.size() - 1);
        recyclerView.postDelayed(new Runnable() {
            @Override
            public void run() {
                recyclerView.scrollToPosition(messagesAdaptorChanges.size() - 1);
            }
        }, 100);
//        chatAdaptor.notifyDataSetChanged();
        //mPlayer.start();
    }

    @Override
    public void onMessageUpdated(Message message, Message.UpdateReason updateReason) {

    }

    @Override
    public void onMessageDeleted(Message message) {

    }

    @Override
    public void onMemberAdded(Member member) {
        Message.Options options = Message.options().withBody(String.format(Locale.getDefault(), "User %s has Joined", member.getIdentity()));
        // new Message(options);
        //messagesAdaptorChanges.add()
    }

    @Override
    public void onMemberUpdated(Member member, Member.UpdateReason updateReason) {

    }

    @Override
    public void onMemberDeleted(Member member) {

    }

    @Override
    public void onTypingStarted(Channel channel, Member member) {
        Log.e("fvhsdbv", "fbhdsj");
    }

    @Override
    public void onTypingEnded(Channel channel, Member member) {
        Log.e("fvhsdbv", "fbhdsj");
    }

    @Override
    public void onSynchronizationChanged(Channel channel) {

    }

    private String getFileExtension(File file) {
        String name = file.getName();
        int lastIndexOf = name.lastIndexOf(".");
        if (lastIndexOf == -1) {
            return ""; // empty extension
        }
        return name.substring(lastIndexOf);
    }

    public String getMimeType(String url) {
        String type = null;
        String extension = MimeTypeMap.getFileExtensionFromUrl(url);
        if (extension != null) {
            type = MimeTypeMap.getSingleton().getMimeTypeFromExtension(extension);
        }
        return type;
    }

    private void showActivityIndicator(final String message) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                progressDialog = new ProgressDialog(ChatActivity.this);
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

    public void startRecord() {
        Log.d(TAG, "startRecord");
        recordChronometer.setBase(SystemClock.elapsedRealtime());
        recordChronometer.start();
        recordChronometer.setVisibility(View.VISIBLE);
        audioRecordTextView.setVisibility(View.VISIBLE);
        vibro.vibrate(100);
        audioLayout.setVisibility(View.VISIBLE);
        relSendMessage.setVisibility(View.GONE);
        audioRecorder.startRecord();
    }

    public void stopRecord() {
        Log.d(TAG, "stopRecord");
        recordChronometer.stop();
        vibro.vibrate(100);
        audioLayout.setVisibility(View.INVISIBLE);
        relSendMessage.setVisibility(View.VISIBLE);
        audioRecorder.stopRecord();
    }

    public void cancelRecord() {
        Log.d(TAG, "cancelRecord");
        hideRecordView();
        Animation shake = AnimationUtils.loadAnimation(this, R.anim.shake);
        bucketView.startAnimation(shake);
        vibro.vibrate(100);
        audioLayout.postDelayed(new Runnable() {
            @Override
            public void run() {
                audioLayout.setVisibility(View.INVISIBLE);
                relSendMessage.setVisibility(View.VISIBLE);
            }
        }, 1500);

        audioRecorder.cancelRecord();
    }

    public void clearRecorder() {
        hideRecordView();
        audioRecorder.releaseMediaRecorder();
    }

    private void hideRecordView() {
        recordChronometer.stop();
        recordChronometer.setVisibility(View.INVISIBLE);
        audioRecordTextView.setVisibility(View.INVISIBLE);
    }

    public void processSendMessage(@NotNull File file) {
        path = file.getPath();
        file = new File(path);
        String extension = getFileExtension(file);
        String mimeType = getMimeType(path);
        sendMediaMessage(extension, mimeType);
        Toast.makeText(this, "Audio recorded! " + path + extension + mimeType, Toast.LENGTH_LONG).show();
        Log.d(TAG, "processSendMessage file= " + file);
    }

    @Override
    public void onLocationChanged(@NotNull Location location) {
        lat = location.getLatitude();
        lon = location.getLongitude();
        uncertainity = location.getAccuracy();
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {

    }

    @Override
    public void onProviderEnabled(String provider) {

    }

    @Override
    public void onProviderDisabled(String provider) {

    }

    private class QBMediaRecordListenerImpl implements QBMediaRecordListener {

        @Override
        public void onMediaRecorded(File file) {
            audioLayout.setVisibility(View.INVISIBLE);
            relSendMessage.setVisibility(View.VISIBLE);
            processSendMessage(file);
        }

        @Override
        public void onMediaRecordError(MediaRecorderException e) {
            Log.d(TAG, "onMediaRecordError e= " + e.getMessage());
            clearRecorder();
        }

        @Override
        public void onMediaRecordClosed() {
            Toast.makeText(ChatActivity.this, "Audio is not recorded", Toast.LENGTH_LONG).show();
        }
    }

    private class RecordTouchListenerImpl implements QBRecordAudioButton.RecordTouchEventListener {

        @Override
        public void onStartClick(View view) {
            startRecord();
        }

        @Override
        public void onCancelClick(View view) {
            cancelRecord();
        }

        @Override
        public void onStopClick(View view) {
            stopRecord();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case REQUEST_RECORD_AUDIO_WRITE_EXTERNAL_STORAGE_PERMISSIONS:
                permissionToRecordAccepted = grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED &&
                        grantResults[1] == PackageManager.PERMISSION_GRANTED;
                break;

            case MY_PERMISSIONS_REQUEST_LOCATION: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    if (ContextCompat.checkSelfPermission(this,
                            Manifest.permission.ACCESS_FINE_LOCATION)
                            == PackageManager.PERMISSION_GRANTED) {
                        //Request location updates:
                        this.registerLocationListener();
                    } else {
                        /* TODO: handle denial gracefully */
                    }

                } else {
                    /* TODO: handle denial gracefully */

                }
                return;
            }
        }
        if (!permissionToRecordAccepted) finish();
    }

    @Override
    protected void onResume() {
        super.onResume();
//        init playing via callback or via handleMessage
        if (mediaManager != null && mediaManager.isMediaPlayerReady()) {
            mediaManager.resumePlay();
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
//       release player via callback or via handleMessage
        if (mediaManager != null && mediaManager.isMediaPlayerReady()) {
            mediaManager.suspendPlay();
        }
    }

    public void checkLocationPermission() {
        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {

            // Should we show an explanation?
            if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                    Manifest.permission.ACCESS_FINE_LOCATION)) {

                ActivityCompat.requestPermissions(ChatActivity.this,
                        new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                        MY_PERMISSIONS_REQUEST_LOCATION);
            } else {
                // No explanation needed, we can request the permission.
                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                        MY_PERMISSIONS_REQUEST_LOCATION);
            }
        } else {
            this.registerLocationListener();
        }
    }

    private void registerLocationListener() {
        LocationManager locationManager;
        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        Criteria criteria = new Criteria();
        criteria.setAccuracy(Criteria.ACCURACY_COARSE);
        criteria.setPowerRequirement(Criteria.POWER_LOW);
        criteria.setAltitudeRequired(false);
        criteria.setBearingRequired(false);
        criteria.setSpeedRequired(false);
        criteria.setCostAllowed(false);
        criteria.setHorizontalAccuracy(Criteria.ACCURACY_LOW);
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        location = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
        lat = 0.0;
        lon = 0.0;
        nonEmpty = false;
        if (location != null) {
            lat = location.getLatitude();
            lon = location.getLongitude();
            uncertainity = location.getAccuracy();
        }
    }

    private void joinFromNotification() {
        // channelManager.joinOrCreateGeneralChannelWithCompletion();
    }
}