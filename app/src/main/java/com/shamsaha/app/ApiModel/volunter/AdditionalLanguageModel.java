package com.shamsaha.app.ApiModel.volunter;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class AdditionalLanguageModel {

    @SerializedName("wcl_id")
    @Expose
    private String wclId;
    @SerializedName("lname")
    @Expose
    private String lname;
    @SerializedName("status")
    @Expose
    private String status;

    public String getWclId() {
        return wclId;
    }

    public void setWclId(String wclId) {
        this.wclId = wclId;
    }

    public String getLname() {
        return lname;
    }

    public void setLname(String lname) {
        this.lname = lname;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

}