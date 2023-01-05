package com.shamsaha.app.activity.CallHelper.fcm;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.TaskStackBuilder;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.util.Log;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;
import com.shamsaha.app.R;
import com.shamsaha.app.activity.CallHelper.Constants;
import com.shamsaha.app.activity.CallHelper.IncomingCallNotificationService;
import com.shamsaha.app.activity.CallHelper.videocall.VideoInviteActivity;
import com.shamsaha.app.activity.ChatHelper.Application.TwilioChatApplication;
import com.shamsaha.app.activity.PublicPart.homeScreenActivity;
import com.shamsaha.app.activity.Victem.ChatActivity;
import com.shamsaha.app.activity.volunteer.onduty.DutyShiftActivity1;
import com.shamsaha.app.utils.SharedPreferencesUtils;
import com.twilio.voice.CallException;
import com.twilio.voice.CallInvite;
import com.twilio.voice.CancelledCallInvite;
import com.twilio.voice.MessageListener;
import com.twilio.voice.Voice;

import org.jetbrains.annotations.NotNull;

import java.util.Map;
import java.util.Objects;
import java.util.Random;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

public class VoiceFirebaseMessagingService extends FirebaseMessagingService {

    private static final String TAG = "VoiceFCMService";
    private static final String NOTIFY_TITLE_DATA_KEY = "twi_title";
    private static final String NOTIFY_BODY_DATA_KEY = "twi_body";

    @Override
    public void onCreate() {
        super.onCreate();
        Log.d(TAG, "onCreate: ");
    }

    /**
     * Called when message is received.
     *
     * @param remoteMessage Object representing the message received from Firebase Cloud Messaging.
     */
    @Override
    public void onMessageReceived(@NotNull RemoteMessage remoteMessage) {
        Log.d(TAG, "Received onMessageReceived()");
        Log.d(TAG, "Data Type: " + remoteMessage.getMessageType());
        Log.d(TAG, "Bundle data: " + remoteMessage.getData());
        Log.d(TAG, "From: " + remoteMessage.getFrom());
        Log.d(TAG, "Payload type: " + remoteMessage.getData().get("twi_message_type"));

        /*ChatClientManager chatClientManager = TwilioChatApplication.get().getChatClientManager();
        NotificationPayload payload = new NotificationPayload(remoteMessage.getData());
        Log.d(TAG, "onMessageReceived: Handling the Notification");
        if (chatClientManager.getChatClient()!=null)
        chatClientManager.getChatClient().handleNotification(payload);

        Log.d(TAG, "onMessageReceived: "+remoteMessage.toString());*/

       /* if (!remoteMessage.getData().isEmpty() && remoteMessage.getNotification() != null) {
            showNotification(remoteMessage.getNotification().getTitle(), remoteMessage.getNotification().getBody());
        } else {
            showNotification(remoteMessage.getData());
        }*/
        if (remoteMessage.getData().get("type") != null) {
            if (Objects.requireNonNull(remoteMessage.getData().get("type")).equals("video")) {
                Log.i(TAG, "onMessageReceived: It's a video notification");
                Log.i(TAG, "onMessageReceived: "+remoteMessage.getData());
                //showNotification(remoteMessage.getNotification().getTitle(), remoteMessage.getNotification().getBody());
                showVideoNotification(remoteMessage.getNotification().getTitle(),remoteMessage.getNotification().getBody(),remoteMessage.getData());
            }
        } else {
            // Check if message contains a data payload.
            if (remoteMessage.getData().size() > 0 && remoteMessage.getData().get("twi_message_type").equals("twilio.voice.call")) {
                boolean valid = Voice.handleMessage(this, remoteMessage.getData(), new MessageListener() {
                    @Override
                    public void onCallInvite(@NonNull CallInvite callInvite) {
                        final int notificationId = (int) System.currentTimeMillis();
                        handleInvite(callInvite, notificationId);
                    }

                    @Override
                    public void onCancelledCallInvite(@NonNull CancelledCallInvite cancelledCallInvite, @Nullable CallException callException) {
                        handleCanceledCallInvite(cancelledCallInvite);
                    }
                });

                if (!valid) {
                    Log.e(TAG, "The message was not a valid Twilio Voice SDK payload: " +
                            remoteMessage.getData());
                }


            }else{
                //Show chat notification.
                //showNotification(remoteMessage.getData());
                showChatNotification(remoteMessage.getData());
            }
        }

    }

