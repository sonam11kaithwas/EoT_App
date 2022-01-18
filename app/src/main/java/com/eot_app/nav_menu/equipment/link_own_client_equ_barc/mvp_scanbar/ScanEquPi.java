package com.eot_app.nav_menu.equipment.link_own_client_equ_barc.mvp_scanbar;

import com.eot_app.nav_menu.audit.audit_list.scanbarcode.model.ScanBarcodeRequest;
import com.eot_app.nav_menu.jobs.job_db.EquArrayModel;

import java.util.List;

/**
 * Created by Sona-11 on 28/8/21.
 */
public interface ScanEquPi {
    void searchJobWithBarcode(ScanBarcodeRequest request, List<EquArrayModel> myEquList);

}
