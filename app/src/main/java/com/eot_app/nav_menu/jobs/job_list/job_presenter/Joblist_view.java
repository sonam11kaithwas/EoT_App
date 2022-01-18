package com.eot_app.nav_menu.jobs.job_list.job_presenter;

import com.eot_app.nav_menu.jobs.job_db.EquArrayModel;
import com.eot_app.nav_menu.jobs.job_db.Job;
import com.eot_app.utility.DropdownListBean;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by aplite_pc302 on 6/1/18.
 */

public interface Joblist_view {
    void setdata(List<Job> data, int visibilityFlag);

    void updateFromApiObserver();

    void onSessionExpired(String msg);

    void filterListByChip(ArrayList<DropdownListBean> listBean, int visibilityFlag);

    void chipAdd(ArrayList<DropdownListBean> listBeans);

    void refreshchangesFromLocalDB();

    void setSearchVisibility(boolean b);

    void setRefereshPullOff();

    void onEquipmentFound(EquArrayModel equipment);
}
