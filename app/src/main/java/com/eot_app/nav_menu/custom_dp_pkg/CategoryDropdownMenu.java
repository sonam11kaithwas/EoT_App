package com.eot_app.nav_menu.custom_dp_pkg;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.PopupWindow;

import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.eot_app.R;
import com.eot_app.nav_menu.custom_dp_pkg.dp_model.Category;

/**
 * Created by Sonam-11 on 6/8/20.
 */
public class CategoryDropdownMenu extends PopupWindow {
    private final Context context;
    private RecyclerView rvCategory;
    private CategoryDropdownAdapter dropdownAdapter;

    public CategoryDropdownMenu(Context context) {
        super(context);
        this.context = context;
        setupView();
    }

    public void setCategorySelectedListener(CategoryDropdownAdapter.CategorySelectedListener categorySelectedListener) {
        dropdownAdapter.setCategorySelectedListener(categorySelectedListener);
    }

    private void setupView() {
//        WindowManager wm = (WindowManager)context.getSystemService(Context.WINDOW_SERVICE);
//        Display display = wm.getDefaultDisplay();
//        Point size = new Point();
//        display.getSize(size);
//        int width = size.x;
//        int height = size.y;


        View view = LayoutInflater.from(context).inflate(R.layout.popup_category, null);


//        PopupWindow   ratePw = new PopupWindow(view);
//        ratePw.setWidth(width-20);
//        ratePw.setHeight(height-20);
//        ratePw.setFocusable(true);
//        ratePw.showAtLocation(view, Gravity.CENTER, 0, 0);

//        View view = LayoutInflater.from(context).inflate(R.layout.popup_category, null);


        rvCategory = view.findViewById(R.id.rvCategory);
        rvCategory.setHasFixedSize(true);
        rvCategory.setLayoutManager(new LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false));
        rvCategory.addItemDecoration(new DividerItemDecoration(context, LinearLayoutManager.VERTICAL));

        dropdownAdapter = new CategoryDropdownAdapter(Category.generateCategoryList());
        rvCategory.setAdapter(dropdownAdapter);

        setContentView(view);
    }
}

