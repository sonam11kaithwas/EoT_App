package com.eot_app.nav_menu.jobs.job_detail.customform.cstm_form_model;


import com.eot_app.utility.App_preference;

import java.util.ArrayList;

/**
 * Created by ubuntu on 11/9/18.
 */

public class FormList_Model_Req {
    /***2 For Audit**/
    private final String linkTo;
    int limit;
    int index;
    String compId;
    String jobId;
    String usrId;
    ArrayList<String> jtId;
    private String auditType;

    /**
     * get form list for JOB
     *******/
    public FormList_Model_Req(int index, int limit, String compId, String jobId, ArrayList<String> jtId, String usrId) {
        this.limit = limit;
        this.index = index;
        this.compId = compId;
        this.jobId = jobId;
        this.jtId = jtId;
        this.usrId = usrId;
        this.linkTo = "1";
    }

    public FormList_Model_Req(int index, int limit, String jobId, ArrayList<String> jtId, String auditType) {
        this.limit = limit;
        this.index = index;
        this.compId = App_preference.getSharedprefInstance().getLoginRes().getCompId();
        this.jobId = jobId;
        this.usrId = App_preference.getSharedprefInstance().getLoginRes().getUsrId();
        this.jtId = jtId;
        this.linkTo = "2";
        this.auditType = auditType;
    }

    public FormList_Model_Req(String compId, String jobId, ArrayList<String> jtId, String usrId) {
        this.compId = compId;
        this.jobId = jobId;
        this.jtId = jtId;
        this.usrId = usrId;
        this.linkTo = "1";
    }


    public String getUsrId() {
        return usrId;
    }

    public void setUsrId(String usrId) {
        this.usrId = usrId;
    }

    public int getLimit() {
        return limit;
    }

    public void setLimit(int limit) {
        this.limit = limit;
    }

    public int getIndex() {
        return index;
    }

    public void setIndex(int index) {
        this.index = index;
    }

    public String getCompId() {
        return compId;
    }

    public void setCompId(String compId) {
        this.compId = compId;
    }

    public String getJobId() {
        return jobId;
    }

    public void setJobId(String jobId) {
        this.jobId = jobId;
    }

    public ArrayList<String> getJtId() {
        return jtId;
    }

    public void setJtId(ArrayList<String> jtId) {
        this.jtId = jtId;
    }
}

