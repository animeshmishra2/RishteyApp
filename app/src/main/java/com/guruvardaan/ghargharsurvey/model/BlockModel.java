package com.guruvardaan.ghargharsurvey.model;

public class BlockModel {
    String property_block_name, totalPlots, pk_prm_block_id;

    public BlockModel(String property_block_name, String totalPlots, String pk_prm_block_id) {
        this.property_block_name = property_block_name;
        this.totalPlots = totalPlots;
        this.pk_prm_block_id = pk_prm_block_id;
    }

    public String getProperty_block_name() {
        return property_block_name;
    }

    public void setProperty_block_name(String property_block_name) {
        this.property_block_name = property_block_name;
    }

    public String getTotalPlots() {
        return totalPlots;
    }

    public void setTotalPlots(String totalPlots) {
        this.totalPlots = totalPlots;
    }

    public String getPk_prm_block_id() {
        return pk_prm_block_id;
    }

    public void setPk_prm_block_id(String pk_prm_block_id) {
        this.pk_prm_block_id = pk_prm_block_id;
    }
}
