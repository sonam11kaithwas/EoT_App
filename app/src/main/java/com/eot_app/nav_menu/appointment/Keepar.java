package com.eot_app.nav_menu.appointment;

import java.io.Serializable;

public class Keepar implements Serializable {
    String usrId;
    String status;

    public String getUsrId() {
        return usrId;
    }

    public void setUsrId(String usrId) {
        this.usrId = usrId;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
