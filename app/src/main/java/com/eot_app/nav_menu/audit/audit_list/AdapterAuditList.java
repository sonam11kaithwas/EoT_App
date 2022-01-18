package com.eot_app.nav_menu.audit.audit_list;

import android.content.Context;
import android.graphics.Color;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
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
import com.eot_app.nav_menu.audit.audit_list.audit_mvp.model.AuditList_Res;
import com.eot_app.nav_menu.jobs.job_detail.detail.jobdetial_model.JobStatusModel;
import com.eot_app.nav_menu.jobs.job_detail.job_status_pkg.JobStatus_Controller;
import com.eot_app.utility.AppConstant;
import com.eot_app.utility.AppUtility;
import com.eot_app.utility.App_preference;
import com.eot_app.utility.EotApp;
import com.eot_app.utility.language_support.LanguageController;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Mahendra Dabi on 6/11/19.
 */
public class AdapterAuditList extends RecyclerView.Adapter<AdapterAuditList.MyViewHolder> {
    private final Context mContext;
    private OnAuditSelection onAuditSelection;
    private List<AuditList_Res> list;
    private boolean isShowSiteName = false;


    public AdapterAuditList(Context mContext) {
        this.mContext = mContext;
        this.list = new ArrayList<>();
    }

    public void setFromShowSiteName(boolean isShowSiteName) {
        this.isShowSiteName = isShowSiteName;
    }

    public void setOnAuditSelection(OnAuditSelection onAuditSelection) {
        this.onAuditSelection = onAuditSelection;
    }

