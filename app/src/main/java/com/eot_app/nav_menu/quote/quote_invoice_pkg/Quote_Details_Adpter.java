package com.eot_app.nav_menu.quote.quote_invoice_pkg;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.eot_app.R;
import com.eot_app.nav_menu.quote.quote_invoice_pkg.quote_model_pkg.Quote_ItemData;
import com.eot_app.utility.AppConstant;
import com.eot_app.utility.AppUtility;
import com.eot_app.utility.App_preference;
import com.eot_app.utility.language_support.LanguageController;
import com.eot_app.utility.util_interfaces.MyListItemSelected;
import com.eot_app.utility.util_interfaces.MyListItemSelectedLisT;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class Quote_Details_Adpter extends RecyclerView.Adapter<Quote_Details_Adpter.MyViewHolder> {

    private final Context context;
    private final Set<String> nm_list;
    private final Set<String> ids_list;
    private final MyListItemSelectedLisT<String> invoce_rm_item;
    private final ArrayList<String> removeItem = new ArrayList<>();
    private final String taxCalculationType;
    Set<Boolean> chk_pos;
    MyListItemSelected<Quote_ItemData> myListItemSelected;
    private List<Quote_ItemData> invoiceItemList;
    private boolean[] is_pos_checked;

    public Quote_Details_Adpter(Context context, List<Quote_ItemData> invoiceItemList, MyListItemSelected<Quote_ItemData> myListItemSelected, MyListItemSelectedLisT<String> invoce_rm_item, String taxCalculationType) {
        this.invoiceItemList = invoiceItemList;
        this.context = context;
        this.myListItemSelected = myListItemSelected;
        this.invoce_rm_item = invoce_rm_item;
        this.taxCalculationType = taxCalculationType;
        this.is_pos_checked = new boolean[invoiceItemList.size()];
        nm_list = new HashSet<>();
        ids_list = new HashSet<>();
        chk_pos = new HashSet<>();

    }


    void updateitemlist(List<Quote_ItemData> invoiceItemList) {
        this.is_pos_checked = new boolean[invoiceItemList.size()];
        this.invoiceItemList = invoiceItemList;
        //itemDataArrayList.clear();
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public Quote_Details_Adpter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.invoice_list_item_adpter, viewGroup, false);
        return new Quote_Details_Adpter.MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final Quote_Details_Adpter.MyViewHolder myViewHolder, final int position) {
        myViewHolder.item_nm_invoice.setText(invoiceItemList.get(position).getInm());
        //  String qty = AppUtility.getRoundoff_amount(invoiceItemList.get(position).getQty());
        //   myViewHolder.qty_invoice.setText(LanguageController.getInstance().getMobileMsgByKey(AppConstant.qty) + ": " + invoiceItemList.get(position).getQty());
        myViewHolder.item_price_invoice.setText(AppUtility.getRoundoff_amount(AppUtility.getCalculatedAmountForQuotes(
                invoiceItemList.get(position).getQty(),
                invoiceItemList.get(position).getRate(),
                invoiceItemList.get(position).getDiscount()
                , invoiceItemList.get(position).getTax(), taxCalculationType)));

        myViewHolder.item_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                myListItemSelected.onMyListitemSeleted(invoiceItemList.get(position));
            }
        });

        /***ShoW unit when Unit permission allow by Administrator ***/
        if (!App_preference.getSharedprefInstance().getLoginRes().getCompPermission().get(0).getUnit().equals("1") && invoiceItemList.get(position).getUnit() != null && !invoiceItemList.get(position).getUnit().equals("")) {
            myViewHolder.qty_invoice.setText(LanguageController.getInstance().getMobileMsgByKey(AppConstant.unit) + ": " + invoiceItemList.get(position).getUnit());
        } else {
            myViewHolder.qty_invoice.setText(LanguageController.getInstance().getMobileMsgByKey(AppConstant.qty) + ": " + invoiceItemList.get(position).getQty());
        }


        /***remove this Jit Sir on 23 oct 2020**/
        if (App_preference.getSharedprefInstance().getLoginRes().getIsItemDeleteEnable().equals("0")) {
            myViewHolder.checkbox_invoice.setVisibility(View.GONE);
            RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
            params.setMargins(-10, 0, 0, 0);
            myViewHolder.relativeLayout.setLayoutParams(params);
        } else {
            myViewHolder.checkbox_invoice.setVisibility(View.VISIBLE);
        }

        myViewHolder.checkbox_invoice.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                is_pos_checked[position] = b;
                if (b) {
                    ids_list.add(invoiceItemList.get(myViewHolder.getAdapterPosition()).getItemId());
                    //itemDataArrayList.add(invoiceItemList.get(myViewHolder.getAdapterPosition()));
                    removeItem.add(invoiceItemList.get(myViewHolder.getAdapterPosition()).getIqmmId());
                } else {
                    ids_list.remove(invoiceItemList.get(myViewHolder.getAdapterPosition()).getItemId());
                    //itemDataArrayList.remove(invoiceItemList.get(myViewHolder.getAdapterPosition()));
                    removeItem.remove(invoiceItemList.get(myViewHolder.getAdapterPosition()).getIqmmId());
                }
                //invoce_rm_item.onMyListitem_Item_Seleted(itemDataArrayList);
                invoce_rm_item.onMyListitem_Item_Seleted(removeItem);
            }
        });


        myViewHolder.non_billable.setVisibility(View.GONE);


        myViewHolder.checkbox_invoice.setChecked(is_pos_checked[position]);

              /*Jit, 17:43 19-10-21
        Hi @all,
        items me  desc show karwana hai*/
        try {
            if (invoiceItemList.get(position).getDes() != null && !invoiceItemList.get(position).getDes().equals("")) {
                myViewHolder.description.setVisibility(View.VISIBLE);
                myViewHolder.description.setText(invoiceItemList.get(position).getDes());
            } else {
                myViewHolder.description.setVisibility(View.GONE);
            }
        } catch (Exception exception) {
            exception.printStackTrace();
        }
    }

    @Override
    public int getItemCount() {
        return invoiceItemList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        private final TextView item_nm_invoice;
        private final TextView qty_invoice;
        private final TextView item_price_invoice, description;
        private final CardView card_invoice_item;
        private final CheckBox checkbox_invoice;
        private final RelativeLayout item_layout;
        private final RelativeLayout relativeLayout;
        private final TextView non_billable;


        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            item_nm_invoice = itemView.findViewById(R.id.item_nm_invoice);
            qty_invoice = itemView.findViewById(R.id.qty_invoice);
            item_price_invoice = itemView.findViewById(R.id.item_price_invoice);
            checkbox_invoice = itemView.findViewById(R.id.checkbox_invoice);

            card_invoice_item = itemView.findViewById(R.id.card_invoice_item);
            item_layout = itemView.findViewById(R.id.item_layout);
            relativeLayout = itemView.findViewById(R.id.relativeLayout);

            non_billable = itemView.findViewById(R.id.non_billable);
            description = itemView.findViewById(R.id.description);

        }
    }
}
