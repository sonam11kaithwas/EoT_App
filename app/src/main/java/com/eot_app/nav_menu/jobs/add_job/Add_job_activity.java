package com.eot_app.nav_menu.jobs.add_job;

import static android.location.LocationManager.GPS_PROVIDER;

import android.Manifest;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Location;
import android.location.LocationManager;
import android.os.Build;
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
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.core.app.ActivityCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.eot_app.R;
import com.eot_app.UploadDocumentActivity;
import com.eot_app.custom_dropDown.CustomDPController;
import com.eot_app.eoteditor.CustomEditor;
import com.eot_app.eoteditor.EotEditor;
import com.eot_app.home_screens.MainActivity;
import com.eot_app.nav_menu.appointment.dbappointment.Appointment;
import com.eot_app.nav_menu.client.client_db.Client;
import com.eot_app.nav_menu.client.clientlist.client_detail.contact.client_dao.ContactData;
import com.eot_app.nav_menu.client.clientlist.client_detail.site.sitelist.Site_model;
import com.eot_app.nav_menu.client.clientlist.client_detail.site.sitelist.editsite.editsitedb.SpinnerCountrySite;
import com.eot_app.nav_menu.custom_fileds.custom_filed_adpter.CustomFieldJobAdapter;
import com.eot_app.nav_menu.custom_fileds.custom_model.CustOmFormQuestionsRes;
import com.eot_app.nav_menu.jobs.add_job.adapters.DynamicClassAdapter;
import com.eot_app.nav_menu.jobs.add_job.adapters.FilterAdapter;
import com.eot_app.nav_menu.jobs.add_job.adapters.FilterAdapterContact;
import com.eot_app.nav_menu.jobs.add_job.adapters.FilterAdapterSites;
import com.eot_app.nav_menu.jobs.add_job.adapters.FilterCountry;
import com.eot_app.nav_menu.jobs.add_job.adapters.FilterStates;
import com.eot_app.nav_menu.jobs.add_job.adapters.TimeSHiftAdpter;
import com.eot_app.nav_menu.jobs.add_job.add_job_recr.AddJobRecrHomeActivity;
import com.eot_app.nav_menu.jobs.add_job.add_job_recr.daily_recr_pkg.CustomWeekSelector;
import com.eot_app.nav_menu.jobs.add_job.add_job_recr.daily_recr_pkg.daily_recur_model.JobRecurModel;
import com.eot_app.nav_menu.jobs.add_job.addjob_presenter.AddJob_pc;
import com.eot_app.nav_menu.jobs.add_job.addjob_presenter.Add_job_pi;
import com.eot_app.nav_menu.jobs.add_job.addjobmodel.AddJob_Req;
import com.eot_app.nav_menu.jobs.add_job.job_weekly_pkg.JOB_weekly_recur_PC;
import com.eot_app.nav_menu.jobs.add_job.job_weekly_pkg.JOB_weekly_recur_PI;
import com.eot_app.nav_menu.jobs.add_job.job_weekly_pkg.JOB_weekly_recur_View;
import com.eot_app.nav_menu.jobs.job_detail.form_form.get_qus_list.ans_model.Answer;
import com.eot_app.nav_menu.jobs.job_detail.form_form.get_qus_list.qus_model.AnswerModel;
import com.eot_app.time_shift_pkg.ShiftTimeReSModel;
import com.eot_app.utility.AppConstant;
import com.eot_app.utility.AppUtility;
import com.eot_app.utility.App_preference;
import com.eot_app.utility.Country;
import com.eot_app.utility.CurrLatLngCntrlr;
import com.eot_app.utility.EotApp;
import com.eot_app.utility.States;
import com.eot_app.utility.db.AppDataBase;
import com.eot_app.utility.language_support.LanguageController;
import com.eot_app.utility.language_support.Language_Preference;
import com.eot_app.utility.settings.SettingUrls;
import com.eot_app.utility.settings.contractdb.ContractRes;
import com.eot_app.utility.settings.firstSync.FirstSyncPC;
import com.eot_app.utility.settings.setting_db.FieldWorker;
import com.eot_app.utility.settings.setting_db.JobTitle;
import com.eot_app.utility.settings.setting_db.Suggestion;
import com.eot_app.utility.settings.setting_db.TagData;
import com.eot_app.utility.util_interfaces.Callback_AlertDialog;
import com.eot_app.utility.util_interfaces.JoBServSuggAdpter;
import com.eot_app.utility.util_interfaces.MyAdapter;
import com.eot_app.utility.util_interfaces.MySpinnerAdapter;
import com.google.android.material.textfield.TextInputLayout;
import com.google.gson.Gson;
import com.hypertrack.hyperlog.HyperLog;

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
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
import java.util.Set;
import java.util.concurrent.Callable;

