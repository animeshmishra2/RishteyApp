package com.guruvardaan.ghargharsurvey.model;

import java.io.Serializable;

public class MyPlotRequestModel implements Serializable {
    String id, advisorId, uniqueName, userDetails, empComment, ins_date, requested_property, requested_block, property_type_name, requested_plot, advisor_name;
    String customerName, booked_property, booked_block, amount, booked_plot, pk_prm_property_id, pk_prm_block_id, pk_prm_property_type, pk_prm_define_property_id, status, requestedCustomerId;
    String allotedBlockName, allotedPropertyName, allotedPlotName, allotedBlockId, request_plot_id, totalPayments, RequestedcustomerName;

    public MyPlotRequestModel(String id, String advisorId, String uniqueName, String userDetails, String empComment, String ins_date, String requested_property, String requested_block, String property_type_name, String requested_plot, String advisor_name, String customerName, String booked_property, String booked_block, String amount, String booked_plot, String pk_prm_property_id, String pk_prm_block_id, String pk_prm_property_type, String pk_prm_define_property_id, String status, String allotedBlockName, String allotedPropertyName, String allotedPlotName, String allotedBlockId, String request_plot_id, String totalPayments, String requestedCustomerId, String RequestedcustomerName) {
        this.id = id;
        this.advisorId = advisorId;
        this.uniqueName = uniqueName;
        this.userDetails = userDetails;
        this.empComment = empComment;
        this.ins_date = ins_date;
        this.requested_property = requested_property;
        this.requested_block = requested_block;
        this.property_type_name = property_type_name;
        this.requested_plot = requested_plot;
        this.advisor_name = advisor_name;
        this.customerName = customerName;
        this.booked_property = booked_property;
        this.booked_block = booked_block;
        this.amount = amount;
        this.booked_plot = booked_plot;
        this.pk_prm_property_id = pk_prm_property_id;
        this.pk_prm_block_id = pk_prm_block_id;
        this.pk_prm_property_type = pk_prm_property_type;
        this.pk_prm_define_property_id = pk_prm_define_property_id;
        this.status = status;
        this.allotedBlockName = allotedBlockName;
        this.allotedPropertyName = allotedPropertyName;
        this.allotedPlotName = allotedPlotName;
        this.allotedBlockId = allotedBlockId;
        this.request_plot_id = request_plot_id;
        this.totalPayments = totalPayments;
        this.requestedCustomerId = requestedCustomerId;
        this.RequestedcustomerName = RequestedcustomerName;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getAdvisorId() {
        return advisorId;
    }

    public void setAdvisorId(String advisorId) {
        this.advisorId = advisorId;
    }

    public String getUniqueName() {
        return uniqueName;
    }

    public void setUniqueName(String uniqueName) {
        this.uniqueName = uniqueName;
    }

    public String getUserDetails() {
        return userDetails;
    }

    public void setUserDetails(String userDetails) {
        this.userDetails = userDetails;
    }

    public String getEmpComment() {
        return empComment;
    }

    public void setEmpComment(String empComment) {
        this.empComment = empComment;
    }

    public String getIns_date() {
        return ins_date;
    }

    public void setIns_date(String ins_date) {
        this.ins_date = ins_date;
    }

    public String getRequested_property() {
        return requested_property;
    }

    public void setRequested_property(String requested_property) {
        this.requested_property = requested_property;
    }

    public String getRequested_block() {
        return requested_block;
    }

    public void setRequested_block(String requested_block) {
        this.requested_block = requested_block;
    }

    public String getProperty_type_name() {
        return property_type_name;
    }

    public void setProperty_type_name(String property_type_name) {
        this.property_type_name = property_type_name;
    }

    public String getRequested_plot() {
        return requested_plot;
    }

    public void setRequested_plot(String requested_plot) {
        this.requested_plot = requested_plot;
    }

    public String getAdvisor_name() {
        return advisor_name;
    }

    public void setAdvisor_name(String advisor_name) {
        this.advisor_name = advisor_name;
    }

    public String getCustomerName() {
        return customerName;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    public String getBooked_property() {
        return booked_property;
    }

    public void setBooked_property(String booked_property) {
        this.booked_property = booked_property;
    }

    public String getBooked_block() {
        return booked_block;
    }

    public void setBooked_block(String booked_block) {
        this.booked_block = booked_block;
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public String getBooked_plot() {
        return booked_plot;
    }

    public void setBooked_plot(String booked_plot) {
        this.booked_plot = booked_plot;
    }

    public String getPk_prm_property_id() {
        return pk_prm_property_id;
    }

    public void setPk_prm_property_id(String pk_prm_property_id) {
        this.pk_prm_property_id = pk_prm_property_id;
    }

    public String getPk_prm_block_id() {
        return pk_prm_block_id;
    }

    public void setPk_prm_block_id(String pk_prm_block_id) {
        this.pk_prm_block_id = pk_prm_block_id;
    }

    public String getPk_prm_property_type() {
        return pk_prm_property_type;
    }

    public void setPk_prm_property_type(String pk_prm_property_type) {
        this.pk_prm_property_type = pk_prm_property_type;
    }

    public String getPk_prm_define_property_id() {
        return pk_prm_define_property_id;
    }

    public void setPk_prm_define_property_id(String pk_prm_define_property_id) {
        this.pk_prm_define_property_id = pk_prm_define_property_id;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getAllotedBlockName() {
        return allotedBlockName;
    }

    public void setAllotedBlockName(String allotedBlockName) {
        this.allotedBlockName = allotedBlockName;
    }

    public String getAllotedPropertyName() {
        return allotedPropertyName;
    }

    public void setAllotedPropertyName(String allotedPropertyName) {
        this.allotedPropertyName = allotedPropertyName;
    }

    public String getAllotedPlotName() {
        return allotedPlotName;
    }

    public void setAllotedPlotName(String allotedPlotName) {
        this.allotedPlotName = allotedPlotName;
    }

    public String getAllotedBlockId() {
        return allotedBlockId;
    }

    public void setAllotedBlockId(String allotedBlockId) {
        this.allotedBlockId = allotedBlockId;
    }

    public String getRequest_plot_id() {
        return request_plot_id;
    }

    public void setRequest_plot_id(String request_plot_id) {
        this.request_plot_id = request_plot_id;
    }

    public String getTotalPayments() {
        return totalPayments;
    }

    public void setTotalPayments(String totalPayments) {
        this.totalPayments = totalPayments;
    }

    public String getRequestedCustomerId() {
        return requestedCustomerId;
    }

    public void setRequestedCustomerId(String requestedCustomerId) {
        this.requestedCustomerId = requestedCustomerId;
    }

    public String getRequestedcustomerName() {
        return RequestedcustomerName;
    }

    public void setRequestedcustomerName(String requestedcustomerName) {
        RequestedcustomerName = requestedcustomerName;
    }
}
