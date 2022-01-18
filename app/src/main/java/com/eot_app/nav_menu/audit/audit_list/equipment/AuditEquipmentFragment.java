package com.eot_app.nav_menu.audit.audit_list.equipment;

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
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.eot_app.R;
import com.eot_app.activitylog.ActivityLogController;
import com.eot_app.nav_menu.audit.audit_list.audit_mvp.model.AuditList_Res;
import com.eot_app.nav_menu.audit.audit_list.equipment.equipadapter.AuditEquipmentAdapter;
import com.eot_app.nav_menu.audit.audit_list.equipment.equipment_mvp.Equipment_PC;
import com.eot_app.nav_menu.audit.audit_list.equipment.equipment_mvp.Equipment_View;
import com.eot_app.nav_menu.audit.audit_list.equipment.model.Equipment_Res;
import com.eot_app.nav_menu.audit.audit_list.equipment.remark.RemarkActivity;
import com.eot_app.nav_menu.audit.nav_scan.EquipmentDetailsActivity;
import com.eot_app.nav_menu.equipment.linkequip.ActivityLinkEquipment;
import com.eot_app.nav_menu.equipment.popupSaveClient.AlertDialogClass;
import com.eot_app.nav_menu.equipment.popupSaveClient.equipmentClinetsave.Equipment_Client_view;
import com.eot_app.nav_menu.jobs.job_detail.job_equipment.EquipmentAttchmentList;
import com.eot_app.nav_menu.jobs.job_detail.job_equipment.add_job_equip.AddJobEquipMentActivity;
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
import java.util.List;
import java.util.concurrent.Callable;

/**
 * Created by Mahendra Dabi on 7/11/19.
 */
public class AuditEquipmentFragment extends Fragment implements Equipment_View, AuditEquipmentAdapter.OnEquipmentSelection, View.OnClickListener, Equipment_Client_view {

    private final int EQUIPMENT_UPDATE_CODE = 141, DETAILSUPDATEFORUSERMANUAL = 142;
    LinearLayout linearFabAdd;
    LinearLayout linearFabclient;
    LinearLayout linearFabown;
    boolean isListLoaded = false;
    boolean isOwn = false;
    EditText edtSearch;
    ImageView imvCross;
    String query;
    private List<Equipment_Res> mylist = new ArrayList<>();
    private RecyclerView recyclerView;
    private View view;
    private AuditEquipmentAdapter adapter;
    private Equipment_PC equipment_pc;
    private LinearLayout nolist_linear;
    private TextView nolist_txt;
    private SwipeRefreshLayout swipeRefreshLayout;
    private FloatingActionButton fab_add_audit;
    private AuditList_Res audit;
    private AlertDialogClass alertDialogClass;
    private String cltId, contrId = "";
    private boolean isFBMenuOpened;
    private View backgroundView;
    private TextView tv_fab_add, tv_fab_client, tv_fab_own;
    private CoordinatorLayout relative_layout;

    public static AuditEquipmentFragment getInstance(String auditID) {
        Bundle bundle = new Bundle();
        bundle.putString("id", auditID);
        AuditEquipmentFragment fragment = new AuditEquipmentFragment();
        fragment.setArguments(bundle);

        return fragment;
    }


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        if (view == null) {
            view = inflater.inflate(R.layout.fragment_audit_equipments_list, container, false);

            relative_layout = view.findViewById(R.id.relative_layout);
            AppUtility.setupUI(relative_layout, getActivity());

            recyclerView = view.findViewById(R.id.audit_equipment_list);
            nolist_txt = view.findViewById(R.id.nolist_txt);
            nolist_linear = view.findViewById(R.id.nolist_linear);
            swipeRefreshLayout = view.findViewById(R.id.swiperefresh);
            fab_add_audit = view.findViewById(R.id.fab_add_audit);
            equipment_pc = new Equipment_PC(this);


            backgroundView = view.findViewById(R.id.backgroundView);


            linearFabAdd = view.findViewById(R.id.linearFabAdd);


            linearFabown = view.findViewById(R.id.linearFabown);
            linearFabclient = view.findViewById(R.id.linearFabclient);
            tv_fab_add = view.findViewById(R.id.tv_fab_add);
            tv_fab_client = view.findViewById(R.id.tv_fab_client);
            tv_fab_own = view.findViewById(R.id.tv_fab_own);

            tv_fab_add.setText(LanguageController.getInstance().getMobileMsgByKey(AppConstant.title_add_equipment));
            tv_fab_client.setText(LanguageController.getInstance().getMobileMsgByKey(AppConstant.link_client_equipment));
            tv_fab_own.setText(LanguageController.getInstance().getMobileMsgByKey(AppConstant.link_own_equipment));


            fab_add_audit.setOnClickListener(this);
            backgroundView.setOnClickListener(this);
            linearFabAdd.setOnClickListener(this);
            linearFabclient.setOnClickListener(this);
            linearFabown.setOnClickListener(this);


            edtSearch = view.findViewById(R.id.edtSearch);
            edtSearch.setHint(LanguageController.getInstance().getMobileMsgByKey(AppConstant.search_by_site));
            imvCross = view.findViewById(R.id.imvCross);

        }

