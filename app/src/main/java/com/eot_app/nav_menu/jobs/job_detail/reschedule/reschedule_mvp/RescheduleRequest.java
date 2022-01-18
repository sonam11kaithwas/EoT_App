package com.eot_app.nav_menu.jobs.job_detail.reschedule.reschedule_mvp;

/**
 * Created by Mahendra Dabi on 21-08-2020.
 */
public class RescheduleRequest {
    private final String jobId;
    private final String usrId;
    private final String schdlFinish;
    private final String schdlStart;

    public RescheduleRequest(String jobId, String usrId, String schdlFinish, String schdlStart) {
        this.jobId = jobId;
        this.usrId = usrId;
        this.schdlFinish = schdlFinish;
        this.schdlStart = schdlStart;
    }


    public String getJobId() {
        return jobId;
    }

    public String getUsrId() {
        return usrId;
    }


    public String getSchdlFinish() {
        return schdlFinish;
    }

    public String getSchdlStart() {
        return schdlStart;
    }
}
