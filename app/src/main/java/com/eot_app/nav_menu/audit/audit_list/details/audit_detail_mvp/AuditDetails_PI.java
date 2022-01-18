package com.eot_app.nav_menu.audit.audit_list.details.audit_detail_mvp;

import com.eot_app.nav_menu.audit.audit_list.audit_mvp.model.AuditStatusRequest;

/**
 * Created by Mahendra Dabi on 11/11/19.
 */
public interface AuditDetails_PI {

    void updateAuditStatus(AuditStatusRequest request);

    void getCustomFieldQues(String jobId);

    void getQuestByParntId(String formId, String jobId);
}
