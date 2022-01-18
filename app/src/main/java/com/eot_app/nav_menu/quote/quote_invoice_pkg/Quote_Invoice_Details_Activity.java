package com.eot_app.nav_menu.quote.quote_invoice_pkg;

import android.animation.Animator;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.eot_app.R;
import com.eot_app.nav_menu.client.clientlist.client_detail.site.sitelist.editsite.editsitedb.SpinnerCountrySite;
import com.eot_app.nav_menu.jobs.job_detail.generate_invoice.invoice_adpter_pkg.Sipping_Adpter;
import com.eot_app.nav_menu.jobs.job_detail.invoice.invoice_detail_pkg.inv_detail_model.ShippingItem;
import com.eot_app.nav_menu.jobs.job_detail.invoice.invoice_email_pkg.Invoice_Email_Activity;
import com.eot_app.nav_menu.quote.add_quotes_pkg.AddQuotes_Activity;
import com.eot_app.nav_menu.quote.quote_invoice_pkg.quote_model_pkg.Quote_ItemData;
import com.eot_app.nav_menu.quote.quote_invoice_pkg.quote_model_pkg.Quote_invoice_Details_Res;
import com.eot_app.nav_menu.quote.quote_invoice_pkg.quote_model_pkg.QuotesDetails;
import com.eot_app.nav_menu.quote.quote_invoice_pkg.quote_mvp_pkg.Quo_Inv_Pc;
import com.eot_app.nav_menu.quote.quote_invoice_pkg.quote_mvp_pkg.Quo_Invo_Pi;
import com.eot_app.nav_menu.quote.quote_invoice_pkg.quote_mvp_pkg.Quo_Invo_View;
import com.eot_app.nav_menu.quote.quotes_add_item_pkg.AddQutesItem_Activity;
import com.eot_app.utility.AppConstant;
import com.eot_app.utility.AppUtility;
import com.eot_app.utility.App_preference;
import com.eot_app.utility.EotApp;
import com.eot_app.utility.language_support.LanguageController;
import com.eot_app.utility.util_interfaces.Callback_AlertDialog;
import com.eot_app.utility.util_interfaces.MyListItemSelected;
import com.eot_app.utility.util_interfaces.MyListItemSelectedLisT;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;

public class Quote_Invoice_Details_Activity extends AppCompatActivity implements Quo_Invo_View, View.OnClickListener, MyListItemSelected<Quote_ItemData>, MyListItemSelectedLisT<String> {

    private final static int ADD_ITEM_DATA = 1;
    static public String quotId;
    boolean isFABOpen = false;
    private RecyclerView recyclerView_quote, recyclerView_shippingitem;
    private LinearLayoutManager layoutManager;
    private TextView list_item_quote_count, pay_txt;
    private RelativeLayout lay;
    private ImageView rm_quote_im;
    private TextView quote_nm, invoice_adrs, in_country, inv_email, quote_total_amount, quote_cre_dt, quote_due_dt, tv_due_date, tv_create_date;
    private ArrayList<String> rm_DataItem = new ArrayList<>();
    private SwipeRefreshLayout swiperefresh;
    private FloatingActionButton invoiceFab;
    private LinearLayout linearFabEmail, linearFabPrintInvoice, linearFabAddNewItem, linearFabQuotesInJob;
    private TextView tv_fab_QuotesToJob, tv_fab_print_invoice, tv_fab_add_new_item, tv_fab_email;
    private ActionBar actionBar;
    private View backgroundView;
    private Quote_Details_Adpter quotes_item_Adpter;
    private Quo_Invo_Pi quo_invo_pi;
    private Quote_invoice_Details_Res quotes_Details_Inv;
    private QuotesDetails quotesDetails;
    private Sipping_Adpter sipping_adpter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().requestFeature(Window.FEATURE_ACTION_BAR_OVERLAY);
        setContentView(R.layout.activity_quote__invoice);
        setTitle(LanguageController.getInstance().getMobileMsgByKey(AppConstant.quote_detail));

        actionBar = getSupportActionBar();
        getSupportActionBar().setElevation(0);

