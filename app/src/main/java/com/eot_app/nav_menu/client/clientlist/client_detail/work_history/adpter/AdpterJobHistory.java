package com.eot_app.nav_menu.client.clientlist.client_detail.work_history.adpter;

import android.content.Context;
import android.graphics.Color;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.TextUtils;
import android.text.style.ForegroundColorSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.eot_app.R;
import com.eot_app.nav_menu.client.clientlist.client_detail.work_history.ClientWorkHistoryList;
import com.eot_app.nav_menu.client.clientlist.client_detail.work_history.LoadMoreItem;
import com.eot_app.nav_menu.jobs.job_db.Job;
import com.eot_app.nav_menu.jobs.job_db.JtId;
import com.eot_app.nav_menu.jobs.job_detail.detail.jobdetial_model.JobStatusModel;
import com.eot_app.nav_menu.jobs.job_detail.job_status_pkg.JobStatus_Controller;
import com.eot_app.utility.AppConstant;
import com.eot_app.utility.AppUtility;
import com.eot_app.utility.App_preference;
import com.eot_app.utility.EotApp;
import com.eot_app.utility.language_support.LanguageController;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class AdpterJobHistory extends RecyclerView.Adapter<AdpterJobHistory.MyViewHolder> {
    private final Context context;
    private final int unScheduleHeaderPos = -1;
    LoadMoreItem loadMoreItem;
    private List<Job> jobdata;
    private OnJobSelection onJobSelection;
    private boolean isShowSiteName = false;


    public AdpterJobHistory(Context context) {
        this.context = context;
    }

    public void setFromShowSiteName(boolean isShowSiteName) {
        this.isShowSiteName = isShowSiteName;
    }

    public void setLoadMoreItem(LoadMoreItem loadMoreItem) {
        this.loadMoreItem = loadMoreItem;
    }

    public void setList(List<Job> jobdata) {
        if (this.jobdata == null)
            this.jobdata = new ArrayList<>();
        this.jobdata.addAll(jobdata);
        notifyDataSetChanged();

    }

    public void setOnJobSelection(OnJobSelection onJobSelection) {
        this.onJobSelection = onJobSelection;
    }


    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.work_history_job_list, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, final int position) {
        if (position == getItemCount() - 1) holder.blank_view.setVisibility(View.VISIBLE);
        else holder.blank_view.setVisibility(View.GONE);
        String today_date = AppUtility.getDateByFormat(AppUtility.dateTimeByAmPmFormate("EEE, dd MMM yyyy ,hh:mm ,a"
                , "EEE, dd MMM yyyy ,kk:mm"));
        String[] today_dt = today_date.split(",");
        String to_day = today_dt[1];


        String temp_date = "", cr_date;


        switchDefaultColor(holder, EotApp.getAppinstance().getResources().getColor(R.color.body_font_color));
        if (jobdata.get(position).getSchdlStart() != null && !jobdata.get(position).getSchdlStart().equals("")) {
            String[] date = AppUtility.getFormatedTime(jobdata.get(position).getSchdlStart());
            cr_date = date[0];
            String[] date_separated = cr_date.split(",");
            Spannable txtFrst;

            if (to_day.contains(date_separated[1])) {
                txtFrst = new SpannableString("Today" + ",");
            } else {
                txtFrst = new SpannableString(date_separated[0] + ",");
            }

            txtFrst.setSpan(new ForegroundColorSpan(Color.parseColor("#00848d")), 0, txtFrst.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
            holder.job_time.setText(date[1]);

            try {
                if (App_preference.getSharedprefInstance().getLoginRes().getIs24hrFormatEnable() != null
                        && App_preference.getSharedprefInstance().getLoginRes().getIs24hrFormatEnable().equals("0"))
                    holder.job_am_pm.setText(date[2]);
            } catch (Exception e) {
                e.getMessage();
            }


            if (position < jobdata.size())
                if (position == 0 || (jobdata.get(position - 1).getSchdlStart() != null && jobdata.get(position - 1).getSchdlStart().equals(""))) {
                    holder.txt_date.setText(txtFrst);
                    holder.txt_date.append(date_separated[1]);
                    holder.date_layout.setVisibility(View.VISIBLE);

                } else if (jobdata.get(position - 1).getSchdlStart() != null && !jobdata.get(position - 1).getSchdlStart().equals("")) {
                    String[] date2 = AppUtility.getFormatedTime(jobdata.get(position - 1).getSchdlStart());
                    temp_date = date2[0];
                    if (cr_date.equals(temp_date)) {
                        holder.date_layout.setVisibility(View.GONE);
                    } else {
                        holder.txt_date.setText(txtFrst);
                        holder.txt_date.append(date_separated[1]);
                        holder.date_layout.setVisibility(View.VISIBLE);
                    }
                }
        } else {
            holder.date_layout.setVisibility(View.GONE);
            holder.txt_date.setText("");
            holder.job_time.setText("");
            holder.job_am_pm.setText("");
        }

        if (unScheduleHeaderPos == position) {
            holder.date_layout.setVisibility(View.VISIBLE);
            holder.txt_date.setText(
                    LanguageController.getInstance().getMobileMsgByKey(AppConstant.unschedule_job)
            );
        }
        String label = jobdata.get(position).getLabel();
        String Nm = jobdata.get(position).getNm();
        if (label != null && Nm != null) {
            holder.label.setText(label + " - " + Nm);
        }
        String StrJt = "";
        List<JtId> jtaray = jobdata.get(position).getJtId();
        if (jtaray != null) {
            Iterator<JtId> it = jtaray.iterator();
            if (it.hasNext()) {
                StrJt += it.next().getTitle();
            }
            while (it.hasNext()) {
                StrJt = StrJt + ", " + it.next().getTitle();
            }
            holder.des.setText(StrJt);
            holder.des.setTextColor(EotApp.getAppinstance().getResources().getColor(R.color.body_font_color));
        }


        if (App_preference.getSharedprefInstance().getSiteNameShowInSetting()) {
            holder.tv_site_name.setVisibility(View.VISIBLE);
            if (!TextUtils.isEmpty(jobdata.get(position).getSnm()))
                holder.tv_site_name.setText(jobdata.get(position).getSnm());
            else holder.tv_site_name.setText("");
        } else holder.tv_site_name.setVisibility(View.GONE);


        String full_address = jobdata.get(position).getAdr() + ", " + jobdata.get(position).getCity();
        holder.address.setText(full_address);
        holder.prty.setImageResource(getImageRes(jobdata.get(position).getPrty()));

        try {
            setViewByJobStatus(holder, Integer.parseInt(jobdata.get(position).getStatus()));
        } catch (NumberFormatException e) {
            e.printStackTrace();
        }

        if (position == jobdata.size() - 1) {
            if (loadMoreItem != null)
                loadMoreItem.onLoadMoreItem(jobdata.size(), ClientWorkHistoryList.FragmentTypes.JOB);
        }

    }

    private void switchDefaultColor(MyViewHolder holder, int color) {
        holder.status.setTextColor(color);
        holder.job_time.setTextColor(color);
        holder.job_am_pm.setTextColor(color);
        holder.status.setTextColor(color);
    }

    private void setViewByJobStatus(MyViewHolder holder, int status) {
        JobStatusModel jobStatusObject = JobStatus_Controller.getInstance().getStatusObjectById(String.valueOf(status));
        if (jobStatusObject != null) {
            holder.status.setText(jobStatusObject.getStatus_name());
            holder.status_constraints.setBackgroundResource(R.color.white);

            int id = context.getResources().getIdentifier(jobStatusObject.getImg(), "drawable", context.getPackageName());
            holder.status_img.setImageResource(id);
            if (jobStatusObject.getStatus_no().equals("7")) {
                holder.status_constraints.setBackgroundResource(R.color.in_progress);
                switchDefaultColor(holder, EotApp.getAppinstance().getResources().getColor(R.color.white));
            }
        }
    }

    private int getImageRes(String prty) {
        if (prty != null) {
            switch (prty) {
                case "1":
                    return R.drawable.prty_low;
                case "2":
                    return R.drawable.prty_medium;
                case "3":
                    return R.drawable.prty_high;
            }
        }
        return R.drawable.prty_low;
    }

    @Override
    public int getItemCount() {
        return jobdata == null ? 0 : jobdata.size();
    }


    public interface OnJobSelection {
        void onJobSelected(Job job);

    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        TextView label, status, des, address, job_time, job_am_pm, txt_date, tv_site_name;//  job_date;
        LinearLayout item_main_layout, date_layout;
        ImageView prty, status_img;
        ConstraintLayout status_constraints;
        View blank_view;


        public MyViewHolder(View itemView) {
            super(itemView);
            item_main_layout = itemView.findViewById(R.id.item_main_layout);
            blank_view = itemView.findViewById(R.id.blank_view);
            label = itemView.findViewById(R.id.label);
            des = itemView.findViewById(R.id.des);
            address = itemView.findViewById(R.id.address);
            prty = itemView.findViewById(R.id.prty);
            status = itemView.findViewById(R.id.status);
            job_time = itemView.findViewById(R.id.job_time);
            job_am_pm = itemView.findViewById(R.id.job_am_pm);
            status_img = itemView.findViewById(R.id.status_img);
            status_constraints = itemView.findViewById(R.id.status_constraints);
            txt_date = itemView.findViewById(R.id.txt_date);
            date_layout = itemView.findViewById(R.id.date_layout);
            tv_site_name = itemView.findViewById(R.id.tv_site_name);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (onJobSelection != null)
                        try {
                            onJobSelection.onJobSelected(jobdata.get(getAdapterPosition()));
                        } catch (ArrayIndexOutOfBoundsException e) {
                            e.printStackTrace();
                        }
                }
            });
        }
    }
}
