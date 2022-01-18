package com.eot_app.nav_menu.expense.add_expense.add_expense_mvp;

import com.eot_app.nav_menu.expense.add_expense.add_expense_model.AddExpenseReq;
import com.eot_app.nav_menu.expense.add_expense.add_expense_model.UpdateExpenseReq;

/**
 * Created by Sonam-11 on 7/5/20.
 */
public interface AddExpense_PI {
    void addExpense(AddExpenseReq addExpenseReq);

    void updateExpense(UpdateExpenseReq addExpenseReq);

    void getCategoryList();

    void getExpenseTagList();

    boolean validExpenseName(String trim, String exNm);

    void getJobServices();

    void getClientList();

    void removeExpanceImage(String id);

    boolean jobValidation(String jobId);

    boolean clientValidation(String clientId);
}
