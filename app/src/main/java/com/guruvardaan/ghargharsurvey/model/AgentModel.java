package com.guruvardaan.ghargharsurvey.model;

public class AgentModel {
    String advisor_name, pk_acc_adm_advisor_id, mobile_no, advisor_rank, iadvisor_name, ipk_acc_adm_advisor_id, imobile_no, iadvisor_rank, pass;

    public AgentModel(String advisor_name, String pk_acc_adm_advisor_id, String mobile_no, String advisor_rank, String iadvisor_name, String ipk_acc_adm_advisor_id, String imobile_no, String iadvisor_rank, String pass) {
        this.advisor_name = advisor_name;
        this.pk_acc_adm_advisor_id = pk_acc_adm_advisor_id;
        this.mobile_no = mobile_no;
        this.advisor_rank = advisor_rank;
        this.iadvisor_name = iadvisor_name;
        this.ipk_acc_adm_advisor_id = ipk_acc_adm_advisor_id;
        this.imobile_no = imobile_no;
        this.iadvisor_rank = iadvisor_rank;
        this.pass = pass;
    }

    public String getAdvisor_name() {
        return advisor_name;
    }

    public void setAdvisor_name(String advisor_name) {
        this.advisor_name = advisor_name;
    }

    public String getPk_acc_adm_advisor_id() {
        return pk_acc_adm_advisor_id;
    }

    public void setPk_acc_adm_advisor_id(String pk_acc_adm_advisor_id) {
        this.pk_acc_adm_advisor_id = pk_acc_adm_advisor_id;
    }

    public String getMobile_no() {
        return mobile_no;
    }

    public void setMobile_no(String mobile_no) {
        this.mobile_no = mobile_no;
    }

    public String getAdvisor_rank() {
        return advisor_rank;
    }

    public void setAdvisor_rank(String advisor_rank) {
        this.advisor_rank = advisor_rank;
    }

    public String getIadvisor_name() {
        return iadvisor_name;
    }

    public void setIadvisor_name(String iadvisor_name) {
        this.iadvisor_name = iadvisor_name;
    }

    public String getIpk_acc_adm_advisor_id() {
        return ipk_acc_adm_advisor_id;
    }

    public void setIpk_acc_adm_advisor_id(String ipk_acc_adm_advisor_id) {
        this.ipk_acc_adm_advisor_id = ipk_acc_adm_advisor_id;
    }

    public String getImobile_no() {
        return imobile_no;
    }

    public void setImobile_no(String imobile_no) {
        this.imobile_no = imobile_no;
    }

    public String getIadvisor_rank() {
        return iadvisor_rank;
    }

    public void setIadvisor_rank(String iadvisor_rank) {
        this.iadvisor_rank = iadvisor_rank;
    }

    public String getPass() {
        return pass;
    }

    public void setPass(String pass) {
        this.pass = pass;
    }
}
