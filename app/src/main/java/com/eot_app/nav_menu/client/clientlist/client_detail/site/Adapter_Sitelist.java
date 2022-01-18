package com.eot_app.nav_menu.client.clientlist.client_detail.site;

import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.eot_app.R;
import com.eot_app.nav_menu.client.clientlist.client_detail.site.sitelist.Site_model;
import com.eot_app.utility.AppConstant;
import com.eot_app.utility.EotApp;
import com.eot_app.utility.db.AppDataBase;
import com.eot_app.utility.language_support.LanguageController;
import com.eot_app.utility.util_interfaces.MyListItemSelected;

import java.util.List;

/**
 * Created by geet-pc on 14/5/18.
 */

public class Adapter_Sitelist extends RecyclerView.Adapter<Adapter_Sitelist.SiteViewHolder> {
    private final MyListItemSelected mlistner;
    private List<Site_model> site_data;
    private int selected_item;

    public Adapter_Sitelist(MyListItemSelected mlistner, List<Site_model> sitedata) {
        this.site_data = sitedata;
        this.mlistner = mlistner;
    }

    @NonNull
    @Override
    public SiteViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.site_item_view, parent, false);
        return new SiteViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final SiteViewHolder holder, final int position) {

        if (site_data.get(position).getDef().equals("1")) {
            holder.site_name.setText(site_data.get(position).getSnm() + " (Default)");
        } else {
            holder.site_name.setText(site_data.get(position).getSnm());
        }
        holder.imageView6.setVisibility(View.VISIBLE);

        if (site_data.get(position).getSiteId().equals(site_data.get(position).getTempId())) {
            holder.site_address.setText(LanguageController.getInstance().getMobileMsgByKey(AppConstant.site_not_sync));
            holder.site_address.setTextColor(EotApp.getAppinstance().getResources().getColor(android.R.color.holo_red_light));
        } else {
            holder.site_address.setText(site_data.get(position).getAdr());
            if (!TextUtils.isEmpty(site_data.get(position).getCity())) {
                holder.site_address.append(", " + site_data.get(position).getCity());
            }
            holder.site_address.setTextColor(EotApp.getAppinstance().getResources().getColor(R.color.body_font_color));
        }

        //if (!site_data.get(position).getSnm().equalsIgnoreCase("self")) {
        holder.client_item_constraint.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!site_data.get(position).getSiteId().equals(site_data.get(position).getTempId())) {
                    mlistner.onMyListitemSeleted(site_data.get(position));
                    selected_item = position;
                }
            }
        });
//        } else {
//            holder.imageView6.setVisibility(View.GONE);
//        }
    }

    public void updateSiteRecord(List<Site_model> site_models) {
        this.site_data = site_models;
        notifyDataSetChanged();
    }

    public void searchData(List<Site_model> site_data) {
        this.site_data = site_data;
        notifyDataSetChanged();
    }

    public void refreshAdapter(int check, String site_id) {
        switch (check) {
            case 1:
                site_data.add(0, AppDataBase.getInMemoryDatabase(EotApp.getAppinstance()).sitemodel().getSiteFromSiteId(site_id));
                break;
            case 2:
                site_data.set(selected_item, AppDataBase.getInMemoryDatabase(EotApp.getAppinstance()).sitemodel().getSiteFromSiteId(site_id));
                break;
        }
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        return site_data.size();
    }

    public static class SiteViewHolder extends RecyclerView.ViewHolder {
        TextView site_name, site_address;
        ConstraintLayout client_item_constraint;
        ImageView imageView6;

        public SiteViewHolder(View itemView) {
            super(itemView);
            client_item_constraint = itemView.findViewById(R.id.client_item_constraint);
            site_name = itemView.findViewById(R.id.site_name);
            site_address = itemView.findViewById(R.id.site_address);
            imageView6 = itemView.findViewById(R.id.imageView6);
        }
    }
}
