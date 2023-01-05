package com.shamsaha.app.viewModels;

import android.util.Log;

import com.shamsaha.app.ApiModel.contact;
import com.shamsaha.app.ApiModel.volunter.MessageModel;
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
 * for the Contact us Activity or Fragment.
 *
 * @author Athanasios Fotiadis
 * Created on 24/7/2021
 */
public class ContactUsViewModel extends ViewModel {
    private static final String TAG = "ContactUsViewModel";
    private final MutableLiveData<String> mutableLiveDataContent = new MutableLiveData<>();
    private final MutableLiveData<String> mutableLiveDataMessageResult = new MutableLiveData<>();
    private final MutableLiveData<String> mutableLiveDataEmail = new MutableLiveData<>();
    private final MutableLiveData<String> mutableLiveDataAddress = new MutableLiveData<>();
    private final MutableLiveData<String> mutableLiveDataContact = new MutableLiveData<>();
    private final MutableLiveData<String> mutableLiveDataEnHelpline = new MutableLiveData<>();
    private final MutableLiveData<String> mutableLiveDataArHelpline = new MutableLiveData<>();
    private final MutableLiveData<String> mutableLiveDataFbUrl = new MutableLiveData<>();
    private final MutableLiveData<String> mutableLiveDataInstUrl = new MutableLiveData<>();
    private final MutableLiveData<String> mutableLiveDataLinkUrl = new MutableLiveData<>();
    private final MutableLiveData<String> mutableLiveDataTwitUrl = new MutableLiveData<>();
    private final MutableLiveData<String> mutableLiveDataWeb = new MutableLiveData<>();
    private final MutableLiveData<String> mutableLiveDataMapLink = new MutableLiveData<>();

    /**
     * Required empty constructor
     */
    public ContactUsViewModel() {
    }

    /**
     * This method exposes the observable MutableLiveData object as
     * LiveData to the view of ContactUsActivity or Fragment.
     *
     * @return The LiveData observable object.
     */
    public LiveData<String> getMutableLiveDataContent() {
        return mutableLiveDataContent;
    }

    public LiveData<String> getMutableLiveDataMessageResult() {
        return mutableLiveDataMessageResult;
    }

    public LiveData<String> getPhoneNumber() {
        return mutableLiveDataContact;
    }

    public LiveData<String> getEnHelpline() {
        return mutableLiveDataEnHelpline;
    }

    public LiveData<String> getArHelpline() {
        return mutableLiveDataArHelpline;
    }

    public LiveData<String> getEmail() {
        return mutableLiveDataEmail;
    }

    public LiveData<String> getAddress() {
        return mutableLiveDataAddress;
    }

    public LiveData<String> getFbUrl() {
        return mutableLiveDataFbUrl;
    }

    public LiveData<String> getInstUrl() {
        return mutableLiveDataInstUrl;
    }

    public LiveData<String> getLinkUrl() {
        return mutableLiveDataLinkUrl;
    }

    public LiveData<String> getTWitUrl() {
        return mutableLiveDataTwitUrl;
    }

    public LiveData<String> getMapLink() {
        return mutableLiveDataMapLink;
    }

    public LiveData<String> getWeb() {
        return mutableLiveDataWeb;
    }

    public void hitMessageApi(String name, String email, String message) {
        api api = retrofit.retrofit.create(api.class);
        Call<MessageModel> call = api.Con_message(name, email, message);
        call.enqueue(new Callback<MessageModel>() {
            @Override
            public void onResponse(@NotNull Call<MessageModel> call, @NotNull Response<MessageModel> response) {
                if (response.isSuccessful()) {
                    mutableLiveDataMessageResult.postValue("Message sent successfully");
                    //StyleableToast.makeText(contactsUsActivity.this, "Message sent successfully", Toast.LENGTH_SHORT, R.style.mytoast).show();
                } else {
                    mutableLiveDataMessageResult.postValue("Some thing went wrong");
                    //StyleableToast.makeText(contactsUsActivity.this, "Some thing went wrong", Toast.LENGTH_SHORT, R.style.mytoast).show();
                }
            }

            @Override
            public void onFailure(@NotNull Call<MessageModel> call, @NotNull Throwable t) {
                Log.d(TAG, "onFailure: " + t.getMessage());
                mutableLiveDataMessageResult.postValue("Some thing went wrong");
            }
        });
    }


    public void getContent() {

        api api = retrofit.retrofit.create(api.class);
        Call<List<contact>> contactCall = api.getContactData();

        contactCall.enqueue(new Callback<List<contact>>() {
            @Override
            public void onResponse(@NotNull Call<List<contact>> call, @NotNull Response<List<contact>> response) {

                List<contact> data = response.body();

                //Toast.makeText(contactsUsActivity.this, response.toString(), Toast.LENGTH_SHORT).show();


                if (data != null) {
                    for (contact l : data) {
                        //Toast.makeText(MainActivity.this, l.getSite_logo(), Toast.LENGTH_SHORT).show();

                        if (baseURL.LANGUAGE_CODE.equalsIgnoreCase("en")) {
                            mutableLiveDataContent.postValue(l.getContentEn());

                        } else {
                            mutableLiveDataContent.postValue(l.getContentAr());

                        }

                        mutableLiveDataContact.postValue(l.getContact());
                        mutableLiveDataEnHelpline.postValue(l.getEnHelpline());
                        mutableLiveDataArHelpline.postValue(l.getArHelpline());
                        mutableLiveDataEmail.postValue(l.getEmail());
                        mutableLiveDataAddress.postValue(l.getAddress());
                        mutableLiveDataFbUrl.postValue(l.getFacebook());
                        mutableLiveDataInstUrl.postValue(l.getInstagram());
                        mutableLiveDataLinkUrl.postValue(l.getLinkden());
                        mutableLiveDataTwitUrl.postValue(l.getTwitter());
                        mutableLiveDataWeb.postValue(l.getWebsite());
                        mutableLiveDataMapLink.postValue(l.getGoogleMap());

                    }
                }
            }

            @Override
            public void onFailure(@NotNull Call<List<contact>> call, @NotNull Throwable t) {

                Log.e(TAG, "onFailure: " + t.getMessage());
                //Toast.makeText(contactsUsActivity.this, t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}
