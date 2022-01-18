package com.eot_app.nav_menu.client.clientlist.client_detail.contact;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.SpinnerAdapter;

import androidx.annotation.NonNull;

import com.eot_app.R;
import com.eot_app.nav_menu.client.clientlist.client_detail.contact.edit_contact.editmodel.SiteId;
import com.eot_app.nav_menu.client.clientlist.client_detail.site.sitelist.Site_model;
import com.eot_app.utility.DropdownListBean;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Created by Sonam-11 on 10/7/20.
 */
public class SiteListAdpter extends BaseAdapter implements SpinnerAdapter {
    private final Context mContext;
    private final List<Site_model> listitem;
    private final Set<String> title_name;
    private final Set<String> title_ids;
    private final boolean[] is_pos_checked;
    SiteListAdpter.OnItemClickListener onItemClickListener;
    private List<SiteId> siteIdList = new ArrayList<>();


    public SiteListAdpter(Context context, int resource, List<Site_model> list, SiteListAdpter.OnItemClickListener
            onItemClickListener) {
        this.mContext = context;
        this.listitem = list;
        this.onItemClickListener = onItemClickListener;
        this.is_pos_checked = new boolean[listitem.size()];
        this.title_name = new HashSet<>();
        this.title_ids = new HashSet<>();
    }


    public SiteListAdpter(Context context, int resource, List<Site_model> list, SiteListAdpter.OnItemClickListener
            onItemClickListener, List<SiteId> siteIdList) {
        this.mContext = context;
        this.listitem = list;
        this.onItemClickListener = onItemClickListener;
        this.is_pos_checked = new boolean[listitem.size()];
        this.title_name = new HashSet<>();
        this.title_ids = new HashSet<>();
        this.siteIdList = siteIdList;
    }


    @Override
    public View getDropDownView(int position, View convertView,
                                ViewGroup parent) {
        return getCustomView(position, convertView, parent);
    }

    @Override
    public int getCount() {
        return listitem.size();
    }

    @Override
    public Object getItem(int i) {
        return listitem.get(i);
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @NonNull
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        return getCustomView(position, convertView, parent);
    }

    public View getCustomView(final int position, View convertView, ViewGroup parent) {
        final SiteListAdpter.ViewHolder holder;

        final Site_model object = listitem.get(position);
        if (convertView == null) {
            LayoutInflater layoutInflator = LayoutInflater.from(mContext);
            convertView = layoutInflator.inflate(R.layout.spn_list_item, parent, false);
            holder = new SiteListAdpter.ViewHolder();
            holder.mCheckBox = convertView.findViewById(R.id.checkbox_invoice);
            convertView.setTag(holder);
        } else {
            holder = (SiteListAdpter.ViewHolder) convertView.getTag();


            /** Set default services edite quotes */
            if (siteIdList.size() > 0) {
                for (SiteId jtId : siteIdList) {
                    if (jtId.getSiteId().equals(listitem.get(position).getSiteId()))
                        is_pos_checked[position] = true;
                    title_name.add(jtId.getSnm());
                    title_ids.add(jtId.getSiteId());
                }
            }
        }

//


        holder.mCheckBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                boolean checked = holder.mCheckBox.isChecked();
                is_pos_checked[position] = checked;
//                bind(object);
                if (checked) {
                    title_name.add(object.getSnm());
                    title_ids.add(object.getSiteId());
                } else if (title_name.contains(object.getSnm())) {
                    title_name.remove(object.getSnm());
                    title_ids.remove(object.getSiteId());
                }
                onItemClickListener.onItemClick(title_name, title_ids);
//                }
            }
        });
        holder.mCheckBox.setChecked(is_pos_checked[position]);
        holder.mCheckBox.setText(((DropdownListBean) object).getName());

        return convertView;
    }


    public interface OnItemClickListener {
        void onItemClick(Set<String> data, Set<String> title_ids);
    }

    private class ViewHolder {
        private CheckBox mCheckBox;
    }
}


