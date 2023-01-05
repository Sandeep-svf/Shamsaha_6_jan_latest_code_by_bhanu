package com.shamsaha.app.activity.Victem;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.widget.ListView;
import android.widget.TextView;

import com.shamsaha.app.R;
import com.shamsaha.app.activity.ChatHelper.AccessToken.AlertDialogHandler;
import com.shamsaha.app.activity.ChatHelper.Application.SessionManager;
import com.shamsaha.app.activity.ChatHelper.Application.TwilioChatApplication;
import com.shamsaha.app.activity.ChatHelper.ChanModel;
import com.shamsaha.app.activity.ChatHelper.Channel.ChannelManager;
import com.shamsaha.app.activity.ChatHelper.Message.ChatClientManager;
import com.shamsaha.app.activity.ChatHelper.listener.TaskCompletionListener;
import com.shamsaha.app.adaptor.ChannelAdapter;
import com.twilio.chat.Channel;
import com.twilio.chat.ChatClient;
import com.twilio.chat.ChatClientListener;
import com.twilio.chat.ErrorInfo;
import com.twilio.chat.StatusListener;
import com.twilio.chat.User;

import java.util.List;

import androidx.appcompat.app.AppCompatActivity;

public class ChatChanelListActivity extends AppCompatActivity implements ChatClientListener {

    private ChatClientManager chatClientManager;
    private TextView usernameTextView;
    private ListView channelsListView;
    private ChanModel chanModel;
    private ChannelManager channelManager;
    private ProgressDialog progressDialog;
    private Context context;
    private ChannelAdapter channelAdapter;


    @Override
    protected void onDestroy() {
        super.onDestroy();
        new Handler().post(() -> {
            //chatClientManager.shutdown();
            //TwilioChatApplication.get().getChatClientManager().setChatClient(null);
        });
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat_chanel_list);

        chanModel = ChanModel.getInstance();

        channelsListView = (ListView) findViewById(R.id.listViewChannels);
        usernameTextView = (TextView) findViewById(R.id.textViewUsername);
        channelManager = ChannelManager.getInstance();

        setUsernameTextView();
        setUpListeners();
        checkTwilioClient();
    }

    private void setUsernameTextView() {
        String username =
                SessionManager.getInstance().getUserDetails().get(SessionManager.KEY_USERNAME);
        usernameTextView.setText(username);
    }

    private void checkTwilioClient() {
        showActivityIndicator(getStringResource(R.string.loading_channels_message));
        chatClientManager = TwilioChatApplication.get().getChatClientManager();
        if (chatClientManager.getChatClient() == null) {
            initializeClient();
        } else {
            populateChannels();
        }
    }

    private void initializeClient() {
        chatClientManager.connectClient("", new TaskCompletionListener<Void, String>() {
            @Override
            public void onSuccess(Void aVoid) {
                populateChannels();
            }

            @Override
            public void onError(String errorMessage) {
                stopActivityIndicator();
                showAlertWithMessage("Client connection error: " + errorMessage);
            }
        });
    }

    private void showAlertWithMessage(final String message) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                AlertDialogHandler.displayAlertWithMessage(message, context);
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

    private void populateChannels() {
        channelManager.setChannelListener(ChatChanelListActivity.this);
        channelManager.populateChannels(channels -> {
            channelAdapter = new ChannelAdapter(ChatChanelListActivity.this, channels);
            channelsListView.setAdapter(channelAdapter);
            ChatChanelListActivity.this.channelManager
                    .joinOrCreateGeneralChannelWithCompletion("", new StatusListener() {
                        @Override
                        public void onSuccess() {
                            runOnUiThread(() -> {
                                channelAdapter.notifyDataSetChanged();
                                stopActivityIndicator();
                                setChannel(0);
                            });
                        }

                        @Override
                        public void onError(ErrorInfo errorInfo) {
                            showAlertWithMessage(getStringResource(R.string.generic_error) + " - " + errorInfo.getMessage());
                        }
                    });
        });
    }

    private String getStringResource(int id) {
        Resources resources = getResources();
        return resources.getString(id);
    }

    private void showActivityIndicator(final String message) {
        runOnUiThread(() -> {
            progressDialog = new ProgressDialog(ChatChanelListActivity.this);
            progressDialog.setMessage(message);
            progressDialog.show();
            progressDialog.setCanceledOnTouchOutside(false);
            progressDialog.setCancelable(false);
        });
    }

    private void setUpListeners() {

        channelsListView.setOnItemClickListener((parent, view, position, id) -> setChannel(position));
    }

    private void setChannel(final int position) {
        List<Channel> channels = channelManager.getChannels();
        if (channels == null) {
            return;
        }
//        final Channel currentChannel = chatFragment.getCurrentChannel();
        final Channel selectedChannel = channels.get(position);

//        if (currentChannel != null && currentChannel.getSid().contentEquals(selectedChannel.getSid())) {
////            drawer.closeDrawer(GravityCompat.START);
//            return;
//        }
//
//        if(selectedChannel != null){
//            showActivityIndicator("Joining " + selectedChannel.getFriendlyName() + " channel");
//            if(currentChannel != null && currentChannel.getStatus() == Channel.ChannelStatus.JOINED){
//                channelManager.leaveChannelWithHandler(currentChannel, new StatusListener() {
//                    @Override
//                    public void onSuccess() {
//                        joinChannel(selectedChannel);
//                    }
//
//                    @Override
//                    public void onError(ErrorInfo errorInfo) {
//                        stopActivityIndicator();
//                    }
//                });
//                return;
//            }
//
//            joinChannel(selectedChannel);
//            stopActivityIndicator();
//        }else {
//            stopActivityIndicator();
//            showAlertWithMessage(getStringResource(R.string.generic_error));
//            Log.e(TwilioChatApplication.TAG,"Selected channel out of range");
//        }
        Log.d("mmmmm", "\nres:------------------------------------------- ");
        Log.d("mmmmm", "res: " + selectedChannel.getFriendlyName());
        Log.d("mmmmm", "res: " + selectedChannel.getDateCreated());
        Log.d("mmmmm", "res: " + selectedChannel.getSid());
        Log.d("mmmmm", "res: " + selectedChannel.getUniqueName());
        Log.d("mmmmm", "res:------------------------------------------- \n");
//joinChannel(selectedChannel);

        chanModel.setChannel(selectedChannel);
        Intent intent = new Intent(ChatChanelListActivity.this, ChatActivity.class);
        startActivity(intent);

    }

    @Override
    public void onChannelJoined(Channel channel) {

    }

    @Override
    public void onChannelInvited(Channel channel) {

    }

    @Override
    public void onChannelAdded(Channel channel) {

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
    }

    @Override
    public void onAddedToChannelNotification(String s) {

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

}