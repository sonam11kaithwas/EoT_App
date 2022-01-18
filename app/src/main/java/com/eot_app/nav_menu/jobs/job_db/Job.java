package com.eot_app.nav_menu.jobs.job_db;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.Index;
import androidx.room.PrimaryKey;
import androidx.room.TypeConverters;

import com.eot_app.nav_menu.audit.audit_list.equipment.model.EquCategoryConvrtr;
import com.eot_app.nav_menu.equipment.model.Keeper;
import com.eot_app.nav_menu.jobs.add_job.JobRecurTypeConvert;
import com.eot_app.nav_menu.jobs.add_job.add_job_recr.RecurReqResModel;
import com.eot_app.nav_menu.jobs.job_detail.addinvoiveitem2pkg.model.InvoiceItemDataModel;
import com.eot_app.nav_menu.jobs.job_detail.addinvoiveitem2pkg.typeconver_pkg.InvoiceItemDataModelConverter;
import com.eot_app.utility.settings.setting_db.TagData;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

/**
 * Created by aplite_pc302 on 6/1/18.
 */
@Entity(indices = {@Index(value = "jobId", unique = true)}) // check user first name is not repeat.
public class Job implements Parcelable {//


    public static final Creator<Job> CREATOR = new Creator<Job>() {
        @Override
        public Job createFromParcel(Parcel in) {
            return new Job(in);
        }

        @Override
        public Job[] newArray(int size) {
            return new Job[size];
        }
    };
    @TypeConverters(SelecetedDaysConverter.class)
    private List<LinkedHashMap<String, Boolean>> selectedDays = new ArrayList<>();
    @TypeConverters(JobRecurTypeConvert.class)
    private List<RecurReqResModel> recurData = new ArrayList<>();
    @PrimaryKey
    @NonNull
    private String jobId;
    private String parentId;
    private String cltId;
    private String siteId;
    private String conId;
    private String quotId;
    private String label;
    private String des;
    private String type;
    private String prty;
    private String athr;
    private String kpr;
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
    private String lat;
    private String lng;
    private String createDate;
    private String updateDate;
    private String status;
    private String extra;
    private String skype;
    private String twitter;
    private String tempId;
    private String landmark;
    private String isRecur;
    private String attachCount = "0";
    @TypeConverters(JtIdConverter.class)
    private List<JtId> jtId = new ArrayList<>();
    @TypeConverters(TagDataConverter.class)
    private List<TagData> tagData = new ArrayList<>();
    private String isdelete;
    @TypeConverters(InvoiceItemDataModelConverter.class)
    private List<InvoiceItemDataModel> itemData = new ArrayList<>();
    private String complNote = "";
    private String canInvoiceCreated;
    @TypeConverters(EquArrayConvrtr.class)
    private List<EquArrayModel> equArray = new ArrayList<>();
    private String contrId;
    @TypeConverters(EquCategoryConvrtr.class)
    private List<String> equCategory = new ArrayList<>();
    @Ignore
    private List<Keeper> keeper = new ArrayList<>();
    private String recurType;
    private String signature;
    private String locId;
    private String quotLabel;

    public Job() {
    }

    protected Job(Parcel in) {
        jobId = in.readString();
        parentId = in.readString();
        cltId = in.readString();
        siteId = in.readString();
        conId = in.readString();
        quotId = in.readString();
        label = in.readString();
        des = in.readString();
        type = in.readString();
        prty = in.readString();
        athr = in.readString();
        kpr = in.readString();
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
        lat = in.readString();
        lng = in.readString();
        createDate = in.readString();
        updateDate = in.readString();
        status = in.readString();
        extra = in.readString();
        skype = in.readString();
        twitter = in.readString();
        tempId = in.readString();
        landmark = in.readString();
        attachCount = in.readString();
        isRecur = in.readString();
        jtId = in.createTypedArrayList(JtId.CREATOR);
        tagData = in.createTypedArrayList(TagData.CREATOR);
        isdelete = in.readString();
        itemData = in.createTypedArrayList(InvoiceItemDataModel.CREATOR);
        complNote = in.readString();
        canInvoiceCreated = in.readString();
        equArray = in.createTypedArrayList(EquArrayModel.CREATOR);
        contrId = in.readString();
        equCategory = in.createStringArrayList();
        keeper = in.createTypedArrayList(Keeper.CREATOR);
        recurType = in.readString();
        signature = in.readString();
        locId = in.readString();
        quotLabel = in.readString();
    }

    public static Creator<Job> getCREATOR() {
        return CREATOR;
    }

    public String getIsRecur() {
        return isRecur;
    }

    public void setIsRecur(String isRecur) {
        this.isRecur = isRecur;
    }

    public List<LinkedHashMap<String, Boolean>> getSelectedDays() {
        return selectedDays;
    }

    public void setSelectedDays(List<LinkedHashMap<String, Boolean>> selectedDays) {
        this.selectedDays = selectedDays;
    }

    public String getRecurType() {
        return recurType;
    }

    public void setRecurType(String recurType) {
        this.recurType = recurType;
    }

    public String getSignature() {
        return signature;
    }

