package com.eot_app.nav_menu.jobs.job_detail.invoice.add_edit_invoice_pkg;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.eot_app.R;
import com.eot_app.login_next.login_next_model.CompPermission;
import com.eot_app.nav_menu.jobs.job_detail.invoice.Auto_Inventry_Adpter;
import com.eot_app.nav_menu.jobs.job_detail.invoice.add_edit_invoice_pkg.add_edit_inv_mvp.Add_Edit_Inv_Pc;
import com.eot_app.nav_menu.jobs.job_detail.invoice.add_edit_invoice_pkg.add_edit_inv_mvp.Add_Edit_Inv_Pi;
import com.eot_app.nav_menu.jobs.job_detail.invoice.add_edit_invoice_pkg.add_edit_inv_mvp.Add_Edit_Inv_View;
import com.eot_app.nav_menu.jobs.job_detail.invoice.add_edit_invoice_pkg.dropdown_item_pkg.Auto_Fieldworker_Adpter;
import com.eot_app.nav_menu.jobs.job_detail.invoice.add_edit_invoice_pkg.dropdown_item_pkg.Services_item_Adapter;
import com.eot_app.nav_menu.jobs.job_detail.invoice.inventry_pkg.Inventry_ReS_Model;
import com.eot_app.nav_menu.jobs.job_detail.invoice.invoice_detail_pkg.inv_detail_model.Inv_Res_Model;
import com.eot_app.nav_menu.jobs.job_detail.invoice.invoice_detail_pkg.inv_detail_model.ItemData;
import com.eot_app.nav_menu.jobs.job_detail.invoice.invoice_detail_pkg.inv_detail_model.ShippingItem;
import com.eot_app.nav_menu.jobs.job_detail.invoice.invoice_detail_pkg.inv_detail_model.Tax;
import com.eot_app.nav_menu.jobs.job_detail.invoice.invoice_model_pkg.NewItem;
import com.eot_app.utility.AppConstant;
import com.eot_app.utility.AppUtility;
import com.eot_app.utility.App_preference;
import com.eot_app.utility.language_support.LanguageController;
import com.eot_app.utility.settings.jobtitle.TaxData;
import com.eot_app.utility.settings.setting_db.FieldWorker;
import com.eot_app.utility.settings.setting_db.JobTitle;
import com.google.android.material.textfield.TextInputLayout;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;

public class Add_Edit_Inv_Invoice_Activity extends AppCompatActivity implements TextWatcher, View.OnClickListener,
        Add_Edit_Inv_View, AdapterView.OnItemSelectedListener, View.OnFocusChangeListener {
    private final List<Tax> tax = new ArrayList<>();
    double taxAmount = 0;
    private float total_tax = 0f;
    private AutoCompleteTextView autocomplete_item;
    private List<Tax> listFilter = new ArrayList<>();
    private EditText edt_item_desc, edt_item_qty, edt_item_rate, edt_item_supplier, edt_item_disc, edt_part_no, edt_hsnCode, edt_unit;
    private TextInputLayout item_desc_layout, item_qty_layout, item_rate_layout, item_discount_layout, itemlayout, item_hsnCode_layout, item_partNo_layout, item_unit_layout, item_supplier_layout;
    private Button add_edit_item_Btn;
    private Add_Edit_Inv_Pi add_edit_pi;
    private Inv_Res_Model invoice_Details;
    private String itemId = "";
    private int type;
    private ItemData ItemData_Add_Edit = null;
    private TextView item_select, fw_select, service_select;
    private String itemName = "";
    private NewItem newItem;
    private int itype = 0;
    private ItemData itemData;
    private LinearLayout layout_fw_item, taxamount_layout;
    private double roundOff;
    private View nm_view, desc_view, qty_view, rate_view, supplier_view, disc_view, tax_view, amount_view, part_no_view, hsncode_view, unit_view, taxrateAmount_view;
    private TextView tax_value_txt, tax_txt_hint, amount_value_txt, taxamount_txt_hint, taxamount_value_txt, amount_txt_hint;
    private LinearLayout tax_layout, amount_layout;
    private int isfwOrItem = 1;
    private String jtId = "";
    private int isItemOrTitle;
    private boolean isOnTextChanged = false;
    private boolean DP_OPEN = false;
    private List<JobTitle> servicesItemList = new ArrayList<>();
    private List<FieldWorker> fieldWorkerList = new ArrayList<>();
    private List<Inventry_ReS_Model> list = new ArrayList<>();
    private boolean isMandtryItem, isMandtryFw, isMandtryServices;

    private boolean updateItem = false;
    private RelativeLayout add_quote_item_layout;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit__invoice);

        initializelables();
        intializeViews();

        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {

            if (getIntent().hasExtra("AddInvoice")) {
                String convertObject = bundle.getString("AddInvoice");
                invoice_Details = new Gson().fromJson(convertObject, Inv_Res_Model.class);

                /**Add shipping item in new item array**/
                //addShipingItem();

                setDefaultValuesForAddNewItem();

/**           we have to get a new tax items from the server */
                add_edit_pi.getTaxList();

                /** supplier cost editable if item found */
                edt_item_supplier.setEnabled(true);

                /** fw/item/services list required for add item */
                add_edit_pi.initialize_FwList_ServiceTittle_inventoryList();

            } else if (getIntent().hasExtra("EditInvoice")) {
                layout_fw_item.setVisibility(View.GONE);
                String convertObject = bundle.getString("EditInvoice");
                invoice_Details = new Gson().fromJson(convertObject, Inv_Res_Model.class);
                ItemData itemdataEdit = new Gson().fromJson(bundle.getString("Itemdata"), ItemData.class);
                updateItem = true;
                if (itemdataEdit.getTax().size() == 0) {
                    /** we have to get a new tax items from the server when tax list empty */
                    add_edit_pi.getTaxList();
                } else {
                    listFilter = itemdataEdit.getTax();
                    for (Tax tax : listFilter) {
//                        if (tax.getRate().isEmpty()) {
//                            /** Rate use for request becoz txrate not send */
//                           //tax.setTxRate("0");
//                          //  tax.setRate("0");
//                        } else {
//                           // tax.setTxRate(tax.getRate());
//                        }
                        //    total_tax += Float.parseFloat(tax.getRate());
                    }
                }
                setItemFields(itemdataEdit);
            }
        }

        set_Title();

