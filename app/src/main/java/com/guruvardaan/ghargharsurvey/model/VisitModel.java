package com.guruvardaan.ghargharsurvey.model;

public class VisitModel {
    String id, vehicleType, carNumber, carName, startingAddress, dtOfVisit, submittedDt, remark, starting_latlong, token_amount, km, amountPerKm, totalFare, totalCustomers, status, act_customers;

    public VisitModel(String id, String vehicleType, String carNumber, String carName, String startingAddress, String dtOfVisit, String submittedDt, String remark, String starting_latlong, String token_amount, String km, String amountPerKm, String totalFare, String totalCustomers, String status, String act_customers) {
        this.id = id;
        this.vehicleType = vehicleType;
        this.carNumber = carNumber;
        this.carName = carName;
        this.startingAddress = startingAddress;
        this.dtOfVisit = dtOfVisit;
        this.submittedDt = submittedDt;
        this.remark = remark;
        this.starting_latlong = starting_latlong;
        this.token_amount = token_amount;
        this.km = km;
        this.amountPerKm = amountPerKm;
        this.totalFare = totalFare;
        this.totalCustomers = totalCustomers;
        this.status = status;
        this.act_customers = act_customers;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getVehicleType() {
        return vehicleType;
    }

    public void setVehicleType(String vehicleType) {
        this.vehicleType = vehicleType;
    }

    public String getCarNumber() {
        return carNumber;
    }

    public void setCarNumber(String carNumber) {
        this.carNumber = carNumber;
    }

    public String getCarName() {
        return carName;
    }

    public void setCarName(String carName) {
        this.carName = carName;
    }

    public String getStartingAddress() {
        return startingAddress;
    }

    public void setStartingAddress(String startingAddress) {
        this.startingAddress = startingAddress;
    }

    public String getDtOfVisit() {
        return dtOfVisit;
    }

    public void setDtOfVisit(String dtOfVisit) {
        this.dtOfVisit = dtOfVisit;
    }

    public String getSubmittedDt() {
        return submittedDt;
    }

    public void setSubmittedDt(String submittedDt) {
        this.submittedDt = submittedDt;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public String getStarting_latlong() {
        return starting_latlong;
    }

    public void setStarting_latlong(String starting_latlong) {
        this.starting_latlong = starting_latlong;
    }

    public String getToken_amount() {
        return token_amount;
    }

    public void setToken_amount(String token_amount) {
        this.token_amount = token_amount;
    }

    public String getKm() {
        return km;
    }

    public void setKm(String km) {
        this.km = km;
    }

    public String getAmountPerKm() {
        return amountPerKm;
    }

    public void setAmountPerKm(String amountPerKm) {
        this.amountPerKm = amountPerKm;
    }

    public String getTotalFare() {
        return totalFare;
    }

    public void setTotalFare(String totalFare) {
        this.totalFare = totalFare;
    }

    public String getTotalCustomers() {
        return totalCustomers;
    }

    public void setTotalCustomers(String totalCustomers) {
        this.totalCustomers = totalCustomers;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getAct_customers() {
        return act_customers;
    }

    public void setAct_customers(String act_customers) {
        this.act_customers = act_customers;
    }
}
