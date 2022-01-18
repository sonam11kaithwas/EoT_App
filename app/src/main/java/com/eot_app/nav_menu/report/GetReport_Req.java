package com.eot_app.nav_menu.report;

import com.eot_app.utility.App_preference;

import java.util.ArrayList;

public class GetReport_Req {
    String dtf;
    String dtt;
    ArrayList<String> usrId = new ArrayList<>();


    public GetReport_Req(String dtf, String dtt) {
        usrId.add(App_preference.getSharedprefInstance().getLoginRes().getUsrId());
        this.dtf = dtf;// + " 00:00:00";
        this.dtt = dtt;//+ " 23:59:59";
    }

    public ArrayList<String> getUsrId() {
        return usrId;
    }

    public void setUsrId(ArrayList<String> usrId) {
        this.usrId = usrId;
    }

    public String getDtf() {
        return dtf;
    }

    public void setDtf(String dtf) {
        this.dtf = dtf;
    }

    public String getDtt() {
        return dtt;
    }

    public void setDtt(String dtt) {
        this.dtt = dtt;
    }
}
