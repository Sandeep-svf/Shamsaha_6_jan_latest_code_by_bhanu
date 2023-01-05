package com.shamsaha.app.adaptor;

public class spinData {
    private String wcrid;
    private String location_name;

    public spinData(String wcrid, String location_name) {
        this.wcrid = wcrid;
        this.location_name = location_name;
    }

    public String getWcrid() {
        return wcrid;
    }

    public void setWcrid(String wcrid) {
        this.wcrid = wcrid;
    }

    public String getLocation_name() {
        return location_name;
    }

    public void setLocation_name(String location_name) {
        this.location_name = location_name;
    }

    @Override
    public String toString() {
        return location_name;
    }
}
