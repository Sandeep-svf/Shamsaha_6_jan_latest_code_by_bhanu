package com.shamsaha.app.ApiModel;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class volunteering  {

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
    @SerializedName("vol_form_con_en")
    @Expose
    private String volFormConEn;
    @SerializedName("vol_form_con_ar")
    @Expose
    private String volFormConAr;
    @SerializedName("vol_goo_con_en")
    @Expose
    private String volGooConEn;
    @SerializedName("vol_goo_con_ar")
    @Expose
    private String volGooConAr;

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

    public String getVolFormConEn() {
        return volFormConEn;
    }

    public void setVolFormConEn(String volFormConEn) {
        this.volFormConEn = volFormConEn;
    }

    public String getVolFormConAr() {
        return volFormConAr;
    }

    public void setVolFormConAr(String volFormConAr) {
        this.volFormConAr = volFormConAr;
    }

    public String getVolGooConEn() {
        return volGooConEn;
    }

    public void setVolGooConEn(String volGooConEn) {
        this.volGooConEn = volGooConEn;
    }

    public String getVolGooConAr() {
        return volGooConAr;
    }

    public void setVolGooConAr(String volGooConAr) {
        this.volGooConAr = volGooConAr;
    }

}