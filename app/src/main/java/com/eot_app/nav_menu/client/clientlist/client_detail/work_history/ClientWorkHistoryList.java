package com.eot_app.nav_menu.client.clientlist.client_detail.work_history;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.widget.ContentLoadingProgressBar;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.eot_app.R;
import com.eot_app.nav_menu.appointment.dbappointment.Appointment;
import com.eot_app.nav_menu.audit.audit_list.audit_mvp.model.AuditList_Res;
import com.eot_app.nav_menu.client.clientlist.client_detail.work_history.adpter.AdpterAduitHistory;
import com.eot_app.nav_menu.client.clientlist.client_detail.work_history.adpter.AdpterAppointmentHistory;
import com.eot_app.nav_menu.client.clientlist.client_detail.work_history.adpter.AdpterJobHistory;
import com.eot_app.nav_menu.client.clientlist.client_detail.work_history.history_presenter.WorkHistoryPC;
import com.eot_app.nav_menu.client.clientlist.client_detail.work_history.history_presenter.WorkHistoryPI;
import com.eot_app.nav_menu.client.clientlist.client_detail.work_history.history_presenter.WorkHistoryView;
import com.eot_app.nav_menu.client.clientlist.client_detail.work_history.view.AppointmentHistoryDetailsActivity;
import com.eot_app.nav_menu.equipment.View.AuditDetailEquActivity;
import com.eot_app.nav_menu.equipment.View.JobdetailsEquActivity;
import com.eot_app.nav_menu.jobs.job_db.Job;
import com.eot_app.utility.AppConstant;
import com.eot_app.utility.AppUtility;
import com.eot_app.utility.App_preference;
import com.eot_app.utility.EotApp;
import com.eot_app.utility.language_support.LanguageController;
import com.google.gson.Gson;

import java.util.List;
import java.util.concurrent.Callable;

/**
 * Created by Mahendra Dabi on 12/3/21.
 */
