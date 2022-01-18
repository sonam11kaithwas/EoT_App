package com.eot_app.utility.language_support;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.eot_app.R;
import com.eot_app.home_screens.MainActivityView;

import java.util.List;

public class LanguageListAdapter extends RecyclerView.Adapter<LanguageListAdapter.LangViewHolder> {
    private final MainActivityView mListener;
    private final List<Language_Model> lan_list;

    public LanguageListAdapter(MainActivityView mListener, List<Language_Model> lan_list) {
        this.mListener = mListener;
        this.lan_list = lan_list;
    }

    @NonNull
    @Override
    public LangViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int i) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.language_item_view, parent, false);
        return new LangViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull LangViewHolder viewHolder, int i) {
        viewHolder.tv_lan_item.setText(lan_list.get(i).getNativeName());
        if (lan_list.get(i).isSelected()) {
            viewHolder.tv_lan_item.setCompoundDrawablePadding(5);
            viewHolder.tv_lan_item.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_tick_ic, 0);
        }
    }

    @Override
    public int getItemCount() {
        return lan_list.size();
    }

    public class LangViewHolder extends RecyclerView.ViewHolder {
        TextView tv_lan_item;

        LangViewHolder(@NonNull View itemView) {
            super(itemView);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (!Language_Preference.getSharedprefInstance().getlanguageFilename().equals(lan_list.get(getAdapterPosition()).getFileName()))
                        mListener.seletedLanguage(lan_list.get(getAdapterPosition()));
                }
            });
            tv_lan_item = itemView.findViewById(R.id.tv_lan_item);
        }
    }
}
