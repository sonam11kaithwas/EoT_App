package com.eot_app.nav_menu.audit.nav_scan;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.appcompat.widget.AppCompatTextView;

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
import com.eot_app.nav_menu.jobs.job_db.EquArrayModel;
import com.eot_app.nav_menu.jobs.job_db.Job;
import com.eot_app.nav_menu.jobs.job_detail.job_equipment.job_equ_remrk.JobEquRemarkRemarkActivity;
import com.eot_app.utility.AppConstant;
import com.eot_app.utility.AppUtility;
import com.eot_app.utility.EotApp;
import com.eot_app.utility.language_support.LanguageController;
import com.eot_app.utility.settings.equipmentdb.Equipment;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.Result;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;

/**
 * Created by Mahendra Dabi on 8/11/19.
 */
public class BarcodeScanActivity extends AppCompatActivity implements ScanBarcode_View {
    private CodeScanner mCodeScanner;

    private ScanBarcode_PC scanBarcode_pc;
    private AppCompatImageView img_search;
    private EditText edit_barcode;
    private AppCompatTextView tv_scan_label;
    // private boolean LINKACTIVITY = false;
    // private String jobId = "";
    private String codeText;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_barcode_scanner);

        scanBarcode_pc = new ScanBarcode_PC(this, true);

        hideActionBar(true);

