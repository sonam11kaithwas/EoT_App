package com.eot_app.nav_menu.equipment.View;

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
import android.view.LayoutInflater;
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
import androidx.appcompat.widget.AppCompatCheckBox;
import androidx.cardview.widget.CardView;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.eot_app.R;
import com.eot_app.eoteditor.PicassoImageGetter;
import com.eot_app.nav_menu.client.clientlist.client_detail.site.sitelist.editsite.editsitedb.SpinnerCountrySite;
import com.eot_app.nav_menu.custom_fileds.custom_model.CustOmFormQuestionsRes;
import com.eot_app.nav_menu.equipment.View.Job_Equipment_MVP.Job_EquipmentPC;
import com.eot_app.nav_menu.equipment.View.Job_Equipment_MVP.Job_Equipment_PI;
import com.eot_app.nav_menu.equipment.View.Job_Equipment_MVP.Job_Equipment_View;
import com.eot_app.nav_menu.equipment.model.Keeper;
import com.eot_app.nav_menu.jobs.job_db.Job;
import com.eot_app.nav_menu.jobs.job_db.JtId;
import com.eot_app.nav_menu.jobs.job_detail.addinvoiveitem2pkg.model.InvoiceItemDataModel;
import com.eot_app.nav_menu.jobs.job_detail.detail.jobdetial_model.JobStatusModel;
import com.eot_app.nav_menu.jobs.job_detail.documents.doc_model.GetFileList_Res;
import com.eot_app.nav_menu.jobs.job_detail.form_form.get_qus_list.qus_model.AnswerModel;
import com.eot_app.nav_menu.jobs.job_detail.job_status_pkg.JobStatus_Controller;
import com.eot_app.utility.AppConstant;
import com.eot_app.utility.AppUtility;
import com.eot_app.utility.EotApp;
import com.eot_app.utility.db.AppDataBase;
import com.eot_app.utility.language_support.LanguageController;
import com.eot_app.utility.settings.setting_db.FieldWorker;
import com.eot_app.utility.settings.setting_db.TagData;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.Callable;

public class JobdetailsEquActivity extends AppCompatActivity implements View.OnClickListener, Job_Equipment_View {
    public static final int MY_PERMISSIONS_REQUEST_CALL_PHONE = 1234;
    Bundle bundle;
    Job job;
    private TextView person_name, textViewJobCode, textViewTime, textViewPriority,
            textViewJobStatus, textViewAddress, textViewDescription, textViewContactperson, textViewTitle, textViewInstruction, txtViewHeader, textViewTags, txt_fw_nm_list, tv_location, tv_description, tv_instruction, tv_contact_name, fw_Nm_List, tv_tag, complation_txt, complation_notes;
    private Button buttonMapView;
    private Dialog enterFieldDialog;
    private ImageView imageViewChat, imageViewCall, imageViewEmail, imageView5;


    private TextView custom_filed_txt, tv_item_list;
    private CardView customField_view;
    private LinearLayout ll_custom_views;

