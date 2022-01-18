package com.eot_app.nav_menu.audit.audit_list.audit_mvp;

import com.eot_app.nav_menu.audit.audit_list.audit_mvp.model.AuditList_Res;

import java.util.List;

/**
 * Created by Mahendra Dabi on 9/11/19.
 */
public interface AuditList_View {

    void setAuditList(List<AuditList_Res> data);

    void onSessionExpired(String message);

    void disableSwipeRefresh();

    void setSearchVisibility(boolean b);

    void showErrorAlertDialog(String message);

    void updateFromApiObserver();

    void setRefereshPullOff();

    void refreshList();

    void onNotificationRedirect();
}
