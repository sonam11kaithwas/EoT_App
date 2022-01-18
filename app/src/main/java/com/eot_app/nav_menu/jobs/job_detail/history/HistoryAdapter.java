package com.eot_app.nav_menu.jobs.job_detail.history;


import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.eot_app.R;
import com.eot_app.utility.AppConstant;
import com.eot_app.utility.AppUtility;
import com.eot_app.utility.App_preference;
import com.eot_app.utility.util_interfaces.MyListItemSelected;

import java.util.List;

public class HistoryAdapter extends RecyclerView.Adapter<HistoryAdapter.MyViewHolder> {
    private final MyListItemSelected<History> mListner;
    private List<History> hisData;

    public HistoryAdapter(List<History> hisData, MyListItemSelected<History> mListner) {
        this.hisData = hisData;
        this.mListner = mListner;
    }

    public void updateRecords(List<History> hisData) {
        this.hisData = hisData;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_historyfragment, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, final int position) {
        setViewByJobStatus(holder, Integer.parseInt(hisData.get(holder.getAdapterPosition()).getStatus()));
        if (position % 2 == 0) {
            holder.txtStatusName.setVisibility(View.VISIBLE);
            holder.txtdatetime.setVisibility(View.VISIBLE);
            holder.txtStatusName1.setVisibility(View.GONE);
            holder.txtdatetime1.setVisibility(View.GONE);
            holder.txtStatusHis.setVisibility(View.VISIBLE);
            holder.txtStatusHis1.setVisibility(View.GONE);
            String[] formated_date = AppUtility.getFormatedTime(hisData.get(holder.getAdapterPosition()).getTime());
            String frmt_dt = formated_date[0];
            String[] frmtDt = frmt_dt.split(",");
            if (formated_date != null) {

                try {
                    if (App_preference.getSharedprefInstance().getLoginRes().getIs24hrFormatEnable() != null &&
                            App_preference.getSharedprefInstance().getLoginRes().getIs24hrFormatEnable().equals("0"))
                        holder.txtdatetime.setText(frmtDt[1] + "  " + formated_date[1] + " " + formated_date[2]);
                    else holder.txtdatetime.setText(frmtDt[1] + "  " + formated_date[1] + "");

                } catch (Exception e) {
                    e.getMessage();
                }

            }
            if (hisData.get(holder.getAdapterPosition()).getReferencebyType().equals("1")) {
                holder.txtStatusName.setText(hisData.get(holder.getAdapterPosition()).getReferencebyName());
            } else {
                holder.txtStatusName.setText(hisData.get(holder.getAdapterPosition()).getReferencebyName() + " (A)");
            }
        } else {
            holder.txtStatusName1.setVisibility(View.VISIBLE);
            holder.txtdatetime1.setVisibility(View.VISIBLE);
            holder.txtStatusName.setVisibility(View.GONE);
            holder.txtdatetime.setVisibility(View.GONE);
            holder.txtStatusHis1.setVisibility(View.VISIBLE);
            holder.txtStatusHis.setVisibility(View.GONE);
            holder.txtStatusName1.setText(hisData.get(holder.getAdapterPosition()).getName());
            String[] formated_date = AppUtility.getFormatedTime(hisData.get(holder.getAdapterPosition()).getTime());
            String frmt_dt = formated_date[0];
            String[] frmtDt = frmt_dt.split(",");
            if (formated_date != null) {
                try {
                    if (App_preference.getSharedprefInstance().getLoginRes().getIs24hrFormatEnable() != null && App_preference.getSharedprefInstance().getLoginRes().getIs24hrFormatEnable().equals("0"))
                        holder.txtdatetime1.setText(frmtDt[1] + "  " + formated_date[1] + " " + formated_date[2]);
                    else holder.txtdatetime1.setText(frmtDt[1] + "  " + formated_date[1] + "");

                } catch (Exception e) {
                    e.getMessage();
                }

            }
            if (hisData.get(holder.getAdapterPosition()).getReferencebyType().equals("1")) {
                holder.txtStatusName1.setText(hisData.get(holder.getAdapterPosition()).getReferencebyName());
            } else {
                holder.txtStatusName1.setText(hisData.get(holder.getAdapterPosition()).getReferencebyName() + " (A)");
            }
        }
    }