public class ClientWorkHistoryList extends Fragment implements WorkHistoryView, AdpterAduitHistory.OnAuditSelection, AdpterJobHistory.OnJobSelection,
        AdpterAppointmentHistory.OnAppointmentSelection, LoadMoreItem {
    boolean istrue = true;
    private RecyclerView recycler_view_work_history;
    private FragmentTypes typeSelected;
    private AdpterJobHistory adapteJobList;
    private WorkHistoryPI workHistoryPI;
    private AdpterAduitHistory adpterAduitHistory;
    private AdpterAppointmentHistory adpterAppointmentHistory;
    private String cltId;
    private FragmentTypes type;
    private LinearLayout nolist_linear;
    private View view;
    private TextView nolist_txt;
    private ContentLoadingProgressBar progressBar;
    private SwipeRefreshLayout swiperefresh;

    private ContentLoadingProgressBar clp_load_more_item;

    public static ClientWorkHistoryList newInstance(FragmentTypes types, String cltId) {
        Bundle bundle = new Bundle();
        bundle.putSerializable("type", types);
        bundle.putString("id", cltId);
        ClientWorkHistoryList fragment = new ClientWorkHistoryList();
        fragment.setArguments(bundle);
        return fragment;
    }


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = LayoutInflater.from(container.getContext())
                .inflate(R.layout.fragment_work_history_list, container, false);

        intializeViews(view);

        progressBar.setVisibility(View.VISIBLE);

        getBundle();


        workHistoryPI = new WorkHistoryPC(this);

        if (type.equals(FragmentTypes.JOB)) {
            istrue = false;
            adapteJobList = new AdpterJobHistory(getActivity());
            adapteJobList.setFromShowSiteName(App_preference.getSharedprefInstance().getSiteNameShowInSetting());
            adapteJobList.setOnJobSelection(this);
            adapteJobList.setLoadMoreItem(this);
            recycler_view_work_history.setAdapter(adapteJobList);
            workHistoryPI.getJobList(cltId);

        } else if (type.equals(FragmentTypes.APPOINTMENT)) {
            adpterAppointmentHistory = new AdpterAppointmentHistory(getActivity());
            adpterAppointmentHistory.setOnAuditSelection(this);
            adpterAppointmentHistory.setFromShowSiteName(App_preference.getSharedprefInstance().getSiteNameShowInSetting());
            adpterAppointmentHistory.setLoadMoreItem(this);
            recycler_view_work_history.setAdapter(adpterAppointmentHistory);
            workHistoryPI.getAppointmentList(cltId);


        } else if (type.equals(FragmentTypes.AUDIT)) {
            adpterAduitHistory = new AdpterAduitHistory(getActivity());
            adpterAduitHistory.setOnAuditSelection(this);
            adpterAduitHistory.setFromShowSiteName(App_preference.getSharedprefInstance().getSiteNameShowInSetting());
            adpterAduitHistory.setLoadMoreItem(this);
            recycler_view_work_history.setAdapter(adpterAduitHistory);
            workHistoryPI.getAuditList(cltId);


        }

        swiperefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                workHistoryPI.loadMoreItem(FragmentTypes.JOB, cltId);

            }
        });


        return view;
    }

    private void intializeViews(View view) {
        swiperefresh = view.findViewById(R.id.swiperefresh);
        recycler_view_work_history = view.findViewById(R.id.recycler_view_work_history);
        recycler_view_work_history.setLayoutManager(new LinearLayoutManager(getActivity()));
        nolist_linear = view.findViewById(R.id.nolist_linear);
        nolist_txt = view.findViewById(R.id.nolist_txt);
        progressBar = view.findViewById(R.id.progressBar);
        clp_load_more_item = view.findViewById(R.id.clp_load_more_item);
    }

    private void getBundle() {
        Bundle arguments = getArguments();
        if (arguments != null) {
            if (arguments.getSerializable("type") != null) {
                type = (FragmentTypes) arguments.getSerializable("type");
                typeSelected = type;
                this.cltId = arguments.getString("id");
            }
        }
    }

    @Override
    public void setJobList(List<Job> job) {
        progressBar.setVisibility(View.GONE);
        swiperefresh.setRefreshing(false);
        if (job != null && job.size() > 0) {
            adapteJobList.setList(job);
            nolist_linear.setVisibility(View.GONE);
        } else {
            nolist_linear.setVisibility(View.VISIBLE);
            nolist_txt.setText(LanguageController.getInstance().getMobileMsgByKey(AppConstant.err_no_jobs_found));
        }
    }

    @Override
    public void setAuditList(List<AuditList_Res> audit) {
        progressBar.setVisibility(View.GONE);
        swiperefresh.setRefreshing(false);
        if (audit != null && audit.size() > 0) {
            adpterAduitHistory.setList(audit);
            nolist_linear.setVisibility(View.GONE);
        } else {
            nolist_linear.setVisibility(View.VISIBLE);
            nolist_txt.setText(LanguageController.getInstance().getMobileMsgByKey(AppConstant.audit_not_found));
        }
    }

    @Override
    public void setAppointmentList(List<Appointment> appointment) {
        progressBar.setVisibility(View.GONE);
        swiperefresh.setRefreshing(false);
        if (appointment != null && appointment.size() > 0) {
            adpterAppointmentHistory.setList(appointment);
            nolist_linear.setVisibility(View.GONE);
        } else {
            nolist_linear.setVisibility(View.VISIBLE);
            nolist_txt.setText(LanguageController.getInstance().getMobileMsgByKey(AppConstant.no_appointment_found));
        }
    }

    @Override
    public void setJobDetails(Job job) {
        if (job != null) {
            Intent intent = new Intent(getActivity(), JobdetailsEquActivity.class);
            intent.putExtra("Job_data", new Gson().toJson(job));
            intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
            startActivity(intent);
        }
    }

    @Override
    public void setAduditDetails(AuditList_Res auditList_res) {
        if (auditList_res != null) {
            Intent intent = new Intent(getActivity(), AuditDetailEquActivity.class);
            intent.putExtra("audit_data", auditList_res);
            String audit_data_str = new Gson().toJson(auditList_res);
            intent.putExtra("audit_data_str", audit_data_str);
            intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
            startActivity(intent);
        }
    }

    @Override
    public void setAppointmentDetails(Appointment app_details) {
        if (app_details != null) {
            Intent intent = new Intent(getActivity(), AppointmentHistoryDetailsActivity.class);
            intent.putExtra("app_data", app_details);
            intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
            startActivity(intent);
        }
    }

    @Override
    public void sessionExpire(String message) {
        AppUtility.alertDialog(getActivity(), LanguageController.getInstance().getMobileMsgByKey(AppConstant.dialog_error_title), message, LanguageController.getInstance().getMobileMsgByKey(AppConstant.ok), "", new Callable<Boolean>() {
            @Override
            public Boolean call() throws Exception {
                EotApp.getAppinstance().sessionExpired();
                return null;
            }
        });
    }

    @Override
    public void showMoreLoadingProgress(boolean b) {
        if (clp_load_more_item != null) {
            if (b) clp_load_more_item.setVisibility(View.VISIBLE);
            else {
                clp_load_more_item.setVisibility(View.GONE);
                if (swiperefresh != null && swiperefresh.isRefreshing())
                    swiperefresh.setRefreshing(false);
            }
        }

    }


    @Override
    public void onAuditSelected(AuditList_Res audit) {
        if (workHistoryPI != null)
            workHistoryPI.getAduitDetails(audit.getAudId());

    }


    @Override
    public void onJobSelected(Job job) {
        if (workHistoryPI != null) {
            workHistoryPI.getJobDetails(job.getJobId());
        }
    }

    @Override
    public void onAppointmentSelected(Appointment appointment) {
        if (workHistoryPI != null) {
            workHistoryPI.getAppointmentDetails(appointment.getAppId());
        }
    }

    @Override
    public void onLoadMoreItem(int size, ClientWorkHistoryList.FragmentTypes types) {
        if (workHistoryPI != null) {
            switch (types) {
                case JOB:
                case AUDIT:
                case APPOINTMENT:
                    workHistoryPI.loadMoreItem(types, cltId);
                    break;
            }
        }

    }


    public enum FragmentTypes {
        JOB,
        AUDIT,
        APPOINTMENT
    }
}
