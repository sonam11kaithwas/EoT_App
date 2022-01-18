package com.eot_app.nav_menu.jobs.job_detail.job_equipment;

import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.text.Spannable;
import android.text.TextUtils;
import android.text.style.TextAppearanceSpan;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.eot_app.R;
import com.eot_app.nav_menu.audit.audit_list.equipment.model.EquipmentStatus;
import com.eot_app.nav_menu.jobs.job_db.EquArrayModel;
import com.eot_app.nav_menu.jobs.job_detail.detail.CompletionAdpterJobDteails;
import com.eot_app.nav_menu.jobs.job_detail.documents.doc_model.GetFileList_Res;
import com.eot_app.nav_menu.jobs.job_detail.job_equipment.job_equ_remrk.JobEquRemarkRemarkActivity;
import com.eot_app.utility.AppConstant;
import com.eot_app.utility.App_preference;
import com.eot_app.utility.CustomLinearLayoutManager;
import com.eot_app.utility.language_support.LanguageController;
import com.google.gson.Gson;
import com.hypertrack.hyperlog.HyperLog;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Sonam-11 on 22/9/20.
 */
public class JobEquipmentAdapter extends RecyclerView.Adapter<JobEquipmentAdapter.MyViewHolder> {
    private final OnEquipmentSelectionForDetails selectionForDetails;
    private final ArrayList<EquArrayModel> suggestions;
    Context mContext;
    List<EquArrayModel> list;
    OnEquipmentSelection onEquipmentSelection;
    List<EquipmentStatus> equipmentStatus;
    SelectedpositionForAttchment selectedpositionForAttchment;


    public JobEquipmentAdapter(Context mContext, OnEquipmentSelectionForDetails selectionForDetails, SelectedpositionForAttchment selectedpositionForAttchment) {
        this.mContext = mContext;
        equipmentStatus = App_preference.getSharedprefInstance().getLoginRes().getEquipmentStatus();
        this.selectionForDetails = selectionForDetails;
        this.suggestions = new ArrayList<>();
        this.selectedpositionForAttchment = selectedpositionForAttchment;
    }


    public void filterList(ArrayList<EquArrayModel> filterdNames) {
        this.list = filterdNames;
        notifyDataSetChanged();
    }


    public List<EquArrayModel> getList() {
        return list;
    }

    public void setList(List<EquArrayModel> list) {
        this.list = list;
        notifyDataSetChanged();
    }

    public void setOnEquipmentSelection(JobEquipmentAdapter.OnEquipmentSelection onEquipmentSelection) {
        this.onEquipmentSelection = onEquipmentSelection;
    }

