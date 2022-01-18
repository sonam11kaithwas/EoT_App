package com.eot_app.nav_menu.equipment.link_own_client_equ_barc.mvp_scanbar;

import com.eot_app.nav_menu.jobs.job_db.EquArrayModel;

/**
 * Created by Sona-11 on 28/8/21.
 */
public interface ScanEquView {
    void onJobEquipmentFound(EquArrayModel equipmentRes);

    void onSessionExpired(String msg);

}
