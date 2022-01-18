package com.eot_app.nav_menu.jobs.add_job.addjobmodel;

/**
 * Created by Mahendra Dabi on 29/3/21.
 */
public class JOBRecurmodel {
    private final String mode;
    private final String startDate;
    private final String numOfWeeks;
    private final String endDate;
    private final String occurences;
    private final String weekDays;
    private final String endRecurMode;
    private final String stopRecur;

    public JOBRecurmodel(String mode, String startDate, String numOfWeeks, String endDate, String occurences, String weekDays, String endRecurMode) {
        this.mode = mode;
        this.startDate = startDate;
        this.numOfWeeks = numOfWeeks;
        this.endDate = endDate;
        this.occurences = occurences;
        this.weekDays = weekDays;
        this.endRecurMode = endRecurMode;
        this.stopRecur = "0";

    }
}
