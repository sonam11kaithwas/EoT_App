package com.eot_app.nav_menu.jobs.job_detail.job_equipment.job_equ_remrk.job_equ_mvp;

import com.eot_app.nav_menu.audit.audit_list.equipment.remark.remark_mvp.RemarkRequest;
import com.eot_app.nav_menu.jobs.job_db.Job;

import java.util.ArrayList;
import java.util.List;

import okhttp3.MultipartBody;

/**
 * Created by Sonam-11 on 22/9/20.
 */
public interface JobEquRemark_PI {
    void getCustomFormList(final Job equipmentRes, final ArrayList<String> jtId);

    void addNewRemark(RemarkRequest remarkRequest, String file, List<MultipartBody.Part> docAns, ArrayList<String> docQueIdArrays,
                      List<MultipartBody.Part> signAns, ArrayList<String> signQueIdArrays);
}
