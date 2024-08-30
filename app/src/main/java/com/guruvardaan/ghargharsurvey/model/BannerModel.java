package com.guruvardaan.ghargharsurvey.model;

public class BannerModel {
    String Id, type, img_name, description;

    public BannerModel(String id, String type, String img_name, String description) {
        Id = id;
        this.type = type;
        this.img_name = img_name;
        this.description = description;
    }

    public String getId() {
        return Id;
    }

    public void setId(String id) {
        Id = id;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getImg_name() {
        return img_name;
    }

    public void setImg_name(String img_name) {
        this.img_name = img_name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