    @NonNull
    @Override
    public JobEquipmentAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_audit_equipment, viewGroup, false);
        return new JobEquipmentAdapter.MyViewHolder(view);
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public void onBindViewHolder(@NonNull JobEquipmentAdapter.MyViewHolder holder, final int position) {
        EquArrayModel equArrayModel = list.get(position);
        Log.e("List", "" + new Gson().toJson(list));
        holder.tv_item_name.setText(equArrayModel.getEqunm());


        if (!TextUtils.isEmpty(equArrayModel.getLocation())) {
            holder.tv_address.setText(equArrayModel.getLocation());
            holder.tv_address.setVisibility(View.VISIBLE);
        } else holder.tv_address.setVisibility(View.GONE);


        if (!TextUtils.isEmpty(equArrayModel.getStatus()) || equArrayModel.getAttachments() != null && equArrayModel.getAttachments().size() > 0) {
            holder.edit_remark_layout.setVisibility(View.VISIBLE);
            setDataInRemarkView(holder, equArrayModel, position);
            holder.add_remark.setVisibility(View.GONE);
        } else {
            HyperLog.i("", "JobEquipmentAdapter: " + "onBindViewHolder(M)" + "status null");
            HyperLog.i("", "JobEquipmentAdapter: " + equArrayModel.getStatus());
            holder.edit_remark_layout.setVisibility(View.GONE);
            holder.add_remark.setVisibility(View.VISIBLE);
            holder.add_remark.setText(LanguageController.getInstance().getMobileMsgByKey(AppConstant.add_remark));

//            if (equArrayModel.getAttachments() != null && equArrayModel.getAttachments().size() > 0) {
//                updateAttchmentList(holder, equArrayModel.getAttachments(), position);
//            }
        }


        /** 0 Mean equipment 1 means Part(Sub equipment) ****/
        if (!TextUtils.isEmpty(equArrayModel.getIsPart()) && !equArrayModel.getIsPart().equals("0"))
            holder.condition.setVisibility(View.VISIBLE);
        else holder.condition.setVisibility(View.GONE);

        holder.tv_model_label.setText(LanguageController.getInstance().getMobileMsgByKey(AppConstant.equipment_model) + ": ");
        holder.tv_serial_label.setText(LanguageController.getInstance().getMobileMsgByKey(AppConstant.equipment_serial) + ": ");

        holder.tv_model.setText("" + equArrayModel.getMno());
        holder.tv_serial.setText("" + equArrayModel.getSno());


        if (!TextUtils.isEmpty(equArrayModel.getImage())) {
            holder.equ_img_view.setVisibility(View.GONE);
            holder.img_equipment.setVisibility(View.VISIBLE);
            Glide.with(mContext).load(App_preference.getSharedprefInstance().getBaseURL() + equArrayModel.getImage())
                    .placeholder(R.drawable.app_logo2).into(holder.img_equipment);
        } else {
            holder.equ_img_view.setVisibility(View.VISIBLE);
            holder.equ_img_view.setText(LanguageController.getInstance().getMobileMsgByKey(AppConstant.Equipment_img));
            holder.img_equipment.setVisibility(View.GONE);
        }

        holder.view_details.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectionForDetails.onEquipmentSelectedForDetails(list.get(position));

            }
        });

    }


    private void setDataInRemarkView(MyViewHolder holder, final EquArrayModel equArrayModel, final int equiPos) {
        holder.remark_txt.setText(LanguageController.getInstance().getMobileMsgByKey(AppConstant.remark));
        holder.remark_condition_txt.setText(LanguageController.getInstance().getMobileMsgByKey(AppConstant.condition) + ": ");
        holder.remark_status.setText(LanguageController.getInstance().getMobileMsgByKey(AppConstant.condition));

        if (!TextUtils.isEmpty(equArrayModel.getStatus())) {
            holder.remark_status.setVisibility(View.VISIBLE);
            holder.remark_condition_txt.setVisibility(View.VISIBLE);
            holder.remark_status.setText(getStatusNameById(equArrayModel.getStatus()));
            setColor(holder.remark_status, holder.remark_status.getText().toString(), "");
        } else {
            holder.remark_status.setVisibility(View.GONE);
            holder.remark_condition_txt.setVisibility(View.GONE);
        }


        if (!TextUtils.isEmpty(equArrayModel.getRemark())) {
            holder.remark_notes.setVisibility(View.VISIBLE);
            holder.remark_notes.setText(equArrayModel.getRemark());
        } else {
            holder.remark_notes.setVisibility(View.GONE);
        }

        ArrayList<GetFileList_Res> attachmentsList = new ArrayList<>();
        if (equArrayModel.getAttachments() != null && equArrayModel.getAttachments().size() > 0)
            attachmentsList = equArrayModel.getAttachments();

        CustomLinearLayoutManager customLayoutManager = new CustomLinearLayoutManager(mContext, LinearLayoutManager.HORIZONTAL
                , false);
        holder.recyclerView.setLayoutManager(customLayoutManager);
        CompletionAdpterJobDteails jobCompletionAdpter = new CompletionAdpterJobDteails(attachmentsList, new SelectedImageView() {
            @Override
            public void sleecteAttch(int pos) {
                if (selectedpositionForAttchment != null) {
                    selectedpositionForAttchment.selectedAttchpos(pos, equiPos);
                }
            }
        });
        holder.recyclerView.setAdapter(jobCompletionAdpter);
    }

    private void updateAttchmentList(MyViewHolder holder, ArrayList<GetFileList_Res> getFileListres, final int equiPos) {
        ArrayList<GetFileList_Res> attachmentsList = new ArrayList<>();
        if (getFileListres != null && getFileListres.size() > 0)
            attachmentsList = getFileListres;

        CustomLinearLayoutManager customLayoutManager = new CustomLinearLayoutManager(mContext, LinearLayoutManager.HORIZONTAL
                , false);
        holder.recyclerView.setLayoutManager(customLayoutManager);
        CompletionAdpterJobDteails jobCompletionAdpter = new CompletionAdpterJobDteails(attachmentsList, new SelectedImageView() {
            @Override
            public void sleecteAttch(int pos) {
                if (selectedpositionForAttchment != null) {
                    selectedpositionForAttchment.selectedAttchpos(pos, equiPos);
                }
            }
        });
        holder.recyclerView.setAdapter(jobCompletionAdpter);
    }

    @Override
    public int getItemCount() {
        return this.list == null ? 0 : this.list.size();
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

    private void setColor(TextView view, String fulltext, String subtext) {
        try {
            view.setText(fulltext, TextView.BufferType.SPANNABLE);
            Spannable str = (Spannable) view.getText();
            int i = fulltext.indexOf(subtext);
            TextAppearanceSpan highlightSpan = new TextAppearanceSpan(null, Typeface.BOLD, -1, null, null);

            str.setSpan(highlightSpan, i + subtext.length(), fulltext.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public interface SelectedpositionForAttchment {
        void selectedAttchpos(int attchmentPos, int equipPos);
    }

    public interface SelectedImageView {
        void sleecteAttch(int pos);
    }

    public interface OnEquipmentSelection {
        void onEquipmentSelected(int position, EquArrayModel equipmentRes);
    }

    public interface OnEquipmentSelectionForDetails {
        void onEquipmentSelectedForDetails(EquArrayModel equipmentRes);
    }


    public class MyViewHolder extends RecyclerView.ViewHolder {
        CardView client_item_constraint;
        TextView tv_item_name, tv_address, condition;
        AppCompatImageView img_equipment;
        TextView equ_img_view;
        //  AppCompatImageView img_remark;
        AppCompatTextView tv_model, tv_serial, tv_model_label, tv_serial_label;//, tv_des;// tv_status, , tv_details ,, tv_remark
        // LinearLayout  ll_remark;//ll_details,
        TextView view_details, add_remark;
        TextView remark_txt, remark_condition_txt, remark_status, remark_notes, btnComplationView;
        View edit_remark_layout;
        RecyclerView recyclerView;


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
            tv_model_label = itemView.findViewById(R.id.tv_model_label);
            tv_serial_label = itemView.findViewById(R.id.tv_serial_label);
            view_details = itemView.findViewById(R.id.view_details);
            view_details.setText(LanguageController.getInstance().getMobileMsgByKey(AppConstant.view_details));
            add_remark = itemView.findViewById(R.id.add_remark);
            remark_txt = itemView.findViewById(R.id.remark_txt);
            remark_condition_txt = itemView.findViewById(R.id.remark_condition_txt);
            remark_status = itemView.findViewById(R.id.remark_status);
            remark_notes = itemView.findViewById(R.id.remark_notes);
            btnComplationView = itemView.findViewById(R.id.btnComplationView);
            btnComplationView.setText(LanguageController.getInstance().getMobileMsgByKey(AppConstant.edit));
            edit_remark_layout = itemView.findViewById(R.id.edit_remark_layout);
            recyclerView = itemView.findViewById(R.id.recyclerView);
            equ_img_view = itemView.findViewById(R.id.equ_img_view);

            add_remark.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    setRemarkActivity();
                }
            });


            btnComplationView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    setRemarkActivity();
                }
            });


        }

        void setRemarkActivity() {
            if (onEquipmentSelection != null) {
                onEquipmentSelection.onEquipmentSelected(getAdapterPosition(), list.get(getAdapterPosition()));
            } else {
                String strEqu = new Gson().toJson(list.get(getAdapterPosition()));
                mContext.startActivity(new Intent(mContext, JobEquRemarkRemarkActivity.class).putExtra("equipment", strEqu));
            }
        }
    }


}
