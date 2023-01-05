package com.shamsaha.app.activity.ChatHelper.listener;

public interface TaskCompletionListener<T, U> {

  void onSuccess(T t);

  void onError(U u);
}
