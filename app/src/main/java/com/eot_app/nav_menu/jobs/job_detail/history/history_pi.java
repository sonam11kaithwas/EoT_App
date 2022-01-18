package com.eot_app.nav_menu.jobs.job_detail.history;


import java.util.List;

public interface history_pi {
    void getHistoryList();


    void loadHsitoryFromServer();

    void addRecordsToDB(List<History> data);

    void getJobId(String jobId);
}
