package com.shamsaha.app.activity.Victem;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.shamsaha.app.ApiModel.volunter.MessageModel;
import com.shamsaha.app.R;
import com.shamsaha.app.activity.CallHelper.videocall.RegistrationIntentService1;
import com.shamsaha.app.activity.CallHelper.videocall.TwilioSDKStarterAPI;
import com.shamsaha.app.activity.ChatHelper.Application.TwilioChatApplication;
import com.shamsaha.app.activity.ChatHelper.ChanModel;
import com.shamsaha.app.activity.ChatHelper.Channel.ChannelManager;
import com.shamsaha.app.activity.ChatHelper.Channel.LoadChannelListener;
import com.shamsaha.app.activity.ChatHelper.Message.ChatClientManager;
import com.shamsaha.app.activity.ChatHelper.listener.TaskCompletionListener;
import com.shamsaha.app.utils.SharedPreferencesUtils;
import com.twilio.chat.CallbackListener;
import com.twilio.chat.Channel;
import com.twilio.chat.ChannelListener;
import com.twilio.chat.Channels;
import com.twilio.chat.ChatClient;
import com.twilio.chat.ChatClientListener;
import com.twilio.chat.ErrorInfo;
import com.twilio.chat.Member;
import com.twilio.chat.Members;
import com.twilio.chat.Message;
import com.twilio.chat.Messages;
import com.twilio.chat.StatusListener;
import com.twilio.chat.User;

import java.util.List;

