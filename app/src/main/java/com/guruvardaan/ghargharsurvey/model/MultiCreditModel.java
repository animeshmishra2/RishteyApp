package com.guruvardaan.ghargharsurvey.model;

public class MultiCreditModel {
    int type;
    String start_date, end_date, id, pk_acc_com_voucher_id, voucher_dateFrom, voucher_dateTo, voucher_generated_date, fk_acc_adm_advisor_id, direct_comission, gap_comission, total_comission, tdsPer, serviceChargePEr, tdsAmount, serviceChargeAmount, totalDeduction, payableAmount, voucher_status, paymentBy, payment_date, paymentTime, payment_transaction_id, paymentMode, utr, option1, option2, option3, ins_system, ins_by, ins_date, ins_time, advisor_name, status;

    public MultiCreditModel(int type, String start_date, String end_date, String id, String pk_acc_com_voucher_id, String voucher_dateFrom, String voucher_dateTo, String voucher_generated_date, String fk_acc_adm_advisor_id, String direct_comission, String gap_comission, String total_comission, String tdsPer, String serviceChargePEr, String tdsAmount, String serviceChargeAmount, String totalDeduction, String payableAmount, String voucher_status, String paymentBy, String payment_date, String paymentTime, String payment_transaction_id, String paymentMode, String utr, String option1, String option2, String option3, String ins_system, String ins_by, String ins_date, String ins_time, String advisor_name, String status) {
        this.type = type;
        this.start_date = start_date;
        this.end_date = end_date;
        this.id = id;
        this.pk_acc_com_voucher_id = pk_acc_com_voucher_id;
        this.voucher_dateFrom = voucher_dateFrom;
        this.voucher_dateTo = voucher_dateTo;
        this.voucher_generated_date = voucher_generated_date;
        this.fk_acc_adm_advisor_id = fk_acc_adm_advisor_id;
        this.direct_comission = direct_comission;
        this.gap_comission = gap_comission;
        this.total_comission = total_comission;
        this.tdsPer = tdsPer;
        this.serviceChargePEr = serviceChargePEr;
        this.tdsAmount = tdsAmount;
        this.serviceChargeAmount = serviceChargeAmount;
        this.totalDeduction = totalDeduction;
        this.payableAmount = payableAmount;
        this.voucher_status = voucher_status;
        this.paymentBy = paymentBy;
        this.payment_date = payment_date;
        this.paymentTime = paymentTime;
        this.payment_transaction_id = payment_transaction_id;
        this.paymentMode = paymentMode;
        this.utr = utr;
        this.option1 = option1;
        this.option2 = option2;
        this.option3 = option3;
        this.ins_system = ins_system;
        this.ins_by = ins_by;
        this.ins_date = ins_date;
        this.ins_time = ins_time;
        this.advisor_name = advisor_name;
        this.status = status;
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

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getPk_acc_com_voucher_id() {
        return pk_acc_com_voucher_id;
    }

    public void setPk_acc_com_voucher_id(String pk_acc_com_voucher_id) {
        this.pk_acc_com_voucher_id = pk_acc_com_voucher_id;
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

    public String getFk_acc_adm_advisor_id() {
        return fk_acc_adm_advisor_id;
    }

    public void setFk_acc_adm_advisor_id(String fk_acc_adm_advisor_id) {
        this.fk_acc_adm_advisor_id = fk_acc_adm_advisor_id;
    }

    public String getDirect_comission() {
        return direct_comission;
    }

    public void setDirect_comission(String direct_comission) {
        this.direct_comission = direct_comission;
    }

    public String getGap_comission() {
        return gap_comission;
    }

    public void setGap_comission(String gap_comission) {
        this.gap_comission = gap_comission;
    }

    public String getTotal_comission() {
        return total_comission;
    }

    public void setTotal_comission(String total_comission) {
        this.total_comission = total_comission;
    }

    public String getTdsPer() {
        return tdsPer;
    }

    public void setTdsPer(String tdsPer) {
        this.tdsPer = tdsPer;
    }

    public String getServiceChargePEr() {
        return serviceChargePEr;
    }

    public void setServiceChargePEr(String serviceChargePEr) {
        this.serviceChargePEr = serviceChargePEr;
    }

    public String getTdsAmount() {
        return tdsAmount;
    }

    public void setTdsAmount(String tdsAmount) {
        this.tdsAmount = tdsAmount;
    }

    public String getServiceChargeAmount() {
        return serviceChargeAmount;
    }

    public void setServiceChargeAmount(String serviceChargeAmount) {
        this.serviceChargeAmount = serviceChargeAmount;
    }

    public String getTotalDeduction() {
        return totalDeduction;
    }

    public void setTotalDeduction(String totalDeduction) {
        this.totalDeduction = totalDeduction;
    }

    public String getPayableAmount() {
        return payableAmount;
    }

    public void setPayableAmount(String payableAmount) {
        this.payableAmount = payableAmount;
    }

    public String getVoucher_status() {
        return voucher_status;
    }

    public void setVoucher_status(String voucher_status) {
        this.voucher_status = voucher_status;
    }

    public String getPaymentBy() {
        return paymentBy;
    }

    public void setPaymentBy(String paymentBy) {
        this.paymentBy = paymentBy;
    }

    public String getPayment_date() {
        return payment_date;
    }

    public void setPayment_date(String payment_date) {
        this.payment_date = payment_date;
    }

    public String getPaymentTime() {
        return paymentTime;
    }

    public void setPaymentTime(String paymentTime) {
        this.paymentTime = paymentTime;
    }

    public String getPayment_transaction_id() {
        return payment_transaction_id;
    }

    public void setPayment_transaction_id(String payment_transaction_id) {
        this.payment_transaction_id = payment_transaction_id;
    }

    public String getPaymentMode() {
        return paymentMode;
    }

    public void setPaymentMode(String paymentMode) {
        this.paymentMode = paymentMode;
    }

    public String getUtr() {
        return utr;
    }

    public void setUtr(String utr) {
        this.utr = utr;
    }

    public String getOption1() {
        return option1;
    }

    public void setOption1(String option1) {
        this.option1 = option1;
    }

    public String getOption2() {
        return option2;
    }

    public void setOption2(String option2) {
        this.option2 = option2;
    }

    public String getOption3() {
        return option3;
    }

    public void setOption3(String option3) {
        this.option3 = option3;
    }

    public String getIns_system() {
        return ins_system;
    }

    public void setIns_system(String ins_system) {
        this.ins_system = ins_system;
    }

    public String getIns_by() {
        return ins_by;
    }

    public void setIns_by(String ins_by) {
        this.ins_by = ins_by;
    }

    public String getIns_date() {
        return ins_date;
    }

    public void setIns_date(String ins_date) {
        this.ins_date = ins_date;
    }

    public String getIns_time() {
        return ins_time;
    }

    public void setIns_time(String ins_time) {
        this.ins_time = ins_time;
    }

    public String getAdvisor_name() {
        return advisor_name;
    }

    public void setAdvisor_name(String advisor_name) {
        this.advisor_name = advisor_name;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
