package com.guruvardaan.ghargharsurvey.model;

public class UnpaidVoucherModel {
    String pk_acc_adm_advisor_id, fk_acc_adm_advisor_id, voucher_dateFrom, voucher_dateTo, voucher_generated_date, payableAmount;

    public UnpaidVoucherModel(String pk_acc_adm_advisor_id, String fk_acc_adm_advisor_id, String voucher_dateFrom, String voucher_dateTo, String voucher_generated_date, String payableAmount) {
        this.pk_acc_adm_advisor_id = pk_acc_adm_advisor_id;
        this.fk_acc_adm_advisor_id = fk_acc_adm_advisor_id;
        this.voucher_dateFrom = voucher_dateFrom;
        this.voucher_dateTo = voucher_dateTo;
        this.voucher_generated_date = voucher_generated_date;
        this.payableAmount = payableAmount;
    }

    public String getPk_acc_adm_advisor_id() {
        return pk_acc_adm_advisor_id;
    }

    public void setPk_acc_adm_advisor_id(String pk_acc_adm_advisor_id) {
        this.pk_acc_adm_advisor_id = pk_acc_adm_advisor_id;
    }

    public String getFk_acc_adm_advisor_id() {
        return fk_acc_adm_advisor_id;
    }

    public void setFk_acc_adm_advisor_id(String fk_acc_adm_advisor_id) {
        this.fk_acc_adm_advisor_id = fk_acc_adm_advisor_id;
    }

    public String getVoucher_dateFrom() {
        return voucher_dateFrom;
    }

    public void setVoucher_dateFrom(String voucher_dateFrom) {
        this.voucher_dateFrom = voucher_dateFrom;
    }

    public String getVoucher_dateTo() {
        return voucher_dateTo;
    }

    public void setVoucher_dateTo(String voucher_dateTo) {
        this.voucher_dateTo = voucher_dateTo;
    }

    public String getVoucher_generated_date() {
        return voucher_generated_date;
    }

    public void setVoucher_generated_date(String voucher_generated_date) {
        this.voucher_generated_date = voucher_generated_date;
    }

    public String getPayableAmount() {
        return payableAmount;
    }

    public void setPayableAmount(String payableAmount) {
        this.payableAmount = payableAmount;
    }
}
