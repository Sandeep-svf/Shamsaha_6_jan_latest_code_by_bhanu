package com.shamsaha.app.ApiModel.volunter;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class OnDateShift {
    @SerializedName("w_sch_id")
    @Expose
    private String wSchId;
    @SerializedName("schedule_id")
    @Expose
    private String scheduleId;
    @SerializedName("date")
    @Expose
    private String date;
    @SerializedName("volunteer_assign")
    @Expose
    private String volunteerAssign;
    @SerializedName("schedule_status")
    @Expose
    private String scheduleStatus;
    @SerializedName("shift_name")
    @Expose
    private String shiftName;
    @SerializedName("shift_language")
    @Expose
    private String shiftLanguage;
    @SerializedName("color")
    @Expose
    private String color;
    @SerializedName("image")
    @Expose
    private String image;
    @SerializedName("shift_time")
    @Expose
    private String shiftTime;
    @SerializedName("vname")
    @Expose
    private String vname;
    @SerializedName("profile_pic")
    @Expose
    private String profilePic;

    public String getWSchId() {
        return wSchId;
    }

    public void setWSchId(String wSchId) {
        this.wSchId = wSchId;
    }

    public String getScheduleId() {
        return scheduleId;
    }

    public void setScheduleId(String scheduleId) {
        this.scheduleId = scheduleId;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getVolunteerAssign() {
        return volunteerAssign;
    }

    public void setVolunteerAssign(String volunteerAssign) {
        this.volunteerAssign = volunteerAssign;
    }

    public String getScheduleStatus() {
        return scheduleStatus;
    }

    public void setScheduleStatus(String scheduleStatus) {
        this.scheduleStatus = scheduleStatus;
    }

    public String getShiftName() {
        return shiftName;
    }

    public void setShiftName(String shiftName) {
        this.shiftName = shiftName;
    }

    public String getShiftLanguage() {
        return shiftLanguage;
    }

    public void setShiftLanguage(String shiftLanguage) {
        this.shiftLanguage = shiftLanguage;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getShiftTime() {
        return shiftTime;
    }

    public void setShiftTime(String shiftTime) {
        this.shiftTime = shiftTime;
    }

    public String getVname() {
        return vname;
    }

    public void setVname(String vname) {
        this.vname = vname;
    }

    public String getProfilePic() {
        return profilePic;
    }

    public void setProfilePic(String profilePic) {
        this.profilePic = profilePic;
    }
}
