package com.eot_app.nav_menu.jobs.job_detail.invoice;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.eot_app.R;
import com.eot_app.nav_menu.jobs.job_detail.invoice.invoice_detail_pkg.inv_detail_model.ItemData;
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


public class Invoice_list_Adpter extends RecyclerView.Adapter<Invoice_list_Adpter.MyViewHolder> {

    private final Context context;
    private final Set<String> nm_list;
    private final Set<String> ids_list;
    private final MyListItemSelectedLisT<ItemData> invoce_rm_item;
    private final ArrayList<ItemData> removeInvoiceItemArrayList = new ArrayList<>();
    Set<Boolean> chk_pos;
    MyListItemSelected<ItemData> myListItemSelected;
    private List<ItemData> invoiceItemList;
    private boolean[] is_pos_checked;
    private String taxCalculationType = "0";

    public Invoice_list_Adpter(Context context, List<ItemData> invoiceItemList) {//, MyListItemSelected<ItemData> myListItemSelected, MyListItemSelectedLisT<ItemData> invoce_rm_item
        this.invoiceItemList = invoiceItemList;
        this.context = context;
        this.is_pos_checked = new boolean[invoiceItemList.size()];
        nm_list = new HashSet<>();
        ids_list = new HashSet<>();
        chk_pos = new HashSet<>();
        this.invoce_rm_item = ((MyListItemSelectedLisT) context);
        this.myListItemSelected = ((MyListItemSelected<ItemData>) context);//.onMyListitemSeleted(invoiceItemList.get(0));

    }

    void updateitemlist(List<ItemData> invoiceItemList, String taxCalculationType) {
        this.is_pos_checked = new boolean[invoiceItemList.size()];
        this.invoiceItemList = invoiceItemList;
        this.taxCalculationType = taxCalculationType;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.invoice_list_item_adpter, viewGroup, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder myViewHolder, final int position) {
        myViewHolder.item_nm_invoice.setText(invoiceItemList.get(position).getInm());
        String qty = AppUtility.getRoundoff_amount(invoiceItemList.get(position).getQty());
        myViewHolder.qty_invoice.setText(LanguageController.getInstance().getMobileMsgByKey(AppConstant.qty) + ": " + qty);
        myViewHolder.item_price_invoice.setText(
                AppUtility.getRoundoff_amount
                        (AppUtility.getCalculatedAmount(invoiceItemList.get(position).getQty(),
                                invoiceItemList.get(position).getRate(),
                                invoiceItemList.get(position).getDiscount(),
                                invoiceItemList.get(position).getTax(),
                                taxCalculationType)));

        myViewHolder.item_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                myListItemSelected.onMyListitemSeleted(invoiceItemList.get(position));
            }
        });

        if (App_preference.getSharedprefInstance().getLoginRes().getCompPermission().get(0).getIsItemEnable().equals("1")) {
            myViewHolder.checkbox_invoice.setVisibility(View.GONE);
        }
        myViewHolder.checkbox_invoice.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                is_pos_checked[position] = b;
                if (b) {
                    ids_list.add(invoiceItemList.get(myViewHolder.getAdapterPosition()).getItemId());
                    removeInvoiceItemArrayList.add(invoiceItemList.get(myViewHolder.getAdapterPosition()));
                    //invoiceItemList.remove(invoiceItemList.get(myViewHolder.getAdapterPosition()));
                } else {
                    ids_list.remove(invoiceItemList.get(myViewHolder.getAdapterPosition()).getItemId());
                    removeInvoiceItemArrayList.remove(invoiceItemList.get(myViewHolder.getAdapterPosition()));
                    // invoiceItemList.add(invoiceItemList.get(myViewHolder.getAdapterPosition()));
                }
                invoce_rm_item.onMyListitem_Item_Seleted(removeInvoiceItemArrayList);
            }
        });

        myViewHolder.checkbox_invoice.setChecked(is_pos_checked[position]);

    }

    @Override
    public int getItemCount() {
        return invoiceItemList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        private final TextView item_nm_invoice;
        private final TextView qty_invoice;
        private final TextView item_price_invoice;
        private final CheckBox checkbox_invoice;
        private final RelativeLayout item_layout;


        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            item_nm_invoice = itemView.findViewById(R.id.item_nm_invoice);
            qty_invoice = itemView.findViewById(R.id.qty_invoice);
            item_price_invoice = itemView.findViewById(R.id.item_price_invoice);
            checkbox_invoice = itemView.findViewById(R.id.checkbox_invoice);

            item_layout = itemView.findViewById(R.id.item_layout);

        }
    }
}