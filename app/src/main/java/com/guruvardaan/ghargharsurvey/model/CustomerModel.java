package com.guruvardaan.ghargharsurvey.model;

import java.io.Serializable;

public class CustomerModel implements Serializable {
    String name, mobile, profession, occupation, address, pr_id, occ_id;

    public CustomerModel(String name, String mobile, String profession, String occupation, String address, String pr_id, String occ_id) {
        this.name = name;
        this.mobile = mobile;
        this.profession = profession;
        this.occupation = occupation;
        this.address = address;
        this.pr_id = pr_id;
        this.occ_id = occ_id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getProfession() {
        return profession;
    }

    public void setProfession(String profession) {
        this.profession = profession;
    }

    public String getOccupation() {
        return occupation;
    }

    public void setOccupation(String occupation) {
        this.occupation = occupation;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getPr_id() {
        return pr_id;
    }

    public void setPr_id(String pr_id) {
        this.pr_id = pr_id;
    }

    public String getOcc_id() {
        return occ_id;
    }

    public void setOcc_id(String occ_id) {
        this.occ_id = occ_id;
    }
}
