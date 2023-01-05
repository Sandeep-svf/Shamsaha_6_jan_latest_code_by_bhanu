package com.shamsaha.app.ApiModel;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class corprateSponsor {

    @SerializedName("title_en")
    @Expose
    private String titleEn;
    @SerializedName("content_en")
    @Expose
    private String contentEn;
    @SerializedName("title_ar")
    @Expose
    private String titleAr;
    @SerializedName("content_ar")
    @Expose
    private String contentAr;

    public String getTitleEn() {
        return titleEn;
    }

    public void setTitleEn(String titleEn) {
        this.titleEn = titleEn;
    }

    public String getContentEn() {
        return contentEn;
    }

    public void setContentEn(String contentEn) {
        this.contentEn = contentEn;
    }

    public String getTitleAr() {
        return titleAr;
    }

    public void setTitleAr(String titleAr) {
        this.titleAr = titleAr;
    }

    public String getContentAr() {
        return contentAr;
    }

    public void setContentAr(String contentAr) {
        this.contentAr = contentAr;
    }

}
