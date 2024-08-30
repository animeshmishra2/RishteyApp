package com.guruvardaan.ghargharsurvey.model;

public class CustPassModel {
    String installment_no, installment_amount, due_date, paid_amount, paid_date, status;

    public CustPassModel(String installment_no, String installment_amount, String due_date, String paid_amount, String paid_date, String status) {
        this.installment_no = installment_no;
        this.installment_amount = installment_amount;
        this.due_date = due_date;
        this.paid_amount = paid_amount;
        this.paid_date = paid_date;
        this.status = status;
    }

    public String getInstallment_no() {
        return installment_no;
    }

    public void setInstallment_no(String installment_no) {
        this.installment_no = installment_no;
    }

    public String getInstallment_amount() {
        return installment_amount;
    }

    public void setInstallment_amount(String installment_amount) {
        this.installment_amount = installment_amount;
    }

    public String getDue_date() {
        return due_date;
    }

    public void setDue_date(String due_date) {
        this.due_date = due_date;
    }

    public String getPaid_amount() {
        return paid_amount;
    }

    public void setPaid_amount(String paid_amount) {
        this.paid_amount = paid_amount;
    }

    public String getPaid_date() {
        return paid_date;
    }

    public void setPaid_date(String paid_date) {
        this.paid_date = paid_date;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
