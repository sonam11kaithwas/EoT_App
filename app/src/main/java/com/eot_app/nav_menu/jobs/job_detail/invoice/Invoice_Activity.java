package com.eot_app.nav_menu.jobs.job_detail.invoice;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.eot_app.R;
import com.eot_app.nav_menu.jobs.job_detail.invoice.add_edit_invoice_pkg.Add_Edit_Inv_Invoice_Activity;
import com.eot_app.nav_menu.jobs.job_detail.invoice.invoice_detail_pkg.Inv_detail_mvp_pkg.Inv_Details_Pc;
import com.eot_app.nav_menu.jobs.job_detail.invoice.invoice_detail_pkg.Inv_detail_mvp_pkg.Inv_Details_Pi;
import com.eot_app.nav_menu.jobs.job_detail.invoice.invoice_detail_pkg.Inv_detail_mvp_pkg.Inv_Details_View;
import com.eot_app.nav_menu.jobs.job_detail.invoice.invoice_detail_pkg.inv_detail_model.Inv_Res_Model;
import com.eot_app.nav_menu.jobs.job_detail.invoice.invoice_detail_pkg.inv_detail_model.ItemData;
import com.eot_app.nav_menu.jobs.job_detail.invoice.invoice_detail_pkg.inv_detail_model.Item_Details;
import com.eot_app.nav_menu.jobs.job_detail.invoice.invoice_detail_pkg.inv_detail_model.ShippingItem;
import com.eot_app.nav_menu.jobs.job_detail.invoice.invoice_detail_pkg.inv_detail_model.Tax;
import com.eot_app.nav_menu.jobs.job_detail.invoice.invoice_model_pkg.Invoice_Update_Request_Model;
import com.eot_app.nav_menu.jobs.job_detail.invoice.invoice_model_pkg.NewItem;
import com.eot_app.utility.AppConstant;
import com.eot_app.utility.AppUtility;
import com.eot_app.utility.App_preference;
import com.eot_app.utility.EotApp;
import com.eot_app.utility.language_support.LanguageController;
import com.eot_app.utility.util_interfaces.Callback_AlertDialog;
import com.eot_app.utility.util_interfaces.MyListItemSelected;
import com.eot_app.utility.util_interfaces.MyListItemSelectedLisT;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;

