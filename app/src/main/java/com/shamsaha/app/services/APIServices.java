package com.shamsaha.app.services;

import com.shamsaha.app.ApiModel.volunter.MessageModel;
import com.shamsaha.app.api.api;
import com.shamsaha.app.retrofitAdaptor.retrofit;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class APIServices {

    public void hitCheckoutAPI(String vounter_id,String status){
        api api = retrofit.retrofit.create(api.class);
        retrofit2.Call<MessageModel> checkin = api.checkin(vounter_id,status);
        checkin.enqueue(new Callback<MessageModel>() {
            @Override
            public void onResponse(Call<MessageModel> call, Response<MessageModel> response) {

            }

            @Override
            public void onFailure(Call<MessageModel> call, Throwable t) {

            }
        });
    }

}
