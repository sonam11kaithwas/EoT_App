package com.eot_app.nav_menu.audit.audit_list.report;

import android.content.Context;
import android.content.ContextWrapper;
import android.content.res.Resources;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.fragment.app.Fragment;

import com.eot_app.R;
import com.eot_app.nav_menu.audit.audit_list.AuditDetails;
import com.eot_app.nav_menu.audit.audit_list.report.mode.ReportRequest;
import com.eot_app.nav_menu.audit.audit_list.report.report_mvp.Report_PC;
import com.eot_app.nav_menu.audit.audit_list.report.report_mvp.Report_View;
import com.eot_app.utility.AppConstant;
import com.eot_app.utility.AppUtility;
import com.eot_app.utility.App_preference;
import com.eot_app.utility.EotApp;
import com.eot_app.utility.SignatureView;
import com.eot_app.utility.language_support.LanguageController;

import java.io.File;
import java.util.concurrent.Callable;

/**
 * Created by Mahendra Dabi on 7/11/19.
 */
public class AuditReportFragment extends Fragment implements View.OnClickListener, Report_View {
    public static String tempDir;
    public String custFileName = null;
    public String auditorFileName = null;
    LinearLayout ll_customer_sign, ll_auditor_sign;
    SignatureView custmerSignView, auditorSignView;
    LanguageController languageController;
    File filecustSign, fileauditorSign;
    LinearLayout ll_main;
    File directory;
    private Button button_submit;
    private AppCompatTextView tv_label_customersign, tv_label_remark, tv_label_auditorsign;
    private AppCompatImageView img_sign;
    private TextView tv_clear_auditor_sign, tv_clear_customer_sign;
    private EditText edit_remarks;
    private Report_PC report_pc;

    public static AuditReportFragment getInstance(String auditId) {
        Bundle bundle = new Bundle();
        bundle.putString("auditId", auditId);
        AuditReportFragment fragment = new AuditReportFragment();
        fragment.setArguments(bundle);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_audit_report, container, false);

        report_pc = new Report_PC(this);
        languageController = LanguageController.getInstance();
        initViews(view);
        setLanguage();

