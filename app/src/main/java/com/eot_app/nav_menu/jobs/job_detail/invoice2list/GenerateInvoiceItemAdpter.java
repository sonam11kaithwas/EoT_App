package com.eot_app.nav_menu.jobs.job_detail.invoice2list;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.eot_app.R;
import com.eot_app.nav_menu.jobs.job_detail.addinvoiveitem2pkg.model.InvoiceItemDataModel;
import com.eot_app.utility.AppConstant;
import com.eot_app.utility.AppUtility;
import com.eot_app.utility.App_preference;
import com.eot_app.utility.EotApp;
import com.eot_app.utility.language_support.LanguageController;
import com.eot_app.utility.util_interfaces.MyListItemSelected;
import com.eot_app.utility.util_interfaces.MyListItemSelectedLisT;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;


/**
 * Created by Sonam-11 on 21/10/20.
 */
public class GenerateInvoiceItemAdpter extends RecyclerView.Adapter<GenerateInvoiceItemAdpter.MyViewHolder> {

    private final Set<String> nm_list;
    private final Set<String> ids_list;
    private final MyListItemSelectedLisT<InvoiceItemDataModel> invoce_rm_item;
    private final ArrayList<InvoiceItemDataModel> removeInvoiceItemArrayList = new ArrayList<>();
    private final Context context;
    Set<Boolean> chk_pos;
    MyListItemSelected<InvoiceItemDataModel> myListItemSelected;
    private List<InvoiceItemDataModel> invoiceItemList;
    private boolean[] is_pos_checked;
    private String taxCalculationType = "0";

    /****This for FORM - 1 **/
    public GenerateInvoiceItemAdpter(Context context, List<InvoiceItemDataModel> invoiceItemList) {
        this.invoiceItemList = invoiceItemList;
        this.is_pos_checked = new boolean[invoiceItemList.size()];
        nm_list = new HashSet<>();
        ids_list = new HashSet<>();
        chk_pos = new HashSet<>();
        this.invoce_rm_item = ((MyListItemSelectedLisT) context);
        this.myListItemSelected = ((MyListItemSelected<InvoiceItemDataModel>) context);
        this.taxCalculationType = App_preference.getSharedprefInstance().getLoginRes().getTaxCalculationType();
        this.context = context;
    }

    /****This for FORM - 1 **/
    public void updateitemlist(List<InvoiceItemDataModel> invoiceItemList) {
        this.is_pos_checked = new boolean[invoiceItemList.size()];
        this.invoiceItemList = invoiceItemList;
        notifyDataSetChanged();
    }


    @NonNull
    @Override
    public GenerateInvoiceItemAdpter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        /****This for FORM - 1 **/
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.invoice_list_item_adpter, viewGroup, false);
        return new GenerateInvoiceItemAdpter.MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final GenerateInvoiceItemAdpter.MyViewHolder myViewHolder, @SuppressLint("RecyclerView") final int position) {

        if (!invoiceItemList.get(position).getInm().equals(""))
            myViewHolder.item_nm_invoice.setText(invoiceItemList.get(position).getInm());
        else myViewHolder.item_nm_invoice.setText(invoiceItemList.get(position).getTempNm());


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


        try {
            if (invoiceItemList.get(position).getIsBillable().equals("0")) {
                //  myViewHolder.linear_lay_billable.setBackgroundResource(R.drawable.item_rate_back);
                myViewHolder.non_billable.setVisibility(View.VISIBLE);
                myViewHolder.non_billable.setText(LanguageController.getInstance().getMobileMsgByKey(AppConstant.non_billable));
                //myViewHolder.item_price_invoice.setTextColor(context.getResources().getColor(R.color.white));
            } else {
                //  myViewHolder.linear_lay_billable.setBackgroundResource(R.drawable.item_billable_bck);
                myViewHolder.non_billable.setVisibility(View.GONE);
                //    myViewHolder.item_price_invoice.setTextColor(context.getResources().getColor(R.color.txt_color));

            }
        } catch (Exception exception) {
            exception.getMessage();
        }


        myViewHolder.item_price_invoice.setText(
                AppUtility.getRoundoff_amount
                        (AppUtility.getCalculatedAmount(invoiceItemList.get(position).getQty(),
                                invoiceItemList.get(position).getRate(),
                                invoiceItemList.get(position).getDiscount(),
                                invoiceItemList.get(position).getTax(),
                                taxCalculationType)));


        /***update Item's**/
        myViewHolder.item_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                myListItemSelected.onMyListitemSeleted(invoiceItemList.get(position));
            }
        });

//        if (App_preference.getSharedprefInstance().getLoginRes().getCompPermission().get(0).getIsItemEnable().equals("1")) {
//            myViewHolder.checkbox_invoice.setVisibility(View.GONE);
//
//            RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
//            params.setMargins(-10, 0, 0, 0);
//            myViewHolder.relativeLayout.setLayoutParams(params);
//        }


        /**Remove selected Item's****/
        myViewHolder.checkbox_invoice.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                is_pos_checked[position] = b;
                if (b) {
                    ids_list.add(invoiceItemList.get(myViewHolder.getAdapterPosition()).getItemId());
                    removeInvoiceItemArrayList.add(invoiceItemList.get(myViewHolder.getAdapterPosition()));
                } else {
                    ids_list.remove(invoiceItemList.get(myViewHolder.getAdapterPosition()).getItemId());
                    removeInvoiceItemArrayList.remove(invoiceItemList.get(myViewHolder.getAdapterPosition()));
                }
                invoce_rm_item.onMyListitem_Item_Seleted(removeInvoiceItemArrayList);
            }
        });

        myViewHolder.checkbox_invoice.setChecked(is_pos_checked[position]);

        if (invoiceItemList.get(position).getIjmmId().equals("")) {
            myViewHolder.des.setVisibility(View.VISIBLE);
            myViewHolder.des.setText(LanguageController.getInstance().getMobileMsgByKey(AppConstant.item_not_sync));
            myViewHolder.des.setTextColor(EotApp.getAppinstance().getResources().getColor(android.R.color.holo_red_light));
        } else {
            myViewHolder.des.setVisibility(View.GONE);
        }

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

    /*****/

    public List<InvoiceItemDataModel> getItemList() {
        return invoiceItemList;
    }

    /****This for FORM - 1 **/
    public class MyViewHolder extends RecyclerView.ViewHolder {
        /****This for FORM - 1 **/
        private final TextView item_nm_invoice;
        private final TextView qty_invoice;
        private final TextView item_price_invoice;
        private final TextView des;
        private final CheckBox checkbox_invoice;
        private final RelativeLayout item_layout;
        private final RelativeLayout relativeLayout;
        private final LinearLayout linear_lay_billable;
        private final TextView non_billable;
        private final TextView description;

        /****This for FORM - 2 Update QTY Form **/
        private TextView item_nm, price;
        private EditText qty_edit;


        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            item_nm_invoice = itemView.findViewById(R.id.item_nm_invoice);
            qty_invoice = itemView.findViewById(R.id.qty_invoice);
            item_price_invoice = itemView.findViewById(R.id.item_price_invoice);
            checkbox_invoice = itemView.findViewById(R.id.checkbox_invoice);
            des = itemView.findViewById(R.id.des);
            description = itemView.findViewById(R.id.description);
            item_layout = itemView.findViewById(R.id.item_layout);
            relativeLayout = itemView.findViewById(R.id.relativeLayout);
            non_billable = itemView.findViewById(R.id.non_billable);
            linear_lay_billable = itemView.findViewById(R.id.linear_lay_billable);
        }
    }
}
