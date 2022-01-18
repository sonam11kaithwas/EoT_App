package com.eot_app.nav_menu.expense.expense_list.mvp;

import com.eot_app.nav_menu.expense.expense_list.model.ExpenseResModel;

import java.util.List;

/**
 * Created by Sonam-11 on 6/5/20.
 */
public interface ExpenseList_View {
    void setExpenseList(List<ExpenseResModel> expenseList);

    void setSearchVisibility(boolean searchVisibility);

    void disableRefersh();

    void refereshExpenseList();

    void onSessionExpire(String msg);

    void finish();
}
