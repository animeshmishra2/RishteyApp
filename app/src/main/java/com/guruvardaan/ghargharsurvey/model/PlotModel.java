package com.guruvardaan.ghargharsurvey.model;

import java.io.Serializable;

public class PlotModel implements Serializable {
    String id, pk_prm_define_property_id, cellcount, uniquename, default_amount, default_area, customerID, option2, status, sel, plotAmount;

    public PlotModel(String id, String pk_prm_define_property_id, String cellcount, String uniquename, String default_amount, String default_area, String customerID, String option2, String status, String sel, String plotAmount) {
        this.id = id;
        this.pk_prm_define_property_id = pk_prm_define_property_id;
        this.cellcount = cellcount;
        this.uniquename = uniquename;
        this.default_amount = default_amount;
        this.default_area = default_area;
        this.customerID = customerID;
        this.option2 = option2;
        this.status = status;
        this.sel = sel;
        this.plotAmount = plotAmount;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getPk_prm_define_property_id() {
        return pk_prm_define_property_id;
    }

    public void setPk_prm_define_property_id(String pk_prm_define_property_id) {
        this.pk_prm_define_property_id = pk_prm_define_property_id;
    }

    public String getCellcount() {
        return cellcount;
    }

    public void setCellcount(String cellcount) {
        this.cellcount = cellcount;
    }

    public String getUniquename() {
        return uniquename;
    }

    public void setUniquename(String uniquename) {
        this.uniquename = uniquename;
    }

    public String getDefault_amount() {
        return default_amount;
    }

    public void setDefault_amount(String default_amount) {
        this.default_amount = default_amount;
    }

    public String getDefault_area() {
        return default_area;
    }

    public void setDefault_area(String default_area) {
        this.default_area = default_area;
    }

    public String getCustomerID() {
        return customerID;
    }

    public void setCustomerID(String customerID) {
        this.customerID = customerID;
    }

    public String getOption2() {
        return option2;
    }

    public void setOption2(String option2) {
        this.option2 = option2;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getSel() {
        return sel;
    }

    public void setSel(String sel) {
        this.sel = sel;
    }

    public String getPlotAmount() {
        return plotAmount;
    }

    public void setPlotAmount(String plotAmount) {
        this.plotAmount = plotAmount;
    }
}
