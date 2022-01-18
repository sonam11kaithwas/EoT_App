package com.eot_app.nav_menu.audit.audit_list.equipment.model;

import java.io.Serializable;

public class EquipmentStatus implements Serializable {
    private String esId;
    private String statusText;
    private String updatedate;
    private String isDefault;

    public String getEsId() {
        return esId;
    }

    public void setEsId(String esId) {
        this.esId = esId;
    }

    public String getStatusText() {
        return statusText;
    }

    public void setStatusText(String statusText) {
        this.statusText = statusText;
    }

    public String getUpdatedate() {
        return updatedate;
    }

    public void setUpdatedate(String updatedate) {
        this.updatedate = updatedate;
    }

    public String getIsDefault() {
        return isDefault;
    }

    public void setIsDefault(String isDefault) {
        this.isDefault = isDefault;
    }


}
