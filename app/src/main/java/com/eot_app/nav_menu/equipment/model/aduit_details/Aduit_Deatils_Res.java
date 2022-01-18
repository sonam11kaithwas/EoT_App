package com.eot_app.nav_menu.equipment.model.aduit_details;

import android.os.Parcel;
import android.os.Parcelable;

import com.eot_app.nav_menu.equipment.model.aduit_job_history.Auditor;

import java.util.ArrayList;
import java.util.List;

public class Aduit_Deatils_Res implements Parcelable {
    public static final Creator<Aduit_Deatils_Res> CREATOR = new Creator<Aduit_Deatils_Res>() {
        @Override
        public Aduit_Deatils_Res createFromParcel(Parcel in) {
            return new Aduit_Deatils_Res(in);
        }

        @Override
        public Aduit_Deatils_Res[] newArray(int size) {
            return new Aduit_Deatils_Res[size];
        }
    };
    private String audId;
    private String parentId;
    private String label;
    private Object contrLabel;
    private Object contrType;
    private String cltId;
    private String siteId;
    private String conId;
    private String des;
    private String type;
    private String prty;
    private String status;
    private String kpr;
    private String athr;
    private String schdlStart;
    private String schdlFinish;
    private String inst;
    private String nm;
    private String cnm;
    private String snm;
    private String email;
    private String mob1;
    private String mob2;
    private String adr;
    private String city;
    private String state;
    private String ctry;
    private String zip;
    private String createDate;
    private String updateDate;
    private String lat;
    private String lng;
    private String compid;
    private String landmark;
    private String remark;
    private String contrId;
    private String recurType;
    private String isRecur;
    private String auditType;
    private Integer isChildJob;
    private String feedCount;
    private String equipmentCount;
    private String attachmentCount;
    private List<String> equCategory;
    private List<Object> tagData;
    private List<Auditor> auditor = new ArrayList<>();

    protected Aduit_Deatils_Res(Parcel in) {
        audId = in.readString();
        parentId = in.readString();
        label = in.readString();
        cltId = in.readString();
        siteId = in.readString();
        conId = in.readString();
        des = in.readString();
        type = in.readString();
        prty = in.readString();
        status = in.readString();
        kpr = in.readString();
        athr = in.readString();
        schdlStart = in.readString();
        schdlFinish = in.readString();
        inst = in.readString();
        nm = in.readString();
        cnm = in.readString();
        snm = in.readString();
        email = in.readString();
        mob1 = in.readString();
        mob2 = in.readString();
        adr = in.readString();
        city = in.readString();
        state = in.readString();
        ctry = in.readString();
        zip = in.readString();
        createDate = in.readString();
        updateDate = in.readString();
        lat = in.readString();
        lng = in.readString();
        compid = in.readString();
        landmark = in.readString();
        remark = in.readString();
        contrId = in.readString();
        recurType = in.readString();
        isRecur = in.readString();
        auditType = in.readString();
        if (in.readByte() == 0) {
            isChildJob = null;
        } else {
            isChildJob = in.readInt();
        }
        feedCount = in.readString();
        equipmentCount = in.readString();
        attachmentCount = in.readString();
        equCategory = in.createStringArrayList();
        auditor = in.createTypedArrayList(Auditor.CREATOR);
    }

    public String getAudId() {
        return audId;
    }

    public void setAudId(String audId) {
        this.audId = audId;
    }

    public String getParentId() {
        return parentId;
    }

