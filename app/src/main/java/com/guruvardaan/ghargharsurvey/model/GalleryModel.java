package com.guruvardaan.ghargharsurvey.model;

public class GalleryModel {
    String id, title, description, imgLoc;

    public GalleryModel(String id, String title, String description, String imgLoc) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.imgLoc = imgLoc;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getImgLoc() {
        return imgLoc;
    }

    public void setImgLoc(String imgLoc) {
        this.imgLoc = imgLoc;
    }
}
