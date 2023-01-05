package com.shamsaha.app.viewModels;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class CallCaseViewModel extends ViewModel {
    private static final String TAG = "CallCaseViewModel";
    private final Repository repository= Repository.getInstance();

    private MutableLiveData<String> callTo = new MutableLiveData<>();
    private final MutableLiveData<String> caseId = new MutableLiveData<>();

    public LiveData<String> getCallTo() {
        return repository.getCallTo();
    }

    public LiveData<String> getCaseId() {
        return caseId;
    }

    public void setCallTo(MutableLiveData<String> callToVolunteer){
        repository.setCallTo(callToVolunteer)  ;
    }
}
