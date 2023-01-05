package com.shamsaha.app.activity.ChatHelper.Channel;

import android.content.Intent;
import android.content.res.Resources;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;

import com.shamsaha.app.R;
import com.shamsaha.app.activity.ChatHelper.AccessToken.AccessTokenFetcher;
import com.shamsaha.app.activity.ChatHelper.Application.TwilioChatApplication;
import com.shamsaha.app.activity.ChatHelper.Message.ChatClientManager;
import com.shamsaha.app.activity.ChatHelper.listener.TaskCompletionListener;
import com.shamsaha.app.activity.Victem.ChatCaseIdActivity;
import com.shamsaha.app.activity.volunteer.onduty.DutyShiftActivity1;
import com.twilio.chat.CallbackListener;
import com.twilio.chat.Channel;
import com.twilio.chat.Channel.ChannelType;
import com.twilio.chat.ChannelDescriptor;
import com.twilio.chat.Channels;
import com.twilio.chat.ChatClient;
import com.twilio.chat.ChatClientListener;
import com.twilio.chat.ErrorInfo;
import com.twilio.chat.Paginator;
import com.twilio.chat.StatusListener;
import com.twilio.chat.User;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import androidx.localbroadcastmanager.content.LocalBroadcastManager;

public class ChannelManager implements ChatClientListener {
    private static final String TAG = "ChannelManager";
    private static ChannelManager sharedManager = new ChannelManager();
    public Channel generalChannel;
    private ChatClientManager chatClientManager;
    private ChannelExtractor channelExtractor;
    private List<Channel> channels;
    private Channels channelsObject;
    private ChatClientListener listener;
    private String defaultChannelName;
    private String defaultChannelUniqueName;
    private Handler handler;
    private Boolean isRefreshingChannels = false;

    private ChannelManager() {
        this.chatClientManager = TwilioChatApplication.get().getChatClientManager();
        this.channelExtractor = new ChannelExtractor();
        this.listener = this;
        defaultChannelName = getStringResource(R.string.default_channel_name);
        defaultChannelUniqueName = getStringResource(R.string.default_channel_unique_name);
        handler = setupListenerHandler();
        channels = new ArrayList<>();
    }

    public static ChannelManager getInstance() {
        return sharedManager;
    }

    public static ChannelManager getInstance1() {
        sharedManager = new ChannelManager();
        return sharedManager;
    }

    public List<Channel> getChannels() {
        return channels;
    }

    public String getDefaultChannelName() {
        return this.defaultChannelName;
    }

    public void leaveChannelWithHandler(Channel channel, StatusListener handler) {
        channel.leave(handler);
    }

    public void deleteChannelWithHandler(Channel channel, StatusListener handler) {
        channel.destroy(handler);
    }

    public void populateChannels(final LoadChannelListener listener) {
        if (this.chatClientManager == null || this.isRefreshingChannels) {
            return;
        }
//        this.isRefreshingChannels = true;
        handler.post(new Runnable() {
            @Override
            public void run() {
                channelsObject = chatClientManager.getChatClient().getChannels();
                //gowri
                channelsObject.getUserChannelsList(new CallbackListener<Paginator<ChannelDescriptor>>() {
                    @Override
                    public void onSuccess(Paginator<ChannelDescriptor> channelDescriptorPaginator) {
                        if (channelDescriptorPaginator.getItems().size() > 0) {
                            channels = new ArrayList<>();
                            extractChannelsFromPaginatorAndPopulate(channelDescriptorPaginator, listener);
                        } else {
                            listener.onChannelsFinishedLoading(null);
                        }
                    }
                });
                //----
//                channelsObject.getPublicChannelsList(new CallbackListener<Paginator<ChannelDescriptor>>() {
//                    @Override
//                    public void onSuccess(Paginator<ChannelDescriptor> channelDescriptorPaginator) {
//                        extractChannelsFromPaginatorAndPopulate(channelDescriptorPaginator, listener);
//                    }
//                });

            }
        });
    }