    private void showVideoNotification(String title, String body, Map<String, String> data) {
        NotificationManagerCompat notificationManagerCompat = NotificationManagerCompat.from(this);
        Intent resultIntent = new Intent(this, VideoInviteActivity.class);
        resultIntent.putExtra("volID",data.get("invite_id"));
        resultIntent.putExtra("caseID",data.get("room_id"));
        TaskStackBuilder stackBuilder = TaskStackBuilder.create(this);
        stackBuilder.addParentStack(DutyShiftActivity1.class);
        stackBuilder.addParentStack(VideoInviteActivity.class);
        stackBuilder.addNextIntent(resultIntent);
        PendingIntent resultPendingIntent;
        NotificationCompat.Builder notificationBuilder;

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {

            notificationBuilder = new NotificationCompat.Builder(this, TwilioChatApplication.NOTIFICATION_CHANNEL)
                    .setAutoCancel(true)
                    .setDefaults(Notification.DEFAULT_ALL)
                    .setWhen(System.currentTimeMillis())
                    .setSmallIcon(R.drawable.ic_logo_no_back)
                    .setContentTitle(title)
                    .setContentText(body)
                    .setContentInfo("INFO");
            resultPendingIntent = stackBuilder.getPendingIntent(0, PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_IMMUTABLE);
            notificationBuilder.setContentIntent(resultPendingIntent);
        }else {
            notificationBuilder = new NotificationCompat.Builder(this, TwilioChatApplication.NOTIFICATION_CHANNEL)
                    .setAutoCancel(true)
                    .setDefaults(Notification.DEFAULT_ALL)
                    .setWhen(System.currentTimeMillis())
                    .setSmallIcon(R.drawable.ic_logo_no_back)
                    .setContentTitle(title)
                    .setContentText(body)
                    .setContentInfo("INFO");
            resultPendingIntent = stackBuilder.getPendingIntent(0, PendingIntent.FLAG_UPDATE_CURRENT);
            notificationBuilder.setContentIntent(resultPendingIntent);
        }
        notificationManagerCompat.notify(new Random().nextInt(), notificationBuilder.build());
    }

    private void showChatNotification(Map<String,String> data){
        NotificationManagerCompat notificationManagerCompat = NotificationManagerCompat.from(this);
        String title = data.get(NOTIFY_TITLE_DATA_KEY);
        String body = data.get(NOTIFY_BODY_DATA_KEY);
        Intent resultIntent = new Intent(this, ChatActivity.class);
        TaskStackBuilder stackBuilder = TaskStackBuilder.create(this);
        if (SharedPreferencesUtils.getIsLoggedIn()){
            resultIntent.putExtra("volID",SharedPreferencesUtils.getVolunteerID());
            stackBuilder.addParentStack(DutyShiftActivity1.class);
        }else{
            resultIntent.putExtra("volID","");
            stackBuilder.addParentStack(homeScreenActivity.class);
        }
        resultIntent.putExtra("caseID",data.get("channel_title"));

        stackBuilder.addNextIntent(resultIntent);

        PendingIntent resultPendingIntent;
        NotificationCompat.Builder notificationBuilder;

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {

            notificationBuilder = new NotificationCompat.Builder(this, TwilioChatApplication.NOTIFICATION_CHANNEL)
                    .setAutoCancel(true)
                    .setDefaults(Notification.DEFAULT_ALL)
                    .setWhen(System.currentTimeMillis())
                    .setSmallIcon(R.drawable.ic_logo_no_back)
                    .setContentTitle(title)
                    .setContentText(body)
                    .setContentInfo("INFO");


            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                resultPendingIntent = stackBuilder.getPendingIntent(0, PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_IMMUTABLE);
            } else {
                resultPendingIntent = stackBuilder.getPendingIntent(0, PendingIntent.FLAG_UPDATE_CURRENT);
            }
            notificationBuilder.setContentIntent(resultPendingIntent);
        } else {

            notificationBuilder = new NotificationCompat.Builder(this, TwilioChatApplication.NOTIFICATION_CHANNEL)
                    .setAutoCancel(true)
                    .setDefaults(Notification.DEFAULT_ALL)
                    .setWhen(System.currentTimeMillis())
                    .setSmallIcon(R.drawable.ic_logo_no_back)
                    .setContentTitle(title)
                    .setContentText(body)
                    .setContentInfo("INFO");

        }

       // NotificationManagerCompat notificationManagerCompat = NotificationManagerCompat.from(this);


