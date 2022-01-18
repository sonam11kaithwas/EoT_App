package com.eot_app.nav_menu.audit.audit_list.scanbarcode;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.fragment.app.Fragment;

import com.budiyev.android.codescanner.CodeScanner;
import com.budiyev.android.codescanner.CodeScannerView;
import com.budiyev.android.codescanner.DecodeCallback;
import com.eot_app.R;
import com.eot_app.nav_menu.audit.audit_list.audit_mvp.model.AuditList_Res;
import com.eot_app.nav_menu.audit.audit_list.equipment.model.Equipment_Res;
import com.eot_app.nav_menu.audit.audit_list.equipment.remark.RemarkActivity;
import com.eot_app.nav_menu.audit.audit_list.scanbarcode.model.ScanBarcodeRequest;
import com.eot_app.nav_menu.audit.audit_list.scanbarcode.scan_mvp.ScanBarcode_PC;
import com.eot_app.nav_menu.audit.audit_list.scanbarcode.scan_mvp.ScanBarcode_View;
import com.eot_app.nav_menu.jobs.job_db.Job;
import com.eot_app.utility.AppConstant;
import com.eot_app.utility.AppUtility;
import com.eot_app.utility.EotApp;
import com.eot_app.utility.language_support.LanguageController;
import com.eot_app.utility.settings.equipmentdb.Equipment;
import com.google.gson.Gson;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.Result;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;

/**
 * Created by Mahendra Dabi on 7/11/19.
 */
public class AuditScanbarcodeFragment extends Fragment implements ScanBarcode_View {
    private CodeScanner mCodeScanner;
    private ScanBarcode_PC scanBarcode_pc;
    private String auditId;
    private AppCompatImageView img_search;
    private EditText edit_barcode;
    private AppCompatTextView tv_scan_label;

    public static AuditScanbarcodeFragment getInstance(String auditID) {
        Bundle bundle = new Bundle();
        bundle.putString("auditId", auditID);
        AuditScanbarcodeFragment fragment = new AuditScanbarcodeFragment();
        fragment.setArguments(bundle);

        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_audit_scan_code, container, false);

        scanBarcode_pc = new ScanBarcode_PC(this);

        Bundle bundle = getArguments();
        if (bundle.getString("auditId") != null) {
            auditId = bundle.getString("auditId");
        }

        final CodeScannerView scannerView = view.findViewById(R.id.scanner_view);
        mCodeScanner = new CodeScanner(getActivity(), scannerView);
        List<BarcodeFormat> list = new ArrayList<>();
        list.add(BarcodeFormat.CODE_128);
        list.add(BarcodeFormat.UPC_A);
        list.add(BarcodeFormat.UPC_E);
        list.add(BarcodeFormat.EAN_13);
        list.add(BarcodeFormat.CODE_39);
        list.add(BarcodeFormat.CODABAR);
        mCodeScanner.setFormats(list);
        mCodeScanner.setAutoFocusEnabled(true);
        img_search = view.findViewById(R.id.img_search);
        edit_barcode = view.findViewById(R.id.edit_barcode);
        tv_scan_label = view.findViewById(R.id.tv_scan_label);

        tv_scan_label.setText(LanguageController.getInstance().getMobileMsgByKey(AppConstant.scan_barcode_manually));

        mCodeScanner.setDecodeCallback(new DecodeCallback() {
            @Override
            public void onDecoded(@NonNull final Result result) {
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        searchEquipment(result.getText());

                    }
                });
            }
        });
        scannerView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startScanner();
            }
        });


        img_search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!TextUtils.isEmpty(edit_barcode.getText().toString())) {
                    AppUtility.hideSoftKeyboard(getActivity());
                    searchEquipment(edit_barcode.getText().toString());
                }

            }
        });


        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        startScanner();

    }

    @Override
    public void onPause() {
        mCodeScanner.releaseResources();
        super.onPause();
    }

    private void startScanner() {
        if (AppUtility.askCameraTakePicture(getActivity())) {
            mCodeScanner.startPreview();
        }
    }

    @Override
    public void onEquipmentFound(Equipment_Res equipmentRes) {
        if (equipmentRes != null) {
            String strEqu = new Gson().toJson(equipmentRes);

            startActivity(new Intent(getActivity(), RemarkActivity.class)
                    .putExtra("equipment", strEqu));
        } else {
            AppUtility.alertDialog(getActivity(), LanguageController.getInstance().getMobileMsgByKey(AppConstant.equipment), LanguageController.getInstance().getMobileMsgByKey(AppConstant.audit_equipment_not_found), LanguageController.getInstance().getMobileMsgByKey(AppConstant.ok), "", new Callable<Boolean>() {
                @Override
                public Boolean call() throws Exception {
                    return null;
                }
            });
        }
    }

    @Override
    public void onEquipmentFoundButNotLinked(Equipment equipment) {

    }

    @Override
    public void onRecordFound(List<Job> jobList, List<AuditList_Res> list) {


    }

    @Override
    public void onSessionExpired(String msg) {
        showDialog(msg);
    }

    private void showDialog(String msg) {
        AppUtility.alertDialog(getActivity(), LanguageController.getInstance().getMobileMsgByKey(AppConstant.dialog_error_title), msg, LanguageController.getInstance().getMobileMsgByKey(AppConstant.ok), "", new Callable<Boolean>() {
            @Override
            public Boolean call() throws Exception {
                EotApp.getAppinstance().sessionExpired();
                return null;
            }
        });
    }


    /*create search request*/
    private void searchEquipment(String barcode) {
        ScanBarcodeRequest request = new ScanBarcodeRequest();
        /*barcode search Param*/
        request.setAudId(auditId);
        request.setBarCode(barcode);
        scanBarcode_pc.searchAuditWithBarcode(request);
    }
}
