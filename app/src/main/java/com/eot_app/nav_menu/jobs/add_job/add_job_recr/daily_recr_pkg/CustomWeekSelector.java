package com.eot_app.nav_menu.jobs.add_job.add_job_recr.daily_recr_pkg;

import android.content.Context;
import android.util.AttributeSet;

import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatTextView;

import com.eot_app.R;

/**
 * Created by Sona-11 on 26/3/21.
 */
public class CustomWeekSelector extends AppCompatTextView {
    boolean isSeleted = false;

    public CustomWeekSelector(Context context) {
        super(context);
        initViews();
    }

    public CustomWeekSelector(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        initViews();
    }

    public CustomWeekSelector(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initViews();
    }


    private void initViews() {
        //  setPadding(30, 10, 30, 10);
        setElevation(5f);
        setSeleted(true);
    }

    public boolean isSeleted() {
        return isSeleted;
    }

    public void setSeleted(boolean seleted) {
        setTextColor(seleted ? getResources().getColor(R.color.white) : getResources().getColor(R.color.colorPrimary));
        setBackgroundResource(seleted ? R.drawable.week_days_selected : R.drawable.week_days_deselecte);
        this.isSeleted = seleted;
    }
}
