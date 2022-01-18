package com.eot_app.nav_menu.audit.addAudit;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
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
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.eot_app.R;
import com.eot_app.UploadDocumentActivity;
import com.eot_app.eoteditor.CustomEditor;
import com.eot_app.eoteditor.EotEditor;
import com.eot_app.home_screens.MainActivity;
import com.eot_app.nav_menu.appointment.dbappointment.Appointment;
import com.eot_app.nav_menu.audit.addAudit.add_aduit_model_pkg.AddAudit_Req;
import com.eot_app.nav_menu.audit.addAudit.add_audit_mvp.AddAduit_pi;
import com.eot_app.nav_menu.audit.addAudit.add_audit_mvp.AddAudit_pc;
import com.eot_app.nav_menu.audit.addAudit.add_audit_mvp.Add_AduitView;
import com.eot_app.nav_menu.audit.addAudit.custom_aduit_dropDown.CustomAduitController;
import com.eot_app.nav_menu.client.client_db.Client;
import com.eot_app.nav_menu.client.clientlist.client_detail.contact.client_dao.ContactData;
import com.eot_app.nav_menu.client.clientlist.client_detail.site.sitelist.Site_model;
import com.eot_app.nav_menu.client.clientlist.client_detail.site.sitelist.editsite.editsitedb.SpinnerCountrySite;
import com.eot_app.nav_menu.custom_fileds.custom_filed_adpter.CustomFieldJobAdapter;
import com.eot_app.nav_menu.custom_fileds.custom_model.CustOmFormQuestionsRes;
import com.eot_app.nav_menu.jobs.add_job.Add_job_activity;
import com.eot_app.nav_menu.jobs.add_job.adapters.DynamicClassAdapter;
import com.eot_app.nav_menu.jobs.add_job.adapters.FilterAdapter;
import com.eot_app.nav_menu.jobs.add_job.adapters.FilterAdapterContact;
import com.eot_app.nav_menu.jobs.add_job.adapters.FilterAdapterSites;
import com.eot_app.nav_menu.jobs.add_job.adapters.FilterCountry;
import com.eot_app.nav_menu.jobs.add_job.adapters.FilterStates;
import com.eot_app.nav_menu.jobs.job_detail.chat.img_crop_pkg.ImageCropFragment;
import com.eot_app.nav_menu.jobs.job_detail.form_form.get_qus_list.ans_model.Answer;
import com.eot_app.nav_menu.jobs.job_detail.form_form.get_qus_list.qus_model.AnswerModel;
import com.eot_app.utility.AppConstant;
import com.eot_app.utility.AppUtility;
import com.eot_app.utility.App_preference;
import com.eot_app.utility.Country;
import com.eot_app.utility.EotApp;
import com.eot_app.utility.States;
import com.eot_app.utility.db.AppDataBase;
import com.eot_app.utility.language_support.LanguageController;
import com.eot_app.utility.language_support.Language_Preference;
import com.eot_app.utility.settings.SettingUrls;
import com.eot_app.utility.settings.contractdb.ContractRes;
import com.eot_app.utility.settings.firstSync.FirstSyncPC;
import com.eot_app.utility.settings.setting_db.FieldWorker;
import com.eot_app.utility.settings.setting_db.TagData;
import com.eot_app.utility.util_interfaces.Callback_AlertDialog;
import com.eot_app.utility.util_interfaces.MyAdapter;
import com.google.android.material.textfield.TextInputLayout;

import org.jsoup.Jsoup;
import org.jsoup.select.Elements;

import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Set;
import java.util.concurrent.Callable;

