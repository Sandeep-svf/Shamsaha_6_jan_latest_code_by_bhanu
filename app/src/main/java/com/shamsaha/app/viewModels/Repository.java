package com.shamsaha.app.viewModels;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

public class Repository {
    private static volatile Repository instance;


    private Repository() {
    }

    public static Repository getInstance() {
        if (instance == null) {
            synchronized (Repository.class) {
                if (instance == null) {
                    instance = new Repository();
                }
            }
        }
        return instance;
    }
    private MutableLiveData<String> callTo = new MutableLiveData<>();

    public void setCallTo(MutableLiveData<String> callTo) {
        this.callTo = callTo;
    }

    public LiveData<String> getCallTo() {
        return callTo;
    }
}
