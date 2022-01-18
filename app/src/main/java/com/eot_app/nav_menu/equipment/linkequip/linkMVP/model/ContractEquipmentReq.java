package com.eot_app.nav_menu.equipment.linkequip.linkMVP.model;

public class ContractEquipmentReq {
    private final int activeRecord = 1;
    private final String search = "";
    private final String expiryDtf = "";
    private final String expiryDtt = "";
    private final String type;
    private final String jobId;
    private final String contrId;
    private int index;
    private int limit;
    private String cltId;

    public ContractEquipmentReq(String type, String jobId, String contrId) {
        this.index = index;
        this.limit = limit;
        this.cltId = cltId;
        this.type = type;
        this.jobId = jobId;
        this.contrId = contrId;
    }

    public int getIndex() {
        return index;
    }

    public void setIndex(int index) {
        this.index = index;
    }

    public int getLimit() {
        return limit;
    }

    public void setLimit(int limit) {
        this.limit = limit;
    }

    public String getCltId() {
        return cltId;
    }

    public String getType() {
        return type;
    }

    public String getJobId() {
        return jobId;
    }

    public String getContrId() {
        return contrId;
    }
}
