package com.guruvardaan.ghargharsurvey.model;

import java.io.Serializable;

public class HeaderModel implements Serializable {
    String id, header_name, header_description, status, department_id, department_name, sel;

    public HeaderModel(String id, String header_name, String header_description, String status, String department_id, String department_name, String sel) {
        this.id = id;
        this.header_name = header_name;
        this.header_description = header_description;
        this.status = status;
        this.department_id = department_id;
        this.department_name = department_name;
        this.sel = sel;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getHeader_name() {
        return header_name;
    }

    public void setHeader_name(String header_name) {
        this.header_name = header_name;
    }

    public String getHeader_description() {
        return header_description;
    }

    public void setHeader_description(String header_description) {
        this.header_description = header_description;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getDepartment_id() {
        return department_id;
    }

    public void setDepartment_id(String department_id) {
        this.department_id = department_id;
    }

    public String getDepartment_name() {
        return department_name;
    }

    public void setDepartment_name(String department_name) {
        this.department_name = department_name;
    }

    public String getSel() {
        return sel;
    }

    public void setSel(String sel) {
        this.sel = sel;
    }
}
