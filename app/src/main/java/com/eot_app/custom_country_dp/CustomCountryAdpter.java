package com.eot_app.custom_country_dp;

import android.annotation.SuppressLint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.eot_app.R;
import com.eot_app.utility.Country;

import java.util.List;

/**
 * Created by Sonam-11 on 17/8/20.
 */
public class CustomCountryAdpter extends RecyclerView.Adapter<CustomCountryAdpter.MyViewHolder> {
    private final CountrySelctListn itemListener;
    private List<Country> countryList;

    //      public View views;
    public CustomCountryAdpter(List<Country> countryList, CountrySelctListn itemListener) {
        this.countryList = countryList;
        this.itemListener = itemListener;
    }


    @Override
    public CustomCountryAdpter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_layput, parent, false);

        return new CustomCountryAdpter.MyViewHolder(itemView);
    }

    public void filterList(List<Country> countryList) {
        this.countryList = countryList;
        notifyDataSetChanged();
    }

    @Override
    public void onBindViewHolder(final CustomCountryAdpter.MyViewHolder holder, @SuppressLint("RecyclerView") final int position) {
        final Country country = countryList.get(position);

        holder.itemName.setText(country.getName());

        holder.dplayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                itemListener.selectedCountry(country);
            }
        });
    }

    @Override
    public int getItemCount() {
        return countryList.size();
    }


    public class MyViewHolder extends RecyclerView.ViewHolder {
        public LinearLayout dplayout;

        public TextView itemName;

        public MyViewHolder(View view) {
            super(view);
            dplayout = view.findViewById(R.id.dplayout);
            // views = view.findViewById(R.id.views);
            itemName = view.findViewById(R.id.itemName);
        }
    }

}