        Bundle bundle = getIntent().getExtras();
        if (getIntent().hasExtra("quotId")) {
            quotId = bundle.getString("quotId");
        }
        initializelables();
        intialize_UI_Views();
    }


    private void initializelables() {
        recyclerView_quote = findViewById(R.id.recyclerView_quote);
        swiperefresh = findViewById(R.id.swiperefresh);
        list_item_quote_count = findViewById(R.id.list_item_quote_count);
        rm_quote_im = findViewById(R.id.rm_quote_im);
        invoiceFab = findViewById(R.id.invoiceFab);

        linearFabEmail = findViewById(R.id.linearFabEmail);
        linearFabPrintInvoice = findViewById(R.id.linearFabPrintInvoice);
        linearFabAddNewItem = findViewById(R.id.linearFabAddNewItem);
        linearFabQuotesInJob = findViewById(R.id.linearFabQuotesInJob);


        lay = findViewById(R.id.lay);

        quote_nm = findViewById(R.id.quote_nm);
        invoice_adrs = findViewById(R.id.invoice_adrs);
        in_country = findViewById(R.id.in_country);
        inv_email = findViewById(R.id.inv_email);
        quote_total_amount = findViewById(R.id.quote_total_amount);
        quote_cre_dt = findViewById(R.id.quote_cre_dt);
        quote_due_dt = findViewById(R.id.quote_due_dt);

        tv_fab_email = findViewById(R.id.tv_fab_email);
        tv_fab_email.setText(LanguageController.getInstance().getMobileMsgByKey(AppConstant.email_quote));
        tv_fab_add_new_item = findViewById(R.id.tv_fab_add_new_item);
        tv_fab_add_new_item.setText(LanguageController.getInstance().getMobileMsgByKey(AppConstant.add_new_item));
        tv_fab_print_invoice = findViewById(R.id.tv_fab_print_invoice);
        tv_fab_print_invoice.setText(LanguageController.getInstance().getMobileMsgByKey(AppConstant.print_quote));
        tv_fab_QuotesToJob = findViewById(R.id.tv_fab_QuotesToJob);
        tv_fab_QuotesToJob.setText(LanguageController.getInstance().getMobileMsgByKey(AppConstant.quote_to_job));

        tv_create_date = findViewById(R.id.tv_create_date);
        tv_create_date.setText(LanguageController.getInstance().getMobileMsgByKey(AppConstant.quotes_start_date));

        tv_due_date = findViewById(R.id.tv_due_date);
        tv_due_date.setText(LanguageController.getInstance().getMobileMsgByKey(AppConstant.quotes_end_date));

        /**this view for shpping item**/
        recyclerView_shippingitem = findViewById(R.id.recyclerView_shippingitem);

    }

    private void intialize_UI_Views() {
        /***Check Remove Item Permission****/
        if (App_preference.getSharedprefInstance().getLoginRes().getIsItemDeleteEnable().equals("0")) {
            rm_quote_im.setVisibility(View.GONE);
        } else {
            rm_quote_im.setVisibility(View.VISIBLE);
        }
        rm_quote_im.setOnClickListener(this);

        pay_txt = findViewById(R.id.pay_txt);
        backgroundView = findViewById(R.id.backgroundView);

        layoutManager = new LinearLayoutManager(this);
        recyclerView_quote.setLayoutManager(layoutManager);

        layoutManager = new LinearLayoutManager(this);
        recyclerView_shippingitem.setLayoutManager(layoutManager);
        List<ShippingItem> shippingItemList = new ArrayList<>();
        sipping_adpter = new Sipping_Adpter(shippingItemList);
        recyclerView_shippingitem.setAdapter(sipping_adpter);


        /**false nested scrolling when multiple recyclerview used in single view**/
        recyclerView_quote.setNestedScrollingEnabled(false);
        recyclerView_shippingitem.setNestedScrollingEnabled(false);

        invoiceFab.setOnClickListener(this);
        linearFabEmail.setOnClickListener(this);
        linearFabPrintInvoice.setOnClickListener(this);
        linearFabAddNewItem.setOnClickListener(this);
        linearFabQuotesInJob.setOnClickListener(this);

        backgroundView.setOnClickListener(this);


        lay.setVisibility(View.VISIBLE);


        quo_invo_pi = new Quo_Inv_Pc(this);
//        quo_invo_pi.getQuotesInvoiceDetails(quotId);

        swiperefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                quo_invo_pi.getQuotesInvoiceDetails(quotId);
            }
        });

    }

    @Override
    protected void onResume() {
        AppUtility.progressBarShow(this);
        quo_invo_pi.getQuotesInvoiceDetails(quotId);
        super.onResume();
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
                editQuotes();
                break;
            case android.R.id.home:
                onBackPressed();
                AppUtility.hideSoftKeyboard(this);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void setQuotesDetails(QuotesDetails quotesDetails) {
        try {
            if (quotesDetails != null) {
                this.quotesDetails = quotesDetails;
                quote_nm.setText(quotesDetails.getNm());
                invoice_adrs.setText(quotesDetails.getAdr());

                StringBuffer sb_adr = new StringBuffer();

                if (quotesDetails.getCity() != null && !quotesDetails.getCity().equals("")) {
                    sb_adr.append(quotesDetails.getCity());
                }
                if (quotesDetails.getState() != null && !quotesDetails.getState().equals("")) {
                    sb_adr.append(", " + SpinnerCountrySite.getStatenameById(quotesDetails.getCtry(), quotesDetails.getState()));
                }
                if (quotesDetails.getCtry() != null && !quotesDetails.getCtry().equals("")) {
                    sb_adr.append(", " + SpinnerCountrySite.getCountryNameById(quotesDetails.getCtry()));
                }
                in_country.setText(sb_adr);

                if (quotesDetails.getInvData() != null) {
                    quotes_Details_Inv = quotesDetails.getInvData();
                    quotesDetails.getInvData().getItemData();
                }

                if (!quotes_Details_Inv.getInvDate().equals(""))
                    quote_cre_dt.setText(AppUtility.getDateWithFormate(Long.parseLong(quotes_Details_Inv.getInvDate()), "dd-MMM-yyyy"));
                if (!quotes_Details_Inv.getDuedate().equals(""))
                    quote_due_dt.setText(AppUtility.getDateWithFormate(Long.parseLong(quotes_Details_Inv.getDuedate()), "dd-MMM-yyyy"));
                quote_total_amount.setText(AppUtility.getRoundoff_amount(quotes_Details_Inv.getTotal()));
                if (quotesDetails.getInvData() != null && quotesDetails.getInvData().getItemData() != null) {
                    quotes_item_Adpter = new Quote_Details_Adpter(this, quotesDetails.getInvData().getItemData(), this, this, quotes_Details_Inv.getTaxCalculationType());
                    recyclerView_quote.setAdapter(quotes_item_Adpter);
                }

                sipping_adpter.updateShpiningItem(quotes_Details_Inv.getShippingItem(), quotes_Details_Inv.getTaxCalculationType());

                setTxtInsideView();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
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

    @Override
    public void onConvertQuotationToJob(String message) {
        EotApp.getAppinstance().showToastmsg(message);
    }

    private void setTxtInsideView() {
        rm_quote_im.setEnabled(false);
        int count = quotes_item_Adpter.getItemCount();
        list_item_quote_count.setText(LanguageController.getInstance().getMobileMsgByKey(AppConstant.list_item) + " (" + count + ")");
    }


    @Override
    public void onMyListitemSeleted(Quote_ItemData itemData) {
        edit_Quotes_Item(itemData);
    }

    private void edit_Quotes_Item(Quote_ItemData itemData) {
        Intent intent = new Intent(this, AddQutesItem_Activity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
        intent.putExtra("quotesDetails", new Gson().toJson(quotesDetails));
        intent.putExtra("itemId", itemData.getItemId());
        startActivityForResult(intent, ADD_ITEM_DATA);
    }

    @Override
    public void onMyListitem_Item_Seleted(ArrayList<String> itemDataRemove) {
        rm_DataItem = itemDataRemove;
        if (rm_DataItem.size() > 0) {
            rm_quote_im.setEnabled(true);
            rm_quote_im.setColorFilter(ContextCompat.getColor(this, R.color.colorPrimary));

        } else {
            rm_quote_im.setEnabled(false);
            rm_quote_im.setColorFilter(ContextCompat.getColor(this, R.color.txt_color));
        }
    }

    private void editQuotes() {
        String object = new Gson().toJson(quotesDetails);
        Intent intent = new Intent(this, AddQuotes_Activity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
        intent.putExtra("EditQuotes", object);
        startActivity(intent);
    }

    @Override
    public void setInvoiceDetails(Quote_invoice_Details_Res invoice_Details_data) {
        if (invoice_Details_data != null) {
            quote_nm.setText(invoice_Details_data.getNm());
            String clientAddress = "";
            if (!invoice_Details_data.getAdr().equals(""))
                clientAddress = invoice_Details_data.getAdr() + "\n";

            invoice_adrs.setText(clientAddress);
        }
    }


    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.invoiceFab:
                if (AppUtility.isInternetConnected()) {
                    if (!isFABOpen) {
                        showFABMenu();
                    } else {
                        closeFABMenu();
                    }
                } else {
                    AppUtility.alertDialog(Quote_Invoice_Details_Activity.this, LanguageController.getInstance().getMobileMsgByKey(AppConstant.dialog_alert), LanguageController.getInstance().getMobileMsgByKey(AppConstant.err_check_network), LanguageController.getInstance().getMobileMsgByKey(AppConstant.ok), "", new Callable<Boolean>() {
                        @Override
                        public Boolean call() throws Exception {

                            return null;
                        }
                    });
                }
                break;
            case R.id.backgroundView:
                closeFABMenu();
                break;
            case R.id.linearFabAddNewItem:
                addQuotesItem();
                closeFABMenu();
                break;
            case R.id.linearFabQuotesInJob:
//                Quotation covert into job change
//                QuotationController/convertQuotationToJob
                linearFabQuotesInJob.setClickable(false);
                quo_invo_pi.convertQuotationToJob(quotId);
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        linearFabQuotesInJob.setClickable(true);
                    }
                }, 500);

                closeFABMenu();
                break;

            case R.id.linearFabEmail:
                if (quotes_Details_Inv != null) {
                    Intent emailIntent = new Intent(this, Invoice_Email_Activity.class);
                    emailIntent.putExtra("quotId", quotId);
                    emailIntent.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
//                    emailIntent.putExtra("compId", invoice_Details.getCompId());
                    startActivity(emailIntent);
                }
                closeFABMenu();
                break;
            case R.id.linearFabPrintInvoice:
                linearFabPrintInvoice.setClickable(false);
                quo_invo_pi.generateQuotPDF(quotId);
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        linearFabPrintInvoice.setClickable(true);
                    }
                }, 500);

                closeFABMenu();
                break;
            case R.id.rm_quote_im:

                if (quotes_Details_Inv != null) {
                    if (quotes_Details_Inv.getItemData().size() > rm_DataItem.size()) {
                        removeSelectedItem();
                    } else {
                        show_Dialog(
                                LanguageController.getInstance().getMobileMsgByKey(AppConstant.remove_item_mandtry)
                        );
                    }
                }
                //   removeSelectedItem();
                break;
        }
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

    private void removeSelectedItem() {

        AppUtility.alertDialog2(this, "", LanguageController.getInstance().getMobileMsgByKey(AppConstant.invoice_remove), LanguageController.getInstance().getMobileMsgByKey(AppConstant.ok), LanguageController.getInstance().getMobileMsgByKey(AppConstant.cancel), new Callback_AlertDialog() {
            @Override
            public void onPossitiveCall() {
                quo_invo_pi.removeQuotesItem(rm_DataItem, quotes_Details_Inv.getInvId());
            }

            @Override
            public void onNegativeCall() {

            }
        });

    }


    @Override
    public void itemdeletedSuccefully() {
        quo_invo_pi.getQuotesInvoiceDetails(quotId);
    }

    private void addQuotesItem() {
        Intent intent = new Intent(this, AddQutesItem_Activity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
        intent.putExtra("AddQuotesItem", new Gson().toJson(quotesDetails));
        startActivity(intent);
    }

    private void showFABMenu() {
        isFABOpen = true;
        actionBar.setBackgroundDrawable(new ColorDrawable(getResources().getColor(R.color.bg_color)));
        actionBar.setStackedBackgroundDrawable(new ColorDrawable(getResources().getColor(R.color.dis_bg_color)));

        linearFabQuotesInJob.setVisibility(View.VISIBLE);
        linearFabEmail.setVisibility(View.VISIBLE);
        // linearFabAddNewItem.setVisibility(View.VISIBLE);
        linearFabPrintInvoice.setVisibility(View.VISIBLE);
        backgroundView.setVisibility(View.VISIBLE);


        if (App_preference.getSharedprefInstance().getLoginRes().getCompPermission().get(0).getIsItemEnable().equals("1")) {
            linearFabAddNewItem.setVisibility(View.GONE);
            linearFabQuotesInJob.animate().translationY(getResources().getDimension(R.dimen.standard_145));
            linearFabEmail.animate().translationY(getResources().getDimension(R.dimen.standard_100));
            linearFabPrintInvoice.animate().translationY(getResources().getDimension(R.dimen.standard_55));
        } else {
            linearFabAddNewItem.setVisibility(View.VISIBLE);
            linearFabQuotesInJob.animate().translationY(getResources().getDimension(R.dimen.standard_200));
            linearFabEmail.animate().translationY(getResources().getDimension(R.dimen.standard_145));
            linearFabPrintInvoice.animate().translationY(getResources().getDimension(R.dimen.standard_100));
            linearFabAddNewItem.animate().translationY(getResources().getDimension(R.dimen.standard_55));
        }

    }


    private void closeFABMenu() {
        isFABOpen = false;
        linearFabQuotesInJob.animate().translationY(0);
        linearFabEmail.animate().translationY(0);
        linearFabAddNewItem.animate().translationY(0);

        linearFabPrintInvoice.animate().translationY(0).setListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animator) {

            }

            @Override
            public void onAnimationEnd(Animator animator) {
                if (!isFABOpen) {
                    linearFabQuotesInJob.setVisibility(View.GONE);
                    linearFabEmail.setVisibility(View.GONE);
                    linearFabPrintInvoice.setVisibility(View.GONE);
                    linearFabAddNewItem.setVisibility(View.GONE);
                    backgroundView.setVisibility(View.GONE);
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
    public void dismissPullTorefresh() {
        AppUtility.progressBarDissMiss();
        if (swiperefresh.isRefreshing()) {
            swiperefresh.setRefreshing(false);
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

}
