package com.eot_app.nav_menu.jobs.add_job.add_job_recr.daily_recr_pkg.daily_recur_model;

import java.util.ArrayList;

/**
 * Created by Sona-11 on 26/3/21.
 */
public class JobRecurModel {
    private final String mode;
    private final String startDate;
    private final String endDate;
    private final String occurences;
    private final String jobId;
    private final String endRecurMode;
    private final String stopRecur;
    String schdlStart;
    private String interval;
    private ArrayList<String> weekDays = new ArrayList<>();
    private String numOfDays;
    private String numOfMonths;
    private String dateNum;
    private String dayNum;
    private String weekNum;
    private String numOfWeeks;
    private String occur_days;
    private String week_num;


    /**
     * This constructor use for MONTHLY Job recurence pattern
     ****/
    public JobRecurModel(String mode, String startDate, String endDate, String occurences, String endRecurMode,
                         String dateNum, String numOfMonths, String dayNum, String weekNum) {
        this.mode = mode;
        this.startDate = startDate;
        this.dateNum = dateNum;
        this.endDate = endDate;
        this.occurences = occurences;
        this.jobId = "";
        this.stopRecur = "0";
        this.endRecurMode = endRecurMode;
        this.numOfMonths = numOfMonths;
        this.dayNum = dayNum;
        this.weekNum = weekNum;
    }


    /****This constructor use for Daily Job recurence pattern****/
    public JobRecurModel(String mode, String startDate, String numOfDays, String endDate, String occurences, String endRecurMode) {
        this.mode = mode;
        this.startDate = startDate;
        this.numOfDays = numOfDays;
        this.endDate = endDate;
        this.occurences = occurences;
        this.jobId = "";
        this.stopRecur = "0";
        this.endRecurMode = endRecurMode;
    }

    /****Weekly Recurence Pettarn  ******/
    public JobRecurModel(String mode, String startDate, String endDate, String occurences,
                         String numOfWeeks, String endRecurMode
            , ArrayList<String> weekDay) {
        this.mode = mode;
        this.startDate = startDate;
        this.endDate = endDate;
        this.occurences = occurences;
        this.jobId = "";
        this.numOfWeeks = numOfWeeks;
        this.endRecurMode = endRecurMode;
        this.stopRecur = "0";
        this.weekDays = weekDay;
    }

    /****Normal Weekly Recurence Pettarn  ******/
    public JobRecurModel(String startDate, String endDate,
                         String endRecurMode
            , ArrayList<String> weekDay) {
        this.mode = "1";
        this.startDate = startDate;
        this.endDate = endDate;
        this.occurences = "0";
        this.jobId = "";
        this.numOfWeeks = "1";
        this.endRecurMode = endRecurMode;
        this.stopRecur = "0";
        this.weekDays = weekDay;
        this.occur_days = occurences;
        //this.interval =
    }

    public String getSchdlStart() {
        return schdlStart;
    }

    public void setSchdlStart(String schdlStart) {
        this.schdlStart = schdlStart;
    }

    public String getMode() {
        return mode;
    }

    public String getStartDate() {
        return startDate;
    }

    public String getEndDate() {
        return endDate;
    }

    public String getOccurences() {
        return occurences;
    }

    public String getJobId() {
        return jobId;
    }

    public String getEndRecurMode() {
        return endRecurMode;
    }

    public String getStopRecur() {
        return stopRecur;
    }

    public String getInterval() {
        return interval;
    }

    public void setInterval(String interval) {
        this.interval = interval;
    }

    public ArrayList<String> getWeekDays() {
        return weekDays;
    }

    public void setWeekDays(ArrayList<String> weekDays) {
        this.weekDays = weekDays;
    }

    public String getNumOfDays() {
        return numOfDays;
    }

    public void setNumOfDays(String numOfDays) {
        this.numOfDays = numOfDays;
    }

    public String getNumOfMonths() {
        return numOfMonths;
    }

    public void setNumOfMonths(String numOfMonths) {
        this.numOfMonths = numOfMonths;
    }

    public String getDateNum() {
        return dateNum;
    }

    public void setDateNum(String dateNum) {
        this.dateNum = dateNum;
    }

    public String getDayNum() {
        return dayNum;
    }

    public void setDayNum(String dayNum) {
        this.dayNum = dayNum;
    }

    public String getWeekNum() {
        return weekNum;
    }

    public void setWeekNum(String weekNum) {
        this.weekNum = weekNum;
    }

    public String getNumOfWeeks() {
        return numOfWeeks;
    }

    public void setNumOfWeeks(String numOfWeeks) {
        this.numOfWeeks = numOfWeeks;
    }

    public String getOccur_days() {
        return occur_days;
    }

    public void setOccur_days(String occur_days) {
        this.occur_days = occur_days;
    }

    public String getWeek_num() {
        return week_num;
    }

    public void setWeek_num(String week_num) {
        this.week_num = week_num;
    }
}
