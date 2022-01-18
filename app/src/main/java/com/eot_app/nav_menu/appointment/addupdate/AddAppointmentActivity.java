package com.eot_app.nav_menu.appointment.addupdate;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.text.Editable;
import android.text.Html;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import com.eot_app.R;
import com.eot_app.UploadDocumentActivity;
import com.eot_app.databinding.ActivityAddAppointmentBinding;
import com.eot_app.eoteditor.EotEditor;
import com.eot_app.nav_menu.appointment.Keepar;
import com.eot_app.nav_menu.appointment.addupdate.model.AppointmentAddReq;
import com.eot_app.nav_menu.appointment.addupdate.model.AppointmentUpdateReq;
import com.eot_app.nav_menu.appointment.dbappointment.Appointment;
import com.eot_app.nav_menu.appointment.dbappointment.ClientCompleteAddress;
import com.eot_app.nav_menu.appointment.details.AppointmentAttachment;
import com.eot_app.nav_menu.client.client_db.Client;
import com.eot_app.nav_menu.client.clientlist.client_detail.site.sitelist.editsite.editsitedb.SpinnerCountrySite;
import com.eot_app.nav_menu.jobs.add_job.Add_job_activity;
import com.eot_app.nav_menu.jobs.add_job.adapters.DynamicClassAdapter;
import com.eot_app.nav_menu.jobs.add_job.adapters.FilterAdapter;
import com.eot_app.nav_menu.jobs.add_job.adapters.FilterCountry;
import com.eot_app.nav_menu.jobs.add_job.adapters.FilterStates;
import com.eot_app.nav_menu.jobs.job_detail.documents.PathUtils;
import com.eot_app.services.Service_apis;
import com.eot_app.utility.AppConstant;
import com.eot_app.utility.AppUtility;
import com.eot_app.utility.App_preference;
import com.eot_app.utility.Country;
import com.eot_app.utility.EotApp;
import com.eot_app.utility.States;
import com.eot_app.utility.db.AppDataBase;
import com.eot_app.utility.db.OfflineDataController;
import com.eot_app.utility.language_support.LanguageController;
import com.eot_app.utility.language_support.Language_Preference;
import com.eot_app.utility.settings.setting_db.FieldWorker;
import com.eot_app.utility.settings.setting_db.Offlinetable;
import com.eot_app.utility.util_interfaces.Callback_AlertDialog;
import com.google.gson.Gson;
import com.hypertrack.hyperlog.HyperLog;

import org.jsoup.Jsoup;
import org.jsoup.select.Elements;

import java.io.File;
import java.io.Serializable;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Set;
import java.util.concurrent.Callable;
import java.util.concurrent.TimeUnit;

public class AddAppointmentActivity extends UploadDocumentActivity implements TextWatcher, View.OnClickListener {
    public static final String APPID = "appid";
    public static final String ISINEDITMODE = "editmode";
    final Set<String> listwork = new HashSet<>();
    String siteId = "";
    String conId = "";
    Appointment appointment;
    List<AppointmentAttachment> localAttachmentList = new ArrayList<>();
    List<FieldWorker> fieldWorkerlist;
    String path = "";
    private AppointmentViewModel appointmentViewModel;
    private ActivityAddAppointmentBinding binding;
    private String ctry_id = "";
    private String state_id = "";
    private int year, month, day, mHour, mMinute;
    private String date_str, time_str, date_en, time_en, schdlStart, schdlFinish;
    private boolean isInEditMode;
    private String appId;
    private String new_cnm = "";
    private String cltId = "";
    private String captureImagePath;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        appointmentViewModel = ViewModelProviders.of(this)
                .get(AppointmentViewModel.class);

        binding = DataBindingUtil.setContentView(this, R.layout.activity_add_appointment);
        binding.setLifecycleOwner(this);
        binding.setAppointment(appointmentViewModel);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle(LanguageController.getInstance().getMobileMsgByKey(AppConstant.add_appointment));


        if (getIntent() != null && getIntent().hasExtra(ISINEDITMODE)) {
            isInEditMode = true;
            getSupportActionBar().setTitle(LanguageController.getInstance().getMobileMsgByKey(AppConstant.appointment_edit));
            appointment = (Appointment) getIntent().getSerializableExtra("appointment");
        }

        initializelables();


        binding.editor.setPlaceholder(LanguageController.getInstance().getMobileMsgByKey(AppConstant.description));
        binding.editor.setTextColor(Color.parseColor("#8C9293"));

        binding.jobDesc.setHint(LanguageController.getInstance().getMobileMsgByKey(AppConstant.description));

        binding.editor.setOnTextChangeListener(new EotEditor.OnTextChangeListener() {
            @Override
            public void onTextChange(String text) {
                if (text.length() >= 1) {
                    binding.jobDesc.setVisibility(View.VISIBLE);
                } else {
                    binding.jobDesc.setVisibility(View.INVISIBLE);
                }
            }
        });

        binding.editor.setBackgroundColor(Color.TRANSPARENT);
        binding.editor.focusEditor();

