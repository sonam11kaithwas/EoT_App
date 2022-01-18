package com.eot_app.nav_menu.quote.add_quotes_pkg;

import android.Manifest;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.text.Html;
import android.text.Spannable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;

import com.eot_app.R;
import com.eot_app.UploadDocumentActivity;
import com.eot_app.custom_dropDown.CustomDPController;
import com.eot_app.eoteditor.CustomEditor;
import com.eot_app.eoteditor.EotEditor;
import com.eot_app.eoteditor.PicassoImageGetter;
import com.eot_app.lat_lng_sync_pck.LatLngSycn_Controller;
import com.eot_app.locations.LocationTracker;
import com.eot_app.locations.OnLocationUpdate;
import com.eot_app.nav_menu.appointment.dbappointment.Appointment;
import com.eot_app.nav_menu.client.client_db.Client;
import com.eot_app.nav_menu.client.clientlist.client_detail.contact.client_dao.ContactData;
import com.eot_app.nav_menu.client.clientlist.client_detail.site.sitelist.Site_model;
import com.eot_app.nav_menu.client.clientlist.client_detail.site.sitelist.editsite.editsitedb.SpinnerCountrySite;
import com.eot_app.nav_menu.jobs.add_job.adapters.FilterAdapter;
import com.eot_app.nav_menu.jobs.add_job.adapters.FilterAdapterContact;
import com.eot_app.nav_menu.jobs.add_job.adapters.FilterAdapterSites;
import com.eot_app.nav_menu.jobs.add_job.adapters.FilterCountry;
import com.eot_app.nav_menu.jobs.add_job.adapters.FilterStates;
import com.eot_app.nav_menu.jobs.job_db.JtId;
import com.eot_app.nav_menu.quote.add_quotes_pkg.add_quote_mvp.Add_Quote_Pc;
import com.eot_app.nav_menu.quote.add_quotes_pkg.add_quote_mvp.Add_Quote_Pi;
import com.eot_app.nav_menu.quote.add_quotes_pkg.add_quote_mvp.Add_Quote_View;
import com.eot_app.nav_menu.quote.add_quotes_pkg.model_pkg.Add_Quote_ReQ;
import com.eot_app.nav_menu.quote.add_quotes_pkg.quotes_adpter.Dynamic_Adpter;
import com.eot_app.nav_menu.quote.quote_invoice_pkg.quote_model_pkg.QuotesDetails;
import com.eot_app.utility.AppConstant;
import com.eot_app.utility.AppUtility;
import com.eot_app.utility.App_preference;
import com.eot_app.utility.Country;
import com.eot_app.utility.EotApp;
import com.eot_app.utility.States;
import com.eot_app.utility.db.AppDataBase;
import com.eot_app.utility.language_support.LanguageController;
import com.eot_app.utility.settings.setting_db.FieldWorker;
import com.eot_app.utility.settings.setting_db.JobTitle;
import com.eot_app.utility.util_interfaces.Callback_AlertDialog;
import com.eot_app.utility.util_interfaces.MyAdapter2;
import com.eot_app.utility.util_interfaces.MySpinnerAdapter;
import com.google.android.material.textfield.TextInputLayout;
import com.google.gson.Gson;
import com.hypertrack.hyperlog.HyperLog;

import org.jsoup.Jsoup;
import org.jsoup.select.Elements;

import java.text.DateFormat;
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

