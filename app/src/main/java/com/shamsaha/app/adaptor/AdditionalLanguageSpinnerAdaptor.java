package com.shamsaha.app.adaptor;

import androidx.annotation.NonNull;

public class AdditionalLanguageSpinnerAdaptor {

    private String Language;
    private String id;

    public AdditionalLanguageSpinnerAdaptor(String Language, String id) {
        this.Language = Language;
        this.id = id;
    }

    public String getLanguage() {
        return Language;
    }

    public void setLanguage(String Language) {
        Language = Language;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    @NonNull
    @Override
    public String toString() {
        return Language;
    }
}