public class AddAuditActivity extends UploadDocumentActivity implements Add_AduitView,
        AdapterView.OnItemSelectedListener, View.OnClickListener
        , TextWatcher, ImageCropFragment.MyDialogInterface {

    private final Set<String> AuditorsList = new HashSet<>();
    private final ArrayList<TagData> tagArray = new ArrayList<>();
    private final String appId = "";
    private final boolean newContact = false;
    private final boolean newSite = false;
    CustomAduitController customDPController;
    TextView cb_future_contact, cb_future_sites;
    List<ContactData> contact_data;
    List<Site_model> site_data;
    ImageView action_insert_image;
    CustomEditor mEditor;
    private Spinner contractSpinner;
    private AddAduit_pi addAduit_pc;
    private FilterAdapterSites filterSites;
    private FilterAdapterContact filterContact;
    private ContactData selectedContactData;
    private Site_model selectedSiteData;
    private TextInputLayout input_layout_client, input_layout_contact, input_layout_site, audit_state_layout, audit_desc_layout, audit_instr_layout, audit_mob1_layout, audit_mob2_layout, audit_email_layout, audit_adr_layout, audit_lat_layout, audit_lng_layout, audit_city_layout, audit_postal_layout, landmark_layout, audit_country_layout, contact_add_layout, site_add_layout;
    private AutoCompleteTextView auto_client, auto_contact, auto_sites, auto_states, et_tag, auto_country;
    private CheckBox cb_future_client;
    private LinearLayout linear_addTag;
    private EditText audit_desc, audit_instr, mob_no, at_mob, email, adderes, city, post_code, edt_lng, edt_lat, landmark_edt, contact_add_edt, site_add_edt;
    private Button add_tag_btn, submit_btn, date_time_clear_btn;
    private TextView jobdeshint, date_start, time_start, date_end, time_end, tag_lable, schel_start, schel_end;//, tv_hint_title ,, title_set
    private String dateTime;
    private String cltId = "", siteId = "", conId = "", new_cnm = "", new_site_nm = "", new_con_nm = "", ctry_id = "", state_id = "";
    private int clientForFuture = 0, siteForFuture = 0, contactForFuture = 0, member_type;
    private String date_str, time_str, date_en, time_en, schdlStart, schdlFinish;
    private int i = 1;
    private String kpr;
    private RelativeLayout relative_main;
    private int s;
    private String[] id_array;
    private String tempId;
    private int year, month, day, mHour, mMinute;
    private List<TagData> tagslist;
    private View lng_view, lat_view, adr_view, landmark_view, contract_view;
    private String schdlStart1, contrId = "";
    private LinearLayout auditservicelayout;
    private TextView auditservicetxtlableset, auditservicelablehint, contract_hint_lable, contarct_lable;
    private LinearLayout contact_new_add_layout, contact_dp_layout, site_dp_layout, site_new_add_layout;
    private ImageView contract_cross_img, contract_dp_img, site_dp_img, contact_dp_img, imvCross, imvCross_site;
    private RelativeLayout contract_parent_view;
    private ArrayList<FieldWorker> dataa = new ArrayList<>();
    private boolean isDefaultFwSelected = false;
    private boolean APPOINMENTJOB = true;//
    private RecyclerView recyclerViewCustomField;
    private CustomFieldJobAdapter customFiledQueAdpter;
    private Add_AduitView addJob_pc;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_audit);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        setTitle(LanguageController.getInstance().getMobileMsgByKey(AppConstant.title_add_audit));
        customDPController = new CustomAduitController();
        initializelables();
        initializeView();
        textInputLayoutHint();

        dateTime = AppUtility.getDateByFormat("dd-MM-yyyy hh:mm:ss a");

        mEditor.setPlaceholder(LanguageController.getInstance().getMobileMsgByKey(AppConstant.audit_desc));
        mEditor.setTextColor(Color.parseColor("#8C9293"));


        mEditor.setOnTextChangeListener(new EotEditor.OnTextChangeListener() {
            @Override
            public void onTextChange(String text) {
                if (text.length() >= 0) {
                    jobdeshint.setVisibility(View.VISIBLE);
                } else {
                    jobdeshint.setVisibility(View.INVISIBLE);
                }
            }
        });

        mEditor.setBackgroundColor(Color.TRANSPARENT);
        mEditor.focusEditor();

        mEditor.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    jobdeshint.setVisibility(View.VISIBLE);
                } else {
                    if (mEditor.getHtml() != null) {
                        if (mEditor.getHtml().length() != 0) {
                            jobdeshint.setVisibility(View.VISIBLE);
                        } else
                            jobdeshint.setVisibility(View.INVISIBLE);
                    } else
                        jobdeshint.setVisibility(View.INVISIBLE);
                }
            }
        });

        action_insert_image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // mEditor.focusEditor();
                selectFile(true);
            }
        });
    }

    private void initializelables() {


        input_layout_client = findViewById(R.id.input_layout_client);
        input_layout_contact = findViewById(R.id.input_layout_contact);
        input_layout_site = findViewById(R.id.input_layout_site);
        audit_country_layout = findViewById(R.id.audit_country_layout);
        audit_state_layout = findViewById(R.id.audit_state_layout);


        auto_client = findViewById(R.id.auto_client);
        auto_client.setHint(LanguageController.getInstance().getMobileMsgByKey(AppConstant.client_mand) + " *");

        auto_contact = findViewById(R.id.auto_contact);
        auto_contact.setHint(LanguageController.getInstance().getMobileMsgByKey(AppConstant.contact_name));


        auto_sites = findViewById(R.id.auto_sites);
        auto_sites.setHint(LanguageController.getInstance().getMobileMsgByKey(AppConstant.site_name));

        auto_country = findViewById(R.id.auto_country);
        auto_country.setHint(LanguageController.getInstance().getMobileMsgByKey(AppConstant.country) + " *");

        auto_states = findViewById(R.id.auto_states);
        auto_states.setHint(LanguageController.getInstance().getMobileMsgByKey(AppConstant.state) + " *");


        tag_lable = findViewById(R.id.tag_lable);
        tag_lable.setHint(LanguageController.getInstance().getMobileMsgByKey(AppConstant.tag));


        et_tag = findViewById(R.id.et_tag);
        et_tag.setHint(LanguageController.getInstance().getMobileMsgByKey(AppConstant.add_tag));


        audit_desc = findViewById(R.id.audit_desc);
        audit_desc.setHint(LanguageController.getInstance().getMobileMsgByKey(AppConstant.audit_desc));

        audit_instr = findViewById(R.id.audit_instr);
        audit_instr.setHint(LanguageController.getInstance().getMobileMsgByKey(AppConstant.audit_instr));

        mob_no = findViewById(R.id.mob_no);
        mob_no.setHint(LanguageController.getInstance().getMobileMsgByKey(AppConstant.mob_no));

        at_mob = findViewById(R.id.at_mob);
        at_mob.setHint(LanguageController.getInstance().getMobileMsgByKey(AppConstant.alt_mobile_number));

        email = findViewById(R.id.email);
        email.setHint(LanguageController.getInstance().getMobileMsgByKey(AppConstant.client_email));

        adderes = findViewById(R.id.adderes);
        adderes.setHint(LanguageController.getInstance().getMobileMsgByKey(AppConstant.address) + " *");

        audit_lat_layout = findViewById(R.id.audit_lat_layout);
        edt_lat = findViewById(R.id.edt_lat);
        edt_lat.setHint(LanguageController.getInstance().getMobileMsgByKey(AppConstant.latitude));

        audit_lng_layout = findViewById(R.id.audit_lng_layout);
        edt_lng = findViewById(R.id.edt_lng);
        edt_lng.setHint(LanguageController.getInstance().getMobileMsgByKey(AppConstant.longitued));

        city = findViewById(R.id.city);
        city.setHint(LanguageController.getInstance().getMobileMsgByKey(AppConstant.city));

        post_code = findViewById(R.id.post_code);
        post_code.setHint(LanguageController.getInstance().getMobileMsgByKey(AppConstant.postal_code));

        linear_addTag = findViewById(R.id.linear_addTag);
        add_tag_btn = findViewById(R.id.add_tag_btn);
        add_tag_btn.setText(LanguageController.getInstance().getMobileMsgByKey(AppConstant.add));

        submit_btn = findViewById(R.id.submit_btn);
        submit_btn.setText(LanguageController.getInstance().getMobileMsgByKey(AppConstant.create_audit));

        cb_future_client = findViewById(R.id.cb_future_client);
        cb_future_client.setText(LanguageController.getInstance().getMobileMsgByKey(AppConstant.save_for_future_use));

        cb_future_contact = findViewById(R.id.cb_future_contact);
        cb_future_contact.setText(LanguageController.getInstance().getMobileMsgByKey(AppConstant.add));

        cb_future_sites = findViewById(R.id.cb_future_sites);
        cb_future_sites.setText(LanguageController.getInstance().getMobileMsgByKey(AppConstant.add_new_site));

        date_start = findViewById(R.id.date_start);
        date_start.setHint(LanguageController.getInstance().getMobileMsgByKey(AppConstant.date_form));

        date_end = findViewById(R.id.date_end);
        date_end.setHint(LanguageController.getInstance().getMobileMsgByKey(AppConstant.date_form));

        time_start = findViewById(R.id.time_start);
        time_start.setHint(LanguageController.getInstance().getMobileMsgByKey(AppConstant.time_form));

        time_end = findViewById(R.id.time_end);
        time_end.setHint(LanguageController.getInstance().getMobileMsgByKey(AppConstant.time_form));


        date_time_clear_btn = findViewById(R.id.date_time_clear_btn);
        date_time_clear_btn.setText(LanguageController.getInstance().getMobileMsgByKey(AppConstant.clear));

        relative_main = findViewById(R.id.relative_main);

        audit_desc_layout = findViewById(R.id.input_layout_des_audit);
        audit_instr_layout = findViewById(R.id.audit_instr_layout);
        audit_mob1_layout = findViewById(R.id.input_layout_mobile);
        audit_mob2_layout = findViewById(R.id.audit_mob2_layout);
        audit_email_layout = findViewById(R.id.audit_email_layout);
        audit_adr_layout = findViewById(R.id.audit_adr_layout);
        audit_city_layout = findViewById(R.id.audit_city_layout);
        audit_postal_layout = findViewById(R.id.audit_postal_layout);
        action_insert_image = findViewById(R.id.action_insert_image);
        mEditor = findViewById(R.id.editor);


        jobdeshint = findViewById(R.id.jobdeshint);
        jobdeshint.setHint(LanguageController.getInstance().getMobileMsgByKey(AppConstant.audit_desc));


        schel_start = findViewById(R.id.schel_start);
        schel_start.setHint(LanguageController.getInstance().getMobileMsgByKey(AppConstant.shdl_start));

        schel_end = findViewById(R.id.schel_end);
        schel_end.setHint(LanguageController.getInstance().getMobileMsgByKey(AppConstant.shdl_end));


        landmark_layout = findViewById(R.id.landmark_layout);
        landmark_edt = findViewById(R.id.landmark_edt);
        landmark_edt.setHint(LanguageController.getInstance().getMobileMsgByKey(AppConstant.landmark_addjob));
        landmark_view = findViewById(R.id.landmark_view);


        auditservicelayout = findViewById(R.id.auditservicelayout);
        auditservicetxtlableset = findViewById(R.id.auditservicetxtlableset);
        auditservicetxtlableset.setHint(LanguageController.getInstance().getMobileMsgByKey(AppConstant.auditors1) + " *");
        auditservicelablehint = findViewById(R.id.auditservicelablehint);
        auditservicelablehint.setHint(LanguageController.getInstance().getMobileMsgByKey(AppConstant.auditors1) + " *");
        auditservicelayout.setOnClickListener(this);


        contract_parent_view = findViewById(R.id.contract_parent_view);
        contract_hint_lable = findViewById(R.id.contract_hint_lable);
        contarct_lable = findViewById(R.id.contarct_lable);
        contract_cross_img = findViewById(R.id.contract_cross_img);
        contract_dp_img = findViewById(R.id.contract_dp_img);
        contractSpinner = findViewById(R.id.contractSpinner);
        contract_view = findViewById(R.id.contract_view);
        contract_cross_img.setOnClickListener(this);
        contract_dp_img.setOnClickListener(this);
        contarct_lable.setOnClickListener(this);
        contract_hint_lable.setText(LanguageController.getInstance().getMobileMsgByKey(AppConstant.contract));

        site_dp_img = findViewById(R.id.site_dp_img);
        site_dp_img.setOnClickListener(this);


        contact_dp_img = findViewById(R.id.contact_dp_img);
        contact_dp_img.setOnClickListener(this);

        contact_new_add_layout = findViewById(R.id.contact_new_add_layout);
        contact_dp_layout = findViewById(R.id.contact_dp_layout);

        contact_add_layout = findViewById(R.id.contact_add_layout);
        contact_add_edt = findViewById(R.id.contact_add_edt);
        contact_add_edt.setHint(LanguageController.getInstance().getMobileMsgByKey(AppConstant.add_contact_name));

        imvCross = findViewById(R.id.imvCross);
        imvCross_site = findViewById(R.id.imvCross_site);

        site_new_add_layout = findViewById(R.id.site_new_add_layout);
        site_dp_layout = findViewById(R.id.site_dp_layout);

        site_add_layout = findViewById(R.id.site_add_layout);
        site_add_edt = findViewById(R.id.site_add_edt);
        site_add_edt.setHint(LanguageController.getInstance().getMobileMsgByKey(AppConstant.add_new_site));

        if (Language_Preference.getSharedprefInstance().getlanguageFilename().equals("iw")) {
            RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) cb_future_contact.getLayoutParams();
            params.addRule(RelativeLayout.ALIGN_PARENT_LEFT);
            cb_future_contact.setLayoutParams(params);

            RelativeLayout.LayoutParams params1 = (RelativeLayout.LayoutParams) cb_future_sites.getLayoutParams();
            params1.addRule(RelativeLayout.ALIGN_PARENT_LEFT);
            cb_future_sites.setLayoutParams(params1);

            RelativeLayout.LayoutParams params2 = (RelativeLayout.LayoutParams) cb_future_client.getLayoutParams();
            params1.addRule(RelativeLayout.ALIGN_PARENT_LEFT);
            cb_future_client.setLayoutParams(params2);

            RelativeLayout.LayoutParams params3 = (RelativeLayout.LayoutParams) jobdeshint.getLayoutParams();
            params3.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
            jobdeshint.setLayoutParams(params3);


        } else {
            RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) cb_future_contact.getLayoutParams();
            params.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
            cb_future_contact.setLayoutParams(params);

            RelativeLayout.LayoutParams params1 = (RelativeLayout.LayoutParams) cb_future_sites.getLayoutParams();
            params1.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
            cb_future_sites.setLayoutParams(params1);

            RelativeLayout.LayoutParams params2 = (RelativeLayout.LayoutParams) cb_future_client.getLayoutParams();
            params2.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
            cb_future_client.setLayoutParams(params2);

            RelativeLayout.LayoutParams params3 = (RelativeLayout.LayoutParams) jobdeshint.getLayoutParams();
            params3.addRule(RelativeLayout.ALIGN_PARENT_LEFT);
            jobdeshint.setLayoutParams(params3);

            auto_contact.setOnFocusChangeListener(new View.OnFocusChangeListener() {
                @Override
                public void onFocusChange(View v, boolean hasFocus) {
                    if (!hasFocus) {
                        String contact_txt = auto_contact.getText().toString().trim();
                        if (contact_txt.length() > 0) {
                            if (contact_data != null) {
                                for (ContactData contactData : contact_data) {
                                    if (contact_txt.equals(contactData.getCnm())) {
                                        new_con_nm = "";
                                        conId = contactData.getConId();
                                    } else {

                                        auto_contact.setText(AppDataBase.getInMemoryDatabase(EotApp.getAppinstance()).contactModel().getContactById(conId).getCnm());
                                    }
                                }
                            }
                        }
                    }
                }
            });
            auto_sites.setOnFocusChangeListener(new View.OnFocusChangeListener() {
                @Override
                public void onFocusChange(View v, boolean hasFocus) {
                    if (!hasFocus) {
                        String site_txt = auto_sites.getText().toString().trim();
                        if (site_txt.length() > 0) {
                            if (site_data != null) {
                                for (Site_model siteData : site_data) {
                                    if (site_txt.equals(siteData.getSnm())) {
                                        new_con_nm = "";
                                        siteId = siteData.getSiteId();
                                    } else {

                                        auto_sites.setText(AppDataBase.getInMemoryDatabase(EotApp.getAppinstance()).sitemodel().getSiteFromSiteId(siteId).getSnm());
                                    }
                                }
                            }
                        }
                    }
                }
            });
        }

        recyclerViewCustomField = findViewById(R.id.recyclerView_custom_field);
        recyclerViewCustomField.setLayoutManager(new LinearLayoutManager(this));


    }


    @Override
    public void onDocumentSelected(String path, boolean isImage) {
        super.onDocumentSelected(path, isImage);
        if (!TextUtils.isEmpty(path) && isImage && mEditor != null) {

            mEditor.getSettings().setAllowFileAccess(true);
            mEditor.insertImage(path, "logo", 320, 250);

        }

    }

    /**
     * set adapter on
     */
    private void setAdapterOfCustomField() {
        ArrayList<CustOmFormQuestionsRes> custOmFormQuestionsList = App_preference.getSharedprefInstance().getAuditCustomFiled();
        if (custOmFormQuestionsList != null) {
            customFiledQueAdpter = new CustomFieldJobAdapter(custOmFormQuestionsList, this);
            recyclerViewCustomField.setAdapter(customFiledQueAdpter);
        }

    }

    private void textInputLayoutHint() {

        setSelectedJobServices(dataa);


        AppUtility.setupUI(relative_main, AddAuditActivity.this);

        audit_desc_layout.getEditText().addTextChangedListener(this);
        audit_instr_layout.getEditText().addTextChangedListener(this);
        audit_mob1_layout.getEditText().addTextChangedListener(this);
        audit_mob2_layout.getEditText().addTextChangedListener(this);
        audit_adr_layout.getEditText().addTextChangedListener(this);
        audit_lat_layout.getEditText().addTextChangedListener(this);
        audit_lng_layout.getEditText().addTextChangedListener(this);
        audit_email_layout.getEditText().addTextChangedListener(this);
        audit_city_layout.getEditText().addTextChangedListener(this);
        audit_postal_layout.getEditText().addTextChangedListener(this);
        audit_state_layout.getEditText().addTextChangedListener(this);
        landmark_layout.getEditText().addTextChangedListener(this);
        contact_add_layout.getEditText().addTextChangedListener(this);
        site_add_layout.getEditText().addTextChangedListener(this);

        if (App_preference.getSharedprefInstance().getLoginRes().getIsCustomFieldEnable().equals("1")) {
            setAdapterOfCustomField();
        }


    }

    private void setClientName(String cltId) {
        if (!TextUtils.isEmpty(cltId)) {
            try {
                int i = Integer.parseInt(cltId);
                Client clientFromId = AppDataBase.getInMemoryDatabase(EotApp.getAppinstance())
                        .clientModel().getClientFromId(i);
                if (clientFromId != null)
                    auto_client.setText(clientFromId.getNm());
            } catch (NumberFormatException e) {
                e.printStackTrace();
            }
        }
    }

    private void initializeView() {
        lat_view = findViewById(R.id.lat_view);
        lng_view = findViewById(R.id.lng_view);
        adr_view = findViewById(R.id.adr_view);
        //title_linearLayout.setOnClickListener(this);


        date_time_clear_btn.setOnClickListener(this);

        add_tag_btn.setOnClickListener(this);
        submit_btn.setOnClickListener(this);
        cb_future_client.setOnClickListener(this);
        cb_future_contact.setOnClickListener(this);
        cb_future_sites.setOnClickListener(this);
        tag_lable.setOnClickListener(this);

        //stringJobTitle = new ArrayList<>();
        addAduit_pc = new AddAudit_pc(this);
        addAduit_pc.getClientList();
        addAduit_pc.getCountryList();
        addAduit_pc.getWorkerList();
        addAduit_pc.getTagDataList();
        addAduit_pc.getCurrentdateTime();

        if (!TextUtils.isEmpty(appId)) {
            APPOINMENTJOB = false;

            Appointment appointment = AppDataBase.getInMemoryDatabase(EotApp.getAppinstance()).appointmentModel().getAppointmentById(appId);
            if (TextUtils.isEmpty(appointment.getNm()) && !TextUtils.isEmpty(appointment.getCltId()))
                setClientName(appointment.getCltId());
            else
                auto_client.setText(appointment.getNm());

            cltId = appointment.getCltId();
            cb_future_client.setVisibility(View.GONE);
            input_layout_client.setHintEnabled(true);
            auto_client.setEnabled(false);//not editable client name

            if (!TextUtils.isEmpty(appointment.getCltId())) {
                addAduit_pc.getCOntactList(appointment.getCltId());
                addAduit_pc.getSiteList(appointment.getCltId());
            }

            auto_contact.setText(appointment.getCnm());
            conId = appointment.getConId();
            if (!conId.isEmpty())
                selectedContactData = AppDataBase.getInMemoryDatabase(EotApp.getAppinstance()).contactModel().getContactById(conId);


            input_layout_contact.setHintEnabled(true);
            cb_future_contact.setVisibility(View.VISIBLE);


            auto_contact.setFocusable(true);
            auto_contact.setFocusableInTouchMode(true);
            contact_dp_img.setClickable(true);

            auto_sites.setText(appointment.getSnm());
            /****/
            siteId = appointment.getSiteId();
            if (!siteId.isEmpty())
                selectedSiteData = AppDataBase.getInMemoryDatabase(EotApp.getAppinstance()).sitemodel().getSiteFromSiteId(siteId);


            input_layout_site.setHintEnabled(true);
            cb_future_sites.setVisibility(View.VISIBLE);
            auto_sites.setFocusable(true);
            auto_sites.setFocusableInTouchMode(true);
            site_dp_img.setClickable(true);

            email.setText(appointment.getEmail());
            try {
                if (appointment.getMob1() != null && appointment.getMob1().length() == 0
                        && App_preference.getSharedprefInstance().getLoginRes().getCtryCode() != null) {
                    mob_no.setText(App_preference.getSharedprefInstance().getLoginRes().getCtryCode());
                } else {
                    mob_no.setText(appointment.getMob1());
                }
                if (appointment.getMob2() != null && appointment.getMob2().length() == 0
                        && App_preference.getSharedprefInstance().getLoginRes().getCtryCode() != null) {
                    at_mob.setText(App_preference.getSharedprefInstance().getLoginRes().getCtryCode());
                } else {
                    at_mob.setText(appointment.getMob2());
                }
            } catch (Exception ex) {
                ex.getMessage();
                mob_no.setText(appointment.getMob1());
                at_mob.setText(appointment.getMob2());
            }
            post_code.setText(appointment.getZip());
            city.setText(appointment.getCity());
            adderes.setText(appointment.getAdr());

            auto_country.setText(SpinnerCountrySite.getCountryNameById(appointment.getCtry()));
            ctry_id = appointment.getCtry();
            state_id = appointment.getState();
            auto_states.setText(SpinnerCountrySite.getStatenameById(appointment.getCtry(), appointment.getState()));
            addAduit_pc.getStateList(ctry_id);
            audit_country_layout.setHintEnabled(true);
            audit_state_layout.setHintEnabled(true);
            audit_mob1_layout.setHintEnabled(true);
            audit_mob2_layout.setHintEnabled(true);

        } else {
            setCompanySettingAdrs();
        }
        /***Hide lat & lng filed when permission deny****/
        if (App_preference.getSharedprefInstance().getCompanySettingsDetails().getIsJobLatLngEnable().equals("0")) {
            lng_view.setVisibility(View.GONE);
            lat_view.setVisibility(View.GONE);
            audit_lat_layout.setVisibility(View.GONE);
            audit_lng_layout.setVisibility(View.GONE);
        }


        /***hide/show landmark field***/
        if (App_preference.getSharedprefInstance().getCompanySettingsDetails().getIsLandmarkEnable().equals("0")) {
            landmark_layout.setVisibility(View.GONE);
            landmark_view.setVisibility(View.GONE);
        }


        SettingUrls settingurl = new SettingUrls(Integer.valueOf(App_preference.getSharedprefInstance().getLoginRes().getCompId()), new FirstSyncPC.CallBackFirstSync() {
            @Override
            public void getCallBackOfComplete(int success_no, String msg) {
               /* addJob_pc.getTagDataList();
                addJob_pc.getJobTitleList();*/
            }
        });
        settingurl.getTagList();
        settingurl.getJobTitleList();


        auto_client.setOnClickListener(this);
        auto_contact.setOnClickListener(this);
        auto_sites.setOnClickListener(this);
        auto_country.setOnClickListener(this);
        auto_states.setOnClickListener(this);

        date_start.setOnClickListener(this);
        date_end.setOnClickListener(this);
        time_start.setOnClickListener(this);
        time_end.setOnClickListener(this);
        imvCross.setOnClickListener(this);
        imvCross_site.setOnClickListener(this);

        et_tag.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                add_tag_btn.setVisibility(View.VISIBLE);
                if (charSequence.length() > 0) {
                    if (tagslist != null) {
                        for (TagData item : tagslist) {
                            if (item.getTnm().equals(charSequence.toString())) {
                                add_tag_btn.setVisibility(View.GONE);
                            }
                        }
                    }
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
    }


    @Override
    public void setWorkerList(final ArrayList<FieldWorker> dataa) {
        this.dataa = dataa;
        id_array = new String[dataa.size()];

    }


    @Override
    public void set_Str_DTime(String str_dt_tm, String time_str) {
        date_str = str_dt_tm;
        this.time_str = time_str;
        date_start.setText(date_str);
        time_start.setText(this.time_str);
    }

    @Override
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

        date_start.setText(date_str);
        time_start.setText(time_str);
    }

    @Override
    public void setSetTagData(List<TagData> tagslist) {
        this.tagslist = tagslist;
        if (tagslist.size() > 0) {
            AppUtility.autocompletetextviewPopUpWindow(et_tag);
            et_tag.setThreshold(1);
            DynamicClassAdapter myClassAdapter = new DynamicClassAdapter<TagData>(this, R.layout.custom_adapter_item_layout, R.id.item_title_name, tagslist);
            et_tag.setAdapter(myClassAdapter);
            et_tag.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
//                if (!tagArray.contains((((TagData) adapterView.getItemAtPosition(i))))) {
                    //tagArray.add((TagData) adapterView.getSelectedItem());
                    addChipsForTags((TagData) adapterView.getItemAtPosition(i));
                    // }
                    et_tag.setText("");
                }
            });
        }
    }


    private void addChipsForTags(final TagData tagData) {
        String tagName = "";
        for (int tag = 0; tag <= tagArray.size(); tag++) {
            if (!tagArray.contains(tagData)) {
                tagName = tagData.getName();
                tagArray.add(tagData);
                break;
            }
        }
        i++;
        try {
            LayoutInflater vi = (LayoutInflater) getApplicationContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            final View v = vi.inflate(R.layout.member_item_view, null);

// fill in any details dynamically here
            TextView textView = v.findViewById(R.id.memberName);
            if ((tagName.length() > 0))
                textView.setText(tagName);

            ImageView deleteMember = v.findViewById(R.id.deleteMember);
            deleteMember.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    linear_addTag.removeView((LinearLayout) view.getParent());
                    for (int j = 0; j < tagArray.size(); j++) {
                        tagArray.remove(tagData);
                    }
                }
            });
