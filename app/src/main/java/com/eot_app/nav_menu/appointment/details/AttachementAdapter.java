package com.eot_app.nav_menu.appointment.details;

import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatCheckBox;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.eot_app.R;
import com.eot_app.nav_menu.jobs.job_detail.documents.DialogUpdateDocuments;
import com.eot_app.utility.App_preference;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class AttachementAdapter extends RecyclerView.Adapter<AttachementAdapter.MyViewHolder> {
    Context context;
    OnItemSelection onItemSelection;
    private List<AppointmentAttachment> list;
    private boolean isFromClientHistory = false;

    public AttachementAdapter(Context context) {
        this.context = context;
    }

    public void setFromClientHistory(boolean fromClientHistory) {
        isFromClientHistory = fromClientHistory;
    }

    public void setOnItemSelection(OnItemSelection onItemSelection) {
        this.onItemSelection = onItemSelection;
    }

    public void setSingleAttachment(List<AppointmentAttachment> list) {
        try {
            if (this.list == null) this.list = new ArrayList<>();
            this.list.addAll(list);
            notifyDataSetChanged();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public List<AppointmentAttachment> getList() {
        return list;
    }

    public void setList(List<AppointmentAttachment> list) {
        this.list = list;
        notifyDataSetChanged();
    }

  /*  public void setLocalAttachment(List<AppointmentAttachment> list) {
        try {
            if (this.list == null) this.list = new ArrayList<>();
            this.list.addAll(list);
            notifyDataSetChanged();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }*/

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.item_appointment_attachement, viewGroup, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final MyViewHolder holder, final int position) {
        String ext = "";

        final AppointmentAttachment fileList = list.get(position);
        final File file = convertToFile(fileList.getAttachFileActualName());
        if (fileList.getType() == 1) {
            if (file != null)
                holder.file_name.setText(file.getName());
        } else
            holder.file_name.setText(fileList.getAttachFileActualName());
        try {
            ext = fileList.getAttachFileActualName().substring((fileList.getAttachFileActualName().lastIndexOf(".")) + 1);
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (!ext.isEmpty()) {
            if (ext.equals("jpg") || ext.equals("jpeg") || ext.equals("png")) {
                if (fileList.getType() == 1) {
                    if (file != null && file.exists()) {
                        Glide.with(context).load(file.getPath())
                                .placeholder(R.drawable.picture).into(holder.image_thumb_nail);
                    }

                } else {
                    Glide.with(context).load(App_preference.getSharedprefInstance().getBaseURL() + fileList.getAttachThumnailFileName())
                            .placeholder(R.drawable.picture).into(holder.image_thumb_nail);
                }
                holder.image_thumb_nail.setScaleType(ImageView.ScaleType.CENTER_CROP);
            } else if (ext.equals("doc") || ext.equals("docx")) {
                holder.image_thumb_nail.setImageResource(R.drawable.word);
                holder.image_thumb_nail.setScaleType(ImageView.ScaleType.CENTER_INSIDE);

            } else if (ext.equals("pdf")) {
                holder.image_thumb_nail.setImageResource(R.drawable.pdf);
                holder.image_thumb_nail.setScaleType(ImageView.ScaleType.CENTER_INSIDE);

            } else if (ext.equals("xlsx") || ext.equals("xls")) {
                holder.image_thumb_nail.setImageResource(R.drawable.excel);
                holder.image_thumb_nail.setScaleType(ImageView.ScaleType.CENTER_INSIDE);

            } else if (ext.equals("csv")) {
                holder.image_thumb_nail.setImageResource(R.drawable.csv);
                holder.image_thumb_nail.setScaleType(ImageView.ScaleType.CENTER_INSIDE);
            } else {
                holder.image_thumb_nail.setImageResource(R.drawable.doc);
                holder.image_thumb_nail.setScaleType(ImageView.ScaleType.CENTER_INSIDE);
            }
        }

        holder.layout_doc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                DialogUpdateDocuments
                        dialogUpdateDocuments = new DialogUpdateDocuments();
                if (isFromClientHistory)
                    dialogUpdateDocuments.setFromClientHistory(isFromClientHistory);
                else
                    dialogUpdateDocuments.setFromClientHistory(isFromClientHistory);
                try {
                    String ext = list.get(position).getAttachFileActualName().substring((list.get(position).getAttachFileActualName().lastIndexOf(".")) + 1);
                    if (!TextUtils.isEmpty(ext) && ext.equals("jpg") || ext.equals("jpeg") || ext.equals("png"))
                        dialogUpdateDocuments.setIsFileImage(true);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                dialogUpdateDocuments.setImgPath(
                        list.get(position).getAttachmentId(),
                        list.get(position).getAttachFileName(),
                        list.get(position).getAttachFileActualName(),
                        list.get(position).getDes(), "", "");

                dialogUpdateDocuments.setOnDocumentUpdate(new DialogUpdateDocuments.OnDocumentUpdate() {
                    @Override
                    public void onUpdateDes(String desc) {
                        if (desc != null)
                            list.get(position).setDes(desc);
                    }
                });
                dialogUpdateDocuments.show(((AppCompatActivity) context).getSupportFragmentManager(), "dialog");
            }
        });

        if (isFromClientHistory)
            holder.checkbox.setVisibility(View.GONE);
        else
            holder.checkbox.setVisibility(View.VISIBLE);

        holder.checkbox.setChecked(fileList.isSelected());

        holder.checkbox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isFileImage(list.get(position).getAttachFileActualName())) {
                    list.get(position).setSelected(!list.get(position).isSelected());
                    holder.checkbox.setChecked(list.get(position).isSelected());
                    if (onItemSelection != null) {
                        onItemSelection.showExportOption(isSelected());
                    }
                } else {
                    holder.checkbox.setChecked(false);
                    if (onItemSelection != null) onItemSelection.showDocFormatMSG();
                }
            }
        });
      /*  holder.checkbox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isFileImage(list.get(position).getAttachFileActualName())) {
                    list.get(position).setSelected(isChecked);
                    if (onItemSelection != null) {
                        if (isSelected())
                            onItemSelection.showExportOption(true);
                        else onItemSelection.showExportOption(false);
                    }
                } else {

                    if (onItemSelection != null) onItemSelection.showDocFormatMSG();
                }
            }
        });*/

    }

    @Override
    public int getItemCount() {
        return list == null ? 0 : list.size();
    }

    private File convertToFile(String path) {
        File file = null;
        if (path != null) {
            try {
                file = new File(path);
                return file;
            } catch (Exception ex) {

            }
        }
        return file;
    }

    private boolean isSelected() {
        if (getList() == null || getList().size() == 0) return false;

        boolean isAllSelected = true;
        boolean isSelected = false;

        for (AppointmentAttachment at : getList())
            if (isFileImage(at.getAttachFileActualName()))
                if (at.isSelected()) {
                    isSelected = true;
                } else isAllSelected = false;

        if (onItemSelection != null) onItemSelection.selectAllOption(isAllSelected);

        return isSelected;

    }

    public void selectAllFiles(boolean b) {
        if (getList() != null && getList().size() > 0)
            for (AppointmentAttachment at : getList()) {
                if (isFileImage(at.getAttachFileActualName()))
                    at.setSelected(b);
            }
        notifyDataSetChanged();
    }

    private boolean isFileImage(String fileName) {
        String ext = null;
        boolean fileFormatImage = false;
        try {
            ext = fileName.substring((fileName.lastIndexOf(".")) + 1);
            if (!ext.isEmpty())
                if (ext.equalsIgnoreCase("jpg") || ext.equalsIgnoreCase("jpeg") || ext.equalsIgnoreCase("png")) {
                    fileFormatImage = true;
                }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return fileFormatImage;
    }

    public interface OnItemSelection {
        void showExportOption(boolean b);

        void selectAllOption(boolean b);

        void showDocFormatMSG();

    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        TextView file_name;
        LinearLayout layout_doc;
        ImageView image_thumb_nail;
        AppCompatCheckBox checkbox;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            file_name = itemView.findViewById(R.id.tv_file_name);
            layout_doc = itemView.findViewById(R.id.layout_doc);
            image_thumb_nail = itemView.findViewById(R.id.image_thumb_nail);
            checkbox = itemView.findViewById(R.id.checkbox);
        }
    }
}
