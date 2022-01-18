package com.eot_app.nav_menu.equipment.adpter;

import android.content.Context;
import android.graphics.Color;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.eot_app.R;
import com.eot_app.nav_menu.equipment.model.aduit_job_history.Aduit_Job_History_Res;
import com.eot_app.nav_menu.jobs.job_detail.detail.jobdetial_model.JobStatusModel;
import com.eot_app.nav_menu.jobs.job_detail.job_status_pkg.JobStatus_Controller;
import com.eot_app.utility.AppConstant;
import com.eot_app.utility.AppUtility;
import com.eot_app.utility.language_support.LanguageController;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class AdpterAuditHistory extends RecyclerView.Adapter<AdpterAuditHistory.MyViewHolder> {
    private final Context mContext;
    private OnAuditSelection onAuditSelection;
    private List<Aduit_Job_History_Res> list;

    public AdpterAuditHistory(Context mContext) {
        this.mContext = mContext;
        this.list = new ArrayList<>();
    }

    public void setOnAuditSelection(OnAuditSelection onAuditSelection) {
        this.onAuditSelection = onAuditSelection;
    }


    public void setList(List<Aduit_Job_History_Res> list) {

        this.list = list;
        notifyDataSetChanged();

    }


    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int type) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.adpter_aduit_history, viewGroup, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {

        String label = list.get(position).getLabel();
        String nm = list.get(position).getNm();
        if (!TextUtils.isEmpty(this.list.get(position).getSchdlStart())) {
            long strtDateLong = Long.parseLong(this.list.get(position).getSchdlStart());
            String dateFormat = "dd MMM yyyy";
            String strtdate = AppUtility.getDateWithFormate2(strtDateLong * 1000, dateFormat);
            holder.date.setText(strtdate);
        }


        setTimeStatus(list.get(position).getSchdlStart(), list.get(position).getSchdlFinish(), list.get(position).getStatus(), holder);


        if (label != null && nm != null) {
            holder.label.setText(label + "-" + nm);
        }

        String full_address = list.get(position).getAdr();
        holder.address.setText(full_address);


        if (list.get(position).getStatus() != null) {
            try {
                setViewByAuditStatus(holder, Integer.parseInt(list.get(position).getStatus()));
            } catch (NumberFormatException e) {
                e.printStackTrace();
            }
        }

    }


    private void setTimeStatus(String start_date, String end_date, String status, MyViewHolder holder) {

        if (!TextUtils.isEmpty(start_date) && status.equals("9")) {
            holder.condition.setText("Done");

            holder.condition.setBackgroundColor(Color.parseColor("#28A745"));
            holder.condition.setTextColor(Color.WHITE);
            return;
        }

        if (TextUtils.isEmpty(start_date)) {
            holder.condition.setText("");
            return;
        }


        Date sdate = new Date(Long.parseLong(start_date) * 1000);

        if (AppUtility.isAfterDay(sdate, Calendar.getInstance().getTime())) {
            holder.condition.setText(LanguageController.getInstance().getMobileMsgByKey(AppConstant.upcoming));
            holder.condition.setTextColor(Color.WHITE);
            holder.condition.setBackgroundColor(Color.parseColor("#29A2B8"));
        } else if (AppUtility.isSameDay(sdate, Calendar.getInstance().getTime())) {
            holder.condition.setText(LanguageController.getInstance().getMobileMsgByKey(AppConstant.today));
            holder.condition.setBackgroundColor(Color.parseColor("#29A2B8"));
            holder.condition.setTextColor(Color.WHITE);
        } else
            //check the start date is before the current date
            if (!AppUtility.isAfterDay(sdate, Calendar.getInstance().getTime())) {
                if (!TextUtils.isEmpty(end_date)) {
                    Date edate = new Date(Long.parseLong(end_date) * 1000);
                    if (!AppUtility.isAfterDay(edate, Calendar.getInstance().getTime())) {
                        holder.condition.setText(LanguageController.getInstance().getMobileMsgByKey(AppConstant.overdue));
                        holder.condition.setTextColor(Color.WHITE);
                        holder.condition.setBackgroundColor(Color.RED);
                        return;
                    }
                }

                holder.condition.setText("Expired");
                holder.condition.setTextColor(Color.BLACK);
            }


    }


    private void setViewByAuditStatus(MyViewHolder holder, int status) {
        JobStatusModel jobStatusObject = JobStatus_Controller.getInstance().getStatusObjectById(String.valueOf(status));
        if (jobStatusObject != null) {
            if (status == 8) {
                holder.status.setText(LanguageController.getInstance().getMobileMsgByKey(AppConstant.new_on_hold));
                holder.status_img.setImageDrawable(mContext.getDrawable(R.drawable.ic_pendng_task));

            } else {
                int id = mContext.getResources().getIdentifier(jobStatusObject.getImg(), "drawable", mContext.getPackageName());
                holder.status_img.setImageResource(id);

                holder.status.setText(jobStatusObject.getStatus_name());
            }

//                int id = mContext.getResources().getIdentifier(jobStatusObject.getImg(), "drawable", mContext.getPackageName());
//            holder.status_img.setImageResource(id);
        }
    }


    @Override
    public int getItemCount() {
        return list == null ? 0 : list.size();
    }

    public interface OnAuditSelection {
        void onAuditSelected(String audit);


    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        TextView label, address, status, date, condition;
        ImageView status_img;


        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            label = itemView.findViewById(R.id.label);
            address = itemView.findViewById(R.id.address);
            status_img = itemView.findViewById(R.id.status_img);
            status = itemView.findViewById(R.id.status);
            date = itemView.findViewById(R.id.date);
            condition = itemView.findViewById(R.id.condition);


            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (onAuditSelection != null)
                        try {
                            onAuditSelection.onAuditSelected(list.get(getAdapterPosition()).getAudId());
                        } catch (ArrayIndexOutOfBoundsException e) {
                            e.printStackTrace();
                        }
                }
            });
        }
    }
}
