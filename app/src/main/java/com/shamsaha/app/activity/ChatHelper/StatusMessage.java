package com.shamsaha.app.activity.ChatHelper;


import com.shamsaha.app.activity.ChatHelper.Message.ChatMessage;

public class StatusMessage implements ChatMessage {
  private String author = "";

  public StatusMessage(String author) {
    this.author = author;
  }

  @Override
  public String getAuthor() {
    return author;
  }

  @Override
  public String getDateCreated() {
    throw new UnsupportedOperationException();
  }

  @Override
  public String getMessageBody() {
    throw new UnsupportedOperationException();
  }
}
