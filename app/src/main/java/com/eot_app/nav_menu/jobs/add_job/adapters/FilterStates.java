package com.eot_app.nav_menu.jobs.add_job.adapters;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Filter;
import android.widget.TextView;

import com.eot_app.R;
import com.eot_app.utility.States;

import java.util.ArrayList;

public class FilterStates extends ArrayAdapter<States> {

    private final ArrayList<States> itemsAll;
    private final ArrayList<States> suggestions;
    int layoutResourceId;
    Context contextMylocationAdapter;
    ArrayList<States> arrayOfLocationPickUpDelivery;
    Filter nameFilter = new Filter() {
        @Override
        public String convertResultToString(Object resultValue) {
            String str = ((States) (resultValue)).getName();
            return str;
        }


        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            if (constraint != null) {
                suggestions.clear();
                for (States customer : itemsAll) {
                    if (customer.getName().toLowerCase().startsWith(constraint.toString().toLowerCase())) {
                        suggestions.add(customer);
                    }
                }
                FilterResults filterResults = new FilterResults();
                filterResults.values = suggestions;
                filterResults.count = suggestions.size();
                return filterResults;
            } else {

                FilterResults filterResults = new FilterResults();
                filterResults.values = itemsAll;
                filterResults.count = itemsAll.size();
                return filterResults;
                //return new FilterResults();
            }
        }
    /*    @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            if (constraint != null) {
                suggestions.clear();
                for (States customer : itemsAll) {
                    if (customer.getName().toLowerCase().startsWith(constraint.toString().toLowerCase())) {
                        suggestions.add(customer);
                    }
                }
                FilterResults filterResults = new FilterResults();
                filterResults.values = suggestions;
                filterResults.count = suggestions.size();
                return filterResults;
            } else {
                return new FilterResults();
            }
        }*/

        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            @SuppressWarnings("unchecked")
            ArrayList<States> filteredList = (ArrayList<States>) results.values;
            if (results != null && results.count > 0) {
                clear();
                for (States c : filteredList) {
                    add(c);
                }
                notifyDataSetChanged();
            }
        }
    };

    @SuppressWarnings("unchecked")
    public FilterStates(Context contextMylocationAdapter, int layoutResourceId, ArrayList<States> arrayOfLocationPickUpDelivery) {
        super(contextMylocationAdapter, layoutResourceId, arrayOfLocationPickUpDelivery);
        this.layoutResourceId = layoutResourceId;
        this.contextMylocationAdapter = contextMylocationAdapter;
        this.arrayOfLocationPickUpDelivery = arrayOfLocationPickUpDelivery;
        this.itemsAll = (ArrayList<States>) arrayOfLocationPickUpDelivery.clone();
        this.suggestions = new ArrayList<States>();
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        View v = convertView;
        if (v == null) {
            LayoutInflater vi = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            v = vi.inflate(layoutResourceId, null);
        }
        States pickOrDropModel = arrayOfLocationPickUpDelivery.get(position);
        if (pickOrDropModel != null) {
            TextView customerNameLabel = v.findViewById(R.id.item_title_name);
            if (customerNameLabel != null) {
                Log.i("", "getView Customer Name:" + pickOrDropModel.getName());
                customerNameLabel.setText(pickOrDropModel.getName());
            }
        }
        return v;
    }

    @Override
    public Filter getFilter() {
        return nameFilter;
    }

}
