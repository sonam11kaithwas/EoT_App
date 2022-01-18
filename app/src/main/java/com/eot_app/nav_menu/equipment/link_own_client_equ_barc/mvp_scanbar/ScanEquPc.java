package com.eot_app.nav_menu.equipment.link_own_client_equ_barc.mvp_scanbar;

import com.eot_app.nav_menu.audit.audit_list.scanbarcode.model.ScanBarcodeRequest;
import com.eot_app.nav_menu.jobs.job_db.EquArrayModel;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Sona-11 on 28/8/21.
 */
public class ScanEquPc implements ScanEquPi {
    private final ScanEquView scanEquView;

    public ScanEquPc(ScanEquView scanEquView) {
        this.scanEquView = scanEquView;
    }

    @Override
    public void searchJobWithBarcode(ScanBarcodeRequest request, List<EquArrayModel> myEquList) {
        if (myEquList != null) {
            boolean isEquipmentFound = false;
            List<EquArrayModel> equArray = new ArrayList<>();
            equArray = myEquList;
            if (equArray != null) {
                for (EquArrayModel equipment : equArray) {
                    if (equipment.getSno() != null && equipment.getSno().equals(request.getBarCode()) ||
                            equipment.getBarcode() != null && equipment.getBarcode().equals(request.getBarCode())) {
                        isEquipmentFound = true;
                        scanEquView.onJobEquipmentFound(equipment);
                        break;
                    }
                }
            }
            if (!isEquipmentFound) scanEquView.onJobEquipmentFound(null);
        } else {
            scanEquView.onJobEquipmentFound(null);
        }
    }
}
