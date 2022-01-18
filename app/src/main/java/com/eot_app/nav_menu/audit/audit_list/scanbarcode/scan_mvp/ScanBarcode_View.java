package com.eot_app.nav_menu.audit.audit_list.scanbarcode.scan_mvp;


import com.eot_app.nav_menu.audit.audit_list.audit_mvp.model.AuditList_Res;
import com.eot_app.nav_menu.audit.audit_list.equipment.model.Equipment_Res;
import com.eot_app.nav_menu.jobs.job_db.Job;
import com.eot_app.utility.settings.equipmentdb.Equipment;

import java.util.List;

/**
 * Created by Mahendra Dabi on 13/11/19.
 */
public interface ScanBarcode_View {
    void onEquipmentFound(Equipment_Res equipmentRes);

//    void onJobEquipmentFound(EquArrayModel equipmentRes);

    void onEquipmentFoundButNotLinked(Equipment equipment);

    void onRecordFound(List<Job> jobList, List<AuditList_Res> list);


    void onSessionExpired(String msg);
}
