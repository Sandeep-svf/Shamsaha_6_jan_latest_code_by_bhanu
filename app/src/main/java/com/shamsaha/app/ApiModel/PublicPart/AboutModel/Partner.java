package com.shamsaha.app.ApiModel.PublicPart.AboutModel;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Partner {

    @SerializedName("pname")
    @Expose
    private String pname;
    @SerializedName("image")
    @Expose
    private String image;
    @SerializedName("website")
    @Expose
    private String website;

    public String getPname() {
        return pname;
    }

    public void setPname(String pname) {
        this.pname = pname;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getWebsite() {
        return website;
    }

    public void setWebsite(String website) {
        this.website = website;
    }

}
