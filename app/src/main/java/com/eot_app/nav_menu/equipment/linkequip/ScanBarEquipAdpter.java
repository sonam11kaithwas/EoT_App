package com.eot_app.nav_menu.equipment.linkequip;

import android.content.Context;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.cardview.widget.CardView;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.eot_app.R;
import com.eot_app.nav_menu.audit.audit_list.equipment.model.EquipmentStatus;
import com.eot_app.nav_menu.jobs.job_db.EquArrayModel;
import com.eot_app.utility.AppConstant;
import com.eot_app.utility.AppUtility;
import com.eot_app.utility.App_preference;
import com.eot_app.utility.language_support.LanguageController;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Sona-11 on 11/9/21.
 */
class ScanBarEquipAdpter extends RecyclerView.Adapter<ScanBarEquipAdpter.MyViewHolder> {
    Context mContext;
    List<EquArrayModel> list;
    ScanBarEquipAdpter.OnEquipmentSelection onEquipmentSelection;
    List<EquipmentStatus> equipmentStatus;
    private List<EquArrayModel> filterList;


    //    boolean FROMSCAN=false;
    public ScanBarEquipAdpter(Context mContext, OnEquipmentSelection onEquipmentSelection) {
        this.mContext = mContext;
        this.onEquipmentSelection = onEquipmentSelection;

    }

    public void setEquipmentStatus(List<EquipmentStatus> equipmentStatus) {
        this.equipmentStatus = equipmentStatus;

    }


