package com.shamsaha.app.activity.CallHelper.videocall;

import android.content.Intent;
import android.util.Log;

import com.google.firebase.messaging.FirebaseMessagingService;

import androidx.annotation.NonNull;

public class NotifyFirebaseInstanceIDService extends FirebaseMessagingService {

    private static final String TAG = "NotifyFbIIDService";
/*

    *
     * Called if InstanceID token is updated. This may occur if the security of
     * the previous token had been compromised. Note that this is called when the InstanceID token
     * is initially generated so this is where you would retrieve the token.
*/
    // [START refresh_token]
   /* @Override
    public void onTokenRefresh() {
        // Get updated InstanceID token.
        String refreshedToken = FirebaseInstanceId.getInstance().getToken();
        Log.d(TAG, "Refreshed token: " + refreshedToken);

        // If you want to send messages to this application instance or
        // manage this apps subscriptions on the server side, send the
        // Instance ID token to your app server.
        sendRegistrationToServer(refreshedToken);
    }*/
    // [END refresh_token]


    /**
     * Refactored by Thanasis Fotiadis on 19/7/2021
     *
     * @param s The new Token used for sending messages to this application instance.
     *          This Token is the same token retrieved by FirebaseMessaging.getToken().
     */
    @Override
    public void onNewToken(@NonNull String s) {
        super.onNewToken(s);
        Log.d(TAG, "onNewToken: " + s);

        sendRegistrationToServer(s);
    }

    /**
     * Persist token to third-party servers.
     * <p>
     * Modify this method to associate the user's FCM InstanceID token with any server-side account
     * maintained by your application.
     *
     * @param token The new token.
     */
    private void sendRegistrationToServer(String token) {
        /*
         * Use the service to refresh the registration binding. The token is not passed
         * because the token is requested by the Service.
         */

        //Before refactor this token is not passed i don't know why
        Intent intent = new Intent(this, RegistrationIntentService.class);
        startService(intent);
    }

}