    public void setList(List<AuditList_Res> list) {

        this.list = list;
        notifyDataSetChanged();

    }


    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int type) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_audit_list, viewGroup, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        if (position == getItemCount() - 1) holder.blank_view.setVisibility(View.VISIBLE);
        else holder.blank_view.setVisibility(View.GONE);


        String today_date = AppUtility.getDateByFormat(AppUtility.dateTimeByAmPmFormate(
                "EEE, dd MMM yyyy ,hh:mm ,a", "EEE, dd MMM yyyy ,kk:mm"));
        String[] today_dt = today_date.split(",");
        String to_day = today_dt[1];


        String temp_date = "", cr_date;

        switchDefaultColor(holder, EotApp.getAppinstance().getResources().getColor(R.color.body_font_color));
        if (list.get(position).getSchdlStart() != null && !list.get(position).getSchdlStart().equals("")) {
            String[] date = AppUtility.getFormatedTime(list.get(position).getSchdlStart());
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
                if (App_preference.getSharedprefInstance().getLoginRes().getIs24hrFormatEnable() != null &&
                        App_preference.getSharedprefInstance().getLoginRes().getIs24hrFormatEnable().equals("0"))
                    holder.job_am_pm.setText(date[2]);
            } catch (Exception e) {
                e.getMessage();
            }
            if (position < list.size())
                if (position == 0 || (list.get(position - 1).getSchdlStart() != null && list.get(position - 1).getSchdlStart().equals(""))) {
                    holder.txt_date.setText(txtFrst);
                    holder.txt_date.append(date_separated[1]);
                    holder.date_layout.setVisibility(View.VISIBLE);

                } else if (list.get(position - 1).getSchdlStart() != null && !list.get(position - 1).getSchdlStart().equals("")) {
                    String[] date2 = AppUtility.getFormatedTime(list.get(position - 1).getSchdlStart());
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
        String label = list.get(position).getLabel();
        String Nm = list.get(position).getNm();

        if (label != null && Nm != null) {
            if (label != null && Nm.equals(""))
                holder.label.setText(label);
            else
                holder.label.setText(label + " - " + Nm);
        } else {
            holder.label.setText("Audit" + " - " + "client");
        }
        /**show and hide site name from list*/
        if (isShowSiteName)
            holder.site_name.setVisibility(View.VISIBLE);
        else
            holder.site_name.setVisibility(View.GONE);

        if (list.get(position).getSnm() != null && !list.get(position).getSnm().equals("")) {
            holder.site_name.setText(list.get(position).getSnm());
        }


        if (list.get(position).getAudId().equals(list.get(position).getTempId())) {
            holder.des.setVisibility(View.VISIBLE);
            holder.des.setText(LanguageController.getInstance().getMobileMsgByKey(AppConstant.audit_not_sync));
            holder.des.setTextColor(EotApp.getAppinstance().getResources().getColor(android.R.color.holo_red_light));
        } else if (holder.des.getVisibility() == View.VISIBLE)
            holder.des.setVisibility(View.GONE);

        String full_address = list.get(position).getAdr() + ", " + list.get(position).getCity();
        holder.address.setText(full_address);


        if (list.get(position).getStatus() != null)
            try {
                setViewByAuditStatus(holder, Integer.parseInt(list.get(position).getStatus()));
            } catch (NumberFormatException e) {
                e.printStackTrace();
            }


        if (list.get(position).getEquArray() != null && list.get(position).getEquArray().size() > 0) {
            holder.equi_flag.setVisibility(View.VISIBLE);
        } else {
            holder.equi_flag.setVisibility(View.GONE);
        }
        if (list.get(position).getAttachCount() != null && Integer.parseInt(list.get(position).getAttachCount()) != 0) {
            holder.attachmemt_flag.setVisibility(View.VISIBLE);
        } else {
            holder.attachmemt_flag.setVisibility(View.GONE);
        }

    }

    private void setViewByAuditStatus(MyViewHolder holder, int status) {
        JobStatusModel jobStatusObject = JobStatus_Controller.getInstance().getStatusObjectById(String.valueOf(status));
        if (jobStatusObject != null) {
            /*for this prod*/
            if (status == 8) {
                holder.status.setText(LanguageController.getInstance().getMobileMsgByKey(AppConstant.new_on_hold));
            } else {
                holder.status.setText(jobStatusObject.getStatus_name());
            }
            holder.status_constraints.setBackgroundResource(R.color.white);
            if (status == 8) {
                holder.status_img.setImageDrawable(mContext.getDrawable(R.drawable.ic_pendng_task));
            } else {
                int id = mContext.getResources().getIdentifier(jobStatusObject.getImg(), "drawable", mContext.getPackageName());
                holder.status_img.setImageResource(id);
            }
            if (jobStatusObject.getStatus_no().equals("7")) {
                holder.status_constraints.setBackgroundResource(R.color.in_progress);
                switchDefaultColor(holder, EotApp.getAppinstance().getResources().getColor(R.color.white));
            } else {
                switchDefaultColor(holder, EotApp.getAppinstance().getResources().getColor(R.color.txt_color));
            }
        }
    }

    private void switchDefaultColor(MyViewHolder holder, int color) {
        holder.status.setTextColor(color);
        holder.job_time.setTextColor(color);
        holder.job_am_pm.setTextColor(color);
        holder.status.setTextColor(color);
    }

    // update auditstatus
    public void updateAuditStatus(int position, int status) {
        if (list != null && getItemCount() > position) {
            for (int i = 0; i < list.size(); i++)
                if (list.get(i).getStatus() != null && list.get(i).getStatus().equals("7")) {
                    list.get(i).setStatus("8");
                    break;
                }

            list.get(position).setStatus(status + "");
            notifyDataSetChanged();
        }
    }


    @Override
    public int getItemCount() {
        return list == null ? 0 : list.size();
    }

    public interface OnAuditSelection {
        void onAuditSelected(AuditList_Res audit, int position);

        void onLoadMore(boolean b);
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        TextView txt_date, job_time, job_am_pm, label, address, status, des, site_name;
        ImageView status_img;
        LinearLayout date_layout;
        ConstraintLayout status_constraints;
        View blank_view;
        ImageView equi_flag, attachmemt_flag;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            blank_view = itemView.findViewById(R.id.blank_view);
            txt_date = itemView.findViewById(R.id.txt_date);
            job_time = itemView.findViewById(R.id.job_time);
            job_am_pm = itemView.findViewById(R.id.job_am_pm);
            label = itemView.findViewById(R.id.label);
            address = itemView.findViewById(R.id.address);
            status_img = itemView.findViewById(R.id.status_img);
            status = itemView.findViewById(R.id.status);
            status_constraints = itemView.findViewById(R.id.status_constraints);
            date_layout = itemView.findViewById(R.id.date_layout);
            des = itemView.findViewById(R.id.des);
            equi_flag = itemView.findViewById(R.id.equi_flag);
            attachmemt_flag = itemView.findViewById(R.id.attachmemt_flag);
            site_name = itemView.findViewById(R.id.site_name);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (onAuditSelection != null)
                        try {
                            onAuditSelection.onAuditSelected(list.get(getAdapterPosition()), getAdapterPosition());
                        } catch (ArrayIndexOutOfBoundsException e) {
                            e.printStackTrace();
                        }
                }
            });
        }
    }
}
