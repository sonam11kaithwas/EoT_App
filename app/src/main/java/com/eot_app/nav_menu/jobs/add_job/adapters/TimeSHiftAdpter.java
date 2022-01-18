package com.eot_app.nav_menu.jobs.add_job.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.eot_app.R;
import com.eot_app.time_shift_pkg.ShiftTimeReSModel;

import java.util.List;

/**
 * Created by Sona-11 on 15/11/21.
 */
public class TimeSHiftAdpter extends BaseAdapter {

    LayoutInflater inflator;
    List<ShiftTimeReSModel> mCounting;

    public TimeSHiftAdpter(Context context, List<ShiftTimeReSModel> counting) {
        inflator = LayoutInflater.from(context);
        mCounting = counting;
    }

    @Override
    public int getCount() {
        return mCounting.size();
    }

    public void updtaeList(List<ShiftTimeReSModel> counting) {
        this.mCounting = counting;
        notifyDataSetChanged();
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        convertView = inflator.inflate(R.layout.spinner_item_layout, null);
        TextView tv = convertView.findViewById(R.id.spinner_textview);
        tv.setText(mCounting.get(position).getShiftNm());
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
}


