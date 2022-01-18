package com.eot_app.nav_menu.audit.audit_list.details.audit_detail_mvp;

import com.eot_app.nav_menu.custom_fileds.custom_model.CustOmFormQuestionsRes;

import java.util.ArrayList;

/**
 * Created by Mahendra Dabi on 12/11/19.
 */
public interface AuditDetails_View {
    void statusChanged(int status);

    void onSessionExpire(String message);

    void setCustomFiledList(ArrayList<CustOmFormQuestionsRes> dataList);

}