    private History getItem(int position) {
        return hisData.get(position);
    }

    @Override
    public int getItemCount() {
        return hisData.size();
    }

    private void setViewByJobStatus(MyViewHolder holder, int status) {
        switch (status) {
            case 1:
                holder.imageViewStatus.setImageResource(R.drawable.ic_new_task);
                holder.txtStatusHis.setText(AppConstant.status[0]);
                holder.txtStatusHis1.setText(AppConstant.status[0]);
                break;
            case 2:
                holder.imageViewStatus.setImageResource(R.drawable.ic_accepted_task);
                holder.txtStatusHis.setText(AppConstant.status[1]);
                holder.txtStatusHis1.setText(AppConstant.status[1]);
                break;
            case 3:
                holder.imageViewStatus.setImageResource(R.drawable.ic_rejected);
                holder.txtStatusHis.setText(AppConstant.status[2]);
                holder.txtStatusHis1.setText(AppConstant.status[2]);
                break;
            case 4:
                holder.imageViewStatus.setImageResource(R.drawable.ic_cancel);
                holder.txtStatusHis.setText(AppConstant.status[3]);
                holder.txtStatusHis1.setText(AppConstant.status[3]);
                break;
            case 5:
                holder.imageViewStatus.setImageResource(R.drawable.ic_travelling_small_icon);
                holder.txtStatusHis.setText(AppConstant.status[4]);
                holder.txtStatusHis1.setText(AppConstant.status[4]);
                break;
            case 6:
                holder.imageViewStatus.setImageResource(R.drawable.ic_break);
                holder.txtStatusHis.setText(AppConstant.status[5]);
                holder.txtStatusHis1.setText(AppConstant.status[5]);
                break;
            case 7:
                holder.imageViewStatus.setImageResource(R.drawable.in_progress);
                holder.txtStatusHis.setText(AppConstant.status[6]);
                holder.txtStatusHis1.setText(AppConstant.status[6]);
                break;
            case 8:
                holder.imageViewStatus.setImageResource(R.drawable.breake_job_his);
//                holder.imageViewStatus.setImageResource(R.drawable.ic_pendng_task);
//                holder.txtStatusHis.setText(AppConstant.status[7]);
//                holder.txtStatusHis1.setText(AppConstant.status[7]);
                holder.txtStatusHis.setText(AppConstant.status[7]);
                holder.txtStatusHis1.setText(AppConstant.status[7]);
                break;
            case 9:
                holder.imageViewStatus.setImageResource(R.drawable.ic_complete_task);
                holder.txtStatusHis.setText(AppConstant.status[8]);
                holder.txtStatusHis1.setText(AppConstant.status[8]);
                break;
            case 10:
                holder.imageViewStatus.setImageResource(R.drawable.ic_closed_small_icon);
                holder.txtStatusHis.setText(AppConstant.status[9]);
                holder.txtStatusHis1.setText(AppConstant.status[9]);
                break;
            case 12:
                holder.imageViewStatus.setImageResource(R.drawable.ic_pendng_task);
                holder.txtStatusHis.setText(AppConstant.status[10]);
                holder.txtStatusHis1.setText(AppConstant.status[10]);
                break;
        }
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {
        TextView txtStatusName, txtdatetime, txtStatusName1, txtdatetime1, txtStatusHis, txtStatusHis1;
        ImageView imageViewStatus;

        public MyViewHolder(View itemView) {
            super(itemView);
            txtStatusName = itemView.findViewById(R.id.txtStatusName);
            txtdatetime = itemView.findViewById(R.id.txtdatetime);
            txtStatusName1 = itemView.findViewById(R.id.txtStatusName1);
            txtdatetime1 = itemView.findViewById(R.id.txtdatetime1);
            txtStatusHis = itemView.findViewById(R.id.txtStatusHis);
            txtStatusHis1 = itemView.findViewById(R.id.txtStatusHis1);
            imageViewStatus = itemView.findViewById(R.id.imageViewStatus);
        }
    }
}
