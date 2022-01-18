package com.eot_app.nav_menu.jobs.job_filter;

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
import com.eot_app.utility.DropdownListBean;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class FilterListAdapter<T> extends BaseAdapter implements SpinnerAdapter {
    private final Set<DropdownListBean> listBeans;
    private final Context mContext;
    private final Set<String> name_list;
    private final boolean[] is_pos_checked;
    private final Set<String> list_ids;
    public List<T> listitem;
    OnItemClickListener onItemClickListener;
    int lastPosition = -1;

    public FilterListAdapter(Context context, List<T> list, OnItemClickListener onItemClickListener) {
        this.mContext = context;
        this.listitem = list;
        this.onItemClickListener = onItemClickListener;
        this.is_pos_checked = new boolean[listitem.size()];
        this.name_list = new HashSet<>();
        this.listBeans = new HashSet<>();
        list_ids = new HashSet<>();
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
        final T t = listitem.get(position);
        final ViewHolder holder;
        if (convertView == null) {
            LayoutInflater layoutInflator = LayoutInflater.from(mContext);
            convertView = layoutInflator.inflate(R.layout.spn_list_item, parent, false);
            holder = new ViewHolder();
            holder.mCheckBox = convertView.findViewById(R.id.checkbox_invoice);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }


        holder.mCheckBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                boolean checked = holder.mCheckBox.isChecked();
                is_pos_checked[position] = checked;

                if (!list_ids.equals(((DropdownListBean) t).getKey())) {
                    if (checked) {
                        name_list.add(((DropdownListBean) t).getName());
                        list_ids.add(((DropdownListBean) t).getKey());
                        addSelectedItem(t);
                    } else {
                        if (list_ids.contains(((DropdownListBean) t).getKey())) {
                            name_list.remove(((DropdownListBean) t).getName());
                            list_ids.remove(((DropdownListBean) t).getKey());
                            removeUnSelectedItem(t);
                        }
                    }
                }

                onItemClickListener.onItemClick(name_list, listBeans);

            }
        });
        holder.mCheckBox.setChecked(is_pos_checked[position]);
        holder.mCheckBox.setText(((DropdownListBean) t).getName());

        return convertView;
    }

    private void removeUnSelectedItem(T t) {
        name_list.remove(((DropdownListBean) t).getName());
        list_ids.remove(((DropdownListBean) t).getKey());
        listBeans.remove(t);
    }

    private void addSelectedItem(T t) {
        name_list.add(((DropdownListBean) t).getName());
        list_ids.add(((DropdownListBean) t).getKey());
        listBeans.add(((DropdownListBean) t));
    }


    public interface OnItemClickListener {
        void onItemClick(Set<String> data, Set<DropdownListBean> t);
    }

    private class ViewHolder {
        private CheckBox mCheckBox;
    }
}




