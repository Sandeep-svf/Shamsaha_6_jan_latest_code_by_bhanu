package com.shamsaha.app.activity.ChatHelper.Channel;


import com.shamsaha.app.R;
import com.shamsaha.app.activity.ChatHelper.Application.TwilioChatApplication;
import com.twilio.chat.Channel;

import java.util.Comparator;

public class CustomChannelComparator implements Comparator<Channel> {
  private String defaultChannelName;

  public CustomChannelComparator() {
    defaultChannelName =
        TwilioChatApplication.get().getResources().getString(R.string.default_channel_name);
  }

  @Override
  public int compare(Channel lhs, Channel rhs) {
    if (lhs.getFriendlyName().contentEquals(defaultChannelName)) {
      return -100;
    } else if (rhs.getFriendlyName().contentEquals(defaultChannelName)) {
      return 100;
    }
    return lhs.getFriendlyName().toLowerCase().compareTo(rhs.getFriendlyName().toLowerCase());
  }
}
