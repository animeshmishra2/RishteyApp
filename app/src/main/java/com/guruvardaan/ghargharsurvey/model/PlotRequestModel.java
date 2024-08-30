package com.guruvardaan.ghargharsurvey.model;

import java.io.Serializable;

public class PlotRequestModel implements Serializable {
    String id, agent_id, customer_name, customer_mobile, customer_address, professionId, professionName, occupationId, occupationName, status_of_plot_buy, payment_option, plot_size, block, category, emiforyear, visit_date, visit_amount, pan, aadhar_front, aadhar_back, meeting_pic;

    public PlotRequestModel(String id, String agent_id, String customer_name, String customer_mobile, String customer_address, String professionId, String professionName, String occupationId, String occupationName, String status_of_plot_buy, String payment_option, String plot_size, String block, String category, String emiforyear, String visit_date, String visit_amount, String pan, String aadhar_front, String aadhar_back, String meeting_pic) {
        this.id = id;
        this.agent_id = agent_id;
        this.customer_name = customer_name;
        this.customer_mobile = customer_mobile;
        this.customer_address = customer_address;
        this.professionId = professionId;
        this.professionName = professionName;
        this.occupationId = occupationId;
        this.occupationName = occupationName;
        this.status_of_plot_buy = status_of_plot_buy;
        this.payment_option = payment_option;
        this.plot_size = plot_size;
        this.block = block;
        this.category = category;
        this.emiforyear = emiforyear;
        this.visit_date = visit_date;
        this.visit_amount = visit_amount;
        this.pan = pan;
        this.aadhar_front = aadhar_front;
        this.aadhar_back = aadhar_back;
        this.meeting_pic = meeting_pic;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getAgent_id() {
        return agent_id;
    }

    public void setAgent_id(String agent_id) {
        this.agent_id = agent_id;
    }

    public String getCustomer_name() {
        return customer_name;
    }

    public void setCustomer_name(String customer_name) {
        this.customer_name = customer_name;
    }

    public String getCustomer_mobile() {
        return customer_mobile;
    }

    public void setCustomer_mobile(String customer_mobile) {
        this.customer_mobile = customer_mobile;
    }

    public String getCustomer_address() {
        return customer_address;
    }

    public void setCustomer_address(String customer_address) {
        this.customer_address = customer_address;
    }

    public String getProfessionId() {
        return professionId;
    }

    public void setProfessionId(String professionId) {
        this.professionId = professionId;
    }

    public String getProfessionName() {
        return professionName;
    }

    public void setProfessionName(String professionName) {
        this.professionName = professionName;
    }

    public String getOccupationId() {
        return occupationId;
    }

    public void setOccupationId(String occupationId) {
        this.occupationId = occupationId;
    }

    public String getOccupationName() {
        return occupationName;
    }

    public void setOccupationName(String occupationName) {
        this.occupationName = occupationName;
    }

    public String getStatus_of_plot_buy() {
        return status_of_plot_buy;
    }

    public void setStatus_of_plot_buy(String status_of_plot_buy) {
        this.status_of_plot_buy = status_of_plot_buy;
    }

    public String getPayment_option() {
        return payment_option;
    }

    public void setPayment_option(String payment_option) {
        this.payment_option = payment_option;
    }

    public String getPlot_size() {
        return plot_size;
    }

    public void setPlot_size(String plot_size) {
        this.plot_size = plot_size;
    }

    public String getBlock() {
        return block;
    }

    public void setBlock(String block) {
        this.block = block;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getEmiforyear() {
        return emiforyear;
    }

    public void setEmiforyear(String emiforyear) {
        this.emiforyear = emiforyear;
    }

    public String getVisit_date() {
        return visit_date;
    }

    public void setVisit_date(String visit_date) {
        this.visit_date = visit_date;
    }

    public String getVisit_amount() {
        return visit_amount;
    }

    public void setVisit_amount(String visit_amount) {
        this.visit_amount = visit_amount;
    }

    public String getPan() {
        return pan;
    }

    public void setPan(String pan) {
        this.pan = pan;
    }

    public String getAadhar_front() {
        return aadhar_front;
    }

    public void setAadhar_front(String aadhar_front) {
        this.aadhar_front = aadhar_front;
    }

    public String getAadhar_back() {
        return aadhar_back;
    }

    public void setAadhar_back(String aadhar_back) {
        this.aadhar_back = aadhar_back;
    }

    public String getMeeting_pic() {
        return meeting_pic;
    }

    public void setMeeting_pic(String meeting_pic) {
        this.meeting_pic = meeting_pic;
    }
}
