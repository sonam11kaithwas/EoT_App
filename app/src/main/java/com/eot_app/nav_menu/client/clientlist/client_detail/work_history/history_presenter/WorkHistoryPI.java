package com.eot_app.nav_menu.client.clientlist.client_detail.work_history.history_presenter;

import com.eot_app.nav_menu.client.clientlist.client_detail.work_history.ClientWorkHistoryList;

/**
 * Created by Mahendra Dabi on 12/3/21.
 */
public interface WorkHistoryPI {
    void getJobList(String cltId);

    void getAuditList(String cltId);

    void getAppointmentList(String cltId);

    void getAduitDetails(String audId);

    void getJobDetails(String jobId);

    void getAppointmentDetails(String appId);

    void loadMoreItem(ClientWorkHistoryList.FragmentTypes types, String cltId);


}
