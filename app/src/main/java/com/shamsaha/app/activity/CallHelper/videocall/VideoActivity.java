package com.shamsaha.app.activity.CallHelper.videocall;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.media.AudioAttributes;
import android.media.AudioFocusRequest;
import android.media.AudioManager;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.installations.FirebaseInstallations;
import com.google.firebase.messaging.FirebaseMessaging;
import com.shamsaha.app.ApiModel.ModelToken;
import com.shamsaha.app.R;
import com.shamsaha.app.api.api;
import com.twilio.audioswitch.AudioSwitch;
import com.twilio.video.CameraCapturer;
import com.twilio.video.ConnectOptions;
import com.twilio.video.LocalAudioTrack;
import com.twilio.video.LocalParticipant;
import com.twilio.video.LocalVideoTrack;
import com.twilio.video.RemoteAudioTrack;
import com.twilio.video.RemoteAudioTrackPublication;
import com.twilio.video.RemoteDataTrack;
import com.twilio.video.RemoteDataTrackPublication;
import com.twilio.video.RemoteParticipant;
import com.twilio.video.RemoteVideoTrack;
import com.twilio.video.RemoteVideoTrackPublication;
import com.twilio.video.Room;
import com.twilio.video.TwilioException;
import com.twilio.video.Video;
import com.twilio.video.VideoRenderer;
import com.twilio.video.VideoTrack;
import com.twilio.video.VideoView;
import com.twilio.voice.RegistrationException;
import com.twilio.voice.RegistrationListener;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Locale;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class VideoActivity extends AppCompatActivity {

    private static final int CAMERA_MIC_PERMISSION_REQUEST_CODE = 1;
    public static final List<String> NOTIFY_TAGS = new ArrayList<String>() {{
        add("video");
    }};
    /*
     * Intent keys used to provide information about a video notification
     */
    public static final String ACTION_VIDEO_NOTIFICATION = "VIDEO_NOTIFICATION";
    public static final String VIDEO_NOTIFICATION_ROOM_NAME = "VIDEO_NOTIFICATION_ROOM_NAME";
    public static final String VIDEO_NOTIFICATION_TITLE = "VIDEO_NOTIFICATION_TITLE";

    /*
     * Intent keys used to obtain a token and register with Twilio Notify
     */
    public static final String ACTION_REGISTRATION = "ACTION_REGISTRATION";
    public static final String REGISTRATION_ERROR = "REGISTRATION_ERROR";
    public static final String REGISTRATION_IDENTITY = "REGISTRATION_IDENTITY";
    public static final String REGISTRATION_TOKEN = "REGISTRATION_TOKEN";

    /*
     * Token obtained from the sdk-starter /token resource
     */
    private String token;

    /*
     * Identity obtained from the sdk-starter /token resource
     */
    private String identity;

    /*
     * A Room represents communication between a local participant and one or more participants.
     */
    private Room room;

    private AlertDialog alertDialog;

    /*
     * A LocalParticipant represents the identity and tracks provided by this instance
     */
    private LocalParticipant localParticipant;

    /*
     * A VideoView receives frames from a local or remote video track and renders them
     * to an associated view.
     */
    private VideoView primaryVideoView;
    private VideoView thumbnailVideoView;

    private boolean isReceiverRegistered;
    private LocalBroadcastReceiver localBroadcastReceiver;
    private NotificationManager notificationManager;
    private Intent cachedVideoNotificationIntent;

    /*
     * Android application UI elements
     */
    private TextView statusTextView;
    private TextView identityTextView;
    private CameraCapturer cameraCapturer;
    private LocalAudioTrack localAudioTrack;
    private LocalVideoTrack localVideoTrack;
    private FloatingActionButton connectActionFab;
    private FloatingActionButton switchCameraActionFab;
    private FloatingActionButton localVideoActionFab;
    private FloatingActionButton muteActionFab;
    private ProgressBar reconnectingProgressBar;
    private AudioManager audioManager;
    private String remoteParticipantIdentity;
    private MenuItem turnSpeakerOnMenuItem;
    private MenuItem turnSpeakerOffMenuItem;
    private String accessToken;
    private AudioSwitch audioSwitch;
    private int previousAudioMode;
    private VideoRenderer localVideoView;
    private boolean disconnectedFromOnDestroy;
    private static final String TWILIO_ACCESS_TOKEN_SERVER_URL = "http://chat.sayg.co/";
    private final static String TAG = "VideoActivity";
    RegistrationListener registrationListener = registrationListener();
    private String callTo = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video);
        primaryVideoView = findViewById(R.id.primary_video_view);
        thumbnailVideoView = findViewById(R.id.thumbnail_video_view);
        statusTextView = findViewById(R.id.status_textview);
        identityTextView = findViewById(R.id.identity_textview);

        connectActionFab = findViewById(R.id.connect_action_fab);
        switchCameraActionFab = findViewById(R.id.switch_camera_action_fab);
        localVideoActionFab = findViewById(R.id.local_video_action_fab);
        muteActionFab = findViewById(R.id.mute_action_fab);
        reconnectingProgressBar = findViewById(R.id.reconnecting_progress_bar);

        /*
         * Hide the connect button until we successfully register with Twilio Notify
         */
        connectActionFab.hide();

        /*
         * Enable changing the volume using the up/down keys during a conversation
         */
        setVolumeControlStream(AudioManager.STREAM_VOICE_CALL);

        /*
         * Needed for setting/abandoning audio focus during call
         */
        audioManager = (AudioManager) getSystemService(Context.AUDIO_SERVICE);
        audioManager.setSpeakerphoneOn(true);

        Intent intent1 = getIntent();
        identity = "gowri";
        callTo = intent1.getStringExtra("volID");

        /*
         * Setup the broadcast receiver to be notified of video notification messages
         */
        localBroadcastReceiver = new LocalBroadcastReceiver();
        registerReceiver();

        notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        Intent intent = getIntent();

        /*
         * Check camera and microphone permissions. Needed in Android M.
         */
        if (!checkPermissionForCameraAndMicrophone()) {
            requestPermissionForCameraAndMicrophone();
        } else if (intent != null && intent.getAction() == ACTION_REGISTRATION) {
            handleRegistration(intent);
        } else {
            retrieveAccessToken();
        }
    }

    private void sendRegistrationSuccess(String identity, String token) {
        Intent intent = new Intent(VideoActivity.ACTION_REGISTRATION);
        intent.putExtra(VideoActivity.REGISTRATION_IDENTITY, identity);
        intent.putExtra(VideoActivity.REGISTRATION_TOKEN, token);
        LocalBroadcastManager.getInstance(this).sendBroadcast(intent);
    }

    private void sendRegistrationFailure(String message) {
        Intent intent = new Intent(VideoActivity.ACTION_REGISTRATION);
        intent.putExtra(VideoActivity.REGISTRATION_ERROR, message);
        LocalBroadcastManager.getInstance(this).sendBroadcast(intent);
    }

    private void retrieveAccessToken() {
        Toast.makeText(this, "   " + identity, Toast.LENGTH_SHORT).show();
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(TWILIO_ACCESS_TOKEN_SERVER_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        api api = retrofit.create(api.class);
        Call<ModelToken> call = api.token(identity);
        call.enqueue(new Callback<ModelToken>() {
            String token = null;

            @Override
            public void onResponse(Call<ModelToken> call, Response<ModelToken> response) {
                if (response.isSuccessful()) {
                    assert response.body() != null;
                    token = response.body().getToken();
                    VideoActivity.this.accessToken = token;
                    VideoActivity.this.registerForCallInvites();
                    sendRegistrationSuccess(response.body().getIdentity(), accessToken);
                } else {
                    sendRegistrationFailure("Failed to fetch token");
                    Snackbar.make(connectActionFab,
                            "Error retrieving access token. Unable to make calls",
                            Snackbar.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(Call<ModelToken> call, Throwable t) {
                Log.d("rrrrrrrrrrr", "res: " + t.getMessage());
                sendRegistrationFailure(t.getMessage());
                Snackbar.make(connectActionFab,
                        "Error retrieving access token. Unable to make calls",
                        Snackbar.LENGTH_LONG).show();
            }
        });
//        Ion.with(this).load(TWILIO_ACCESS_TOKEN_SERVER_URL + "?identity=" + identity)
//                .asString()
//                .setCallback(new FutureCallback<String>() {
//                    @Override
//                    public void onCompleted(Exception e, String accessToken) {
//                        if (e == null) {
//                            Log.d(TAG, "Access token: " + accessToken);
//                            VideoActivity.this.accessToken = accessToken;
//                            VideoActivity.this.registerForCallInvites();
//                            sendRegistrationSuccess(identity,accessToken);
//                        } else {
//                            sendRegistrationFailure(e.getMessage());
//                            Snackbar.make(connectActionFab,
//                                    "Error retrieving access token. Unable to make calls",
//                                    Snackbar.LENGTH_LONG).show();
//                        }
//                    }
//                });
    }

    private void registerForCallInvites() {
        FirebaseInstallations.getInstance().getId().addOnSuccessListener(this, new OnSuccessListener<String>() {
            @Override
            public void onSuccess(@NonNull String s) {
                String fcmToken = FirebaseMessaging.getInstance().getToken().toString();

                Log.i(TAG, "Registering with FCM");

                connectToRoom(identity);
                VideoActivity.this.notify(identity);
            }
        });

       /* {
            @Override
            public void onSuccess(InstanceIdResult instanceIdResult) {
                String fcmToken = instanceIdResult.getToken();
                Log.i(TAG, "Registering with FCM");

                connectToRoom(identity);
                VideoActivity.this.notify(identity);
            }
        });*/

    }


    private class LocalBroadcastReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if (action.equals(ACTION_REGISTRATION)) {
                handleRegistration(intent);
            } else if (action.equals(ACTION_VIDEO_NOTIFICATION)) {
                handleVideoNotificationIntent(intent);
            }
        }
    }

    private void registerReceiver() {
        if (!isReceiverRegistered) {
            IntentFilter intentFilter = new IntentFilter();
            intentFilter.addAction(ACTION_VIDEO_NOTIFICATION);
            intentFilter.addAction(ACTION_REGISTRATION);
            LocalBroadcastManager.getInstance(this).registerReceiver(
                    localBroadcastReceiver, intentFilter);
            isReceiverRegistered = true;
        }
    }

    private boolean checkPermissionForCameraAndMicrophone() {
        int resultCamera = ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA);
        int resultMic = ContextCompat.checkSelfPermission(this, Manifest.permission.RECORD_AUDIO);
        return resultCamera == PackageManager.PERMISSION_GRANTED &&
                resultMic == PackageManager.PERMISSION_GRANTED;
    }

    private void requestPermissionForCameraAndMicrophone() {
        if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.CAMERA) ||
                ActivityCompat.shouldShowRequestPermissionRationale(this,
                        Manifest.permission.RECORD_AUDIO)) {
            Toast.makeText(this,
                    "Permission Needed!!",
                    Toast.LENGTH_LONG).show();
        } else {
            ActivityCompat.requestPermissions(
                    this,
                    new String[]{Manifest.permission.CAMERA, Manifest.permission.RECORD_AUDIO},
                    CAMERA_MIC_PERMISSION_REQUEST_CODE);
        }
    }

    private void createLocalTracks() {
        // Share your microphone
        localAudioTrack = LocalAudioTrack.create(this, true);

        // Share your camera
        cameraCapturer = new CameraCapturer(this, getAvailableCameraSource());
        localVideoTrack = LocalVideoTrack.create(this, true, cameraCapturer);
        primaryVideoView.setMirror(true);
        localVideoTrack.addRenderer(primaryVideoView);
        localVideoView = primaryVideoView;
    }

    private CameraCapturer.CameraSource getAvailableCameraSource() {
        return (CameraCapturer.isSourceAvailable(CameraCapturer.CameraSource.FRONT_CAMERA)) ?
                (CameraCapturer.CameraSource.FRONT_CAMERA) :
                (CameraCapturer.CameraSource.BACK_CAMERA);
    }

    private void handleRegistration(Intent intent) {
        String registrationError = intent.getStringExtra(REGISTRATION_ERROR);
        if (registrationError != null) {
            statusTextView.setText(registrationError);
        } else {
            createLocalTracks();
            identity = intent.getStringExtra(REGISTRATION_IDENTITY);
            token = intent.getStringExtra(REGISTRATION_TOKEN);
            identityTextView.setText(identity);
            statusTextView.setText("Registered");
            intializeUI();
            if (cachedVideoNotificationIntent != null) {
                handleVideoNotificationIntent(cachedVideoNotificationIntent);
                cachedVideoNotificationIntent = null;
            }
        }
    }

    private void intializeUI() {
        connectActionFab.setImageDrawable(ContextCompat.getDrawable(this,
                R.drawable.ic_video_call_white_24dp));
//        connectActionFab.show();
//        connectActionFab.setOnClickListener(connectActionClickListener());
        switchCameraActionFab.show();
        switchCameraActionFab.setOnClickListener(switchCameraClickListener());
        localVideoActionFab.show();
        localVideoActionFab.setOnClickListener(localVideoClickListener());
        muteActionFab.show();
        muteActionFab.setOnClickListener(muteClickListener());
//        connectToRoom(identity);
//        VideoActivity.this.notify(identity);
    }

    private void handleVideoNotificationIntent(Intent intent) {
        notificationManager.cancelAll();
        /*
         * Only handle the notification if not already connected to a Video Room
         */
        if (room == null) {
            String title = intent.getStringExtra(VIDEO_NOTIFICATION_TITLE);
            String dialogRoomName = intent.getStringExtra(VIDEO_NOTIFICATION_ROOM_NAME);
            showVideoNotificationConnectDialog(title, dialogRoomName);
        }
    }

    /*
     * Creates a connect UI dialog to handle notifications
     */
    private void showVideoNotificationConnectDialog(String title, String roomName) {
        EditText roomEditText = new EditText(this);
        roomEditText.setText(roomName);
        // Use the default color instead of the disabled color
        int currentColor = roomEditText.getCurrentTextColor();
        roomEditText.setEnabled(false);
        roomEditText.setTextColor(currentColor);
        alertDialog = createConnectDialog(title,
                roomEditText,
                videoNotificationConnectClickListener(roomEditText),
                cancelConnectDialogClickListener(),
                this);
        alertDialog.show();
    }

    public static AlertDialog createConnectDialog(String title,
                                                  EditText roomEditText,
                                                  DialogInterface.OnClickListener callParticipantsClickListener,
                                                  DialogInterface.OnClickListener cancelClickListener,
                                                  Context context) {
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(context);
        alertDialogBuilder.setIcon(R.drawable.ic_video_call_black_24dp);
        alertDialogBuilder.setTitle(title);
        alertDialogBuilder.setPositiveButton("Connect", callParticipantsClickListener);
        alertDialogBuilder.setNegativeButton("Cancel", cancelClickListener);
        alertDialogBuilder.setCancelable(false);
        alertDialogBuilder.setView(roomEditText);
        return alertDialogBuilder.create();
    }

    private void requestAudioFocus() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            AudioAttributes playbackAttributes = new AudioAttributes.Builder()
                    .setUsage(AudioAttributes.USAGE_VOICE_COMMUNICATION)
                    .setContentType(AudioAttributes.CONTENT_TYPE_SPEECH)
                    .build();
            AudioFocusRequest focusRequest =
                    new AudioFocusRequest.Builder(AudioManager.AUDIOFOCUS_GAIN_TRANSIENT)
                            .setAudioAttributes(playbackAttributes)
                            .setAcceptsDelayedFocusGain(true)
                            .setOnAudioFocusChangeListener(
                                    i -> {
                                    })
                            .build();
            audioManager.requestAudioFocus(focusRequest);
        } else {
            audioManager.requestAudioFocus(null, AudioManager.STREAM_VOICE_CALL,
                    AudioManager.AUDIOFOCUS_GAIN_TRANSIENT);
        }
    }

    private void enableVolumeControl(boolean volumeControl) {
        if (volumeControl) {
            /*
             * Enable changing the volume using the up/down keys during a conversation
             */
            setVolumeControlStream(AudioManager.STREAM_VOICE_CALL);
        } else {
            setVolumeControlStream(getVolumeControlStream());
        }
    }

    private void enableAudioFocus(boolean focus) {
        if (focus) {
            previousAudioMode = audioManager.getMode();
            // Request audio focus before making any device switch.
            requestAudioFocus();
            /*
             * Use MODE_IN_COMMUNICATION as the default audio mode. It is required
             * to be in this mode when playout and/or recording starts for the best
             * possible VoIP performance. Some devices have difficulties with
             * speaker mode if this is not set.
             */
            audioManager.setMode(AudioManager.MODE_IN_COMMUNICATION);
        } else {
            audioManager.setMode(previousAudioMode);
            audioManager.abandonAudioFocus(null);
        }
    }

    private View.OnClickListener muteClickListener() {
        return v -> {
            /*
             * Enable/disable the local audio track. The results of this operation are
             * signaled to other Participants in the same Room. When an audio track is
             * disabled, the audio is muted.
             */
            if (localAudioTrack != null) {
                boolean enable = !localAudioTrack.isEnabled();
                localAudioTrack.enable(enable);
                int icon = enable ?
                        R.drawable.ic_mic_white_24dp : R.drawable.ic_mic_off_black_24dp;
                muteActionFab.setImageDrawable(ContextCompat.getDrawable(
                        VideoActivity.this, icon));
            }
        };
    }

    private DialogInterface.OnClickListener connectClickListener(final EditText roomEditText) {
        return (dialog, which) -> {
            final String roomName = roomEditText.getText().toString();
            /*
             * Connect to room
             */
            connectToRoom(roomName);
            /*
             * Notify other participants to join the room
             */
            VideoActivity.this.notify(roomName);
        };
    }

    private DialogInterface.OnClickListener videoNotificationConnectClickListener(final EditText roomEditText) {
        return (dialog, which) -> {
            /*
             * Connect to room
             */
            connectToRoom(roomEditText.getText().toString());
        };
    }

    private View.OnClickListener disconnectClickListener() {
        return v -> {
            /*
             * Disconnect from room
             */
            if (room != null) {
                room.disconnect();
            }
            intializeUI();
        };
    }

    /*
     * Creates a connect UI dialog
     */
    private void showConnectDialog() {
        EditText roomEditText = new EditText(this);
        String title = "Connect to a video room";
        roomEditText.setHint("room name");
        alertDialog = createConnectDialog(title,
                roomEditText,
                connectClickListener(roomEditText),
                cancelConnectDialogClickListener(),
                this);
        alertDialog.show();
    }

    private View.OnClickListener connectActionClickListener() {
        return v -> showConnectDialog();
    }

    private DialogInterface.OnClickListener cancelConnectDialogClickListener() {
        return (dialog, which) -> {
            intializeUI();
            alertDialog.dismiss();
        };
    }

    private View.OnClickListener switchCameraClickListener() {
        return v -> {
            if (cameraCapturer != null) {
                CameraCapturer.CameraSource cameraSource = cameraCapturer.getCameraSource();
                cameraCapturer.switchCamera();
                if (thumbnailVideoView.getVisibility() == View.VISIBLE) {
                    thumbnailVideoView.setMirror(cameraSource == CameraCapturer.CameraSource.BACK_CAMERA);
                } else {
                    primaryVideoView.setMirror(cameraSource == CameraCapturer.CameraSource.BACK_CAMERA);
                }
            }
        };
    }

    private View.OnClickListener localVideoClickListener() {
        return v -> {
            /*
             * Enable/disable the local video track
             */
            if (localVideoTrack != null) {
                boolean enable = !localVideoTrack.isEnabled();
                localVideoTrack.enable(enable);
                int icon;
                if (enable) {
                    icon = R.drawable.ic_videocam_white_24dp;
                    switchCameraActionFab.show();
                } else {
                    icon = R.drawable.ic_videocam_off_black_24dp;
                    switchCameraActionFab.hide();
                }
                localVideoActionFab.setImageDrawable(
                        ContextCompat.getDrawable(VideoActivity.this, icon));
            }
        };
    }

    @SuppressLint("SetTextI18n")
    private RemoteParticipant.Listener mediaListener() {
        return new RemoteParticipant.Listener() {
            @Override
            public void onAudioTrackPublished(RemoteParticipant remoteParticipant,
                                              RemoteAudioTrackPublication remoteAudioTrackPublication) {
                statusTextView.setText("onAudioTrackPublished");
            }

            @Override
            public void onAudioTrackUnpublished(RemoteParticipant remoteParticipant,
                                                RemoteAudioTrackPublication remoteAudioTrackPublication) {
                statusTextView.setText("onAudioTrackPublished");
            }

            @Override
            public void onVideoTrackPublished(RemoteParticipant remoteParticipant,
                                              RemoteVideoTrackPublication remoteVideoTrackPublication) {
                statusTextView.setText("onVideoTrackPublished");
            }

            @Override
            public void onVideoTrackUnpublished(RemoteParticipant remoteParticipant,
                                                RemoteVideoTrackPublication remoteVideoTrackPublication) {
                statusTextView.setText("onVideoTrackUnpublished");
            }

            @Override
            public void onDataTrackPublished(RemoteParticipant remoteParticipant,
                                             RemoteDataTrackPublication remoteDataTrackPublication) {
                statusTextView.setText("onDataTrackPublished");
            }

            @Override
            public void onDataTrackUnpublished(RemoteParticipant remoteParticipant,
                                               RemoteDataTrackPublication remoteDataTrackPublication) {
                statusTextView.setText("onDataTrackUnpublished");
            }

            @Override
            public void onAudioTrackSubscribed(RemoteParticipant remoteParticipant,
                                               RemoteAudioTrackPublication remoteAudioTrackPublication,
                                               RemoteAudioTrack remoteAudioTrack) {
                statusTextView.setText("onAudioTrackSubscribed");
            }

            @Override
            public void onAudioTrackUnsubscribed(RemoteParticipant remoteParticipant,
                                                 RemoteAudioTrackPublication remoteAudioTrackPublication,
                                                 RemoteAudioTrack remoteAudioTrack) {
                statusTextView.setText("onAudioTrackUnsubscribed");
            }

            @Override
            public void onAudioTrackSubscriptionFailed(RemoteParticipant remoteParticipant,
                                                       RemoteAudioTrackPublication remoteAudioTrackPublication,
                                                       TwilioException twilioException) {
                statusTextView.setText("onAudioTrackSubscriptionFailed");
            }

            @Override
            public void onVideoTrackSubscribed(RemoteParticipant remoteParticipant,
                                               RemoteVideoTrackPublication remoteVideoTrackPublication,
                                               RemoteVideoTrack remoteVideoTrack) {
                statusTextView.setText("onVideoTrackSubscribed");
                addRemoteParticipantVideo(remoteVideoTrack);
            }

            @Override
            public void onVideoTrackUnsubscribed(RemoteParticipant remoteParticipant,
                                                 RemoteVideoTrackPublication remoteVideoTrackPublication,
                                                 RemoteVideoTrack remoteVideoTrack) {
                statusTextView.setText("onVideoTrackUnsubscribed");
                removeParticipantVideo(remoteVideoTrack);
            }

            @Override
            public void onVideoTrackSubscriptionFailed(RemoteParticipant remoteParticipant,
                                                       RemoteVideoTrackPublication remoteVideoTrackPublication,
                                                       TwilioException twilioException) {
                statusTextView.setText("onVideoTrackSubscriptionFailed");
                Snackbar.make(connectActionFab,
                        String.format("Failed to subscribe to %s video track",
                                remoteParticipant.getIdentity()),
                        Snackbar.LENGTH_LONG)
                        .show();
            }

            @Override
            public void onDataTrackSubscribed(RemoteParticipant remoteParticipant,
                                              RemoteDataTrackPublication remoteDataTrackPublication,
                                              RemoteDataTrack remoteDataTrack) {
                statusTextView.setText("onDataTrackSubscribed");
            }

            @Override
            public void onDataTrackUnsubscribed(RemoteParticipant remoteParticipant,
                                                RemoteDataTrackPublication remoteDataTrackPublication,
                                                RemoteDataTrack remoteDataTrack) {
                statusTextView.setText("onDataTrackUnsubscribed");
            }

            @Override
            public void onDataTrackSubscriptionFailed(RemoteParticipant remoteParticipant,
                                                      RemoteDataTrackPublication remoteDataTrackPublication,
                                                      TwilioException twilioException) {
                statusTextView.setText("onDataTrackSubscriptionFailed");
            }

            @Override
            public void onAudioTrackEnabled(RemoteParticipant remoteParticipant,
                                            RemoteAudioTrackPublication remoteAudioTrackPublication) {

            }

            @Override
            public void onAudioTrackDisabled(RemoteParticipant remoteParticipant,
                                             RemoteAudioTrackPublication remoteAudioTrackPublication) {

            }

            @Override
            public void onVideoTrackEnabled(RemoteParticipant remoteParticipant,
                                            RemoteVideoTrackPublication remoteVideoTrackPublication) {

            }

            @Override
            public void onVideoTrackDisabled(RemoteParticipant remoteParticipant,
                                             RemoteVideoTrackPublication remoteVideoTrackPublication) {

            }
        };
    }


    /*
     * Room events listener
     */
    @SuppressLint("SetTextI18n")
    private Room.Listener roomListener() {
        return new Room.Listener() {
            @Override
            public void onConnected(Room room) {
                localParticipant = room.getLocalParticipant();
                statusTextView.setText("Connected to " + room.getName());
                setTitle(room.getName());

                for (RemoteParticipant remoteParticipant : room.getRemoteParticipants()) {
                    addRemoteParticipant(remoteParticipant);
                    break;
                }
            }

            @Override
            public void onConnectFailure(Room room, TwilioException e) {
                statusTextView.setText("Failed to connect");
            }

            @Override
            public void onDisconnected(Room room, TwilioException e) {
                localParticipant = null;
                statusTextView.setText("Disconnected from " + room.getName());
                reconnectingProgressBar.setVisibility(View.GONE);
                VideoActivity.this.room = null;
                enableAudioFocus(false);
                enableVolumeControl(false);
                // Only reinitialize the UI if disconnect was not called from onDestroy()
                if (!disconnectedFromOnDestroy) {
                    intializeUI();
                    moveLocalVideoToPrimaryView();
                }
            }

            @Override
            public void onParticipantConnected(Room room, RemoteParticipant remoteParticipant) {
                addRemoteParticipant(remoteParticipant);

            }

            @Override
            public void onParticipantDisconnected(Room room, RemoteParticipant remoteParticipant) {
                removeParticipant(remoteParticipant);
            }

            @Override
            public void onRecordingStarted(Room room) {
                /*
                 * Indicates when media shared to a Room is being recorded. Note that
                 * recording is only available in our Group Rooms developer preview.
                 */
            }

            @Override
            public void onRecordingStopped(Room room) {
                /*
                 * Indicates when media shared to a Room is no longer being recorded. Note that
                 * recording is only available in our Group Rooms developer preview.
                 */
            }

            @Override
            public void onReconnecting(Room room, TwilioException exception) {
                statusTextView.setText("Reconnecting to " + room.getName());
                reconnectingProgressBar.setVisibility(View.VISIBLE);
            }

            @Override
            public void onReconnected(Room room) {
                statusTextView.setText("Connected to " + room.getName());
                reconnectingProgressBar.setVisibility(View.GONE);
            }
        };
    }

    @SuppressLint("SetTextI18n")
    private void removeParticipant(RemoteParticipant remoteParticipant) {
        statusTextView.setText("Participant " + remoteParticipant.getIdentity() + " left.");
        if (!remoteParticipant.getIdentity().equals(remoteParticipantIdentity)) {
            return;
        }

        /*
         * Remove participant renderer
         */
        if (remoteParticipant.getRemoteVideoTracks().size() > 0) {
            RemoteVideoTrackPublication remoteVideoTrackPublication =
                    remoteParticipant.getRemoteVideoTracks().get(0);

            /*
             * Remove video only if subscribed to participant track.
             */
            if (remoteVideoTrackPublication.isTrackSubscribed()) {
                removeParticipantVideo(remoteVideoTrackPublication.getRemoteVideoTrack());
            }
        }
        moveLocalVideoToPrimaryView();
    }

    private void removeParticipantVideo(VideoTrack videoTrack) {
        videoTrack.removeRenderer(primaryVideoView);
    }

    private void moveLocalVideoToPrimaryView() {
        if (thumbnailVideoView.getVisibility() == View.VISIBLE) {
            localVideoTrack.removeRenderer(thumbnailVideoView);
            thumbnailVideoView.setVisibility(View.GONE);
            localVideoTrack.addRenderer(primaryVideoView);
            localVideoView = primaryVideoView;
            primaryVideoView.setMirror(cameraCapturer.getCameraSource() ==
                    CameraCapturer.CameraSource.FRONT_CAMERA);
        }
    }

    /*
     * Set primary view as renderer for participant video track
     */
    private void addRemoteParticipantVideo(VideoTrack videoTrack) {
        moveLocalVideoToThumbnailView();
        primaryVideoView.setMirror(false);
        videoTrack.addRenderer(primaryVideoView);
    }

    private void moveLocalVideoToThumbnailView() {
        if (thumbnailVideoView.getVisibility() == View.GONE) {
            thumbnailVideoView.setVisibility(View.VISIBLE);
            if (localVideoTrack != null) {
                localVideoTrack.removeRenderer(primaryVideoView);
                localVideoTrack.addRenderer(thumbnailVideoView);
            }
            localVideoView = thumbnailVideoView;
            thumbnailVideoView.setMirror(cameraCapturer.getCameraSource() ==
                    CameraCapturer.CameraSource.FRONT_CAMERA);
        }
    }

    /*
     * Called when remote participant joins the room
     */
    @SuppressLint("SetTextI18n")
    private void addRemoteParticipant(RemoteParticipant remoteParticipant) {
        /*
         * This app only displays video for one additional participant per Room
         */
        if (thumbnailVideoView.getVisibility() == View.VISIBLE) {
            Snackbar.make(connectActionFab,
                    "Rendering multiple participants not supported in this app",
                    Snackbar.LENGTH_LONG)
                    .show();
            return;
        }
        remoteParticipantIdentity = remoteParticipant.getIdentity();
        statusTextView.setText("RemoteParticipant " + remoteParticipantIdentity + " joined");

        /*
         * Add remote participant renderer
         */
        if (remoteParticipant.getRemoteVideoTracks().size() > 0) {
            RemoteVideoTrackPublication remoteVideoTrackPublication =
                    remoteParticipant.getRemoteVideoTracks().get(0);

            /*
             * Only render video tracks that are subscribed to
             */
            if (remoteVideoTrackPublication.isTrackSubscribed()) {
                addRemoteParticipantVideo(remoteVideoTrackPublication.getRemoteVideoTrack());
            }
        }

        /*
         * Start listening for participant media events
         */
        remoteParticipant.setListener(mediaListener());
    }

    /*
     * The behavior applied to disconnect
     */
    private void setDisconnectBehavior() {
        connectActionFab.setImageDrawable(ContextCompat.getDrawable(this,
                R.drawable.ic_call_end_white_24dp));
        connectActionFab.show();
        connectActionFab.setOnClickListener(disconnectClickListener());
    }

    void notify(final String roomName) {
        String inviteJsonString;
        Invite invite = new Invite("gowri", roomName);

        /*
         * Use Twilio Notify to let others know you are connecting to a Room
         */
        Notification notification = new Notification("Join " + "identity" + " in room " + roomName,
                "identity" + " has invited you to join video room " + roomName,
                invite.getMap(),
                NOTIFY_TAGS);

//        sendRegistrationSuccess(notification);
//        TwilioSDKStarterAPI.notify(notification).enqueue(new Callback<Void>() {
//            @Override
//            public void onResponse(Call<Void> call, Response<Void> response) {
//                if (!response.isSuccessful()) {
//                    String message = "Sending notification failed: " + response.code() + " " + response.message();
//                    Log.e(TAG, message);
//                    statusTextView.setText(message);
//                }
//            }
//
//            @Override
//            public void onFailure(Call<Void> call, Throwable t) {
//                String message = "Sending notification failed: " + t.getMessage();
//                Log.e(TAG, message);
//                statusTextView.setText(message);
//            }
//        });
    }

    private void connectToRoom(String roomName) {
        enableAudioFocus(true);
        enableVolumeControl(true);

        ConnectOptions.Builder connectOptionsBuilder = new ConnectOptions.Builder(token)
                .roomName(roomName);

        /*
         * Add local audio track to connect options to share with participants.
         */
        if (localAudioTrack != null) {
            connectOptionsBuilder
                    .audioTracks(Collections.singletonList(localAudioTrack));
        }

        /*
         * Add local video track to connect options to share with participants.
         */
        if (localVideoTrack != null) {
            connectOptionsBuilder.videoTracks(Collections.singletonList(localVideoTrack));
        }

        room = Video.connect(this, connectOptionsBuilder.build(), roomListener());
        setDisconnectBehavior();
    }

    @SuppressLint("SetTextI18n")
    @Override
    protected void onResume() {
        super.onResume();
        registerReceiver();
        /*
         * If the local video track was released when the app was put in the background, recreate.
         */
        if (localVideoTrack == null &&
                checkPermissionForCameraAndMicrophone() &&
                cameraCapturer != null) {
            localVideoTrack = LocalVideoTrack.create(this, true, cameraCapturer);
            localVideoTrack.addRenderer(localVideoView);


            /*
             * If connected to a Room then share the local video track.
             */
            if (localParticipant != null) {
                localParticipant.publishTrack(localVideoTrack);
            }
        }

        /*
         * Update reconnecting UI
         */
        if (room != null) {
            reconnectingProgressBar.setVisibility((room.getState() != Room.State.RECONNECTING) ?
                    View.GONE :
                    View.VISIBLE);
            statusTextView.setText("Connected to " + room.getName());
        }
    }

    private void unregisterReceiver() {
        LocalBroadcastManager.getInstance(this).unregisterReceiver(localBroadcastReceiver);
        isReceiverRegistered = false;
    }

    @Override
    protected void onPause() {
        unregisterReceiver();
        /*
         * Release the local video track before going in the background. This ensures that the
         * camera can be used by other applications while this app is in the background.
         *
         * If this local video track is being shared in a Room, participants will be notified
         * that the track has been unpublished.
         */
        if (localVideoTrack != null) {
            /*
             * If this local video track is being shared in a Room, unpublish from room before
             * releasing the video track. Participants will be notified that the track has been
             * removed.
             */
            if (localParticipant != null) {
                localParticipant.unpublishTrack(localVideoTrack);
            }
            localVideoTrack.release();
            localVideoTrack = null;
        }
        super.onPause();
    }

    @Override
    protected void onDestroy() {
        /*
         * Always disconnect from the room before leaving the Activity to
         * ensure any memory allocated to the Room resource is freed.
         */
        if (room != null && room.getState() != Room.State.DISCONNECTED) {
            room.disconnect();
            disconnectedFromOnDestroy = true;
        }

        /*
         * Release the local audio and video tracks ensuring any memory allocated to audio
         * or video is freed.
         */
        if (localAudioTrack != null) {
            localAudioTrack.release();
            localAudioTrack = null;
        }
        if (localVideoTrack != null) {
            localVideoTrack.release();
            localVideoTrack = null;
        }

        super.onDestroy();
    }

    /*
     * Called when a notification is clicked and this activity is in the background or closed
     */
    @Override
    protected void onNewIntent(final Intent intent) {
        super.onNewIntent(intent);
        if (intent.getAction() == ACTION_VIDEO_NOTIFICATION) {
            handleVideoNotificationIntent(intent);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == CAMERA_MIC_PERMISSION_REQUEST_CODE) {
            boolean cameraAndMicPermissionGranted = true;

            for (int grantResult : grantResults) {
                cameraAndMicPermissionGranted &= grantResult == PackageManager.PERMISSION_GRANTED;
            }

            if (cameraAndMicPermissionGranted) {
                retrieveAccessToken();
            } else {
                Toast.makeText(this,
                        "Permission Needed!!",
                        Toast.LENGTH_LONG).show();
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
                Snackbar.make(connectActionFab, message, Snackbar.LENGTH_LONG).show();
            }
        };
    }

}