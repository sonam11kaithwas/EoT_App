package com.eot_app.nav_menu.audit.audit_list.equipment.remark.remark_mvp;

import com.eot_app.nav_menu.jobs.job_detail.customform.cstm_form_model.CustomFormList_Res;

import java.util.ArrayList;

/**
 * Created by Mahendra Dabi on 12/11/19.
 */
public interface Remark_View {
    void onRemarkUpdate(String message);

    void onSessionExpire(String message);

    void onErrorMessage(String msg);

    void setList(ArrayList<CustomFormList_Res> customFormLists);

    void formNotFound();

    void finishErroroccur();
}
