package com.guruvardaan.ghargharsurvey.model;

public class TeamStatementModel {
    int type;
    String start_date, end_date, pk_acc_adm_advisor_id, advisor_name, mobile_no, whatsapp, advisor_rank, selfBusiness, selfCount;

    public TeamStatementModel(int type, String start_date, String end_date, String pk_acc_adm_advisor_id, String advisor_name, String mobile_no, String whatsapp, String advisor_rank, String selfBusiness, String selfCount) {
        this.type = type;
        this.start_date = start_date;
        this.end_date = end_date;
        this.pk_acc_adm_advisor_id = pk_acc_adm_advisor_id;
        this.advisor_name = advisor_name;
        this.mobile_no = mobile_no;
        this.whatsapp = whatsapp;
        this.advisor_rank = advisor_rank;
        this.selfBusiness = selfBusiness;
        this.selfCount = selfCount;
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

    public String getPk_acc_adm_advisor_id() {
        return pk_acc_adm_advisor_id;
    }

    public void setPk_acc_adm_advisor_id(String pk_acc_adm_advisor_id) {
        this.pk_acc_adm_advisor_id = pk_acc_adm_advisor_id;
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

    public String getWhatsapp() {
        return whatsapp;
    }

    public void setWhatsapp(String whatsapp) {
        this.whatsapp = whatsapp;
    }

    public String getAdvisor_rank() {
        return advisor_rank;
    }

    public void setAdvisor_rank(String advisor_rank) {
        this.advisor_rank = advisor_rank;
    }

    public String getSelfBusiness() {
        return selfBusiness;
    }

    public void setSelfBusiness(String selfBusiness) {
        this.selfBusiness = selfBusiness;
    }

    public String getSelfCount() {
        return selfCount;
    }

    public void setSelfCount(String selfCount) {
        this.selfCount = selfCount;
    }
}
