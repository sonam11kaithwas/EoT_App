package com.eot_app.nav_menu.client.clientlist.client_detail.work_history.view;

import static com.eot_app.nav_menu.jobs.job_detail.detail.DetailFragment.MY_PERMISSIONS_REQUEST_CALL_PHONE;

import android.Manifest;
import android.app.Dialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.TextUtils;
import android.text.method.LinkMovementMethod;
import android.text.util.Linkify;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.widget.ContentLoadingProgressBar;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.eot_app.R;
import com.eot_app.nav_menu.appointment.dbappointment.Appointment;
import com.eot_app.nav_menu.appointment.details.AppointmentAttachment;
import com.eot_app.nav_menu.appointment.details.AttachementAdapter;
import com.eot_app.nav_menu.client.client_db.Client;
import com.eot_app.nav_menu.client.clientlist.client_detail.site.sitelist.editsite.editsitedb.SpinnerCountrySite;
import com.eot_app.nav_menu.client.clientlist.client_detail.work_history.history_presenter.appointment_Details_presrnter.Appointment_Deatil_PC;
import com.eot_app.nav_menu.client.clientlist.client_detail.work_history.history_presenter.appointment_Details_presrnter.Appointment_Deatils_View;
import com.eot_app.nav_menu.client.clientlist.client_detail.work_history.history_presenter.appointment_Details_presrnter.Appointmnet_Deatils_PI;
import com.eot_app.utility.AppConstant;
import com.eot_app.utility.AppUtility;
import com.eot_app.utility.App_preference;
import com.eot_app.utility.EotApp;
import com.eot_app.utility.db.AppDataBase;
import com.eot_app.utility.language_support.LanguageController;
import com.hypertrack.hyperlog.HyperLog;

import java.util.List;
import java.util.concurrent.Callable;


