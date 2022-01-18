package com.eot_app.utility;

import android.content.Context;

import androidx.recyclerview.widget.LinearLayoutManager;

public class CustomLinearLayoutManager extends LinearLayoutManager {
    public CustomLinearLayoutManager(Context context, int orientation, boolean reverseLayout) {
        super(context, orientation, reverseLayout);

    }

    // it will always pass false to RecyclerView when calling "canScrollVertically()" method.
    @Override
    public boolean canScrollVertically() {
        return false;
        /*        CustomLinearLayoutManager customLayoutManager = new CustomLinearLayoutManager(this, LinearLayoutManager.VERTICAL
                , false);

        recyclerView.setLayoutManager(customLayoutManager);*/
    }

    @Override
    public boolean canScrollHorizontally() {
        return false;
    }
}
