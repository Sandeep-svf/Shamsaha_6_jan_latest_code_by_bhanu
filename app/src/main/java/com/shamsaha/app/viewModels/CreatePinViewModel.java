package com.shamsaha.app.viewModels;

import android.util.Log;

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
 * for the CreatePin Activity or Fragment.
 *
 * @author Athanasios Fotiadis
 * Created on 2/8/2021
 */
public class CreatePinViewModel extends ViewModel {
    private final MutableLiveData<String> mutableLiveDataHitApi = new MutableLiveData<>();
    private final MutableLiveData<String> mutableLiveDataHitChangePINApi = new MutableLiveData<>();
    private final MutableLiveData<String> mutableLiveDataHitDisablePINApi = new MutableLiveData<>();


    public LiveData<String> getMutableLiveDataHitApi() {
        return mutableLiveDataHitApi;
    }

    public LiveData<String> getMutableLiveDataHitChangePINApi() {
        return mutableLiveDataHitChangePINApi;
    }

    public LiveData<String> getMutableLiveDataHitDisablePINApi() {
        return mutableLiveDataHitDisablePINApi;
    }

    public void hitApi(String deviceID, String pin, String conf_pin, String name, String email, String mobile, String address, String nationality) {
        api api = retrofit.retrofit.create(api.class);
        Call<MessageModel> call = api.register_victim(deviceID, pin, conf_pin, name, email, mobile, address, nationality);
        call.enqueue(new Callback<MessageModel>() {
            @Override
            public void onResponse(@NotNull Call<MessageModel> call, @NotNull Response<MessageModel> response) {
                if (response.isSuccessful()) {
                    if (response.body().getStatus().equals("valid")) {
                        mutableLiveDataHitApi.postValue("valid");
                    } else {
                        mutableLiveDataHitApi.postValue(response.body().getMessage());
                    }
                } else {
                    Log.d("err-Nithin", "error ----> " + response.code());
                    //Toast.makeText(CreatePinActivity.this, "result: " + response.body().getMessage(), Toast.LENGTH_SHORT).show();
                    mutableLiveDataHitApi.postValue(response.body().getMessage());
                }
            }

            @Override
            public void onFailure(@NotNull Call<MessageModel> call, @NotNull Throwable t) {
                Log.d("err-Nithin", "error1 ----> " + t.toString());
                mutableLiveDataHitApi.postValue(t.getMessage());
            }
        });
    }

    public void hitChangePINApi(String deviceID, String pin) {

        api api = retrofit.retrofit.create(api.class);
        Call<MessageModel> call = api.changepinVictim(deviceID, pin);
        call.enqueue(new Callback<MessageModel>() {
            @Override
            public void onResponse(@NotNull Call<MessageModel> call, @NotNull Response<MessageModel> response) {
                if (response.isSuccessful()) {
                    if (response.body().getStatus().equals("valid")) {
                        mutableLiveDataHitChangePINApi.postValue("valid");
                    }
                } else {
                    Log.d("err-Nithin", "error ----> " + response.code());
                    mutableLiveDataHitChangePINApi.postValue(response.body().getMessage());
                }
            }

            @Override
            public void onFailure(@NotNull Call<MessageModel> call, @NotNull Throwable t) {
                Log.d("err-Nithin", "error1 ----> " + t.toString());
                mutableLiveDataHitChangePINApi.postValue(t.getMessage());
            }
        });
    }

    public void hitDisablePINApi(String deviceID) {
        api api = retrofit.retrofit.create(api.class);
        Call<MessageModel> call = api.checkdeviceVictim(deviceID);
        call.enqueue(new Callback<MessageModel>() {
            @Override
            public void onResponse(@NotNull Call<MessageModel> call, @NotNull Response<MessageModel> response) {
                if (response.isSuccessful()) {
                    if (response.body().getStatus().equals("valid")) {
                        mutableLiveDataHitDisablePINApi.postValue("valid");
                    }
                } else {
                    Log.d("err-Nithin", "error ----> " + response.code());
                    mutableLiveDataHitDisablePINApi.postValue(response.body().getMessage());

                }
            }

            @Override
            public void onFailure(@NotNull Call<MessageModel> call, @NotNull Throwable t) {
                Log.d("err-Nithin", "error1 ----> " + t.toString());
                mutableLiveDataHitDisablePINApi.postValue(t.getMessage());
            }
        });
    }
}