        nolist_txt.setText(LanguageController.getInstance().getMobileMsgByKey(AppConstant.equipment_not_found));

        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        if (adapter == null) {
            adapter = new AuditEquipmentAdapter(getActivity(), new AuditEquipmentAdapter.OnEquipmentSelectionForDetails() {
                @Override
                public void onEquipmentSelectedForDetails(Equipment_Res equipmentRes) {
                    Intent intent = new Intent(getActivity(), EquipmentDetailsActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                    intent.putExtra("audit_equip", equipmentRes);
                    String str = new Gson().toJson(equipmentRes);
                    intent.putExtra("audit_equip_str", str);
                    intent.putExtra("equipment", true);
                    startActivityForResult(intent, DETAILSUPDATEFORUSERMANUAL);
                }
            },
                    new AuditEquipmentAdapter.SelectedpositionForAttchment() {
                        @Override
                        public void selectedAttchpos(int attchpos, int equPos) {

                            try {
                                if (attchpos == 0 || attchpos == 1) {
                                    startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(App_preference.getSharedprefInstance().getBaseURL()
                                            + "" + (mylist.get(equPos).getAttachments().get(attchpos).getAttachFileName()))));

                                } else {
                                    try {
                                        if (mylist.size() > 0) {
                                            String str = new Gson().toJson(mylist.get(equPos).getAttachments());
                                            Intent intent = new Intent(getActivity(), EquipmentAttchmentList.class);
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


        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                if (equipment_pc != null) {
                    recyclerView.setNestedScrollingEnabled(false);
                    equipment_pc.refreshList(getArguments().getString("id"));
                }
            }
        });

        if (equipment_pc != null && adapter != null && adapter.getItemCount() == 0) {
            if (AppUtility.isInternetConnected()) {
                swipeRefreshLayout.setEnabled(true);
                recyclerView.setNestedScrollingEnabled(false);
                equipment_pc.refreshList(getArguments().getString("id"));
            } else
                equipment_pc.getEquipmentList(getArguments().getString("id"));
        }

        ActivityLogController
                .saveActivity(
                        ActivityLogController.AUDIT_MODULE,
                        ActivityLogController.AUDIT_EQUIP,
                        ActivityLogController.AUDIT_MODULE
                );

        if (getArguments().getString("id") != null) {
            audit = AppDataBase.getInMemoryDatabase(EotApp.getAppinstance()).auditDao().getAuditById(getArguments().getString("id"));
            alertDialogClass = new AlertDialogClass(getActivity(), this, "", audit.getAudId(), "");
            cltId = audit.getCltId();
            contrId = audit.getContrId();

            if (TextUtils.isEmpty(contrId))
                contrId = "";
            else if (contrId.equals("0"))
                contrId = "";
        }


        edtSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (edtSearch.getText().toString().equals("")) {
                    equipment_pc.getEquipmentList(getArguments().getString("id"));
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
                String searchText = "%" + query + "%";
                if (query.trim().length() > 0)
                    filter(query);

                //equipment_pc.getEquipmentBySiteName(getArguments().getString("id"), searchText);
            }
        });
        imvCross.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                edtSearch.setText("");
                equipment_pc.getEquipmentList(getArguments().getString("id"));
            }
        });


        return view;
    }

    private void filter(String text) {
        //new array list that will hold the filtered data
        ArrayList<Equipment_Res> filterdNames = new ArrayList<>();

        //looping through existing elements
        for (Equipment_Res s : mylist) {
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
//        edtSearch.setText("");
//        equipment_pc.getEquipmentList(getArguments().getString("id"));
    }

    @Override
    public void setEuqipmentList(List<Equipment_Res> list) {
        if (!mylist.isEmpty())
            mylist.clear();
        this.mylist = list;
        isListLoaded = true;
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

    private void checkLinkedEquipmentType() {
        if (adapter != null && adapter.getList() != null &&
                adapter.getList().size() > 0) {
            isOwn = adapter.getList().get(0).getType().equals("1");
            if (!TextUtils.isEmpty(contrId)) isOwn = false;
        }
    }


    @Override
    public void showErrorAlertDialog(String message) {
        if (swipeRefreshLayout != null) swipeRefreshLayout.setRefreshing(false);
        showDialog(message);
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

    public void refreshListOnEquipmentTabSelection() {
        if (equipment_pc != null && recyclerView != null) {
            recyclerView.setNestedScrollingEnabled(false);
            equipment_pc.refreshList(getArguments().getString("id"));
        }
    }

    private void showDialog(String message) {
        AppUtility.alertDialog(getActivity(), LanguageController.getInstance().getMobileMsgByKey(AppConstant.dialog_error_title), message, LanguageController.getInstance().getMobileMsgByKey(AppConstant.ok), "", new Callable<Boolean>() {
            @Override
            public Boolean call() throws Exception {
                EotApp.getAppinstance().sessionExpired();
                return null;
            }
        });
    }

    @Override
    public void onEquipmentSelected(int position, Equipment_Res equipmentRes) {
        Intent intent = new Intent(getActivity(), RemarkActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
        String strEqu = new Gson().toJson(equipmentRes);
        intent.putExtra("equipment", strEqu);
        startActivityForResult(intent, EQUIPMENT_UPDATE_CODE);
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == EQUIPMENT_UPDATE_CODE || requestCode == DETAILSUPDATEFORUSERMANUAL && resultCode == Activity.RESULT_OK) {
            if (adapter != null) {
                equipment_pc.refreshList(getArguments().getString("id"));
            }
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.fab_add_audit:
                if (cltId.equals("0")) {
                    alertDialogClass.alertDialog();
                } else {
                    showFloatingButtons();
                }
                break;
            case R.id.linearFabAdd:
                if (AppUtility.isInternetConnected()) {

                    Intent intent = new Intent(getActivity(), AddJobEquipMentActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                    intent.putExtra("auditId", getArguments().getString("id"));
                    intent.putExtra("cltId", cltId);
                    startActivityForResult(intent, EQUIPMENT_UPDATE_CODE);

                } else {
                    AppUtility.alertDialog(getActivity(),
                            "", LanguageController.getInstance().getMobileMsgByKey(AppConstant.network_error),
                            LanguageController.getInstance().getMobileMsgByKey(AppConstant.ok), "", new Callable<Boolean>() {
                                @Override
                                public Boolean call() throws Exception {
                                    return null;
                                }
                            });
                }
                closeFABMenu();
                break;
            case R.id.backgroundView:
                closeFABMenu();
                break;

            case R.id.linearFabclient:
                Intent intent = new Intent(getActivity(), ActivityLinkEquipment.class);
                intent.putExtra("type", "2");
                intent.putExtra("cltId", cltId);
                intent.putExtra("id", audit.getAudId());
                intent.putExtra("contrId", contrId);
                startActivityForResult(intent, EQUIPMENT_UPDATE_CODE);
                closeFABMenu();
                break;

            case R.id.linearFabown:
                Intent intent1 = new Intent(getActivity(), ActivityLinkEquipment.class);
                intent1.putExtra("type", "1");
                intent1.putExtra("cltId", "");
                intent1.putExtra("id", audit.getAudId());
                startActivityForResult(intent1, EQUIPMENT_UPDATE_CODE);
                closeFABMenu();
                break;


        }
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
                    ((AppCompatActivity) getActivity()).getSupportActionBar().setBackgroundDrawable(new ColorDrawable(getResources().getColor(R.color.colorPrimary)));

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


    private void showFloatingButtons() {
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
        ((AppCompatActivity) getActivity()).getSupportActionBar().setBackgroundDrawable(new ColorDrawable(getResources().getColor(R.color.bg_color)));
        ((AppCompatActivity) getActivity()).getSupportActionBar().setStackedBackgroundDrawable(new ColorDrawable(Color.parseColor("#90ffffff")));
        backgroundView.setVisibility(View.VISIBLE);
//        if (App_preference.getSharedprefInstance().getLoginRes().getRights().get(0).getIsAuditVisible() == 0)
//            linearFabAdd.setVisibility(View.VISIBLE);
//        else linearFabAdd.setVisibility(View.GONE);

        if (App_preference.getSharedprefInstance().getLoginRes().getCompPermission().get(0).getIsEquipmentAddEnable() != null && App_preference.getSharedprefInstance().getLoginRes().getCompPermission().get(0).getIsEquipmentAddEnable().equals("0")) {
            linearFabAdd.setVisibility(View.VISIBLE);
        } else linearFabAdd.setVisibility(View.GONE);
        linearFabclient.setVisibility(View.VISIBLE);
        linearFabown.setVisibility(View.VISIBLE);
        linearFabAdd.animate().translationY(getResources().getDimension(R.dimen.standard_145));
        linearFabclient.animate().translationY(getResources().getDimension(R.dimen.standard_100));
        linearFabown.animate().translationY(getResources().getDimension(R.dimen.standard_55));
        isFBMenuOpened = true;
    }

    @Override
    public void setClientForSaveUse(EquipmentSaveClientRes equipmentSaveClientRes) {
        cltId = equipmentSaveClientRes.getCltId();
    }


}
