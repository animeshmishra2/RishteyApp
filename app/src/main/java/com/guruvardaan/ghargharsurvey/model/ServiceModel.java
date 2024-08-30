package com.guruvardaan.ghargharsurvey.model;

public class ServiceModel {
    String pk_acc_cor_service_id, name;

    public ServiceModel(String pk_acc_cor_service_id, String name) {
        this.pk_acc_cor_service_id = pk_acc_cor_service_id;
        this.name = name;
    }

    public String getPk_acc_cor_service_id() {
        return pk_acc_cor_service_id;
    }

    public void setPk_acc_cor_service_id(String pk_acc_cor_service_id) {
        this.pk_acc_cor_service_id = pk_acc_cor_service_id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
