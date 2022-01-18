package com.eot_app.nav_menu.equipment.link_own_client_equ_barc;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.appcompat.widget.AppCompatTextView;

import com.budiyev.android.codescanner.CodeScanner;
import com.budiyev.android.codescanner.CodeScannerView;
import com.budiyev.android.codescanner.DecodeCallback;
import com.eot_app.R;
import com.eot_app.nav_menu.audit.audit_list.scanbarcode.model.ScanBarcodeRequest;
import com.eot_app.nav_menu.equipment.link_own_client_equ_barc.mvp_scanbar.ScanEquPc;
import com.eot_app.nav_menu.equipment.link_own_client_equ_barc.mvp_scanbar.ScanEquView;
import com.eot_app.nav_menu.jobs.job_db.EquArrayModel;
import com.eot_app.utility.AppConstant;
import com.eot_app.utility.AppUtility;
import com.eot_app.utility.EotApp;
import com.eot_app.utility.language_support.LanguageController;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.Result;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;

public class JobEquipmentScanActivity extends AppCompatActivity implements ScanEquView {
    //   public static boolean SUCCESS = false;
    List<EquArrayModel> myEquList = new ArrayList<>();
    String myEqu = "";
    private CodeScanner mCodeScanner;
    private ScanEquPc scanBarcode_pc;
    private AppCompatImageView img_search;
    private EditText edit_barcode;
    private AppCompatTextView tv_scan_label;
    private String jobId = "";
    private String strstatus = "";
    private String type = "", cltId = "", contrId = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_job_equipment_scan);
        setTitle(LanguageController.getInstance().getMobileMsgByKey(AppConstant.detail_scan_barcode));


        scanBarcode_pc = new ScanEquPc(this);

        try {
            if (getIntent().hasExtra("JOBID")) {
                jobId = getIntent().getStringExtra("JOBID");
                type = getIntent().getStringExtra("type");
                cltId = getIntent().getStringExtra("cltId");
                contrId = getIntent().getStringExtra("contrId");
                myEqu = getIntent().getStringExtra("myEquList");
                Type listType = new TypeToken<List<EquArrayModel>>() {
                }.getType();
                myEquList = new Gson().fromJson(myEqu, listType);
                strstatus = getIntent().getStringExtra("strstatus");
            }

        } catch (Exception exception) {
            exception.getMessage();
        }

        initializeViews();
    }

    public String getJobId() {
        return jobId;
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

    private void initializeViews() {

        final CodeScannerView scannerView = findViewById(R.id.scanner_view);
        mCodeScanner = new CodeScanner(this, scannerView);
        List<BarcodeFormat> list = new ArrayList<>();
        list.add(BarcodeFormat.CODE_128);
        list.add(BarcodeFormat.UPC_A);
        list.add(BarcodeFormat.UPC_E);
        list.add(BarcodeFormat.EAN_13);
        list.add(BarcodeFormat.CODE_39);
        list.add(BarcodeFormat.CODABAR);
        mCodeScanner.setFormats(list);
        mCodeScanner.setAutoFocusEnabled(true);
        img_search = findViewById(R.id.img_search);
        edit_barcode = findViewById(R.id.edit_barcode);
        tv_scan_label = findViewById(R.id.tv_scan_label);

        tv_scan_label.setText(LanguageController.getInstance().getMobileMsgByKey(AppConstant.scan_barcode_manually));

        mCodeScanner.setDecodeCallback(new DecodeCallback() {
            @Override
            public void onDecoded(@NonNull final Result result) {
                JobEquipmentScanActivity.this.runOnUiThread(new Runnable() {
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
                    AppUtility.hideSoftKeyboard(JobEquipmentScanActivity.this);
                    searchEquipment(edit_barcode.getText().toString());
                }

            }
        });


    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == android.R.id.home)
            onBackPressed();
        return true;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public void onBackPressed() {
        this.finish();
    }

    private void startScanner() {
        if (AppUtility.askCameraTakePicture(this)) {
            mCodeScanner.startPreview();
        }
    }

    @Override
    public void onJobEquipmentFound(EquArrayModel equipmentRes) {
        if (equipmentRes != null) {
            String str = new Gson().toJson(equipmentRes);
            Intent intent = new Intent();
            intent.putExtra("str", str);
            setResult(RESULT_OK, intent);
            finish();
        } else {
            finish();

        }
    }

    /*create search request*/
    private void searchEquipment(String barcode) {
        ScanBarcodeRequest request = new ScanBarcodeRequest();
        /*barcode search Param*/
        request.setAudId(jobId);
        request.setBarCode(barcode);
        scanBarcode_pc.searchJobWithBarcode(request, myEquList);
    }

    @Override
    public void onSessionExpired(String msg) {
        showDialog(msg);
    }

    private void showDialog(String msg) {
        AppUtility.alertDialog(this, LanguageController.getInstance().getMobileMsgByKey(AppConstant.dialog_error_title), msg, LanguageController.getInstance().getMobileMsgByKey(AppConstant.ok), "", new Callable<Boolean>() {
            @Override
            public Boolean call() throws Exception {
                EotApp.getAppinstance().sessionExpired();
                return null;
            }
        });
    }
}