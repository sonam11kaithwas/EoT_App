package com.eot_app.nav_menu.admin_fw_chat_pkg.sonam_user_user_chat_pkg.usertouser_model;

/**
 * Created by Sonam-11 on 17/4/20.
 */
public class ChatDocResModel {
    private String attachmentId;
    private String imageName;
    private String userId;
    private String attachFileName;
    private String attachThumnailFileName;
    private String attachFileActualName;
    private String createdate;
    private String name;

    public ChatDocResModel() {
    }

    public ChatDocResModel(String attachmentId, String imageName, String userId, String attachFileName, String attachThumnailFileName, String attachFileActualName, String createdate, String name) {
        this.attachmentId = attachmentId;
        this.imageName = imageName;
        this.userId = userId;
        this.attachFileName = attachFileName;
        this.attachThumnailFileName = attachThumnailFileName;
        this.attachFileActualName = attachFileActualName;
        this.createdate = createdate;
        this.name = name;
    }

    public String getAttachmentId() {
        return attachmentId;
    }

    public void setAttachmentId(String attachmentId) {
        this.attachmentId = attachmentId;
    }

    public String getImageName() {
        return imageName;
    }

    public void setImageName(String imageName) {
        this.imageName = imageName;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
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

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
