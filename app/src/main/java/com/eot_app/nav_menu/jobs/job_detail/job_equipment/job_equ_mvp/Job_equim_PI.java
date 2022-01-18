package com.eot_app.nav_menu.jobs.job_detail.job_equipment.job_equ_mvp;

/**
 * Created by Sonam-11 on 22/9/20.
 */
public interface Job_equim_PI {
    void getEquipmentList(String auditId);

    void refreshList(String auditID, String jobId);

    void getEquipmentBySiteName(String jobId, String siteNAme);
}
