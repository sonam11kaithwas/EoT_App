package com.eot_app.nav_menu.jobs.job_detail.form_form.get_qus_list.ques_ans_pck.ans_req_respn;

import java.util.List;

/**
 * Created by ubuntu on 18/9/18.
 */

public class Answer_Req {
    private String usrId;
    private List<Answer> answer = null;

    public String getUsrId() {
        return usrId;
    }

    public void setUsrId(String usrId) {
        this.usrId = usrId;
    }

    public List<Answer> getAnswer() {
        return answer;
    }

    public void setAnswer(List<Answer> answer) {
        this.answer = answer;
    }
}
