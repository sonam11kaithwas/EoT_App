package com.eot_app.nav_menu.jobs.job_detail.addinvoiveitem2pkg;

import static com.eot_app.nav_menu.jobs.job_detail.invoice2list.JoBInvoiceItemList2Activity.ADD_ITEM_DATA;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.eot_app.R;
import com.eot_app.login_next.FooterMenu;
import com.eot_app.login_next.login_next_model.CompPermission;
import com.eot_app.nav_menu.jobs.job_db.Job;
import com.eot_app.nav_menu.jobs.job_detail.addinvoiveitem2pkg.model.AddInvoiceItemReqModel;
import com.eot_app.nav_menu.jobs.job_detail.addinvoiveitem2pkg.model.InvoiceItemDataModel;
import com.eot_app.nav_menu.jobs.job_detail.addinvoiveitem2pkg.mvp.AddEditInvoiceItem_PC;
import com.eot_app.nav_menu.jobs.job_detail.addinvoiveitem2pkg.mvp.AddEditInvoiceItem_PI;
import com.eot_app.nav_menu.jobs.job_detail.addinvoiveitem2pkg.mvp.AddEditInvoiceItem_View;
import com.eot_app.nav_menu.jobs.job_detail.invoice.Auto_Inventry_Adpter;
import com.eot_app.nav_menu.jobs.job_detail.invoice.add_edit_invoice_pkg.dropdown_item_pkg.Auto_Fieldworker_Adpter;
import com.eot_app.nav_menu.jobs.job_detail.invoice.add_edit_invoice_pkg.dropdown_item_pkg.Services_item_Adapter;
import com.eot_app.nav_menu.jobs.job_detail.invoice.inventry_pkg.Inventry_ReS_Model;
import com.eot_app.nav_menu.jobs.job_detail.invoice.invoice_detail_pkg.inv_detail_model.Tax;
import com.eot_app.nav_menu.jobs.job_detail.job_equipment.add_job_equip.AddJobEquipMentActivity;
import com.eot_app.nav_menu.jobs.joboffline_db.JobOfflineDataModel;
import com.eot_app.services.Service_apis;
import com.eot_app.utility.AppConstant;
import com.eot_app.utility.AppUtility;
import com.eot_app.utility.App_preference;
import com.eot_app.utility.EotApp;
import com.eot_app.utility.db.AppDataBase;
import com.eot_app.utility.db.OfflineDataController;
import com.eot_app.utility.language_support.LanguageController;
import com.eot_app.utility.settings.jobtitle.TaxData;
import com.eot_app.utility.settings.setting_db.FieldWorker;
import com.eot_app.utility.settings.setting_db.JobTitle;
import com.eot_app.utility.settings.setting_db.Offlinetable;
import com.eot_app.utility.util_interfaces.Callback_AlertDialog;
import com.google.android.material.textfield.TextInputLayout;
import com.google.gson.Gson;
import com.hypertrack.hyperlog.HyperLog;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.Callable;