public class Invoice_Activity extends AppCompatActivity implements MyListItemSelected<ItemData>, View.OnClickListener, Inv_Details_View, MyListItemSelectedLisT<ItemData> {
    public final static int ADD_ITEM_DATA = 1;
    boolean islang;
    private Button pay_btn_inv, addInvoiceItem_btn;
    private String jobId;
    private List<ItemData> itemData_Details = new ArrayList<>();
    private RecyclerView recyclerView_invoice;
    private LinearLayoutManager layoutManager;
    private Invoice_list_Adpter invoice_list_adpter;
    private TextView list_item_invoice_count, pay_txt;
    private RelativeLayout lay;
    private ImageView rm_invice_im;
    private Inv_Details_Pi inv_details_pi;
    private TextView invoice_nm, invoice_adrs, in_country, inv_email, inv_total_amount, invoice_cre_dt, invoice_due_dt;
    private ArrayList<ItemData> rm_DataItem = new ArrayList<>();
    private LinearLayout empty_invoice_lay;
    private Inv_Res_Model invoice_Details;
    private Item_Details item_details;
    private TextView empty_inv_txt;
    private SwipeRefreshLayout swiperefresh;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().requestFeature(Window.FEATURE_ACTION_BAR_OVERLAY);
        setContentView(R.layout.activity_invoice);
        setTitle(LanguageController.getInstance().getMobileMsgByKey(AppConstant.items_screen_title));
        getSupportActionBar().setElevation(0);  //remove actionbar/title shadow
        Bundle bundle = getIntent().getExtras();
        if (getIntent().hasExtra("JobId")) {
            jobId = bundle.getString("JobId");
        }
        initializelables();
        intialize_UI_Views();
    }

    private void initializelables() {
        recyclerView_invoice = findViewById(R.id.recyclerView_invoice);
        swiperefresh = findViewById(R.id.swiperefresh);
        list_item_invoice_count = findViewById(R.id.list_item_invoice_count);
        rm_invice_im = findViewById(R.id.rm_invice_im);

        lay = findViewById(R.id.lay);
        pay_txt = findViewById(R.id.pay_txt);

        invoice_nm = findViewById(R.id.invoice_nm);
        invoice_adrs = findViewById(R.id.invoice_adrs);
        in_country = findViewById(R.id.in_country);
        inv_email = findViewById(R.id.inv_email);
        inv_total_amount = findViewById(R.id.inv_total_amount);
        invoice_cre_dt = findViewById(R.id.invoice_cre_dt);
        invoice_due_dt = findViewById(R.id.invoice_due_dt);
        empty_inv_txt = findViewById(R.id.empty_inv_txt);
        empty_inv_txt.setText(LanguageController.getInstance().getMobileMsgByKey(AppConstant.no_invoice));

        addInvoiceItem_btn = findViewById(R.id.addInvoiceItem_btn);
        addInvoiceItem_btn.setText(LanguageController.getInstance().getMobileMsgByKey(AppConstant.add_new_item));
    }

    private void intialize_UI_Views() {

        layoutManager = new LinearLayoutManager(this);
        recyclerView_invoice.setLayoutManager(layoutManager);

        invoice_list_adpter = new Invoice_list_Adpter(this, itemData_Details);//, this, this
        recyclerView_invoice.setAdapter(invoice_list_adpter);

        rm_invice_im.setOnClickListener(this);

        lay.setVisibility(View.VISIBLE);


        pay_txt.setOnClickListener(this);

        addInvoiceItem_btn.setOnClickListener(this);

        inv_details_pi = new Inv_Details_Pc(this);
        inv_details_pi.getinvoicedetails(jobId);

        empty_invoice_lay = findViewById(R.id.empty_invoice_lay);
        swiperefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                inv_details_pi.getinvoicedetails(jobId);
            }
        });

        //item add button hide when admin not authorized
        if (App_preference.getSharedprefInstance().getLoginRes().getCompPermission().get(0).getIsItemEnable().equals("1")) {
            addInvoiceItem_btn.setVisibility(View.GONE);
        }
        setTxtInsideView();
    }


    @Override
    public void onMyListitemSeleted(ItemData itemData) {
        add_Edit_Invoice(itemData);
    }


    @Override
    public void onMyListitem_Item_Seleted(ArrayList<ItemData> itemDataRemove) {
        rm_DataItem = itemDataRemove;
        if (rm_DataItem.size() > 0) {
            /** item remove button enable */
            rm_invice_im.setEnabled(true);
            rm_invice_im.setColorFilter(ContextCompat.getColor(this, R.color.colorPrimary));
        } else {
            /** item remove button disable */
            rm_invice_im.setEnabled(false);
            rm_invice_im.setColorFilter(ContextCompat.getColor(this, R.color.txt_color));
        }
    }

    @Override
    public void setItemDataList(Inv_Res_Model invResModel) {
        for (ItemData itemData : invResModel.getItemData()) {
            itemData.setAmount(AppUtility.getCalculatedAmount(itemData.getQty(), itemData.getRate(), itemData.getDiscount(),
                    itemData.getTax(), invResModel.getTaxCalculationType()));
        }
        this.invoice_Details = invResModel;
        this.itemData_Details = invResModel.getItemData();

        if (rm_DataItem.size() > 0) {
            rm_DataItem.clear();
        }
        setInvoiceitemDetails(this.invoice_Details);
        invoice_list_adpter.updateitemlist(itemData_Details, invoice_Details.getTaxCalculationType());
        rm_invice_im.setColorFilter(getResources().getColor(R.color.txt_color));
        rm_invice_im.setEnabled(false);
        setTxtInsideView();
        if (this.itemData_Details.size() == 0) {
            InvoiceNotFound("empty invoice");
        }
    }

    @Override
    public void setInvoiceDetails(Inv_Res_Model inv_res_model) {
        for (ItemData itemData : inv_res_model.getItemData()) {
            itemData.setAmount(AppUtility.getCalculatedAmount(itemData.getQty(), itemData.getRate(), itemData.getDiscount(), itemData.getTax(), inv_res_model.getTaxCalculationType()));
        }
        this.invoice_Details = inv_res_model;
        setItemDataList(inv_res_model);
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
        empty_inv_txt.setText(LanguageController.getInstance().getMobileMsgByKey(AppConstant.no_invoice));
    }

    private void setInvoiceitemDetails(Inv_Res_Model invResModel) {
        item_details = new Gson().fromJson(invResModel.getPro(), Item_Details.class);
        if (item_details != null) {
            invoice_nm.setText(item_details.getCmpnm());
            invoice_adrs.setText(item_details.getAdr() + " " + item_details.getCity());
            in_country.setText(item_details.getCountry());
            inv_email.setText(item_details.getCmpemail());
            inv_total_amount.setText(invResModel.getTotal());
        }
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
    public void onGetPdfPath(String pdfPath) {

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.pay_txt:
                pay_dialog();
                break;
            case R.id.addInvoiceItem_btn:
                addInvoiceItem();
                break;
            case R.id.rm_invice_im:
                if (invoice_Details != null) {
                    if (invoice_Details.getItemData().size() > rm_DataItem.size()) {
                        removeSelectedItem();
                    } else {
                        show_Dialog(
                                LanguageController.getInstance().getMobileMsgByKey(AppConstant.remove_item_mandtry)
                        );
                    }
                }
                break;
        }
    }


    private void addInvoiceItem() {
        if (AppUtility.isInternetConnected()) {
            if (invoice_Details != null) {
                String object = new Gson().toJson(invoice_Details);
                Intent intent = new Intent(this, Add_Edit_Inv_Invoice_Activity.class);
                intent.putExtra("AddInvoice", object);
                startActivityForResult(intent, ADD_ITEM_DATA);
            } else {
                show_Dialog(LanguageController.getInstance().getMobileMsgByKey(AppConstant.invoice_details_not_found));
            }
        } else {
            AppUtility.alertDialog(this, LanguageController.getInstance().
                    getMobileMsgByKey(AppConstant.dialog_alert), LanguageController.getInstance().
                    getMobileMsgByKey(AppConstant.err_check_network), LanguageController.getInstance().
                    getMobileMsgByKey(AppConstant.ok), "", new Callable<Boolean>() {
                @Override
                public Boolean call() throws Exception {
                    return null;
                }
            });
        }

    }

    private void pay_dialog() {
        final Dialog dialog = new Dialog(this);
        dialog.setContentView(R.layout.pay_dialog_invoice);
        pay_btn_inv = dialog.findViewById(R.id.pay_btn_inv);

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
        list_item_invoice_count.setText(LanguageController.getInstance().getMobileMsgByKey(AppConstant.list_item) + " (" + count + ")");
    }


    private void add_Edit_Invoice(ItemData edit_Invoice_Item) {
        String object = new Gson().toJson(invoice_Details);
        Intent intent = new Intent(this, Add_Edit_Inv_Invoice_Activity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
        intent.putExtra("EditInvoice", object);
        intent.putExtra("Itemdata", new Gson().toJson(edit_Invoice_Item));
        startActivityForResult(intent, ADD_ITEM_DATA);
    }


    /**
     * remove invoice item from list
     */
    private void removeSelectedItem() {
        AppUtility.alertDialog2(this, "", LanguageController.getInstance().getMobileMsgByKey(AppConstant.invoice_remove), LanguageController.getInstance().getMobileMsgByKey(AppConstant.ok), LanguageController.getInstance().getMobileMsgByKey(AppConstant.cancel), new Callback_AlertDialog() {
            @Override
            public void onPossitiveCall() {
                rm_invice_im.setEnabled(false);
                for (ItemData itemData : rm_DataItem) {
                    itemData_Details.remove(itemData);
                }
                invoice_Details.setItemData(itemData_Details);

                String dueDate = "", invDate = "";
                if (!(invoice_Details.getDuedate().equals("")) && !(invoice_Details.getInvDate().equals(""))) {
                    dueDate = AppUtility.getDateWithFormate(Long.parseLong(invoice_Details.getDuedate()), "yyyy-MM-dd");
                    invDate = AppUtility.getDateWithFormate(Long.parseLong(invoice_Details.getInvDate()), "yyyy-MM-dd");
                }


                final Invoice_Update_Request_Model obj = new Invoice_Update_Request_Model(invoice_Details.getNm(), invoice_Details.getPro(),
                        invoice_Details.getInvId(),
                        invoice_Details.getCompId(), ""
                        , invoice_Details.getJobId(), "", invoice_Details.getInv_client_address(),
                        invoice_Details.getDiscount()
                        , invoice_Details.getPaid(), invoice_Details.getNote(),
                        dueDate, invDate
                        , invoice_Details.getItemData(), invoice_Details.getPono(),
                        invoice_Details.getInvType(), invoice_Details.getCur(), "0", invoice_Details.getIsShowInList(), getShppingToNewItem()
                        , invoice_Details.getShipto(), invoice_Details.getShippingItem(),
                        String.valueOf(getTotalAmount()));

                if (invoice_Details.getItemData().size() > 0) {
                    inv_details_pi.rmInvooiceItemApiCall(obj);

                } else {
                    //    Toast.makeText(Invoice_Activity.this, AppConstant.remove_item_mandtry, Toast.LENGTH_SHORT).show();
                    show_Dialog(
                            LanguageController.getInstance().getMobileMsgByKey(AppConstant.remove_item_mandtry)
                    );
                }
            }

            @Override
            public void onNegativeCall() {

            }
        });
    }

    /**
     * convert shpping to newitem
     **/
    private List<NewItem> getShppingToNewItem() {
        List<NewItem> newItemList = new ArrayList<>();
        for (ShippingItem shippingItem : invoice_Details.getShippingItem()) {
            NewItem shipping_Item = new NewItem(shippingItem.getItemId(), shippingItem.getInm(), "", shippingItem.getRate(), "", Integer.parseInt(shippingItem.getItype()), new ArrayList<Tax>(), 0, "", "", "", "", "", "", "");
            newItemList.add(shipping_Item);
        }
        return newItemList;
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

    private double getTotalAmount() {
        double total = 0;
        for (ItemData itemData : invoice_Details.getItemData()) {
            total = Double.parseDouble(itemData.getAmount()) + total;
        }
        /**add shipping rate in total amount**/
        for (ShippingItem shippingItem : invoice_Details.getShippingItem()) {
            total += Double.parseDouble(shippingItem.getRate());
        }

        return total;
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (data == null) {
            return;
        }
        if (requestCode == ADD_ITEM_DATA && resultCode == RESULT_OK) {
            empty_invoice_lay.setVisibility(View.GONE);
            String stringdata = data.getExtras().get("AddInvoice").toString();
            Inv_Res_Model inv_res_model = new Gson().fromJson(stringdata, Inv_Res_Model.class);
            setInvoiceDetails(inv_res_model);
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
}






