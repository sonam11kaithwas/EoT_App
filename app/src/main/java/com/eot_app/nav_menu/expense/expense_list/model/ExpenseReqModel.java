package com.eot_app.nav_menu.expense.expense_list.model;

/**
 * Created by Sonam-11 on 6/5/20.
 */
public class ExpenseReqModel {
    private final int limit;
    private final int index;
    private final String search;
    private final String apiRequestFrom;

    public ExpenseReqModel(int limit, int index, String search) {
        this.limit = limit;
        this.index = index;
        this.search = search;
        apiRequestFrom = "2";
    }

}
