package com.eot_app.nav_menu.jobs.job_detail.form_form.get_qus_list.ans_model;

import java.util.ArrayList;

/**
 * Created by ubuntu on 19/9/18.
 */

public class Ans_Req {
    private String usrId;
    private ArrayList<Answer> answer = new ArrayList<>();
    private String frmId;
    private String jobId;
    private String isdelete;
    private String type;
    private String audId;

    public Ans_Req() {
    }

    /****Job Custom Form****/
    public Ans_Req(String usrId, ArrayList<Answer> answer, String frmId, String jobId) {
        this.usrId = usrId;
        this.answer = answer;
        this.frmId = frmId;
        this.jobId = jobId;
        this.type = "2";
        this.isdelete = "1";
    }

    public Ans_Req(ArrayList<Answer> answerArrayList, String frmId, String jobId) {
        this.answer = answerArrayList;
        this.frmId = frmId;
        this.jobId = jobId;
        this.type = "1";
        this.isdelete = "1";
    }

    /*****AUDIT Custom field****/
    public Ans_Req(ArrayList<Answer> answerArrayList, String frmId) {
        this.answer = answerArrayList;
        this.frmId = frmId;
        this.type = "1";
        this.isdelete = "1";
    }

    public String getAudId() {
        return audId;
    }

    public void setAudId(String audId) {
        this.audId = audId;
    }

    public String getIsdelete() {
        return isdelete;
    }

    public String getType() {
        return type;
    }

    public String getJobId() {
        return jobId;
    }

    public void setJobId(String jobId) {
        this.jobId = jobId;
    }

    public String getUsrId() {
        return usrId;
    }

    public void setUsrId(String usrId) {
        this.usrId = usrId;
    }

    public String getFrmId() {
        return frmId;
    }

    public void setFrmId(String frmId) {
        this.frmId = frmId;
    }

    public ArrayList<Answer> getAnswer() {
        return answer;
    }

    public void setAnswer(ArrayList<Answer> answer) {
        this.answer = answer;
    }
}
