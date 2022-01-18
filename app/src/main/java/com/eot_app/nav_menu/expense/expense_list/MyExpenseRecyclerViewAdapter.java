package com.eot_app.nav_menu.expense.expense_list;

import android.app.Activity;
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

import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.eot_app.R;
import com.eot_app.nav_menu.expense.ExpenseStatus_Controller;
import com.eot_app.nav_menu.expense.expense_list.model.ExpenseResModel;
import com.eot_app.nav_menu.jobs.job_detail.detail.jobdetial_model.JobStatusModel;
import com.eot_app.utility.AppConstant;
import com.eot_app.utility.AppUtility;
import com.eot_app.utility.EotApp;
import com.eot_app.utility.language_support.LanguageController;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

/**
 * {@link RecyclerView.Adapter} that can display a {@link ExpenseResModel} and makes a call to the
 * specified {@link ExpencesInteraction}.
 * TODO: Replace the implementation with code for your data type.
 */
public class MyExpenseRecyclerViewAdapter extends RecyclerView.Adapter<MyExpenseRecyclerViewAdapter.ViewHolder> {

    private final List<ExpenseResModel> filterList;
    private final ExpencesInteraction mListener;
    Context mContex;
    private List<ExpenseResModel> mValues;


    public MyExpenseRecyclerViewAdapter(Context mContex, List<ExpenseResModel> items, ExpencesInteraction listener) {
        this.mContex = mContex;
        mValues = items;
        this.filterList = new ArrayList<ExpenseResModel>();
        // we copy the original list to the filter list and use it for setting row values
        this.filterList.addAll(this.mValues);
        mListener = listener;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.fragment_expense_list_item, parent, false);
        return new ViewHolder(view);
    }

    public void updateAdpter(List<ExpenseResModel> mValues) {
        this.mValues = mValues;
        if (this.filterList != null) {
            filterList.clear();
            this.filterList.addAll(this.mValues);
        }
        notifyDataSetChanged();
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        if (position == getItemCount() - 1) holder.blank_view.setVisibility(View.VISIBLE);
        else holder.blank_view.setVisibility(View.GONE);


        holder.expenceNM.setText(filterList.get(position).getName());
        if (!TextUtils.isEmpty(filterList.get(position).getCategory())) {
            holder.expenceCatgry.setVisibility(View.VISIBLE);
            holder.expenceCatgry.setText(filterList.get(position).getCategory());
        } else holder.expenceCatgry.setVisibility(View.INVISIBLE);
        holder.desc.setText(filterList.get(position).getDes());

        holder.tv_amount.setText(AppUtility.getRoundoff_amount(String.valueOf(filterList.get(position).getAmt())));

        if (!filterList.get(position).getTag().equals("")) {
            Spannable txt_Frst;
            txt_Frst = new SpannableString(filterList.get(position).getTag());
            txt_Frst.setSpan(new ForegroundColorSpan(Color.parseColor("#00848d")),
                    0, txt_Frst.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
            // holder.tv_tag.setText("Group: ");
            holder.tv_tag.setText(LanguageController.getInstance().getMobileMsgByKey(AppConstant.expense_group) + ": ");
            holder.tv_tag.append(txt_Frst);
        } else {
            holder.tv_tag.setText(filterList.get(position).getTag());
        }

        String temp_date = "", cr_date;
        try {

            if (filterList.get(position).getDateTime() != null && !filterList.get(position).getDateTime().equals("")) {
                String[] date = AppUtility.getFormatedTime(filterList.get(position).getDateTime());
                cr_date = date[0];
                String[] date_separated = cr_date.split(",");
                Spannable txtFrst;

                String today_date = AppUtility.getDateByFormat("EEE,dd MMM yyyy ,hh:mm ,a");
                String[] today_dt = today_date.split(",");
                String to_day = today_dt[1];

                if (to_day.contains(date_separated[1])) {
                    txtFrst = new SpannableString("Today" + ",");
                } else {
                    txtFrst = new SpannableString(date_separated[0] + ",");
                }
                txtFrst.setSpan(new ForegroundColorSpan(Color.parseColor("#00848d")), 0, txtFrst.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);


                String dateExpenses = date_separated[1];
                String[] dateExpensesArray = dateExpenses.split(" ");
                DecimalFormat formatter = new DecimalFormat("00");
                String[] aa = dateExpensesArray[1].split(" ");
                //time_start.setText((formatter.format(Integer.parseInt(aa[0]))) + ":" + aa[1]);
                holder.expense_date.setText((formatter.format(Integer.parseInt(aa[0]))) + " " + dateExpensesArray[2]);
                holder.expense_am_pm.setText(dateExpensesArray[3]);

                if (position < filterList.size())
                    if (position == 0 || (filterList.get(position - 1).getDateTime() != null && filterList.get(position - 1).getDateTime().equals(""))) {
                        holder.txt_date.setText(txtFrst);
                        holder.txt_date.append(date_separated[1]);
                        holder.date_layout.setVisibility(View.VISIBLE);

                    } else if (filterList.get(position - 1).getDateTime() != null && !filterList.get(position - 1).getDateTime().equals("")) {
                        String[] date2 = AppUtility.getFormatedTime(filterList.get(position - 1).getDateTime());
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
                holder.txt_date.setText("");
                holder.expense_date.setText("");
                holder.expense_am_pm.setText("");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        holder.cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (null != mListener) {
                    mListener.onListFragmentInteraction(filterList.get(position));
                }
            }
        });

        try {
            setViewByJobStatus(holder, Integer.parseInt(filterList.get(position).getStatus()));
        } catch (NumberFormatException e) {
            e.printStackTrace();
        }
    }

    private void setViewByJobStatus(ViewHolder holder, int status) {
        if (status == 0) {
            status = 5;
        }
        JobStatusModel jobStatusObject = ExpenseStatus_Controller.getInstance().getStatusObjectById(String.valueOf(status));
        if (jobStatusObject != null) {
            if (jobStatusObject.getStatus_no().equals("1")) {
                holder.status.setText("Claim");
            } else {
                holder.status.setText(jobStatusObject.getStatus_name());
            }

            int id = EotApp.getAppinstance().getResources().getIdentifier(jobStatusObject.getImg(), "drawable", EotApp.getAppinstance().getPackageName());
            holder.status_img.setImageResource(id);

        }
    }


    @Override
    public int getItemCount() {
        mListener.isListEmpty(mListener != null && filterList == null || filterList.size() == 0);

        return (null != filterList ? filterList.size() : 0);
    }

    // Do Search...
    public void filterByName(final String text) {
        // Searching could be complex..so we will dispatch it to a different thread...
        new Thread(new Runnable() {
            @Override
            public void run() {

                // Clear the filter list
                filterList.clear();

                // If there is no search value, then add all original list items to filter list
                if (TextUtils.isEmpty(text)) {

                    filterList.addAll(mValues);

                } else {
                    // Iterate in the original List and add it to filter list...
                    for (ExpenseResModel item : mValues) {
                        if (item.getName().toLowerCase().contains(text.toLowerCase())) {
                            // Adding Matched items
                            filterList.add(item);
                        }
                    }
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

    // Do Search...
    public void filterByStatus(final String text) {
        // Searching could be complex..so we will dispatch it to a different thread...
        new Thread(new Runnable() {
            @Override
            public void run() {

                // Clear the filter list
                filterList.clear();

                // If there is no search value, then add all original list items to filter list
                if (TextUtils.isEmpty(text)) {

                    filterList.addAll(mValues);

                } else {
                    String trimText = text.trim();
                    String[] s = trimText.split(" ");
                    // Iterate in the original List and add it to filter list...
                    for (ExpenseResModel item : mValues) {
                        for (String status : s)
                            if (item.getStatus().toLowerCase().contains(status.toLowerCase())) {
                                // Adding Matched items
                                filterList.add(item);
                            }
                    }
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


    interface ExpencesInteraction {
        void onListFragmentInteraction(ExpenseResModel expenseResModel);

        void isListEmpty(boolean b);
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public View mView;
        public TextView expenceNM;
        public TextView expenceCatgry, desc, status, expense_date, expense_am_pm, txt_date, tv_amount, tv_tag;
        public CardView cardView;
        public ImageView status_img;
        public View blank_view;
        LinearLayout date_layout;

        public ViewHolder(View view) {
            super(view);
            mView = view;
            blank_view = view.findViewById(R.id.blank_view);
            expenceNM = view.findViewById(R.id.expenceNM);
            expenceCatgry = view.findViewById(R.id.expenceCatgry);
            tv_amount = view.findViewById(R.id.tv_amount);
            tv_tag = view.findViewById(R.id.tv_tag);
            desc = view.findViewById(R.id.desc);
            status = view.findViewById(R.id.status);
            status_img = view.findViewById(R.id.status_img);
            expense_date = view.findViewById(R.id.expense_date);
            expense_am_pm = view.findViewById(R.id.expense_am_pm);
            txt_date = view.findViewById(R.id.txt_date);
            cardView = view.findViewById(R.id.cardView);
            date_layout = view.findViewById(R.id.date_layout);
        }
    }


}

