package com.eot_app.nav_menu.jobs.job_detail.customform;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.core.graphics.drawable.DrawableCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.eot_app.R;
import com.eot_app.nav_menu.jobs.job_controller.ChatController;
import com.eot_app.nav_menu.jobs.job_detail.MenuItemsModel;
import com.eot_app.utility.util_interfaces.MyListItemSelected;

import java.util.List;

/**
 * Created by ubuntu on 17/9/18.
 */

public class ButtomBarAdapter extends RecyclerView.Adapter<ButtomBarAdapter.MyViewHolder> {
    private final String jobId;
    List<MenuItemsModel> datalist;
    MyListItemSelected<MenuItemsModel> mListner;
    Context mContex;
    private TextView clientBatch;


    public ButtomBarAdapter(List<MenuItemsModel> more_item_list, MyListItemSelected<MenuItemsModel> callback, String jobId) {
        this.datalist = more_item_list;
        this.mListner = callback;
        this.jobId = jobId;
    }

    public void updateSelectedMenu(int id) {
        if (datalist != null) {
            for (MenuItemsModel model : datalist)
                model.setSelected(model.getMenu_item_id() == id);
            notifyDataSetChanged();
        }
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.buttombaricon, parent, false);
        if (mContex == null) mContex = parent.getContext();
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        holder.im.setImageResource(datalist.get(position).getMenu_icon());
        holder.frmName.setText(datalist.get(position).getMenu_title());

        if (datalist.get(position).isSelected()) {
            DrawableCompat.setTint(holder.im.getDrawable(), ContextCompat.getColor(mContex, R.color.colorPrimary));
            holder.frmName.setTextColor(mContex.getResources().getColor(R.color.colorPrimary));
        } else {
            DrawableCompat.setTint(holder.im.getDrawable(), ContextCompat.getColor(mContex, R.color.color_gray));
            holder.frmName.setTextColor(mContex.getResources().getColor(R.color.color_gray));
        }

        clientBatch.setText("");
        clientBatch.setVisibility(View.GONE);
        /*** add batch count for client chat ***/
        if (datalist.get(position).getMenu_item_id() == 110) {
            int batchCount = ChatController.getInstance().getClientChatBatchCount(jobId);
            if (batchCount > 0) {
                clientBatch.setVisibility(View.VISIBLE);
                clientBatch.setText(batchCount + "");
            } else {
                clientBatch.setText("");
                clientBatch.setVisibility(View.GONE);
            }
        } else if (datalist.get(position).getMenu_item_id() == 104) {
            int batchCount = ChatController.getInstance().getbatchCount(jobId);
            if (batchCount > 0) {
                clientBatch.setVisibility(View.VISIBLE);
                clientBatch.setText(batchCount + "");
            } else {
                clientBatch.setText("");
                clientBatch.setVisibility(View.GONE);
            }
        } else {
            clientBatch.setText("");
            clientBatch.setVisibility(View.GONE);
        }
    }

    public void setCount(int count) {
        /*** add batch count for client chat ***/
        // notifyDataSetChanged();

//        for (MenuItemsModel model : datalist) {
//            if (model.getMenu_item_id() == 110) {
//                int batchCount = ChatController.getInstance().getClientChatBatchCount(jobId);
//                if (batchCount > 0) {
//                    clientBatch.setVisibility(View.VISIBLE);
//                    clientBatch.setText(batchCount + "");
//                } else {
//                    clientBatch.setText("");
//                    clientBatch.setVisibility(View.GONE);
//                }
//            } else if (model.getMenu_item_id() == 104) {
//                int batchCount = ChatController.getInstance().getbatchCount(jobId);
//                if (batchCount > 0) {
//                    clientBatch.setVisibility(View.VISIBLE);
//                    clientBatch.setText(batchCount + "");
//                } else {
//                    clientBatch.setText("");
//                    clientBatch.setVisibility(View.GONE);
//                }
//            }
//        }
    }

    @Override
    public int getItemCount() {
        return datalist.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        ImageView im;
        TextView frmName;

        public MyViewHolder(View itemView) {
            super(itemView);
            im = itemView.findViewById(R.id.iconadd);
            frmName = itemView.findViewById(R.id.frmName);
            itemView.setOnClickListener(this);

            clientBatch = itemView.findViewById(R.id.clientBatch);
        }

        @Override
        public void onClick(View v) {
            mListner.onMyListitemSeleted(datalist.get(getAdapterPosition()));
        }
    }
}
