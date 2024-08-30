package com.guruvardaan.ghargharsurvey.model;

public class ChequeStatusModel {
    String id, chequeNo, bank_name, branch, chequeDt, amount, cid, customerFirstName, advisorId, advisor_name, userComment, empComment, empins_date, empins_time, username, chequeImage, paymentSlip, ins_date, ChequeStatus;


    public ChequeStatusModel(String id, String chequeNo, String bank_name, String branch, String chequeDt, String amount, String cid, String customerFirstName, String advisorId, String advisor_name, String userComment, String empComment, String empins_date, String empins_time, String username, String chequeImage, String paymentSlip, String ins_date, String chequeStatus) {
        this.id = id;
        this.chequeNo = chequeNo;
        this.bank_name = bank_name;
        this.branch = branch;
        this.chequeDt = chequeDt;
        this.amount = amount;
        this.cid = cid;
        this.customerFirstName = customerFirstName;
        this.advisorId = advisorId;
        this.advisor_name = advisor_name;
        this.userComment = userComment;
        this.empComment = empComment;
        this.empins_date = empins_date;
        this.empins_time = empins_time;
        this.username = username;
        this.chequeImage = chequeImage;
        this.paymentSlip = paymentSlip;
        this.ins_date = ins_date;
        ChequeStatus = chequeStatus;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getChequeNo() {
        return chequeNo;
    }

    public void setChequeNo(String chequeNo) {
        this.chequeNo = chequeNo;
    }

    public String getBank_name() {
        return bank_name;
    }

    public void setBank_name(String bank_name) {
        this.bank_name = bank_name;
    }

    public String getBranch() {
        return branch;
    }

    public void setBranch(String branch) {
        this.branch = branch;
    }

    public String getChequeDt() {
        return chequeDt;
    }

    public void setChequeDt(String chequeDt) {
        this.chequeDt = chequeDt;
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public String getCid() {
        return cid;
    }

    public void setCid(String cid) {
        this.cid = cid;
    }

    public String getCustomerFirstName() {
        return customerFirstName;
    }

    public void setCustomerFirstName(String customerFirstName) {
        this.customerFirstName = customerFirstName;
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

    public String getUserComment() {
        return userComment;
    }

    public void setUserComment(String userComment) {
        this.userComment = userComment;
    }

    public String getEmpComment() {
        return empComment;
    }

    public void setEmpComment(String empComment) {
        this.empComment = empComment;
    }

    public String getEmpins_date() {
        return empins_date;
    }

    public void setEmpins_date(String empins_date) {
        this.empins_date = empins_date;
    }

    public String getEmpins_time() {
        return empins_time;
    }

    public void setEmpins_time(String empins_time) {
        this.empins_time = empins_time;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getChequeImage() {
        return chequeImage;
    }

    public void setChequeImage(String chequeImage) {
        this.chequeImage = chequeImage;
    }

    public String getPaymentSlip() {
        return paymentSlip;
    }

    public void setPaymentSlip(String paymentSlip) {
        this.paymentSlip = paymentSlip;
    }

    public String getIns_date() {
        return ins_date;
    }

    public void setIns_date(String ins_date) {
        this.ins_date = ins_date;
    }

    public String getChequeStatus() {
        return ChequeStatus;
    }

    public void setChequeStatus(String chequeStatus) {
        ChequeStatus = chequeStatus;
    }
}
