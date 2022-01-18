package com.eot_app.nav_menu.jobs.add_job.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Filter;
import android.widget.TextView;

import com.eot_app.R;
import com.eot_app.utility.DropdownListBean;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by aplite_pc302 on 7/4/18.
 */

public class DynamicClassAdapter<T> extends ArrayAdapter<T> {

    Context context;
    int resource, textViewResourceId;
    List<T> items, tempItems, suggestions;
    /**
     * Custom Filter implementation for custom suggestions we provide.
     */
    Filter nameFilter = new Filter() {
        @Override
        public CharSequence convertResultToString(Object resultValue) {
            String str = ((DropdownListBean) resultValue).getName();
            return str;
        }

        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            if (constraint != null) {
                suggestions.clear();
                for (T model : tempItems) {
                    if (((DropdownListBean) model).getName().toLowerCase().contains(constraint.toString().toLowerCase())) {
                        suggestions.add(model);
                    }
                }
                FilterResults filterResults = new FilterResults();
                filterResults.values = suggestions;
                filterResults.count = suggestions.size();
                return filterResults;
            } else {
                return new FilterResults();
            }
        }

        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            List<T> filterList = (ArrayList<T>) results.values;
            if (results != null && results.count > 0) {
                clear();
                for (T people : filterList) {
                    add(people);
                    notifyDataSetChanged();
                }
            }
        }
    };

    public DynamicClassAdapter(Context context, int resource, int textViewResourceId, List<T> items) {
        super(context, resource, textViewResourceId, items);
        this.context = context;
        this.resource = resource;
        this.textViewResourceId = textViewResourceId;
        this.items = items;
        tempItems = new ArrayList<T>(items); // this makes the difference.
        suggestions = new ArrayList<T>();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = convertView;
        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = inflater.inflate(R.layout.custom_adapter_item_layout, parent, false);
        }
        T model = items.get(position);
        if (model != null) {
            TextView lblName = view.findViewById(R.id.item_title_name);
            if (lblName != null)
                lblName.setText(((DropdownListBean) model).getName());
        }
        return view;
    }

    @Override
    public Filter getFilter() {
        return nameFilter;
    }
}
