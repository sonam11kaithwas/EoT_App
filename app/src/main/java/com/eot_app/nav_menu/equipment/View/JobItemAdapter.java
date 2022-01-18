package com.eot_app.nav_menu.equipment.View;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.eot_app.R;
import com.eot_app.nav_menu.jobs.job_detail.addinvoiveitem2pkg.model.InvoiceItemDataModel;
import com.eot_app.utility.AppConstant;
import com.eot_app.utility.AppUtility;
import com.eot_app.utility.App_preference;
import com.eot_app.utility.language_support.LanguageController;

import java.util.List;

public class JobItemAdapter extends RecyclerView.Adapter<JobItemAdapter.MyViewHolder> {
    private final String taxCalculationType = "0";
    List<InvoiceItemDataModel> data;
    Context mContext;


    public JobItemAdapter(Context mContext, List<InvoiceItemDataModel> data) {
        this.mContext = mContext;
        this.data = data;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_job_item_details, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder myViewHolder, int position) {
        myViewHolder.item_nm_invoice.setText(data.get(position).getInm());

        /***ShoW unit when Unit permission allow by Administrator ***/
        if (!App_preference.getSharedprefInstance().getLoginRes().getCompPermission().get(0).getUnit().equals("1")
                && data.get(position).getUnit() != null
                && !data.get(position).getUnit().equals("")) {
            myViewHolder.qty_invoice.setText(LanguageController.getInstance().getMobileMsgByKey(AppConstant.unit) + ": " + data.get(position).getUnit());
        } else {
            myViewHolder.qty_invoice.setText(LanguageController.getInstance().getMobileMsgByKey(AppConstant.qty) + ": " + data.get(position).getQty());
        }


        myViewHolder.item_price_invoice.setText(
                AppUtility.getRoundoff_amount
                        (AppUtility.getCalculatedAmount(data.get(position).getQty(),
                                data.get(position).getRate(),
                                data.get(position).getDiscount(),
                                data.get(position).getTax(),
                                taxCalculationType)));


    }


    @Override
    public int getItemCount() {
        return data == null ? 0 : data.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        private final TextView des;
        private final TextView item_nm_invoice;
        private final TextView qty_invoice;
        private final TextView item_price_invoice;


        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            item_nm_invoice = itemView.findViewById(R.id.item_nm_invoice);
            qty_invoice = itemView.findViewById(R.id.qty_invoice);
            item_price_invoice = itemView.findViewById(R.id.item_price_invoice);
            des = itemView.findViewById(R.id.des);
        }
    }
}
