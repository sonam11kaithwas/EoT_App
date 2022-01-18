package com.eot_app.nav_menu.jobs.job_list.job_presenter;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.util.Log;
import android.view.View;
import android.view.animation.DecelerateInterpolator;

import com.eot_app.activitylog.ActivityLogController;
import com.eot_app.activitylog.LogModel;
import com.eot_app.nav_menu.jobs.job_controller.ChatController;
import com.eot_app.nav_menu.jobs.job_db.EquArrayModel;
import com.eot_app.nav_menu.jobs.job_db.Job;
import com.eot_app.nav_menu.jobs.job_db.JobListRequestModel;
import com.eot_app.nav_menu.jobs.job_list.JobFilterModel;
import com.eot_app.services.ApiClient;
import com.eot_app.services.Service_apis;
import com.eot_app.utility.AppConstant;
import com.eot_app.utility.AppUtility;
import com.eot_app.utility.App_preference;
import com.eot_app.utility.EotApp;
import com.eot_app.utility.db.AppDataBase;
import com.eot_app.utility.language_support.LanguageController;
import com.eot_app.utility.settings.setting_db.TagData;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;


/**
 * Created by aplite_pc302 on 6/1/18.
 */

public class JobList_pc implements JobList_pi {
    private final String UPDATED_DATE_FIELD = "updateDate";
    private final String START_DATE_FIELD = "schdlStart";
    private final Joblist_view joblist_view;
    private final int updatelimit;
    private final boolean isFromScan;
    private String sortedBy = UPDATED_DATE_FIELD;
    private List<String> filter;
    private int count;
    private int updateindex;
    private JobFilterModel jobFilterModel;
    private int visibilityFlag = 0;

    public JobList_pc(Joblist_view joblist_view, boolean isFromScan) {
        this.isFromScan = isFromScan;
        this.joblist_view = joblist_view;
        this.updatelimit = AppConstant.LIMIT_HIGH;
        jobFilterModel = new JobFilterModel();
        this.updateindex = 0;
    }

    @Override
    public void getJobList() {
// get Records from database
        loadFromServer();

        List<Job> data = new ArrayList<>();
        if (sortedBy.equals(START_DATE_FIELD)) {
            data = AppDataBase.getInMemoryDatabase(EotApp.getAppinstance()).jobModel().getJoblistByScheduleStart();
        } else {
            data = AppDataBase.getInMemoryDatabase(EotApp.getAppinstance()).jobModel().getJoblist();
        }

        joblist_view.setdata(data, visibilityFlag);
    }

    @Override
    public void getFilterJobList(JobFilterModel jobFilterModel, int visibilityFlag) {
        if (!isFromScan) {
            this.jobFilterModel = jobFilterModel;
            this.visibilityFlag = visibilityFlag;
            loadFromServer();
            updateDataToView(this.jobFilterModel);
        }
    }

    @Override
    public void setSearchOnTextChange(String searchString) {
        if (jobFilterModel != null) {
            jobFilterModel.setSearch(searchString);
        }
    }

    @Override
    public void searchEquipment(String jobId, String barcode) {
        Job jobs = AppDataBase.getInMemoryDatabase(EotApp.getAppinstance())
                .jobModel().getJobsById(jobId);
        if (jobs != null) {
            boolean isEquipmentFound = false;
            List<EquArrayModel> equArray = jobs.getEquArray();
            if (equArray != null) {
                for (EquArrayModel equipment : equArray) {
                    if (equipment.getSno() != null && equipment.getSno().equals(barcode) ||
                            equipment.getBarcode() != null && equipment.getBarcode().equals(barcode)) {
                        isEquipmentFound = true;
                        joblist_view.onEquipmentFound(equipment);
                        break;
                    }
                }
            }
            if (!isEquipmentFound) joblist_view.onEquipmentFound(null);
        } else {
            joblist_view.onEquipmentFound(null);
        }
    }


