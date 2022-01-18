package com.eot_app.nav_menu.jobs.job_detail.detail;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.eot_app.R;

import java.util.ArrayList;
import java.util.List;

public class JobServiceAdpter extends RecyclerView.Adapter<JobServiceAdpter.ViewHolder> {

    private List<String> mData = new ArrayList<>();


    // data is passed into the constructor
    JobServiceAdpter(List<String> data) {
        this.mData = data;
    }

    // inflates the row layout from xml when needed
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.job_lable_dynamic_layout, parent, false);
        return new ViewHolder(view);
    }

    // binds the data to the TextView in each row
    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        String animal = mData.get(position);
        holder.myTextView.setText(animal);
    }


    @Override
    public int getItemCount() {
        return mData.size();
    }


    public void updateList(List<String> mData) {
        if (this.mData.size() > 0)
            this.mData.clear();
        this.mData.addAll(mData);
        notifyDataSetChanged();
    }

    // stores and recycles views as they are scrolled off screen
    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView myTextView;

        ViewHolder(View itemView) {
            super(itemView);
            myTextView = itemView.findViewById(R.id.job_lables);

        }
    }
}