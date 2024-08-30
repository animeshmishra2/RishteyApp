package com.guruvardaan.ghargharsurvey.model;

public class DirectTeamModel {
    String agentId, agentName, agentMobile, agentRank, agentPassword, int_agentId, int_agentName, int_agentMobile, int_agentRank, pass;


    public DirectTeamModel(String agentId, String agentName, String agentMobile, String agentRank, String agentPassword, String int_agentId, String int_agentName, String int_agentMobile, String int_agentRank, String pass) {
        this.agentId = agentId;
        this.agentName = agentName;
        this.agentMobile = agentMobile;
        this.agentRank = agentRank;
        this.agentPassword = agentPassword;
        this.int_agentId = int_agentId;
        this.int_agentName = int_agentName;
        this.int_agentMobile = int_agentMobile;
        this.pass = pass;
        this.int_agentRank = int_agentRank;
    }

    public String getAgentId() {
        return agentId;
    }

    public void setAgentId(String agentId) {
        this.agentId = agentId;
    }

    public String getAgentName() {
        return agentName;
    }

    public void setAgentName(String agentName) {
        this.agentName = agentName;
    }

    public String getAgentMobile() {
        return agentMobile;
    }

    public void setAgentMobile(String agentMobile) {
        this.agentMobile = agentMobile;
    }

    public String getAgentRank() {
        return agentRank;
    }

    public void setAgentRank(String agentRank) {
        this.agentRank = agentRank;
    }

    public String getAgentPassword() {
        return agentPassword;
    }

    public void setAgentPassword(String agentPassword) {
        this.agentPassword = agentPassword;
    }

    public String getInt_agentId() {
        return int_agentId;
    }

    public void setInt_agentId(String int_agentId) {
        this.int_agentId = int_agentId;
    }

    public String getInt_agentName() {
        return int_agentName;
    }

    public void setInt_agentName(String int_agentName) {
        this.int_agentName = int_agentName;
    }

    public String getInt_agentMobile() {
        return int_agentMobile;
    }

    public void setInt_agentMobile(String int_agentMobile) {
        this.int_agentMobile = int_agentMobile;
    }

    public String getInt_agentRank() {
        return int_agentRank;
    }

    public void setInt_agentRank(String int_agentRank) {
        this.int_agentRank = int_agentRank;
    }

    public String getPass() {
        return pass;
    }

    public void setPass(String pass) {
        this.pass = pass;
    }
}
