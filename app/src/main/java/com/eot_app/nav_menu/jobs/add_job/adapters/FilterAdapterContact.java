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
import com.eot_app.nav_menu.client.clientlist.client_detail.contact.client_dao.ContactData;

import java.util.ArrayList;

public class FilterAdapterContact extends ArrayAdapter<ContactData> {

    private final ArrayList<ContactData> itemsAll;
    private final ArrayList<ContactData> suggestions;
    int layoutResourceId;
    Context context;
    ArrayList<ContactData> array;
    Filter nameFilter = new Filter() {
        @Override
        public String convertResultToString(Object resultValue) {
            String str = ((ContactData) (resultValue)).getCnm();
            return str;
        }

        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            if (!TextUtils.isEmpty(constraint)) {
                suggestions.clear();
                for (ContactData customer : itemsAll) {
                    if (customer.getCnm().toLowerCase().contains(constraint.toString().toLowerCase())) {
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
            ArrayList<ContactData> filteredList = (ArrayList<ContactData>) results.values;
            if (results != null && results.count > 0) {
                clear();
                for (ContactData c : filteredList) {
                    add(c);
                }
                notifyDataSetChanged();
            }
        }
    };

    @SuppressWarnings("unchecked")
    public FilterAdapterContact(Context contextMylocationAdapter, int layoutResourceId, ArrayList<ContactData> arrayOfLocationPickUpDelivery) {
        super(contextMylocationAdapter, layoutResourceId, arrayOfLocationPickUpDelivery);
        this.layoutResourceId = layoutResourceId;
        this.context = contextMylocationAdapter;
        this.array = arrayOfLocationPickUpDelivery;
        this.itemsAll = (ArrayList<ContactData>) arrayOfLocationPickUpDelivery.clone();
        this.suggestions = new ArrayList<ContactData>();
    }

    @NonNull
    public View getView(int position, View convertView, @NonNull ViewGroup parent) {
        View v = convertView;
        if (v == null) {
            LayoutInflater vi = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            if (vi != null)
                v = vi.inflate(layoutResourceId, null);
        }
        ContactData pickOrDropModel = array.get(position);
        if (pickOrDropModel != null) {
            TextView customerNameLabel = v.findViewById(R.id.item_title_name);
            if (customerNameLabel != null) {
                Log.i("", "getView Customer Name:" + pickOrDropModel.getCnm());
                customerNameLabel.setText(pickOrDropModel.getCnm());
            }
        }
        return v;
    }

    @Override
    public Filter getFilter() {
        return nameFilter;
    }

    public void clearListData() {
        array.clear();
        itemsAll.clear();
        notifyDataSetChanged();
    }
}
