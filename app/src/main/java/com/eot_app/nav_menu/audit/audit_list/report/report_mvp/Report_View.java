package com.eot_app.nav_menu.audit.audit_list.report.report_mvp;


/**
 * Created by Mahendra Dabi on 13/11/19.
 */
public interface Report_View {
    void onSuccess(String message);

    void onSessionExpire(String msg);
}
