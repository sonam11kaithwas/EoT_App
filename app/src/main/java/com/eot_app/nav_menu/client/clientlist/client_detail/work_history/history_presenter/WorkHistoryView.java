package com.eot_app.nav_menu.client.clientlist.client_detail.work_history.history_presenter;

import com.eot_app.nav_menu.appointment.dbappointment.Appointment;
import com.eot_app.nav_menu.audit.audit_list.audit_mvp.model.AuditList_Res;
import com.eot_app.nav_menu.jobs.job_db.Job;

import java.util.List;

/**
 * Created by Mahendra Dabi on 12/3/21.
 */
public interface WorkHistoryView {

    void setJobList(List<Job> job);

    void setAuditList(List<AuditList_Res> audit);

    void setAppointmentList(List<Appointment> appointment);

    void setJobDetails(Job job);

    void setAduditDetails(AuditList_Res auditList_res);

    void setAppointmentDetails(Appointment app_details);

    void sessionExpire(String message);

    void showMoreLoadingProgress(boolean b);

}
