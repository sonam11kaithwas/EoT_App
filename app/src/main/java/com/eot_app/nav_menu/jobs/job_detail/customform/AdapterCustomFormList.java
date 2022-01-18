package com.eot_app.nav_menu.jobs.job_detail.customform;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.eot_app.R;
import com.eot_app.nav_menu.jobs.job_detail.customform.cstm_form_model.CustomFormList_Res;
import com.eot_app.utility.util_interfaces.MyListItemSelected;

import java.util.ArrayList;
import java.util.List;

public class AdapterCustomFormList extends RecyclerView.Adapter<AdapterCustomFormList.CuetomFormList_ViewHolder> {
    List<CustomFormList_Res> data_list;
    MyListItemSelected<CustomFormList_Res> mListner;

    public AdapterCustomFormList(List<CustomFormList_Res> data_list, MyListItemSelected<CustomFormList_Res> mListner) {
        this.data_list = data_list;
        this.mListner = mListner;
    }

    @NonNull
    @Override
    public CuetomFormList_ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.customformlist_item_layout, viewGroup, false);
        return new CuetomFormList_ViewHolder(view);
    }

    public void updateFormList(ArrayList<CustomFormList_Res> tempList) {

    }

    @Override
    public void onBindViewHolder(@NonNull CuetomFormList_ViewHolder holder, int position) {
        holder.custom_form_item_title.setText(data_list.get(position).getFrmnm());
    }

    @Override
    public int getItemCount() {
        return data_list.size();
    }

    class CuetomFormList_ViewHolder extends RecyclerView.ViewHolder {
        private final TextView custom_form_item_title;

        public CuetomFormList_ViewHolder(@NonNull View itemView) {
            super(itemView);
            custom_form_item_title = itemView.findViewById(R.id.custom_form_item_title);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mListner.onMyListitemSeleted(data_list.get(getAdapterPosition()));
                }
            });
        }
    }
}
