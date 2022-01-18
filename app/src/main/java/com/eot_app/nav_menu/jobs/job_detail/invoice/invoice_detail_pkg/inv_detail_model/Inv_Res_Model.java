package com.eot_app.nav_menu.jobs.job_detail.invoice.invoice_detail_pkg.inv_detail_model;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.List;

public class Inv_Res_Model implements Parcelable {

    public static final Creator<Inv_Res_Model> CREATOR = new Creator<Inv_Res_Model>() {
        @Override
        public Inv_Res_Model createFromParcel(Parcel in) {
            return new Inv_Res_Model(in);
        }

        @Override
        public Inv_Res_Model[] newArray(int size) {
            return new Inv_Res_Model[size];
        }
    };
    private String invId;
    private String parentId;
    private String compId;
    private String cltId;
    private String jobId;
    private String code;
    private String nm;
    private String adr;
    private String inv_client_address;
    private String pro;
    private String discount;
    private String total;
    private String note;
    private String pono;
    private String invDate;
    private String duedate;
    private String createdate;
    private String label;
    private String paid;
    private String cur;
    private String invType;
    private List<ItemData> itemData = null;
    private List<ShippingItem> shippingItem = null;
    private String curSym;
    private String[] groupByData = {};
    private String isShowInList;
    private String shipto;
    private String hsnCodeLable;
    private String taxCalculationType = "0";

    protected Inv_Res_Model(Parcel in) {
        invId = in.readString();
        parentId = in.readString();
        compId = in.readString();
        cltId = in.readString();
        jobId = in.readString();
        code = in.readString();
        nm = in.readString();
        adr = in.readString();
        inv_client_address = in.readString();
        pro = in.readString();
        discount = in.readString();
        total = in.readString();
        note = in.readString();
        pono = in.readString();
        invDate = in.readString();
        duedate = in.readString();
        createdate = in.readString();
        label = in.readString();
        paid = in.readString();
        cur = in.readString();
        invType = in.readString();
        itemData = in.createTypedArrayList(ItemData.CREATOR);
        curSym = in.readString();
        groupByData = in.createStringArray();
        isShowInList = in.readString();
        shipto = in.readString();
        hsnCodeLable = in.readString();
        taxCalculationType = in.readString();
    }

    public String getTaxCalculationType() {
        return taxCalculationType;
    }

    public void setTaxCalculationType(String taxCalculationType) {
        this.taxCalculationType = taxCalculationType;
    }

    public String getHsnCodeLable() {
        return hsnCodeLable;
    }

    public void setHsnCodeLable(String hsnCodeLable) {
        this.hsnCodeLable = hsnCodeLable;
    }

    public String getIsShowInList() {
        return isShowInList;
    }

    public void setIsShowInList(String isShowInList) {
        this.isShowInList = isShowInList;
    }

    public String getInv_client_address() {
        return inv_client_address;
    }

    public void setInv_client_address(String inv_client_address) {
        this.inv_client_address = inv_client_address;
    }


    public String[] getGroupByData() {
        return groupByData;
    }

    public void setGroupByData(String[] groupByData) {
        this.groupByData = groupByData;
    }

    public String getInvId() {
        return invId;
    }

    public void setInvId(String invId) {
        this.invId = invId;
    }

    public String getParentId() {
        return parentId;
    }

    public void setParentId(String parentId) {
        this.parentId = parentId;
    }

    public String getCompId() {
        return compId;
    }

    public void setCompId(String compId) {
        this.compId = compId;
    }

    public String getCltId() {
        return cltId;
    }

    public void setCltId(String cltId) {
        this.cltId = cltId;
    }

    public String getJobId() {
        return jobId;
    }

    public void setJobId(String jobId) {
        this.jobId = jobId;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getNm() {
        return nm;
    }

    public void setNm(String nm) {
        this.nm = nm;
    }

    public String getShipto() {
        return shipto;
    }

    public void setShipto(String shipto) {
        this.shipto = shipto;
    }

    public String getAdr() {
        return adr;
    }

    public void setAdr(String adr) {
        this.adr = adr;
    }

    public String getPro() {
        return pro;
    }

    public void setPro(String pro) {
        this.pro = pro;
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

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public String getPono() {
        return pono;
    }

    public void setPono(String pono) {
        this.pono = pono;
    }

    public String getInvDate() {
        return invDate;
    }

    public void setInvDate(String invDate) {
        this.invDate = invDate;
    }

    public String getDuedate() {
        return duedate;
    }

    public void setDuedate(String duedate) {
        this.duedate = duedate;
    }

    public String getCreatedate() {
        return createdate;
    }

    public void setCreatedate(String createdate) {
        this.createdate = createdate;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public String getPaid() {
        return paid;
    }

    public void setPaid(String paid) {
        this.paid = paid;
    }

    public String getCur() {
        return cur;
    }

    public void setCur(String cur) {
        this.cur = cur;
    }

    public String getInvType() {
        return invType;
    }

    public void setInvType(String invType) {
        this.invType = invType;
    }

    public List<ItemData> getItemData() {
        return itemData;
    }

    public void setItemData(List<ItemData> itemData) {
        this.itemData = itemData;
    }

    public List<ShippingItem> getShippingItem() {
        return shippingItem;
    }

    public void setShippingItem(List<ShippingItem> shippingItem) {
        this.shippingItem = shippingItem;
    }

    public String getCurSym() {
        return curSym;
    }

    public void setCurSym(String curSym) {
        this.curSym = curSym;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(invId);
        parcel.writeString(parentId);
        parcel.writeString(compId);
        parcel.writeString(cltId);
        parcel.writeString(jobId);
        parcel.writeString(code);
        parcel.writeString(nm);
        parcel.writeString(adr);
        parcel.writeString(inv_client_address);
        parcel.writeString(pro);
        parcel.writeString(discount);
        parcel.writeString(total);
        parcel.writeString(note);
        parcel.writeString(pono);
        parcel.writeString(invDate);
        parcel.writeString(duedate);
        parcel.writeString(createdate);
        parcel.writeString(label);
        parcel.writeString(paid);
        parcel.writeString(cur);
        parcel.writeString(invType);
        parcel.writeTypedList(itemData);
        parcel.writeString(curSym);
        parcel.writeStringArray(groupByData);
        parcel.writeString(isShowInList);
        parcel.writeString(shipto);
        parcel.writeString(hsnCodeLable);
        parcel.writeString(taxCalculationType);
    }
}
