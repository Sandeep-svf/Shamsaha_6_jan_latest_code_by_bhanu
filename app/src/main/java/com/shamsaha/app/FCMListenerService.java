package com.shamsaha.app;

import android.app.Notification;
import android.app.NotificationManager;
import android.content.Context;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;
import com.shamsaha.app.activity.ChatHelper.Application.TwilioChatApplication;
import com.shamsaha.app.activity.ChatHelper.Message.ChatClientManager;
import com.twilio.chat.ChatClient;
import com.twilio.chat.NotificationPayload;
import com.twilio.chat.StatusListener;

import org.json.JSONObject;

import androidx.annotation.NonNull;
import androidx.core.app.NotificationCompat;


public class FCMListenerService extends FirebaseMessagingService {
//    private static final Logger logger = Logger.getLogger(FCMListenerService.class);


    @Override
    public void onNewToken(@NonNull String s) {
        super.onNewToken(s);
        ChatClientManager chatClientManager = TwilioChatApplication.get().getChatClientManager();
        chatClientManager.getChatClient().registerFCMToken(new ChatClient.FCMToken(s), new StatusListener() {
            @Override
            public void onSuccess() {
                Log.d("FCMListener", "onSuccess: Token has been registered again");
            }
        });
    }

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        // If the application is in the foreground handle both data and notification messages here.
        // Also if you intend on generating your own notifications as a result of a received FCM
        // message, here is where that should be initiated. See sendNotification method below.
        ChatClientManager chatClientManager = TwilioChatApplication.get().getChatClientManager();

        Log.d("FCMListener","onMessageReceived for FCM");

        Log.d("FCMListener","From: " + remoteMessage.getFrom());


        // Check if message contains a data payload.
        if (remoteMessage.getData().size() > 0) {
            Log.d("FCMListener","Data Message Body: " + remoteMessage.getData());
            JSONObject obj = new JSONObject(remoteMessage.getData());
            Bundle data = new Bundle();
            data.putString("channel_id", obj.optString("channel_id"));
            data.putString("message_id", obj.optString("message_id"));
            data.putString("author", obj.optString("author"));
            data.putString("message_sid", obj.optString("message_sid"));
            data.putString("twi_sound", obj.optString("twi_sound"));
            data.putString("twi_message_type", obj.optString("twi_message_type"));
            data.putString("channel_sid", obj.optString("channel_sid"));
            data.putString("twi_message_id", obj.optString("twi_message_id"));
            data.putString("twi_body", obj.optString("twi_body"));
            data.putString("channel_title", obj.optString("channel_title"));

            NotificationPayload payload = new NotificationPayload(data);

            ChatClient client = chatClientManager.getChatClient() ;

            if (client != null) {
                Log.d("FCMListener","onMessageReceived for FCM" +remoteMessage.toString());
                client.handleNotification(payload);
            }

            NotificationPayload.Type type = payload.getType();

            if (type == NotificationPayload.Type.UNKNOWN) return; // Ignore everything we don't support

            String title = "Twilio Notification";

            if (type == NotificationPayload.Type.NEW_MESSAGE)
                title = "Twilio: New Message";
            if (type == NotificationPayload.Type.ADDED_TO_CHANNEL)
                title = "Twilio: Added to Channel";
            if (type == NotificationPayload.Type.INVITED_TO_CHANNEL)
                title = "Twilio: Invited to Channel";
            if (type == NotificationPayload.Type.REMOVED_FROM_CHANNEL)
                title = "Twilio: Removed from Channel";

            // Set up action Intent
//            Intent intent = new Intent(this, MessageActivity.class);
//            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

            String cSid = payload.getChannelSid();
            if (!"".contentEquals(cSid)) {
//                intent.putExtra("Channel SID", cSid);
            }

//            PendingIntent pendingIntent =
//                    PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_ONE_SHOT);

            NotificationCompat.Builder notification =
                    new NotificationCompat.Builder(this,"Shamsaha")
                            .setSmallIcon(R.drawable.ic_logo_no_back)
                            .setContentTitle(title)
                            .setContentText(payload.getBody())
                            .setAutoCancel(true)
//                            .setContentIntent(pendingIntent)
                            .setColor(Color.rgb(214, 10, 37));

            String soundFileName = payload.getSound();
            Notification notification1 = notification.build();
            if (getResources().getIdentifier(soundFileName, "raw", getPackageName()) != 0) {
                Uri sound = Uri.parse("android.resource://" + getPackageName() + "/raw/" + soundFileName);

                notification1.defaults &= ~Notification.DEFAULT_SOUND;
                notification1.sound = sound;
                Log.d("FCMListener","Playing specified sound "+soundFileName);
//                logger.d("Playing specified sound "+soundFileName);
            } else {
                notification1.defaults |= Notification.DEFAULT_SOUND;
                Log.d("FCMListener","Playing default sound");
            }

            NotificationManager notificationManager =
                    (NotificationManager)getSystemService(Context.NOTIFICATION_SERVICE);

            notificationManager.notify(0, notification1);
        }

        // Check if message contains a notification payload.
        if (remoteMessage.getNotification() != null) {
            Log.d("FCMListener","Notification Message Body: " + remoteMessage.getNotification().getBody());
            Log.d("FCMListener","We do not parse notification body - leave it to system");

        }
    }
}
