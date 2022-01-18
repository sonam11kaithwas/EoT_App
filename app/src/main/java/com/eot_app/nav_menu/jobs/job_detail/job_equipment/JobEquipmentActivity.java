package com.eot_app.nav_menu.jobs.job_detail.job_equipment;

import android.animation.Animator;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.eot_app.R;
import com.eot_app.nav_menu.audit.nav_scan.EquipmentDetailsActivity;
import com.eot_app.nav_menu.equipment.linkequip.ActivityLinkEquipment;
import com.eot_app.nav_menu.equipment.popupSaveClient.AlertDialogClass;
import com.eot_app.nav_menu.equipment.popupSaveClient.equipmentClinetsave.Equipment_Client_view;
import com.eot_app.nav_menu.jobs.job_db.EquArrayModel;
import com.eot_app.nav_menu.jobs.job_db.Job;
import com.eot_app.nav_menu.jobs.job_detail.job_equipment.add_job_equip.AddJobEquipMentActivity;
import com.eot_app.nav_menu.jobs.job_detail.job_equipment.job_equ_mvp.Job_equim_PC;
import com.eot_app.nav_menu.jobs.job_detail.job_equipment.job_equ_mvp.Job_equim_PI;
import com.eot_app.nav_menu.jobs.job_detail.job_equipment.job_equ_mvp.Job_equim_View;
import com.eot_app.nav_menu.jobs.job_detail.job_equipment.job_equ_remrk.JobEquRemarkRemarkActivity;
import com.eot_app.nav_menu.jobs.job_detail.job_equipment.model.EquipmentSaveClientRes;
import com.eot_app.utility.AppConstant;
import com.eot_app.utility.AppUtility;
import com.eot_app.utility.App_preference;
import com.eot_app.utility.EotApp;
import com.eot_app.utility.db.AppDataBase;
import com.eot_app.utility.language_support.LanguageController;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.concurrent.Callable;

public class JobEquipmentActivity extends AppCompatActivity implements Job_equim_View, JobEquipmentAdapter.OnEquipmentSelection, View.OnClickListener, Equipment_Client_view {


