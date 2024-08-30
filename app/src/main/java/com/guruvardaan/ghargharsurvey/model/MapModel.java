package com.guruvardaan.ghargharsurvey.model;

public class MapModel {
    String lat, ins_date;

    public MapModel(String lat, String ins_date) {
        this.lat = lat;
        this.ins_date = ins_date;
    }

    public String getLat() {
        return lat;
    }

    public void setLat(String lat) {
        this.lat = lat;
    }

    public String getIns_date() {
        return ins_date;
    }

    public void setIns_date(String ins_date) {
        this.ins_date = ins_date;
    }
}
