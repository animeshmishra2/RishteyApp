package com.guruvardaan.ghargharsurvey.model;

public class AgentReceiptModel {
    int type;
    String id, start_date, end_date, pdfType, showText, ins_dt, url, customerFirstName, plotNumber, transaction_amount;

    public AgentReceiptModel(int type, String start_date, String end_date, String id, String pdfType, String showText, String ins_dt, String url, String customerFirstName, String plotNumber, String transaction_amount) {
        this.type = type;
        this.id = id;
        this.pdfType = pdfType;
        this.showText = showText;
        this.ins_dt = ins_dt;
        this.url = url;
        this.start_date = start_date;
        this.end_date = end_date;
        this.customerFirstName = customerFirstName;
        this.plotNumber = plotNumber;
        this.transaction_amount = transaction_amount;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getPdfType() {
        return pdfType;
    }

    public void setPdfType(String pdfType) {
        this.pdfType = pdfType;
    }

    public String getShowText() {
        return showText;
    }

    public void setShowText(String showText) {
        this.showText = showText;
    }

    public String getIns_dt() {
        return ins_dt;
    }

    public void setIns_dt(String ins_dt) {
        this.ins_dt = ins_dt;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getCustomerFirstName() {
        return customerFirstName;
    }

    public void setCustomerFirstName(String customerFirstName) {
        this.customerFirstName = customerFirstName;
    }

    public String getPlotNumber() {
        return plotNumber;
    }

    public void setPlotNumber(String plotNumber) {
        this.plotNumber = plotNumber;
    }

    public String getTransaction_amount() {
        return transaction_amount;
    }

    public void setTransaction_amount(String transaction_amount) {
        this.transaction_amount = transaction_amount;
    }

    public String getStart_date() {
        return start_date;
    }

    public void setStart_date(String start_date) {
        this.start_date = start_date;
    }

    public String getEnd_date() {
        return end_date;
    }

    public void setEnd_date(String end_date) {
        this.end_date = end_date;
    }
}
