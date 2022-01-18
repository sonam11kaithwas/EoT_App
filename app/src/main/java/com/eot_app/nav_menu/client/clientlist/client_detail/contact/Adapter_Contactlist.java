package com.eot_app.nav_menu.client.clientlist.client_detail.contact;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.eot_app.R;
import com.eot_app.nav_menu.client.clientlist.client_detail.contact.client_dao.ContactData;
import com.eot_app.utility.AppConstant;
import com.eot_app.utility.EotApp;
import com.eot_app.utility.language_support.LanguageController;
import com.eot_app.utility.util_interfaces.MyListItemSelected;

import java.util.List;

/**
 * Created by geet-pc on 14/5/18.
 */

public class Adapter_Contactlist extends RecyclerView.Adapter<Adapter_Contactlist.ContactViewHolder> {

    private final MyListItemSelected mlistner;
    private List<ContactData> contact_data;

    //    private int selected_item;
    public Adapter_Contactlist(MyListItemSelected mlistner, List<ContactData> contactdata) {
        this.contact_data = contactdata;
        this.mlistner = mlistner;
    }

    @NonNull
    @Override
    public ContactViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.contact_item_view, parent, false);
        return new ContactViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ContactViewHolder holder, final int position) {
        if (contact_data.get(position).getDef().equals("1")) {
            holder.contact_name.setText(contact_data.get(position).getCnm() + " (Default)");
        } else {
            holder.contact_name.setText(contact_data.get(position).getCnm());
        }
        if (contact_data.get(position).getConId().equals(contact_data.get(position).getTempId())) {
            holder.contact_no.setText(LanguageController.getInstance().getMobileMsgByKey(AppConstant.contact_not_sync));
            holder.contact_no.setTextColor(EotApp.getAppinstance().getResources().getColor(android.R.color.holo_red_light));
        } else {
            holder.contact_no.setText(contact_data.get(position).getMob1());
            holder.contact_no.setTextColor(EotApp.getAppinstance().getResources().getColor(R.color.body_font_color));
        }
        holder.client_item_constraint.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!contact_data.get(position).getConId().equals(contact_data.get(position).getTempId())) {
                    mlistner.onMyListitemSeleted(contact_data.get(position));
                }

            }
        });
    }

    //    public void refreshContactList(int check, String conId) {
//        switch (check) {
//            case 1:
//                contact_data.add(0,AppDataBase.getInMemoryDatabase(EotApp.getAppinstance()).contactModel().getContactById(conId));
//                break;
//            case 2:
//                contact_data.set(selected_item, AppDataBase.getInMemoryDatabase(EotApp.getAppinstance()).contactModel().getContactById(conId));
//                break;
//        }
//        notifyDataSetChanged();
//    }
    @Override
    public int getItemCount() {
        return contact_data.size();
    }

    public void updateRecords(List<ContactData> contactData) {
        this.contact_data = contactData;
        notifyDataSetChanged();
    }

    public void searchData(List<ContactData> contact_data) {
        this.contact_data = contact_data;
        notifyDataSetChanged();
    }

    public static class ContactViewHolder extends RecyclerView.ViewHolder {
        TextView contact_name, contact_no;
        ConstraintLayout client_item_constraint;

        public ContactViewHolder(View itemView) {
            super(itemView);
            client_item_constraint = itemView.findViewById(R.id.client_item_constraint);
            contact_name = itemView.findViewById(R.id.contact_name);
            contact_no = itemView.findViewById(R.id.contact_no);
        }
    }
}
