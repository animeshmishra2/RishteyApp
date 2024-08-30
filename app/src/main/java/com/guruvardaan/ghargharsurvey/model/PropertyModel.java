package com.guruvardaan.ghargharsurvey.model;

public class PropertyModel {
    String pk_prm_property_id, property_name;

    public PropertyModel(String pk_prm_property_id, String property_name) {
        this.pk_prm_property_id = pk_prm_property_id;
        this.property_name = property_name;
    }

    public String getPk_prm_property_id() {
        return pk_prm_property_id;
    }

    public void setPk_prm_property_id(String pk_prm_property_id) {
        this.pk_prm_property_id = pk_prm_property_id;
    }

    public String getProperty_name() {
        return property_name;
    }

    public void setProperty_name(String property_name) {
        this.property_name = property_name;
    }
}
