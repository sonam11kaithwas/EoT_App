package com.eot_app.nav_menu.jobs.job_detail.reschedule.reschedule_mvp;

import com.eot_app.services.Service_apis;
import com.eot_app.utility.AppConstant;
import com.eot_app.utility.AppUtility;
import com.eot_app.utility.EotApp;
import com.eot_app.utility.db.AppDataBase;
import com.eot_app.utility.db.OfflineDataController;
import com.google.gson.Gson;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * Created by Mahendra Dabi on 21-08-2020.
 */
public class Reschedule_PC implements Reschedule_PI {
    private final Reschedule_view reschedule_view;

    public Reschedule_PC(Reschedule_view reschedule_view) {
        this.reschedule_view = reschedule_view;
    }


    @Override
    public void RescheduleJob(RescheduleRequest rescheduleRequest) {
        if (rescheduleRequest != null) {
            String startTime = getTimeStampFromFormatedDate(rescheduleRequest.getSchdlStart());
            String endTime = getTimeStampFromFormatedDate(rescheduleRequest.getSchdlFinish());
            AppDataBase.getInMemoryDatabase(EotApp.getAppinstance()).jobModel().rescheduleJob(startTime, endTime, rescheduleRequest.getJobId());

            String dateTime = AppUtility.getDateByFormat(AppConstant.DATE_TIME_FORMAT);


            Gson gson = new Gson();
            String rescheduleReq = gson.toJson(rescheduleRequest);
            OfflineDataController.getInstance().addInOfflineDB(Service_apis.rescheduleJob, rescheduleReq, dateTime);

            reschedule_view.onRescheduleCompleted(startTime, endTime);


        }

    }

    private String getTimeStampFromFormatedDate(String schdlStart) {
        SimpleDateFormat gettingfmt = new SimpleDateFormat(
                AppUtility.dateTimeByAmPmFormate("dd-MM-yyyy hh:mm a", "dd-MM-yyyy kk:mm"), Locale.US);
        try {
            Date formated = gettingfmt.parse(schdlStart);
            String str = String.valueOf(formated.getTime() / 1000);
            return str;
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return null;
    }
}
