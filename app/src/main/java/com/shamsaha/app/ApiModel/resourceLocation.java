package com.shamsaha.app.ApiModel;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class resourceLocation {

    @SerializedName("wcrid")
    @Expose
    private String wcrid;
    @SerializedName("location_name")
    @Expose
    private String locationName;

    public String getWcrid() {
        return wcrid;
    }

    public void setWcrid(String wcrid) {
        this.wcrid = wcrid;
    }

    public String getLocationName() {
        return locationName;
    }

    public void setLocationName(String locationName) {
        this.locationName = locationName;
    }

}