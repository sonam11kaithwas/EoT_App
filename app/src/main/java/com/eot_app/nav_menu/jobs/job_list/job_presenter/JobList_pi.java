package com.eot_app.nav_menu.jobs.job_list.job_presenter;

import android.view.View;

import com.eot_app.nav_menu.jobs.job_db.Job;
import com.eot_app.nav_menu.jobs.job_list.JobFilterModel;
import com.eot_app.utility.settings.setting_db.TagData;

import java.util.List;
import java.util.Set;

/**
 * Created by aplite_pc302 on 6/1/18.
 */

public interface JobList_pi {
    void getJobList();

    //    void loadFromDb();
    void loadFromServer();

    void addRecordsToDB(List<Job> data);

    void filterDataFromStatus(String status, boolean check);

    void expand(View cv_filter, int i, int i1);

    void collapse(View cv_filter, int i, int i1);

    void clearfiletrlist();

    void rorateClockwise(View view);

    void rorateAntiClockwise(View view);

    void getFilterListByStatus_Prity(List<TagData> tagDataList, Set<String> status_Id_List, Set<String> prity_Id_List);

    void setSoringByDate(boolean isListSortByStartDate);

    void laodRecordsFromDB();

    void getJobListByFilter(JobFilterModel name);

    void getFilterJobList(JobFilterModel jobFilterModel, int visibilityFlag);


    void setSearchOnTextChange(String searchString);

    void searchEquipment(String jobId, String barcode);

    //void loadJobForAdnewUpdate();
}
