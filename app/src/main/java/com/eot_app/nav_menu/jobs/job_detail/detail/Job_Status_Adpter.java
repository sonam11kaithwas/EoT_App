package com.eot_app.nav_menu.jobs.job_detail.detail;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.eot_app.R;
import com.eot_app.utility.AppConstant;
import com.eot_app.utility.language_support.LanguageController;

public class Job_Status_Adpter extends BaseAdapter {

    private final LayoutInflater inflator;
    private final MyJobStatusDp myJobStatusDp;
    //    private ArrayList<String> mCounting = new ArrayList<>();
    String[] mCounting;


    // public Job_Status_Adpter(Context context, ArrayList<String> counting) {
    public Job_Status_Adpter(Context context, String[] mCounting, MyJobStatusDp myJobStatusDp) {
        inflator = LayoutInflater.from(context);
        this.mCounting = mCounting;
        this.myJobStatusDp = myJobStatusDp;
    }

    public void updtaeList(String[] counting) {
        //   if (this.mCounting.length() > 0)
        //   this.mCounting.clear();
        this.mCounting = counting;
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return mCounting.length;
    }

    @Override
    public Object getItem(int position) {
        return position;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        convertView = inflator.inflate(R.layout.spinner_item_layout, null);

        TextView tv = convertView.findViewById(R.id.spinner_textview);
        if (mCounting[position].equals(LanguageController.getInstance().getMobileMsgByKey(AppConstant.reschedule))) {
            tv.setTextColor(inflator.getContext().getResources().getColor(R.color.in_progress));
        } else if (mCounting[position].equals(LanguageController.getInstance().getMobileMsgByKey(AppConstant.require_revisit))) {
            tv.setTextColor(inflator.getContext().getResources().getColor(R.color.blue_color));
        } else
            tv.setTextColor(inflator.getContext().getResources().getColor(R.color.txt_color));
        tv.setText(mCounting[position]);

        tv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (myJobStatusDp != null) {
                    myJobStatusDp.selectedStatus(position);
                }
            }
        });


        return convertView;
    }

    @Override
    public View getDropDownView(int position, View convertView, ViewGroup parent) {
        convertView = super.getDropDownView(position, convertView,
                parent);

        convertView.setVisibility(View.VISIBLE);
        ViewGroup.LayoutParams p = convertView.getLayoutParams();
        if (p != null) {
            p.width = ViewGroup.LayoutParams.MATCH_PARENT;
            p.height = ViewGroup.LayoutParams.WRAP_CONTENT;
        } else
            p = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        convertView.setLayoutParams(p);

        return convertView;

    }


    public interface MyJobStatusDp {
        void selectedStatus(int pos);
    }
}

