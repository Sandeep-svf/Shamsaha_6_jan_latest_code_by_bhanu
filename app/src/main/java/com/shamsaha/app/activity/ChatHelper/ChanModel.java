package com.shamsaha.app.activity.ChatHelper;

import com.twilio.chat.Channel;

public class ChanModel {

    private static ChanModel instance;
    public Channel channel;

    private ChanModel() {
    }

    public static ChanModel getInstance() {
        if (instance == null) {
            instance = new ChanModel();
        }
        return instance;
    }

    public static ChanModel getInstance1() {
//        if (instance == null) {
        instance = new ChanModel();
//        }
        return instance;
    }

    public Channel getChannel() {
        return channel;
    }

    public void setChannel(Channel channel) {
        this.channel = channel;
    }
}
