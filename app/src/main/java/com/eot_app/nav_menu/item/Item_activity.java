package com.eot_app.nav_menu.item;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.eot_app.R;
import com.eot_app.nav_menu.jobs.job_db.Job;
import com.eot_app.nav_menu.jobs.job_detail.addinvoiveitem2pkg.AddEditInvoiceItemActivity2;
import com.eot_app.nav_menu.jobs.job_detail.addinvoiveitem2pkg.model.AddInvoiceItemReqModel;
import com.eot_app.nav_menu.jobs.job_detail.addinvoiveitem2pkg.model.InvoiceItemDataModel;
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
import com.google.gson.Gson;
import com.hypertrack.hyperlog.HyperLog;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.concurrent.Callable;

public class Item_activity extends AppCompatActivity implements ItemList_View, JobItem_Observer, View.OnClickListener {
    public final static int ADD_ITEM_DATA = 1;
    private RecyclerView recyclerView_item;
    private Button addInvoiceItem_btn;
    private SwipeRefreshLayout swiperefresh;
    private LinearLayoutManager layoutManager;
    private AdditemAdpter invoice_list_adpter;
    private String jobId, invId = "", locId = "0";
    private ItemList_PI itemListPi;
    private RelativeLayout lay;
    private LinearLayout empty_invoice_lay;
    private TextView empty_inv_txt;
    private Job job;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_item_activity);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle(LanguageController.getInstance().getMobileMsgByKey(AppConstant.items_screen_title));


        EotApp.getAppinstance().setApiItemAddEdit_Observer(this);
        Bundle bundle = getIntent().getExtras();
        if (getIntent().hasExtra("JobId")) {
            jobId = bundle.getString("JobId");
            try {
                job = AppDataBase.getInMemoryDatabase(EotApp.getAppinstance()).jobModel().getJobsById(jobId);
            } catch (Exception exception) {
                exception.getMessage();
                locId = "0";
            }
        }

        initializelables();
    }

    private void initializelables() {
        lay = findViewById(R.id.lay);
        swiperefresh = findViewById(R.id.swiperefresh);
        recyclerView_item = findViewById(R.id.recyclerView_item);

        addInvoiceItem_btn = findViewById(R.id.addInvoiceItem_btn);
        addInvoiceItem_btn.setText(LanguageController.getInstance().getMobileMsgByKey(AppConstant.add_new_item));

        empty_invoice_lay = findViewById(R.id.nolist_linear);

        empty_inv_txt = findViewById(R.id.nolist_txt);
        empty_inv_txt.setText(LanguageController.getInstance().getMobileMsgByKey(AppConstant.no_invoice));

        intialize_UI_Views();
        AppUtility.setupUI(lay, Item_activity.this);
    }

    @Override
    public void setLocationTaxsList(List<TaxesLocation> taxList) {

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
            case Service_apis.updateItemQuantity:
                if (itemListPi != null) {
                    this.jobId = jobId;
                    itemListPi.getItemListByJobFromDB(jobId);
                }
                break;
        }
    }

    private void intialize_UI_Views() {
        layoutManager = new LinearLayoutManager(this);
        recyclerView_item.setLayoutManager(layoutManager);

        invoice_list_adpter = new AdditemAdpter(this, new ArrayList<InvoiceItemDataModel>());//, this, this
        recyclerView_item.setAdapter(invoice_list_adpter);

        addInvoiceItem_btn.setOnClickListener(this);
        swiperefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                OfflineDataController.getInstance().updateSyncdata();
                itemListPi.getItemFromServer(jobId);
            }
        });

        /******item add button hide when admin not authorized*****/
        if (App_preference.getSharedprefInstance().getLoginRes().getCompPermission().get(0).getIsItemEnable().equals("1")) {
            addInvoiceItem_btn.setVisibility(View.GONE);
        }

        itemListPi = new ItemList_PC(this);
        itemListPi.getItemFromServer(jobId);
        itemListPi.getItemListByJobFromDB(jobId);
    }

    @Override
    public void setItemListByJob(List<InvoiceItemDataModel> itemList) {
        //*** *Sort Item By name**
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
        if (itemList != null && invoice_list_adpter != null) {
            invoice_list_adpter.updateitemlist(itemList);
        }
        InvoiceNotFound(itemList.size() == 0);
        dismissPullTorefresh();
    }

    private void InvoiceNotFound(boolean enable) {
        if (enable) {
            empty_invoice_lay.setVisibility(View.VISIBLE);
            recyclerView_item.setVisibility(View.GONE);
            empty_inv_txt.setText(LanguageController.getInstance().getMobileMsgByKey(AppConstant.no_invoice));
        } else {
            empty_invoice_lay.setVisibility(View.GONE);
            recyclerView_item.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void dismissPullTorefresh() {
        if (swiperefresh.isRefreshing()) {
            swiperefresh.setRefreshing(false);
        }
    }

    @Override
    public void setInvoiceDetails(InvoiceItemDetailsModel invResModel) {
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
    public void finishActivity() {

    }


    /****Add Item**/
    private void addInvoiceItem() {
        Intent intent = new Intent(this, AddEditInvoiceItemActivity2.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
        intent.putExtra("jobId", jobId);
        intent.putExtra("invId", invId);
        intent.putExtra("locId", locId);
        intent.putExtra("NONBILLABLE", false);
        startActivityForResult(intent, ADD_ITEM_DATA);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if (data == null) {
            return;
        } else {
            switch (requestCode) {
                case ADD_ITEM_DATA:
                    if (data.hasExtra("AddInvoiceItem")) {
                        itemListPi.getItemListByJobFromDB(jobId);
                        break;
                    }
            }
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.addInvoiceItem_btn:
                addInvoiceItem();
                break;
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.items_menu, menu);

        MenuItem item = menu.findItem(R.id.miCompose);
        item.setTitle(LanguageController.getInstance().getMobileMsgByKey(AppConstant.save_btn));
        List<InvoiceItemDataModel> itemList = invoice_list_adpter.getItemList();
        // empty_invoice_lay.setVisibility(View.GONE);
        //empty_invoice_lay.setVisibility(View.VISIBLE);
        item.setVisible(itemList.size() > 0);

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

            case android.R.id.home:
                onBackPressed();
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