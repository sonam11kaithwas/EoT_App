package com.eot_app.nav_menu.jobs.job_detail.generate_invoice;

import android.animation.Animator;
import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.eot_app.R;
import com.eot_app.nav_menu.jobs.job_detail.addinvoiveitem2pkg.AddEditInvoiceItemActivity2;
import com.eot_app.nav_menu.jobs.job_detail.addinvoiveitem2pkg.model.InvoiceItemDataModel;
import com.eot_app.nav_menu.jobs.job_detail.generate_invoice.invoice_adpter_pkg.Sipping_Adpter;
import com.eot_app.nav_menu.jobs.job_detail.invoice.invoice_db.location_tax_dao.TaxLocAdpter;
import com.eot_app.nav_menu.jobs.job_detail.invoice.invoice_db.location_tax_dao.TaxesLocation;
import com.eot_app.nav_menu.jobs.job_detail.invoice.invoice_detail_pkg.inv_detail_model.Inv_Client_Address_model;
import com.eot_app.nav_menu.jobs.job_detail.invoice.invoice_detail_pkg.inv_detail_model.ShippingItem;
import com.eot_app.nav_menu.jobs.job_detail.invoice.invoice_email_pkg.Invoice_Email_Activity;
import com.eot_app.nav_menu.jobs.job_detail.invoice2list.GenerateInvoiceItemAdpter;
import com.eot_app.nav_menu.jobs.job_detail.invoice2list.itemlist_model.InvoiceItemDetailsModel;
import com.eot_app.nav_menu.jobs.job_detail.invoice2list.itemlist_mvp.ItemList_PC;
import com.eot_app.nav_menu.jobs.job_detail.invoice2list.itemlist_mvp.ItemList_PI;
import com.eot_app.nav_menu.jobs.job_detail.invoice2list.itemlist_mvp.ItemList_View;
import com.eot_app.utility.AppConstant;
import com.eot_app.utility.AppUtility;
import com.eot_app.utility.App_preference;
import com.eot_app.utility.EotApp;
import com.eot_app.utility.db.AppDataBase;
import com.eot_app.utility.language_support.LanguageController;
import com.eot_app.utility.util_interfaces.Callback_AlertDialog;
import com.eot_app.utility.util_interfaces.MyListItemSelected;
import com.eot_app.utility.util_interfaces.MyListItemSelectedLisT;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.concurrent.Callable;

