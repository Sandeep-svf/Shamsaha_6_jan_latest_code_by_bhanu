package com.shamsaha.app.activity.CallHelper.videocall;

import android.media.session.MediaSession;
import android.provider.ContactsContract;

import com.shamsaha.app.ApiModel.volunter.MessageModel;
import com.shamsaha.app.utils.SharedPreferencesUtils;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.RequestBody;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.jackson.JacksonConverterFactory;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;

public class TwilioSDKStarterAPI {
    /**
     * Resources defined in the sdk-starter projects available in C#, Java, Node, PHP, Python, or Ruby.
     *
     * https://github.com/TwilioDevEd?q=sdk-starter
     */
    interface SDKStarterService {
        // Fetch an access token
        @GET("/token")
        Call<MediaSession.Token> fetchToken();
        // Fetch an access token with a specific identity
        @Headers("Content-Type: application/json")
        @POST("/token")
        Call<MediaSession.Token> fetchToken(@Body ContactsContract.CommonDataKinds.Identity identity);
        // Register this binding with Twilio Notify
        @Headers("Content-Type: application/json")
        @POST("register.php")
        Call<Void> register(@Body Binding binding);
        // Send notifications to Twilio Notify registrants
        @Headers("Content-Type: application/json")
        @POST("send-notification.php")
        Call<Void> sendNotification(@Body Notification notification);

        @Multipart
        @POST("twilioNotify.php")
        Call<Void> sendVideoNotification(@Part("invite_id") RequestBody volId, @Part("message") RequestBody message, @Part("room_id") RequestBody caseID, @Part ("type") RequestBody type);
        //Call<Void> sendVideoNotification(@Body RequestBody body);
/*Call<Void> sendVideoNotification(@Field("invite_id") String volId, @Field("message") String message,
                                         @Field("room_id") String caseID, @Field("type") String type);*/

        @FormUrlEncoded
        @POST("defaultchat.php")
        Call<MessageModel> defaultMessages(@Field("CHANNEL") String channel,
                                           @Field("USER") String sender,
                                           @Field("MESSAGE") String body);
    }

    private static HttpLoggingInterceptor logging = new HttpLoggingInterceptor();

    private static OkHttpClient.Builder httpClient = new OkHttpClient.Builder().addInterceptor(logging.setLevel(HttpLoggingInterceptor.Level.BODY));

    private static SDKStarterService sdkStarterService = new Retrofit.Builder()
            .baseUrl(VideoInviteActivity.TWILIO_SDK_STARTER_SERVER_URL)
            .addConverterFactory(JacksonConverterFactory.create())
            .client(httpClient.build())
            .build()
            .create(SDKStarterService.class);

    public static Call<Void> registerBinding(final Binding binding) {
        return sdkStarterService.register(binding);
    }

    public static Call<Void> notify(Notification notification) {
        return sdkStarterService.sendNotification(notification);
    }

    public static Call<Void> videoNotify(String caseId,String identity) {
        String message;
        if (SharedPreferencesUtils.getIsLoggedIn()){
             message = SharedPreferencesUtils.getVolunteerID()+" invites you to a video call";
        }else {
            message = caseId+" invites you to a video call";
        }
        String invite_id = identity;
        String room_id = caseId;
        String type = "video";
        VideoNotificationBody body = new VideoNotificationBody("SV000071",message,"CI000147","video");
        RequestBody identity_body = RequestBody.create(invite_id,MediaType.parse("text/plain"));
        RequestBody message_body = RequestBody.create(message,MediaType.parse("text/plain"));
        RequestBody caseID_body = RequestBody.create(room_id,MediaType.parse("text/plain"));
        RequestBody type_body = RequestBody.create(type,MediaType.parse("text/plain"));

        RequestBody requestBody = new MultipartBody.Builder()
                .addFormDataPart("invite_id",invite_id)
                .addFormDataPart("message",message)
                .addFormDataPart("room_id",room_id)
                .addFormDataPart("type",type)
                .build();
        return sdkStarterService.sendVideoNotification(identity_body,message_body,caseID_body,type_body);
        //return sdkStarterService.sendVideoNotification(requestBody);
    }

    public static Call<MessageModel> defaultMessage(String channel,String sender,String message) {
        return sdkStarterService.defaultMessages(channel,sender,message);
    }


}