    private Job_Equipment_PI jobDetail_pi;
    private RecyclerView recyclerView_job_item;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_jobdetails);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        setTitle(LanguageController.getInstance().getMobileMsgByKey(AppConstant.job_details));

        jobDetail_pi = new Job_EquipmentPC(this);


        bundle = getIntent().getExtras();
        if (bundle != null) {
            if (getIntent().hasExtra("Job_data")) {
                String job_data = getIntent().getStringExtra("Job_data");
                job = new Gson().fromJson(job_data, Job.class);
            }
        }
        initializelables();
        if (job != null) {
            setJobDetail(job);
            setDataToView(job);
        }
    }

    private void initializelables() {

        person_name = findViewById(R.id.person_name);
        textViewJobCode = findViewById(R.id.textViewJobCode);
        textViewTime = findViewById(R.id.textViewTime);
        textViewPriority = findViewById(R.id.textViewPriority);
        textViewJobStatus = findViewById(R.id.textViewJobStatus);
        textViewAddress = findViewById(R.id.textViewAddress);
        textViewAddress.setText(LanguageController.getInstance().getMobileMsgByKey(AppConstant.no_location));

        textViewDescription = findViewById(R.id.textViewDescription);
        textViewDescription.setText(LanguageController.getInstance().getMobileMsgByKey(AppConstant.no_desc));

        textViewTags = findViewById(R.id.textViewTags);
        textViewTags.setText(LanguageController.getInstance().getMobileMsgByKey(AppConstant.no_tag));


        textViewContactperson = findViewById(R.id.textViewContactperson);
        textViewContactperson.setText(LanguageController.getInstance().getMobileMsgByKey(AppConstant.no_contact_available));
        textViewTitle = findViewById(R.id.textViewTitle);

        textViewInstruction = findViewById(R.id.textViewInstruction);
        textViewInstruction.setText(LanguageController.getInstance().getMobileMsgByKey(AppConstant.no_instr));

        txtViewHeader = findViewById(R.id.txtViewHeader);


        buttonMapView = findViewById(R.id.buttonMapView);
        buttonMapView.setText(LanguageController.getInstance().getMobileMsgByKey(AppConstant.map));

        imageViewChat = findViewById(R.id.imageViewChat);
        imageViewCall = findViewById(R.id.imageViewCall);
        imageViewEmail = findViewById(R.id.imageViewEmail);
        imageView5 = findViewById(R.id.imageView5);

        txt_fw_nm_list = findViewById(R.id.txt_fw_nm_list);
        txt_fw_nm_list.setText(LanguageController.getInstance().getMobileMsgByKey(AppConstant.no_fw_available));


        tv_location = findViewById(R.id.textView6);
        tv_location.setText(LanguageController.getInstance().getMobileMsgByKey(AppConstant.location));

        tv_description = findViewById(R.id.textView8);
        tv_description.setText(LanguageController.getInstance().getMobileMsgByKey(AppConstant.description));

        tv_instruction = findViewById(R.id.textView9);
        tv_instruction.setText(LanguageController.getInstance().getMobileMsgByKey(AppConstant.instr));

        tv_contact_name = findViewById(R.id.textView10);
        tv_contact_name.setText(LanguageController.getInstance().getMobileMsgByKey(AppConstant.contact_name));

        fw_Nm_List = findViewById(R.id.fw_Nm_List);
        fw_Nm_List.setText(LanguageController.getInstance().getMobileMsgByKey(AppConstant.fieldworkers));

        tv_tag = findViewById(R.id.textView11);
        tv_tag.setText(LanguageController.getInstance().getMobileMsgByKey(AppConstant.tags));

        complation_txt = findViewById(R.id.complation_txt);
        complation_notes = findViewById(R.id.complation_notes);
        complation_txt.setText(LanguageController.getInstance().getMobileMsgByKey(AppConstant.completion_note));


        customField_view = findViewById(R.id.customField_view);
        ll_custom_views = findViewById(R.id.ll_custom_views);

        custom_filed_txt = findViewById(R.id.custom_filed_txt);
        custom_filed_txt.setText(LanguageController.getInstance().getMobileMsgByKey(AppConstant.title_cutom_field));
        custom_filed_txt.setOnClickListener(this);

        tv_item_list = findViewById(R.id.tv_item_list);
        tv_item_list.setText(LanguageController.getInstance().getMobileMsgByKey(AppConstant.list_item));

        recyclerView_job_item = findViewById(R.id.recyclerView_job_item);
        recyclerView_job_item.setLayoutManager(new LinearLayoutManager(this));
        recyclerView_job_item.setNestedScrollingEnabled(false);

        buttonMapView.setOnClickListener(this);
        imageViewChat.setOnClickListener(this);
        imageViewCall.setOnClickListener(this);
        imageViewEmail.setOnClickListener(this);

    }

    private void setViewByJobStatus(int status) {
        JobStatusModel jobStatusObject = JobStatus_Controller.getInstance().getStatusObjectById(String.valueOf(status));
        if (jobStatusObject != null) {

            textViewJobStatus.setText(jobStatusObject.getStatus_name());
            int id = this.getResources().getIdentifier(jobStatusObject.getImg(), "drawable", this.getPackageName());
            imageView5.setImageResource(id);
        }

    }

    @Override
    public void setList(ArrayList<GetFileList_Res> getFileList_res, String isAttachCompletionNotes) {

    }

    public void setDataToView(Job job) {

        if (job.getStatus() != null) {
            try {
                setViewByJobStatus(Integer.parseInt(job.getStatus()));
            } catch (NumberFormatException e) {
                e.printStackTrace();
            }
        }


        if (TextUtils.isEmpty(job.getInst()))
            findViewById(R.id.cv_instruction).setVisibility(View.GONE);
        else
            findViewById(R.id.cv_instruction).setVisibility(View.VISIBLE);


        if (TextUtils.isEmpty(job.getDes()))
            findViewById(R.id.cv_des).setVisibility(View.GONE);
        else
            findViewById(R.id.cv_des).setVisibility(View.VISIBLE);


        String fw = "";
        List<Keeper> Keepers = job.getKeepers();
        for (Keeper keeper : Keepers) {
            String userid = keeper.getUsrId();
            fw = fw + "," + userid;
        }
        setFwList(fw);
        try {
            StringBuffer StrJt = new StringBuffer();
            List<JtId> jtaray = job.getJtId();
            Iterator<JtId> it = jtaray.iterator();
            if (it.hasNext()) {
                StrJt.append(it.next().getTitle());
            }
            while (it.hasNext()) {
                StrJt.append(", ").append(it.next().getTitle());
            }
            textViewTitle.setText(StrJt);
            StringBuilder sb = new StringBuilder();
            for (TagData item : job.getTagData()) {
                if (sb.length() > 0) {
                    sb.append(", ");
                }
                sb.append(item.getTnm());
            }
            textViewTags.setText(sb.toString());
            SpannableStringBuilder builder = new SpannableStringBuilder();
            SpannableString str1 = new SpannableString(LanguageController.getInstance().getMobileMsgByKey(AppConstant.job_code) + " ");
            str1.setSpan(new ForegroundColorSpan(Color.parseColor("#737D7E")), 0, str1.length(), 0);
            builder.append(str1);

            SpannableString str2 = new SpannableString(job.getLabel());
            // str2.setSpan(new ForegroundColorSpan(getResources().getColor(R.color.txt_color)), 0, str2.length(), 0);
            str2.setSpan(new android.text.style.StyleSpan(android.graphics.Typeface.BOLD), 0, str2.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
            builder.append(str2);

            textViewJobCode.setText(builder, TextView.BufferType.SPANNABLE);
            if (!job.getSchdlStart().equals("")) {
                String[] formated_date = AppUtility.getFormatedTime(job.getSchdlStart());
                textViewTime.setText(formated_date[1] + formated_date[2]);
            }
            if (job.getPrty().equals("1"))
                textViewPriority.setText(LanguageController.getInstance().getMobileMsgByKey(AppConstant.Low));
            else if (job.getPrty().equals("2"))
                textViewPriority.setText(LanguageController.getInstance().getMobileMsgByKey(AppConstant.medium));
            else
                textViewPriority.setText(LanguageController.getInstance().getMobileMsgByKey(AppConstant.High));

            textViewInstruction.setText(job.getInst());

            Spannable spannableHtmlWithImageGetter = PicassoImageGetter.getSpannableHtmlWithImageGetter(textViewDescription, job.getDes());

            PicassoImageGetter.setClickListenerOnHtmlImageGetter(spannableHtmlWithImageGetter, new PicassoImageGetter.Callback() {
                @Override
                public void onImageClick(String imageUrl) {
                    if (!TextUtils.isEmpty(imageUrl)) {
                        startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(imageUrl)));
                    }
                }
            });
            textViewDescription.setText(spannableHtmlWithImageGetter);
            textViewDescription.setMovementMethod(LinkMovementMethod.getInstance());

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void setFwList(String fwList) {
        try {
            StringBuffer stringBuffer = new StringBuffer();
            String kpr = fwList;
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


    private void setJobDetail(Job job) {
        if (job != null) {

            if (jobDetail_pi != null) {
                jobDetail_pi.getCustomFieldQues(job.getJobId());
                jobDetail_pi.getJobCompletionDetails(job.getJobId());
                jobDetail_pi.getJobItemList(job.getJobId());
            }

            person_name.setText(job.getNm());
            StringBuffer sb_adr = new StringBuffer();
            if (job.getAdr() != null && !job.getAdr().equals("")) {
                sb_adr.append(job.getAdr());
            }
            if (!TextUtils.isEmpty(job.getLandmark())) {
                sb_adr.append(", " + job.getLandmark());
            }

            if (job.getCity() != null && !job.getCity().equals("")) {
                sb_adr.append(", " + job.getCity());
            }
            if (job.getState() != null && !job.getState().equals("")) {
                sb_adr.append(", " + SpinnerCountrySite.getStatenameById(job.getCtry(), job.getState()));
            }
            if (job.getCtry() != null && !job.getCtry().equals("")) {
                sb_adr.append(", " + SpinnerCountrySite.getCountryNameById(job.getCtry()));
            }
            if (!TextUtils.isEmpty(job.getZip())) {
                sb_adr.append(", " + job.getZip());
            }

            textViewAddress.setText(sb_adr);
            textViewContactperson.setText(job.getCnm());
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.buttonMapView:
                drawRoute();
                break;
            case R.id.imageViewEmail:
                if (job != null) {
                    if (!job.getEmail().equals("")) {
                        getDialogEmail();
                        TextView txtViewSkypeCon1 = enterFieldDialog.findViewById(R.id.txt_email_popup);
                        final SpannableString s1 = new SpannableString(job.getEmail().trim());
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
                if (this.job != null) {
                    if (!this.job.getMob1().equals("") || !this.job.getMob2().equals("")) {
                        getDialogCall();
                        try {
                            final TextView txtViewSkypeCon1 = enterFieldDialog.findViewById(R.id.txtViewSkypeCon1);
                            txtViewSkypeCon1.setVisibility(View.VISIBLE);
                            final SpannableString s1 = new SpannableString(this.job.getMob1().trim());
                            Linkify.addLinks(txtViewSkypeCon1, Linkify.ALL);
                            if (TextUtils.isEmpty(s1))
                                txtViewSkypeCon1.setText(LanguageController.getInstance().getMobileMsgByKey(AppConstant.no_contact_available));
                            else
                                txtViewSkypeCon1.setText(s1);

                            txtViewSkypeCon1.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    if (ContextCompat.checkSelfPermission(JobdetailsEquActivity.this, Manifest.permission.CALL_PHONE)
                                            != PackageManager.PERMISSION_GRANTED) {

                                        ActivityCompat.requestPermissions(JobdetailsEquActivity.this, new String[]{Manifest.permission.CALL_PHONE}, MY_PERMISSIONS_REQUEST_CALL_PHONE);
                                    } else if (AppUtility.isvalidPhoneNo(txtViewSkypeCon1.getText().toString())) {
                                        AppUtility.getCallOnNumber(JobdetailsEquActivity.this, txtViewSkypeCon1.getText().toString());

                                    }
                                }
                            });

                            final TextView txtViewSkypeCon2 = enterFieldDialog.findViewById(R.id.txtViewSkypeCon2);
                            txtViewSkypeCon2.setVisibility(View.VISIBLE);
                            Linkify.addLinks(txtViewSkypeCon2, Linkify.ALL);
                            final SpannableString s = new SpannableString(this.job.getMob2().trim());
                            if (TextUtils.isEmpty(s))
                                txtViewSkypeCon2.setText(LanguageController.getInstance().getMobileMsgByKey(AppConstant.no_contact_available));
                            else
                                txtViewSkypeCon2.setText(s);
                            txtViewSkypeCon2.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    if (ContextCompat.checkSelfPermission(JobdetailsEquActivity.this, Manifest.permission.CALL_PHONE)
                                            != PackageManager.PERMISSION_GRANTED) {

                                        ActivityCompat.requestPermissions(JobdetailsEquActivity.this, new String[]{Manifest.permission.CALL_PHONE}, MY_PERMISSIONS_REQUEST_CALL_PHONE);
                                    } else if (AppUtility.isvalidPhoneNo(txtViewSkypeCon2.getText().toString())) {
                                        AppUtility.getCallOnNumber(JobdetailsEquActivity.this, txtViewSkypeCon2.getText().toString());

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
        if (!TextUtils.isEmpty(this.job.getLat()) && !TextUtils.isEmpty(this.job.getLng())) {
            try {
                String geoUri = "http://maps.google.com/maps?daddr=" + this.job.getLat() + "," + this.job.getLng();
                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(geoUri));
                startActivity(intent);
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else if (!TextUtils.isEmpty(textViewAddress.getText().toString())) {
            try {

                String completeAddress = job.getAdr() + " " + job.getCity() + " " + SpinnerCountrySite.getCountryNameById(job.getCtry());

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
            enterFieldDialog = new Dialog(JobdetailsEquActivity.this);
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


    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == android.R.id.home)
            onBackPressed();
        return true;
    }

    @Override
    public void setButtonsUI(JobStatusModel jobstatus) {

    }

    @Override
    public void setResultForChangeInJob(String update, String jobid) {

    }

    @Override
    public void resetstatus(String status_no) {

    }


    @Override
    public void setCustomFiledList(ArrayList<CustOmFormQuestionsRes> questionList) {
        try {
            if (questionList.size() > 0) {
                //   this.questionList = questionList;
                customField_view.setVisibility(View.VISIBLE);
                ll_custom_views.removeAllViews();
                for (CustOmFormQuestionsRes res : questionList) {
                    View customView = LayoutInflater.from(this).inflate(R.layout.item_custom_field_job_overview, null, false);
                    TextView textView = customView.findViewById(R.id.custom_filed_form);
                    AppCompatCheckBox checkBox = customView.findViewById(R.id.checkbox);
                    textView.setText(res.getDes() + " : ");

                    List<AnswerModel> ans = res.getAns();
                    if (ans != null && ans.size() > 0) {
                        for (AnswerModel model : ans) {
                            if (!TextUtils.isEmpty(model.getValue())) {
                                switch (res.getType()) {
                                    case "5"://date type
                                        try {
                                            if (!(model.getValue()).equals("")) {
                                                String[] dateConvert = AppUtility.getFormatedTime(model.getValue());
                                                String s = dateConvert[0];
                                                String[] date = s.split(",");
                                                textView.append(date[1].trim().replace(" ", "-"));

                                                //  holder.tvDate.setText(date[1].trim().replace(" ", "-"));
                                            }
                                        } catch (Exception ex) {
                                            ex.printStackTrace();
                                        }
                                        break;
                                    case "6"://Time type
                                        try {
                                            if (!(model.equals(""))) {
                                                String time = AppUtility.getDateWithFormate2((Long.parseLong(model.
                                                                getValue()) * 1000),
                                                        "hh:mm a");
                                                textView.append(time);
                                            }
                                        } catch (Exception ex) {
                                            ex.printStackTrace();
                                        }
                                        break;
                                    case "7": //Date Time Type
                                        try {
                                            if (!model.getValue().equals("")) {
                                                Long dateLong = Long.parseLong(model.getValue());
                                                String dateConvert = AppUtility.getDate(dateLong, "dd-MMM-yyyy hh:mm a");
                                                textView.append(dateConvert);
                                            }
                                        } catch (NumberFormatException e) {
                                            e.printStackTrace();
                                        }
                                        break;

                                    //single checkbox
                                    case "8":
                                        checkBox.setVisibility(View.VISIBLE);
                                        if (!TextUtils.isEmpty(model.getValue())) {
                                            checkBox.setChecked(model.getValue().equals("1"));
                                        } else checkBox.setChecked(false);
                                        textView.setText(res.getDes());
                                        break;

                                    default:
                                        textView.append(model.getValue() + " ");


                                }
                            }
                        }

                    } else if (res.getType().equals("8")) {
                        textView.setText(res.getDes());
                        checkBox.setVisibility(View.VISIBLE);
                    }

                    ll_custom_views.addView(customView);
                }
                // addCustomFieldText();
            } else {
                customField_view.setVisibility(View.GONE);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void sessionExpire(String msg) {
        AppUtility.alertDialog(this, LanguageController.getInstance().getMobileMsgByKey(AppConstant.dialog_error_title), msg, LanguageController.getInstance().getMobileMsgByKey(AppConstant.ok), "", new Callable<Boolean>() {
            @Override
            public Boolean call() throws Exception {
                EotApp.getAppinstance().sessionExpired();
                return null;
            }
        });
    }

    @Override
    public void StopRecurPatternHide() {

    }

    @Override
    public void setCompletionNote(String completionNote) {
        if (!TextUtils.isEmpty(completionNote) && complation_notes != null)
            complation_notes.setText(completionNote);
    }

    @Override
    public void setJobItemList(List<InvoiceItemDataModel> data) {
        if (recyclerView_job_item != null && data != null) {
            JobItemAdapter jobItemAdapter = new JobItemAdapter(this, data);
            recyclerView_job_item.setAdapter(jobItemAdapter);
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }
}