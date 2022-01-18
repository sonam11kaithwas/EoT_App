package com.eot_app.login_next.login_next_model;

import com.eot_app.login_next.FooterMenu;
import com.eot_app.nav_menu.audit.audit_list.equipment.model.EquipmentStatus;
import com.eot_app.utility.language_support.Language_Model;
import com.eot_app.utility.language_support.Language_Settings;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by geet-pc on 28/5/18.
 */

public class ResLoginData {
    public List<FooterMenu> footerMenu = null;
    private String usrId;
    private String compId;
    private String fnm;
    private String lnm;
    private String email;
    private String img;
    private String udId;
    private List<Right> rights = new ArrayList<>();
    private String username;
    private String duration;
    private String token;
    private String isCompInfoFill = "0";
    private String jobCurrentTime;
    private String jobSchedule;
    private List<String> adminIds;
    private String isFWgpsEnable;
    private String trkDuration;
    private String trkDistance;
    private String trkStartingHour;
    private String trkEndingHour;
    private String lat;
    private String lng;
    private String isHideContact;
    private String version;
    private String forceupdate_version;
    private String isClientEnable = "0";
    private String isOnetoOneChatEnable = "0";
    private String isActivityLogEnable = "0";
    private String isLock;
    private int isClientVisible;
    private String taxCalculationType;
    private String hsnCodeLable;
    private String ctryCode;
    private String expireStatus;
    private String isCustomFormEnable = "0";
    private String isCustomFieldEnable = "0";
    private String isEquipmentEnable = "0";
    private String isContractEnable = "0";
    private String isEmailLogEnable = "0";
    private String isJobItemQuantityFormEnable = "0";
    private String isItemEditEnable = "0";
    private String isClientForFutureEnable;
    private String isShowRejectStatus;
    private String QuotMailConfimation;
    private String lastCheckIn;

    private ArrayList<String> confirmationTrigger = new ArrayList<>();
    /**
     * all company permission's
     */
    private List<CompPermission> comp_permission = new ArrayList<>();
    private Language_Settings language;
    private List<Language_Model> languageList;
    private String checkId;
    private String locId;
    private String isHideTravelBtn = "0";
    private int staticJobId;

    private String shiftStartTime;
    private String shiftEndTime;
    private String isItemDeleteEnable;
    /**
     * equipment status list
     */
    private List<EquipmentStatus> equipmentStatus = new ArrayList<>();
    private String is24hrFormatEnable;

    public ArrayList<String> getConfirmationTrigger() {
        return confirmationTrigger;
    }

    public String getIsClientForFutureEnable() {
        return isClientForFutureEnable;
    }

    public String getIsEmailLogEnable() {
        return isEmailLogEnable;
    }

    public String getIsActivityLogEnable() {
        return isActivityLogEnable;
    }

    public String getIsHideTravelBtn() {
        return isHideTravelBtn;
    }

    public int getStaticJobId() {
        return staticJobId;
    }

    public Language_Settings getLanguage() {
        return language;
    }

    public List<Language_Model> getLanguageList() {
        return languageList;
    }

    public String getIsShowRejectStatus() {
        return isShowRejectStatus;
    }

    public List<CompPermission> getCompPermission() {
        return comp_permission;
    }

    public String getIsHideContact() {
        return isHideContact;
    }

    public String getIsClientEnable() {
        return isClientEnable;
    }

    public String getIsCustomFormEnable() {
        return isCustomFormEnable;
    }

    public String getIsOnetoOneChatEnable() {
        return isOnetoOneChatEnable;
    }

    public String getForceupdate_version() {
        return forceupdate_version;
    }

    public String getLat() {
        return lat;
    }

