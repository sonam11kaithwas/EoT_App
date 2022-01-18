package com.eot_app.nav_menu.client.add_client.clientadpter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.eot_app.R;
import com.eot_app.utility.settings.client_refrence_db.ClientRefrenceModel;

import java.util.List;

/**
 * Created by Sona-11 on 14/9/21.
 */
public class ClientRefrenceAdpter extends BaseAdapter {

    LayoutInflater inflator;
    List<ClientRefrenceModel> mCounting;

    public ClientRefrenceAdpter(Context context, List<ClientRefrenceModel> counting) {
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
        tv.setText(mCounting.get(position).getRefName());
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


