package com.guruvardaan.ghargharsurvey.model;

public class OfferModel {
    String id, offerDetails, stDt, endDt, depositAmountFrom, depositAmountTo, offerAmount;

    public OfferModel(String id, String offerDetails, String stDt, String endDt, String depositAmountFrom, String depositAmountTo, String offerAmount) {
        this.id = id;
        this.offerDetails = offerDetails;
        this.stDt = stDt;
        this.endDt = endDt;
        this.depositAmountFrom = depositAmountFrom;
        this.depositAmountTo = depositAmountTo;
        this.offerAmount = offerAmount;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getOfferDetails() {
        return offerDetails;
    }

    public void setOfferDetails(String offerDetails) {
        this.offerDetails = offerDetails;
    }

    public String getStDt() {
        return stDt;
    }

    public void setStDt(String stDt) {
        this.stDt = stDt;
    }

    public String getEndDt() {
        return endDt;
    }

    public void setEndDt(String endDt) {
        this.endDt = endDt;
    }

    public String getDepositAmountFrom() {
        return depositAmountFrom;
    }

    public void setDepositAmountFrom(String depositAmountFrom) {
        this.depositAmountFrom = depositAmountFrom;
    }

    public String getDepositAmountTo() {
        return depositAmountTo;
    }

    public void setDepositAmountTo(String depositAmountTo) {
        this.depositAmountTo = depositAmountTo;
    }

    public String getOfferAmount() {
        return offerAmount;
    }

    public void setOfferAmount(String offerAmount) {
        this.offerAmount = offerAmount;
    }
}
