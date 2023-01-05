package com.shamsaha.app.ApiModel.PublicPart.AboutModel;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class BordMembers {

    @SerializedName("bname")
    @Expose
    private String bname;
    @SerializedName("image")
    @Expose
    private String image;
    @SerializedName("designation")
    @Expose
    private String designation;

    public String getBname() {
        return bname;
    }

    public void setBname(String bname) {
        this.bname = bname;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getDesignation() {
        return designation;
    }

    public void setDesignation(String designation) {
        this.designation = designation;
    }


}
