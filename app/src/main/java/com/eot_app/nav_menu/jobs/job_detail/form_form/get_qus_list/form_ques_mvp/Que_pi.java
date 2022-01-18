package com.eot_app.nav_menu.jobs.job_detail.form_form.get_qus_list.form_ques_mvp;

import com.eot_app.nav_menu.jobs.job_detail.form_form.get_qus_list.ans_model.Ans_Req;
import com.eot_app.nav_menu.jobs.job_detail.form_form.get_qus_list.qus_model.QuesGetModel;

import java.util.ArrayList;
import java.util.List;

import okhttp3.MultipartBody;

/**
 * Created by ubuntu on 17/9/18.
 */

public interface Que_pi {

    void getQuestions(QuesGetModel quesGetModel);

    //   void giveAns(Ans_Req ans_req);

    void addAnswerWithAttachments(Ans_Req ans_req, List<MultipartBody.Part> signAns, List<MultipartBody.Part> docAns, ArrayList<String> signQueIdArray, ArrayList<String> docQueIdArray);


}
