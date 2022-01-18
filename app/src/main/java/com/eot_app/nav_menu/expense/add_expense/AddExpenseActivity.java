package com.eot_app.nav_menu.expense.add_expense;

import static com.eot_app.nav_menu.expense.expense_detail.ExpenseDetailsActivity.UPDATEEXPANSE;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.util.Log;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.eot_app.R;
import com.eot_app.UploadDocumentActivity;
import com.eot_app.home_screens.MainActivity;
import com.eot_app.nav_menu.client.client_db.Client;
import com.eot_app.nav_menu.expense.add_expense.add_expense_model.AddExpenseReq;
import com.eot_app.nav_menu.expense.add_expense.add_expense_model.UpdateExpenseReq;
import com.eot_app.nav_menu.expense.add_expense.add_expense_mvp.AddExpense_PC;
import com.eot_app.nav_menu.expense.add_expense.add_expense_mvp.AddExpense_PI;
import com.eot_app.nav_menu.expense.add_expense.add_expense_mvp.AddExpense_View;
import com.eot_app.nav_menu.expense.add_expense.category_tag.CategoryModel;
import com.eot_app.nav_menu.expense.add_expense.category_tag.Job_Adpter;
import com.eot_app.nav_menu.expense.add_expense.category_tag.TagListAdpter;
import com.eot_app.nav_menu.expense.add_expense.category_tag.TagModel;
import com.eot_app.nav_menu.expense.expense_detail.expense_detail_model.ExpenseRes;
import com.eot_app.nav_menu.jobs.add_job.adapters.FilterAdapter;
import com.eot_app.nav_menu.jobs.job_db.Job;
import com.eot_app.nav_menu.quote.add_quotes_pkg.quotes_adpter.Dynamic_Adpter;
import com.eot_app.utility.AppConstant;
import com.eot_app.utility.AppUtility;
import com.eot_app.utility.App_preference;
import com.eot_app.utility.EotApp;
import com.eot_app.utility.language_support.LanguageController;
import com.google.android.material.textfield.TextInputLayout;
import com.google.gson.Gson;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.net.URLConnection;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.Callable;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;

/**
 * Created by Sonam-11 on 2020-05-06.
 */
