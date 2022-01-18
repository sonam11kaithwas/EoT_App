package com.eot_app.nav_menu.expense.expense_detail.expense_mvp;

import com.eot_app.nav_menu.expense.expense_detail.expense_detail_model.ExpenseRes;
import com.eot_app.nav_menu.expense.expense_detail.expense_history.ExpenseStatushistoryModel;

import java.util.List;

/**
 * Created by Sonam-11 on 7/5/20.
 */
public interface ExpenseDetails_View {
    void setExpenseDetails(ExpenseRes expenseDetails);

    void setgetExpensehistory(List<ExpenseStatushistoryModel> expenseStatushistory);

    // void setgetExpensehistory2(LiveData<List<ExpenseStatushistoryModel>> expenseStatushistory);

    void onSessionExpire(String msg);

    void finishActivity();

}
