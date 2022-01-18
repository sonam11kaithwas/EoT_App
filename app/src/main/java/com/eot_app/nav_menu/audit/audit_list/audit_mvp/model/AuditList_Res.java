package com.eot_app.nav_menu.audit.audit_list.audit_mvp.model;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;
import androidx.room.TypeConverters;

import com.eot_app.nav_menu.audit.audit_list.equipment.model.EquCategoryConvrtr;
import com.eot_app.nav_menu.audit.audit_list.equipment.model.EquipmentTypeConverter;
import com.eot_app.nav_menu.audit.audit_list.equipment.model.Equipment_Res;
import com.eot_app.nav_menu.equipment.model.aduit_job_history.Auditor;
import com.eot_app.nav_menu.jobs.job_db.TagDataConverter;
import com.eot_app.utility.settings.setting_db.TagData;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Mahendra Dabi on 9/11/19.
 */
@Entity(tableName = "AuditList_Res")
public class AuditList_Res implements Parcelable {


    public static final Creator<AuditList_Res> CREATOR = new Creator<AuditList_Res>() {
        @Override
        public AuditList_Res createFromParcel(Parcel in) {
            return new AuditList_Res(in);
        }

        @Override
        public AuditList_Res[] newArray(int size) {
            return new AuditList_Res[size];
        }
    };
    @PrimaryKey
    @NonNull
    private String audId;
    private String parentId;
    private String cltId;
    private String siteId;
    private String conId;
    private String contrId;
    private String label;
    private String nm;
    private String cnm;
    private String snm;
    private String des;
    private String type;
    private String prty;
    private String kpr;
    private String athr;
    private String schdlStart;
    private String schdlFinish;
    private String inst;
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
    private String status;
    private String isdelete;
    private String auditType = "";
    private String tempId;
    private String attachCount = "0";
    @TypeConverters(TagDataConverter.class)
    private List<TagData> tagData = new ArrayList<>();
    @TypeConverters(EquipmentTypeConverter.class)
    private List<Equipment_Res> equArray = new ArrayList<>();
    @TypeConverters(EquCategoryConvrtr.class)
    private List<String> equCategory = new ArrayList<>();
    @Ignore
    private List<Auditor> auditor = new ArrayList<>();

    public AuditList_Res() {
    }

    protected AuditList_Res(Parcel in) {
        audId = in.readString();
        parentId = in.readString();
        cltId = in.readString();
        siteId = in.readString();
        conId = in.readString();
        contrId = in.readString();
        label = in.readString();
        nm = in.readString();
        cnm = in.readString();
        snm = in.readString();
        des = in.readString();
        type = in.readString();
        prty = in.readString();
        kpr = in.readString();
        athr = in.readString();
        schdlStart = in.readString();
        schdlFinish = in.readString();
        inst = in.readString();
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
        status = in.readString();
        isdelete = in.readString();
        auditType = in.readString();
        tagData = in.createTypedArrayList(TagData.CREATOR);
        equArray = in.createTypedArrayList(Equipment_Res.CREATOR);
        equCategory = in.createStringArrayList();
        tempId = in.readString();
        attachCount = in.readString();
        auditor = in.createTypedArrayList(Auditor.CREATOR);

    }

    public static Creator<AuditList_Res> getCREATOR() {
        return CREATOR;
    }

    public List<Auditor> getAuditor() {
        return auditor;
    }

    public void setAuditor(List<Auditor> auditor) {
        this.auditor = auditor;
    }

    @NonNull
    public String getAudId() {
        return audId;
    }

    public void setAudId(@NonNull String audId) {
        this.audId = audId;
    }

    public String getParentId() {
        return parentId;
    }

    public void setParentId(String parentId) {
        this.parentId = parentId;
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

    public String getContrId() {
        return contrId;
    }

    public void setContrId(String contrId) {
        this.contrId = contrId;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
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

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getIsdelete() {
        return isdelete;
    }

    public void setIsdelete(String isdelete) {
        this.isdelete = isdelete;
    }

    public String getAuditType() {
        return auditType;
    }

    public void setAuditType(String auditType) {
        this.auditType = auditType;
    }

    public List<TagData> getTagData() {
        return tagData;
    }

    public void setTagData(List<TagData> tagData) {
        this.tagData = tagData;
    }

    public List<Equipment_Res> getEquArray() {
        return equArray;
    }

    public void setEquArray(List<Equipment_Res> equArray) {
        this.equArray = equArray;
    }

    public List<String> getEquCategory() {
        return equCategory;
    }

    public void setEquCategory(List<String> equCategory) {
        this.equCategory = equCategory;
    }

    public String getTempId() {
        return tempId;
    }

    public void setTempId(String tempId) {
        this.tempId = tempId;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(audId);
        dest.writeString(parentId);
        dest.writeString(cltId);
        dest.writeString(siteId);
        dest.writeString(conId);
        dest.writeString(contrId);
        dest.writeString(label);
        dest.writeString(nm);
        dest.writeString(cnm);
        dest.writeString(snm);
        dest.writeString(des);
        dest.writeString(type);
        dest.writeString(prty);
        dest.writeString(kpr);
        dest.writeString(athr);
        dest.writeString(schdlStart);
        dest.writeString(schdlFinish);
        dest.writeString(inst);
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
        dest.writeString(status);
        dest.writeString(isdelete);
        dest.writeString(auditType);
        dest.writeTypedList(tagData);
        dest.writeTypedList(equArray);
        dest.writeStringList(equCategory);
        dest.writeString(tempId);
        dest.writeString(attachCount);
        dest.writeTypedList(auditor);
    }

    public String getAttachCount() {
        return attachCount;
    }

    public void setAttachCount(String attachCount) {
        this.attachCount = attachCount;
    }

    @Override
    public int describeContents() {
        return 0;
    }
}
