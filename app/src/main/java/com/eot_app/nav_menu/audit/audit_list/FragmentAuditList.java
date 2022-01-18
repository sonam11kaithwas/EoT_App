package com.eot_app.nav_menu.audit.audit_list;

import android.content.Intent;
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
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.eot_app.R;
import com.eot_app.home_screens.MainActivity;
import com.eot_app.nav_menu.audit.audit_list.audit_mvp.AuditList_PC;
import com.eot_app.nav_menu.audit.audit_list.audit_mvp.AuditList_PI;
import com.eot_app.nav_menu.audit.audit_list.audit_mvp.AuditList_View;
import com.eot_app.nav_menu.audit.audit_list.audit_mvp.model.AuditList_Res;
import com.eot_app.nav_menu.audit.audit_list.equipment.model.Equipment_Res;
import com.eot_app.nav_menu.audit.audit_list.equipment.remark.RemarkActivity;
import com.eot_app.nav_menu.audit.audit_list.scanbarcode.model.ScanBarcodeRequest;
import com.eot_app.nav_menu.audit.audit_list.scanbarcode.scan_mvp.ScanBarcode_PC;
import com.eot_app.nav_menu.audit.audit_list.scanbarcode.scan_mvp.ScanBarcode_View;
import com.eot_app.nav_menu.jobs.job_db.Job;
import com.eot_app.utility.AppConstant;
import com.eot_app.utility.AppUtility;
import com.eot_app.utility.App_preference;
import com.eot_app.utility.EotApp;
import com.eot_app.utility.db.AppDataBase;
import com.eot_app.utility.language_support.LanguageController;
import com.eot_app.utility.settings.equipmentdb.Equipment;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.Callable;

/**
 * Created by Mahendra Dabi on 6/11/19.
 */
