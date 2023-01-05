package com.shamsaha.app.ApiModel;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class ChannelListModel {

    @SerializedName("status")
    @Expose
    private Integer status;
    @SerializedName("message")
    @Expose
    private String message;
    @SerializedName("data")
    @Expose
    private ArrayList<Datum> data = null;

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public ArrayList<Datum> getData() {
        return data;
    }

    public void setData(ArrayList<Datum> data) {
        this.data = data;
    }

    public static class Datum {

        @SerializedName("victim_id")
        @Expose
        private String victimId;
        @SerializedName("language")
        @Expose
        private String language;
        @SerializedName("status")
        @Expose
        private String status;
        @SerializedName("screen_name")
        @Expose
        private String screenName;
        @SerializedName("are_you_in_crisis")
        @Expose
        private String areYouInCrisis;
        @SerializedName("age")
        @Expose
        private String age;
        @SerializedName("gender")
        @Expose
        private String gender;
        @SerializedName("race_or_ethnicity")
        @Expose
        private String raceOrEthnicity;
        @SerializedName("hear_about_us")
        @Expose
        private String hearAboutUs;
        @SerializedName("case_id")
        @Expose
        private String caseId;
        @SerializedName("yesterday_date")
        @Expose
        private String yesterdayDate;
        @Expose
        private String connection_type;

        private String reported_date;

        public String getVictimId() {
            return victimId;
        }

        public void setVictimId(String victimId) {
            this.victimId = victimId;
        }

        public String getLanguage() {
            return language;
        }

        public void setLanguage(String language) {
            this.language = language;
        }

        public String getStatus() {
            return status;
        }

        public void setStatus(String status) {
            this.status = status;
        }

        public String getScreenName() {
            return screenName;
        }

        public void setScreenName(String screenName) {
            this.screenName = screenName;
        }

        public String getAreYouInCrisis() {
            return areYouInCrisis;
        }

        public void setAreYouInCrisis(String areYouInCrisis) {
            this.areYouInCrisis = areYouInCrisis;
        }

        public String getAge() {
            return age;
        }

        public void setAge(String age) {
            this.age = age;
        }

        public String getGender() {
            return gender;
        }

        public void setGender(String gender) {
            this.gender = gender;
        }

        public String getRaceOrEthnicity() {
            return raceOrEthnicity;
        }

        public void setRaceOrEthnicity(String raceOrEthnicity) {
            this.raceOrEthnicity = raceOrEthnicity;
        }

        public String getHearAboutUs() {
            return hearAboutUs;
        }

        public void setHearAboutUs(String hearAboutUs) {
            this.hearAboutUs = hearAboutUs;
        }

        public String getCaseId() {
            return caseId;
        }

        public void setCaseId(String caseId) {
            this.caseId = caseId;
        }

        public String getYesterdayDate() {
            return yesterdayDate;
        }

        public void setYesterdayDate(String yesterdayDate) {
            this.yesterdayDate = yesterdayDate;
        }

        public String getConnection_type() {
            return connection_type;
        }

        public void setConnection_type(String connection_type) {
            this.connection_type = connection_type;
        }

        public String getReported_date() {
            return reported_date;
        }

        public void setReported_date(String reported_date) {
            this.reported_date = reported_date;
        }
    }
}
