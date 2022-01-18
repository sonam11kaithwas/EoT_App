package com.eot_app.nav_menu.jobs.job_detail.job_audit_remark_attchment_pkg.mvp;

/**
 * Created by Sona-11 on 1/11/21.
 */
public interface JobAudit_View {
    void onErrorMsg(String s);

    void onSessionExpire(String message);

    void attchmentUpload(String convert);
}