import androidx.appcompat.app.AppCompatActivity;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ChatCaseIdActivity extends AppCompatActivity implements ChatClientListener, ChannelListener {

    private static final String TAG = "abc";
    private String CaseId, volID, language;
    private TextView tv_caseID;
    private Button btn_talk_with_us;
    private ChannelManager channelManager;
    private ProgressDialog progressDialog;
    private ChatClientManager chatClientManager;
    Messages messagesObject;
    private ChanModel chanModel;
    private Channels channelsObject;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat_case_id);

        tv_caseID = findViewById(R.id.tv_caseID);
        btn_talk_with_us = findViewById(R.id.btn_talk_with_us);
        channelManager = ChannelManager.getInstance();
        Intent intent = getIntent();
        CaseId = intent.getStringExtra("caseID");
        volID = intent.getStringExtra("VolID");
        language = intent.getStringExtra("language");


        tv_caseID.setText(CaseId);
        chanModel = ChanModel.getInstance();

        btn_talk_with_us.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkTwilioClient();
            }
        });

    }

    private void checkTwilioClient() {
        showActivityIndicator(getResources().getString(R.string.loading_channels_message));
        chatClientManager = TwilioChatApplication.get().getChatClientManager();
//        if (chatClientManager.getChatClient() == null) {
        initializeClient();
//        } else {
//            populateChannels();
//        }
    }

    private void initializeClient() {
        chatClientManager.connectClient(CaseId, new TaskCompletionListener<Void, String>() {
            @Override
            public void onSuccess(Void aVoid) {
                //call FCM TOKEN and registerFCM...
                chatClientManager.getChatClient().registerFCMToken(new ChatClient.FCMToken(SharedPreferencesUtils.getFCMToken()), new StatusListener() {
                    @Override
                    public void onSuccess() {
                        Log.d("Register FCM Token", "onSuccess: Successful");
                    }
                });



                populateChannels();
            }

            @Override
            public void onError(String errorMessage) {
                stopActivityIndicator();
            }
        });
    }


    private void showActivityIndicator(final String message) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                progressDialog = new ProgressDialog(ChatCaseIdActivity.this);
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

    private void getCurrentChannel(ChatClientManager chatClientManager1, final StatusListener listener){
        channelsObject = chatClientManager1.getChatClient().getChannels();
        listener.onSuccess();
    }

    private void populateChannels() {
        channelManager.setChannelListener(ChatCaseIdActivity.this);
        channelManager.populateChannels(new LoadChannelListener() {
            @Override
            public void onChannelsFinishedLoading(List<Channel> channels) {
                channelManager.createChannelWithName(CaseId, new StatusListener() {
                    @Override
                    public void onSuccess() {
//                        refreshChannels();
                        getCurrentChannel(chatClientManager, new StatusListener() {
                            @Override
                            public void onSuccess() {
                                channelsObject.getChannel(CaseId, new CallbackListener<Channel>() {
                                    @Override
                                    public void onSuccess(Channel channel) {
                                        setChannel(channel);
                                    }
                                    @Override
                                    public void onError(ErrorInfo errorInfo) {
                                        super.onError(errorInfo);
                                        Log.d(TAG, "onError: "+errorInfo);
                                        Toast.makeText(ChatCaseIdActivity.this, "errorInfo " + errorInfo, Toast.LENGTH_SHORT).show();
//                                initializeClientCase(caseID);
                                    }
                                });
                            }
                        });
//                        channelsObject = chatClientManager.getChatClient().getChannels();

                    }
                    @Override
                    public void onError(ErrorInfo errorInfo) {
//                        setChannel(CaseId);
                        getCurrentChannel(chatClientManager, new StatusListener() {
                            @Override
                            public void onSuccess() {
                                channelsObject.getChannel(CaseId, new CallbackListener<Channel>() {
                                    @Override
                                    public void onSuccess(Channel channel) {
                                        setChannel1(channel);
                                    }
                                    @Override
                                    public void onError(ErrorInfo errorInfo) {
                                        super.onError(errorInfo);
                                        Log.d(TAG, "onError: "+errorInfo);
                                        Toast.makeText(ChatCaseIdActivity.this, "errorInfo " + errorInfo, Toast.LENGTH_SHORT).show();
//                                initializeClientCase(caseID);
                                    }
                                });
                            }
                        });
                        Toast.makeText(ChatCaseIdActivity.this, errorInfo.getMessage(), Toast.LENGTH_SHORT).show();
                        Log.d(TAG,errorInfo.getMessage());
//                        stopActivityIndicator();
                    }
                });
            }
        });
    }

    private void register(Channel channel) {
        Intent intent = new Intent(this, RegistrationIntentService1.class);
        intent.putExtra("CHANNEL", channel);
        startService(intent);
    }


    @Override
    public void onChannelJoined(Channel channel) {

    }

    @Override
    public void onChannelInvited(Channel channel) {

    }

    @Override
    public void onChannelAdded(Channel channel) {
//        refreshChannels();
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
        Log.d("bjbsjg","s = "+s+"\ns1 = "+s1);
        Log.d(TAG, "onNewMessageNotification: "+"source s:"+s+"Source s1: "+s1);

    }

    @Override
    public void onAddedToChannelNotification(String s) {
        Log.d(TAG, "onAddedToChannelNotification: "+s);

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

//    private void setChannel(final String name) {
//        List<Channel> channels = channelManager.getChannels();
//        if (channels == null) {
//            return;
//        }
//        int position = 0;
//        for (int i = 0; i < channels.size(); i++) {
//            if (name.equalsIgnoreCase(channels.get(i).getFriendlyName())) {
//                position = i;
//            }
//        }
//
////        final Channel currentChannel = chatFragment.getCurrentChannel();
//        final Channel selectedChannel = channels.get(position);
//
////        if (currentChannel != null && currentChannel.getSid().contentEquals(selectedChannel.getSid())) {
//////            drawer.closeDrawer(GravityCompat.START);
////            return;
////        }
////
////        if(selectedChannel != null){
////            showActivityIndicator("Joining " + selectedChannel.getFriendlyName() + " channel");
////            if(currentChannel != null && currentChannel.getStatus() == Channel.ChannelStatus.JOINED){
////                channelManager.leaveChannelWithHandler(currentChannel, new StatusListener() {
////                    @Override
////                    public void onSuccess() {
////                        joinChannel(selectedChannel);
////                    }
////
////                    @Override
////                    public void onError(ErrorInfo errorInfo) {
////                        stopActivityIndicator();
////                    }
////                });
////                return;
////            }
////
////            joinChannel(selectedChannel);
////            stopActivityIndicator();
////        }else {
////            stopActivityIndicator();
////            showAlertWithMessage(getStringResource(R.string.generic_error));
////            Log.e(TwilioChatApplication.TAG,"Selected channel out of range");
////        }
//        Log.d("mmmmm", "\nres:------------------------------------------- ");
//        Log.d("mmmmm", "res: " + selectedChannel.getFriendlyName());
//        Log.d("mmmmm", "res: " + selectedChannel.getDateCreated());
//        Log.d("mmmmm", "res: " + selectedChannel.getSid());
//        Log.d("mmmmm", "res: " + selectedChannel.getUniqueName());
//        Log.d("mmmmm", "res:------------------------------------------- \n");
////joinChannel(selectedChannel);
//        chanModel.setChannel(selectedChannel);
//        register(selectedChannel);
////        joinChannel(selectedChannel);
//        channelManager = ChannelManager.getInstance1();
//        chanModel = ChanModel.getInstance1();
//        chatClientManager = TwilioChatApplication.get().getChatClientManager();
//        chatClientManager.connectClient(volID, new TaskCompletionListener<Void, String>() {
//            @Override
//            public void onSuccess(Void aVoid) {
//                runOnUiThread(new Runnable() {
//                    @Override
//                    public void run() {
//                        Members membersObject = selectedChannel.getMembers();
//                        membersObject.addByIdentity(volID, new StatusListener() {
//                            @Override
//                            public void onSuccess() {
//                                Toast.makeText(ChatCaseIdActivity.this, volID + " Member Added", Toast.LENGTH_SHORT).show();
//                                channelManager.populateChannels(new LoadChannelListener() {
//                                    @Override
//                                    public void onChannelsFinishedLoading(List<Channel> channels) {
//                                        if (channels == null) {
//                                            stopActivityIndicator();
//                                            return;
//                                        }
//                                        int position = 0;
//                                        for (int i = 0; i < channels.size(); i++) {
//                                            if (name.equalsIgnoreCase(channels.get(i).getFriendlyName())) {
//                                                position = i;
//                                            }
//                                        }
//                                        final Channel selectedChannel = channels.get(position);
//                                        chanModel.setChannel(selectedChannel);
//                                        joinChannel(chanModel.getChannel());
//                                    }
//                                });
//                            }
//
//                            @Override
//                            public void onError(ErrorInfo errorInfo) {
//                                Toast.makeText(ChatCaseIdActivity.this, errorInfo.getMessage(), Toast.LENGTH_SHORT).show();
//                            }
//                        });
//                    }
//                });
//            }
//
//            @Override
//            public void onError(String errorMessage) {
//                stopActivityIndicator();
//            }
//        });
//    }
//    private void setChannel(final String name) {
//        List<Channel> channels = channelManager.getChannels();
//        if (channels == null) {
//            return;
//        }
//        int position = 0;
//        for (int i = 0; i < channels.size(); i++) {
//            if (name.equalsIgnoreCase(channels.get(i).getFriendlyName())) {
//                position = i;
//            }
//        }
//        final Channel selectedChannel = channels.get(position);
//        Log.d("mmmmm", "\nres:------------------------------------------- ");
//        Log.d("mmmmm", "res: " + selectedChannel.getFriendlyName());
//        Log.d("mmmmm", "res: " + selectedChannel.getDateCreated());
//        Log.d("mmmmm", "res: " + selectedChannel.getSid());
//        Log.d("mmmmm", "res: " + selectedChannel.getUniqueName());
//        Log.d("mmmmm", "res:------------------------------------------- \n");
////joinChannel(selectedChannel);
//        chanModel.setChannel(selectedChannel);
//        register(selectedChannel);
////        joinChannel(selectedChannel);
//        channelManager = ChannelManager.getInstance1();
//        chanModel = ChanModel.getInstance1();
//        chatClientManager = TwilioChatApplication.get().getChatClientManager();
//        chatClientManager.connectClient(volID, new TaskCompletionListener<Void, String>() {
//            @Override
//            public void onSuccess(Void aVoid) {
//                runOnUiThread(new Runnable() {
//                    @Override
//                    public void run() {
//                        Members membersObject = selectedChannel.getMembers();
//                        membersObject.addByIdentity(volID, new StatusListener() {
//                            @Override
//                            public void onSuccess() {
//                                Toast.makeText(ChatCaseIdActivity.this, volID + " Member Added", Toast.LENGTH_SHORT).show();
//                                channelManager.populateChannels(new LoadChannelListener() {
//                                    @Override
//                                    public void onChannelsFinishedLoading(List<Channel> channels) {
//                                        if (channels == null) {
//                                            stopActivityIndicator();
//                                            return;
//                                        }
//                                        int position = 0;
//                                        for (int i = 0; i < channels.size(); i++) {
//                                            if (name.equalsIgnoreCase(channels.get(i).getFriendlyName())) {
//                                                position = i;
//                                            }
//                                        }
//                                        final Channel selectedChannel = channels.get(position);
//                                        chanModel.setChannel(selectedChannel);
//                                        joinChannel(false,chanModel.getChannel());
//                                    }
//                                });
//                            }
//
//                            @Override
//                            public void onError(ErrorInfo errorInfo) {
//                                channelManager.populateChannels(new LoadChannelListener() {
//                                    @Override
//                                    public void onChannelsFinishedLoading(List<Channel> channels) {
//                                        if (channels == null) {
//                                            stopActivityIndicator();
//                                            return;
//                                        }
//                                        int position = 0;
//                                        for (int i = 0; i < channels.size(); i++) {
//                                            if (name.equalsIgnoreCase(channels.get(i).getFriendlyName())) {
//                                                position = i;
//                                            }
//                                        }
//                                        final Channel selectedChannel = channels.get(position);
//                                        chanModel.setChannel(selectedChannel);
//                                        joinChannel(true,chanModel.getChannel());
//                                    }
//                                });
////                                stopActivityIndicator();
//                                Toast.makeText(ChatCaseIdActivity.this, errorInfo.getMessage(), Toast.LENGTH_SHORT).show();
//                            }
//                        });
//                    }
//                });
//            }
//
//            @Override
//            public void onError(String errorMessage) {
//                stopActivityIndicator();
//            }
//        });
//    }






    private void hitApiForSurvivorResult(String channel,String sender, String Message) {
      /*  Retrofit retrofit = new  Retrofit.Builder()
                .baseUrl(baseURL.defaultMessage)
                .addConverterFactory(GsonConverterFactory.create())
                .build();*/




        Call<MessageModel> call = TwilioSDKStarterAPI.defaultMessage(channel,sender,Message);
        call.enqueue(new Callback<MessageModel>() {
            @Override
            public void onResponse(Call<MessageModel> call, Response<MessageModel> response) {
                if(response.isSuccessful() && response.code() == 200){
                    stopActivityIndicator();
                    Intent intent = new Intent(ChatCaseIdActivity.this, ChatActivity.class);
                    intent.putExtra("volID", "");
                    intent.putExtra("caseID", CaseId);
                    startActivity(intent);
                }
            }

            @Override
            public void onFailure(Call<MessageModel> call, Throwable t) {
                stopActivityIndicator();
                Toast.makeText(ChatCaseIdActivity.this, ""+t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }


    private void setChannel(final Channel channel) {
//        List<Channel> channels = channelManager.getChannels();
//        if (channels == null) {
//            return;
//        }
//        int position = 0;
//        for (int i = 0; i < channels.size(); i++) {
//            if (name.equalsIgnoreCase(channels.get(i).getFriendlyName())) {
//                position = i;
//            }
//        }
        final Channel selectedChannel = channel;
        Log.d("mmmmm", "\nres:------------------------------------------- ");
        Log.d("mmmmm", "res: " + selectedChannel.getFriendlyName());
        Log.d("mmmmm", "res: " + selectedChannel.getDateCreated());
        Log.d("mmmmm", "res: " + selectedChannel.getSid());
        Log.d("mmmmm", "res: " + selectedChannel.getUniqueName());
        Log.d("mmmmm", "res:------------------------------------------- \n");
//joinChannel(selectedChannel);
        chanModel.setChannel(selectedChannel);
        register(selectedChannel);
//        joinChannel(selectedChannel);
        channelManager = ChannelManager.getInstance1();
        chanModel = ChanModel.getInstance1();
        chatClientManager = TwilioChatApplication.get().getChatClientManager();
        chatClientManager.connectClient(volID, new TaskCompletionListener<Void, String>() {
            @Override
            public void onSuccess(Void aVoid) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Members membersObject = selectedChannel.getMembers();
                        membersObject.addByIdentity(volID, new StatusListener() {
                            @Override
                            public void onSuccess() {
                                Toast.makeText(ChatCaseIdActivity.this, volID + " Member Added", Toast.LENGTH_SHORT).show();
//                                channelManager.populateChannels(new LoadChannelListener() {
//                                    @Override
//                                    public void onChannelsFinishedLoading(List<Channel> channels) {
////                                        if (channels == null) {
////                                            stopActivityIndicator();
////                                            return;
////                                        }
////                                        int position = 0;
////                                        for (int i = 0; i < channels.size(); i++) {
////                                            if (name.equalsIgnoreCase(channels.get(i).getFriendlyName())) {
////                                                position = i;
////                                            }
////                                        }
//                                        final Channel selectedChannel = channel;
//                                        chanModel.setChannel(selectedChannel);
//                                        joinChannel(false,chanModel.getChannel());
//                                    }
//                                });



//                                stopActivityIndicator();
                                chanModel.setChannel(selectedChannel);
                                stopActivityIndicator();
                                Intent intent = new Intent(ChatCaseIdActivity.this, ChatActivity.class);
                                intent.putExtra("volID", "");
                                intent.putExtra("caseID", CaseId);
                                startActivity(intent);
                               /* if(language.equals("ARABIC")){
                                    //TODO Arabic
                                    hitApiForSurvivorResult(selectedChannel.getUniqueName().toString(),volID,"شكرًا لتواصلك معنا. أنا هنا لأسمتع إليك وأساعدك. نحن مجموعة من المتطوعات مدربات بنظام نوبات على مدار 24 ساعة في كل يوم من السنة. لذا، إذا اتصلتي بنا من قبل، قد لا أعرف قصتك، لكن لا تترددي في التحدث معي وإخباري كيف بإمكاني مساعدتك.");
                                }else {
                                    hitApiForSurvivorResult(selectedChannel.getUniqueName().toString(),volID,"Thank you for contacting us. I am here to listen and help you. We are a group of trained rotating volunteers 24hrs every day, so if you called us before, I may not know your history. But for now, please feel free to talk to me and let me know how I can help.");
                                }*/


                            }

                            @Override
                            public void onError(ErrorInfo errorInfo) {
                                chanModel.setChannel(selectedChannel);
                                stopActivityIndicator();
                                Intent intent = new Intent(ChatCaseIdActivity.this, ChatActivity.class);
                                intent.putExtra("volID", "");
                                intent.putExtra("caseID", CaseId);
                                startActivity(intent);
//                                channelManager.populateChannels(new LoadChannelListener() {
//                                    @Override
//                                    public void onChannelsFinishedLoading(List<Channel> channels) {
////                                        if (channels == null) {
////                                            stopActivityIndicator();
////                                            return;
////                                        }
////                                        int position = 0;
////                                        for (int i = 0; i < channels.size(); i++) {
////                                            if (name.equalsIgnoreCase(channels.get(i).getFriendlyName())) {
////                                                position = i;
////                                            }
////                                        }
//                                        final Channel selectedChannel = channel;
//                                        chanModel.setChannel(selectedChannel);
//                                        joinChannel(true,chanModel.getChannel());
//                                    }
//                                });
//                                stopActivityIndicator();
                                Toast.makeText(ChatCaseIdActivity.this, errorInfo.getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        });
                    }
                });
            }

            @Override
            public void onError(String errorMessage) {
                stopActivityIndicator();
            }
        });
    }
    private void setChannel1(final Channel channel) {
//        List<Channel> channels = channelManager.getChannels();
//        if (channels == null) {
//            return;
//        }
//        int position = 0;
//        for (int i = 0; i < channels.size(); i++) {
//            if (name.equalsIgnoreCase(channels.get(i).getFriendlyName())) {
//                position = i;
//            }
//        }
        final Channel selectedChannel = channel;
        Log.d("mmmmm", "\nres:------------------------------------------- ");
        Log.d("mmmmm", "res: " + selectedChannel.getFriendlyName());
        Log.d("mmmmm", "res: " + selectedChannel.getDateCreated());
        Log.d("mmmmm", "res: " + selectedChannel.getSid());
        Log.d("mmmmm", "res: " + selectedChannel.getUniqueName());
        Log.d("mmmmm", "res:------------------------------------------- \n");
//joinChannel(selectedChannel);
        chanModel.setChannel(selectedChannel);
        register(selectedChannel);
//        joinChannel(selectedChannel);
        channelManager = ChannelManager.getInstance1();
        chanModel = ChanModel.getInstance1();
        chatClientManager = TwilioChatApplication.get().getChatClientManager();
        chatClientManager.connectClient(volID, new TaskCompletionListener<Void, String>() {
            @Override
            public void onSuccess(Void aVoid) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Members membersObject = selectedChannel.getMembers();
                        membersObject.addByIdentity(volID, new StatusListener() {
                            @Override
                            public void onSuccess() {
                                Toast.makeText(ChatCaseIdActivity.this, volID + " Member Added", Toast.LENGTH_SHORT).show();
//                                channelManager.populateChannels(new LoadChannelListener() {
//                                    @Override
//                                    public void onChannelsFinishedLoading(List<Channel> channels) {
////                                        if (channels == null) {
////                                            stopActivityIndicator();
////                                            return;
////                                        }
////                                        int position = 0;
////                                        for (int i = 0; i < channels.size(); i++) {
////                                            if (name.equalsIgnoreCase(channels.get(i).getFriendlyName())) {
////                                                position = i;
////                                            }
////                                        }
//                                        final Channel selectedChannel = channel;
//                                        chanModel.setChannel(selectedChannel);
//                                        joinChannel(false,chanModel.getChannel());
//                                    }
//                                });



//                                stopActivityIndicator();
                                chanModel.setChannel(selectedChannel);
                                stopActivityIndicator();
                                Intent intent = new Intent(ChatCaseIdActivity.this, ChatActivity.class);
                                intent.putExtra("volID", "");
                                intent.putExtra("caseID", CaseId);
                                startActivity(intent);

                            }

                            @Override
                            public void onError(ErrorInfo errorInfo) {
                                chanModel.setChannel(selectedChannel);
                                stopActivityIndicator();
                                Intent intent = new Intent(ChatCaseIdActivity.this, ChatActivity.class);
                                intent.putExtra("volID", "");
                                intent.putExtra("caseID", CaseId);
                                intent.putExtra("vId", volID);
                                startActivity(intent);
                                finish();
//                                channelManager.populateChannels(new LoadChannelListener() {
//                                    @Override
//                                    public void onChannelsFinishedLoading(List<Channel> channels) {
////                                        if (channels == null) {
////                                            stopActivityIndicator();
////                                            return;
////                                        }
////                                        int position = 0;
////                                        for (int i = 0; i < channels.size(); i++) {
////                                            if (name.equalsIgnoreCase(channels.get(i).getFriendlyName())) {
////                                                position = i;
////                                            }
////                                        }
//                                        final Channel selectedChannel = channel;
//                                        chanModel.setChannel(selectedChannel);
//                                        joinChannel(true,chanModel.getChannel());
//                                    }
//                                });
//                                stopActivityIndicator();
                                Toast.makeText(ChatCaseIdActivity.this, errorInfo.getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        });
                    }
                });
            }

            @Override
            public void onError(String errorMessage) {
                stopActivityIndicator();
            }
        });
    }










    private void joinChannel(boolean memberAdded,final Channel selectedChannel) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                setCurrentChannel(memberAdded,selectedChannel, new StatusListener() {
                    @Override
                    public void onSuccess() {
                        Log.d("ChatCaseIdActivity", "Joined channel");
                    }

                    @Override
                    public void onError(ErrorInfo errorInfo) {
                    }
                });
            }
        });
    }

    public void setCurrentChannel(boolean memberAdded,Channel currentChannel, final StatusListener handler) {
        currentChannel.addListener(this);
        //currentChannel.addListener(this);
        if(!memberAdded){
            if (currentChannel.getStatus() == Channel.ChannelStatus.JOINED) {
                messagesObject = currentChannel.getMessages();
                if (messagesObject != null) {
                    Message.Options messageOptions = Message.options().withBody("Thank you for contacting us. I am here to listen and help you.\n" +
                            "We are a group of trained rotating volunteers 24hrs every day, so if you called us before, I may not know your history. But for now, please feel free to talk to me and let me know how I can help.");
                    messagesObject.sendMessage(messageOptions, null);
                }
                loadMessages(currentChannel, handler);
            } else {
                currentChannel.join(new StatusListener() {
                    @Override
                    public void onSuccess() {
                        messagesObject = currentChannel.getMessages();
                        if (messagesObject != null) {
                            Message.Options messageOptions = Message.options().withBody("Thank you for contacting us. I am here to listen and help you.\n"+
                                    "We are a group of trained rotating volunteers 24hrs every day, so if you called us before, I may not know your history. But for now, please feel free to talk to me and let me know how I can help.");
                            messagesObject.sendMessage(messageOptions, null);
                        }
                        loadMessages(currentChannel, handler);
                    }

                    @Override
                    public void onError(ErrorInfo errorInfo) {
                        Toast.makeText(ChatCaseIdActivity.this, errorInfo.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
            }
        }else {
            if (currentChannel.getStatus() == Channel.ChannelStatus.JOINED) {
//                messagesObject = currentChannel.getMessages();
//                if (messagesObject != null) {
//                    Message.Options messageOptions = Message.options().withBody("Thank you for contacting us. I am here to listen and help you.\n" +
//                            "We are a group of trained rotating volunteers 24hrs every day, so if you called us before, I may not know your history. But for now, please feel free to talk to me and let me know how I can help.");
//                    messagesObject.sendMessage(messageOptions, null);
//                }
                loadMessages(currentChannel, handler);
            } else {
                currentChannel.join(new StatusListener() {
                    @Override
                    public void onSuccess() {
//                        messagesObject = currentChannel.getMessages();
//                        if (messagesObject != null) {
//                            Message.Options messageOptions = Message.options().withBody("Thank you for contacting us. I am here to listen and help you.\n"+
//                                    "We are a group of trained rotating volunteers 24hrs every day, so if you called us before, I may not know your history. But for now, please feel free to talk to me and let me know how I can help.");
//                            messagesObject.sendMessage(messageOptions, null);
//                        }
                        loadMessages(currentChannel, handler);
                    }

                    @Override
                    public void onError(ErrorInfo errorInfo) {
                        Toast.makeText(ChatCaseIdActivity.this, errorInfo.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
            }
        }

    }

    private void loadMessages(Channel currentChannel, final StatusListener handler) {
        channelManager = ChannelManager.getInstance1();
        chanModel = ChanModel.getInstance1();
        chatClientManager = TwilioChatApplication.get().getChatClientManager();
        chatClientManager.connectClient(CaseId, new TaskCompletionListener<Void, String>() {
            @Override
            public void onSuccess(Void aVoid) {
                channelManager.setChannelListener(ChatCaseIdActivity.this);
                handler.onSuccess();
                channelManager.populateChannels(new LoadChannelListener() {
                    @Override
                    public void onChannelsFinishedLoading(List<Channel> channels) {
                        if (channels == null) {
                            stopActivityIndicator();
                            return;
                        }
                        int position = 0;
                        for (int i = 0; i < channels.size(); i++) {
                            if (currentChannel.getFriendlyName().equalsIgnoreCase(channels.get(i).getFriendlyName())) {
                                position = i;
                            }
                        }
                        final Channel selectedChannel = channels.get(position);
                        chanModel.setChannel(selectedChannel);
                        register(currentChannel);
                        stopActivityIndicator();
                        Intent intent = new Intent(ChatCaseIdActivity.this, ChatActivity.class);
                        intent.putExtra("volID", "");
                        intent.putExtra("caseID", CaseId);
                        startActivity(intent);
                    }
                });
//                channelManager.populateChannels(new LoadChannelListener() {
//                    @Override
//                    public void onChannelsFinishedLoading(final List<Channel> channels) {
//                        if (channels == null) {
//                            return;
//                        }
//                        int position = 0;
//                        for (int i = 0; i < channels.size(); i++) {
//                            if (CaseId.equalsIgnoreCase(channels.get(i).getFriendlyName())) {
//                                position = i;
//                            }
//                        }
//
////        final Channel currentChannel = chatFragment.getCurrentChannel();
//                        final Channel selectedChannel = channels.get(position);
//                        chanModel.setChannel(selectedChannel);
//                        register(selectedChannel);
//                        stopActivityIndicator();
//                        Intent intent = new Intent(ChatCaseIdActivity.this, ChatActivity.class);
//                        intent.putExtra("volID", "");
//                        intent.putExtra("caseID", CaseId);
//                        startActivity(intent);
//                        runOnUiThread(new Runnable() {
//                            @Override
//                            public void run() {
//                                Members membersObject = selectedChannel.getMembers();
//                                membersObject.addByIdentity(CaseId, new StatusListener() {
//                                    @Override
//                                    public void onSuccess() {
//                                        Toast.makeText(ChatCaseIdActivity.this, CaseId + " Member Added", Toast.LENGTH_SHORT).show();
//                                        currentChannel.addListener(ChatCaseIdActivity.this);
//                                        //currentChannel.addListener(this);
//                                        if (currentChannel.getStatus() == Channel.ChannelStatus.JOINED) {
//                                            stopActivityIndicator();
//                                            Intent intent = new Intent(ChatCaseIdActivity.this, ChatActivity.class);
//                                            intent.putExtra("volID", "");
//                                            intent.putExtra("caseID", CaseId);
//                                            startActivity(intent);
//                                        } else {
//                                            currentChannel.join(new StatusListener() {
//                                                @Override
//                                                public void onSuccess() {
//                                                    stopActivityIndicator();
//                                                    Intent intent = new Intent(ChatCaseIdActivity.this, ChatActivity.class);
//                                                    intent.putExtra("volID", "");
//                                                    intent.putExtra("caseID", CaseId);
//                                                    startActivity(intent);
//                                                }
//
//                                                @Override
//                                                public void onError(ErrorInfo errorInfo) {
//                                                }
//                                            });
//                                        }
//                                    }
//
//                                    @Override
//                                    public void onError(ErrorInfo errorInfo) {
//                                        Toast.makeText(ChatCaseIdActivity.this, errorInfo.getMessage(), Toast.LENGTH_SHORT).show();
//                                        stopActivityIndicator();
//                                    }
//                                });
//                            }
//                        });
            }

            @Override
            public void onError(String s) {
                stopActivityIndicator();
            }
        });
    }

//            @Override
//            public void onError(String errorMessage) {
//                stopActivityIndicator();
//            }
//        });
//    }


//    private void refreshChannels() {
//        channelManager.populateChannels(new LoadChannelListener() {
//            @Override
//            public void onChannelsFinishedLoading(final List<Channel> channels) {
//                Toast.makeText(ChatCaseIdActivity.this, "Channel created for author " + CaseId + " and channel name " + CaseId, Toast.LENGTH_SHORT).show();
//                setChannel(CaseId);
//            }
//        });
//    }

    @Override
    public void onMessageAdded(Message message) {
        Log.e("bjbsjg", "bfjbgus");
    }

    @Override
    public void onMessageUpdated(Message message, Message.UpdateReason updateReason) {

    }

    @Override
    public void onMessageDeleted(Message message) {

    }

    @Override
    public void onMemberAdded(Member member) {

    }

    @Override
    public void onMemberUpdated(Member member, Member.UpdateReason updateReason) {

    }

    @Override
    public void onMemberDeleted(Member member) {

    }

    @Override
    public void onTypingStarted(Channel channel, Member member) {

    }

    @Override
    public void onTypingEnded(Channel channel, Member member) {

    }

    @Override
    public void onSynchronizationChanged(Channel channel) {

    }
}