public class AddExpenseActivity extends UploadDocumentActivity implements TextWatcher, View.OnClickListener
        , AddExpense_View, RadioGroup.OnCheckedChangeListener, CheckBox.OnCheckedChangeListener {
    int cateListSize, tagListSize;
    String amount = "";
    LinearLayout category_linearLayout;
    private Button submit_btn, upload_btn;
    private TextInputLayout expense_nm_layout, expense_amount_layout, expense_desc_layout, expense_group_layout, job_layout, clients_layout;
    private EditText edt_expense_nm, edt_expense_amount, edt_expense_desc;
    private TextView txt_date, click_here_txt, link_to, edt_expense_date;
    private RadioButton radio_none, radio_job, radio_client;
    private ImageView upload_Img, remove_img;
    private AddExpense_PI addExpensePi;
    private CheckBox checkbox_rem_claim;
    private boolean Job_Client_View = false;
    private RadioGroup rediogrp;
    private View job_divider, client_divider;
    private TextView txt_hint_category;
    private AutoCompleteTextView auto_group, auto_job, auto_client;
    private String ecId = "", etId = "", jobId = "", cltId = "";
    private RelativeLayout layout;
    private String exDate = "", image_path = "";
    /**
     * select date from picker & concanate current time
     */
    private final DatePickerDialog.OnDateSetListener datePickerListener = new DatePickerDialog.OnDateSetListener() {
        @Override
        public void onDateSet(DatePicker view, int selectedYear, int selectedMonth, int selectedDay) {
            String dateselect = "";
            try {
                DateFormat formatter = new SimpleDateFormat("dd-MM-yyyy", Locale.US);//hh:mm:ss a
                Date startDate = formatter.parse(selectedDay + "-" + (selectedMonth + 1) + "-" + selectedYear);
                dateselect = formatter.format(startDate);
            } catch (ParseException e) {
                e.printStackTrace();
            }
            DateFormat dateFormat = new SimpleDateFormat("hh:mm:ss a", Locale.US);//append current time
            dateFormat.format(new Date());
            exDate = dateselect + " " + dateFormat.format(new Date());

            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
            params.setMargins(0, 5, 0, 0);
            txt_date.setLayoutParams(params);
            txt_date.setVisibility(View.VISIBLE);
            edt_expense_date.setText(dateselect);
        }
    };
    private String status = "1";
    private ExpenseRes expenseDetails;
    private TextView tv_hint_category, tv_spinner_category;
    private Spinner spinn_category;
    private boolean rmvImg, JOBCHECK, CLIENTCHECK;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_expense);


        initializeViews();
        emptyGroupCheck();

        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            if (getIntent().hasExtra("EditExpense")) {
                String str = getIntent().getExtras().getString("EditExpense");
                expenseDetails = new Gson().fromJson(str, ExpenseRes.class);// getIntent().getExtras().getParcelable(".");
                getServerImage();
                editExpanse();
            }
        } else {
            setTitle(LanguageController.getInstance().getMobileMsgByKey(AppConstant.add_expense));
        }
    }

    private void getServerImage() {
        if (expenseDetails != null && expenseDetails.getReceipt() != null && expenseDetails.getReceipt().size() > 0) {
            try {
                if (expenseDetails.getReceipt().get(expenseDetails.getReceipt().size() - 1).getReceipt() != null) {
                    Picasso.with(this)
                            .load(App_preference.getSharedprefInstance().getBaseURL()
                                    + expenseDetails.getReceipt().get(expenseDetails.getReceipt().size() - 1).getReceipt())
                            .into(upload_Img);
                    upload_Img.setVisibility(View.VISIBLE);
                    remove_img.setVisibility(View.VISIBLE);
                    upload_btn.setVisibility(View.GONE);
                    click_here_txt.setVisibility(View.GONE);

                } else {
                    upload_Img.setVisibility(View.GONE);
                    remove_img.setVisibility(View.GONE);
                    upload_btn.setVisibility(View.VISIBLE);
                    click_here_txt.setVisibility(View.VISIBLE);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private void emptyGroupCheck() {

        auto_group.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (tagListSize == 0) {
                    if (MotionEvent.ACTION_UP == event.getAction()) {
                        auto_group.setInputType(InputType.TYPE_NULL);
                        AppUtility.alertDialog(AddExpenseActivity.this, "", LanguageController.getInstance().getMobileMsgByKey(AppConstant.err_empty_expense_Group), LanguageController.getInstance().getMobileMsgByKey(AppConstant.ok), "", new Callable<Boolean>() {
                            @Override
                            public Boolean call() throws Exception {
                                return null;
                            }
                        });
                    }
                }
                return false;
            }
        });
    }

    private void editExpanse() {
        setTitle(LanguageController.getInstance().getMobileMsgByKey(AppConstant.expense_edit));
        submit_btn.setText(LanguageController.getInstance().getMobileMsgByKey(AppConstant.expense_edit));
        edt_expense_nm.setText(expenseDetails.getName());
        if (!expenseDetails.getDateTime().equals("")) {
            txt_date.setVisibility(View.VISIBLE);
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
            params.setMargins(0, 5, 0, 0);
            txt_date.setLayoutParams(params);
            edt_expense_date.setText(AppUtility.getDateWithFormate
                    (Long.parseLong(expenseDetails.getDateTime()), "dd-MM-yyyy"));
        }

        if (!expenseDetails.getCategoryName().equals("")) {
            tv_hint_category.setVisibility(View.VISIBLE);
            tv_spinner_category.setText(expenseDetails.getCategoryName());
        }

        edt_expense_amount.setText(AppUtility.getRoundoff_amount(String.valueOf(expenseDetails.getAmt())));

        edt_expense_desc.setText(expenseDetails.getDes());
        if (!expenseDetails.getTagName().equals(""))
            expense_group_layout.setHintEnabled(true);
        auto_group.setText(expenseDetails.getTagName());
        checkbox_rem_claim.setChecked(expenseDetails.getStatus().equals("1"));
        status = expenseDetails.getStatus();
        ecId = expenseDetails.getCategory();
        etId = expenseDetails.getTag();
        jobId = expenseDetails.getJobId();
        cltId = expenseDetails.getCltId();
        try {
            if (expenseDetails.getDateTime() != null && !expenseDetails.getDateTime().equals("")) {
                exDate = AppUtility.getDate(Long.parseLong(expenseDetails.getDateTime()), "dd-MM-yyyy");
                edt_expense_date.setText(exDate);
            } else {
                edt_expense_date.setText(AppUtility.getDateByFormat("dd-MMM-yyyy"));
            }
        } catch (Exception e) {
            e.getMessage();
        }


        if (expenseDetails.getJobId().equals("0") && expenseDetails.getCltId().equals("0")) {
            radio_none.setChecked(true);
            JOBCHECK = false;
            CLIENTCHECK = false;
        } else if (!expenseDetails.getJobId().equals("0")) {
            radio_job.setChecked(true);
            job_layout.setHintEnabled(true);
            auto_job.setText(expenseDetails.getJobCode());
            JOBCHECK = true;
            CLIENTCHECK = false;
        } else if (!expenseDetails.getCltId().equals("0")) {
            radio_client.setChecked(true);
            clients_layout.setHintEnabled(true);
            auto_client.setText(expenseDetails.getCltName());
            JOBCHECK = false;
            CLIENTCHECK = true;
        } else {
            radio_none.setChecked(true);
            JOBCHECK = false;
            CLIENTCHECK = false;
        }
    }

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        switch (buttonView.getId()) {
            case R.id.checkbox_rem_claim:
                if (buttonView.isChecked()) {
                    /**1 for Claim statu's***/
                    status = "1";
                } else {
                    /**5 for Open status****/
                    status = "5";
                }
                break;
        }
    }

    private void roundOffAmount() {
        try {
            if (!edt_expense_amount.getText().toString().equals("")) {
                amount = AppUtility.getRoundoff_amount(edt_expense_amount.getText().toString());
            }
        } catch (Exception ex) {
            ex.getMessage();
        }
    }

    private void initializeViews() {
        layout = findViewById(R.id.layout);
        AppUtility.setupUI(layout, this);

        upload_Img = findViewById(R.id.upload_Img);

        upload_btn = findViewById(R.id.upload_btn);
        submit_btn = findViewById(R.id.submit_btn);
        click_here_txt = findViewById(R.id.click_here_txt);

        expense_nm_layout = findViewById(R.id.expense_nm_layout);
        //expense_date_layout = findViewById(R.id.expense_date_layout);
        expense_amount_layout = findViewById(R.id.expense_amount_layout);
        expense_desc_layout = findViewById(R.id.expense_desc_layout);
        expense_group_layout = findViewById(R.id.expense_group_layout);
        job_layout = findViewById(R.id.job_layout);
        clients_layout = findViewById(R.id.clients_layout);
//        category_layout = findViewById(R.id.category_layout);


        edt_expense_nm = findViewById(R.id.edt_expense_nm);
        edt_expense_date = findViewById(R.id.edt_expense_date);
        edt_expense_amount = findViewById(R.id.edt_expense_amount);
        edt_expense_desc = findViewById(R.id.edt_expense_desc);
        //edt_expense_group = findViewById(R.id.edt_expense_group);
//        edt_job = findViewById(R.id.edt_job);
//        edt_clients = findViewById(R.id.edt_clients);

        link_to = findViewById(R.id.link_to);

        radio_none = findViewById(R.id.radio_none);
        radio_job = findViewById(R.id.radio_job);
        radio_client = findViewById(R.id.radio_client);

        checkbox_rem_claim = findViewById(R.id.checkbox_rem_claim);

        rediogrp = findViewById(R.id.rediogrp);
        job_divider = findViewById(R.id.job_divider);
        client_divider = findViewById(R.id.client_divider);

//        category_linearLayout = findViewById(R.id.category_linearLayout);
//        txt_hint_category = findViewById(R.id.txt_hint_category);

        //auto_category = findViewById(R.id.auto_category);


        auto_group = findViewById(R.id.auto_group);

        txt_date = findViewById(R.id.txt_date);

        auto_job = findViewById(R.id.auto_job);
        auto_client = findViewById(R.id.auto_client);
        remove_img = findViewById(R.id.remove_img);


        tv_hint_category = findViewById(R.id.tv_hint_category);
        tv_spinner_category = findViewById(R.id.tv_spinner_category);
        category_linearLayout = findViewById(R.id.category_linearLayout);
        spinn_category = findViewById(R.id.spinn_category);


        setTextInViesw();

    }

    private void setTextInViesw() {
        edt_expense_nm.setHint(LanguageController.getInstance().getMobileMsgByKey(AppConstant.expense_nm) + " *");
        edt_expense_date.setHint(LanguageController.getInstance().getMobileMsgByKey(AppConstant.expense_date));
        edt_expense_amount.setHint(LanguageController.getInstance().getMobileMsgByKey(AppConstant.expense_amount) + " *");
        edt_expense_desc.setHint(LanguageController.getInstance().getMobileMsgByKey(AppConstant.expense_description));
        //edt_expense_group.setHint(LanguageController.getInstance().getMobileMsgByKey(AppConstant.expense_group));
//        edt_job.setHint(LanguageController.getInstance().getMobileMsgByKey(AppConstant.expense_jobid));
//        edt_clients.setHint(LanguageController.getInstance().getMobileMsgByKey(AppConstant.expense_clientid));

        click_here_txt.setText(LanguageController.getInstance().getMobileMsgByKey(AppConstant.click_here));
        link_to.setText(LanguageController.getInstance().getMobileMsgByKey(AppConstant.expense_link));

        radio_none.setText(LanguageController.getInstance().getMobileMsgByKey(AppConstant.expense_none));
        radio_job.setText(LanguageController.getInstance().getMobileMsgByKey(AppConstant.expense_jobid));
        radio_client.setText(LanguageController.getInstance().getMobileMsgByKey(AppConstant.expense_clientid));

        upload_btn.setText(LanguageController.getInstance().getMobileMsgByKey(AppConstant.expense_upload));
        submit_btn.setText(LanguageController.getInstance().getMobileMsgByKey(AppConstant.add_expense));

        checkbox_rem_claim.setText(LanguageController.getInstance().getMobileMsgByKey(AppConstant.claim_reimbu));

//        txt_hint_category.setHint(LanguageController.getInstance().getMobileMsgByKey(AppConstant.expense_category));
        // auto_category.setHint(LanguageController.getInstance().getMobileMsgByKey(AppConstant.expense_category));
        auto_group.setHint(LanguageController.getInstance().getMobileMsgByKey(AppConstant.expense_group));


        txt_date.setHint(LanguageController.getInstance().getMobileMsgByKey(AppConstant.date) + " *");

        auto_job.setHint(LanguageController.getInstance().getMobileMsgByKey(AppConstant.expense_jobid));
        auto_client.setHint(LanguageController.getInstance().getMobileMsgByKey(AppConstant.expense_clientid));
        tv_hint_category.setText(LanguageController.getInstance().getMobileMsgByKey(AppConstant.expense_category));
        tv_spinner_category.setHint(LanguageController.getInstance().getMobileMsgByKey(AppConstant.expense_category));


        edt_expense_date.setText(AppUtility.getDateByFormat("dd-MMM-yyyy"));
        exDate = AppUtility.getDateByFormat("dd-MM-yyyy hh:mm:ss a");

        clickListners();

        addExpensePi = new AddExpense_PC(this);
        addExpensePi.getCategoryList();
    }

    private void clickListners() {
        submit_btn.setOnClickListener(this);
        upload_btn.setOnClickListener(this);
        remove_img.setOnClickListener(this);


        checkbox_rem_claim.setOnCheckedChangeListener(this);


        expense_nm_layout.getEditText().addTextChangedListener(this);
        //expense_date_layout.getEditText().addTextChangedListener(this);
        expense_amount_layout.getEditText().addTextChangedListener(this);
        expense_desc_layout.getEditText().addTextChangedListener(this);
        expense_group_layout.getEditText().addTextChangedListener(this);
        job_layout.getEditText().addTextChangedListener(this);
        clients_layout.getEditText().addTextChangedListener(this);
        // category_layout.getEditText().addTextChangedListener(this);

        rediogrp.setOnCheckedChangeListener(this);

//        category_linearLayout.setOnClickListener(this);
//        category_layout.setOnClickListener(this);
//        auto_category.setOnClickListener(this);
        auto_group.setOnClickListener(this);
        edt_expense_date.setOnClickListener(this);
        auto_job.setOnClickListener(this);
        auto_client.setOnClickListener(this);
        category_linearLayout.setOnClickListener(this);

    }

    @Override
    public void setJobServiceList(List<Job> servicesItemList) {
        AppUtility.autocompletetextviewPopUpWindow(auto_job);
        final Job_Adpter services_item_adapter = new Job_Adpter(this,
                R.layout.custom_adapter_item_layout, (ArrayList<Job>) servicesItemList);
        auto_job.setAdapter(services_item_adapter);
        auto_job.setThreshold(1);
        auto_job.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                job_layout.setHintEnabled(true);
                Job job = services_item_adapter.getSelectedItem(auto_job.getText().toString().trim());
                jobId = job.getJobId();
                //jobName = services_item_adapter.getSelectedItem(i).getNm();
            }
        });

        auto_job.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (charSequence.length() >= 1) {
                    job_layout.setHintEnabled(true);
                    //  jobName = charSequence.toString();
                } else if (charSequence.length() <= 0) {
                    job_layout.setHintEnabled(false);
                    // auto_group.setText("");
                    // auto_job.removeTextChangedListener(this);
                    jobId = "";
                    // JOBCHECK = false;
                    // jobName = "";
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

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
    public void msg(String msg) {
        showDialogs(msg);
    }

    @Override
    public void setClientList(List<Client> data) {
        AppUtility.autocompletetextviewPopUpWindow(auto_client);
        final FilterAdapter filter = new FilterAdapter(this, R.layout.custom_adapter_item_layout, (ArrayList<Client>) data);
        auto_client.setAdapter(filter);
        auto_client.setThreshold(2);
        auto_client.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                //   cltId = (((Client) adapterView.getItemAtPosition(i)).getCltId());
                Client client = filter.getSelectedItem(auto_client.getText().toString().trim());
                cltId = client.getCltId();
            }
        });


        auto_client.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (charSequence.length() >= 1) {
                    clients_layout.setHintEnabled(true);
                } else if (charSequence.length() <= 0) {
                    clients_layout.setHintEnabled(false);
                    // auto_group.setText("");
                    //   auto_client.removeTextChangedListener(this);
                    cltId = "";
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
    }

    @Override
    public void setCategoryList(List<CategoryModel> categoryModelList) {
    }

    @Override
    public void setCategorySpinnerList(final List<CategoryModel> categoryModelList) {
        cateListSize = categoryModelList.size();
        AppUtility.spinnerPopUpWindow(spinn_category);
        Dynamic_Adpter myClassAdapter = new Dynamic_Adpter<>(this, categoryModelList);
        spinn_category.setAdapter(myClassAdapter);
        spinn_category.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int position, long l) {
                tv_hint_category.setVisibility(View.VISIBLE);
                tv_spinner_category.setText(categoryModelList.get(position).getName());
                ecId = categoryModelList.get(position).getEcId();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
    }


    @Override
    public void setExpenseTagList(List<TagModel> categoryModelList) {
        tagListSize = categoryModelList.size();
        AppUtility.autocompletetextviewPopUpWindow(auto_group);
        final TagListAdpter countryAdapter = new TagListAdpter(
                this, R.layout.custom_adapter_item_layout,
//                this, R.layout.drop_layout,
                (ArrayList<TagModel>) categoryModelList);
        auto_group.setAdapter(countryAdapter);
        auto_group.setThreshold(1);
        auto_group.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                etId = ((TagModel) adapterView.getItemAtPosition(i)).getEtId();
            }
        });

        auto_group.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (charSequence.length() >= 1) {
                    expense_group_layout.setHintEnabled(true);
                } else if (charSequence.length() <= 0) {
                    expense_group_layout.setHintEnabled(false);
                    //auto_group.setText("");
                    etId = "";
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
    }

    @Override
    public void errorDialog(String msg) {
        showDialogs(msg);
    }

    private void showDialogs(String msg) {
        AppUtility.error_Alert_Dialog(this, msg, LanguageController.getInstance().getMobileMsgByKey(AppConstant.ok)
                , new Callable<Boolean>() {
                    @Override
                    public Boolean call() throws Exception {
                        return null;
                    }
                });
    }

    @Override
    public void updateExpenseDetails(ExpenseRes expenseRes) {
        EotApp.getAppinstance().showToastmsg(LanguageController.getInstance().getMobileMsgByKey(AppConstant.expense_update));
        String str = new Gson().toJson(expenseRes);
        Intent intent = new Intent();
        //   intent.putExtra("Update", "Update");
        intent.putExtra("EditExpense", str);
        setResult(UPDATEEXPANSE, intent);
        this.finish();
    }

    @Override
    public void onBackPressed() {
        Log.e("", "");
        if (rmvImg) {
            String str = new Gson().toJson(expenseDetails);
            Intent intent = new Intent();
            intent.putExtra("EditExpense", str);
            setResult(UPDATEEXPANSE, intent);
        }
        super.onBackPressed();
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.category_linearLayout:
                if (cateListSize > 0)
                    spinn_category.performClick();
                else {
                    AppUtility.alertDialog(this, "", LanguageController.getInstance().getMobileMsgByKey(AppConstant.err_empty_expense_Category), LanguageController.getInstance().getMobileMsgByKey(AppConstant.ok), "", new Callable<Boolean>() {
                        @Override
                        public Boolean call() throws Exception {
                            return null;
                        }
                    });
                }
                break;
            case R.id.remove_img:
                removeImage();
                break;
            case R.id.submit_btn:
                submit_btn.setEnabled(false);
                createAddExpanseRequest();
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        submit_btn.setEnabled(true);
                    }
                }, 500);
                break;
            case R.id.upload_btn:
                selectFile(true);
                break;
            case R.id.auto_group:
                if (tagListSize > 0)
                    auto_group.showDropDown();
                break;
            case R.id.edt_expense_date:
                SelectStartDate();
                break;
            case R.id.auto_job:
                auto_job.showDropDown();
                break;
            case R.id.auto_client:
                auto_client.showDropDown();
                break;
        }

    }

    public void createAddExpanseRequest() {

        if (submit_btn.getText().toString().equals(LanguageController.getInstance().
                getMobileMsgByKey(AppConstant.add_expense))) {

            MultipartBody.Part[] receipt = new MultipartBody.Part[1];
            String mimeType = "";
            File file = new File(image_path);
            if (file != null && file.exists()) {
                mimeType = URLConnection.guessContentTypeFromName(file.getName());
                if (mimeType == null) {
                    mimeType = URLConnection.guessContentTypeFromName(file.getName());
                    if (mimeType == null) {
                        return;
                    }
                }
                // MultipartBody.Part is used to send also the actual file name
                // receipt = new MultipartBody.Part[1];

                for (int index = 0; index <
                        1; index++) {
                    RequestBody surveyBody = RequestBody.create(MediaType.parse(mimeType), file);
                    receipt[index] = MultipartBody.Part.createFormData(
                            "receipt[]", file.getName(),
                            surveyBody);
                }
            }

            callApiWithValidations(new AddExpenseReq(jobId,
                    cltId,
                    edt_expense_nm.getText().toString().trim(),
                    edt_expense_amount.getText().toString().trim()
                    , exDate, ecId,
                    etId, edt_expense_desc.getText().toString().trim()
                    , status, receipt));
            Log.e("Add Ecpense", "");
        } else {
            MultipartBody.Part[] receipt = null;
            if (!image_path.equals("")) {
                receipt = new MultipartBody.Part[1];
                String mimeType = "";
                File file = new File(image_path);
                if (file != null && file.exists()) {
                    mimeType = URLConnection.guessContentTypeFromName(file.getName());
                    if (mimeType == null) {
                        mimeType = URLConnection.guessContentTypeFromName(file.getName());
                        if (mimeType == null) {
                            return;
                        }
                    }
                    for (int index = 0; index <
                            1; index++) {
                        RequestBody surveyBody = RequestBody.create(MediaType.parse(mimeType), file);
                        receipt[index] = MultipartBody.Part.createFormData(
                                "receipt[]", file.getName(),
                                surveyBody);
                    }
                }
            }
            if (addExpensePi.validExpenseName(edt_expense_nm.getText().toString().trim(),
                    edt_expense_amount.getText().toString().trim())) {
                if (JOBCHECK) {// && !auto_job.getText().toString().trim().equals("")
                    if (addExpensePi.jobValidation(jobId)) {
                        addExpensePi.updateExpense(new UpdateExpenseReq(jobId, cltId,
                                edt_expense_nm.getText().toString().trim(),
                                edt_expense_amount.getText().toString().trim()
                                , exDate, ecId, etId, status,
                                edt_expense_desc.getText().toString().trim()
                                , receipt, expenseDetails.getExpId()
                                , ""));
                    }
                } else if (CLIENTCHECK) {//&& !auto_client.getText().toString().trim().equals("")
                    if (addExpensePi.clientValidation(cltId)) {
                        addExpensePi.updateExpense(new UpdateExpenseReq(jobId, cltId,
                                edt_expense_nm.getText().toString().trim(),
                                edt_expense_amount.getText().toString().trim()
                                , exDate, ecId, etId, status,
                                edt_expense_desc.getText().toString().trim()
                                , receipt, expenseDetails.getExpId()
                                , ""));
                    }
                } else {
                    addExpensePi.updateExpense(new UpdateExpenseReq(jobId, cltId,
                            edt_expense_nm.getText().toString().trim(),
                            edt_expense_amount.getText().toString().trim()
                            , exDate, ecId, etId, status,
                            edt_expense_desc.getText().toString().trim()
                            , receipt, expenseDetails.getExpId()
                            , ""));
                }

            }

            Log.e("Add Ecpense", "");
        }

    }


    private void removeImage() {
        addExpensePi.removeExpanceImage(expenseDetails.getReceipt().get(0).getErId());
    }

    @Override
    public void imgRemoveSuccessfully() {
        remove_img.setVisibility(View.GONE);
        upload_btn.setVisibility(View.VISIBLE);
        click_here_txt.setVisibility(View.VISIBLE);
        upload_Img.setVisibility(View.GONE);
        rmvImg = true;
    }

    private void callApiWithValidations(AddExpenseReq addExpenseReq) {
        if (addExpensePi.validExpenseName(edt_expense_nm.getText().toString().trim()
                , edt_expense_amount.getText().toString().trim())) {
            if (JOBCHECK) {// && !auto_job.getText().toString().trim().equals("")
                if (addExpensePi.jobValidation(jobId)) {
                    addExpensePi.addExpense(addExpenseReq);
                }
            } else if (CLIENTCHECK) {//&& !auto_client.getText().toString().trim().equals("")
                if (addExpensePi.clientValidation(cltId)) {
                    addExpensePi.addExpense(addExpenseReq);
                }
            } else {
                addExpensePi.addExpense(addExpenseReq);
            }
        }
    }


    private void SelectStartDate() {
        Calendar myCalendar = Calendar.getInstance();
        int year = myCalendar.get(Calendar.YEAR);
        int month = myCalendar.get(Calendar.MONTH);
        int dayOfMonth = myCalendar.get(Calendar.DAY_OF_MONTH);
        DatePickerDialog datePickerDialog = new DatePickerDialog(AddExpenseActivity.this, datePickerListener, year, month, dayOfMonth);
        datePickerDialog.show();
    }

    @Override
    public void onCheckedChanged(RadioGroup group, int checkedId) {
        switch (checkedId) {
            case R.id.radio_none:
                hideJobClientView();
                break;
            case R.id.radio_job:
                Job_Client_View = true;
                showJobView();
                break;
            case R.id.radio_client:
                Job_Client_View = false;
                showJobView();
                break;

        }
    }

    private void showJobView() {
        if (Job_Client_View) {
            cltId = "";
            CLIENTCHECK = false;
            JOBCHECK = true;
            job_layout.setVisibility(View.VISIBLE);
            job_divider.setVisibility(View.VISIBLE);
            clients_layout.setVisibility(View.GONE);
            client_divider.setVisibility(View.GONE);
        } else {
            jobId = "";
            CLIENTCHECK = true;
            JOBCHECK = false;
            job_layout.setVisibility(View.GONE);
            job_divider.setVisibility(View.GONE);
            client_divider.setVisibility(View.VISIBLE);
            clients_layout.setVisibility(View.VISIBLE);
        }

    }

    /**
     * select NONe check box
     **/
    private void hideJobClientView() {
        job_divider.setVisibility(View.GONE);
        client_divider.setVisibility(View.GONE);
        job_layout.setVisibility(View.GONE);
        clients_layout.setVisibility(View.GONE);
        jobId = cltId = "";
        JOBCHECK = CLIENTCHECK = false;
    }

    @Override
    public void addExpenseSuccesFully() {
        EotApp.getAppinstance().showToastmsg(LanguageController.getInstance().getMobileMsgByKey(AppConstant.expense_added));
        Intent intent = new Intent();
        setResult(MainActivity.ADDEXPENSES, intent);
        this.finish();
    }


    @Override
    public void sessionExpire(String msg) {
        AppUtility.alertDialog(this, LanguageController.getInstance().getMobileMsgByKey(AppConstant.dialog_error_title)
                , msg, LanguageController.getInstance().getMobileMsgByKey(AppConstant.ok), "",
                new Callable<Boolean>() {
                    @Override
                    public Boolean call() throws Exception {
                        EotApp.getAppinstance().sessionExpired();
                        return null;
                    }
                });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            onBackPressed();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence charSequence, int start, int before, int count) {
        if (charSequence.length() >= 1) {
            if (charSequence.hashCode() == edt_expense_nm.getText().hashCode())
                expense_nm_layout.setHintEnabled(true);
            if (charSequence.hashCode() == edt_expense_amount.getText().hashCode()) {
                expense_amount_layout.setHintEnabled(true);
                roundOffAmount();
            }
            if (charSequence.hashCode() == edt_expense_desc.getText().hashCode()) {
                expense_desc_layout.setHintEnabled(true);
                edt_expense_amount.setText(amount + "");
            }

//            roundOffAmount();
        } else if (charSequence.length() <= 0) {
            if (charSequence.hashCode() == edt_expense_nm.getText().hashCode())
                expense_nm_layout.setHintEnabled(false);
            if (charSequence.hashCode() == edt_expense_amount.getText().hashCode()) {
                expense_amount_layout.setHintEnabled(false);
                roundOffAmount();
            }
            if (charSequence.hashCode() == edt_expense_desc.getText().hashCode()) {
                expense_desc_layout.setHintEnabled(false);
                // edt_expense_amount.setText(amount + "");
            }
//            roundOffAmount();
        }

    }


    @Override
    public void afterTextChanged(Editable s) {

    }


    @Override
    public void onDocumentSelected(String path, boolean isImage) {
        super.onDocumentSelected(path, isImage);
        try {
            if (!path.isEmpty()) {
                image_path = path;
                upload_Img.setVisibility(View.VISIBLE);
                upload_Img.setImageURI(Uri.fromFile(new File(path)));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
