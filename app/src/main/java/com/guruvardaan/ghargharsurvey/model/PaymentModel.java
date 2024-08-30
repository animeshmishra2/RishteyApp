package com.guruvardaan.ghargharsurvey.model;

public class PaymentModel {
    String transaction_date, fk_cnf_branch_id, transaction_amount, transaction_mode, offerTaken, offerAmount;

    public PaymentModel(String transaction_date, String fk_cnf_branch_id, String transaction_amount, String transaction_mode, String offerTaken, String offerAmount) {
        this.transaction_date = transaction_date;
        this.fk_cnf_branch_id = fk_cnf_branch_id;
        this.transaction_amount = transaction_amount;
        this.transaction_mode = transaction_mode;
        this.offerTaken = offerTaken;
        this.offerAmount = offerAmount;
    }

    public String getTransaction_date() {
        return transaction_date;
    }

    public void setTransaction_date(String transaction_date) {
        this.transaction_date = transaction_date;
    }

    public String getFk_cnf_branch_id() {
        return fk_cnf_branch_id;
    }

    public void setFk_cnf_branch_id(String fk_cnf_branch_id) {
        this.fk_cnf_branch_id = fk_cnf_branch_id;
    }

    public String getTransaction_amount() {
        return transaction_amount;
    }

    public void setTransaction_amount(String transaction_amount) {
        this.transaction_amount = transaction_amount;
    }

    public String getTransaction_mode() {
        return transaction_mode;
    }

    public void setTransaction_mode(String transaction_mode) {
        this.transaction_mode = transaction_mode;
    }

    public String getOfferTaken() {
        return offerTaken;
    }

    public void setOfferTaken(String offerTaken) {
        this.offerTaken = offerTaken;
    }

    public String getOfferAmount() {
        return offerAmount;
    }

    public void setOfferAmount(String offerAmount) {
        this.offerAmount = offerAmount;
    }
}