        notificationManagerCompat.notify(new Random().nextInt(), notificationBuilder.build());

    }




    private void showNotification(Map<String, String> data) {
        String title = data.get(NOTIFY_TITLE_DATA_KEY);
        String body = data.get(NOTIFY_BODY_DATA_KEY);

        NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        //String NOTIFICATION_CHANNEL_ID = "org.shamsaha.app.Services";

        /*if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel notificationChannel = new NotificationChannel(NOTIFICATION_CHANNEL_ID, "Notification", NotificationManager.IMPORTANCE_DEFAULT);
            notificationChannel.setDescription("TEST");
            notificationChannel.enableLights(true);
            notificationChannel.setLightColor(Color.BLUE);
            notificationChannel.enableLights(true);
            notificationChannel.enableVibration(true);
            notificationManager.createNotificationChannel(notificationChannel);
        }*/
        Intent resultIntent = new Intent(this, homeScreenActivity.class);
        TaskStackBuilder stackBuilder = TaskStackBuilder.create(this);
        stackBuilder.addParentStack(homeScreenActivity.class);

// Adds the Intent that starts the Activity to the top of the stack
        stackBuilder.addNextIntent(resultIntent);
        PendingIntent resultPendingIntent;
        NotificationCompat.Builder notificationBuilder;

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {

            notificationBuilder = new NotificationCompat.Builder(this, TwilioChatApplication.NOTIFICATION_CHANNEL)
                    .setAutoCancel(true)
                    .setDefaults(Notification.DEFAULT_ALL)
                    .setWhen(System.currentTimeMillis())
                    .setSmallIcon(R.drawable.ic_logo_no_back)
                    .setContentTitle(title)
                    .setContentText(body)
                    .setContentInfo("INFO");


            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                resultPendingIntent = stackBuilder.getPendingIntent(0, PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_IMMUTABLE);
            } else {
                resultPendingIntent = stackBuilder.getPendingIntent(0, PendingIntent.FLAG_UPDATE_CURRENT);
            }
            notificationBuilder.setContentIntent(resultPendingIntent);
        } else {

            notificationBuilder = new NotificationCompat.Builder(this, TwilioChatApplication.NOTIFICATION_CHANNEL)
                    .setAutoCancel(true)
                    .setDefaults(Notification.DEFAULT_ALL)
                    .setWhen(System.currentTimeMillis())
                    .setSmallIcon(R.drawable.ic_logo_no_back)
                    .setContentTitle(title)
                    .setContentText(body)
                    .setContentInfo("INFO");

        }

        NotificationManagerCompat notificationManagerCompat = NotificationManagerCompat.from(this);


        notificationManagerCompat.notify(new Random().nextInt(), notificationBuilder.build());

    }

    private void showNotification(String title, String body) {

        NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        String NOTIFICATION_CHANNEL_ID = "org.shamsaha.app.Services";

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel notificationChannel = new NotificationChannel(NOTIFICATION_CHANNEL_ID, "Notification", NotificationManager.IMPORTANCE_DEFAULT);
            notificationChannel.setDescription("TEST");
            notificationChannel.enableLights(true);
            notificationChannel.setLightColor(Color.BLUE);
            notificationChannel.enableLights(true);
            notificationChannel.enableVibration(true);
            notificationManager.createNotificationChannel(notificationChannel);
        }
        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this, NOTIFICATION_CHANNEL_ID);
        notificationBuilder.setAutoCancel(true)
                .setDefaults(Notification.DEFAULT_ALL)
                .setWhen(System.currentTimeMillis())
                .setSmallIcon(R.drawable.ic_logo_no_back)
                .setContentTitle(title)
                .setContentText(body)
                .setContentInfo("INFO");

        Intent resultIntent = new Intent(this, homeScreenActivity.class);
        TaskStackBuilder stackBuilder = TaskStackBuilder.create(this);
        stackBuilder.addParentStack(homeScreenActivity.class);

// Adds the Intent that starts the Activity to the top of the stack
        stackBuilder.addNextIntent(resultIntent);
        PendingIntent resultPendingIntent;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            resultPendingIntent = stackBuilder.getPendingIntent(0, PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_IMMUTABLE);
        } else {
            resultPendingIntent = stackBuilder.getPendingIntent(0, PendingIntent.FLAG_UPDATE_CURRENT);
        }
        notificationBuilder.setContentIntent(resultPendingIntent);

        notificationManager.notify(new Random().nextInt(), notificationBuilder.build());
    }

    @Override
    public void onNewToken(@NotNull String token) {
        super.onNewToken(token);
        Intent intent = new Intent(Constants.ACTION_FCM_TOKEN);
        LocalBroadcastManager.getInstance(this).sendBroadcast(intent);
        /*ChatClientManager chatClientManager = TwilioChatApplication.get().getChatClientManager();
        chatClientManager.getChatClient().registerFCMToken(new ChatClient.FCMToken(token), new StatusListener() {
            @Override
            public void onSuccess() {
                Log.d("FCMListener", "onSuccess: Token has been registered again");
            }
        })*/

    }

    private void handleInvite(CallInvite callInvite, int notificationId) {
        Intent intent = new Intent(this, IncomingCallNotificationService.class);
        intent.setAction(Constants.ACTION_INCOMING_CALL);
        intent.putExtra(Constants.INCOMING_CALL_NOTIFICATION_ID, notificationId);
        intent.putExtra(Constants.INCOMING_CALL_INVITE, callInvite);

        startService(intent);
    }

    private void handleCanceledCallInvite(CancelledCallInvite cancelledCallInvite) {
        Intent intent = new Intent(this, IncomingCallNotificationService.class);
        intent.setAction(Constants.ACTION_CANCEL_CALL);
        intent.putExtra(Constants.CANCELLED_CALL_INVITE, cancelledCallInvite);

        startService(intent);
    }
}
