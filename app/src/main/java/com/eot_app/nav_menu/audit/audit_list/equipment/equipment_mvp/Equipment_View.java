package com.eot_app.nav_menu.audit.audit_list.equipment.equipment_mvp;

import com.eot_app.nav_menu.audit.audit_list.equipment.model.Equipment_Res;

import java.util.List;

/**
 * Created by Mahendra Dabi on 11/11/19.
 */
public interface Equipment_View {

    void setEuqipmentList(List<Equipment_Res> list);

    void onSessionExpired(String msg);

    void showErrorAlertDialog(String message);

    void swipeRefresh();
}
