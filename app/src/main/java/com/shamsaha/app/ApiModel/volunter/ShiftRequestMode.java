package com.shamsaha.app.ApiModel.volunter;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ShiftRequestMode {

    @SerializedName("shift_language")
    @Expose
    private String shiftLanguage;
    @SerializedName("shift_name")
    @Expose
    private String shiftName;
    @SerializedName("color")
    @Expose
    private String color;
    @SerializedName("shift_time")
    @Expose
    private String shiftTime;
    @SerializedName("image")
    @Expose
    private String image;
    @SerializedName("w_sch_id")
    @Expose
    private String wSchId;
    @SerializedName("schedule_id")
    @Expose
    private String scheduleId;
    @SerializedName("shift_id")
    @Expose
    private String shiftId;
    @SerializedName("date")
    @Expose
    private String date;
    @SerializedName("volunteer_assign")
    @Expose
    private String volunteerAssign;
    @SerializedName("accept_time")
    @Expose
    private String acceptTime;
    @SerializedName("requested_by")
    @Expose
    private String requestedBy;
    @SerializedName("requested_date_time")
    @Expose
    private String requestedDateTime;
    @SerializedName("request_end_time")
    @Expose
    private String requestEndTime;
    @SerializedName("schedule_status")
    @Expose
    private String scheduleStatus;
    @SerializedName("created_at")
    @Expose
    private String createdAt;

    @SerializedName("status")
    @Expose
    private String status;
    @SerializedName("message")
    @Expose
    private String message;

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getShiftLanguage() {
        return shiftLanguage;
    }

    public void setShiftLanguage(String shiftLanguage) {
        this.shiftLanguage = shiftLanguage;
    }

    public String getShiftName() {
        return shiftName;
    }

    public void setShiftName(String shiftName) {
        this.shiftName = shiftName;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public String getShiftTime() {
        return shiftTime;
    }

    public void setShiftTime(String shiftTime) {
        this.shiftTime = shiftTime;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

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

    public String getShiftId() {
        return shiftId;
    }

    public void setShiftId(String shiftId) {
        this.shiftId = shiftId;
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

    public String getAcceptTime() {
        return acceptTime;
    }

    public void setAcceptTime(String acceptTime) {
        this.acceptTime = acceptTime;
    }

    public String getRequestedBy() {
        return requestedBy;
    }

    public void setRequestedBy(String requestedBy) {
        this.requestedBy = requestedBy;
    }

    public String getRequestedDateTime() {
        return requestedDateTime;
    }

    public void setRequestedDateTime(String requestedDateTime) {
        this.requestedDateTime = requestedDateTime;
    }

    public String getRequestEndTime() {
        return requestEndTime;
    }

    public void setRequestEndTime(String requestEndTime) {
        this.requestEndTime = requestEndTime;
    }

    public String getScheduleStatus() {
        return scheduleStatus;
    }

    public void setScheduleStatus(String scheduleStatus) {
        this.scheduleStatus = scheduleStatus;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }


}
