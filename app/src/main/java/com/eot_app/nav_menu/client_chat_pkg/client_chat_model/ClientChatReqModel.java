package com.eot_app.nav_menu.client_chat_pkg.client_chat_model;

import com.eot_app.utility.AppUtility;
import com.eot_app.utility.App_preference;

/**
 * Created by Sonam-11 on 2020-01-03.
 */
public class ClientChatReqModel {
    private String usrid;
    private String usrnm;
    private String msg;
    private String file;
    private String time;
    private String jobCode;
    private String jobId;
    private String type;

    /**
     * **Define without-Argument constructor
     */
    public ClientChatReqModel() {
    }

    public ClientChatReqModel(String msg, String file, String jobCode, String jobId, String type) {
        usrid = App_preference.getSharedprefInstance().getLoginRes().getUsrId();
        usrnm = App_preference.getSharedprefInstance().getLoginRes().getFnm() + " " +
                App_preference.getSharedprefInstance().getLoginRes().getLnm();
        this.msg = msg;
        this.file = file;
        time = AppUtility.getDateByMiliseconds();
        this.jobCode = jobCode;
        this.jobId = jobId;
        this.type = type;
    }

    public String getUsrid() {
        return usrid;
    }

    public String getUsrnm() {
        return usrnm;
    }

    public String getMsg() {
        return msg;
    }

    public String getFile() {
        return file;
    }

    public String getTime() {
        return time;
    }

    public String getJobCode() {
        return jobCode;
    }

    public String getJobId() {
        return jobId;
    }

    public String getType() {
        return type;
    }


}