    public void setList(List<EquArrayModel> list) {
        this.list = list;
        if (this.filterList != null)
            this.filterList.clear();
        else
            this.filterList = new ArrayList<EquArrayModel>();
        // we copy the original list to the filter list and use it for setting row values
        this.filterList.addAll(this.list);
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ScanBarEquipAdpter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_link_equipment2, viewGroup, false);
        return new ScanBarEquipAdpter.MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ScanBarEquipAdpter.MyViewHolder holder, final int position) {

        holder.tv_item_name.setText(filterList.get(position).getEqunm());

        if (!TextUtils.isEmpty(filterList.get(position).getAdr())) {
            holder.tv_address.setText(filterList.get(position).getAdr());
            holder.tv_address.setVisibility(View.VISIBLE);
        } else holder.tv_address.setVisibility(View.GONE);


        /** 0 Main equipment 1 means Part(Sub equipment) ****/
        if (!TextUtils.isEmpty(filterList.get(position).getIsPart()) && !filterList.get(position).getIsPart().equals("0"))
            holder.condition.setVisibility(View.VISIBLE);
        else holder.condition.setVisibility(View.GONE);

        if (filterList.get(position).getLinkStatus() == 0) {
            holder.ll_remark.setBackground(ContextCompat.getDrawable(mContext, R.drawable.chip_backgrnd));
            holder.tv_remark.setText(LanguageController.getInstance().getMobileMsgByKey(AppConstant.unlinked));
            holder.img_off.setVisibility(View.VISIBLE);
            holder.img_on.setVisibility(View.GONE);
        } else if (filterList.get(position).getLinkStatus() == 1) {
            holder.ll_remark.setBackground(ContextCompat.getDrawable(mContext, R.drawable.link_equipment_back));
            holder.tv_remark.setText(LanguageController.getInstance().getMobileMsgByKey(AppConstant.linked));
            holder.img_off.setVisibility(View.GONE);
            holder.img_on.setVisibility(View.VISIBLE);
        }


        holder.tv_model_label.setText(LanguageController.getInstance().getMobileMsgByKey(AppConstant.equipment_model) + ": ");
        holder.tv_serial_label.setText(LanguageController.getInstance().getMobileMsgByKey(AppConstant.equipment_serial) + ": ");

        holder.tv_model.setText("" + filterList.get(position).getMno());
        holder.tv_serial.setText("" + filterList.get(position).getSno());


        if (!TextUtils.isEmpty(filterList.get(position).getImage())) {
            Glide.with(mContext).load(App_preference.getSharedprefInstance().getBaseURL() + filterList.get(position).getImage())
                    .placeholder(R.drawable.app_logo2).into(holder.img_equipment);
        }

        try {
            if (filterList.get(position).getStatus() != null) {
                if (filterList.get(position).getStatus().equals("")) {
                    holder.tv_status.setVisibility(View.GONE);
                } else if (filterList.get(position).getStatus().equals("0")) {
                    holder.tv_status.setVisibility(View.GONE);
                } else {
                    holder.tv_status.setVisibility(View.VISIBLE);
                    holder.tv_status.setText(getStatusNameById(filterList.get(position).getStatus()));
                }
            } else {
                holder.tv_status.setVisibility(View.GONE);
            }
        } catch (Exception ex) {
            holder.tv_status.setVisibility(View.GONE);
        }
    }

    @Override
    public int getItemCount() {
        return (null != filterList ? filterList.size() : 0);
    }

    // Do Search...
    public void filterListByStatus(final int filterIndex) {
        // Searching could be complex..so we will dispatch it to a different thread...
        new Thread(new Runnable() {
            @Override
            public void run() {

                // Clear the filter list
                filterList.clear();

                // If there is no search value, then add all original list items to filter list
                if (filterIndex == 0) {
                    filterList.addAll(list);

                } else if (filterIndex == 1) {
                    for (EquArrayModel item : list) {
                        if (item.getLinkStatus() == 1) {
                            filterList.add(item);
                            Log.d("adapterStatus", item.getLinkStatus() + "");
                        }
                    }
                } else if (filterIndex == 2) {
                    for (EquArrayModel item : list) {
                        if (item.getLinkStatus() == 0) {
                            filterList.add(item);
                            Log.d("adapterStatus", item.getLinkStatus() + "");

                        }
                    }
                }


//                // Set on UI Thread
//                ((Activity) mContext).runOnUiThread(new Runnable() {
//                    @Override
//                    public void run() {
//                        // Notify the List that the DataSet has changed...
//                        if (onEquipmentSelection != null)
//                            onEquipmentSelection.onListSizeChange(filterList.size());
//                        notifyDataSetChanged();
//                    }
//                });

            }
        }).start();
    }

    private String getStatusNameById(String statusId) {
        String statusName = "";
        if (equipmentStatus != null) {
            for (EquipmentStatus equipmentStatus : equipmentStatus)
                if (equipmentStatus.getEsId().equals(statusId)) {
                    statusName = equipmentStatus.getStatusText();
                    break;
                }

        }
        return statusName;
    }

    private void getLinkedEquipmentFromList(EquArrayModel equArrayModel) {
        if (list != null) {
            List<String> selectedEquipment = new ArrayList<>();
            for (EquArrayModel model : list) {
                if (model.equId.equals(equArrayModel.getEquId()))
                    model.setLinkStatus(equArrayModel.getLinkStatus());

                if (model.getLinkStatus() == 1)
                    selectedEquipment.add(model.getEquId());
            }


            onEquipmentSelection.onEquipmentSelected(selectedEquipment);
        }
    }

    public void updateListByLinkToUnLink() {
        try {
            if (list != null && list.size() > 0) {
//                if (list.get(0).getLinkStatus() == 0) {
//                    list.get(0).setLinkStatus(1);
//                } else if (list.get(0).getLinkStatus() == 1) {
//                    list.get(0).setLinkStatus(0);
//                }
                notifyDataSetChanged();
            }
        } catch (Exception exception) {
            exception.printStackTrace();
        }
    }

    public List<String> getSelectedEquList() {
        List<String> selectedEquipment = new ArrayList<>();
        try {
            for (EquArrayModel model : list) {
                if (model.getLinkStatus() == 1)
                    selectedEquipment.add(model.getEquId());
            }
        } catch (Exception exception) {
            exception.printStackTrace();
        }
        return selectedEquipment;
    }


    public EquArrayModel getSelectedEquModel() {
        EquArrayModel model = new EquArrayModel();
        try {
            for (EquArrayModel models : list) {
                if (models.getLinkStatus() == 1)
                    return models;
            }
        } catch (Exception exception) {
            exception.printStackTrace();
        }
        return model;
    }

    public interface OnEquipmentSelection {
        void onEquipmentSelected(List<String> linkedEquipment);
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        CardView client_item_constraint;
        TextView tv_item_name, tv_address, condition;
        AppCompatImageView img_equipment;
        AppCompatImageView img_off, img_on;//img_remark,
        AppCompatTextView tv_model, tv_serial, tv_remark, tv_model_label, tv_serial_label, tv_status;
        LinearLayout ll_remark;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            client_item_constraint = itemView.findViewById(R.id.client_item_constraint);

            condition = itemView.findViewById(R.id.condition);
            condition.setText(LanguageController.getInstance().getMobileMsgByKey(AppConstant.parts));
            tv_item_name = itemView.findViewById(R.id.tv_item_name);
            tv_address = itemView.findViewById(R.id.tv_address);
            img_equipment = itemView.findViewById(R.id.img_equipment);
            tv_model = itemView.findViewById(R.id.tv_model);
            tv_serial = itemView.findViewById(R.id.tv_serial);
            tv_remark = itemView.findViewById(R.id.tv_remark);
            tv_model_label = itemView.findViewById(R.id.tv_model_label);
            tv_serial_label = itemView.findViewById(R.id.tv_serial_label);
            ll_remark = itemView.findViewById(R.id.ll_remark);
            //img_remark = itemView.findViewById(R.id.img_remark);
            tv_status = itemView.findViewById(R.id.tv_status);
            img_off = itemView.findViewById(R.id.img_off);
            img_on = itemView.findViewById(R.id.img_on);

            ll_remark.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (onEquipmentSelection != null) {
                        if (filterList.get(getAdapterPosition()).getIsRemarkAdd() == 1) {
                            AppUtility.alertDialog(mContext, "",
                                    LanguageController.getInstance().getMobileMsgByKey(AppConstant.unlink_info_msg),
                                    "", LanguageController.getInstance().getMobileMsgByKey(AppConstant.ok), null);
                        } else {
                            if (filterList.get(getAdapterPosition()).getLinkStatus() == 1)
                                filterList.get(getAdapterPosition()).setLinkStatus(0);
                            else filterList.get(getAdapterPosition()).setLinkStatus(1);
                            getLinkedEquipmentFromList(filterList.get(getAdapterPosition()));
                        }
                    }
                }
            });
        }
    }
}

