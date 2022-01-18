package com.eot_app.nav_menu.jobs.job_detail.invoice2list;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.eot_app.R;
import com.eot_app.nav_menu.item.ItemDatum;
import com.eot_app.nav_menu.item.QtyReqModel;
import com.eot_app.nav_menu.jobs.job_db.Job;
import com.eot_app.nav_menu.jobs.job_detail.addinvoiveitem2pkg.AddEditInvoiceItemActivity2;
import com.eot_app.nav_menu.jobs.job_detail.addinvoiveitem2pkg.model.AddInvoiceItemReqModel;
import com.eot_app.nav_menu.jobs.job_detail.addinvoiveitem2pkg.model.InvoiceItemDataModel;
import com.eot_app.nav_menu.jobs.job_detail.invoice.invoice_db.location_tax_dao.TaxLocAdpter;
import com.eot_app.nav_menu.jobs.job_detail.invoice.invoice_db.location_tax_dao.TaxesLocation;
import com.eot_app.nav_menu.jobs.job_detail.invoice2list.itemlist_model.InvoiceItemDetailsModel;
import com.eot_app.nav_menu.jobs.job_detail.invoice2list.itemlist_mvp.ItemList_PC;
import com.eot_app.nav_menu.jobs.job_detail.invoice2list.itemlist_mvp.ItemList_PI;
import com.eot_app.nav_menu.jobs.job_detail.invoice2list.itemlist_mvp.ItemList_View;
import com.eot_app.nav_menu.jobs.joboffline_db.JobItem_Observer;
import com.eot_app.nav_menu.jobs.joboffline_db.JobOfflineDataModel;
import com.eot_app.services.Service_apis;
import com.eot_app.utility.AppConstant;
import com.eot_app.utility.AppUtility;
import com.eot_app.utility.App_preference;
import com.eot_app.utility.EotApp;
import com.eot_app.utility.db.AppDataBase;
import com.eot_app.utility.db.OfflineDataController;
import com.eot_app.utility.language_support.LanguageController;
import com.eot_app.utility.settings.setting_db.Offlinetable;
import com.eot_app.utility.util_interfaces.Callback_AlertDialog;
import com.eot_app.utility.util_interfaces.MyListItemSelected;
import com.eot_app.utility.util_interfaces.MyListItemSelectedLisT;
import com.google.gson.Gson;
import com.hypertrack.hyperlog.HyperLog;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.concurrent.Callable;

