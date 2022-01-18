package com.eot_app.nav_menu.jobs.job_detail.invoice.add_edit_invoice_pkg.dropdown_item_pkg;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Filter;
import android.widget.TextView;

import com.eot_app.R;
import com.eot_app.utility.settings.setting_db.JobTitle;

import java.util.ArrayList;

public class Services_item_Adapter extends ArrayAdapter<JobTitle> {
    private final int layoutResourceId;
    private final ArrayList<JobTitle> itemsAll;
    private final ArrayList<JobTitle> suggestions;
    Context contextMylocationAdapter;
    ArrayList<JobTitle> arrayOfLocationPickUpDelivery;
    Filter nameFilter = new Filter() {
        @Override
        public String convertResultToString(Object resultValue) {
            String str = ((JobTitle) (resultValue)).getTitle();
            return str;
        }


        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            if (constraint != null) {
                suggestions.clear();
                for (JobTitle customer : itemsAll) {
                    if (customer.getTitle().toLowerCase().contains(constraint.toString().toLowerCase())) {
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
        }

        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            @SuppressWarnings("unchecked")
            ArrayList<JobTitle> filteredList = (ArrayList<JobTitle>) results.values;
            if (results != null && results.count > 0) {
                clear();
                for (JobTitle c : filteredList) {
                    add(c);
                }
                notifyDataSetChanged();
            }
        }
    };

    public Services_item_Adapter(Context context, int textViewResourceId, ArrayList<JobTitle> joblist) {
        super(context, textViewResourceId, joblist);
        this.layoutResourceId = textViewResourceId;
        this.contextMylocationAdapter = contextMylocationAdapter;
        this.arrayOfLocationPickUpDelivery = joblist;
        this.itemsAll = (ArrayList<JobTitle>) arrayOfLocationPickUpDelivery.clone();
        this.suggestions = new ArrayList<JobTitle>();
    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View v = convertView;
        if (v == null) {
            LayoutInflater vi = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            v = vi.inflate(layoutResourceId, null);
        }

        JobTitle pickOrDropModel = arrayOfLocationPickUpDelivery.get(position);
        if (pickOrDropModel != null) {
            TextView customerNameLabel = v.findViewById(R.id.item_title_name);
            if (customerNameLabel != null) {
                Log.i("", "getView Customer Name:" + pickOrDropModel.getTitle());
                customerNameLabel.setText(pickOrDropModel.getTitle());
            }
        }
        return v;
    }


    @Override
    public Filter getFilter() {
        return nameFilter;
    }

    public JobTitle getSelectedItem(int position) {
        JobTitle model = null;
        try {
            model = itemsAll.get(position);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return model;
    }
}