public class AddEditInvoiceItemActivity2 extends AppCompatActivity implements AddEditInvoiceItem_View, TextWatcher, View.OnFocusChangeListener, View.OnClickListener, RadioGroup.OnCheckedChangeListener {
    public static final int EQUIPMENTCONVERT = 201;
    private String isBillableChange = "";//,isBillableChange="";
    private String invId = "", locId = "0";
    private double taxAmount = 0;
    private AddEditInvoiceItem_PI invoiceItemPi;
    private InvoiceItemDataModel updateItemDataModel;
    private String inm, itemId, dataType, itemType = "";
    private float total_tax = 0f;
    private boolean IS_ITEM_MANDATRY, IS_FW_MANDATRY, IS_SERVICES_MANDATRY;
    private int TAB_SELECT = 1;
    private AutoCompleteTextView autocomplete_item;
    private List<Tax> listFilter = new ArrayList<>();
    private EditText edt_item_desc, edt_item_qty, edt_item_rate, edt_item_supplier, edt_item_disc, edt_part_no, edt_hsnCode, edt_unit, edt_serialNo, edt_item_tax_rate;
    private TextInputLayout item_desc_layout, item_qty_layout, item_rate_layout, item_discount_layout, itemlayout, item_hsnCode_layout, item_partNo_layout, item_unit_layout, item_supplier_layout, serialNo_layout, item_tax_rate_layout;
    private Button add_edit_item_Btn;
    private TextView item_select, fw_select, service_select;
    private LinearLayout layout_fw_item, taxamount_layout;
    private double roundOff;
    private View nm_view, desc_view, qty_view, rate_view, supplier_view, disc_view, tax_view, amount_view, part_no_view, hsncode_view, unit_view, taxrateAmount_view, seroal_no_view, tax_rate_view;
    private TextView tax_value_txt, tax_txt_hint, amount_value_txt, taxamount_txt_hint, taxamount_value_txt, amount_txt_hint;
    private LinearLayout tax_layout, amount_layout;
    private boolean DP_OPEN = false;
    private List<JobTitle> servicesItemList = new ArrayList<>();
    private List<FieldWorker> fieldWorkerList = new ArrayList<>();
    private List<Inventry_ReS_Model> itemsList = new ArrayList<>();
    private String jobId;
    private String jtId = "";
    private String isBillable = "";
    private boolean addItemOnInvoice;
    private RelativeLayout add_quote_item_layout;
    private Job jobModel;
    private TextView convert_item_to_equi;
    private Boolean jobItemCountForFlag = false;
    private boolean updateItem = false;
    private boolean ITEMSYNC = false;
    private RadioGroup rediogrp;
    private RadioButton radio_billable, radio_none_billable;
    private boolean NOITEMRELECT = false, NONBILLABLE = false;
    private TextView text_default;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit__invoice);
        initializelables();
        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            try {
                if (getIntent().hasExtra("NONBILLABLE")) {
                    NONBILLABLE = bundle.getBoolean("NONBILLABLE");
                }
                if (getIntent().hasExtra("locId")) {
                    locId = bundle.getString("locId");
                } else {
                    locId = "0";
                }
            } catch (Exception exception) {
                exception.getMessage();
                locId = "0";
            }

            /****Add item***/
            if (getIntent().hasExtra("jobId")) {
                jobId = bundle.getString("jobId");
                invId = bundle.getString("invId");
                addItemOnInvoice = bundle.getBoolean("addItemOnInvoice");

                setDefaultValuesForAddNewItem();
                /** we have to get a new tax items from the server */
                invoiceItemPi.getTaxList();
            } else if (getIntent().hasExtra("InvoiceItemDataModel")) {
                jobId = bundle.getString("edit_jobId");
                invId = bundle.getString("invId");
                addItemOnInvoice = bundle.getBoolean("addItemOnInvoice");
                updateItemDataModel = bundle.getParcelable("InvoiceItemDataModel");


                invoiceItemPi.getTaxList();
                goneViewsForUpdate();
            }
            getJobById();
        } else {
            return;
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
                                IS_ITEM_MANDATRY = true;
                                if (itemsList != null &&
                                        AppDataBase.getInMemoryDatabase(EotApp.getAppinstance()).invoiceItemDao().getInventryItemList().size() == 0
                                        && autocomplete_item.getText().toString().length() >= 3) {
                                    if (!AppUtility.isInternetConnected() && !ITEMSYNC) {
                                        showDialogForLoadData();
                                        ITEMSYNC = true;
                                    } else {
                                        invoiceItemPi.getDataFromServer(autocomplete_item.getText().toString());
                                    }

                                }
                            } else if (charSequence.length() <= 0) {
                                itemlayout.setHintEnabled(false);
                                IS_ITEM_MANDATRY = false;
                            }
                            /*****/
                            if (!itemsList.contains(charSequence)) {
                                inm = charSequence.toString();
                                setEmptyFieldsNonInventry();
                            }
                            break;
                        case "Fw":
                            if (charSequence.length() >= 1) {
                                itemlayout.setHintEnabled(true);
                            } else if (charSequence.length() <= 0) {
                                itemlayout.setHintEnabled(false);
                                IS_FW_MANDATRY = false;
                            }
                            if (!fieldWorkerList.contains(charSequence)) {
                                setEmptyFields();
                            }
                            Log.e("", "");
                            break;
                        case "Services":
                            if (charSequence.length() >= 1) {
                                itemlayout.setHintEnabled(true);
                            } else if (charSequence.length() <= 0) {
                                itemlayout.setHintEnabled(false);
                                IS_SERVICES_MANDATRY = false;
                            }
                            if (!servicesItemList.contains(charSequence)) {
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
                                invoiceItemPi.getDataFromServer(autocomplete_item.getText().toString());
                            } else {
                                showDisError(LanguageController.getInstance().getMobileMsgByKey(AppConstant.network_error));
                            }
                        } catch (Exception ex) {
                            ex.printStackTrace();
                        }

                    }

                    @Override
                    public void onNegativeCall() {
                        Log.e("", "");
                    }
                });


    }

    private void goneViewsForUpdate() {
        layout_fw_item.setVisibility(View.GONE);

        try {
            if (locId != null && !locId.equals("0") && updateItemDataModel.getTaxType() != null && updateItemDataModel.getTaxType().equals("2")) {
                applytaxBasesOnLoc();
            } else if (updateItemDataModel.getTax().size() > 0) {
                List<Tax> filterSelectedTaxList = updateItemDataModel.getTax();
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

        setItemFields();
    }


    private void setItemFields() {
        tax_value_txt.setText((String.valueOf(total_tax)));
        calculateTaxRate();

        /** type 1 for inventry item  */
        if (updateItemDataModel.getDataType().equals("1")) {
            autocomplete_item.setHint(LanguageController.getInstance().getMobileMsgByKey(AppConstant.item));
            setTxtBkgColor(1);
            fw_service_filed_hide(1);
            IS_ITEM_MANDATRY = true;
            TAB_SELECT = 1;
        }
        /** type 2 for FieldWorker item  */
        else if (updateItemDataModel.getDataType().equals("2")) {
            setTxtBkgColor(2);
            autocomplete_item.setHint(LanguageController.getInstance().getMobileMsgByKey(AppConstant.fieldworkers_name));
            fw_service_filed_hide(2);
            IS_FW_MANDATRY = true;
            TAB_SELECT = 2;
        } else  /** type 3 for Job Services item  */
            if (updateItemDataModel.getDataType().equals("3")) {
                setTxtBkgColor(3);
                autocomplete_item.setHint(LanguageController.getInstance().getMobileMsgByKey(AppConstant.services_name));
                fw_service_filed_hide(3);
                IS_SERVICES_MANDATRY = true;
                TAB_SELECT = 3;

            } else /** type 6 for LABOUR item  */
                if (updateItemDataModel.getDataType().equals("6")) {

                }


        if (!updateItemDataModel.getInm().equals(""))
            autocomplete_item.setText(updateItemDataModel.getInm());
        else autocomplete_item.setText(updateItemDataModel.getTempNm());
        autocomplete_item.setFocusableInTouchMode(false);
        autocomplete_item.dismissDropDown();
        itemlayout.setHintEnabled(true);

        edt_item_qty.setText(updateItemDataModel.getQty());
        edt_item_rate.setText(AppUtility.getRoundoff_amount(updateItemDataModel.getRate()));
        edt_item_supplier.setText(AppUtility.getRoundoff_amount((updateItemDataModel.getSupplierCost())));
        edt_item_disc.setText(updateItemDataModel.getDiscount());
        edt_item_desc.setText(updateItemDataModel.getDes());
        edt_part_no.setText(updateItemDataModel.getPno());
        edt_hsnCode.setHint(App_preference.getSharedprefInstance().getLoginRes().getHsnCodeLable());
        edt_hsnCode.setText(updateItemDataModel.getHsncode());
        edt_unit.setText(updateItemDataModel.getUnit());
        edt_serialNo.setText(updateItemDataModel.getSerialNo());

        itemId = updateItemDataModel.getItemId();
        dataType = updateItemDataModel.getDataType();
        itemType = updateItemDataModel.getItemType();
        inm = updateItemDataModel.getInm();
        jtId = updateItemDataModel.getJtId();
        try {
            if (!NONBILLABLE && updateItemDataModel.getDataType().equals("1")) {
                if (updateItemDataModel.getIsBillable() != null) {
                    isBillable = updateItemDataModel.getIsBillable();
                }
                if (updateItemDataModel.getIsBillableChange() != null)
                    isBillableChange = updateItemDataModel.getIsBillableChange();
                if (isBillable.equals("1")) {
                    radio_billable.setChecked(true);
                    radio_none_billable.setChecked(false);
                } else if (isBillable.equals("0")) {
                    radio_none_billable.setChecked(true);
                    radio_billable.setChecked(false);
                } else {
                    radio_none_billable.setChecked(false);
                    radio_billable.setChecked(false);
                }
            } else {
                rediogrp.setVisibility(View.GONE);
                isBillable = updateItemDataModel.getIsBillable();
                isBillableChange = updateItemDataModel.getIsBillableChange();
            }
        } catch (Exception e) {
            e.getMessage();
        }


    }

    private void setEmptyFieldsNonInventry() {
        jtId = "";
        dataType = "1";
        itemType = "1";
        itemId = "";
        isBillable = "";
        isBillableChange = "";
        total_tax = 0f;
        edt_item_desc.setText("");
        edt_item_qty.setText("1");
        edt_item_rate.setText("0");
        edt_item_supplier.setText("0");
        edt_item_disc.setText("0");
        tax_value_txt.setText("0");
        amount_value_txt.setText("0");
        taxamount_value_txt.setText("0");
        edt_part_no.setText("0");
        edt_hsnCode.setText("0");
        edt_serialNo.setText("");
        setTaxDialogFiledsEmpty();
        rediogrp.setVisibility(View.GONE);
    }

    private void set_Title() {
        if (updateItemDataModel == null) {
            /**  */
            getSupportActionBar().setTitle(LanguageController.getInstance().getMobileMsgByKey(AppConstant.addItem_screen_title));
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            add_edit_item_Btn.setText(LanguageController.getInstance().getMobileMsgByKey(AppConstant.save_btn));
        } else {
            /**Convert Item to Equipment Only for Inventry Item's*****/
            if (App_preference.getSharedprefInstance().getLoginRes().getIsEquipmentEnable().equals("1")
                    && updateItemDataModel.getItemType().equals("0")) {
                for (FooterMenu serverList : App_preference.getSharedprefInstance().getLoginRes().getFooterMenu()) {
                    if (serverList.isEnable.equals("1") && serverList.getMenuField().equals("set_equipmentMenuOrdrNo")) {
                        convert_item_to_equi.setVisibility(View.VISIBLE);
                        break;
                    }
                }
            }

            /*****this permission for Edit item***/
            if (App_preference.getSharedprefInstance().getLoginRes().getIsItemEditEnable().equals("1")) {
                getSupportActionBar().setTitle(LanguageController.getInstance().getMobileMsgByKey(AppConstant.update_item));
                getSupportActionBar().setDisplayHomeAsUpEnabled(true);
                add_edit_item_Btn.setText(LanguageController.getInstance().getMobileMsgByKey(AppConstant.update_btn));
                DP_OPEN = false;
            } else {
                EnableDisbleFields();
                getSupportActionBar().setTitle(LanguageController.getInstance().getMobileMsgByKey(AppConstant.view_details));
                getSupportActionBar().setDisplayHomeAsUpEnabled(true);
                add_edit_item_Btn.setVisibility(View.GONE);
            }


            if (updateItemDataModel != null && updateItemDataModel.getDataType() != null && updateItemDataModel.getDataType().equals("6")) {
                EnableDisbleFields();
                getSupportActionBar().setTitle(LanguageController.getInstance().getMobileMsgByKey(AppConstant.view_details));
                getSupportActionBar().setDisplayHomeAsUpEnabled(true);
                add_edit_item_Btn.setVisibility(View.GONE);
            }


        }
    }

    /**
     * User not editable all fields when admin not allow & not update
     */
    private void EnableDisbleFields() {
        autocomplete_item.setEnabled(false);
        edt_part_no.setEnabled(false);
        edt_unit.setEnabled(false);
        edt_item_desc.setEnabled(false);
        edt_item_qty.setEnabled(false);
        edt_item_rate.setEnabled(false);
        edt_item_supplier.setEnabled(false);
        edt_unit.setEnabled(false);
        edt_item_desc.setEnabled(false);
        tax_value_txt.setEnabled(false);
        amount_value_txt.setEnabled(false);
        edt_item_disc.setEnabled(false);
        taxamount_value_txt.setEnabled(false);
        edt_hsnCode.setEnabled(false);
        edt_serialNo.setEnabled(false);
        radio_billable.setEnabled(false);
        radio_none_billable.setEnabled(false);
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
        edt_item_desc = findViewById(R.id.edt_item_desc);
        edt_item_desc.setHint(LanguageController.getInstance().getMobileMsgByKey(AppConstant.item_desc));

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
        edt_item_disc = findViewById(R.id.edt_item_disc);
        edt_item_disc.setHint(LanguageController.getInstance().getMobileMsgByKey(AppConstant.discount) + " (%)");

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
        edt_serialNo = findViewById(R.id.edt_serialNo);
        edt_serialNo.setHint(LanguageController.getInstance().getMobileMsgByKey(AppConstant.serial_no));

        convert_item_to_equi = findViewById(R.id.convert_item_to_equi);
        convert_item_to_equi.setText(LanguageController.getInstance().getMobileMsgByKey(AppConstant.convert_item_to_equ));

        rediogrp = findViewById(R.id.rediogrp);
        rediogrp.setOnCheckedChangeListener(this);
        radio_billable = findViewById(R.id.radio_billable);
        radio_none_billable = findViewById(R.id.radio_none_billable);
        text_default = findViewById(R.id.text_default);
        radio_none_billable.setText(LanguageController.getInstance().getMobileMsgByKey(AppConstant.non_billable));
        radio_billable.setText(LanguageController.getInstance().getMobileMsgByKey(AppConstant.billable));
        text_default.setText(" (" + LanguageController.getInstance().getMobileMsgByKey(AppConstant.text_default) + ")");

        intializeViews();
    }

    @Override
    public void onCheckedChanged(RadioGroup group, int checkedId) {
        Log.e("", "");
        switch (checkedId) {
            case R.id.radio_none_billable:
                nonBillableItem();
                break;
            case R.id.radio_billable:
                billableItem();
                break;
        }

    }

    private void nonBillableItem() {
        isBillable = "0";
        isBillableChange = "";
    }

    private void billableItem() {
        isBillableChange = isBillable = "1";
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
        seroal_no_view = findViewById(R.id.seroal_no_view);


        item_tax_rate_layout = findViewById(R.id.item_tax_rate_layout);
        edt_item_tax_rate = findViewById(R.id.edt_item_tax_rate);
        tax_rate_view = findViewById(R.id.tax_rate_view);
        edt_item_tax_rate.setHint(LanguageController.getInstance().getMobileMsgByKey(AppConstant.rate_inclu_tax));
        edt_item_tax_rate.setEnabled(false);

        add_quote_item_layout = findViewById(R.id.add_quote_item_layout);
        AppUtility.setupUI(add_quote_item_layout, AddEditInvoiceItemActivity2.this);

        item_desc_layout.getEditText().addTextChangedListener(this);
        item_qty_layout.getEditText().addTextChangedListener(this);
        item_rate_layout.getEditText().addTextChangedListener(this);
        item_supplier_layout.getEditText().addTextChangedListener(this);
        item_discount_layout.getEditText().addTextChangedListener(this);
        item_partNo_layout.getEditText().addTextChangedListener(this);
        item_hsnCode_layout.getEditText().addTextChangedListener(this);
        item_unit_layout.getEditText().addTextChangedListener(this);
        serialNo_layout.getEditText().addTextChangedListener(this);
        item_tax_rate_layout.getEditText().addTextChangedListener(this);


        add_edit_item_Btn.setOnClickListener(this);
        edt_item_desc.addTextChangedListener(this);
        item_select.setOnClickListener(this);
        fw_select.setOnClickListener(this);
        service_select.setOnClickListener(this);
        tax_value_txt.setOnClickListener(this);
        edt_item_rate.setOnFocusChangeListener(this);
        edt_item_disc.setOnFocusChangeListener(this);
        amount_value_txt.setOnFocusChangeListener(this);
        autocomplete_item.setOnClickListener(this);
        convert_item_to_equi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (AppUtility.isInternetConnected())
                    convertInEquip();
                else
                    showDisError(LanguageController.getInstance().getMobileMsgByKey(AppConstant.network_error));
            }
        });

        autocomplete_item.addTextChangedListener(this);

        hideShowInvoiceItemDetails();

        /*****this permission for EDIT item***/
        if (App_preference.getSharedprefInstance().getLoginRes().getIsItemEditEnable().equals("1")) {
            add_edit_item_Btn.setVisibility(View.VISIBLE);
        }

        if (!NONBILLABLE) {
            rediogrp.setVisibility(View.GONE);
        }

        invoiceItemPi = new AddEditInvoiceItem_PC(this);
        invoiceItemPi.getInventryItemList();
    }

    /**
     * Convert Item to Equipment
     *****/
    private void convertInEquip() {
        if (Integer.parseInt(updateItemDataModel.getItemConvertCount()) >= Integer.parseInt(updateItemDataModel.getQty())) {
            AppUtility.alertDialog2(this, LanguageController.getInstance().getMobileMsgByKey(AppConstant.are_you_sure)
                    , LanguageController.getInstance().getMobileMsgByKey(AppConstant.item_convrt_count), LanguageController.getInstance().getMobileMsgByKey(AppConstant.ok)
                    , LanguageController.getInstance().getMobileMsgByKey(AppConstant.cancel),
                    new Callback_AlertDialog() {
                        @Override
                        public void onPossitiveCall() {
                            sentDataForEquip();
                        }

                        @Override
                        public void onNegativeCall() {
                        }
                    });
        } else {
            sentDataForEquip();
        }

    }

    private void sentDataForEquip() {
        Intent intent = new Intent(AddEditInvoiceItemActivity2.this, AddJobEquipMentActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
        intent.putExtra("edit_jobId", jobId);
        intent.putExtra("invId", invId);
        intent.putExtra("InvoiceItemDataModel", updateItemDataModel);
        try {
            intent.putExtra("objectStr", new Gson().toJson(updateItemDataModel));
        } catch (Exception exception) {
            exception.printStackTrace();
        }
        startActivityForResult(intent, EQUIPMENTCONVERT);
    }

    @Override
    public void setItemdataFromServer(List<Inventry_ReS_Model> list) {

    }

    @Override
    public void setItemdata(List<Inventry_ReS_Model> list) {
        Log.e("", "");
        this.itemsList = list;
        AppUtility.autocompletetextviewPopUpWindow(autocomplete_item);
        autocomplete_item.setTag("Item");
        final Auto_Inventry_Adpter itemAdapter = new Auto_Inventry_Adpter(this,
                R.layout.custom_item_adpter, (ArrayList<Inventry_ReS_Model>) this.itemsList);
        autocomplete_item.setAdapter(itemAdapter);
        autocomplete_item.setThreshold(3);

        autocomplete_item.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                try {
                    setSelectedItemData(((Inventry_ReS_Model) adapterView.getItemAtPosition(position)));
                } catch (Exception ex) {
                    ex.getMessage();
                }
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Log.e("", "");
        switch (requestCode) {
            case EQUIPMENTCONVERT:
                try {
                    if (jobId != null && !jobId.equals("")) {
                        Job jobList = AppDataBase.getInMemoryDatabase(EotApp.getAppinstance()).jobModel().getJobsById(jobId);
                        if (jobList != null) {
                            for (InvoiceItemDataModel model : jobList.getItemData()) {
                                if (model.getIjmmId().equals(updateItemDataModel.getIjmmId())) {
                                    updateItemDataModel = model;
                                    break;
                                }
                            }
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
                break;
        }
    }

    /*** add item from dropdown*/
    private void setSelectedItemData(Inventry_ReS_Model itemselected) {
        /********* 1 For Selected tax on Item************/
        if (itemselected.getTaxType() != null && itemselected.getTaxType().equals("1")) {
            setDefaultTax(itemselected.getTax());

        } else /********* 2 For Apply tax on Item************/
            if (itemselected.getTaxType() != null && itemselected.getTaxType().equals("2")) {
                applytaxBasesOnLoc();
            } else  /********* 0 For no selection************/
                if (itemselected.getTaxType() != null && itemselected.getTaxType().equals("0")) {

                }
        itemId = itemselected.getItemId();
        inm = "";
        itemId = itemselected.getItemId();
        dataType = "1";
        itemType = "0";
        jtId = "";
        try {
            if (itemselected.getIsBillable() != null)
                isBillable = itemselected.getIsBillable();
            if (itemselected.getIsBillableChange() != null)
                isBillableChange = itemselected.getIsBillableChange();

            if (isBillable.equals("1")) {
                radio_billable.setChecked(true);
                // radio_none_billable.setChecked(false);
            } else if (isBillable.equals("0")) {
                radio_none_billable.setChecked(true);
                // radio_billable.setChecked(false);
            } else {
                radio_none_billable.setChecked(false);
                radio_billable.setChecked(false);
            }

        } catch (Exception ex) {
            ex.getMessage();
        }

        /** quantity always be 1 initially */
        edt_item_qty.setText("1");
        edt_part_no.setText(itemselected.getPno());
        edt_hsnCode.setText(itemselected.getHsncode());
        edt_item_desc.setText(itemselected.getIdes());
        edt_unit.setText(itemselected.getUnit());
        edt_serialNo.setText(itemselected.getSerialNo());
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
        edt_item_disc.setText(AppUtility.getRoundoff_amount(itemselected.getDiscount()));

        total_Amount_cal();
        if (!NONBILLABLE)
            rediogrp.setVisibility(View.VISIBLE);
//        radio_billable.setChecked(true);
//        radio_none_billable.setChecked(true);

    }

    /********* Apply tax on Item bases on Location************/
    private void applytaxBasesOnLoc() {

        try {
            if (locId != null && !locId.equals("0")) {
                List<Tax> locTaxList = AppDataBase.getInMemoryDatabase(EotApp.getAppinstance()).invoiceTaxDao().getTaxListByLocId(locId);
                if (locTaxList != null && locTaxList.size() == 1) {
                    for (Tax tax1 : listFilter) {
                        if (tax1.getLocId() == null)
                            tax1.setLocId("0");
                        if (tax1.getLocId().equals(locId)) {
                            tax1.setSelect(true);
                            total_tax = Float.parseFloat(tax1.getRate()) + total_tax;
                            break;
                        }
                    }
                }
            }

            tax_value_txt.setText((String.valueOf(total_tax)));
            calculateTaxRate();

        } catch (NumberFormatException e) {
            e.printStackTrace();
        }

    }

    private void setDefaultTax(List<Tax> tax_default_list) {
        for (Tax tax1 : listFilter) {
            for (Tax tax2 : tax_default_list) {
                if (tax1.getTaxId().equals(tax2.getTaxId())) {
                    tax1.setRate(tax2.getRate());
                    tax1.setSelect(true);
                    total_tax = Float.parseFloat(tax1.getRate()) + total_tax;
                    break;
                }
            }
        }

        tax_value_txt.setText((String.valueOf(total_tax)));
        calculateTaxRate();

        Log.e("", "");
    }


    @Override
    public void setFieldWorKerList(List<FieldWorker> fieldWorkerList) {
        this.fieldWorkerList = fieldWorkerList;
        AppUtility.autocompletetextviewPopUpWindow(autocomplete_item);
        ShowFwList(fieldWorkerList);
        final Auto_Fieldworker_Adpter countryAdapter = new Auto_Fieldworker_Adpter(this,
                R.layout.custom_adapter_item_layout, (ArrayList<FieldWorker>) fieldWorkerList);
        autocomplete_item.setAdapter(countryAdapter);
        autocomplete_item.setThreshold(1);
        autocomplete_item.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                itemId = ((FieldWorker) adapterView.getItemAtPosition(i)).getUsrId();
                itemlayout.setHintEnabled(true);
                setFieldWorkerData(((FieldWorker) adapterView.getItemAtPosition(i)));
                IS_FW_MANDATRY = true;
            }
        });
    }


    /**
     * fillter already added FW's
     **/
    private void ShowFwList(List<FieldWorker> fieldWorkerList) {
        if (jobModel != null && jobModel.getItemData() != null) {
            for (InvoiceItemDataModel itemData : jobModel.getItemData()) {
                for (FieldWorker fw : fieldWorkerList) {
                    if (itemData.getDataType().equals("2") && fw.getUsrId().equals(itemData.getItemId())) {
                        fieldWorkerList.remove(fw);
                        break;
                    }
                }
            }
        }
    }

    private void setFieldWorkerData(FieldWorker fieldWorkerselected) {
        itemId = fieldWorkerselected.getUsrId();
        dataType = "2";
        itemType = "";
        inm = "";
        /** quantity always be 1 initially */
        edt_item_qty.setText("1");
        edt_item_rate.setText("0");
        edt_item_supplier.setText("0");
        edt_item_disc.setText("0");
        tax_value_txt.setText("0");
        total_Amount_cal();
    }

    @Override
    public void setJobtitleList(List<JobTitle> servicesItemList) {
        this.servicesItemList = servicesItemList;
        ShowServicesList(servicesItemList);
        AppUtility.autocompletetextviewPopUpWindow(autocomplete_item);
        autocomplete_item.setTag("Services");
        final Services_item_Adapter services_item_adapter = new Services_item_Adapter(this,
                R.layout.custom_adapter_item_layout, (ArrayList<JobTitle>) servicesItemList);
        autocomplete_item.setAdapter(services_item_adapter);
        autocomplete_item.setThreshold(1);
        autocomplete_item.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                itemlayout.setHintEnabled(true);
                setServiceData(((JobTitle) adapterView.getItemAtPosition(i)));
            }
        });

    }

    private void setServiceData(JobTitle serViceItem) {
        jtId = serViceItem.getJtId();
        IS_SERVICES_MANDATRY = true;
        dataType = "3";
        itemType = "";
        itemId = serViceItem.getJtId();
        inm = serViceItem.getName();
        isBillable = "1";
        edt_item_qty.setText("1"); // quantity always be 1 initially
        edt_item_rate.setText(serViceItem.getLabour());
        edt_item_supplier.setText("0");
        edt_item_disc.setText("0");
        rediogrp.setVisibility(View.GONE);
        isBillableChange = "";
        setServicesTax(serViceItem.getTaxData());
        total_Amount_cal();
    }

    /**
     * set tax value for specifiec lable when set default job service tax amount
     */
    private void setServicesTax(List<TaxData> taxDataList) {
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
    }

    /**
     * not show alredy exiting services in services  dropdown
     */
    private void ShowServicesList(List<JobTitle> servicesItemList) {
        if (jobModel != null && jobModel.getItemData() != null) {
            for (InvoiceItemDataModel itemData : jobModel.getItemData()) {
                for (JobTitle jobTitle : servicesItemList) {
                    if (itemData.getDataType().equals("3") && jobTitle.getJtId().equals(itemData.getJtId())) {
                        servicesItemList.remove(jobTitle);
                        break;
                    }
                }
            }
        }
    }


    /******Add taxe's when add new Item ****/
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


                if (locId.equals("0")) {
                    listFilter.add(tax);
                } else if (locId.equals(tax.getLocId()) || tax.getLocId().equals("0")) {
                    listFilter.add(tax);
                }

                // listFilter.add(tax);
                Log.e("", "");
            }
        }

        Collections.sort(listFilter, new Comparator<Tax>() {
            @Override
            public int compare(Tax o1, Tax o2) {
                return o2.getLabel().compareTo(o1.getLabel());
            }
        });
        Log.e("", "");

    }


    @Override
    public void afterTextChanged(Editable s) {

    }


    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.autocomplete_item:
                if (DP_OPEN && autocomplete_item.getTag().equals("Services")) {
                    if (servicesItemList.size() > 0)
                        autocomplete_item.showDropDown();
                    else {
                        showDisError(LanguageController.getInstance().getMobileMsgByKey(AppConstant.job_services_use));
                    }
                } else if (DP_OPEN && autocomplete_item.getTag().equals("Fw")) {
                    if (fieldWorkerList.size() > 0)
                        autocomplete_item.showDropDown();
                    else {
                        showDisError(LanguageController.getInstance().getMobileMsgByKey(AppConstant.job_fieldworker_use));
                    }
                } else if (DP_OPEN) {
                    autocomplete_item.showDropDown();
                }
                break;
            case R.id.add_edit_item_Btn:
                add_edit_item_Btn.setEnabled(false);
                checkMandtryFileds();
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        add_edit_item_Btn.setEnabled(true);
                    }
                }, 500);

                break;
            case R.id.item_select:
                autocomplete_item.setTag("Item");
                invoiceItemPi.getInventryItemList();
                setTxtBkgColor(1);
                fw_service_filed_hide(1);
                break;
            case R.id.fw_select:
                autocomplete_item.setTag("Fw");
                setTxtBkgColor(2);
                invoiceItemPi.getFwList();
                fw_service_filed_hide(2);
                break;
            case R.id.tax_value_txt:
                showDialogTax();
                break;
            case R.id.service_select:
                autocomplete_item.setTag("Services");
                setTxtBkgColor(3);
                invoiceItemPi.getJobServiceTittle();
                fw_service_filed_hide(3);
                break;
        }
    }


    private void checkMandtryFileds() {
        if (!IS_ITEM_MANDATRY && TAB_SELECT == 1) {
            AppUtility.alertDialog(this, "", LanguageController.getInstance().getMobileMsgByKey(AppConstant.item_empty), LanguageController.getInstance().getMobileMsgByKey(AppConstant.ok),
                    "", new Callable<Boolean>() {
                        @Override
                        public Boolean call() throws Exception {
                            return null;
                        }
                    });
            return;
        } else if (!IS_FW_MANDATRY && TAB_SELECT == 2) {
            AppUtility.alertDialog(this, "", LanguageController.getInstance().getMobileMsgByKey(AppConstant.fw_valid), LanguageController.getInstance().getMobileMsgByKey(AppConstant.ok), "", new Callable<Boolean>() {
                @Override
                public Boolean call() throws Exception {
                    return null;
                }
            });
            return;
        } else if (!IS_SERVICES_MANDATRY && TAB_SELECT == 3) {
            AppUtility.alertDialog(this, "", LanguageController.getInstance().getMobileMsgByKey(AppConstant.service_error), AppConstant.ok, "", new Callable<Boolean>() {
                @Override
                public Boolean call() throws Exception {
                    return null;
                }
            });
            return;
        } else {

            if (updateItemDataModel == null) {
                addItemsOnJob();
            } else {
                updateItemsOnJob();
            }
        }

    }

    private void addItemsOnJob() {
        try {
            HyperLog.i("TAG", "addItemsOnJob(M) started");

            List<Tax> taxListFilter = new ArrayList<>();
            for (Tax tax : listFilter) {
                if (tax.isSelect())
                    taxListFilter.add(tax);
            }


            InvoiceItemDataModel addItemDataModel =
                    new InvoiceItemDataModel(autocomplete_item.getText().toString().trim(),
                            inm, itemId, dataType,
                            itemType, edt_item_rate.getText().toString().trim(), edt_item_qty.getText().toString().trim(),
                            edt_item_disc.getText().toString().trim(), edt_item_desc.getText().toString().trim(),
                            edt_hsnCode.getText().toString().trim(), edt_part_no.getText().toString().trim(),
                            edt_unit.getText().toString().trim(),
                            (String.valueOf(taxAmount))
                            , edt_item_supplier.getText().toString().trim()
                            , taxListFilter, jtId, edt_serialNo.getText().toString().trim(), isBillableChange);

            try {
                if (!isBillable.equals("")) {
                    addItemDataModel.setIsBillable(isBillable);
                }
            } catch (Exception exception) {
                exception.getMessage();
            }

            List<InvoiceItemDataModel> itemDataList = new ArrayList<>();

            itemDataList.add(addItemDataModel);
            if (jobModel != null && jobModel.getItemData() != null) {
                itemDataList.addAll(jobModel.getItemData());
            }

            /****Add  item's in job***/
            AppDataBase.getInMemoryDatabase(EotApp.getAppinstance()).jobModel().updateJobitems(jobId, itemDataList);


            /******/
            if (jobModel.getJobId().equals(jobModel.getTempId())) {
                addItemWitoutJobSync(itemDataList);
            } else {
                addItemWithJobSync(addItemDataModel);
            }
            HyperLog.i("TAG", "addItemsOnJob(M) finished");

            finish_Activity();
        } catch (Exception ex) {
            ex.printStackTrace();
            HyperLog.i("TAG", "addItemsOnJob(M) exception:" + ex.toString());

        }

    }

    private void addItemWithJobSync(InvoiceItemDataModel addItemDataModel) {
        try {
            HyperLog.i("TAG", "addItemWithJobSync(M) start");

            boolean noItem = false;
            List<Offlinetable> offlinetableList = AppDataBase.getInMemoryDatabase(EotApp.getAppinstance()).offlinemodel()
                    .getOfflinetablesById(Service_apis.addItemOnJob);
            if (offlinetableList != null && offlinetableList.size() > 0) {
                HyperLog.i("TAG", "addItemWithJobSync(M) item found");

                for (Offlinetable offlinetableModel : offlinetableList) {
                    AddInvoiceItemReqModel reqModel = new Gson().fromJson(offlinetableModel.getParams(), AddInvoiceItemReqModel.class);
                    if (reqModel.getJobId().equals(jobId)) {

                        HyperLog.i("Add Item :::", "noItem");
                        reqModel.getItemData().add(addItemDataModel);
                        offlinetableModel.setParams(new Gson().toJson(reqModel));
                        AppDataBase.getInMemoryDatabase(EotApp.getAppinstance()).offlinemodel().update(offlinetableModel);
                        noItem = true;
                        break;
                    }
                }

                if (!noItem) {
                    HyperLog.i("TAG", "addItemWithJobSync(M) item not found");

                    List<InvoiceItemDataModel> list = new ArrayList<>();
                    list.add(addItemDataModel);
                    AddInvoiceItemReqModel object = new AddInvoiceItemReqModel(jobModel.getJobId(), list, addItemOnInvoice, locId);
                    String dateTime = AppUtility.getDateByFormat(AppConstant.DATE_TIME_FORMAT);
                    Gson gson = new Gson();
                    String addJobReqest = gson.toJson(object);
                    OfflineDataController.getInstance().addInOfflineDB(Service_apis.addItemOnJob, addJobReqest, dateTime);
                }

            } else {

                HyperLog.i("TAG", "addItemWithJobSync(M) item added in offline quque");

                List<InvoiceItemDataModel> list = new ArrayList<>();
                list.add(addItemDataModel);
                AddInvoiceItemReqModel object = new AddInvoiceItemReqModel(jobModel.getJobId(), list, addItemOnInvoice, locId);
                String dateTime = AppUtility.getDateByFormat(AppConstant.DATE_TIME_FORMAT);
                Gson gson = new Gson();
                String addJobReqest = gson.toJson(object);
                OfflineDataController.getInstance().addInOfflineDB(Service_apis.addItemOnJob, addJobReqest, dateTime);
            }

            HyperLog.i("TAG", "addItemWithJobSync(M) finish");


        } catch (Exception e) {
            HyperLog.i("TAG", "addItemWithJobSync(M) exception:" + e.toString());

            e.printStackTrace();
        }


    }


    /***Add Item's Without Job Id**/
    private void addItemWitoutJobSync(List<InvoiceItemDataModel> itemDataList) {
        try {
            HyperLog.i("TAG", "addItemWitoutJobSync(M) start");

            JobOfflineDataModel jobOfflineDataModel = AppDataBase.getInMemoryDatabase(EotApp.getAppinstance()).jobOfflineDao().
                    getJobofflineDataForInvoice(Service_apis.addItemOnJob, jobModel.getTempId());
            if (jobOfflineDataModel != null) {
                AddInvoiceItemReqModel addInvoiceItemReqModel = new Gson().fromJson(jobOfflineDataModel.getParams(), AddInvoiceItemReqModel.class);
                addInvoiceItemReqModel.setItemData(itemDataList);
                jobOfflineDataModel.setParams(new Gson().toJson(addInvoiceItemReqModel));
                AppDataBase.getInMemoryDatabase(EotApp.getAppinstance()).jobOfflineDao().update(jobOfflineDataModel);
                //  HyperLog.i(" Item added:", "addItemWitoutJobSync----");
                HyperLog.i("TAG", "addItemWitoutJobSync(M) addItemWitoutJobSync");

            } else {
                String dateTime = AppUtility.getDateByFormat(AppConstant.DATE_TIME_FORMAT);
                Gson gson = new Gson();
                AddInvoiceItemReqModel addInvoiceItem = new AddInvoiceItemReqModel(jobId, itemDataList, addItemOnInvoice, locId);
                JobOfflineDataModel model = new JobOfflineDataModel(Service_apis.addItemOnJob, gson.toJson(addInvoiceItem),
                        dateTime, jobModel.getTempId());
                AppDataBase.getInMemoryDatabase(EotApp.getAppinstance()).jobOfflineDao().insertJobOfflineData(model);
                HyperLog.i("TAG", "addItemWitoutJobSync(M) addItemWitoutJobSync else part");

            }
            HyperLog.i("TAG", "addItemWitoutJobSync(M) finish");


        } catch (Exception ex) {
            ex.printStackTrace();
            HyperLog.i("TAG", "addItemWitoutJobSync(M) exception:" + ex.toString());
        }

    }

    private void updateItemsOnJob() {
        try {
            HyperLog.i("TAG", "updateItemsOnJob(M) start");

            List<Tax> taxListFilter = new ArrayList<>();
            for (Tax tax : listFilter) {
                if (tax.isSelect())
                    taxListFilter.add(tax);
            }


            InvoiceItemDataModel updateItemModel = new InvoiceItemDataModel(autocomplete_item.getText().toString().trim()
                    , inm, updateItemDataModel.getIjmmId(),
                    itemId, dataType, itemType, edt_item_rate.getText().toString().trim(), edt_item_qty.getText().toString().trim(),
                    edt_item_disc.getText().toString().trim(),
                    edt_item_desc.getText().toString().trim(),
                    edt_hsnCode.getText().toString().trim(), edt_part_no.getText().toString().trim(),
                    edt_unit.getText().toString().trim(),
                    (String.valueOf(taxAmount)),
                    edt_item_supplier.getText().toString().trim()
                    , taxListFilter, jtId, edt_serialNo.getText().toString().trim(), updateItemDataModel.getItemConvertCount()
                    , isBillableChange
                    //        , updateItemDataModel.getIsBillable()
            );

            try {
                if (!isBillable.equals("")) {
                    updateItemModel.setIsBillable(isBillable);
                }
            } catch (Exception exception) {
                exception.getMessage();
            }


            List<InvoiceItemDataModel> itemDataList = new ArrayList<>();
            /***not sync item update**/
            if (updateItemDataModel.getDataType().equals("1") || (updateItemDataModel.getDataType().equals("2"))
                    && updateItemDataModel.getItemType().equals("0") || updateItemDataModel.getItemType().equals("")) {

                HyperLog.i("TAG", "updateItemsOnJob(M) not sync item update");

                /**check inventry item **/
                if (jobModel != null && jobModel.getItemData() != null) {
                    HyperLog.i("TAG", "updateItemsOnJob(M) check inventry item ");

                    for (InvoiceItemDataModel tempModel : jobModel.getItemData()) {
                        if (tempModel.getItemId().equals(updateItemDataModel.getItemId())) {
                            jobModel.getItemData().remove(tempModel);
                            break;
                        }
                    }
                }
            } else if (updateItemDataModel.getDataType().equals("1") && updateItemDataModel.getItemType().equals("1")) {
                /**check Non- inventry item **/
                HyperLog.i("TAG", "updateItemsOnJob(M) check Non- inventry item ");

                if (jobModel != null && jobModel.getItemData() != null) {
                    for (InvoiceItemDataModel tempModel : jobModel.getItemData()) {
                        if (tempModel.getInm().equals(updateItemDataModel.getInm())) {
                            jobModel.getItemData().remove(tempModel);
                            break;
                        }
                    }
                }
            } else if (updateItemDataModel.getDataType().equals("3")) {
                /** Job services **/
                HyperLog.i("TAG", "updateItemsOnJob(M) Job services case");

                if (jobModel != null && jobModel.getItemData() != null) {
                    for (InvoiceItemDataModel tempModel : jobModel.getItemData()) {
                        if (tempModel.getItemId().equals(updateItemDataModel.getItemId())) {
                            jobModel.getItemData().remove(tempModel);
                            break;
                        }
                    }
                }
            }
            itemDataList.add(updateItemModel);
            if (jobModel != null && jobModel.getItemData() != null) {
                itemDataList.addAll(jobModel.getItemData());
            }


            /****update Invoice item's in job***/
            AppDataBase.getInMemoryDatabase(EotApp.getAppinstance()).jobModel().updateJobitems(jobId, itemDataList);


            /***UPDATE Item Without Job Sync**/
            if (jobModel.getJobId().equals(jobModel.getTempId())) {
                updateItemWithoutJobSync(updateItemModel);
            } else {
                if (updateItemDataModel.getIjmmId().equals("")) {
                    /**Item Update Without Sync***/
                    notSyncItemUpdate(updateItemModel);
                } else {
                    /***Item update After Sync**/
                    updateSyncItem(updateItemModel);
                }
            }

            HyperLog.i("TAG", "updateItemsOnJob(M) finish");

            finish_Activity();
        } catch (Exception ex) {
            ex.printStackTrace();
            HyperLog.i("TAG", "updateItemsOnJob(M) exception:" + ex.toString());

        }

    }

    /***Item update After Sync**/
    private void updateSyncItem(InvoiceItemDataModel updateItemModel) {
        try {
            List<Offlinetable> offlinetableList = AppDataBase.getInMemoryDatabase(EotApp.getAppinstance()).offlinemodel().getOfflinetablesById(Service_apis.updateItemInJobMobile);
            List<InvoiceItemDataModel> tempList = new ArrayList<>();
            tempList.add(updateItemModel);
            if (offlinetableList.size() > 0) {
                for (Offlinetable offLineModel : offlinetableList) {
                    AddInvoiceItemReqModel updateItemReqModel = new Gson().fromJson(offLineModel.getParams(), AddInvoiceItemReqModel.class);
                    if (updateItemReqModel.getJobId().equals(jobId)) {
                        updateItemReqModel.getItemData().clear();
                        updateItemReqModel.setItemData(tempList);
                        offLineModel.setParams(new Gson().toJson(updateItemReqModel));
                        AppDataBase.getInMemoryDatabase(EotApp.getAppinstance()).offlinemodel().update(offLineModel);
                        break;
                    }
                }
            } else {
                AddInvoiceItemReqModel updateItemReqModel = new AddInvoiceItemReqModel(jobId, tempList, addItemOnInvoice, locId);
                String dateTime = AppUtility.getDateByFormat(AppConstant.DATE_TIME_FORMAT);
                Gson gson = new Gson();
                String addJobReqest = gson.toJson(updateItemReqModel);
                OfflineDataController.getInstance().addInOfflineDB(Service_apis.updateItemInJobMobile, addJobReqest, dateTime);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }

    }


    /***Job Sync But Item Not Sync Update
     * @param updateItemModel**/
    private void notSyncItemUpdate(InvoiceItemDataModel updateItemModel) {
        try {
            List<Offlinetable> offlineTableList = AppDataBase.getInMemoryDatabase(EotApp.getAppinstance()).offlinemodel().
                    getOfflinetablesById(Service_apis.addItemOnJob);

            if (offlineTableList != null && offlineTableList.size() > 0) {
                for (Offlinetable offLineModel : offlineTableList) {
                    AddInvoiceItemReqModel addItemReqModel = new Gson().fromJson(offLineModel.getParams(), AddInvoiceItemReqModel.class);
                    if (addItemReqModel.getJobId().equals(jobId)) {

                        for (InvoiceItemDataModel tempModel : addItemReqModel.getItemData()) {
                            if (tempModel.getTempNm().equals(updateItemDataModel.getTempNm())) {
                                addItemReqModel.getItemData().remove(tempModel);
                                addItemReqModel.getItemData().add(updateItemModel);
                                offLineModel.setParams(new Gson().toJson(addItemReqModel));
                                AppDataBase.getInMemoryDatabase(EotApp.getAppinstance()).offlinemodel().update(offLineModel);
                                break;
                            }
                        }

                        break;
                    }
                }
            }
        } catch (Exception ec) {
            ec.printStackTrace();
        }


    }

    private void updateItemWithoutJobSync(InvoiceItemDataModel updateItemModel) {
        try {
            HyperLog.i("TAG", "updateItemWithoutJobSync(M) start");

            JobOfflineDataModel jobOfflineDataModel = AppDataBase.getInMemoryDatabase(EotApp.getAppinstance()).jobOfflineDao().
                    getJobofflineDataForInvoice(Service_apis.addItemOnJob, jobModel.getTempId());
            List<InvoiceItemDataModel> tempItemList = new ArrayList<>();

            if (jobOfflineDataModel != null) {
                AddInvoiceItemReqModel addInvoiceItemReqModel = new Gson().fromJson(jobOfflineDataModel.getParams(), AddInvoiceItemReqModel.class);
                for (InvoiceItemDataModel tempModel : addInvoiceItemReqModel.getItemData()) {
                    if (tempModel.getTempNm().equals(updateItemDataModel.getTempNm())) {
                        addInvoiceItemReqModel.getItemData().remove(tempModel);
                        addInvoiceItemReqModel.getItemData().add(updateItemModel);
                        jobOfflineDataModel.setParams(new Gson().toJson(addInvoiceItemReqModel));
                        AppDataBase.getInMemoryDatabase(EotApp.getAppinstance()).jobOfflineDao().update(jobOfflineDataModel);
                        break;
                    }
                }
            } else {
                tempItemList.add(updateItemModel);
                String dateTime = AppUtility.getDateByFormat(AppConstant.DATE_TIME_FORMAT);
                Gson gson = new Gson();
                AddInvoiceItemReqModel addInvoiceItem = new AddInvoiceItemReqModel(jobId, tempItemList, addItemOnInvoice, locId);
                JobOfflineDataModel model = new JobOfflineDataModel(Service_apis.addItemOnJob, gson.toJson(addInvoiceItem),
                        dateTime, jobModel.getTempId());
                AppDataBase.getInMemoryDatabase(EotApp.getAppinstance()).jobOfflineDao().insertJobOfflineData(model);
            }
            HyperLog.i("TAG", "updateItemWithoutJobSync(M) finish");

        } catch (Exception e) {
            e.printStackTrace();
            HyperLog.i("TAG", "updateItemWithoutJobSync(M) exception:" + e.toString());

        }

    }


    private void fw_service_filed_hide(int type) {
        switch (type) {
            case 1:/** Show/enable all fields when inventry/Non inventry item Added */
                item_supplier_layout.setVisibility(View.VISIBLE);
                supplier_view.setVisibility(View.VISIBLE);

                item_hsnCode_layout.setVisibility(View.VISIBLE);
                hsncode_view.setVisibility(View.VISIBLE);

                item_partNo_layout.setVisibility(View.VISIBLE);
                part_no_view.setVisibility(View.VISIBLE);

                item_unit_layout.setVisibility(View.VISIBLE);
                unit_view.setVisibility(View.VISIBLE);

                item_discount_layout.setVisibility(View.VISIBLE);
                disc_view.setVisibility(View.VISIBLE);

                serialNo_layout.setVisibility(View.VISIBLE);
                seroal_no_view.setVisibility(View.VISIBLE);

                item_tax_rate_layout.setVisibility(View.VISIBLE);
                tax_rate_view.setVisibility(View.VISIBLE);
                break;
            case 2:/** hide supplier cost,han code,part no  when fieldworker added for Item */
                item_supplier_layout.setVisibility(View.GONE);
                supplier_view.setVisibility(View.GONE);

                item_hsnCode_layout.setVisibility(View.GONE);
                hsncode_view.setVisibility(View.GONE);

                item_partNo_layout.setVisibility(View.GONE);
                part_no_view.setVisibility(View.GONE);

                item_unit_layout.setVisibility(View.GONE);
                unit_view.setVisibility(View.GONE);

                item_discount_layout.setVisibility(View.VISIBLE);
                disc_view.setVisibility(View.VISIBLE);

                serialNo_layout.setVisibility(View.GONE);
                seroal_no_view.setVisibility(View.GONE);

                item_tax_rate_layout.setVisibility(View.VISIBLE);
                tax_rate_view.setVisibility(View.VISIBLE);
                break;
            case 3:/** hide supplier cost,han code,part no & unit  when Job service(title) added for Item */

                item_supplier_layout.setVisibility(View.GONE);
                supplier_view.setVisibility(View.GONE);

                disc_view.setVisibility(View.GONE);
                item_discount_layout.setVisibility(View.GONE);

                item_hsnCode_layout.setVisibility(View.VISIBLE);
                hsncode_view.setVisibility(View.VISIBLE);

                item_partNo_layout.setVisibility(View.VISIBLE);
                part_no_view.setVisibility(View.VISIBLE);

                item_unit_layout.setVisibility(View.VISIBLE);
                unit_view.setVisibility(View.VISIBLE);

                serialNo_layout.setVisibility(View.VISIBLE);
                seroal_no_view.setVisibility(View.VISIBLE);

                item_tax_rate_layout.setVisibility(View.VISIBLE);
                tax_rate_view.setVisibility(View.VISIBLE);
                break;
        }
        hideShowInvoiceItemDetails();
    }


    private void getJobById() {
        jobModel = AppDataBase.getInMemoryDatabase(EotApp.getAppinstance()).jobModel().getJobsById(jobId);
        if (jobModel == null) {
            HyperLog.i("", "getJobById(M) job model null found");
            finish();
        } else if (jobModel.getItemData() != null && jobModel.getItemData().size() == 0) {
            jobItemCountForFlag = true;
        }
    }

    private void finish_Activity() {
        if (!NOITEMRELECT) {
            try {
                if (locId != null && !locId.equals("0") && jobModel != null && jobModel.getLocId() != null && !locId.equals(jobModel.getLocId())) {
                    jobModel.setLocId(locId);
                    AppDataBase.getInMemoryDatabase(EotApp.getAppinstance()).jobModel().updateJob(jobModel);
                }

            } catch (Exception exception) {
                exception.getMessage();
            }
        }

        Intent intent = new Intent();
        intent.putExtra("AddInvoiceItem", "AddItem");
        setResult(ADD_ITEM_DATA, intent);
        /***Notify Job Overview For Item Flag call only when Item First time Added****/
        if (jobItemCountForFlag) {
            EotApp.getAppinstance().getJobFlagOverView();
        }
        this.finish();
    }

    private void showDialogTax() {
        if (listFilter.size() > 0) {
            try {
                Collections.sort(listFilter, new Comparator<Tax>() {
                    @Override
                    public int compare(Tax tax, Tax t1) {
                        return t1.getLabel().compareTo(tax.getLabel());
                    }
                });
            } catch (Exception e) {
                e.printStackTrace();
            }

            final Dialog dialog = new Dialog(this);
            dialog.setCancelable(false);
            dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            dialog.setContentView(R.layout.tax_dialog_layout2);
            final LinearLayout root = dialog.findViewById(R.id.root);
            TextView txtRateHeading = dialog.findViewById(R.id.txtRateHeading);
            txtRateHeading.setText(LanguageController.getInstance().getMobileMsgByKey(AppConstant.tax_rate));
            TextView selected_tax_nm = dialog.findViewById(R.id.selected_tax_nm);
/**            add all taxes into the views */
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
                if (!child0.getText().toString().isEmpty()) {
                    float updatedtax = Float.parseFloat(child0.getText().toString());
                    count += updatedtax;
                    if (listFilter != null && listFilter.size() == root.getChildCount()) {
                        listFilter.get(i).setRate(String.valueOf(updatedtax));
                    }
                } else {
                    if (listFilter != null && listFilter.size() == root.getChildCount()) {
                        listFilter.get(i).setRate("0");
                    }
                }
            }

        }
        return count;
    }

    private void generateDynamicViews(final List<Tax> listFilter, LinearLayout root, final TextView selected_tax_nm) {
        // final Set<String> tempTaxList = new HashSet<>();
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

//                   else  if (tax.isSelect()) {
//                            tax.setSelect(false);
//                        }

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
                } else {
                    checkbox.setText(tax.getLabel() + " (" + tax.getRate() + "%)");
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
//        else
        // selected_tax_nm.setText(tempTaxList.toString().replace("[", "").replace("]", ""));
        for (Tax tax : listFilter) {
            if (tax.isSelect()) {
                selected_tax_nm.setText(tax.getLabel());
                break;
            } else {
                selected_tax_nm.setText(LanguageController.getInstance().getMobileMsgByKey(AppConstant.selected_tax_lable));
            }
        }

    }


    /**
     * set background color when tab change item/fw/services
     */
    private void setTxtBkgColor(int selected_option) {
        if (selected_option == 1) {//for item
            item_select.setBackgroundResource(R.drawable.item_tab_on_bkg);
            item_select.setTextColor(getResources().getColor(R.color.white));
            fw_select.setBackgroundResource(R.drawable.item_tab_off_bkg);
            fw_select.setTextColor(getResources().getColor(R.color.colorPrimary));
            service_select.setBackgroundResource(R.drawable.item_tab_off_bkg);
            service_select.setTextColor(getResources().getColor(R.color.colorPrimary));
            autocomplete_item.setText("");
            autocomplete_item.setHint(LanguageController.getInstance().getMobileMsgByKey(AppConstant.items_name) + " *");
            DP_OPEN = false;
            TAB_SELECT = 1;
            rediogrp.setVisibility(View.VISIBLE);
            hideShowInvoiceItemDetails();

        } else if (selected_option == 2) {//for fieldworker
            item_select.setBackgroundResource(R.drawable.item_tab_off_bkg);
            item_select.setTextColor(getResources().getColor(R.color.colorPrimary));
            fw_select.setBackgroundResource(R.drawable.item_tab_on_bkg);
            fw_select.setTextColor(getResources().getColor(R.color.white));
            service_select.setBackgroundResource(R.drawable.item_tab_off_bkg);
            service_select.setTextColor(getResources().getColor(R.color.colorPrimary));
            autocomplete_item.setText("");
            autocomplete_item.setHint(LanguageController.getInstance().getMobileMsgByKey(AppConstant.fieldworkers_name) + " *");
            DP_OPEN = true;
            TAB_SELECT = 2;
            ITEMSYNC = false;
            rediogrp.setVisibility(View.GONE);
            hideShowInvoiceItemDetails();
        } else {//for services
            item_select.setBackgroundResource(R.drawable.item_tab_off_bkg);
            item_select.setTextColor(getResources().getColor(R.color.colorPrimary));
            fw_select.setBackgroundResource(R.drawable.item_tab_off_bkg);
            fw_select.setTextColor(getResources().getColor(R.color.colorPrimary));
            service_select.setBackgroundResource(R.drawable.item_tab_on_bkg);
            service_select.setTextColor(getResources().getColor(R.color.white));
            autocomplete_item.setText("");
            autocomplete_item.setHint(LanguageController.getInstance().getMobileMsgByKey(AppConstant.services_name) + " *");
            DP_OPEN = true;
            TAB_SELECT = 3;
            ITEMSYNC = false;
            rediogrp.setVisibility(View.GONE);

            hideShowInvoiceItemDetails();
        }
    }


    /**
     * hide/show add item field accroding to admin authentication
     */
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

            if (compPermission.getSerialNo().equals("1")) {
                serialNo_layout.setVisibility(View.GONE);
                seroal_no_view.setVisibility(View.GONE);
            }

            if (compPermission.getIsRateIncludingTax().equals("1")) {
                item_tax_rate_layout.setVisibility(View.GONE);
                tax_rate_view.setVisibility(View.GONE);
            }
        }
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
    public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
    }

    @Override
    public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
        if (charSequence.length() >= 1) {
            if (charSequence.hashCode() == edt_item_desc.getText().hashCode())
                item_desc_layout.setHintEnabled(true);
            if (charSequence.hashCode() == edt_item_qty.getText().hashCode())
                item_qty_layout.setHintEnabled(true);
            if (charSequence.hashCode() == edt_item_rate.getText().hashCode()) {
                item_rate_layout.setHintEnabled(true);
                calculateTaxRate();
            }
            if (charSequence.hashCode() == edt_item_supplier.getText().hashCode())
                item_supplier_layout.setHintEnabled(true);
            if (charSequence.hashCode() == edt_item_disc.getText().hashCode()) {
                item_discount_layout.setHintEnabled(true);
                /** discount must not be gratter than 100 */
                if (Float.parseFloat(edt_item_disc.getText().toString()) > 100) {
                    showDisError(LanguageController.getInstance().getMobileMsgByKey(AppConstant.discountError));
                    edt_item_disc.setText("0");
                }
                calculateTaxRate();

            }
            if (charSequence.hashCode() == edt_part_no.getText().hashCode())
                item_partNo_layout.setHintEnabled(true);
            if (charSequence.hashCode() == edt_hsnCode.getText().hashCode())
                item_hsnCode_layout.setHintEnabled(true);
            if (charSequence.hashCode() == edt_unit.getText().hashCode())
                item_unit_layout.setHintEnabled(true);
            if (charSequence.hashCode() == edt_serialNo.getText().hashCode())
                serialNo_layout.setHintEnabled(true);
            if (charSequence.hashCode() == edt_item_tax_rate.getText().hashCode())
                item_tax_rate_layout.setHintEnabled(true);

            total_Amount_cal();
        } else if (charSequence.length() <= 0) {
            if (charSequence.hashCode() == edt_item_desc.getText().hashCode())
                item_desc_layout.setHintEnabled(false);
            if (charSequence.hashCode() == edt_item_qty.getText().hashCode())
                item_qty_layout.setHintEnabled(false);
            if (charSequence.hashCode() == edt_item_rate.getText().hashCode()) {
                item_rate_layout.setHintEnabled(false);
                calculateTaxRate();
            }
            if (charSequence.hashCode() == edt_item_supplier.getText().hashCode())
                item_supplier_layout.setHintEnabled(false);
            if (charSequence.hashCode() == edt_item_disc.getText().hashCode()) {
                item_discount_layout.setHintEnabled(false);
                calculateTaxRate();
            }
            if (charSequence.hashCode() == edt_part_no.getText().hashCode())
                item_partNo_layout.setHintEnabled(false);
            if (charSequence.hashCode() == edt_hsnCode.getText().hashCode())
                item_hsnCode_layout.setHintEnabled(false);
            if (charSequence.hashCode() == edt_unit.getText().hashCode())
                item_unit_layout.setHintEnabled(false);
            if (charSequence.hashCode() == edt_serialNo.getText().hashCode())
                serialNo_layout.setHintEnabled(false);
            if (charSequence.hashCode() == edt_item_tax_rate.getText().hashCode())
                item_tax_rate_layout.setHintEnabled(false);
            total_Amount_cal();
        }
    }

    private void showDisError(String msg) {
        AppUtility.alertDialog(this, "", msg, LanguageController.getInstance().getMobileMsgByKey(AppConstant.ok),
                "", new Callable<Boolean>() {
                    @Override
                    public Boolean call() throws Exception {
                        return true;
                    }
                });
    }

    /**
     * tax calculation for current add/edit item
     */
    private void total_Amount_cal() {

        double qty = 0, rate = 0, dis = 0;
        double amount = 0;
        /**  check of amount calculation */
        try {
            if (!edt_item_qty.getText().toString().equals("")) {
                qty = Double.parseDouble((edt_item_qty.getText().toString()));
            }
            if (!edt_item_rate.getText().toString().equals("")) {
                rate = Double.parseDouble(AppUtility.getRoundoff_amount(edt_item_rate.getText().toString()));
            }
            if (!edt_item_disc.getText().toString().equals("")) {
                dis = Double.parseDouble(AppUtility.getRoundoff_amount(edt_item_disc.getText().toString()));
            }

        } catch (Exception ex) {
            ex.getMessage();
        }


        /**  */
        if (App_preference.getSharedprefInstance().getLoginRes().getTaxCalculationType().equals("0")) {
//            amount = (qty * rate + qty * ((rate * total_tax) / 100)) - qty * ((rate * dis) / 100);
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

        } else if (App_preference.getSharedprefInstance().getLoginRes().getTaxCalculationType().equals("1")) {
            double newRate = (rate - ((rate * dis) / 100));
            amount = qty * (newRate + ((newRate * total_tax) / 100));


            taxAmount = ((total_tax * rate * qty) / 100);
            String tax_Amount = AppUtility.getRoundoff_amount(String.valueOf(taxAmount));
            taxamount_value_txt.setText(tax_Amount);
        }


        String amountString = AppUtility.getRoundoff_amount(String.valueOf(amount));
        amount_value_txt.setText(amountString);

    }


    private void calculateTaxRate() {
        if (!App_preference.getSharedprefInstance().getLoginRes().getCompPermission().get(0).getIsRateIncludingTax().equals("1")) {

            double rate = 0, dis = 0;

            /**  check of amount calculation */
            try {

                if (!edt_item_rate.getText().toString().equals("")) {
                    rate = Double.parseDouble(AppUtility.getRoundoff_amount(edt_item_rate.getText().toString()));
                }
                if (!edt_item_disc.getText().toString().equals("")) {
                    dis = Double.parseDouble(AppUtility.getRoundoff_amount(edt_item_disc.getText().toString()));
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

        }


    }


    @Override
    public void onBackPressed() {
        NOITEMRELECT = true;
        finish_Activity();
        super.onBackPressed();
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            onBackPressed();
            AppUtility.hideSoftKeyboard(AddEditInvoiceItemActivity2.this);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void setEmptyFields() {
        dataType = "";
        itemType = "";
        itemId = "";
        isBillable = "";
        isBillableChange = "";
        total_tax = 0f;
        edt_item_desc.setText("");
        edt_item_qty.setText("1");
        edt_item_rate.setText("0");
        edt_item_supplier.setText("0");
        edt_item_disc.setText("0");
        tax_value_txt.setText("0");
        amount_value_txt.setText("0");
        taxamount_value_txt.setText("0");
        edt_part_no.setText("0");
        edt_hsnCode.setText("0");
        edt_serialNo.setText("");
        rediogrp.setVisibility(View.GONE);
        IS_SERVICES_MANDATRY = false;
        setTaxDialogFiledsEmpty();
    }

    private void setTaxDialogFiledsEmpty() {
        for (Tax tax : listFilter) {
            tax.setSelect(false);
        }
    }

    /**** set default values 0 when new item added*/
    private void setDefaultValuesForAddNewItem() {
        /***** quantity always be 1 initially*/
        edt_item_qty.setText("1");
        edt_item_rate.setText("0");
        edt_item_supplier.setText("0");
        edt_item_disc.setText("0");
        tax_value_txt.setText("0");
        amount_value_txt.setText("0");
        taxamount_value_txt.setText("0");
        edt_item_tax_rate.setText("0");
        edt_hsnCode.setHint(App_preference.getSharedprefInstance().getLoginRes().getHsnCodeLable());
    }


}