    public void setSignature(String signature) {
        this.signature = signature;
    }

    public List<Keeper> getKeepers() {
        return keeper;
    }

    public void setKeepers(List<Keeper> Keepers) {
        this.keeper = Keepers;
    }

    public List<String> getEquCategory() {
        return equCategory;
    }

    public void setEquCategory(List<String> equCategory) {
        this.equCategory = equCategory;
    }

    public String getContrId() {
        return contrId;
    }

    public void setContrId(String contrId) {
        this.contrId = contrId;
    }

    public List<EquArrayModel> getEquArray() {
        return equArray;
    }

    public void setEquArray(List<EquArrayModel> equArray) {
        this.equArray = equArray;
    }

    public boolean hasTagData(List<TagData> tagList) {
        for (TagData tagData : tagList) {
            if (this.tagData != null && this.getTagData().contains(tagData)) {
                return true;
            }
        }
        return false;
    }

    @NonNull
    public String getJobId() {
        return jobId;
    }

    public void setJobId(@NonNull String jobId) {
        this.jobId = jobId;
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

    public String getQuotId() {
        return quotId;
    }

    public void setQuotId(String quotId) {
        this.quotId = quotId;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
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

    public String getAthr() {
        return athr;
    }

    public void setAthr(String athr) {
        this.athr = athr;
    }

    public String getKpr() {
        return kpr;
    }

    public void setKpr(String kpr) {
        this.kpr = kpr;
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

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getExtra() {
        return extra;
    }

    public void setExtra(String extra) {
        this.extra = extra;
    }

    public String getSkype() {
        return skype;
    }

    public void setSkype(String skype) {
        this.skype = skype;
    }

    public String getTwitter() {
        return twitter;
    }

    public void setTwitter(String twitter) {
        this.twitter = twitter;
    }

    public String getTempId() {
        return tempId;
    }

    public void setTempId(String tempId) {
        this.tempId = tempId;
    }

    public String getLandmark() {
        return landmark;
    }

    public void setLandmark(String landmark) {
        this.landmark = landmark;
    }

    public List<JtId> getJtId() {
        return jtId;
    }

    public void setJtId(List<JtId> jtId) {
        this.jtId = jtId;
    }

    public List<TagData> getTagData() {
        return tagData;
    }

    public void setTagData(List<TagData> tagData) {
        this.tagData = tagData;
    }

    public List<RecurReqResModel> getRecurData() {
        return recurData;
    }

    public void setRecurData(List<RecurReqResModel> recurData) {
        this.recurData = recurData;
    }

    public String getIsdelete() {
        return isdelete;
    }

    public void setIsdelete(String isdelete) {
        this.isdelete = isdelete;
    }

    public List<InvoiceItemDataModel> getItemData() {
        return itemData;
    }

    public void setItemData(List<InvoiceItemDataModel> itemData) {
        this.itemData = itemData;
    }

    public String getComplNote() {
        return complNote;
    }

    public void setComplNote(String complNote) {
        this.complNote = complNote;
    }

    public String getCanInvoiceCreated() {
        return canInvoiceCreated;
    }

    public void setCanInvoiceCreated(String canInvoiceCreated) {
        this.canInvoiceCreated = canInvoiceCreated;
    }

    public String getLocId() {
        return locId;
    }

    public void setLocId(String locId) {
        this.locId = locId;
    }

    @androidx.annotation.NonNull
    @Override
    public String toString() {
        return super.toString();
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(status);
        dest.writeString(jobId);
        dest.writeString(parentId);
        dest.writeString(cltId);
        dest.writeString(siteId);
        dest.writeString(conId);
        dest.writeString(quotId);
        dest.writeString(label);
        dest.writeString(des);
        dest.writeString(type);
        dest.writeString(prty);
        dest.writeString(athr);
        dest.writeString(kpr);
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
        dest.writeString(isRecur);
        dest.writeString(city);
        dest.writeString(state);
        dest.writeString(ctry);
        dest.writeString(zip);
        dest.writeString(lat);
        dest.writeString(lng);
        dest.writeString(createDate);
        dest.writeString(updateDate);
        dest.writeString(extra);
        dest.writeString(skype);
        dest.writeString(twitter);
        dest.writeString(tempId);
        dest.writeString(landmark);
        dest.writeString(attachCount);
        dest.writeTypedList(jtId);
        dest.writeTypedList(tagData);
        dest.writeString(isdelete);
        dest.writeTypedList(itemData);
        dest.writeString(complNote);
        dest.writeString(canInvoiceCreated);
        dest.writeTypedList(equArray);
        dest.writeString(contrId);
        dest.writeStringList(equCategory);
        dest.writeTypedList(keeper);
        dest.writeString(recurType);
        dest.writeString(signature);
        dest.writeString(locId);
        dest.writeString(quotLabel);
    }

    public String getQuotLabel() {
        return quotLabel;
    }

    public void setQuotLabel(String quotLabel) {
        this.quotLabel = quotLabel;
    }

    public String getAttachCount() {
        return attachCount;
    }

    public void setAttachCount(String attachCount) {
        this.attachCount = attachCount;
    }
}
