package com.eot_app.nav_menu.audit.audit_list.scanbarcode.scan_mvp;

import com.eot_app.nav_menu.audit.audit_list.scanbarcode.model.ScanBarcodeRequest;

/**
 * Created by Mahendra Dabi on 13/11/19.
 */
public interface ScanBarcode_PI {
    void searchEquipmentinAudit(ScanBarcodeRequest request);

    void searchAuditWithBarcode(ScanBarcodeRequest request);


    void syncEquipments();
}
