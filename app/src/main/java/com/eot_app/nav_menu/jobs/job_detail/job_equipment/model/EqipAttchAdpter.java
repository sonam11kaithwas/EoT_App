package com.eot_app.nav_menu.jobs.job_detail.job_equipment.model;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.eot_app.R;
import com.eot_app.nav_menu.jobs.job_detail.documents.doc_model.GetFileList_Res;
import com.eot_app.utility.App_preference;

import java.util.List;

public class EqipAttchAdpter extends RecyclerView.Adapter<EqipAttchAdpter.MyViewHolder> {
    private final List<GetFileList_Res> jobdata;
    private final Context context;


    public EqipAttchAdpter(List<GetFileList_Res> arrayList, Context context) {
        this.jobdata = arrayList;
        this.context = context;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.equ_attch_adp_lay, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(EqipAttchAdpter.MyViewHolder holder, int position) {
        String ext = "";
        try {
            ext = jobdata.get(position).getImage_name().substring((jobdata.get(position).getImage_name().lastIndexOf(".")) + 1);
        } catch (Exception e) {
            e.printStackTrace();
        }
        try {
            if (!ext.isEmpty()) {
                if (ext.equals("jpg") || ext.equals("jpeg") || ext.equals("png")) {
                    Glide.with(context).load(App_preference.getSharedprefInstance().getBaseURL() + jobdata.get(position).getAttachThumnailFileName())
                            .placeholder(R.drawable.picture).into(holder.image_thumb_nail);
                    holder.image_thumb_nail.setScaleType(ImageView.ScaleType.FIT_XY);
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
        } catch (Exception ex) {
            ex.getMessage();
        }

    }

    @Override
    public int getItemCount() {
        return jobdata.size();
    }


    public class MyViewHolder extends RecyclerView.ViewHolder {
        ImageView image_thumb_nail;

        public MyViewHolder(View itemView) {
            super(itemView);
            image_thumb_nail = itemView.findViewById(R.id.image_thumb_nail);
        }
    }
}