public class AppointmentHistoryDetailsActivity extends AppCompatActivity implements View.OnClickListener, Appointment_Deatils_View, AttachementAdapter.OnItemSelection {
    AttachementAdapter attachementAdapter;
    private TextView tv_client_name, tv_view_on_map, tv_label_des, tv_des,
            tv_lable_schedule_date_time, tv_start_time, tv_start_date, tv_end_time,
            tv_end_date, tv_label_quotation, tv_label_quotation_id, tv_label_job, tv_label_job_id, tv_complete_address, tb_label_attachment,
            nolist_txt;
    private LinearLayout ll_quote, ll_job, nolist_linear;
    private AppCompatTextView tv_recent_quote, tv_recent_job;
    private AppCompatImageView img_call, img_email;
    private Dialog enterFieldDialog;
    private Appointmnet_Deatils_PI appointmnet_deatils_pi;
    private Appointment model;
    private RecyclerView recyclerView;
    private ContentLoadingProgressBar progressBar;
    private View quote_view;
    private AppCompatButton btn_appointment_new, btn_appointment_completed;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_appointment_details2);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        setTitle(LanguageController.getInstance().getMobileMsgByKey(AppConstant.appointment_details));
        appointmnet_deatils_pi = new Appointment_Deatil_PC(this);

        initializelables();

        Intent intent = getIntent();
        if (intent.hasExtra("app_data")) {
            this.model = (Appointment) getIntent().getSerializableExtra("app_data");
            appointmnet_deatils_pi.getAppointmentAttachment(model.getAppId());
            setDataInUI(model);
        }

    }


    @Override
    public void setAppointmentAttachment(List<AppointmentAttachment> appointmentAttachments) {
        progressBar.setVisibility(View.GONE);
        if (appointmentAttachments != null && appointmentAttachments.size() > 0) {
            nolist_linear.setVisibility(View.GONE);
            attachementAdapter.setList(appointmentAttachments);
        } else {
            nolist_linear.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void sessionExpire(String message) {
        AppUtility.alertDialog(this, LanguageController.getInstance().getMobileMsgByKey(AppConstant.dialog_error_title), message, LanguageController.getInstance().getMobileMsgByKey(AppConstant.ok), "", new Callable<Boolean>() {
            @Override
            public Boolean call() throws Exception {
                EotApp.getAppinstance().sessionExpired();
                return null;
            }
        });
    }

    private void initializelables() {
        btn_appointment_new = findViewById(R.id.btn_appointment_new);
        btn_appointment_completed = findViewById(R.id.btn_appointment_completed);

        progressBar = findViewById(R.id.progressBar);
        nolist_txt = findViewById(R.id.nolist_txt);
        nolist_linear = findViewById(R.id.nolist_linear);

        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new GridLayoutManager(this, 2));
        recyclerView.setNestedScrollingEnabled(false);

        attachementAdapter = new AttachementAdapter(this);
        attachementAdapter.setFromClientHistory(true);
        attachementAdapter.setOnItemSelection(this);

        recyclerView.setAdapter(attachementAdapter);

        tb_label_attachment = findViewById(R.id.tb_label_attachment);
        tb_label_attachment.setText(LanguageController.getInstance().getMobileMsgByKey(AppConstant.document));

        tv_client_name = findViewById(R.id.tv_client_name);
        tv_complete_address = findViewById(R.id.tv_complete_address);
        tv_view_on_map = findViewById(R.id.tv_view_on_map);
        tv_view_on_map.setText(LanguageController.getInstance().getMobileMsgByKey(AppConstant.view_on_map));

        tv_label_des = findViewById(R.id.tv_label_des);
        tv_label_des.setText(LanguageController.getInstance().getMobileMsgByKey(AppConstant.description));

        tv_des = findViewById(R.id.tv_des);


        tv_lable_schedule_date_time = findViewById(R.id.tv_lable_schedule_date_time);
        tv_lable_schedule_date_time.setText(LanguageController.getInstance().getMobileMsgByKey(AppConstant.appointment_start_end));

        tv_start_time = findViewById(R.id.tv_start_time);
        tv_start_date = findViewById(R.id.tv_start_date);
        tv_end_time = findViewById(R.id.tv_end_time);
        tv_end_date = findViewById(R.id.tv_end_date);

        tv_label_quotation = findViewById(R.id.tv_label_quotation);
        tv_label_quotation.setText(LanguageController.getInstance().getMobileMsgByKey(AppConstant.quotation_label));

        tv_label_quotation_id = findViewById(R.id.tv_label_quotation_id);

        tv_label_job = findViewById(R.id.tv_label_job);
        tv_label_job.setText(LanguageController.getInstance().getMobileMsgByKey(AppConstant.job));

        tv_label_job_id = findViewById(R.id.tv_label_job_id);

        ll_quote = findViewById(R.id.ll_quote);
        ll_job = findViewById(R.id.ll_job);


        tv_recent_quote = findViewById(R.id.tv_recent_quote);
        tv_recent_quote.setText(LanguageController.getInstance().getMobileMsgByKey(AppConstant.recent_quote));

        tv_recent_job = findViewById(R.id.tv_recent_job);

        img_call = findViewById(R.id.img_call);
        img_email = findViewById(R.id.img_email);

        if (AppUtility.isInternetConnected()) {
            progressBar.setVisibility(View.VISIBLE);
            nolist_txt.setText(LanguageController.getInstance().getMobileMsgByKey(AppConstant.appointment_attach_msg));
        } else {
            progressBar.setVisibility(View.GONE);
            nolist_txt.setText(LanguageController.getInstance().getMobileMsgByKey(AppConstant.err_check_network));
            nolist_linear.setVisibility(View.VISIBLE);
        }


        img_call.setOnClickListener(this);
        img_email.setOnClickListener(this);
        tv_view_on_map.setOnClickListener(this);


    }

    private void setDataInUI(Appointment model) {
        HyperLog.i("", "setDataInUI(M) start");
        if (model != null) {


            setClientName(model);
            setViewByAppointmentStatus(Integer.parseInt(model.getStatus()));
            setQuotationDetails(model);
            setJobDetails(model);

            if (!TextUtils.isEmpty(model.getNm()))
                tv_client_name.setText(model.getNm());
            setcompleteAddress(model);

            if (TextUtils.isEmpty(model.getDes()))
                tv_des.setText(LanguageController.getInstance().getMobileMsgByKey(AppConstant.no_desc));
            else
                tv_des.setText(model.getDes());

            setScheduleDates(model);


        }

        HyperLog.i("", "setDataInUI(M) completed");

    }

    private void setQuotationDetails(Appointment model) {
        if (!TextUtils.isEmpty(model.getQuotId()) &&
                !TextUtils.isEmpty(model.getQuotLabel()) &&
                App_preference.getSharedprefInstance().getLoginRes().getCompPermission().get(0).getIsQuoteNoShowOnAppointment() != null
                && App_preference.getSharedprefInstance().getLoginRes().getCompPermission().get(0).getIsQuoteNoShowOnAppointment().equals("0")) {
            tv_recent_quote.setText(LanguageController.getInstance().getMobileMsgByKey(AppConstant.recent_quote) + " : ");
            tv_label_quotation_id.setText(model.getQuotLabel());
            {

                ll_quote.setVisibility(View.VISIBLE);
            }
        } else {
            ll_quote.setVisibility(View.GONE);
        }

    }

    private void setJobDetails(Appointment model) {
        if (model != null)
            if (!TextUtils.isEmpty(model.getJobId()) &&
                    !TextUtils.isEmpty(model.getJobLabel())) {
                tv_recent_job.setText(LanguageController.getInstance().getMobileMsgByKey(AppConstant.recent_job) + " : ");
                tv_label_job_id.setText(model.getJobLabel());
                ll_job.setVisibility(View.VISIBLE);

            } else ll_job.setVisibility(View.GONE);

    }

    private void setClientName(Appointment model) {
        if (!TextUtils.isEmpty(model.getCltId())) {
            try {
                int i = Integer.parseInt(model.getCltId());
                Client clientFromId = AppDataBase.getInMemoryDatabase(EotApp.getAppinstance())
                        .clientModel().getClientFromId(i);
                if (clientFromId != null)
                    tv_client_name.setText(clientFromId.getNm());
            } catch (NumberFormatException e) {
                e.printStackTrace();
                HyperLog.i("", "setClientName(M) exception:" + e.toString());

            }
        }

    }

    private void setcompleteAddress(Appointment model) {
        tv_complete_address.setText("");
        if (!TextUtils.isEmpty(model.getAdr()))
            tv_complete_address.setText(model.getAdr());
        if (!TextUtils.isEmpty(model.getCity()))
            tv_complete_address.append(" " + model.getCity() + ", ");
        if (!TextUtils.isEmpty(model.getState())
                && !TextUtils.isEmpty(model.getCtry())) {
            try {
                String stateName = SpinnerCountrySite.getStatenameById(model.getCtry(), model.getState());
                if (!TextUtils.isEmpty(stateName))
                    tv_complete_address.append(stateName);
                String countryId = SpinnerCountrySite.getCountryNameById(model.getCtry());
                if (!TextUtils.isEmpty(countryId))
                    tv_complete_address.append(", " + countryId);
            } catch (Exception e) {
                e.printStackTrace();
                HyperLog.i("", "setcompleteAddress(M) exception:" + e.toString());

            }
        }
    }

    private void setViewByAppointmentStatus(int sta) {
        if (sta == 9) {
            btn_appointment_completed.setVisibility(View.VISIBLE);
            btn_appointment_completed.setText(LanguageController.getInstance().getMobileMsgByKey(AppConstant.done));

        } else if (sta == 12) {
            btn_appointment_new.setVisibility(View.VISIBLE);
            btn_appointment_new.setText(LanguageController.getInstance().getMobileMsgByKey(AppConstant.status_new));

        }


    }

    private void setScheduleDates(Appointment model) {
        if (model != null && !TextUtils.isEmpty(model.getSchdlStart())) {
            try {
                long longStartTime = Long.parseLong(model.getSchdlStart());
                String timeFormat = AppUtility.getDateWithFormate(longStartTime, "hh:mm a");
                tv_start_time.setText(timeFormat);

                String dateFormat = AppUtility.getDateWithFormate(longStartTime, "dd-MMM-yyyy");
                tv_start_date.setText(dateFormat);

                long endTime = Long.parseLong(model.getSchdlFinish());
                timeFormat = AppUtility.getDateWithFormate(endTime, "hh:mm a");
                tv_end_time.setText(timeFormat);

                dateFormat = AppUtility.getDateWithFormate(endTime, "dd-MMM-yyyy");
                tv_end_date.setText(dateFormat);

            } catch (Exception ex) {
                HyperLog.i("", "setScheduleDates(M) exception:" + ex.toString());

            }
        }
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.img_call:
                raiseCall();
                break;
            case R.id.img_email:
                showEmailDialog();
                break;

            case R.id.tv_view_on_map:
                showLocation();
                break;
        }
    }


    private void raiseCall() {
        if (this.model != null && this.model.getMob1() != null) {
            if (!this.model.getMob1().equals("") || this.model.getMob2() != null && !this.model.getMob2().equals("")) {
                //frameView.setAlpha(0.3F);
                getDialogCall();
                try {
                    final TextView txtViewSkypeCon1 = enterFieldDialog.findViewById(R.id.txtViewSkypeCon1);
                    txtViewSkypeCon1.setVisibility(View.VISIBLE);
                    final SpannableString s1 = new SpannableString(this.model.getMob1().trim());
                    Linkify.addLinks(txtViewSkypeCon1, Linkify.ALL);
                    if (TextUtils.isEmpty(s1))
                        txtViewSkypeCon1.setText(LanguageController.getInstance().getMobileMsgByKey(AppConstant.no_contact_available));
                    else
                        txtViewSkypeCon1.setText(s1);

                    txtViewSkypeCon1.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            if (ContextCompat.checkSelfPermission(AppointmentHistoryDetailsActivity.this, Manifest.permission.CALL_PHONE)
                                    != PackageManager.PERMISSION_GRANTED) {

                                ActivityCompat.requestPermissions(AppointmentHistoryDetailsActivity.this, new String[]{Manifest.permission.CALL_PHONE}, MY_PERMISSIONS_REQUEST_CALL_PHONE);
                            } else if (AppUtility.isvalidPhoneNo(txtViewSkypeCon1.getText().toString())) {
                                AppUtility.getCallOnNumber(AppointmentHistoryDetailsActivity.this, txtViewSkypeCon1.getText().toString());

                            }
                        }
                    });

                    final TextView txtViewSkypeCon2 = enterFieldDialog.findViewById(R.id.txtViewSkypeCon2);
                    txtViewSkypeCon2.setVisibility(View.VISIBLE);
                    Linkify.addLinks(txtViewSkypeCon2, Linkify.ALL);
                    final SpannableString s = new SpannableString(this.model.getMob2().trim());
                    if (TextUtils.isEmpty(s))
                        txtViewSkypeCon2.setText(LanguageController.getInstance().getMobileMsgByKey(AppConstant.no_contact_available));
                    else
                        txtViewSkypeCon2.setText(s);
                    txtViewSkypeCon2.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            if (ContextCompat.checkSelfPermission(AppointmentHistoryDetailsActivity.this, Manifest.permission.CALL_PHONE)
                                    != PackageManager.PERMISSION_GRANTED) {

                                ActivityCompat.requestPermissions(AppointmentHistoryDetailsActivity.this, new String[]{Manifest.permission.CALL_PHONE}, MY_PERMISSIONS_REQUEST_CALL_PHONE);
                            } else if (AppUtility.isvalidPhoneNo(txtViewSkypeCon2.getText().toString())) {
                                AppUtility.getCallOnNumber(AppointmentHistoryDetailsActivity.this, txtViewSkypeCon2.getText().toString());

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
                        // frameView.setAlpha(1.0F);
                        enterFieldDialog.dismiss();
                    }
                });
                enterFieldDialog.show();
            } else {
                AppUtility.alertDialog(AppointmentHistoryDetailsActivity.this, LanguageController.getInstance().getMobileMsgByKey(AppConstant.dialog_alert), LanguageController.getInstance().getMobileMsgByKey(AppConstant.no_contact_available), LanguageController.getInstance().getMobileMsgByKey(AppConstant.ok), "", new Callable<Boolean>() {
                    @Override
                    public Boolean call() throws Exception {
                        return true;
                    }
                });
            }
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
            enterFieldDialog = new Dialog(AppointmentHistoryDetailsActivity.this);
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

    private void showEmailDialog() {
        if (model != null) {
            if (model.getEmail() != null && !model.getEmail().equals("")) {
                // frameView.setAlpha(0.3F);
                getDialogEmail();
                TextView txtViewSkypeCon1 = enterFieldDialog.findViewById(R.id.txt_email_popup);
                final SpannableString s1 = new SpannableString(model.getEmail().trim());
                Linkify.addLinks(s1, Linkify.WEB_URLS | Linkify.EMAIL_ADDRESSES);
                txtViewSkypeCon1.setTextIsSelectable(true);
                txtViewSkypeCon1.setMovementMethod(LinkMovementMethod.getInstance());
                if (TextUtils.isEmpty(s1))
                    txtViewSkypeCon1.setText(LanguageController.getInstance().getMobileMsgByKey(AppConstant.appoint_email_not_available));
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
                AppUtility.alertDialog(this, LanguageController.getInstance().getMobileMsgByKey(AppConstant.dialog_alert), LanguageController.getInstance().getMobileMsgByKey(AppConstant.appoint_email_not_available), LanguageController.getInstance().getMobileMsgByKey(AppConstant.ok), "", new Callable<Boolean>() {
                    @Override
                    public Boolean call() throws Exception {
                        return true;
                    }
                });
            }
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
            enterFieldDialog = new Dialog(AppointmentHistoryDetailsActivity.this);
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

    private void showLocation() {
        if (model != null) {

            String locationdata = "";
            if (!TextUtils.isEmpty(model.getLat()) && !model.getLat().equals("0") && !model.getLng().equals("0")) {
                locationdata = "http://maps.google.com/maps?daddr=" + model.getLat() + "," + model.getLng();
            } else {
                String completeAddress = model.getAdr() + " " + model.getCity() + " " + SpinnerCountrySite.getCountryNameById(model.getCtry());
                //    String searchableAddress = completeAddress.replace(" ", "+");
                locationdata = "http://maps.google.com/maps?daddr=" + completeAddress;
            }
            try {
                Intent intent = new Intent(Intent.ACTION_VIEW,
                        Uri.parse(locationdata));
                //   intent.setPackage("com.google.android.apps.maps");
                startActivity(intent);
            } catch (Exception ex) {
                ex.printStackTrace();
            }

        } else {
            AppUtility.alertDialog(AppointmentHistoryDetailsActivity.this, LanguageController.getInstance().getMobileMsgByKey(AppConstant.dialog_alert), LanguageController.getInstance().getMobileMsgByKey(AppConstant.no_location), LanguageController.getInstance().getMobileMsgByKey(AppConstant.ok), "", new Callable<Boolean>() {
                @Override
                public Boolean call() throws Exception {
                    return true;
                }
            });
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
            return true;
        }
        return true;
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }


    @Override
    public void showExportOption(boolean b) {

    }

    @Override
    public void selectAllOption(boolean b) {

    }

    @Override
    public void showDocFormatMSG() {

    }
}