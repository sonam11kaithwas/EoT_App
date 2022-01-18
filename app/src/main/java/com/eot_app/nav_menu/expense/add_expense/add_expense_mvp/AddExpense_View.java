package com.eot_app.nav_menu.expense.add_expense.add_expense_mvp;

import com.eot_app.nav_menu.client.client_db.Client;
import com.eot_app.nav_menu.expense.add_expense.category_tag.CategoryModel;
import com.eot_app.nav_menu.expense.add_expense.category_tag.TagModel;
import com.eot_app.nav_menu.expense.expense_detail.expense_detail_model.ExpenseRes;
import com.eot_app.nav_menu.jobs.job_db.Job;

import java.util.List;

/**
 * Created by Sonam-11 on 7/5/20.
 */
public interface AddExpense_View {
    void sessionExpire(String msg);

    void setCategoryList(List<CategoryModel> categoryModelList);

    void setExpenseTagList(List<TagModel> categoryModelList);

    void addExpenseSuccesFully();

    void errorDialog(String msg);

    void updateExpenseDetails(ExpenseRes expenseRes);

    void setJobServiceList(List<Job> servicesItemList);

    void setClientList(List<Client> data);

    void onSessionExpire(String msg);

    void finishActivity();

    void msg(String msg);

    void imgRemoveSuccessfully();

    void setCategorySpinnerList(List<CategoryModel> categoryModelList);
//    void validExpenseAmount();
}
