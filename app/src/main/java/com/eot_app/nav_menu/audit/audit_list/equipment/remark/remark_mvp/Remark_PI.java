package com.eot_app.nav_menu.audit.audit_list.equipment.remark.remark_mvp;

import com.eot_app.nav_menu.audit.audit_list.audit_mvp.model.AuditList_Res;

import java.util.ArrayList;
import java.util.List;

import okhttp3.MultipartBody;

/**
 * Created by Mahendra Dabi on 12/11/19.
 */
public interface Remark_PI {
    void addNewRemark(RemarkRequest remarkRequest, String file, List<MultipartBody.Part> docAns, ArrayList<String> docQueIdArrays,
                      List<MultipartBody.Part> signAns, ArrayList<String> signQueIdArrays);

    void getCustomFormList(AuditList_Res equipmentRes, ArrayList<String> jtId);

}
