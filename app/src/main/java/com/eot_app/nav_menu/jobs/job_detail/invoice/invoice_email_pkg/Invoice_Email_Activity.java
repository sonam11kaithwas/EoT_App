package com.eot_app.nav_menu.jobs.job_detail.invoice.invoice_email_pkg;

import android.graphics.Color;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.eot_app.R;
import com.eot_app.eoteditor.EotEditor;
import com.eot_app.nav_menu.jobs.job_detail.invoice.invoice_email_pkg.get_email_temp_model.Get_Email_ReS_Model;
import com.eot_app.nav_menu.jobs.job_detail.invoice.invoice_email_pkg.get_email_temp_model.InvoiceEmaliTemplate;
import com.eot_app.nav_menu.jobs.job_detail.invoice.invoice_email_pkg.send_email_temp_model.Send_Email_ReS_Model;
import com.eot_app.utility.AppConstant;
import com.eot_app.utility.AppUtility;
import com.eot_app.utility.App_preference;
import com.eot_app.utility.language_support.LanguageController;
import com.google.android.material.textfield.TextInputLayout;
import com.hypertrack.hyperlog.HyperLog;

import java.util.ArrayList;
import java.util.concurrent.Callable;

public class Invoice_Email_Activity extends AppCompatActivity implements View.OnClickListener, Invoice_Email_View, TextWatcher {

