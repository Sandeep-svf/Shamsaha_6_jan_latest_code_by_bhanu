package com.shamsaha.app.viewModels;

import android.util.Log;

import com.shamsaha.app.ApiModel.ResourceCategory;
import com.shamsaha.app.ApiModel.resource_location;
import com.shamsaha.app.adaptor.SpinnerAdaptor;
import com.shamsaha.app.api.api;
import com.shamsaha.app.retrofitAdaptor.retrofit;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * This is the ViewModel class that handles the data and network requests
 * for the Resources Activity or Fragment.
 *
 * @author Athanasios Fotiadis
 * Created on 7/8/2021
 */
public class ResourcesViewModel extends ViewModel {
    private final MutableLiveData<List<SpinnerAdaptor>> listResourceLocation = new MutableLiveData<>();
    private final MutableLiveData<List<ResourceCategory>> listResourceCategory = new MutableLiveData<>();


    public LiveData<List<SpinnerAdaptor>> getListResourceLocation() {
        return listResourceLocation;
    }

    public LiveData<List<ResourceCategory>> getListResourceCategory() {
        return listResourceCategory;
    }

    public void spinnerApi() {
        api api = retrofit.retrofit.create(api.class);
        Call<List<resource_location>> resource = api.getResource();
        resource.enqueue(new Callback<List<resource_location>>() {
            @Override
            public void onResponse(@NotNull Call<List<resource_location>> call, @NotNull Response<List<resource_location>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    List<resource_location> resData = response.body();
                    List<SpinnerAdaptor> location = new ArrayList<>();
                    for (resource_location resource_location : resData) {
                        SpinnerAdaptor adaptor = new SpinnerAdaptor(resource_location.getLocationName(), resource_location.getWcrid());
                        location.add(adaptor);
                    }
                    listResourceLocation.postValue(location);

                } else {
                    listResourceLocation.postValue(null);

                }
            }

            @Override
            public void onFailure(@NotNull Call<List<resource_location>> call, @NotNull Throwable t) {
                listResourceLocation.postValue(null);
            }
        });
    }

    public void hitApiResourceCategory(String location_id) {
        api api = retrofit.retrofit.create(com.shamsaha.app.api.api.class);
        Call<List<ResourceCategory>> call = api.resource_category(location_id);
        call.enqueue(new Callback<List<ResourceCategory>>() {
            @Override
            public void onResponse(@NotNull Call<List<ResourceCategory>> call, @NotNull Response<List<ResourceCategory>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    listResourceCategory.postValue(response.body());
                } else {
                    listResourceCategory.postValue(null);
                    Log.d("respoCall", "err : " + response.errorBody());
                }

                if (response.body() != null) {
                    Log.d("respoCall", String.valueOf(response.body().size()));
                }

            }

            @Override
            public void onFailure(@NotNull Call<List<ResourceCategory>> call, @NotNull Throwable t) {
                listResourceCategory.postValue(null);
                Log.d("respoCall", "err : " + t.toString());
            }
        });
    }
}
