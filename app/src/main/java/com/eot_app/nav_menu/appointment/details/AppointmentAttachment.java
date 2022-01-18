package com.eot_app.nav_menu.appointment.details;

import java.io.Serializable;

public class AppointmentAttachment implements Serializable {
    private String attachmentId;
    private String appId;
    private String deleteTable;
    private String imageName;
    private String attachFileName;
    private String attachThumnailFileName;
    private String attachFileActualName;
    private String createdate;
    private String des;
    private int type;
    private boolean isSelected;


    public boolean isSelected() {
        return isSelected;
    }

    public void setSelected(boolean selected) {
        isSelected = selected;
    }

    public String getDes() {
        return des;
    }

    public void setDes(String des) {
        this.des = des;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getAttachmentId() {
        return attachmentId;
    }

    public void setAttachmentId(String attachmentId) {
        this.attachmentId = attachmentId;
    }

    public String getAppId() {
        return appId;
    }

    public void setAppId(String appId) {
        this.appId = appId;
    }

    public String getDeleteTable() {
        return deleteTable;
    }

    public void setDeleteTable(String deleteTable) {
        this.deleteTable = deleteTable;
    }

    public String getImageName() {
        return imageName;
    }

    public void setImageName(String imageName) {
        this.imageName = imageName;
    }

    public String getAttachFileName() {
        return attachFileName;
    }

    public void setAttachFileName(String attachFileName) {
        this.attachFileName = attachFileName;
    }

    public String getAttachThumnailFileName() {
        return attachThumnailFileName;
    }

    public void setAttachThumnailFileName(String attachThumnailFileName) {
        this.attachThumnailFileName = attachThumnailFileName;
    }

    public String getAttachFileActualName() {
        return attachFileActualName;
    }

    public void setAttachFileActualName(String attachFileActualName) {
        this.attachFileActualName = attachFileActualName;
    }

    public String getCreatedate() {
        return createdate;
    }

    public void setCreatedate(String createdate) {
        this.createdate = createdate;
    }


}
