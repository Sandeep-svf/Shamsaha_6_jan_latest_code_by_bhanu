package com.shamsaha.app.ApiModel;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class resource {

    @SerializedName("wcresid")
    @Expose
    private String wcresid;
    @SerializedName("res_loc_id")
    @Expose
    private String resLocId;
    @SerializedName("res_res_cat_id")
    @Expose
    private String resResCatId;
    @SerializedName("content")
    @Expose
    private String content;
    @SerializedName("contact_info1")
    @Expose
    private String contactInfo1;
    @SerializedName("contact_info2")
    @Expose
    private String contactInfo2;
    @SerializedName("contact_info3")
    @Expose
    private String contactInfo3;
    @SerializedName("contact_info4")
    @Expose
    private String contactInfo4;
    @SerializedName("email_info1")
    @Expose
    private String emailInfo1;
    @SerializedName("email_info2")
    @Expose
    private String emailInfo2;
    @SerializedName("email_info3")
    @Expose
    private String emailInfo3;
    @SerializedName("address_info")
    @Expose
    private String addressInfo;
    @SerializedName("web_info1")
    @Expose
    private String webInfo1;
    @SerializedName("web_info2")
    @Expose
    private String webInfo2;
    @SerializedName("name")
    @Expose
    private String name;
    @SerializedName("timings")
    @Expose
    private String timings;

    public String getWcresid() {
        return wcresid;
    }

    public void setWcresid(String wcresid) {
        this.wcresid = wcresid;
    }

    public String getResLocId() {
        return resLocId;
    }

    public void setResLocId(String resLocId) {
        this.resLocId = resLocId;
    }

    public String getResResCatId() {
        return resResCatId;
    }

    public void setResResCatId(String resResCatId) {
        this.resResCatId = resResCatId;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getContactInfo1() {
        return contactInfo1;
    }

    public void setContactInfo1(String contactInfo1) {
        this.contactInfo1 = contactInfo1;
    }

    public String getContactInfo2() {
        return contactInfo2;
    }

    public void setContactInfo2(String contactInfo2) {
        this.contactInfo2 = contactInfo2;
    }

    public String getContactInfo3() {
        return contactInfo3;
    }

    public void setContactInfo3(String contactInfo3) {
        this.contactInfo3 = contactInfo3;
    }

    public String getContactInfo4() {
        return contactInfo4;
    }

    public void setContactInfo4(String contactInfo4) {
        this.contactInfo4 = contactInfo4;
    }

    public String getEmailInfo1() {
        return emailInfo1;
    }

    public void setEmailInfo1(String emailInfo1) {
        this.emailInfo1 = emailInfo1;
    }

    public String getEmailInfo2() {
        return emailInfo2;
    }

    public void setEmailInfo2(String emailInfo2) {
        this.emailInfo2 = emailInfo2;
    }

    public String getEmailInfo3() {
        return emailInfo3;
    }

    public void setEmailInfo3(String emailInfo3) {
        this.emailInfo3 = emailInfo3;
    }

    public String getAddressInfo() {
        return addressInfo;
    }

    public void setAddressInfo(String addressInfo) {
        this.addressInfo = addressInfo;
    }

    public String getWebInfo1() {
        return webInfo1;
    }

    public void setWebInfo1(String webInfo1) {
        this.webInfo1 = webInfo1;
    }

    public String getWebInfo2() {
        return webInfo2;
    }

    public void setWebInfo2(String webInfo2) {
        this.webInfo2 = webInfo2;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getTimings() {
        return timings;
    }

    public void setTimings(String timings) {
        this.timings = timings;
    }

}