//        Bundle bundle = getIntent().getExtras();
//        if (bundle != null) {
//            /****Add item***/
//            if (getIntent().hasExtra("LINKACTIVITY")) {
//                LINKACTIVITY = bundle.getBoolean("LINKACTIVITY");
//                jobId = bundle.getString("jobId");
//            }
//        }

        CodeScannerView scannerView = findViewById(R.id.scanner_view);

        img_search = findViewById(R.id.img_search);
        edit_barcode = findViewById(R.id.edit_barcode);
        tv_scan_label = findViewById(R.id.tv_scan_label);
        tv_scan_label.setText(LanguageController.getInstance().getMobileMsgByKey(AppConstant.scan_barcode_manually));

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
        mCodeScanner.setDecodeCallback(new DecodeCallback() {
            @Override
            public void onDecoded(@NonNull final Result result) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        codeText = result.getText();
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
                    AppUtility.hideSoftKeyboard(BarcodeScanActivity.this);

                    codeText = edit_barcode.getText().toString();
                    searchEquipment(edit_barcode.getText().toString());
                }

            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();
        startScanner();

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

    @Override
    public void onPause() {
        mCodeScanner.releaseResources();
        super.onPause();
    }

    private void startScanner() {
        if (AppUtility.askCameraTakePicture(this)) {
            mCodeScanner.startPreview();
        }
    }

    /*hide show actionbar*/
    private void hideActionBar(boolean hide) {
        Window window = getWindow();
        if (hide) {
            getSupportActionBar().hide();
            window.setStatusBarColor(Color.BLACK);
        } else {
            getSupportActionBar().show();
            window.setStatusBarColor(getResources().getColor(R.color.colorPrimaryDark));
        }
    }

    @Override
    public void onEquipmentFound(Equipment_Res equipmentRes) {
        if (equipmentRes != null) {
            String strEqu = new Gson().toJson(equipmentRes);
            startActivity(new Intent(this, RemarkActivity.class)
                    .putExtra("equipment", strEqu));
        }
    }

    @Override
    public void onEquipmentFoundButNotLinked(Equipment equipment) {
        if (equipment == null) {
            try {
                TextView dailog_title, dialog_msg;
                final AlertDialog.Builder alertDialog = new AlertDialog.Builder(this);

                LayoutInflater inflater = getLayoutInflater();// (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                final View customLayout = inflater.inflate(R.layout.dialog_layout, null);
                alertDialog.setView(customLayout);
                alertDialog.setCancelable(false);

                dailog_title = customLayout.findViewById(R.id.dai_title);
                dialog_msg = customLayout.findViewById(R.id.dia_msg);
                dailog_title.setVisibility(View.VISIBLE);
                dailog_title.setText(LanguageController.getInstance().getMobileMsgByKey(AppConstant.equipment));

                dialog_msg.setText(LanguageController.getInstance().getMobileMsgByKey(AppConstant.equ_not_found));

                alertDialog.setPositiveButton(LanguageController.getInstance().getMobileMsgByKey(AppConstant.ok), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        try {

                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                });

                alertDialog.setNegativeButton(LanguageController.getInstance().getMobileMsgByKey(AppConstant.equipment_sync_button_text), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // dialog.dismiss();
                        scanBarcode_pc.syncEquipments();
                    }
                });
                AlertDialog alertDialog1 = alertDialog.create();
                alertDialog1.show();
                alertDialog1.getButton(AlertDialog.BUTTON_NEGATIVE).setAllCaps(false);

            } catch (Exception e) {
                e.printStackTrace();
                EotApp.getAppinstance().showToastmsg("Something went wrong here.");
            }

        } else {
            Intent intent = new Intent(this, EquipmentDetailsActivity.class);
            intent.putExtra("equipment", true);
            intent.putExtra("equipment_id", equipment.getEquId());
            startActivity(intent);
        }


    }

    @Override
    public void onRecordFound(List<Job> jobList, List<AuditList_Res> list) {

        if (jobList.size() == 0 && list.size() == 0) {

        } else {
            Intent intent = new Intent(this, EquipmentDetailsActivity.class);
            String jobData = new Gson().toJson(jobList);
            String auditData = new Gson().toJson(list);
            intent.putExtra("JOBDATA", jobData);
            intent.putExtra("AUDITDATA", auditData);
            intent.putExtra("codetext", codeText);
            startActivityForResult(intent, 100);
        }

        //    finish();
    }

    /*create search request*/
    private void searchEquipment(String barcode) {
        ScanBarcodeRequest request = new ScanBarcodeRequest();
        /*barcode search Param*/
        request.setAudId("");
        request.setBarCode(barcode);
        scanBarcode_pc.searchEquipmentinAudit(request);
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 100 && resultCode == RESULT_OK) {
            if (data != null)
                //  if (data.hasExtra("data"))
                if (data.hasExtra("AUDITDATA"))
                    handleAuditIntent(data);
                    //   else if (data.hasExtra("jobdata"))
                else if (data.hasExtra("JOBDATA"))
                    handleJobIntent(data);
        }
    }

    private void handleJobIntent(Intent data) {
        if (data.hasExtra("JOBDATA")) {
            // List<Job> list = (List<Job>) data.getExtras().get("jobdata");
            List<Job> list = new ArrayList<>();
            String str = "";
            try {
                str = data.getExtras().getString("JOBDATA");
                Type listType = new TypeToken<List<Job>>() {
                }.getType();
                list = new Gson().fromJson(str, listType);
            } catch (Exception exception) {
                exception.printStackTrace();
            }
            if (list != null && list.size() == 1) {
                List<EquArrayModel> equArray = list.get(0).getEquArray();
                if (equArray != null) {
                    for (EquArrayModel equipment : equArray) {
                        if (equipment.getSno() != null && equipment.getSno().equals(codeText) ||
                                equipment.getBarcode() != null && equipment.getBarcode().equals(codeText)) {

                            Intent intent = new Intent(this, JobEquRemarkRemarkActivity.class);
                            String strEqu = new Gson().toJson(equipment);
                            intent.putExtra("equipment", strEqu);
                            intent.putExtra("jobId", list.get(0).getJobId());
                            intent.putExtra("JOBDATA", str);
                            startActivity(intent);
                            finish();
                        }
                    }

                }
            } else {
                Intent intent = new Intent();
                intent.putExtra("code", codeText);
                intent.putExtra("JOBDATA", str);
                setResult(RESULT_OK, intent);
                finish();
            }
        }
    }

    private void handleAuditIntent(Intent data) {
        if (data.hasExtra("AUDITDATA")) {
            // List<AuditList_Res> list = (List<AuditList_Res>) data.getExtras().get("data");
            List<AuditList_Res> list = new ArrayList<>();
            String str = "";
            try {
                str = data.getExtras().getString("AUDITDATA");
                Type listType = new TypeToken<List<AuditList_Res>>() {
                }.getType();
                list = new Gson().fromJson(str, listType);
            } catch (Exception exception) {
                exception.printStackTrace();
            }

            if (list != null && list.size() == 1) {
                List<Equipment_Res> equArray = list.get(0).getEquArray();
                if (equArray != null) {
                    for (Equipment_Res equipment : equArray) {
                        if (equipment.getSno() != null && equipment.getSno().equals(codeText) ||
                                equipment.getBarcode() != null && equipment.getBarcode().equals(codeText)) {
                            String strEqu = new Gson().toJson(equipment);
                            startActivity(new Intent(this, RemarkActivity.class)
                                    .putExtra("equipment", strEqu)
                                    .putExtra("AUDITDATA", str
                                    ));
                            finish();
                        }
                    }

                }
            } else {
                Intent intent = new Intent();
                intent.putExtra("code", codeText);
                intent.putExtra("AUDITDATA", str);
                setResult(RESULT_OK, intent);
                finish();
            }
        }
    }
}
