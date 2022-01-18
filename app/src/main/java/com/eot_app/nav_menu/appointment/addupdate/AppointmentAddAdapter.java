package com.eot_app.nav_menu.appointment.addupdate;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.eot_app.R;
import com.eot_app.nav_menu.appointment.details.AppointmentAttachment;
import com.eot_app.utility.App_preference;

import java.io.File;
import java.util.List;

public class AppointmentAddAdapter extends RecyclerView.Adapter<AppointmentAddAdapter.MyViewHolder> {
    Context context;
    private boolean showDeleteOption;
    private List<AppointmentAttachment> list;

    public AppointmentAddAdapter(Context context) {
        this.context = context;
    }

    public void setList(List<AppointmentAttachment> list) {
        if (list != null) {
            this.list = list;
            notifyDataSetChanged();
        }
    }

    public void addNewAttachement(AppointmentAttachment at) {
        if (at != null && list != null) {
            try {
                list.add(0, at);
                notifyDataSetChanged();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public void addNewAttachement(List<AppointmentAttachment> at) {
        if (at != null && list != null) {
            try {
                list.addAll(at);
                notifyDataSetChanged();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public void setShowDeleteOption(boolean showDeleteOption) {
        this.showDeleteOption = showDeleteOption;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {

        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_attachment_with_delete_option, viewGroup, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        if (showDeleteOption)
            holder.img_delete.setVisibility(View.VISIBLE);
        else holder.img_delete.setVisibility(View.GONE);

        final AppointmentAttachment fileList = list.get(position);
        final File file = convertToFile(fileList.getAttachFileActualName());
     /*   if (fileList.getType() == 1) {
            if (file != null)
                holder.file_name.setText(file.getName());
        } else
            holder.file_name.setText(fileList.getAttachFileActualName());*/
        String ext = "";
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
                                .placeholder(R.drawable.picture).into(holder.img_doc);
                    }

                } else {
                    Glide.with(context).load(App_preference.getSharedprefInstance().getBaseURL() + fileList.getAttachThumnailFileName())
                            .placeholder(R.drawable.picture).into(holder.img_doc);
                }
                holder.img_doc.setScaleType(ImageView.ScaleType.FIT_XY);
            } else if (ext.equals("doc") || ext.equals("docx")) {
                holder.img_doc.setImageResource(R.drawable.word);
                holder.img_doc.setScaleType(ImageView.ScaleType.CENTER_INSIDE);

            } else if (ext.equals("pdf")) {
                holder.img_doc.setImageResource(R.drawable.pdf);
                holder.img_doc.setScaleType(ImageView.ScaleType.CENTER_INSIDE);

            } else if (ext.equals("xlsx") || ext.equals("xls")) {
                holder.img_doc.setImageResource(R.drawable.excel);
                holder.img_doc.setScaleType(ImageView.ScaleType.CENTER_INSIDE);

            } else if (ext.equals("csv")) {
                holder.img_doc.setImageResource(R.drawable.csv);
                holder.img_doc.setScaleType(ImageView.ScaleType.CENTER_INSIDE);
            } else {
                holder.img_doc.setImageResource(R.drawable.doc);
                holder.img_doc.setScaleType(ImageView.ScaleType.CENTER_INSIDE);
            }
        }


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

    public class MyViewHolder extends RecyclerView.ViewHolder {
        AppCompatImageView img_delete, img_doc;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            img_delete = itemView.findViewById(R.id.img_delete);
            img_doc = itemView.findViewById(R.id.img_doc);
        }
    }
}
