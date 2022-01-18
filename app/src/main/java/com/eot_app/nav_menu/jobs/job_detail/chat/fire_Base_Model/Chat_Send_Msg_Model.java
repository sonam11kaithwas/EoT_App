package com.eot_app.nav_menu.jobs.job_detail.chat.fire_Base_Model;

import com.eot_app.utility.App_preference;

public class Chat_Send_Msg_Model {
    private String usrid;
    private String usrnm;
    private String msg;
    private String file;
    private String time;
    private String jobCode;
    private String jobId;
    private String type;

    public Chat_Send_Msg_Model() {
    }

    public Chat_Send_Msg_Model(String msg,
                               String file, String time, String jobCode, String jobId, String type) {
        this.usrid = App_preference.getSharedprefInstance().getLoginRes().getUsrId();
        this.usrnm = App_preference.getSharedprefInstance().getLoginRes().getFnm()
                + " " + App_preference.getSharedprefInstance().getLoginRes().getLnm();
        this.msg = msg;
        this.file = file;
        this.time = time;
        this.jobCode = jobCode;
        this.jobId = jobId;
        this.type = type;
    }

    public String getUsrid() {
        return usrid;
    }

    public void setUsrid(String usrid) {
        this.usrid = usrid;
    }

    public String getUsrnm() {
        return usrnm;
    }

    public void setUsrnm(String usrnm) {
        this.usrnm = usrnm;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public String getFile() {
        return file;
    }

    public void setFile(String file) {
        this.file = file;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getJobCode() {
        return jobCode;
    }

    public void setJobCode(String jobCode) {
        this.jobCode = jobCode;
    }

    public String getJobId() {
        return jobId;
    }

    public void setJobId(String jobId) {
        this.jobId = jobId;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
