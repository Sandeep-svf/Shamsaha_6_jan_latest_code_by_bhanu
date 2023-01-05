package com.shamsaha.app.adaptor;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Datum {

    @SerializedName("wc_vid")
    @Expose
    private String wcVid;
    @SerializedName("vounter_id")
    @Expose
    private String vounterId;
    @SerializedName("vname")
    @Expose
    private String vname;
    @SerializedName("vemail")
    @Expose
    private String vemail;
    @SerializedName("vmobile")
    @Expose
    private String vmobile;
    @SerializedName("dob")
    @Expose
    private String dob;
    @SerializedName("doj")
    @Expose
    private String doj;
    @SerializedName("profile_pic")
    @Expose
    private String profilePic;
    @SerializedName("gender")
    @Expose
    private String gender;
    @SerializedName("shift_language")
    @Expose
    private String shiftLanguage;
    @SerializedName("language_known")
    @Expose
    private String languageKnown;
    @SerializedName("address")
    @Expose
    private String address;
    @SerializedName("total_rewards")
    @Expose
    private String totalRewards;
    @SerializedName("status")
    @Expose
    private String status;
    @SerializedName("password_login_first")
    @Expose
    private String passwordLoginFirst;

    public String getWcVid() {
        return wcVid;
    }

    public void setWcVid(String wcVid) {
        this.wcVid = wcVid;
    }

    public String getVounterId() {
        return vounterId;
    }

    public void setVounterId(String vounterId) {
        this.vounterId = vounterId;
    }

    public String getVname() {
        return vname;
    }

    public void setVname(String vname) {
        this.vname = vname;
    }

    public String getVemail() {
        return vemail;
    }

    public void setVemail(String vemail) {
        this.vemail = vemail;
    }

    public String getVmobile() {
        return vmobile;
    }

    public void setVmobile(String vmobile) {
        this.vmobile = vmobile;
    }

    public String getDob() {
        return dob;
    }

    public void setDob(String dob) {
        this.dob = dob;
    }

    public String getDoj() {
        return doj;
    }

    public void setDoj(String doj) {
        this.doj = doj;
    }

    public String getProfilePic() {
        return profilePic;
    }

    public void setProfilePic(String profilePic) {
        this.profilePic = profilePic;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getShiftLanguage() {
        return shiftLanguage;
    }

    public void setShiftLanguage(String shiftLanguage) {
        this.shiftLanguage = shiftLanguage;
    }

    public String getLanguageKnown() {
        return languageKnown;
    }

    public void setLanguageKnown(String languageKnown) {
        this.languageKnown = languageKnown;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getTotalRewards() {
        return totalRewards;
    }

    public void setTotalRewards(String totalRewards) {
        this.totalRewards = totalRewards;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getPasswordLoginFirst() {
        return passwordLoginFirst;
    }

    public void setPasswordLoginFirst(String passwordLoginFirst) {
        this.passwordLoginFirst = passwordLoginFirst;
    }
}
