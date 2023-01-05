package com.shamsaha.app.viewModels;

import com.shamsaha.app.ApiModel.GetInvolved;
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

public class GetInvolvedViewModel extends ViewModel {

    private final MutableLiveData<String> mutableLiveDataContent = new MutableLiveData<>();
    public LiveData<String> getMutableLiveDataContent() {
        return mutableLiveDataContent;
    }

    public void hitGetInvolvedApi() {
        api api = retrofit.retrofit.create(api.class);
        Call<List<GetInvolved>> getInvolved = api.getInvolvedData();
        getInvolved.enqueue(new Callback<List<GetInvolved>>() {
            @Override
            public void onResponse(@NotNull Call<List<GetInvolved>> call, @NotNull Response<List<GetInvolved>> response) {

                if (response.isSuccessful() && response.body()!=null){
                    List<GetInvolved> data = response.body();

                    for (GetInvolved l : data) {


                        if (baseURL.LANGUAGE_CODE.equalsIgnoreCase("en")) {
                            mutableLiveDataContent.postValue(l.getContentEn());
                        } else {
                            mutableLiveDataContent.postValue(l.getContentAr());
                        }

                        //aboutTxt.setText(l.getContentEn());
                    }

                }

            }

            @Override
            public void onFailure(@NotNull Call<List<GetInvolved>> call, @NotNull Throwable t) {
                mutableLiveDataContent.postValue(t.getMessage());
            }
        });
    }


}