        return view;
    }

    private void initViews(View view) {
        report_pc = new Report_PC(this);

        button_submit = view.findViewById(R.id.button_submit);
        edit_remarks = view.findViewById(R.id.edit_remarks);

        ll_main = view.findViewById(R.id.ll_main);

        edit_remarks.setHint(LanguageController.getInstance().getMobileMsgByKey(AppConstant.remark_msg));

        tv_label_customersign = view.findViewById(R.id.tv_label_customersign);
        tv_label_remark = view.findViewById(R.id.tv_label_remark);
        tv_label_auditorsign = view.findViewById(R.id.tv_label_auditorsign);

        ll_customer_sign = view.findViewById(R.id.ll_customer_sign);
        ll_auditor_sign = view.findViewById(R.id.ll_auditor_sign);

        tv_clear_auditor_sign = view.findViewById(R.id.tv_clear_auditor_sign);
        tv_clear_customer_sign = view.findViewById(R.id.tv_clear_customer_sign);
        img_sign = view.findViewById(R.id.img_sign);

        tv_clear_customer_sign.setText(LanguageController.getInstance().getMobileMsgByKey(AppConstant.clear));
        tv_clear_auditor_sign.setText(LanguageController.getInstance().getMobileMsgByKey(AppConstant.clear));

        tv_clear_auditor_sign.setOnClickListener(this);
        tv_clear_customer_sign.setOnClickListener(this);

        button_submit.setOnClickListener(this);

        custmerSignView = new SignatureView(getActivity());
        auditorSignView = new SignatureView(getActivity());

        ll_customer_sign.addView(custmerSignView, ViewGroup.LayoutParams.FILL_PARENT, ViewGroup.LayoutParams.FILL_PARENT);
        ll_auditor_sign.addView(auditorSignView, ViewGroup.LayoutParams.FILL_PARENT, ViewGroup.LayoutParams.FILL_PARENT);

        ll_main.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                AppUtility.hideSoftKeyboard(getActivity());
                return true;
            }
        });

        createFiles();

    }

    private void createFiles() {
        try {
            ContextWrapper cw = new ContextWrapper(getActivity().getApplicationContext());
            directory = cw.getDir(getResources().getString(R.string.external_dir), Context.MODE_PRIVATE);
        } catch (Resources.NotFoundException e) {
            e.printStackTrace();
        }
    }


    private void setLanguage() {
        tv_label_customersign.setText(languageController.getMobileMsgByKey(AppConstant.customer_signature));
        tv_label_auditorsign.setText(languageController.getMobileMsgByKey(AppConstant.auditor_signature));
        tv_label_remark.setText(languageController.getMobileMsgByKey(AppConstant.remark));
        tv_label_remark.setHint(languageController.getMobileMsgByKey(AppConstant.remark));
        button_submit.setText(languageController.getMobileMsgByKey(AppConstant.submit_btn));

    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.tv_clear_customer_sign:
                custmerSignView.clear();
                break;
            case R.id.tv_clear_auditor_sign:
                auditorSignView.clear();
                break;

            case R.id.button_submit:
                AppUtility.hideSoftKeyboard(getActivity());
                button_submit.setEnabled(false);
                createReportRequest();
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        button_submit.setEnabled(true);
                    }
                }, 500);
                break;
        }
    }

    @Override
    public void onSuccess(String message) {
        if (message != null) {
            edit_remarks.setText("");
            custmerSignView.clear();
            auditorSignView.clear();

            AuditDetails auditDetails = (AuditDetails) getActivity();
            if (auditDetails != null)
                auditDetails.refreshDocuments();


            EotApp.getAppinstance().showToastmsg(
                    LanguageController.getInstance().getServerMsgByKey(message)
            );
        }
    }

    @Override
    public void onSessionExpire(String msg) {
        showDialog(msg);
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

    /*create new report request for submit*/
    private void createReportRequest() {
        ReportRequest reportRequest = new ReportRequest();

        reportRequest.setAudId(getArguments().getString("auditId"));
        reportRequest.setUsrId(App_preference.getSharedprefInstance().getLoginRes().getUsrId());
        reportRequest.setDes(edit_remarks.getText().toString());

        String custFilePath;
        String auditorFilePath;


        File customerSign = null;
        File auditorSign = null;

        try {
            if (custmerSignView != null) {

                if (custmerSignView.isSignatureEmpty()) {
                    customerSign = null;
                } else {
                    custFileName = "eot_cust_" + AppUtility.getDateByMiliseconds() + ".png";
                    filecustSign = new File(directory, custFileName);

                    custFilePath = filecustSign.getAbsolutePath();
                    customerSign = custmerSignView.exportFile(custFilePath, custFileName);
                }
            }

            if (auditorSignView != null) {
                if (auditorSignView.isSignatureEmpty())
                    auditorSign = null;
                else {
                    auditorFileName = "eot_audi_" + AppUtility.getDateByMiliseconds() + ".png";
                    fileauditorSign = new File(directory, auditorFileName);

                    auditorFilePath = fileauditorSign.getAbsolutePath();
                    auditorSign = auditorSignView.exportFile(auditorFilePath, auditorFileName);
                }

            }


        } catch (Exception e) {
            e.printStackTrace();
        }



        /*set report Params*/
        if (custmerSignView.isSignatureEmpty() && auditorSignView.isSignatureEmpty() && TextUtils.isEmpty(edit_remarks.getText().toString())) {
            EotApp.getAppinstance().showToastmsg(
                    LanguageController.getInstance().getMobileMsgByKey(AppConstant.report_mandatory_msg)
            );
            return;
        }


        reportRequest.setFileCustomerSign(customerSign);
        reportRequest.setFileAuditorSign(auditorSign);


        report_pc.addNewReport(reportRequest);
    }

    @Override
    public void onResume() {
        super.onResume();
        createFiles();
    }
}
