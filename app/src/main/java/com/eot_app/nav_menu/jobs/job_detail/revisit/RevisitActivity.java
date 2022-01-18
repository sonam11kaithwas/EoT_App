package com.eot_app.nav_menu.jobs.job_detail.revisit;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.os.Bundle;
import android.text.Editable;
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
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.eot_app.R;
import com.eot_app.custom_dropDown.CustomDPController;
import com.eot_app.nav_menu.client.client_db.Client;
import com.eot_app.nav_menu.client.clientlist.client_detail.contact.client_dao.ContactData;
import com.eot_app.nav_menu.client.clientlist.client_detail.site.sitelist.Site_model;
import com.eot_app.nav_menu.jobs.add_job.Add_job_activity;
import com.eot_app.nav_menu.jobs.add_job.AddjobView;
import com.eot_app.nav_menu.jobs.add_job.adapters.DynamicClassAdapter;
import com.eot_app.nav_menu.jobs.add_job.addjob_presenter.AddJob_pc;
import com.eot_app.nav_menu.jobs.add_job.addjob_presenter.Add_job_pi;
import com.eot_app.nav_menu.jobs.add_job.addjobmodel.AddJob_Req;
import com.eot_app.nav_menu.jobs.job_db.Job;
import com.eot_app.nav_menu.jobs.job_db.JtId;
import com.eot_app.time_shift_pkg.ShiftTimeReSModel;
import com.eot_app.utility.AppConstant;
import com.eot_app.utility.AppUtility;
import com.eot_app.utility.App_preference;
import com.eot_app.utility.Country;
import com.eot_app.utility.EotApp;
import com.eot_app.utility.States;
import com.eot_app.utility.db.AppDataBase;
import com.eot_app.utility.language_support.LanguageController;
import com.eot_app.utility.settings.contractdb.ContractRes;
import com.eot_app.utility.settings.setting_db.FieldWorker;
import com.eot_app.utility.settings.setting_db.JobTitle;
import com.eot_app.utility.settings.setting_db.TagData;
import com.eot_app.utility.util_interfaces.MySpinnerAdapter;
import com.google.android.material.textfield.TextInputLayout;
import com.google.gson.Gson;

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

/**
 * Created by Mahendra Dabi on 21-08-2020.
 */
