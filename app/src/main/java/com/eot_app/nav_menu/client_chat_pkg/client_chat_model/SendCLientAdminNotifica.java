package com.eot_app.nav_menu.client_chat_pkg.client_chat_model;

/**
 * Created by Sona-11 on 20/9/21.
 */
public class SendCLientAdminNotifica {
    private final String jobCode;
    private final String jobId;
    private final String time;
    private final String usrnm;
    private final String type;
    private boolean isClientChat;
    private String msg;

    public SendCLientAdminNotifica(String jobCode, String jobId, String msg, String time, String usrnm, String type) {
        this.jobCode = jobCode;
        this.jobId = jobId;
        this.msg = msg;
        this.time = time;
        this.usrnm = usrnm;
        this.type = type;
    }

    public String getType() {
        return type;
    }

    public boolean isClientChat() {
        return isClientChat;
    }

    public void setIsClientChat(boolean clientChat) {
        isClientChat = clientChat;
    }

    public String getJobCode() {
        return jobCode;
    }

    public String getJobId() {
        return jobId;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public String getTime() {
        return time;
    }

    public String getUsrnm() {
        return usrnm;
    }
}

