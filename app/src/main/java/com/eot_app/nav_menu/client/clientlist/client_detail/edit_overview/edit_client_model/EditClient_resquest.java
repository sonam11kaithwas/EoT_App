package com.eot_app.nav_menu.client.clientlist.client_detail.edit_overview.edit_client_model;

/**
 * Created by aplite_pc302 on 6/18/18.
 */

public class EditClient_resquest {
    String cltId;
    String nm;
    String pymtType;
    String gstNo;
    String tinNo;
    String industry;
    String note;
    int isactive;
    String lat;
    String lng;
    String referral;

    public EditClient_resquest(String cltId, String nm, String pymtType, String gstNo, String tinNo, String industry, String note, int isActive, String lat, String lng
            , String referral) {
        this.cltId = cltId;
        this.nm = nm;
        this.pymtType = pymtType;
        this.gstNo = gstNo;
        this.tinNo = tinNo;
        this.industry = industry;
        this.note = note;
        this.isactive = isActive;
        this.lat = lat;
        this.lng = lng;
        this.referral = referral;
    }
}
