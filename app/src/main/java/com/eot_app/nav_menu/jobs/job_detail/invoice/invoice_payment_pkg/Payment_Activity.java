package com.eot_app.nav_menu.jobs.job_detail.invoice.invoice_payment_pkg;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.eot_app.R;
import com.eot_app.nav_menu.client.clientlist.client_detail.site.sitelist.editsite.editsitedb.SpinnerCountrySite;
import com.eot_app.nav_menu.jobs.job_detail.invoice.invoice_detail_pkg.inv_detail_model.Inv_Res_Model;
import com.eot_app.nav_menu.jobs.job_detail.invoice.invoice_payment_pkg.payment_mvp.PayMent_View;
import com.eot_app.nav_menu.jobs.job_detail.invoice.invoice_payment_pkg.payment_mvp.Payment_pc;
import com.eot_app.nav_menu.jobs.job_detail.invoice.invoice_payment_pkg.payment_mvp.Payment_pi;
import com.eot_app.utility.AppConstant;
import com.eot_app.utility.AppUtility;
import com.eot_app.utility.App_preference;
import com.eot_app.utility.Currency;
import com.eot_app.utility.EotApp;
import com.eot_app.utility.language_support.LanguageController;
import com.eot_app.utility.util_interfaces.Callback_AlertDialog;
import com.google.android.material.textfield.TextInputLayout;

import java.util.Calendar;
import java.util.List;
import java.util.concurrent.Callable;

