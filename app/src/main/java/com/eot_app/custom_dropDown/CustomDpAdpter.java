package com.eot_app.custom_dropDown;

import android.annotation.SuppressLint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.LinearLayout;

import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.eot_app.R;
import com.eot_app.utility.settings.setting_db.JobTitle;

import java.util.List;

/**
 * Created by Sonam-11 on 13/8/20.
 */
class CustomDpAdpter extends RecyclerView.Adapter<CustomDpAdpter.MyViewHolder> {
    private final ItemListener itemListener;
    private List<JobTitle> jobServiceList;


    public CustomDpAdpter(List<JobTitle> jobServiceList, ItemListener itemListener) {
        this.jobServiceList = jobServiceList;
        this.itemListener = itemListener;
    }


    @Override
    public CustomDpAdpter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.custom_spinner_items, parent, false);

        return new CustomDpAdpter.MyViewHolder(itemView);
    }

    public void filterList(List<JobTitle> jobServiceList) {
        this.jobServiceList = jobServiceList;
        notifyDataSetChanged();
    }

    @Override
    public void onBindViewHolder(final CustomDpAdpter.MyViewHolder holder, @SuppressLint("RecyclerView") final int position) {
        final JobTitle jobTitle = jobServiceList.get(position);

        holder.checkbox.setChecked(false);

        holder.checkbox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                jobTitle.setSelect(!jobTitle.isSelect());

                notifyDataSetChanged();
                itemListener.onItemClick();
            }
        });

        if (jobTitle.isSelect()) {
            holder.checkbox.setChecked(true);
            holder.checkbox.setTextColor(ContextCompat.getColor(holder.checkbox.getContext(), R.color.colorPrimary));
        } else {
            holder.checkbox.setChecked(false);
            holder.checkbox.setTextColor(ContextCompat.getColor(holder.checkbox.getContext(), R.color.txt_color));
        }
        holder.checkbox.setText(jobTitle.getTitle());
    }

    @Override
    public int getItemCount() {
        return jobServiceList.size();
    }


    public class MyViewHolder extends RecyclerView.ViewHolder {
        public CheckBox checkbox;
        public LinearLayout dplayout;

        public MyViewHolder(View view) {
            super(view);
            dplayout = view.findViewById(R.id.dplayout);
            checkbox = view.findViewById(R.id.checkbox);
        }
    }

}