package com.eot_app.utility.util_interfaces;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.eot_app.R;

public class JoBServSuggAdpter extends BaseAdapter {

    private final LayoutInflater inflator;
    private final SelectedService selectedService;
    private String[] mCounting;


    public JoBServSuggAdpter(Context context, String[] counting, SelectedService selectedService) {
        inflator = LayoutInflater.from(context);
        mCounting = counting;
        this.selectedService = selectedService;
    }


    public void updtaeList(String[] counting) {
        this.mCounting = counting;
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return mCounting.length;
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
    public View getView(final int position, View convertView, ViewGroup parent) {
        convertView = inflator.inflate(R.layout.job_ser_sugge_lay, null);
        TextView tv = convertView.findViewById(R.id.spinner_textview);
        tv.setText(mCounting[position]);
        tv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (selectedService != null) {
                    selectedService.getSerNm(mCounting[position]);
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

    public interface SelectedService {
        void getSerNm(String nm);
    }
}

