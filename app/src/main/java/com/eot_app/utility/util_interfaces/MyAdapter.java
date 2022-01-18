package com.eot_app.utility.util_interfaces;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.eot_app.R;
import com.eot_app.utility.DropdownListBean;

import java.util.List;

/**
 * Created by ubuntu on 15/8/18.
 */

public class MyAdapter<T> extends ArrayAdapter {
    private final Context mContext;
    private final List<T> listitem;
    private final int layoutRes;

    public MyAdapter(Context context, int resource, List<T> list) {
        super(context, resource, list);
        this.mContext = context;
        this.listitem = list;
        this.layoutRes = resource;
    }

    @Override
    public View getDropDownView(int position, View convertView,
                                ViewGroup parent) {
        return getCustomView(position, convertView, parent);
    }

    @NonNull
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        return getCustomView(position, convertView, parent);
    }

    public View getCustomView(final int position, View convertView, ViewGroup parent) {
        final ViewHolder holder;

        final DropdownListBean object = (DropdownListBean) listitem.get(position);
        if (convertView == null) {
            LayoutInflater layoutInflator = LayoutInflater.from(mContext);
            convertView = layoutInflator.inflate(layoutRes, parent, false);
            holder = new ViewHolder();
            holder.item_title_name = convertView.findViewById(R.id.item_title_name);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        holder.item_title_name.setText(object.getName());

        return convertView;
    }


    private class ViewHolder {
        private TextView item_title_name;
    }
}

