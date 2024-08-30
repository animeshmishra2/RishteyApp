package com.guruvardaan.ghargharsurvey.model;

public class SearchModel {
    String name, UserRank, userId, mobile1, mobile2, userImage, type, keyword;

    public SearchModel(String name, String userRank, String userId, String mobile1, String mobile2, String userImage, String type, String keyword) {
        this.name = name;
        UserRank = userRank;
        this.userId = userId;
        this.mobile1 = mobile1;
        this.mobile2 = mobile2;
        this.userImage = userImage;
        this.type = type;
        this.keyword = keyword;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUserRank() {
        return UserRank;
    }

    public void setUserRank(String userRank) {
        UserRank = userRank;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getMobile1() {
        return mobile1;
    }

    public void setMobile1(String mobile1) {
        this.mobile1 = mobile1;
    }

    public String getMobile2() {
        return mobile2;
    }

    public void setMobile2(String mobile2) {
        this.mobile2 = mobile2;
    }

    public String getUserImage() {
        return userImage;
    }

    public void setUserImage(String userImage) {
        this.userImage = userImage;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getKeyword() {
        return keyword;
    }

    public void setKeyword(String keyword) {
        this.keyword = keyword;
    }
}
