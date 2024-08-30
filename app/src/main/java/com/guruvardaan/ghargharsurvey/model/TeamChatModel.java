package com.guruvardaan.ghargharsurvey.model;

public class TeamChatModel {
    String id, name, userid, firebase_token, msg, description, header_id, image_url, user_status, team_status, status, chat_time, header_name;

    public TeamChatModel(String id, String name, String userid, String firebase_token, String msg, String description, String header_id, String image_url, String user_status, String team_status, String status, String chat_time, String header_name) {
        this.id = id;
        this.name = name;
        this.userid = userid;
        this.firebase_token = firebase_token;
        this.msg = msg;
        this.description = description;
        this.header_id = header_id;
        this.image_url = image_url;
        this.user_status = user_status;
        this.team_status = team_status;
        this.status = status;
        this.chat_time = chat_time;
        this.header_name = header_name;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUserid() {
        return userid;
    }

    public void setUserid(String userid) {
        this.userid = userid;
    }

    public String getFirebase_token() {
        return firebase_token;
    }

    public void setFirebase_token(String firebase_token) {
        this.firebase_token = firebase_token;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getHeader_id() {
        return header_id;
    }

    public void setHeader_id(String header_id) {
        this.header_id = header_id;
    }

    public String getImage_url() {
        return image_url;
    }

    public void setImage_url(String image_url) {
        this.image_url = image_url;
    }

    public String getUser_status() {
        return user_status;
    }

    public void setUser_status(String user_status) {
        this.user_status = user_status;
    }

    public String getTeam_status() {
        return team_status;
    }

    public void setTeam_status(String team_status) {
        this.team_status = team_status;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getChat_time() {
        return chat_time;
    }

    public void setChat_time(String chat_time) {
        this.chat_time = chat_time;
    }

    public String getHeader_name() {
        return header_name;
    }

    public void setHeader_name(String header_name) {
        this.header_name = header_name;
    }
}
