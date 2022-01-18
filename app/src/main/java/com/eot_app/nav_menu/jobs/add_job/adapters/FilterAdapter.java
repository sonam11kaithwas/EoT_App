package com.eot_app.nav_menu.jobs.add_job.adapters;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Filter;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.eot_app.R;
import com.eot_app.nav_menu.client.client_db.Client;

import java.util.ArrayList;
import java.util.List;

public class FilterAdapter extends ArrayAdapter<Client> {

    private final ArrayList<Client> itemsAll;
    private final ArrayList<Client> suggestions;
    int layoutResourceId;
    Context context;
    ArrayList<Client> array;
    Filter nameFilter = new Filter() {
        @Override
        public String convertResultToString(Object resultValue) {
            String str = ((Client) (resultValue)).getNm();
            return str;
        }

        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            if (constraint != null) {
                suggestions.clear();
                for (Client customer : itemsAll) {
                    if (customer.getNm().toLowerCase().startsWith(constraint.toString().toLowerCase())) {
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
            try {
                if (results != null && results.count > 0) {
                    List<Client> filteredList = (ArrayList<Client>) ((ArrayList<Client>) results.values).clone();
                    clear();
                    for (Client c : filteredList) {
                        add(c);
                    }
                    notifyDataSetChanged();
                }
            } catch (Exception ex) {
                ex.getMessage();
            }

        }
    };

    @SuppressWarnings("unchecked")
    public FilterAdapter(Context contextMylocationAdapter, int layoutResourceId, ArrayList<Client> arrayOfLocationPickUpDelivery) {
        super(contextMylocationAdapter, layoutResourceId, arrayOfLocationPickUpDelivery);
        this.layoutResourceId = layoutResourceId;
        this.context = contextMylocationAdapter;
        this.array = arrayOfLocationPickUpDelivery;
        this.itemsAll = (ArrayList<Client>) arrayOfLocationPickUpDelivery.clone();
        this.suggestions = new ArrayList<Client>();
    }

    @NonNull
    public View getView(int position, View convertView, @NonNull ViewGroup parent) {
        View v = convertView;
        if (v == null) {
            LayoutInflater vi = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            if (vi != null)
                v = vi.inflate(layoutResourceId, null);
        }
        Client pickOrDropModel = array.get(position);
        if (pickOrDropModel != null) {
            TextView customerNameLabel = v.findViewById(R.id.item_title_name);
            if (customerNameLabel != null) {
                Log.i("", "getView Customer Name:" + pickOrDropModel.getNm());
                customerNameLabel.setText(pickOrDropModel.getNm());
            }
        }
        return v;
    }

    @Override
    public Filter getFilter() {
        return nameFilter;
    }


    public Client getSelectedItem(String itemName) {
        Client model = null;
        try {
            for (Client model1 : itemsAll) {
                if (model1.getNm().equals(itemName)) {
                    model = model1;
                    break;
                }
            }
            // model = itemsAll.get(position);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return model;
    }
}
