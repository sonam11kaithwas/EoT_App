package com.eot_app.utility.util_interfaces;

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
import com.eot_app.nav_menu.jobs.job_db.JtId;
import com.eot_app.utility.DropdownListBean;
import com.eot_app.utility.settings.setting_db.JobTitle;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Created by ubuntu on 15/8/18.
 */

public class MyAdapter2 extends BaseAdapter implements SpinnerAdapter {//, ThemedSpinnerAdapter
    private final Context mContext;
    private final List<JobTitle> listitem;
    private final Set<String> title_name;
    private final Set<String> title_ids;
    private final boolean[] is_pos_checked;
    OnItemClickListener onItemClickListener;
    private List<JtId> jtIdList = new ArrayList<>();
    //private  ThemedSpinnerAdapter.Helper mDropDownHelper;

   /* @Override
    public void setDropDownViewTheme(@Nullable Resources.Theme theme) {

        mDropDownHelper.setDropDownViewTheme(theme);
    }

    @Nullable
    @Override
    public Resources.Theme getDropDownViewTheme() {
        return mDropDownHelper.getDropDownViewTheme();
    }*/

    public MyAdapter2(Context context, int resource, List<JobTitle> list, OnItemClickListener onItemClickListener) {
        this.mContext = context;
        this.listitem = list;
        this.onItemClickListener = onItemClickListener;
        this.is_pos_checked = new boolean[listitem.size()];
        this.title_name = new HashSet<>();
        this.title_ids = new HashSet<>();
        //mDropDownHelper = new ThemedSpinnerAdapter.Helper(context);
    }


    public MyAdapter2(Context context, int resource, List<JobTitle> list, OnItemClickListener onItemClickListener, List<JtId> jtIdList) {
        this.mContext = context;
        this.listitem = list;
        this.onItemClickListener = onItemClickListener;
        this.is_pos_checked = new boolean[listitem.size()];
        this.title_name = new HashSet<>();
        this.title_ids = new HashSet<>();
        this.jtIdList = jtIdList;
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
        final ViewHolder holder;

        final JobTitle object = listitem.get(position);
        if (convertView == null) {
            LayoutInflater layoutInflator = LayoutInflater.from(mContext);
            convertView = layoutInflator.inflate(R.layout.spn_list_item, parent, false);
            holder = new ViewHolder();
            holder.mCheckBox = convertView.findViewById(R.id.checkbox_invoice);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        /** Set default services edite quotes */
        if (jtIdList.size() > 0) {
            for (JtId jtId : jtIdList) {
                if (jtId.getJtId().equals(listitem.get(position).getJtId()))
                    is_pos_checked[position] = true;
                title_name.add(jtId.getTitle());
                title_ids.add(jtId.getJtId());
            }
        }


        holder.mCheckBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                boolean checked = holder.mCheckBox.isChecked();
                is_pos_checked[position] = checked;
//                bind(object);
                if (checked) {
                    title_name.add(object.getName());
                    title_ids.add(object.getJtId());
                } else if (title_name.contains(object.getName())) {
                    title_name.remove(object.getName());
                    title_ids.remove(object.getJtId());
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

