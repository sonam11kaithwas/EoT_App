package com.eot_app.nav_menu.jobs.job_detail.invoice2list.itemlist_model;

import android.os.Parcel;
import android.os.Parcelable;

import com.eot_app.nav_menu.jobs.job_detail.addinvoiveitem2pkg.model.InvoiceGroupData;
import com.eot_app.nav_menu.jobs.job_detail.addinvoiveitem2pkg.model.InvoiceItemDataModel;
import com.eot_app.nav_menu.jobs.job_detail.invoice.invoice_detail_pkg.inv_detail_model.ShippingItem;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Sonam-11 on 24/7/20.
 */
public class InvoiceItemDetailsModel implements Parcelable {
    public static final Creator<InvoiceItemDetailsModel> CREATOR = new Creator<InvoiceItemDetailsModel>() {
        @Override
        public InvoiceItemDetailsModel createFromParcel(Parcel in) {
            return new InvoiceItemDetailsModel(in);
        }

        @Override
        public InvoiceItemDetailsModel[] newArray(int size) {
            return new InvoiceItemDetailsModel[size];
        }
    };
    private String invId;
    private String parentId;
    private String compId;
    private String cltId;
    private String jobId;
    private String code;
    private String nm;
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
    private String isShowInList;
    private String shipto;
    private String hsnCodeLable;
    private String taxCalculationType;
    private String contrId;
    private String isRecur;
    private String recurType;
    private String isSendInvoiceEmail;
    private Integer isChildJob;
    private List<InvoiceItemDataModel> itemData = null;
    private List<InvoiceGroupData> groupData = new ArrayList<>();
    private List<ShippingItem> shippingItem = new ArrayList<>();
    private String curSym;
    private String locId;

    public InvoiceItemDetailsModel() {
    }

    protected InvoiceItemDetailsModel(Parcel in) {
        invId = in.readString();
        parentId = in.readString();
        compId = in.readString();
        cltId = in.readString();
        jobId = in.readString();
        code = in.readString();
        nm = in.readString();
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
        isShowInList = in.readString();
        shipto = in.readString();
        hsnCodeLable = in.readString();
        taxCalculationType = in.readString();
        contrId = in.readString();
        isRecur = in.readString();
        recurType = in.readString();
        locId = in.readString();
        isSendInvoiceEmail = in.readString();
        if (in.readByte() == 0) {
            isChildJob = null;
        } else {
            isChildJob = in.readInt();
        }
        itemData = in.createTypedArrayList(InvoiceItemDataModel.CREATOR);
        groupData = in.createTypedArrayList(InvoiceGroupData.CREATOR);
        shippingItem = in.createTypedArrayList(ShippingItem.CREATOR);
        curSym = in.readString();
    }

    public static Creator<InvoiceItemDetailsModel> getCREATOR() {
        return CREATOR;
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

    public String getInvClientAddress() {
        return inv_client_address;
    }

    public void setInvClientAddress(String inv_client_address) {
        this.inv_client_address = inv_client_address;
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

    public String getIsShowInList() {
        return isShowInList;
    }

    public void setIsShowInList(String isShowInList) {
        this.isShowInList = isShowInList;
    }

    public String getShipto() {
        return shipto;
    }

    public void setShipto(String shipto) {
        this.shipto = shipto;
    }

    public String getHsnCodeLable() {
        return hsnCodeLable;
    }

    public void setHsnCodeLable(String hsnCodeLable) {
        this.hsnCodeLable = hsnCodeLable;
    }

    public String getTaxCalculationType() {
        return taxCalculationType;
    }

    public void setTaxCalculationType(String taxCalculationType) {
        this.taxCalculationType = taxCalculationType;
    }

    public String getContrId() {
        return contrId;
    }

    public void setContrId(String contrId) {
        this.contrId = contrId;
    }

    public String getIsRecur() {
        return isRecur;
    }

    public void setIsRecur(String isRecur) {
        this.isRecur = isRecur;
    }

    public String getRecurType() {
        return recurType;
    }

    public void setRecurType(String recurType) {
        this.recurType = recurType;
    }

    public String getIsSendInvoiceEmail() {
        return isSendInvoiceEmail;
    }

    public void setIsSendInvoiceEmail(String isSendInvoiceEmail) {
        this.isSendInvoiceEmail = isSendInvoiceEmail;
    }

    public Integer getIsChildJob() {
        return isChildJob;
    }

    public void setIsChildJob(Integer isChildJob) {
        this.isChildJob = isChildJob;
    }

    public List<InvoiceItemDataModel> getItemData() {
        return itemData;
    }

    public void setItemData(List<InvoiceItemDataModel> itemData) {
        this.itemData = itemData;
    }

    public List<InvoiceGroupData> getGroupData() {
        return groupData;
    }

    public void setGroupData(List<InvoiceGroupData> groupData) {
        this.groupData = groupData;
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
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(invId);
        dest.writeString(parentId);
        dest.writeString(compId);
        dest.writeString(cltId);
        dest.writeString(jobId);
        dest.writeString(code);
        dest.writeString(nm);
        dest.writeString(inv_client_address);
        dest.writeString(pro);
        dest.writeString(discount);
        dest.writeString(total);
        dest.writeString(note);
        dest.writeString(pono);
        dest.writeString(invDate);
        dest.writeString(duedate);
        dest.writeString(createdate);
        dest.writeString(label);
        dest.writeString(paid);
        dest.writeString(cur);
        dest.writeString(invType);
        dest.writeString(isShowInList);
        dest.writeString(shipto);
        dest.writeString(hsnCodeLable);
        dest.writeString(taxCalculationType);
        dest.writeString(contrId);
        dest.writeString(isRecur);
        dest.writeString(locId);
        dest.writeString(recurType);
        dest.writeString(isSendInvoiceEmail);
        if (isChildJob == null) {
            dest.writeByte((byte) 0);
        } else {
            dest.writeByte((byte) 1);
            dest.writeInt(isChildJob);
        }
        dest.writeTypedList(itemData);
        dest.writeTypedList(groupData);
        dest.writeTypedList(shippingItem);
        dest.writeString(curSym);
    }

    public String getLocId() {
        return locId;
    }

    public void setLocId(String locId) {
        this.locId = locId;
    }

    @Override
    public int describeContents() {
        return 0;
    }
}
