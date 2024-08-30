package com.guruvardaan.ghargharsurvey.model;


public class GooglePlaceModel {
    String mainText;
    String secondText;
    String placeID;

    public GooglePlaceModel(String mainText, String secondText, String placeID) {
        this.mainText = mainText;
        this.secondText = secondText;
        this.placeID = placeID;
    }

    public String getMainText() {
        return mainText;
    }

    public void setMainText(String mainText) {
        this.mainText = mainText;
    }

    public String getSecondText() {
        return secondText;
    }

    public void setSecondText(String secondText) {
        this.secondText = secondText;
    }

    public String getPlaceID() {
        return placeID;
    }

    public void setPlaceID(String placeID) {
        this.placeID = placeID;
    }
}
