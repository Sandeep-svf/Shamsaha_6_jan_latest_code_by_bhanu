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
 * for the Home Activity or Fragment.
 *
 * @author Athanasios Fotiadis
 * Created on 21/7/2021
 */
public class HomeViewModel extends ViewModel {
    private static final String TAG = "HomeViewModel";
    //private String api_data;
    private final MutableLiveData<String> mutableLiveDataContent = new MutableLiveData<>();

    /**
     * Required empty constructor
     */
    public HomeViewModel() {
    }

    /**
     * This method exposes the observable MutableLiveData object as
     * LiveData to the view of HomeActivity or HomeFragment.
     *
     * @return The LiveData observable object.
     */
    public LiveData<String> getMutableLiveDataContent() {
        return mutableLiveDataContent;
    }

    /**
     * This is the method that makes the api call and sets the result
     * string into the MutableLiveData.
     */
    public void hitApi() {
        api api = retrofit.retrofit.create(api.class);
        Call<List<Who_we_are>> aboutCall = api.getAboutData();
//        showActivityIndicator("Loading");
        aboutCall.enqueue(new Callback<List<Who_we_are>>() {
            @Override
            public void onResponse(@NotNull Call<List<Who_we_are>> call, @NotNull Response<List<Who_we_are>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    List<Who_we_are> dataAbout = response.body();

                    for (Who_we_are about : dataAbout) {

                        if (baseURL.LANGUAGE_CODE.equalsIgnoreCase("en")) {
//                        imageView.setVisibility(View.VISIBLE);
//                        imageView27.setVisibility(View.VISIBLE);
//                        imageView28.setVisibility(View.GONE);
//                        imageView29.setVisibility(View.GONE);
                            mutableLiveDataContent.postValue(about.getContentEn());
                            // isViewLoaded = true;

                        } else {
//                        imageView.setVisibility(View.GONE);
//                        imageView27.setVisibility(View.GONE);
//                        imageView28.setVisibility(View.VISIBLE);
//                        imageView29.setVisibility(View.VISIBLE);
                            //imageView28.animate().rotation(180);
                            //imageView29.animate().rotation(180);
                            mutableLiveDataContent.postValue(about.getContentAr());
                            //isViewLoaded = true;

                        }
//                    api_data = api_data.replace("<p>", "<p style=\"font-family:avenir;font-size:18;color:white;text-align: center;\">");
//                    api_data = api_data.replace("<h5>", "<h5 style=\"font-family:avenir;font-size:18;color:white;text-align: center;\">");
//                    api_data = api_data.replace("<h4>", "<h4 style=\"font-family:avenir;font-size:18;color:white;text-align: center;\">");
//                    api_data = api_data.replace("<h3>", "<h3 style=\"font-family:avenir;font-size:18;color:white;text-align: center;\">");
//                    api_data = api_data.replace("<h2>", "<h2 style=\"font-family:avenir;font-size:18;color:white;text-align: center;\">");
//                    api_data = api_data.replace("<h1>", "<h1 style=\"font-family:avenir;font-size:18;color:white;text-align: center;\">");
//                    api_data = api_data.replace("<pre>", "<pre style=\"font-family:avenir;font-size:18;color:white;text-align: center;\">");
//                    api_data = api_data.replace("&#39;", "'");
//                    Log.d("apidata", api_data);
                        //setwebView(api_data);
//                    about_text = about.getContentEn().replace("<h3>", "");
//                    about_text = about_text.replace("</h3>", "");
//                    about_text = about_text.replace("<p>", "");
//                    about_text = about_text.replace("</p>", "");
//                    about_text = about_text.replace("&amp;", "&");
                    }
//                stopActivityIndicator();
                }


            }

            @Override
            public void onFailure(@NotNull Call<List<Who_we_are>> call, @NotNull Throwable t) {
//                stopActivityIndicator();
                //Post the error message to result Content.
                mutableLiveDataContent.postValue(t.getMessage());
                Log.e(TAG, "onFailure: " + t.getMessage());
            }
        });
    }

}
