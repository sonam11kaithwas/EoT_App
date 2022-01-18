package com.eot_app.nav_menu.quote.quotes_list_pkg;

import android.content.Context;
import android.content.res.Resources;
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

import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.eot_app.R;
import com.eot_app.nav_menu.jobs.job_db.JtId;
import com.eot_app.nav_menu.quote.quotes_list_pkg.qoute_model_pkg.Quote_ReS;
import com.eot_app.utility.AppConstant;
import com.eot_app.utility.AppUtility;
import com.eot_app.utility.EotApp;
import com.eot_app.utility.util_interfaces.MyListItemSelected;

import java.util.Iterator;
import java.util.List;

public class QuoteList_Adpter extends RecyclerView.Adapter<QuoteList_Adpter.MyViewHolder> {
    private static List<Quote_ReS> quoteListData;
    private final Context context;
    private final MyListItemSelected<Quote_ReS> mListner;
    private int selected_item;


    public QuoteList_Adpter(Context context, List<Quote_ReS> quoteListData, MyListItemSelected<Quote_ReS> mListner) {
        QuoteList_Adpter.quoteListData = quoteListData;
        this.context = context;
        this.mListner = mListner;
    }

    public void updateRecords(List<Quote_ReS> quoteListData) {
        QuoteList_Adpter.quoteListData = quoteListData;
        notifyDataSetChanged();
    }

    @Override
    public QuoteList_Adpter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.quote_list_adpter_layout, parent, false);
        return new QuoteList_Adpter.MyViewHolder(view, mListner);
    }

    @Override
    public void onBindViewHolder(QuoteList_Adpter.MyViewHolder holder, final int position) {

        String today_date = AppUtility.getDateByFormat(
                AppUtility.dateTimeByAmPmFormate("EEE, dd MMM yyyy ,hh:mm ,a", "EEE, dd MMM yyyy ,kk:mm"));
        String[] today_dt = today_date.split(",");
        String to_day = today_dt[1];

        String temp_date = "", cr_date;

        if (quoteListData.get(position).getQuotDate() != null) {
            if (!quoteListData.get(position).getQuotDate().equals("") && !quoteListData.get(position).getDuedate().equals("")) {
                String[] date = AppUtility.getFormatedTime(quoteListData.get(position).getQuotDate());
                cr_date = date[0];
                String[] date_separated = cr_date.split(",");
                Spannable txtFrst;

                if (to_day.contains(date_separated[1])) {
                    txtFrst = new SpannableString("Today" + ",");
                } else {
                    txtFrst = new SpannableString(date_separated[0] + ",");
                }

                txtFrst.setSpan(new ForegroundColorSpan(Color.parseColor("#00848d")), 0, txtFrst.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);


                if (position < quoteListData.size())
                    if (position == 0 || (quoteListData.get(position - 1).getQuotDate() != null && quoteListData.get(position - 1).getQuotDate().equals(""))) {
                        holder.txt_date.setText(txtFrst);
                        holder.txt_date.append(date_separated[1]);
                        holder.date_layout.setVisibility(View.VISIBLE);

                    } else if (quoteListData.get(position - 1).getQuotDate() != null && !quoteListData.get(position - 1).getQuotDate().equals("")) {
                        String[] date2 = AppUtility.getFormatedTime(quoteListData.get(position - 1).getQuotDate());
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
                //   holder.txt_date.setText("");
//                holder.job_time.setText("");
//                holder.job_am_pm.setText("");
            }
        }


        switchDefaultColor(holder, EotApp.getAppinstance().getResources().getColor(R.color.body_font_color));
        setViewByJobStatus(holder, Integer.parseInt(quoteListData.get(position).getStatus()));

        String label = quoteListData.get(position).getLabel();
        String Nm = quoteListData.get(position).getNm();
        if (label != null && Nm != null) {
            holder.label.setText(label + " - " + Nm);
        } else {
            holder.label.setText("Job" + " - " + "client");
        }

        try {
            String StrJt = "";
            List<JtId> jtaray = quoteListData.get(position).getJtId();
            if (jtaray != null) {
                Iterator<JtId> it = jtaray.iterator();
                if (it.hasNext()) {
                    StrJt += it.next().getTitle();
                }
                while (it.hasNext()) {
                    StrJt = StrJt + ", " + it.next().getTitle();
                }
                if (!StrJt.equals("")) {
                    holder.des.setVisibility(View.VISIBLE);
                    holder.des.setText(StrJt);
                    holder.des.setTextColor(EotApp.getAppinstance().getResources().getColor(R.color.body_font_color));
                } else {
                    holder.des.setVisibility(View.GONE);
                }

            }
        } catch (Resources.NotFoundException e) {
            e.printStackTrace();
        }

        String full_address = quoteListData.get(position).getAdr();
        holder.address.setText(full_address);

    }

    private Quote_ReS getItem(int position) {
        return quoteListData.get(position);
    }

    private void switchDefaultColor(QuoteList_Adpter.MyViewHolder holder, int color) {
        holder.status.setTextColor(color);
    }

    private void setViewByJobStatus(QuoteList_Adpter.MyViewHolder holder, int status) {

        switch (status) {
            case 1:
                holder.status.setText(AppConstant.status_new_key);
                holder.status_img.setImageResource(R.drawable.ic_new_task);
                break;
            case 2:
                holder.status.setText(AppConstant.approved);
                holder.status_img.setImageResource(R.drawable.ic_accepted_task);
                break;
            case 3:
                holder.status.setText(AppConstant.rejected);
                holder.status_img.setImageResource(R.drawable.ic_rejected);
                break;
            case 8:
                holder.status.setText(AppConstant.status_onhold);
                holder.status_img.setImageResource(R.drawable.ic_pendng_task);
                break;
        }
    }


    @Override
    public int getItemCount() {
        return quoteListData.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {
        TextView label, status, des, address, txt_date;//job_time, job_am_pm,
        LinearLayout item_main_layout, date_layout;
        ImageView status_img;
        ConstraintLayout status_constraints;
        MyListItemSelected<Quote_ReS> mListner;
        ImageView item_flag;

        public MyViewHolder(View itemView, MyListItemSelected<Quote_ReS> mListner) {
            super(itemView);
            this.mListner = mListner;
            item_main_layout = itemView.findViewById(R.id.item_main_layout);
            status_img = itemView.findViewById(R.id.status_img);
            status = itemView.findViewById(R.id.status);
            label = itemView.findViewById(R.id.label);
            des = itemView.findViewById(R.id.des);
            address = itemView.findViewById(R.id.address);
            status_constraints = itemView.findViewById(R.id.status_constraints);
            txt_date = itemView.findViewById(R.id.txt_date);
            date_layout = itemView.findViewById(R.id.date_layout);
            item_flag = itemView.findViewById(R.id.item_flag);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    try {
                        MyViewHolder.this.mListner.onMyListitemSeleted(quoteListData.get(getAdapterPosition()));
                    } catch (Exception ex) {
                        ex.printStackTrace();
                    }
                }
            });


        }
    }
}