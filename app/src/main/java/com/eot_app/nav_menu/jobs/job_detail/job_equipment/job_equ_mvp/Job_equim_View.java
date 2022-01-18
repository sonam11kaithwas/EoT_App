package com.eot_app.nav_menu.jobs.job_detail.job_equipment.job_equ_mvp;

import com.eot_app.nav_menu.jobs.job_db.EquArrayModel;

import java.util.List;

/**
 * Created by Sonam-11 on 22/9/20.
 */
public interface Job_equim_View {
    void setEuqipmentList(List<EquArrayModel> equArray);

    void onSessionExpired(String msg);

    void showErrorAlertDialog(String message);

    void swipeRefresh();
}
