package com.eot_app.nav_menu.client.add_client.clientadpter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.eot_app.R;
import com.eot_app.utility.settings.clientindustry_db.ClientIndustryModel;

import java.util.List;

/**
 * Created by Sonam-11 on 10/7/20.
 */
public class ClientIndustryAdpter extends BaseAdapter {

    LayoutInflater inflator;
    List<ClientIndustryModel> mCounting;

    public ClientIndustryAdpter(Context context, List<ClientIndustryModel> counting) {
        inflator = LayoutInflater.from(context);
        mCounting = counting;
    }

    @Override
    public int getCount() {
        return mCounting.size();
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
        tv.setText(mCounting.get(position).getIndustryName());
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


