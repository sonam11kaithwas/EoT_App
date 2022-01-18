package com.eot_app.nav_menu.admin_fw_chat_pkg.sonam_user_user_chat_pkg.usertouser_model;

import com.eot_app.utility.AppUtility;
import com.eot_app.utility.App_preference;

import java.io.Serializable;

/**
 * Created by Sonam-11 on 7/4/20.
 */
public class MsgModel implements Serializable {
    private String senderId;
    private String createdAt;
    private String content;
    private String doc;
    private String senderNm;

    public MsgModel(String senderId, String content) {//String createdAt,
        this.senderId = senderId;
        this.createdAt = AppUtility.getDateByMiliseconds();
        this.content = content;
        this.senderNm = App_preference.getSharedprefInstance().getLoginRes().getFnm() + " " + App_preference.getSharedprefInstance().getLoginRes().getLnm();
    }

    public MsgModel(String senderId, String createdAt, String doc) {
        this.senderId = senderId;
        this.createdAt = createdAt;
        this.doc = doc;
        this.senderNm = App_preference.getSharedprefInstance().getLoginRes().getFnm() + " " + App_preference.getSharedprefInstance().getLoginRes().getLnm();
    }

    public MsgModel() {
    }

    public String getDoc() {
        return doc;
    }

    public void setDoc(String doc) {
        this.doc = doc;
    }

    public String getSenderId() {
        return senderId;
    }

    public void setSenderId(String senderId) {
        this.senderId = senderId;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getSenderNm() {
        return senderNm;
    }

    public void setSenderNm(String senderNm) {
        this.senderNm = senderNm;
    }
}

