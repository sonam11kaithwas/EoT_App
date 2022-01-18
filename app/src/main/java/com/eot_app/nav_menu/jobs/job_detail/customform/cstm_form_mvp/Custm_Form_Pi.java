package com.eot_app.nav_menu.jobs.job_detail.customform.cstm_form_mvp;

import java.util.ArrayList;

/**
 * Created by ubuntu on 13/9/18.
 */

public interface Custm_Form_Pi {
    void callApiGetFormlist(String jobId, ArrayList<String> jtId);

    void getFormListBYSwipe(String jobId, ArrayList<String> jtId);
}
