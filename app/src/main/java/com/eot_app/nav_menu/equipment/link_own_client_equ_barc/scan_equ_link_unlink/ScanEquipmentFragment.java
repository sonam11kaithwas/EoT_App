package com.eot_app.nav_menu.equipment.link_own_client_equ_barc.scan_equ_link_unlink;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.appcompat.widget.AppCompatTextView;
import androidx.core.widget.ContentLoadingProgressBar;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.eot_app.R;
import com.eot_app.nav_menu.audit.audit_list.equipment.model.EquipmentStatus;
import com.eot_app.nav_menu.equipment.link_own_client_equ_barc.JobEquipmentScanActivity;
import com.eot_app.nav_menu.equipment.linkequip.EquipmentLinkAdapter;
import com.eot_app.nav_menu.jobs.job_db.EquArrayModel;
import com.eot_app.services.ApiClient;
import com.eot_app.services.Service_apis;
import com.eot_app.utility.AppConstant;
import com.eot_app.utility.AppUtility;
import com.eot_app.utility.EotApp;
import com.eot_app.utility.language_support.LanguageController;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.Callable;

import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ScanEquipmentFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ScanEquipmentFragment extends Fragment implements EquipmentLinkAdapter.OnEquipmentSelection {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private final List<EquArrayModel> tempList = new ArrayList<>();
    private final String cltId = "";
    private final String contrId = "";
    //copy contrac id for client and contract equipment option select and change it later use
    private final String tempContrId = "";
    private final boolean contractMsg = true;
    private final boolean BARCODESCANEQUIPM = false;
    EquipmentLinkAdapter adapter;
    RecyclerView recyclerView;
    List<EquipmentStatus> statusList = new ArrayList<>();

    ContentLoadingProgressBar content_loading_progress;
    LinearLayout nolist_linear;
    TextView nolist_txt;
    AppCompatTextView tv_equipment_size;
    Spinner spinner_filter;
    AppCompatTextView tv_filter_name, tv_contract_msg;
    List<EquArrayModel> myEquList = new ArrayList<>();
    private String type;
    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public ScanEquipmentFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment ScanEquipmentFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static ScanEquipmentFragment newInstance(String param1, String param2) {
        ScanEquipmentFragment fragment = new ScanEquipmentFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this

        View view = inflater.inflate(R.layout.fragment_scan_equipment, container, false);
        getActivity().setTitle(LanguageController.getInstance().getMobileMsgByKey(AppConstant.link_equipment));

        findMyViews(view);
        return view;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        return true;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
    }

    private void findMyViews(View view) {
        nolist_linear = view.findViewById(R.id.nolist_linear);
        nolist_txt = view.findViewById(R.id.nolist_txt);
        nolist_txt.setText(LanguageController.getInstance().getMobileMsgByKey(AppConstant.equipment_not_found));

        recyclerView = view.findViewById(R.id.audit_equipment_list);
        recyclerView.setHasFixedSize(false);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        try {
            EquArrayModel equArrayModel = new Gson().fromJson(mParam1, EquArrayModel.class);
            myEquList.add(equArrayModel);
            Type statuslistType = new TypeToken<List<EquipmentStatus>>() {
            }.getType();
            statusList = new Gson().fromJson(mParam2, statuslistType);
        } catch (Exception e) {

        }

        adapter = new EquipmentLinkAdapter(getActivity());
        adapter.setList(myEquList);
        adapter.setEquipmentStatus(statusList);
        adapter.setOnEquipmentSelection(this);
        recyclerView.setAdapter(adapter);
    }

    @Override
    public void onEquipmentSelected(List<String> linkedEquipment) {
        try {
            addJobEqi(linkedEquipment, ((JobEquipmentScanActivity) getActivity()).getJobId(), "");
        } catch (Exception e) {
            e.getMessage();
        }
    }


    private void success() {
        //  JobEquipmentScanActivity.SUCCESS = true;
        getActivity().onBackPressed();
    }

    private void addJobEqi(List<String> equId, String audId, String contrId) {
        if (AppUtility.isInternetConnected()) {
            AppUtility.progressBarShow(getActivity());
            HashMap hashMap = new HashMap();
            hashMap.put("equId", equId);
            hashMap.put("audId", audId);
            hashMap.put("contrId", contrId);

            String data = new Gson().toJson(hashMap);

            ApiClient.getservices().eotServiceCall(
                    Service_apis.addAuditEquipment,
                    AppUtility.getApiHeaders(),
                    AppUtility.getJsonObject(data)
            ).subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new Observer<JsonObject>() {
                        @Override
                        public void onSubscribe(@NonNull Disposable d) {

                        }

                        @Override
                        public void onNext(@NonNull JsonObject jsonObject) {
                            if (jsonObject.get("success").getAsBoolean()) {
                                success();
                            } else if (jsonObject.get("statusCode") != null && jsonObject.get("statusCode").getAsString().equals(AppConstant.SESSION_EXPIRE)) {
                                showDialog(LanguageController.getInstance().getServerMsgByKey(jsonObject.get("message").getAsString()));
                            } else {
                                success();
                            }
                        }

                        @Override
                        public void onError(@NonNull Throwable e) {
                            System.out.println();
                            AppUtility.progressBarDissMiss();
                            EotApp.getAppinstance().showToastmsg(e.getMessage());
                        }

                        @Override
                        public void onComplete() {
                            AppUtility.progressBarDissMiss();
                        }
                    });

        } else
            EotApp.getAppinstance().showToastmsg(LanguageController.getInstance().getMobileMsgByKey(AppConstant.network_error));
    }

    private void showDialog(String msg) {
        AppUtility.alertDialog(getActivity(), LanguageController.getInstance().getMobileMsgByKey(AppConstant.dialog_error_title),
                msg, LanguageController.getInstance().getMobileMsgByKey(AppConstant.ok), "", new Callable<Boolean>() {
                    @Override
                    public Boolean call() throws Exception {
                        EotApp.getAppinstance().sessionExpired();
                        return null;
                    }
                });
    }

    @Override
    public void onListSizeChange(int size) {
        if (size == 0) {
            nolist_linear.setVisibility(View.VISIBLE);
        } else {
            nolist_linear.setVisibility(View.GONE);
        }
    }

}