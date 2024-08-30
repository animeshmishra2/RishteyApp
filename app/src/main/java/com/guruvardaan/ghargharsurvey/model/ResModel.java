package com.guruvardaan.ghargharsurvey.model;

public class ResModel {
    String id, complaint_id, type, response_type, user_id, replier_id, transfer_to, image_url, response, user_status, team_status, res_date, status, rep_name, U_name, reply_thread_id;

    int rep_pos;

    public ResModel(String id, String complaint_id, String type, String response_type, String user_id, String replier_id, String transfer_to, String image_url, String response, String user_status, String team_status, String res_date, String status, String rep_name, String u_name, String reply_thread_id, int rep_pos) {
        this.id = id;
        this.complaint_id = complaint_id;
        this.type = type;
        this.response_type = response_type;
        this.user_id = user_id;
        this.replier_id = replier_id;
        this.transfer_to = transfer_to;
        this.image_url = image_url;
        this.response = response;
        this.user_status = user_status;
        this.team_status = team_status;
        this.res_date = res_date;
        this.status = status;
        this.rep_name = rep_name;
        U_name = u_name;
        this.reply_thread_id = reply_thread_id;
        this.rep_pos = rep_pos;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getComplaint_id() {
        return complaint_id;
    }

    public void setComplaint_id(String complaint_id) {
        this.complaint_id = complaint_id;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getResponse_type() {
        return response_type;
    }

    public void setResponse_type(String response_type) {
        this.response_type = response_type;
    }

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    public String getReplier_id() {
        return replier_id;
    }

    public void setReplier_id(String replier_id) {
        this.replier_id = replier_id;
    }

    public String getTransfer_to() {
        return transfer_to;
    }

    public void setTransfer_to(String transfer_to) {
        this.transfer_to = transfer_to;
    }

    public String getImage_url() {
        return image_url;
    }

    public void setImage_url(String image_url) {
        this.image_url = image_url;
    }

    public String getResponse() {
        return response;
    }

    public void setResponse(String response) {
        this.response = response;
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

    public String getRes_date() {
        return res_date;
    }

    public void setRes_date(String res_date) {
        this.res_date = res_date;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getRep_name() {
        return rep_name;
    }

    public void setRep_name(String rep_name) {
        this.rep_name = rep_name;
    }

    public String getU_name() {
        return U_name;
    }

    public void setU_name(String u_name) {
        U_name = u_name;
    }

    public String getReply_thread_id() {
        return reply_thread_id;
    }

    public void setReply_thread_id(String reply_thread_id) {
        this.reply_thread_id = reply_thread_id;
    }

    public int getRep_pos() {
        return rep_pos;
    }

    public void setRep_pos(int rep_pos) {
        this.rep_pos = rep_pos;
    }
}
