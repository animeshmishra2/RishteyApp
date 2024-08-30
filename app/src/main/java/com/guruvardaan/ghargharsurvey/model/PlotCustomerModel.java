package com.guruvardaan.ghargharsurvey.model;

public class PlotCustomerModel {
    String name, plotName, plotRate, sqft, service_duration, mobile1, total, paid, cPending, lastAmount, dt, registeryStatus, remaining;

    public PlotCustomerModel(String name, String plotName, String plotRate, String sqft, String service_duration, String mobile1, String total, String paid, String cPending, String lastAmount, String dt, String registeryStatus, String remaining) {
        this.name = name;
        this.plotName = plotName;
        this.plotRate = plotRate;
        this.sqft = sqft;
        this.service_duration = service_duration;
        this.mobile1 = mobile1;
        this.total = total;
        this.paid = paid;
        this.cPending = cPending;
        this.lastAmount = lastAmount;
        this.dt = dt;
        this.registeryStatus = registeryStatus;
        this.remaining = remaining;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPlotName() {
        return plotName;
    }

    public void setPlotName(String plotName) {
        this.plotName = plotName;
    }

    public String getPlotRate() {
        return plotRate;
    }

    public void setPlotRate(String plotRate) {
        this.plotRate = plotRate;
    }

    public String getSqft() {
        return sqft;
    }

    public void setSqft(String sqft) {
        this.sqft = sqft;
    }

    public String getService_duration() {
        return service_duration;
    }

    public void setService_duration(String service_duration) {
        this.service_duration = service_duration;
    }

    public String getMobile1() {
        return mobile1;
    }

    public void setMobile1(String mobile1) {
        this.mobile1 = mobile1;
    }

    public String getTotal() {
        return total;
    }

    public void setTotal(String total) {
        this.total = total;
    }

    public String getPaid() {
        return paid;
    }

    public void setPaid(String paid) {
        this.paid = paid;
    }

    public String getcPending() {
        return cPending;
    }

    public void setcPending(String cPending) {
        this.cPending = cPending;
    }

    public String getLastAmount() {
        return lastAmount;
    }

    public void setLastAmount(String lastAmount) {
        this.lastAmount = lastAmount;
    }

    public String getDt() {
        return dt;
    }

    public void setDt(String dt) {
        this.dt = dt;
    }

    public String getRegisteryStatus() {
        return registeryStatus;
    }

    public void setRegisteryStatus(String registeryStatus) {
        this.registeryStatus = registeryStatus;
    }

    public String getRemaining() {
        return remaining;
    }

    public void setRemaining(String remaining) {
        this.remaining = remaining;
    }
}
