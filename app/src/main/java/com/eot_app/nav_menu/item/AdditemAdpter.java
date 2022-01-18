package com.eot_app.nav_menu.item;

import android.annotation.SuppressLint;
import android.content.Context;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.eot_app.R;
import com.eot_app.nav_menu.jobs.job_detail.addinvoiveitem2pkg.model.InvoiceItemDataModel;
import com.eot_app.utility.AppUtility;
import com.eot_app.utility.App_preference;

import java.util.List;


public class AdditemAdpter extends RecyclerView.Adapter<AdditemAdpter.MyViewHolder> {

    private List<InvoiceItemDataModel> invoiceItemList;


    public AdditemAdpter(Context context, List<InvoiceItemDataModel> invoiceItemList) {
        this.invoiceItemList = invoiceItemList;
    }

    public void updateitemlist(List<InvoiceItemDataModel> invoiceItemList) {
        this.invoiceItemList = invoiceItemList;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.add_item_list_adpter, viewGroup, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder myViewHolder, @SuppressLint("RecyclerView") final int position) {

        if (!invoiceItemList.get(position).getInm().equals(""))
            myViewHolder.item_nm.setText(invoiceItemList.get(position).getInm());
        else myViewHolder.item_nm.setText(invoiceItemList.get(position).getTempNm());


        myViewHolder.qty_edit.setText(invoiceItemList.get(position).getQty());


        myViewHolder.qty_edit.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                invoiceItemList.get(position).setQty(myViewHolder.qty_edit.getText().toString());


                myViewHolder.price.setText(
                        AppUtility.getRoundoff_amount
                                (AppUtility.getCalculatedAmount(invoiceItemList.get(position).getQty(),
                                        invoiceItemList.get(position).getRate(),
                                        invoiceItemList.get(position).getDiscount(),
                                        invoiceItemList.get(position).getTax(),
                                        App_preference.getSharedprefInstance().getLoginRes().getTaxCalculationType())));
            }


            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

    }

    /***
     *
     * ***Calculate Total Amount*******/
    synchronized private void updtaeAmount(final int position) {
        // notifyItemChanged(position);

//        try {
//            new Thread(new Runnable() {
//                @Override
//                public void run() {
//                    notifyItemChanged(position);
//                }
//            }).start();
//        } catch (Exception e) {
//            e.printStackTrace();
//        }

    }


    @Override
    public int getItemViewType(int position) {
        return position;
    }

    public List<InvoiceItemDataModel> getItemList() {
        return invoiceItemList;
    }


    @Override
    public long getItemId(int position) {
        return position;
    }


    @Override
    public int getItemCount() {
        return invoiceItemList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        private final TextView item_nm;
        private final TextView price;
        private final EditText qty_edit;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            item_nm = itemView.findViewById(R.id.item_nm);
            price = itemView.findViewById(R.id.price);
            qty_edit = itemView.findViewById(R.id.qty_edit);

        }


    }


}
