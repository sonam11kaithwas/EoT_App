package com.eot_app.nav_menu.admin_fw_chat_pkg.sonam_user_user_chat_pkg.usertouser_model;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.eot_app.R;

import java.util.List;

/**
 * Created by Sona-11 on 31/12/21.
 */
public class GrpUsrlistStatusAdpter
        extends BaseAdapter {
    private final List<GrpUsrlistStatus> fwList;
    private final Context context;

    public GrpUsrlistStatusAdpter(Context context, List<GrpUsrlistStatus> fwList) {
        this.context = context;
        this.fwList = fwList;
    }

    @Override
    public int getCount() {
        return fwList.size();
    }

    @Override
    public Object getItem(int i) {
        return null;
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup viewGroup) {
        return getCustomView(position, convertView, viewGroup);
    }

    private View getCustomView(final int position, View convertView, ViewGroup parent) {
        View row = LayoutInflater.from(parent.getContext()).inflate(R.layout.usr_online_offline_grp, parent, false);
        final TextView userNmImg = row.findViewById(R.id.userNmImg);
        final TextView usrNm = row.findViewById(R.id.usrNm);
        final ImageView batchIcon = row.findViewById(R.id.batchIcon);
        GrpUsrlistStatus memberModel = fwList.get(position);

        usrNm.setText(memberModel.getNm());
        userNmImg.setText(memberModel.getUsrIm());
        if (memberModel.getStatus().equals("1") || memberModel.getStatus().equals("3")) {
            batchIcon.setBackground(context.getResources().getDrawable(R.drawable.user_online_batch));
        } else {
            batchIcon.setBackground(context.getResources().getDrawable(R.drawable.user_offline_batch));
        }
        return row;
    }


    @Override
    public View getDropDownView(int position, View convertView, ViewGroup parent) {
        return getCustomView(position, convertView, parent);
    }
}
