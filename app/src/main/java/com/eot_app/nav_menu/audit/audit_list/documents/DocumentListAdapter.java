package com.eot_app.nav_menu.audit.audit_list.documents;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.eot_app.R;
import com.eot_app.nav_menu.audit.audit_list.documents.doc_model.GetFileList_Res;
import com.eot_app.utility.App_preference;

import java.util.ArrayList;

/**
 * Created by ubuntu on 9/10/18.
 */

public class DocumentListAdapter extends RecyclerView.Adapter<DocumentListAdapter.MyView_Holder> implements Filterable {
    private final ArrayList<GetFileList_Res> suggestions = new ArrayList<>();
    private final FileDesc_Item_Selected fileDesc_item_selected;
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


    public DocumentListAdapter(FileDesc_Item_Selected fileDesc_item_selected, ArrayList<GetFileList_Res> getFileList_res) {
        this.getFileList_res = getFileList_res;
        this.fileDesc_item_selected = fileDesc_item_selected;
        tempFileList = new ArrayList<>();
        this.tempFileList = getFileList_res;
    }

    public void updateFileList(ArrayList<GetFileList_Res> getFileListres) {
        // getFileList_res.clear();
        getFileList_res.addAll(getFileListres);
        notifyDataSetChanged();
    }


    @NonNull
    @Override
    public MyView_Holder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        context = parent.getContext();
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.documentlist_adapter, parent, false);
        return new MyView_Holder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final MyView_Holder holder, int position) {
        GetFileList_Res fileList = getFileList_res.get(position);

        holder.file_name.setText(fileList.getAttachFileActualName());
        String ext = "";
        try {
            ext = fileList.getImage_name().substring((fileList.getImage_name().lastIndexOf(".")) + 1);
        } catch (Exception e) {
            e.printStackTrace();
        }
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
        holder.layout_doc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (fileDesc_item_selected != null)
                    fileDesc_item_selected.OnItemClick_Document(getFileList_res.get(holder.getAdapterPosition()));
            }
        });
    }

    @Override
    public int getItemCount() {
        return getFileList_res.size();
    }

    public void clearAllItems() {
        if (getFileList_res != null) {
            getFileList_res.clear();
            notifyDataSetChanged();
        }
    }

    @Override
    public Filter getFilter() {
        return nameFilter;
    }

    public interface FileDesc_Item_Selected {
        void OnItemClick_Document(GetFileList_Res getFileList_res);
    }

    class MyView_Holder extends RecyclerView.ViewHolder {
        TextView file_name;
        LinearLayout layout_doc;
        ImageView image_thumb_nail;

        public MyView_Holder(View itemView) {
            super(itemView);
            file_name = itemView.findViewById(R.id.tv_file_name);
            layout_doc = itemView.findViewById(R.id.layout_doc);
            image_thumb_nail = itemView.findViewById(R.id.image_thumb_nail);
        }
    }

}
