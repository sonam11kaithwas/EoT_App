package com.eot_app.nav_menu.jobs.add_job.add_job_recr.daily_recr_pkg.daily_recur_model;

/**
 * Created by Sona-11 on 25/3/21.
 */
public class DailyMsgResModel {
    private String interval;
    private String start_date;
    private String end_date;
    private String occurences;
    private String week_num;

    public String getEnd_date() {
        return end_date;
    }

    public void setEnd_date(String end_date) {
        this.end_date = end_date;
    }

    public String getOccurences() {
        return occurences;
    }

    public void setOccurences(String occurences) {
        this.occurences = occurences;
    }

    public String getInterval() {
        return interval;
    }

    public void setInterval(String interval) {
        this.interval = interval;
    }

    public String getStart_date() {
        return start_date;
    }

    public void setStart_date(String start_date) {
        this.start_date = start_date;
    }

    public String getWeek_num() {
        return week_num;
    }

    public void setWeek_num(String week_num) {
        this.week_num = week_num;
    }
}
