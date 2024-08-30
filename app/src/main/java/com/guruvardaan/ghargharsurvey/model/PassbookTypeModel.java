package com.guruvardaan.ghargharsurvey.model;

public class PassbookTypeModel {
    String typeId, type_name;

    public PassbookTypeModel(String typeId, String type_name) {
        this.typeId = typeId;
        this.type_name = type_name;
    }

    public String getTypeId() {
        return typeId;
    }

    public void setTypeId(String typeId) {
        this.typeId = typeId;
    }

    public String getType_name() {
        return type_name;
    }

    public void setType_name(String type_name) {
        this.type_name = type_name;
    }
}
