package com.eot_app.nav_menu.jobs.job_detail.job_audit_remark_attchment_pkg.mvp;

import com.eot_app.nav_menu.jobs.job_detail.job_audit_remark_attchment_pkg.model_pkg.JobAuditSingleAttchReqModel;

/**
 * Created by Sona-11 on 1/11/21.
 */
public interface JobAudit_PI {
    void uploadAttchmentOnserverForJobAudit(JobAuditSingleAttchReqModel model);
}
