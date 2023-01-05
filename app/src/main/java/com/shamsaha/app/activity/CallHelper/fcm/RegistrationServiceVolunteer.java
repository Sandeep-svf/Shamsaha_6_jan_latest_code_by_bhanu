package com.shamsaha.app.activity.CallHelper.fcm;

import android.app.IntentService;
import android.content.Intent;
import android.util.Log;

import com.google.firebase.installations.FirebaseInstallations;
import com.google.firebase.messaging.FirebaseMessaging;
import com.shamsaha.app.activity.CallHelper.videocall.Binding;
import com.shamsaha.app.activity.CallHelper.videocall.TwilioSDKStarterAPI;
import com.shamsaha.app.activity.ChatHelper.Application.TwilioChatApplication;
import com.shamsaha.app.activity.ChatHelper.Channel.ChannelManager;
import com.shamsaha.app.activity.ChatHelper.Message.ChatClientManager;
import com.shamsaha.app.utils.SharedPreferencesUtils;
import com.twilio.chat.ChatClient;
import com.twilio.chat.StatusListener;

import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.util.Collections;

import androidx.annotation.NonNull;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RegistrationServiceVolunteer extends IntentService {
    private static final String TAG = "RegistrationServiceVolu";
    private  ChatClientManager chatClientManager;

    public RegistrationServiceVolunteer() {
        super(TAG);
        chatClientManager = TwilioChatApplication.get().getChatClientManager();
    }



    @Override
    protected void onHandleIntent(@NonNull Intent intent) {
        FirebaseMessaging.getInstance().getToken().addOnSuccessListener(token -> {
            Log.i(TAG, "onHandleIntent: FCM: "+token);
            String identity = intent.getStringExtra("volID");
            Log.i(TAG, "onHandleIntent: identity"+identity);
            String caseId = intent.getStringExtra("caseID");
            String author = "";
            if (SharedPreferencesUtils.getIsLoggedIn()){
                author = identity;
            }else {
                author = caseId;
            }
            /*if (identity != null && identity.isEmpty() && caseId != null && !caseId.isEmpty()) {
                author = caseId;
            } else if (identity != null && !identity.isEmpty() && caseId != null && caseId.isEmpty()) {
                author = identity;
            }*/
            sendRegistrationToServer(author,token);
            registerFCM(token);
        });



    }

    private void sendRegistrationToServer(String author,String token) {
        final String newEndpoint = author + "@" + FirebaseInstallations.getInstance().getId();
        final Binding binding = new Binding(author,
                newEndpoint,
                token,
                "fcm",
                Collections.singletonList("all"));


        TwilioSDKStarterAPI.registerBinding(binding).enqueue(new Callback<Void>() {
            @Override
            public void onResponse(@NotNull Call<Void> call, @NotNull Response<Void> response) {
                if (response.isSuccessful()){
                    Log.i(TAG, "onResponse: RegisterBinding Success");
                }else {
                    try {
                        Log.i(TAG, "onResponse: RegisterBinding unsuccessful"+response.errorBody().string());
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onFailure(@NotNull Call<Void> call, @NotNull Throwable t) {
                Log.i(TAG, "onFailure: Call Failed"+t.getMessage());
            }
        });
    }

    private void registerFCM(String FCMToken) {
        if (chatClientManager.getChatClient() != null) {

            chatClientManager.getChatClient().registerFCMToken(new ChatClient.FCMToken(FCMToken),
                    new StatusListener() {
                        @Override
                        public void onSuccess() {
                            Log.d(TAG, "onSuccess: FCMToken registered for chat client.");
                            ChannelManager.getInstance().onNotificationSubscribed();
                        }
                    });

        }
    }


}
