package com.eot_app.nav_menu.jobs.add_job.adapters;

import android.content.Context;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Filter;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.eot_app.R;
import com.eot_app.nav_menu.client.clientlist.client_detail.site.sitelist.Site_model;

import java.util.ArrayList;

public class FilterAdapterSites extends ArrayAdapter<Site_model> {

    private final ArrayList<Site_model> itemsAll;
    private final ArrayList<Site_model> suggestions;
    int layoutResourceId;
    Context context;
    ArrayList<Site_model> array;
    Filter nameFilter = new Filter() {
        @Override
        public String convertResultToString(Object resultValue) {
            String str = ((Site_model) (resultValue)).getSnm();
            return str;
        }

        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            if (!TextUtils.isEmpty(constraint)) {
                suggestions.clear();
                for (Site_model customer : itemsAll) {
                    if (customer.getSnm().toLowerCase().contains(constraint.toString().toLowerCase())) {
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
            }
        }

        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            @SuppressWarnings("unchecked")
            ArrayList<Site_model> filteredList = (ArrayList<Site_model>) results.values;
            if (results != null && results.count > 0) {
                clear();
                for (Site_model c : filteredList) {
                    add(c);
                }
                notifyDataSetChanged();
            }
        }
    };

    @SuppressWarnings("unchecked")
    public FilterAdapterSites(Context contextMylocationAdapter, int layoutResourceId, ArrayList<Site_model> arrayOfLocationPickUpDelivery) {
        super(contextMylocationAdapter, layoutResourceId, arrayOfLocationPickUpDelivery);
        this.layoutResourceId = layoutResourceId;
        this.context = contextMylocationAdapter;
        this.array = arrayOfLocationPickUpDelivery;
        this.itemsAll = (ArrayList<Site_model>) arrayOfLocationPickUpDelivery.clone();
        this.suggestions = new ArrayList<Site_model>();
    }

    @NonNull
    public View getView(int position, View convertView, @NonNull ViewGroup parent) {
        View v = convertView;
        if (v == null) {
            LayoutInflater vi = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            if (vi != null)
                v = vi.inflate(layoutResourceId, null);
        }
        Site_model pickOrDropModel = array.get(position);
        if (pickOrDropModel != null) {
            TextView customerNameLabel = v.findViewById(R.id.item_title_name);
            if (customerNameLabel != null) {
                Log.i("", "getView Customer Name:" + pickOrDropModel.getSnm());
                customerNameLabel.setText(pickOrDropModel.getSnm());
            }
        }
        return v;
    }

    @Override
    public Filter getFilter() {
        return nameFilter;
    }

}