    public String getLng() {
        return lng;
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

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public String getCheckId() {
        return checkId;
    }

    public void setCheckId(String checkId) {
        this.checkId = checkId;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getDuration() {
        return duration;
    }

    public String getUsrId() {
        return usrId;
    }

    public String getCompId() {
        return compId;
    }

    public String getFnm() {
        return fnm;
    }

    public String getLnm() {
        return lnm;
    }

    public String getEmail() {
        return email;
    }

    public String getImg() {
        return img;
    }

    public String getUdId() {
        return udId;
    }

    public List<Right> getRights() {
        return rights;
    }

    public String getJobCurrentTime() {
        return jobCurrentTime;
    }

    public String getJobSchedule() {
        return jobSchedule;
    }

    public String getTrkStartingHour() {
        return trkStartingHour;
    }

    public String getCustomFormEnable() {
        return isCustomFormEnable;
    }

    public String getTrkEndingHour() {
        return trkEndingHour;
    }

    public List<EquipmentStatus> getEquipmentStatus() {
        return equipmentStatus;
    }

    /**
     * set Login responce Data
     *****/
    public void setMobileLoginData(ResMobileLogin resMobileLogin) {
        if (resMobileLogin != null) {
            this.usrId = resMobileLogin.getUsrId();
            this.compId = resMobileLogin.getCompId();
            this.fnm = resMobileLogin.getFnm();
            this.lnm = resMobileLogin.getLnm();
            this.email = resMobileLogin.getEmail();
            this.img = resMobileLogin.getImg();
            this.udId = resMobileLogin.getUdId();
            this.username = resMobileLogin.getUsername();
            this.token = resMobileLogin.getToken();
            this.isCompInfoFill = resMobileLogin.getIsCompInfoFill();
        }
    }

    public String getIsCompInfoFill() {
        return isCompInfoFill;
    }

    public void setIsCompInfoFill(String isCompInfoFill) {
        this.isCompInfoFill = isCompInfoFill;
    }

    public String getExpireStatus() {
        return expireStatus;
    }

    public String getTaxCalculationType() {
        return taxCalculationType;
    }

    public String getHsnCodeLable() {
        return hsnCodeLable;
    }

    public String getCtryCode() {
        return ctryCode;
    }

    /**
     * set Mobile Setting Data
     *****/
    public void setMobileDefaultSettings(MobileDefaultSettings mobileDefaultSettings) {
        this.rights = mobileDefaultSettings.getRights();
        this.duration = mobileDefaultSettings.getDuration();
        this.jobCurrentTime = mobileDefaultSettings.getJobCurrentTime();
        this.jobSchedule = mobileDefaultSettings.getJobSchedule();
        this.isFWgpsEnable = mobileDefaultSettings.getIsFWgpsEnable();
        this.trkDuration = mobileDefaultSettings.getTrkDuration();
        this.trkDistance = mobileDefaultSettings.getTrkDistance();
        this.adminIds = mobileDefaultSettings.getAdminIds();
        this.isHideContact = mobileDefaultSettings.getIsHideContact();
        this.lat = mobileDefaultSettings.getLat();
        this.lng = mobileDefaultSettings.getLng();
        this.comp_permission = mobileDefaultSettings.getCompPermission();
        this.language = mobileDefaultSettings.getLanguage();
        this.languageList = mobileDefaultSettings.getLanguageList();
        this.checkId = mobileDefaultSettings.getCheckId();
        this.version = mobileDefaultSettings.getVersion();
        this.forceupdate_version = mobileDefaultSettings.getForceupdateVersion();
        this.isHideTravelBtn = mobileDefaultSettings.getIsHideTravelBtn();
        this.staticJobId = mobileDefaultSettings.getStaticJobId();
        this.trkStartingHour = mobileDefaultSettings.getTrkStartingHour();
        this.trkEndingHour = mobileDefaultSettings.getTrkEndingHour();
        this.isClientEnable = mobileDefaultSettings.getIsClientChatEnable();
        this.isOnetoOneChatEnable = mobileDefaultSettings.getIsOnetoOneChatEnable();
        this.isActivityLogEnable = mobileDefaultSettings.getIsActivityLogEnable();
        this.version = mobileDefaultSettings.getVersion();
        this.taxCalculationType = mobileDefaultSettings.getTaxCalculationType();
        this.hsnCodeLable = mobileDefaultSettings.getHsnCodeLable();
        this.ctryCode = mobileDefaultSettings.getCtryCode();
        this.expireStatus = mobileDefaultSettings.getExpireStatus();
        this.isCustomFormEnable = mobileDefaultSettings.getIsCustomFormEnable();
        this.isCustomFieldEnable = mobileDefaultSettings.getIsCustomFieldEnable();
        this.isEquipmentEnable = mobileDefaultSettings.getIsEquipmentEnable();
        this.isContractEnable = mobileDefaultSettings.getIsContractEnable();
        this.isEmailLogEnable = mobileDefaultSettings.getIsEmailLogEnable();
        this.footerMenu = mobileDefaultSettings.getFooterMenu();
        this.isJobItemQuantityFormEnable = mobileDefaultSettings.getIsJobItemQuantityFormEnable();
        this.equipmentStatus = mobileDefaultSettings.getEquipmentStatus();
        this.isItemDeleteEnable = mobileDefaultSettings.getIsItemDeleteEnable();
        this.isItemEditEnable = mobileDefaultSettings.getIsItemEditEnable();
        this.isClientForFutureEnable = mobileDefaultSettings.getIsClientForFutureEnable();
        this.isShowRejectStatus = mobileDefaultSettings.getIsShowRejectStatus();
        this.is24hrFormatEnable = mobileDefaultSettings.getIs24hrFormatEnable();
        this.locId = mobileDefaultSettings.getLocId();
        this.QuotMailConfimation = mobileDefaultSettings.getQuotMailConfimation();
        this.confirmationTrigger = mobileDefaultSettings.getConfirmationTrigger();
        this.shiftStartTime = mobileDefaultSettings.getShiftStartTime();
        this.shiftEndTime = mobileDefaultSettings.getShiftEndTime();
        this.lastCheckIn = mobileDefaultSettings.getLastCheckIn();


    }

    public String getShiftStartTime() {
        return shiftStartTime;
    }

    public String getShiftEndTime() {
        return shiftEndTime;
    }

    public String getQuotMailConfimation() {
        return QuotMailConfimation;
    }

    public String getLocId() {
        return locId;
    }

    public String getIs24hrFormatEnable() {
        return is24hrFormatEnable;
    }

    public String getIsItemEditEnable() {
        return isItemEditEnable;
    }


    public String getIsItemDeleteEnable() {
        return isItemDeleteEnable;
    }

    public String getIsCustomFieldEnable() {
        return isCustomFieldEnable;
    }

    public List<FooterMenu> getFooterMenu() {
        return footerMenu;
    }

    public void setFooterMenu(List<FooterMenu> footerMenu) {
        this.footerMenu = footerMenu;
    }

    public String getIsEquipmentEnable() {
        return isEquipmentEnable;
    }

    public String getIsContractEnable() {
        return isContractEnable;
    }

    public String getIsJobItemQuantityFormEnable() {
        return isJobItemQuantityFormEnable;
    }

    public void setIsJobItemQuantityFormEnable(String isJobItemQuantityFormEnable) {
        this.isJobItemQuantityFormEnable = isJobItemQuantityFormEnable;
    }

    public String getLastCheckIn() {
        return lastCheckIn;
    }
}
