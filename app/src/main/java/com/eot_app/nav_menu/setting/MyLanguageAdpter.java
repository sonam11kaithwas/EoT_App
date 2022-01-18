package com.eot_app.nav_menu.setting;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.eot_app.R;
import com.eot_app.home_screens.MainActivityView;
import com.eot_app.utility.language_support.Language_Model;

import java.util.List;

/**
 * Created by ubuntu on 8/8/18.
 */

public class MyLanguageAdpter extends BaseAdapter {

    private final List<Language_Model> lan_list;
    private final MainActivityView mListener;
    private final String status = "";
    private LayoutInflater inflator;

    public MyLanguageAdpter(MainActivityView mListener, List<Language_Model> lan_list) {
        this.mListener = mListener;
        this.lan_list = lan_list;
    }


    @Override
    public int getCount() {
        return lan_list.size();
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
        tv.setText((CharSequence) lan_list.get(position));
        // tv.setBackgroundColor(Color.parseColor(colorsback[position]));

           /* convertView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (!Language_Preference.getSharedprefInstance().getlanguageFilename().equals(lan_list.get(getAdapterPosition()).getFileName()))
                        mListener.seletedLanguage(lan_list.get(getAdapterPosition()));
                }
            });
*/
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

