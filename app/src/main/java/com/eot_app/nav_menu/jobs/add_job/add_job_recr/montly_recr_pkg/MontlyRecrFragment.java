package com.eot_app.nav_menu.jobs.add_job.add_job_recr.montly_recr_pkg;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;

import com.eot_app.R;
import com.eot_app.nav_menu.jobs.add_job.Add_job_activity;
import com.eot_app.nav_menu.jobs.add_job.add_job_recr.daily_recr_pkg.daily_recur_model.DailyMsgReqModel;
import com.eot_app.nav_menu.jobs.add_job.add_job_recr.daily_recr_pkg.daily_recur_model.DailyMsgResModel;
import com.eot_app.nav_menu.jobs.add_job.add_job_recr.daily_recr_pkg.daily_recur_model.JobRecurModel;
import com.eot_app.nav_menu.jobs.add_job.add_job_recr.dateTime_pkg.DateTimeCallBack;
import com.eot_app.nav_menu.jobs.add_job.add_job_recr.dateTime_pkg.DateTimeDiloag;
import com.eot_app.nav_menu.jobs.add_job.add_job_recr.montly_recr_pkg.monthly_recr_mvp.Monthly_Recr_PC;
import com.eot_app.nav_menu.jobs.add_job.add_job_recr.montly_recr_pkg.monthly_recr_mvp.Monthly_Recr_PI;
import com.eot_app.nav_menu.jobs.add_job.add_job_recr.montly_recr_pkg.monthly_recr_mvp.Monthly_Recr_VIEW;
import com.eot_app.utility.AppConstant;
import com.eot_app.utility.AppUtility;
import com.eot_app.utility.EotApp;
import com.eot_app.utility.language_support.LanguageController;
import com.eot_app.utility.util_interfaces.MySpinnerAdapter;
import com.google.gson.Gson;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.concurrent.Callable;