public class RevisitActivity extends AppCompatActivity implements View.OnClickListener, TextWatcher, AddjobView, AdapterView.OnItemSelectedListener {
    final Set<String> listwork = new HashSet<>();
    private final ArrayList<JobTitle> datastr = new ArrayList<>();
    private final ArrayList<TagData> tagArray = new ArrayList<>();
    private final int i = 1;
    private final Set<String> jtIdList = new HashSet<>();
    private final int clientForFuture = 0;
    private final int siteForFuture = 0;
    private final int contactForFuture = 0;
    CustomDPController customDPController;
    private String date_str, time_str, date_en, time_en, schdlStart, schdlFinish;
    private int year, month, day, mHour, mMinute;
    private Button cancel_btn, submit_btn, date_time_clear_btn, add_tag_btn;
    private TextView date_start, time_start, date_end, time_end, schel_start, schel_end, tv_spinner, assign_to, tv_hint;
    private TextInputLayout job_desc_layout, job_instr_layout;
    private LinearLayout j_prio_linearLayout;
    private LinearLayout linearMainView, assignto_linear, linear_addTag;
    private AutoCompleteTextView members, et_tag;
    private EditText job_desc, job_instr;
    private LinearLayout jobservicelayout;
    private TextView jobservicetxtlableset, jobservicelablehint, tag_lable;
    private RelativeLayout relative_main;
    private Spinner job_priority_spinner;
    private Job job;
    private String prty = AppConstant.Prioty[1];
    private List<TagData> tagslist;
    private String[] id_array;
    private List<String> stringJobTitle;
    private String[] job_prioty;
    private Add_job_pi addJob_pc;
    private int s;
    private String tempId, dateTime;
    private String cltId = "", siteId = "", conId = "", new_cnm = "", new_site_nm = "", new_con_nm = "", ctry_id = "", state_id = "";
    private int member_type;
    private String kpr;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_revisit);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle(LanguageController.getInstance().getMobileMsgByKey(AppConstant.revisit));
        initViews();
        textInputLayoutHint();
        jobPrioritySet();
        customDPController = new CustomDPController();
        //  dateTime = AppUtility.getDateByFormat(AppConstant.DATE_TIME_FORMAT);
        setTaskPriority();

        if (getIntent().hasExtra("job")) {
            //  job = (getIntent().getParcelableExtra("job"));
            String str = getIntent().getExtras().getString("job");
            job = new Gson().fromJson(str, Job.class);
            //  job = AppDataBase.getInMemoryDatabase(EotApp.getAppinstance()).jobModel().getJobsById(job.getJobId());

            preFillForm();
        }

    }

    @Override
    public void setTimeShiftList(List<ShiftTimeReSModel> list) {

    }

    /***this use in add job view**/
    @Override
    public void setContractlist(List<ContractRes> contractlist) {

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

    private void initViews() {

        date_time_clear_btn = findViewById(R.id.date_time_clear_btn);
        date_time_clear_btn.setText(LanguageController.getInstance().getMobileMsgByKey(AppConstant.clear));

        schel_start = findViewById(R.id.schel_start);
        schel_start.setHint(LanguageController.getInstance().getMobileMsgByKey(AppConstant.shdl_start));

        schel_end = findViewById(R.id.schel_end);
        schel_end.setHint(LanguageController.getInstance().getMobileMsgByKey(AppConstant.shdl_end));

        date_start = findViewById(R.id.date_start);
        date_start.setHint(LanguageController.getInstance().getMobileMsgByKey(AppConstant.date_form));

        date_end = findViewById(R.id.date_end);
        date_end.setHint(LanguageController.getInstance().getMobileMsgByKey(AppConstant.date_form));

        time_start = findViewById(R.id.time_start);
        time_start.setHint(LanguageController.getInstance().getMobileMsgByKey(AppConstant.time_form));

        time_end = findViewById(R.id.time_end);
        time_end.setHint(LanguageController.getInstance().getMobileMsgByKey(AppConstant.time_form));

        et_tag = findViewById(R.id.et_tag);
        et_tag.setHint(LanguageController.getInstance().getMobileMsgByKey(AppConstant.add_tag));


        job_desc = findViewById(R.id.job_desc);
        job_desc.setHint(LanguageController.getInstance().getMobileMsgByKey(AppConstant.job_desc));

        job_instr = findViewById(R.id.job_instr);
        job_instr.setHint(LanguageController.getInstance().getMobileMsgByKey(AppConstant.job_inst));

        tv_spinner = findViewById(R.id.tv_spinner_account);

        tv_hint = findViewById(R.id.tv_hint_prioty);
        tv_hint.setHint(LanguageController.getInstance().getMobileMsgByKey(AppConstant.job_priority));

        relative_main = findViewById(R.id.relative_main);


        assign_to = findViewById(R.id.assign_to);
        assign_to.setHint(LanguageController.getInstance().getMobileMsgByKey(AppConstant.assign_to));


        job_desc_layout = findViewById(R.id.input_layout_des_job);
        job_instr_layout = findViewById(R.id.job_instr_layout);
        j_prio_linearLayout = findViewById(R.id.j_prio_linearLayout);

        job_priority_spinner = findViewById(R.id.job_priority_spinner);

        members = findViewById(R.id.members);
        members.setHint(LanguageController.getInstance().getMobileMsgByKey(AppConstant.add_fieldworker));

        linearMainView = findViewById(R.id.linearMainView);
        assignto_linear = findViewById(R.id.assignto_linear);

        tag_lable = findViewById(R.id.tag_lable);
        tag_lable.setHint(LanguageController.getInstance().getMobileMsgByKey(AppConstant.tag));

        linear_addTag = findViewById(R.id.linear_addTag);
        add_tag_btn = findViewById(R.id.add_tag_btn);
        add_tag_btn.setText(LanguageController.getInstance().getMobileMsgByKey(AppConstant.add));

        jobservicelayout = findViewById(R.id.jobservicelayout);
        jobservicetxtlableset = findViewById(R.id.jobservicetxtlableset);
        jobservicetxtlableset.setHint(LanguageController.getInstance().getMobileMsgByKey(AppConstant.job_title) + " *");
        jobservicelablehint = findViewById(R.id.jobservicelablehint);
        jobservicelablehint.setHint(LanguageController.getInstance().getMobileMsgByKey(AppConstant.job_title) + " *");
        jobservicelayout.setOnClickListener(this);


        submit_btn = findViewById(R.id.submit_btn);
        submit_btn.setText(LanguageController.getInstance().getMobileMsgByKey(AppConstant.revisit));

        date_time_clear_btn.setOnClickListener(this);
        submit_btn.setOnClickListener(this);

        j_prio_linearLayout.setOnClickListener(this);


        date_start.setOnClickListener(this);
        date_end.setOnClickListener(this);
        time_start.setOnClickListener(this);
        time_end.setOnClickListener(this);
        assignto_linear.setOnClickListener(this);
        tag_lable.setOnClickListener(this);
        add_tag_btn.setOnClickListener(this);

        stringJobTitle = new ArrayList<>();
        addJob_pc = new AddJob_pc(this);
        addJob_pc.getTagDataList();
        addJob_pc.getWorkerList();
        addJob_pc.getCountryList();


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


    private void preFillForm() {
        if (job != null) {

            cltId = job.getCltId();
            siteId = job.getSiteId();
            conId = job.getConId();

            new_cnm = job.getNm();
            new_con_nm = job.getCnm();

            new_site_nm = job.getSnm();

            ctry_id = job.getCtry();
            state_id = job.getState();
            addJob_pc.getStateList(ctry_id);
            try {
                tv_hint.setVisibility(View.VISIBLE);

                if (job.getPrty().equals("1"))
                    tv_spinner.setText(LanguageController.getInstance().getMobileMsgByKey(AppConstant.Low));
                else if (job.getPrty().equals("2"))
                    tv_spinner.setText(LanguageController.getInstance().getMobileMsgByKey(AppConstant.medium));
                else
                    tv_spinner.setText(LanguageController.getInstance().getMobileMsgByKey(AppConstant.High));
                prty = job.getPrty();
            } catch (Exception e) {
                e.printStackTrace();
            }

            if (!TextUtils.isEmpty(job.getInst()))
                job_instr.setText(job.getInst());

            if (!TextUtils.isEmpty(job.getDes()))
                job_desc.setText(job.getDes());


            setScheduleDates();

            Set<String> data = new HashSet<>();
            for (JtId jtId : job.getJtId()) {
                data.add(jtId.getTitle());
                jtIdList.add(jtId.getJtId());
            }
            jobServiceSelected(data);
            addJob_pc.getJobTitleList();
            setFwList();


            List<TagData> tagData = job.getTagData();
            if (tagData != null) {
                for (TagData td : tagData)
                    addChipsForTags(td);

                linear_addTag.setVisibility(View.VISIBLE);
            }


        }
    }

    private void jobServiceSelected(Set<String> data) {
        if (data.size() >= 4) {//show count when more than 4 services selected
            jobservicetxtlableset.setText(LanguageController.getInstance().getMobileMsgByKey(AppConstant.items_selected) + " " + data.size());
        } else {
            jobservicetxtlableset.setText(data.toString().replace("[", "").replace("]", ""));
        }
        if (data.size() > 0) {
            jobservicelablehint.setVisibility(View.VISIBLE);
        } else {
            jobservicelablehint.setVisibility(View.INVISIBLE);
        }

    }

    private void setFwList() {
        try {
            String kpr = job.getKpr();
            String[] kprList = kpr.split(",");
            for (String id : kprList) {
                FieldWorker fieldWorker = AppDataBase.getInMemoryDatabase(EotApp.getAppinstance()).fieldWorkerModel().getFieldWorkerByID(id);
                if (fieldWorker != null) {
                    addChips(fieldWorker);
                    linearMainView.setVisibility(View.VISIBLE);
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void textInputLayoutHint() {
        AppUtility.setupUI(relative_main, RevisitActivity.this);

        job_desc_layout.getEditText().addTextChangedListener(this);
        job_instr_layout.getEditText().addTextChangedListener(this);

    }

    //set  Job Priority by default medium
    private void setTaskPriority() {
        tv_hint.setVisibility(View.VISIBLE);
        tv_spinner.setText(prty);
        prty = String.valueOf(2);
    }

    private void setScheduleDates() {
      /*  try {
            Calendar instance = Calendar.getInstance();
            instance.setTimeInMillis(System.currentTimeMillis());
            instance.add(Calendar.DAY_OF_MONTH, 1);

            String dateFormat = AppUtility.getDateWithFormate(instance.getTimeInMillis()/1000, "dd-MM-yyyy");
            date_start.setText(dateFormat);
            date_str = dateFormat;

            String timeFormat = AppUtility.getDateWithFormate(instance.getTimeInMillis()/1000, "hh:mm a");
            time_start.setText(timeFormat);
            time_str = timeFormat;


            instance.add(Calendar.HOUR_OF_DAY,3);
            timeFormat = AppUtility.getDateWithFormate(instance.getTimeInMillis()/1000, "hh:mm a");
            time_end.setText(timeFormat);
            time_en = timeFormat;

            dateFormat = AppUtility.getDateWithFormate(instance.getTimeInMillis()/1000, "dd-MM-yyyy");
            date_end.setText(dateFormat);
            date_en = dateFormat;
        } catch (Exception e) {
            e.printStackTrace();
        }
*/
        if (job != null && !TextUtils.isEmpty(job.getSchdlStart())) {
            try {
                long longStartTime = Long.parseLong(job.getSchdlStart());
                Calendar instance = Calendar.getInstance();
                instance.setTimeInMillis(longStartTime * 1000);
                instance.add(Calendar.DATE, 1);
                String timeFormat = AppUtility.getDateWithFormate(instance.getTimeInMillis() / 1000,
                        AppUtility.dateTimeByAmPmFormate("hh:mm a", "kk:mm"));
                time_start.setText(timeFormat);
                time_str = timeFormat;

                String dateFormat = AppUtility.getDateWithFormate(instance.getTimeInMillis() / 1000, "dd-MM-yyyy");
                date_start.setText(dateFormat);
                date_str = dateFormat;

                long endTime = Long.parseLong(job.getSchdlFinish());
                instance.setTimeInMillis(endTime * 1000);
                instance.add(Calendar.DATE, 1);
                timeFormat = AppUtility.getDateWithFormate(instance.getTimeInMillis() / 1000,
                        AppUtility.dateTimeByAmPmFormate("hh:mm a", "kk:mm"));
                time_end.setText(timeFormat);
                time_en = timeFormat;

                dateFormat = AppUtility.getDateWithFormate(instance.getTimeInMillis() / 1000, "dd-MM-yyyy");
                date_end.setText(dateFormat);
                date_en = dateFormat;
            } catch (Exception ex) {

            }
        }
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


    private void addChips(final FieldWorker itemAtPosition) {//add chip for fieldworker to assign job
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
                    linearMainView.removeView((LinearLayout) view.getParent());
                    if (linearMainView.getChildCount() <= 0) {
                        linearMainView.setVisibility(View.GONE);
                    }
                    listwork.remove(itemAtPosition.getUsrId());
                }
            });


            linearMainView.addView(v, 0, new ViewGroup.LayoutParams(ViewGroup.LayoutParams.FILL_PARENT, ViewGroup.LayoutParams.FILL_PARENT));

        } catch (Exception ex) {
            ex.printStackTrace();
        }
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
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home)
            onBackPressed();
        return true;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.date_time_clear_btn:
                setDateTimeEmptyTime();
                break;

            case R.id.button_cancel:
                onBackPressed();
                break;

            case R.id.submit_btn:
                revisitJob();
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
            case R.id.j_prio_linearLayout:
                job_priority_spinner.performClick();
                break;

            case R.id.assignto_linear:
                members.showDropDown();
                break;
            case R.id.tag_lable:
                et_tag.showDropDown();
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
        }

    }

    private void revisitJob() {
        if (job != null) {

            String clientReq = "";
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

            if (TextUtils.isEmpty(date_str)) { //remove space from schdlFinish & schdlStart
                schdlStart = schdlFinish = "";
            } else {
                schdlStart = date_str + " " + time_str;
                schdlFinish = date_en + " " + time_en;
            }

            new_con_nm = "";
            if (new_con_nm.equals("")) {
                // new_con_nm = new_cnm;
                new_con_nm = "Self";
                //   auto_contact.setText(new_con_nm);
                conId = "";
            }
            if (!conId.equals("")) {
                new_con_nm = "";
            }


            if (!siteId.equals("")) {
                new_site_nm = "";
            }
            String adr = job.getAdr();

            new_site_nm = "";
            if (!new_site_nm.equals("")) {
                siteId = "";
            }

            if (!cltId.equals("")) {
                clientReq = cltId;
            } else if (!new_cnm.equals("")) {
                clientReq = new_cnm;
            }

            if (!TextUtils.isEmpty(cltId))
                new_cnm = "";

            if (schdlStart.isEmpty()) {
                callAddJob(clientReq, adr);


            } else {


                if (!conditionCheck(schdlStart, schdlFinish)) {
                    EotApp.getAppinstance().
                            showToastmsg(LanguageController.getInstance().getMobileMsgByKey(AppConstant.Sche_end_start_time));
                } else {
                    callAddJob(clientReq, adr);
                }
            }


        }
    }

    private void callAddJob(String clientReq, String adr) {
        if (addJob_pc.RequiredFieldsForRiviste(clientReq, adr, this.jtIdList, ctry_id, state_id, job.getMob1(),
                job.getMob1(), job.getEmail())) {
            if (!schdlStart.equals("") && (App_preference.getSharedprefInstance().getLoginRes().getIs24hrFormatEnable()
                    != null && App_preference.getSharedprefInstance().getLoginRes().getIs24hrFormatEnable().equals("1"))) {
                Date startDate = null, endDate = null;
                String[] words = time_str.split(":");
                int t1 = Integer.valueOf(words[0]);
                String[] words2 = time_str.split(":");
                int t2 = Integer.valueOf(words2[0]);
                try {
                    if (t1 != 12) {
                        startDate = new SimpleDateFormat("hh:mm", Locale.getDefault()).parse(time_str);
                        time_str = "";
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

            String contractId = "";
            if (!TextUtils.isEmpty(job.getContrId()))
                contractId = job.getContrId();
            AddJob_Req addJob_req = new AddJob_Req(tempId + 1,
                    Integer.parseInt(job.getJobId()),
                    contractId,
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
                    job.getMob1(),
                    job.getMob2(),
                    job.getEmail(),
                    adr,
                    job.getCity(),
                    ctry_id,
                    state_id,
                    job.getZip(),
                    member_type,
                    listwork,
                    schdlStart,
                    schdlFinish,
                    tagArray,
                    App_preference.getSharedprefInstance().getLoginRes().getUsrId(),
                    kpr,
                    AppUtility.getDateByFormat(AppConstant.DATE_TIME_FORMAT),
                    job.getLat(), job.getLng(),
                    job.getLandmark(), ""
            );

            Log.d("", "");
            addJob_pc.callApiForAddJob(addJob_req);
        }
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence charSequence, int start, int before, int count) {
        /**Floating hint enbale after text enter**/
        if (charSequence.length() >= 1) {
            if (charSequence.hashCode() == job_desc.getText().hashCode())
                job_desc_layout.setHintEnabled(true);
            if (charSequence.hashCode() == job_instr.getText().hashCode())
                job_instr_layout.setHintEnabled(true);


        } else if (charSequence.length() <= 0) {
            /**Floating hint Disable after text enter**/
            if (charSequence.hashCode() == job_desc.getText().hashCode())
                job_desc_layout.setHintEnabled(false);
            if (charSequence.hashCode() == job_instr.getText().hashCode())
                job_instr_layout.setHintEnabled(false);

        }
    }

    @Override
    public void afterTextChanged(Editable s) {

    }

    @Override
    public void SetJobTittle(ArrayList<JobTitle> jobServiceslist) {
        if (jtIdList != null && jtIdList.size() > 0 && jobServiceslist != null) {
            for (JobTitle jt : jobServiceslist)
                for (String selected : jtIdList)
                    if (jt.getJtId().equals(selected))
                        jt.setSelect(true);

        }
        this.datastr.addAll(jobServiceslist);
        id_array = new String[datastr.size()];
    }

    @Override
    public void setClientlist(List<Client> data) {

    }

    @Override
    public void setSiteList(List<Site_model> data) {

    }

    @Override
    public void setContactList(List<ContactData> data) {

    }

    @Override
    public void setCountryList(List<Country> countryList) {

    }

    @Override
    public void setStateList(List<States> statesList) {

    }

    @Override
    public void setWorkerList(List<FieldWorker> fieldWorkerlist) {
        AppUtility.autocompletetextviewPopUpWindow(members);
        members.setThreshold(1);
        DynamicClassAdapter myClassAdapter = new DynamicClassAdapter<FieldWorker>(this, R.layout.custom_adapter_item_layout, R.id.item_title_name, fieldWorkerlist);
        members.setAdapter(myClassAdapter);
        members.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                if (!listwork.contains((((FieldWorker) adapterView.getItemAtPosition(i)).getUsrId()))) {
                    addChips((FieldWorker) adapterView.getItemAtPosition(i));
                }
                linearMainView.setVisibility(View.VISIBLE);
                members.setText("");
            }
        });
    }

    @Override
    public void finishActivity() {
        onBackPressed();
    }

    @Override
    public void jobPrioritySet() {
        AppUtility.spinnerPopUpWindow(job_priority_spinner);
        job_prioty = AppConstant.Prioty;
        job_priority_spinner.setAdapter(new MySpinnerAdapter(RevisitActivity.this, job_prioty));
        job_priority_spinner.setOnItemSelectedListener(this);
    }

    @Override
    public void set_Str_DTime(String str_dt_tm, String time_str) {

    }

    @Override
    public void set_End_Date_Time(String std) {

    }

    @Override
    public void set_str_DT_after_cur(String std) {

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
    public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) {
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
    public void onNothingSelected(AdapterView<?> parent) {

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
            jobservicetxtlableset.setText(LanguageController.getInstance().getMobileMsgByKey(AppConstant.items_selected) + " " + jobServiceNm.size());
        else
            jobservicetxtlableset.setText(jobServiceNm.toString().replace("[", "").replace("]",
                    ""));
        if (jobServiceNm.size() > 0) {
            jobservicelablehint.setVisibility(View.VISIBLE);
        } else jobservicelablehint.setVisibility(View.INVISIBLE);
    }

}
