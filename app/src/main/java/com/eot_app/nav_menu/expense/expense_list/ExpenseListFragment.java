package com.eot_app.nav_menu.expense.expense_list;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.eot_app.R;
import com.eot_app.nav_menu.expense.ExpenseStatus_Controller;
import com.eot_app.nav_menu.expense.expense_detail.ExpenseDetailsActivity;
import com.eot_app.nav_menu.expense.expense_list.model.ExpenseResModel;
import com.eot_app.nav_menu.expense.expense_list.mvp.ExpenseList_PI;
import com.eot_app.nav_menu.expense.expense_list.mvp.ExpenseList_View;
import com.eot_app.nav_menu.expense.expense_list.mvp.ExpenseList_pc;
import com.eot_app.utility.AppConstant;
import com.eot_app.utility.AppUtility;
import com.eot_app.utility.CustomFilterButton;
import com.eot_app.utility.EotApp;
import com.eot_app.utility.language_support.LanguageController;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;


/**
 * A fragment representing a list of Items.
 * <p/>
 * Activities containing this fragment MUST implement the {@link }
 * interface.
 */

/**
 * Created by Sonam-11 on 2020-05-06.
 */
public class ExpenseListFragment extends Fragment implements ExpenseList_View
        , MyExpenseRecyclerViewAdapter.ExpencesInteraction, View.OnClickListener, TextWatcher {

    // TODO: Customize parameter argument names
    private static final String ARG_COLUMN_COUNT = "column-count";
    // TODO: Customize parameters
    private final int mColumnCount = 1;
    /**
     * <p>Filter button options view</p>
     */
    CustomFilterButton cfb_claim, cfb_approved, cfb_reject, cfb_open, cfb_paid;
    private ExpenseList_PI expenseListPi;
    private LinearLayout searchLayout;
    private EditText edtSearch;
    private ImageView search_btn;
    private MyExpenseRecyclerViewAdapter myExpenseRecyclerViewAdapter;
    private SwipeRefreshLayout swiperefresh;
    private LinearLayout ll_empty_msg;
    private TextView tv_no_expenses;
    private boolean filterByName;


    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public ExpenseListFragment() {
    }

    // TODO: Customize parameter initialization
    @SuppressWarnings("unused")
    public static ExpenseListFragment newInstance(int columnCount) {
        ExpenseListFragment fragment = new ExpenseListFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_COLUMN_COUNT, columnCount);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_expense_list, container, false);
        initializeView(view);
        // Set the adapter
        return view;
    }

    private void initializeView(View view) {
        ExpenseStatus_Controller.getInstance().getStatusList();

        cfb_claim = view.findViewById(R.id.cfb_claim);
        cfb_approved = view.findViewById(R.id.cfb_approved);
        cfb_reject = view.findViewById(R.id.cfb_reject);
        cfb_paid = view.findViewById(R.id.cfb_paid);
        cfb_open = view.findViewById(R.id.cfb_open);

        cfb_claim.setOnClickListener(this);
        cfb_approved.setOnClickListener(this);
        cfb_reject.setOnClickListener(this);
        cfb_paid.setOnClickListener(this);
        cfb_open.setOnClickListener(this);

        cfb_claim.setText(LanguageController.getInstance().getMobileMsgByKey(AppConstant.Expense_Claim_Reimbursement));
        cfb_approved.setText(LanguageController.getInstance().getMobileMsgByKey(AppConstant.Expense_Approved));
        cfb_reject.setText(LanguageController.getInstance().getMobileMsgByKey(AppConstant.Expense_Reject));
        cfb_paid.setText(LanguageController.getInstance().getMobileMsgByKey(AppConstant.Expense_Paid));
        cfb_open.setText(LanguageController.getInstance().getMobileMsgByKey(AppConstant.Expense_Open));

        ll_empty_msg = view.findViewById(R.id.ll_empty_msg);
        tv_no_expenses = view.findViewById(R.id.tv_no_expenses);
        tv_no_expenses.setText(LanguageController.getInstance().getMobileMsgByKey(AppConstant.expense_not_found));


        RecyclerView recyclerView = view.findViewById(R.id.list);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        myExpenseRecyclerViewAdapter = new MyExpenseRecyclerViewAdapter(getActivity(), new ArrayList<ExpenseResModel>(), this);
        recyclerView.setAdapter(myExpenseRecyclerViewAdapter);
        swiperefresh = view.findViewById(R.id.swiperefresh);
        searchLayout = view.findViewById(R.id.searchLayout);

        searchLayout = view.findViewById(R.id.searchLayout);
        edtSearch = view.findViewById(R.id.edtSearch);
        edtSearch.setHint(LanguageController.getInstance().getMobileMsgByKey(AppConstant.expense_search_name));
        edtSearch.addTextChangedListener(this);
        search_btn = view.findViewById(R.id.search_btn);
        search_btn.setOnClickListener(this);
        expenseListPi = new ExpenseList_pc(this);
        swiperefresh.setRefreshing(true);
        //expenseListPi.getExpenseList(edtSearch.getText().toString().trim());

        swiperefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                swiperefresh.setRefreshing(true);
                expenseListPi.getExpenseList(edtSearch.getText().toString().trim());
            }
        });
    }

    @Override
    public void disableRefersh() {
        if (swiperefresh.isRefreshing())
            swiperefresh.setRefreshing(false);
    }

    @Override
    public void finish() {

    }

    @Override
    public void onResume() {
        Log.e("", "");
        expenseListPi.getExpenseList(edtSearch.getText().toString().trim());
//        if (builder2!=null&&!TextUtils.isEmpty(builder2.toString()))
//            myExpenseRecyclerViewAdapter.filterByStatus(builder2.toString());
        super.onResume();
    }

    @Override
    public void onPause() {
        Log.e("", "");
        filterByName = false;
        super.onPause();
    }

    @Override
    public void setExpenseList(List<ExpenseResModel> expenseList) {
        resetFilterButton();
        myExpenseRecyclerViewAdapter.updateAdpter(expenseList);
        disableRefersh();
    }

    private void searchBtExpensenm() {
        if (!TextUtils.isEmpty(edtSearch.getText().toString())) {
            AppUtility.hideSoftKeyboard(getActivity());
            // filterListByName(edtSearch.getText().toString().trim());
            if (myExpenseRecyclerViewAdapter != null) {
                resetFilterButton();
                myExpenseRecyclerViewAdapter.filterByName(edtSearch.getText().toString());
                filterByName = true;
            }
            //  expenseListPi.getExpenseList(edtSearch.getText().toString().trim());
        }
    }

    @Override
    public void onListFragmentInteraction(ExpenseResModel item) {
        Intent intent = new Intent(getActivity(), ExpenseDetailsActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
        intent.putExtra("expId", item.getExpId());
        startActivityForResult(intent, 147);
    }

    @Override
    public void isListEmpty(boolean b) {
        if (b)
            ll_empty_msg.setVisibility(View.VISIBLE);
        else ll_empty_msg.setVisibility(View.GONE);

    }

    @Override
    public void setSearchVisibility(boolean b) {
        searchLayout.setVisibility(b ? View.VISIBLE : View.GONE);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.search_btn:
                searchBtExpensenm();
                break;
            case R.id.cfb_claim:
                cfb_claim.setSeleted(!cfb_claim.isSeleted());
                applySelector();
                break;
            case R.id.cfb_approved:
                cfb_approved.setSeleted(!cfb_approved.isSeleted());
                applySelector();
                break;
            case R.id.cfb_reject:
                cfb_reject.setSeleted(!cfb_reject.isSeleted());
                applySelector();
                break;
            case R.id.cfb_paid:
                cfb_paid.setSeleted(!cfb_paid.isSeleted());
                applySelector();
                break;
            case R.id.cfb_open:
                cfb_open.setSeleted(!cfb_open.isSeleted());
                applySelector();
                break;
        }
    }

    private void resetFilterButton() {
        cfb_claim.setSeleted(false);
        cfb_approved.setSeleted(false);
        cfb_reject.setSeleted(false);
        cfb_open.setSeleted(false);
        cfb_paid.setSeleted(false);
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {
        //if (s.length() == 0) {
        if (filterByName && s.length() == 0) {
            /***get all users list without search****/
            AppUtility.hideSoftKeyboard(getActivity());
            expenseListPi.getExpenseList("");
        }
    }

    @Override
    public void afterTextChanged(Editable s) {

    }

    @Override
    public void refereshExpenseList() {
        if (expenseListPi != null) {
            expenseListPi.getExpenseList(edtSearch.getText().toString().trim());
        }
    }

    @Override
    public void onSessionExpire(String msg) {
        AppUtility.alertDialog(getActivity(), LanguageController.getInstance().getMobileMsgByKey(AppConstant.dialog_error_title), msg, LanguageController.getInstance().getMobileMsgByKey(AppConstant.ok), "",
                new Callable<Boolean>() {
                    @Override
                    public Boolean call() throws Exception {
                        EotApp.getAppinstance().sessionExpired();
                        return null;
                    }
                });
    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }

    public void applySelector() {
        if (myExpenseRecyclerViewAdapter != null) {
            setSearchVisibility(false);

            StringBuilder builder = new StringBuilder();
            if (cfb_claim.isSeleted())
                builder.append(AppConstant.EXPENSE_CLAIM);
            if (cfb_approved.isSeleted())
                builder.append(" " + AppConstant.EXPENSE_APPROVED);
            if (cfb_reject.isSeleted())
                builder.append(" " + AppConstant.EXPENSE_REJECT);
            if (cfb_open.isSeleted())
                builder.append(" " + AppConstant.EXPENSE_OPEN);
            if (cfb_paid.isSeleted())
                builder.append(" " + AppConstant.EXPENSE_PAID);

//            builder2 = new StringBuilder();
//            builder2.append(builder);

            if (!TextUtils.isEmpty(builder.toString()))
                myExpenseRecyclerViewAdapter.filterByStatus(builder.toString());
            else myExpenseRecyclerViewAdapter.filterByName("");


        }

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK) {
            resetFilterButton();
            swiperefresh.setRefreshing(true);
            expenseListPi.getExpenseList("");
        }

    }
}
