package com.shamsaha.app.ApiModel;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class job {

    @SerializedName("wcjid")
    @Expose
    private String wcjid;
    @SerializedName("job_id")
    @Expose
    private String jobId;
    @SerializedName("title")
    @Expose
    private String title;
    @SerializedName("job_type")
    @Expose
    private String jobType;
    @SerializedName("brief")
    @Expose
    private String brief;
    @SerializedName("detail")
    @Expose
    private String detail;
    @SerializedName("jdate")
    @Expose
    private String jdate;
    @SerializedName("edate")
    @Expose
    private String edate;

    public String getWcjid() {
        return wcjid;
    }

    public void setWcjid(String wcjid) {
        this.wcjid = wcjid;
    }

    public String getJobId() {
        return jobId;
    }

    public void setJobId(String jobId) {
        this.jobId = jobId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getJobType() {
        return jobType;
    }

    public void setJobType(String jobType) {
        this.jobType = jobType;
    }

    public String getBrief() {
        return brief;
    }

    public void setBrief(String brief) {
        this.brief = brief;
    }

    public String getDetail() {
        return detail;
    }

    public void setDetail(String detail) {
        this.detail = detail;
    }

    public String getJdate() {
        return jdate;
    }

    public void setJdate(String jdate) {
        this.jdate = jdate;
    }

    public String getEdate() {
        return edate;
    }

    public void setEdate(String edate) {
        this.edate = edate;
    }

}
