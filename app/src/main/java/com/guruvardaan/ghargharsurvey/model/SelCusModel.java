package com.guruvardaan.ghargharsurvey.model;

public class SelCusModel {
    String cid, name;

    public SelCusModel(String cid, String name) {
        this.cid = cid;
        this.name = name;
    }

    public String getCid() {
        return cid;
    }

    public void setCid(String cid) {
        this.cid = cid;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
