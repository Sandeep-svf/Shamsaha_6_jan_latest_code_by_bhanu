package com.shamsaha.app.ApiModel;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class event {

    @SerializedName("wceid")
    @Expose
    private String wceid;
    @SerializedName("event_type")
    @Expose
    private String eventType;
    @SerializedName("title_en")
    @Expose
    private String titleEn;
    @SerializedName("title_ar")
    @Expose
    private String titleAr;
    @SerializedName("short_en")
    @Expose
    private String shortEn;
    @SerializedName("content_en")
    @Expose
    private String contentEn;
    @SerializedName("content_ar")
    @Expose
    private String contentAr;
    @SerializedName("venu")
    @Expose
    private String venu;
    @SerializedName("venu_time")
    @Expose
    private String venuTime;
    @SerializedName("date")
    @Expose
    private String date;
    @SerializedName("price")
    @Expose
    private String price;
    @SerializedName("status")
    @Expose
    private String status;
    @SerializedName("event_pic")
    @Expose
    private String image;

    public String getWceid() {
        return wceid;
    }

    public void setWceid(String wceid) {
        this.wceid = wceid;
    }

    public String getEventType() {
        return eventType;
    }

    public void setEventType(String eventType) {
        this.eventType = eventType;
    }

    public String getTitleEn() {
        return titleEn;
    }

    public void setTitleEn(String titleEn) {
        this.titleEn = titleEn;
    }

    public String getTitleAr() {
        return titleAr;
    }

    public void setTitleAr(String titleAr) {
        this.titleAr = titleAr;
    }

    public String getShortEn() {
        return shortEn;
    }

    public void setShortEn(String shortEn) {
        this.shortEn = shortEn;
    }

    public String getContentEn() {
        return contentEn;
    }

    public void setContentEn(String contentEn) {
        this.contentEn = contentEn;
    }

    public String getContentAr() {
        return contentAr;
    }

    public void setContentAr(String contentAr) {
        this.contentAr = contentAr;
    }

    public String getVenu() {
        return venu;
    }

    public void setVenu(String venu) {
        this.venu = venu;
    }

    public String getVenuTime() {
        return venuTime;
    }

    public void setVenuTime(String venuTime) {
        this.venuTime = venuTime;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }


}
