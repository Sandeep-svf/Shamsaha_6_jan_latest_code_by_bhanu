package com.shamsaha.app.ApiModel;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ResourceCategory {

    @SerializedName("wcrcid")
    @Expose
    private String wcrcid;
    @SerializedName("category_name")
    @Expose
    private String categoryName;
    @SerializedName("category_icon")
    @Expose
    private String categoryIcon;
    @SerializedName("location_name")
    @Expose
    private String locationName;
    @SerializedName("wcrid")
    @Expose
    private String wcrid;

    public String getWcrcid() {
        return wcrcid;
    }

    public void setWcrcid(String wcrcid) {
        this.wcrcid = wcrcid;
    }

    public String getCategoryName() {
        return categoryName;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }

    public String getCategoryIcon() {
        return categoryIcon;
    }

    public void setCategoryIcon(String categoryIcon) {
        this.categoryIcon = categoryIcon;
    }

    public String getLocationName() {
        return locationName;
    }

    public void setLocationName(String locationName) {
        this.locationName = locationName;
    }

    public String getWcrid() {
        return wcrid;
    }

    public void setWcrid(String wcrid) {
        this.wcrid = wcrid;
    }

}
