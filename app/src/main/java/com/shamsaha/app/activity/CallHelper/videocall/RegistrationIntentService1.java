package com.shamsaha.app.activity.CallHelper.videocall;

import android.app.IntentService;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import com.shamsaha.app.activity.volunteer.onduty.DutyShiftActivity1;
import com.twilio.chat.Channel;

import androidx.annotation.Nullable;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

public class RegistrationIntentService1 extends IntentService {

    private static final String TAG = "RegIntentService";

    /*
     * The notify binding type to use. Use FCM since GCM has been deprecated by Google
     */
    private static final String BINDING_TYPE = "fcm";

    private SharedPreferences sharedPreferences;

    public RegistrationIntentService1() {
        super(TAG);
    }

    @Override
    public void onCreate() {
        super.onCreate();
    }

    @Override
    protected void onHandleIntent(@Nullable Intent intent) {
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        if (intent != null && intent.getExtras() != null) {
            Channel channel = intent.getExtras().getParcelable("CHANNEL");
            sendRegistrationSuccess(channel);
        }
    }

    public void sendRegistrationSuccess(Channel channel) {
        Intent intent = new Intent(this, DutyShiftActivity1.class);
        intent.setAction(DutyShiftActivity1.ACTION_CHANNEL_CREATED);
        intent.putExtra(DutyShiftActivity1.ACTION_CHANNEL, channel);
        LocalBroadcastManager.getInstance(this).sendBroadcast(intent);
    }


}