public class Generate_Invoice_Activity extends AppCompatActivity implements MyListItemSelected<InvoiceItemDataModel>,
        View.OnClickListener, MyListItemSelectedLisT<InvoiceItemDataModel>
        , ItemList_View, InvoiceItemObserver {
    private final static int ADD_ITEM_DATA = 1;
    static public String jobId, invId = "";
    Button pay_btn_inv, addInvoiceItem_btn;
    boolean islang;
    boolean isFABOpen = false;
    private String locId = "0";
    private List<InvoiceItemDataModel> itemData_Details = new ArrayList<>();
    private RecyclerView recyclerView_invoice, recyclerView_shippingitem;
    private LinearLayoutManager layoutManager;
    private GenerateInvoiceItemAdpter invoice_list_adpter;
    private Sipping_Adpter sipping_adpter;
    private TextView list_item_invoice_count, pay_txt;
    private RelativeLayout lay;
    private ImageView rm_invice_im;
    private EditText pay_edt_partial;
    private LinearLayout rm_layout;
    private TextView invoice_nm, invoice_adrs, inv_email, inv_total_amount, invoice_cre_dt, invoice_due_dt, tv_fab_email, tv_fab_print_invoice, tv_fab_add_new_item, tv_due_date, tv_create_date;
    private ArrayList<InvoiceItemDataModel> rm_DataItem = new ArrayList<>();
    private List<InvoiceItemDataModel> tempItemList = new ArrayList<>();
    private LinearLayout empty_invoice_lay;
    private InvoiceItemDetailsModel invoice_Details;
    private Inv_Client_Address_model client_address_model;
    private TextView empty_inv_txt;
    private SwipeRefreshLayout swiperefresh;
    private FloatingActionButton invoiceFab;
    private LinearLayout linearFabEmail, linearFabPrintInvoice, linearFabAddNewItem;
    private View backgroundView;
    private ActionBar actionBar;

    private ItemList_PI itemListPi;
    private int totalItemSize = 0;
    private Spinner loc_tax_dp;
    private TextView tax_loc_lable, loc_txt, remove_txt_loc;
    private List<TaxesLocation> taxList = new ArrayList<>();
    private RelativeLayout rl;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().requestFeature(Window.FEATURE_ACTION_BAR_OVERLAY);
        setContentView(R.layout.activity_generate_invoice);

        //        set observer for callback
        EotApp.getAppinstance().setInvoiceItemObserver(this);

        setTitle(LanguageController.getInstance().getMobileMsgByKey(AppConstant.title_invoice));

        actionBar = getSupportActionBar();

        getSupportActionBar().setElevation(0);  //remove shadow

        Bundle bundle = getIntent().getExtras();
        if (getIntent().hasExtra("JobId")) {
            jobId = bundle.getString("JobId");
        }
        initializelables();
        intialize_UI_Views();
    }

    @Override
    public void finishActivity() {
        this.finish();
    }

    @Override
    public void setLocationTaxsList(final List<TaxesLocation> taxList) {
        this.taxList = taxList;
        AppUtility.spinnerPopUpWindow(loc_tax_dp);
        loc_tax_dp.setAdapter(new TaxLocAdpter(this, taxList));

        loc_tax_dp.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                showLocationDialog(taxList.get(position));

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    private void showLocationDialog(final TaxesLocation taxesLocation) {
        AppUtility.alertDialog2(this, LanguageController.getInstance().getMobileMsgByKey(AppConstant.are_you_sure)
                , LanguageController.getInstance().getMobileMsgByKey(AppConstant.loc_tax_msg)

                , LanguageController.getInstance().getMobileMsgByKey(AppConstant.yes), LanguageController.getInstance().getMobileMsgByKey(AppConstant.cancel), new Callback_AlertDialog() {
                    @Override
                    public void onPossitiveCall() {
                        locId = taxesLocation.getLocId();
                        loc_txt.setText(taxesLocation.getLocation());
                        remove_txt_loc.setVisibility(View.VISIBLE);
                    }

                    @Override
                    public void onNegativeCall() {

                    }
                });

    }

    private void initializelables() {
        recyclerView_invoice = findViewById(R.id.recyclerView_invoice);
        swiperefresh = findViewById(R.id.swiperefresh);
        list_item_invoice_count = findViewById(R.id.list_item_invoice_count);
        rm_invice_im = findViewById(R.id.rm_invice_im);
        invoiceFab = findViewById(R.id.invoiceFab);
        linearFabEmail = findViewById(R.id.linearFabEmail);
        linearFabPrintInvoice = findViewById(R.id.linearFabPrintInvoice);
        linearFabAddNewItem = findViewById(R.id.linearFabAddNewItem);

        backgroundView = findViewById(R.id.backgroundView);
        lay = findViewById(R.id.lay);

        addInvoiceItem_btn = findViewById(R.id.addInvoiceItem_btn);

        invoice_nm = findViewById(R.id.invoice_nm);
        invoice_adrs = findViewById(R.id.invoice_adrs);
        inv_email = findViewById(R.id.inv_email);
        inv_total_amount = findViewById(R.id.inv_total_amount);
        invoice_cre_dt = findViewById(R.id.invoice_cre_dt);
        invoice_due_dt = findViewById(R.id.invoice_due_dt);

        empty_invoice_lay = findViewById(R.id.empty_invoice_lay);
        empty_inv_txt = findViewById(R.id.empty_inv_txt);
        empty_inv_txt.setText(LanguageController.getInstance().getMobileMsgByKey(AppConstant.item_not_found));

        tv_fab_email = findViewById(R.id.tv_fab_email);
        tv_fab_email.setText(LanguageController.getInstance().getMobileMsgByKey(AppConstant.email_invoice));
        tv_fab_add_new_item = findViewById(R.id.tv_fab_add_new_item);
        tv_fab_add_new_item.setText(LanguageController.getInstance().getMobileMsgByKey(AppConstant.add_new_item));

        tv_fab_print_invoice = findViewById(R.id.tv_fab_print_invoice);
        tv_fab_print_invoice.setText(LanguageController.getInstance().getMobileMsgByKey(AppConstant.print_invoice));

        tv_create_date = findViewById(R.id.tv_create_date);
        tv_create_date.setText(LanguageController.getInstance().getMobileMsgByKey(AppConstant.invoice_date));      //change when json file updated.

        tv_due_date = findViewById(R.id.tv_due_date);
        tv_due_date.setText(LanguageController.getInstance().getMobileMsgByKey(AppConstant.due_date));

        /**this view for shpping item**/
        recyclerView_shippingitem = findViewById(R.id.recyclerView_shippingitem);
    }

    private void intialize_UI_Views() {
/***this for invoice items**/
        layoutManager = new LinearLayoutManager(this);
        recyclerView_invoice.setLayoutManager(layoutManager);

        invoice_list_adpter = new GenerateInvoiceItemAdpter(this, new ArrayList<InvoiceItemDataModel>());//, this, this
        recyclerView_invoice.setAdapter(invoice_list_adpter);

/**this view for shipping items***/
        layoutManager = new LinearLayoutManager(this);
        recyclerView_shippingitem.setLayoutManager(layoutManager);
        List<ShippingItem> shippingItemList = new ArrayList<>();
        sipping_adpter = new Sipping_Adpter(shippingItemList);
        recyclerView_shippingitem.setAdapter(sipping_adpter);


        /**false nested scrolling when multiple recyclerview used in single view**/
        recyclerView_invoice.setNestedScrollingEnabled(false);
        recyclerView_shippingitem.setNestedScrollingEnabled(false);

        rm_invice_im.setOnClickListener(this);

        invoiceFab.setOnClickListener(this);
        linearFabEmail.setOnClickListener(this);
        linearFabPrintInvoice.setOnClickListener(this);
        backgroundView.setOnClickListener(this);
        linearFabAddNewItem.setOnClickListener(this);


        lay.setVisibility(View.VISIBLE);

        pay_txt = findViewById(R.id.pay_txt);
        pay_txt.setOnClickListener(this);


        addInvoiceItem_btn.setOnClickListener(this);


        loc_tax_dp = findViewById(R.id.loc_tax_dp);
        tax_loc_lable = findViewById(R.id.tax_loc_lable);
        loc_txt = findViewById(R.id.loc_txt);
        remove_txt_loc = findViewById(R.id.remove_txt_loc);
        loc_txt.setOnClickListener(this);
        remove_txt_loc.setOnClickListener(this);

        tax_loc_lable.setText(LanguageController.getInstance().getMobileMsgByKey(AppConstant.loca_tax_based));
        remove_txt_loc.setText(LanguageController.getInstance().getMobileMsgByKey(AppConstant.remove));
        remove_txt_loc.setVisibility(View.GONE);

        loc_txt.setText(LanguageController.getInstance().getMobileMsgByKey(AppConstant.select));


        rl = findViewById(R.id.rl);
        if (App_preference.getSharedprefInstance().getLoginRes().getCompPermission().get(0).getIsLocationEnable() != null && App_preference.getSharedprefInstance().getLoginRes().getCompPermission().get(0).getIsLocationEnable().equals("1")) {
            rl.setVisibility(View.VISIBLE);
        } else {
            rl.setVisibility(View.GONE);
        }

        swiperefresh.setEnabled(false);

        /***getInvoiceDetail call karna padegi****/
        itemListPi = new ItemList_PC(this);
        itemListPi.getloctaxexList();
        itemListPi.getinvoicedetails(jobId);


        swiperefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                //     itemListPi.getItemFromServer(jobId);
                // dismissPullTorefresh();
            }
        });

        setTxtInsideView();

