package com.shamsaha.app.ApiModel.Victem;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class CaseModel {

    @SerializedName("case_id")
    @Expose
    private String caseId;
    @SerializedName("status")
    @Expose
    private String status;
    @SerializedName("message")
    @Expose
    private String message;
    @SerializedName("volunteer")
    @Expose
    private VolunteerOnline volunteer = null;

    public String getCaseId() {
        return caseId;
    }

    public void setCaseId(String caseId) {
        this.caseId = caseId;
    }

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

    public VolunteerOnline getVolunteer() {
        return volunteer;
    }

    public void setVolunteer(VolunteerOnline volunteer) {
        this.volunteer = volunteer;
    }

}
