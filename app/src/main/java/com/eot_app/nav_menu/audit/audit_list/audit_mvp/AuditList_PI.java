package com.eot_app.nav_menu.audit.audit_list.audit_mvp;


/**
 * Created by Mahendra Dabi on 9/11/19.
 */
public interface AuditList_PI {
    void getAuditList();

    void getAudit();

    void getAuditListBySearch(String text);

    void updatedAuditAtRefresh();
}