// insert into main view

            if ((tagName.length() > 0)) {
                linear_addTag.addView(v, 0, new ViewGroup.LayoutParams(ViewGroup.LayoutParams.FILL_PARENT, ViewGroup.LayoutParams.FILL_PARENT));
            }

        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }


    @Override
    public void set_End_Date_Time(String std) {
        String[] time_duration = std.split(" ");
        date_en = time_duration[0];
        try {
            if (App_preference.getSharedprefInstance().getLoginRes().getIs24hrFormatEnable() != null &&
                    App_preference.getSharedprefInstance().getLoginRes().getIs24hrFormatEnable().equals("0"))
                time_en = time_duration[1] + " " + time_duration[2];
            else time_en = time_duration[1] + "";
        } catch (Exception e) {

        }
        date_end.setText(date_en);
        time_end.setText(time_en);
    }


    /**
     * set Contract list accroding to Client
     ******/
    @Override
    public void setContractlist(List<ContractRes> contractlist) {
        AppUtility.spinnerPopUpWindow(contractSpinner);
        if (contractlist.size() > 0) {
            setContractViews(contractlist.get(0));
            contract_parent_view.setVisibility(View.VISIBLE);
            contract_view.setVisibility(View.VISIBLE);
            //   ContractAdpter contractAdpter = new ContractAdpter(this, R.layout.custom_adapter_item_layout, (ArrayList<ContractRes>) contractlist);
            MyAdapter<ContractRes> contractAdpter = new MyAdapter<>(this, R.layout.custom_adapter_item_layout,
                    contractlist);
            contractSpinner.setAdapter(contractAdpter);

            contractSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> adapterView, View view, int pos, long l) {
                    ContractRes model = (ContractRes) adapterView.getItemAtPosition(pos);
                    setContractViews(model);
                }

                @Override
                public void onNothingSelected(AdapterView<?> adapterView) {

                }
            });
        } else {
            contract_parent_view.setVisibility(View.GONE);
            contract_view.setVisibility(View.GONE);
        }
    }


    /****set contractor value's in View****/
    private void setContractViews(ContractRes contractRes) {
        contarct_lable.setText(contractRes.getLabel());
        contract_hint_lable.setVisibility(View.VISIBLE);
        contract_cross_img.setVisibility(View.VISIBLE);
        contrId = contractRes.getContrId();
    }


    @Override
    public void setClientlist(final List<Client> data) {
        AppUtility.autocompletetextviewPopUpWindow(auto_client);
        FilterAdapter filter = new FilterAdapter(this, R.layout.custom_adapter_item_layout, (ArrayList<Client>) data);
        auto_client.setAdapter(filter);
        auto_client.setThreshold(2);
        auto_client.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (filterContact != null) {
                    filterContact.clearListData();
                    filterContact.notifyDataSetInvalidated();
                }

                new_cnm = charSequence.toString();

                if (charSequence.length() >= 1) {
                    input_layout_client.setHintEnabled(true);

                    /****Visible on 3 CHaracter's***/
                    if (charSequence.length() >= 3) {
                        cb_future_client.setVisibility(View.VISIBLE);
                        /****Default check On when Admin Allow permission for Save for future use***/
                        if (App_preference.getSharedprefInstance().getLoginRes().getIsClientForFutureEnable() != null) {
                            cb_future_client.setChecked(App_preference.getSharedprefInstance().getLoginRes().getIsClientForFutureEnable().equals("1"));
                            clientForFuture = contactForFuture = siteForFuture = 1;
                        } else {
                            clientForFuture = contactForFuture = siteForFuture = 0;
                        }

                        addNewClient();
                    }
                } else if (charSequence.length() <= 0) {
                    addNewClient();
                    input_layout_client.setHintEnabled(false);
                    input_layout_site.setHintEnabled(false);
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {
            }
        });

        auto_client.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                cb_future_client.setVisibility(View.GONE);
                cb_future_client.setChecked(false);

                imvCross.setVisibility(View.VISIBLE);
                site_new_add_layout.setVisibility(View.GONE);
                site_dp_layout.setVisibility(View.VISIBLE);

                cltId = (((Client) adapterView.getItemAtPosition(i)).getCltId());
                new_cnm = "";
                clientForFuture = 0;


                addAduit_pc.getContractList(((Client) adapterView.getItemAtPosition(i)).getCltId());
                addAduit_pc.getCOntactList(((Client) adapterView.getItemAtPosition(i)).getCltId());
                addAduit_pc.getSiteList(((Client) adapterView.getItemAtPosition(i)).getCltId());

            }
        });
    }

    /*********/
    private void setCompanySettingAdrs() {
        auto_country.setText(SpinnerCountrySite.getCountryNameById(App_preference.getSharedprefInstance().getCompanySettingsDetails().getCtry()));
        addAduit_pc.getStateList((App_preference.getSharedprefInstance().getCompanySettingsDetails().getCtry()));
        audit_state_layout.setHintEnabled(true);
        auto_states.setText(SpinnerCountrySite.getStatenameById(App_preference.getSharedprefInstance().getCompanySettingsDetails().getCtry(), App_preference.getSharedprefInstance().getCompanySettingsDetails().getState()));
        //   city.setText(App_preference.getSharedprefInstance().getCompanySettingsDetails().getCity());
        state_id = (App_preference.getSharedprefInstance().getCompanySettingsDetails().getState());
        ctry_id = (App_preference.getSharedprefInstance().getCompanySettingsDetails().getCtry());

        audit_mob1_layout.getEditText().addTextChangedListener(this);
        audit_mob2_layout.getEditText().addTextChangedListener(this);

        try {
            if (App_preference.getSharedprefInstance().getLoginRes().getCtryCode() != null) {
                mob_no.setText(App_preference.getSharedprefInstance().getLoginRes().getCtryCode());
                at_mob.setText(App_preference.getSharedprefInstance().getLoginRes().getCtryCode());
            }
        } catch (Exception e) {
            e.getMessage();
        }

    }

    private void addNewClient() {
        HideContractView();
        if (new_cnm.length() > 0) {
            auto_sites.setFocusableInTouchMode(false);
            auto_sites.setFocusable(false);
            cltId = "";
            new_site_nm = "self";
            new_con_nm = "self";

            site_dp_img.setClickable(false);
            contact_dp_img.setClickable(false);

            //   auto_sites.setText(new_site_nm);

            cb_future_sites.setVisibility(View.GONE);
            cb_future_contact.setVisibility(View.GONE);

            edt_lat.setText("");
            edt_lng.setText("");

            newContactForNewClient();
            newSiteForNewClient();

            input_layout_site.setHintEnabled(true);
            input_layout_contact.setHintEnabled(true);


            auto_contact.setFocusableInTouchMode(false);
            auto_contact.setFocusable(false);
            //  auto_contact.setText(new_con_nm);


            contact_add_edt.setText("");
            site_add_edt.setText("");

            //change for new client with add contact and site name
            site_add_edt.setText(new_site_nm);
            site_new_add_layout.setVisibility(View.VISIBLE);
            site_dp_layout.setVisibility(View.GONE);
            imvCross_site.setVisibility(View.GONE);


            contact_add_edt.setText(new_con_nm);
            contact_new_add_layout.setVisibility(View.VISIBLE);
            contact_dp_layout.setVisibility(View.GONE);
            imvCross.setVisibility(View.GONE);

            cb_future_client.setVisibility(View.VISIBLE);


        } else {
            cb_future_client.setVisibility(View.GONE);
            auto_sites.setText("");
            auto_contact.setText("");
            cb_future_client.setChecked(false);
            auto_sites.setFocusableInTouchMode(true);
            clientForFuture = contactForFuture = siteForFuture = 0;
        }
    }


    /**
     * Hide View when New Client add
     ****/
    private void HideContractView() {
        clearContractData();
        contract_view.setVisibility(View.GONE);
        contract_parent_view.setVisibility(View.GONE);
    }

    private void newSiteForNewClient() {
        auto_country.setText("");
        auto_states.setText("");
        post_code.setText("");
        city.setText("");
        adderes.setText("");
        siteId = "";
        //  newSite = true;
    }

    @Override
    public void setContactList(final List<ContactData> data) {
        this.contact_data = data;
        AppUtility.autocompletetextviewPopUpWindow(auto_contact);
        if (APPOINMENTJOB) {
            for (ContactData contactData : data) {
                if (contactData.getDef().equals("1")) {
                    auto_contact.setText(contactData.getCnm());
                    setContactDefaultData(contactData);
                    input_layout_contact.setHintEnabled(true);
                    selectedContactData = contactData;
                    break;
                }
            }
        }
        filterContact = new FilterAdapterContact(this, R.layout.custom_adapter_item_layout, (ArrayList<ContactData>) data);
        auto_contact.setAdapter(filterContact);
        auto_contact.setThreshold(0);

        auto_contact.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                ContactData contactData = (ContactData) ((ListView) adapterView).getAdapter().getItem(i);
                setContactDefaultData(contactData);
                contact_add_edt.setText("");
            }
        });
        auto_contact.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                //new_con_nm = charSequence.toString();
                if (charSequence.length() >= 1) {
                    input_layout_contact.setHintEnabled(true);
                } else if (charSequence.length() <= 0) {
                    input_layout_contact.setHintEnabled(false);
                }

                if (data.size() == 0) {
                    cb_future_contact.setVisibility(View.GONE);
                }


            }

            @Override
            public void afterTextChanged(Editable editable) {
            }
        });

    }

    void setContactDefaultData(ContactData contactData) {
        if (contactData != null) {
            cb_future_contact.setVisibility(View.VISIBLE);
            contact_dp_layout.setVisibility(View.VISIBLE);
            contact_new_add_layout.setVisibility(View.GONE);
            contact_add_edt.setText("");

            auto_contact.setFocusableInTouchMode(true);
            auto_contact.setFocusable(true);
            contact_dp_img.setClickable(true);

            contactForFuture = 0;
            conId = contactData.getConId();
            new_con_nm = "";
            try {
                if (contactData.getMob1() != null && contactData.getMob1().length() == 0
                        && App_preference.getSharedprefInstance().getLoginRes().getCtryCode() != null) {
                    mob_no.setText(App_preference.getSharedprefInstance().getLoginRes().getCtryCode());
                } else {
                    mob_no.setText(contactData.getMob1());
                }
                if (contactData.getMob2() != null && contactData.getMob2().length() == 0
                        && App_preference.getSharedprefInstance().getLoginRes().getCtryCode() != null) {
                    at_mob.setText(App_preference.getSharedprefInstance().getLoginRes().getCtryCode());
                } else {
                    at_mob.setText(contactData.getMob2());
                }
            } catch (Exception ex) {
                ex.getMessage();
                mob_no.setText(contactData.getMob1());
                at_mob.setText(contactData.getMob2());
            }
            email.setText(contactData.getEmail());
        }
    }

    void newContactForNewClient() {
        mob_no.setText("");
        at_mob.setText("");
        email.setText("");
        conId = "";
//        newContact = true;
    }


    @Override
    public void setSiteList(final List<Site_model> data) {
        this.site_data = data;
        AppUtility.autocompletetextviewPopUpWindow(auto_sites);
        if (APPOINMENTJOB) {
            for (Site_model siteData : data) {
                if (siteData.getDef().equals("1")) {
                    auto_sites.setText(siteData.getSnm());
                    setSitetDefaultData(siteData);
                    input_layout_site.setHintEnabled(true);
                    selectedSiteData = siteData;
                    break;
                }
            }
        }
        filterSites = new FilterAdapterSites(this, R.layout.custom_adapter_item_layout, (ArrayList<Site_model>) data);
        auto_sites.setAdapter(filterSites);
        auto_sites.setThreshold(0);
        auto_sites.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Site_model site_model = (Site_model) (adapterView.getAdapter().getItem(i));
                selectedSiteData = site_model;
                setSitetDefaultData(site_model);
                site_add_edt.setText("");
            }
        });
        auto_sites.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                // new_site_nm = charSequence.toString();

                if (charSequence.length() >= 1) {
                    input_layout_site.setHintEnabled(true);
                } else if (charSequence.length() <= 0) {
                    input_layout_site.setHintEnabled(false);
                }


                if (data.size() == 0) {
                    cb_future_sites.setVisibility(View.GONE);
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {
            }
        });
    }

    private void setSitetDefaultData(Site_model sitetData) {
        if (sitetData != null) {
            cb_future_sites.setVisibility(View.VISIBLE);
            siteForFuture = 0;
            new_site_nm = "";
            siteId = sitetData.getSiteId();
            site_add_edt.setText("");

            auto_sites.setFocusableInTouchMode(true);
            auto_sites.setFocusable(true);
            site_dp_img.setClickable(true);

            edt_lat.setText(sitetData.getLat());
            edt_lng.setText(sitetData.getLng());
            adderes.setText(sitetData.getAdr());
            auto_country.setText(SpinnerCountrySite.getCountryNameById(sitetData.getCtry()));
            auto_states.setText(SpinnerCountrySite.getStatenameById((sitetData.getCtry()), sitetData.getState()));
            audit_state_layout.setHintEnabled(true);
            post_code.setText(sitetData.getZip());
            city.setText((sitetData.getCity()));

        }
    }

    @Override
    public void setCountryList(List<Country> countryList) {
        // this.countryList = countryList;
        AppUtility.autocompletetextviewPopUpWindow(auto_country);
        final FilterCountry countryAdapter = new FilterCountry(this, R.layout.custom_adapter_item_layout, (ArrayList<Country>) countryList);
        auto_country.setAdapter(countryAdapter);
        auto_country.setThreshold(1);
        auto_country.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                ctry_id = ((Country) adapterView.getItemAtPosition(i)).getId();
                addAduit_pc.getStateList(ctry_id);
                audit_country_layout.setHintEnabled(true);
            }
        });

        auto_country.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (charSequence.length() >= 1) {
                    audit_country_layout.setHintEnabled(true);
                } else if (charSequence.length() <= 0) {
                    audit_country_layout.setHintEnabled(false);
                }
                auto_states.setText("");
                ctry_id = "";
                state_id = "";
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
    }

    @Override
    public void setStateList(List<States> statesList) {
        FilterStates stateAdapter = new FilterStates(this, R.layout.custom_adapter_item_layout, (ArrayList<States>) statesList);
        auto_states.setAdapter(stateAdapter);
        auto_country.setThreshold(0);
        auto_states.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                state_id = ((States) adapterView.getItemAtPosition(i)).getId();
                audit_state_layout.setHintEnabled(true);
            }
        });


        auto_states.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (charSequence.length() >= 1) {
                    audit_state_layout.setHintEnabled(true);
                } else if (charSequence.length() <= 0) {
                    audit_state_layout.setHintEnabled(false);
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
    }


    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int pos, long l) {
        switch (parent.getId()) {

        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {
    }

    @Override
    public void clientNameError(String msg) {
        showErrorDialog(msg);
    }

    @Override
    public void setAddr_Error(String msg) {
        showErrorDialog(msg);
    }

    @Override
    public void set_auditor_Error(String title) {
        showErrorDialog(title);
    }

    @Override
    public void setCountryError(String msg) {
        showErrorDialog(msg);
    }

    @Override
    public void setStateError(String msg) {
        showErrorDialog(msg);

    }

    @Override
    public void errorMsg(String error) {
        showErrorDialog(error);
    }

    private void showErrorDialog(String msg) {
        AppUtility.error_Alert_Dialog(this, msg, LanguageController.getInstance().getMobileMsgByKey(AppConstant.ok)
                , new Callable<Boolean>() {
                    @Override
                    public Boolean call() throws Exception {
                        return null;
                    }
                });
    }

    private void setDateTimeEmptyTime() {
        date_start.setText("");
        time_start.setText("");
        date_end.setText("");
        time_end.setText("");
        date_start.setHint(LanguageController.getInstance().getMobileMsgByKey(AppConstant.date_form));
        time_start.setHint(LanguageController.getInstance().getMobileMsgByKey(AppConstant.time_form));
        date_end.setHint(LanguageController.getInstance().getMobileMsgByKey(AppConstant.date_form));
        time_end.setHint(LanguageController.getInstance().getMobileMsgByKey(AppConstant.time_form));
        date_str = time_str = date_en = time_en = "";
    }

    @Override
    public void setMobError(String msg) {
        showErrorDialog(msg);
    }

    @Override
    public void setEmailError(String msg) {
        showErrorDialog(msg);
    }

    //Convert DP to Pixel
    private int getPxFromDp(int dp) {
        return (int) (dp * getResources().getDisplayMetrics().density);
    }

    //private boolean isDefaultFwSelected=false;
    public void setSelectedJobServices(ArrayList<FieldWorker> selectedJobServices) {
        AuditorsList.clear();
        Set<String> jobServiceNm = new HashSet<>();

        for (FieldWorker jobTitle : selectedJobServices) {
            if (!isDefaultFwSelected && jobTitle.getUsrId().equals(App_preference.getSharedprefInstance().getLoginRes().getUsrId())) {
                jobTitle.setSelect(true);
                isDefaultFwSelected = true;
            }
            if (jobTitle.isSelect()) {
                AuditorsList.add(jobTitle.getUsrId());
                jobServiceNm.add(jobTitle.getName());

            }
        }
// jtIdList = title_ids;
        if (jobServiceNm.size() >= 4)
            auditservicetxtlableset.setText(LanguageController.getInstance().getMobileMsgByKey(AppConstant.items_selected) + " " + jobServiceNm.size());
        else
            auditservicetxtlableset.setText(jobServiceNm.toString().replace("[", "").replace("]", ""));
        if (jobServiceNm.size() > 0) {
            auditservicelablehint.setVisibility(View.VISIBLE);
        } else auditservicelablehint.setVisibility(View.INVISIBLE);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.site_dp_img:
            case R.id.input_layout_site:
                auto_sites.showDropDown();
                break;
            case R.id.contact_dp_img:
            case R.id.input_layout_contact:
                auto_contact.showDropDown();
                break;
            case R.id.contract_cross_img:
                clearContractData();
                break;
            case R.id.contarct_lable:
            case R.id.contract_dp_img:
                contractSpinner.performClick();
                break;
            case R.id.auditservicelayout:

                if (id_array.length > 0) {
                    customDPController.showSpinnerDropDown(this, auditservicelayout, dataa);
                } else {
                    AppUtility.alertDialog(this, "", LanguageController.getInstance().getMobileMsgByKey(AppConstant.err_empty_Title), LanguageController.getInstance().getMobileMsgByKey(AppConstant.ok), "", new Callable<Boolean>() {
                        @Override
                        public Boolean call() throws Exception {
                            return null;
                        }
                    });
                }

                break;
            case R.id.date_time_clear_btn:
                setDateTimeEmptyTime();
                break;

            case R.id.cb_future_client:
                if (cb_future_client.isChecked()) {
                    clientForFuture = siteForFuture = contactForFuture = 1;
                } else {
                    clientForFuture = siteForFuture = contactForFuture = 0;
                }
                break;
            case R.id.cb_future_contact:
                contact_new_add_layout.setVisibility(View.VISIBLE);
                contact_dp_layout.setVisibility(View.GONE);
                cb_future_contact.setVisibility(View.GONE);
                imvCross.setVisibility(View.VISIBLE);

                newContactForNewClient();
                break;
            case R.id.imvCross:
                cb_future_contact.setVisibility(View.VISIBLE);
                contact_new_add_layout.setVisibility(View.GONE);
                contact_dp_layout.setVisibility(View.VISIBLE);
                contact_add_edt.setText("");
                if (selectedContactData != null)
                    setContactDefaultData(selectedContactData);
                break;
            case R.id.imvCross_site:
                cb_future_sites.setVisibility(View.VISIBLE);
                site_new_add_layout.setVisibility(View.GONE);
                site_dp_layout.setVisibility(View.VISIBLE);
                site_add_edt.setText("");
                if (selectedSiteData != null)
                    setSitetDefaultData(selectedSiteData);
                break;
            case R.id.cb_future_sites:
                new_site_nm = "";
                site_new_add_layout.setVisibility(View.VISIBLE);
                site_dp_layout.setVisibility(View.GONE);
                cb_future_sites.setVisibility(View.GONE);
                imvCross_site.setVisibility(View.VISIBLE);
                newSiteForNewClient();
                break;
            case R.id.date_start:
                SelectDate();
                break;
            case R.id.date_end:
                SelectDate1();
                break;
            case R.id.time_start:
                SelectTime();
                break;
            case R.id.time_end:
                SelectTime1();
                break;
            case R.id.add_tag_btn:
                String tagName = et_tag.getText().toString();
                if (tagName.length() > 0) {
                    final TagData tagdata = new TagData();
                    tagdata.setTagId("");
                    tagdata.setTnm(tagName);
                    addChipsForTags(tagdata);
                    et_tag.setText("");
                }
                break;
            case R.id.submit_btn:
                submit_btn.setEnabled(false);
                createAddAduitRequest();
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        submit_btn.setEnabled(true);
                    }
                }, 500);


                break;

            case R.id.tag_lable:
                et_tag.showDropDown();
                break;
        }
    }


    public void createAddAduitRequest() {

        if (!cb_future_client.isChecked()) {
            clientForFuture = contactForFuture = siteForFuture = 0;
        }


        if (!contact_add_edt.getText().toString().trim().equals("")) {
            contactForFuture = 1;
            conId = "";
            new_con_nm = contact_add_edt.getText().toString().trim();
        }

        if (!site_add_edt.getText().toString().trim().equals("")) {
            siteForFuture = 1;
            siteId = "";
            new_site_nm = site_add_edt.getText().toString().trim();
        }

        if (conId.isEmpty() && new_con_nm.isEmpty()) {
            setContactDefaultData(selectedContactData);
        }


        if (siteId.isEmpty() && new_site_nm.isEmpty()) {
            setSitetDefaultData(selectedSiteData);
        }

        if (AuditorsList.isEmpty()) {
            member_type = 1;
            kpr = "";
        } else if (AuditorsList.size() == 1) {
            member_type = 1;
            Iterator itr = AuditorsList.iterator();
            while (itr.hasNext()) {
                kpr = itr.next().toString();
            }

        } else {
            member_type = 2;
            kpr = null;
        }

        if (date_str.isEmpty()) { //remove space from schdlFinish & schdlStart
            schdlStart = schdlFinish = "";
        } else {
            schdlStart = date_str + " " + time_str;
            schdlFinish = date_en + " " + time_en;
        }


        String adr = adderes.getText().toString();


        String countryname, statename;
        countryname = auto_country.getText().toString();
        statename = auto_states.getText().toString();
        if (ctry_id.equals("")) {
            ctry_id = addAduit_pc.cntryId(countryname);
            state_id = addAduit_pc.statId(ctry_id, statename);
        }

        if (!cltId.equals("")) {
            String contact_name = auto_contact.getText().toString().trim();

            if (contact_name.length() > 0) {
                List<String> contact = new ArrayList<>();
                if (contact_data != null) {
                    for (ContactData contactData : contact_data) {
                        contact.add(contactData.getCnm());
                    }
                }
                if (!contact.contains(contact_name)) {
                    AppUtility.error_Alert_Dialog(this, LanguageController.getInstance().getMobileMsgByKey(AppConstant.valid_contact_check), "Ok", null);
                    return;
                }
            }

            String site_name = auto_sites.getText().toString().trim();

            if (site_name.length() > 0) {
                List<String> site = new ArrayList<>();
                if (site_data != null) {
                    for (Site_model siteData : site_data) {
                        site.add(siteData.getSnm());
                    }
                }
                if (!site.contains(site_name)) {
                    AppUtility.error_Alert_Dialog(this, LanguageController.getInstance().getMobileMsgByKey(AppConstant.valid_site_check), "Ok", null);
                    return;
                }
            }
        }

        if (schdlStart.isEmpty()) {
            callAddAudit(adr);
        } else {
            if (!conditionCheck(schdlStart, schdlFinish)) {
                EotApp.getAppinstance().
                        showToastmsg(LanguageController.getInstance().getMobileMsgByKey(AppConstant.Sche_end_start_time));
            } else {
                callAddAudit(adr);
            }
        }
    }

    /**
     * Clear Selected Contract Data
     ****/
    private void clearContractData() {
        contract_hint_lable.setVisibility(View.GONE);
        contarct_lable.setText("");
        contarct_lable.setText(LanguageController.getInstance().getMobileMsgByKey(AppConstant.contract));
        contract_cross_img.setVisibility(View.GONE);
        contrId = "";
    }


    private void callAddAudit(String adr) {

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
                    time_str = time_start.getText().toString() + " " + "PM";
                    //   time_str = time_str + " " + "PM";
                }
                if (t2 != 12) {
                    endDate = new SimpleDateFormat("hh:mm", Locale.getDefault()).parse(time_en);
                    time_en = "";
                    time_en = new SimpleDateFormat("hh:mm a", Locale.getDefault()).format(endDate);
                } else {
                    time_en = "";
                    time_en = time_end.getText().toString() + " " + "PM";
                    //   time_en = time_en + " " + "PM";
                }
            } catch (ParseException e) {
                e.printStackTrace();
            }
            schdlStart = date_str + " " + time_str;
            schdlFinish = date_en + " " + time_en;
        }

        //  if (mob_no.getText().toString().trim().equals())
        //String m1= mob_no.getText().toString().trim()

        if (addAduit_pc.RequiredFields(auto_client.getText().toString(), newContact, newSite, new_con_nm, new_site_nm, adr, this.AuditorsList, ctry_id, state_id, mob_no.getText().toString().trim(), at_mob.getText().toString().trim(), email.getText().toString())) {
            final AddAudit_Req addJob_req = new AddAudit_Req(member_type,
                    cltId,
                    auto_client.getText().toString(),
                    siteId,
                    conId,
                    contrId,
                    "",
                    audit_desc.getText().toString(),
                    1,
                    App_preference.getSharedprefInstance().getLoginRes().getUsrId(),
                    kpr,
                    schdlStart,
                    schdlFinish,
                    audit_instr.getText().toString(),
                    new_con_nm,
                    new_site_nm,
                    email.getText().toString(),
                    mob_no.getText().toString(),
                    at_mob.getText().toString(),
                    adr,
                    city.getText().toString(),
                    state_id,
                    ctry_id,
                    post_code.getText().toString(),
                    this.AuditorsList,
                    tagArray,
                    AppUtility.getDateByFormat(AppConstant.DATE_TIME_FORMAT),
                    edt_lat.getText().toString(),
                    edt_lng.getText().toString(),
                    landmark_edt.getText().toString(),
                    "1",
                    tempId + 1, clientForFuture, siteForFuture, contactForFuture);

            /**
             * get the anser submitted by the user
             *
             * */
            if (customFiledQueAdpter != null) {
                ArrayList<CustOmFormQuestionsRes> typeList = customFiledQueAdpter.getTypeList();
                if (typeList != null) {
                    ArrayList<Answer> customFieldAnswerList = getCustomFieldAnswerList(typeList);
                    addJob_req.setAnswerArray(customFieldAnswerList);
                    Log.d("answerOfCustomField", customFiledQueAdpter.toString());
                }
            }

            ArrayList links = new ArrayList();
            List<String> fileNames = new ArrayList<>();
            String jobDescription = mEditor.getHtml();
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

                addJob_req.setDes("<p>" + jobDescription + "</p>");

            }

            if (links != null && links.size() > 0) {
                if (AppUtility.isInternetConnected())
                    addAduit_pc.addJobWithImageDescription(addJob_req, links, fileNames);
                else {
                    AppUtility.alertDialog2(AddAuditActivity.this,
                            LanguageController.getInstance().getMobileMsgByKey(AppConstant.add_new_audit),
                            LanguageController.getInstance().getMobileMsgByKey(AppConstant.add_audit_offline_msg),
                            LanguageController.getInstance().getMobileMsgByKey(AppConstant.yes),
                            LanguageController.getInstance().getMobileMsgByKey(AppConstant.no), new Callback_AlertDialog() {
                                @Override
                                public void onPossitiveCall() {
                                    String des = addJob_req.getDes();
                                    if (!TextUtils.isEmpty(des))
                                        addJob_req.setDes(Html.fromHtml(des).toString());
                                    addAduit_pc.callApiForAddAudit(addJob_req);
                                }

                                @Override
                                public void onNegativeCall() {

                                }
                            });
                }

            } else addAduit_pc.callApiForAddAudit(addJob_req);


            //  addAduit_pc.callApiForAddAudit(addJob_req);

            //
        }
    }

    public ArrayList<Answer> getCustomFieldAnswerList(ArrayList<CustOmFormQuestionsRes> questionList) {

        ArrayList<Answer> answerArrayList = new ArrayList<>();

        for (int i = 0; i < questionList.size(); i++) {
            String key = "";
            String ans = "";
            ArrayList<AnswerModel> ansArrayList = new ArrayList<>();
            Answer answer = null;
            switch (questionList.get(i).getType()) {
                case "8":
                    if (questionList.get(i).getAns() != null && questionList.get(i).getAns().size() > 0) {
                        AnswerModel answerModel = new AnswerModel(questionList.get(i).getAns().get(0).getKey(), questionList.get(i).getAns().get(0).getValue());
                        ansArrayList.add(answerModel);
                        answer = new Answer(questionList.get(i).getQueId(), questionList.get(i).getType(), ansArrayList, questionList.get(i).getFrmId());
                        answerArrayList.add(answer);
                    }

                    break;
                case "2":
                case "5":
                case "6":
                case "7":
                case "1":
                    if (questionList.get(i).getAns() != null && questionList.get(i).getAns().size() > 0) {
                        if (questionList.get(i).getType().equals("5")) {
                            if (!TextUtils.isEmpty(questionList.get(i).getAns().get(0).getValue())) {
                                long l = Long.parseLong(questionList.get(i).getAns().get(0).getValue());
                                String date = AppUtility.getDate(l, "dd-MMM-yyyy");
                                ans = date;
                            }
                        } else if (questionList.get(i).getType().equals("6")) {
                            if (!TextUtils.isEmpty(questionList.get(i).getAns().get(0).getValue())) {
                                long l = Long.parseLong(questionList.get(i).getAns().get(0).getValue());
                                String formate = AppUtility.dateTimeByAmPmFormate(
                                        "hh:mm a", "kk:mm");
                                String date = AppUtility.getDate(l, formate);
                                ans = date;
                            }
                        } else if (questionList.get(i).getType().equals("7")) {
                            if (!TextUtils.isEmpty(questionList.get(i).getAns().get(0).getValue())) {
                                long l = Long.parseLong(questionList.get(i).getAns().get(0).getValue());
                                String formate = AppUtility.dateTimeByAmPmFormate("dd-MMM-yyyy hh:mm a", "dd-MMM-yyyy kk:mm");
                                String date = AppUtility.getDate(l, formate);
                                ans = date;
                            }
                        } else
                            ans = questionList.get(i).getAns().get(0).getValue();
                        AnswerModel answerModel = new AnswerModel(key, ans);
                        ansArrayList.add(answerModel);
                        answer = new Answer(questionList.get(i).getQueId(),
                                questionList.get(i).getType(), ansArrayList, questionList.get(i).getFrmId());
                        answerArrayList.add(answer);

                    }
                    break;
                case "4":
                case "3":
                    if (questionList.get(i).getAns() != null && questionList.get(i).getAns().size() > 0) {
                        List<AnswerModel> ans1 = questionList.get(i).getAns();
                        if (ans1 != null)
                            for (AnswerModel am : ans1) {
                                key = am.getKey();
                                ans = am.getValue();
                                AnswerModel answerModel = new AnswerModel(key, ans);
                                ansArrayList.add(answerModel);
                            }
                    }
                    if (ansArrayList.size() > 0) {
                        answer = new Answer(questionList.get(i).getQueId(), questionList.get(i).getType(), ansArrayList, questionList.get(i).getFrmId());
                        answerArrayList.add(answer);
                    } else {
                        answer = new Answer(questionList.get(i).getQueId(),
                                questionList.get(i).getType(), ansArrayList, questionList.get(i).getFrmId());
                        answerArrayList.add(answer);
                    }
                    break;
            }

        }


        return answerArrayList;

    }


    //start date,time must be grater than to end date time
    private boolean conditionCheck(String schdlStart, String schdlFinish) {
        Locale.getDefault().getDisplayLanguage();
        try {
            SimpleDateFormat gettingfmt = new SimpleDateFormat(AppUtility.dateTimeByAmPmFormate(
                    "dd-MM-yyyy hh:mm a", "dd-MM-yyyy kk:mm"), Locale.US);//, Locale.US
            Date date = gettingfmt.parse(schdlStart);
            date.getTime();

            Date date1 = gettingfmt.parse(schdlFinish);
            date1.getTime();
            if (date1.getTime() > date.getTime())
                return true;

        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    @Override
    public void finishActivity() {
        Intent intent = new Intent();
        setResult(MainActivity.AuditADD, intent);
        this.finish();

    }


    //get start date
    private void SelectDate() {
        final Calendar c = Calendar.getInstance();
        year = c.get(Calendar.YEAR);
        month = c.get(Calendar.MONTH);
        day = c.get(Calendar.DAY_OF_MONTH);
        showDialog(R.id.date_start);
    }

    //get end date
    private void SelectDate1() {
        final Calendar c = Calendar.getInstance();
        year = c.get(Calendar.YEAR);
        month = c.get(Calendar.MONTH);
        day = c.get(Calendar.DAY_OF_MONTH);
        showDialog(R.id.date_end);
    }

    //schedule start time
    private void SelectTime() {
        final Calendar c = Calendar.getInstance();
        mHour = c.get(Calendar.HOUR_OF_DAY);
        mMinute = c.get(Calendar.MINUTE);
        showDialog(R.id.time_start);
    }

    //schedule end time
    private void SelectTime1() {
        final Calendar c = Calendar.getInstance();
        mHour = c.get(Calendar.HOUR_OF_DAY);
        mMinute = c.get(Calendar.MINUTE);
        showDialog(R.id.time_end);
    }


    @Override
    protected Dialog onCreateDialog(int id) {
        switch (id) {
            case R.id.date_start:
                DatePickerDialog datePickerDialogSelectDate = new DatePickerDialog(this, AppUtility.InputDateSet(this, new Add_job_activity.DateTimeCallback() {
                    @Override
                    public void setDateTime(String dateTime) {
                        date_start.setText(dateTime);
                        date_end.setText(dateTime);
                        date_en = date_str = dateTime;
                    }
                }, LanguageController.getInstance().getMobileMsgByKey(AppConstant.err_start_end_date)), year, month, day);
                return datePickerDialogSelectDate;

            case R.id.date_end:
                final DatePickerDialog datePickerDialog = new DatePickerDialog(this, AppUtility.CompareInputOutputDate(this, new Add_job_activity.DateTimeCallback() {
                    @Override
                    public void setDateTime(String dateTime) {
                        date_end.setText(dateTime);
                        date_en = dateTime;
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
                        time_start.setText((formatter.format(Integer.parseInt(aa[0]))) + ":" + aa[1]);
                    }
                }, LanguageController.getInstance().getMobileMsgByKey(AppConstant.err_start_end_time)), mHour, mMinute, true);
                return timePickerDialog;

            case R.id.time_end:
                TimePickerDialog timePickerDialog1 = new TimePickerDialog(this, AppUtility.OutPutTime(this, new Add_job_activity.DateTimeCallback() {
                    @Override
                    public void setDateTime(String dateTime) {
                        time_en = dateTime;
                        DecimalFormat formatter = new DecimalFormat("00");
                        String[] aa = dateTime.split(":");
                        time_end.setText((formatter.format(Integer.parseInt(aa[0]))) + ":" + aa[1]);
                    }
                }, LanguageController.getInstance().getMobileMsgByKey(AppConstant.err_due_start_time)), mHour, mMinute, true);
                return timePickerDialog1;

        }
        return null;
    }


    @Override
    public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

    }

    @Override

    public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
        /**Floating hint enbale after text enter**/
        if (charSequence.length() >= 1) {
            if (charSequence.hashCode() == audit_desc.getText().hashCode())
                audit_desc_layout.setHintEnabled(true);
            if (charSequence.hashCode() == audit_instr.getText().hashCode())
                audit_instr_layout.setHintEnabled(true);
            if (charSequence.hashCode() == mob_no.getText().hashCode())
                audit_mob1_layout.setHintEnabled(true);
            if (charSequence.hashCode() == at_mob.getText().hashCode())
                audit_mob2_layout.setHintEnabled(true);
            if (charSequence.hashCode() == email.getText().hashCode())
                audit_email_layout.setHintEnabled(true);
            if (charSequence.hashCode() == adderes.getText().hashCode())
                audit_adr_layout.setHintEnabled(true);
            if (charSequence.hashCode() == edt_lat.getText().hashCode())
                audit_lat_layout.setHintEnabled(true);
            if (charSequence.hashCode() == edt_lng.getText().hashCode())
                audit_lng_layout.setHintEnabled(true);
            if (charSequence.hashCode() == city.getText().hashCode())
                audit_city_layout.setHintEnabled(true);
            if (charSequence.hashCode() == post_code.getText().hashCode())
                audit_postal_layout.setHintEnabled(true);
            if (charSequence.hashCode() == landmark_edt.getText().hashCode())
                landmark_layout.setHintEnabled(true);
            if (charSequence.hashCode() == contact_add_edt.getText().hashCode())
                contact_add_layout.setHintEnabled(true);
            if (charSequence.hashCode() == site_add_edt.getText().hashCode())
                site_add_layout.setHintEnabled(true);

        } else if (charSequence.length() <= 0) {
            /**Floating hint Disable after text enter**/
            if (charSequence.hashCode() == audit_desc.getText().hashCode())
                audit_desc_layout.setHintEnabled(false);
            if (charSequence.hashCode() == audit_instr.getText().hashCode())
                audit_instr_layout.setHintEnabled(false);
            if (charSequence.hashCode() == mob_no.getText().hashCode())
                audit_mob1_layout.setHintEnabled(false);
            if (charSequence.hashCode() == mob_no.getText().hashCode())
                audit_mob2_layout.setHintEnabled(false);
            if (charSequence.hashCode() == email.getText().hashCode())
                audit_email_layout.setHintEnabled(false);
            if (charSequence.hashCode() == adderes.getText().hashCode())
                audit_adr_layout.setHintEnabled(false);
            if (charSequence.hashCode() == edt_lat.getText().hashCode())
                audit_lat_layout.setHintEnabled(false);
            if (charSequence.hashCode() == edt_lng.getText().hashCode())
                audit_lng_layout.setHintEnabled(false);
            if (charSequence.hashCode() == city.getText().hashCode())
                audit_city_layout.setHintEnabled(false);
            if (charSequence.hashCode() == post_code.getText().hashCode())
                audit_postal_layout.setHintEnabled(false);
            if (charSequence.hashCode() == landmark_edt.getText().hashCode())
                landmark_layout.setHintEnabled(false);
            if (charSequence.hashCode() == contact_add_edt.getText().hashCode())
                contact_add_layout.setHintEnabled(false);
            if (charSequence.hashCode() == site_add_edt.getText().hashCode())
                site_add_layout.setHintEnabled(false);
        }
    }

    @Override
    public void afterTextChanged(Editable editable) {

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            onBackPressed();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }


}