    EotEditor editor;
    private String tempId = "";
    private EditText edt_email_to, edt_email_cc, edt_email_subject/*, edt_email_message*/;
    private Button btn_send_email;
    private Invoice_Email_pi invoice_email_pi;
    private String invId, quotId, appId, jobId;
    private TextInputLayout input_layout_email_to, input_layout_email_cc, input_layout_email_subject, input_layout_email_message;
    private Get_Email_ReS_Model email_reS_model;
    private String isProformaInv = "0";
    private Spinner email_template_dp;
    private TextView email_temp_txt;
    private ArrayList<InvoiceEmaliTemplate> templateList = new ArrayList<>();
    private RelativeLayout rl;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_invoice_email);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        setTitle(LanguageController.getInstance().getMobileMsgByKey(AppConstant.email_invoice));
        invoice_email_pi = new Invoice_Email_pc(this);
        findViews();
        Bundle bundle = getIntent().getExtras();
        if (getIntent().hasExtra("invId")) {
            invId = bundle.getString("invId");
            if (bundle.getString("isShowInList") != null) {
                if (bundle.getString("isShowInList").equals("0"))
                    isProformaInv = "1";
                else isProformaInv = "0";
            }
            HyperLog.i("", "invoice intent received:" + invId);

        }
        if (getIntent().hasExtra("quotId")) {
            quotId = bundle.getString("quotId");
            HyperLog.i("", "quotation intent received:" + quotId);

        }
        if (getIntent().hasExtra("appId")) {
            appId = bundle.getString("appId");
            HyperLog.i("", "appointment intent received:" + appId);

        }
        if (getIntent().hasExtra("jobId")) {
            jobId = bundle.getString("jobId");
            HyperLog.i("", "Job intent received:" + jobId);

        }

        if (invId != null) {
            setTitle(LanguageController.getInstance().getMobileMsgByKey(AppConstant.email_invoice));
            invoice_email_pi.getJobInvoicetemplateList();
            invoice_email_pi.getInvoiceEmailTempApi(invId, isProformaInv);
        } else if (quotId != null) {
            setTitle(LanguageController.getInstance().getMobileMsgByKey(AppConstant.email_quotes));
            invoice_email_pi.getQuotesInvoicetemplateList();
            invoice_email_pi.getQuotationEmailTemplate(quotId);
        } else if (appId != null) {
            setTitle(LanguageController.getInstance().getMobileMsgByKey(AppConstant.email_document));
            invoice_email_pi.getJobDocEmailTemplate(appId);
        } else if (jobId != null) {
            setTitle(LanguageController.getInstance().getMobileMsgByKey(AppConstant.email_job_card));
            invoice_email_pi.getJobCardetemplateList();
            invoice_email_pi.getJobCardEmailTemplate(jobId);
        }
    }

    @Override
    public void setInvoiceTmpList(final ArrayList<InvoiceEmaliTemplate> templateList) {
        this.templateList = templateList;
        AppUtility.spinnerPopUpWindow(email_template_dp);
        email_template_dp.setAdapter(new EmailTmpAdpter(this, templateList));
        rl.setVisibility(View.VISIBLE);
        if (templateList != null && templateList.size() > 0) {
            for (InvoiceEmaliTemplate model : templateList) {
                if (model.getDefaultTemp() != null && model.getDefaultTemp().equals("1")) {
                    email_temp_txt.setText(model.getInputValue());
                    tempId = model.getInvTempId();
                    break;
                }
            }
        }


        email_template_dp.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                email_temp_txt.setText(templateList.get(position).getInputValue());
                tempId = templateList.get(position).getInvTempId();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    private void findViews() {
        editor = findViewById(R.id.eot_editor);

        editor.setTextColor(Color.parseColor("#8C9293"));
        editor.setBackgroundColor(Color.TRANSPARENT);
        editor.focusEditor();

        edt_email_to = findViewById(R.id.edt_email_to);
        edt_email_to.setHint(LanguageController.getInstance().getMobileMsgByKey(AppConstant.to) + " *");

        edt_email_cc = findViewById(R.id.edt_email_cc);
        edt_email_cc.setHint(LanguageController.getInstance().getMobileMsgByKey(LanguageController.getInstance().getMobileMsgByKey(AppConstant.cc)));

        edt_email_subject = findViewById(R.id.edt_email_subject);
        edt_email_subject.setHint(LanguageController.getInstance().getMobileMsgByKey(AppConstant.subject) + " *");


        btn_send_email = findViewById(R.id.btn_send_email);
        input_layout_email_to = findViewById(R.id.input_layout_email_to);
        input_layout_email_cc = findViewById(R.id.input_layout_email_cc);
        input_layout_email_subject = findViewById(R.id.input_layout_email_subject);
        input_layout_email_message = findViewById(R.id.input_layout_email_message);

        input_layout_email_to.getEditText().addTextChangedListener(this);
        input_layout_email_cc.getEditText().addTextChangedListener(this);
        input_layout_email_subject.getEditText().addTextChangedListener(this);
        input_layout_email_message.getEditText().addTextChangedListener(this);


        email_template_dp = findViewById(R.id.email_template_dp);
        email_temp_txt = findViewById(R.id.email_temp_txt);
        rl = findViewById(R.id.rl);

        email_temp_txt.setOnClickListener(this);

        btn_send_email.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.email_temp_txt:
                email_template_dp.performClick();
                break;
            case R.id.btn_send_email:
                String emailTo = edt_email_to.getText().toString().trim();
                String emailCc = edt_email_cc.getText().toString().trim();
                String emailSubject = edt_email_subject.getText().toString().trim();
                String emailMessage = editor.getHtml();
                if (invoice_email_pi.isInputFieldDataValid(emailTo, emailCc, emailSubject, emailMessage)) {
                    String messageInHtml = emailMessage.replaceAll("\n", "<br>");
                    invoice_email_pi.sendInvoiceEmailTempApi(invId,
                            App_preference.getSharedprefInstance().getLoginRes().getCompId(),
                            messageInHtml,
                            emailSubject,
                            emailTo,
                            emailCc,
                            isProformaInv, tempId);
                }
                break;
        }
    }

    @Override
    public void onGetEmailTempData(Get_Email_ReS_Model email_reS_model) {
        if (email_reS_model.getTo() != null && !email_reS_model.getTo().equals("")) {
            edt_email_to.setText(email_reS_model.getTo());
        }
        if (email_reS_model.getSubject() != null && !email_reS_model.getSubject().equals("")) {
            edt_email_subject.setText(email_reS_model.getSubject());
        }
        if (email_reS_model.getMessage() != null && !email_reS_model.getMessage().equals("")) {
            editor.setHtml(email_reS_model.getMessage());
        }
        this.email_reS_model = email_reS_model;

    }

    @Override
    public void onSendInvoiceEmail(Send_Email_ReS_Model email_reS_model) {
        AppUtility.alertDialog(this, "", LanguageController.getInstance().getServerMsgByKey(email_reS_model.getMessage()), LanguageController.getInstance().getMobileMsgByKey(AppConstant.ok), "", new Callable<Boolean>() {
            @Override
            public Boolean call() throws Exception {
                finish();
                return null;
            }
        });
        AppUtility.hideSoftKeyboard(Invoice_Email_Activity.this);

    }

    @Override
    public void showErrorAlertDialog(String error) {

        showDialog(error);
    }

    @Override
    public void setSessionExpire(String msg) {
        showDialog(msg);
    }

    private void showDialog(String error) {
        AppUtility.alertDialog(this, "", error, LanguageController.getInstance().getMobileMsgByKey(AppConstant.ok), "", new Callable<Boolean>() {
            @Override
            public Boolean call() throws Exception {
                return null;
            }
        });
    }

    @Override
    public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

    }

    @Override
    public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
        if (charSequence.length() >= 1) {
            if (charSequence.hashCode() == edt_email_to.getText().hashCode()) {
                input_layout_email_to.setHintEnabled(true);
            }
            if (charSequence.hashCode() == edt_email_cc.getText().hashCode()) {
                input_layout_email_cc.setHintEnabled(true);
            }
            if (charSequence.hashCode() == edt_email_subject.getText().hashCode()) {
                input_layout_email_subject.setHintEnabled(true);
            }

        } else if (charSequence.length() <= 0) {
            if (charSequence.hashCode() == edt_email_to.getText().hashCode()) {
                input_layout_email_to.setHintEnabled(false);
            }
            if (charSequence.hashCode() == edt_email_cc.getText().hashCode()) {
                input_layout_email_cc.setHintEnabled(false);
            }
            if (charSequence.hashCode() == edt_email_subject.getText().hashCode()) {
                input_layout_email_subject.setHintEnabled(false);
            }

        }
    }

    @Override
    public void afterTextChanged(Editable editable) {

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.send_mail_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.send_email:
                String emailTo = edt_email_to.getText().toString().trim();
                String emailCc = edt_email_cc.getText().toString().trim();
                String emailSubject = edt_email_subject.getText().toString().trim();
                String emailMessage = editor.getHtml();

                if (invoice_email_pi.isInputFieldDataValid(emailTo, emailCc, emailSubject, emailMessage)) {
                    String messageInHtml = emailMessage.replaceAll("\n", "<br>");
                    if (invId != null) {
                        invoice_email_pi.sendInvoiceEmailTempApi(invId,
                                App_preference.getSharedprefInstance().getLoginRes().getCompId(),
                                messageInHtml,
                                emailSubject,
                                emailTo,
                                emailCc, isProformaInv, tempId);
                    } else if (quotId != null) {
                        invoice_email_pi.sendQuotationEmailTemplate(quotId,
                                messageInHtml,
                                emailSubject,
                                emailTo,
                                emailCc,
                                "",
                                email_reS_model.getFrom(),
                                email_reS_model.getFromnm(), tempId);
                    } else if (appId != null) {
                        invoice_email_pi.sendJObDocEmailTemplate(appId,
                                getIntent().getStringExtra("pdfPath"),
                                messageInHtml,
                                emailSubject,
                                emailTo,
                                emailCc,
                                "",
                                email_reS_model.getFrom(),
                                email_reS_model.getFromnm()
                        );
                    } else if (jobId != null) {
                        invoice_email_pi.sendJobCardEmailTemplate(jobId,
                                getIntent().getStringExtra("pdfPath"),
                                messageInHtml,
                                emailSubject,
                                emailTo,
                                emailCc, tempId);
                    }
                }
                break;
            case android.R.id.home:
                AppUtility.hideSoftKeyboard(Invoice_Email_Activity.this);
                onBackPressed();
                break;
        }
        return super.onOptionsItemSelected(item);
    }


}
