package com.shamsaha.app.utils;

import android.util.Log;
import android.webkit.JavascriptInterface;

public class JsInterface {
   private final SubmitListener mCallback;

    public JsInterface(SubmitListener mCallback) {
        this.mCallback = mCallback;
    }

    @JavascriptInterface
    public void successfulReport(String message){
        Log.i("", "onSubmit: Called");
        mCallback.onSubmit(message);
    }

    public interface SubmitListener {
        void onSubmit(String message);
    }
}
