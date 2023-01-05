package com.shamsaha.app.activity.volunteer.onduty;

import android.Manifest;
import android.app.AlertDialog;
import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.content.res.ColorStateList;
import android.media.AudioManager;
import android.os.Build;
import android.os.Bundle;
import android.os.SystemClock;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Chronometer;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.installations.FirebaseInstallations;
import com.google.firebase.messaging.FirebaseMessaging;
import com.shamsaha.app.R;
import com.shamsaha.app.activity.CallHelper.Constants;
import com.shamsaha.app.activity.CallHelper.IncomingCallNotificationService;
import com.shamsaha.app.activity.CallHelper.SoundPoolManager;
import com.shamsaha.app.activity.volunteer.WebViewActivity;
import com.shamsaha.app.retrofitAdaptor.AccessTokenResponse;
import com.shamsaha.app.retrofitAdaptor.RetrofitInstance;
import com.shamsaha.app.retrofitAdaptor.ShamsahaService;
import com.shamsaha.app.utils.ConstantsURL.baseURL;
import com.twilio.audioswitch.AudioDevice;
import com.twilio.audioswitch.AudioSwitch;
import com.twilio.voice.Call;
import com.twilio.voice.CallException;
import com.twilio.voice.CallInvite;
import com.twilio.voice.ConnectOptions;
import com.twilio.voice.RegistrationException;
import com.twilio.voice.RegistrationListener;
import com.twilio.voice.Voice;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Set;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.lifecycle.Lifecycle;
import androidx.lifecycle.ProcessLifecycleOwner;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import kotlin.Unit;
import kotlin.jvm.functions.Function2;
import retrofit2.Callback;
import retrofit2.Response;

public class DutyShiftActivity extends AppCompatActivity {

    private static final String TAG = "DutyShiftActivity";
    //    private static final String identity = "Deva";
    private String identity = "Deva";
    private String callTo = "";
    private CallInvite callInvite;

    private static final String TWILIO_ACCESS_TOKEN_SERVER_URL = baseURL.BaseURLTwilioVoice;
    private static final int MIC_PERMISSION_REQUEST_CODE = 1;
    private String accessToken;
    private AudioSwitch audioSwitch;
    private int savedVolumeControlStream;
    private MenuItem audioDeviceMenuItem;

    private ImageView iv_audio_mode;

