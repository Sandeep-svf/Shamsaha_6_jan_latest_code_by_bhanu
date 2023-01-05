package com.shamsaha.app.ApiModel.PublicPart.AboutModel;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Who_we_are {
    @SerializedName("title_en")
    @Expose
    private String titleEn;
    @SerializedName("content_en")
    @Expose
    private String contentEn;
    @SerializedName("title_ar")
    @Expose
    private String titleAr;
    @SerializedName("content_ar")
    @Expose
    private String contentAr;
    @SerializedName("team1")
    @Expose
    private String team1;
    @SerializedName("team2")
    @Expose
    private String team2;
    @SerializedName("team_a_info")
    @Expose
    private String teamAInfo;
    @SerializedName("team_b_info")
    @Expose
    private String teamBInfo;
    @SerializedName("team_a_name")
    @Expose
    private String teamAName;
    @SerializedName("team_b_name")
    @Expose
    private String teamBName;

    public String getTitleEn() {
        return titleEn;
    }

    public void setTitleEn(String titleEn) {
        this.titleEn = titleEn;
    }

    public String getContentEn() {
        return contentEn;
    }

    public void setContentEn(String contentEn) {
        this.contentEn = contentEn;
    }

    public String getTitleAr() {
        return titleAr;
    }

    public void setTitleAr(String titleAr) {
        this.titleAr = titleAr;
    }

    public String getContentAr() {
        return contentAr;
    }

    public void setContentAr(String contentAr) {
        this.contentAr = contentAr;
    }

    public String getTeam1() {
        return team1;
    }

    public void setTeam1(String team1) {
        this.team1 = team1;
    }

    public String getTeam2() {
        return team2;
    }

    public void setTeam2(String team2) {
        this.team2 = team2;
    }

    public String getTeamAInfo() {
        return teamAInfo;
    }

    public void setTeamAInfo(String teamAInfo) {
        this.teamAInfo = teamAInfo;
    }

    public String getTeamBInfo() {
        return teamBInfo;
    }

    public void setTeamBInfo(String teamBInfo) {
        this.teamBInfo = teamBInfo;
    }

    public String getTeamAName() {
        return teamAName;
    }

    public void setTeamAName(String teamAName) {
        this.teamAName = teamAName;
    }

    public String getTeamBName() {
        return teamBName;
    }

    public void setTeamBName(String teamBName) {
        this.teamBName = teamBName;
    }

}