package com.eot_app.nav_menu.expense.expense_detail;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.TextUtils;
import android.text.style.ForegroundColorSpan;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.eot_app.R;
import com.eot_app.nav_menu.expense.ExpenseStatus_Controller;
import com.eot_app.nav_menu.expense.add_expense.AddExpenseActivity;
import com.eot_app.nav_menu.expense.expense_detail.expense_detail_model.ExpenseRes;
import com.eot_app.nav_menu.expense.expense_detail.expense_history.ExpenseStatusHistoryAdpter;
import com.eot_app.nav_menu.expense.expense_detail.expense_history.ExpenseStatushistoryModel;
import com.eot_app.nav_menu.expense.expense_detail.expense_mvp.ExpenseDetails_PC;
import com.eot_app.nav_menu.expense.expense_detail.expense_mvp.ExpenseDetails_PI;
import com.eot_app.nav_menu.expense.expense_detail.expense_mvp.ExpenseDetails_View;
import com.eot_app.nav_menu.jobs.job_detail.detail.jobdetial_model.JobStatusModel;
import com.eot_app.utility.AppConstant;
import com.eot_app.utility.AppUtility;
import com.eot_app.utility.EotApp;
import com.eot_app.utility.language_support.LanguageController;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;

public class ExpenseDetailsActivity extends AppCompatActivity implements ExpenseDetails_View {
    public static final int UPDATEEXPANSE = 100;
    ExpenseStatusHistoryAdpter expenseStatusHistoryAdpter;
    private String expId;
    private ExpenseDetails_PI expenseDetailsPi;
    private TextView expense_date_txt, textExpenseNm, textViewCategroy, textViewJobCode, textViewAmount, textViewstatus, textViewDescription, textViewGroup;
    private RecyclerView expense_histry_list;
    private LinearLayoutManager layoutManager;
    private TextView textView8, textView6, history;
    private ExpenseRes expenseDetails;
    private ImageView img_status;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_expense_details);
        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            expId = getIntent().getStringExtra("expId");
        }

        initializeViews();
        setTitle(LanguageController.getInstance().getMobileMsgByKey(AppConstant.expense_details));
    }

    private void initializeViews() {
        expense_date_txt = findViewById(R.id.expense_date_txt);
        textExpenseNm = findViewById(R.id.textExpenseNm);
        textViewCategroy = findViewById(R.id.textViewCategroy);
        textViewJobCode = findViewById(R.id.textViewJobCode);
        textViewAmount = findViewById(R.id.textViewAmount);
        textViewstatus = findViewById(R.id.textViewstatus);
        textViewDescription = findViewById(R.id.textViewDescription);
        textViewGroup = findViewById(R.id.textViewGroup);

        expense_histry_list = findViewById(R.id.expense_histry_list);
        layoutManager = new LinearLayoutManager(this);
        expense_histry_list.setLayoutManager(layoutManager);

        img_status = findViewById(R.id.imageView4);

        textView8 = findViewById(R.id.textView8);
        textView8.setText(LanguageController.getInstance().getMobileMsgByKey(AppConstant.expense_description) + ":");
        textView6 = findViewById(R.id.textView6);
        textView6.setText(LanguageController.getInstance().getMobileMsgByKey(AppConstant.expense_group) + ":");
        history = findViewById(R.id.history);
        history.setText(LanguageController.getInstance().getMobileMsgByKey(AppConstant.expense_history));

        expenseStatusHistoryAdpter = new ExpenseStatusHistoryAdpter(new ArrayList<ExpenseStatushistoryModel>());
        expense_histry_list.setAdapter(expenseStatusHistoryAdpter);

        expenseDetailsPi = new ExpenseDetails_PC(this);
        expenseDetailsPi.getExpenseDetails(expId);
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    public void setExpenseDetails(ExpenseRes expenseDetails) {
        this.expenseDetails = expenseDetails;
        if (expenseDetails.getDateTime() != null && !expenseDetails.getDateTime().equals("")) {
            expense_date_txt.setText(AppUtility.getDateWithFormate(Long.parseLong(expenseDetails.getDateTime()), "dd-MMM-yyyy"));
        } else {
            expense_date_txt.setText("");
        }
        textExpenseNm.setText(expenseDetails.getName());

        Spannable txtFrst;

        txtFrst = new SpannableString(expenseDetails.getCategoryName());
        txtFrst.setSpan(new ForegroundColorSpan(Color.parseColor("#26a645")),
                0, txtFrst.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);

/*        if (!TextUtils.isEmpty(expenseDetails.getCategoryName())) {
            textViewCategroy.setVisibility(View.VISIBLE);*/
        textViewCategroy.setText(LanguageController.getInstance().getMobileMsgByKey(AppConstant.expense_category)
                + ": ");
        textViewCategroy.append(txtFrst);
        /*} else {
            textViewCategroy.setVisibility(View.GONE);
        }*/

        txtFrst = new SpannableString(expenseDetails.getJobCode());
        txtFrst.setSpan(new ForegroundColorSpan(ContextCompat.getColor(this, R.color.color_gray)),
                0, txtFrst.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        if (TextUtils.isEmpty(expenseDetails.getJobCode()))
            textViewJobCode.setVisibility(View.GONE);
        else {
            textViewJobCode.setVisibility(View.VISIBLE);
            textViewJobCode.setText(LanguageController.getInstance().getMobileMsgByKey(AppConstant.job_code) + ": " + expenseDetails.getJobCode());
        }
        textViewAmount.setText(AppUtility.getRoundoff_amount(expenseDetails.getAmt()));
        if (expenseDetails.getStatus().equals("0")) {
            expenseDetails.setStatus("5");
        }
        JobStatusModel jobStatusObject = ExpenseStatus_Controller.getInstance().getStatusObjectById(
                String.valueOf(expenseDetails.getStatus()));
        if (jobStatusObject != null) {
            textViewstatus.setText(jobStatusObject.getStatus_name());
            int id = EotApp.getAppinstance().getResources().getIdentifier(jobStatusObject.getImg(), "drawable", EotApp.getAppinstance().getPackageName());
            img_status.setImageResource(id);
        }
        textViewDescription.setText(expenseDetails.getDes());
        textViewGroup.setText(expenseDetails.getTagName());


    }

    @Override
    public void onSessionExpire(String msg) {
        AppUtility.alertDialog(this, LanguageController.getInstance().getMobileMsgByKey(AppConstant.dialog_error_title), msg, LanguageController.getInstance().getMobileMsgByKey(AppConstant.ok), "", new Callable<Boolean>() {
            @Override
            public Boolean call() throws Exception {
                EotApp.getAppinstance().sessionExpired();
                return null;
            }
        });
    }

    @Override
    public void finishActivity() {
        this.finish();
    }

    @Override
    public void setgetExpensehistory(List<ExpenseStatushistoryModel> expenseStatushistory) {
        expenseStatusHistoryAdpter.updateAdpter(expenseStatushistory);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.quotes_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.quotes_edit:
                editExpense();
                break;
            case android.R.id.home:
                onBackPressed();
                AppUtility.hideSoftKeyboard(this);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void editExpense() {
        if (expenseDetails != null) {
            Intent intent = new Intent(this, AddExpenseActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
            intent.putExtra("EditExpense", new Gson().toJson(expenseDetails));
            startActivityForResult(intent, UPDATEEXPANSE);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if (data == null) {
//            expenseDetailsPi.getExpenseDetails(expId);
            return;
        }
        switch (requestCode) {
            case UPDATEEXPANSE:
                setResult(RESULT_OK);
//                  if (data.getStringExtra("Update").equals("Update")) {
                String str = data.getStringExtra("EditExpense");
                expenseDetails = new Gson().fromJson(str, ExpenseRes.class);
                expenseDetailsPi.getExpenseDetails(expenseDetails.getExpId());
                //setExpenseDetails(expenseDetails);
//                } else if (data.getBooleanExtra("REMOVEIMAGE", false)) {
//                    expenseDetailsPi.getExpenseDetails(expId);
//                }
                break;
        }
        super.onActivityResult(requestCode, resultCode, data);
    }


}
