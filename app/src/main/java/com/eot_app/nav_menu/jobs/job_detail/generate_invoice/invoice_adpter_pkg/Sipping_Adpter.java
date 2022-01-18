package com.eot_app.nav_menu.jobs.job_detail.generate_invoice.invoice_adpter_pkg;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.eot_app.R;
import com.eot_app.nav_menu.jobs.job_detail.invoice.invoice_detail_pkg.inv_detail_model.ShippingItem;
import com.eot_app.nav_menu.jobs.job_detail.invoice.invoice_detail_pkg.inv_detail_model.Tax;
import com.eot_app.utility.AppUtility;

import java.util.ArrayList;
import java.util.List;

public class Sipping_Adpter extends RecyclerView.Adapter<Sipping_Adpter.MyViewHolder> {
    //   private Context context;
    private static final int NUMBER_OF_BLANK_CARDS = 2;
    private List<ShippingItem> shippingItemList;
    private String taxCalculationType;

    public Sipping_Adpter(List<ShippingItem> shippingItem) {
        this.shippingItemList = shippingItem;
    }


    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.shipping_adpter_layout, viewGroup, false);
        return new Sipping_Adpter.MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder myViewHolder, int pos) {
        if (pos >= shippingItemList.size()) {
            myViewHolder.card_shipping_item.setVisibility(View.INVISIBLE);
        } else {
            myViewHolder.card_shipping_item.setVisibility(View.VISIBLE);
            myViewHolder.shiping_Item_Nm.setText(shippingItemList.get(pos).getInm());
            //   myViewHolder.shpinng_item_rate.setText(shippingItemList.get(pos).getRate());
            List<Tax> taxList = new ArrayList<>();
            myViewHolder.shpinng_item_rate.setText(AppUtility.getRoundoff_amount(AppUtility.getCalculatedAmount("1", shippingItemList.get(pos).getRate(), "0", taxList, taxCalculationType)));
        }
    }

    @Override
    public int getItemCount() {
        return shippingItemList.size() + NUMBER_OF_BLANK_CARDS;
//        return shippingItemList.size();
    }
//    public int getListItemCount() {
//        return shippingItemList.size();
//    }


    public void updateShpiningItem(List<ShippingItem> shippingItem_List, String taxCalculationType) {
        this.shippingItemList = shippingItem_List;
        this.taxCalculationType = taxCalculationType;
        notifyDataSetChanged();
    }


    class MyViewHolder extends RecyclerView.ViewHolder {

        private final TextView shiping_Item_Nm;
        private final TextView shpinng_item_rate;
        private final CardView card_shipping_item;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            card_shipping_item = itemView.findViewById(R.id.card_shipping_item);
            shiping_Item_Nm = itemView.findViewById(R.id.shiping_Item_Nm);
            shpinng_item_rate = itemView.findViewById(R.id.shpinng_item_rate);
        }
    }
}
