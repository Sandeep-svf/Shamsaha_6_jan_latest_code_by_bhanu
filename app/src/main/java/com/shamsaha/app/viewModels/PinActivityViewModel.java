package com.shamsaha.app.viewModels;

import com.shamsaha.app.ApiModel.PinModel;
import com.shamsaha.app.ApiModel.volunter.MessageModel;
import com.shamsaha.app.api.api;
import com.shamsaha.app.retrofitAdaptor.retrofit;

import org.jetbrains.annotations.NotNull;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
/**
 * This is the ViewModel class that handles the data and network requests
 * for the Pin Activity or Fragment.
 *
 * @author Athanasios Fotiadis
 * Created on 7/8/2021
 */
public class PinActivityViewModel extends ViewModel {
    private final MutableLiveData<String> hitApiResult = new MutableLiveData<>();
    private final MutableLiveData<String> hitForgetApiResult = new MutableLiveData<>();


    public LiveData<String> getHitApiResult() {
        return hitApiResult;
    }

    public LiveData<String> getHitForgetApiResult() {
        return hitForgetApiResult;
    }

    public void hitApi(String deviceId, String pin) {
        api api = retrofit.retrofit.create(api.class);
        Call<PinModel> call = api.checkvictim(deviceId, pin);
        call.enqueue(new Callback<PinModel>() {
            @Override
            public void onResponse(@NotNull Call<PinModel> call, @NotNull Response<PinModel> response) {
                if (response.isSuccessful() && response.body() != null) {
                    if (response.body().getStatus().equals("valid")) {
                        hitApiResult.postValue("valid");

                    } else {
                        hitApiResult.postValue("invalid");

                    }
                } else {
                    hitApiResult.postValue("request was unsuccessful");

                }
            }

            @Override
            public void onFailure(@NotNull Call<PinModel> call, @NotNull Throwable t) {
                hitApiResult.postValue("No internet");
            }
        });
    }

    public void hitForgetApi(String deviceId) {
        api api = retrofit.retrofit.create(api.class);
        Call<MessageModel> call = api.forgetPINVictim(deviceId);
        call.enqueue(new Callback<MessageModel>() {
            @Override
            public void onResponse(@NotNull Call<MessageModel> call, @NotNull Response<MessageModel> response) {
                if (response.isSuccessful() && response.body() != null) {
                    hitForgetApiResult.postValue(response.body().getMessage());
                } else {
                    hitForgetApiResult.postValue(response.message());
                }
            }

            @Override
            public void onFailure(@NotNull Call<MessageModel> call, @NotNull Throwable t) {
                hitForgetApiResult.postValue("No internet");
            }
        });
    }
}
