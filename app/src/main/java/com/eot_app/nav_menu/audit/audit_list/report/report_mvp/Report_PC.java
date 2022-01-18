package com.eot_app.nav_menu.audit.audit_list.report.report_mvp;

import android.content.Context;

import com.eot_app.activitylog.ActivityLogController;
import com.eot_app.nav_menu.audit.audit_list.report.AuditReportFragment;
import com.eot_app.nav_menu.audit.audit_list.report.mode.ReportRequest;
import com.eot_app.services.Service_apis;
import com.eot_app.utility.AppConstant;
import com.eot_app.utility.AppUtility;
import com.eot_app.utility.db.OfflineDataController;
import com.eot_app.utility.language_support.LanguageController;
import com.google.gson.Gson;

/**
 * Created by Mahendra Dabi on 13/11/19.
 */
public class Report_PC implements Report_PI {
    private final Report_View report_view;
    private Context mContext;

    public Report_PC(Report_View report_view) {
        this.report_view = report_view;
        try {
            mContext = ((AuditReportFragment) report_view).getActivity();
        } catch (ClassCastException ex) {
            ex.printStackTrace();
        }
    }


    @Override
    public void addNewReport(ReportRequest request) {
        ActivityLogController.saveActivity(ActivityLogController.AUDIT_MODULE, ActivityLogController.AUDIT_ADD_REPORT, ActivityLogController.AUDIT_MODULE);

        OfflineDataController.getInstance().addInOfflineDB(Service_apis.addAuditReport, new Gson().toJson(request), AppUtility.getDateByFormat(AppConstant.DATE_TIME_FORMAT));

        if (AppUtility.isInternetConnected()) {
            report_view.onSuccess(LanguageController.getInstance().getMobileMsgByKey(AppConstant.report_submit));
        } else {
            report_view.onSuccess(LanguageController.getInstance().getMobileMsgByKey(AppConstant.report_offline));
        }


    }
}
