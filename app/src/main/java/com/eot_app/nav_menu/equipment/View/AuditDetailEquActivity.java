package com.eot_app.nav_menu.equipment.View;

import static com.eot_app.nav_menu.jobs.job_detail.detail.DetailFragment.MY_PERMISSIONS_REQUEST_CALL_PHONE;

import android.Manifest;
import android.app.Dialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.SpannableStringBuilder;
import android.text.TextUtils;
import android.text.method.LinkMovementMethod;
import android.text.style.ForegroundColorSpan;
import android.text.util.Linkify;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.eot_app.R;
import com.eot_app.nav_menu.audit.audit_list.audit_mvp.model.AuditList_Res;
import com.eot_app.nav_menu.client.clientlist.client_detail.site.sitelist.editsite.editsitedb.SpinnerCountrySite;
import com.eot_app.nav_menu.equipment.model.aduit_job_history.Auditor;
import com.eot_app.nav_menu.jobs.job_detail.detail.jobdetial_model.JobStatusModel;
import com.eot_app.nav_menu.jobs.job_detail.job_status_pkg.JobStatus_Controller;
import com.eot_app.utility.AppConstant;
import com.eot_app.utility.AppUtility;
import com.eot_app.utility.EotApp;
import com.eot_app.utility.db.AppDataBase;
import com.eot_app.utility.language_support.LanguageController;
import com.eot_app.utility.settings.setting_db.FieldWorker;
import com.google.gson.Gson;

import java.util.List;
import java.util.concurrent.Callable;

public class AuditDetailEquActivity extends AppCompatActivity implements View.OnClickListener {

