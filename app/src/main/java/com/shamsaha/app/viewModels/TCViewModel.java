package com.shamsaha.app.viewModels;

import com.shamsaha.app.ApiModel.Termsncondition;
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
 * for the TCActivity or Fragment.
 *
 * @author Athanasios Fotiadis
 * Created on 28/7/2021
 */
public class TCViewModel extends ViewModel {
    private final MutableLiveData<String> stringMutableLiveData= new MutableLiveData<>();

    public LiveData<String> getApi_data() {
        return stringMutableLiveData;
    }

    public TCViewModel() {
    }

    public void hitApi() {
        api api = retrofit.retrofit.create(api.class);
        Call<List<Termsncondition>> getInvolved = api.termsnconditionsData();
        getInvolved.enqueue(new Callback<List<Termsncondition>>() {
            @Override
            public void onResponse(@NotNull Call<List<Termsncondition>> call, @NotNull Response<List<Termsncondition>> response) {
                List<Termsncondition> data = response.body();

                if (data != null) {
                    for (Termsncondition l : data) {
                        String api_data = l.getContentEn();
                        api_data = api_data.replace("color:#555555", "color:#FFF");
                        stringMutableLiveData.postValue(api_data);
                    }
                }
            }

            @Override
            public void onFailure(@NotNull Call<List<Termsncondition>> call, @NotNull Throwable t) {
                stringMutableLiveData.postValue(t.getMessage());
            }
        });
    }
}
