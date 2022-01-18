package com.eot_app.nav_menu.expense.add_expense.category_tag;

import com.eot_app.utility.DropdownListBean;

/**
 * Created by Sonam-11 on 8/5/20.
 */
public class CategoryModel implements DropdownListBean {
    private String ecId;
    private String name;

    public String getEcId() {
        return ecId;
    }

    public void setEcId(String ecId) {
        this.ecId = ecId;
    }

    @Override
    public String getKey() {
        return getEcId();
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}