public class FragmentAuditList extends Fragment implements AuditList_View, AdapterAuditList.OnAuditSelection,
        ScanBarcode_View {

    private static final int AUDIT_DETAILS_CODE = 12;
    private final AuditList_PI auditList_pi = new AuditList_PC(this);
    View view;
    ScanBarcode_PC scanBarcode_pc = new ScanBarcode_PC(this);
    LinearLayoutManager layoutManager;
    //views variable
    private RecyclerView auditList;
    private AdapterAuditList adapterAuditList;
    private LinearLayout nolist_linear;
    private TextView nolist_txt;
    private ImageView search_btn;
    private SwipeRefreshLayout swiperefresh;
    private LinearLayout audit_search_view, filter_parent;
    private EditText edtSearch;
    private boolean isScanSearchActive;
    private String barcode;
    TextWatcher textWatcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {


        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            if (s.length() > 2) {
                if (auditList_pi != null) {
                    auditList_pi.getAuditListBySearch(s.toString());
                } else if (scanBarcode_pc != null) {
                    ScanBarcodeRequest request = new ScanBarcodeRequest();
                    request.setBarCode(barcode);
                    request.setAudId("");
                    request.setSearch(edtSearch.getText().toString());
                    scanBarcode_pc.searchAuditWithBarcode(request);
                }
            } else {
                if (auditList_pi != null) {
                    auditList_pi.getAuditList();
                }
            }
        }

        @Override
        public void afterTextChanged(Editable s) {

        }
    };


    public static FragmentAuditList newInstance(List<AuditList_Res> list, boolean isSearch, String barocde) {
        Bundle bundle = new Bundle();
        bundle.putBoolean("issearch", isSearch);
        bundle.putString("code", barocde);
        String str = new Gson().toJson(list);
        bundle.putString("data", str);
        FragmentAuditList fragmentAuditList = new FragmentAuditList();
        fragmentAuditList.setArguments(bundle);
        return fragmentAuditList;
    }

    public static FragmentAuditList newInstance(boolean istrue) {
        FragmentAuditList fragment = new FragmentAuditList();
        Bundle args = new Bundle();
        args.putBoolean("istrue", istrue);
        fragment.setArguments(args);
        return fragment;
    }


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if (view == null) {
            view = LayoutInflater.from(container.getContext()).inflate(R.layout.fragment_audit_list, container, false);
            intializeViews(view);
        }

        Bundle bundle = getArguments();

        if (bundle != null) {
            if (bundle.getBoolean("issearch")) {
                if (bundle.get("data") != null) {
                    barcode = bundle.getString("code");
                    isScanSearchActive = true;
                    try {
                        List<AuditList_Res> auditLists = new ArrayList<>();
                        Type listType = new TypeToken<List<AuditList_Res>>() {
                        }.getType();
                        String str = getArguments().getString("data");
                        auditLists = new Gson().fromJson(str, listType);
                        setAuditList(auditLists);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    //  setAuditList((List<AuditList_Res>) bundle.get("data"));
                }
            } else if (bundle.getBoolean("istrue")) {
                auditList_pi.getAudit();
            }
        } else {

            auditList_pi.getAudit();
        }


        search_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AppUtility.hideSoftKeyboard(getActivity());
                if (auditList_pi != null)
                    auditList_pi.getAuditListBySearch(edtSearch.getText().toString());
                else if (scanBarcode_pc != null) {
                    ScanBarcodeRequest request = new ScanBarcodeRequest();
                    request.setBarCode(barcode);
                    request.setAudId("");
                    request.setSearch(edtSearch.getText().toString());
                    scanBarcode_pc.searchAuditWithBarcode(request);
                }
            }
        });
        swiperefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                edtSearch.removeTextChangedListener(textWatcher);
                edtSearch.setText("");
                edtSearch.addTextChangedListener(textWatcher);
                if (auditList_pi != null) {

                    Bundle bundle = getArguments();

                    if (bundle != null) {
                        if (bundle.getBoolean("issearch")) {
                            if (bundle.get("data") != null) {
                                // scanBarcode_pc = new ScanBarcode_PC(this);
                                barcode = bundle.getString("code");
                                isScanSearchActive = true;
                                setAuditList((List<AuditList_Res>) bundle.get("data"));
                            }
                        } else if (bundle.getBoolean("istrue")) {
                            auditList_pi.getAudit();
                        }
                    }
                    //auditList_pi.getAudit();
                } else if (scanBarcode_pc != null) {
                    ScanBarcodeRequest request = new ScanBarcodeRequest();
                    request.setBarCode(barcode);
                    request.setAudId("");
                    request.setSearch(edtSearch.getText().toString());
                    swiperefresh.setRefreshing(false);

                }
            }
        });

        return view;
    }

    @Override
    public void disableSwipeRefresh() {
        if (swiperefresh != null) {
            if (swiperefresh.isRefreshing()) {
                swiperefresh.setRefreshing(false);
            }
        }
    }


    public void showSiteName(boolean isShow) {
        if (adapterAuditList != null) {
            adapterAuditList.setFromShowSiteName(isShow);
            adapterAuditList.notifyDataSetChanged();
        }

    }

    @Override
    public void updateFromApiObserver() {
        if (auditList_pi != null) {
            auditList_pi.getAudit();
        }
    }

    @Override
    public void refreshList() {
        if (auditList_pi != null) {
            auditList_pi.getAudit();
        }
    }

    @Override
    public void onNotificationRedirect() {
        if (getActivity() instanceof MainActivity) {
            String notificationDataId = ((MainActivity) getActivity()).getNotificationDataId();
            ((MainActivity) getActivity()).setNotificationDataId(null);
            if (!TextUtils.isEmpty(notificationDataId)) {
                AuditList_Res audit = AppDataBase.getInMemoryDatabase(EotApp.getAppinstance())
                        .auditDao().getAuditById(notificationDataId);
                if (audit != null) {
                    Intent intent = new Intent(getActivity(), AuditDetails.class);
                    intent.putExtra("audit", audit);
                    intent.putExtra("position", -1);
                    String str = new Gson().toJson(audit);
                    intent.putExtra("auditstr", str);
                    intent.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                    startActivityForResult(intent, AUDIT_DETAILS_CODE);
                }
            }
        }
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
    }

    private void intializeViews(View view) {

        swiperefresh = view.findViewById(R.id.swiperefresh);

        auditList = view.findViewById(R.id.audit_list);


        nolist_txt = view.findViewById(R.id.nolist_txt);
        nolist_linear = view.findViewById(R.id.nolist_linear);

        audit_search_view = view.findViewById(R.id.filter_parent);
        edtSearch = view.findViewById(R.id.edtSearch);
        edtSearch.setHint(LanguageController.getInstance().getMobileMsgByKey(AppConstant.search_quote_code_client_name_address));
        search_btn = view.findViewById(R.id.search_btn);

        edtSearch.setHint(LanguageController.getInstance().getMobileMsgByKey(AppConstant.audit_search_hint));

        nolist_txt.setText(LanguageController.getInstance().getMobileMsgByKey(AppConstant.audit_not_found));

        edtSearch.addTextChangedListener(textWatcher);


        layoutManager = new LinearLayoutManager(getActivity());
        auditList.setLayoutManager(layoutManager);

        adapterAuditList = new AdapterAuditList(getActivity());
        adapterAuditList.setFromShowSiteName(App_preference.getSharedprefInstance().getSiteNameShowInSetting());
        adapterAuditList.setOnAuditSelection(this);
        adapterAuditList.setList(Collections.<AuditList_Res>emptyList());
        auditList.setAdapter(adapterAuditList);


    }


    @Override
    public void setSearchVisibility(boolean b) {
        if (audit_search_view != null) {
            audit_search_view.setVisibility(b ? View.VISIBLE : View.GONE);
        }

    }

    @Override
    public void showErrorAlertDialog(String message) {
        showDialog(message);
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
    public void setAuditList(List<AuditList_Res> data) {

        disableSwipeRefresh();

        try {
            for (AuditList_Res dataList : data) {
                if (dataList.getStatus() != null && !(AppUtility.auditStatusList().contains(dataList.getStatus()))) {
                    data.remove(dataList);
                }
            }
        } catch (Exception exception) {
            exception.printStackTrace();
            adapterAuditList.setList(data);
        }

        adapterAuditList.setList(data);
        if (adapterAuditList.getItemCount() == 0)
            nolist_linear.setVisibility(View.VISIBLE);
        else nolist_linear.setVisibility(View.GONE);


    }

    @Override
    public void setRefereshPullOff() {
        if (swiperefresh != null)
            swiperefresh.setRefreshing(false);
    }

    /*scan barcode view*/
    @Override
    public void onEquipmentFound(Equipment_Res equipmentRes) {
        if (equipmentRes != null) {
            String strEqu = new Gson().toJson(equipmentRes);
            Intent intent = new Intent(getActivity(), RemarkActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
            startActivity(intent.putExtra("equipment", strEqu));
        }
    }

    @Override
    public void onEquipmentFoundButNotLinked(Equipment equipment) {

    }


    /*scan barcode view */

    @Override
    public void onRecordFound(List<Job> jobList, List<AuditList_Res> list) {
        setAuditList(list);
    }

    @Override
    public void onSessionExpired(String message) {
        AppUtility.alertDialog(getActivity(), LanguageController.getInstance().getMobileMsgByKey(AppConstant.dialog_error_title), message, LanguageController.getInstance().getMobileMsgByKey(AppConstant.ok), "", new Callable<Boolean>() {
            @Override
            public Boolean call() throws Exception {
                EotApp.getAppinstance().sessionExpired();
                return null;
            }
        });
    }

    @Override
    public void onAuditSelected(AuditList_Res audit, int position) {
        if (TextUtils.isEmpty(barcode)) {
            Intent intent = new Intent(getActivity(), AuditDetails.class);
            intent.putExtra("audit", audit);
            intent.putExtra("position", position);
            String str = new Gson().toJson(audit);
            intent.putExtra("auditstr", str);
            intent.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
            startActivityForResult(intent, AUDIT_DETAILS_CODE);
        } else {
            if (audit != null && scanBarcode_pc != null) {
                ScanBarcodeRequest request = new ScanBarcodeRequest();
                request.setAudId(audit.getAudId());
                request.setBarCode(barcode);
                scanBarcode_pc.searchAuditWithBarcode(request);
            }
        }
    }

    @Override
    public void onLoadMore(boolean b) {
        if (b) {
            if (auditList_pi != null) {
            }
            // auditList_pi.loadMoreItems();
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == AUDIT_DETAILS_CODE) {
            if (data != null && data.hasExtra("position")) {
                /*
                 * change audit list locally with current changed status of audit at position
                 * and refreshing  other one audit  in progress status to on hold
                 * */
                if (adapterAuditList != null) {
                    int status = data.getIntExtra("status", -1);
                    int pos = data.getIntExtra("position", -1);
                    if (pos > -1 && status > 0) {
                        adapterAuditList.updateAuditStatus(pos, status);
                    } else if (pos == -1) {
                        auditList_pi.getAudit();
                    }

                }
            } else {
                if (adapterAuditList != null && auditList_pi != null) {
                    auditList_pi.getAudit();
                }
            }
        }

    }


}
