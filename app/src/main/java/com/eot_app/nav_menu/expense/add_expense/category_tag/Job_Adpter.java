package com.eot_app.nav_menu.expense.add_expense.category_tag;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Filter;
import android.widget.TextView;

import com.eot_app.R;
import com.eot_app.nav_menu.jobs.job_db.Job;

import java.util.ArrayList;

/**
 * Created by Sonam-11 on 12/5/20.
 */
public class Job_Adpter extends ArrayAdapter<Job> {
    private final int layoutResourceId;
    private final ArrayList<Job> itemsAll;
    private final ArrayList<Job> suggestions;
    Context contextMylocationAdapter;
    ArrayList<Job> arrayOfLocationPickUpDelivery;
    Filter nameFilter = new Filter() {
        @Override
        public String convertResultToString(Object resultValue) {
            String str = ((Job) (resultValue)).getLabel();
            return str;
        }


        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            if (constraint != null) {
                suggestions.clear();
                for (Job customer : itemsAll) {
                    if (customer.getLabel().toLowerCase().startsWith(constraint.toString().toLowerCase())) {
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
            ArrayList<Job> filteredList = (ArrayList<Job>) results.values;
            if (results != null && results.count > 0) {
                clear();
                for (Job c : filteredList) {
                    add(c);
                }
                notifyDataSetChanged();
            }
        }
    };

    public Job_Adpter(Context context, int textViewResourceId, ArrayList<Job> joblist) {
        super(context, textViewResourceId, joblist);
        this.layoutResourceId = textViewResourceId;
        this.contextMylocationAdapter = contextMylocationAdapter;
        this.arrayOfLocationPickUpDelivery = joblist;
        this.itemsAll = (ArrayList<Job>) arrayOfLocationPickUpDelivery.clone();
        this.suggestions = new ArrayList<Job>();
    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View v = convertView;
        if (v == null) {
            LayoutInflater vi = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            v = vi.inflate(layoutResourceId, null);
        }

        Job pickOrDropModel = arrayOfLocationPickUpDelivery.get(position);
        if (pickOrDropModel != null) {
            TextView customerNameLabel = v.findViewById(R.id.item_title_name);
            if (customerNameLabel != null) {
                Log.i("", "getView Customer Name:" + pickOrDropModel.getLabel());
                customerNameLabel.setText(pickOrDropModel.getLabel());
            }
        }
        return v;
    }


    @Override
    public Filter getFilter() {
        return nameFilter;
    }

    public Job getSelectedItem(String itemName) {
        Job model = null;
        try {
            for (Job model1 : itemsAll) {
                if (model1.getLabel().equals(itemName)) {
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

