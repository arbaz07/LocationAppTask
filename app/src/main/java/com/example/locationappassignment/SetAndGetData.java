package com.example.locationappassignment;

public class SetAndGetData {

    private static SetAndGetData setAndGetData = null;
    double lat, lng;

    public SetAndGetData() {
    }

    public double getLat() {
        return lat;
    }

    public void setLat(double lat) {
        this.lat = lat;
    }

    public double getLng() {
        return lng;
    }

    public void setLng(double lng) {
        this.lng = lng;
    }

    public static SetAndGetData getInstance() {
        if (setAndGetData == null)
            setAndGetData = new SetAndGetData();

        return setAndGetData;
    }
}

