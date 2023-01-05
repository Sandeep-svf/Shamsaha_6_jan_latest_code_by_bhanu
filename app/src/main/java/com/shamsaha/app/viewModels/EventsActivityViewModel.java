package com.shamsaha.app.viewModels;

import android.util.Log;

import com.shamsaha.app.ApiModel.event;
import com.shamsaha.app.api.api;
import com.shamsaha.app.retrofitAdaptor.retrofit;

import org.jetbrains.annotations.NotNull;

import java.util.List;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
/**
 * This is the ViewModel class that handles the data and network requests
 * for the events Activity or Fragment.
 *
 * @author Athanasios Fotiadis
 * Created on 8/8/2021
 */
public class EventsActivityViewModel extends ViewModel {
    private final MutableLiveData<List<event>> events = new MutableLiveData<>();


    public LiveData<List<event>> getEvents() {
        return events;
    }

    public void hitApi() {
        api api = retrofit.retrofit.create(api.class);
        Call<List<event>> call = api.event();
        call.enqueue(new Callback<List<event>>() {
            @Override
            public void onResponse(@NotNull Call<List<event>> call, @NotNull Response<List<event>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    Log.d("respCall", String.valueOf(response.body().size()));
                    events.postValue(response.body());

                } else {
                    Log.d("respCall", "response : \n\n" + response.errorBody());
                }
            }

            @Override
            public void onFailure(@NotNull Call<List<event>> call, @NotNull Throwable t) {
                Log.d("respCall", "err : " + t.toString());
            }
        });
    }
}
