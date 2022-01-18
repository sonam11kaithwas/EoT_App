package com.eot_app.nav_menu.quote.quotes_add_item_pkg;

import android.app.Dialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.eot_app.R;
import com.eot_app.login_next.login_next_model.CompPermission;
import com.eot_app.nav_menu.jobs.job_detail.invoice.Auto_Inventry_Adpter;
import com.eot_app.nav_menu.jobs.job_detail.invoice.add_edit_invoice_pkg.dropdown_item_pkg.Auto_Fieldworker_Adpter;
import com.eot_app.nav_menu.jobs.job_detail.invoice.add_edit_invoice_pkg.dropdown_item_pkg.Services_item_Adapter;
import com.eot_app.nav_menu.jobs.job_detail.invoice.inventry_pkg.Inventry_ReS_Model;
import com.eot_app.nav_menu.jobs.job_detail.invoice.invoice_detail_pkg.inv_detail_model.Tax;
import com.eot_app.nav_menu.quote.add_quotes_pkg.model_pkg.Update_Quote_ReQ;
import com.eot_app.nav_menu.quote.quote_invoice_pkg.quote_model_pkg.Quote_ItemData;
import com.eot_app.nav_menu.quote.quote_invoice_pkg.quote_model_pkg.QuotesDetails;
import com.eot_app.nav_menu.quote.quotes_add_item_pkg.item_add_mvp.QuoteItemAdd_PC;
import com.eot_app.nav_menu.quote.quotes_add_item_pkg.item_add_mvp.QuoteItemAdd_PI;
import com.eot_app.nav_menu.quote.quotes_add_item_pkg.item_add_mvp.QuoteItemAdd_View;
import com.eot_app.nav_menu.quote.quotes_add_item_pkg.item_model_pkg.AddItem_Model;
import com.eot_app.utility.AppConstant;
import com.eot_app.utility.AppUtility;
import com.eot_app.utility.App_preference;
import com.eot_app.utility.EotApp;
import com.eot_app.utility.db.AppDataBase;
import com.eot_app.utility.language_support.LanguageController;
import com.eot_app.utility.settings.jobtitle.TaxData;
import com.eot_app.utility.settings.setting_db.FieldWorker;
import com.eot_app.utility.settings.setting_db.JobTitle;
import com.eot_app.utility.util_interfaces.Callback_AlertDialog;
import com.google.android.material.textfield.TextInputLayout;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.Callable;

