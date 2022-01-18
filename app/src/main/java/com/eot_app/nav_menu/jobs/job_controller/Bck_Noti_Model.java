package com.eot_app.nav_menu.jobs.job_controller;

import com.eot_app.nav_menu.jobs.job_detail.chat.fire_Base_Model.Chat_Send_Msg_Model;

import java.util.ArrayList;

class Chat_Bck_Notification_Model {
    private ArrayList<String> usrId;
    private Chat_Send_Msg_Model msg;
    //  private ClientChatReqModel clientChatReqModel;
    private String notyType;
    private String conId;


    public Chat_Bck_Notification_Model(ArrayList<String> usrId, Chat_Send_Msg_Model msg) {
        this.usrId = usrId;
        this.msg = msg;
    }


    public Chat_Bck_Notification_Model(String cltId, Chat_Send_Msg_Model msg, String notyType) {
        this.conId = cltId;
        this.msg = msg;
        this.notyType = notyType;
    }

    public Chat_Bck_Notification_Model(ArrayList<String> usrId, Chat_Send_Msg_Model msg, String notyType) {
        this.usrId = usrId;
        this.msg = msg;
        this.notyType = notyType;
    }

    public ArrayList<String> getUsrId() {
        return usrId;
    }

    public void setUsrId(ArrayList<String> usrId) {
        this.usrId = usrId;
    }

    public Chat_Send_Msg_Model getMsg() {
        return msg;
    }

    public void setMsg(Chat_Send_Msg_Model msg) {
        this.msg = msg;
    }

    public String getNotyType() {
        return notyType;
    }

    public void setNotyType(String notyType) {
        this.notyType = notyType;
    }


}
