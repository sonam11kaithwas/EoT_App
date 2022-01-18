package com.eot_app.nav_menu.jobs.job_complation;

import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.eot_app.R;
import com.eot_app.nav_menu.jobs.job_detail.documents.DialogUpdateDocuments;
import com.eot_app.nav_menu.jobs.job_detail.documents.doc_model.GetFileList_Res;
import com.eot_app.utility.AppConstant;
import com.eot_app.utility.App_preference;
import com.eot_app.utility.language_support.LanguageController;

import java.util.ArrayList;

public class JobCompletionAdpter extends RecyclerView.Adapter<RecyclerView.ViewHolder> implements Filterable {
    private final ArrayList<GetFileList_Res> suggestions = new ArrayList<>();
    private final FileDesc_Item_Selected fileDesc_item_selected;
    private final String jobId;
    private final JobCompletionActivity jobCompletionActivity;
    private final RemoveAttchment removeAttchment;
    private ArrayList<GetFileList_Res> getFileList_res = new ArrayList<>();
    private ArrayList<GetFileList_Res> tempFileList;
    Filter nameFilter = new Filter() {


        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            FilterResults results = new FilterResults();        // Holds the results of a filtering operation in values
            ArrayList<GetFileList_Res> FilteredArrList = new ArrayList<>();


            if (tempFileList == null) {
                tempFileList = new ArrayList<GetFileList_Res>(getFileList_res); // saves the original data in mOriginalValues
            }
            FilteredArrList.clear();
            if (constraint == null || constraint.length() == 0) {

                // set the Original result to return
                results.count = tempFileList.size();
                results.values = tempFileList;
            } else {
                constraint = constraint.toString().toLowerCase();

                for (GetFileList_Res fileList : getFileList_res) {

                    if (fileList.getAttachFileActualName().toLowerCase().startsWith(constraint.toString().toLowerCase())) {
                        FilteredArrList.add(fileList);
                    }
                }


                // set the Filtered result to return
                results.count = FilteredArrList.size();
                results.values = FilteredArrList;
            }
            return results;


        }

        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            getFileList_res = (ArrayList<GetFileList_Res>) results.values;
            notifyDataSetChanged();  // notifies the
        }
    };
    private Context context;

    public JobCompletionAdpter(FileDesc_Item_Selected fileDesc_item_selected, ArrayList<GetFileList_Res> getFileList_res,
                               JobCompletionActivity jobCompletionActivity, String jobId
            , RemoveAttchment removeAttchment) {
        this.getFileList_res = getFileList_res;
        this.fileDesc_item_selected = fileDesc_item_selected;
        tempFileList = new ArrayList<>();
        this.tempFileList = getFileList_res;
        this.jobCompletionActivity = jobCompletionActivity;
        this.jobId = jobId;
        this.removeAttchment = removeAttchment;
    }

    public void updateFileList(ArrayList<GetFileList_Res> getFileListres) {
//        this.getFileList_res = getFileListres;
//        notifyDataSetChanged();
        if (this.getFileList_res != null) this.getFileList_res.clear();
        this.getFileList_res.addAll(getFileListres);
        notifyDataSetChanged();
    }

    @Override
    public int getItemViewType(int position) {
        if (position == 0)
            return 1;
        else
            return 0;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        context = parent.getContext();
        View view = null;
        if (viewType == 1) {
            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.add_attachemnt_view, parent, false);
            return new JobCompletionAdpter.UploadViewHolder(view);
        } else
            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.jobcompletion_adpter, parent, false);
        return new JobCompletionAdpter.MyView_Holder(view);

    }

    @Override
    public void onBindViewHolder(@NonNull final RecyclerView.ViewHolder viewHolder, final int pos) {
        if (viewHolder instanceof MyView_Holder) {
            final int position = pos - 1;
            final MyView_Holder holder = (MyView_Holder) viewHolder;
            GetFileList_Res fileList = getFileList_res.get(position);

            final String ext = fileList.getImage_name().substring((fileList.getImage_name().lastIndexOf(".")) + 1);
            if (!ext.isEmpty()) {
                if (ext.equals("jpg") || ext.equals("jpeg") || ext.equals("png")) {
                    Glide.with(context).load(App_preference.getSharedprefInstance().getBaseURL() + fileList.getAttachThumnailFileName())
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
            holder.image_thumb_nail.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (getFileList_res.get(position).getType() != null && getFileList_res.get(position).getType().equals("2") || getFileList_res.get(position).getType().equals("6")) {
                        DialogUpdateDocuments
                                dialogUpdateDocuments = new DialogUpdateDocuments();

                        if (!TextUtils.isEmpty(ext) && ext.equals("jpg") || ext.equals("jpeg") || ext.equals("png"))
                            dialogUpdateDocuments.setIsFileImage(true);

                        dialogUpdateDocuments.setImgPath(
                                getFileList_res.get(position).getAttachmentId(),
                                getFileList_res.get(position).getAttachFileName(),
                                getFileList_res.get(position).getAttachFileActualName(),
                                getFileList_res.get(position).getDes(),
                                getFileList_res.get(position).getType(),
                                jobId);

                        dialogUpdateDocuments.setOnDocumentUpdate(new DialogUpdateDocuments.OnDocumentUpdate() {
                            @Override
                            public void onUpdateDes(String desc) {
                                if (desc != null)
                                    getFileList_res.get(position).setDes(desc);
                                if (jobCompletionActivity != null)
                                    jobCompletionActivity.setUpdatedDesc(desc);

                            }
                        });
                        dialogUpdateDocuments.show(((AppCompatActivity) context).getSupportFragmentManager(), "dialog");
                    } else
                        fileDesc_item_selected.OnItemClick_Document(getFileList_res.get(holder.getAdapterPosition()));
                }
            });


            holder.image_txt.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    removeAttchment.removeAttchment(getFileList_res.get(position).getAttachmentId());
                }
            });


        } else if (viewHolder instanceof UploadViewHolder) {
            UploadViewHolder holder = (UploadViewHolder) viewHolder;
            holder.add_attach_btn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (fileDesc_item_selected != null)
                        fileDesc_item_selected.openAttachmentDialog();
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        if (getFileList_res == null)
            return 1;
        else
            return getFileList_res.size() + 1;
    }

    @Override
    public Filter getFilter() {
        return nameFilter;
    }

    public interface FileDesc_Item_Selected {
        void OnItemClick_Document(GetFileList_Res getFileList_res);

        void openAttachmentDialog();
    }

    interface RemoveAttchment {
        void removeAttchment(String id);
    }

    class MyView_Holder extends RecyclerView.ViewHolder {
        LinearLayout layout_doc;
        ImageView image_thumb_nail;
        //        ImageView viewOne, viewTwo;
        ImageView image_txt;
        //  ImageView add_attach_btn;

        public MyView_Holder(View itemView) {
            super(itemView);
            layout_doc = itemView.findViewById(R.id.layout_doc);
            image_thumb_nail = itemView.findViewById(R.id.image_thumb_nail);

            image_txt = itemView.findViewById(R.id.image_txt);
            //   add_attach_btn = itemView.findViewById(R.id.add_attach_btn);
        }

    }

    class UploadViewHolder extends RecyclerView.ViewHolder {
        LinearLayout add_attach_btn;
        TextView upload_file;

        public UploadViewHolder(View itemView) {
            super(itemView);
            add_attach_btn = itemView.findViewById(R.id.add_attach_btn);
            upload_file = itemView.findViewById(R.id.upload_file);
            upload_file.setText(LanguageController.getInstance().getMobileMsgByKey(AppConstant.upld_fil));
        }
    }
}