public class JoBInvoiceItemList2Activity extends AppCompatActivity implements View.OnClickListener, ItemList_View
        , MyListItemSelectedLisT<InvoiceItemDataModel>, MyListItemSelected<InvoiceItemDataModel>, JobItem_Observer {
    public final static int ADD_ITEM_DATA = 1;
    boolean islang;
    private Menu menu;
    private Button pay_btn_inv, addInvoiceItem_btn;
    private String jobId, invId = "";
    private RelativeLayout listItem_invoice_layout;
    private RecyclerView recyclerView_invoice;
    private LinearLayoutManager layoutManager;
    private InvoiceItemList2Adpter invoice_list_adpter;
    private TextView list_item_invoice_count;//, pay_txt;
    // private RelativeLayout lay;
    private ImageView rm_invice_im;
    private ItemList_PI itemListPi;
    private ArrayList<InvoiceItemDataModel> rm_DataItem = new ArrayList<>();
    private LinearLayout empty_invoice_lay;
    private TextView empty_inv_txt;
    private SwipeRefreshLayout swiperefresh;
    private int totalItemSize = 0;
    private List<InvoiceItemDataModel> tempItemList = new ArrayList<>();
    private Spinner loc_tax_dp;
    private String locId = "0";
    private TextView tax_loc_lable, loc_txt, remove_txt_loc;
    private List<TaxesLocation> taxList = new ArrayList<>();
    private RelativeLayout rl;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().requestFeature(Window.FEATURE_ACTION_BAR_OVERLAY);
        setContentView(R.layout.activity_invoice);

        //        set observer for callback
        EotApp.getAppinstance().setApiItemAddEdit_Observer(this);


        setTitle(LanguageController.getInstance().getMobileMsgByKey(AppConstant.items_screen_title));
        getSupportActionBar().setElevation(0);  //remove actionbar/title shadow
        Bundle bundle = getIntent().getExtras();
        if (getIntent().hasExtra("JobId")) {
            jobId = bundle.getString("JobId");
            try {
                if (jobId != null && !jobId.equals("")) {
                    Job job = AppDataBase.getInMemoryDatabase(EotApp.getAppinstance()).jobModel().getJobsById(jobId);
                    if (job.getLocId() == null)
                        job.setLocId("0");
                    else locId = job.getLocId();
                }
            } catch (Exception ex) {
                ex.getMessage();
            }
        }
        initializelables();

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

    @Override
    protected void onPause() {
        Log.e("", "");
        super.onPause();
    }

    @Override
    protected void onResume() {
        Log.e("", "");
        super.onResume();
    }

    @Override
    protected void onStop() {
        Log.e("", "");

        super.onStop();
    }

    @Override
    protected void onDestroy() {
        Log.e("", "");

        super.onDestroy();
    }

    @Override
    public void setInvoiceDetails(InvoiceItemDetailsModel invResModel) {
        /**this for Item COnverter for equipment ***/
        if (invResModel != null && invResModel.getInvId() != null) {
            invId = invResModel.getInvId();
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
    public void InvoiceNotFound(String text) {

    }

    @Override
    public void setItemDataList(InvoiceItemDetailsModel invResModel) {

    }

    @Override
    public void onGetPdfPath(String pdfPath) {

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

    /***Notify After Item Sync****/
    @Override
    public void onObserveCallBack(String api_name, String jobId) {
        Log.e("TAG", api_name);
        switch (api_name) {
            case Service_apis.addItemOnJob:
            case Service_apis.deleteItemFromJob:
            case Service_apis.updateItemInJobMobile:
            case Service_apis.updateItemQuantity:
                if (itemListPi != null) {
                    this.jobId = jobId;
                    itemListPi.getItemListByJobFromDB(jobId);
                }
                break;
        }
    }

    private void initializelables() {
        recyclerView_invoice = findViewById(R.id.recyclerView_invoice);
        swiperefresh = findViewById(R.id.swiperefresh);
        list_item_invoice_count = findViewById(R.id.list_item_invoice_count);
        rm_invice_im = findViewById(R.id.rm_invice_im);


        /****Hide view for Form  2 ***/
        listItem_invoice_layout = findViewById(R.id.listItem_invoice_layout);
        if (!App_preference.getSharedprefInstance().getLoginRes().getIsJobItemQuantityFormEnable().equals("1")) {
            listItem_invoice_layout.setVisibility(View.VISIBLE);
        } else {
            listItem_invoice_layout.setVisibility(View.GONE);
        }


        /***Check Remove Item Permission****/
        if (App_preference.getSharedprefInstance().getLoginRes().getIsItemDeleteEnable().equals("0")) {
            rm_invice_im.setVisibility(View.GONE);
        } else {
            rm_invice_im.setVisibility(View.VISIBLE);
        }


        /***remove this Jit Sir on 23 oct 2020**/
        /*  *//****Add\Edit permission allow ***//*
        if (!App_preference.getSharedprefInstance().getLoginRes().getCompPermission().get(0).getIsItemEnable().equals("1")) {
  rm_invice_im.setOnClickListener(this);
        }*/


        rm_invice_im.setOnClickListener(this);
        // lay = findViewById(R.id.lay);
        empty_inv_txt = findViewById(R.id.empty_inv_txt);
        empty_inv_txt.setText(LanguageController.getInstance().getMobileMsgByKey(AppConstant.no_invoice));

        addInvoiceItem_btn = findViewById(R.id.addInvoiceItem_btn);
        addInvoiceItem_btn.setText(LanguageController.getInstance().getMobileMsgByKey(AppConstant.add_new_item));

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
        intialize_UI_Views();
    }

    private void intialize_UI_Views() {

        layoutManager = new LinearLayoutManager(this);
        recyclerView_invoice.setLayoutManager(layoutManager);

        invoice_list_adpter = new InvoiceItemList2Adpter(this, new ArrayList<InvoiceItemDataModel>());//, this, this
        recyclerView_invoice.setAdapter(invoice_list_adpter);


        // lay.setVisibility(View.VISIBLE);
        addInvoiceItem_btn.setOnClickListener(this);


        empty_invoice_lay = findViewById(R.id.empty_invoice_lay);
        swiperefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                OfflineDataController.getInstance().updateSyncdata();
                itemListPi.getItemFromServer(jobId);
            }
        });

        //item add button hide when admin not authorized
        if (App_preference.getSharedprefInstance().getLoginRes().getCompPermission().get(0).getIsItemEnable().equals("1")) {
            addInvoiceItem_btn.setVisibility(View.GONE);
        }


        setTxtInsideView();

        try {
            if (!locId.equals("0") && !locId.equals("")) {
                showLocationData(locId);


            } else if (App_preference.getSharedprefInstance().getLoginRes().getLocId() != null && !App_preference.getSharedprefInstance().getLoginRes().getLocId().equals("0")) {
                locId = App_preference.getSharedprefInstance().getLoginRes().getLocId();
                showLocationData(locId);
            }
        } catch (Exception ex) {
            ex.getMessage();
        }


        rl = findViewById(R.id.rl);
        if (App_preference.getSharedprefInstance().getLoginRes().getCompPermission().get(0).getIsLocationEnable() != null && App_preference.getSharedprefInstance().getLoginRes().getCompPermission().get(0).getIsLocationEnable().equals("1")) {
            rl.setVisibility(View.VISIBLE);
        } else {
            rl.setVisibility(View.GONE);
        }

        itemListPi = new ItemList_PC(this);
        itemListPi.getItemFromServer(jobId);
        itemListPi.getItemListByJobFromDB(jobId);
        itemListPi.getloctaxexList();
    }

    private void showLocationData(String locId) {
        TaxesLocation taxesLocation = AppDataBase.getInMemoryDatabase(EotApp.getAppinstance()).taxesLocationDao().getTaxLocationByid(locId);
        loc_txt.setText(taxesLocation.getLocation());
        remove_txt_loc.setVisibility(View.VISIBLE);
    }

    @Override
    public void finishActivity() {
        this.finish();
    }


    @Override
    public void setItemListByJob(List<InvoiceItemDataModel> itemList) {
        /**** *Sort Item By name***/
        try {
            Collections.sort(itemList, new Comparator<InvoiceItemDataModel>() {
                @Override
                public int compare(InvoiceItemDataModel o1, InvoiceItemDataModel o2) {
                    return o1.getInm().compareTo(o2.getInm());
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }

        this.tempItemList = itemList;
        if (rm_DataItem.size() > 0) {
            rm_DataItem.clear();
        }
        if (itemList != null && invoice_list_adpter != null && itemList.size() > 0) {
            invoice_list_adpter.updateitemlist(itemList);
            invalidateOptionsMenu();
        }
        setTxtInsideView();
        InvoiceNotFound(itemList.size() == 0);
        dismissPullTorefresh();
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

    private void setTxtInsideView() {
        if (invoice_list_adpter != null) {
            totalItemSize = invoice_list_adpter.getItemCount();
            list_item_invoice_count.setText(LanguageController.getInstance().getMobileMsgByKey(AppConstant.list_item) + " (" + totalItemSize + ")");
        }
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
            case R.id.addInvoiceItem_btn:
                addInvoiceItem();
                break;
            case R.id.rm_invice_im:
                if (rm_DataItem.size() > 0) {
                    removeSelectedItem();
                } else {
                    show_Dialog(
                            LanguageController.getInstance().getMobileMsgByKey(AppConstant.remove_item_mandtry)
                    );
                }
                break;
        }
    }

    private void removeApplyTaxtion() {
        locId = "0";
        loc_txt.setText(LanguageController.getInstance().getMobileMsgByKey(AppConstant.select));
        remove_txt_loc.setVisibility(View.GONE);

    }

    @Override
    public void dismissPullTorefresh() {
        if (swiperefresh.isRefreshing()) {
            swiperefresh.setRefreshing(false);
        }
    }

    /****Add Item**/
    private void addInvoiceItem() {
        Intent intent = new Intent(this, AddEditInvoiceItemActivity2.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
        intent.putExtra("invId", invId);
        intent.putExtra("jobId", jobId);
        intent.putExtra("locId", locId);
        intent.putExtra("NONBILLABLE", false);

        startActivityForResult(intent, ADD_ITEM_DATA);
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


    @Override
    public void onMyListitem_Item_Seleted(ArrayList<InvoiceItemDataModel> removeItemList) {
        rm_DataItem = removeItemList;
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
    public void onMyListitemSeleted(InvoiceItemDataModel InvoiceItemDataModel) {
        editInvoiceItem(InvoiceItemDataModel);
    }

    /**
     * edit Invoice item
     **/
    private void editInvoiceItem(InvoiceItemDataModel invoiceItemDataModel) {
        //convertInEquip(invoiceItemDataModel);
        Intent intent = new Intent(this, AddEditInvoiceItemActivity2.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
        intent.putExtra("InvoiceItemDataModel", invoiceItemDataModel);
        intent.putExtra("edit_jobId", jobId);
        intent.putExtra("invId", invId);
        intent.putExtra("locId", locId);
        intent.putExtra("NONBILLABLE", false);

        startActivityForResult(intent, ADD_ITEM_DATA);
    }

    /**
     * remove invoice item from list
     */
    private void removeSelectedItem() {
        AppUtility.alertDialog2(this, "", LanguageController.getInstance().getMobileMsgByKey(AppConstant.invoice_remove), LanguageController.getInstance().getMobileMsgByKey(AppConstant.ok), LanguageController.getInstance().getMobileMsgByKey(AppConstant.cancel), new Callback_AlertDialog() {
            @Override
            public void onPossitiveCall() {
                try {
                    ArrayList<String> ijmmIdList = new ArrayList<>();
                    List<InvoiceItemDataModel> notSyncItemList = new ArrayList<>();
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
                        itemListPi.updareRmitemsInDB(jobId, tempItemList, ijmmIdList, notSyncItemList, false);
                    } else {
                        show_Dialog(
                                LanguageController.getInstance().getMobileMsgByKey(AppConstant.remove_item_mandtry)
                        );
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }


            }

            @Override
            public void onNegativeCall() {

            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        try {
            if (data == null) {
                return;
            } else {
                switch (requestCode) {
                    case ADD_ITEM_DATA:
                        if (data.hasExtra("AddInvoiceItem")) {
//                            locId = App_preference.getSharedprefInstance().getLoginRes().getLocId();
//                            showLocationData(locId);
                            itemListPi.getItemListByJobFromDB(jobId);

                            break;
                        }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        if (this.menu == null) {
            this.menu = menu;
        }

        return super.onPrepareOptionsMenu(menu);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        this.menu = menu;

        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.items_menu, menu);

        MenuItem item = menu.findItem(R.id.miCompose);
        item.setTitle(LanguageController.getInstance().getMobileMsgByKey(AppConstant.update_btn));
        List<InvoiceItemDataModel> itemList = invoice_list_adpter.getItemList();

        if (App_preference.getSharedprefInstance().getLoginRes().getIsJobItemQuantityFormEnable().equals("1")) {
            item.setVisible(itemList.size() > 0);
        } else {
            item.setVisible(false);
        }

        return true;
    }

    /*****update Item List in Local DB for WithOut JOB synced***/
    private void updateItemWithoutJobSync(List<InvoiceItemDataModel> itemList, String jobTempId) {
        HyperLog.i("", "Item_activity: " + "updateItemWithoutJobSync(M)" + "Start");
        try {
            JobOfflineDataModel jobOfflineDataModel = AppDataBase.getInMemoryDatabase(EotApp.getAppinstance()).jobOfflineDao().
                    getJobofflineDataForInvoice(Service_apis.addItemOnJob, jobTempId);

            if (jobOfflineDataModel != null) {
                for (InvoiceItemDataModel updateItemModel : itemList) {

                    AddInvoiceItemReqModel addInvoiceItemReqModel = new Gson().fromJson(jobOfflineDataModel.getParams(), AddInvoiceItemReqModel.class);
                    for (InvoiceItemDataModel tempModel : addInvoiceItemReqModel.getItemData()) {
                        if (tempModel.getTempNm().equals(updateItemModel.getTempNm())) {
                            addInvoiceItemReqModel.getItemData().remove(tempModel);
                            addInvoiceItemReqModel.getItemData().add(updateItemModel);
                            jobOfflineDataModel.setParams(new Gson().toJson(addInvoiceItemReqModel));
                            AppDataBase.getInMemoryDatabase(EotApp.getAppinstance()).jobOfflineDao().update(jobOfflineDataModel);
                            break;
                        }
                    }
                }
            } else {
//                tempItemList.addAll(itemList);
                String dateTime = AppUtility.getDateByFormat(AppConstant.DATE_TIME_FORMAT);
                Gson gson = new Gson();
                AddInvoiceItemReqModel addInvoiceItem = new AddInvoiceItemReqModel(jobId, itemList, false, locId);
                JobOfflineDataModel model = new JobOfflineDataModel(Service_apis.addItemOnJob, gson.toJson(addInvoiceItem),
                        dateTime, jobTempId);
                AppDataBase.getInMemoryDatabase(EotApp.getAppinstance()).jobOfflineDao().insertJobOfflineData(model);
                HyperLog.i("", "Item_activity: " + "Insert In JobOfflineDataModel");
            }
            EotApp.getAppinstance().showToastmsg(LanguageController.getInstance().getMobileMsgByKey(AppConstant.item_qty_update));
        } catch (Exception e) {
            HyperLog.i("", "Item_activity: " + "Exception" + e.toString());
            e.getMessage();
        }
        HyperLog.i("", "Item_activity: " + "updateItemWithoutJobSync(M)" + "Completed");
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                return true;
            case R.id.miCompose:
                AppUtility.hideSoftKeyboard(this);
                Log.e("", "");
                HyperLog.i("", "Item_activity: " + "onOptionsItemSelected(M) start");


                try {
                    List<InvoiceItemDataModel> dbitemList = new ArrayList<>();
                    Job job = AppDataBase.getInMemoryDatabase(this).jobModel().getJobsById(jobId);
                    if (job != null && job.getItemData().size() > 0) {
                        dbitemList.addAll(job.getItemData());
                    }

                    List<InvoiceItemDataModel> itemList = invoice_list_adpter.getItemList();


                    AppDataBase.getInMemoryDatabase(EotApp.getAppinstance()).jobModel().updateJobitems(job.getJobId(), itemList);

                    try {
                        Collections.sort(itemList, new Comparator<InvoiceItemDataModel>() {
                            @Override
                            public int compare(InvoiceItemDataModel o1, InvoiceItemDataModel o2) {
                                return o1.getInm().compareTo(o2.getInm());
                            }
                        });
                    } catch (Exception e) {
                        HyperLog.i("", "Item_activity: " + "itemList(M) Comparator" + e.toString());
                        e.printStackTrace();
                    }


                    try {
                        Collections.sort(dbitemList, new Comparator<InvoiceItemDataModel>() {
                            @Override
                            public int compare(InvoiceItemDataModel o1, InvoiceItemDataModel o2) {
                                return o1.getInm().compareTo(o2.getInm());
                            }
                        });
                    } catch (Exception e) {
                        HyperLog.i("", "Item_activity: " + "dbitemList(M) Comparator" + e.toString());
                        e.printStackTrace();
                    }


                    /******This Block For JOB Synced***/
                    if (job != null && !job.getJobId().equals(job.getTempId())) {
                        HyperLog.i("", "Item_activity: " + "JOB Synced(Block)");

                        List<InvoiceItemDataModel> adpterUpdateQtyList = new ArrayList<>();
                        List<InvoiceItemDataModel> notSyncedItemReplaceList = new ArrayList<>();
                        for (InvoiceItemDataModel dbModel : dbitemList) {
                            for (InvoiceItemDataModel itemListModel : itemList) {
                                /***Synced item's Qty Update*****/
                                //  if (!dbModel.getIjmmId().equals("")) {
                                if ((!dbModel.getIjmmId().equals("")) && dbModel.getIjmmId().equals(itemListModel.getIjmmId())) {
                                    if (!dbModel.getQty().equals(itemListModel.getQty())) {
                                        adpterUpdateQtyList.add(itemListModel);
                                    }
                                    break;
                                } else {
                                    /*****Not Synced Item Update in Offline ReQuest DB****/
                                    if (!dbModel.getTempNm().equals("") && dbModel.getTempNm().equals(itemListModel.getTempNm())) {
                                        if (!dbModel.getQty().equals(itemListModel.getQty())) {
                                            notSyncedItemReplaceList.add(itemListModel);
                                        }
                                        break;
                                    }
                                }
                            }
                        }

                        Log.e("", "");

                        /****Filter List QTY ***/
                        List<ItemDatum> qitemList = new ArrayList<>();
                        for (InvoiceItemDataModel dataModel : adpterUpdateQtyList) {
                            Log.d("Mytag", dataModel.getInm() + " qty " + dataModel.getQty());
                            qitemList.add(new ItemDatum(dataModel.getIjmmId(), dataModel.getQty()));
                        }

                        /****Update QTY Synced Item's***/
                        if (qitemList != null && qitemList.size() > 0) {
                            callOnServerUpdateQty(new QtyReqModel(job.getJobId(), qitemList));
                        }


                        /***Update Item in Offline DB for WithOut synced Item's after Chnage Item Qty****/
                        if (notSyncedItemReplaceList != null && notSyncedItemReplaceList.size() > 0) {
                            updateWithOutSyncedItems(notSyncedItemReplaceList);
                        }

                    } else {
                        HyperLog.i("", "Item_activity: " + "JOB Not Synced(Block)");
                        /******update job Item List For WithOut Job Synced *****/
                        if (itemList != null && itemList.size() > 0)
                            updateItemWithoutJobSync(itemList, job.getTempId());
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    HyperLog.i("Item_activity", "onOptionsItemSelected(M) Exception" + e.toString());
                }
                HyperLog.i("", "Item_activity: " + "onOptionsItemSelected(M) Completed");
                return true;
        }

        return super.onOptionsItemSelected(item);
    }


    /**
     * Update Item's In Offline DB Not synced Item's
     * ****
     *
     * @param notSyncedItemReplaceList
     */
    private void updateWithOutSyncedItems(List<InvoiceItemDataModel> notSyncedItemReplaceList) {
        HyperLog.i("", "Item_activity: " + "updateWithOutSyncedItems(M)" + "Strat");
        notSyncdItemUpdateInOfflineDb(notSyncedItemReplaceList);
        HyperLog.i("", "Item_activity: " + "updateWithOutSyncedItems(M)" + "Complete");
    }


    /**
     * Update Qty on Server for synced Item's
     ******/

    private void callOnServerUpdateQty(QtyReqModel qtyReqModelsList) {
        HyperLog.i("", "Item_activity: " + "callOnServerUpdateQty(M)" + "Strat");
        Gson gson = new Gson();
        String addJobReqest = gson.toJson(qtyReqModelsList);
        OfflineDataController.getInstance().addInOfflineDB(Service_apis.updateItemQuantity, addJobReqest, AppUtility.getDateByFormat(AppConstant.DATE_TIME_FORMAT));
        HyperLog.i("", "Item_activity: " + "callOnServerUpdateQty(M)" + "Completed");
        EotApp.getAppinstance().showToastmsg(LanguageController.getInstance().getMobileMsgByKey(AppConstant.item_qty_update));
    }


    /***Job Sync But Item Not Sync Update
     * @param notSyncedItemReplaceList**/
    private void notSyncdItemUpdateInOfflineDb(List<InvoiceItemDataModel> notSyncedItemReplaceList) {
        HyperLog.i("", "Item_activity: " + "notSyncdItemUpdateInOfflineDb(M)" + "Strat");
        try {
            for (InvoiceItemDataModel updateItemModel : notSyncedItemReplaceList) {
                List<Offlinetable> offlineTableList = AppDataBase.getInMemoryDatabase(EotApp.getAppinstance()).offlinemodel().
                        getOfflinetablesById(Service_apis.addItemOnJob);
                if (offlineTableList != null && offlineTableList.size() > 0) {
                    for (Offlinetable offLineModel : offlineTableList) {
                        AddInvoiceItemReqModel addItemReqModel = new Gson().fromJson(offLineModel.getParams(), AddInvoiceItemReqModel.class);
                        if (addItemReqModel.getJobId().equals(jobId)) {
                            for (InvoiceItemDataModel tempModel : addItemReqModel.getItemData()) {
                                if (tempModel.getTempNm().equals(updateItemModel.getTempNm())) {
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
            }
        } catch (Exception ec) {
            ec.printStackTrace();
            HyperLog.i("", "Item_activity: " + ec.getMessage());
        }
        HyperLog.i("", "Item_activity: " + "notSyncdItemUpdateInOfflineDb(M)" + "Complete");
        EotApp.getAppinstance().showToastmsg(LanguageController.getInstance().getMobileMsgByKey(AppConstant.item_qty_update));
    }


}