    public static final int ADDEQUIPMENT = 1000;
    private final int EQUIPMENT_UPDATE_CODE = 141, DETAILSUPDATEFORUSERMANUAL = 142;
    LinearLayout linearFabAdd;
    LinearLayout linearFabclient;
    LinearLayout linearFabown;
    AlertDialogClass alertDialogClass;
    boolean isListLoaded = false;
    List<EquArrayModel> myList = new ArrayList<>();
    boolean isOwn = false;
    Job job;
    EditText edtSearch;
    ImageView imvCross;
    String query;
    private RecyclerView recyclerView;
    private SwipeRefreshLayout swipeRefreshLayout;
    private LinearLayout nolist_linear;
    private TextView nolist_txt, tv_fab_add, tv_fab_client, tv_fab_own;
    private FloatingActionButton fab;
    private boolean isFBMenuOpened;
    private JobEquipmentAdapter adapter;
    private Job_equim_PI jobEquimPi;
    private String jobId = "", auditid = "", contrId = "";
    private String cltId;
    private CoordinatorLayout relative_layout;
    private View backgroundView;
    private String appId;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_job_equipment);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        setTitle(LanguageController.getInstance().getMobileMsgByKey(AppConstant.detail_equipment));
        getintentData();
        initializeView();

    }

    private void getintentData() {
        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            jobId = getIntent().getStringExtra("JobId");
            auditid = getIntent().getStringExtra("auditid");
            cltId = getIntent().getStringExtra("cltId");
            contrId = getIntent().getStringExtra("contrId");
            appId = getIntent().getStringExtra("appId");
            if (TextUtils.isEmpty(contrId))
                contrId = "";
            else if (contrId.equals("0"))
                contrId = "";

        }
    }

    private void initializeView() {
        if (jobId != null) {
            job = AppDataBase.getInMemoryDatabase(this).jobModel().getJobsById(jobId);
            cltId = job.getCltId();
        }
        if (!TextUtils.isEmpty(appId))
            alertDialogClass = new AlertDialogClass(this, this, jobId, "", appId);
        else
            alertDialogClass = new AlertDialogClass(this, this, jobId, "", "");
        backgroundView = findViewById(R.id.backgroundView);


        relative_layout = findViewById(R.id.relative_layout);
        AppUtility.setupUI(relative_layout, JobEquipmentActivity.this);

        fab = findViewById(R.id.fab);

        linearFabAdd = findViewById(R.id.linearFabAdd);


        linearFabown = findViewById(R.id.linearFabown);
        linearFabclient = findViewById(R.id.linearFabclient);

        tv_fab_add = findViewById(R.id.tv_fab_add);
        tv_fab_client = findViewById(R.id.tv_fab_client);
        tv_fab_own = findViewById(R.id.tv_fab_own);

        tv_fab_add.setText(LanguageController.getInstance().getMobileMsgByKey(AppConstant.title_add_equipment));
        tv_fab_client.setText(LanguageController.getInstance().getMobileMsgByKey(AppConstant.link_client_equipment));
        tv_fab_own.setText(LanguageController.getInstance().getMobileMsgByKey(AppConstant.link_own_equipment));

        recyclerView = findViewById(R.id.audit_equipment_list);
        nolist_txt = findViewById(R.id.nolist_txt);
        nolist_linear = findViewById(R.id.nolist_linear);
        swipeRefreshLayout = findViewById(R.id.swiperefresh);
        swipeRefreshLayout.setRefreshing(false);

        nolist_txt.setText(LanguageController.getInstance().getMobileMsgByKey(AppConstant.equipment_not_found));

        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        fab.setOnClickListener(this);
        backgroundView.setOnClickListener(this);
        linearFabAdd.setOnClickListener(this);
        linearFabclient.setOnClickListener(this);
        linearFabown.setOnClickListener(this);


        edtSearch = findViewById(R.id.edtSearch);
        edtSearch.setHint(LanguageController.getInstance().getMobileMsgByKey(AppConstant.search_by_site));
        imvCross = findViewById(R.id.imvCross);

        if (adapter == null) {
            adapter = new JobEquipmentAdapter(this, new JobEquipmentAdapter.OnEquipmentSelectionForDetails() {
                @Override
                public void onEquipmentSelectedForDetails(EquArrayModel equipmentRes) {
                    Intent intent = new Intent(JobEquipmentActivity.this, EquipmentDetailsActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                    intent.putExtra("job_equip", equipmentRes);
                    String str = new Gson().toJson(equipmentRes);
                    intent.putExtra("job_equip_str", str);
                    intent.putExtra("equipment", true);
                    startActivityForResult(intent, DETAILSUPDATEFORUSERMANUAL);
                }
            }, new JobEquipmentAdapter.SelectedpositionForAttchment() {
                @Override
                public void selectedAttchpos(int attchpos, int equPos) {
                    try {
                        if (attchpos == 0 || attchpos == 1) {
                            startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(App_preference.getSharedprefInstance().getBaseURL()
                                    + "" + (myList.get(equPos).getAttachments().get(attchpos).getAttachFileName()))));

                        } else {
                            try {
                                if (myList.size() > 0) {
                                    String str = new Gson().toJson(myList.get(equPos).getAttachments());
                                    Intent intent = new Intent(JobEquipmentActivity.this, EquipmentAttchmentList.class);
                                    intent.putExtra("list", str);
                                    startActivity(intent);
                                }
                            } catch (Exception exception) {
                                exception.getMessage();
                            }

                        }
                    } catch (Exception e) {
                        e.getMessage();
                    }

                }
            });
            adapter.setOnEquipmentSelection(this);
            recyclerView.setAdapter(adapter);
        }

        jobEquimPi = new Job_equim_PC(this);
        if (jobEquimPi != null && !auditid.equals("0")) {
            jobEquimPi.refreshList(auditid, jobId);
        } else {
            jobEquimPi.getEquipmentList(jobId);
        }

        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                if (jobEquimPi != null && !auditid.equals("0")) {
                    recyclerView.setNestedScrollingEnabled(false);
                    jobEquimPi.refreshList(auditid, jobId);
                } else {
                    swipeRefresh();
                }
            }
        });


        edtSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (edtSearch.getText().toString().equals("")) {
                    jobEquimPi.getEquipmentList(auditid);
                }
                query = edtSearch.getText().toString();
                if (query.length() > 0) {
                    if (query.length() >= 1) {
                        imvCross.setVisibility(View.VISIBLE);
                    } else {
                        imvCross.setVisibility(View.GONE);
                    }
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
                query = edtSearch.getText().toString();
                filter(query);
                // adapter.getNameFilter().filter(searchText);
                //jobEquimPi.getEquipmentBySiteName(jobId, searchText);
            }
        });
        imvCross.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                edtSearch.setText("");
                jobEquimPi.getEquipmentList(jobId);
            }
        });


    }


    private void filter(String text) {
        //new array list that will hold the filtered data
        ArrayList<EquArrayModel> filterdNames = new ArrayList<>();

        //looping through existing elements
        for (EquArrayModel s : myList) {
            //if the existing elements contains the search input
            if (s.getSnm().toLowerCase().contains(text.toLowerCase())
                    || (s.getSno() != null && s.getSno().equalsIgnoreCase(text.toLowerCase())) ||
                    (s.getMno() != null && s.getMno().equalsIgnoreCase(text.toLowerCase()))
                    || (s.getEqunm() != null && s.getEqunm().equalsIgnoreCase(text.toLowerCase())
            )) {
                //adding the element to filtered list
                filterdNames.add(s);
            }
        }

        //calling a method of the adapter class and passing the filtered list
        adapter.filterList(filterdNames);
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void setEuqipmentList(List<EquArrayModel> list) {
        if (!myList.isEmpty())
            myList.clear();
        this.myList = list;
        isListLoaded = true;
        swipeRefresh();
        Collections.sort(list, new Comparator<EquArrayModel>() {
            @Override
            public int compare(EquArrayModel o1, EquArrayModel o2) {
                return o1.getEqunm().compareTo(o2.getEqunm());
            }
        });

        if (swipeRefreshLayout != null) swipeRefreshLayout.setRefreshing(false);
        if (list != null) {
            if (list.size() == 0)
                nolist_linear.setVisibility(View.VISIBLE);
            else nolist_linear.setVisibility(View.GONE);
            adapter.setList(list);
            checkLinkedEquipmentType();

            recyclerView.setNestedScrollingEnabled(true);
        }
    }

    @Override
    public void showErrorAlertDialog(String message) {
        if (swipeRefreshLayout != null) swipeRefreshLayout.setRefreshing(false);
        showDialog(message);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            onBackPressed();
            return true;
        }
        return true;
    }

    @Override
    public void onSessionExpired(String msg) {
        if (swipeRefreshLayout != null) swipeRefreshLayout.setRefreshing(false);
        showDialog(msg);
    }

    @Override
    public void swipeRefresh() {
        if (swipeRefreshLayout.isRefreshing())
            swipeRefreshLayout.setRefreshing(false);
    }

    private void showDialog(String message) {
        AppUtility.alertDialog(this, LanguageController.getInstance().getMobileMsgByKey(AppConstant.dialog_error_title), message, LanguageController.getInstance().getMobileMsgByKey(AppConstant.ok), "", new Callable<Boolean>() {
            @Override
            public Boolean call() throws Exception {
                EotApp.getAppinstance().sessionExpired();
                return null;
            }
        });
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == EQUIPMENT_UPDATE_CODE || requestCode == ADDEQUIPMENT || (DETAILSUPDATEFORUSERMANUAL == requestCode) && resultCode == Activity.RESULT_OK) {
            if (adapter != null && jobEquimPi != null) {
                jobEquimPi.refreshList(jobId, jobId);
            }
        }
    }

    @Override
    public void onEquipmentSelected(int positions, EquArrayModel equipmentRes) {
        Intent intent = new Intent(this, JobEquRemarkRemarkActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
        String strEqu = new Gson().toJson(equipmentRes);
        intent.putExtra("equipment", strEqu);
        intent.putExtra("jobId", jobId);
        intent.putExtra("positions", positions);
        intent.putExtra("isGetData", "");
        startActivityForResult(intent, EQUIPMENT_UPDATE_CODE);
    }


    @Override
    public void onBackPressed() {
        setResult(RESULT_OK, new Intent());
        finish();
    }


    public void showFloatingButtons() {
        if (isFBMenuOpened) closeFABMenu();
        else showFBButtons();
    }

    private void showFBButtons() {
        if (isListLoaded) {
            if (!isOwn && !TextUtils.isEmpty(contrId)) {
                linearFabown.setAlpha(0.5f);
                linearFabclient.setAlpha(1f);
                linearFabclient.setClickable(true);
                linearFabown.setClickable(false);
            } else if (adapter != null && adapter.getList().size() == 0) {
                linearFabown.setAlpha(1f);
                linearFabclient.setAlpha(1f);
                linearFabclient.setClickable(true);
                linearFabown.setClickable(true);
            } else if (isOwn) {
                linearFabown.setAlpha(1f);
                linearFabclient.setAlpha(0.5f);
                linearFabclient.setClickable(false);
                linearFabown.setClickable(true);
            } else {
                linearFabown.setAlpha(0.5f);
                linearFabclient.setAlpha(1f);
                linearFabclient.setClickable(true);
                linearFabown.setClickable(false);
            }
        } else {
            linearFabown.setAlpha(0.5f);
            linearFabclient.setAlpha(0.5f);
            linearFabclient.setClickable(false);
            linearFabown.setClickable(false);
        }
        getSupportActionBar().setBackgroundDrawable(new ColorDrawable(getResources().getColor(R.color.bg_color)));
        getSupportActionBar().setStackedBackgroundDrawable(new ColorDrawable(Color.parseColor("#90ffffff")));
        backgroundView.setVisibility(View.VISIBLE);
        if (App_preference.getSharedprefInstance().getLoginRes().getCompPermission().get(0).getIsEquipmentAddEnable().equals("0")) {
            linearFabAdd.setVisibility(View.VISIBLE);
        } else linearFabAdd.setVisibility(View.GONE);
        linearFabclient.setVisibility(View.VISIBLE);
        linearFabown.setVisibility(View.VISIBLE);
        linearFabAdd.animate().translationY(getResources().getDimension(R.dimen.standard_145));
        linearFabclient.animate().translationY(getResources().getDimension(R.dimen.standard_100));
        linearFabown.animate().translationY(getResources().getDimension(R.dimen.standard_55));
        isFBMenuOpened = true;
    }

    private void closeFABMenu() {
        isFBMenuOpened = false;
        linearFabAdd.animate().translationY(0);
        linearFabclient.animate().translationY(0);
        linearFabown.animate().translationY(0);

        linearFabown.animate().translationY(0).setListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animator) {

            }

            @Override
            public void onAnimationEnd(Animator animator) {
                if (!isFBMenuOpened) {
                    backgroundView.setVisibility(View.GONE);
                    linearFabown.setVisibility(View.GONE);
                    linearFabclient.setVisibility(View.GONE);
                    linearFabAdd.setVisibility(View.GONE);
                    getSupportActionBar().setBackgroundDrawable(new ColorDrawable(getResources().getColor(R.color.colorPrimary)));

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
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.fab:
                if (cltId.equals("0")) {
                    alertDialogClass.alertDialog();
                } else {
                    showFloatingButtons();
                }
                break;

            case R.id.linearFabAdd:
                if (AppUtility.isInternetConnected()) {

                    Intent intent = new Intent(JobEquipmentActivity.this, AddJobEquipMentActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                    intent.putExtra("JobId", jobId);
                    intent.putExtra("cltId", cltId);
                    startActivityForResult(intent, ADDEQUIPMENT);

                } else {
                    AppUtility.alertDialog(JobEquipmentActivity.this, LanguageController.getInstance().getMobileMsgByKey(AppConstant.dialog_alert), LanguageController.getInstance().getMobileMsgByKey(AppConstant.err_check_network), LanguageController.getInstance().getMobileMsgByKey(AppConstant.ok), "", new Callable<Boolean>() {
                        @Override
                        public Boolean call() throws Exception {
                            return null;
                        }
                    });
                }
                closeFABMenu();
                break;
            case R.id.linearFabclient:
                Intent intent = new Intent(JobEquipmentActivity.this, ActivityLinkEquipment.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                intent.putExtra("type", "2");
                intent.putExtra("cltId", cltId);
                intent.putExtra("id", jobId);
                intent.putExtra("contrId", contrId);
                startActivityForResult(intent, EQUIPMENT_UPDATE_CODE);
                closeFABMenu();
                break;

            case R.id.linearFabown:
                Intent intent1 = new Intent(JobEquipmentActivity.this, ActivityLinkEquipment.class);
                intent1.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                intent1.putExtra("type", "1");
                intent1.putExtra("cltId", "");
                intent1.putExtra("id", jobId);
                startActivityForResult(intent1, EQUIPMENT_UPDATE_CODE);
                closeFABMenu();
                break;

            case R.id.backgroundView:
                closeFABMenu();
                break;

        }
    }


    private void checkLinkedEquipmentType() {
        if (adapter != null && adapter.getList() != null &&
                adapter.getList().size() > 0) {
            isOwn = adapter.getList().get(0).getType().equals("1");
            if (!TextUtils.isEmpty(contrId)) isOwn = false;
        }
    }


    @Override
    public void setClientForSaveUse(EquipmentSaveClientRes equipmentSaveClientRes) {

        cltId = equipmentSaveClientRes.getCltId();

    }

}