    private void requestNextPage(Paginator<ChannelDescriptor> channelsPaginator,
                                 final LoadChannelListener listener) {
        channelsPaginator.requestNextPage(new CallbackListener<Paginator<ChannelDescriptor>>() {
            @Override
            public void onSuccess(Paginator<ChannelDescriptor> channelDescriptorPaginator) {
                extractChannelsFromPaginatorAndPopulate(channelDescriptorPaginator, listener);
            }
        });
    }

    private void extractChannelsFromPaginatorAndPopulate(final Paginator<ChannelDescriptor> channelsPaginator,
                                                         final LoadChannelListener listener) {
        try{
            channelExtractor.extractAndSortFromChannelDescriptor(channelsPaginator,
                    new TaskCompletionListener<List<Channel>, String>() {
                        @Override
                        public void onSuccess(List<Channel> channels) {
                            ChannelManager.this.channels.addAll(channels);
                            Collections.sort(ChannelManager.this.channels, new CustomChannelComparator());
                            ChannelManager.this.isRefreshingChannels = false;
                            chatClientManager.addClientListener(ChannelManager.this);
                            if (channelsPaginator.hasNextPage()) {
                                requestNextPage(channelsPaginator, listener);
                            } else {
                                listener.onChannelsFinishedLoading(ChannelManager.this.channels);
                            }
                        }

                        @Override
                        public void onError(String errorText) {
                            Log.e(TwilioChatApplication.TAG, "Error populating channels: " + errorText);
                        }
                    });
        }catch (Exception e){
            Log.e(TAG, "extractChannelsFromPaginatorAndPopulate: "+e.getMessage());
        }

    }

    public void createChannelWithName(String name, final StatusListener handler) {
        this.channelsObject
                .channelBuilder()
                .withFriendlyName(name)
                .withUniqueName(name)
                .withType(ChannelType.PUBLIC)
                .build(new CallbackListener<Channel>() {
                    @Override
                    public void onSuccess(final Channel newChannel) {
                        handler.onSuccess();
                    }

                    @Override
                    public void onError(ErrorInfo errorInfo) {
                        handler.onError(errorInfo);
                    }
                });
    }

    public void joinOrCreateGeneralChannelWithCompletion(String channelName, final StatusListener listener) {
        defaultChannelUniqueName = channelName;
        defaultChannelName = channelName;
        channelsObject.getChannel(defaultChannelUniqueName, new CallbackListener<Channel>() {
            @Override
            public void onSuccess(Channel channel) {
                ChannelManager.this.generalChannel = channel;
                if (channel != null) {
                    joinGeneralChannelWithCompletion(listener);
                } else {
                    createGeneralChannelWithCompletion(listener);
                }
            }
        });
    }

    private void joinGeneralChannelWithCompletion(final StatusListener listener) {
        if (generalChannel.getStatus() == Channel.ChannelStatus.JOINED) {
            listener.onSuccess();
            return;
        }
        this.generalChannel.join(new StatusListener() {
            @Override
            public void onSuccess() {
                listener.onSuccess();
            }

            @Override
            public void onError(ErrorInfo errorInfo) {
                listener.onError(errorInfo);
            }
        });
    }

    private void createGeneralChannelWithCompletion(final StatusListener listener) {
        this.channelsObject
                .channelBuilder()
                .withFriendlyName(defaultChannelName)
                .withUniqueName(defaultChannelUniqueName)
                .withType(ChannelType.PUBLIC)
                .build(new CallbackListener<Channel>() {
                    @Override
                    public void onSuccess(final Channel channel) {
                        ChannelManager.this.generalChannel = channel;
                        ChannelManager.this.channels.add(channel);
                        joinGeneralChannelWithCompletion(listener);
                    }

                    @Override
                    public void onError(ErrorInfo errorInfo) {
                        listener.onError(errorInfo);
                    }
                });
    }

