package com.guruvardaan.ghargharsurvey.model;

public class SelfStatementModel {
    int type;
    String start_date, end_date, transaction_date, cid, customerFirstName, fk_acc_cor_service_id, mobile1, mobile2, whatsapp, plotNo, transaction_mode, transaction_amount, empcomment;

    public SelfStatementModel(int type, String start_date, String end_date, String transaction_date, String cid, String customerFirstName, String fk_acc_cor_service_id, String mobile1, String mobile2, String whatsapp, String plotNo, String transaction_mode, String transaction_amount, String empcomment) {
        this.type = type;
        this.start_date = start_date;
        this.end_date = end_date;
        this.transaction_date = transaction_date;
        this.cid = cid;
        this.customerFirstName = customerFirstName;
        this.fk_acc_cor_service_id = fk_acc_cor_service_id;
        this.mobile1 = mobile1;
        this.mobile2 = mobile2;
        this.whatsapp = whatsapp;
        this.plotNo = plotNo;
        this.transaction_mode = transaction_mode;
        this.transaction_amount = transaction_amount;
        this.empcomment = empcomment;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getStart_date() {
        return start_date;
    }

    public void setStart_date(String start_date) {
        this.start_date = start_date;
    }

    public String getEnd_date() {
        return end_date;
    }

    public void setEnd_date(String end_date) {
        this.end_date = end_date;
    }

    public String getTransaction_date() {
        return transaction_date;
    }

    public void setTransaction_date(String transaction_date) {
        this.transaction_date = transaction_date;
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

    public String getFk_acc_cor_service_id() {
        return fk_acc_cor_service_id;
    }

    public void setFk_acc_cor_service_id(String fk_acc_cor_service_id) {
        this.fk_acc_cor_service_id = fk_acc_cor_service_id;
    }

    public String getMobile1() {
        return mobile1;
    }

    public void setMobile1(String mobile1) {
        this.mobile1 = mobile1;
    }

    public String getMobile2() {
        return mobile2;
    }

    public void setMobile2(String mobile2) {
        this.mobile2 = mobile2;
    }

    public String getWhatsapp() {
        return whatsapp;
    }

    public void setWhatsapp(String whatsapp) {
        this.whatsapp = whatsapp;
    }

    public String getPlotNo() {
        return plotNo;
    }

    public void setPlotNo(String plotNo) {
        this.plotNo = plotNo;
    }

    public String getTransaction_mode() {
        return transaction_mode;
    }

    public void setTransaction_mode(String transaction_mode) {
        this.transaction_mode = transaction_mode;
    }

    public String getTransaction_amount() {
        return transaction_amount;
    }

    public void setTransaction_amount(String transaction_amount) {
        this.transaction_amount = transaction_amount;
    }

    public String getEmpcomment() {
        return empcomment;
    }

    public void setEmpcomment(String empcomment) {
        this.empcomment = empcomment;
    }
}
