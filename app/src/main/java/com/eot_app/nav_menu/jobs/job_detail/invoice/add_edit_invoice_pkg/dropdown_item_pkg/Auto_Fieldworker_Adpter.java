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
import com.eot_app.utility.settings.setting_db.FieldWorker;

import java.util.ArrayList;

public class Auto_Fieldworker_Adpter extends ArrayAdapter<FieldWorker> {
    private final ArrayList<FieldWorker> itemsAll;
    private final ArrayList<FieldWorker> suggestions;
    int layoutResourceId;
    Context contextMylocationAdapter;
    ArrayList<FieldWorker> arrayOfLocationPickUpDelivery;
    Filter nameFilter = new Filter() {
        @Override
        public String convertResultToString(Object resultValue) {
            String str = ((FieldWorker) (resultValue)).getName();
            return str;
        }

        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            if (constraint != null) {
                suggestions.clear();
                for (FieldWorker customer : itemsAll) {
                    if (customer.getName().toLowerCase().contains(constraint.toString().toLowerCase())) {
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
            ArrayList<FieldWorker> filteredList = (ArrayList<FieldWorker>) results.values;
            if (results != null && results.count > 0) {
                clear();
                for (FieldWorker c : filteredList) {
                    add(c);
                }
                notifyDataSetChanged();
            }
        }
    };

    @SuppressWarnings("unchecked")
    public Auto_Fieldworker_Adpter(Context contextMylocationAdapter, int layoutResourceId,
                                   ArrayList<FieldWorker> arrayOfLocationPickUpDelivery) {
        super(contextMylocationAdapter, layoutResourceId, arrayOfLocationPickUpDelivery);
        this.layoutResourceId = layoutResourceId;
        this.contextMylocationAdapter = contextMylocationAdapter;
        this.arrayOfLocationPickUpDelivery = arrayOfLocationPickUpDelivery;
        this.itemsAll = (ArrayList<FieldWorker>) arrayOfLocationPickUpDelivery.clone();
        this.suggestions = new ArrayList<FieldWorker>();
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        View v = convertView;
        if (v == null) {
            LayoutInflater vi = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            v = vi.inflate(layoutResourceId, null);
        }

        FieldWorker pickOrDropModel = arrayOfLocationPickUpDelivery.get(position);
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
