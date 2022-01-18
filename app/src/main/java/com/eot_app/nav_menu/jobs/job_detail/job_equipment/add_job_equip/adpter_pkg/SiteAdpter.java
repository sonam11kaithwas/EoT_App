package com.eot_app.nav_menu.jobs.job_detail.job_equipment.add_job_equip.adpter_pkg;

import android.content.Context;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Filter;
import android.widget.TextView;

import com.eot_app.R;
import com.eot_app.nav_menu.jobs.job_detail.job_equipment.add_job_equip.clientEqu.ClientEquRes;

import java.util.ArrayList;

public class SiteAdpter extends ArrayAdapter<ClientEquRes> {

    private final ArrayList<ClientEquRes> itemsAll;
    private final ArrayList<ClientEquRes> suggestions;
    int layoutResourceId;
    Context contextMylocationAdapter;
    ArrayList<ClientEquRes> arrayOfLocationPickUpDelivery;
    Filter nameFilter = new Filter() {
        @Override
        public String convertResultToString(Object resultValue) {
            String str = ((ClientEquRes) (resultValue)).getSnm();
            return str;
        }

        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            if (!TextUtils.isEmpty(constraint)) {
                suggestions.clear();
                for (ClientEquRes customer : itemsAll) {
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
            ArrayList<ClientEquRes> filteredList = (ArrayList<ClientEquRes>) results.values;
            if (results != null && results.count > 0) {
                clear();
                for (ClientEquRes c : filteredList) {
                    add(c);
                }
                notifyDataSetChanged();
            }
        }
    };

    @SuppressWarnings("unchecked")
    public SiteAdpter(Context contextMylocationAdapter, int layoutResourceId, ArrayList<ClientEquRes> arrayOfLocationPickUpDelivery) {
        super(contextMylocationAdapter, layoutResourceId, arrayOfLocationPickUpDelivery);
        this.layoutResourceId = layoutResourceId;
        this.contextMylocationAdapter = contextMylocationAdapter;
        this.arrayOfLocationPickUpDelivery = arrayOfLocationPickUpDelivery;
        this.itemsAll = (ArrayList<ClientEquRes>) arrayOfLocationPickUpDelivery.clone();
        this.suggestions = new ArrayList<ClientEquRes>();
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        View v = convertView;
        if (v == null) {
            LayoutInflater vi = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            v = vi.inflate(layoutResourceId, null);
        }

        ClientEquRes pickOrDropModel = arrayOfLocationPickUpDelivery.get(position);
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

