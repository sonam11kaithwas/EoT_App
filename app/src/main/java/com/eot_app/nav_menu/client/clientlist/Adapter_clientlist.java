package com.eot_app.nav_menu.client.clientlist;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.eot_app.R;
import com.eot_app.nav_menu.client.client_db.Client;
import com.eot_app.nav_menu.client.clientlist.client_detail.site.sitelist.Site_model;
import com.eot_app.utility.AppConstant;
import com.eot_app.utility.EotApp;
import com.eot_app.utility.db.AppDataBase;
import com.eot_app.utility.language_support.LanguageController;
import com.eot_app.utility.util_interfaces.MyListItemSelected;

import java.util.ArrayList;
import java.util.List;


/**
 * Created by geet-pc on 10/5/18.
 */

public class Adapter_clientlist extends RecyclerView.Adapter<Adapter_clientlist.ClientViewHolder> implements Filterable {

    private final MyListItemSelected mlistner;
    ValueFilter valueFilter;
    private List<Client> client_data;

    public Adapter_clientlist(MyListItemSelected clientList, List<Client> client_data) {
        this.client_data = client_data;
        this.mlistner = clientList;
    }

    @NonNull
    @Override
    public ClientViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.client_item_view, parent, false);
        return new ClientViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final ClientViewHolder holder, final int position) {
        holder.client_name.setText(client_data.get(position).getNm());
        StringBuffer full_address = new StringBuffer();
//        Log.e("Site::", new Gson().toJson(AppDataBase.getInMemoryDatabase(EotApp.getAppinstance()).sitemodel().getSitelist()));
        Site_model site = AppDataBase.getInMemoryDatabase(EotApp.getAppinstance()).sitemodel().getDefaultFromCltId(client_data.get(position).getCltId());
        if (site != null) {
            if (site.getAdr() != null) {
                full_address.append(site.getAdr());
            }
            if (site.getCity() != null && !site.getCity().equals("")) {
                full_address.append(", " + site.getCity());
            }
        }
        if (client_data.get(position).getCltId().equals(client_data.get(position).getTempId())) {
            holder.cnt_address.setText(LanguageController.getInstance().getMobileMsgByKey(AppConstant.client_syn));
            holder.cnt_address.setTextColor(EotApp.getAppinstance().getResources().getColor(android.R.color.holo_red_light));
        } else {
            holder.cnt_address.setText(full_address);
            holder.cnt_address.setTextColor(EotApp.getAppinstance().getResources().getColor(R.color.body_font_color));
        }
//"\n" + client_data.get(position).getState()
        holder.client_item_constraint.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!client_data.get(position).getCltId().equals(client_data.get(position).getTempId())) {
                    mlistner.onMyListitemSeleted(client_data.get(position));
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return client_data.size();
    }

    public void updateRecords(List<Client> clientsdata) {
        this.client_data = clientsdata;
        notifyDataSetChanged();
    }

    public void searchData(List<Client> clientsdata) {
        this.client_data = clientsdata;
        notifyDataSetChanged();
    }

    public void refreshAdapter(String cltId) {
        int id = Integer.parseInt(cltId);
        client_data.add(AppDataBase.getInMemoryDatabase(EotApp.getAppinstance()).clientModel().getClientFromId(id));
        notifyDataSetChanged();
    }

    @Override
    public Filter getFilter() {
        if (valueFilter == null) {

            valueFilter = new ValueFilter();
        }

        return valueFilter;
    }

    public static class ClientViewHolder extends RecyclerView.ViewHolder {
        TextView client_name, cnt_address;
        ConstraintLayout client_item_constraint;

        public ClientViewHolder(View itemView) {
            super(itemView);
            client_item_constraint = itemView.findViewById(R.id.client_item_constraint);
            client_name = itemView.findViewById(R.id.client_name);
            cnt_address = itemView.findViewById(R.id.cnt_address);
        }
    }

    private class ValueFilter extends Filter {

        //Invoked in a worker thread to filter the data according to the constraint.
        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            FilterResults results = new FilterResults();
            if (constraint != null && constraint.length() > 0) {
                List<Client> filterList = new ArrayList<Client>();
                for (int i = 0; i < client_data.size(); i++) {
                    if ((client_data.get(i).getNm().toUpperCase())
                            .contains(constraint.toString().toUpperCase())) {
                        Client contacts = new Client();
                        contacts.setNm(client_data.get(i).getNm());
                        contacts.setCltId(client_data.get(i).getCltId());
                        filterList.add(contacts);
                    }
                }
                results.count = filterList.size();
                results.values = filterList;
            } else {
                results.count = client_data.size();
                results.values = client_data;
            }
            return results;
        }


        //Invoked in the UI thread to publish the filtering results in the user interface.
        @SuppressWarnings("unchecked")
        @Override
        protected void publishResults(CharSequence constraint,
                                      FilterResults results) {
            client_data = (List<Client>) results.values;
            notifyDataSetChanged();
        }
    }

}