public class AddQutesItem_Activity extends AppCompatActivity implements TextWatcher, View.OnClickListener, View.OnFocusChangeListener, QuoteItemAdd_View {
    double taxAmount = 0;
    private float total_tax = 0f;
    private AutoCompleteTextView autocomplete_item;
    private List<Tax> listFilter = new ArrayList<>();
    private EditText edt_item_description, edt_item_qty, edt_item_rate, edt_item_supplier, edt_item_discount, edt_part_no, edt_unit, edt_hsnCode, edt_item_tax_rate;
    private TextInputLayout item_desc_layout, item_qty_layout, item_rate_layout, item_discount_layout, itemlayout, item_hsnCode_layout, item_partNo_layout, item_unit_layout, item_supplier_layout, serialNo_layout, item_tax_rate_layout;
    private Button add_edit_item_Btn;
    private String itemId = "";
    private Quote_ItemData ItemData_Add_Edit = null;
    private TextView item_select, fw_select, service_select;
    private String type = "0";//, itype = "0"
    private LinearLayout layout_fw_item, taxamount_layout;
    private double roundOff;
    private View nm_view, desc_view, qty_view, rate_view, supplier_view, disc_view, tax_view, amount_view, part_no_view, hsncode_view, unit_view, taxrateAmount_view, tax_rate_view;
    private TextView tax_value_txt, tax_txt_hint, amount_value_txt, taxamount_txt_hint, taxamount_value_txt, amount_txt_hint;
    private LinearLayout tax_layout, amount_layout;
    private int isfwOrItem = 1;
    private String jtId = "", inm = "", iqmmId = "";
    private boolean DP_OPEN = false;
    private boolean isMandtryItem, isMandtryFw, isMandtryServices;
    private QuoteItemAdd_PI quoteItemAdd_pi;
    private String addQuotesId, invId;
    private Quote_ItemData quote_itemData;
    private String isInvOrNoninv = "";
    private QuotesDetails quotesDetails;
    private List<Inventry_ReS_Model> inventryItemList = new ArrayList<>();
    private List<FieldWorker> fwDataList = new ArrayList<>();
    private List<JobTitle> jobServicesDataList = new ArrayList<>();
    private RelativeLayout add_quote_item_layout;
    private View seroal_no_view;
    private boolean updateItem = false;
    private boolean ITEMSYNC = false;
    private RadioGroup rediogrp;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit__invoice);
        initializelables();
        intializeViews();

        Bundle bundle = getIntent().getExtras();
        /** Add new quotes**/
        if (getIntent().hasExtra("AddQuotesItem")) {
            String detailsobject = bundle.getString("AddQuotesItem");
            quotesDetails = new Gson().fromJson(detailsobject, QuotesDetails.class);
            if (quotesDetails == null) {
                return;
            }
            invId = quotesDetails.getInvData().getInvId();
            addQuotesId = quotesDetails.getQuotId();
            setDefaultValuesForAddNewItem();
//            we have to get a new tax items from the server
            quoteItemAdd_pi.getTaxList();

            //            supplier cost editable if item found
            edt_item_supplier.setEnabled(true);

        } else if (getIntent().hasExtra("quotesDetails")) {
            /**edit quotes */
            layout_fw_item.setVisibility(View.GONE);
            String detailsobject = bundle.getString("quotesDetails");
            quotesDetails = new Gson().fromJson(detailsobject, QuotesDetails.class);
            invId = quotesDetails.getInvData().getInvId();
            addQuotesId = quotesDetails.getQuotId();
            String itemId = bundle.getString("itemId");
            if (quotesDetails != null && quotesDetails != null && quotesDetails.getInvData() != null && quotesDetails.getInvData().getItemData() != null) {
                for (Quote_ItemData itemdata : quotesDetails.getInvData().getItemData()) {
                    if (itemdata.getItemId().equals(itemId)) {
                        quote_itemData = itemdata;
                        break;
                    }
                }
            }
            quoteItemAdd_pi.getTaxList();

            try {
                if (quote_itemData.getTax().size() > 0) {
                    List<Tax> filterSelectedTaxList = quote_itemData.getTax();
                    for (Tax taxModel : listFilter) {
                        for (Tax taxselect : filterSelectedTaxList) {
                            if (taxModel.getTaxId().equals(taxselect.getTaxId())) {
                                if (Double.parseDouble(taxModel.getRate()) != Double.parseDouble(taxselect.getRate())) {
                                    taxModel.setAppliedTax(LanguageController.getInstance().getMobileMsgByKey
                                            (AppConstant.applied_tax) + " (" + taxselect.getRate() + " %)");
                                    taxModel.setSelect(true);
                                    total_tax += Float.parseFloat(taxselect.getRate());
                                } else {
                                    taxModel.setSelect(true);
                                    total_tax += Float.parseFloat(taxselect.getRate());
                                }
                                taxModel.setOldTax(taxselect.getRate());
                                updateItem = true;
                                break;
                            }
                        }
                    }
                }
            } catch (Exception ex) {
                ex.getMessage();
            }

            setItemFields(quote_itemData);
        }

        set_Title();

        autocomplete_item.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (autocomplete_item.getTag() == null) {
                    return;
                } else {
                    switch (autocomplete_item.getTag().toString()) {
                        case "Item":
                            Log.e("", "");
                            if (charSequence.length() >= 1) {
                                itemlayout.setHintEnabled(true);
                                if (inventryItemList != null &&
                                        AppDataBase.getInMemoryDatabase(EotApp.getAppinstance()).invoiceItemDao().getInventryItemList().size() == 0
                                        && autocomplete_item.getText().toString().length() >= 3) {
                                    if (!AppUtility.isInternetConnected() && !ITEMSYNC) {
                                        showDialogForLoadData();
                                        ITEMSYNC = true;
                                    } else {
                                        quoteItemAdd_pi.loadFromServer(autocomplete_item.getText().toString());
                                    }
                                    //  showDialogForLoadData();
                                }
                            } else if (charSequence.length() <= 0) {
                                inm = "";
                                itemlayout.setHintEnabled(false);
                            }


                            if (!inventryItemList.contains(charSequence)) {
                                isInvOrNoninv = "2";
                                type = "1";
                                inm = charSequence.toString();
                                itemId = "";
                                setEmptyFields();
                            }
                            isMandtryItem = true;
                            break;
                        case "Fw":
                            if (charSequence.length() >= 1) {
                                itemlayout.setHintEnabled(true);
                            } else if (charSequence.length() <= 0) {
                                itemlayout.setHintEnabled(false);
                            }
                            if (!fwDataList.contains(charSequence)) {
                                isMandtryFw = false;
                                setEmptyFields();
                            }
                            Log.e("", "");
                            break;
                        case "Services":
                            if (charSequence.length() >= 1) {
                                itemlayout.setHintEnabled(true);
                            } else if (charSequence.length() <= 0) {
                                itemlayout.setHintEnabled(false);
                            }
                            if (!jobServicesDataList.contains(charSequence)) {
                                isMandtryServices = false;
                                setEmptyFields();
                            }

                            Log.e("", "");
                            break;
                    }
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {
                Log.e("", "");
            }
        });
    }

    private void showDialogForLoadData() {
        AppUtility.alertDialog2(this,
                "", LanguageController.getInstance().getMobileMsgByKey(AppConstant.item_sync),
                LanguageController.getInstance().getMobileMsgByKey(AppConstant.ok),
                LanguageController.getInstance().getMobileMsgByKey(AppConstant.cancel), new Callback_AlertDialog() {
                    @Override
                    public void onPossitiveCall() {
                        try {
                            if (AppUtility.isInternetConnected()) {
                                quoteItemAdd_pi.loadFromServer(autocomplete_item.getText().toString());
                            } else {
                                showDisError(LanguageController.getInstance().getMobileMsgByKey(AppConstant.network_error));
                            }
                        } catch (Exception ex) {
                            ex.printStackTrace();
                        }


                    }

                    @Override
                    public void onNegativeCall() {

                    }
                });


    }

    private void initializelables() {
        itemlayout = findViewById(R.id.itemlayout);
        layout_fw_item = findViewById(R.id.layout_fw_item);
        autocomplete_item = findViewById(R.id.autocomplete_item);
        autocomplete_item.setHint(LanguageController.getInstance().getMobileMsgByKey(AppConstant.items_name) + " *");

        item_select = findViewById(R.id.item_select);
        item_select.setText(LanguageController.getInstance().getMobileMsgByKey(AppConstant.items_name));

        fw_select = findViewById(R.id.fw_select);
        fw_select.setText(LanguageController.getInstance().getMobileMsgByKey(AppConstant.fieldworkers));

        item_partNo_layout = findViewById(R.id.item_partNo_layout);
        edt_part_no = findViewById(R.id.edt_part_no);
        edt_part_no.setHint(LanguageController.getInstance().getMobileMsgByKey(AppConstant.part_no));

        item_desc_layout = findViewById(R.id.item_desc_layout);
        edt_item_description = findViewById(R.id.edt_item_desc);
        edt_item_description.setHint(LanguageController.getInstance().getMobileMsgByKey(AppConstant.item_desc));

        item_qty_layout = findViewById(R.id.item_qty_layout);
        edt_item_qty = findViewById(R.id.edt_item_qty);
        edt_item_qty.setHint(LanguageController.getInstance().getMobileMsgByKey(AppConstant.qty_hr));

        item_rate_layout = findViewById(R.id.item_rate_layout);
        edt_item_rate = findViewById(R.id.edt_item_rate);
        edt_item_rate.setHint(LanguageController.getInstance().getMobileMsgByKey(AppConstant.rate));

        item_unit_layout = findViewById(R.id.item_unit_layout);
        edt_unit = findViewById(R.id.edt_unit);
        edt_unit.setHint(LanguageController.getInstance().getMobileMsgByKey(AppConstant.unit));

        item_discount_layout = findViewById(R.id.item_discount_layout);
        edt_item_discount = findViewById(R.id.edt_item_disc);
        edt_item_discount.setHint(LanguageController.getInstance().getMobileMsgByKey(AppConstant.discount) + " (%)");

        taxamount_layout = findViewById(R.id.taxamount_layout);
        taxamount_txt_hint = findViewById(R.id.taxamount_txt_hint);
        taxamount_txt_hint.setHint(LanguageController.getInstance().getMobileMsgByKey(AppConstant.tax_amount));

        tax_layout = findViewById(R.id.tax_layout);
        tax_txt_hint = findViewById(R.id.tax_txt_hint);
        tax_txt_hint.setText(LanguageController.getInstance().getMobileMsgByKey(AppConstant.tax));

        amount_layout = findViewById(R.id.amount_layout);
        amount_value_txt = findViewById(R.id.amount_value_txt);
        amount_txt_hint = findViewById(R.id.amount_txt_hint);
        amount_txt_hint.setText(LanguageController.getInstance().getMobileMsgByKey(AppConstant.amt));

        service_select = findViewById(R.id.service_select);
        service_select.setText(LanguageController.getInstance().getMobileMsgByKey(AppConstant.services_name));

        item_supplier_layout = findViewById(R.id.item_supplier_layout);
        edt_item_supplier = findViewById(R.id.edt_item_supplier);
        edt_item_supplier.setHint(LanguageController.getInstance().getMobileMsgByKey(AppConstant.supplier_cost));

        serialNo_layout = findViewById(R.id.serialNo_layout);
        serialNo_layout.setVisibility(View.GONE);
        seroal_no_view = findViewById(R.id.seroal_no_view);
        seroal_no_view.setVisibility(View.GONE);


        item_tax_rate_layout = findViewById(R.id.item_tax_rate_layout);
        edt_item_tax_rate = findViewById(R.id.edt_item_tax_rate);
        tax_rate_view = findViewById(R.id.tax_rate_view);
        edt_item_tax_rate.setHint(LanguageController.getInstance().getMobileMsgByKey(AppConstant.rate_inclu_tax));
        edt_item_tax_rate.setEnabled(false);


        rediogrp = findViewById(R.id.rediogrp);
        rediogrp.setVisibility(View.GONE);

    }


    private void intializeViews() {
        item_hsnCode_layout = findViewById(R.id.item_hsnCode_layout);
        edt_hsnCode = findViewById(R.id.edt_hsnCode);


//        set focus change listener for round off the value.

        taxamount_value_txt = findViewById(R.id.taxamount_value_txt);

        add_edit_item_Btn = findViewById(R.id.add_edit_item_Btn);
        tax_value_txt = findViewById(R.id.tax_value_txt);


        nm_view = findViewById(R.id.nm_view);
        desc_view = findViewById(R.id.desc_view);
        qty_view = findViewById(R.id.qty_view);
        rate_view = findViewById(R.id.rate_view);
        supplier_view = findViewById(R.id.supplier_view);
        disc_view = findViewById(R.id.disc_view);
        tax_view = findViewById(R.id.tax_view);
        amount_view = findViewById(R.id.amount_view);
        taxrateAmount_view = findViewById(R.id.taxrateAmount_view);
        part_no_view = findViewById(R.id.part_no_view);
        hsncode_view = findViewById(R.id.hsncode_view);
        unit_view = findViewById(R.id.unit_view);

        add_quote_item_layout = findViewById(R.id.add_quote_item_layout);
        AppUtility.setupUI(add_quote_item_layout, AddQutesItem_Activity.this);

        item_desc_layout.getEditText().addTextChangedListener(this);
        item_qty_layout.getEditText().addTextChangedListener(this);
        item_rate_layout.getEditText().addTextChangedListener(this);
        item_supplier_layout.getEditText().addTextChangedListener(this);
        item_discount_layout.getEditText().addTextChangedListener(this);
        item_partNo_layout.getEditText().addTextChangedListener(this);
        //   item_hsnCode_layout.getEditText().addTextChangedListener(this);
        item_unit_layout.getEditText().addTextChangedListener(this);
        item_tax_rate_layout.getEditText().addTextChangedListener(this);


        add_edit_item_Btn.setOnClickListener(this);
        edt_item_description.addTextChangedListener(this);
        item_select.setOnClickListener(this);
        fw_select.setOnClickListener(this);
        service_select.setOnClickListener(this);
        tax_value_txt.setOnClickListener(this);
        edt_item_rate.setOnFocusChangeListener(this);
        edt_item_discount.setOnFocusChangeListener(this);
        amount_value_txt.setOnFocusChangeListener(this);

        autocomplete_item.setOnClickListener(this);
        itemlayout.setOnClickListener(this);

        autocomplete_item.addTextChangedListener(this);

        item_hsnCode_layout.setVisibility(View.GONE);
        hsncode_view.setVisibility(View.GONE);

        hideShowInvoiceItemDetails();

        /*****this permission for EDIT item***/
        if (App_preference.getSharedprefInstance().getLoginRes().getIsItemEditEnable().equals("1")) {
            add_edit_item_Btn.setVisibility(View.VISIBLE);
        }

        quoteItemAdd_pi = new QuoteItemAdd_PC(this);
        quoteItemAdd_pi.initialize_FwList_ServiceTittle_inventoryList();
        quoteItemAdd_pi.getInventryItemList();
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

    /**
     * tax calculation for current add/edit item
     */
    private void total_Amount_cal() {
        if (quotesDetails != null) {
            double qty = 0, rate = 0, dis = 0, suppiler = 0;
            double amount = 0;
            // check of amount calculation
            try {
                if (!edt_item_qty.getText().toString().equals("")) {
                    qty = Double.parseDouble((edt_item_qty.getText().toString()));
                }
                if (!edt_item_rate.getText().toString().equals("")) {
                    rate = Double.parseDouble(AppUtility.getRoundoff_amount(edt_item_rate.getText().toString()));
                }
                if (!edt_item_discount.getText().toString().equals("")) {
                    dis = Double.parseDouble(AppUtility.getRoundoff_amount(edt_item_discount.getText().toString()));
                }

            } catch (Exception ex) {
                ex.getMessage();
            }
            if (quotesDetails.getInvData() != null) {
                if (quotesDetails.getInvData().getTaxCalculationType().equals("0")) { //important this line
//                    amount = (qty * rate + qty * ((rate * total_tax) / 100)) - qty * ((rate * dis) / 100);

                    try {

                        double calculaterateDis = (rate * dis) / 100;
                        double newRate = rate - calculaterateDis;
                        double newAmt = (newRate * total_tax) / 100;
                        amount = newAmt + newRate;


                        amount = amount * qty;
                        taxAmount = newAmt * qty;

                        String tax_Amount = AppUtility.getRoundoff_amount(String.valueOf(taxAmount));
                        taxamount_value_txt.setText(tax_Amount);


                    } catch (Exception exception) {
                        exception.printStackTrace();
                    }
                } else if (quotesDetails.getInvData().getTaxCalculationType().equals("1")) {
                    double newRate = (rate - ((rate * dis) / 100));
                    amount = qty * (newRate + ((newRate * total_tax) / 100));


                    taxAmount = ((total_tax * rate * qty) / 100);
                    String tax_Amount = AppUtility.getRoundoff_amount(String.valueOf(taxAmount));
                    taxamount_value_txt.setText(tax_Amount);

                }
            }
            // roundOff = Math.round(amount * 100.0) / 100.0;
            String amountString = AppUtility.getRoundoff_amount(String.valueOf(amount));
            amount_value_txt.setText(amountString);


        }
    }


    private void setItemFields(Quote_ItemData itemdata) {
        ItemData_Add_Edit = itemdata;
        tax_value_txt.setText((String.valueOf(total_tax)));
        calculateTaxRate();

        if (itemdata.getType().equals("1")) { //for inventry item
            autocomplete_item.setHint(LanguageController.getInstance().getMobileMsgByKey(AppConstant.item));
            setTxtBkgColor(1);
            isMandtryItem = true;
            fw_service_filed_hide(1);

        } else if (itemdata.getType().equals("2")) {//for FieldWorker
            setTxtBkgColor(2);
            autocomplete_item.setHint(LanguageController.getInstance().getMobileMsgByKey(AppConstant.fieldworkers_name));
            isMandtryFw = true;
            isfwOrItem = 2;
            fw_service_filed_hide(2);
        }
        if (itemdata.getType().equals("3")) { //for services item
            setTxtBkgColor(2);
            autocomplete_item.setHint(LanguageController.getInstance().getMobileMsgByKey(AppConstant.services_name));
            isMandtryServices = true;
            isfwOrItem = 3;
            fw_service_filed_hide(3);
        }
        autocomplete_item.setText(itemdata.getInm());
        autocomplete_item.setFocusableInTouchMode(false);
        autocomplete_item.dismissDropDown();

        itemlayout.setHintEnabled(true);

        edt_item_qty.setText(itemdata.getQty());
        edt_item_rate.setText(AppUtility.getRoundoff_amount((itemdata.getRate())));
        edt_item_supplier.setText(AppUtility.getRoundoff_amount((itemdata.getSupplierCost())));
        edt_item_discount.setText(itemdata.getDiscount());
        edt_item_description.setText(itemdata.getDes());
        edt_part_no.setText(itemdata.getPno());
        edt_hsnCode.setText(itemdata.getHsncode());
        edt_unit.setText(itemdata.getUnit());

        itemId = itemdata.getItemId();
        iqmmId = itemdata.getIqmmId();
        type = itemdata.getType();
    }

    private void set_Title() {
        if (ItemData_Add_Edit == null) {
            getSupportActionBar().setTitle(LanguageController.getInstance().getMobileMsgByKey(AppConstant.addItem_screen_title));
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            add_edit_item_Btn.setText(LanguageController.getInstance().getMobileMsgByKey(AppConstant.save_btn));
        } else {
            /** set update title  */
            //  if (App_preference.getSharedprefInstance().getLoginRes().getCompPermission().get(0).getIsItemEnable().equals("0")) {
            if (App_preference.getSharedprefInstance().getLoginRes().getIsItemEditEnable().equals("1")) {
                getSupportActionBar().setTitle(LanguageController.getInstance().getMobileMsgByKey(AppConstant.update_item));
                getSupportActionBar().setDisplayHomeAsUpEnabled(true);
                add_edit_item_Btn.setText(LanguageController.getInstance().getMobileMsgByKey(AppConstant.update_btn));
                DP_OPEN = true;
            } else {
                EnableDisbleFields();
                getSupportActionBar().setTitle(LanguageController.getInstance().getMobileMsgByKey(AppConstant.view_details));
                getSupportActionBar().setDisplayHomeAsUpEnabled(true);
                add_edit_item_Btn.setVisibility(View.GONE);
            }
        }
    }

    private void hideShowInvoiceItemDetails() {
        CompPermission compPermission = App_preference.getSharedprefInstance().getLoginRes().getCompPermission().get(0);
        if (compPermission.getModuleId().equals("1")) {
            if (compPermission.getDescription().equals("1")) {
                item_desc_layout.setVisibility(View.GONE);
                desc_view.setVisibility(View.GONE);
            }
            if (compPermission.getRate().equals("1")) {
                item_rate_layout.setVisibility(View.GONE);
                rate_view.setVisibility(View.GONE);
            }
            if (compPermission.getSupplierCost().equals("1")) {
                item_supplier_layout.setVisibility(View.GONE);
                supplier_view.setVisibility(View.GONE);
            }
            if (compPermission.getDiscount().equals("1")) {
                item_discount_layout.setVisibility(View.GONE);
                disc_view.setVisibility(View.GONE);
            }
            if (compPermission.getTax().equals("1")) {
                tax_layout.setVisibility(View.GONE);
                tax_view.setVisibility(View.GONE);
            }
            if (compPermission.getAmount().equals("1")) {
                amount_layout.setVisibility(View.GONE);
                amount_view.setVisibility(View.GONE);
            }
            if (compPermission.getPno().equals("1")) {
                item_partNo_layout.setVisibility(View.GONE);
                part_no_view.setVisibility(View.GONE);
            }
            if (compPermission.getHsncode().equals("1")) {
                item_hsnCode_layout.setVisibility(View.GONE);
                hsncode_view.setVisibility(View.GONE);
            }
            if (compPermission.getUnit().equals("1")) {
                item_unit_layout.setVisibility(View.GONE);
                unit_view.setVisibility(View.GONE);
            }
            if (compPermission.getTaxamnt().equals("1")) {
                taxamount_layout.setVisibility(View.GONE);
                taxrateAmount_view.setVisibility(View.GONE);
            }

            if (compPermission.getIsRateIncludingTax().equals("1")) {
                item_tax_rate_layout.setVisibility(View.GONE);
                tax_rate_view.setVisibility(View.GONE);
            }
        }
    }


    private void EnableDisbleFields() {
        autocomplete_item.setEnabled(false);
        edt_part_no.setEnabled(false);
        edt_unit.setEnabled(false);
        edt_item_description.setEnabled(false);
        edt_item_qty.setEnabled(false);
        edt_item_rate.setEnabled(false);
        edt_item_supplier.setEnabled(false);
        edt_unit.setEnabled(false);
        edt_item_discount.setEnabled(false);
        tax_value_txt.setEnabled(false);
        amount_value_txt.setEnabled(false);
        edt_item_discount.setEnabled(false);
        taxamount_value_txt.setEnabled(false);
        edt_hsnCode.setEnabled(false);
    }

    @Override
    public void setItemAdded() {//Quote_ItemData itemAdded
        this.finish();
    }

    private void setDefaultValuesForAddNewItem() {
        edt_item_qty.setText("1"); // quantity always be 1 initially
        edt_item_rate.setText("0");
        edt_item_supplier.setText("0");
        edt_item_discount.setText("0");
        tax_value_txt.setText("0");
        amount_value_txt.setText("0");
        taxamount_value_txt.setText("0");
    }

    @Override
    public void setInventryItem(final List<Inventry_ReS_Model> inventryItemList) {
        this.inventryItemList = inventryItemList;
        AppUtility.autocompletetextviewPopUpWindow(autocomplete_item);
        autocomplete_item.setTag("Item");
        final Auto_Inventry_Adpter countryAdapter = new Auto_Inventry_Adpter(this,
                R.layout.custom_adapter_item_layout, (ArrayList<Inventry_ReS_Model>) inventryItemList);
        autocomplete_item.setAdapter(countryAdapter);
        autocomplete_item.setThreshold(3);

        autocomplete_item.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                itemlayout.setHintEnabled(true);
                setSelectedItemData(((Inventry_ReS_Model) adapterView.getItemAtPosition(i)));
                isInvOrNoninv = "1";

            }
        });


    }

    private void setSelectedItemData(Inventry_ReS_Model itemselected) {//add item from dropdown
        setDefaultTax(itemselected.getTax());
        itemId = itemselected.getItemId();
        type = "1";
        edt_item_qty.setText("1"); // quantity always be 1 initially
        edt_part_no.setText(itemselected.getPno());
        edt_hsnCode.setText(itemselected.getHsncode());
        edt_item_description.setText(itemselected.getIdes());
        edt_unit.setText(itemselected.getUnit());
        if (itemselected.getRate().isEmpty()) {
            edt_item_rate.setText("0");
        } else {
            edt_item_rate.setText(AppUtility.getRoundoff_amount(itemselected.getRate()));
        }
        if (itemselected.getSupplierCost().isEmpty()) {
            edt_item_supplier.setText("0");
        } else {
            edt_item_supplier.setText(AppUtility.getRoundoff_amount(itemselected.getSupplierCost()));
        }
        edt_item_discount.setText(AppUtility.getRoundoff_amount(itemselected.getDiscount()));

        total_Amount_cal();
    }

    private void setDefaultTax(List<Tax> tax_default_list) {
        for (Tax tax1 : listFilter) {
            for (Tax tax2 : tax_default_list) {
                if (tax1.getTaxId().equals(tax2.getTaxId())) {
                    tax1.setRate(tax2.getRate());
                    tax1.setSelect(true);
                    total_tax = Float.parseFloat(tax1.getRate()) + total_tax;
                }
            }
        }
        tax_value_txt.setText((String.valueOf(total_tax)));
        calculateTaxRate();
    }

    @Override
    public void setTaxList(List<Tax> taxList) {
        listFilter = new ArrayList<>();
        for (Tax tax : taxList) {
            if (tax.getIsactive().equals("1") && tax.getShow_Invoice().equals("1")) {
                if (tax.getRate() == null && tax.getPercentage() == null) {
                    tax.setRate("0");
                } else if (tax.getPercentage() != null) {
                    tax.setRate(tax.getPercentage());
                } else {
                    tax.setRate("0");
                }
                listFilter.add(tax);

            }
        }
    }


    @Override
    public void setFwList(final List<FieldWorker> fwDataList) {
        listFilterItemIteM(fwDataList);
        this.fwDataList = fwDataList;
        AppUtility.autocompletetextviewPopUpWindow(autocomplete_item);
        final Auto_Fieldworker_Adpter countryAdapter = new Auto_Fieldworker_Adpter(this,
                R.layout.custom_adapter_item_layout, (ArrayList<FieldWorker>) fwDataList);
        autocomplete_item.setAdapter(countryAdapter);
        autocomplete_item.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                itemlayout.setHintEnabled(true);
                isInvOrNoninv = "1";
                setFieldWorkerData(((FieldWorker) adapterView.getItemAtPosition(i)));
            }
        });
    }

    private void listFilterItemIteM(List<FieldWorker> fwDataList) {
        if (quotesDetails != null && quotesDetails.getInvData() != null && quotesDetails.getInvData().getItemData() != null)
            for (Quote_ItemData itemData : quotesDetails.getInvData().getItemData()) {
                for (FieldWorker fw : fwDataList) {
                    if (itemData.getType().equals("2") && fw.getUsrId().equals(itemData.getItemId())) {
                        fwDataList.remove(fw);
                        break;
                    }
                }
            }
    }


    private void setFieldWorkerData(FieldWorker fieldWorkerselected) {
        isMandtryFw = true;
        itemId = fieldWorkerselected.getUsrId();
        type = "2";
        edt_item_qty.setText("1"); // quantity always be 1 initially
        edt_item_rate.setText("0");
        edt_item_supplier.setText("0");
        edt_item_discount.setText("0");
        tax_value_txt.setText("0");
        total_Amount_cal();
    }

    @Override
    public void setJobServices(final List<JobTitle> jobServicesDataList) {
        ShowServicesList(jobServicesDataList);
        this.jobServicesDataList = jobServicesDataList;
        AppUtility.autocompletetextviewPopUpWindow(autocomplete_item);
        //   autocomplete_item.setTag("Services");
        final Services_item_Adapter services_item_adapter = new Services_item_Adapter(this,
                R.layout.custom_adapter_item_layout, (ArrayList<JobTitle>) jobServicesDataList);
        autocomplete_item.setAdapter(services_item_adapter);
        autocomplete_item.setThreshold(1);

        autocomplete_item.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                itemId = jtId = ((JobTitle) adapterView.getItemAtPosition(i)).getJtId();
                setServiceData(((JobTitle) adapterView.getItemAtPosition(i)));
            }
        });

    }

    /**
     * Remove alredy added services from current list
     */
    private void ShowServicesList(List<JobTitle> servicesItemList) {
        if (quotesDetails != null && quotesDetails.getInvData() != null && quotesDetails.getInvData().getItemData() != null)
            for (Quote_ItemData itemData : quotesDetails.getInvData().getItemData()) {
                for (JobTitle jobTitle : servicesItemList) {
                    if (itemData.getType().equals("3") && jobTitle.getJtId().equals(itemData.getJtId())) {//type check becouse details itme all type
                        servicesItemList.remove(jobTitle);
                        break;
                    }
                }
            }
    }


    private void setServiceData(JobTitle serViceItem) {
        itemlayout.setHintEnabled(true);
        type = "3";
        isInvOrNoninv = "2";
        isMandtryServices = true;
        jtId = serViceItem.getJtId();
        inm = serViceItem.getTitle();
        edt_item_qty.setText("1"); // quantity always be 1 initially
        edt_item_rate.setText(serViceItem.getLabour());
        edt_item_supplier.setText("0");
        edt_item_discount.setText("0");
        setServicesTax(serViceItem.getTaxData());
        total_Amount_cal();
    }

    private void setServicesTax(List<TaxData> taxDataList) { //set tax value for specifiec lable when set default job service tax amount
        for (Tax tax : listFilter) {
            for (TaxData taxData : taxDataList) {
                if (tax.getTaxId().equals(taxData.getTaxId())) {
                    /**In previous app***/
                    tax.setRate(taxData.getRate());
                    tax.setSelect(true);
                    total_tax = Float.parseFloat(tax.getRate()) + total_tax;
                }
                break;
            }
        }
        tax_value_txt.setText((String.valueOf(total_tax)));
        calculateTaxRate();

        Log.e("", "");
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public void onFocusChange(View v, boolean hasFocus) {
        if (!hasFocus) {
            if (v instanceof EditText) {
                ((EditText) v).setText(AppUtility.getRoundoff_amount(((EditText) v).getText().toString()));
            } else if (v instanceof TextView) {
                ((TextView) v).setText(AppUtility.getRoundoff_amount(((TextView) v).getText().toString()));
            }
        }
    }

    @Override
    public void afterTextChanged(Editable editable) {

    }


    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.item_select:
                autocomplete_item.setTag("Item");
                isfwOrItem = 1;
                setEmptyFields();
                quoteItemAdd_pi.getInventryItemList();
                setTxtBkgColor(1);
                fw_service_filed_hide(1);
                break;
            case R.id.tax_value_txt:
                showDialogTax();
                break;
            case R.id.fw_select:
                autocomplete_item.setTag("Fw");
                isfwOrItem = 2;
                setEmptyFields();
                setTxtBkgColor(2);
                quoteItemAdd_pi.getFwList();
                fw_service_filed_hide(2);
                break;
            case R.id.autocomplete_item:
                if (DP_OPEN) {
                    autocomplete_item.showDropDown();
                }
                break;
            case R.id.service_select:
                autocomplete_item.setTag("Services");
                isfwOrItem = 3;
                setTxtBkgColor(3);
                quoteItemAdd_pi.getJobServiceTittle();
                fw_service_filed_hide(3);
                break;

            case R.id.add_edit_item_Btn:
                add_edit_item_Btn.setEnabled(false);
                checkMandatryFields();
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        add_edit_item_Btn.setEnabled(true);
                    }
                }, 500);

                break;

        }
    }

    private void checkMandatryFields() {
        if (!isMandtryItem && isfwOrItem == 1) {
            AppUtility.alertDialog(this, "", LanguageController.getInstance().getMobileMsgByKey(AppConstant.item_empty), LanguageController.getInstance().getMobileMsgByKey(AppConstant.ok),
                    "", new Callable<Boolean>() {
                        @Override
                        public Boolean call() throws Exception {
                            return null;
                        }
                    });
            return;
        } else if (!isMandtryFw && isfwOrItem == 2) {//validation for fieldworker
            AppUtility.alertDialog(this, "", LanguageController.getInstance().getMobileMsgByKey(AppConstant.fw_valid), LanguageController.getInstance().getMobileMsgByKey(AppConstant.ok), "", new Callable<Boolean>() {
                @Override
                public Boolean call() throws Exception {
                    return null;
                }
            });
            return;
        } else if (!isMandtryServices && isfwOrItem == 3) {//validation for services
            AppUtility.alertDialog(this, "", LanguageController.getInstance().getMobileMsgByKey(AppConstant.service_error), AppConstant.ok, "", new Callable<Boolean>() {
                @Override
                public Boolean call() throws Exception {
                    return null;
                }
            });
            return;
        } else {
            List<Tax> taxListFilter = new ArrayList<>();
            for (Tax tax : listFilter) {
                if (tax.isSelect())
                    taxListFilter.add(tax);
            }

            if (quote_itemData != null) {
                Update_Quote_ReQ updateModel = new Update_Quote_ReQ(iqmmId, invId, type,
                        edt_item_rate.getText().toString().trim(), edt_item_qty.getText().toString().trim(),
                        edt_unit.getText().toString().trim(), edt_item_discount.getText().toString().trim(),
                        edt_item_description.getText().toString().trim(), amount_value_txt.getText().toString().trim(),
                        taxListFilter,
                        edt_item_supplier.getText().toString().trim(),
                        edt_part_no.getText().toString().trim(), String.valueOf(taxAmount), jtId, itemId
                        , quote_itemData.getInm());

                quoteItemAdd_pi.callApiUpdateQuotesItem(updateModel);
            } else {
                AddItem_Model reqModel = new AddItem_Model(addQuotesId, itemId, invId, type,
                        edt_item_rate.getText().toString().trim(), edt_item_qty.getText().toString().trim(), edt_item_discount.getText().toString().trim(),
                        edt_item_description.getText().toString().trim(),
                        // listFilter,
                        taxListFilter,
                        edt_unit.getText().toString().trim(),
                        edt_item_supplier.getText().toString().trim()
                        , isInvOrNoninv, edt_part_no.getText().toString().trim(), String.valueOf(taxAmount), amount_value_txt.getText().toString().trim(), jtId, inm);

                quoteItemAdd_pi.apiCallAddQuotesItem(reqModel);
            }

        }


    }


    private void showDialogTax() {
        if (listFilter.size() > 0) {
            final Dialog dialog = new Dialog(this);
            dialog.setCancelable(false);
            dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            dialog.setContentView(R.layout.tax_dialog_layout2);
            final LinearLayout root = dialog.findViewById(R.id.root);
            TextView txtRateHeading = dialog.findViewById(R.id.txtRateHeading);
            txtRateHeading.setText(LanguageController.getInstance().getMobileMsgByKey(AppConstant.tax_rate));
            TextView selected_tax_nm = dialog.findViewById(R.id.selected_tax_nm);

//            add all taxes into the views
            generateDynamicViews(listFilter, root, selected_tax_nm);
            Button cancel_btn = dialog.findViewById(R.id.cancel_btn);
            cancel_btn.setText(LanguageController.getInstance().getMobileMsgByKey(AppConstant.close));
            cancel_btn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    float localtax = getTotalApplyTax();
                    total_tax = localtax;
                    tax_value_txt.setText((String.valueOf(localtax)));
                    calculateTaxRate();
                    total_Amount_cal();
                    dialog.dismiss();
                }
            });

            dialog.show();
        }
    }

    private float getTotalApplyTax() {
        float applytax = 0;
        try {
            for (Tax selectedtax : listFilter) {
                if (selectedtax.isSelect()) {
                    if (updateItem) {
                        if (selectedtax.getOldTax() != null && !selectedtax.getOldTax().equals(""))
                            applytax += Float.valueOf(selectedtax.getOldTax());
                    } else {
                        applytax += Float.valueOf(selectedtax.getRate());
                    }
                }
            }
        } catch (Exception ex) {
            ex.getMessage();
        }
        return applytax;

    }


    private float getAllTax(LinearLayout root) {
        float count = 0;
        if (root != null) {
            for (int i = 0; i < listFilter.size(); i++) {
                LinearLayout itemparaent = ((LinearLayout) root.getChildAt(i));
                EditText child0 = ((EditText) itemparaent.getChildAt(1));
                TextView child1 = ((TextView) itemparaent.getChildAt(0));
                if (!child0.getText().toString().isEmpty()) {
                    float updatedtax = Float.parseFloat(child0.getText().toString());
                    count += updatedtax;
                    if (listFilter != null && listFilter.size() == root.getChildCount()) {
                        listFilter.get(i).setRate(String.valueOf(updatedtax));
                        //   listFilter.get(i).setTxRate(String.valueOf(updatedtax));
                    }
                } else {
                    if (listFilter != null && listFilter.size() == root.getChildCount()) {
                        listFilter.get(i).setRate("0");
                        // listFilter.get(i).setTxRate("0");
                    }
                }
            }

        }
        return count;
    }

    private void generateDynamicViews(final List<Tax> listFilter, LinearLayout root, final TextView selected_tax_nm) {
        //  final Set<String> tempTaxList = new HashSet<>();
        for (final Tax tax : listFilter) {
            View itemView = LayoutInflater.from(this).inflate(R.layout.tax_adpter_layout2, null);
            final CheckBox checkbox = itemView.findViewById(R.id.checkbox);
            TextView appliedTax = itemView.findViewById(R.id.appliedTax);
            View view = itemView.findViewById(R.id.view);


            /***This For Search Key***/
            if (tax.getAppliedTax() != null && !tax.getAppliedTax().equals("") && appliedTax != null) {
                appliedTax.setVisibility(View.VISIBLE);
                appliedTax.setText(tax.getAppliedTax());
                view.setVisibility(View.VISIBLE);
            } else if (appliedTax != null) {
                appliedTax.setVisibility(View.GONE);
                view.setVisibility(View.GONE);
                checkbox.setBackground(getResources().getDrawable(R.drawable.view));
            } else {
                view.setVisibility(View.GONE);
                checkbox.setBackground(getResources().getDrawable(R.drawable.view));
            }


//            if (tax.isSelect())
//                tempTaxList.add(tax.getLabel());

            checkbox.setChecked(false);

            checkbox.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    /*if (tempTaxList.contains(tax.getLabel())) {
                        tempTaxList.remove(tax.getLabel());
                        tax.setSelect(false);
                    } else {
                        tempTaxList.add(tax.getLabel());
                        tax.setSelect(true);
                    }*/

                    boolean t = false;
                    for (Tax temptax : listFilter) {
                        if (temptax.isSelect() && !temptax.getLabel().equalsIgnoreCase(tax.getLabel())) {
                            t = true;
                        }
                    }

                    if (!t) {
                        tax.setSelect(!tax.isSelect());
                        checkbox.setChecked(tax.isSelect());
                    } else {
                        checkbox.setChecked(false);
                    }


                    if (updateItem) {
                        tax.setOldTax(tax.getRate());
                    }
                    setSelectedTaxLable(new HashSet<String>(), selected_tax_nm);
                }
            });

            checkbox.setChecked(tax.isSelect());

            checkbox.setHintTextColor(Color.BLACK);
            if (tax.getRate() != null) {
                if (tax.getRate().isEmpty()) {
                    checkbox.setText(tax.getLabel() + " (0 %)");
                    //+ "/n &nbps;&nbps;&nbps;\n" +"\n" + " Sonam kaithwas"
                } else {
                    checkbox.setText(tax.getLabel() + " (" + tax.getRate() + "%)");
                    //  + "/n /n/n &nbps;&nbps;&nbps;\n" + "\n" + " Sonam 22kaithwas"
                }
            }
            root.addView(itemView);
        }
        setSelectedTaxLable(new HashSet<String>(), selected_tax_nm);
    }

    void setSelectedTaxLable(Set<String> tempTaxList, TextView selected_tax_nm) {
//        if (tempTaxList.size() == 0) {
//            selected_tax_nm.setText(LanguageController.getInstance().getMobileMsgByKey(AppConstant.selected_tax_lable));
//        } else if (tempTaxList.size() >= 3)
//            selected_tax_nm.setText(tempTaxList.size() + " " + LanguageController.getInstance().getMobileMsgByKey(AppConstant.items_selected));
//            // selected_tax_nm.setText(LanguageController.getInstance().getMobileMsgByKey(AppConstant.items_selected) + " " + tempTaxList.size());
//        else
//            selected_tax_nm.setText(tempTaxList.toString().replace("[", "").replace("]", ""));

        for (Tax tax : listFilter) {
            if (tax.isSelect()) {
                selected_tax_nm.setText(tax.getLabel());
                break;
            } else {
                selected_tax_nm.setText(LanguageController.getInstance().getMobileMsgByKey(AppConstant.selected_tax_lable));
            }
        }
    }

    private void setEmptyFields() {
        isMandtryServices = false;
        itemId = "";
        total_tax = 0f;
        edt_item_description.setText("");
        edt_item_qty.setText("1");
        edt_item_rate.setText("0");
        edt_item_supplier.setText("0");
        edt_item_discount.setText("0");
        tax_value_txt.setText("0");
        amount_value_txt.setText("0");
        taxamount_value_txt.setText("0");
        edt_part_no.setText("0");
        edt_hsnCode.setText("0");
        setTaxDialogFiledsEmpty();
    }

    private void setTaxDialogFiledsEmpty() {
        for (Tax tax : listFilter) {
            // tax.setRate("0");
            tax.setSelect(false);
        }
    }

    private void setTxtBkgColor(int selected_option) {
        if (selected_option == 1) {//check for items
            item_select.setBackgroundResource(R.drawable.item_tab_on_bkg);
            item_select.setTextColor(getResources().getColor(R.color.white));
            fw_select.setBackgroundResource(R.drawable.item_tab_off_bkg);
            fw_select.setTextColor(getResources().getColor(R.color.colorPrimary));
            service_select.setBackgroundResource(R.drawable.item_tab_off_bkg);
            service_select.setTextColor(getResources().getColor(R.color.colorPrimary));
            autocomplete_item.setText("");
            autocomplete_item.setHint(LanguageController.getInstance().getMobileMsgByKey(AppConstant.items_name));
            isMandtryItem = false;
            isfwOrItem = 1;
            DP_OPEN = false;
        } else if (selected_option == 2) {//for FeildWorker
            item_select.setBackgroundResource(R.drawable.item_tab_off_bkg);
            item_select.setTextColor(getResources().getColor(R.color.colorPrimary));
            fw_select.setBackgroundResource(R.drawable.item_tab_on_bkg);
            fw_select.setTextColor(getResources().getColor(R.color.white));
            service_select.setBackgroundResource(R.drawable.item_tab_off_bkg);
            service_select.setTextColor(getResources().getColor(R.color.colorPrimary));
            autocomplete_item.setText("");
            autocomplete_item.setHint(LanguageController.getInstance().getMobileMsgByKey(AppConstant.fieldworkers_name));
            isMandtryFw = false;
            isfwOrItem = 2;
            DP_OPEN = true;
            ITEMSYNC = false;

        } else {//for Services
            item_select.setBackgroundResource(R.drawable.item_tab_off_bkg);
            item_select.setTextColor(getResources().getColor(R.color.colorPrimary));
            fw_select.setBackgroundResource(R.drawable.item_tab_off_bkg);
            fw_select.setTextColor(getResources().getColor(R.color.colorPrimary));
            service_select.setBackgroundResource(R.drawable.item_tab_on_bkg);
            service_select.setTextColor(getResources().getColor(R.color.white));
            autocomplete_item.setText("");
            autocomplete_item.setHint(LanguageController.getInstance().getMobileMsgByKey(AppConstant.services_name));
            isfwOrItem = 3;
            isMandtryServices = false;
            DP_OPEN = true;
            ITEMSYNC = false;

        }
    }


    /**
     * hide fields(discount & suppiler cost) when fw add as a item Amd hide fileds(Hsn code,unit,part no , suppiler cost)
     */
    private void fw_service_filed_hide(int type) {
        switch (type) {
            case 1://item
                item_supplier_layout.setVisibility(View.VISIBLE);
                supplier_view.setVisibility(View.VISIBLE);

                item_partNo_layout.setVisibility(View.VISIBLE);
                part_no_view.setVisibility(View.VISIBLE);

                item_unit_layout.setVisibility(View.VISIBLE);
                unit_view.setVisibility(View.VISIBLE);

                item_discount_layout.setVisibility(View.VISIBLE);
                disc_view.setVisibility(View.VISIBLE);

                item_tax_rate_layout.setVisibility(View.VISIBLE);
                tax_rate_view.setVisibility(View.VISIBLE);
                break;
            case 2://fw
                item_supplier_layout.setVisibility(View.GONE);
                supplier_view.setVisibility(View.GONE);


                item_partNo_layout.setVisibility(View.GONE);
                part_no_view.setVisibility(View.GONE);

                item_unit_layout.setVisibility(View.GONE);
                unit_view.setVisibility(View.GONE);

                item_discount_layout.setVisibility(View.VISIBLE);
                disc_view.setVisibility(View.VISIBLE);
                item_tax_rate_layout.setVisibility(View.VISIBLE);
                tax_rate_view.setVisibility(View.VISIBLE);
                break;
            case 3://job services

                item_supplier_layout.setVisibility(View.GONE);
                supplier_view.setVisibility(View.GONE);

                disc_view.setVisibility(View.GONE);
                item_discount_layout.setVisibility(View.GONE);

                item_partNo_layout.setVisibility(View.VISIBLE);
                part_no_view.setVisibility(View.VISIBLE);

                item_unit_layout.setVisibility(View.VISIBLE);
                unit_view.setVisibility(View.VISIBLE);

                item_tax_rate_layout.setVisibility(View.VISIBLE);
                tax_rate_view.setVisibility(View.VISIBLE);
                break;
        }
        hideShowInvoiceItemDetails();

    }


    @Override
    public void updateItemServiceListner() {

        switch (isfwOrItem) {
            case 1:
                quoteItemAdd_pi.getInventryItemList();
                break;
            case 2:
                quoteItemAdd_pi.getFwList();
                break;
            case 3:
                quoteItemAdd_pi.getJobServiceTittle();
                break;
        }


    }

    @Override
    public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
    }

    @Override
    public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
        if (charSequence.length() >= 1) {
            if (charSequence.hashCode() == edt_item_description.getText().hashCode())
                item_desc_layout.setHintEnabled(true);
            if (charSequence.hashCode() == edt_item_qty.getText().hashCode())
                item_qty_layout.setHintEnabled(true);
            if (charSequence.hashCode() == edt_item_rate.getText().hashCode()) {
                item_rate_layout.setHintEnabled(true);
                calculateTaxRate();
            }
            if (charSequence.hashCode() == edt_item_supplier.getText().hashCode())
                item_supplier_layout.setHintEnabled(true);
            if (charSequence.hashCode() == edt_item_discount.getText().hashCode()) {
                item_discount_layout.setHintEnabled(true);
                if (Float.parseFloat(edt_item_discount.getText().toString()) > 100) {
                    showDisError(LanguageController.getInstance().getMobileMsgByKey(AppConstant.discountError));
                    edt_item_discount.setText("0");
                }
                calculateTaxRate();
            }
            if (charSequence.hashCode() == edt_part_no.getText().hashCode())
                item_partNo_layout.setHintEnabled(true);
            if (charSequence.hashCode() == edt_unit.getText().hashCode())
                item_unit_layout.setHintEnabled(true);

            if (charSequence.hashCode() == edt_item_tax_rate.getText().hashCode()) {
                item_tax_rate_layout.setHintEnabled(true);

            }

            total_Amount_cal();

        } else if (charSequence.length() <= 0) {
            if (charSequence.hashCode() == edt_item_description.getText().hashCode())
                item_desc_layout.setHintEnabled(false);
            if (charSequence.hashCode() == edt_item_qty.getText().hashCode())
                item_qty_layout.setHintEnabled(false);
            if (charSequence.hashCode() == edt_item_rate.getText().hashCode()) {
                item_rate_layout.setHintEnabled(false);
                calculateTaxRate();
            }
            if (charSequence.hashCode() == edt_item_supplier.getText().hashCode())
                item_supplier_layout.setHintEnabled(false);
            if (charSequence.hashCode() == edt_item_discount.getText().hashCode()) {
                item_discount_layout.setHintEnabled(false);
                calculateTaxRate();
            }
            if (charSequence.hashCode() == edt_part_no.getText().hashCode())
                item_partNo_layout.setHintEnabled(false);
            if (charSequence.hashCode() == edt_unit.getText().hashCode())
                item_unit_layout.setHintEnabled(false);
            if (charSequence.hashCode() == edt_item_tax_rate.getText().hashCode())
                item_tax_rate_layout.setHintEnabled(false);

            total_Amount_cal();
        }
    }

    private void calculateTaxRate() {
        if (!App_preference.getSharedprefInstance().getLoginRes().getCompPermission().get(0).getIsRateIncludingTax().equals("1")) {
            double rate = 0, dis = 0;

            /**  check of amount calculation */
            try {

                if (!edt_item_rate.getText().toString().equals("")) {
                    rate = Double.parseDouble(AppUtility.getRoundoff_amount(edt_item_rate.getText().toString()));
                }
                if (!edt_item_discount.getText().toString().equals("")) {
                    dis = Double.parseDouble(AppUtility.getRoundoff_amount(edt_item_discount.getText().toString()));
                }


                if (App_preference.getSharedprefInstance().getLoginRes().getTaxCalculationType().equals("0")) {
                    double calculaterateDis = (rate * dis) / 100;
                    double newRate = rate - calculaterateDis;
                    double newAmt = (newRate * total_tax) / 100;
                    double d = rate + newAmt;
                    String tax_Amount = AppUtility.getRoundoff_amount(String.valueOf(d));
                    edt_item_tax_rate.setText(tax_Amount);
                } else if (App_preference.getSharedprefInstance().getLoginRes().getTaxCalculationType().equals("1")) {
                    double newRate = Double.parseDouble((taxamount_value_txt.getText().toString())) + rate;
                    String tax_Amount = AppUtility.getRoundoff_amount(String.valueOf(newRate));
                    edt_item_tax_rate.setText(tax_Amount);
                }

            } catch (Exception ex) {
                ex.getMessage();
            }

        } else {
            double rate = 0;
            try {
                if (!edt_item_rate.getText().toString().equals("")) {
                    rate = Double.parseDouble(AppUtility.getRoundoff_amount(edt_item_rate.getText().toString()));
                }
                double newAmt = (rate * total_tax) / 100;
                double d = rate + newAmt;
                String tax_Amount = AppUtility.getRoundoff_amount(String.valueOf(d));
                edt_item_tax_rate.setText(tax_Amount);

            } catch (Exception ex) {
                ex.getMessage();
            }
        }

    }


    private void showDisError(String msg) {
        AppUtility.alertDialog(this, "", msg, LanguageController.getInstance().getMobileMsgByKey(AppConstant.ok), "", new Callable<Boolean>() {
            @Override
            public Boolean call() throws Exception {
                return true;
            }
        });
    }


}