    LanguageController langInstance;
    ImageView status_img;
    TextView status_txt;
    private LinearLayout ll_status;
    private ImageView imageViewChat, imageViewCall, imageViewEmail;
    private Button buttonView, buttonMapView, buttonView1;
    private TextView textViewJobCode, textViewAddress, textViewDescription, textViewContactperson, textViewInstruction, txt_fw_nm_list, tv_location, tv_description, tv_instruction, tv_contact_name, fw_Nm_List;
    private Dialog enterFieldDialog;
    private AuditList_Res audit;
    private TextView tv_end_label, tv_start_label, tv_end_date, tv_start_date;
    private TextView tv_end_time, tv_start_time;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_audit_details2);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        setTitle(LanguageController.getInstance().getMobileMsgByKey(AppConstant.detail_audit));


        initializelables();
        Intent intent = getIntent();
        if (intent.hasExtra("audit_data")) {
            //  this.audit = getIntent().getParcelableExtra("audit_data");
            String str = getIntent().getExtras().getString("audit_data_str");
            this.audit = new Gson().fromJson(str, AuditList_Res.class);
            if (audit != null)
                setDataToView(audit);
        }
    }

    private void initializelables() {
        langInstance = LanguageController.getInstance();
        status_img = findViewById(R.id.status_img);
        status_txt = findViewById(R.id.status);
        ll_status = findViewById(R.id.ll_status);

        tv_start_label = findViewById(R.id.tv_start_label);
        tv_end_label = findViewById(R.id.tv_end_label);

        tv_start_date = findViewById(R.id.tv_start_date);
        tv_end_date = findViewById(R.id.tv_end_date);

        tv_start_time = findViewById(R.id.tv_start_time);
        tv_end_time = findViewById(R.id.tv_end_time);


        tv_start_label.setText(LanguageController.getInstance().getMobileMsgByKey(AppConstant.start));
        tv_end_label.setText(LanguageController.getInstance().getMobileMsgByKey(AppConstant.end));

        tv_start_date = findViewById(R.id.tv_start_date);
        tv_end_date = findViewById(R.id.tv_end_date);

        textViewJobCode = findViewById(R.id.textViewJobCode);

        textViewAddress = findViewById(R.id.textViewAddress);
        textViewAddress.setText(langInstance.getMobileMsgByKey(AppConstant.no_location));

        textViewDescription = findViewById(R.id.textViewDescription);
        textViewDescription.setText(langInstance.getMobileMsgByKey(AppConstant.no_desc));

        textViewContactperson = findViewById(R.id.textViewContactperson);


        textViewInstruction = findViewById(R.id.textViewInstruction);
        textViewInstruction.setText(langInstance.getMobileMsgByKey(AppConstant.no_instr));

        buttonMapView = findViewById(R.id.buttonMapView);
        buttonMapView.setText(langInstance.getMobileMsgByKey(AppConstant.map));

        imageViewChat = findViewById(R.id.imageViewChat);
        imageViewCall = findViewById(R.id.imageViewCall);
        imageViewEmail = findViewById(R.id.imageViewEmail);


        txt_fw_nm_list = findViewById(R.id.txt_fw_nm_list);
        txt_fw_nm_list.setText(langInstance.getMobileMsgByKey(AppConstant.no_auditor_available));


        tv_location = findViewById(R.id.textView6);
        tv_location.setText(langInstance.getMobileMsgByKey(AppConstant.location));

        tv_description = findViewById(R.id.textView8);
        tv_description.setText(langInstance.getMobileMsgByKey(AppConstant.description));

        tv_instruction = findViewById(R.id.textView9);
        tv_instruction.setText(langInstance.getMobileMsgByKey(AppConstant.instr));

        tv_contact_name = findViewById(R.id.textView10);
        tv_contact_name.setText(langInstance.getMobileMsgByKey(AppConstant.contact_name));

        fw_Nm_List = findViewById(R.id.fw_Nm_List);
        fw_Nm_List.setText(langInstance.getMobileMsgByKey(AppConstant.auditors));


        buttonMapView.setOnClickListener(this);
        imageViewCall.setOnClickListener(this);
        imageViewEmail.setOnClickListener(this);
        imageViewChat.setOnClickListener(this);
    }

    private void setDataToView(AuditList_Res audit) {


        if (!TextUtils.isEmpty(this.audit.getAdr())) {
            StringBuilder addres = new StringBuilder();
            addres.append(this.audit.getAdr());
            if (!TextUtils.isEmpty(this.audit.getLandmark()))
                addres.append(", " + this.audit.getLandmark());

            if (!TextUtils.isEmpty(this.audit.getCity()))
                addres.append(", " + this.audit.getCity());

            if (!TextUtils.isEmpty(this.audit.getState())
                    && !TextUtils.isEmpty(this.audit.getCtry())) {
                try {
                    String stateName = SpinnerCountrySite.getStatenameById(this.audit.getCtry(), this.audit.getState());
                    if (!TextUtils.isEmpty(stateName))
                        addres.append(", " + stateName);
                    String countryId = SpinnerCountrySite.getCountryNameById(this.audit.getCtry());
                    if (!TextUtils.isEmpty(countryId))
                        addres.append(", " + countryId);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            if (!TextUtils.isEmpty(this.audit.getZip())) {
                addres.append(", " + this.audit.getZip());
            }

            textViewAddress.setText(addres.toString());
        }

        if (TextUtils.isEmpty(audit.getDes()))
            textViewDescription.setText(langInstance.getMobileMsgByKey(AppConstant.no_desc));
        else
            textViewDescription.setText(this.audit.getDes());

        if (TextUtils.isEmpty(audit.getInst()))
            textViewInstruction.setText(langInstance.getMobileMsgByKey(AppConstant.no_instr));
        else
            textViewInstruction.setText(this.audit.getInst());


        if (audit.getStatus() != null) {
            try {
                setViewByAuditStatus(Integer.parseInt(audit.getStatus()));
            } catch (NumberFormatException e) {
                e.printStackTrace();
            }
        }

        String fw = "";
        List<Auditor> auditors = audit.getAuditor();
        for (Auditor auditor : auditors) {
            String userid = auditor.getUsrId();
            fw = fw + "," + userid;
        }
        setAuditorName(fw);


        if (TextUtils.isEmpty(audit.getCnm()))
            textViewContactperson.setText(langInstance.getMobileMsgByKey(AppConstant.no_contact_available));
        else
            textViewContactperson.setText(audit.getCnm());

        if (this.audit.getLabel() != null)
            textViewJobCode.setText(getSpannebleFormat(LanguageController.getInstance().getMobileMsgByKey(AppConstant.audit_code), this.audit.getLabel()), TextView.BufferType.SPANNABLE);

        String dateFormat = "dd MMM yyyy";
        String timeFormat = "hh:mm a";

        if (!TextUtils.isEmpty(this.audit.getSchdlStart())) {
            long startDateLong = Long.parseLong(this.audit.getSchdlStart());
            String startDate = AppUtility.getDateWithFormate2(startDateLong * 1000, dateFormat);
            tv_start_date.setText(startDate);
            String startTime = AppUtility.getDateWithFormate2(startDateLong * 1000, timeFormat);
            tv_start_time.setText(startTime);

        }

        if (!TextUtils.isEmpty(this.audit.getSchdlFinish())) {
            long endDateLong = Long.parseLong(this.audit.getSchdlFinish());

            String endDate = AppUtility.getDateWithFormate2(endDateLong * 1000, dateFormat);
            tv_end_date.setText(endDate);

            String endTime = AppUtility.getDateWithFormate2(endDateLong * 1000, timeFormat);
            tv_end_time.setText(endTime);

        }

    }

    private void setViewByAuditStatus(int status) {
        JobStatusModel jobStatusObject = JobStatus_Controller.getInstance().getStatusObjectById(String.valueOf(status));
        if (jobStatusObject != null) {

            status_txt.setText(jobStatusObject.getStatus_name());
            ll_status.setBackgroundResource(R.color.white);

            int id = this.getResources().getIdentifier(jobStatusObject.getImg(), "drawable", this.getPackageName());
            status_img.setImageResource(id);
            if (jobStatusObject.getStatus_no().equals("7")) {
                ll_status.setBackgroundResource(R.color.in_progress);
                switchDefaultColor(EotApp.getAppinstance().getResources().getColor(R.color.white));
            } else {
                switchDefaultColor(EotApp.getAppinstance().getResources().getColor(R.color.txt_color));
            }
        } else {
            if (audit != null && audit.getAudId() != null)
                AppDataBase.getInMemoryDatabase(EotApp.getAppinstance()).auditDao().deletAuditById(audit.getAudId());
            return;
        }
    }

    private void switchDefaultColor(int color) {
        status_txt.setTextColor(color);
    }


    private SpannableStringBuilder getSpannebleFormat(String lableName, String value) {
        SpannableStringBuilder builder = new SpannableStringBuilder();
        SpannableString str1 = new SpannableString(lableName + " ");
        str1.setSpan(new ForegroundColorSpan(Color.parseColor("#737D7E")), 0, str1.length(), 0);
        builder.append(str1);

        SpannableString str2 = new SpannableString(value);
        str2.setSpan(new android.text.style.StyleSpan(android.graphics.Typeface.BOLD), 0, str2.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        builder.append(str2);

        return builder;
    }

    private void setAuditorName(String names) {
        try {
            StringBuffer stringBuffer = new StringBuffer();
            String kpr = names;
            String[] kprList = kpr.split(",");
            for (String id : kprList) {
                FieldWorker fieldWorker = AppDataBase.getInMemoryDatabase(this).fieldWorkerModel().getFieldWorkerByID(id);
                if (fieldWorker != null) {
                    stringBuffer.append(fieldWorker.getName())
                            .append(fieldWorker.getLnm())
                            .append(", ");
                }

            }
            stringBuffer.deleteCharAt(stringBuffer.length() - 2);
            String paramIds = stringBuffer.toString();
            txt_fw_nm_list.setText(paramIds);
        } catch (Exception e) {
            e.printStackTrace();
            txt_fw_nm_list.setText("Not available");
        }
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.buttonMapView:
                drawRoute();
                break;


            case R.id.imageViewEmail:
                if (audit != null) {
                    if (!audit.getEmail().equals("")) {
                        getDialogEmail();
                        TextView txtViewSkypeCon1 = enterFieldDialog.findViewById(R.id.txt_email_popup);
                        final SpannableString s1 = new SpannableString(audit.getEmail().trim());
                        Linkify.addLinks(s1, Linkify.WEB_URLS | Linkify.EMAIL_ADDRESSES);
                        txtViewSkypeCon1.setTextIsSelectable(true);
                        txtViewSkypeCon1.setMovementMethod(LinkMovementMethod.getInstance());
                        if (TextUtils.isEmpty(s1))
                            txtViewSkypeCon1.setText(LanguageController.getInstance().getMobileMsgByKey(AppConstant.audit_email_msg));
                        else
                            txtViewSkypeCon1.setText(s1);
                        enterFieldDialog.show();

                        TextView okBtn = enterFieldDialog.findViewById(R.id.btnClose);
                        okBtn.setText(LanguageController.getInstance().getMobileMsgByKey(AppConstant.ok));

                        okBtn.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                enterFieldDialog.dismiss();
                            }
                        });

                    } else {
                        AppUtility.alertDialog(this, LanguageController.getInstance().getMobileMsgByKey(AppConstant.dialog_alert), LanguageController.getInstance().getMobileMsgByKey(AppConstant.job_det_email), LanguageController.getInstance().getMobileMsgByKey(AppConstant.ok), "", new Callable<Boolean>() {
                            @Override
                            public Boolean call() throws Exception {
                                return true;
                            }
                        });
                    }
                }

                break;


            case R.id.imageViewChat:
                AppUtility.alertDialog(this, LanguageController.getInstance().getMobileMsgByKey(AppConstant.dialog_alert),
                        LanguageController.getInstance().getMobileMsgByKey(AppConstant.no_chat),
                        LanguageController.getInstance().getMobileMsgByKey(AppConstant.no), "", new Callable<Boolean>() {
                            @Override
                            public Boolean call() throws Exception {
                                return true;
                            }
                        });
                break;

            case R.id.imageViewCall:
                if (this.audit != null) {
                    if (!this.audit.getMob1().equals("") || !this.audit.getMob2().equals("")) {
                        getDialogCall();
                        try {
                            final TextView txtViewSkypeCon1 = enterFieldDialog.findViewById(R.id.txtViewSkypeCon1);
                            txtViewSkypeCon1.setVisibility(View.VISIBLE);
                            final SpannableString s1 = new SpannableString(this.audit.getMob1().trim());
                            Linkify.addLinks(txtViewSkypeCon1, Linkify.ALL);
                            if (TextUtils.isEmpty(s1))
                                txtViewSkypeCon1.setText(LanguageController.getInstance().getMobileMsgByKey(AppConstant.no_contact_available));
                            else
                                txtViewSkypeCon1.setText(s1);

                            txtViewSkypeCon1.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    if (ContextCompat.checkSelfPermission(AuditDetailEquActivity.this, Manifest.permission.CALL_PHONE)
                                            != PackageManager.PERMISSION_GRANTED) {

                                        ActivityCompat.requestPermissions(AuditDetailEquActivity.this, new String[]{Manifest.permission.CALL_PHONE}, MY_PERMISSIONS_REQUEST_CALL_PHONE);
                                    } else if (AppUtility.isvalidPhoneNo(txtViewSkypeCon1.getText().toString())) {
                                        AppUtility.getCallOnNumber(AuditDetailEquActivity.this, txtViewSkypeCon1.getText().toString());

                                    }
                                }
                            });

                            final TextView txtViewSkypeCon2 = enterFieldDialog.findViewById(R.id.txtViewSkypeCon2);
                            txtViewSkypeCon2.setVisibility(View.VISIBLE);
                            Linkify.addLinks(txtViewSkypeCon2, Linkify.ALL);
                            final SpannableString s = new SpannableString(this.audit.getMob2().trim());
                            if (TextUtils.isEmpty(s))
                                txtViewSkypeCon2.setText(LanguageController.getInstance().getMobileMsgByKey(AppConstant.no_contact_available));
                            else
                                txtViewSkypeCon2.setText(s);
                            txtViewSkypeCon2.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    if (ContextCompat.checkSelfPermission(AuditDetailEquActivity.this, Manifest.permission.CALL_PHONE)
                                            != PackageManager.PERMISSION_GRANTED) {

                                        ActivityCompat.requestPermissions(AuditDetailEquActivity.this, new String[]{Manifest.permission.CALL_PHONE}, MY_PERMISSIONS_REQUEST_CALL_PHONE);
                                    } else if (AppUtility.isvalidPhoneNo(txtViewSkypeCon2.getText().toString())) {
                                        AppUtility.getCallOnNumber(AuditDetailEquActivity.this, txtViewSkypeCon2.getText().toString());

                                    }
                                }
                            });

                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        TextView okBtn = enterFieldDialog.findViewById(R.id.btnClose);
                        okBtn.setText(LanguageController.getInstance().getMobileMsgByKey(AppConstant.ok));
                        okBtn.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                enterFieldDialog.dismiss();
                            }
                        });
                        enterFieldDialog.show();
                    } else {
                        AppUtility.alertDialog(this, LanguageController.getInstance().getMobileMsgByKey(AppConstant.dialog_alert), LanguageController.getInstance().getMobileMsgByKey(AppConstant.no_contact_available), LanguageController.getInstance().getMobileMsgByKey(AppConstant.ok), "", new Callable<Boolean>() {
                            @Override
                            public Boolean call() throws Exception {
                                return true;
                            }
                        });
                    }
                }
                break;

        }
    }

    /***
     * redirecting to google map to draw route
     * between user current location to audit location
     *
     ***/
    private void drawRoute() {
        if (!TextUtils.isEmpty(this.audit.getLat()) && !TextUtils.isEmpty(this.audit.getLng())) {
            // String geoUri = "http://maps.google.com/maps?q=loc:" + this.audit.getLat() + "," + this.audit.getLng();
            try {
                String geoUri = "http://maps.google.com/maps?daddr=" + this.audit.getLat() + "," + this.audit.getLng();
                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(geoUri));
                startActivity(intent);
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else if (!TextUtils.isEmpty(textViewAddress.getText().toString())) {
            try {

                String completeAddress = audit.getAdr() + " " + audit.getCity() + " " + SpinnerCountrySite.getCountryNameById(audit.getCtry());

                String geoUri = "http://maps.google.com/maps?daddr=" + completeAddress;
                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(geoUri));
                startActivity(intent);
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            AppUtility.alertDialog(this, LanguageController.getInstance().getMobileMsgByKey(AppConstant.dialog_alert), LanguageController.getInstance().getMobileMsgByKey(AppConstant.no_location), LanguageController.getInstance().getMobileMsgByKey(AppConstant.ok), "", new Callable<Boolean>() {
                @Override
                public Boolean call() throws Exception {
                    return true;
                }
            });
        }
    }

    private void getDialogEmail() {
        try {
            if (enterFieldDialog != null)
                if (enterFieldDialog.isShowing())
                    enterFieldDialog.dismiss();
        } catch (Exception e) {
            e.printStackTrace();
        }
        try {
            if (enterFieldDialog != null)
                enterFieldDialog = null;
            enterFieldDialog = new Dialog(this);
            enterFieldDialog.setCancelable(false);
            enterFieldDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            enterFieldDialog.setContentView(R.layout.popup_emai_layout);

            Window window = enterFieldDialog.getWindow();
            window.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            WindowManager.LayoutParams wlp = window.getAttributes();

            wlp.gravity = Gravity.CENTER;
            wlp.flags &= ~WindowManager.LayoutParams.FLAG_BLUR_BEHIND;
            window.setAttributes(wlp);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    private void getDialogCall() {
        try {
            if (enterFieldDialog != null)
                if (enterFieldDialog.isShowing())
                    enterFieldDialog.dismiss();
        } catch (Exception e) {
            e.printStackTrace();
        }
        try {
            if (enterFieldDialog != null)
                enterFieldDialog = null;
            enterFieldDialog = new Dialog(this);
            enterFieldDialog.setCancelable(false);
            enterFieldDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            enterFieldDialog.setContentView(R.layout.popup_call);

            Window window = enterFieldDialog.getWindow();
            window.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            WindowManager.LayoutParams wlp = window.getAttributes();

            wlp.gravity = Gravity.CENTER;
            wlp.flags &= ~WindowManager.LayoutParams.FLAG_BLUR_BEHIND;
            window.setAttributes(wlp);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == android.R.id.home)
            onBackPressed();
        return true;
    }


    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }


}