package com.eot_app.nav_menu.audit.audit_list.equipment.equipment_mvp;

/**
 * Created by Mahendra Dabi on 11/11/19.
 */
public interface Equipment_PI {
    void getEquipmentList(String auditId);

    void refreshList(String auditID);

    void getEquipmentBySiteName(String auditID, String snm);

}
