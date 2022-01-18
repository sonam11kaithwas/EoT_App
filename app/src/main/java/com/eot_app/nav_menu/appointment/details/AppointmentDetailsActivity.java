package com.eot_app.nav_menu.appointment.details;

import static com.eot_app.nav_menu.jobs.job_detail.detail.DetailFragment.MY_PERMISSIONS_REQUEST_CALL_PHONE;

import android.Manifest;
import android.animation.Animator;
import android.app.Dialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.TextUtils;
import android.text.method.LinkMovementMethod;
import android.text.util.Linkify;
import android.transition.TransitionManager;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.CompoundButton;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import androidx.recyclerview.widget.GridLayoutManager;

import com.eot_app.R;
import com.eot_app.UploadDocumentActivity;
import com.eot_app.databinding.ActivityAppointmentDetailsBinding;
import com.eot_app.nav_menu.appointment.Keepar;
import com.eot_app.nav_menu.appointment.addupdate.AddAppointmentActivity;
import com.eot_app.nav_menu.appointment.addupdate.model.AppointmentUpdateReq;
import com.eot_app.nav_menu.appointment.dbappointment.Appointment;
import com.eot_app.nav_menu.appointment.details.documents.ActivityDocumentUpload;
import com.eot_app.nav_menu.appointment.details.documents.DocumentExportReq;
import com.eot_app.nav_menu.client.client_db.Client;
import com.eot_app.nav_menu.client.clientlist.client_detail.site.sitelist.editsite.editsitedb.SpinnerCountrySite;
import com.eot_app.nav_menu.jobs.add_job.Add_job_activity;
import com.eot_app.nav_menu.jobs.job_db.Job;
import com.eot_app.nav_menu.jobs.job_detail.JobDetailActivity;
import com.eot_app.nav_menu.quote.add_quotes_pkg.AddQuotes_Activity;
import com.eot_app.nav_menu.quote.quote_invoice_pkg.Quote_Invoice_Details_Activity;
import com.eot_app.services.Service_apis;
import com.eot_app.utility.AppConstant;
import com.eot_app.utility.AppUtility;
import com.eot_app.utility.App_preference;
import com.eot_app.utility.EotApp;
import com.eot_app.utility.db.AppDataBase;
import com.eot_app.utility.db.OfflineDataController;
import com.eot_app.utility.language_support.LanguageController;
import com.google.gson.Gson;
import com.hypertrack.hyperlog.HyperLog;

import java.io.File;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.Callable;

public class AppointmentDetailsActivity extends UploadDocumentActivity implements View.OnClickListener, AttachementAdapter.OnItemSelection {
    private static final int EDIT_APPOINTMENT_CODE = 10;
    private static final int UPLOADED_NEW_LIST = 148;
    private final int ADD_QUOTE_RESULT = 123;


    ActivityAppointmentDetailsBinding binding;
    AppointmentDetailsViewModel detailsViewModel;
    Appointment model = new Appointment();


