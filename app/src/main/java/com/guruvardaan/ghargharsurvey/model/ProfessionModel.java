package com.guruvardaan.ghargharsurvey.model;

public class ProfessionModel {
    String id, occupationName;

    public ProfessionModel(String id, String occupationName) {
        this.id = id;
        this.occupationName = occupationName;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getOccupationName() {
        return occupationName;
    }

    public void setOccupationName(String occupationName) {
        this.occupationName = occupationName;
    }
}