    private boolean isReceiverRegistered = false;
    private DutyShiftActivity.VoiceBroadcastReceiver voiceBroadcastReceiver;

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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_duty_shift);
        Intent intent = getIntent();
        callInvite = getIntent().getParcelableExtra(Constants.INCOMING_CALL_INVITE);
        //identity = intent.getStringExtra("volID");
        identity = callInvite.getTo();
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
        iv_audio_mode = findViewById(R.id.iv_audio_mode);

        callActionFab.setOnClickListener(callActionFabClickListener());
        hangupActionFab.setOnClickListener(hangupActionFabClickListener());
        holdActionFab.setOnClickListener(holdActionFabClickListener());
        muteActionFab.setOnClickListener(muteActionFabClickListener());
        iv_audio_mode.setOnClickListener(audioSelectListener());
        notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        voiceBroadcastReceiver = new DutyShiftActivity.VoiceBroadcastReceiver();
        registerReceiver();
        audioSwitch = new AudioSwitch(getApplicationContext());
        savedVolumeControlStream = getVolumeControlStream();
        setVolumeControlStream(AudioManager.STREAM_VOICE_CALL);

        resetUI();

        handleIncomingCallIntent(getIntent());

        if (!checkPermissionForMicrophone()) {
            requestPermissionForMicrophone();
        } else {
            retrieveAccessToken();
        }

    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        handleIncomingCallIntent(intent);
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
                    SoundPoolManager.getInstance(DutyShiftActivity.this).playRinging();
                }
            }

            @Override
            public void onConnectFailure(@NonNull Call call, @NonNull CallException error) {
                audioSwitch.deactivate();
                if (Constants.playCustomRingback) {
                    SoundPoolManager.getInstance(DutyShiftActivity.this).stopRinging();
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
                    SoundPoolManager.getInstance(DutyShiftActivity.this).stopRinging();
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
                    SoundPoolManager.getInstance(DutyShiftActivity.this).stopRinging();
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
                //home(baseURL.BaseURL + "crreport.php?volunteer=" + identity + "case_id=");
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

    private void home(String url) {

        Intent i = new Intent(getApplicationContext(), WebViewActivity.class);
        i.putExtra("type", "volunteer");
        i.putExtra("url", url);
        startActivity(i);
        finish();

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
//        callActionFab.show();
        callActionFab.hide();
        muteActionFab.setImageDrawable(ContextCompat.getDrawable(DutyShiftActivity.this, R.drawable.ic_mic_white_24dp));
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
    }

    @Override
    protected void onPause() {
        super.onPause();
        unregisterReceiver();
    }

    @Override
    public void onDestroy() {
        /*
         * Tear down audio device management and restore previous volume stream
         */
        audioSwitch.stop();
        setVolumeControlStream(savedVolumeControlStream);
        SoundPoolManager.getInstance(this).release();
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
                Intent acceptIntent = new Intent(DutyShiftActivity.this.getApplicationContext(), IncomingCallNotificationService.class);
                acceptIntent.setAction(Constants.ACTION_ACCEPT);
                acceptIntent.putExtra(Constants.INCOMING_CALL_INVITE, activeCallInvite);
                acceptIntent.putExtra(Constants.INCOMING_CALL_NOTIFICATION_ID, activeCallNotificationId);
                Log.d(TAG, "Clicked accept startService");
                DutyShiftActivity.this.startService(acceptIntent);
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
                activeCall = Voice.connect(DutyShiftActivity.this, connectOptions, callListener);
                DutyShiftActivity.this.setCallUI();
                alertDialog.dismiss();
            }
        };
    }

    private DialogInterface.OnClickListener cancelCallClickListener() {
        return new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                SoundPoolManager.getInstance(DutyShiftActivity.this).stopRinging();
                if (activeCallInvite != null) {
                    Intent intent = new Intent(DutyShiftActivity.this, IncomingCallNotificationService.class);
                    intent.setAction(Constants.ACTION_REJECT);
                    intent.putExtra(Constants.INCOMING_CALL_INVITE, activeCallInvite);
                    DutyShiftActivity.this.startService(intent);
                }
                if (alertDialog != null && alertDialog.isShowing()) {
                    alertDialog.dismiss();
                    onBackPressed();
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
        FirebaseInstallations.getInstance().getId().addOnSuccessListener(this, new OnSuccessListener<String>() {
            @Override
            public void onSuccess(@NonNull String s) {
                String fcmToken = FirebaseMessaging.getInstance().getToken().toString();
                Log.i(TAG, "Registering with FCM");
                Voice.register(accessToken, Voice.RegistrationChannel.FCM, fcmToken, registrationListener);
            }
        }); /*{
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
        return new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showAudioDevices();
            }
        };
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
                activeCall = Voice.connect(DutyShiftActivity.this, connectOptions, callListener);
                DutyShiftActivity.this.setCallUI();

            }
        };
    }

    private View.OnClickListener hangupActionFabClickListener() {
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SoundPoolManager.getInstance(DutyShiftActivity.this).playDisconnect();
                DutyShiftActivity.this.resetUI();
                DutyShiftActivity.this.disconnect();
                onBackPressed();
            }
        };
    }

    private View.OnClickListener holdActionFabClickListener() {
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DutyShiftActivity.this.hold();
            }
        };
    }

    private View.OnClickListener muteActionFabClickListener() {
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DutyShiftActivity.this.mute();
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
            onBackPressed();
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
                muteActionFab.setImageDrawable(ContextCompat.getDrawable(DutyShiftActivity.this, R.drawable.ic_mic_off_black_24dp));
            } else {
                muteActionFab.setImageDrawable(ContextCompat.getDrawable(DutyShiftActivity.this, R.drawable.ic_mic_white_24dp));
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
                DutyShiftActivity.this.updateAudioDeviceIcon(audioDevice);
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
                                    DutyShiftActivity.this.updateAudioDeviceIcon(selectedAudioDevice);
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
        Glide.with(DutyShiftActivity.this).load(audioDeviceMenuIcon).into(iv_audio_mode);
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
            alertDialog = createIncomingCallDialog(DutyShiftActivity.this,
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
        Toast.makeText(this, "   " + identity, Toast.LENGTH_SHORT).show();
        /*StringTokenizer tokenizer = new StringTokenizer(identity,":");
        List<String> tokens = new ArrayList<>();
        while (tokenizer.hasMoreTokens()){
            tokens.add(tokenizer.nextToken());
        }
        identity = tokens.get(1);*/
        /*Ion.with(this).load(TWILIO_ACCESS_TOKEN_SERVER_URL + "?identity=" + identity)
                .asString()
                .setCallback(new FutureCallback<String>() {
                    @Override
                    public void onCompleted(Exception e, String accessToken) {
                        if (e == null) {
                            Log.d(TAG, "Access token: " + accessToken);
                            DutyShiftActivity.this.accessToken = accessToken;
//                            DutyShiftActivity.this.registerForCallInvites();
                            audioSwitch.start(new Function2<List<? extends AudioDevice>, AudioDevice, Unit>() {
                                @Override
                                public Unit invoke(List<? extends AudioDevice> audioDevices, AudioDevice audioDevice) {
                                    DutyShiftActivity.this.updateAudioDeviceIcon(audioDevice);
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

        ShamsahaService digitalService= RetrofitInstance.getRetrofitInstance().getDigitalService();
        retrofit2.Call<AccessTokenResponse> accessTokenCall = digitalService.getAccessToken(identity);
        accessTokenCall.enqueue(new Callback<AccessTokenResponse>() {
            @Override
            public void onResponse(@NotNull retrofit2.Call<AccessTokenResponse> call, @NotNull Response<AccessTokenResponse> response) {
                if (response.isSuccessful() && response.body()!=null){
                    accessToken = response.body().getToken();
                    registerForCallInvites();
                    audioSwitch.start((audioDevices, audioDevice) -> {
                        updateAudioDeviceIcon(audioDevice);
                        return Unit.INSTANCE;
                    });
                }else {
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
        activeCall = Voice.connect(DutyShiftActivity.this, connectOptions, callListener);
        DutyShiftActivity.this.setCallUI();
    }
}

