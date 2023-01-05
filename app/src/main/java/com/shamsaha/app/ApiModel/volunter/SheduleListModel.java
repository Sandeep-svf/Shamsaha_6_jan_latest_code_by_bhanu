package com.shamsaha.app.ApiModel.volunter;

import org.json.JSONArray;

import java.util.List;

public class SheduleListModel {

    private String Date;
    private List<JSONArray> JsonArray;

    public SheduleListModel(String date, List<JSONArray> jsonArray) {
        Date = date;
        JsonArray = jsonArray;
    }

    public String getDate() {
        return Date;
    }

    public void setDate(String date) {
        Date = date;
    }

    public List<JSONArray> getJsonArray() {
        return JsonArray;
    }

    public void setJsonArray(List<JSONArray> jsonArray) {
        JsonArray = jsonArray;
    }
}
