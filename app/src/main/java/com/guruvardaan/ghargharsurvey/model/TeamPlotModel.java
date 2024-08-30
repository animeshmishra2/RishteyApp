package com.guruvardaan.ghargharsurvey.model;

public class TeamPlotModel {
    String fk_acc_adm_advisor_id, advisor_name, mobile_no, alternate_no, advisor_rank, totalSaleArea, plotAmount, lastMonthBusiness, total, paid, cPending;

    public TeamPlotModel(String fk_acc_adm_advisor_id, String advisor_name, String mobile_no, String alternate_no, String advisor_rank, String totalSaleArea, String plotAmount, String lastMonthBusiness, String total, String paid, String cPending) {
        this.fk_acc_adm_advisor_id = fk_acc_adm_advisor_id;
        this.advisor_name = advisor_name;
        this.mobile_no = mobile_no;
        this.alternate_no = alternate_no;
        this.advisor_rank = advisor_rank;
        this.totalSaleArea = totalSaleArea;
        this.plotAmount = plotAmount;
        this.lastMonthBusiness = lastMonthBusiness;
        this.total = total;
        this.paid = paid;
        this.cPending = cPending;
    }

    public String getFk_acc_adm_advisor_id() {
        return fk_acc_adm_advisor_id;
    }

    public void setFk_acc_adm_advisor_id(String fk_acc_adm_advisor_id) {
        this.fk_acc_adm_advisor_id = fk_acc_adm_advisor_id;
    }

    public String getAdvisor_name() {
        return advisor_name;
    }

    public void setAdvisor_name(String advisor_name) {
        this.advisor_name = advisor_name;
    }

    public String getMobile_no() {
        return mobile_no;
    }

    public void setMobile_no(String mobile_no) {
        this.mobile_no = mobile_no;
    }

    public String getAlternate_no() {
        return alternate_no;
    }

    public void setAlternate_no(String alternate_no) {
        this.alternate_no = alternate_no;
    }

    public String getAdvisor_rank() {
        return advisor_rank;
    }

    public void setAdvisor_rank(String advisor_rank) {
        this.advisor_rank = advisor_rank;
    }

    public String getTotalSaleArea() {
        return totalSaleArea;
    }

    public void setTotalSaleArea(String totalSaleArea) {
        this.totalSaleArea = totalSaleArea;
    }

    public String getPlotAmount() {
        return plotAmount;
    }

    public void setPlotAmount(String plotAmount) {
        this.plotAmount = plotAmount;
    }

    public String getLastMonthBusiness() {
        return lastMonthBusiness;
    }

    public void setLastMonthBusiness(String lastMonthBusiness) {
        this.lastMonthBusiness = lastMonthBusiness;
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
}
