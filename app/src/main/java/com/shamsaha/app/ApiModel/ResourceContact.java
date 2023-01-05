package com.shamsaha.app.ApiModel;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ResourceContact {

    @SerializedName("name")
    @Expose
    private String name;
    @SerializedName("contact_info")
    @Expose
    private String contactInfo;
    @SerializedName("email_info")
    @Expose
    private String emailInfo;
    @SerializedName("address_info")
    @Expose
    private String addressInfo;
    @SerializedName("web_info")
    @Expose
    private String webInfo;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getContactInfo() {
        return contactInfo;
    }

    public void setContactInfo(String contactInfo) {
        this.contactInfo = contactInfo;
    }

    public String getEmailInfo() {
        return emailInfo;
    }

    public void setEmailInfo(String emailInfo) {
        this.emailInfo = emailInfo;
    }

    public String getAddressInfo() {
        return addressInfo;
    }

    public void setAddressInfo(String addressInfo) {
        this.addressInfo = addressInfo;
    }

    public String getWebInfo() {
        return webInfo;
    }

    public void setWebInfo(String webInfo) {
        this.webInfo = webInfo;
    }


}
