package com.guruvardaan.ghargharsurvey.model;

import java.util.ArrayList;

public class NewChatModel {
    String id, first_name, second_name, type, message, show;
    ArrayList<HeaderModel> headerModels;
    ArrayList<SubHeaderModel> subHeaderModels;
    long times;

    public NewChatModel(String id, String first_name, String second_name, String type, String message, String show, long times, ArrayList<HeaderModel> headerModels, ArrayList<SubHeaderModel> subHeaderModels) {
        this.id = id;
        this.first_name = first_name;
        this.second_name = second_name;
        this.type = type;
        this.message = message;
        this.show = show;
        this.headerModels = headerModels;
        this.times = times;
        this.subHeaderModels = subHeaderModels;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getFirst_name() {
        return first_name;
    }

    public void setFirst_name(String first_name) {
        this.first_name = first_name;
    }

    public String getSecond_name() {
        return second_name;
    }

    public void setSecond_name(String second_name) {
        this.second_name = second_name;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public ArrayList<HeaderModel> getHeaderModels() {
        return headerModels;
    }

    public void setHeaderModels(ArrayList<HeaderModel> headerModels) {
        this.headerModels = headerModels;
    }

    public String getShow() {
        return show;
    }

    public void setShow(String show) {
        this.show = show;
    }

    public long getTimes() {
        return times;
    }

    public void setTimes(long times) {
        this.times = times;
    }

    public ArrayList<SubHeaderModel> getSubHeaderModels() {
        return subHeaderModels;
    }

    public void setSubHeaderModels(ArrayList<SubHeaderModel> subHeaderModels) {
        this.subHeaderModels = subHeaderModels;
    }
}