    /**
     * Load Job list from server when Pull to refresh
     */
    @Override
    synchronized public void loadFromServer() {
        if (AppUtility.isInternetConnected()) {

            LogModel logModel = ActivityLogController
                    .getObj(ActivityLogController.JOB_MODULE, ActivityLogController.JOB_LIST, ActivityLogController.JOB_MODULE);
            ActivityLogController.saveOfflineTable(logModel);

            JobListRequestModel jobListRequestModel = new JobListRequestModel(Integer.parseInt(App_preference.getSharedprefInstance().getLoginRes().getUsrId()),
                    updatelimit, updateindex, App_preference.getSharedprefInstance().getJobSyncTime());

            String data = new Gson().toJson(jobListRequestModel);
            ApiClient.getservices().eotServiceCall(Service_apis.getUserJobList, AppUtility.getApiHeaders(), AppUtility.getJsonObject(data))
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new Observer<JsonObject>() {
                        @Override
                        public void onSubscribe(Disposable d) {

                        }

                        @Override
                        public void onNext(JsonObject jsonObject) {
                            if (jsonObject.get("success").getAsBoolean()) {
                                count = jsonObject.get("count").getAsInt();
                                String convert = new Gson().toJson(jsonObject.get("data").getAsJsonArray());
                                Type listType = new TypeToken<List<Job>>() {
                                }.getType();
                                List<Job> data = new Gson().fromJson(convert, listType);
                                addRecordsToDB(data);
                            } else if (jsonObject.get("statusCode") != null && jsonObject.get("statusCode").getAsString().equals(AppConstant.SESSION_EXPIRE)) {
                                joblist_view.onSessionExpired(LanguageController.getInstance().getServerMsgByKey(jsonObject.get("message").getAsString()));
                            }
                        }


                        @Override
                        public void onError(Throwable e) {
                            Log.e("TAG", e.getMessage());
                        }

                        @Override
                        public void onComplete() {
                            if ((updateindex + updatelimit) <= count) {
                                updateindex += updatelimit;
                                loadFromServer();
                            } else {
                                if (count != 0) {
                                    App_preference.getSharedprefInstance().setJobSyncTime(AppUtility.getDateByFormat(AppConstant.DATE_TIME_FORMAT));
                                }
                                updateindex = 0;
                                count = 0;
                                AppDataBase.getInMemoryDatabase(EotApp.getAppinstance()).jobModel().deleteJobByIsDelete();
                                updateDataToView(jobFilterModel);
                            }
                        }
                    });
        } else {
            joblist_view.setRefereshPullOff();
        }
    }

    private void updateDataToView(JobFilterModel jobFilterModel) {
        List<Job> data;
        if (jobFilterModel.getSearch().equals("") && jobFilterModel.getJobPriotiesList() != null && jobFilterModel.getJobPriotiesList().size() == 0 && jobFilterModel.getTagDataList().size() == 0 && jobFilterModel.getStatusModelsList().size() == 0) {
            if (sortedBy.equals(START_DATE_FIELD)) {
                data = AppDataBase.getInMemoryDatabase(EotApp.getAppinstance()).jobModel().getJoblistByScheduleStart();
            } else {
                data = AppDataBase.getInMemoryDatabase(EotApp.getAppinstance()).jobModel().getJoblist();
            }
            joblist_view.setdata(data, visibilityFlag);
        } else {
            getJobListByFilter(jobFilterModel);
        }
    }

    @Override
    public void addRecordsToDB(List<Job> data) {
        AppDataBase.getInMemoryDatabase(EotApp.getAppinstance()).jobModel().inserJob(data);
//        for add/remove listener.
        for (Job item : data) {
            if (item.getIsdelete().equals("0")
                    || item.getStatus().equals(AppConstant.Cancel)
                    || item.getStatus().equals(AppConstant.Closed)
                    || item.getStatus().equals(AppConstant.Reject)) {
                ChatController.getInstance().removeListnerByJobID(item.getJobId());
            } else {
                ChatController.getInstance().registerChatListner(item);
            }
        }
    }

    @Override
    public void filterDataFromStatus(String status, boolean check) {
        if (filter == null) {
            filter = new ArrayList<>();
        }

//      ****  temporary logic   *****

        if (status.equals(AppConstant.Accepted)) {
            if (check) {
                filter.add(AppConstant.Travelling);
                filter.add(AppConstant.Break);
            } else {
                filter.remove(AppConstant.Travelling);
                filter.remove(AppConstant.Break);
            }
        }
//      ***  temporary logic   ****

        if (check) {
            filter.add(status);
        } else filter.remove(status);
        if (filter.size() > 0) {
            List<Job> data = AppDataBase.getInMemoryDatabase(EotApp.getAppinstance()).jobModel().getJobsByStatus(filter);

            joblist_view.setdata(data, visibilityFlag);
        } else {
            List<Job> data;
            if (sortedBy.equals(START_DATE_FIELD)) {
                data = AppDataBase.getInMemoryDatabase(EotApp.getAppinstance()).jobModel().getJoblistByScheduleStart();
            } else {
                data = AppDataBase.getInMemoryDatabase(EotApp.getAppinstance()).jobModel().getJoblist();
            }

            joblist_view.setdata(data, visibilityFlag);
        }

    }

    @Override
    public void expand(final View v, int duration, int targetHeight) {
        int prevHeight = v.getHeight();

        v.setVisibility(View.VISIBLE);
        ValueAnimator valueAnimator = ValueAnimator.ofInt(prevHeight, targetHeight);
        valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                v.getLayoutParams().height = (int) animation.getAnimatedValue();
                v.requestLayout();

            }
        });
        valueAnimator.setInterpolator(new DecelerateInterpolator());
        valueAnimator.setDuration(duration);
        valueAnimator.start();
        valueAnimator.addListener(new AnimatorListenerAdapter() {

            @Override
            public void onAnimationEnd(Animator animation) {
                v.clearAnimation();
            }
        });
    }

    @Override
    public void collapse(final View v, int duration, int targetHeight) {
        int prevHeight = v.getHeight();
        ValueAnimator valueAnimator = ValueAnimator.ofInt(prevHeight, targetHeight);
        valueAnimator.setInterpolator(new DecelerateInterpolator());
        valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                v.getLayoutParams().height = (int) animation.getAnimatedValue();
                v.requestLayout();

            }
        });
        valueAnimator.setInterpolator(new DecelerateInterpolator());
        valueAnimator.setDuration(duration);
        valueAnimator.start();
        valueAnimator.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                v.clearAnimation();

            }
        });
    }

    @Override
    public void clearfiletrlist() {
        if (filter != null && filter.size() > 0) {
            filter.clear();
        }
    }

    @Override
    public void rorateClockwise(View view) {
        ObjectAnimator rotate = ObjectAnimator.ofFloat(view, "rotation", 180f, 0f);
//        rotate.setRepeatCount(10);
        rotate.setDuration(500);
        rotate.start();
    }

    @Override
    public void rorateAntiClockwise(View view) {
        ObjectAnimator rotate = ObjectAnimator.ofFloat(view, "rotation", 0f, 180f);
//        rotate.setRepeatCount(10);
        rotate.setDuration(500);
        rotate.start();
    }


    /**
     * fetch jon list by update date or creted date
     */
    @Override
    public void setSoringByDate(boolean isListSortByStartDate) {
        sortedBy = isListSortByStartDate ? UPDATED_DATE_FIELD : START_DATE_FIELD;
        updateDataToView(jobFilterModel);
    }

    @Override
    public void laodRecordsFromDB() {
        if (sortedBy.equals(START_DATE_FIELD)) {
            List<Job> data = AppDataBase.getInMemoryDatabase(EotApp.getAppinstance()).jobModel().getJoblistByScheduleStart();
            joblist_view.setdata(data, visibilityFlag);

        } else {
            List<Job> data = AppDataBase.getInMemoryDatabase(EotApp.getAppinstance()).jobModel().getJoblist();
            joblist_view.setdata(data, visibilityFlag);

        }
    }

    @Override
    public void getFilterListByStatus_Prity(List<TagData> tagDataList, Set<String> status_Id_List, Set<String> prity_Id_List) {
        try {
            ArrayList<Job> jobSet = new ArrayList<>();

            List<Job> jobTagFilterList = new ArrayList<>();

            Set<Job> tag_Filter_List = new HashSet<>();
            List<Job> jobList;
            if (sortedBy.equals(START_DATE_FIELD)) {
                jobList = AppDataBase.getInMemoryDatabase(EotApp.getAppinstance()).jobModel().getJoblistByScheduleStart();
            } else {
                jobList = AppDataBase.getInMemoryDatabase(EotApp.getAppinstance()).jobModel().getJoblist();
            }

            for (Job job : jobList) {  //filter list by tagdata
                for (TagData tagData : tagDataList)
                    if (job.getTagData() != null && job.getTagData().contains(tagData))
                        jobTagFilterList.add(job);
            }


            String oldJobId = null;  //remove repet job....tag
            for (Job tag_job : jobTagFilterList) {
                if ((oldJobId == null) || !(oldJobId.equals(tag_job.getJobId()))) {
                    tag_Filter_List.add(tag_job);
                }
                oldJobId = tag_job.getJobId();
            }

            List<Job> statusPrtyFilterList = new ArrayList<>();
            if ((status_Id_List.size() > 0) && (prity_Id_List.size() > 0)) {
                //get job by status & prity
                statusPrtyFilterList = AppDataBase.getInMemoryDatabase(EotApp.getAppinstance()).jobModel().loadJobsFromStatusPrtY(status_Id_List, prity_Id_List);
            } else if (status_Id_List.size() > 0) {
                //get job list by only status
                statusPrtyFilterList = AppDataBase.getInMemoryDatabase(EotApp.getAppinstance()).jobModel().loadJobsFromStatus(status_Id_List);
            } else if (prity_Id_List.size() > 0) {
                //get job by only prity
                statusPrtyFilterList = AppDataBase.getInMemoryDatabase(EotApp.getAppinstance()).jobModel().loadJobsFromPrty(prity_Id_List);
            } else if (statusPrtyFilterList.size() == 0) {
                //searach only by tag when no job status & prity
                jobSet.addAll(tag_Filter_List);
            }


            if (tag_Filter_List.size() > 0) {
                for (Job jobStatuPtry : statusPrtyFilterList) {
                    for (Job jobtag : tag_Filter_List) {
                        if (jobtag.getPrty().equals(jobStatuPtry.getPrty()) && jobtag.getStatus().equals(jobStatuPtry.getStatus())) {
                            jobSet.add(jobtag);
                        }
                    }
                }
            } else {
                jobSet.addAll(statusPrtyFilterList);
            }

            joblist_view.setdata(jobSet, visibilityFlag);

        } catch (Exception e) {
            e.printStackTrace();
        }


    }

    @Override
    public void getJobListByFilter(JobFilterModel filterModel) {
/**         get job list by client name/job lable & job status, job prity , job tag* */
        Log.e("", "");

        try {
            String searchString;
            if (filterModel.getSearch().equals("")) {
                searchString = null;
            } else {
                searchString = filterModel.getSearch();
            }

            ArrayList<Job> jobSet = new ArrayList<>();

            List<Job> listFilterBySelectedTag = new ArrayList<>();

            Set<Job> tag_Filter_List = new HashSet<>();
            List<Job> jobList;

            /** Get All Job List  */
            if (sortedBy.equals(START_DATE_FIELD)) {
                jobList = AppDataBase.getInMemoryDatabase(EotApp.getAppinstance()).jobModel().getJoblistByScheduleStart();
            } else {
                jobList = AppDataBase.getInMemoryDatabase(EotApp.getAppinstance()).jobModel().getJoblist();
            }
            /** get job list for selected tag data */

            for (Job job : jobList) {
                if (job.hasTagData(jobFilterModel.getTagDataList())) {
                    listFilterBySelectedTag.add(job);
                }
            }


            String oldJobId = null;  //remove repet job....tag
            for (Job tag_job : listFilterBySelectedTag) {
                if ((oldJobId == null) || !(oldJobId.equals(tag_job.getJobId()))) {
                    tag_Filter_List.add(tag_job);
                }
                oldJobId = tag_job.getJobId();
            }

            List<Job> statusPrtyFilterList;
            if (filterModel.getJobPriotiesList().isEmpty()) {
                if (sortedBy.equals(START_DATE_FIELD)) {
                    statusPrtyFilterList = AppDataBase.getInMemoryDatabase(EotApp.getAppinstance()).jobModel().
                            getJobFilterByScheduleStart_null_priority(searchString, filterModel.getStatusModelsList());
                } else {
                    statusPrtyFilterList = AppDataBase.getInMemoryDatabase(EotApp.getAppinstance()).jobModel().
                            getJobFilterByDate_null_priority(searchString, filterModel.getStatusModelsList());
                }
            } else if (filterModel.getStatusModelsList().isEmpty()) {
                if (sortedBy.equals(START_DATE_FIELD)) {
                    statusPrtyFilterList = AppDataBase.getInMemoryDatabase(EotApp.getAppinstance()).jobModel().
                            getJobFilterByScheduleStart_null_status(searchString, filterModel.getJobPriotiesList());
                } else {
                    statusPrtyFilterList = AppDataBase.getInMemoryDatabase(EotApp.getAppinstance()).jobModel().
                            getJobFilterByDate_null_status(searchString, filterModel.getJobPriotiesList());
                }
            } else {
                if (sortedBy.equals(START_DATE_FIELD)) {
                    statusPrtyFilterList = AppDataBase.getInMemoryDatabase(EotApp.getAppinstance()).jobModel().
                            getJobFilterByScheduleStart(searchString, filterModel.getStatusModelsList(), filterModel.getJobPriotiesList());
                } else {
                    statusPrtyFilterList = AppDataBase.getInMemoryDatabase(EotApp.getAppinstance()).jobModel().
                            getJobFilterByDate(searchString, filterModel.getStatusModelsList(), filterModel.getJobPriotiesList());
                }
            }


            /** searach only by tag when no job status & prity & search key available */
            if (statusPrtyFilterList.size() == 0) {
                //search only by tag when no job status & prity & search key available
                jobSet.addAll(tag_Filter_List);
            }

            if (jobFilterModel.getTagDataList().size() > 0) {
                if (tag_Filter_List.size() > 0) {
                    for (Job jobStatuPtry : statusPrtyFilterList) {
                        for (Job jobtag : tag_Filter_List) {
                            if (jobtag.getPrty().equals(jobStatuPtry.getPrty()) && jobtag.getStatus().equals(jobStatuPtry.getStatus()) && !jobSet.contains(jobtag)) {
                                jobSet.add(jobtag);
                            }
                        }
                    }
                }
            } else {
                jobSet.addAll(statusPrtyFilterList);
            }
            joblist_view.setdata(jobSet, visibilityFlag);

        } catch (Exception e) {
            e.printStackTrace();
        }


    }

}

