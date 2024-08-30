package com.guruvardaan.ghargharsurvey.model;

public class AgentCarModel {
    String carName, carNumber, rcImage, loanDocument1, loanDocument2, loanDocument3, status, selected;

    public AgentCarModel(String carName, String carNumber, String rcImage, String loanDocument1, String loanDocument2, String loanDocument3, String status, String selected) {
        this.carName = carName;
        this.carNumber = carNumber;
        this.rcImage = rcImage;
        this.loanDocument1 = loanDocument1;
        this.loanDocument2 = loanDocument2;
        this.loanDocument3 = loanDocument3;
        this.status = status;
        this.selected = selected;
    }

    public String getCarName() {
        return carName;
    }

    public void setCarName(String carName) {
        this.carName = carName;
    }

    public String getCarNumber() {
        return carNumber;
    }

    public void setCarNumber(String carNumber) {
        this.carNumber = carNumber;
    }

    public String getRcImage() {
        return rcImage;
    }

    public void setRcImage(String rcImage) {
        this.rcImage = rcImage;
    }

    public String getLoanDocument1() {
        return loanDocument1;
    }

    public void setLoanDocument1(String loanDocument1) {
        this.loanDocument1 = loanDocument1;
    }

    public String getLoanDocument2() {
        return loanDocument2;
    }

    public void setLoanDocument2(String loanDocument2) {
        this.loanDocument2 = loanDocument2;
    }

    public String getLoanDocument3() {
        return loanDocument3;
    }

    public void setLoanDocument3(String loanDocument3) {
        this.loanDocument3 = loanDocument3;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getSelected() {
        return selected;
    }

    public void setSelected(String selected) {
        this.selected = selected;
    }
}
