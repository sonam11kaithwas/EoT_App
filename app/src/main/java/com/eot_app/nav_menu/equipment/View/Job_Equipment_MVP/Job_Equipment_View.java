package com.eot_app.nav_menu.equipment.View.Job_Equipment_MVP;

import com.eot_app.nav_menu.jobs.job_detail.addinvoiveitem2pkg.model.InvoiceItemDataModel;
import com.eot_app.nav_menu.jobs.job_detail.detail.job_detail_view.JobDetail_view;

import java.util.List;

public interface Job_Equipment_View extends JobDetail_view {

    void setCompletionNote(String completionNote);

    void setJobItemList(List<InvoiceItemDataModel> data);
}
