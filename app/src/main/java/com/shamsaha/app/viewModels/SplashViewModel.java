package com.shamsaha.app.viewModels;

import android.content.SharedPreferences;

import com.google.android.exoplayer2.util.Log;
import com.shamsaha.app.ApiModel.volunter.MessageModel;
import com.shamsaha.app.activity.ChatHelper.Application.TwilioChatApplication;
import com.shamsaha.app.api.api;
import com.shamsaha.app.retrofitAdaptor.retrofit;

import org.jetbrains.annotations.NotNull;

import java.util.UUID;

import androidx.lifecycle.ViewModel;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.content.Context.MODE_PRIVATE;

public class SplashViewModel extends ViewModel {
    private static final String TAG = "SplashViewModel";

    public SplashViewModel() {
    }

    public void hitApi(String uniqueDeviceId){
        if (uniqueDeviceId.trim().isEmpty()){
            uniqueDeviceId = UUID.randomUUID().toString();
        }
        Log.d("SplashActivity", "Device ID : __________ " + uniqueDeviceId);
        api api = retrofit.retrofit.create(api.class);
        Call<MessageModel> call = api.Checkdevice(uniqueDeviceId);
        call.enqueue(new Callback<MessageModel>() {
            @Override
            public void onResponse(@NotNull Call<MessageModel> call, @NotNull Response<MessageModel> response) {
                if (response.isSuccessful() && response.body() != null) {
                    saveData(response.body().getStatus().equals("valid"));
                }
            }

            @Override
            public void onFailure(@NotNull Call<MessageModel> call, @NotNull Throwable t) {
                android.util.Log.e(TAG, "onFailure: " + t.getMessage());
            }
        });
    }

    private void saveData(boolean valid) {
        SharedPreferences sharedPreferences = TwilioChatApplication.get().getSharedPreferences("Setting", MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean("PIN_Status", valid);
        editor.apply();
        android.util.Log.i(TAG, "saveData: "+sharedPreferences.getString("Setting",""));
    }

}
