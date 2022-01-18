package com.eot_app.nav_menu.equipment.linkequip.linkMVP;

import com.eot_app.nav_menu.audit.audit_list.equipment.model.EquipmentStatus;
import com.eot_app.nav_menu.jobs.job_db.EquArrayModel;

import java.util.List;

public interface LinkEquipmentView {

    void setEquipmentList(List<EquArrayModel> list);

    void showHideProgressBar(boolean isShowProgress);

    void refreshEquipmentList();

    void onSessionExpired(String msg);

    void setEquStatusList(List<EquipmentStatus> list);

    void updateLinkUnlinkEqu();
}
