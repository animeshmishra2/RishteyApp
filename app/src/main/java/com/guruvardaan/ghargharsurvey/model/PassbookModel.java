package com.guruvardaan.ghargharsurvey.model;

public class PassbookModel {
    String id, dt, amount, details, creditType, pay;

    public PassbookModel(String id, String dt, String amount, String details, String creditType, String pay) {
        this.id = id;
        this.dt = dt;
        this.amount = amount;
        this.details = details;
        this.creditType = creditType;
        this.pay = pay;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getDt() {
        return dt;
    }

    public void setDt(String dt) {
        this.dt = dt;
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public String getDetails() {
        return details;
    }

    public void setDetails(String details) {
        this.details = details;
    }

    public String getCreditType() {
        return creditType;
    }

    public void setCreditType(String creditType) {
        this.creditType = creditType;
    }

    public String getPay() {
        return pay;
    }

    public void setPay(String pay) {
        this.pay = pay;
    }
}
