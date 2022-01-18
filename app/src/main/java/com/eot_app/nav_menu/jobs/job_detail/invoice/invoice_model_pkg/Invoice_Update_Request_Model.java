package com.eot_app.nav_menu.jobs.job_detail.invoice.invoice_model_pkg;

import com.eot_app.nav_menu.jobs.job_detail.invoice.invoice_detail_pkg.inv_detail_model.ItemData;
import com.eot_app.nav_menu.jobs.job_detail.invoice.invoice_detail_pkg.inv_detail_model.ShippingItem;

import java.util.List;

public class Invoice_Update_Request_Model {
    private final String invType;
    private final String changeState;
    private final String shipto;
    private String nm;
    private String pro;
    private String invId;
    private String compId;
    private String parentId;
    private String jobId;
    private String cltId;
    private String adr = "";
    private String discount;
    private String total;
    private String paid;
    private String note;
    private String dueDate;
    private String invDate;
    private List<NewItem> newItem = null;
    private List<ItemData> itemData = null;
    //  private List<Object> groupByData = null;
    private String pono;
    private String cur;
    private String isShowInList;
    private List<ShippingItem> shippingItem = null;
//    private Map<String, Object> additionalProperties = new HashMap<String, Object>();


    public Invoice_Update_Request_Model(String nm, String pro, String invId, String compId, String parentId, String jobId,
                                        String cltId, String adr, String discount, String paid, String note,
                                        String dueDate, String invDate, List<ItemData> itemData,//, List<NewItem> newItem
                                        String pono, String invType, String cur, String changeState,//  List<Object> groupByData
                                        String isShowInList, List<NewItem> newItem, String shipto, List<ShippingItem> shippingItem, String total) {
        this.nm = nm;
        this.pro = pro;
        this.invId = invId;
        this.compId = compId;
        this.parentId = parentId;
        this.jobId = jobId;
        this.cltId = cltId;
        this.adr = adr;
        this.discount = discount;
        this.total = total;
        this.paid = paid;
        this.note = note;
        this.dueDate = dueDate;
        this.invDate = invDate;
        //this.newItem = newItem;
        this.itemData = itemData;
        // this.groupByData = groupByData;
        this.pono = pono;
        this.invType = invType;
        this.cur = cur;
        this.changeState = changeState;
        this.isShowInList = isShowInList;
        this.newItem = newItem;
        this.shipto = shipto;
        this.shippingItem = shippingItem;
    }

    public List<ShippingItem> getShippingItem() {
        return shippingItem;
    }

    public void setShippingItem(List<ShippingItem> shippingItem) {
        this.shippingItem = shippingItem;
    }

    public String getNm() {
        return nm;
    }

    public void setNm(String nm) {
        this.nm = nm;
    }

    public String getPro() {
        return pro;
    }

    public void setPro(String pro) {
        this.pro = pro;
    }

    public String getInvId() {
        return invId;
    }

    public void setInvId(String invId) {
        this.invId = invId;
    }

    public String getCompId() {
        return compId;
    }

    public void setCompId(String compId) {
        this.compId = compId;
    }

    public String getParentId() {
        return parentId;
    }

    public void setParentId(String parentId) {
        this.parentId = parentId;
    }

    public String getJobId() {
        return jobId;
    }

    public void setJobId(String jobId) {
        this.jobId = jobId;
    }

    public String getCltId() {
        return cltId;
    }

    public void setCltId(String cltId) {
        this.cltId = cltId;
    }

    public String getAdr() {
        return adr;
    }

    public void setAdr(String adr) {
        this.adr = adr;
    }

    public String getDiscount() {
        return discount;
    }

    public void setDiscount(String discount) {
        this.discount = discount;
    }

    public String getTotal() {
        return total;
    }

    public void setTotal(String total) {
        this.total = total;
    }

    public String getPaid() {
        return paid;
    }

    public void setPaid(String paid) {
        this.paid = paid;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public String getDueDate() {
        return dueDate;
    }

    public void setDueDate(String dueDate) {
        this.dueDate = dueDate;
    }

    public String getInvDate() {
        return invDate;
    }

    public void setInvDate(String invDate) {
        this.invDate = invDate;
    }

//    public List<NewItem> getNewItem() {
//        return newItem;
//    }
//
//    public void setNewItem(List<NewItem> newItem) {
//        this.newItem = newItem;
//    }

    public List<ItemData> getItemData() {
        return itemData;
    }

    public void setItemData(List<ItemData> itemData) {
        this.itemData = itemData;
    }

//    public List<Object> getGroupByData() {
//        return groupByData;
//    }
//
//    public void setGroupByData(List<Object> groupByData) {
//        this.groupByData = groupByData;
//    }

    public String getPono() {
        return pono;
    }

    public void setPono(String pono) {
        this.pono = pono;
    }


    public String getCur() {
        return cur;
    }

    public void setCur(String cur) {
        this.cur = cur;
    }

    public String getIsShowInList() {
        return isShowInList;
    }

    public void setIsShowInList(String isShowInList) {
        this.isShowInList = isShowInList;
    }

   /* public Map<String, Object> getAdditionalProperties() {
        return additionalProperties;
    }

    public void setAdditionalProperties(Map<String, Object> additionalProperties) {
        this.additionalProperties = additionalProperties;
    }*/
}
