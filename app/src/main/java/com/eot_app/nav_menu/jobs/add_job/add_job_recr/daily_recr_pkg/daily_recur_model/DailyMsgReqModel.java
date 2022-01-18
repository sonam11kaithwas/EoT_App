package com.eot_app.nav_menu.jobs.add_job.add_job_recr.daily_recr_pkg.daily_recur_model;

import java.util.ArrayList;

/**
 * Created by Sona-11 on 25/3/21.
 */
public class DailyMsgReqModel {
    private final String jobId;
    private final String schdlStart;
    private final String mode;
    private final String interval;
    private final String endRecurMode;
    private final String type;
    private final String numberOfOcurrences;
    private String startDate;
    private String endDate;
    private ArrayList<String> weekDays = new ArrayList<>();
    private String occurDay;
    private String weekNum;
    private String dayNum;

    /*****this for Add Dialy Recur***/
    public DailyMsgReqModel(String jobId, String schdlStart, String mode, String startDate, String interval,
                            String endDate, String numberOfOcurrences, String endRecurMode) {
        this.jobId = jobId;
        this.schdlStart = schdlStart;
        this.mode = mode;
        this.startDate = startDate;
        this.interval = interval;
        this.endDate = endDate;
        this.numberOfOcurrences = numberOfOcurrences;
        this.endRecurMode = endRecurMode;
        this.type = "0";
    }


    /*****this for Add Weekly Recur***/

    public DailyMsgReqModel(String mode, String schdlStart, String startDate, String endDate
            , String interval, String endRecurMode,
                            ArrayList<String> weekDays, String numberOfOcurrences) {

        this.jobId = "";
        this.schdlStart = schdlStart;
        this.mode = mode;
        this.startDate = startDate;
        this.interval = interval;
        this.endDate = endDate;
        this.numberOfOcurrences = numberOfOcurrences;
        this.endRecurMode = endRecurMode;
        this.type = "0";
        this.weekDays = weekDays;
    }


    /*****this for Add Monthly Recur***/
    public DailyMsgReqModel(String schdlStart, String mode, String interval,
                            String numberOfOcurrences, String endRecurMode, String startDate,
                            String endDate, String occurDay, String weekNum, String dayNum) {
        this.jobId = "";
        this.schdlStart = schdlStart;
        this.mode = mode;
        this.interval = interval;
        this.numberOfOcurrences = numberOfOcurrences;
        this.endRecurMode = endRecurMode;
        this.type = "0";
        this.startDate = startDate;
        this.endDate = endDate;
        this.occurDay = occurDay;
        this.weekNum = weekNum;
        this.dayNum = dayNum;
    }

    public String getEndDate() {
        return endDate;
    }

    public void setEndDate(String endDate) {
        this.endDate = endDate;
    }

    public void setStartDate(String startDate) {
        this.startDate = startDate;
    }
}