    public void setParentId(String parentId) {
        this.parentId = parentId;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public Object getContrLabel() {
        return contrLabel;
    }

    public void setContrLabel(Object contrLabel) {
        this.contrLabel = contrLabel;
    }

    public Object getContrType() {
        return contrType;
    }

    public void setContrType(Object contrType) {
        this.contrType = contrType;
    }

    public String getCltId() {
        return cltId;
    }

    public void setCltId(String cltId) {
        this.cltId = cltId;
    }

    public String getSiteId() {
        return siteId;
    }

    public void setSiteId(String siteId) {
        this.siteId = siteId;
    }

    public String getConId() {
        return conId;
    }

    public void setConId(String conId) {
        this.conId = conId;
    }

    public String getDes() {
        return des;
    }

    public void setDes(String des) {
        this.des = des;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getPrty() {
        return prty;
    }

    public void setPrty(String prty) {
        this.prty = prty;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getKpr() {
        return kpr;
    }

    public void setKpr(String kpr) {
        this.kpr = kpr;
    }

    public String getAthr() {
        return athr;
    }

    public void setAthr(String athr) {
        this.athr = athr;
    }

    public String getSchdlStart() {
        return schdlStart;
    }

    public void setSchdlStart(String schdlStart) {
        this.schdlStart = schdlStart;
    }

    public String getSchdlFinish() {
        return schdlFinish;
    }

    public void setSchdlFinish(String schdlFinish) {
        this.schdlFinish = schdlFinish;
    }

    public String getInst() {
        return inst;
    }

    public void setInst(String inst) {
        this.inst = inst;
    }

    public String getNm() {
        return nm;
    }

    public void setNm(String nm) {
        this.nm = nm;
    }

    public String getCnm() {
        return cnm;
    }

    public void setCnm(String cnm) {
        this.cnm = cnm;
    }

    public String getSnm() {
        return snm;
    }

    public void setSnm(String snm) {
        this.snm = snm;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getMob1() {
        return mob1;
    }

    public void setMob1(String mob1) {
        this.mob1 = mob1;
    }

    public String getMob2() {
        return mob2;
    }

    public void setMob2(String mob2) {
        this.mob2 = mob2;
    }

    public String getAdr() {
        return adr;
    }

    public void setAdr(String adr) {
        this.adr = adr;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getCtry() {
        return ctry;
    }

    public void setCtry(String ctry) {
        this.ctry = ctry;
    }

    public String getZip() {
        return zip;
    }

    public void setZip(String zip) {
        this.zip = zip;
    }

    public String getCreateDate() {
        return createDate;
    }

    public void setCreateDate(String createDate) {
        this.createDate = createDate;
    }

    public String getUpdateDate() {
        return updateDate;
    }

    public void setUpdateDate(String updateDate) {
        this.updateDate = updateDate;
    }

    public String getLat() {
        return lat;
    }

    public void setLat(String lat) {
        this.lat = lat;
    }

    public String getLng() {
        return lng;
    }

    public void setLng(String lng) {
        this.lng = lng;
    }

    public String getCompid() {
        return compid;
    }

    public void setCompid(String compid) {
        this.compid = compid;
    }

    public String getLandmark() {
        return landmark;
    }

    public void setLandmark(String landmark) {
        this.landmark = landmark;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public String getContrId() {
        return contrId;
    }

    public void setContrId(String contrId) {
        this.contrId = contrId;
    }

    public String getRecurType() {
        return recurType;
    }

    public void setRecurType(String recurType) {
        this.recurType = recurType;
    }

    public String getIsRecur() {
        return isRecur;
    }

    public void setIsRecur(String isRecur) {
        this.isRecur = isRecur;
    }

    public String getAuditType() {
        return auditType;
    }

    public void setAuditType(String auditType) {
        this.auditType = auditType;
    }

    public Integer getIsChildJob() {
        return isChildJob;
    }

    public void setIsChildJob(Integer isChildJob) {
        this.isChildJob = isChildJob;
    }

    public List<Auditor> getAuditor() {
        return auditor;
    }

    public void setAuditor(List<Auditor> auditor) {
        this.auditor = auditor;
    }

    public String getFeedCount() {
        return feedCount;
    }

    public void setFeedCount(String feedCount) {
        this.feedCount = feedCount;
    }

    public String getEquipmentCount() {
        return equipmentCount;
    }

    public void setEquipmentCount(String equipmentCount) {
        this.equipmentCount = equipmentCount;
    }

    public String getAttachmentCount() {
        return attachmentCount;
    }

    public void setAttachmentCount(String attachmentCount) {
        this.attachmentCount = attachmentCount;
    }

    public List<String> getEquCategory() {
        return equCategory;
    }

    public void setEquCategory(List<String> equCategory) {
        this.equCategory = equCategory;
    }

    public List<Object> getTagData() {
        return tagData;
    }

    public void setTagData(List<Object> tagData) {
        this.tagData = tagData;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(audId);
        dest.writeString(parentId);
        dest.writeString(label);
        dest.writeString(cltId);
        dest.writeString(siteId);
        dest.writeString(conId);
        dest.writeString(des);
        dest.writeString(type);
        dest.writeString(prty);
        dest.writeString(status);
        dest.writeString(kpr);
        dest.writeString(athr);
        dest.writeString(schdlStart);
        dest.writeString(schdlFinish);
        dest.writeString(inst);
        dest.writeString(nm);
        dest.writeString(cnm);
        dest.writeString(snm);
        dest.writeString(email);
        dest.writeString(mob1);
        dest.writeString(mob2);
        dest.writeString(adr);
        dest.writeString(city);
        dest.writeString(state);
        dest.writeString(ctry);
        dest.writeString(zip);
        dest.writeString(createDate);
        dest.writeString(updateDate);
        dest.writeString(lat);
        dest.writeString(lng);
        dest.writeString(compid);
        dest.writeString(landmark);
        dest.writeString(remark);
        dest.writeString(contrId);
        dest.writeString(recurType);
        dest.writeString(isRecur);
        dest.writeString(auditType);
        if (isChildJob == null) {
            dest.writeByte((byte) 0);
        } else {
            dest.writeByte((byte) 1);
            dest.writeInt(isChildJob);
        }
        dest.writeString(feedCount);
        dest.writeString(equipmentCount);
        dest.writeString(attachmentCount);
        dest.writeStringList(equCategory);
        dest.writeTypedList(auditor);
    }
}