        binding.editor.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    binding.jobDesc.setVisibility(View.VISIBLE);
                } else {
                    if (binding.editor.getHtml() != null) {
                        if (binding.editor.getHtml().length() != 0) {
                            binding.jobDesc.setVisibility(View.VISIBLE);
                        } else
                            binding.jobDesc.setVisibility(View.INVISIBLE);
                    } else
                        binding.jobDesc.setVisibility(View.INVISIBLE);
                }
            }
        });

        binding.actionInsertImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                binding.editor.focusEditor();
                selectFile(true);
            }
        });


        textInputLayoutHint();


        appointmentViewModel.getServerMessage().observe(this, new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {
                if (!TextUtils.isEmpty(s)) {
                    Toast.makeText(AddAppointmentActivity.this, s, Toast.LENGTH_SHORT).show();
                }
            }
        });

        appointmentViewModel.getFinishActivity().observe(this, new Observer<Boolean>() {
            @Override
            public void onChanged(@Nullable Boolean aBoolean) {
                if (aBoolean) {
                    setResult(RESULT_OK, new Intent());
                    finish();
                }
            }
        });

    }

    @Override
    public void onDocumentSelected(String path, boolean isImage) {
        super.onDocumentSelected(path, isImage);
        if (!TextUtils.isEmpty(path) && isImage && binding.editor != null) {
            try {
                AppointmentAttachment
                        attachment = new AppointmentAttachment();
                attachment.setType(1);
                attachment.setAttachFileActualName(path);
                localAttachmentList.add(0, attachment);
            } catch (Exception exception) {
                exception.getMessage();
            }


            binding.editor.getSettings().setAllowFileAccess(true);
            binding.editor.insertImage(path, "logo", 320, 250);

        }

    }

    private void textInputLayoutHint() {
        AppUtility.setupUI(binding.relativeMain, AddAppointmentActivity.this);

        //  binding.inputLayoutAppointmentName.getEditText().addTextChangedListener(this);
        binding.inputLayoutDesJob.getEditText().addTextChangedListener(this);
        binding.inputLayoutMobile.getEditText().addTextChangedListener(this);
        binding.jobMob2Layout.getEditText().addTextChangedListener(this);
        binding.jobAdrLayout.getEditText().addTextChangedListener(this);
        binding.jobCityLayout.getEditText().addTextChangedListener(this);
        binding.jobLngLayout.getEditText().addTextChangedListener(this);
        binding.jobEmailLayout.getEditText().addTextChangedListener(this);
        binding.jobCityLayout.getEditText().addTextChangedListener(this);
        binding.jobPostalLayout.getEditText().addTextChangedListener(this);
        //   binding.jobStateLayout.getEditText().addTextChangedListener(this);
        binding.landmarkLayout.getEditText().addTextChangedListener(this);

        if (isInEditMode) preFilledForm();
        else {
            FieldWorker yourId = getDefaultFieldWorker(App_preference.getSharedprefInstance().getLoginRes().getUsrId());
            if (yourId != null) {
                addChips(yourId);
                binding.linearMainView.setVisibility(View.VISIBLE);
            }
        }
    }

    private void preFilledForm() {
        HyperLog.i("", "preFilledForm(M) start");
        // Appointment appointment = (Appointment) getIntent().getSerializableExtra("appointment");
        if (appointment == null) return;


        if (TextUtils.isEmpty(appointment.getNm()))
            setClientName(appointment);
        else
            binding.autoClient.setText(appointment.getNm());
        binding.autoCountry.setText(SpinnerCountrySite.getCountryNameById(appointment.getCtry()));
        binding.autoStates.setText(SpinnerCountrySite.getStatenameById(appointment.getCtry(), appointment.getState()));
        binding.adderes.setText(appointment.getAdr());
        binding.city.setText(appointment.getCity());
        binding.postCode.setText(appointment.getZip());
        binding.jobDesc.setText(appointment.getDes());
        binding.email.setText(appointment.getEmail());
        binding.mobNo.setText(appointment.getMob1());
        binding.cbFutureClient.setVisibility(View.GONE);

        cltId = appointment.getCltId();
        conId = appointment.getConId();
        ctry_id = appointment.getCtry();
        state_id = appointment.getState();
        List<Keepar> kprList = appointment.getKpr();
        if (kprList != null) {
            for (Keepar kpr : kprList) {
                FieldWorker yourId = getDefaultFieldWorker(kpr.getUsrId());
                if (yourId != null) {
                    addChips(yourId);
                }
            }
            binding.linearMainView.setVisibility(View.VISIBLE);
        }


        binding.autoClient.setEnabled(false);
        binding.city.setEnabled(true);
        binding.adderes.setEnabled(true);
        binding.postCode.setEnabled(true);
        binding.email.setEnabled(true);
        binding.mobNo.setEnabled(true);
        binding.autoCountry.setEnabled(false);
        binding.autoStates.setEnabled(false);


        try {
            long longStartTime = Long.parseLong(appointment.getSchdlStart());
            String timeFormat = AppUtility.getDateWithFormate(longStartTime, AppUtility.dateTimeByAmPmFormate("hh:mm a",
                    "kk:mm"));
            binding.timeStart.setText(timeFormat);
            time_str = timeFormat;

            String dateFormat = AppUtility.getDateWithFormate(longStartTime, "dd-MM-yyyy");
            binding.dateStart.setText(dateFormat);
            date_str = dateFormat;

            long endTime = Long.parseLong(appointment.getSchdlFinish());
            timeFormat = AppUtility.getDateWithFormate(endTime, AppUtility
                    .dateTimeByAmPmFormate("hh:mm a", "kk:mm"));
            binding.timeEnd.setText(timeFormat);
            time_en = timeFormat;

            dateFormat = AppUtility.getDateWithFormate(endTime, "dd-MM-yyyy");
            binding.dateEnd.setText(dateFormat);
            date_en = dateFormat;


            try {
                if (!TextUtils.isEmpty(this.appointment.getDes())) {
                    binding.jobdeshint.setVisibility(View.VISIBLE);
                    binding.editor.setHtml(this.appointment.getDes());
                } else binding.jobdeshint.setVisibility(View.GONE);


                if (this.appointment.getDes() != null) {
//                    Spannable spannableHtmlWithImageGetter = PicassoImageGetter.getSpannableHtmlWithImageGetter(binding.jobdeshint,
//                            this.appointment.getDes());

//                    PicassoImageGetter.setClickListenerOnHtmlImageGetter(spannableHtmlWithImageGetter, new PicassoImageGetter.Callback() {
//                        @Override
//                        public void onImageClick(String imageUrl) {
//                            if (!TextUtils.isEmpty(imageUrl)) {
//                                startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(imageUrl)));
//                            }
//                        }
//                    });
//                    binding.jobdeshint.setText(spannableHtmlWithImageGetter);
//                    binding.jobdeshint.setMovementMethod(LinkMovementMethod.getInstance());
                }
            } catch (Exception exception) {
                exception.getMessage();
            }


        } catch (Exception ex) {
            HyperLog.i("", "preFilledForm(M) exception:" + ex.toString());

        }
        HyperLog.i("", "preFilledForm(M) completed");

    }

    private void initializelables() {


        if (Language_Preference.getSharedprefInstance().getlanguageFilename().equals("iw")) {
            RelativeLayout.LayoutParams params2 = (RelativeLayout.LayoutParams) binding.cbFutureClient.getLayoutParams();
            params2.addRule(RelativeLayout.ALIGN_PARENT_LEFT);
            binding.cbFutureClient.setLayoutParams(params2);

        } else {
            RelativeLayout.LayoutParams params2 = (RelativeLayout.LayoutParams) binding.cbFutureClient.getLayoutParams();
            params2.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
            binding.cbFutureClient.setLayoutParams(params2);
        }
        binding.cbFutureClient.setText(LanguageController.getInstance().getMobileMsgByKey(AppConstant.save_for_future_use));

        binding.autoClient.setHint(LanguageController.getInstance().getMobileMsgByKey(AppConstant.client_mand) + " *");

        binding.autoCountry.setHint(LanguageController.getInstance().getMobileMsgByKey(AppConstant.country));

        binding.autoStates.setHint(LanguageController.getInstance().getMobileMsgByKey(AppConstant.state));

        binding.members.setHint(LanguageController.getInstance().getMobileMsgByKey(AppConstant.add_fieldworker));

        //  binding.tagLable.setHint(LanguageController.getInstance().getMobileMsgByKey(AppConstant.tag));

        // binding.etTag.setHint(LanguageController.getInstance().getMobileMsgByKey(AppConstant.add_tag));

        // binding.jobDesc.setHint(LanguageController.getInstance().getMobileMsgByKey(AppConstant.description));
        // binding.appointmentName.setHint("Appointment Name");


        binding.mobNo.setHint(LanguageController.getInstance().getMobileMsgByKey(AppConstant.mob_no));

        binding.atMob.setHint(LanguageController.getInstance().getMobileMsgByKey(AppConstant.alt_mobile_number));

        binding.email.setHint(LanguageController.getInstance().getMobileMsgByKey(AppConstant.client_email));

        binding.adderes.setHint(LanguageController.getInstance().getMobileMsgByKey(AppConstant.address));

        binding.edtLat.setHint(LanguageController.getInstance().getMobileMsgByKey(AppConstant.latitude));

        binding.edtLng.setHint(LanguageController.getInstance().getMobileMsgByKey(AppConstant.longitued));

        binding.city.setHint(LanguageController.getInstance().getMobileMsgByKey(AppConstant.city));

        binding.postCode.setHint(LanguageController.getInstance().getMobileMsgByKey(AppConstant.postal_code));

        binding.tvHeadStart.setText(LanguageController.getInstance().getMobileMsgByKey(AppConstant.start_date_and_time));

        binding.tvHeadEnd.setText(LanguageController.getInstance().getMobileMsgByKey(AppConstant.end_date_and_time));

        binding.dateStart.setHint(LanguageController.getInstance().getMobileMsgByKey(AppConstant.date_form));

        binding.dateEnd.setHint(LanguageController.getInstance().getMobileMsgByKey(AppConstant.date_form));

        binding.timeStart.setHint(LanguageController.getInstance().getMobileMsgByKey(AppConstant.time_form));

        binding.timeEnd.setHint(LanguageController.getInstance().getMobileMsgByKey(AppConstant.time_form));


        binding.dateTimeClearBtn.setText(LanguageController.getInstance().getMobileMsgByKey(AppConstant.clear));

        binding.assignTo.setHint(LanguageController.getInstance().getMobileMsgByKey(AppConstant.assign_to));


        binding.tvLableScheduleDateTime.setText(LanguageController.getInstance().getMobileMsgByKey(AppConstant.appointment_schedule));
        binding.schelStart.setHint(LanguageController.getInstance().getMobileMsgByKey(AppConstant.shdl_start));
        binding.schelEnd.setHint(LanguageController.getInstance().getMobileMsgByKey(AppConstant.shdl_end));
        binding.landmarkEdt.setHint(LanguageController.getInstance().getMobileMsgByKey(AppConstant.landmark_addjob));

        if (isInEditMode)
            binding.submitBtn.setText(LanguageController.getInstance().getMobileMsgByKey(AppConstant.update_appointment));
        else
            binding.submitBtn.setText(LanguageController.getInstance().getMobileMsgByKey(AppConstant.add_appointment));

        binding.jobdeshint.setHint(LanguageController.getInstance().getMobileMsgByKey(AppConstant.description));

        //  binding.dateStart.setOnClickListener(this);
        //binding.dateEnd.setOnClickListener(this);
        // binding.timeStart.setOnClickListener(this);
        // binding.timeEnd.setOnClickListener(this);
        binding.llDateStart.setOnClickListener(this);
        binding.llDateEnd.setOnClickListener(this);
        binding.assigntoLinear.setOnClickListener(this);
        // binding.appointmentAttachment.setOnClickListener(this);

        binding.submitBtn.setOnClickListener(this);

        binding.inputLayoutClient.setHintEnabled(true);

        binding.cbFutureClient.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    binding.adderes.setHint(LanguageController.getInstance().getMobileMsgByKey(AppConstant.address) + " *");
                    binding.autoCountry.setHint(LanguageController.getInstance().getMobileMsgByKey(AppConstant.country) + " *");
                    binding.autoStates.setHint(LanguageController.getInstance().getMobileMsgByKey(AppConstant.state) + " *");

                } else {
                    binding.adderes.setHint(LanguageController.getInstance().getMobileMsgByKey(AppConstant.address));
                    binding.autoCountry.setHint(LanguageController.getInstance().getMobileMsgByKey(AppConstant.country));
                    binding.autoStates.setHint(LanguageController.getInstance().getMobileMsgByKey(AppConstant.state));

                }
            }
        });


        setCountryList();
        getCurrentdateTime();
        setClientlist();
        setWorkerList();
        setCompanySettingAdrs();


    }

    private void setCompanySettingAdrs() {
        binding.autoCountry.setText(SpinnerCountrySite.getCountryNameById(App_preference.getSharedprefInstance().getCompanySettingsDetails().getCtry()));
        setStateList((App_preference.getSharedprefInstance().getCompanySettingsDetails().getCtry()));
        binding.jobStateLayout.setHintEnabled(true);
        binding.autoStates.setText(SpinnerCountrySite.getStatenameById(App_preference.getSharedprefInstance().getCompanySettingsDetails().getCtry(), App_preference.getSharedprefInstance().getCompanySettingsDetails().getState()));
        //   city.setText(App_preference.getSharedprefInstance().getCompanySettingsDetails().getCity());
        state_id = (App_preference.getSharedprefInstance().getCompanySettingsDetails().getState());
        ctry_id = (App_preference.getSharedprefInstance().getCompanySettingsDetails().getCtry());
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

    private void setWorkerList() {
        fieldWorkerlist = AppDataBase.getInMemoryDatabase(EotApp.getAppinstance()).fieldWorkerModel().getFieldWorkerlist();
        if (fieldWorkerlist != null) {
            AppUtility.autocompletetextviewPopUpWindow(binding.members);
            binding.members.setThreshold(1);
            DynamicClassAdapter myClassAdapter = new DynamicClassAdapter<FieldWorker>(this, R.layout.custom_adapter_item_layout, R.id.item_title_name, fieldWorkerlist);
            binding.members.setAdapter(myClassAdapter);
            binding.members.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                    if (!listwork.contains((((FieldWorker) adapterView.getItemAtPosition(i)).getUsrId()))) {
                        addChips((FieldWorker) adapterView.getItemAtPosition(i));
                    }
                    binding.linearMainView.setVisibility(View.VISIBLE);
                    binding.members.setText("");
                }
            });
        }
    }

    private void addChips(final FieldWorker itemAtPosition) {
//        add id in list
        listwork.add(itemAtPosition.getUsrId());
        try {
            LayoutInflater vi = (LayoutInflater) getApplicationContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View v = vi.inflate(R.layout.member_item_view, null);

            TextView textView = v.findViewById(R.id.memberName);
            textView.setText(itemAtPosition.getFnm());
            ImageView deleteMember = v.findViewById(R.id.deleteMember);
            deleteMember.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    binding.linearMainView.removeView((LinearLayout) view.getParent());
                    if (binding.linearMainView.getChildCount() <= 0) {
                        binding.linearMainView.setVisibility(View.GONE);
                    }
                    listwork.remove(itemAtPosition.getUsrId());
                }
            });


            binding.linearMainView.addView(v, 0, new ViewGroup.LayoutParams(ViewGroup.LayoutParams.FILL_PARENT, ViewGroup.LayoutParams.FILL_PARENT));

        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public void setClientlist() {
        final List<Client> data = AppDataBase.getInMemoryDatabase(EotApp.getAppinstance()).clientModel().getActiveClientList();
        AppUtility.autocompletetextviewPopUpWindow(binding.autoClient);
        FilterAdapter filter = new FilterAdapter(this, R.layout.custom_adapter_item_layout, (ArrayList<Client>) data);
        binding.autoClient.setAdapter(filter);
        binding.autoClient.setThreshold(2);
        binding.autoClient.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (!data.contains(charSequence)) {
                    new_cnm = charSequence.toString();
                    cltId = "";
                    addNewClient();
                }

                if (charSequence.length() >= 1) {
                    binding.llContactData.setVisibility(View.VISIBLE);
                    binding.inputLayoutClient.setHintEnabled(true);
                    if (charSequence.length() >= 3) {
                        binding.cbFutureClient.setVisibility(View.VISIBLE);
                    }
                } else if (charSequence.length() <= 0) {
                    binding.llContactData.setVisibility(View.GONE);
                    binding.inputLayoutClient.setHintEnabled(false);
                }

            }

            @Override
            public void afterTextChanged(Editable editable) {
            }
        });

        binding.autoClient.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                binding.cbFutureClient.setVisibility(View.GONE);
                Client selectedClient = ((Client) adapterView.getItemAtPosition(i));
                AddAppointmentActivity.this.cltId = selectedClient.getCltId();
                new_cnm = "";
                setContactList(selectedClient.getCltId());
            }
        });
    }

    private void addNewClient() {
        HyperLog.i("", "addNewClient(M) start");

        if (new_cnm.length() > 0) {
            binding.city.setEnabled(true);
            binding.adderes.setEnabled(true);
            binding.postCode.setEnabled(true);
            binding.email.setEnabled(true);
            binding.email.setEnabled(true);
            binding.mobNo.setEnabled(true);
            binding.autoCountry.setEnabled(true);
            binding.autoStates.setEnabled(true);

            // binding.autoStates.setText("");
            //binding.autoCountry.setText("");
            binding.city.setText("");
            binding.adderes.setText("");
            binding.postCode.setText("");
            binding.mobNo.setText("");
            binding.email.setText("");
            siteId = "";
            conId = "";
        } else {
            binding.cbFutureClient.setVisibility(View.GONE);
            binding.cbFutureClient.setChecked(false);
        }

        HyperLog.i("", "addNewClient(M) completed");

    }

    public void setContactList(String cltId) {
        ClientCompleteAddress completeAddress = AppDataBase.getInMemoryDatabase(EotApp.getAppinstance()).appointmentModel().getCompleteAddress(cltId);
        if (completeAddress != null) {
            siteId = completeAddress.getSiteId();
            conId = completeAddress.getConId();
            binding.city.setEnabled(true);
            binding.adderes.setEnabled(true);
            binding.postCode.setEnabled(true);
            binding.email.setEnabled(true);
            binding.mobNo.setEnabled(true);
            binding.autoCountry.setEnabled(false);
            binding.autoStates.setEnabled(false);
            binding.city.setText(completeAddress.getCity());
            binding.adderes.setText(completeAddress.getAdr());
            binding.postCode.setText(completeAddress.getZip());
            binding.email.setText(completeAddress.getEmail());
            binding.mobNo.setText(completeAddress.getMob1());
            ctry_id = completeAddress.getCtry();
            state_id = completeAddress.getState();
            if (!TextUtils.isEmpty(completeAddress.getState()) && !TextUtils.isEmpty(completeAddress.getCtry())) {
                binding.autoCountry.setText(SpinnerCountrySite.getCountryNameById(completeAddress.getCtry()));
                binding.autoStates.setText(SpinnerCountrySite.getStatenameById((completeAddress.getCtry()), completeAddress.getState()));

            }
        }
    }

    private String getTimeStampFromFormatedDate(String schdlStart) {
        SimpleDateFormat gettingfmt = new SimpleDateFormat("dd-MM-yyyy hh:mm a"
                , Locale.US);
        try {
            Date formated = gettingfmt.parse(schdlStart);
            long l = formated.getTime() / 1000;
            String str = AppUtility.getDateWithFormate(l, "yyyy-MM-dd HH:mm:ss"
            );
            return str;
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return "";
    }

    private String getLongTimeStamp(String schdlStart) {
        SimpleDateFormat gettingfmt = new SimpleDateFormat(AppUtility.dateTimeByAmPmFormate(
                "dd-MM-yyyy hh:mm a", "dd-MM-yyyy kk:mm"), Locale.US);
        try {
            Date formated = gettingfmt.parse(schdlStart);
            long l = formated.getTime() / 1000;
            String str = l + "";
            return str;
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return "";
    }

    private AppointmentAddReq addAppointment() {
        HyperLog.i("", "addAppointment(M) start");

        if (date_str.isEmpty()) {
            schdlStart = schdlFinish = "";
        } else {
            schdlStart = date_str + " " + time_str;
            schdlFinish = date_en + " " + time_en;


            if (!schdlStart.equals("") && (App_preference.getSharedprefInstance().getLoginRes().getIs24hrFormatEnable()
                    != null && App_preference.getSharedprefInstance().getLoginRes().getIs24hrFormatEnable().equals("1"))) {
                Date startDate = null, endDate = null;
                String[] words = time_str.split(":");
                int t1 = Integer.valueOf(words[0]);
                String[] words2 = time_en.split(":");
                int t2 = Integer.valueOf(words2[0]);
                try {
                    if (t1 != 12) {
                        startDate = new SimpleDateFormat("hh:mm", Locale.getDefault()).parse(time_str);
                        time_str = "";
                        time_str = new SimpleDateFormat("hh:mm a", Locale.getDefault()).format(startDate);
                    } else {
                        time_str = "";
                        time_str = binding.timeStart.getText().toString() + " " + "PM";
                    }
                    //time_str = time_str + " " + "PM";}
                    if (t2 != 12) {
                        endDate = new SimpleDateFormat("hh:mm", Locale.getDefault()).parse(time_en);
                        time_en = "";
                        time_en = new SimpleDateFormat("hh:mm a", Locale.getDefault()).format(endDate);
                    } else {
                        time_en = "";
                        time_en = binding.timeEnd.getText().toString() + " " + "PM";
                        //   time_en = time_en + " " + "PM";
                    }
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                schdlStart = date_str + " " + time_str;
                schdlFinish = date_en + " " + time_en;
            }
        }
        final AppointmentAddReq model = new AppointmentAddReq();
        model.setTempId(AppUtility.getTempIdFormat("Appointment"));
        model.setCltId(cltId);
        model.setConId(conId);
        model.setSiteId(siteId);
        if (TextUtils.isEmpty(cltId)) {
            model.setNm(new_cnm);
        }
        model.setDes(binding.jobDesc.getText().toString());
        model.setSchdlStart(getTimeStampFromFormatedDate(schdlStart));
        model.setSchdlFinish(getTimeStampFromFormatedDate(schdlFinish));
        model.setEmail(binding.email.getText().toString());
        model.setMob1(binding.mobNo.getText().toString());
        model.setAdr(binding.adderes.getText().toString());
        model.setCity(binding.city.getText().toString());

        if (TextUtils.isEmpty(ctry_id)) {
            model.setCtry(App_preference.getSharedprefInstance().getCompanySettingsDetails().getCtry());

        } else
            model.setCtry(ctry_id);

        if (TextUtils.isEmpty(state_id)) {
            model.setState(App_preference.getSharedprefInstance().getCompanySettingsDetails().getState());

        } else
            model.setState(state_id);

        model.setZip(binding.postCode.getText().toString());
        model.setMemIds(listwork);

        if (binding.cbFutureClient.isChecked()) {
            model.setClientForFuture("1");
            model.setContactForFuture("1");
            model.setSiteForFuture("1");
        } else {
            model.setClientForFuture("0");
            model.setContactForFuture("0");
            model.setSiteForFuture("0");
        }

        model.setCnm("self");
        model.setSnm("self");

        if (!siteId.isEmpty())
            model.setSnm("");
        if (!conId.isEmpty())
            model.setCnm("");


//        if (localAttachmentList != null && localAttachmentList.size() > 0) {
//            List<String> filePaths = new ArrayList<>();
//            for (AppointmentAttachment at : localAttachmentList) {
//                filePaths.add(at.getAttachFileActualName());
//            }
//            model.setAppDoc(filePaths);
//        }


        ArrayList links = new ArrayList();
        List<String> fileNames = new ArrayList<>();
        String jobDescription = binding.editor.getHtml();
        if (!TextUtils.isEmpty(jobDescription)) {
            Elements srcs = Jsoup.parse(jobDescription).select("[src]"); //get All tags containing "src"
            for (int i = 0; i < srcs.size(); i++) {
                if (srcs.get(i).attributes() != null) {
                    String link = null;
                    link = srcs.get(i).attributes().get("src");// get links of selected tags
                    links.add(link);
                    String filename = link.substring(link.lastIndexOf("/") + 1);
                    fileNames.add(filename);
                    jobDescription = jobDescription.replace(link, "_jobAttSeq_" + i + "_");
                }

            }
            model.setDes("<p>" + jobDescription + "</p>");

        }


        if (links != null && links.size() > 0) {
            if (AppUtility.isInternetConnected()) {
                model.setAppDoc(links);
                model.setFileNames(fileNames);
            } else {
                AppUtility.alertDialog2(AddAppointmentActivity.this,
                        LanguageController.getInstance().getMobileMsgByKey(AppConstant.add_new_apppinment),
                        LanguageController.getInstance().getMobileMsgByKey(AppConstant.add_appoinment_offline_msg),
                        LanguageController.getInstance().getMobileMsgByKey(AppConstant.yes),
                        LanguageController.getInstance().getMobileMsgByKey(AppConstant.no), new Callback_AlertDialog() {
                            @Override
                            public void onPossitiveCall() {
                                String des = model.getDes();
                                if (!TextUtils.isEmpty(des))
                                    model.setDes(Html.fromHtml(des).toString());
                                if (model.getAppDoc() != null && model.getAppDoc().size() > 0)
                                    model.getAppDoc().clear();
                            }

                            @Override
                            public void onNegativeCall() {

                            }
                        });
            }

        }


        HyperLog.i("", "addAppointment(M) completed");

        return model;

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home)
            onBackPressed();
        return true;
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence charSequence, int start, int before, int count) {
        /**Floating hint enbale after text enter**/
        if (charSequence.length() >= 1) {
           /* if (charSequence.hashCode() == binding.appointmentName.getText().hashCode())
                binding.inputLayoutAppointmentName.setHintEnabled(true);*/
            if (charSequence.hashCode() == binding.jobDesc.getText().hashCode())
                binding.inputLayoutDesJob.setHintEnabled(true);
            if (charSequence.hashCode() == binding.mobNo.getText().hashCode())
                binding.inputLayoutMobile.setHintEnabled(true);
            if (charSequence.hashCode() == binding.atMob.getText().hashCode())
                binding.jobMob2Layout.setHintEnabled(true);
            if (charSequence.hashCode() == binding.email.getText().hashCode())
                binding.jobEmailLayout.setHintEnabled(true);
            if (charSequence.hashCode() == binding.adderes.getText().hashCode())
                binding.jobAdrLayout.setHintEnabled(true);
            if (charSequence.hashCode() == binding.edtLat.getText().hashCode())
                binding.jobLatLayout.setHintEnabled(true);
            if (charSequence.hashCode() == binding.edtLng.getText().hashCode())
                binding.jobLngLayout.setHintEnabled(true);
            if (charSequence.hashCode() == binding.city.getText().hashCode())
                binding.jobCityLayout.setHintEnabled(true);
            if (charSequence.hashCode() == binding.postCode.getText().hashCode())
                binding.jobPostalLayout.setHintEnabled(true);
            if (charSequence.hashCode() == binding.landmarkEdt.getText().hashCode())
                binding.landmarkLayout.setHintEnabled(true);

        } else if (charSequence.length() <= 0) {
            /**Floating hint Disable after text enter**/
           /* if (charSequence.hashCode() == binding.appointmentName.getText().hashCode())
                binding.inputLayoutAppointmentName.setHintEnabled(false);*/
            if (charSequence.hashCode() == binding.jobDesc.getText().hashCode())
                binding.inputLayoutDesJob.setHintEnabled(false);
            if (charSequence.hashCode() == binding.mobNo.getText().hashCode())
                binding.inputLayoutMobile.setHintEnabled(false);
            if (charSequence.hashCode() == binding.atMob.getText().hashCode())
                binding.jobMob2Layout.setHintEnabled(false);
            if (charSequence.hashCode() == binding.email.getText().hashCode())
                binding.jobEmailLayout.setHintEnabled(false);
            if (charSequence.hashCode() == binding.adderes.getText().hashCode())
                binding.jobAdrLayout.setHintEnabled(false);
            if (charSequence.hashCode() == binding.edtLat.getText().hashCode())
                binding.jobLatLayout.setHintEnabled(false);
            if (charSequence.hashCode() == binding.edtLng.getText().hashCode())
                binding.jobLngLayout.setHintEnabled(false);
            if (charSequence.hashCode() == binding.city.getText().hashCode())
                binding.jobCityLayout.setHintEnabled(false);
            if (charSequence.hashCode() == binding.postCode.getText().hashCode())
                binding.jobPostalLayout.setHintEnabled(false);
            if (charSequence.hashCode() == binding.landmarkEdt.getText().hashCode())
                binding.landmarkLayout.setHintEnabled(false);
        }
    }

    @Override
    public void afterTextChanged(Editable s) {

    }

    private void setCountryList() {
        List<Country> countryList = SpinnerCountrySite.clientCountryList();// clientCountryList("countries.json");

        AppUtility.autocompletetextviewPopUpWindow(binding.autoCountry);
        final FilterCountry countryAdapter = new FilterCountry(this, R.layout.custom_adapter_item_layout, (ArrayList<Country>) countryList);
        binding.autoCountry.setAdapter(countryAdapter);
        binding.autoCountry.setThreshold(1);
        binding.autoCountry.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                ctry_id = ((Country) adapterView.getItemAtPosition(i)).getId();
                setStateList(ctry_id);
                binding.jobCountryLayout.setHintEnabled(true);
            }
        });

        binding.autoCountry.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (charSequence.length() >= 1) {
                    binding.jobCountryLayout.setHintEnabled(true);
                } else if (charSequence.length() <= 0) {
                    binding.jobCountryLayout.setHintEnabled(false);
                }

                if (TextUtils.isEmpty(cltId)) {
                    binding.autoStates.setText("");
                    ctry_id = "";
                    state_id = "";
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
    }

    public void setStateList(String countyId) {
        List<States> statesList = SpinnerCountrySite.clientStatesList(countyId);//clientStatesList("states.json", countyId);

        FilterStates stateAdapter = new FilterStates(this, R.layout.custom_adapter_item_layout, (ArrayList<States>) statesList);
        binding.autoStates.setAdapter(stateAdapter);
        binding.autoCountry.setThreshold(0);
        binding.autoStates.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                state_id = ((States) adapterView.getItemAtPosition(i)).getId();
                // binding.jobStateLayout.setHintEnabled(true);
            }
        });


        binding.autoStates.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (charSequence.length() >= 1) {
                    //    binding.jobStateLayout.setHintEnabled(true);
                } else if (charSequence.length() <= 0) {
                    //   binding.jobStateLayout.setHintEnabled(false);
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
    }

    public void getCurrentdateTime() {
        String dateTime = AppUtility.getDateByFormat(AppUtility.dateTimeByAmPmFormate(
                "dd-MM-yyyy hh:mm:ss a", "dd-MM-yyyy kk:mm:ss"));
        String[] date_Time = dateTime.split(" ");
        String datestr = date_Time[0];

        String time1 = App_preference.getSharedprefInstance().getLoginRes().getJobSchedule();
        if (!TextUtils.isEmpty(time1)) {
            schdul_Start_Date_Time(AppUtility.getFormatedTime(time1), datestr);
        }

        String sch_tm_dt = App_preference.getSharedprefInstance().getLoginRes().getJobCurrentTime();
        if (!TextUtils.isEmpty(sch_tm_dt)) {
            try {
                if (App_preference.getSharedprefInstance().getLoginRes().getIs24hrFormatEnable() != null && App_preference.getSharedprefInstance().getLoginRes().getIs24hrFormatEnable().equals("0"))
                    sch_time_cur(datestr, date_Time[1] + " " + date_Time[2], sch_tm_dt);
                else sch_time_cur(datestr, date_Time[1] + "", sch_tm_dt);
            } catch (Exception e) {
                e.getMessage();
            }
        }
    }

    private void sch_time_cur(String datestr, String date_Time, String sch_tm_dt) {
        String an_pm = "";
        try {
            String[] remv_sec = date_Time.split(":");
            try {
                if (App_preference.getSharedprefInstance().getLoginRes().getIs24hrFormatEnable() != null &&
                        App_preference.getSharedprefInstance().getLoginRes().getIs24hrFormatEnable().equals("0")) {
                    String[] am_pm = date_Time.split(" ");
                    an_pm = " " + am_pm[1];
                }

            } catch (Exception e) {
                e.getMessage();
            }
            String cur_start = remv_sec[0] + ":" + remv_sec[1] + an_pm;
            String date_time = datestr + " " + cur_start;
            String[] time_dur = sch_tm_dt.split(":");
            long dur_milliseconds = TimeUnit.SECONDS.toMillis(TimeUnit.HOURS.toSeconds(Integer.parseInt(time_dur[0])) +
                    TimeUnit.MINUTES.toSeconds(Integer.parseInt(time_dur[1])));

            SimpleDateFormat simpleDate = new SimpleDateFormat(AppUtility.dateTimeByAmPmFormate(
                    "dd-MM-yyyy hh:mm a", "dd-MM-yyyy kk:mm"), Locale.US);
            Date past = null;
            long milisce = 0;
            try {
                past = simpleDate.parse(date_time);
                milisce = past.getTime() + dur_milliseconds;
            } catch (ParseException e) {
                e.printStackTrace();
            }

            Calendar calendar = Calendar.getInstance();
            calendar.setTimeInMillis(milisce);
            String std = simpleDate.format(calendar.getTime());
            set_str_DT_after_cur(std);
            String[] time_duration = std.split(" ");
            date_str = time_duration[0];
            try {
                if (App_preference.getSharedprefInstance().getLoginRes().getIs24hrFormatEnable() != null &&
                        App_preference.getSharedprefInstance().getLoginRes().getIs24hrFormatEnable().equals("0"))
                    time_str = time_duration[1] + " " + time_duration[2];
                else time_str = time_duration[1] + "";

            } catch (Exception e) {
                e.getMessage();
            }
            end_Date_Time();
        } catch (NumberFormatException e) {
            e.printStackTrace();
        }
    }

    public void set_str_DT_after_cur(String std) {
        String[] time_duration = std.split(" ");
        date_str = time_duration[0];
        try {
            if (App_preference.getSharedprefInstance().getLoginRes().getIs24hrFormatEnable() != null &&
                    App_preference.getSharedprefInstance().getLoginRes().getIs24hrFormatEnable().equals("0"))
                time_str = time_duration[1] + " " + time_duration[2];
            else time_str = time_duration[1] + "";

        } catch (Exception e) {
            e.getMessage();
        }

        binding.dateStart.setText(date_str);
        binding.timeStart.setText(time_str);
    }

    private void schdul_Start_Date_Time(String[] sch_time, String datestr) {
        date_str = datestr;
        try {
            if (App_preference.getSharedprefInstance().getLoginRes().getIs24hrFormatEnable() != null &&
                    App_preference.getSharedprefInstance().getLoginRes().getIs24hrFormatEnable().equals("0"))
                time_str = sch_time[1] + " " + sch_time[2];
            else time_str = sch_time[1] + "";

        } catch (Exception e) {
            e.getMessage();
        }

        set_Str_DTime(date_str, time_str);
        end_Date_Time();
    }

    public void set_Str_DTime(String str_dt_tm, String time_str) {
        date_str = str_dt_tm;
        this.time_str = time_str;
        binding.dateStart.setText(date_str);
        binding.timeStart.setText(this.time_str);
    }

    private void end_Date_Time() {
        String date_time = date_str + " " + time_str;
        SimpleDateFormat simpleDate = new SimpleDateFormat(AppUtility.dateTimeByAmPmFormate(
                "dd-MM-yyyy hh:mm a", "dd-MM-yyyy kk:mm"), Locale.US);
        Date past = null;
        long milisce = 0;
        try {
            past = simpleDate.parse(date_time);
            milisce = past.getTime() + duration_Time();
        } catch (ParseException e) {
            e.printStackTrace();
        }
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(milisce);
        String std = simpleDate.format(calendar.getTime());
        set_End_Date_Time(std);
    }

    public void set_End_Date_Time(String std) {
        String[] time_duration = std.split(" ");
        date_en = time_duration[0];
        try {
            if (App_preference.getSharedprefInstance().getLoginRes().getIs24hrFormatEnable() != null &&
                    App_preference.getSharedprefInstance().getLoginRes().getIs24hrFormatEnable().equals("0"))
                time_en = time_duration[1] + " " + time_duration[2];
            else time_en = time_duration[1] + "";

        } catch (Exception e) {
            e.getMessage();
        }

        binding.dateEnd.setText(date_en);
        binding.timeEnd.setText(time_en);
    }

    //set schedula date time accoroding to duration
    private long duration_Time() {
        String duration = App_preference.getSharedprefInstance().getLoginRes().getDuration();
        String[] time_dur = duration.split(":");
        long dur_milliseconds = TimeUnit.SECONDS.toMillis(TimeUnit.HOURS.toSeconds(Integer.parseInt(time_dur[0])) +
                TimeUnit.MINUTES.toSeconds(Integer.parseInt(time_dur[1])));
        return dur_milliseconds;
    }

    //get start date
    private void selectStartDate() {
        final Calendar c = Calendar.getInstance();
        year = c.get(Calendar.YEAR);
        month = c.get(Calendar.MONTH);
        day = c.get(Calendar.DAY_OF_MONTH);
        showDialog(R.id.date_start);
    }

    //get end date
    private void selectEndDate() {
        final Calendar c = Calendar.getInstance();
        year = c.get(Calendar.YEAR);
        month = c.get(Calendar.MONTH);
        day = c.get(Calendar.DAY_OF_MONTH);
        showDialog(R.id.date_end);
    }

    //schedule start time
    private void selectStartTime() {
        final Calendar c = Calendar.getInstance();
        mHour = c.get(Calendar.HOUR_OF_DAY);
        mMinute = c.get(Calendar.MINUTE);
        showDialog(R.id.time_start);
    }

    //schedule end time
    private void selectEndTime() {
        final Calendar c = Calendar.getInstance();
        mHour = c.get(Calendar.HOUR_OF_DAY);
        mMinute = c.get(Calendar.MINUTE);
        showDialog(R.id.time_end);
    }

    public FieldWorker getDefaultFieldWorker(String fwid) {
        return AppDataBase.getInMemoryDatabase(EotApp.getAppinstance()).fieldWorkerModel().getFieldWorkerByID(fwid);
    }

    @Override
    protected Dialog onCreateDialog(int id) {
        switch (id) {
            case R.id.date_start:
                DatePickerDialog datePickerDialogSelectDate = new DatePickerDialog(this, AppUtility.InputDateSet(this, new Add_job_activity.DateTimeCallback() {
                    @Override
                    public void setDateTime(String dateTime) {
                        binding.dateStart.setText(dateTime);
                        date_en = date_str = dateTime;
                        binding.dateEnd.setText(date_en);
                        selectStartTime();

                    }
                }, LanguageController.getInstance().getMobileMsgByKey(AppConstant.err_start_end_date)), year, month, day);
                return datePickerDialogSelectDate;

            case R.id.date_end:
                final DatePickerDialog datePickerDialog = new DatePickerDialog(this, AppUtility.CompareInputOutputDate(this, new Add_job_activity.DateTimeCallback() {
                    @Override
                    public void setDateTime(String dateTime) {
                        binding.dateEnd.setText(dateTime);
                        date_en = dateTime;
                        selectEndTime();
                    }
                }, LanguageController.getInstance().getMobileMsgByKey(AppConstant.err_due_start_date)), year, month, day);
                datePickerDialog.getDatePicker().setMinDate(System.currentTimeMillis());
                return datePickerDialog;

            case R.id.time_start:
                TimePickerDialog timePickerDialog = new TimePickerDialog(this, AppUtility.InputTimeSet(this, new Add_job_activity.DateTimeCallback() {
                    @Override
                    public void setDateTime(String dateTime) {
                        time_str = dateTime;
                        DecimalFormat formatter = new DecimalFormat("00");
                        String[] aa = dateTime.split(":");
                        binding.timeStart.setText((formatter.format(Integer.parseInt(aa[0]))) + ":" + aa[1]);
                    }
                }, LanguageController.getInstance().getMobileMsgByKey(AppConstant.err_start_end_time)), mHour, mMinute, false);
                return timePickerDialog;

            case R.id.time_end:
                TimePickerDialog timePickerDialog1 = new TimePickerDialog(this, AppUtility.OutPutTime(this, new Add_job_activity.DateTimeCallback() {
                    @Override
                    public void setDateTime(String dateTime) {
                        time_en = dateTime;
                        DecimalFormat formatter = new DecimalFormat("00");
                        String[] aa = dateTime.split(":");
                        binding.timeEnd.setText((formatter.format(Integer.parseInt(aa[0]))) + ":" + aa[1]);
                    }
                }, LanguageController.getInstance().getMobileMsgByKey(AppConstant.err_due_start_time)), mHour, mMinute, false);
                return timePickerDialog1;


        }
        return null;
    }

    private void sendUpdateRequest() {
        HyperLog.i("", "sendUpdateRequest(M) start");

        if (appointment != null && !appointment.getTempId().equals(appointment.getAppId())) {
            HyperLog.i("", "sendUpdateRequest(M) on synced appointment");

            if (date_str.isEmpty()) {
                schdlStart = schdlFinish = "";
            } else {
                schdlStart = date_str + " " + time_str;
                schdlFinish = date_en + " " + time_en;


                if (!schdlStart.equals("") && (App_preference.getSharedprefInstance().getLoginRes().getIs24hrFormatEnable()
                        != null && App_preference.getSharedprefInstance().getLoginRes().getIs24hrFormatEnable().equals("1"))) {
                    Date startDate = null, endDate = null;
                    String[] words = time_str.split(":");
                    int t1 = Integer.valueOf(words[0]);
                    String[] words2 = time_en.split(":");
                    int t2 = Integer.valueOf(words2[0]);
                    try {
                        if (t1 != 12) {
                            startDate = new SimpleDateFormat("hh:mm", Locale.getDefault()).parse(time_str);
                            time_str = "";
                            time_str = new SimpleDateFormat("hh:mm a", Locale.getDefault()).format(startDate);
                        } else {
                            time_str = "";
                            time_str = binding.timeStart.getText().toString() + " " + "PM";

                            //   time_str = time_str + " " + "PM";
                        }
                        if (t2 != 12) {
                            endDate = new SimpleDateFormat("hh:mm", Locale.getDefault()).parse(time_en);
                            time_en = "";
                            time_en = new SimpleDateFormat("hh:mm a", Locale.getDefault()).format(endDate);
                        } else {
                            //   time_en = time_en + " " + "PM";
                            time_en = "";
                            time_en = binding.timeEnd.getText().toString() + " " + "PM";
                        }
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                    schdlStart = date_str + " " + time_str;
                    schdlFinish = date_en + " " + time_en;
                }
            }

            final AppointmentUpdateReq updateReq = new AppointmentUpdateReq();
            updateReq.setAppId(appointment.getAppId());
            updateReq.setCltId(cltId);
            updateReq.setSiteId(siteId);
            updateReq.setConId(conId);
            updateReq.setDes(binding.jobDesc.getText().toString());

            updateReq.setSchdlStart(getTimeStampFromFormatedDate(schdlStart));
            updateReq.setSchdlFinish(getTimeStampFromFormatedDate(schdlFinish));
            updateReq.setMemIds(listwork);
            updateReq.setCtry(ctry_id);
            updateReq.setState(state_id);
            updateReq.setStatus(appointment.getStatus());
            updateReq.setCity(binding.city.getText().toString());
            updateReq.setAdr(binding.adderes.getText().toString());
            updateReq.setZip(binding.postCode.getText().toString());
            updateReq.setMob1(binding.mobNo.getText().toString());
            updateReq.setNm(appointment.getNm());
            updateReq.setEmail(binding.email.getText().toString());

//            List<String> files = new ArrayList<>();
//            if (localAttachmentList != null && localAttachmentList.size() > 0) {
//                for (AppointmentAttachment at : localAttachmentList)
//                    files.add(at.getAttachFileActualName());
//            }
//            updateReq.setAppDoc(files);

            /**************attchment Uploaded *******/
            ArrayList links = new ArrayList();
            List<String> fileNames = new ArrayList<>();
            String jobDescription = binding.editor.getHtml();
            int count = 0;
            if (!TextUtils.isEmpty(jobDescription)) {
                Elements srcs = Jsoup.parse(jobDescription).select("[src]"); //get All tags containing "src"
                for (int i = 0; i < srcs.size(); i++) {
                    /***check for already upload attchment****/
                    // if (!srcs.get(0).attributes().html().contains("http:")){
                    if (srcs.get(i).attributes() != null) {
                        String link = null;
                        link = srcs.get(i).attributes().get("src");// get links of selected tags
                        if (!link.contains("http")) {
                            links.add(link);
                            String filename = link.substring(link.lastIndexOf("/") + 1);
                            fileNames.add(filename);
                            //jobDescription = jobDescription.replace(link, "_jobAttSeq_" + i + "_");
                            jobDescription = jobDescription.replace(link, "_jobAttSeq_" + count + "_");
                            count++;
                        } else {
                            jobDescription = jobDescription.replace(link, link);
                        }
                        //           }
                    }

                }

                updateReq.setDes("<p>" + jobDescription + "</p>");

            }


            synchronized (this) {
                if (links != null && links.size() > 0) {
                    if (AppUtility.isInternetConnected()) {
                        updateReq.setFileNames(fileNames);
                        updateReq.setAppDoc(links);
                        updateSyncAppoinemnt(updateReq);
                    } else {
                        AppUtility.alertDialog2(AddAppointmentActivity.this,
                                LanguageController.getInstance().getMobileMsgByKey(AppConstant.update_new_appoinment),
                                LanguageController.getInstance().getMobileMsgByKey(AppConstant.update_appoinment_offline_msg),
                                LanguageController.getInstance().getMobileMsgByKey(AppConstant.yes),
                                LanguageController.getInstance().getMobileMsgByKey(AppConstant.no), new Callback_AlertDialog() {
                                    @Override
                                    public void onPossitiveCall() {
                                        String des = updateReq.getDes();
                                        if (!TextUtils.isEmpty(des))
                                            updateReq.setDes(Html.fromHtml(des).toString());
                                        if (updateReq.getAppDoc() != null && updateReq.getAppDoc().size() > 0) {
                                            updateReq.getAppDoc().clear();
                                            updateReq.getFileNames().clear();
                                        }
                                        updateSyncAppoinemnt(updateReq);

                                    }

                                    @Override
                                    public void onNegativeCall() {

                                    }
                                });
                    }


                } else updateSyncAppoinemnt(updateReq);

            }


//            String s = new Gson().toJson(updateReq);
//            String dateTime = AppUtility.getDateByFormat(AppConstant.DATE_TIME_FORMAT);
//
//            //update in local DB
//            Appointment newAppointment = new Appointment();
//            newAppointment.setTempId(appointment.getTempId());
//            newAppointment.setAppId(appointment.getAppId());
//            newAppointment.setLabel(appointment.getLabel());
//            newAppointment.setCltId(cltId);
//            newAppointment.setSiteId(siteId);
//            newAppointment.setConId(conId);
//            newAppointment.setStatus(appointment.getStatus());
//            newAppointment.setIsdelete("1");
//            newAppointment.setNm(appointment.getNm());
//            newAppointment.setEmail(binding.email.getText().toString());
//            newAppointment.setMob1(binding.mobNo.getText().toString());
//            newAppointment.setDes(binding.jobDesc.getText().toString());
//            newAppointment.setCtry(ctry_id);
//            newAppointment.setState(state_id);
//            newAppointment.setAdr(binding.adderes.getText().toString());
//            newAppointment.setCity(binding.city.getText().toString());
//            newAppointment.setZip(binding.postCode.getText().toString());
//            newAppointment.setUpdateDate(AppUtility.getDateByMiliseconds());
//            newAppointment.setSchdlStart(getLongTimeStamp(schdlStart));
//            newAppointment.setSchdlFinish(getLongTimeStamp(schdlFinish));
//            AppDataBase.getInMemoryDatabase(EotApp.getAppinstance()).appointmentModel().insertSingleAppointment(newAppointment);
//            OfflineDataController.getInstance().addInOfflineDB(Service_apis.updateAppointment, s, dateTime);
//            Intent intent = new Intent();
//            intent.putExtra("appointment", newAppointment);
//            intent.putExtra("doc", (Serializable) localAttachmentList);
//            setResult(RESULT_OK, intent);
//            finish();
//            Log.d("", "");

        }/**
         appointment not synced yet not user want to modified the add request
         */
        else if (appointment != null && appointment.getTempId().equals(appointment.getAppId())) {
            HyperLog.i("", "sendUpdateRequest(M) on not synced appointment");

            List<Offlinetable> offlineData = AppDataBase.getInMemoryDatabase(EotApp.getAppinstance())
                    .appointmentModel().getOfflineData(Service_apis.addAppointment);
            if (offlineData != null && offlineData.size() > 0) {
                for (final Offlinetable tbl : offlineData) {
                    if (tbl.getParams().contains(appointment.getTempId())) {
                        String params = tbl.getParams();
                        final AppointmentAddReq appointmentAddReq = new Gson().fromJson(params, AppointmentAddReq.class);
                        appointmentAddReq.setTempId(appointment.getTempId());
                        appointmentAddReq.setCltId(cltId);
                        appointmentAddReq.setSiteId(siteId);
                        appointmentAddReq.setConId(conId);
                        appointmentAddReq.setNm(appointment.getNm());
                        appointmentAddReq.setEmail(binding.email.getText().toString());
                        appointmentAddReq.setMob1(binding.mobNo.getText().toString());
                        appointmentAddReq.setDes(binding.jobDesc.getText().toString());
                        appointmentAddReq.setCtry(ctry_id);
                        appointmentAddReq.setState(state_id);
                        appointmentAddReq.setAdr(binding.adderes.getText().toString());
                        appointmentAddReq.setCity(binding.city.getText().toString());
                        appointmentAddReq.setZip(binding.postCode.getText().toString());
                        appointmentAddReq.setSchdlStart(getTimeStampFromFormatedDate(schdlStart));
                        appointmentAddReq.setSchdlFinish(getTimeStampFromFormatedDate(schdlFinish));
                        appointmentAddReq.setMemIds(listwork);
//                        List<String> appDoc = appointmentAddReq.getAppDoc();
//                        if (localAttachmentList != null && localAttachmentList.size() > 0) {
//                            for (AppointmentAttachment at : localAttachmentList)
//                                appDoc.add(at.getAttachFileActualName());
//                        }


                        /********Upload attchment remove when network not avalable**************/

                        /**************attchment Uploaded *******/
                        ArrayList links = new ArrayList();
                        List<String> fileNames = new ArrayList<>();
                        String jobDescription = binding.editor.getHtml();
                        if (!TextUtils.isEmpty(jobDescription)) {
                            Elements srcs = Jsoup.parse(jobDescription).select("[src]"); //get All tags containing "src"
                            for (int i = 0; i < srcs.size(); i++) {
                                /***check for already upload attchment****/
                                // if (!srcs.get(0).attributes().html().contains("http:")){
                                if (srcs.get(i).attributes() != null) {
                                    String link = null;
                                    link = srcs.get(i).attributes().get("src");// get links of selected tags
                                    if (!link.contains("http")) {
                                        links.add(link);
                                        String filename = link.substring(link.lastIndexOf("/") + 1);
                                        fileNames.add(filename);
                                        jobDescription = jobDescription.replace(link, "_jobAttSeq_" + i + "_");
                                    } else {
                                        jobDescription = jobDescription.replace(link, link);
                                    }
                                    //           }
                                }

                            }

                            appointmentAddReq.setDes("<p>" + jobDescription + "</p>");

                        }


                        if (links != null && links.size() > 0) {
                            if (AppUtility.isInternetConnected()) {
                                appointmentAddReq.setFileNames(fileNames);
                                appointmentAddReq.setAppDoc(links);
                                updateNotSyncAppoinemnt(appointmentAddReq, tbl);
                            } else {
                                AppUtility.alertDialog2(AddAppointmentActivity.this,
                                        LanguageController.getInstance().getMobileMsgByKey(AppConstant.title_add_job),
                                        LanguageController.getInstance().getMobileMsgByKey(AppConstant.add_job_offline_msg),
                                        LanguageController.getInstance().getMobileMsgByKey(AppConstant.yes),
                                        LanguageController.getInstance().getMobileMsgByKey(AppConstant.no), new Callback_AlertDialog() {
                                            @Override
                                            public void onPossitiveCall() {
                                                String des = appointmentAddReq.getDes();
                                                if (!TextUtils.isEmpty(des))
                                                    appointmentAddReq.setDes(Html.fromHtml(des).toString());
                                                if (appointmentAddReq.getAppDoc() != null && appointmentAddReq.getAppDoc().size() > 0) {
                                                    appointmentAddReq.getAppDoc().clear();
                                                    appointmentAddReq.getFileNames().clear();
                                                }
                                                updateNotSyncAppoinemnt(appointmentAddReq, tbl);

                                            }

                                            @Override
                                            public void onNegativeCall() {

                                            }
                                        });
                            }
                        } else updateNotSyncAppoinemnt(appointmentAddReq, tbl);


                        /****Old ****/
//                        tbl.setParams(new Gson().toJson(appointmentAddReq));


                        //update in local DB
//                        Appointment newAppointment = new Appointment();
//                        newAppointment.setTempId(appointment.getTempId());
//                        newAppointment.setAppId(appointment.getAppId());
//                        newAppointment.setCltId(cltId);
//                        newAppointment.setSiteId(siteId);
//                        newAppointment.setConId(conId);
//                        newAppointment.setIsdelete("1");
//                        newAppointment.setNm(appointment.getNm());
//                        newAppointment.setEmail(binding.email.getText().toString());
//                        newAppointment.setMob1(binding.mobNo.getText().toString());
//                        newAppointment.setDes(binding.jobDesc.getText().toString());
//                        newAppointment.setCtry(ctry_id);
//                        newAppointment.setState(state_id);
//                        newAppointment.setAdr(binding.adderes.getText().toString());
//                        newAppointment.setCity(binding.city.getText().toString());
//                        newAppointment.setZip(binding.postCode.getText().toString());
//                        newAppointment.setUpdateDate(AppUtility.getDateByMiliseconds());
//                        newAppointment.setSchdlStart(getLongTimeStamp(schdlStart));
//                        newAppointment.setSchdlFinish(getLongTimeStamp(schdlFinish));
//
//
//                        AppDataBase.getInMemoryDatabase(EotApp.getAppinstance()).appointmentModel().
//                                insertSingleAppointment(newAppointment);
//
//                        AppDataBase.getInMemoryDatabase(EotApp.getAppinstance()).appointmentModel()
//                                .updateOfflineTable(tbl);
//
//                        Intent intent = new Intent();
//                        intent.putExtra("appointment", newAppointment);
//                        intent.putExtra("doc", (Serializable) localAttachmentList);
//                        setResult(RESULT_OK, intent);
//                        finish();
                        break;
                    }
                }
            }

        }
        HyperLog.i("", "sendUpdateRequest(M) Completed");

    }

    synchronized private void updateNotSyncAppoinemnt(AppointmentAddReq appointmentAddReq, Offlinetable tbl) {
        /****Old ****/
        tbl.setParams(new Gson().toJson(appointmentAddReq));


        //update in local DB
        Appointment newAppointment = new Appointment();
        newAppointment.setTempId(appointment.getTempId());
        newAppointment.setAppId(appointment.getAppId());
        newAppointment.setCltId(cltId);
        newAppointment.setSiteId(siteId);
        newAppointment.setConId(conId);
        newAppointment.setIsdelete("1");
        newAppointment.setNm(appointment.getNm());
        newAppointment.setEmail(binding.email.getText().toString());
        newAppointment.setMob1(binding.mobNo.getText().toString());
        newAppointment.setDes(binding.jobDesc.getText().toString());
        newAppointment.setCtry(ctry_id);
        newAppointment.setState(state_id);
        newAppointment.setAdr(binding.adderes.getText().toString());
        newAppointment.setCity(binding.city.getText().toString());
        newAppointment.setZip(binding.postCode.getText().toString());
        newAppointment.setUpdateDate(AppUtility.getDateByMiliseconds());
        newAppointment.setSchdlStart(getLongTimeStamp(schdlStart));
        newAppointment.setSchdlFinish(getLongTimeStamp(schdlFinish));


        AppDataBase.getInMemoryDatabase(EotApp.getAppinstance()).appointmentModel().
                insertSingleAppointment(newAppointment);

        AppDataBase.getInMemoryDatabase(EotApp.getAppinstance()).appointmentModel()
                .updateOfflineTable(tbl);

        Intent intent = new Intent();
        intent.putExtra("appointment", newAppointment);
        intent.putExtra("doc", (Serializable) localAttachmentList);
        setResult(RESULT_OK, intent);
        finish();
    }

    private void updateSyncAppoinemnt(AppointmentUpdateReq updateReq) {
        String s = new Gson().toJson(updateReq);
        String dateTime = AppUtility.getDateByFormat("dd-MM-yyyy hh:mm:ss a");

        //update in local DB
        Appointment newAppointment = new Appointment();
        newAppointment.setTempId(appointment.getTempId());
        newAppointment.setAppId(appointment.getAppId());
        newAppointment.setLabel(appointment.getLabel());
        newAppointment.setCltId(cltId);
        newAppointment.setSiteId(siteId);
        newAppointment.setConId(conId);
        newAppointment.setStatus(appointment.getStatus());
        newAppointment.setIsdelete("1");
        newAppointment.setNm(appointment.getNm());
        newAppointment.setEmail(binding.email.getText().toString());
        newAppointment.setMob1(binding.mobNo.getText().toString());
        newAppointment.setDes(binding.jobDesc.getText().toString());
        newAppointment.setCtry(ctry_id);
        newAppointment.setState(state_id);
        newAppointment.setAdr(binding.adderes.getText().toString());
        newAppointment.setCity(binding.city.getText().toString());
        newAppointment.setZip(binding.postCode.getText().toString());
        newAppointment.setUpdateDate(AppUtility.getDateByMiliseconds());
        newAppointment.setSchdlStart(getLongTimeStamp(schdlStart));
        newAppointment.setSchdlFinish(getLongTimeStamp(schdlFinish));
        AppDataBase.getInMemoryDatabase(EotApp.getAppinstance()).appointmentModel().insertSingleAppointment(newAppointment);
        OfflineDataController.getInstance().addInOfflineDB(Service_apis.updateAppointment, s, dateTime);
        Intent intent = new Intent();
        intent.putExtra("appointment", newAppointment);
        intent.putExtra("doc", (Serializable) localAttachmentList);
        setResult(RESULT_OK, intent);
        finish();
        Log.d("", "");
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.submit_btn:
                if (TextUtils.isEmpty(cltId) && TextUtils.isEmpty(new_cnm)) {
                    showDialogs(LanguageController.getInstance().getMobileMsgByKey(AppConstant.err_client_name));
                } else {

                    if (binding.cbFutureClient.isChecked()) {
                        if (TextUtils.isEmpty(binding.adderes.getText().toString())) {
                            showDialogs(LanguageController.getInstance().getMobileMsgByKey(AppConstant.err_addr));
                            return;
                        } else if (TextUtils.isEmpty(ctry_id)) {
                            showDialogs(LanguageController.getInstance().getMobileMsgByKey(AppConstant.country_error));
                            return;
                        } else if (TextUtils.isEmpty(state_id)) {
                            showDialogs(LanguageController.getInstance().getMobileMsgByKey(AppConstant.state_error));
                            return;
                        }
                    }

                    if (date_str.isEmpty()) {
                        schdlStart = schdlFinish = "";
                    } else {
                        schdlStart = date_str + " " + time_str;
                        schdlFinish = date_en + " " + time_en;
                    }

                    if (!TextUtils.isEmpty(schdlStart)) {
                        SimpleDateFormat format = new SimpleDateFormat(
                                //"dd-MM-yyyy hh:mm a"
                                AppUtility.dateTimeByAmPmFormate("dd-MM-yyyy hh:mm a", "dd-MM-yyyy kk:mm")
                                , Locale.US);
                        try {
                            Date start = format.parse(schdlStart);
                            Date end = format.parse(schdlFinish);
                            if (start.getTime() > end.getTime()) {
                                showDialogs(LanguageController.getInstance().getMobileMsgByKey(AppConstant.err_start_end_time));
                                return;
                            }

                        } catch (ParseException e) {
                            e.printStackTrace();
                        }


                    }

                    if (isInEditMode) {
                        sendUpdateRequest();
                    } else {
                        AppointmentAddReq appointmentAddReq = addAppointment();
                        appointmentViewModel.addAppointment(appointmentAddReq);
                    }
                }
                break;

            case R.id.ll_date_start:
                selectStartDate();
                break;
            case R.id.ll_date_end:
                selectEndDate();
                break;

            case R.id.time_start:
                selectStartTime();
                break;

            case R.id.time_end:
                selectEndTime();
                break;

            case R.id.assignto_linear:
                binding.members.showDropDown();
                break;

          /*  case R.id.appointment_attachment:
                sendImageForChat();
                break;
*/

        }

    }


    @Override
    public void onClickContinuarEvent(Uri uri) {
        // path = PathUtils.getPath(this, uri);
        path = PathUtils.getRealPath(this, uri);
        if (!path.isEmpty()) {
            File file = new File(path);
            if (file != null && file.exists()) {
                AppointmentAttachment
                        attachment = new AppointmentAttachment();
                attachment.setType(1);
                attachment.setAttachFileActualName(path);
                localAttachmentList.add(0, attachment);
                // appointmentAddAdapter.setList(localAttachmentList);
                // appointmentAddAdapter.addNewAttachement(attachment);
            }
        }
    }


    /*get clinet name from local DB*/
    private void setClientName(Appointment model) {
        if (TextUtils.isEmpty(model.getNm()))
            if (!TextUtils.isEmpty(model.getCltId())) {
                try {
                    int i = Integer.parseInt(model.getCltId());
                    Client clientFromId = AppDataBase.getInMemoryDatabase(EotApp.getAppinstance())
                            .clientModel().getClientFromId(i);
                    if (clientFromId != null)
                        binding.autoClient.setText(clientFromId.getNm());
                } catch (NumberFormatException e) {
                    e.printStackTrace();
                }
            }
    }
}
