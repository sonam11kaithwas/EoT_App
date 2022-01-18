package com.eot_app.nav_menu.jobs.job_detail.detail;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.eot_app.R;
import com.eot_app.nav_menu.jobs.job_detail.documents.doc_model.GetFileList_Res;
import com.eot_app.nav_menu.jobs.job_detail.job_equipment.JobEquipmentAdapter;
import com.eot_app.utility.App_preference;

import java.text.DecimalFormat;
import java.util.ArrayList;

public class CompletionAdpterJobDteails extends RecyclerView.Adapter<CompletionAdpterJobDteails.ViewHolder> {

    ArrayList<GetFileList_Res> getFileListres = new ArrayList<>();
    boolean EQUIPMENTATTCHMENT = false;
    JobEquipmentAdapter.SelectedImageView selectedImageView;
    private CallBAckForAttchemnt callBAckForAttchemnt;
    private Context context;
    private int counter = 0;

    /**
     * Initialize the dataset of the Adapter.
     *
     * @param getFileListres String[] containing the data to populate views to be used
     *                       by RecyclerView.
     */
    public CompletionAdpterJobDteails(ArrayList<GetFileList_Res> getFileListres, CallBAckForAttchemnt callBAckForAttchemnt) {
        this.getFileListres = getFileListres;
        this.callBAckForAttchemnt = callBAckForAttchemnt;
        this.EQUIPMENTATTCHMENT = false;
    }


    public CompletionAdpterJobDteails(ArrayList<GetFileList_Res> getFileListres, JobEquipmentAdapter.SelectedImageView selectedImageView) {
        this.getFileListres = getFileListres;
        this.EQUIPMENTATTCHMENT = true;
        this.selectedImageView = selectedImageView;
    }

    public void updateFileList(ArrayList<GetFileList_Res> getFileListres) {
        if (this.getFileListres != null) this.getFileListres.clear();
        this.getFileListres.addAll(getFileListres);
        notifyDataSetChanged();
    }

    // Create new views (invoked by the layout manager)
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        // Create a new view, which defines the UI of the list item
        context = viewGroup.getContext();
        View view = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.text_row_item, viewGroup, false);

        return new ViewHolder(view);
    }

    public int getCounter() {
        return counter;
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(ViewHolder viewHolder, final int position) {

        // Get element from your dataset at this position and replace the
        // contents of the view with that element

        GetFileList_Res fileList = getFileListres.get(position);
        counter = +1;
        final String ext = fileList.getImage_name().substring((fileList.getImage_name().lastIndexOf(".")) + 1);
        if (!ext.isEmpty()) {
            if (ext.equals("jpg") || ext.equals("jpeg") || ext.equals("png")) {
                Glide.with(context).load(App_preference.getSharedprefInstance().getBaseURL() + fileList.getAttachThumnailFileName())
                        .placeholder(R.drawable.picture).into(viewHolder.image_thumb_nail);
                viewHolder.image_thumb_nail.setScaleType(ImageView.ScaleType.FIT_XY);
            } else if (ext.equals("doc") || ext.equals("docx")) {
                viewHolder.image_thumb_nail.setImageResource(R.drawable.word);
                viewHolder.image_thumb_nail.setScaleType(ImageView.ScaleType.CENTER_INSIDE);
            } else if (ext.equals("pdf")) {
                viewHolder.image_thumb_nail.setImageResource(R.drawable.pdf);
                viewHolder.image_thumb_nail.setScaleType(ImageView.ScaleType.CENTER_INSIDE);

            } else if (ext.equals("xlsx") || ext.equals("xls")) {
                viewHolder.image_thumb_nail.setImageResource(R.drawable.excel);
                viewHolder.image_thumb_nail.setScaleType(ImageView.ScaleType.CENTER_INSIDE);

            } else if (ext.equals("csv")) {
                viewHolder.image_thumb_nail.setImageResource(R.drawable.csv);
                viewHolder.image_thumb_nail.setScaleType(ImageView.ScaleType.CENTER_INSIDE);
            } else {
                viewHolder.image_thumb_nail.setImageResource(R.drawable.doc);
                viewHolder.image_thumb_nail.setScaleType(ImageView.ScaleType.CENTER_INSIDE);
            }
            if (position != 0 && position != 1 && getFileListres.size() > 3) {
                viewHolder.myImageViewText.setVisibility(View.VISIBLE);
                try {
                    int pos = (getFileListres.size() - (position + 1));
                    DecimalFormat formatter = new DecimalFormat("00");
                    String s = (formatter.format(Integer.parseInt(pos + "")));
                    viewHolder.myImageViewText.setText(s + "+");
                } catch (Exception ex) {
                    viewHolder.myImageViewText.setText((getFileListres.size() - (position + 1)) + "+");
                }
            } else {
                viewHolder.myImageViewText.setVisibility(View.GONE);
            }

            viewHolder.layout_doc.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Log.e("", "");
                    try {
                        if (!EQUIPMENTATTCHMENT && callBAckForAttchemnt != null)
                            callBAckForAttchemnt.getAttchment();
                        else if (EQUIPMENTATTCHMENT && selectedImageView != null) {
                            selectedImageView.sleecteAttch(position);
                        }
                    } catch (Exception e) {
                        e.getMessage();
                    }

                }
            });
        }
    }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return getFileListres.size();
    }

    public interface CallBAckForAttchemnt {
        void getAttchment();
    }

    /**
     * Provide a reference to the type of views that you are using
     * (custom ViewHolder).
     */
    public static class ViewHolder extends RecyclerView.ViewHolder {
        private final ImageView image_thumb_nail;
        private final TextView myImageViewText;
        private final RelativeLayout layout_doc;

        public ViewHolder(View view) {
            super(view);
            // Define click listener for the ViewHolder's View

            image_thumb_nail = view.findViewById(R.id.image_thumb_nail);
            myImageViewText = view.findViewById(R.id.myImageViewText);
            layout_doc = view.findViewById(R.id.layout_doc);
        }

    }

}

