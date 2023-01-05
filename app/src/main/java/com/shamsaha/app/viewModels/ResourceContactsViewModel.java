package com.shamsaha.app.viewModels;

import android.util.Log;

import com.shamsaha.app.ApiModel.resource;
import com.shamsaha.app.api.api;
import com.shamsaha.app.retrofitAdaptor.retrofit;

import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.util.List;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ResourceContactsViewModel extends ViewModel {
    private static final String TAG = "ResourceContactsViewMod";
    private final MutableLiveData<List<resource>> listMutableLiveDataResource = new MutableLiveData<>();
    private final MutableLiveData<String> result = new MutableLiveData<>();


    public LiveData<List<resource>> getListMutableLiveDataResource() {
        return listMutableLiveDataResource;
    }

    public LiveData<String> getResult() {
        return result;
    }

    public void hitApiResourceContact(String location, String category) {
        api api = retrofit.retrofit.create(com.shamsaha.app.api.api.class);
        Call<List<resource>> call = api.resourceContact(location, category);

        call.enqueue(new Callback<List<resource>>() {
            @Override
            public void onResponse(@NotNull Call<List<resource>> call, @NotNull Response<List<resource>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    listMutableLiveDataResource.postValue(response.body());
                } else {
                    try {
                        if (response.errorBody() != null) {
                            result.postValue(response.errorBody().string());
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                }

            }

            @Override
            public void onFailure(@NotNull Call<List<resource>> call, @NotNull Throwable t) {
                Log.d(TAG, "onFailure: "+t.getMessage());
            }
        });

    }
}
