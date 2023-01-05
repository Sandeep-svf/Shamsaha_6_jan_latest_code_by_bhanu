package com.shamsaha.app.adaptor;

import androidx.annotation.NonNull;

public class CaseIDSpinnerAdaptor {

    private String CaseID;

    public CaseIDSpinnerAdaptor(String _CaseID) {
        this.CaseID = _CaseID;
    }

    public String getLanguage() {
        return CaseID;
    }

    public void setLanguage(String _CaseID) {
        CaseID = _CaseID;
    }


    @NonNull
    @Override
    public String toString() {
        return CaseID;
    }
}
