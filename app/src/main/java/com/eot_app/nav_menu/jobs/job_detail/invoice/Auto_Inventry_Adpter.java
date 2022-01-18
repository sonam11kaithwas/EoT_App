package com.eot_app.nav_menu.jobs.job_detail.invoice;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Filter;
import android.widget.TextView;

import com.eot_app.R;
import com.eot_app.nav_menu.jobs.job_detail.invoice.inventry_pkg.Inventry_ReS_Model;

import java.util.ArrayList;
import java.util.List;

public class Auto_Inventry_Adpter extends ArrayAdapter<Inventry_ReS_Model> {
    private final ArrayList<Inventry_ReS_Model> itemsAll;
    private final ArrayList<Inventry_ReS_Model> suggestions;
    int layoutResourceId;
    Context contextMylocationAdapter;
    ArrayList<Inventry_ReS_Model> arrayOfLocationPickUpDelivery;
    Filter nameFilter = new Filter() {
        @Override
        public String convertResultToString(Object resultValue) {
            String str = ((Inventry_ReS_Model) (resultValue)).getInm();
            return str;
        }

        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            if (constraint != null) {
                suggestions.clear();
                for (Inventry_ReS_Model customer : itemsAll) {
                    if (customer.getSearchKey() != null) {
                        if (customer.getInm().toLowerCase().contains(constraint.toString().toLowerCase())
                                || customer.getSearchKey().toLowerCase().contains(constraint.toString().toLowerCase())) {
                            try {
                                if (!suggestions.contains(customer))
                                    suggestions.add(customer);
                            } catch (Exception exception) {
                                exception.printStackTrace();
                            }
                        }
                    } else {
                        if (customer.getInm().toLowerCase().contains(constraint.toString().toLowerCase())) {
                            try {
                                if (!suggestions.contains(customer))
                                    suggestions.add(customer);
                            } catch (Exception exception) {
                                exception.printStackTrace();
                            }
                        }
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
                    List<Inventry_ReS_Model> filteredList = (ArrayList<Inventry_ReS_Model>) ((ArrayList<Inventry_ReS_Model>) results.values).clone();
                    clear();
                    for (Inventry_ReS_Model c : filteredList) {
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
    public Auto_Inventry_Adpter(Context contextMylocationAdapter, int layoutResourceId,
                                ArrayList<Inventry_ReS_Model> arrayOfLocationPickUpDelivery) {
        super(contextMylocationAdapter, layoutResourceId, arrayOfLocationPickUpDelivery);
        this.layoutResourceId = layoutResourceId;
        this.contextMylocationAdapter = contextMylocationAdapter;
        this.arrayOfLocationPickUpDelivery = arrayOfLocationPickUpDelivery;
        this.itemsAll = (ArrayList<Inventry_ReS_Model>) arrayOfLocationPickUpDelivery.clone();
        this.suggestions = new ArrayList<Inventry_ReS_Model>();
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        View v = convertView;
        if (v == null) {
            LayoutInflater vi = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            v = vi.inflate(layoutResourceId, null);
        }

        Inventry_ReS_Model pickOrDropModel = arrayOfLocationPickUpDelivery.get(position);
        if (pickOrDropModel != null) {
            TextView customerNameLabel = v.findViewById(R.id.item_title_name);
            TextView searchKey = v.findViewById(R.id.searchKey);
            if (customerNameLabel != null) {
                Log.i("", "getView Customer Name:" + pickOrDropModel.getInm());
                customerNameLabel.setText(pickOrDropModel.getInm());
                /***This For Search Key***/
                if (pickOrDropModel.getSearchKey() != null && !pickOrDropModel.getSearchKey().equals("") && searchKey != null) {
                    searchKey.setVisibility(View.VISIBLE);
                    searchKey.setText(pickOrDropModel.getSearchKey());
                } else if (searchKey != null) {
                    searchKey.setVisibility(View.GONE);
                    int paddingDp = 12;
                    float density = contextMylocationAdapter.getResources().getDisplayMetrics().density;
                    int paddingPixel = (int) (paddingDp * density);
                    customerNameLabel.setPadding(paddingPixel, paddingPixel, paddingPixel, paddingPixel);
                }
                if (searchKey != null) {
                    searchKey.setText(pickOrDropModel.getSearchKey());
                }
            }
        }
        return v;
    }

    @Override
    public Filter getFilter() {
        return nameFilter;
    }

    public Inventry_ReS_Model getSelectedItem(int position, String itemName) {
        Inventry_ReS_Model model = null;
        try {
            for (Inventry_ReS_Model model1 : itemsAll) {
                if (model1.getInm().equals(itemName)) {
                    model = model1;
                    break;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return model;
    }

}



