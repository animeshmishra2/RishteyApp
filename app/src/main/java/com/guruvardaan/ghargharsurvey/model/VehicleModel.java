package com.guruvardaan.ghargharsurvey.model;

import java.io.Serializable;

public class VehicleModel implements Serializable {
    String id, vehicle_name, status;
    int img;

    public VehicleModel(String id, String vehicle_name, String status, int img) {
        this.id = id;
        this.vehicle_name = vehicle_name;
        this.status = status;
        this.img = img;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getVehicle_name() {
        return vehicle_name;
    }

    public void setVehicle_name(String vehicle_name) {
        this.vehicle_name = vehicle_name;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public int getImg() {
        return img;
    }

    public void setImg(int img) {
        this.img = img;
    }
}
