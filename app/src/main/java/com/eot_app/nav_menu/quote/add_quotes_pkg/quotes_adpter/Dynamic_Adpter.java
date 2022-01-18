package com.eot_app.nav_menu.quote.add_quotes_pkg.quotes_adpter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.eot_app.R;
import com.eot_app.utility.DropdownListBean;

import java.util.List;

public class Dynamic_Adpter<T> extends BaseAdapter {
    private final List<T> fwList;
    private final Context context;
    private LayoutInflater inflator;

    public Dynamic_Adpter(Context context, List<T> fwList) {
        this.context = context;
        this.fwList = fwList;
    }

    @Override
    public int getCount() {
        return fwList.size();
    }

    @Override
    public Object getItem(int i) {
        return null;
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup viewGroup) {
        return getCustomView(position, convertView, viewGroup);
    }

    private View getCustomView(final int position, View convertView, ViewGroup parent) {
        View row = LayoutInflater.from(parent.getContext()).inflate(R.layout.spinner_item_layout, parent, false);
        final TextView label = row.findViewById(R.id.spinner_textview);
        T model = fwList.get(position);
        label.setText(((DropdownListBean) model).getName());

        return row;
    }


    @Override
    public View getDropDownView(int position, View convertView, ViewGroup parent) {
        return getCustomView(position, convertView, parent);
    }
}