/** */
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
                            isOnTextChanged = true;
                            if (charSequence.length() >= 1) {
                                itemlayout.setHintEnabled(true);
                            } else if (charSequence.length() <= 0) {
                                itemlayout.setHintEnabled(false);
                            }
                            if (!updateItem) {
                                if (!list.contains(charSequence)) {
                                    itemName = charSequence.toString();
                                    itype = 1;
                                    isItemOrTitle = 1;
                                    setEmptyFields();
                                }
                            }
                            isMandtryItem = true;
                            break;
                        case "Fw":
                            if (charSequence.length() >= 1) {
                                itemlayout.setHintEnabled(true);
                            } else if (charSequence.length() <= 0) {
                                itemlayout.setHintEnabled(false);
                            }
                            if (!fieldWorkerList.contains(charSequence)) {
                                itemName = charSequence.toString();
                                itype = 1;
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
                            if (!servicesItemList.contains(charSequence)) {
                                itype = 0;
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
                switch (autocomplete_item.getTag().toString()) {
                    case "Item":
                        if (isOnTextChanged && editable.toString().length() == 3) {
                            add_edit_pi.updateSearchText(editable.toString());
                            //  autocomplete_item.removeTextChangedListener(null);
                            isOnTextChanged = false;
                        }
                        break;
                }
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
        AppUtility.setupUI(add_quote_item_layout, Add_Edit_Inv_Invoice_Activity.this);

        item_desc_layout.getEditText().addTextChangedListener(this);
        item_qty_layout.getEditText().addTextChangedListener(this);
        item_rate_layout.getEditText().addTextChangedListener(this);
        item_supplier_layout.getEditText().addTextChangedListener(this);
        item_discount_layout.getEditText().addTextChangedListener(this);
        item_partNo_layout.getEditText().addTextChangedListener(this);
        item_hsnCode_layout.getEditText().addTextChangedListener(this);
        item_unit_layout.getEditText().addTextChangedListener(this);


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

        autocomplete_item.addTextChangedListener(this);

        hideShowInvoiceItemDetails();

        if (App_preference.getSharedprefInstance().getLoginRes().getCompPermission().get(0).getIsItemEnable().equals("1")) {
            add_edit_item_Btn.setVisibility(View.GONE);
        }

        add_edit_pi = new Add_Edit_Inv_Pc(this);
        setItemdata(new ArrayList<Inventry_ReS_Model>());


        //        add_edit_pi.initialize_FwList_ServiceTittle_inventoryList();

//        initialize listener and item click of drop down views.
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
        }
    }

    // set default values 0 when new item added
    private void setDefaultValuesForAddNewItem() {
        edt_item_qty.setText("1"); // quantity always be 1 initially
        edt_item_rate.setText("0");
        edt_item_supplier.setText("0");
        edt_item_disc.setText("0");
        tax_value_txt.setText("0");
        amount_value_txt.setText("0");
        taxamount_value_txt.setText("0");
        edt_hsnCode.setHint(invoice_Details.getHsnCodeLable());
    }

    private void setItemFields(ItemData itemdata) {
        ItemData_Add_Edit = itemdata;
        tax_value_txt.setText(AppUtility.getRoundoff_amount(String.valueOf(total_tax)));
        /** type 1 for inventry item  */
        if (itemdata.getType() == 1) {
            autocomplete_item.setHint(LanguageController.getInstance().getMobileMsgByKey(AppConstant.item));
            setTxtBkgColor(1);
            fw_service_filed_hide(1);
            isMandtryItem = true;
            // isfwOrItem = 1;
        }
        /** type 2 for FieldWorker item  */
        else if (itemdata.getType() == 2) {
            setTxtBkgColor(2);
            autocomplete_item.setHint(LanguageController.getInstance().getMobileMsgByKey(AppConstant.fieldworkers_name));
            fw_service_filed_hide(2);
            isMandtryFw = true;
            isfwOrItem = 2;
        } else  /** type 3 for Job Services item  */
            if (itemdata.getType() == 3) {
                setTxtBkgColor(3);
                autocomplete_item.setHint(LanguageController.getInstance().getMobileMsgByKey(AppConstant.services_name));
                fw_service_filed_hide(3);
                isMandtryServices = true;
                isfwOrItem = 3;
            }
        autocomplete_item.setText(itemdata.getInm());
        autocomplete_item.setFocusableInTouchMode(false);
        autocomplete_item.dismissDropDown();
        itemlayout.setHintEnabled(true);

        edt_item_qty.setText(itemdata.getQty());
        edt_item_rate.setText(AppUtility.getRoundoff_amount((itemdata.getRate())));
        edt_item_supplier.setText(AppUtility.getRoundoff_amount((itemdata.getSupplierCost())));
        edt_item_disc.setText(itemdata.getDiscount());
        edt_item_desc.setText(itemdata.getDes());
        edt_part_no.setText(itemdata.getPno());
        edt_hsnCode.setHint(invoice_Details.getHsnCodeLable());
        edt_hsnCode.setText(itemdata.getHsncode());
        edt_unit.setText(itemdata.getUnit());

        itemId = itemdata.getItemId();
        type = itemdata.getType();
        itemName = itemdata.getInm();

        // DP_OPEN=false;
    }


    private void set_Title() {
        if (ItemData_Add_Edit == null) {
            /**  */
            getSupportActionBar().setTitle(LanguageController.getInstance().getMobileMsgByKey(AppConstant.addItem_screen_title));
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            add_edit_item_Btn.setText(LanguageController.getInstance().getMobileMsgByKey(AppConstant.save_btn));
        } else {
            /** set update title  */
            if (App_preference.getSharedprefInstance().getLoginRes().getCompPermission().get(0).getIsItemEnable().equals("0")) {
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
                qty = Double.parseDouble(AppUtility.getRoundoff_amount(edt_item_qty.getText().toString()));
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
        if (invoice_Details.getTaxCalculationType().equals("0")) {
//            amount = (qty * rate + qty * ((rate * total_tax) / 100)) - qty * ((rate * dis) / 100);
            try {
                rate = qty * rate;
                double t = (rate - ((rate * dis) / 100));
                amount = (t * total_tax) / 100;
            } catch (Exception exception) {
                exception.printStackTrace();
            }

        } else if (invoice_Details.getTaxCalculationType().equals("1")) {
            double newRate = (rate - ((rate * dis) / 100));
            amount = qty * (newRate + ((newRate * total_tax) / 100));
        }
        roundOff = Math.round(amount * 100.0) / 100.0;
        String amountString = AppUtility.getRoundoff_amount(String.valueOf(roundOff));
        amount_value_txt.setText(amountString);


        taxAmount = ((total_tax * rate * qty) / 100);
        String tax_Amount = AppUtility.getRoundoff_amount(String.valueOf(taxAmount));
        taxamount_value_txt.setText(tax_Amount);

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
            autocomplete_item.setHint(LanguageController.getInstance().getMobileMsgByKey(AppConstant.items_name));
            DP_OPEN = false;
            isMandtryItem = false;
            //  isfwOrItem = 1;
        } else if (selected_option == 2) {//for fieldworker
            item_select.setBackgroundResource(R.drawable.item_tab_off_bkg);
            item_select.setTextColor(getResources().getColor(R.color.colorPrimary));
            fw_select.setBackgroundResource(R.drawable.item_tab_on_bkg);
            fw_select.setTextColor(getResources().getColor(R.color.white));
            service_select.setBackgroundResource(R.drawable.item_tab_off_bkg);
            service_select.setTextColor(getResources().getColor(R.color.colorPrimary));
            autocomplete_item.setText("");
            autocomplete_item.setHint(LanguageController.getInstance().getMobileMsgByKey(AppConstant.fieldworkers_name));
            DP_OPEN = true;
            //   isfwOrItem = 2;
            isMandtryFw = false;
        } else {//for services
            item_select.setBackgroundResource(R.drawable.item_tab_off_bkg);
            item_select.setTextColor(getResources().getColor(R.color.colorPrimary));
            fw_select.setBackgroundResource(R.drawable.item_tab_off_bkg);
            fw_select.setTextColor(getResources().getColor(R.color.colorPrimary));
            service_select.setBackgroundResource(R.drawable.item_tab_on_bkg);
            service_select.setTextColor(getResources().getColor(R.color.white));
            autocomplete_item.setText("");
            autocomplete_item.setHint(LanguageController.getInstance().getMobileMsgByKey(AppConstant.services_name));
            DP_OPEN = true;
            isMandtryServices = false;
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
/**            add all taxes into the views */
            generateDynamicViews(listFilter, root);
            Button cancel_btn = dialog.findViewById(R.id.cancel_btn);
            cancel_btn.setText(LanguageController.getInstance().getMobileMsgByKey(AppConstant.cancel));
            //  Button total_tax_btn = dialog.findViewById(R.id.total_tax_btn);
            //  total_tax_btn.setText(LanguageController.getInstance().getMobileMsgByKey(AppConstant.save_btn));
            cancel_btn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dialog.dismiss();
                }
            });
