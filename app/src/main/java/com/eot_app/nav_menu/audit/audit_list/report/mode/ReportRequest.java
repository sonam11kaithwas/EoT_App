package com.eot_app.nav_menu.audit.audit_list.report.mode;

import java.io.File;

/**
 * Created by Mahendra Dabi on 13/11/19.
 */
public class ReportRequest {
    File fileCustomerSign;
    File fileAuditorSign;
    private String usrId;
    private String audId;
    private String des;

    public String getUsrId() {
        return usrId;
    }

    public void setUsrId(String usrId) {
        this.usrId = usrId;
    }

    public String getAudId() {
        return audId;
    }

    public void setAudId(String audId) {
        this.audId = audId;
    }

    public String getDes() {
        return des;
    }

    public void setDes(String des) {
        this.des = des;
    }

    public File getFileCustomerSign() {
        return fileCustomerSign;
    }

    public void setFileCustomerSign(File fileCustomerSign) {
        this.fileCustomerSign = fileCustomerSign;
    }

    public File getFileAuditorSign() {
        return fileAuditorSign;
    }

    public void setFileAuditorSign(File fileAuditorSign) {
        this.fileAuditorSign = fileAuditorSign;
    }
}
