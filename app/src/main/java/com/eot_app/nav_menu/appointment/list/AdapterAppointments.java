package com.eot_app.nav_menu.appointment.list;

import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.eot_app.R;
import com.eot_app.nav_menu.appointment.list.common.CommonAppointmentModel;
import com.eot_app.utility.AppConstant;
import com.eot_app.utility.AppUtility;
import com.eot_app.utility.language_support.LanguageController;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Mahendra Dabi on 6/11/19.
 */
public class AdapterAppointments extends RecyclerView.Adapter<AdapterAppointments.MyViewHolder> {
    private final int APPOINT_VIEW = 0;
    private final int JOB_VIEW = 1;
    private final int AUDIT_VIEW = 2;
    Context mContext;
    List<CommonAppointmentModel> list;
    OnAppointmentItemClicked onAppointmentItemClicked;

    public AdapterAppointments(Context mContext) {
        this.mContext = mContext;
        if (this.list == null)
            this.list = new ArrayList<>();
    }

    public void setOnAppointmentItemClicked(OnAppointmentItemClicked onAppointmentItemClicked) {
        this.onAppointmentItemClicked = onAppointmentItemClicked;
    }

    public void setList(List<CommonAppointmentModel> newList) {
        if (this.list == null)
            this.list = new ArrayList<>();
        this.list.clear();
        this.list.addAll(newList);
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int type) {
        if (type == JOB_VIEW) {
            View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_appointment_job, viewGroup, false);
            return new MyViewHolder(view);
        } else if (type == APPOINT_VIEW) {
            View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_appointment, viewGroup, false);
            return new MyViewHolder(view);
        } else if (type == AUDIT_VIEW) {
            View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_appointment_audit, viewGroup, false);
            return new MyViewHolder(view);
        } else {
            View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_appointment, viewGroup, false);
            return new MyViewHolder(view);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull final MyViewHolder holder, final int position) {
        CommonAppointmentModel appointment = list.get(position);
        holder.tv_not_sync.setVisibility(View.GONE);
        holder.tv_title.setVisibility(View.VISIBLE);

        holder.tv_title.setText(appointment.getTitle());
        holder.tv_address.setText(appointment.getDes());

        //set type tag on
        if (appointment.getType() == APPOINT_VIEW) {
            holder.tv_type.setText(LanguageController.getInstance().getMobileMsgByKey(AppConstant.appointment));
            /**appointment complete flat with tick option*/
            if (!TextUtils.isEmpty(appointment.getStatus()) && appointment.getStatus().equals("9"))
                holder.img_appointment_status.setVisibility(View.VISIBLE);
            else holder.img_appointment_status.setVisibility(View.GONE);


        } else if (appointment.getType() == JOB_VIEW) {
            holder.tv_type.setText(LanguageController.getInstance().getMobileMsgByKey(AppConstant.job));
            /*****Job Item**/
            if (appointment.getJobItemCount() > 0) {
                holder.item_flag.setVisibility(View.VISIBLE);
            } else holder.item_flag.setVisibility(View.GONE);

            /****Equipment for Job & Audit**/
            if (appointment.getEquipmentCount() > 0) {
                holder.equi_flag.setVisibility(View.VISIBLE);
            } else {
                holder.equi_flag.setVisibility(View.GONE);
            }


        } else if (appointment.getType() == AUDIT_VIEW) {
            holder.tv_type.setText(LanguageController.getInstance().getMobileMsgByKey(AppConstant.audit_nav));

            /****Equipment for Job & Audit**/
            if (appointment.getEquipmentCount() > 0) {
                holder.equi_flag.setVisibility(View.VISIBLE);
            } else {
                holder.equi_flag.setVisibility(View.GONE);
            }

        }


        /*****check that job , appointment and audit has been synced or not and show the message*/
        if (appointment.getId().equals(appointment.getTempId())) {
            holder.tv_title.setVisibility(View.GONE);
            holder.tv_not_sync.setVisibility(View.VISIBLE);
            if (appointment.getType() == APPOINT_VIEW)
                holder.tv_not_sync.setText(LanguageController.getInstance().getMobileMsgByKey(AppConstant.appointment_not_sync));
            else if (appointment.getType() == JOB_VIEW)
                holder.tv_not_sync.setText(LanguageController.getInstance().getMobileMsgByKey(AppConstant.job_not_sync));
            else if (appointment.getType() == AUDIT_VIEW)
                holder.tv_not_sync.setText(LanguageController.getInstance().getMobileMsgByKey(AppConstant.audit_not_sync));
        }


        try {
            String formate = AppUtility.dateTimeByAmPmFormate("hh:mm a", "kk:mm");
            String time =
                    AppUtility.getDateWithFormate(Long.parseLong(appointment.getStartDateTime()), formate);
            holder.tv_start_time.setText(time);
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    if (onAppointmentItemClicked != null)
                        onAppointmentItemClicked.onAppointItemClick(list.get(holder.getAdapterPosition()));
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

        /***appointment/job/audit attachment flag*/
        if (appointment.getAttchmentCount() != 0)
            holder.attachmemt_flag.setVisibility(View.VISIBLE);
        else
            holder.attachmemt_flag.setVisibility(View.GONE);
    }

    @Override
    public int getItemCount() {
        return list == null ? 0 : list.size();
    }

    @Override
    public int getItemViewType(int position) {
        if (list.get(position).getType() == 0)
            return APPOINT_VIEW;
        else if (list.get(position).getType() == 1)
            return JOB_VIEW;
        else if (list.get(position).getType() == 2)
            return AUDIT_VIEW;
        else return APPOINT_VIEW;
    }

    public interface OnAppointmentItemClicked {
        void onAppointItemClick(CommonAppointmentModel cpm);
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        TextView tv_start_time, tv_type, tv_title, tv_address, tv_not_sync;
        ConstraintLayout status_constraints;
        AppCompatImageView img_appointment_status;
        ImageView item_flag, equi_flag, attachmemt_flag;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            tv_start_time = itemView.findViewById(R.id.tv_start_time);
            tv_type = itemView.findViewById(R.id.tv_type);
            tv_not_sync = itemView.findViewById(R.id.tv_not_sync);
            tv_title = itemView.findViewById(R.id.tv_title);
            tv_address = itemView.findViewById(R.id.tv_address);
            img_appointment_status = itemView.findViewById(R.id.img_appointment_status);

            item_flag = itemView.findViewById(R.id.item_flag);
            equi_flag = itemView.findViewById(R.id.equi_flag);
            attachmemt_flag = itemView.findViewById(R.id.attachmemt_flag);

        }
    }

}
