package com.shamsaha.app.viewModels;

import android.util.Log;

import com.shamsaha.app.ApiModel.PublicPart.AboutModel.Who_we_are;
import com.shamsaha.app.api.api;
import com.shamsaha.app.retrofitAdaptor.retrofit;
import com.shamsaha.app.utils.ConstantsURL.baseURL;

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
 * for the About Activity or Fragment.
 *
 * @author Athanasios Fotiadis
 * Created on 30/7/2021
 */
public class AboutViewModel extends ViewModel {
    private static final String TAG = "AboutViewModel";
    private final MutableLiveData<String> mutableLiveDataContent = new MutableLiveData<>();

    public LiveData<String> getMutableLiveDataContent() {
        return mutableLiveDataContent;
    }


    public void getContent(){
        api api = retrofit.retrofit.create(api.class);
        Call<List<Who_we_are>> aboutCall = api.aboutData();

        aboutCall.enqueue(new Callback<List<Who_we_are>>() {
            @Override
            public void onResponse(@NotNull Call<List<Who_we_are>> call, @NotNull Response<List<Who_we_are>> response) {
                if (response.isSuccessful() && response.body()!= null){
                    List<Who_we_are> dataAbout = response.body();
                    for (Who_we_are about : dataAbout){
                        if (baseURL.LANGUAGE_CODE.equalsIgnoreCase("en")) {
                            mutableLiveDataContent.postValue(about.getContentEn());
                        }else {
                            mutableLiveDataContent.postValue(about.getContentAr());
                        }
                    }
                }
            }

            @Override
            public void onFailure(@NotNull Call<List<Who_we_are>> call, @NotNull Throwable t) {
                Log.e(TAG, "onFailure: "+t.getMessage(),t );
            }
        });
    }

}
