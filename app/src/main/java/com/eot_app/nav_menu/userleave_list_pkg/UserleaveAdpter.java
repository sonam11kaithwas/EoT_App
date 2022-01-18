package com.eot_app.nav_menu.userleave_list_pkg;

import android.app.Activity;
import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.eot_app.R;
import com.eot_app.nav_menu.jobs.job_detail.detail.jobdetial_model.JobStatusModel;
import com.eot_app.nav_menu.jobs.job_detail.job_status_pkg.JobStatus_Controller;
import com.eot_app.utility.AppUtility;
import com.eot_app.utility.EotApp;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Sona-11 on 9/11/21.
 */
public class UserleaveAdpter extends RecyclerView.Adapter<UserleaveAdpter.ViewHolder> {
    private final List<UserLeaveResModel> filterList;

    Context mContex;
    private List<UserLeaveResModel> list;

    public UserleaveAdpter(Context mContex, List<UserLeaveResModel> items) {
        this.mContex = mContex;
        this.filterList = new ArrayList<UserLeaveResModel>();
        this.list = items;
        this.filterList.addAll(this.list);
    }


    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.user_leave_list_lay, parent, false);
        return new UserleaveAdpter.ViewHolder(view);
    }

    private void switchDefaultColor(ViewHolder holder, int color) {
        holder.status.setTextColor(color);
        holder.status.setTextColor(color);
    }

    // Do Search...
    public void filterByStatus(final String text) {
        System.out.println();
        // Searching could be complex..so we will dispatch it to a different thread...
        new Thread(new Runnable() {
            @Override
            public void run() {

                // Clear the filter list
                filterList.clear();

                // If there is no search value, then add all original list items to filter list
                if (TextUtils.isEmpty(text)) {

                    filterList.addAll(list);

                } else {
                    String trimText = text.trim();
                    String[] s = trimText.split(" ");
                    // Iterate in the original List and add it to filter list...
                    for (UserLeaveResModel item : list) {
                        for (String status : s)
                            if (item.getStatus().toLowerCase().contains(status.toLowerCase())) {
                                // Adding Matched items
                                filterList.add(item);
                            }
                    }
                    //  notifyDataSetChanged();

                }

                // Set on UI Thread
                ((Activity) mContex).runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        // Notify the List that the DataSet has changed...
                        notifyDataSetChanged();
                    }
                });

            }
        }).start();
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        UserLeaveResModel model = filterList.get(position);

        String statDate = AppUtility.getDateWithFormate(Long.parseLong(model.getStartDateTime()),
                "dd MMM yyyy");
        String endDate = AppUtility.getDateWithFormate(Long.parseLong(model.getFinishDateTime()),
                "dd MMM yyyy");

        String statTime = AppUtility.getDateWithFormate(Long.parseLong(model.getStartDateTime()), AppUtility.dateTimeByAmPmFormate(
                "hh:mm a",
                "kk:mm"));
        String endTime = AppUtility.getDateWithFormate(Long.parseLong(model.getFinishDateTime()), AppUtility.dateTimeByAmPmFormate(
                "hh:mm a",
                "kk:mm"));

        holder.leave_time_label1.setText(statTime);
        holder.leave_time_label2.setText(endTime);


        holder.leave_date_label1.setText(statDate);
        holder.leave_date_label2.setText(endDate);

        if (model.getNote() != null && !model.getNote().equals("")) {
            holder.leave_label.setVisibility(View.VISIBLE);
            holder.leave_label.setText(model.getNote());
        } else {
            holder.leave_label.setVisibility(View.GONE);
        }
        if (model.getReason() != null && !model.getReason().equals("")) {
            holder.leave_notes.setVisibility(View.VISIBLE);
            holder.leave_notes.setText(model.getReason());
        } else {
            holder.leave_notes.setVisibility(View.GONE);
        }


        if (model.getStatus() != null && !model.getStatus().equals(""))
            try {
                setViewByAuditStatus(holder, Integer.parseInt(model.getStatus()));
            } catch (Exception e) {
                e.printStackTrace();
            }


    }

    private void setViewByAuditStatus(ViewHolder holder, int status) {
        JobStatusModel jobStatusObject = JobStatus_Controller.getInstance().getStatusObjectById(String.valueOf(status));
        if (jobStatusObject != null) {

            holder.status.setText(jobStatusObject.getStatus_name());

            holder.status_constraints.setBackgroundResource(R.color.white);

            int id = mContex.getResources().getIdentifier(jobStatusObject.getImg(), "drawable", mContex.getPackageName());
            holder.status_img.setImageResource(id);
        }
        if (jobStatusObject.getStatus_no().equals("7")) {
            holder.status_constraints.setBackgroundResource(R.color.in_progress);
            switchDefaultColor(holder, EotApp.getAppinstance().getResources().getColor(R.color.white));
        } else {
            switchDefaultColor(holder, EotApp.getAppinstance().getResources().getColor(R.color.txt_color));
        }
    }

    @Override
    public int getItemCount() {
        return filterList.size();
    }

    public void UpdateList(List<UserLeaveResModel> items) {
        this.list = items;
        if (this.filterList != null) {
            filterList.clear();
            this.filterList.addAll(this.list);
        }
        notifyDataSetChanged();
    }

    public void UpdateAllDataList() {
        if (this.filterList != null) {
            filterList.clear();
            this.filterList.addAll(this.list);
        }
        notifyDataSetChanged();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public View mView;
        TextView txt_date, address, status, des, site_name, leave_time_label1, leave_time_label2, leave_date_label1, leave_date_label2;
        TextView leave_label, leave_notes;
        ImageView status_img;
        LinearLayout date_layout;
        ConstraintLayout status_constraints;
        View blank_view;


        public ViewHolder(View view) {
            super(view);
            mView = view;
            leave_label = itemView.findViewById(R.id.leave_label);
            leave_notes = itemView.findViewById(R.id.leave_notes);


            blank_view = itemView.findViewById(R.id.blank_view);
            txt_date = itemView.findViewById(R.id.txt_date);
            leave_time_label1 = itemView.findViewById(R.id.leave_time_label1);
            leave_time_label2 = itemView.findViewById(R.id.leave_time_label2);
            leave_date_label1 = itemView.findViewById(R.id.leave_date_label1);
            leave_date_label2 = itemView.findViewById(R.id.leave_date_label2);
            address = itemView.findViewById(R.id.address);
            status_img = itemView.findViewById(R.id.status_img);
            status = itemView.findViewById(R.id.status);
            status_constraints = itemView.findViewById(R.id.status_constraints);
            date_layout = itemView.findViewById(R.id.date_layout);
            des = itemView.findViewById(R.id.des);

            site_name = itemView.findViewById(R.id.site_name);


        }
    }


}


