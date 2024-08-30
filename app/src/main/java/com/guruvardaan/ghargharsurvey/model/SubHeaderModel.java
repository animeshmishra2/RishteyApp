package com.guruvardaan.ghargharsurvey.model;

public class SubHeaderModel {
    String id, header_id, sub_head_name, priority, status;

    public SubHeaderModel(String id, String header_id, String sub_head_name, String priority, String status) {
        this.id = id;
        this.header_id = header_id;
        this.sub_head_name = sub_head_name;
        this.priority = priority;
        this.status = status;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getHeader_id() {
        return header_id;
    }

    public void setHeader_id(String header_id) {
        this.header_id = header_id;
    }

    public String getSub_head_name() {
        return sub_head_name;
    }

    public void setSub_head_name(String sub_head_name) {
        this.sub_head_name = sub_head_name;
    }

    public String getPriority() {
        return priority;
    }

    public void setPriority(String priority) {
        this.priority = priority;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
