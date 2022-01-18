package com.eot_app.nav_menu.jobs.job_list.customselecter;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.LinearLayout;

import androidx.annotation.Nullable;

import com.eot_app.R;

/**
 * Created by aplite_pc302 on 6/5/18.
 */

public class CustomSelecter extends LinearLayout {
    boolean isSeleted = false;

    public CustomSelecter(Context context) {
        super(context);
        initViews();
    }


    public CustomSelecter(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        initViews();
    }

    public CustomSelecter(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initViews();
    }

    public CustomSelecter(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        initViews();
    }

    private void initViews() {
        setPadding(30, 10, 30, 10);
        setElevation(5f);
        setSeleted(false);
    }

    public boolean isSeleted() {
        return isSeleted;
    }


    public void setSeleted(boolean seleted) {
        setBackgroundResource(seleted ? R.drawable.filter_seleted_bg : R.drawable.custome_search_bg);
        this.isSeleted = seleted;
    }
}
