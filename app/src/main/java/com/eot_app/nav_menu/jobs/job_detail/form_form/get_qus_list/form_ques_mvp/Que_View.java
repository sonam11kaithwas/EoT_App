package com.eot_app.nav_menu.jobs.job_detail.form_form.get_qus_list.form_ques_mvp;

import com.eot_app.nav_menu.jobs.job_detail.form_form.get_qus_list.qus_model.QuesRspncModel;

import java.util.List;

/**
 * Created by ubuntu on 18/9/18.
 */

public interface Que_View {
    void questionlist(List<QuesRspncModel> quesRspncModelList);

    void onSubmitSuccess(String msg);

    void addfragmentDynamically(List<QuesRspncModel> quesRspncModelList);

    void onSessionExpire(String msg);

    void showOfflineAlert(String msg);

    void finishMuAvtivity();
}
