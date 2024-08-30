package com.guruvardaan.ghargharsurvey.model;

public class RegistryModel {
    String id, advisorId, advisor_name, memberId, customerFirstName, dt, requestDt1, requestDt2, requestDt3, paymentMode, status, aadharFront, aadharBack, Pan, photo, cheque1, cheque2;

    public RegistryModel(String id, String advisorId, String advisor_name, String memberId, String customerFirstName, String dt, String requestDt1, String requestDt2, String requestDt3, String paymentMode, String status, String aadharFront, String aadharBack, String pan, String photo, String cheque1, String cheque2) {
        this.id = id;
        this.advisorId = advisorId;
        this.advisor_name = advisor_name;
        this.memberId = memberId;
        this.customerFirstName = customerFirstName;
        this.dt = dt;
        this.requestDt1 = requestDt1;
        this.requestDt2 = requestDt2;
        this.requestDt3 = requestDt3;
        this.paymentMode = paymentMode;
        this.status = status;
        this.aadharFront = aadharFront;
        this.aadharBack = aadharBack;
        Pan = pan;
        this.photo = photo;
        this.cheque1 = cheque1;
        this.cheque2 = cheque2;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getAdvisorId() {
        return advisorId;
    }

    public void setAdvisorId(String advisorId) {
        this.advisorId = advisorId;
    }

    public String getAdvisor_name() {
        return advisor_name;
    }

    public void setAdvisor_name(String advisor_name) {
        this.advisor_name = advisor_name;
    }

    public String getMemberId() {
        return memberId;
    }

    public void setMemberId(String memberId) {
        this.memberId = memberId;
    }

    public String getCustomerFirstName() {
        return customerFirstName;
    }

    public void setCustomerFirstName(String customerFirstName) {
        this.customerFirstName = customerFirstName;
    }

    public String getDt() {
        return dt;
    }

    public void setDt(String dt) {
        this.dt = dt;
    }

    public String getRequestDt1() {
        return requestDt1;
    }

    public void setRequestDt1(String requestDt1) {
        this.requestDt1 = requestDt1;
    }

    public String getRequestDt2() {
        return requestDt2;
    }

    public void setRequestDt2(String requestDt2) {
        this.requestDt2 = requestDt2;
    }

    public String getRequestDt3() {
        return requestDt3;
    }

    public void setRequestDt3(String requestDt3) {
        this.requestDt3 = requestDt3;
    }

    public String getPaymentMode() {
        return paymentMode;
    }

    public void setPaymentMode(String paymentMode) {
        this.paymentMode = paymentMode;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getAadharFront() {
        return aadharFront;
    }

    public void setAadharFront(String aadharFront) {
        this.aadharFront = aadharFront;
    }

    public String getAadharBack() {
        return aadharBack;
    }

    public void setAadharBack(String aadharBack) {
        this.aadharBack = aadharBack;
    }

    public String getPan() {
        return Pan;
    }

    public void setPan(String pan) {
        Pan = pan;
    }

    public String getPhoto() {
        return photo;
    }

    public void setPhoto(String photo) {
        this.photo = photo;
    }

    public String getCheque1() {
        return cheque1;
    }

    public void setCheque1(String cheque1) {
        this.cheque1 = cheque1;
    }

    public String getCheque2() {
        return cheque2;
    }

    public void setCheque2(String cheque2) {
        this.cheque2 = cheque2;
    }
}
