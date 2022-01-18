package com.eot_app.login_next.login_next_model;

import com.eot_app.login_next.FooterMenu;
import com.eot_app.nav_menu.audit.audit_list.equipment.model.EquipmentStatus;
import com.eot_app.utility.language_support.Language_Model;
import com.eot_app.utility.language_support.Language_Settings;

import java.util.ArrayList;
import java.util.List;

public class MobileDefaultSettings {
    private final List<EquipmentStatus> equipmentStatus = new ArrayList<>();
    private final ArrayList<String> confirmationTrigger = new ArrayList<>();
    public List<FooterMenu> footerMenu = new ArrayList<>();
    private String isGpsEnable;
    private String usr_compid;
    private List<Right> rights;
    private String duration;
    private String jobCurrentTime;
    private String jobSchedule;
    private String isFWgpsEnable;
    private String trkDuration;
    private String trkDistance;
    private List<String> adminIds;
    private String isHideContact;
    private String lat;
    private String lng;
    private List<CompPermission> comp_permission;
    private Language_Settings language;
    private List<Language_Model> languageList;
    private String checkId;
    private String version;
    private String forceupdate_version;
    private String isHideTravelBtn;
    private int staticJobId;
    private String isClientChatEnable;
    private String isOnetoOneChatEnable;
    private String isActivityLogEnable;
    private String trkStartingHour;
    private String trkEndingHour;
    private String taxCalculationType;
    private String hsnCodeLable;
    private String ctryCode;
    private String expireStatus;
    private String isCustomFormEnable;
    private String isCustomFieldEnable;
    private String isEquipmentEnable;
    private String isContractEnable;
    private String isEmailLogEnable;
    private String isJobItemQuantityFormEnable;
    private String isItemDeleteEnable;
    private String isItemEditEnable;
    private String isClientForFutureEnable;
    private String isShowRejectStatus;
    private String is24hrFormatEnable;
    private String locId;
    private String QuotMailConfimation;

    private String shiftStartTime;
    private String shiftEndTime;
    private String lastCheckIn;

    public String getShiftStartTime() {
        return shiftStartTime;
    }

    public String getShiftEndTime() {
        return shiftEndTime;
    }

    public ArrayList<String> getConfirmationTrigger() {
        return confirmationTrigger;
    }

    public String getIs24hrFormatEnable() {
        return is24hrFormatEnable;
    }

    public String getIsClientForFutureEnable() {
        return isClientForFutureEnable;
    }

    public String getIsItemEditEnable() {
        return isItemEditEnable;
    }

    public String getIsItemDeleteEnable() {
        return isItemDeleteEnable;
    }

    public List<FooterMenu> getFooterMenu() {
        return footerMenu;
    }

    public String getHsnCodeLable() {
        return hsnCodeLable;
    }

    public String getCtryCode() {
        return ctryCode;
    }

    public void setCtryCode(String ctryCode) {
        this.ctryCode = ctryCode;
    }

    public String getTaxCalculationType() {
        return taxCalculationType;
    }

    public String getIsActivityLogEnable() {
        return isActivityLogEnable;
    }

    public String getIsOnetoOneChatEnable() {
        return isOnetoOneChatEnable;
    }

    public String getIsClientChatEnable() {
        return isClientChatEnable;
    }

    public String getTrkStartingHour() {
        return trkStartingHour;
    }

    public String getTrkEndingHour() {
        return trkEndingHour;
    }

    public int getStaticJobId() {
        return staticJobId;
    }

    public String getIsHideTravelBtn() {
        return isHideTravelBtn;
    }

    public String getIsGpsEnable() {
        return isGpsEnable;
    }

    public String getUsrCompid() {
        return usr_compid;
    }

    public List<Right> getRights() {
        return rights;
    }

    public String getDuration() {
        return duration;
    }

    public String getJobCurrentTime() {
        return jobCurrentTime;
    }

    public String getJobSchedule() {
        return jobSchedule;
    }

    public String getIsFWgpsEnable() {
        return isFWgpsEnable;
    }

    public String getTrkDuration() {
        return trkDuration;
    }

    public String getTrkDistance() {
        return trkDistance;
    }

    public List<String> getAdminIds() {
        return adminIds;
    }


    public String getIsHideContact() {
        return isHideContact;
    }


    public String getLat() {
        return lat;
    }

    public String getLng() {
        return lng;
    }

    public List<CompPermission> getCompPermission() {
        return comp_permission;
    }

    public Language_Settings getLanguage() {
        return language;
    }

    public List<Language_Model> getLanguageList() {
        return languageList;
    }

    public String getCheckId() {
        return checkId;
    }

    public String getVersion() {
        return version;
    }

    public String getForceupdateVersion() {
        return forceupdate_version;
    }

    public String getExpireStatus() {
        return expireStatus;
    }

    public String getIsCustomFormEnable() {
        return isCustomFormEnable;
    }

    public String getIsCustomFieldEnable() {
        return isCustomFieldEnable;
    }

    public String getIsContractEnable() {
        return isContractEnable;
    }

    public String getIsEquipmentEnable() {
        return isEquipmentEnable;
    }

    public String getIsEmailLogEnable() {
        return isEmailLogEnable;
    }

    public String getIsJobItemQuantityFormEnable() {
        return isJobItemQuantityFormEnable;
    }

    public List<EquipmentStatus> getEquipmentStatus() {
        return equipmentStatus;
    }

    public String getLocId() {
        return locId;
    }

    public String getIsShowRejectStatus() {
        return isShowRejectStatus;
    }

    public String getQuotMailConfimation() {
        return QuotMailConfimation;
    }

    public String getLastCheckIn() {
        return lastCheckIn;
    }
}
