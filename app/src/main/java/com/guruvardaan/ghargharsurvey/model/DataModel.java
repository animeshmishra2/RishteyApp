package com.guruvardaan.ghargharsurvey.model;

public class DataModel {
    String id, name, sub_head_id, status, value;

    public DataModel(String id, String name, String sub_head_id, String status, String value) {
        this.id = id;
        this.name = name;
        this.sub_head_id = sub_head_id;
        this.status = status;
        this.value = value;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSub_head_id() {
        return sub_head_id;
    }

    public void setSub_head_id(String sub_head_id) {
        this.sub_head_id = sub_head_id;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }
}
