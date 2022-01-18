package com.eot_app.nav_menu.audit.audit_list.documents.fileattach_mvp;

/**
 * Created by Mahendra Dabi on 15/11/19.
 */
public class AuditDocRequest {
    int index;
    int limit;
    String audId;
    String usrId;

    public AuditDocRequest(int index, int limit, String audId, String usrId) {
        this.index = index;
        this.limit = limit;
        this.audId = audId;
        this.usrId = usrId;
    }
}