public class MontlyRecrFragment extends Fragment implements AdapterView.OnItemSelectedListener, View.OnClickListener, CompoundButton.OnCheckedChangeListener, TextWatcher, Monthly_Recr_VIEW {
    String[] dayArray = {LanguageController.getInstance().getMobileMsgByKey(AppConstant.first),
            LanguageController.getInstance().getMobileMsgByKey(AppConstant.second),
            LanguageController.getInstance().getMobileMsgByKey(AppConstant.third),
            LanguageController.getInstance().getMobileMsgByKey(AppConstant.forth),
            LanguageController.getInstance().getMobileMsgByKey(AppConstant.last)};
    String[] weekdayArray = {LanguageController.getInstance().getMobileMsgByKey(AppConstant.check_mon), LanguageController.getInstance().getMobileMsgByKey(AppConstant.check_tues), LanguageController.getInstance().getMobileMsgByKey(AppConstant.check_wedns), LanguageController.getInstance().getMobileMsgByKey(AppConstant.check_thurs), LanguageController.getInstance().getMobileMsgByKey(AppConstant.check_friday), LanguageController.getInstance().getMobileMsgByKey(AppConstant.check_satur), LanguageController.getInstance().getMobileMsgByKey(AppConstant.check_sun)};
    private String dateNum = "";
    private int RECRMSG = 1;
    private Spinner spinnerday, spinnerweek;
    private TextView tv_spinner_day, tv_spinner_week, range_of_recurence, schel_start, occurance, recuring_pattenr;
    private LinearLayout linearLayout_day, linearLayout_week, montly_every_layout, montly_every_layout2, months_layout, ever_month_the_layout, every_img_layout, every_img_layout2;
    private ImageView every_recr_up_img, every_recr_down_img, month_up_img, month_down_img, end_after_up_img, end_after_down_img, every_month_up_img, every_month_down_img;
    private EditText every_recr_edt, month_edt, end_after_edt, every_month_edt;
    private TextView date_start, end_date_by, recur_job_days_msg, of_every, of_every_day, months_txt, months_txt_day;
    private String defaultJobDateTime = "";
    private RadioButton radio_no_end_date, radio_end_after, radio_end_by, radio_day, the_radio;
    private LinearLayout end_after_layout, end_count_layout;
    private Monthly_Recr_PI monthlyRecrPi;
    private Button submit_btn;
    private String schdlStart = "";
    private String mode = "1";
    private String endRecurMode = "0";
    private String occurances = "";
    private String numOfMonths = "";
    private String startDate = "";
    private String endDate = "";
    private String weekNum = "";
    private String dayNum = "";
    /**
     * This use for disable Previous date for Start Date picker
     ***/
    private boolean StartRecrCheck;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.montly_recr_layout, container, false);


        Bundle bundle = getArguments();

        if (bundle != null) {
            defaultJobDateTime = bundle.getString("dateTime");
            Date startDate = null;
            try {
                startDate = new SimpleDateFormat("dd-MM-yyyy", Locale.getDefault()).parse(defaultJobDateTime);
                schdlStart = startDate.getTime() + "";
            } catch (ParseException e) {
                e.printStackTrace();
            }
            defaultJobDateTime = new SimpleDateFormat("dd-MMM-yyyy", Locale.getDefault()).format(startDate);
            defaultJobDateTime = AppUtility.getDate(defaultJobDateTime);

        }


        initializeMyViews(view);


        spinnerday = view.findViewById(R.id.spinnerday);
        spinnerweek = view.findViewById(R.id.spinnerweek);
        tv_spinner_day = view.findViewById(R.id.tv_spinner_day);
        tv_spinner_week = view.findViewById(R.id.tv_spinner_week);

        AppUtility.spinnerPopUpWindow(spinnerday);
        spinnerday.setAdapter(new MySpinnerAdapter(getActivity(), dayArray));
        spinnerday.setOnItemSelectedListener(this);

        //spinnerday.setSelection(0, true);


        linearLayout_day = view.findViewById(R.id.linearLayout_day);
        linearLayout_week = view.findViewById(R.id.linearLayout_week);

        montly_every_layout = view.findViewById(R.id.montly_every_layout);
        montly_every_layout2 = view.findViewById(R.id.montly_every_layout2);
        months_layout = view.findViewById(R.id.months_layout);


        AppUtility.spinnerPopUpWindow(spinnerweek);
        spinnerweek.setAdapter(new MySpinnerAdapter(getActivity(), weekdayArray));
        spinnerweek.setOnItemSelectedListener(this);
        //spinnerweek.setSelection(0, true);


        return view;

    }

    private void initializeMyViews(View view) {
        radio_day = view.findViewById(R.id.radio_day);
        the_radio = view.findViewById(R.id.the_radio);

        every_recr_edt = view.findViewById(R.id.every_recr_edt);
        of_every = view.findViewById(R.id.of_every);
        of_every_day = view.findViewById(R.id.of_every_day);
        months_txt = view.findViewById(R.id.months_txt);
        months_txt_day = view.findViewById(R.id.months_txt_day);
        every_recr_up_img = view.findViewById(R.id.every_recr_up_img);
        every_recr_down_img = view.findViewById(R.id.every_recr_down_img);

        month_edt = view.findViewById(R.id.month_edt);
        month_up_img = view.findViewById(R.id.month_up_img);
        month_down_img = view.findViewById(R.id.month_down_img);

        end_after_up_img = view.findViewById(R.id.end_after_up_img);
        end_after_down_img = view.findViewById(R.id.end_after_down_img);
        end_after_edt = view.findViewById(R.id.end_after_edt);

        every_month_edt = view.findViewById(R.id.every_month_edt);
        every_month_up_img = view.findViewById(R.id.every_month_up_img);
        every_month_down_img = view.findViewById(R.id.every_month_down_img);


        date_start = view.findViewById(R.id.date_start);
        end_date_by = view.findViewById(R.id.end_date_by);
        end_date_by.setClickable(false);


        radio_no_end_date = view.findViewById(R.id.radio_no_end_date);
        radio_end_after = view.findViewById(R.id.radio_end_after);
        radio_end_by = view.findViewById(R.id.radio_end_by);

        end_after_layout = view.findViewById(R.id.end_after_layout);
        end_count_layout = view.findViewById(R.id.end_count_layout);

        recur_job_days_msg = view.findViewById(R.id.recur_job_days_msg);
        range_of_recurence = view.findViewById(R.id.range_of_recurence);

        schel_start = view.findViewById(R.id.schel_start);

        recuring_pattenr = view.findViewById(R.id.recuring_pattenr);
        occurance = view.findViewById(R.id.occurance);

        ever_month_the_layout = view.findViewById(R.id.ever_month_the_layout);
        every_img_layout = view.findViewById(R.id.every_img_layout);
        every_img_layout2 = view.findViewById(R.id.every_img_layout2);

        submit_btn = view.findViewById(R.id.submit_btn);


        setOnclickListnerForView();


    }

    private void setOnclickListnerForView() {

        radio_no_end_date.setOnCheckedChangeListener(this);
        radio_end_after.setOnCheckedChangeListener(this);
        radio_end_by.setOnCheckedChangeListener(this);


        every_recr_up_img.setOnClickListener(this);
        every_recr_down_img.setOnClickListener(this);

        month_down_img.setOnClickListener(this);
        month_up_img.setOnClickListener(this);

        end_after_up_img.setOnClickListener(this);
        end_after_down_img.setOnClickListener(this);

        every_month_up_img.setOnClickListener(this);
        every_month_down_img.setOnClickListener(this);

        date_start.setOnClickListener(this);
        end_date_by.setOnClickListener(this);
        submit_btn.setOnClickListener(this);

        every_recr_edt.addTextChangedListener(this);
        //   end_after_edt.addTextChangedListener(this);
        month_edt.addTextChangedListener(this);

        radio_day.setOnCheckedChangeListener(this);
        the_radio.setOnCheckedChangeListener(this);

        setDefaultValues();
    }

    private void setDefaultValues() {
        date_start.setText(defaultJobDateTime);

        radio_day.setText(LanguageController.getInstance().getMobileMsgByKey(AppConstant.day));
        the_radio.setText(LanguageController.getInstance().getMobileMsgByKey(AppConstant.the_radio));
        of_every.setText(LanguageController.getInstance().getMobileMsgByKey(AppConstant.of_every));
        of_every_day.setText(LanguageController.getInstance().getMobileMsgByKey(AppConstant.of_every));
        months_txt.setText(LanguageController.getInstance().getMobileMsgByKey(AppConstant.months_txt));
        months_txt_day.setText(LanguageController.getInstance().getMobileMsgByKey(AppConstant.months_txt));

        range_of_recurence.setText(LanguageController.getInstance().getMobileMsgByKey(AppConstant.range_of_recurence));
        radio_no_end_date.setText(LanguageController.getInstance().getMobileMsgByKey(AppConstant.radio_no_end_date));
        radio_end_after.setText(LanguageController.getInstance().getMobileMsgByKey(AppConstant.radio_end_after));
        radio_end_by.setText(LanguageController.getInstance().getMobileMsgByKey(AppConstant.radio_end_by));
        occurance.setText(LanguageController.getInstance().getMobileMsgByKey(AppConstant.occurance));
        recuring_pattenr.setText(LanguageController.getInstance().getMobileMsgByKey(AppConstant.recuring_pattenr));
        schel_start.setText(LanguageController.getInstance().getMobileMsgByKey(AppConstant.schel_start));
        submit_btn.setText(LanguageController.getInstance().getMobileMsgByKey(AppConstant.save_btn));


        monthlyRecrPi = new Monthly_Recr_PC(this);
        callMonthlyRecuApi();

    }


    /****update Every day recur Count***/
    private void setCountForEvery(boolean updateCOunt) {
        int EVERYDAYSCOUNT = 0;
        if (!every_recr_edt.getText().toString().equals(""))
            EVERYDAYSCOUNT = Integer.parseInt(every_recr_edt.getText().toString());
        if (updateCOunt) {
            EVERYDAYSCOUNT++;
        } else {
            if (EVERYDAYSCOUNT != 1)
                EVERYDAYSCOUNT--;
        }
        if (EVERYDAYSCOUNT >= 1)
            every_recr_edt.setText(EVERYDAYSCOUNT + "");
        else every_recr_edt.setText("");
    }


    @Override
    public void onItemSelected(AdapterView<?> adapterView, View arg1, int position, long id) {
        switch (adapterView.getId()) {
            case R.id.spinnerday:
                tv_spinner_day.setText(dayArray[position]);
                weekNum = position + 1 + "";
                callMonthlyRecuApi();
                break;
            case R.id.spinnerweek:
                tv_spinner_week.setText(weekdayArray[position]);
                dayNum = position + 1 + "";
                callMonthlyRecuApi();
                break;

        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> arg0) {
        // TODO Auto-generated method stub
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.submit_btn:
                addMonthlyRecurPattern();
                break;
            case R.id.linearLayout_day:
                spinnerday.performClick();
                break;
            case R.id.linearLayout_week:
                spinnerweek.performClick();
                break;
            case R.id.every_recr_up_img:
                setCountForEvery(true);
                break;
            case R.id.every_recr_down_img:
                setCountForEvery(false);
                break;
            case R.id.month_up_img:
                setCountForMonthDay(true);
                break;
            case R.id.month_down_img:
                setCountForMonthDay(false);
                break;
            case R.id.end_after_up_img:
                setCountForEndAfter(true);
                break;
            case R.id.end_after_down_img:
                setCountForEndAfter(false);
                break;
            case R.id.every_month_up_img:
                setCountForEveryMoth(true);
                break;
            case R.id.every_month_down_img:
                setCountForEveryMoth(false);
                break;
            case R.id.date_start:
                StartRecrCheck = true;
                getDatePickerDialog("date_start");
                break;
            case R.id.end_date_by:
                StartRecrCheck = false;
                getDatePickerDialog("end_date_by");
                break;
        }


    }

    private void addMonthlyRecurPattern() {
        if (RECRMSG == 3 && end_date_by.getText().toString().equals("")) {
            showDialog(LanguageController.getInstance().getMobileMsgByKey(AppConstant.select_end_date));
        } else {
            occurances = RECRMSG == 1 ? "" : end_after_edt.getText().toString();
            dateNum = mode.equals("1") ? every_recr_edt.getText().toString() : "";
            numOfMonths = mode.equals("1") ? month_edt.getText().toString() : every_month_edt.getText().toString();
            dayNum = mode.equals("1") ? "" : dayNum;
            weekNum = mode.equals("1") ? "" : weekNum;
            Intent intent = new Intent();
            intent.putExtra("recurType", "3");
            intent.putExtra("recurMsg", recur_job_days_msg.getText().toString());
            intent.putExtra("daily_recur_pattern", new Gson().toJson
                    (new JobRecurModel(mode, startDate, endDate, occurances, endRecurMode, dateNum, numOfMonths, dayNum, weekNum)));
            getActivity().setResult(Add_job_activity.ADDCUSTOMRECUR, intent);
            getActivity().finish();
        }
    }

    private void showDialog(String msg) {
        AppUtility.alertDialog(getActivity(),
                "", msg, LanguageController.getInstance().getMobileMsgByKey(AppConstant.ok), "", new Callable<Boolean>() {
                    @Override
                    public Boolean call() throws Exception {
                        return null;
                    }
                });
    }


    private void getDatePickerDialog(final String dateView) {
        DialogFragment datePicker = new DateTimeDiloag(new DateTimeCallBack() {
            @Override
            public void getDateTimeFromPicker(String currentDateString) {
                if (dateView.equals("date_start")) {
                    date_start.setText(currentDateString);
                } else if (radio_end_by.isChecked())
                    setEndDateByWeekDays(currentDateString);
                else
                    end_date_by.setText(currentDateString);

                callMonthlyRecuApi();

            }

        }, StartRecrCheck);
        datePicker.show(getFragmentManager(), dateView);
    }

    private void setCountForEveryMoth(boolean b) {
        int EVERYDAYSCOUNT = 0;
        if (!every_month_edt.getText().toString().equals(""))
            EVERYDAYSCOUNT = Integer.parseInt(every_month_edt.getText().toString());
        if (b) {
            EVERYDAYSCOUNT++;
        } else {
            if (EVERYDAYSCOUNT != 1)
                EVERYDAYSCOUNT--;
        }
        if (EVERYDAYSCOUNT >= 1)
            every_month_edt.setText(EVERYDAYSCOUNT + "");
        else every_month_edt.setText("");
    }

    /***End Date Set in View by Selected Week day's
     * @param currentDateString*****/
    private void setEndDateByWeekDays(String currentDateString) {
        end_date_by.setText(currentDateString);

        Calendar calendar = Calendar.getInstance();
        try {
            calendar.setTime(new SimpleDateFormat("dd-MMM-yyyy", Locale.getDefault()).parse(currentDateString));
        } catch (ParseException e) {
            e.printStackTrace();
        }


//        if (SELECTED_DAYS.contains(calendar.get(Calendar.DAY_OF_WEEK))) {
//            Log.e("", "");
//        }


        if (calendar.get(Calendar.DAY_OF_WEEK) == Calendar.SATURDAY) {
            calendar.add(Calendar.DATE, -1);  // number of days to add, can also use Calendar.DAY_OF_MONTH in place of Calendar.DATE
        } else if (calendar.get(Calendar.DAY_OF_WEEK) == Calendar.SUNDAY) {
            calendar.add(Calendar.DATE, -2);  // number of days to add, can also use Calendar.DAY_OF_MONTH in place of Calendar.DATE
        }

    }

    private void setCountForEndAfter(boolean b) {
        int EVERYDAYSCOUNT = 0;
        if (!end_after_edt.getText().toString().equals(""))
            EVERYDAYSCOUNT = Integer.parseInt(end_after_edt.getText().toString());
        if (b) {
            EVERYDAYSCOUNT++;
        } else {
            if (EVERYDAYSCOUNT != 1)
                EVERYDAYSCOUNT--;
        }
        if (EVERYDAYSCOUNT >= 1)
            end_after_edt.setText(EVERYDAYSCOUNT + "");
        else end_after_edt.setText("");
    }


    private void setCountForMonthDay(boolean b) {
        int EVERYDAYSCOUNT = 0;
        if (!month_edt.getText().toString().equals(""))
            EVERYDAYSCOUNT = Integer.parseInt(month_edt.getText().toString());
        if (b) {
            EVERYDAYSCOUNT++;
        } else {
            if (EVERYDAYSCOUNT != 1)
                EVERYDAYSCOUNT--;
        }
        if (EVERYDAYSCOUNT >= 1)
            month_edt.setText(EVERYDAYSCOUNT + "");
        else month_edt.setText("");
    }

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        if (buttonView.getId() == R.id.radio_day) {
            if (isChecked) {
                radio_day.setChecked(true);
                the_radio.setChecked(false);
                setDayMonthViewVisibility();

            } else {
                radio_day.setChecked(false);
            }
        } else if (buttonView.getId() == R.id.the_radio) {
            if (isChecked) {
                the_radio.setChecked(true);
                radio_day.setChecked(false);
                setEveryMonthradioViews();

            } else {
                the_radio.setChecked(false);
            }
        } else if (buttonView.getId() == R.id.radio_no_end_date) {
            if (isChecked) {
                radio_no_end_date.setChecked(true);
                radio_end_after.setChecked(false);
                radio_end_by.setChecked(false);
                setViewVisibility(1);
            } else {
                radio_no_end_date.setChecked(false);
            }
        } else if (buttonView.getId() == R.id.radio_end_after) {
            if (isChecked) {
                radio_no_end_date.setChecked(false);
                radio_end_after.setChecked(true);
                radio_end_by.setChecked(false);
                setViewVisibility(2);
            } else {
                radio_end_after.setChecked(false);
            }
        } else if (buttonView.getId() == R.id.radio_end_by) {
            if (isChecked) {
                radio_no_end_date.setChecked(false);
                radio_end_after.setChecked(false);
                radio_end_by.setChecked(true);
                setViewVisibility(3);
            } else {
                radio_end_by.setChecked(false);
            }
        }
    }

    /***this for Day radio check****/
    private void setDayMonthViewVisibility() {
        mode = "1";
        month_edt.addTextChangedListener(this);

        montly_every_layout.setBackgroundResource(R.drawable.edittext_shap_qus);
        montly_every_layout2.setBackgroundResource(R.drawable.edittext_shap_qus);
        linearLayout_day.setBackgroundResource(R.drawable.layout_disable);
        linearLayout_week.setBackgroundResource(R.drawable.layout_disable);
        months_layout.setBackgroundResource(R.drawable.layout_disable);
        linearLayout_day.setClickable(false);
        linearLayout_week.setClickable(false);
        every_recr_edt.setVisibility(View.VISIBLE);
        month_edt.setVisibility(View.VISIBLE);
        every_recr_edt.setText("1");
        month_edt.setText("1");
        months_layout.setBackgroundResource(R.drawable.layout_disable);
        every_month_edt.setVisibility(View.INVISIBLE);
        every_month_edt.setText("");
        ever_month_the_layout.setVisibility(View.INVISIBLE);
        every_img_layout.setVisibility(View.VISIBLE);
        every_img_layout2.setVisibility(View.VISIBLE);

        tv_spinner_day.setText("");
        tv_spinner_week.setText("");
        every_month_edt.removeTextChangedListener(null);
        every_recr_edt.addTextChangedListener(this);
    }

    /***this for The radio check****/
    private void setEveryMonthradioViews() {
        mode = "2";
        every_recr_edt.addTextChangedListener(this);
        month_edt.addTextChangedListener(this);
        montly_every_layout.setBackgroundResource(R.drawable.layout_disable);
        montly_every_layout2.setBackgroundResource(R.drawable.layout_disable);
        linearLayout_day.setBackgroundResource(R.drawable.edittext_shap_qus);
        linearLayout_week.setBackgroundResource(R.drawable.edittext_shap_qus);
        months_layout.setBackgroundResource(R.drawable.edittext_shap_qus);

        every_recr_edt.setVisibility(View.INVISIBLE);
        month_edt.setVisibility(View.INVISIBLE);
        linearLayout_day.setClickable(true);
        linearLayout_week.setClickable(true);
        every_recr_edt.setText("");
        month_edt.setText("");


        linearLayout_day.setOnClickListener(this);
        linearLayout_week.setOnClickListener(this);

        months_layout.setBackgroundResource(R.drawable.edittext_shap_qus);
        every_month_edt.setVisibility(View.VISIBLE);
        ever_month_the_layout.setVisibility(View.VISIBLE);
        every_month_edt.setText("1");

        every_img_layout.setVisibility(View.INVISIBLE);
        every_img_layout2.setVisibility(View.INVISIBLE);
        every_recr_edt.setVisibility(View.INVISIBLE);
        month_edt.setVisibility(View.INVISIBLE);


        linearLayout_day.setVisibility(View.VISIBLE);
        linearLayout_week.setVisibility(View.VISIBLE);


        spinnerday.setSelection(0, true);
        spinnerweek.setSelection(0, true);
        every_month_edt.addTextChangedListener(this);

    }


    private void setViewVisibility(int i) {
        if (i == 1) {
            end_after_edt.removeTextChangedListener(this);
            end_after_layout.setBackgroundResource(R.drawable.layout_disable);
            end_date_by.setBackgroundResource(R.drawable.layout_disable);
            end_count_layout.setVisibility(View.INVISIBLE);
            end_after_edt.setVisibility(View.INVISIBLE);
            end_date_by.setClickable(false);
            endRecurMode = "0";
            RECRMSG = 1;
            end_after_edt.setText("");
            end_date_by.setText("");
            callMonthlyRecuApi();
        } else if (i == 2) {
            end_after_edt.addTextChangedListener(this);
            end_after_layout.setBackgroundResource(R.drawable.edittext_shap_qus);
            end_date_by.setBackgroundResource(R.drawable.layout_disable);
            end_count_layout.setVisibility(View.VISIBLE);
            end_after_edt.setVisibility(View.VISIBLE);
            end_date_by.setClickable(false);
            endRecurMode = "1";
            RECRMSG = 2;
            end_after_edt.setText("1");
            end_date_by.setText("");
        } else if (i == 3) {
            end_after_edt.removeTextChangedListener(this);
            end_after_layout.setBackgroundResource(R.drawable.layout_disable);
            end_date_by.setBackgroundResource(R.drawable.edittext_shap_qus);
            end_count_layout.setVisibility(View.INVISIBLE);
            end_after_edt.setVisibility(View.INVISIBLE);
            end_date_by.setClickable(true);
            end_date_by.setOnClickListener(this);
            end_after_edt.setText("");
            end_date_by.setText("");
            endRecurMode = "2";
            RECRMSG = 3;
        }
    }


    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence charSequence, int start, int before, int count) {
        if (charSequence.length() == 1 && charSequence.toString().equals("0")) {
            if (charSequence.hashCode() == every_recr_edt.getText().hashCode())
                every_recr_edt.setText("");
            else if (charSequence.hashCode() == end_after_edt.getText().hashCode())
                end_after_edt.setText("");
            else if (charSequence.hashCode() == every_month_edt.getText().hashCode())
                every_month_edt.setText("");
            else if (charSequence.hashCode() == month_edt.getText().hashCode())
                month_edt.setText("");
        }


        if (charSequence.toString().equals("0") || charSequence.toString().equals(""))
            charSequence = "1";


        if (charSequence.length() > 0) {
            if (charSequence.hashCode() == every_recr_edt.getText().hashCode()) {
                if (Integer.parseInt(every_recr_edt.getText().toString()) > 31) {
                    showDialog(LanguageController.getInstance().getMobileMsgByKey(AppConstant.max_day));
                } else {
                    callMonthlyRecuApi();
                }
            } else if (charSequence.hashCode() == end_after_edt.getText().hashCode() ||
                    charSequence.hashCode() == month_edt.getText().hashCode()
                    || charSequence.hashCode() == every_month_edt.getText().hashCode()) {
                callMonthlyRecuApi();
            }

        } else {
            recur_job_days_msg.setVisibility(View.GONE);
        }

    }


    @Override
    public void afterTextChanged(Editable s) {

    }

    @Override
    public void onSessionExpired(String message) {
        AppUtility.alertDialog(getActivity(), LanguageController.getInstance().getMobileMsgByKey(AppConstant.dialog_error_title), message, LanguageController.getInstance().getMobileMsgByKey(AppConstant.ok), "", new Callable<Boolean>() {
            @Override
            public Boolean call() throws Exception {
                EotApp.getAppinstance().sessionExpired();
                return null;
            }
        });
    }

    @Override
    public void showMsgOnView(DailyMsgResModel dailyMsgResModel) {
        recur_job_days_msg.setVisibility(View.VISIBLE);


        if (RECRMSG == 2) {
            if (dailyMsgResModel.getEnd_date() != null && !dailyMsgResModel.getEnd_date().equals("")) {
                Date start_Date = null;
                try {
                    start_Date = new SimpleDateFormat("dd-MMM-yyyy", Locale.getDefault()).parse(dailyMsgResModel.getEnd_date());
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                endDate = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(start_Date);
                end_date_by.setText(new SimpleDateFormat("dd-MMM-yyyy", Locale.getDefault()).format(start_Date));
                Log.e("", "");
            }
        } else if (RECRMSG == 3) {
            if (dailyMsgResModel.getOccurences() != null) {
                end_after_edt.setVisibility(View.VISIBLE);
                end_after_edt.setText(dailyMsgResModel.getOccurences());
            }
            if (dailyMsgResModel.getEnd_date() != null && !dailyMsgResModel.getEnd_date().equals("")) {
                Date start_Date = null;
                try {
                    start_Date = new SimpleDateFormat("dd-MMM-yyyy", Locale.getDefault()).parse(dailyMsgResModel.getEnd_date());
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                end_date_by.setText(new SimpleDateFormat("dd-MMM-yyyy", Locale.getDefault()).format(start_Date));
                Log.e("", "");
            }
        }

        if (RECRMSG == 1) {
            recur_job_days_msg.setText(LanguageController.getInstance().getMobileMsgByKey(AppConstant.weekly_msg1) + " " +
                    dailyMsgResModel.getWeek_num()
                    + " " + LanguageController.getInstance().getMobileMsgByKey(AppConstant.every) + " " +
                    month_edt.getText().toString() + " " + LanguageController.getInstance().getMobileMsgByKey(AppConstant.months_txt) + " " +
                    schel_start.getText().toString().toLowerCase() + " " +
                    dailyMsgResModel.getStart_date() + " " +
                    LanguageController.getInstance().getMobileMsgByKey(AppConstant.custom_recur_msg2) + " " + LanguageController.getInstance().getMobileMsgByKey(AppConstant.infinity));
        } else {
            recur_job_days_msg.setText(LanguageController.getInstance().getMobileMsgByKey(AppConstant.weekly_msg1) + " " +
                    dailyMsgResModel.getWeek_num() + " " + tv_spinner_day.getText().toString().toLowerCase() + " " +
                    radio_day.getText().toString().toLowerCase()
                    + " " + every_recr_edt.getText().toString() + " " +
                    LanguageController.getInstance().getMobileMsgByKey(AppConstant.months_starting_on) + " "
                    + dailyMsgResModel.getStart_date() + " " +

                    LanguageController.getInstance().getMobileMsgByKey(AppConstant.custom_recur_msg2) + " "
                    + end_after_edt.getText().toString() +
                    " " + LanguageController.getInstance().getMobileMsgByKey(AppConstant.custom_recur_msg3) + " " +
                    dailyMsgResModel.getEnd_date());
        }
    }

    private void callMonthlyRecuApi() {
        String occurDay = "", interval = "", numberOfOcurrences = "";
        if (mode.equals("1")) {
            occurDay = every_recr_edt.getText().toString();
            interval = month_edt.getText().toString();
            weekNum = "";
            dayNum = "";
        } else if (mode.equals("2")) {
            interval = every_month_edt.getText().toString();
        }

        if (RECRMSG == 2 && !end_after_edt.getText().toString().equals("")) {
            numberOfOcurrences = end_after_edt.getText().toString();
        } else if (RECRMSG == 3 && !end_date_by.getText().toString().equals("")) {
            Date startDate = null;
            try {
                startDate = new SimpleDateFormat("dd-MMM-yyyy", Locale.getDefault()).parse(end_date_by.getText().toString());
            } catch (ParseException e) {
                e.printStackTrace();
            }
            endDate = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(startDate);
            numberOfOcurrences = "";
        } else {
            endDate = "";
        }
        if (!date_start.getText().toString().equals("")) {
            Date start_Date = null;
            try {
                start_Date = new SimpleDateFormat("dd-MMM-yyyy", Locale.getDefault()).parse(date_start.getText().toString());
            } catch (ParseException e) {
                e.printStackTrace();
            }
            startDate = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(start_Date);
        }

        monthlyRecrPi.getApiMontlyRecurMsg(new DailyMsgReqModel(schdlStart, mode, interval, numberOfOcurrences, endRecurMode,
                startDate, endDate, occurDay, weekNum, dayNum));

    }
}