public class Payment_Activity extends AppCompatActivity implements View.OnClickListener, AdapterView.OnItemSelectedListener, TextWatcher, PayMent_View {
    final Calendar myCalendar = Calendar.getInstance();
    final DatePickerDialog.OnDateSetListener date = new DatePickerDialog.OnDateSetListener() {
        @Override
        public void onDateSet(DatePicker view, int year, int monthOfYear,
                              int dayOfMonth) {
            myCalendar.set(Calendar.YEAR, year);
            myCalendar.set(Calendar.MONTH, monthOfYear);
            myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
            txt_date_value.setText(AppUtility.getDateWithFormate2(myCalendar.getTimeInMillis(), "dd-MMM-yyyy"));
        }
    };
    Inv_Res_Model invoiceDetails;
    Spinner payment_type_spinner;
    String[] paymentType = {"Cash", "Cheque", "Credit Card", "Debit Card", "Direct Deposit"};
    String payType = "", invId = "", invNm = "";
    TextInputLayout input_layout_amount, input_layout_payment_notes;
    String jobId;
    List<Currency> currencyList;
    String[] setCurrency;
    private TextView payment_date, txtTotalAmount, txtPaidAmount, txtDueAmount, tv_payment_type, txt_date_value, totalInvoiceAmo, paidAmo, dueAmo, txtPayType;
    private Button btnPayment;
    private EditText edtAmountReceived, notesInvoice;
    private Payment_pi payment_pi;
    private LinearLayout linearPaymentType, linearPaymentDate;
    private Boolean isEmailSendOrNot = false;
    private CheckBox checkbox_payment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment);
        setTitle(LanguageController.getInstance().getMobileMsgByKey(AppConstant.title_payment));

        Bundle bundle = getIntent().getExtras();
        if (getIntent().hasExtra("JobId")) {
            jobId = bundle.getString("JobId");
        }
        initializelables();
        findViews();
        setTextData();

        payment_pi = new Payment_pc(this);
        payment_pi.getInvoiceDetails(jobId);

        AppUtility.spinnerPopUpWindow(payment_type_spinner);
        PaymentSpinnerAdapter adapter = new PaymentSpinnerAdapter(this, paymentType);
        payment_type_spinner.setAdapter(adapter);
        payment_type_spinner.setOnItemSelectedListener(this);
    }

    @Override
    public void onPaymentSuccess(String message) {
        /*  final Payment_MyDialog mydialog = new Payment_MyDialog(this, new Payment_MyDialog.myOnClickListener() {
          @Override
            public void onButtonClick() {
                onBackPressed();
            }
        });
        mydialog.setCancelable(false);
        mydialog.show();*/

        EotApp.getAppinstance().showToastmsg(message);
        finish();
        AppUtility.hideSoftKeyboard(Payment_Activity.this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.linearPaymentDate:
                new DatePickerDialog(this, date, myCalendar
                        .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                        myCalendar.get(Calendar.DAY_OF_MONTH)).show();
                break;
            case R.id.invoice_save_btn:
                final String amount = edtAmountReceived.getText().toString().trim();
                final String notes = notesInvoice.getText().toString().trim();
                if (payment_pi.isInputFieldDataValid(amount, payType, invoiceDetails)) {

                    if (App_preference.getSharedprefInstance().getLoginRes().getConfirmationTrigger() != null) {
                        //for (String trigger : App_preference.getSharedprefInstance().getLoginRes().getConfirmationTrigger()) {
                        if (App_preference.getSharedprefInstance().getLoginRes().getConfirmationTrigger().contains("11")) {
                            AppUtility.alertDialog2(this,
                                    "",
                                    LanguageController.getInstance().getMobileMsgByKey(AppConstant.send_client_mail),
                                    LanguageController.getInstance().getMobileMsgByKey(AppConstant.yes),
                                    LanguageController.getInstance().getMobileMsgByKey(AppConstant.no), new Callback_AlertDialog() {
                                        @Override
                                        public void onPossitiveCall() {
                                            payment_pi.payment_invoice_apicall(amount, notes,
                                                    invId, invNm,
                                                    payType, myCalendar.getTimeInMillis(),
                                                    isEmailSendOrNot, jobId, "1");
                                        }

                                        @Override
                                        public void onNegativeCall() {
                                            payment_pi.payment_invoice_apicall(amount, notes,
                                                    invId, invNm,
                                                    payType, myCalendar.getTimeInMillis(),
                                                    isEmailSendOrNot, jobId, "0");
                                        }
                                    });
                            // break;
                            //}
                        } else {
                            notMailStatusFound(amount, notes);
                        }
                    } else {
                        notMailStatusFound(amount, notes);
                    }

//                    payment_pi.payment_invoice_apicall(amount, notes,
//                            invId, invNm,
//                            payType, myCalendar.getTimeInMillis(),
//                            isEmailSendOrNot, jobId);
                }
                break;
            case R.id.paymentType:
                payment_type_spinner.performClick();
                break;
        }
    }

    private void notMailStatusFound(String amount, String notes) {
        payment_pi.payment_invoice_apicall(amount, notes,
                invId, invNm,
                payType, myCalendar.getTimeInMillis(),
                isEmailSendOrNot, jobId, "1");
    }

    private void initializelables() {
        totalInvoiceAmo = findViewById(R.id.totalInvoiceAmo);
        totalInvoiceAmo.setText(LanguageController.getInstance().getMobileMsgByKey(AppConstant.total_inv_amt));
        paidAmo = findViewById(R.id.paidAmo);
        paidAmo.setText(LanguageController.getInstance().getMobileMsgByKey(AppConstant.paid_amt));
        dueAmo = findViewById(R.id.dueAmo);
        dueAmo.setText(LanguageController.getInstance().getMobileMsgByKey(AppConstant.due_amt));
        payment_date = findViewById(R.id.txt_payment_date);
        payment_date.setText(LanguageController.getInstance().getMobileMsgByKey(AppConstant.pay_date));
        txtTotalAmount = findViewById(R.id.txtTotalAmount);
        txtPaidAmount = findViewById(R.id.txtPaidAmount);
        txtDueAmount = findViewById(R.id.txtDueAmount);
        btnPayment = findViewById(R.id.invoice_save_btn);
        btnPayment.setText(LanguageController.getInstance().getMobileMsgByKey(AppConstant.save_btn));
        edtAmountReceived = findViewById(R.id.edt_amount_received);
        edtAmountReceived.setHint(LanguageController.getInstance().getMobileMsgByKey(AppConstant.amt));
        notesInvoice = findViewById(R.id.edt_payment_notes);
        notesInvoice.setHint(LanguageController.getInstance().getMobileMsgByKey(AppConstant.notes));
        payment_type_spinner = findViewById(R.id.payment_type_spinner);
        payment_type_spinner = findViewById(R.id.payment_type_spinner);
        linearPaymentType = findViewById(R.id.paymentType);
        tv_payment_type = findViewById(R.id.txt_payment_type);
        tv_payment_type.setText(LanguageController.getInstance().getMobileMsgByKey(AppConstant.lable_pay_type));
        input_layout_amount = findViewById(R.id.input_layout_amount);
        input_layout_payment_notes = findViewById(R.id.input_layout_payment_notes);
        txt_date_value = findViewById(R.id.txt_date_value);
        linearPaymentDate = findViewById(R.id.linearPaymentDate);
        txtPayType = findViewById(R.id.txtPayType);
        txtPayType.setText(LanguageController.getInstance().getMobileMsgByKey(AppConstant.lable_pay_type));

        checkbox_payment = findViewById(R.id.checkbox_payment);
        checkbox_payment.setText(LanguageController.getInstance().getMobileMsgByKey(AppConstant.send_payment_reciept));
        checkbox_payment.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                isEmailSendOrNot = isChecked;
            }
        });

        /****set  Default Curreny*****/
        txtTotalAmount.setText(SpinnerCountrySite.getCourrencyNamebyId(App_preference.getSharedprefInstance().getCompanySettingsDetails().getCur()) + AppUtility.getRoundoff_amount(String.valueOf(0)));
        txtPaidAmount.setText(SpinnerCountrySite.getCourrencyNamebyId(App_preference.getSharedprefInstance().getCompanySettingsDetails().getCur()) + AppUtility.getRoundoff_amount(String.valueOf(0)));
        txtDueAmount.setText(SpinnerCountrySite.getCourrencyNamebyId(App_preference.getSharedprefInstance().getCompanySettingsDetails().getCur()) + AppUtility.getRoundoff_amount(String.valueOf(0)));

    }

    private void findViews() {
        input_layout_amount.getEditText().addTextChangedListener(this);
        input_layout_payment_notes.getEditText().addTextChangedListener(this);

        linearPaymentType.setOnClickListener(this);
        btnPayment.setOnClickListener(this);
        linearPaymentDate.setOnClickListener(this);
    }

    private void setTextData() {
        tv_payment_type.setText(paymentType[0]);
        payType = "1";
        txt_date_value.setText(AppUtility.getDateWithFormate2(myCalendar.getTimeInMillis(), "dd-MMM-yyyy"));
    }

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
        tv_payment_type.setText(paymentType[i]);
        payType = String.valueOf(adapterView.getSelectedItemPosition() + 1);
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }

    @Override
    public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

    }

    @Override
    public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
        if (charSequence.length() >= 1) {
            if (charSequence.hashCode() == edtAmountReceived.getText().hashCode()) {
                input_layout_amount.setHintEnabled(true);
            }
            if (charSequence.hashCode() == notesInvoice.getText().hashCode()) {
                input_layout_payment_notes.setHintEnabled(true);
            }
        } else if (charSequence.length() <= 0) {
            if (charSequence.hashCode() == edtAmountReceived.getText().hashCode()) {
                input_layout_amount.setHintEnabled(false);
            }
            if (charSequence.hashCode() == notesInvoice.getText().hashCode()) {
                input_layout_payment_notes.setHintEnabled(false);
            }
        }
    }

    @Override
    public void afterTextChanged(Editable editable) {

    }

    @Override
    public void setData(Inv_Res_Model invoice_Details) {
        this.invoiceDetails = invoice_Details;
        if (invoiceDetails != null && invoiceDetails.getInvId() != null) {
            invId = invoiceDetails.getInvId();
            invNm = invoiceDetails.getNm();
        }
        //currencyList("currency.json");
        double remainingAmount = 0, totalAmount = 0, paidAmount = 0;
        if (invoice_Details != null) {
            if (invoice_Details.getTotal() != null && !invoice_Details.getTotal().equals("")) {

                try {
                    totalAmount = Double.parseDouble(invoice_Details.getTotal());
                    txtTotalAmount.setText(invoice_Details.getCurSym() + AppUtility.getRoundoff_amount(String.valueOf(totalAmount)));
                } catch (NumberFormatException e) {
                    e.printStackTrace();
                }
            }
            if (invoice_Details.getPaid() != null && !invoice_Details.getPaid().equals("")) {
                try {
                    paidAmount = Double.parseDouble(invoice_Details.getPaid());
                    txtPaidAmount.setText(invoice_Details.getCurSym() + AppUtility.getRoundoff_amount(String.valueOf(paidAmount)));
                } catch (NumberFormatException e) {
                    e.printStackTrace();
                }
            }

            remainingAmount = totalAmount - paidAmount;
            /**
             * live fix 2.32 null currency showing
             **/
            if (invoice_Details != null && !TextUtils.isEmpty(invoice_Details.getCurSym()))
                txtDueAmount.setText(invoice_Details.getCurSym() + AppUtility.getRoundoff_amount(String.valueOf(remainingAmount)));

        }
    }

    @Override
    public void onInvoiceSuccessFalse(String errorMessage) {
        AppUtility.alertDialog(this, "", errorMessage, LanguageController.getInstance().getMobileMsgByKey(AppConstant.ok), "", new Callable<Boolean>() {
            @Override
            public Boolean call() throws Exception {
                finish();
                return null;
            }
        });
    }

    @Override
    public void showErrorAlertDialog(String errorMessage) {
        AppUtility.alertDialog(this, "", errorMessage, LanguageController.getInstance().getMobileMsgByKey(AppConstant.ok), "", new Callable<Boolean>() {
            @Override
            public Boolean call() throws Exception {
                return null;
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                AppUtility.hideSoftKeyboard(Payment_Activity.this);
                /**
                 * live fix 2.32 null currency showing
                 **/
                onBackPressed();
                break;
        }
        /**
         * live fix 2.32 null currency showing
         **/
        return true;
    }
    //    public void currencyList(String text) {
//        try {
//            InputStream is = Payment_Activity.this.getResources().getAssets().open(text);
//            int size = is.available();
//            byte[] data = new byte[size];
//            is.read(data);
//            is.close();
//            String json = new String(data);
//            JSONObject countries = new JSONObject(json);
//            JSONArray jsinState = countries.getJSONArray("data");
//            if (jsinState != null) {
//                for (int i = 0; i < jsinState.length(); i++) {
//                    JSONObject currenctText = jsinState.getJSONObject(i);
//                    if(invoiceDetails.getCur().equals(currenctText.getString("id")))
//                    {
//                        setCurrency = currenctText.getString("text").split("-");
//                    }
//
//                }
//            }
//
//        } catch (IOException e) {
//            e.printStackTrace();
//        } catch (JSONException je) {
//            Log.e("Error", je.getMessage());
//            je.printStackTrace();
//        }
//
//    }
}