//        /****Add\Edit permission allow ***/
//        if (!App_preference.getSharedprefInstance().getLoginRes().getCompPermission().get(0).getIsItemEnable().equals("1")) {
//            rm_invice_im.setOnClickListener(this);
//        }


        /***Check Remove Item Permission****/
        if (App_preference.getSharedprefInstance().getLoginRes().getIsItemDeleteEnable().equals("0")) {
            rm_invice_im.setVisibility(View.GONE);
        } else {
            rm_invice_im.setVisibility(View.VISIBLE);
        }

    }

    @Override
    public void setItemListByJob(final List<InvoiceItemDataModel> itemList) {
        this.tempItemList = itemList;
        if (rm_DataItem.size() > 0) {
            rm_DataItem.clear();
        }
        invoice_list_adpter.updateitemlist(itemList);
        setTxtInsideView();
        InvoiceNotFound(itemList.size() == 0);
        dismissPullTorefresh();
    }

    @Override
    public void setInvoiceDetails(InvoiceItemDetailsModel invResModel) {
        this.invoice_Details = invResModel;
        setItemDataList(invResModel);
    }

    private void InvoiceNotFound(boolean enable) {
        if (enable) {
            empty_invoice_lay.setVisibility(View.VISIBLE);
            recyclerView_invoice.setVisibility(View.GONE);
            empty_inv_txt.setText(LanguageController.getInstance().getMobileMsgByKey(AppConstant.no_invoice));
        } else {
            empty_invoice_lay.setVisibility(View.GONE);
            recyclerView_invoice.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void onMyListitemSeleted(InvoiceItemDataModel itemData) {
        editInvoiceItem(itemData);
    }


    @Override
    public void onMyListitem_Item_Seleted(ArrayList<InvoiceItemDataModel> itemDataRemove) {
        rm_DataItem = itemDataRemove;
        if (rm_DataItem.size() > 0) {
            rm_invice_im.setEnabled(true);
            rm_invice_im.setColorFilter(ContextCompat.getColor(this, R.color.colorPrimary));
        } else {
            rm_invice_im.setEnabled(false);
            rm_invice_im.setColorFilter(ContextCompat.getColor(this, R.color.txt_color));
        }
    }

    private void showLocationData(String locId) {
        TaxesLocation taxesLocation = AppDataBase.getInMemoryDatabase(EotApp.getAppinstance()).taxesLocationDao().getTaxLocationByid(locId);
        loc_txt.setText(taxesLocation.getLocation());
        remove_txt_loc.setVisibility(View.VISIBLE);
    }

    @Override
    public void setItemDataList(InvoiceItemDetailsModel invResModel) {
        this.invoice_Details = invResModel;
        this.itemData_Details = invResModel.getItemData();

        try {
            if (invResModel != null && invResModel.getLocId() != null) {
                locId = invResModel.getLocId();
                if (locId != null && !locId.equals("0")) {
                    showLocationData(locId);
                } else if (App_preference.getSharedprefInstance().getLoginRes().getLocId() != null && !App_preference.getSharedprefInstance().getLoginRes().getLocId().equals("0")) {
                    locId = App_preference.getSharedprefInstance().getLoginRes().getLocId();
                    showLocationData(locId);
                }
            }
        } catch (Exception exception) {
            exception.getMessage();
        }
        /**** *Sort Item By name***/
        Collections.sort(invResModel.getItemData(), new Comparator<InvoiceItemDataModel>() {
            @Override
            public int compare(InvoiceItemDataModel o1, InvoiceItemDataModel o2) {
                return o1.getInm().compareTo(o2.getInm());
            }
        });

        if (rm_DataItem.size() > 0) {
            rm_DataItem.clear();
        }
        setInvoiceitemDetails(this.invoice_Details);
        sipping_adpter.updateShpiningItem(invoice_Details.getShippingItem(), App_preference.getSharedprefInstance().getLoginRes().getTaxCalculationType());
        rm_invice_im.setColorFilter(getResources().getColor(R.color.txt_color));
        rm_invice_im.setEnabled(false);
        setTxtInsideView();
        if (this.itemData_Details.size() == 0) {
            InvoiceNotFound("empty invoice");
        } else {
            setItemListByJob(invResModel.getItemData());
        }

    }


    @Override
    public void dismissPullTorefresh() {
        if (swiperefresh.isRefreshing()) {
            swiperefresh.setRefreshing(false);
        }
    }

    @Override
    public void InvoiceNotFound(String msg) {
        empty_invoice_lay.setVisibility(View.VISIBLE);
        empty_inv_txt.setText(LanguageController.getInstance().getMobileMsgByKey(AppConstant.item_not_found));
        show_Dialog(msg);
    }


    @Override
    public void errorActivityFinish(String msg) {
        AppUtility.alertDialog2(this, "", msg,
                LanguageController.getInstance().getMobileMsgByKey(AppConstant.ok)
                , "", new Callback_AlertDialog() {
                    @Override
                    public void onPossitiveCall() {
                        finishActivity();
                    }

                    @Override
                    public void onNegativeCall() {

                    }
                });
    }

    @Override
    public void onGetPdfPath(String pdfPath) {
        String path = App_preference.getSharedprefInstance().getBaseURL() + pdfPath;
        Intent openAnyType = new Intent(Intent.ACTION_VIEW);
        openAnyType.setData(Uri.parse(path));
        try {
            startActivity(openAnyType);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void setInvoiceitemDetails(InvoiceItemDetailsModel invResModel) {
        if (invResModel != null && invResModel.getInvId() != null) {
            invId = invResModel.getInvId();
        }

        client_address_model = new Gson().fromJson(invResModel.getInvClientAddress(), Inv_Client_Address_model.class);
        if (client_address_model != null) {
            invoice_nm.setText(client_address_model.getNm());
            String clientAddress = "";
            if (client_address_model.getAdr() != null && !client_address_model.getAdr().equals(""))
                clientAddress = client_address_model.getAdr() + "\n";
            if (client_address_model.getCity() != null && !client_address_model.getCity().equals(""))
                clientAddress = clientAddress + client_address_model.getCity() + "\n";
            if (client_address_model.getCountry() != null && !client_address_model.getCountry().equals(""))
                clientAddress = clientAddress + client_address_model.getCountry() + "\n";
            if (client_address_model.getMob() != null && !client_address_model.getMob().equals(""))
                clientAddress = clientAddress + client_address_model.getMob() + "\n";
            if (client_address_model.getGst() != null && client_address_model.getGst() != null && !client_address_model.getGst().isEmpty())
                clientAddress = clientAddress + client_address_model.getGst();

            invoice_adrs.setText(clientAddress);
        }

        inv_total_amount.setText(AppUtility.getRoundoff_amount(invResModel.getTotal() + ""));

        if (!invResModel.getInvDate().equals(""))
            invoice_cre_dt.setText(AppUtility.getDateWithFormate(Long.parseLong(invResModel.getInvDate()), "dd-MMM-yyyy"));
        if (!invResModel.getDuedate().equals(""))
            invoice_due_dt.setText(AppUtility.getDateWithFormate(Long.parseLong(invResModel.getDuedate()), "dd-MMM-yyyy"));
    }

    private String getSpiningTotal(String total) {
        double total_amount = Double.parseDouble(total);
        for (ShippingItem sppingitem : invoice_Details.getShippingItem()) {
            total_amount = total_amount + Double.parseDouble(AppUtility.getRoundoff_amount(sppingitem.getRate()));
        }
        return String.valueOf(total_amount);
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

    private void show_Dialog(String msg) {
        AppUtility.error_Alert_Dialog(this, msg, LanguageController.getInstance().getMobileMsgByKey(AppConstant.ok)
                , new Callable<Boolean>() {
                    @Override
                    public Boolean call() throws Exception {
                        return null;
                    }
                });
    }

    private void removeApplyTaxtion() {
        locId = "0";
        loc_txt.setText(LanguageController.getInstance().getMobileMsgByKey(AppConstant.select));
        remove_txt_loc.setVisibility(View.GONE);

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.remove_txt_loc:
                removeApplyTaxtion();
                break;
            case R.id.loc_txt:
                if (taxList != null && taxList.size() > 0)
                    loc_tax_dp.performClick();
                else
                    show_Dialog(LanguageController.getInstance().getMobileMsgByKey(AppConstant.taxLoc_not_found));
                break;
            case R.id.pay_txt:
                pay_dialog();
                break;

            case R.id.rm_invice_im:
                if (rm_DataItem.size() > 0) {
                    removeSelectedItem();
                } else {
                    show_Dialog(LanguageController.getInstance().getMobileMsgByKey(AppConstant.remove_item_mandtry));
                }
                break;
            case R.id.invoiceFab:
                if (!isFABOpen) {
                    showFABMenu();
                } else {
                    closeFABMenu();
                }
                break;
            case R.id.linearFabEmail:
                if (invoice_Details != null) {
                    Intent emailIntent = new Intent(Generate_Invoice_Activity.this, Invoice_Email_Activity.class);
                    emailIntent.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                    emailIntent.putExtra("invId", invoice_Details.getInvId());
                    emailIntent.putExtra("compId", invoice_Details.getCompId());
                    emailIntent.putExtra("isShowInList", invoice_Details.getIsShowInList());
                    startActivity(emailIntent);
                }
                closeFABMenu();
                break;
            case R.id.linearFabPrintInvoice: {
                linearFabPrintInvoice.setClickable(false);
                if (invoice_Details != null) {
                    String isProformaInv = "0";
                    if (invoice_Details.getIsShowInList() != null && invoice_Details.getIsShowInList().equals("0"))
                        isProformaInv = "1";
                    else isProformaInv = "0";
                    itemListPi.getGenerateInvoicePdf(invoice_Details.getInvId(), isProformaInv);
                }
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        linearFabPrintInvoice.setClickable(true);
                    }
                }, 500);

                closeFABMenu();
            }
            break;
            case R.id.backgroundView:
                closeFABMenu();
                break;
            case R.id.linearFabAddNewItem:
                addInvoiceItem();
                closeFABMenu();
                break;
        }
    }

    private void addInvoiceItem() {
        Intent intent = new Intent(this, AddEditInvoiceItemActivity2.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
        intent.putExtra("invId", invId);
        intent.putExtra("jobId", jobId);
        intent.putExtra("locId", locId);
        intent.putExtra("addItemOnInvoice", true);
        intent.putExtra("NONBILLABLE", true);
        startActivityForResult(intent, ADD_ITEM_DATA);
    }

    private void pay_dialog() {
        final Dialog dialog = new Dialog(this);
        dialog.setContentView(R.layout.pay_dialog_invoice);
        pay_btn_inv = dialog.findViewById(R.id.pay_btn_inv);
        pay_edt_partial = dialog.findViewById(R.id.pay_edt_partial);

        final TextView full_amount = dialog.findViewById(R.id.full_amount);
        final TextView partial_amount = dialog.findViewById(R.id.partial_amount);


        pay_btn_inv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (islang) {
                    EotApp.getAppinstance().setLocalLanguage("hi");
                } else {
                    EotApp.getAppinstance().setLocalLanguage("en");
                }
                islang = !islang;
                full_amount.setText(EotApp.getAppinstance().getString("pay_full_amount"));
                partial_amount.setText(EotApp.getAppinstance().getString("pay_partial_amount"));
                dialog.dismiss();
            }
        });
        dialog.show();
    }

    private void setTxtInsideView() {
        int count = invoice_list_adpter.getItemCount();
        totalItemSize = count;
        list_item_invoice_count.setText(LanguageController.getInstance().getMobileMsgByKey(AppConstant.list_item) + " (" + count + ")");

    }

    private void editInvoiceItem(InvoiceItemDataModel invoiceItemDataModel) {
        Intent intent = new Intent(this, AddEditInvoiceItemActivity2.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
        intent.putExtra("InvoiceItemDataModel", invoiceItemDataModel);
        intent.putExtra("invId", invId);
        intent.putExtra("edit_jobId", jobId);
        intent.putExtra("locId", locId);
        intent.putExtra("addItemOnInvoice", true);
        intent.putExtra("NONBILLABLE", true);
        startActivityForResult(intent, ADD_ITEM_DATA);
    }

    private void removeSelectedItem() {

        AppUtility.alertDialog2(this, "", LanguageController.getInstance().getMobileMsgByKey(AppConstant.invoice_remove),
                LanguageController.getInstance().getMobileMsgByKey(AppConstant.ok),
                LanguageController.getInstance().getMobileMsgByKey(AppConstant.cancel), new Callback_AlertDialog() {
                    @Override
                    public void onPossitiveCall() {
                        try {
                            List<InvoiceItemDataModel> notSyncItemList = new ArrayList<>();

                            ArrayList<String> ijmmIdList = new ArrayList<>();

                            if (totalItemSize > rm_DataItem.size()) {
                                for (InvoiceItemDataModel str : rm_DataItem) {
                                    if (str.getIjmmId().equals("")) {
                                        tempItemList.remove(str);
                                        notSyncItemList.add(str);
                                    } else {
                                        tempItemList.remove(str);
                                        ijmmIdList.add(str.getIjmmId());
                                    }
                                }
                                rm_invice_im.setEnabled(false);
                                itemListPi.updareRmitemsInDB(jobId, tempItemList, ijmmIdList, notSyncItemList, true);
                            } else {
                                show_Dialog(
                                        LanguageController.getInstance().getMobileMsgByKey(AppConstant.remove_item_mandtry)
                                );
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


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if (data == null) {
            return;
        } else {
            switch (requestCode) {
                case ADD_ITEM_DATA:
                    if (data.hasExtra("AddInvoiceItem")) {
                        itemListPi.getinvoicedetails(jobId);
                        break;
                    }
            }
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            onBackPressed();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void showFABMenu() {
        isFABOpen = true;
        actionBar.setBackgroundDrawable(new ColorDrawable(getResources().getColor(R.color.bg_color)));
        actionBar.setStackedBackgroundDrawable(new ColorDrawable(Color.parseColor("#90ffffff")));
        backgroundView.setVisibility(View.VISIBLE);
        linearFabEmail.setVisibility(View.VISIBLE);
        linearFabPrintInvoice.setVisibility(View.VISIBLE);
        if (App_preference.getSharedprefInstance().getLoginRes().getCompPermission().get(0).getIsItemEnable().equals("1")) {
            linearFabAddNewItem.setVisibility(View.GONE);
            linearFabEmail.animate().translationY(getResources().getDimension(R.dimen.standard_100));
            linearFabPrintInvoice.animate().translationY(getResources().getDimension(R.dimen.standard_55));
        } else {
            linearFabAddNewItem.setVisibility(View.VISIBLE);
            linearFabEmail.animate().translationY(getResources().getDimension(R.dimen.standard_145));
            linearFabPrintInvoice.animate().translationY(getResources().getDimension(R.dimen.standard_100));
            linearFabAddNewItem.animate().translationY(getResources().getDimension(R.dimen.standard_55));
        }

    }

    private void closeFABMenu() {
        isFABOpen = false;
        linearFabEmail.animate().translationY(0);
        linearFabAddNewItem.animate().translationY(0);

        linearFabPrintInvoice.animate().translationY(0).setListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animator) {

            }

            @Override
            public void onAnimationEnd(Animator animator) {
                if (!isFABOpen) {
                    backgroundView.setVisibility(View.GONE);
                    linearFabEmail.setVisibility(View.GONE);
                    linearFabPrintInvoice.setVisibility(View.GONE);
                    linearFabAddNewItem.setVisibility(View.GONE);
                    actionBar.setBackgroundDrawable(new ColorDrawable(getResources().getColor(R.color.colorPrimary)));
                }
            }

            @Override
            public void onAnimationCancel(Animator animator) {

            }

            @Override
            public void onAnimationRepeat(Animator animator) {

            }
        });

    }

    @Override
    public void onObserveCallBack(String totalAmount) {
        if (itemListPi != null) {
            inv_total_amount.setText(AppUtility.getRoundoff_amount(totalAmount + ""));
        }
    }

}






