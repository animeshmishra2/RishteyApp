package com.guruvardaan.ghargharsurvey.model;

public class VoucherAccountModel {
    String id, acHolder, acNo, IFSC, bankId, bank_name, branch, selected;

    public VoucherAccountModel(String id, String acHolder, String acNo, String IFSC, String bankId, String bank_name, String branch, String selected) {
        this.id = id;
        this.acHolder = acHolder;
        this.acNo = acNo;
        this.IFSC = IFSC;
        this.bankId = bankId;
        this.bank_name = bank_name;
        this.branch = branch;
        this.selected = selected;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getAcHolder() {
        return acHolder;
    }

    public void setAcHolder(String acHolder) {
        this.acHolder = acHolder;
    }

    public String getAcNo() {
        return acNo;
    }

    public void setAcNo(String acNo) {
        this.acNo = acNo;
    }

    public String getIFSC() {
        return IFSC;
    }

    public void setIFSC(String IFSC) {
        this.IFSC = IFSC;
    }

    public String getBankId() {
        return bankId;
    }

    public void setBankId(String bankId) {
        this.bankId = bankId;
    }

    public String getBank_name() {
        return bank_name;
    }

    public void setBank_name(String bank_name) {
        this.bank_name = bank_name;
    }

    public String getBranch() {
        return branch;
    }

    public void setBranch(String branch) {
        this.branch = branch;
    }

    public String getSelected() {
        return selected;
    }

    public void setSelected(String selected) {
        this.selected = selected;
    }
}