public class AddQuotes_Activity extends UploadDocumentActivity implements View.OnClickListener, Add_Quote_View, TextWatcher
        , Spinner.OnItemSelectedListener {
    private final String[] arraystatus = {"New", "Approved", "Reject", "On Hold"};
    private final String Start_Date = "StartDate";
    private final String End_Date = "EndDate";
    private final Set<String> jtIdList = new HashSet<>();
    private final List<JtId> Edit_jtIdList = new ArrayList<>();
    private final ArrayList<JobTitle> datastr = new ArrayList<>();
    Calendar myCalendar;
    LocationTracker locationTracker;
    CustomDPController customDPController;
    CustomEditor mEditor;
    TextView quotesdeshint;
    ImageView action_insert_image;
    private TextInputLayout quote_instr_layout, quote_mobile_layout, quote_mob2_layout, quote_email_layout, quote_adr_layout, quote_city_layout, quote_postal_layout, client_quote_layout, contact_quote_layout, site_quote_layout, quote_country_layout, quote_state_layout, quote_notes_layout, quote_term_layout;
    //quote_desc_layout,
    private EditText quote_instr_edt, quote_mob_no_edt, quote_at_mob_edt, quote_email_edt, quote_adrs_edt, quote_city_edt, quote_post_code_edt, quote_notes_edt, quote_term_edt;
    //quote_desc_edt,
    private Button submit_btn;
    private TextView quote_title_hint_tv, quote_title_set, schel_start, schel_end, date_start, time_start, date_end, time_end;
    private LinearLayout jobservicelayout;
    // private ConstraintLayout quote_title_layout;
    private Add_Quote_Pi add_quote_pi;
    private Spinner quote_title_spinner;
    private CheckBox quote_client_checkBox, quote_contact_checkBox, quote_site_checkBox;
    private AutoCompleteTextView auto_quote_client, auto_sites, auto_quote_contact, auto_quote_country, auto_quote_state;
    private RelativeLayout relative_main;
    private String invDate = "", dueDate = "";
    /**
     * select date from picker & concanate current time
     */
    private final DatePickerDialog.OnDateSetListener datePickerListener = new DatePickerDialog.OnDateSetListener() {
        @Override
        public void onDateSet(DatePicker view, int selectedYear, int selectedMonth, int selectedDay) {
            String dateselect = "";
            try {
                // AppUtility.getDateWithFormate
                //                (Long.parseLong(quotesDetails.getInvData().getInvDate()), "dd-MM-yyyy hh:mm:ss a");
                DateFormat formatter = new SimpleDateFormat("dd-MM-yyyy", Locale.US);//hh:mm:ss a
                Date startDate = formatter.parse(selectedDay + "-" + (selectedMonth + 1) + "-" + selectedYear);
                dateselect = formatter.format(startDate);
            } catch (ParseException e) {
                e.printStackTrace();
            }
            DateFormat dateFormat = new SimpleDateFormat(
                    AppUtility.dateTimeByAmPmFormate("hh:mm:ss a", "kk:mm:ss"), Locale.US);//append current time
            dateFormat.format(new Date());
            String tag = ((String) view.getTag());
            if (tag.equals(Start_Date)) {
                invDate = dateselect + " ";// + dateFormat.format(new Date());
                //dueDate= dateselect + " " + dateFormat.format(new Date());
                date_start.setText(dateselect);
            } else if (tag.equals(End_Date)) {
                dueDate = dateselect + " ";//+ dateFormat.format(new Date());
                date_end.setText(dateselect);
            }
        }
    };
    private String cltId = "", nm = "", siteId = "", snm, conId = "", cnm, assignByUser = "", quotId = "", invId = "";
    private FilterAdapterContact filterContact;
    private String ctry_id = "", state_id = "";
    private int clientForFuture = 0, siteForFuture = 0, contactForFuture = 0;
    private QuotesDetails quotesDetails;
    private TextView hint_txt_fw_Nm, hint_txt_status_Nm, tv_spinner_Fw, tv_spinner_status;
    private LinearLayout linearLayout_Fw, linearLayout_status, ll_status;
    private List<FieldWorker> fw_List = new ArrayList<>();
    private MyAdapter2 myAdapter;
    private Spinner spinner_Fw, spinner_status;
    private boolean isUpdate;
    private String[] id_array;
    private String appId;
    private ContactData selectedContactData;
    private Site_model selectedSiteData;
    private ImageView site_dp_img, contact_dp_img;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_quotes);
        setTitle(LanguageController.getInstance().getMobileMsgByKey(AppConstant.add_quote));
        initializelables();
        customDPController = new CustomDPController();

        Bundle bundle = getIntent().getExtras();
        if (getIntent().hasExtra("EditQuotes")) {
            HyperLog.i("", "EditQuotes from Quotes List");
            ll_status.setVisibility(View.VISIBLE);
            String convertObject = bundle.getString("EditQuotes");
            quotesDetails = new Gson().fromJson(convertObject, QuotesDetails.class);
            setTitle(LanguageController.getInstance().getMobileMsgByKey(AppConstant.edit_quote));
            submit_btn.setText(LanguageController.getInstance().getMobileMsgByKey(AppConstant.update));
            isUpdate = true;
            setDefaultValuesOfQuoteS(quotesDetails);//set view for update Quotes
            add_quote_pi.getJobServices();

        } else if (getIntent().hasExtra("appointmentId")) {
            HyperLog.i("", "EditQuotes from appointment details");

            isUpdate = false;
            ll_status.setVisibility(View.GONE);
            setTitle(LanguageController.getInstance().getMobileMsgByKey(AppConstant.add_quote));
            submit_btn.setText(LanguageController.getInstance().getMobileMsgByKey(AppConstant.create_quote));
            //addQuotesData();
            getCurrentdate();
            add_quote_pi.getJobServices();
            add_quote_pi.getTermsConditions();


            String appointmentId = getIntent().getStringExtra("appointmentId");
            Appointment appointment = AppDataBase.getInMemoryDatabase(EotApp.getAppinstance()).appointmentModel().getAppointmentById(appointmentId);
            if (appointment != null) {
                appId = appointment.getAppId();

                client_quote_layout.setHintEnabled(true);
                quote_client_checkBox.setVisibility(View.GONE);
                quote_client_checkBox.setChecked(false);

                site_quote_layout.setHintEnabled(true);
                quote_site_checkBox.setVisibility(View.GONE);

                auto_quote_client.setEnabled(false);//not editable client name
                auto_quote_client.setText(appointment.getNm());
                auto_sites.setText(appointment.getSnm());
                auto_quote_contact.setText(appointment.getCnm());


                contact_quote_layout.setHintEnabled(true);
                quote_contact_checkBox.setVisibility(View.GONE);
                nm = appointment.getNm();
                snm = appointment.getSnm();
                cnm = appointment.getCnm();
                cltId = appointment.getCltId();

                if (cltId.equals("0")) {
                    conId = "";
                    siteId = "";

                } else {
                    //nm = "";
                    conId = appointment.getConId();
                    siteId = appointment.getSiteId();

                    add_quote_pi.getContactList(appointment.getCltId());
                    add_quote_pi.getSilteList(appointment.getCltId());

                }
                quote_email_edt.setText(appointment.getEmail());
                quote_mob_no_edt.setText(appointment.getMob1());
                quote_at_mob_edt.setText(appointment.getMob2());
                quote_post_code_edt.setText(appointment.getZip());
                quote_city_edt.setText(appointment.getCity());
                quote_adrs_edt.setText(appointment.getAdr());


                auto_quote_country.setText(SpinnerCountrySite.getCountryNameById(appointment.getCtry()));
                ctry_id = appointment.getCtry();
                state_id = appointment.getState();
                auto_quote_state.setText(SpinnerCountrySite.getStatenameById(appointment.getCtry(), appointment.getState()));
                add_quote_pi.getStateList(ctry_id);
                quote_country_layout.setHintEnabled(true);
                quote_state_layout.setHintEnabled(true);


            }
        } else {
            HyperLog.i("", "Add Quote form opening");

            isUpdate = false;
            ll_status.setVisibility(View.GONE);
            setTitle(LanguageController.getInstance().getMobileMsgByKey(AppConstant.add_quote));
            submit_btn.setText(LanguageController.getInstance().getMobileMsgByKey(AppConstant.create_quote));
            addQuotesData();
            add_quote_pi.getTermsConditions();
        }


    }


    private void addQuotesData() {
        setCompanySettingAdrs();
        getCurrentdate();
        add_quote_pi.getClientList();//client list need only add new Quotes
        add_quote_pi.getJobServices();
    }


    @Override
    public void setfwListForQuotes(final List<FieldWorker> fwList) {
        this.fw_List = fwList;
        if (fwList.size() > 0) {
            AppUtility.spinnerPopUpWindow(spinner_Fw);
            Dynamic_Adpter myClassAdapter = new Dynamic_Adpter<>(this, fwList);
            spinner_Fw.setAdapter(myClassAdapter);

            spinner_Fw.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> adapterView, View view, int position, long l) {
                    tv_spinner_Fw.setText(fw_List.get(position).getName());
                    assignByUser = fwList.get(position).getUsrId();
                }

                @Override
                public void onNothingSelected(AdapterView<?> adapterView) {

                }
            });
        }

    }

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }

    /**
     * Set Default data when update quotes
     */
    private void setDefaultValuesOfQuoteS(QuotesDetails quotesDetails) {
        HyperLog.i("", "setDefaultValuesOfQuoteS(M) Start");
        Set<String> data = new HashSet<>();
        for (JtId jtId : quotesDetails.getJtId()) {
            data.add(jtId.getTitle());
            jtIdList.add(jtId.getJtId());
        }
        jobServiceSelected(data);


        //  quote_desc_edt.setText(quotesDetails.getDes());
        quote_instr_edt.setText(quotesDetails.getInst());

        cltId = quotesDetails.getCltId();

        auto_quote_client.setText(quotesDetails.getNm());
        quote_client_checkBox.setVisibility(View.GONE);
        auto_quote_client.setEnabled(false);//not editable client name
        client_quote_layout.setHintEnabled(true);

        conId = quotesDetails.getConId();
        siteId = quotesDetails.getSiteId();

        if (cltId.equals("0") || cltId.equals("")) {
            nm = quotesDetails.getNm();
        } else {
            add_quote_pi.getContactList(quotesDetails.getCltId());
            add_quote_pi.getSilteList(quotesDetails.getCltId());

        }
        auto_quote_contact.setText(quotesDetails.getCnm());
        contact_quote_layout.setHintEnabled(true);
        quote_contact_checkBox.setVisibility(View.GONE);

        auto_sites.setText(quotesDetails.getSnm());
        site_quote_layout.setHintEnabled(true);
        quote_site_checkBox.setVisibility(View.GONE);

        quote_email_edt.setText(quotesDetails.getEmail());
        try {
            if (quotesDetails.getMob1() != null && quotesDetails.getMob1().length() == 0
                    && App_preference.getSharedprefInstance().getLoginRes().getCtryCode() != null) {
                quote_mob_no_edt.setText(App_preference.getSharedprefInstance().getLoginRes().getCtryCode());
            } else {
                quote_mob_no_edt.setText(quotesDetails.getMob1());
            }
            if (quotesDetails.getMob2() != null && quotesDetails.getMob2().length() == 0
                    && App_preference.getSharedprefInstance().getLoginRes().getCtryCode() != null) {
                quote_at_mob_edt.setText(App_preference.getSharedprefInstance().getLoginRes().getCtryCode());
            } else {
                quote_at_mob_edt.setText(quotesDetails.getMob2());
            }
        } catch (Exception ex) {
            ex.getMessage();
            quote_mob_no_edt.setText(quotesDetails.getMob1());
            quote_at_mob_edt.setText(quotesDetails.getMob2());
        }

        quote_post_code_edt.setText(quotesDetails.getZip());
        quote_city_edt.setText(quotesDetails.getCity());
        quote_adrs_edt.setText(quotesDetails.getAdr());

        quote_notes_edt.setText(quotesDetails.getInvData().getNote());

        auto_quote_country.setText(SpinnerCountrySite.getCountryNameById(quotesDetails.getCtry()));
        ctry_id = quotesDetails.getCtry();
        state_id = quotesDetails.getState();
        auto_quote_state.setText(SpinnerCountrySite.getStatenameById(quotesDetails.getCtry(), quotesDetails.getState()));
        add_quote_pi.getStateList(ctry_id);
        quote_country_layout.setHintEnabled(true);
        quote_state_layout.setHintEnabled(true);

        invDate = AppUtility.getDateWithFormate
                (Long.parseLong(quotesDetails.getInvData().getInvDate()),
                        AppUtility.dateTimeByAmPmFormate("dd-MM-yyyy hh:mm:ss a",
                                "dd-MM-yyyy kk:mm:ss"));
        dueDate = AppUtility.getDateWithFormate(Long.parseLong(quotesDetails.getInvData().getDuedate()),
                AppUtility.dateTimeByAmPmFormate("dd-MM-yyyy hh:mm:ss a"
                        , "dd-MM-yyyy kk:mm:ss"));
        date_start.setText(AppUtility.getDateWithFormate
                (Long.parseLong(quotesDetails.getInvData().getInvDate()), "dd-MM-yyyy"));
        date_end.setText(AppUtility.getDateWithFormate(Long.parseLong(quotesDetails.getInvData().getDuedate()), "dd-MM-yyyy"));

        quote_notes_edt.setText(quotesDetails.getInvData().getNote());

        assignByUser = quotesDetails.getAssignByUser();
        getAssignuserNameById();

        quotId = quotesDetails.getQuotId();
        invId = quotesDetails.getInvData().getInvId();
        if (!TextUtils.isEmpty(quotesDetails.getTerm()))
            quote_term_edt.setText(Html.fromHtml(quotesDetails.getTerm()));

        if (!TextUtils.isEmpty(quotesDetails.getStatus())) {
            try {
                int i = Integer.parseInt(quotesDetails.getStatus());
                if (i == 8) i = 4;
                i--;
                spinner_status.setSelection(i);
                tv_spinner_status.setText(arraystatus[i]);
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }


        try {
            if (!TextUtils.isEmpty(this.quotesDetails.getDes())) {
                quotesdeshint.setVisibility(View.VISIBLE);
                mEditor.setHtml(this.quotesDetails.getDes());
            } else quotesdeshint.setVisibility(View.GONE);

//      if (!TextUtils.isEmpty(this.quotesDetails.getDes())) {
//                  quotesdeshint.setVisibility(View.VISIBLE);
//              //  mEditor.setHtml(this.quotesDetails.getDes());
//            } else quotesdeshint.setVisibility(View.GONE);


            if (this.quotesDetails.getDes() != null) {
                Spannable spannableHtmlWithImageGetter = PicassoImageGetter.getSpannableHtmlWithImageGetter(quotesdeshint,
                        this.quotesDetails.getDes());

//                PicassoImageGetter.setClickListenerOnHtmlImageGetter(spannableHtmlWithImageGetter, new PicassoImageGetter.Callback() {
//                    @Override
//                    public void onImageClick(String imageUrl) {
//                        if (!TextUtils.isEmpty(imageUrl)) {
//                            startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(imageUrl)));
//                        }
//                    }
//                });
                // quotesdeshint.setText(spannableHtmlWithImageGetter);
                //    quotesdeshint.setMovementMethod(LinkMovementMethod.getInstance());
            }
        } catch (Exception e) {
            e.getMessage();
        }


        HyperLog.i("", "setDefaultValuesOfQuoteS(M) End");

    }

    private void getAssignuserNameById() {
        for (FieldWorker fw : fw_List) {
            if (fw.getUsrId().equals(assignByUser)) {
                tv_spinner_Fw.setText(fw.getName());
                break;
            }
        }

    }

    private void initializelables() {
        relative_main = findViewById(R.id.relative_main);
        site_dp_img = findViewById(R.id.site_dp_img);
        site_dp_img.setOnClickListener(this);


        contact_dp_img = findViewById(R.id.contact_dp_img);
        contact_dp_img.setOnClickListener(this);

        action_insert_image = findViewById(R.id.action_insert_image);

        mEditor = findViewById(R.id.editor);
        quotesdeshint = findViewById(R.id.jobdeshint);
        quotesdeshint.setHint(LanguageController.getInstance().getMobileMsgByKey(AppConstant.quote_desc));

//        quote_desc_layout = findViewById(R.id.quote_desc_layout);
//        quote_desc_edt = findViewById(R.id.quote_desc_edt);
//        quote_desc_edt.setHint(LanguageController.getInstance().getMobileMsgByKey(AppConstant.quote_desc));

        quote_instr_layout = findViewById(R.id.quote_instr_layout);
        quote_instr_edt = findViewById(R.id.quote_instr_edt);
        quote_instr_edt.setHint(LanguageController.getInstance().getMobileMsgByKey(AppConstant.quote_instr));


        client_quote_layout = findViewById(R.id.client_quote_layout);
        quote_client_checkBox = findViewById(R.id.quote_client_checkBox);
        quote_client_checkBox.setText(LanguageController.getInstance().getMobileMsgByKey(AppConstant.save_for_future_use));
        auto_quote_client = findViewById(R.id.auto_quote_client);
        auto_quote_client.setHint(LanguageController.getInstance().getMobileMsgByKey(AppConstant.client_mand) + " *");

        contact_quote_layout = findViewById(R.id.contact_quote_layout);
        quote_contact_checkBox = findViewById(R.id.quote_contact_checkBox);
        quote_contact_checkBox.setText(LanguageController.getInstance().getMobileMsgByKey(AppConstant.save_for_future_use));
        auto_quote_contact = findViewById(R.id.auto_quote_contact);
        auto_quote_contact.setHint(LanguageController.getInstance().getMobileMsgByKey(AppConstant.contact_name));

        site_quote_layout = findViewById(R.id.site_quote_layout);
        quote_site_checkBox = findViewById(R.id.quote_site_checkBox);
        quote_site_checkBox.setText(LanguageController.getInstance().getMobileMsgByKey(AppConstant.save_for_future_use));
        auto_sites = findViewById(R.id.auto_sites);
        auto_sites.setHint(LanguageController.getInstance().getMobileMsgByKey(AppConstant.site_name));


        quote_mobile_layout = findViewById(R.id.quote_mobile_layout);
        quote_mob_no_edt = findViewById(R.id.quote_mob_no_edt);
        quote_mob_no_edt.setHint(LanguageController.getInstance().getMobileMsgByKey(AppConstant.mob_no));

        quote_mob2_layout = findViewById(R.id.quote_mob2_layout);
        quote_at_mob_edt = findViewById(R.id.quote_at_mob_edt);
        quote_at_mob_edt.setHint(LanguageController.getInstance().getMobileMsgByKey(AppConstant.alt_mobile_number));

        quote_email_layout = findViewById(R.id.quote_email_layout);
        quote_email_edt = findViewById(R.id.quote_email_edt);
        quote_email_edt.setHint(LanguageController.getInstance().getMobileMsgByKey(AppConstant.email));


        jobservicelayout = findViewById(R.id.jobservicelayout);
        quote_title_set = findViewById(R.id.quote_title_set);
        quote_title_set.setHint(LanguageController.getInstance().getMobileMsgByKey(AppConstant.job_title));//+ " *"
        quote_title_hint_tv = findViewById(R.id.quote_title_hint_tv);
        quote_title_hint_tv.setText(LanguageController.getInstance().getMobileMsgByKey(AppConstant.job_title));// + " *"


        quote_adr_layout = findViewById(R.id.quote_adr_layout);
        quote_adrs_edt = findViewById(R.id.quote_adrs_edt);
        quote_adrs_edt.setHint(LanguageController.getInstance().getMobileMsgByKey(AppConstant.address) + " *");


        quote_country_layout = findViewById(R.id.quote_country_layout);
        auto_quote_country = findViewById(R.id.auto_quote_country);
        auto_quote_country.setHint(LanguageController.getInstance().getMobileMsgByKey(AppConstant.country) + " *");

        quote_state_layout = findViewById(R.id.quote_state_layout);
        auto_quote_state = findViewById(R.id.auto_quote_state);
        auto_quote_state.setHint(LanguageController.getInstance().getMobileMsgByKey(AppConstant.state) + " *");

        quote_city_layout = findViewById(R.id.quote_city_layout);
        quote_city_edt = findViewById(R.id.quote_city_edt);
        quote_city_edt.setHint(LanguageController.getInstance().getMobileMsgByKey(AppConstant.city));

        quote_postal_layout = findViewById(R.id.quote_postal_layout);
        quote_post_code_edt = findViewById(R.id.quote_post_code_edt);
        quote_post_code_edt.setHint(LanguageController.getInstance().getMobileMsgByKey(AppConstant.postal_code));

        schel_start = findViewById(R.id.schel_start);
        schel_start.setHint(LanguageController.getInstance().getMobileMsgByKey(AppConstant.quotes_start_date));

        schel_end = findViewById(R.id.schel_end);
        schel_end.setHint(LanguageController.getInstance().getMobileMsgByKey(AppConstant.quotes_end_date));

        date_start = findViewById(R.id.date_start);
        date_start.setHint(LanguageController.getInstance().getMobileMsgByKey(AppConstant.date_form));

        date_end = findViewById(R.id.date_end);
        date_end.setHint(LanguageController.getInstance().getMobileMsgByKey(AppConstant.date_form));

        quote_notes_layout = findViewById(R.id.quote_notes_layout);
        quote_notes_edt = findViewById(R.id.quote_notes_edt);
        quote_notes_edt.setHint(LanguageController.getInstance().getMobileMsgByKey(AppConstant.notes));

        quote_term_layout = findViewById(R.id.quote_term_layout);
        quote_term_edt = findViewById(R.id.quote_term_edt);
        quote_term_edt.setHint(LanguageController.getInstance().getMobileMsgByKey(AppConstant.term_condition));

        time_start = findViewById(R.id.time_start);
        time_start.setHint(LanguageController.getInstance().getMobileMsgByKey(AppConstant.time_form));

        time_end = findViewById(R.id.time_end);
        time_end.setHint(LanguageController.getInstance().getMobileMsgByKey(AppConstant.time_form));

        linearLayout_Fw = findViewById(R.id.linearLayout_Fw);
        linearLayout_status = findViewById(R.id.linearLayout_status);
        ll_status = findViewById(R.id.ll_status);

        hint_txt_fw_Nm = findViewById(R.id.hint_txt_fw_Nm);
        hint_txt_fw_Nm.setText(LanguageController.getInstance().getMobileMsgByKey(AppConstant.assign_to_fw));
        hint_txt_status_Nm = findViewById(R.id.hint_txt_status_Nm);
        hint_txt_status_Nm.setText(LanguageController.getInstance().getMobileMsgByKey(AppConstant.status_radio_btn));

        tv_spinner_Fw = findViewById(R.id.tv_spinner_Fw);
        tv_spinner_Fw.setText(LanguageController.getInstance().getMobileMsgByKey(AppConstant.assign_to_fw));
        spinner_Fw = findViewById(R.id.spinner_Fw);
        tv_spinner_Fw.setText(App_preference.getSharedprefInstance().getLoginRes().getUsername());

        tv_spinner_status = findViewById(R.id.tv_spinner_status);
        tv_spinner_status.setText(arraystatus[0]);
        spinner_status = findViewById(R.id.spinner_status);
        // tv_spinner_status.setText(App_preference.getSharedprefInstance().getLoginRes().getUsername());

        submit_btn = findViewById(R.id.submit_btn);
        submit_btn.setText(LanguageController.getInstance().getMobileMsgByKey(AppConstant.submit_btn));


        initializeView();
    }

    @Override
    public void onDocumentSelected(String path, boolean isImage) {
        super.onDocumentSelected(path, isImage);
        if (!TextUtils.isEmpty(path) && isImage && mEditor != null) {

            mEditor.getSettings().setAllowFileAccess(true);
            mEditor.insertImage(path, "logo", 320, 250);

        }

    }


    private void initializeView() {
        //   quote_title_spinner = findViewById(R.id.quote_title_spinner);


        jobservicelayout.setOnClickListener(this);
        auto_quote_client.setOnClickListener(this);
        quote_client_checkBox.setOnClickListener(this);
        quote_contact_checkBox.setOnClickListener(this);
        quote_site_checkBox.setOnClickListener(this);
        date_start.setOnClickListener(this);
        date_end.setOnClickListener(this);
        time_start.setOnClickListener(this);
        time_end.setOnClickListener(this);
        submit_btn.setOnClickListener(this);
        linearLayout_Fw.setOnClickListener(this);
        linearLayout_status.setOnClickListener(this);


        textInputLayoutHint();

        add_quote_pi = new Add_Quote_Pc(this);
        //  add_quote_pi.getJobServices();//use ful
        add_quote_pi.getCountryList();
        add_quote_pi.getActiveUserList();

        AppUtility.spinnerPopUpWindow(spinner_status);
        spinner_status.setSelected(false);
        spinner_status.setAdapter(new MySpinnerAdapter(this, arraystatus));
        spinner_status.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                tv_spinner_status.setText(arraystatus[position]);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        getUpdatedLocation();


        mEditor.setPlaceholder(LanguageController.getInstance().getMobileMsgByKey(AppConstant.quote_desc));
        mEditor.setTextColor(Color.parseColor("#8C9293"));


        mEditor.setOnTextChangeListener(new EotEditor.OnTextChangeListener() {
            @Override
            public void onTextChange(String text) {
                if (text.length() >= 1) {
                    quotesdeshint.setVisibility(View.VISIBLE);
                } else {
                    quotesdeshint.setVisibility(View.INVISIBLE);
                }
            }
        });

        mEditor.setBackgroundColor(Color.TRANSPARENT);
        mEditor.focusEditor();

        mEditor.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    quotesdeshint.setVisibility(View.VISIBLE);
                } else {
                    if (mEditor.getHtml() != null) {
                        if (mEditor.getHtml().length() != 0) {
                            quotesdeshint.setVisibility(View.VISIBLE);
                        } else
                            quotesdeshint.setVisibility(View.INVISIBLE);
                    } else
                        quotesdeshint.setVisibility(View.INVISIBLE);
                }
            }
        });

        action_insert_image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mEditor.focusEditor();
                selectFile(true);
            }
        });


    }


    private void setCompanySettingAdrs() {
        auto_quote_country.setText(SpinnerCountrySite.getCountryNameById(App_preference.getSharedprefInstance().getCompanySettingsDetails().getCtry()));
        add_quote_pi.getStateList((App_preference.getSharedprefInstance().getCompanySettingsDetails().getCtry()));
        quote_state_layout.setHintEnabled(true);
        auto_quote_state.setText(SpinnerCountrySite.getStatenameById(App_preference.getSharedprefInstance().getCompanySettingsDetails().getCtry(), App_preference.getSharedprefInstance().getCompanySettingsDetails().getState()));
        //   city.setText(App_preference.getSharedprefInstance().getCompanySettingsDetails().getCity());
        state_id = (App_preference.getSharedprefInstance().getCompanySettingsDetails().getState());
        ctry_id = (App_preference.getSharedprefInstance().getCompanySettingsDetails().getCtry());

        try {
            if (App_preference.getSharedprefInstance().getLoginRes().getCtryCode() != null) {
                quote_mob_no_edt.setText(App_preference.getSharedprefInstance().getLoginRes().getCtryCode());
                quote_at_mob_edt.setText(App_preference.getSharedprefInstance().getLoginRes().getCtryCode());
            }
        } catch (Exception e) {
            e.getMessage();
        }
    }


    private void getCurrentdate() {
        String datetime = AppUtility.getDateByFormat(
                AppUtility.dateTimeByAmPmFormate("dd-MM-yyyy hh:mm:ss a", "dd-MM-yyyy kk:mm:ss"));//get current date time
        dueDate = invDate = datetime;
        String crrntDate = getDate(datetime);
        date_start.setText(crrntDate);
        date_end.setText(crrntDate);
    }

    private void textInputLayoutHint() {
        AppUtility.setupUI(relative_main, this);

        //quote_desc_layout.getEditText().addTextChangedListener(this);
        quote_instr_layout.getEditText().addTextChangedListener(this);

        quote_mobile_layout.getEditText().addTextChangedListener(this);
        quote_mob2_layout.getEditText().addTextChangedListener(this);
        quote_adr_layout.getEditText().addTextChangedListener(this);
        quote_email_layout.getEditText().addTextChangedListener(this);
        quote_city_layout.getEditText().addTextChangedListener(this);
        quote_postal_layout.getEditText().addTextChangedListener(this);
        quote_state_layout.getEditText().addTextChangedListener(this);
        quote_notes_layout.getEditText().addTextChangedListener(this);
        quote_term_layout.getEditText().addTextChangedListener(this);

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.site_dp_img:
                auto_sites.showDropDown();
                break;
            case R.id.contact_dp_img:
                auto_quote_contact.showDropDown();
                break;
            case R.id.linearLayout_Fw:
                spinner_Fw.performClick();
                break;
            case R.id.linearLayout_status:
                spinner_status.performClick();
                break;
            case R.id.jobservicelayout:
                //  quote_title_spinner.performClick();
                if (id_array.length > 0) {
                    customDPController.showSpinnerDropDown(this, jobservicelayout, datastr);
                    // CustomDPController.getINSTANCE().showSpinnerDropDown(this, jobservicelayout, datastr);
                } else {
                    AppUtility.alertDialog(this, "", LanguageController.getInstance().getMobileMsgByKey(AppConstant.err_empty_Title), LanguageController.getInstance().getMobileMsgByKey(AppConstant.ok), "", new Callable<Boolean>() {
                        @Override
                        public Boolean call() throws Exception {
                            return null;
                        }
                    });
                }
                break;
            case R.id.date_start:
                SelectStartDate("StartDate");
                break;
            case R.id.date_end:
                SelectStartDate("EndDate");
                break;
            case R.id.time_start:
                break;
            case R.id.time_end:
                break;
            case R.id.quote_client_checkBox:
                if (quote_client_checkBox.isChecked()) {
                    clientForFuture = siteForFuture = contactForFuture = 1;
                } else {
                    clientForFuture = siteForFuture = contactForFuture = 0;
                }
                break;
            case R.id.quote_contact_checkBox:
                if (quote_contact_checkBox.isChecked()) {
                    contactForFuture = 1;
                }
                break;
            case R.id.quote_site_checkBox:
                if (quote_site_checkBox.isChecked()) {
                    siteForFuture = 1;
                }
                break;
            case R.id.submit_btn:

                submit_btn.setEnabled(false);
                createAddQuotesRequest();
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        submit_btn.setEnabled(true);
                    }
                }, 500);
                break;
        }
    }

    private boolean conditionCheck(String schdlStart, String schdlFinish) {
        try {
            SimpleDateFormat gettingfmt = new SimpleDateFormat("dd-MM-yyyy", Locale.US);//hh:mm:s a
            Date date = gettingfmt.parse(schdlStart);
            date.getTime();

            Date date1 = gettingfmt.parse(schdlFinish);
//            date1.getTime();
//            if (date1.getTime() > date.getTime() || date1.getTime() == date.getTime())
//                return true;

            date1.getDate();
            if (date1.getDate() > date.getDate() || date1.getDate() == date.getDate())
                return true;

        } catch (Exception e) {
            e.printStackTrace();
        }


        return false;
    }

    public void createAddQuotesRequest() {
        if (assignByUser.equals("")) {
            assignByUser = App_preference.getSharedprefInstance().getLoginRes().getUsrId();
        }

        String clientNameReq = "";
        cnm = auto_quote_contact.getText().toString().trim();
        snm = auto_sites.getText().toString().trim();

        if (cltId.equals("0") || cltId.equals("")) {
            clientNameReq = nm;
            cltId = "";
            conId = "";
            siteId = "";
        } else {

            clientNameReq = cltId;
            nm = "";

            if (!conId.equals("")) {
                cnm = "";
            }

            if (!siteId.equals("")) {
                snm = "";
            }


        }


        String countryname, statename;
        countryname = auto_quote_country.getText().toString().trim();
        statename = auto_quote_state.getText().toString().trim();
        if (ctry_id.equals("")) {
            ctry_id = add_quote_pi.cntryId(countryname);
            state_id = add_quote_pi.statId(ctry_id, statename);
        }

        if (!quote_client_checkBox.isChecked()) {
            clientForFuture = contactForFuture = siteForFuture = 0;
        }
        if (quote_contact_checkBox.isChecked()) {
            contactForFuture = 1;
        }
        if (quote_site_checkBox.isChecked()) {
            siteForFuture = 1;
        }
        callAddQuotes(clientNameReq, quote_adrs_edt.getText().toString().trim());
    }


    private void SelectStartDate(String tag) {
        myCalendar = Calendar.getInstance();
        int year = myCalendar.get(Calendar.YEAR);
        int month = myCalendar.get(Calendar.MONTH);
        int dayOfMonth = myCalendar.get(Calendar.DAY_OF_MONTH);
        DatePickerDialog datePickerDialog = new DatePickerDialog(AddQuotes_Activity.this, datePickerListener, year, month, dayOfMonth);
        datePickerDialog.getDatePicker().setTag(tag);
        datePickerDialog.show();
    }

    @Override
    public void setJobServiceslist(List<JobTitle> jobServiceslist) {
        if (jtIdList != null && jtIdList.size() > 0 && jobServiceslist != null) {
            for (JobTitle jt : jobServiceslist)
                for (String selected : jtIdList)
                    if (jt.getJtId().equals(selected))
                        jt.setSelect(true);

        }
        this.datastr.addAll(jobServiceslist);
        id_array = new String[jobServiceslist.size()];

    }

    private void jobServiceSelected(Set<String> data) {
        if (data.size() >= 4) {//show count when more than 4 services selected
            quote_title_set.setText(LanguageController.getInstance().getMobileMsgByKey(AppConstant.items_selected) + " " + data.size());
        } else {
            quote_title_set.setText(data.toString().replace("[", "").replace("]", ""));
        }
        if (data.size() > 0) {
            quote_title_hint_tv.setVisibility(View.VISIBLE);
        } else {
            quote_title_hint_tv.setVisibility(View.INVISIBLE);
        }

    }

    private void callAddQuotes(String clientNameReq, String adr) {
        HyperLog.i("", "callAddQuotes(M) Start");

        if (!conditionCheck(invDate, dueDate)) {
            EotApp.getAppinstance().
                    showToastmsg(LanguageController.getInstance().getMobileMsgByKey(AppConstant.quotes_end_start_time));
        } else if (add_quote_pi.requiredFileds(jtIdList, clientNameReq, adr, ctry_id, state_id, quote_mob_no_edt.getText().toString().trim(), quote_at_mob_edt.getText().toString().trim(), quote_email_edt.getText().toString().trim())) {

            String terms = "";
            String status = "1";
            if (!TextUtils.isEmpty(quote_term_edt.getText().toString())) {
                terms = quote_term_edt.getText().toString().replace("\n", "<br>");
            }

            try {
                if (isUpdate) {
                    if (spinner_status.getSelectedItemPosition() == 3)
                        status = "8";
                    else status = spinner_status.getSelectedItemPosition() + 1 + "";
                }
            } catch (Exception e) {
                e.printStackTrace();
                HyperLog.i("", "callAddQuotes(M) Exception:" + e.toString());

            }

            final Add_Quote_ReQ add_quote_reQ = new Add_Quote_ReQ(jtIdList, cltId, siteId, conId,
                    "",
                    App_preference.getSharedprefInstance().getLoginRes().getUsrId(), quote_instr_edt.getText().toString().trim(),
                    nm, cnm, snm, quote_email_edt.getText().toString().trim(), quote_mob_no_edt.getText().toString().trim(), quote_at_mob_edt.getText().toString().trim(),
                    adr, quote_city_edt.getText().toString().trim(), state_id, ctry_id, quote_post_code_edt.getText().toString().trim(), clientForFuture, contactForFuture,
                    siteForFuture, invDate, dueDate, quote_notes_edt.getText().toString().trim()
                    , assignByUser, quotId, invId, terms, status,
                    LatLngSycn_Controller.getInstance().getLat(),
                    LatLngSycn_Controller.getInstance().getLng());

            add_quote_reQ.setAppId(appId);

            final ArrayList links = new ArrayList();
            final List<String> fileNames = new ArrayList<>();
            String jobDescription = mEditor.getHtml();
            int count = 0;
            if (!TextUtils.isEmpty(jobDescription)) {
                Elements srcs = Jsoup.parse(jobDescription).select("[src]"); //get All tags containing "src"
                for (int i = 0; i < srcs.size(); i++) {
                    if (srcs.get(i).attributes() != null) {
                        String link = null;
                        link = srcs.get(i).attributes().get("src");// get links of selected tags
                        if (isUpdate && quotesDetails != null) {
                            if (!link.contains("http")) {
                                jobDescription = jobDescription.replace(link, "_jobAttSeq_" + count + "_");
                                count++;
                                links.add(link);
                                String filename = link.substring(link.lastIndexOf("/") + 1);
                                fileNames.add(filename);
                            } else {
                                jobDescription = jobDescription.replace(link, link);
                            }

                        } else {
                            links.add(link);
                            String filename = link.substring(link.lastIndexOf("/") + 1);
                            fileNames.add(filename);
                            jobDescription = jobDescription.replace(link, "_jobAttSeq_" + count + "_");
                            count++;
                        }
                    }

                }

                add_quote_reQ.setDes("<p>" + jobDescription + "</p>");

            }

            if (links != null && links.size() > 0) {
                if (AppUtility.isInternetConnected()) {
//                    if (status.equals("2")) {
                    if (App_preference.getSharedprefInstance().getLoginRes().getQuotMailConfimation() != null) {
                        if (App_preference.getSharedprefInstance().getLoginRes().getQuotMailConfimation().contains("1") && status.equals("2")) {
                            AppUtility.alertDialog2(this,
                                    "",
                                    LanguageController.getInstance().getMobileMsgByKey(AppConstant.send_client_mail),
                                    LanguageController.getInstance().getMobileMsgByKey(AppConstant.yes),
                                    LanguageController.getInstance().getMobileMsgByKey(AppConstant.no), new Callback_AlertDialog() {
                                        @Override
                                        public void onPossitiveCall() {
                                            add_quote_reQ.setIsMailSentToClt("1");
                                            add_quote_pi.addQuoteWithDocuments(add_quote_reQ, links, fileNames);
                                        }

                                        @Override
                                        public void onNegativeCall() {
                                            add_quote_reQ.setIsMailSentToClt("0");
                                            add_quote_pi.addQuoteWithDocuments(add_quote_reQ, links, fileNames);
                                        }

                                    });
                        } else {
                            add_quote_reQ.setIsMailSentToClt("1");
                            add_quote_pi.addQuoteWithDocuments(add_quote_reQ, links, fileNames);
                        }
                    } else {
                        add_quote_reQ.setIsMailSentToClt("1");
                        add_quote_pi.addQuoteWithDocuments(add_quote_reQ, links, fileNames);
                    }

                } else {
                    String title = "", msg = "";
                    title = (isUpdate && quotesDetails != null) ? LanguageController.getInstance().getMobileMsgByKey(AppConstant.update_new_quotes) : LanguageController.getInstance().getMobileMsgByKey(AppConstant.add_new_auotes);
                    msg = (isUpdate && quotesDetails != null) ? LanguageController.getInstance().getMobileMsgByKey(AppConstant.update_new_quotes) : LanguageController.getInstance().getMobileMsgByKey(AppConstant.add_quotes_offline_msg);
                    AppUtility.alertDialog2(AddQuotes_Activity.this,
                            title,
                            msg,
                            LanguageController.getInstance().getMobileMsgByKey(AppConstant.yes),
                            LanguageController.getInstance().getMobileMsgByKey(AppConstant.no), new Callback_AlertDialog() {
                                @Override
                                public void onPossitiveCall() {
                                    String des = add_quote_reQ.getDes();
                                    if (!TextUtils.isEmpty(des))
                                        add_quote_reQ.setDes(Html.fromHtml(des).toString());
                                    showDialogForSendMailToClt(add_quote_reQ);

//                                    add_quote_pi.addQuotes(add_quote_reQ);
                                }

                                @Override
                                public void onNegativeCall() {

                                }
                            });
                }
            } else {
                showDialogForSendMailToClt(add_quote_reQ);
                //   add_quote_pi.addQuotes(add_quote_reQ);
            }
            HyperLog.i("", "callAddQuotes(M) completed");
        }
    }

    private void showDialogForSendMailToClt(final Add_Quote_ReQ add_quote_reQ) {
        //                                    add_quote_pi.addQuotes(add_quote_reQ);

        if (App_preference.getSharedprefInstance().getLoginRes().getQuotMailConfimation() != null) {
            if (App_preference.getSharedprefInstance().getLoginRes().getQuotMailConfimation().contains("1") && add_quote_reQ.getStatus().equals("2")) {
                AppUtility.alertDialog2(this,
                        "",
                        LanguageController.getInstance().getMobileMsgByKey(AppConstant.send_client_mail),
                        LanguageController.getInstance().getMobileMsgByKey(AppConstant.yes),
                        LanguageController.getInstance().getMobileMsgByKey(AppConstant.no), new Callback_AlertDialog() {
                            @Override
                            public void onPossitiveCall() {
                                add_quote_reQ.setIsMailSentToClt("1");
                                add_quote_pi.addQuotes(add_quote_reQ);

                            }

                            @Override
                            public void onNegativeCall() {
                                add_quote_reQ.setIsMailSentToClt("0");
                                add_quote_pi.addQuotes(add_quote_reQ);
                            }

                        });
            } else {
                add_quote_reQ.setIsMailSentToClt("1");
                add_quote_pi.addQuotes(add_quote_reQ);
            }
        } else {
            add_quote_reQ.setIsMailSentToClt("1");
            add_quote_pi.addQuotes(add_quote_reQ);
        }

    }

    @Override
    public void onUpdateQuote() {
        finish();
    }

    @Override
    public void onAddNewQuotes(String quotId, String lable) {
        Intent intent = new Intent();
        intent.putExtra("quotId", quotId);
        intent.putExtra("label", lable);
        setResult(RESULT_OK, intent);
        this.finish();
    }

    @Override
    public void finishActivity() {
        this.finish();
    }

    @Override
    public void errorMsg(String error) {
        showErrorDialog(error);
    }

    @Override
    public void setTermsConditions(String termsConditions) {
        if (termsConditions != null) {
            quote_term_edt.setText(Html.fromHtml(termsConditions));
        }
    }

    @Override
    public void onSessionExpire(String msg) {
        AppUtility.alertDialog(this, LanguageController.getInstance().getMobileMsgByKey(AppConstant.dialog_error_title), msg, LanguageController.getInstance().getMobileMsgByKey(AppConstant.ok), "", new Callable<Boolean>() {
            @Override
            public Boolean call() throws Exception {
                EotApp.getAppinstance().sessionExpired();
                return null;
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            onBackPressed();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void setCountryError(String errorMsg) {
        showErrorDialog(errorMsg);
    }

    @Override
    public void setStateError(String errorMsg) {
        showErrorDialog(errorMsg);
    }

    @Override
    public void setquoteTypeError(String errorMsg) {
        showErrorDialog(errorMsg);
    }

    private void showErrorDialog(String errorMsg) {
        AppUtility.error_Alert_Dialog(this, errorMsg, LanguageController.getInstance().getMobileMsgByKey(AppConstant.ok)
                , new Callable<Boolean>() {
                    @Override
                    public Boolean call() throws Exception {
                        return null;
                    }
                });
    }

    @Override
    public void setAddr_Error(String errorMsg) {
        showErrorDialog(errorMsg);
    }

    @Override
    public void setClientNameError(String errorMsg) {
        showErrorDialog(errorMsg);
    }

    @Override
    public void setMobError(String errorMsg) {
        showErrorDialog(errorMsg);
    }

    @Override
    public void setEmailError(String errorMsg) {
        showErrorDialog(errorMsg);
    }

    @Override
    public void setCountryList(List<Country> countryList) {
        AppUtility.autocompletetextviewPopUpWindow(auto_quote_country);
        final FilterCountry countryAdapter = new FilterCountry(this, R.layout.custom_adapter_item_layout, (ArrayList<Country>) countryList);
        auto_quote_country.setAdapter(countryAdapter);
        auto_quote_country.setThreshold(1);
        auto_quote_country.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                ctry_id = ((Country) adapterView.getItemAtPosition(i)).getId();
                add_quote_pi.getStateList(((Country) adapterView.getItemAtPosition(i)).getId());
                quote_country_layout.setHintEnabled(true);
            }
        });

        auto_quote_country.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (charSequence.length() >= 1) {
                    quote_country_layout.setHintEnabled(true);
                } else if (charSequence.length() <= 0) {
                    quote_country_layout.setHintEnabled(false);
                }
                auto_quote_state.setText("");
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
        auto_quote_state.setAdapter(stateAdapter);
        auto_quote_state.setThreshold(0);
        auto_quote_state.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                state_id = ((States) adapterView.getItemAtPosition(i)).getId();
                quote_state_layout.setHintEnabled(true);
            }
        });


        auto_quote_state.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (charSequence.length() >= 1) {
                    quote_state_layout.setHintEnabled(true);
                } else if (charSequence.length() <= 0) {
                    quote_state_layout.setHintEnabled(false);
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
    }

    @Override
    public void setClientList(final List<Client> clientList) {
        AppUtility.autocompletetextviewPopUpWindow(auto_quote_client);
        FilterAdapter filter = new FilterAdapter(this, R.layout.custom_adapter_item_layout, (ArrayList<Client>) clientList);
        auto_quote_client.setAdapter(filter);
        auto_quote_client.setThreshold(2);
        auto_quote_client.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (filterContact != null) {
                    filterContact.clearListData();
                    filterContact.notifyDataSetInvalidated();
                }
                nm = charSequence.toString();
                if (charSequence.length() >= 1) {
                    client_quote_layout.setHintEnabled(true);
                    /****Visible on 3 CHaracter's***/
                    if (charSequence.length() >= 3) {
                        quote_client_checkBox.setVisibility(View.VISIBLE);
                        /****Default check On when Admin Allow permission for Save for future use***/
                        if (App_preference.getSharedprefInstance().getLoginRes().getIsClientForFutureEnable() != null) {
                            quote_client_checkBox.setChecked(App_preference.getSharedprefInstance().getLoginRes().getIsClientForFutureEnable().equals("1"));
                            clientForFuture = contactForFuture = siteForFuture = 1;
                        } else {
                            clientForFuture = contactForFuture = siteForFuture = 0;
                        }

                        addNewClient();
                    }


                } else if (charSequence.length() <= 0) {
                    addNewClient();
                    client_quote_layout.setHintEnabled(false);
                    site_quote_layout.setHintEnabled(false);
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {
            }
        });

        auto_quote_client.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                quote_client_checkBox.setVisibility(View.GONE);
                quote_client_checkBox.setChecked(false);

                auto_sites.setFocusableInTouchMode(true);
                auto_sites.setFocusable(true);
                auto_sites.setText("");

                auto_quote_contact.setFocusableInTouchMode(true);
                auto_quote_contact.setFocusable(true);
                auto_quote_contact.setText("");

                cltId = (((Client) adapterView.getItemAtPosition(i)).getCltId());
                nm = "";
                add_quote_pi.getContactList(((Client) adapterView.getItemAtPosition(i)).getCltId());
                add_quote_pi.getSilteList(((Client) adapterView.getItemAtPosition(i)).getCltId());
            }
        });
    }

    private void addNewClient() {
        if (nm.length() > 0) {
            snm = "self";
            cnm = "self";
            auto_sites.setText(snm);
            auto_quote_contact.setText(cnm);

            cltId = "";
            quote_site_checkBox.setVisibility(View.GONE);
            quote_contact_checkBox.setVisibility(View.GONE);
            newContact();
            newSite();
            site_quote_layout.setHintEnabled(true);
            auto_sites.dismissDropDown();
            contact_quote_layout.setHintEnabled(true);
            auto_quote_contact.dismissDropDown();
            site_dp_img.setClickable(false);
            contact_dp_img.setClickable(false);

        } else {
            quote_client_checkBox.setVisibility(View.GONE);
            auto_sites.setText("");
            auto_quote_contact.setText("");
            quote_client_checkBox.setChecked(false);
            auto_sites.setFocusableInTouchMode(true);
            auto_quote_contact.setFocusableInTouchMode(true);
            clientForFuture = contactForFuture = siteForFuture = 0;
        }
    }

    @Override
    public void setContactList(final List<ContactData> contactList) {
        //if(contactList.size()>0) {
        AppUtility.autocompletetextviewPopUpWindow(auto_quote_contact);
        for (ContactData contactData : contactList) {
            if (contactData.getDef().equals("1")) { //set contact value default for new Quotes Add
                auto_quote_contact.setText(contactData.getCnm());
                setContactDefaultData(contactData);
                contact_quote_layout.setHintEnabled(true);
                selectedContactData = contactData;
                break;
            }
        }

        filterContact = new FilterAdapterContact(this, R.layout.custom_adapter_item_layout, (ArrayList<ContactData>) contactList);
        auto_quote_contact.setAdapter(filterContact);
        auto_quote_contact.setThreshold(0);

        auto_quote_contact.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                ContactData contactData = (ContactData) ((ListView) adapterView).getAdapter().getItem(i);
                selectedContactData = contactData;
                setContactDefaultData(contactData);
            }
        });
        auto_quote_contact.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (!contactList.contains(charSequence)) {
                    if (charSequence.length() > 0) {
                        cnm = charSequence.toString();
                        if (cltId.equals("") || cltId.equals("0"))
                            quote_contact_checkBox.setVisibility(View.GONE);
                        else
                            quote_contact_checkBox.setVisibility(View.VISIBLE);
                        newContact();
                    } else {
                        quote_contact_checkBox.setVisibility(View.GONE);
                        quote_contact_checkBox.setChecked(false);
                    }
                }

                if (charSequence.length() >= 1) {
                    contact_quote_layout.setHintEnabled(true);
                } else if (charSequence.length() <= 0) {
                    contact_quote_layout.setHintEnabled(false);
                }


            }

            @Override
            public void afterTextChanged(Editable editable) {
            }
        });

        // }
         /* else if (contactList.size() == 0) {
            quote_contact_checkBox.setVisibility(View.GONE);
            auto_quote_contact.setFocusableInTouchMode(false);
        }*/
    }

    void setContactDefaultData(ContactData contactData) {
        quote_contact_checkBox.setVisibility(View.GONE);
        conId = contactData.getConId();
        contact_dp_img.setClickable(true);
        try {
            if (contactData.getMob1() != null && contactData.getMob1().length() == 0
                    && App_preference.getSharedprefInstance().getLoginRes().getCtryCode() != null) {
                quote_mob_no_edt.setText(App_preference.getSharedprefInstance().getLoginRes().getCtryCode());
            } else {
                quote_mob_no_edt.setText(contactData.getMob1());
            }
            if (contactData.getMob2() != null && contactData.getMob2().length() == 0
                    && App_preference.getSharedprefInstance().getLoginRes().getCtryCode() != null) {
                quote_at_mob_edt.setText(App_preference.getSharedprefInstance().getLoginRes().getCtryCode());
            } else {
                quote_at_mob_edt.setText(contactData.getMob2());
            }
        } catch (Exception ex) {
            ex.getMessage();
            quote_mob_no_edt.setText(contactData.getMob1());
            quote_at_mob_edt.setText(contactData.getMob2());
        }

        quote_email_edt.setText(contactData.getEmail());
    }

    void newContact() {
        quote_mob_no_edt.setText("");
        quote_at_mob_edt.setText("");
        quote_email_edt.setText("");
        conId = "";
    }

    @Override
    public void setSiteList(final List<Site_model> siteList) {
        //if(siteList.size()>0){

        AppUtility.autocompletetextviewPopUpWindow(auto_sites);

        for (Site_model siteData : siteList) {////set site default values for new Quotes Add
            if (siteData.getDef().equals("1")) {
                auto_sites.setText(siteData.getSnm());
                setSitetDefaultData(siteData);
                site_quote_layout.setHintEnabled(true);
                selectedSiteData = siteData;
                break;
            }
        }

        FilterAdapterSites filterSites = new FilterAdapterSites(this, R.layout.custom_adapter_item_layout, (ArrayList<Site_model>) siteList);
        auto_sites.setAdapter(filterSites);
        auto_sites.setThreshold(0);
        auto_sites.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Site_model site_model = (Site_model) (adapterView.getAdapter().getItem(i));
                selectedSiteData = site_model;
                setSitetDefaultData(site_model);
            }
        });
        auto_sites.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (!siteList.contains(charSequence)) {
                    if (charSequence.length() > 0) {
                        snm = charSequence.toString();
                        if (cltId.equals("") || cltId.equals("0"))
                            quote_site_checkBox.setVisibility(View.GONE);
                        else
                            quote_site_checkBox.setVisibility(View.VISIBLE);
                        newSite();
                    } else {
                        quote_site_checkBox.setVisibility(View.GONE);
                        quote_site_checkBox.setChecked(false);
                    }
                }

                if (charSequence.length() >= 1) {
                    site_quote_layout.setHintEnabled(true);
                } else if (charSequence.length() <= 0) {
                    site_quote_layout.setHintEnabled(false);
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {
            }
        });
        // }
        /*else if (siteList.size() == 0) {
            quote_site_checkBox.setVisibility(View.GONE);
            auto_sites.setFocusableInTouchMode(false);
        }*/
    }

    private void setSitetDefaultData(Site_model sitetData) {
        quote_site_checkBox.setVisibility(View.GONE);
        siteId = sitetData.getSiteId();
        quote_adrs_edt.setText(sitetData.getAdr());
        auto_quote_country.setText(SpinnerCountrySite.getCountryNameById(sitetData.getCtry()));
        auto_quote_state.setText(SpinnerCountrySite.getStatenameById((sitetData.getCtry()), sitetData.getState()));
        quote_state_layout.setHintEnabled(true);
        quote_post_code_edt.setText(sitetData.getZip());
        quote_city_edt.setText((sitetData.getCity()));
        site_dp_img.setClickable(true);
        if (SpinnerCountrySite.getStatenameById((sitetData.getCtry()), sitetData.getState()).length() >= 0)
            site_quote_layout.setHintEnabled(true);
        else site_quote_layout.setHintEnabled(true);


    }

    private void newSite() {
        //auto_quote_country.setText("");
        // auto_quote_state.setText("");
        quote_post_code_edt.setText("");
        quote_city_edt.setText("");
        quote_adrs_edt.setText("");
        siteId = "";


    }

    private String getDate(String dateSplit) {
        String[] date = dateSplit.split(" ");
        return date[0];
    }

    @Override
    public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

    }

    @Override
    public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

        if (charSequence.length() >= 1) {

//            if (charSequence.hashCode() == quote_desc_edt.getText().hashCode())
//                quote_desc_layout.setHintEnabled(true);
            if (charSequence.hashCode() == quote_instr_edt.getText().hashCode())
                quote_instr_layout.setHintEnabled(true);

            if (charSequence.hashCode() == quote_mob_no_edt.getText().hashCode())
                quote_mobile_layout.setHintEnabled(true);
            if (charSequence.hashCode() == quote_at_mob_edt.getText().hashCode())
                quote_mob2_layout.setHintEnabled(true);
            if (charSequence.hashCode() == quote_email_edt.getText().hashCode())
                quote_email_layout.setHintEnabled(true);
            if (charSequence.hashCode() == quote_adrs_edt.getText().hashCode())
                quote_adr_layout.setHintEnabled(true);
            if (charSequence.hashCode() == quote_city_edt.getText().hashCode())
                quote_city_layout.setHintEnabled(true);
            if (charSequence.hashCode() == quote_post_code_edt.getText().hashCode())
                quote_postal_layout.setHintEnabled(true);
            if (charSequence.hashCode() == quote_notes_edt.getText().hashCode())
                quote_notes_layout.setHintEnabled(true);
            if (charSequence.hashCode() == quote_term_edt.getText().hashCode())
                quote_term_layout.setHintEnabled(true);

        } else if (charSequence.length() <= 0) {

//            if (charSequence.hashCode() == quote_desc_edt.getText().hashCode())
//                quote_desc_layout.setHintEnabled(false);
            if (charSequence.hashCode() == quote_instr_edt.getText().hashCode())
                quote_instr_layout.setHintEnabled(false);

            if (charSequence.hashCode() == quote_mob_no_edt.getText().hashCode())
                quote_mobile_layout.setHintEnabled(false);
            if (charSequence.hashCode() == quote_at_mob_edt.getText().hashCode())
                quote_mob2_layout.setHintEnabled(false);
            if (charSequence.hashCode() == quote_email_edt.getText().hashCode())
                quote_email_layout.setHintEnabled(false);
            if (charSequence.hashCode() == quote_adrs_edt.getText().hashCode())
                quote_adr_layout.setHintEnabled(false);
            if (charSequence.hashCode() == quote_city_edt.getText().hashCode())
                quote_city_layout.setHintEnabled(false);
            if (charSequence.hashCode() == quote_post_code_edt.getText().hashCode())
                quote_postal_layout.setHintEnabled(false);
            if (charSequence.hashCode() == quote_notes_edt.getText().hashCode())
                quote_notes_layout.setHintEnabled(false);
            if (charSequence.hashCode() == quote_term_edt.getText().hashCode())
                quote_term_layout.setHintEnabled(false);
        }

    }

    @Override
    public void afterTextChanged(Editable editable) {

    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == 145) {
            if (grantResults != null && grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                if (locationTracker != null) {
                    locationTracker.getCurrentLocation();
                }
            }
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

    }

    private void getUpdatedLocation() {
        locationTracker = new LocationTracker(this, new OnLocationUpdate() {
            @Override
            public void OnContinue(boolean isLocationUpdated, boolean isPermissionAllowed) {
                if (isPermissionAllowed) {
                    locationTracker.getCurrentLocation();
                } else {
                    ActivityCompat.requestPermissions(AddQuotes_Activity.this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION,
                                    Manifest.permission.ACCESS_COARSE_LOCATION},
                            145);
                }
            }
        });

    }

    public void setSelectedJobServices(ArrayList<JobTitle> selectedJobServices) {
        jtIdList.clear();
        Set<String> jobServiceNm = new HashSet<>();
        for (JobTitle jobTitle : selectedJobServices) {
            if (jobTitle.isSelect()) {
                jtIdList.add(jobTitle.getJtId());
                jobServiceNm.add(jobTitle.getName());
            }
        }
//        jtIdList = title_ids;
        if (jobServiceNm.size() >= 4)
            quote_title_set.setText(LanguageController.getInstance().getMobileMsgByKey(AppConstant.items_selected) + " " + jobServiceNm.size());
        else
            quote_title_set.setText(jobServiceNm.toString().replace("[", "").replace("]",
                    ""));
        if (jobServiceNm.size() > 0) {
            quote_title_hint_tv.setVisibility(View.VISIBLE);
        } else quote_title_hint_tv.setVisibility(View.INVISIBLE);
    }


}