public class Add_job_activity extends UploadDocumentActivity implements AddjobView,
        AdapterView.OnItemSelectedListener, View.OnClickListener
        , TextWatcher, CompoundButton.OnCheckedChangeListener, JOB_weekly_recur_View {

    public static final int ADDCUSTOMRECUR = 501;
    public static final int LOCATION_REQUEST = 1000;
    final Set<String> listwork = new HashSet<>();
    private final ArrayList<String> weekDays = new ArrayList<>();
    private final Set<String> jtIdList = new HashSet<>();
    private final ArrayList<TagData> tagArray = new ArrayList<>();
    private final boolean newContact = false;
    private final boolean newSite = false;
    CustomDPController customDPController;
    CustomEditor mEditor;
    ImageView action_insert_image;
    ImageView action_insert_suggestion;
    ArrayList<JobRecurModel> recudata = new ArrayList<>();
    List<LinkedHashMap<String, Boolean>> selectedDays = new ArrayList<>();
    List<ContactData> contact_data;
    List<Site_model> site_data;
    List<Suggestion> suggestions = new ArrayList<>();
    String[] suggestionsArray = new String[suggestions.size()];
    JoBServSuggAdpter suggestionAdapter;
    private View lat_lng_view_lay;
    private LocationManager locationManager;
    private TextView lat_lng_txt_lable;
    private String isRecur = "";
    private String startDate = "";
    private LinkedHashMap<String, Boolean> selectedDaysMap = new LinkedHashMap<>();
    private String recurType = "", recurMsg = "";
    private String endRecurMode = "";
    private String endDate = "";
    private TextView cb_future_contact, cb_future_sites;
    private Spinner job_priority_spinner, contractSpinner, job_suggestion_spinner;
    private Add_job_pi addJob_pc;
    private FilterAdapterSites filterSites;
    private FilterAdapterContact filterContact;
    private TextInputLayout input_layout_client, input_layout_contact, input_layout_site, job_state_layout, job_desc_layout, job_instr_layout, job_mob1_layout, job_mob2_layout, job_email_layout, job_adr_layout, job_lat_layout, job_lng_layout, job_city_layout, job_postal_layout, job_prty_lyut, landmark_layout, job_country_layout, contact_add_layout, site_add_layout;
    private AutoCompleteTextView auto_client, auto_contact, auto_sites, auto_states, members, et_tag, auto_country;
    private CheckBox cb_future_client;
    private LinearLayout linearMainView, linear_addTag, assignto_linear;
    private EditText job_desc, job_instr, ctryCode, mob_no, at_mob, email, adderes, city, post_code, edt_lng, edt_lat, landmark_edt, contact_add_edt, site_add_edt;
    private Button add_tag_btn, submit_btn, date_time_clear_btn;
    private TextView date_start, time_start, date_end, time_end, tv_spinner, tv_hint, tag_lable, assign_to, schel_start, schel_end;
    private String cltId = "";
    private String siteId = "";
    private String conId = "";
    private String prty = AppConstant.Prioty[1];
    private String new_cnm = "";
    private String new_site_nm = "";
    private String new_con_nm = "";
    private String ctry_id = "";
    private String state_id = "";
    private int clientForFuture = 0, siteForFuture = 0, contactForFuture = 0, member_type;
    private String date_str, time_str, date_en, time_en, schdlStart, schdlFinish;
    private int i = 1;
    private String kpr;
    private String[] job_prioty;
    private RelativeLayout relative_main;
    private LinearLayout j_prio_linearLayout;
    private int s;
    private int su;
    private String tempId;
    private String[] id_array;
    private int year, month, day, mHour, mMinute;
    private List<TagData> tagslist;
    private View lng_view, lat_view, adr_view, landmark_view, contract_view;
    private String schdlStart1, contrId = "";
    private LinearLayout jobservicelayout;
    private TextView jobservicetxtlableset, jobservicelablehint, jobdeshint, contract_hint_lable, contarct_lable;
    private ArrayList<JobTitle> datastr = new ArrayList<>();
    private ImageView contract_cross_img, contract_dp_img, site_dp_img, contact_dp_img, imvCross, imvCross_site;
    private RelativeLayout contract_parent_view;
    private LinearLayout contact_new_add_layout, contact_dp_layout, site_dp_layout, site_new_add_layout;
    private String appId = "";
    private boolean APPOINMENTJOB = true;//
    private ContactData selectedContactData;
    private Site_model selectedSiteData;
    private Appointment appointment;
    private RecyclerView recyclerViewCustomField;
    private CustomFieldJobAdapter customFiledQueAdpter;
    /*****Recur View's****/
    private LinearLayout normal_weekly_recur, recur_view, recur_parent_view, recur_pattern_view;
    private CheckBox add_recur_checkBox;
    private RadioButton radio_on, radio_never_end;
    private TextView end_date_for_weekly_recur, recur_job_days_msg, cutom_txt;
    private JOB_weekly_recur_PI jobWeeklyRecurPi;
    private AppCompatTextView custom_recur_pattern, recur_not_work;
    private int RECURTYPENORMAL_CUSTOM = 1;
    private RelativeLayout msg_pattern_view;
    private String tempStrDt, tempEndDt, tempStrTm, tempEndTm;
    private CustomWeekSelector mon_week_day, tu_week_day, wed_week_day, thu_week_day, fri_week_day, sat_week_day, sun_week_day;

    private TextView shift_time_txt;
    private Switch switch_btn;
    private Spinner time_shift_dp;
    private TimeSHiftAdpter timeSHiftAdpter;
    private List<ShiftTimeReSModel> list = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_job);
        setTitle(LanguageController.getInstance().getMobileMsgByKey(AppConstant.title_add_job));

        //check the add job called form appointment and get the appId
        if (getIntent().hasExtra("appId")) {
            appId = getIntent().getStringExtra("appId");
        }
        customDPController = new CustomDPController();
        initializelables();
        initializeView();
        textInputLayoutHint();
        jobPrioritySet();

        setTaskPriority();

        mEditor.setPlaceholder(LanguageController.getInstance().getMobileMsgByKey(AppConstant.job_desc));
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
                mEditor.focusEditor();
                HyperLog.i("Add_job_activity", "action_insert_image(M) started");
                selectFile(true);
                HyperLog.i("Add_job_activity", "action_insert_image(M) End");

            }
        });
        action_insert_suggestion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mEditor.focusEditor();
                try {
                    if (suggestionsArray != null && suggestionsArray.length > 0)
                        job_suggestion_spinner.performClick();
                    else {
                        AppUtility.alertDialog(Add_job_activity.this,
                                "", LanguageController.getInstance()
                                        .getMobileMsgByKey(AppConstant.no_suggesstion)
                                , LanguageController.getInstance().getMobileMsgByKey(AppConstant.ok), "", new Callable<Boolean>() {
                                    @Override
                                    public Boolean call() throws Exception {
                                        return null;
                                    }
                                });
                    }
                } catch (Exception exception) {
                    exception.getMessage();
                }


            }
        });


        /***find Recur View's****/
        findRecurView();


    }


    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        if (buttonView.getId() == R.id.add_recur_checkBox) {
            if (isChecked) {
                if (date_str.equals("")) {
                    add_recur_checkBox.setChecked(false);
                    showErrorDialog(LanguageController.getInstance().getMobileMsgByKey(AppConstant.sch_time_required));
                } else
                    jobWeeklyRecur();
            } else {
                normal_weekly_recur.setVisibility(View.GONE);
                recur_view.setVisibility(View.GONE);
                recur_pattern_view.setVisibility(View.GONE);
                setJobWeeklyRecurView();
            }
        } else if (buttonView.getId() == R.id.radio_on) {
            if (isChecked) {
                radio_on.setChecked(true);
                radio_never_end.setChecked(false);
                end_date_for_weekly_recur.setBackgroundResource(R.drawable.edittext_shap_qus);
                end_date_for_weekly_recur.setClickable(true);
                jobWeeklyRecur();
            } else {
                radio_on.setChecked(false);
            }
        } else if (buttonView.getId() == R.id.radio_never_end) {
            if (isChecked) {
                radio_never_end.setChecked(true);
                radio_on.setChecked(false);
                end_date_for_weekly_recur.setText("");
                end_date_for_weekly_recur.setBackgroundResource(R.drawable.layout_disable);
                end_date_for_weekly_recur.setText("");
                end_date_for_weekly_recur.setClickable(false);
                endDate = "";
                endRecurMode = "0";
            } else {
                radio_never_end.setChecked(false);
            }
        } else if (buttonView.getId() == R.id.switch_btn) {
            if (isChecked) {
                if (list != null && list.size() > 1) {
                    shift_time_txt.setVisibility(View.VISIBLE);
                } else {
                    shift_time_txt.setVisibility(View.GONE);
                    if (list.size() > 0) {
                        setDataInDateTimeField(list.get(0));
                    }
                }
            } else {
                shift_time_txt.setVisibility(View.GONE);

                addJob_pc.getCurrentdateTime();
            }
        } else {
            shift_time_txt.setVisibility(View.GONE);
        }
    }


    @Override
    public void setTimeShiftList(final List<ShiftTimeReSModel> list) {
        this.list = list;
        try {
            if (timeSHiftAdpter == null) {
                AppUtility.spinnerPopUpWindow(time_shift_dp);
                timeSHiftAdpter = new TimeSHiftAdpter(this, list);
                time_shift_dp.setAdapter(timeSHiftAdpter);
            } else {
                timeSHiftAdpter.updtaeList(list);
            }

            time_shift_dp.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    setDataInDateTimeField(list.get(position));
                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {

                }
            });


        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void setDataInDateTimeField(ShiftTimeReSModel shiftTimeReSModel) {
        try {
            date_str = AppUtility.getDateByFormat("dd-MM-yyyy");
            date_start.setText(date_str);

            String[] startTime = shiftTimeReSModel.getShiftStartTime().split(":");


            String temptime_str = AppUtility.updateTime(Integer.parseInt(startTime[0]), Integer.parseInt(startTime[1]));

            DecimalFormat formatterStr = new DecimalFormat("00");
            String[] aa = temptime_str.split(":");
            time_str = ((formatterStr.format(Integer.parseInt(aa[0]))) + ":" + aa[1]);

            time_start.setText(time_str);


            date_en = AppUtility.getDateByFormat("dd-MM-yyyy");
            String[] endTime = shiftTimeReSModel.getShiftEndTime().split(":");
            String temptime_en = AppUtility.updateTime(Integer.parseInt(endTime[0]), Integer.parseInt(endTime[1]));


            DecimalFormat formatterend = new DecimalFormat("00");
            String[] aaen = temptime_en.split(":");
            time_en = ((formatterend.format(Integer.parseInt(aaen[0]))) + ":" + aaen[1]);


            date_end.setText(date_en);
            time_end.setText(time_en);

            /**this for Add recur**/
            schdlStart = date_str + " " + time_str;
            schdlFinish = date_en + " " + time_en;
        } catch (NumberFormatException e) {
            e.printStackTrace();
        }
    }


    /****Hide Recur View When no need for Recur*******/
    private void setJobWeeklyRecurView() {
        mon_week_day.setSeleted(false);
        tu_week_day.setSeleted(false);
        wed_week_day.setSeleted(false);
        thu_week_day.setSeleted(false);
        fri_week_day.setSeleted(false);
        sat_week_day.setSeleted(false);
        sun_week_day.setSeleted(false);

        weekDays.clear();
        selectedDays.clear();
        selectedDaysMap.clear();

        recudata.clear();
        recurType = "";
        recurMsg = "";
        isRecur = "";

    }

    private void initializelables() {

        recyclerViewCustomField = findViewById(R.id.recyclerView_custom_field);
        recyclerViewCustomField.setLayoutManager(new LinearLayoutManager(this));

        mEditor = findViewById(R.id.editor);
        action_insert_image = findViewById(R.id.action_insert_image);
        action_insert_suggestion = findViewById(R.id.action_insert_suggestion);
        job_priority_spinner = findViewById(R.id.job_priority_spinner);
        job_suggestion_spinner = findViewById(R.id.job_suggestion_spinner);
        input_layout_client = findViewById(R.id.input_layout_client);
        input_layout_contact = findViewById(R.id.input_layout_contact);
        input_layout_site = findViewById(R.id.input_layout_site);
        job_country_layout = findViewById(R.id.job_country_layout);
        job_state_layout = findViewById(R.id.job_state_layout);
        j_prio_linearLayout = findViewById(R.id.j_prio_linearLayout);


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

        members = findViewById(R.id.members);
        members.setHint(LanguageController.getInstance().getMobileMsgByKey(AppConstant.add_fieldworker));

        linearMainView = findViewById(R.id.linearMainView);
        assignto_linear = findViewById(R.id.assignto_linear);
        tag_lable = findViewById(R.id.tag_lable);
        tag_lable.setHint(LanguageController.getInstance().getMobileMsgByKey(AppConstant.tag));


        et_tag = findViewById(R.id.et_tag);
        et_tag.setHint(LanguageController.getInstance().getMobileMsgByKey(AppConstant.add_tag));


        job_desc = findViewById(R.id.job_desc);
        job_desc.setHint(LanguageController.getInstance().getMobileMsgByKey(AppConstant.job_desc));

        job_instr = findViewById(R.id.job_instr);
        job_instr.setHint(LanguageController.getInstance().getMobileMsgByKey(AppConstant.job_inst));


        mob_no = findViewById(R.id.mob_no);
        mob_no.setHint(LanguageController.getInstance().getMobileMsgByKey(AppConstant.mob_no));

        at_mob = findViewById(R.id.at_mob);
        at_mob.setHint(LanguageController.getInstance().getMobileMsgByKey(AppConstant.alt_mobile_number));

        email = findViewById(R.id.email);
        email.setHint(LanguageController.getInstance().getMobileMsgByKey(AppConstant.client_email));

        adderes = findViewById(R.id.adderes);
        adderes.setHint(LanguageController.getInstance().getMobileMsgByKey(AppConstant.address) + " *");

        job_lat_layout = findViewById(R.id.job_lat_layout);
        edt_lat = findViewById(R.id.edt_lat);
        edt_lat.setHint(LanguageController.getInstance().getMobileMsgByKey(AppConstant.latitude));

        job_lng_layout = findViewById(R.id.job_lng_layout);
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
        submit_btn.setText(LanguageController.getInstance().getMobileMsgByKey(AppConstant.create_job));

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

        tv_spinner = findViewById(R.id.tv_spinner_account);

        tv_hint = findViewById(R.id.tv_hint_prioty);
        tv_hint.setHint(LanguageController.getInstance().getMobileMsgByKey(AppConstant.job_priority));

        date_time_clear_btn = findViewById(R.id.date_time_clear_btn);
        date_time_clear_btn.setText(LanguageController.getInstance().getMobileMsgByKey(AppConstant.clear));

        relative_main = findViewById(R.id.relative_main);

        job_desc_layout = findViewById(R.id.input_layout_des_job);
        job_instr_layout = findViewById(R.id.job_instr_layout);
        job_mob1_layout = findViewById(R.id.input_layout_mobile);
        job_mob2_layout = findViewById(R.id.job_mob2_layout);
        job_email_layout = findViewById(R.id.job_email_layout);
        job_adr_layout = findViewById(R.id.job_adr_layout);
        job_city_layout = findViewById(R.id.job_city_layout);
        job_postal_layout = findViewById(R.id.job_postal_layout);

        assign_to = findViewById(R.id.assign_to);
        assign_to.setHint(LanguageController.getInstance().getMobileMsgByKey(AppConstant.assign_to));

        schel_start = findViewById(R.id.schel_start);
        schel_start.setHint(LanguageController.getInstance().getMobileMsgByKey(AppConstant.shdl_start));

        schel_end = findViewById(R.id.schel_end);
        schel_end.setHint(LanguageController.getInstance().getMobileMsgByKey(AppConstant.shdl_end));


        landmark_layout = findViewById(R.id.landmark_layout);
        landmark_edt = findViewById(R.id.landmark_edt);
        landmark_edt.setHint(LanguageController.getInstance().getMobileMsgByKey(AppConstant.landmark_addjob));
        landmark_view = findViewById(R.id.landmark_view);


        jobservicelayout = findViewById(R.id.jobservicelayout);
        jobservicetxtlableset = findViewById(R.id.jobservicetxtlableset);
        jobservicetxtlableset.setHint(LanguageController.getInstance().getMobileMsgByKey(AppConstant.job_title) + " *");
        jobservicelablehint = findViewById(R.id.jobservicelablehint);
        jobservicelablehint.setHint(LanguageController.getInstance().getMobileMsgByKey(AppConstant.job_title) + " *");
        jobservicelayout.setOnClickListener(this);


        jobdeshint = findViewById(R.id.jobdeshint);
        jobdeshint.setHint(LanguageController.getInstance().getMobileMsgByKey(AppConstant.job_desc));

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
            params2.addRule(RelativeLayout.ALIGN_PARENT_LEFT);
            cb_future_client.setLayoutParams(params2);

            RelativeLayout.LayoutParams params3 = (RelativeLayout.LayoutParams) jobdeshint.getLayoutParams();
            params3.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
            jobdeshint.setLayoutParams(params3);

            RelativeLayout.LayoutParams params4 = (RelativeLayout.LayoutParams) action_insert_image.getLayoutParams();
            params4.addRule(RelativeLayout.ALIGN_PARENT_LEFT);
            action_insert_image.setLayoutParams(params4);

            RelativeLayout.LayoutParams params5 = (RelativeLayout.LayoutParams) action_insert_suggestion.getLayoutParams();
            params4.addRule(RelativeLayout.ALIGN_PARENT_LEFT);
            action_insert_suggestion.setLayoutParams(params5);
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

            RelativeLayout.LayoutParams params4 = (RelativeLayout.LayoutParams) action_insert_image.getLayoutParams();
            params4.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
            action_insert_image.setLayoutParams(params4);

            RelativeLayout.LayoutParams params5 = (RelativeLayout.LayoutParams) action_insert_suggestion.getLayoutParams();
            params4.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
            action_insert_suggestion.setLayoutParams(params5);

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

        lat_lng_view_lay = findViewById(R.id.lat_lng_view_lay);
        lat_lng_view_lay.setOnClickListener(this);
        lat_lng_txt_lable = findViewById(R.id.lat_lng_txt_lable);
        lat_lng_txt_lable.setText(LanguageController.getInstance().getMobileMsgByKey(AppConstant.get_current_lat_long));
        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);


        shift_time_txt = findViewById(R.id.shift_time_txt);
        switch_btn = findViewById(R.id.switch_btn);
        time_shift_dp = findViewById(R.id.time_shift_dp);
        shift_time_txt.setText(LanguageController.getInstance().getMobileMsgByKey(AppConstant.all_day_leave));

        shift_time_txt.setOnClickListener(this);
        switch_btn.setOnCheckedChangeListener(this);

    }

    private void findRecurView() {

        recur_parent_view = findViewById(R.id.recur_parent_view);
        recur_pattern_view = findViewById(R.id.recur_pattern_view);
        normal_weekly_recur = findViewById(R.id.normal_weekly_recur);
        add_recur_checkBox = findViewById(R.id.add_recur_checkBox);

        end_date_for_weekly_recur = findViewById(R.id.end_date_for_weekly_recur);

        custom_recur_pattern = findViewById(R.id.custom_recur_pattern);
        recur_not_work = findViewById(R.id.recur_not_work);

        mon_week_day = findViewById(R.id.mon_week_day);
        tu_week_day = findViewById(R.id.tu_week_day);
        wed_week_day = findViewById(R.id.wed_week_day);
        thu_week_day = findViewById(R.id.thu_week_day);
        fri_week_day = findViewById(R.id.fri_week_day);
        sat_week_day = findViewById(R.id.sat_week_day);
        sun_week_day = findViewById(R.id.sun_week_day);

        radio_on = findViewById(R.id.radio_on);
        radio_never_end = findViewById(R.id.radio_never_end);

        recur_view = findViewById(R.id.recur_view);

        recur_job_days_msg = findViewById(R.id.recur_job_days_msg);
        cutom_txt = findViewById(R.id.cutom_txt);

        msg_pattern_view = findViewById(R.id.msg_pattern_view);
        msg_pattern_view.setOnClickListener(this);


        add_recur_checkBox.setOnCheckedChangeListener(this);
        radio_on.setOnCheckedChangeListener(this);
        radio_never_end.setOnCheckedChangeListener(this);
        end_date_for_weekly_recur.setOnClickListener(this);

        custom_recur_pattern.setOnClickListener(this);

        mon_week_day.setOnClickListener(this);
        tu_week_day.setOnClickListener(this);
        wed_week_day.setOnClickListener(this);
        thu_week_day.setOnClickListener(this);
        fri_week_day.setOnClickListener(this);
        sat_week_day.setOnClickListener(this);
        sun_week_day.setOnClickListener(this);

        jobWeeklyRecurPi = new JOB_weekly_recur_PC(this);

        setTextView();


    }

    private void setTextView() {
        add_recur_checkBox.setText(LanguageController.getInstance().getMobileMsgByKey(AppConstant.add_rucr_for_job));
        recur_not_work.setText(LanguageController.getInstance().getMobileMsgByKey(AppConstant.dont_create_recur) + " ");
        custom_recur_pattern.setText(LanguageController.getInstance().getMobileMsgByKey(AppConstant.custom_recur_pattern));

        radio_on.setText(LanguageController.getInstance().getMobileMsgByKey(AppConstant.radio_end_by));
        radio_never_end.setText(LanguageController.getInstance().getMobileMsgByKey(AppConstant.never_end));


        /****permission for Add recur***/
        if (App_preference.getSharedprefInstance().getLoginRes().getRights().get(0).getIsRecur().equals("0")) {
            recur_parent_view.setVisibility(View.VISIBLE);
        } else {
            recur_parent_view.setVisibility(View.GONE);
        }

    }


    /***Enable All recur view for End Date*****/
    private void jobWeeklyRecur() {
        recur_pattern_view.setVisibility(View.VISIBLE);
        normal_weekly_recur.setVisibility(View.VISIBLE);
        recur_view.setVisibility(View.VISIBLE);
        if (!date_start.getText().toString().equals(""))
            jobWeeklyRecurPi.getTillDateForRecur(date_start.getText().toString());

        mon_week_day.setSeleted(false);
        tu_week_day.setSeleted(false);
        wed_week_day.setSeleted(false);
        thu_week_day.setSeleted(false);
        fri_week_day.setSeleted(false);
        sat_week_day.setSeleted(false);
        sun_week_day.setSeleted(false);


        endRecurMode = "2";

    }


    @Override
    public void setTillDateForRecur(String tillDate) {
        radio_on.setChecked(true);
        end_date_for_weekly_recur.setText(tillDate);
    }


    //set  Job Priority by default medium
    private void setTaskPriority() {
        tv_hint.setVisibility(View.VISIBLE);
        tv_spinner.setText(prty);
        prty = String.valueOf(2);
    }


    private void textInputLayoutHint() {
        //  AppUtility.setupUI(relative_main, Add_job_activity.this);

        job_desc_layout.getEditText().addTextChangedListener(this);
        job_instr_layout.getEditText().addTextChangedListener(this);
        job_mob1_layout.getEditText().addTextChangedListener(this);
        job_mob2_layout.getEditText().addTextChangedListener(this);
        job_adr_layout.getEditText().addTextChangedListener(this);
        job_lat_layout.getEditText().addTextChangedListener(this);
        job_lng_layout.getEditText().addTextChangedListener(this);
        job_email_layout.getEditText().addTextChangedListener(this);
        job_city_layout.getEditText().addTextChangedListener(this);
        job_postal_layout.getEditText().addTextChangedListener(this);
        job_state_layout.getEditText().addTextChangedListener(this);
        landmark_layout.getEditText().addTextChangedListener(this);
        contact_add_layout.getEditText().addTextChangedListener(this);
        site_add_layout.getEditText().addTextChangedListener(this);


        if (!TextUtils.isEmpty(appId)) {
            APPOINMENTJOB = false;

            appointment = AppDataBase.getInMemoryDatabase(EotApp.getAppinstance()).appointmentModel().getAppointmentById(appId);
            auto_client.setText(appointment.getNm());
            cltId = appointment.getCltId();
            cb_future_client.setVisibility(View.GONE);
            input_layout_client.setHintEnabled(true);
            auto_client.setEnabled(false);//not editable client name

            input_layout_site.setHintEnabled(true);
            input_layout_contact.setHintEnabled(true);


            if (cltId.equals("0")) {
                auto_sites.setFocusableInTouchMode(false);
                auto_sites.setFocusable(false);


                auto_contact.setFocusableInTouchMode(false);
                auto_contact.setFocusable(false);

                new_cnm = appointment.getNm();
                new_site_nm = "Self";
                new_con_nm = "self";
                auto_sites.setText(new_site_nm);
                auto_contact.setText(new_con_nm);

                site_dp_img.setClickable(false);
                contact_dp_img.setClickable(false);


                cb_future_sites.setVisibility(View.GONE);
                cb_future_contact.setVisibility(View.GONE);
            } else {
                cb_future_client.setVisibility(View.GONE);
                cb_future_client.setChecked(false);

                site_new_add_layout.setVisibility(View.GONE);
                contact_new_add_layout.setVisibility(View.GONE);

                contact_dp_layout.setVisibility(View.VISIBLE);
                site_dp_layout.setVisibility(View.VISIBLE);

                new_cnm = "";
                cltId = appointment.getCltId();
                conId = appointment.getConId();
                siteId = appointment.getSiteId();

                cb_future_contact.setVisibility(View.VISIBLE);
                cb_future_sites.setVisibility(View.VISIBLE);
                auto_contact.setFocusable(true);
                auto_contact.setFocusableInTouchMode(true);
                contact_dp_img.setClickable(true);

                auto_sites.setText(appointment.getSnm());
                auto_contact.setText(appointment.getCnm());

                auto_sites.setFocusable(true);
                auto_sites.setFocusableInTouchMode(true);
                site_dp_img.setClickable(true);

                if (!TextUtils.isEmpty(appointment.getCltId())) {
                    addJob_pc.getCOntactList(appointment.getCltId());
                    addJob_pc.getContractList(appointment.getCltId());
                    addJob_pc.getSiteList(appointment.getCltId());
                }
            }


            conId = appointment.getConId();
            if (!TextUtils.isEmpty(conId))
                selectedContactData = AppDataBase.getInMemoryDatabase(EotApp.getAppinstance()).contactModel().getContactById(conId);

            siteId = appointment.getSiteId();
            if (!TextUtils.isEmpty(siteId))
                selectedSiteData = AppDataBase.getInMemoryDatabase(EotApp.getAppinstance()).sitemodel().getSiteFromSiteId(siteId);


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
            addJob_pc.getStateList(ctry_id);
            job_country_layout.setHintEnabled(true);
            job_state_layout.setHintEnabled(true);

            job_mob1_layout.setHintEnabled(true);
            job_mob2_layout.setHintEnabled(true);
            job_adr_layout.setHintEnabled(true);
            job_email_layout.setHintEnabled(true);

            if (!TextUtils.isEmpty(appointment.getLat())) {
                edt_lat.setText(appointment.getLat());
                job_lat_layout.setHintEnabled(true);
            }
            if (!TextUtils.isEmpty(appointment.getLng())) {
                edt_lng.setText(appointment.getLng());
                job_lng_layout.setHintEnabled(true);
            }

            if (!TextUtils.isEmpty(appointment.getCity())) {
                city.setText(appointment.getCity());
                job_city_layout.setHintEnabled(true);
            }

            if (!TextUtils.isEmpty(appointment.getZip())) {
                post_code.setText(appointment.getZip());
                job_postal_layout.setHintEnabled(true);
            }


            /***set this for exiting client*****/
            clientForFuture = contactForFuture = siteForFuture = 0;
            new_cnm = new_con_nm = new_site_nm = "";

        } else {
            setCompanySettingAdrs();
        }


    }

    private void initializeView() {

        lat_view = findViewById(R.id.lat_view);
        lng_view = findViewById(R.id.lng_view);
        adr_view = findViewById(R.id.adr_view);

        j_prio_linearLayout.setOnClickListener(this);

        date_time_clear_btn.setOnClickListener(this);

        add_tag_btn.setOnClickListener(this);
        submit_btn.setOnClickListener(this);
        cb_future_client.setOnClickListener(this);
        cb_future_contact.setOnClickListener(this);
        cb_future_sites.setOnClickListener(this);
        assignto_linear.setOnClickListener(this);
        tag_lable.setOnClickListener(this);

        addJob_pc = new AddJob_pc(this);
        addJob_pc.getJobTitleList();
        addJob_pc.getClientList();
        addJob_pc.getCountryList();
        addJob_pc.getWorkerList();
        addJob_pc.getTagDataList();
        addJob_pc.getCurrentdateTime();
        addJob_pc.getTimeShiftList();


        /***Hide lat & lng filed when permission deny****/
        if (App_preference.getSharedprefInstance().getCompanySettingsDetails().getIsJobLatLngEnable().equals("0")) {
            lng_view.setVisibility(View.GONE);
            lat_view.setVisibility(View.GONE);
            job_lat_layout.setVisibility(View.GONE);
            job_lng_layout.setVisibility(View.GONE);
            lat_lng_view_lay.setVisibility(View.GONE);

        }


        /***hide/show landmark field***/
        if (App_preference.getSharedprefInstance().getCompanySettingsDetails().getIsLandmarkEnable().equals("0")) {
            landmark_layout.setVisibility(View.GONE);
            landmark_view.setVisibility(View.GONE);
        }

//      for add user as default fieldworker in member list initialize.
        FieldWorker yourId = addJob_pc.getDefaultFieldWorker();
        if (yourId != null) {
            linearMainView.setVisibility(View.VISIBLE);
            addChips(yourId);
        }

        SettingUrls settingurl = new SettingUrls(Integer.valueOf(App_preference.getSharedprefInstance().getLoginRes().getCompId()), new FirstSyncPC.CallBackFirstSync() {
            @Override
            public void getCallBackOfComplete(int success_no, String msg) {
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

        if (App_preference.getSharedprefInstance().getLoginRes().getIsCustomFieldEnable().equals("1")) {
            setAdapterOfCustomField();
        }

    }

    /**
     * set adapter on
     */
    private void setAdapterOfCustomField() {
        ArrayList<CustOmFormQuestionsRes> custOmFormQuestionsList = App_preference.getSharedprefInstance().getJobCustomFields();
        if (custOmFormQuestionsList != null) {
            customFiledQueAdpter = new CustomFieldJobAdapter(custOmFormQuestionsList, this);
            recyclerViewCustomField.setAdapter(customFiledQueAdpter);
        }

    }

    @Override
    public void jobPrioritySet() {
        AppUtility.spinnerPopUpWindow(job_priority_spinner);
        job_prioty = AppConstant.Prioty;
        job_priority_spinner.setAdapter(new MySpinnerAdapter(Add_job_activity.this, job_prioty));
        job_priority_spinner.setOnItemSelectedListener(this);
    }

    public void setdataInSuggestion() {
        suggestionsArray = new String[suggestions.size()];
        for (int i = 0; i < suggestions.size(); i++) {
            try {
                suggestionsArray[i] = suggestions.get(i).getJtDesSugg();//String.valueOf(suggestions.get(i).getJtDesSugg());
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        try {
            if (suggestionAdapter == null) {
                AppUtility.spinnerPopUpWindow(job_priority_spinner);
                suggestionAdapter = new JoBServSuggAdpter(Add_job_activity.this, suggestionsArray
                        , new JoBServSuggAdpter.SelectedService() {
                    @Override
                    public void getSerNm(String nm) {
                        setSelectedSuggeston(nm);
                    }
                });
                job_suggestion_spinner.setAdapter(suggestionAdapter);
            } else {
                suggestionAdapter.updtaeList(suggestionsArray);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    private void setSelectedSuggeston(String nm) {
        try {
            // action_insert_suggestion.setText(nm);
            String str = "";
            if (mEditor.getHtml() != null) {
                str = mEditor.getHtml() + "\n" + nm;
                mEditor.setHtml(str);
            } else {
                mEditor.setHtml(nm);
            }

            jobdeshint.setVisibility(View.VISIBLE);
        } catch (Exception e) {
            e.getMessage();
        }

    }

    @Override
    public void SetJobTittle(final ArrayList<JobTitle> datastr) {
        this.datastr = datastr;
        id_array = new String[datastr.size()];
    }

    @Override
    public void set_Str_DTime(String str_dt_tm, String time_str) {
        date_str = str_dt_tm;
        this.time_str = time_str;
        date_start.setText(date_str);
        time_start.setText(this.time_str);

        /**this for  add recur**/
        schdlStart = date_str + " " + time_str;
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

        /**this for Add recur**/
        schdlStart = date_str + " " + time_str;

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
                    addChipsForTags((TagData) adapterView.getItemAtPosition(i));
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
            else time_en = time_duration[1] + " ";
        } catch (Exception e) {

        }
        date_end.setText(date_en);
        time_end.setText(time_en);


    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            onBackPressed();
            return true;
        }
        return super.onOptionsItemSelected(item);
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
        } else if (contractlist.size() == 0) {
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
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        Log.e("permission", "permission");
        switch (requestCode) {
            case LOCATION_REQUEST: {
                //location for FW
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    getLocation();
                } else {
                    HyperLog.i("TAG", "Location Permission denied from user");
                    showErrorDialog(LanguageController.getInstance().getMobileMsgByKey(AppConstant.loc_permission_deny));
                }
                break;
            }
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
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
                Log.e("", "");
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


                addJob_pc.getContractList(((Client) adapterView.getItemAtPosition(i)).getCltId());
                addJob_pc.getCOntactList(((Client) adapterView.getItemAtPosition(i)).getCltId());
                addJob_pc.getSiteList(((Client) adapterView.getItemAtPosition(i)).getCltId());


            }
        });
    }


    @Override
    public void setContactList(final List<ContactData> data) {
        if (data.size() > 0) {
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
                    selectedContactData = contactData;
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


                }

                @Override
                public void afterTextChanged(Editable editable) {
                    Log.e("", "");
                }
            });
        }
        if (data.size() == 0) {
            cb_future_contact.setVisibility(View.GONE);
            auto_contact.setFocusableInTouchMode(false);
            auto_contact.setFocusable(false);
        }
    }

    /*********/
    private void setCompanySettingAdrs() {
        auto_country.setText(SpinnerCountrySite.getCountryNameById(App_preference.getSharedprefInstance().getCompanySettingsDetails().getCtry()));
        addJob_pc.getStateList((App_preference.getSharedprefInstance().getCompanySettingsDetails().getCtry()));
        job_state_layout.setHintEnabled(true);
        auto_states.setText(SpinnerCountrySite.getStatenameById(App_preference.getSharedprefInstance().getCompanySettingsDetails().getCtry(), App_preference.getSharedprefInstance().getCompanySettingsDetails().getState()));
        //   city.setText(App_preference.getSharedprefInstance().getCompanySettingsDetails().getCity());
        state_id = (App_preference.getSharedprefInstance().getCompanySettingsDetails().getState());
        ctry_id = (App_preference.getSharedprefInstance().getCompanySettingsDetails().getCtry());


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

            // auto_sites.setText(new_site_nm);

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
            //   auto_contact.setText(new_con_nm);


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

            site_add_edt.setText("");
            contact_add_edt.setText("");
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
        if (data.size() > 0) {
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
                    //new_site_nm = charSequence.toString();
                    if (charSequence.length() >= 1) {
                        input_layout_site.setHintEnabled(true);
                    } else if (charSequence.length() <= 0) {
                        input_layout_site.setHintEnabled(false);
                    }


                }

                @Override
                public void afterTextChanged(Editable editable) {
                }
            });
        } else if (data.size() == 0) {
            cb_future_sites.setVisibility(View.GONE);
            auto_sites.setFocusableInTouchMode(false);
            auto_sites.setFocusable(false);
        }
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
            job_state_layout.setHintEnabled(true);
            post_code.setText(sitetData.getZip());
            city.setText((sitetData.getCity()));
        }
    }

    private void newSiteForNewClient() {
        auto_country.setText("");
        auto_states.setText("");
        post_code.setText("");
        city.setText("");
        adderes.setText("");
        edt_lng.setText("");
        edt_lat.setText("");
        siteId = "";
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
                addJob_pc.getStateList(ctry_id);
                job_country_layout.setHintEnabled(true);
            }
        });

        auto_country.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (charSequence.length() >= 1) {
                    job_country_layout.setHintEnabled(true);
                } else if (charSequence.length() <= 0) {
                    job_country_layout.setHintEnabled(false);
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
                job_state_layout.setHintEnabled(true);
            }
        });


        auto_states.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (charSequence.length() >= 1) {
                    job_state_layout.setHintEnabled(true);
                } else if (charSequence.length() <= 0) {
                    job_state_layout.setHintEnabled(false);
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
    }

    @Override
    public void setWorkerList(final List<FieldWorker> fieldWorkerlist) {
        AppUtility.autocompletetextviewPopUpWindow(members);
        members.setThreshold(1);
        DynamicClassAdapter myClassAdapter = new DynamicClassAdapter<FieldWorker>(this, R.layout.custom_adapter_item_layout, R.id.item_title_name, fieldWorkerlist);
        members.setAdapter(myClassAdapter);
        members.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                if (!listwork.contains((((FieldWorker) adapterView.getItemAtPosition(i)).getUsrId()))) {
//                    listwork.add((((FieldWorker) adapterView.getItemAtPosition(i)).getUsrId()));
                    addChips((FieldWorker) adapterView.getItemAtPosition(i));
                }
                linearMainView.setVisibility(View.VISIBLE);
                members.setText("");
            }
        });
    }

    private void addChips(final FieldWorker itemAtPosition) {//add chip for fieldworker to assign job
//        add id in list
        listwork.add(itemAtPosition.getUsrId());
        try {
            LayoutInflater vi = (LayoutInflater) getApplicationContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View v = vi.inflate(R.layout.member_item_view, null);
//            memIds.add(itemAtPosition.getUsrId());
// fill in any details dynamically here

            TextView textView = v.findViewById(R.id.memberName);
            textView.setText(itemAtPosition.getFnm());
            ImageView deleteMember = v.findViewById(R.id.deleteMember);
            deleteMember.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    linearMainView.removeView((LinearLayout) view.getParent());
                    if (linearMainView.getChildCount() <= 0) {
                        linearMainView.setVisibility(View.GONE);
                    }
                    listwork.remove(itemAtPosition.getUsrId());
                }
            });