//            total_tax_btn.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    float localtax = getAllTax(root);
//                    total_tax = localtax;
//                    tax_value_txt.setText(AppUtility.getRoundoff_amount(String.valueOf(localtax)));
//                    total_Amount_cal();
//                    dialog.dismiss();
//                }
//            });
            dialog.show();
        }
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
//                        listFilter.get(i).setRate(String.valueOf(updatedtax));
//                        listFilter.get(i).setTxRate(String.valueOf(updatedtax));
                    }
                } else {
                    if (listFilter != null && listFilter.size() == root.getChildCount()) {
//                        listFilter.get(i).setRate("0");
//                        listFilter.get(i).setTxRate("0");
                    }
                }
            }

        }
        return count;
    }

    private void generateDynamicViews(List<Tax> listFilter, LinearLayout root) {
//        for (Tax tax : listFilter) {
//            View itemView = LayoutInflater.from(this).inflate(R.layout.tax_adpter_layout2, null);
//            EditText auto_value = itemView.findViewById(R.id.auto_value);
//            TextView txt_label = itemView.findViewById(R.id.txt_label);
//
//            auto_value.setOnFocusChangeListener(Add_Edit_Inv_Invoice_Activity.this);
//
//            txt_label.setText(tax.getLabel());
//            auto_value.setHintTextColor(Color.BLACK);
//            root.addView(itemView);
//        }

    }

    /**
     * add new Itemdata in old item list
     */
    private List<ItemData> set_New_Itemdata(ItemData itemData) {
        List<ItemData> itemlist = new ArrayList<>();
        if (itemData != null) {
            itemlist.add(itemData);
        }

        for (ItemData itemObject : invoice_Details.getItemData()) {
            itemlist.add(itemObject);
        }
        invoice_Details.setItemData(itemlist);
        return itemlist;
    }


    private List<NewItem> setNonInventryItem(NewItem newItem) {
        List<NewItem> newItemList = new ArrayList<>();
        if (newItem != null) {
            newItemList.add(newItem);
        }

        /**send shipping item as a non invetry with itemid**/
        List<ShippingItem> shippingItemList = invoice_Details.getShippingItem();
        for (ShippingItem shippingItem : shippingItemList) {
            NewItem shipping_Item = new NewItem(shippingItem.getItemId(), shippingItem.getInm(), "", shippingItem.getRate(), "", Integer.parseInt(shippingItem.getItype()),
                    new ArrayList<Tax>(), 0, "", "", "", "", "", "", "");
            newItemList.add(shipping_Item);
        }

        return newItemList;

    }

    /**
     * update item remove from Item list
     */
    private List<ItemData> Edit_ItemData() {
        for (ItemData data : invoice_Details.getItemData()) {
            if (data.getItemId().equals(ItemData_Add_Edit.getItemId())) {
                invoice_Details.getItemData().remove(data);
                break;
            }
        }
        return invoice_Details.getItemData();
    }

    @Override
    public void setInvoiceData(Inv_Res_Model invoiceData) {
        String convert = new Gson().toJson(invoiceData);
        Intent intent = new Intent();
        intent.putExtra("AddInvoice", convert);
        setResult(RESULT_OK, intent);
        //  setResult(ADD_ITEM_DATA, intent);
        AppUtility.hideSoftKeyboard(Add_Edit_Inv_Invoice_Activity.this);
        this.finish();
    }

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int position, long l) {
        switch (adapterView.getId()) {
        }
    }

    @Override
    public void setFieldWorKerList(final List<FieldWorker> fieldWorkerList) {
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
                itype = 0;
                setFieldWorkerData(((FieldWorker) adapterView.getItemAtPosition(i)));
            }
        });
    }

    private void ShowFwList(List<FieldWorker> fieldWorkerList) {
        for (ItemData itemData : invoice_Details.getItemData()) {
            for (FieldWorker fw : fieldWorkerList) {
                if (itemData.getType() == 2 && fw.getUsrId().equals(itemData.getItemId())) {
                    fieldWorkerList.remove(fw);
                    break;
                }
            }
        }
    }


    @Override
    public void setJobtitleList(final List<JobTitle> servicesItemList) {
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
                itype = 1;
                isItemOrTitle = 3;
                setServiceData(((JobTitle) adapterView.getItemAtPosition(i)));
            }
        });

    }

    @Override
    public void updateItemServiceListner() {
        switch (isfwOrItem) {
            case 1:
                add_edit_pi.getInventryItemList();
                break;
            case 2:
                add_edit_pi.getFwList();
                break;
            case 3:
                add_edit_pi.getJobServiceTittle();
                break;
        }

    }

    /**
     * not show alredy exiting services in services  dropdown
     */
    private void ShowServicesList(List<JobTitle> servicesItemList) {
        for (ItemData itemData : invoice_Details.getItemData()) {
            for (JobTitle jobTitle : servicesItemList) {
                if (itemData.getType() == 3 && jobTitle.getJtId().equals(itemData.getJtId())) {
                    servicesItemList.remove(jobTitle);
                    break;
                }
            }
        }
    }

    @Override
    public void setItemdata(final List<Inventry_ReS_Model> list) {
        this.list = list;
        AppUtility.autocompletetextviewPopUpWindow(autocomplete_item);
        autocomplete_item.setTag("Item");
        final Auto_Inventry_Adpter itemAdapter = new Auto_Inventry_Adpter(this,
                R.layout.custom_adapter_item_layout, (ArrayList<Inventry_ReS_Model>) this.list);
        autocomplete_item.setAdapter(itemAdapter);


        autocomplete_item.setThreshold(3);

        autocomplete_item.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
              /*  try {
                    setSelectedItemData(((Inventry_ReS_Model) adapterView.getItemAtPosition(position)));
                } catch (Exception ex) {
                    ex.getMessage();
                }*/

                Inventry_ReS_Model selectedItem = itemAdapter.getSelectedItem(position, itemName);
                if (selectedItem != null) {
                    setSelectedItemData(selectedItem);
                }
                itype = 0;
            }
        });
    }


    private void setServiceData(JobTitle serViceItem) {
        isMandtryServices = true;
        // is_SerVice_Item_Inv = true;
        itype = 1;
        jtId = serViceItem.getJtId();
        itemName = serViceItem.getTitle();
        edt_item_qty.setText("1"); // quantity always be 1 initially
        edt_item_rate.setText(serViceItem.getLabour());
        edt_item_supplier.setText("0");
        edt_item_disc.setText("0");
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
//                    tax.setRate(taxData.getRate());
//                    tax.setTxRate(taxData.getRate());
//                    total_tax = Float.parseFloat(tax.getRate()) + total_tax;
                }
            }
        }
        tax_value_txt.setText(AppUtility.getRoundoff_amount(String.valueOf(total_tax)));
    }

    private void setEmptyFields() {
        itemId = "";
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
        setTaxDialogFiledsEmpty();
    }

    private void setTaxDialogFiledsEmpty() {
        for (Tax tax : listFilter) {
            //    tax.setRate("0");
            //tax.setTxRate("0");
        }
    }

    /*** add item from dropdown*/
    private void setSelectedItemData(Inventry_ReS_Model itemselected) {
        setDefaultTax(itemselected.getTax());
        itemId = itemselected.getItemId();
        type = 1;
        itemName = itemselected.getInm();

        /** quantity always be 1 initially */
        edt_item_qty.setText("1");
        edt_part_no.setText(itemselected.getPno());
        edt_hsnCode.setText(itemselected.getHsncode());
        edt_item_desc.setText(itemselected.getIdes());
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
        edt_item_disc.setText(AppUtility.getRoundoff_amount(itemselected.getDiscount()));

        total_Amount_cal();
    }


    private void setFieldWorkerData(FieldWorker fieldWorkerselected) {
        isMandtryFw = true;
        // is_Fw_Item_Inv = true;
        itemId = fieldWorkerselected.getUsrId();
        type = 2;
        itemName = fieldWorkerselected.getName();
        /** quantity always be 1 initially */
        edt_item_qty.setText("1");
        edt_item_rate.setText("0");
        edt_item_supplier.setText("0");
        edt_item_disc.setText("0");
        tax_value_txt.setText("0");
        total_Amount_cal();
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
                break;
        }
    }


    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.autocomplete_item:
                if (DP_OPEN) {
                    autocomplete_item.showDropDown();
                }
                break;
            case R.id.add_edit_item_Btn:
                checkMendartyFields();
                break;
            case R.id.item_select:
                autocomplete_item.setTag("Item");
                isfwOrItem = 1;
                if (invoice_Details == null) {
                    setEmptyFields();
                }
                add_edit_pi.getInventryItemList();
                setTxtBkgColor(1);
                fw_service_filed_hide(1);
                break;
            case R.id.fw_select:
                autocomplete_item.setTag("Fw");
                isfwOrItem = 2;
                if (invoice_Details == null) {
                    setEmptyFields();
                }
                setTxtBkgColor(2);
                add_edit_pi.getFwList();
                fw_service_filed_hide(2);
                break;
            case R.id.tax_value_txt:
                showDialogTax();
                break;
            case R.id.service_select:
                autocomplete_item.setTag("Services");
                isfwOrItem = 3;
                setTxtBkgColor(3);
                add_edit_pi.getJobServiceTittle();
                fw_service_filed_hide(3);
                break;
        }
    }

    private void checkMendartyFields() {
        if (!isMandtryItem && isfwOrItem == 1) {
            AppUtility.alertDialog(this, "", LanguageController.getInstance().getMobileMsgByKey(AppConstant.item_empty), LanguageController.getInstance().getMobileMsgByKey(AppConstant.ok),
                    "", new Callable<Boolean>() {
                        @Override
                        public Boolean call() throws Exception {
                            return null;
                        }
                    });
            return;
        } else if (!isMandtryFw && isfwOrItem == 2) {
            AppUtility.alertDialog(this, "", LanguageController.getInstance().getMobileMsgByKey(AppConstant.fw_valid), LanguageController.getInstance().getMobileMsgByKey(AppConstant.ok), "", new Callable<Boolean>() {
                @Override
                public Boolean call() throws Exception {
                    return null;
                }
            });
            return;
        } else if (!isMandtryServices && isfwOrItem == 3) {
            AppUtility.alertDialog(this, "", LanguageController.getInstance().getMobileMsgByKey(AppConstant.service_error), AppConstant.ok, "", new Callable<Boolean>() {
                @Override
                public Boolean call() throws Exception {
                    return null;
                }
            });
            return;
        } else {
            callServiceForAddItem();
        }
    }

    private void callServiceForAddItem() {


       /* List<OfflineInvoiceItem> offlineInvoiceItemList = AppDataBase.getInMemoryDatabase(EotApp.getAppinstance()).itemsDAO().getItems(invoice_Details.getJobId());
        Invoice_Update_Request_Model add_update_object = null;
        if (invoice_Details != null) {
            String dueDate = "", invDate = "";
            if (!(invoice_Details.getDuedate().equals("")) && !(invoice_Details.getInvDate().equals(""))) {
                dueDate = AppUtility.getDateWithFormate(Long.parseLong(invoice_Details.getDuedate()), "yyyy-MM-dd");
                invDate = AppUtility.getDateWithFormate(Long.parseLong(invoice_Details.getInvDate()), "yyyy-MM-dd");
            }

            *//**add  non inventry item  *//*
         *//** no add non inventry item for fw & service *//*
            if (itype != 0) {
                newItem = new NewItem(itemName //add non inventry item
                        , edt_item_qty.getText().toString().trim(), AppUtility.getRoundoff_amount(edt_item_rate.getText().toString().trim()),
                        AppUtility.getRoundoff_amount(edt_item_disc.getText().toString().trim()),
                         itype, tax, isItemOrTitle, edt_item_desc.getText().toString().trim()
                        , edt_part_no.getText().toString().trim(), edt_hsnCode.getText().toString().trim(), edt_unit.getText().toString().trim()
                        , String.valueOf(taxAmount), edt_item_supplier.getText().toString().trim(), jtId);
                newItem.setTax(listFilter);
                newItem.setAmount(AppUtility.getRoundoff_amount(String.valueOf(roundOff)));
            } else {
                itemData = new ItemData(itemId, invoice_Details.getJobId(), //add inventry item but not show in Inventry List
                        type, AppUtility.getRoundoff_amount(edt_item_rate.getText().toString().trim()),
                        edt_item_qty.getText().toString().trim(),
                        AppUtility.getRoundoff_amount(edt_item_disc.getText().toString().trim()), 0,
                         tax, edt_item_desc.getText().toString().trim()
                        , edt_part_no.getText().toString().trim(),
                        , edt_part_no.getText().toString().trim(),
                        edt_hsnCode.getText().toString().trim(), edt_unit.getText().toString().trim()
                        type, AppUtility.getRoundoff_amount(edt_item_rate.getText().toString().trim()), edt_item_qty.getText().toString().trim(),
                        AppUtility.getRoundoff_amount(edt_item_disc.getText().toString().trim()), 0,
                        tax, edt_item_desc.getText().toString().trim()
                        , edt_part_no.getText().toString().trim(), edt_hsnCode.getText().toString().trim(), edt_unit.getText().toString().trim()
                        , String.valueOf(taxAmount), edt_item_supplier.getText().toString().trim());
                itemData.setTax(listFilter);
                itemData.setAmount(AppUtility.getRoundoff_amount(String.valueOf(roundOff)));
            }

            if (ItemData_Add_Edit == null) {//add new Inventry/Non inventry item
                List<ItemData> newItemList = set_New_Itemdata(itemData);

                List<ItemData2Model> itemData2Models = new ArrayList<>();
              //  if (offlineInvoiceItemList.size() > 0) {
                  *//*  for (ItemData2Model data2Model : offlineInvoiceItemList.get(0).getItemData2List()) {
                        itemData2Models.add(
                                new ItemData2Model(data2Model.getItemId(), data2Model.getInm()
                                        , data2Model.getPartno(), data2Model.getQty(), data2Model.getRate(),
                                        data2Model.getSupplierCost(), data2Model.getTax()
                                        , data2Model.getHsncode(), data2Model.getIdesc(), data2Model.getUnit()
                                        , data2Model.getDiscount(), String.valueOf(data2Model.getType())
                                        , "", data2Model.getTaxAmount(), AppUtility.getRoundoff_amount(String.valueOf(roundOff))
                                ));
                    }*//*
                    offlineInvoiceItemList.get(0).getItemData2List().add(
                            new ItemData2Model(itemId, itemName, edt_part_no.getText().toString().trim()
                                    , edt_item_qty.getText().toString().trim(), edt_item_rate.getText().toString().trim()
                                    , edt_item_supplier.getText().toString().trim(), tax, edt_hsnCode.getText().toString().trim()
                                    , edt_item_desc.getText().toString().trim()
                                    , edt_unit.getText().toString().trim(), edt_item_disc.getText().toString().trim()
                                    , String.valueOf(type), "", amount_value_txt.getText().toString(), AppUtility.getRoundoff_amount(String.valueOf(roundOff))
                            )
                    );
                    AppDataBase.getInMemoryDatabase(EotApp.getAppinstance()).itemsDAO().insertInvoiceItem(
                            new OfflineInvoiceItem(invoice_Details.getJobId(), offlineInvoiceItemList.get(0).getItemData2List(),
                                    new ArrayList<ItemUpdateModel>()
                                    , offlineInvoiceItemList.get(0).getRemobeItem()));

                    String dateTime = AppUtility.getDateByFormat(AppConstant.DATE_TIME_FORMAT);
                    Gson gson = new Gson();
                    String addJobReqest = gson.toJson(new OfflineInvoiceItem(invoice_Details.getJobId(), offlineInvoiceItemList.get(0).getItemData2List(),
                            new ArrayList<ItemUpdateModel>(), new ArrayList<String>()));
//      add temp job in db
                    //   addtempJobInDb(addJobReq);
                  //  OfflineDataController.getInstance().addInOfflineDB(Service_apis.addJob, addJobReqest, dateTime);

                } else {
                    itemData2Models.add(
                            new ItemData2Model(itemId, itemName, edt_part_no.getText().toString().trim()
                                    , edt_item_qty.getText().toString().trim(), edt_item_rate.getText().toString().trim()
                                    , edt_item_supplier.getText().toString().trim(), tax, edt_hsnCode.getText().toString().trim()
                                    , edt_item_desc.getText().toString().trim()
                                    , edt_unit.getText().toString().trim(), edt_item_disc.getText().toString().trim()
                                    , String.valueOf(type), "", amount_value_txt.getText().toString(),
                                     AppUtility.getRoundoff_amount(String.valueOf(roundOff))
                            )
                    );

                    AppDataBase.getInMemoryDatabase(EotApp.getAppinstance()).itemsDAO().insertInvoiceItem(
                            new OfflineInvoiceItem(invoice_Details.getJobId(), itemData2Models,
                                    new ArrayList<ItemUpdateModel>(), new ArrayList<String>()));


                    String dateTime = AppUtility.getDateByFormat(AppConstant.DATE_TIME_FORMAT);
                    Gson gson = new Gson();
                    String addJobReqest = gson.toJson(new OfflineInvoiceItem(invoice_Details.getJobId(), offlineInvoiceItemList.get(0).getItemData2List(),
                            new ArrayList<ItemUpdateModel>(), new ArrayList<String>()));
//      add temp job in db
                    //   addtempJobInDb(addJobReq);
                 //   OfflineDataController.getInstance().addInOfflineDB(Service_apis.addJob, addJobReqest, dateTime);

                }


//                invoice_Details.getItemData().clear();
//                invoice_Details.setItemData(newItemList);

                add_update_object = new Invoice_Update_Request_Model(invoice_Details.getNm(),
                        invoice_Details.getPro(),
                        invoice_Details.getInvId(),
                        invoice_Details.getCompId(), ""
                        , invoice_Details.getJobId(), "", invoice_Details.getInv_client_address(),
                        //invoice_Details.getAdr(),
                        invoice_Details.getDiscount()
                        , invoice_Details.getPaid(), invoice_Details.getNote(),
                        dueDate, invDate
                        , newItemList, "",
                        invoice_Details.getInvType(), invoice_Details.getCur(), "0",
                        invoice_Details.getIsShowInList(), setNonInventryItem(newItem)
                        , invoice_Details.getShipto(), invoice_Details.getShippingItem(),
                        String.valueOf(getTotalAmount()));
            } else { // edit Inventry & Non Inventry item
                List<ItemData> itemList = Edit_ItemData();
                if (itemData != null) {
                    itemList.add(itemData);
//                    invoice_Details.getItemData().clear();
//                    invoice_Details.setItemData(itemList);
                }


                AppDataBase.getInMemoryDatabase(EotApp.getAppinstance()).itemsDAO().insertInvoiceItem(
                        new OfflineInvoiceItem(invoice_Details.getJobId(), offlineInvoiceItemList.get(0).getItemData2List(),
                                offlineInvoiceItemList.get(0).getItemUpdateModelsList()
                                , new ArrayList<String>()));


                add_update_object = new Invoice_Update_Request_Model(invoice_Details.getNm(),
                        invoice_Details.getPro(),
                        invoice_Details.getInvId(),
                        invoice_Details.getCompId(), ""
                        , invoice_Details.getJobId(), "", invoice_Details.getInv_client_address(),
                        //invoice_Details.getAdr(),
                        invoice_Details.getDiscount()
                        , invoice_Details.getPaid(), invoice_Details.getNote(),
                        dueDate, invDate
                        , itemList, "",
                        invoice_Details.getInvType(), invoice_Details.getCur(), "0", invoice_Details.getIsShowInList(), setNonInventryItem(newItem)
                        , invoice_Details.getShipto(), invoice_Details.getShippingItem(),
                        String.valueOf(getTotalAmount()));
            }
            if (!itemName.equals("")) {


              //  add_edit_pi.addInvoiceItem(add_update_object);
            } else {
                AppUtility.alertDialog(this, "", LanguageController.getInstance().getMobileMsgByKey(AppConstant.item_empty), LanguageController.getInstance().getMobileMsgByKey(AppConstant.ok),
                        "", new Callable<Boolean>() {
                            @Override
                            public Boolean call() throws Exception {
                                return null;
                            }
                        });
            }*/
    }


    private double getTotalAmount() {

//        for (ItemData itemDatas : invoice_Details.getItemData()) {
//            itemDatas.setAmount(AppUtility.getCalculatedAmount(itemData.getQty(), itemData.getRate(),
//                    itemData.getDiscount(), itemData.getTax(), invoice_Details.getTaxCalculationType()));
//        }
        double total = 0;
        for (ItemData itemData : invoice_Details.getItemData()) {
            if (itemData.getAmount() != null) {
                total = Double.parseDouble(itemData.getAmount()) + total;
            }
        }
        if (newItem != null) {
            total += Double.parseDouble(newItem.getAmount());
        }


        /**add shipping rate in total amount**/
        for (ShippingItem shippingItem : invoice_Details.getShippingItem()) {
            total += Double.parseDouble(shippingItem.getRate());
        }
        return total;
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            onBackPressed();
            AppUtility.hideSoftKeyboard(Add_Edit_Inv_Invoice_Activity.this);
            return true;
        }
        return super.onOptionsItemSelected(item);
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
            if (charSequence.hashCode() == edt_item_desc.getText().hashCode())
                item_desc_layout.setHintEnabled(true);
            if (charSequence.hashCode() == edt_item_qty.getText().hashCode())
                item_qty_layout.setHintEnabled(true);
            if (charSequence.hashCode() == edt_item_rate.getText().hashCode())
                item_rate_layout.setHintEnabled(true);
            if (charSequence.hashCode() == edt_item_supplier.getText().hashCode())
                item_supplier_layout.setHintEnabled(true);
            if (charSequence.hashCode() == edt_item_disc.getText().hashCode()) {
                item_discount_layout.setHintEnabled(true);
                /** discount must not be gratter than 100 */
                if (Float.parseFloat(edt_item_disc.getText().toString()) > 100) {
                    showDisError(LanguageController.getInstance().getMobileMsgByKey(AppConstant.discountError));
                    edt_item_disc.setText("0");
                }
            }
            if (charSequence.hashCode() == edt_part_no.getText().hashCode())
                item_partNo_layout.setHintEnabled(true);
            if (charSequence.hashCode() == edt_hsnCode.getText().hashCode())
                item_hsnCode_layout.setHintEnabled(true);
            if (charSequence.hashCode() == edt_unit.getText().hashCode())
                item_unit_layout.setHintEnabled(true);


            total_Amount_cal();
        } else if (charSequence.length() <= 0) {
            if (charSequence.hashCode() == edt_item_desc.getText().hashCode())
                item_desc_layout.setHintEnabled(false);
            if (charSequence.hashCode() == edt_item_qty.getText().hashCode())
                item_qty_layout.setHintEnabled(false);
            if (charSequence.hashCode() == edt_item_rate.getText().hashCode())
                item_rate_layout.setHintEnabled(false);
            if (charSequence.hashCode() == edt_item_supplier.getText().hashCode())
                item_supplier_layout.setHintEnabled(false);
            if (charSequence.hashCode() == edt_item_disc.getText().hashCode())
                item_discount_layout.setHintEnabled(false);
            if (charSequence.hashCode() == edt_part_no.getText().hashCode())
                item_partNo_layout.setHintEnabled(false);
            if (charSequence.hashCode() == edt_hsnCode.getText().hashCode())
                item_hsnCode_layout.setHintEnabled(false);
            if (charSequence.hashCode() == edt_unit.getText().hashCode())
                item_unit_layout.setHintEnabled(false);

            total_Amount_cal();
        }
    }

    @Override
    public void afterTextChanged(Editable editable) {

    }

    @Override
    public void errorOccured() {
        this.finish();
    }

    private void showDisError(String msg) {
        AppUtility.alertDialog(this, "", msg, LanguageController.getInstance().getMobileMsgByKey(AppConstant.ok), "", new Callable<Boolean>() {
            @Override
            public Boolean call() throws Exception {
                return true;
            }
        });
    }


    /**
     * set default (value) tax rate when add new item
     */
    @Override
    public void setTaxList(List<Tax> taxList) {
        listFilter = new ArrayList<>();
        for (Tax tax : taxList) {
//            if (tax.getIsactive().equals("1") && tax.getShowInvoice().equals("1")) {
//                if (tax.getRate() == null) {
//                   //tax.setTxRate("0");
//                    tax.setRate("0");
//                } else {
//                    tax.setTxRate(tax.getRate());
//                }
//                listFilter.add(tax);
//            }
        }
    }


    private void setDefaultTax(List<Tax> tax_default_list) {
        for (Tax tax1 : listFilter) {
            for (Tax tax2 : tax_default_list) {
                if (tax1.getTaxId().equals(tax2.getTaxId())) {
//                    tax1.setRate(tax2.getRate());
//                    tax1.setTxRate(tax2.getRate());
//                    total_tax = Float.parseFloat(tax1.getRate()) + total_tax;
                }
            }
        }
        tax_value_txt.setText(AppUtility.getRoundoff_amount(String.valueOf(total_tax)));
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
}

