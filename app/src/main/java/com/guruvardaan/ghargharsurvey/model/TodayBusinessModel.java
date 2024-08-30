package com.guruvardaan.ghargharsurvey.model;

public class TodayBusinessModel {
    String fk_acc_adm_advisor_id, transaction_date, cid, customerFirstName, fk_acc_cor_service_id, mobile1, plotNo, transaction_mode, transaction_amount;

    public TodayBusinessModel(String fk_acc_adm_advisor_id, String transaction_date, String cid, String customerFirstName, String fk_acc_cor_service_id, String mobile1, String plotNo, String transaction_mode, String transaction_amount) {
        this.fk_acc_adm_advisor_id = fk_acc_adm_advisor_id;
        this.transaction_date = transaction_date;
        this.cid = cid;
        this.customerFirstName = customerFirstName;
        this.fk_acc_cor_service_id = fk_acc_cor_service_id;
        this.mobile1 = mobile1;
        this.plotNo = plotNo;
        this.transaction_mode = transaction_mode;
        this.transaction_amount = transaction_amount;
    }

    public String getFk_acc_adm_advisor_id() {
        return fk_acc_adm_advisor_id;
    }

    public void setFk_acc_adm_advisor_id(String fk_acc_adm_advisor_id) {
        this.fk_acc_adm_advisor_id = fk_acc_adm_advisor_id;
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
}
