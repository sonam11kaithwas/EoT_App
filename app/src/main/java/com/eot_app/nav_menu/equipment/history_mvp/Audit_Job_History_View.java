package com.eot_app.nav_menu.equipment.history_mvp;

import com.eot_app.nav_menu.audit.audit_list.audit_mvp.model.AuditList_Res;
import com.eot_app.nav_menu.equipment.model.aduit_job_history.Aduit_Job_History_Res;
import com.eot_app.nav_menu.jobs.job_db.Job;

import java.util.List;

public interface Audit_Job_History_View {

    void setEquipmentAduitList(List<Aduit_Job_History_Res> aduit_res);

    void setEquipmentJobList(List<Aduit_Job_History_Res> aduit_res);

    void getAduitSize(int size);

    void getJobSize(int size);

    void setNetworkError(String message);

    void setJobDetails(Job job);

    void setAduditDetails(AuditList_Res auditList_res);

    void sessionExpire(String msg);

    void finishErroroccur(String msg);

    void uploadAttchView(String msg);

}
