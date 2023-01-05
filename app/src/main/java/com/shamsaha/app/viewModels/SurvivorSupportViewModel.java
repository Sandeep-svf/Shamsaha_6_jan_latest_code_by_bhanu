package com.shamsaha.app.viewModels;

import android.util.Log;

import com.shamsaha.app.ApiModel.SurvivorModel;
import com.shamsaha.app.api.api;
import com.shamsaha.app.utils.ConstantsURL.baseURL;

import org.jetbrains.annotations.NotNull;

import java.util.List;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
/**
 * This is the ViewModel class that handles the data and network requests
 * for the SurvivorSupport Activity or Fragment.
 *
 * @author Athanasios Fotiadis
 * Created on 27/7/2021
 */
public class SurvivorSupportViewModel extends ViewModel {
    private static final String TAG = "SurvivorSupportViewMode";
    private final MutableLiveData<List<SurvivorModel.Datum>> listMutableLiveData = new MutableLiveData<>();

    /**
     * This method exposes the observable MutableLiveData object as
     * LiveData to the view of SurvivorSupportActivity or Fragment.
     *
     * @return The LiveData observable object.
     */
    public LiveData<List<SurvivorModel.Datum>> getData() {
        return listMutableLiveData;
    }

    public SurvivorSupportViewModel() {
    }

    public void hitApiForSurvivorResult() {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(baseURL.BaseURL + "core_script/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        api api = retrofit.create(api.class);

        Call<SurvivorModel> call = api.getSurvivorResults();
        call.enqueue(new Callback<SurvivorModel>() {
            @Override
            public void onResponse(@NotNull Call<SurvivorModel> call, @NotNull Response<SurvivorModel> response) {
                if (response.isSuccessful() && response.code() == 200 && response.body() != null && response.body().getData() != null && response.body().getData().size() > 0) {
                    listMutableLiveData.postValue(response.body().getData());
                }
            }

            @Override
            public void onFailure(@NotNull Call<SurvivorModel> call, @NotNull Throwable t) {
                Log.e(TAG, "onFailure: " + t.getMessage(), t);
                //Toast.makeText(SurvivorSupportActivity.this, "Nothing Found!!", Toast.LENGTH_SHORT).show();
            }
        });
    }

}
