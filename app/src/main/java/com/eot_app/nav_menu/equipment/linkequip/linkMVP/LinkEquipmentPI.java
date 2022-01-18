package com.eot_app.nav_menu.equipment.linkequip.linkMVP;

import com.eot_app.nav_menu.equipment.linkequip.linkMVP.model.ContractEquipmentReq;

import java.util.List;

public interface LinkEquipmentPI {

    void getEquipmentList(String type, String cltId, String audId);

    void getAttachedEquipmentList(String audId, String contrId);

    void addAuditEquipment(List<String> equId, String audId, String contrId);

    void linkUnlinkEquipment(List<String> equId, String audId, String contrId);

    void getContractList(ContractEquipmentReq req);

    void getEquipmentStatus();


}