    private final BroadcastReceiver mMessageReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            // Get extra data included in the Intent
            if (model != null) {
                model = AppDataBase.getInMemoryDatabase(EotApp.getAppinstance()).appointmentModel().getAppointmentById(model.getAppId());
                setJobDetails();
            }
            Log.d("appointment", "Appointment details receiver called");
        }
    };
    AttachementAdapter attachementAdapter;
    String path = "";
    boolean isFBMenuOpened;
    //custom dialog for instruction and details
    private Dialog enterFieldDialog;
    private String captureImagePath;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        detailsViewModel = ViewModelProviders.of(this).get(AppointmentDetailsViewModel.class);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_appointment_details);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle(LanguageController.getInstance().getMobileMsgByKey(AppConstant.appointment_details));

        binding.tvLabelDes.setText(LanguageController.getInstance().getMobileMsgByKey(AppConstant.description));
        binding.tvAddNew.setText(LanguageController.getInstance().getMobileMsgByKey(AppConstant.appointment_add_new_attach));
        binding.tvLableScheduleDateTime.setText(LanguageController.getInstance().getMobileMsgByKey(AppConstant.appointment_start_end));
        binding.tvViewOnMap.setText(LanguageController.getInstance().getMobileMsgByKey(AppConstant.view_on_map));
        binding.tbLabelAttachment.setText(LanguageController.getInstance().getMobileMsgByKey(AppConstant.document));
        binding.tvFabAddQuote.setText(LanguageController.getInstance().getMobileMsgByKey(AppConstant.create_quotation));
        binding.tvFabJob.setText(LanguageController.getInstance().getMobileMsgByKey(AppConstant.title_add_job));
        binding.checkboxSelectAll.setText(LanguageController.getInstance().getMobileMsgByKey(AppConstant.select_all));
        binding.tvExportAll.setText(LanguageController.getInstance().getMobileMsgByKey(AppConstant.export_document));
        binding.tvLabelQuotation.setText(LanguageController.getInstance().getMobileMsgByKey(AppConstant.quotation_label));
        binding.tvRecentQuote.setText(LanguageController.getInstance().getMobileMsgByKey(AppConstant.recent_quote));
        binding.btnAppointmentDone.setText(LanguageController.getInstance().getMobileMsgByKey(AppConstant.mark_as_done));
        binding.btnAppointmentCompleted.setText(LanguageController.getInstance().getMobileMsgByKey(AppConstant.completed));


        if (AppUtility.isInternetConnected()) {
            binding.progressBar.setVisibility(View.VISIBLE);
            binding.nolistTxt.setText(LanguageController.getInstance().getMobileMsgByKey(AppConstant.appointment_attach_msg));
        } else {
            binding.progressBar.setVisibility(View.GONE);
            binding.nolistTxt.setText(LanguageController.getInstance().getMobileMsgByKey(AppConstant.err_check_network));
            binding.nolistLinear.setVisibility(View.VISIBLE);
        }

        model = (Appointment) getIntent().getSerializableExtra("data");
        if (model != null) {
            setDataInUI(model);
        }
        binding.recyclerView.setLayoutManager(new GridLayoutManager(this, 2));
        binding.recyclerView.setNestedScrollingEnabled(false);

        attachementAdapter = new AttachementAdapter(this);
        attachementAdapter.setOnItemSelection(this);

        binding.recyclerView.setAdapter(attachementAdapter);


        detailsViewModel.fetchAppointmentDetails(model);


        detailsViewModel.getLiveAttachments().observe(this, new Observer<List<AppointmentAttachment>>() {
            @Override
            public void onChanged(@Nullable List<AppointmentAttachment> appointmentAttachments) {
                binding.progressBar.setVisibility(View.GONE);
                if (appointmentAttachments != null && appointmentAttachments.size() > 0) {
                    binding.nolistLinear.setVisibility(View.GONE);
                    attachementAdapter.setList(appointmentAttachments);
                } else {
                    binding.nolistLinear.setVisibility(View.VISIBLE);
                }
            }
        });


        detailsViewModel.getIsUploading().observe(this, new Observer<Boolean>() {
            @Override
            public void onChanged(@Nullable Boolean aBoolean) {
                if (aBoolean)
                    AppUtility.progressBarShow(AppointmentDetailsActivity.this);
                else AppUtility.progressBarDissMiss();
            }
        });

        detailsViewModel.pdfPath.observe(this, new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {
                if (s != null) {
                    try {
                        Bundle bundle = new Bundle();
                        bundle.putString("appId", model.getAppId());
                        bundle.putString("pdfPath", s);
                        DialogEmailDocument emailDocument = new DialogEmailDocument();
                        emailDocument.setArguments(bundle);
                        emailDocument.show(getSupportFragmentManager(), "emaildialog");
                        //   startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(App_preference.getSharedprefInstance().getBaseURL() + "" + s)));
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        });

        binding.checkboxSelectAll.setOnCheckedChangeListener(selectAllListener());
        binding.fab.setOnClickListener(this);
        binding.backgroundView.setOnClickListener(this);
        binding.linearFabQuote.setOnClickListener(this);
        binding.linearFabJob.setOnClickListener(this);
        binding.tvExportAll.setOnClickListener(this);

        binding.btnAppointmentDone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (model != null && !model.getTempId().equals(model.getAppId())) {
                    sendUpdateRequest();
                    TransitionManager.beginDelayedTransition(binding.parentLayout);
                    binding.btnAppointmentDone.setVisibility(View.GONE);
                    binding.btnAppointmentCompleted.setVisibility(View.VISIBLE);
                } else
                    showDialogs(LanguageController.getInstance().getMobileMsgByKey(AppConstant.appointment_not_sync));

            }
        });

        LocalBroadcastManager.getInstance(this).registerReceiver(mMessageReceiver,
                new IntentFilter("appointment_details_refresh"));
    }

    private CompoundButton.OnCheckedChangeListener selectAllListener() {
        return new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (attachementAdapter != null)
                    attachementAdapter.selectAllFiles(isChecked);
            }
        };
    }

    private void setDataInUI(final Appointment model) {
        HyperLog.i("", "setDataInUI(M) start");
        if (model != null) {
            if (!TextUtils.isEmpty(model.getStatus())) {
                if (model.getStatus().equals("9")) {
                    TransitionManager.beginDelayedTransition(binding.parentLayout);
                    binding.btnAppointmentDone.setVisibility(View.GONE);
                    binding.btnAppointmentCompleted.setVisibility(View.VISIBLE);
                } else {
                    TransitionManager.beginDelayedTransition(binding.parentLayout);
                    binding.btnAppointmentCompleted.setVisibility(View.GONE);
                    binding.btnAppointmentDone.setVisibility(View.VISIBLE);
                }


            }

            setClientName(model);

            setQuotationDetails();
            setJobDetails();

            if (!TextUtils.isEmpty(model.getNm()))
                binding.tvClientName.setText(model.getNm());
            setcompleteAddress(model);


            setScheduleDates(model);

            appoinmentAttchment(model);


            binding.imgCall.setOnClickListener(this);
            binding.imgEmail.setOnClickListener(this);
            binding.tvViewOnMap.setOnClickListener(this);
            binding.tvAddNew.setOnClickListener(this);

        }


        HyperLog.i("", "setDataInUI(M) completed");

    }

    private void appoinmentAttchment(Appointment model) {
        try {
            binding.editor.setPlaceholder(LanguageController.getInstance().getMobileMsgByKey(AppConstant.description));
            binding.editor.setTextColor(Color.parseColor("#8C9293"));

            binding.editor.setBackgroundColor(Color.TRANSPARENT);
            binding.editor.focusEditor();
            binding.editor.setInputEnabled(false);

            if (!TextUtils.isEmpty(model.getDes())) {
                // binding.tvDes.setHint(LanguageController.getInstance().getMobileMsgByKey(AppConstant.description));
                binding.editor.setHtml(model.getDes());
                //   binding.tvDes.setVisibility(View.VISIBLE);
            } else {
                //    binding.tvDes.setVisibility(View.GONE);
            }

            if (model.getDes() != null) {

            }

        } catch (Exception exception) {
            exception.getMessage();
        }
    }

    private void setQuotationDetails() {
        if (!TextUtils.isEmpty(model.getQuotId()) &&
                !TextUtils.isEmpty(model.getQuotLabel()) &&
                App_preference.getSharedprefInstance().getLoginRes().getCompPermission().get(0).getIsQuoteNoShowOnAppointment() != null
                && App_preference.getSharedprefInstance().getLoginRes().getCompPermission().get(0).getIsQuoteNoShowOnAppointment().equals("0")) {
            binding.quoteViews.setVisibility(View.VISIBLE);
            binding.tvRecentQuote.setText(LanguageController.getInstance().getMobileMsgByKey(AppConstant.recent_quote) + " : ");
            binding.tvLabelQuotationId.setText(model.getQuotLabel());
            binding.llQuote.setVisibility(View.VISIBLE);
            binding.llQuoteDetails.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent quotesinvoiceIntent = new Intent(AppointmentDetailsActivity.this, Quote_Invoice_Details_Activity.class);
                    quotesinvoiceIntent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                    quotesinvoiceIntent.putExtra("quotId", model.getQuotId());
                    startActivity(quotesinvoiceIntent);
                }
            });
        } else {
            binding.llQuote.setVisibility(View.GONE);
            binding.quoteViews.setVisibility(View.GONE);
        }


    }

    private void setJobDetails() {
        if (model != null)
            if (!TextUtils.isEmpty(model.getJobId()) &&
                    !TextUtils.isEmpty(model.getJobLabel())) {
                binding.tvRecentJob.setText(LanguageController.getInstance().getMobileMsgByKey(AppConstant.recent_job) + " : ");
                binding.tvLabelJobId.setText(model.getJobLabel());
                binding.llJob.setVisibility(View.VISIBLE);
                binding.llJobDetails.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent jobDetails = new Intent(AppointmentDetailsActivity.this, JobDetailActivity.class);
                        Job jobsById = AppDataBase.getInMemoryDatabase(EotApp.getAppinstance()).jobModel().getJobsById(model.getJobId());
                        if (jobsById != null) {
                            //    jobDetails.putExtra("JOBS", jobsById);
                            String strjob = new Gson().toJson(jobsById);
                            jobDetails.putExtra("JOBS", strjob);
                            jobDetails.putExtra("appId", model.getAppId());
                            jobDetails.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                            startActivity(jobDetails);
                        } else {
                            AppUtility.alertDialog(AppointmentDetailsActivity.this,
                                    LanguageController.getInstance().getMobileMsgByKey(AppConstant.job),
                                    LanguageController.getInstance().getMobileMsgByKey(AppConstant.job_not_fount), "",
                                    LanguageController.getInstance().getMobileMsgByKey(AppConstant.ok), null);
                        }
                    }
                });
            } else binding.llJob.setVisibility(View.GONE);

    }


    private void setClientName(Appointment model) {
        if (!TextUtils.isEmpty(model.getCltId())) {
            try {
                int i = Integer.parseInt(model.getCltId());
                Client clientFromId = AppDataBase.getInMemoryDatabase(EotApp.getAppinstance())
                        .clientModel().getClientFromId(i);
                if (clientFromId != null)
                    binding.tvClientName.setText(clientFromId.getNm());
            } catch (NumberFormatException e) {
                e.printStackTrace();
                HyperLog.i("", "setClientName(M) exception:" + e.toString());

            }
        }
    }

    private void setScheduleDates(Appointment model) {
        if (model != null && !TextUtils.isEmpty(model.getSchdlStart())) {
            try {
                long longStartTime = Long.parseLong(model.getSchdlStart());
                String timeFormat = AppUtility.getDateWithFormate(longStartTime,
                        //"hh:mm a"
                        AppUtility.dateTimeByAmPmFormate("hh:mm a", "kk:mm")
                );
                binding.tvStartTime.setText(timeFormat);

                String dateFormat = AppUtility.getDateWithFormate(longStartTime, "dd-MMM-yyyy");
                binding.tvStartDate.setText(dateFormat);

                long endTime = Long.parseLong(model.getSchdlFinish());
                timeFormat = AppUtility.getDateWithFormate(endTime,
                        //"hh:mm a"
                        AppUtility.dateTimeByAmPmFormate("hh:mm a", "kk:mm"));
                binding.tvEndTime.setText(timeFormat);

                dateFormat = AppUtility.getDateWithFormate(endTime, "dd-MMM-yyyy");
                binding.tvEndDate.setText(dateFormat);

            } catch (Exception ex) {
                HyperLog.i("", "setScheduleDates(M) exception:" + ex.toString());

            }
        }
    }

    private void setcompleteAddress(Appointment model) {
        binding.tvCompleteAddress.setText("");
        if (!TextUtils.isEmpty(model.getAdr()))
            binding.tvCompleteAddress.setText(model.getAdr());
        if (!TextUtils.isEmpty(model.getCity()))
            binding.tvCompleteAddress.append(" " + model.getCity() + ", ");
        if (!TextUtils.isEmpty(model.getState())
                && !TextUtils.isEmpty(model.getCtry())) {
            try {
                String stateName = SpinnerCountrySite.getStatenameById(model.getCtry(), model.getState());
                if (!TextUtils.isEmpty(stateName))
                    binding.tvCompleteAddress.append(stateName);
                String countryId = SpinnerCountrySite.getCountryNameById(model.getCtry());
                if (!TextUtils.isEmpty(countryId))
                    binding.tvCompleteAddress.append(", " + countryId);
            } catch (Exception e) {
                e.printStackTrace();
                HyperLog.i("", "setcompleteAddress(M) exception:" + e.toString());

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
            enterFieldDialog = new Dialog(AppointmentDetailsActivity.this);
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
            enterFieldDialog = new Dialog(AppointmentDetailsActivity.this);
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
                            if (ContextCompat.checkSelfPermission(AppointmentDetailsActivity.this, Manifest.permission.CALL_PHONE)
                                    != PackageManager.PERMISSION_GRANTED) {

                                ActivityCompat.requestPermissions(AppointmentDetailsActivity.this, new String[]{Manifest.permission.CALL_PHONE}, MY_PERMISSIONS_REQUEST_CALL_PHONE);
                            } else if (AppUtility.isvalidPhoneNo(txtViewSkypeCon1.getText().toString())) {
                                AppUtility.getCallOnNumber(AppointmentDetailsActivity.this, txtViewSkypeCon1.getText().toString());

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
                            if (ContextCompat.checkSelfPermission(AppointmentDetailsActivity.this, Manifest.permission.CALL_PHONE)
                                    != PackageManager.PERMISSION_GRANTED) {

                                ActivityCompat.requestPermissions(AppointmentDetailsActivity.this, new String[]{Manifest.permission.CALL_PHONE}, MY_PERMISSIONS_REQUEST_CALL_PHONE);
                            } else if (AppUtility.isvalidPhoneNo(txtViewSkypeCon2.getText().toString())) {
                                AppUtility.getCallOnNumber(AppointmentDetailsActivity.this, txtViewSkypeCon2.getText().toString());

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
                AppUtility.alertDialog(AppointmentDetailsActivity.this, LanguageController.getInstance().getMobileMsgByKey(AppConstant.dialog_alert), LanguageController.getInstance().getMobileMsgByKey(AppConstant.no_contact_available), LanguageController.getInstance().getMobileMsgByKey(AppConstant.ok), "", new Callable<Boolean>() {
                    @Override
                    public Boolean call() throws Exception {
                        return true;
                    }
                });
            }
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

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.quotes_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home)
            onBackPressed();
        else if (item.getItemId() == R.id.quotes_edit) {
            Intent intent = new Intent(this, AddAppointmentActivity.class);
            intent.putExtra(AddAppointmentActivity.ISINEDITMODE, true);
            intent.putExtra("appointment", model);
            intent.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
            startActivityForResult(intent, EDIT_APPOINTMENT_CODE);
        }
        return true;
    }

    @Override
    public void onBackPressed() {
        //   EotApp.getAppinstance().notifyApiObserver(Service_apis.addAppointment);
        super.onBackPressed();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {

            case R.id.backgroundView:
                closeFABMenu();
                break;
            case R.id.fab:
                if (isFBMenuOpened)
                    closeFABMenu();
                else
                    showFBButtons();
                break;
            case R.id.linearFabQuote:
                if (model != null) {
                    if (model.getTempId().equals(model.getAppId()))
                        showDialogs(LanguageController.getInstance().getMobileMsgByKey(AppConstant.appointment_not_sync));
                    else {
                        closeFABMenu();
                        Intent intent = new Intent(this, AddQuotes_Activity.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                        startActivityForResult(intent.putExtra("appointmentId", model.getAppId()), ADD_QUOTE_RESULT);
                    }
                }
                break;

            case R.id.linearFabJob:
                if (model != null) {
                    if (model.getTempId().equals(model.getAppId()))
                        showDialogs(LanguageController.getInstance().getMobileMsgByKey(AppConstant.appointment_not_sync));
                    else {
                        closeFABMenu();
                        Intent open_add_job = new Intent(AppointmentDetailsActivity.this, Add_job_activity.class);
                        open_add_job.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                        startActivity(open_add_job.putExtra("appId", model.getAppId()));
                    }
                }

                break;
            case R.id.img_call:
                raiseCall();
                break;
            case R.id.img_email:
                showEmailDialog();
                break;

            case R.id.tv_view_on_map:
                showLocation();
                break;

            case R.id.tv_add_new:
                if (model != null && model.getTempId().equals(model.getAppId())) {
                    showDialogs(LanguageController.getInstance().getMobileMsgByKey(AppConstant.appointment_not_sync));
                } else if (!AppUtility.isInternetConnected())
                    showDialogs(LanguageController.getInstance().getMobileMsgByKey(AppConstant.network_error));
                else {
                    selectFile(false);
                    //selectAttachment();
                }

                break;

            case R.id.tv_export_all:
                exportDocument();
                break;
        }

    }

    private void exportDocument() {

        if (attachementAdapter != null) {
            HyperLog.i("", "exportDocument(M) start");
            List<AppointmentAttachment> list = attachementAdapter.getList();
            if (list != null) {
                List<String> selectedFiles = new ArrayList<>();
                for (AppointmentAttachment attachment : list) {
                    if (attachment.isSelected())
                        selectedFiles.add(attachment.getAttachmentId());
                }
                DocumentExportReq req = new DocumentExportReq();
                req.setJobId(model.getAppId());
                req.setDocumentId(selectedFiles);
                detailsViewModel.exportDocumentToPDF(req);

                HyperLog.i("", "exportDocument(M) completed");

            }
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
            AppUtility.alertDialog(AppointmentDetailsActivity.this, LanguageController.getInstance().getMobileMsgByKey(AppConstant.dialog_alert), LanguageController.getInstance().getMobileMsgByKey(AppConstant.no_location), LanguageController.getInstance().getMobileMsgByKey(AppConstant.ok), "", new Callable<Boolean>() {
                @Override
                public Boolean call() throws Exception {
                    return true;
                }
            });
        }
    }


    @Override
    public void onDocumentSelected(String path, boolean isImage) {
        super.onDocumentSelected(path, isImage);
        if (path != null) {
            try {
                if (isImage) {
                    File file = new File(path);
                    if (file != null && file.exists()) {
                        Uri parse = Uri.fromFile(file);
                        openUploadDocument(parse, isImage);
                    }
                } else {
                    openUploadDocument(path, isImage);
                }
            } catch (Exception e) {
                HyperLog.d("", "Exception in appointment Details: " + e.toString());
                e.printStackTrace();
            }
        }

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        switch (requestCode) {
            case ADD_QUOTE_RESULT:
                if (data != null && data.hasExtra("quotId")) {
                    if (model != null && !TextUtils.isEmpty(model.getAppId())) {
                        model.setQuotId(data.getStringExtra("quotId"));
                        model.setQuotLabel(data.getStringExtra("label"));
                        AppDataBase.getInMemoryDatabase(EotApp.getAppinstance())
                                .appointmentModel().updateAppointment(model.getAppId(), model.getQuotId(), model.getQuotLabel());
                        setQuotationDetails();
                    }
                }
                break;


            case EDIT_APPOINTMENT_CODE:
                if (data != null) {
                    model = (Appointment) data.getSerializableExtra("appointment");
                    if (model != null)
                        setDataInUI(model);
                }

                break;

            case UPLOADED_NEW_LIST:
                if (data != null && data.hasExtra("list")) {
                    ArrayList<AppointmentAttachment> updateList = (ArrayList<AppointmentAttachment>) data.getSerializableExtra("list");
                    if (updateList != null && attachementAdapter != null) {
                        attachementAdapter.setSingleAttachment(updateList);
                        binding.nolistLinear.setVisibility(View.GONE);
                    }

                }
                break;


        }
        super.onActivityResult(requestCode, resultCode, data);
    }


    private void openUploadDocument(Uri uri, boolean isImage) {
        Intent intent = new Intent(this, ActivityDocumentUpload.class);
        intent.putExtra("uri", uri);
        intent.putExtra("isImage", isImage);
        intent.putExtra("appId", model.getAppId());
        startActivityForResult(intent, UPLOADED_NEW_LIST);
    }

    private void openUploadDocument(String uri, boolean isImage) {
        Intent intent = new Intent(this, ActivityDocumentUpload.class);
        intent.putExtra("uri", uri);
        intent.putExtra("isImage", isImage);
        intent.putExtra("appId", model.getAppId());
        startActivityForResult(intent, UPLOADED_NEW_LIST);
    }

    private void showDialogs(String msg) {
        AppUtility.error_Alert_Dialog(this, msg, LanguageController.getInstance().getMobileMsgByKey(AppConstant.ok)
                , new Callable<Boolean>() {
                    @Override
                    public Boolean call() throws Exception {
                        return null;
                    }
                });
    }


    private void showFBButtons() {
        getSupportActionBar().setBackgroundDrawable(new ColorDrawable(getResources().getColor(R.color.bg_color)));
        getSupportActionBar().setStackedBackgroundDrawable(new ColorDrawable(Color.parseColor("#90ffffff")));
        binding.backgroundView.setVisibility(View.VISIBLE);
        if (App_preference.getSharedprefInstance().getLoginRes().getRights().get(0).getIsQuoteVisible() == 0)
            binding.linearFabQuote.setVisibility(View.VISIBLE);
        binding.linearFabJob.setVisibility(View.VISIBLE);
        binding.linearFabJob.animate().translationY(getResources().getDimension(R.dimen.standard_55));
        binding.linearFabQuote.animate().translationY(getResources().getDimension(R.dimen.standard_100));
        isFBMenuOpened = true;
    }

    private void closeFABMenu() {
        isFBMenuOpened = false;
        binding.linearFabJob.animate().translationY(0);
        binding.linearFabQuote.animate().translationY(0);

        binding.linearFabQuote.animate().translationY(0).setListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animator) {

            }

            @Override
            public void onAnimationEnd(Animator animator) {
                if (!isFBMenuOpened) {
                    binding.backgroundView.setVisibility(View.GONE);
                    binding.linearFabJob.setVisibility(View.GONE);
                    binding.linearFabQuote.setVisibility(View.GONE);
                    getSupportActionBar().setBackgroundDrawable(new ColorDrawable(getResources().getColor(R.color.colorPrimary)));

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

    @Override
    public void showExportOption(boolean b) {

        if (b)
            binding.rlExportDoc.setVisibility(View.VISIBLE);
        else
            binding.rlExportDoc.setVisibility(View.GONE);

    }

    @Override
    public void selectAllOption(boolean b) {
        binding.checkboxSelectAll.setOnCheckedChangeListener(null);
        binding.checkboxSelectAll.setChecked(b);
        binding.checkboxSelectAll.setOnCheckedChangeListener(selectAllListener());
    }

    @Override
    public void showDocFormatMSG() {
        AppUtility.alertDialog(this, "", LanguageController.getInstance().getMobileMsgByKey(AppConstant.select_doc_validation), LanguageController.getInstance().getMobileMsgByKey(AppConstant.ok), "", new Callable<Boolean>() {
            @Override
            public Boolean call() throws Exception {
                return null;
            }
        });
    }

    private void sendUpdateRequest() {
        HyperLog.i("", "sendUpdateRequest(M) start");

        HyperLog.i("", "sendUpdateRequest(M) on synced appointment");
        AppointmentUpdateReq updateReq = new AppointmentUpdateReq();
        updateReq.setAppId(model.getAppId());
        updateReq.setCltId(model.getCltId());
        updateReq.setSiteId(model.getSiteId());
        updateReq.setConId(model.getConId());
        updateReq.setStatus("9");
        updateReq.setDes(model.getDes());

        try {
            updateReq.setSchdlStart(AppUtility.getDateWithFormate(Long.parseLong(model.getSchdlStart()), "yyyy-MM-dd HH:mm:ss"));
            updateReq.setSchdlFinish(AppUtility.getDateWithFormate(Long.parseLong(model.getSchdlFinish()), "yyyy-MM-dd HH:mm:ss"));
        } catch (NumberFormatException e) {
            e.printStackTrace();
            updateReq.setSchdlStart("");
            updateReq.setSchdlFinish("");
        }
        Set<String> kprs = new HashSet<>();
        if (model != null && model.getKpr() != null)
            for (Keepar keepar : model.getKpr()) {
                kprs.add(keepar.getUsrId());
                updateReq.setMemIds(kprs);
            }
        updateReq.setCtry(model.getCtry());
        updateReq.setState(model.getState());
        updateReq.setCity(model.getCity());
        updateReq.setAdr(model.getAdr());
        updateReq.setZip(model.getZip());
        updateReq.setMob1(model.getMob1());
        updateReq.setNm(model.getNm());
        updateReq.setEmail(model.getEmail());
        updateReq.setAttachCount(model.getAttachCount());

        List<String> files = new ArrayList<>();
        updateReq.setAppDoc(files);
        String s = new Gson().toJson(updateReq);
        String dateTime = AppUtility.getDateByFormat(AppConstant.DATE_TIME_FORMAT);

        //update in local DB
        model.setStatus("9");
        AppDataBase.getInMemoryDatabase(EotApp.getAppinstance()).appointmentModel().insertSingleAppointment(model);
        OfflineDataController.getInstance().addInOfflineDB(Service_apis.updateAppointment, s, dateTime);
        Intent intent = new Intent();
        intent.putExtra("isUpdate", true);
        setResult(RESULT_OK, intent);
        //refresh and mark appointment as completed on list
        LocalBroadcastManager.getInstance(this).sendBroadcast(new Intent("appointment_refresh"));
        HyperLog.i("", "sendUpdateRequest(M) Completed");

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        try {
            if (mMessageReceiver != null)
                LocalBroadcastManager.getInstance(this).unregisterReceiver(mMessageReceiver);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
