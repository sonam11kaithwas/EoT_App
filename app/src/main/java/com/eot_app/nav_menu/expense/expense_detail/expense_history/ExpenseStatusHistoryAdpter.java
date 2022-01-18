package com.eot_app.nav_menu.expense.expense_detail.expense_history;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.eot_app.R;
import com.eot_app.nav_menu.expense.ExpenseStatus_Controller;
import com.eot_app.nav_menu.jobs.job_detail.detail.jobdetial_model.JobStatusModel;
import com.eot_app.utility.AppUtility;
import com.eot_app.utility.EotApp;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Sonam-11 on 8/5/20.
 */
public class ExpenseStatusHistoryAdpter extends
        RecyclerView.Adapter<ExpenseStatusHistoryAdpter.ViewHolder> {

    private List<ExpenseStatushistoryModel> mValues;
    private ExpenseStatusHistoryAdpter.ExpencesInteraction mListener;

    public ExpenseStatusHistoryAdpter(List<ExpenseStatushistoryModel> items, ExpenseStatusHistoryAdpter.ExpencesInteraction listener) {
        mValues = items;
        mListener = listener;
    }

    public ExpenseStatusHistoryAdpter(ArrayList<ExpenseStatushistoryModel> expenseStatushistoryModels) {
        mValues = expenseStatushistoryModels;
    }

    @Override
    public ExpenseStatusHistoryAdpter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.expense_status_list, parent, false);
        return new ExpenseStatusHistoryAdpter.ViewHolder(view);
    }

    public void updateAdpter(List<ExpenseStatushistoryModel> mValues) {
        this.mValues = mValues;
        notifyDataSetChanged();
    }

    @Override
    public void onBindViewHolder(final ExpenseStatusHistoryAdpter.ViewHolder holder, final int position) {
        String date = null;
        if (mValues.get(position).getDateTime() != null && !mValues.get(position).getDateTime().equals("")) {
            date = AppUtility.getDateWithFormate((Long.parseLong(mValues.get(position).getDateTime()))
                    , "dd-MMM-yyyy");
        }
        holder.expense_status.setText(mValues.get(position).getStatus());
        try {
            if (date != null) {
//                String[] parts = date[0].split(",");
//                String part1 = parts[1]; // 004
                holder.expense_date.setText(date);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        holder.item_main_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (null != mListener) {
                    mListener.onListFragmentInteraction(mValues.get(position));
                }
            }
        });

        try {
            setViewByJobStatus(holder, Integer.parseInt(mValues.get(position).getStatus()));
        } catch (NumberFormatException e) {
            e.printStackTrace();
        }
    }

    private void setViewByJobStatus(ExpenseStatusHistoryAdpter.ViewHolder holder, int status) {
        if (status == 0) {
            status = 5;
        }
        JobStatusModel jobStatusObject = ExpenseStatus_Controller.getInstance().getStatusObjectById(String.valueOf(status));
        if (jobStatusObject != null) {
            holder.expense_status.setText(jobStatusObject.getStatus_name());
            int id = EotApp.getAppinstance().getResources().getIdentifier(jobStatusObject.getImg(), "drawable", EotApp.getAppinstance().getPackageName());
            holder.status_img.setImageResource(id);
        }
    }

    @Override
    public int getItemCount() {
        return mValues.size();
    }

    interface ExpencesInteraction {
        void onListFragmentInteraction(ExpenseStatushistoryModel ExpenseStatushistoryModel);
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public View mView;
        public TextView expense_date, expense_status;
        public RelativeLayout item_main_layout;
        public ImageView status_img;

        public ViewHolder(View view) {
            super(view);
            mView = view;
            expense_status = view.findViewById(R.id.expense_status);
            status_img = view.findViewById(R.id.status_img);
            expense_date = view.findViewById(R.id.expense_date);
            item_main_layout = view.findViewById(R.id.item_main_layout);
        }
    }
}
