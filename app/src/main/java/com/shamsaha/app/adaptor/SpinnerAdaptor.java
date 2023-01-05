package com.shamsaha.app.adaptor;

import androidx.annotation.NonNull;

public class SpinnerAdaptor {

    private String Location;
    private String id;

    public SpinnerAdaptor(String location, String id) {
        this.Location = location;
        this.id = id;
    }

    public String getLocation() {
        return Location;
    }

    public void setLocation(String location) {
        Location = location;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    @NonNull
    @Override
    public String toString() {
        return Location;
    }
}
