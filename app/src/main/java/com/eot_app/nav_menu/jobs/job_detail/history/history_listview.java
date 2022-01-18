package com.eot_app.nav_menu.jobs.job_detail.history;

import java.util.List;

public interface history_listview {
    void setdata(List<History> data);

    void onSessionExpire(String msg);

    void netwrkDialog();
}