    public void setChannelListener(ChatClientListener listener) {
        this.listener = listener;
    }

    private String getStringResource(int id) {
        Resources resources = TwilioChatApplication.get().getResources();
        return resources.getString(id);
    }

    private void sendRegistrationSuccess(Channel channel) {
        Intent intent = new Intent(DutyShiftActivity1.ACTION_CHANNEL_CREATED);
        intent.putExtra(DutyShiftActivity1.ACTION_CHANNEL, channel);
        LocalBroadcastManager.getInstance(TwilioChatApplication.get()).sendBroadcast(intent);
    }

    @Override
    public void onChannelAdded(Channel channel) {
        if (listener != null) {
            if (listener.getClass() == ChatCaseIdActivity.class) {
                sendRegistrationSuccess(channel);
            }
            listener.onChannelAdded(channel);
        }
    }

    @Override
    public void onChannelUpdated(Channel channel, Channel.UpdateReason updateReason) {
//        if (listener != null) {
//            listener.onChannelUpdated(channel, updateReason);
//        }
    }

    @Override
    public void onChannelDeleted(Channel channel) {
        if (listener != null) {
            listener.onChannelDeleted(channel);
        }
    }

    @Override
    public void onChannelSynchronizationChange(Channel channel) {
        if (listener != null) {
            listener.onChannelSynchronizationChange(channel);
        }
    }

    @Override
    public void onError(ErrorInfo errorInfo) {
        if (listener != null) {
            listener.onError(errorInfo);
        }
    }

    @Override
    public void onClientSynchronization(ChatClient.SynchronizationStatus synchronizationStatus) {

    }

    @Override
    public void onChannelJoined(Channel channel) {

    }

    @Override
    public void onChannelInvited(Channel channel) {
        if (listener != null) {
            listener.onChannelInvited(channel);
        }
    }

    @Override
    public void onUserUpdated(User user, User.UpdateReason updateReason) {
        if (listener != null) {
            listener.onUserUpdated(user, updateReason);
        }
    }

    @Override
    public void onUserSubscribed(User user) {

    }

    @Override
    public void onUserUnsubscribed(User user) {

    }

    @Override
    public void onNewMessageNotification(String s, String s1, long l) {
        Log.d(TAG, "onNewMessageNotification: Called");
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
        Log.d(TAG, "onNotificationSubscribed: CALLED");
    }

    @Override
    public void onNotificationFailed(ErrorInfo errorInfo) {

    }

    @Override
    public void onConnectionStateChange(ChatClient.ConnectionState connectionState) {

    }

    @Override
    public void onTokenExpired() {
        refreshAccessToken();
    }

    @Override
    public void onTokenAboutToExpire() {
        refreshAccessToken();
    }

    private void refreshAccessToken() {
        AccessTokenFetcher accessTokenFetcher = chatClientManager.getAccessTokenFetcher();
        String identity = chatClientManager.getIdentity();
        accessTokenFetcher.fetch(identity, new TaskCompletionListener<String, String>() {
            @Override
            public void onSuccess(String token) {
                ChannelManager.this.chatClientManager.getChatClient().updateToken(token, new StatusListener() {
                    @Override
                    public void onSuccess() {
                        Log.d(TwilioChatApplication.TAG, "Successfully updated access token.");
                    }
                });
            }

            @Override
            public void onError(String message) {
                Log.e(TwilioChatApplication.TAG, "Error trying to fetch token: " + message);
            }
        });
    }

    private Handler setupListenerHandler() {
        Looper looper;
        Handler handler;
        if ((looper = Looper.myLooper()) != null) {
            handler = new Handler(looper);
        } else if ((looper = Looper.getMainLooper()) != null) {
            handler = new Handler(looper);
        } else {
            throw new IllegalArgumentException("Channel Listener must have a Looper.");
        }
        return handler;
    }
}