// insert into main view

            linearMainView.addView(v, 0, new ViewGroup.LayoutParams(ViewGroup.LayoutParams.FILL_PARENT, ViewGroup.LayoutParams.FILL_PARENT));

        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int pos, long l) {
        switch (parent.getId()) {
            case R.id.job_priority_spinner:
                tv_hint.setVisibility(View.VISIBLE);
                tv_spinner.setText(job_prioty[pos]);
                s = pos;
                prty = String.valueOf(parent.getSelectedItemPosition() + 1);
                break;

        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {
    }


    @Override
    public void showErrorMsgsForValidation(String msg) {
        showErrorDialog(msg);
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
        schdlStart = date_str = time_str = date_en = time_en = "";

        if (App_preference.getSharedprefInstance().getLoginRes().getRights().get(0).getIsRecur().equals("0")) {
            recur_pattern_view.setVisibility(View.GONE);
            setJobWeeklyRecurView();
        }
        if (add_recur_checkBox.isChecked())
            add_recur_checkBox.performClick();
    }

    public void setSelectedJobServices(ArrayList<JobTitle> selectedJobServices) {
        jtIdList.clear();
        if (suggestions != null && suggestions.size() > 0) {
            suggestions.clear();
        }
        Set<String> jobServiceNm = new HashSet<>();
        for (JobTitle jobTitle : selectedJobServices) {
            if (jobTitle.isSelect()) {
                jtIdList.add(jobTitle.getJtId());
                jobServiceNm.add(jobTitle.getName());
                try {
                    if (jobTitle.getSuggestionList() != null)
                        suggestions.addAll(jobTitle.getSuggestionList());
                } catch (Exception exception) {
                    exception.printStackTrace();
                }
            }
        }

        if (jobServiceNm.size() >= 4)
            jobservicetxtlableset.setText(LanguageController.getInstance().getMobileMsgByKey(AppConstant.items_selected) + " " + jobServiceNm.size());
        else
            jobservicetxtlableset.setText(jobServiceNm.toString().replace("[", "").replace("]", ""));
        if (jobServiceNm.size() > 0) {
            jobservicelablehint.setVisibility(View.VISIBLE);
        } else jobservicelablehint.setVisibility(View.INVISIBLE);

        setdataInSuggestion();

    }


    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    private void getLocation() {
        try {
            if (ActivityCompat.checkSelfPermission(
                    Add_job_activity.this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                    && ActivityCompat.checkSelfPermission(
                    Add_job_activity.this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                        LOCATION_REQUEST);
            } else {
                Location locationGPS = null;
                try {
                    if (Build.VERSION.SDK_INT >= 30) {
                        locationGPS = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
                        HyperLog.i("Add_job_activity", "getLocation-" + "above version 11");
                        //provider = LocationManager.NETWORK_PROVIDER;
                    } else {
                        locationGPS = locationManager.getLastKnownLocation(GPS_PROVIDER);
                        HyperLog.i("Add_job_activity", "getLocation-" + "below version 10");
                        //  provider = GPS_PROVIDER;
                    }
                } catch (Exception exception) {
                    getLatLngCntr();
                    exception.getMessage();
                    HyperLog.i("Add_job_activity", "getLocation-" + exception.getMessage());

                }


                if (locationGPS != null) {
                    double lat = locationGPS.getLatitude();
                    double longi = locationGPS.getLongitude();
                    try {
                        DecimalFormat dFormat = new DecimalFormat("#.######");
                        lat = Double.valueOf(dFormat.format(lat));
                        longi = Double.valueOf(dFormat.format(longi));
                    } catch (Exception e) {
                        e.printStackTrace();
                        lat = locationGPS.getLatitude();
                        longi = locationGPS.getLongitude();
                    }

                    String latitude = String.valueOf(lat);
                    String longitude = String.valueOf(longi);
                    edt_lat.setText(latitude);
                    edt_lng.setText(longitude);
                } else {
                    //Toast.makeText(this, "Unable to find location.", Toast.LENGTH_SHORT).show();
                    HyperLog.i("Add_job_activity", "getLocation-" + "Unable to find location.");
                    getLatLngCntr();
                }
            }

        } catch (Exception exception) {
            HyperLog.i("Add_job_activity", exception.getMessage());
            getLatLngCntr();
            exception.getMessage();
        }
    }


    private void getLatLngCntr() {
        CurrLatLngCntrlr.getInstance().getCurrLatLng(new CurrLatLngCntrlr.GetLatLng() {
            @Override
            public void getLatLngs(String lat, String lng) {
                edt_lat.setText(lat);
                edt_lng.setText(lng);
            }
        });
    }


    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.shift_time_txt:
                time_shift_dp.performClick();
                break;
            case R.id.lat_lng_view_lay:
                getLocation();
                break;
            case R.id.mon_week_day:
                mon_week_day.setSeleted(!mon_week_day.isSeleted());
                if (mon_week_day.isSeleted()) {
                    weekDays.add("1");
                    selectedDaysMap.put("monday", true);
                } else {
                    selectedDaysMap.remove("monday");
                    weekDays.remove("1");
                }

                break;
            case R.id.tu_week_day:
                tu_week_day.setSeleted(!tu_week_day.isSeleted());
                if (tu_week_day.isSeleted()) {
                    selectedDaysMap.put("tuesday", true);
                    weekDays.add("2");
                } else {
                    selectedDaysMap.remove("tuesday");
                    weekDays.remove("2");
                }
                break;
            case R.id.wed_week_day:
                wed_week_day.setSeleted(!wed_week_day.isSeleted());
                if (wed_week_day.isSeleted()) {
                    weekDays.add("3");
                    selectedDaysMap.put("wednesday", true);
                } else {
                    selectedDaysMap.remove("wednesday");
                    weekDays.remove("3");
                }
                break;
            case R.id.thu_week_day:
                thu_week_day.setSeleted(!thu_week_day.isSeleted());
                if (thu_week_day.isSeleted()) {
                    weekDays.add("4");
                    selectedDaysMap.put("thursday", true);
                } else {
                    selectedDaysMap.remove("thursday");
                    weekDays.remove("4");
                }
                break;
            case R.id.fri_week_day:
                fri_week_day.setSeleted(!fri_week_day.isSeleted());
                if (fri_week_day.isSeleted()) {
                    weekDays.add("5");
                    selectedDaysMap.put("friday", true);
                } else {
                    selectedDaysMap.remove("friday");
                    weekDays.remove("5");
                }
                break;
            case R.id.sat_week_day:
                sat_week_day.setSeleted(!sat_week_day.isSeleted());
                if (sat_week_day.isSeleted()) {
                    weekDays.add("6");
                    selectedDaysMap.put("saturday", true);
                } else {
                    weekDays.remove("6");
                    selectedDaysMap.remove("saturday");
                }
                break;
            case R.id.sun_week_day:
                sun_week_day.setSeleted(!sun_week_day.isSeleted());
                if (sun_week_day.isSeleted()) {
                    weekDays.add("7");
                    selectedDaysMap.put("sunday", true);
                } else {
                    weekDays.remove("7");
                    selectedDaysMap.remove("sunday");
                }
                break;
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
            case R.id.jobservicelayout:
                if (id_array.length > 0) {
                    customDPController.showSpinnerDropDown(this, jobservicelayout, datastr);
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

            case R.id.j_prio_linearLayout:
                job_priority_spinner.performClick();
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
                if (selectedContactData != null) {
                    setContactDefaultData(selectedContactData);
                } else if (appointment != null && !appointment.getConId().equals("")) {
                    setSitetDefaultData(AppDataBase.getInMemoryDatabase(EotApp.getAppinstance()).sitemodel().getSiteByClientId(appointment.getConId()));
                }

                break;
            case R.id.imvCross_site:
                cb_future_sites.setVisibility(View.VISIBLE);
                site_new_add_layout.setVisibility(View.GONE);
                site_dp_layout.setVisibility(View.VISIBLE);
                site_add_edt.setText("");
                if (selectedSiteData != null) {
                    setSitetDefaultData(selectedSiteData);
                } else if (appointment != null && !appointment.getSiteId().equals("")) {
                    setSitetDefaultData(AppDataBase.getInMemoryDatabase(EotApp.getAppinstance()).sitemodel().getSiteByClientId(appointment.getSiteId()));
                }
                break;
            case R.id.cb_future_sites:
                new_site_nm = "";
                site_new_add_layout.setVisibility(View.VISIBLE);
                site_dp_layout.setVisibility(View.GONE);
                cb_future_sites.setVisibility(View.GONE);
                imvCross_site.setVisibility(View.VISIBLE);
                newSiteForNewClient();
                break;
            case R.id.end_date_for_weekly_recur:
                selectRecurtillDate();
                break;
            case R.id.custom_recur_pattern:
                if (LanguageController.getInstance().getMobileMsgByKey(AppConstant.normal_recur).equals(custom_recur_pattern.getText().toString())) {
                    msg_pattern_view.setVisibility(View.GONE);
                    custom_recur_pattern.setText(LanguageController.getInstance().getMobileMsgByKey(AppConstant.custom_recur_pattern));
                    normal_weekly_recur.setVisibility(View.VISIBLE);
                    jobWeeklyRecur();
                } else {
                    jobCustomRecurPattenrn();
                }
                break;

            case R.id.msg_pattern_view:
                jobCustomRecurPattenrn();
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

                if (listwork.isEmpty()) {
                    member_type = 1;
                    kpr = "";
                } else if (listwork.size() == 1) {
                    member_type = 1;
                    Iterator itr = listwork.iterator();
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
                    ctry_id = addJob_pc.cntryId(countryname);
                    state_id = addJob_pc.statId(ctry_id, statename);
                }


                if (TextUtils.isEmpty(appId) && !cltId.equals("0") && !cltId.equals("")) {
                    String contact_name = auto_contact.getText().toString().trim();
                    if (contact_name.length() > 0) {
                        List<String> contact = new ArrayList<>();
                        if (contact_data != null) {
                            for (ContactData contactData : contact_data) {
                                contact.add(contactData.getCnm());
                            }
                        }
                        if (!contact.contains(contact_name)) {
                            AppUtility.error_Alert_Dialog(this, LanguageController.getInstance().getMobileMsgByKey(AppConstant.valid_contact_check), LanguageController.getInstance().getMobileMsgByKey(AppConstant.ok), null);
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
                            AppUtility.error_Alert_Dialog(this, LanguageController.getInstance().getMobileMsgByKey(AppConstant.valid_site_check), LanguageController.getInstance().getMobileMsgByKey(AppConstant.ok), null);
                            return;
                        }
                    }
                }
                /* for appointmnet*/
                if (!TextUtils.isEmpty(appId)) {
                    if (cltId.equals("0")) {
                        cltId = "";
                        conId = "";
                        siteId = "";
                        new_site_nm = auto_sites.getText().toString();
                        new_con_nm = auto_contact.getText().toString();
                        new_cnm = auto_client.getText().toString();
                        clientForFuture = contactForFuture = siteForFuture = 0;
                    }
                }


                if (schdlStart.isEmpty()) {
                    callAddJob(adr);
                } else {
                    if (!conditionCheck(schdlStart, schdlFinish)) {
                        EotApp.getAppinstance().
                                showToastmsg(LanguageController.getInstance().getMobileMsgByKey(AppConstant.Sche_end_start_time));
                    } else {
                        callAddJob(adr);
                    }
                }
                break;
            case R.id.assignto_linear:
                members.showDropDown();
                break;
            case R.id.tag_lable:
                et_tag.showDropDown();
                break;
        }
    }


    private void jobCustomRecurPattenrn() {
        if (AppUtility.isInternetConnected()) {
            showCustomRecurDialog();
        } else {
            showErrorDialog(LanguageController.getInstance().getMobileMsgByKey(AppConstant.offline_feature_alert));
        }
    }

    private void showCustomRecurDialog() {
        AppUtility.alertDialog2(this,
                LanguageController.getInstance().getMobileMsgByKey(AppConstant.custom_recur),
                LanguageController.getInstance().getMobileMsgByKey(AppConstant.add_custom_recur),
                LanguageController.getInstance().getMobileMsgByKey(AppConstant.yes),
                LanguageController.getInstance().getMobileMsgByKey(AppConstant.no)
                , new Callback_AlertDialog() {
                    @Override
                    public void onPossitiveCall() {
                        RECURTYPENORMAL_CUSTOM = 2;
                        Intent addJobrecrIntent = new Intent(Add_job_activity.this, AddJobRecrHomeActivity.class);
                        addJobrecrIntent.putExtra("AddJobScdlStartTime", date_str);
                        startActivityForResult(addJobrecrIntent, ADDCUSTOMRECUR);
                    }

                    @Override
                    public void onNegativeCall() {

                    }
                });
    }


    /***Recur Date Picker*******/
    private void selectRecurtillDate() {
        final Calendar c = Calendar.getInstance();
        showDialog(R.id.end_date_for_weekly_recur);
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


    private void callAddJob(String adr) {
        if (add_recur_checkBox.isChecked() && RECURTYPENORMAL_CUSTOM == 1) {
            Date schduleStartDate = null;

            if (!date_start.getText().toString().equals("")) {
                Date start_Date = null;
                try {
                    start_Date = new SimpleDateFormat("dd-MM-yyyy", Locale.getDefault()).parse(date_start.getText().toString());

                } catch (ParseException e) {
                    e.printStackTrace();
                }
                startDate = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(start_Date);


                try {
                    schduleStartDate = new SimpleDateFormat("dd-MM-yyyy", Locale.getDefault()).parse(date_start.getText().toString());
                } catch (ParseException e) {
                    e.printStackTrace();
                }

            }


            if (radio_on.isChecked() && !end_date_for_weekly_recur.equals("")) {
                Date end_Date = null;
                try {
                    end_Date = new SimpleDateFormat("dd-MMM-yyyy", Locale.getDefault()).parse(end_date_for_weekly_recur.getText().toString());
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                endDate = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(end_Date);
            } else {
                endDate = "";
            }


            JobRecurModel model = (new JobRecurModel(startDate, endDate,
                    endRecurMode, weekDays));
            String str = (mon_week_day.isSeleted() ? "Monday," : "") + (tu_week_day.isSeleted() ? "Tuesday," : "") +
                    (wed_week_day.isSeleted() ? "Wednesday," : "") + (thu_week_day.isSeleted() ? "Thursday," : "") +
                    (fri_week_day.isSeleted() ? "Friday," : "") + (sat_week_day.isSeleted() ? "Saturday," : "") +
                    (sun_week_day.isSeleted() ? "Sunday" : "");
            model.setWeek_num(str);
            recudata.clear();
            selectedDays.clear();
            recudata.add(model);
            selectedDays.add(selectedDaysMap);
            recurType = "2";
            isRecur = "1";


        }


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
                    // time_str = "";
                    time_str = new SimpleDateFormat("hh:mm a", Locale.getDefault()).format(startDate);
                } else {
                    // time_str = time_str + " " + "PM";
                    time_str = "";
                    time_str = time_start.getText().toString() + " " + "PM";
                }
                if (t2 != 12) {
                    endDate = new SimpleDateFormat("hh:mm", Locale.getDefault()).parse(time_en);
                    time_en = "";
                    time_en = new SimpleDateFormat("hh:mm a", Locale.getDefault()).format(endDate);
                } else {
                    //time_en = time_en + " " + "PM";
                    time_en = "";
                    time_en = time_end.getText().toString() + " " + "PM";
                }
            } catch (ParseException e) {
                e.printStackTrace();
            }
            schdlStart = date_str + " " + time_str;
            schdlFinish = date_en + " " + time_en;
        }

        /****This for Add normal JOB without Recuring****/
        if (addJob_pc.RequiredFields(auto_client.getText().toString(), newContact, newSite, new_con_nm, new_site_nm, adr, this.jtIdList, ctry_id,
                state_id, mob_no.getText().toString(), at_mob.getText().toString(), email.getText().toString(),
                (add_recur_checkBox.isChecked() && RECURTYPENORMAL_CUSTOM == 1), weekDays.size())) {
            final AddJob_Req addJob_req = new AddJob_Req(tempId + 1,
                    0,
                    contrId,
                    (App_preference.getSharedprefInstance().getLoginRes().getCompId()),
                    "",
                    this.jtIdList,
                    job_desc.getText().toString(),
                    prty,
                    job_instr.getText().toString(),
                    cltId,
                    new_cnm,
                    clientForFuture,
                    siteId,
                    new_site_nm,
                    siteForFuture,
                    conId,
                    new_con_nm,
                    contactForFuture,
                    mob_no.getText().toString(),
                    at_mob.getText().toString(),
                    email.getText().toString(),
                    adr,
                    city.getText().toString(),
                    ctry_id,
                    state_id,
                    post_code.getText().toString(),
                    member_type,
                    listwork,
                    schdlStart,
                    schdlFinish,
                    tagArray,
                    App_preference.getSharedprefInstance().getLoginRes().getUsrId(),
                    kpr,
                    AppUtility.getDateByFormat(AppConstant.DATE_TIME_FORMAT),
                    edt_lat.getText().toString(),
                    edt_lng.getText().toString(),
                    landmark_edt.getText().toString(),
                    appId, recudata, recurType, selectedDays, isRecur);

            /**
             * get the anser submitted by the user
             *
             * */
            if (customFiledQueAdpter != null) {
                ArrayList<CustOmFormQuestionsRes> typeList = customFiledQueAdpter.getTypeList();
                if (typeList != null) {
                    ArrayList<Answer> customFieldAnswerList = getCustomFieldAnswerList(typeList);
                    addJob_req.setAnswerArrayList(customFieldAnswerList);
                    Log.d("answerOfCustomField", customFiledQueAdpter.toString());
                }
            }

            final ArrayList links = new ArrayList();
            final List<String> fileNames = new ArrayList<>();
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
                if (AppUtility.isInternetConnected()) {

                    if (App_preference.getSharedprefInstance().getLoginRes().getConfirmationTrigger() != null) {
                        if (App_preference.getSharedprefInstance().getLoginRes().getConfirmationTrigger().contains("1")) {
                            AppUtility.alertDialog2(this,
                                    "",
                                    LanguageController.getInstance().getMobileMsgByKey(AppConstant.send_client_mail),
                                    LanguageController.getInstance().getMobileMsgByKey(AppConstant.yes),
                                    LanguageController.getInstance().getMobileMsgByKey(AppConstant.no), new Callback_AlertDialog() {
                                        @Override
                                        public void onPossitiveCall() {
                                            addJob_req.setIsMailSentToClt("1");
                                            addJob_pc.addJobWithImageDescription(addJob_req, links, fileNames);
                                        }

                                        @Override
                                        public void onNegativeCall() {
                                            addJob_req.setIsMailSentToClt("0");
                                            addJob_pc.addJobWithImageDescription(addJob_req, links, fileNames);
                                        }
                                    });
                        } else {
                            addJob_req.setIsMailSentToClt("1");
                            addJob_pc.addJobWithImageDescription(addJob_req, links, fileNames);
                        }

                    } else {
                        addJob_req.setIsMailSentToClt("1");
                        addJob_pc.addJobWithImageDescription(addJob_req, links, fileNames);
                    }
                } else {
                    AppUtility.alertDialog2(Add_job_activity.this,
                            LanguageController.getInstance().getMobileMsgByKey(AppConstant.title_add_job),
                            LanguageController.getInstance().getMobileMsgByKey(AppConstant.add_job_offline_msg),
                            LanguageController.getInstance().getMobileMsgByKey(AppConstant.yes),
                            LanguageController.getInstance().getMobileMsgByKey(AppConstant.no), new Callback_AlertDialog() {
                                @Override
                                public void onPossitiveCall() {
                                    String des = addJob_req.getDes();
                                    if (!TextUtils.isEmpty(des))
                                        addJob_req.setDes(Html.fromHtml(des).toString());
                                    showDialogForSendMailToClt(addJob_req);
                                }

                                @Override
                                public void onNegativeCall() {

                                }
                            });
                }

            } else {
                showDialogForSendMailToClt(addJob_req);
            }

        }
    }

    private void showDialogForSendMailToClt(final AddJob_Req addJob_req) {
        if (App_preference.getSharedprefInstance().getLoginRes().getConfirmationTrigger() != null) {
            if (App_preference.getSharedprefInstance().getLoginRes().getConfirmationTrigger().contains("1")) {
                AppUtility.alertDialog2(this,
                        "",
                        LanguageController.getInstance().getMobileMsgByKey(AppConstant.send_client_mail),
                        LanguageController.getInstance().getMobileMsgByKey(AppConstant.yes),
                        LanguageController.getInstance().getMobileMsgByKey(AppConstant.no), new Callback_AlertDialog() {
                            @Override
                            public void onPossitiveCall() {
                                addJob_req.setIsMailSentToClt("1");
                                addJob_pc.callApiForAddJob(addJob_req);

                            }

                            @Override
                            public void onNegativeCall() {
                                addJob_req.setIsMailSentToClt("0");
                                addJob_pc.callApiForAddJob(addJob_req);
                            }

                        });
            } else {
                addJob_req.setIsMailSentToClt("0");
                addJob_pc.callApiForAddJob(addJob_req);
            }
        } else {
            addJob_req.setIsMailSentToClt("0");
            addJob_pc.callApiForAddJob(addJob_req);
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
                                String date = AppUtility.getDate(l, "hh:mm a");
                                ans = date;
                            }
                        } else if (questionList.get(i).getType().equals("7")) {
                            if (!TextUtils.isEmpty(questionList.get(i).getAns().get(0).getValue())) {
                                long l = Long.parseLong(questionList.get(i).getAns().get(0).getValue());
                                String date = AppUtility.getDate(l, "dd-MMM-yyyy hh:mm a");
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


    private ArrayList pullLinks(String html) {
        ArrayList links = new ArrayList();
        Elements srcs = Jsoup.parse(html).select("[src]"); //get All tags containing "src"
        for (int i = 0; i < srcs.size(); i++) {
            if (srcs.get(i).attributes() != null)
                links.add(srcs.get(i).attributes().get("src")); // get links of selected tags
        }
        return links;
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
            if (date1.getTime() > date.getTime()) {
                return true;
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    @Override
    public void finishActivity() {
        Intent intent = new Intent();
        setResult(MainActivity.ADDJOB, intent);
        this.finish();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (data == null) {
            RECURTYPENORMAL_CUSTOM = 1;
        } else if (data != null) {
            switch (requestCode) {
                case ADDCUSTOMRECUR:
                    String str = data.getStringExtra("daily_recur_pattern");
                    recurType = data.getStringExtra("recurType");
                    recurMsg = data.getStringExtra("recurMsg");
                    JobRecurModel model = new Gson().fromJson(str, JobRecurModel.class);
                    recudata.clear();
                    isRecur = "1";
                    recudata.add(model);
                    if (data.getStringExtra("selectedDays") != null) {
                        Log.e("", "");
                        String selectedDaysstr = data.getStringExtra("selectedDays");
                        selectedDaysMap.clear();
                        selectedDaysMap = new Gson().fromJson(selectedDaysstr, LinkedHashMap.class);
                        selectedDays.clear();
                        selectedDays.add(selectedDaysMap);

                        Log.e("", "");
                    }

                    showCustomRecurpattern();
                    break;
            }
        }
    }


    private void showCustomRecurpattern() {
        custom_recur_pattern.setText(LanguageController.getInstance().getMobileMsgByKey(AppConstant.normal_recur));
        msg_pattern_view.setVisibility(View.VISIBLE);
        recur_job_days_msg.setText(recurMsg);
        cutom_txt.setText(LanguageController.getInstance().getMobileMsgByKey(AppConstant.custom));
        normal_weekly_recur.setVisibility(View.GONE);
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
            case R.id.end_date_for_weekly_recur:

                DatePickerDialog endDateForjobRecur = new DatePickerDialog(this, AppUtility.recurendDate(new DateTimeCallback() {
                    @Override
                    public void setDateTime(String dateTime) {
                        end_date_for_weekly_recur.setText(dateTime);
                    }
                }), year, month, day);

                endDateForjobRecur.getDatePicker().setMinDate(Calendar.getInstance().getTimeInMillis());
                return endDateForjobRecur;

            case R.id.date_start:
                DatePickerDialog datePickerDialogSelectDate = new DatePickerDialog(this, AppUtility.InputDateSet(this, new DateTimeCallback() {
                    @Override
                    public void setDateTime(String dateTime) {
                        date_start.setText(dateTime);
                        date_end.setText(dateTime);
                        date_en = date_str = dateTime;
                        if (time_str != null && time_str.equals("")) {
                            Date date = new Date(System.currentTimeMillis());
                            String formate = AppUtility.dateTimeByAmPmFormate("hh:mm aa", "kk:mm");
                            SimpleDateFormat dateFormat = new SimpleDateFormat(formate,
                                    Locale.getDefault());
                            time_str = dateFormat.format(date);
                            time_start.setText(time_str);
                        }

                        if (time_en != null && time_en.equals("")) {
                            addJob_pc.getEndTime(date_str, time_str);
                        }

                        schdlStart = date_start + " " + time_str;


                        /***set start date for Recurance only when add recurance permission Allow***/
                        if (App_preference.getSharedprefInstance().getLoginRes().getRights().get(0).getIsRecur().equals("0")
                                && !date_start.getText().toString().equals("") && add_recur_checkBox.isChecked()) {
                            if (recur_pattern_view.getVisibility() == View.GONE)
                                recur_pattern_view.setVisibility(View.VISIBLE);
                            jobWeeklyRecur();
                        }


                    }
                }, LanguageController.getInstance().getMobileMsgByKey(AppConstant.err_start_end_date)), year, month, day);
                return datePickerDialogSelectDate;

            case R.id.date_end:
                final DatePickerDialog datePickerDialog = new DatePickerDialog(this, AppUtility.CompareInputOutputDate(this, new DateTimeCallback() {
                    @Override
                    public void setDateTime(String dateTime) {
                        date_end.setText(dateTime);
                        date_en = dateTime;

                        if (time_en != null && time_en.equals("")) {
                            Date date = new Date(System.currentTimeMillis());
                            String formate = AppUtility.dateTimeByAmPmFormate("hh:mm aa", "kk:mm");
                            SimpleDateFormat dateFormat = new SimpleDateFormat(formate,
                                    Locale.getDefault());
                            time_en = dateFormat.format(date);
                            time_end.setText(time_en);
                        }
                    }
                }, LanguageController.getInstance().getMobileMsgByKey(AppConstant.err_due_start_date)), year, month, day);
                datePickerDialog.getDatePicker().setMinDate(System.currentTimeMillis());
                return datePickerDialog;

            case R.id.time_start:
                TimePickerDialog timePickerDialog = new TimePickerDialog(this, AppUtility.InputTimeSet(this, new DateTimeCallback() {
                    @Override
                    public void setDateTime(String dateTime) {
                        time_str = dateTime;
                        DecimalFormat formatter = new DecimalFormat("00");
                        String[] aa = dateTime.split(":");
                        time_start.setText((formatter.format(Integer.parseInt(aa[0]))) + ":" + aa[1]);
                        schdlStart = date_str + " " + time_str;
                    }
                }, LanguageController.getInstance().getMobileMsgByKey(AppConstant.err_start_end_time)), mHour, mMinute, true);

                return timePickerDialog;

            case R.id.time_end:
                TimePickerDialog timePickerDialog1 = new TimePickerDialog(this, AppUtility.OutPutTime(this, new DateTimeCallback() {
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
            if (charSequence.hashCode() == job_desc.getText().hashCode())
                job_desc_layout.setHintEnabled(true);
            if (charSequence.hashCode() == job_instr.getText().hashCode())
                job_instr_layout.setHintEnabled(true);
            if (charSequence.hashCode() == mob_no.getText().hashCode())
                job_mob1_layout.setHintEnabled(true);
            if (charSequence.hashCode() == at_mob.getText().hashCode())
                job_mob2_layout.setHintEnabled(true);
            if (charSequence.hashCode() == email.getText().hashCode())
                job_email_layout.setHintEnabled(true);
            if (charSequence.hashCode() == adderes.getText().hashCode())
                job_adr_layout.setHintEnabled(true);
            if (charSequence.hashCode() == edt_lat.getText().hashCode())
                job_lat_layout.setHintEnabled(true);
            if (charSequence.hashCode() == edt_lng.getText().hashCode())
                job_lng_layout.setHintEnabled(true);
            if (charSequence.hashCode() == city.getText().hashCode())
                job_city_layout.setHintEnabled(true);
            if (charSequence.hashCode() == post_code.getText().hashCode())
                job_postal_layout.setHintEnabled(true);
            if (charSequence.hashCode() == landmark_edt.getText().hashCode())
                landmark_layout.setHintEnabled(true);
            if (charSequence.hashCode() == contact_add_edt.getText().hashCode())
                contact_add_layout.setHintEnabled(true);
            if (charSequence.hashCode() == site_add_edt.getText().hashCode())
                site_add_layout.setHintEnabled(true);

        } else if (charSequence.length() <= 0) {
            /**Floating hint Disable after text enter**/
            if (charSequence.hashCode() == job_desc.getText().hashCode())
                job_desc_layout.setHintEnabled(false);
            if (charSequence.hashCode() == job_instr.getText().hashCode())
                job_instr_layout.setHintEnabled(false);
            if (charSequence.hashCode() == mob_no.getText().hashCode())
                job_mob1_layout.setHintEnabled(false);
            if (charSequence.hashCode() == mob_no.getText().hashCode())
                job_mob2_layout.setHintEnabled(false);
            if (charSequence.hashCode() == email.getText().hashCode())
                job_email_layout.setHintEnabled(false);
            if (charSequence.hashCode() == adderes.getText().hashCode())
                job_adr_layout.setHintEnabled(false);
            if (charSequence.hashCode() == edt_lat.getText().hashCode())
                job_lat_layout.setHintEnabled(false);
            if (charSequence.hashCode() == edt_lng.getText().hashCode())
                job_lng_layout.setHintEnabled(false);
            if (charSequence.hashCode() == city.getText().hashCode())
                job_city_layout.setHintEnabled(false);
            if (charSequence.hashCode() == post_code.getText().hashCode())
                job_postal_layout.setHintEnabled(false);
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
    public void onDocumentSelected(String path, boolean isImage) {
        super.onDocumentSelected(path, isImage);
        if (!TextUtils.isEmpty(path) && isImage && mEditor != null) {
            mEditor.getSettings().setAllowFileAccess(true);
            mEditor.insertImage(path, "logo", 320, 250);
        }

    }

    public interface DateTimeCallback {
        void setDateTime(String dateTime);
    }


}