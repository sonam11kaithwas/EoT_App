package com.eot_app.nav_menu.jobs.add_job.add_job_recr.weekly_recr_pkg;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
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
import com.eot_app.nav_menu.jobs.add_job.add_job_recr.weekly_recr_pkg.weekly_recr_mvp.WeeklyRecr_PC;
import com.eot_app.nav_menu.jobs.add_job.add_job_recr.weekly_recr_pkg.weekly_recr_mvp.WeeklyRecr_PI;
import com.eot_app.nav_menu.jobs.add_job.add_job_recr.weekly_recr_pkg.weekly_recr_mvp.WeeklyRecr_View;
import com.eot_app.utility.AppConstant;
import com.eot_app.utility.AppUtility;
import com.eot_app.utility.EotApp;
import com.eot_app.utility.language_support.LanguageController;
import com.google.gson.Gson;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.concurrent.Callable;

public class WeeklyRecrFragment extends Fragment implements View.OnClickListener, CompoundButton.OnCheckedChangeListener, TextWatcher, WeeklyRecr_View {
    private final String mode = "1";
    private final HashMap<String, Boolean> selectedDays = new HashMap<>();
    private final ArrayList<String> weekDays = new ArrayList<>();
    private String occurances = "";
    private String numOfWeeks = "";
    /***This use for disable Previous date for Start Date picker***/
    private boolean StartRecrCheck;
    /***RECRMSG use for Range of Recurrence CheckBox Type's******/
    private int RECRMSG = 1;
    private TextView date_start, end_date_by, recur_job_days_msg, weeks_on, range_of_recurence, schel_start, occurance, recuring_pattenr;
    private ImageView every_recr_up_img, every_recr_down_img, end_after_up_img, end_after_down_img;
    private EditText end_after_edt, every_recr_edt;
    private RadioButton radio_no_end_date, radio_end_after, radio_end_by, radio_everyDay;//, radio_day;
    private LinearLayout end_after_layout, end_count_layout;
    private CheckBox check_mon, check_tues, check_wedns, check_thurs, check_friday, check_satur, check_sun;
    private String defaultJobDateTime = "";
    private WeeklyRecr_PI weeklyRecrPi;
    private String schdlStart;
    private String endRecurMode = "0";
    private String endDate = "", startDate = "";
    private Button submit_btn;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
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

        String str = (check_mon.isChecked() ? "Monday," : "") + (check_tues.isChecked() ? "Tuesday," : "") + (check_wedns.isChecked() ? "Wednesday," : "") + (check_thurs.isChecked() ? "Thursday," : "") + (check_friday.isChecked() ? "Friday," : "") + (check_satur.isChecked() ? "Saturday," : "") + (check_sun.isChecked() ? "Sunday" : "");
        Log.e("", "");


        if (str != null && str.length() > 0 && str.charAt(str.length() - 1) == ',') {
            str = str.substring(0, str.length() - 1);
        }


        if (RECRMSG == 2 && dailyMsgResModel.getEnd_date() != null && !dailyMsgResModel.getEnd_date().equals("")) {
            Date start_Date = null;
            try {
                start_Date = new SimpleDateFormat("dd-MMM-yyyy", Locale.getDefault()).parse(dailyMsgResModel.getEnd_date());
            } catch (ParseException e) {
                e.printStackTrace();
            }
            endDate = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(start_Date);
            end_date_by.setText(new SimpleDateFormat("dd-MMM-yyyy", Locale.getDefault()).format(start_Date));
            Log.e("", "");
        } else if (RECRMSG == 3 && dailyMsgResModel.getOccurences() != null) {
            end_after_edt.setVisibility(View.VISIBLE);
            end_after_edt.setText(dailyMsgResModel.getOccurences());
            Date start_Date = null;
            try {
                start_Date = new SimpleDateFormat("dd-MMM-yyyy", Locale.getDefault()).parse(dailyMsgResModel.getEnd_date());
            } catch (ParseException e) {
                e.printStackTrace();
            }
            end_date_by.setText(new SimpleDateFormat("dd-MMM-yyyy", Locale.getDefault()).format(start_Date));
        }

        if (RECRMSG == 1) {
            recur_job_days_msg.setText(LanguageController.getInstance().getMobileMsgByKey(AppConstant.weekly_msg1) + " " +
                    str + " " + LanguageController.getInstance().getMobileMsgByKey(AppConstant.every) + " " + every_recr_edt.getText().toString() + " " +
                    LanguageController.getInstance().getMobileMsgByKey(AppConstant.weeks) + " " +
                    dailyMsgResModel.getStart_date() + " " +
                    LanguageController.getInstance().getMobileMsgByKey(AppConstant.custom_recur_msg2) + " " + LanguageController.getInstance().getMobileMsgByKey(AppConstant.infinity));
        } else {
            recur_job_days_msg.setText(LanguageController.getInstance().getMobileMsgByKey(AppConstant.weekly_msg1) + " " +
                    str + " " + LanguageController.getInstance().getMobileMsgByKey(AppConstant.every) + " " + every_recr_edt.getText().toString() + " " +
                    LanguageController.getInstance().getMobileMsgByKey(AppConstant.weeks) + " " + dailyMsgResModel.getStart_date() + " " +
                    LanguageController.getInstance().getMobileMsgByKey(AppConstant.custom_recur_msg2) + " " + end_after_edt.getText().toString() +
                    " " + LanguageController.getInstance().getMobileMsgByKey(AppConstant.custom_recur_msg3) + " " + dailyMsgResModel.getEnd_date());
        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.job_recr_week_layout, container, false);

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


        return view;
    }

    private void initializeMyViews(View view) {

        radio_everyDay = view.findViewById(R.id.radio_everyDay);
        weeks_on = view.findViewById(R.id.weeks_on);


        every_recr_edt = view.findViewById(R.id.every_recr_edt);
        every_recr_up_img = view.findViewById(R.id.every_recr_up_img);
        every_recr_down_img = view.findViewById(R.id.every_recr_down_img);

        date_start = view.findViewById(R.id.date_start);
        end_date_by = view.findViewById(R.id.end_date_by);


        end_after_up_img = view.findViewById(R.id.end_after_up_img);
        end_after_down_img = view.findViewById(R.id.end_after_down_img);
        end_after_edt = view.findViewById(R.id.end_after_edt);

        radio_no_end_date = view.findViewById(R.id.radio_no_end_date);
        radio_end_after = view.findViewById(R.id.radio_end_after);
        radio_end_by = view.findViewById(R.id.radio_end_by);

        end_after_layout = view.findViewById(R.id.end_after_layout);
        end_count_layout = view.findViewById(R.id.end_count_layout);

        recur_job_days_msg = view.findViewById(R.id.recur_job_days_msg);


        check_mon = view.findViewById(R.id.check_mon);
        check_tues = view.findViewById(R.id.check_tues);
        check_wedns = view.findViewById(R.id.check_wedns);
        check_thurs = view.findViewById(R.id.check_thurs);
        check_friday = view.findViewById(R.id.check_friday);
        check_satur = view.findViewById(R.id.check_satur);
        check_sun = view.findViewById(R.id.check_sun);

        range_of_recurence = view.findViewById(R.id.range_of_recurence);

        schel_start = view.findViewById(R.id.schel_start);

        recuring_pattenr = view.findViewById(R.id.recuring_pattenr);
        occurance = view.findViewById(R.id.occurance);
        submit_btn = view.findViewById(R.id.submit_btn);


        setOnclickListnerForView();


    }


    private void setOnclickListnerForView() {
        radio_everyDay.setOnCheckedChangeListener(this);


        every_recr_up_img.setOnClickListener(this);
        every_recr_down_img.setOnClickListener(this);

        end_after_up_img.setOnClickListener(this);
        end_after_down_img.setOnClickListener(this);

        radio_no_end_date.setOnCheckedChangeListener(this);
        radio_end_after.setOnCheckedChangeListener(this);
        radio_end_by.setOnCheckedChangeListener(this);


        check_mon.setOnCheckedChangeListener(this);
        check_tues.setOnCheckedChangeListener(this);
        check_wedns.setOnCheckedChangeListener(this);
        check_thurs.setOnCheckedChangeListener(this);
        check_friday.setOnCheckedChangeListener(this);
        check_satur.setOnCheckedChangeListener(this);
        check_sun.setOnCheckedChangeListener(this);

        every_recr_edt.addTextChangedListener(this);
        end_after_edt.addTextChangedListener(this);

        date_start.setOnClickListener(this);
        submit_btn.setOnClickListener(this);

        setDefaultValues();
    }

    private void setDefaultValues() {
        date_start.setText(defaultJobDateTime);

        setTextViews();

        weeklyRecrPi = new WeeklyRecr_PC(this);
        callApiForRecurMsg();

    }

    private void setTextViews() {
        radio_everyDay.setText(LanguageController.getInstance().getMobileMsgByKey(AppConstant.radio_everyDay));
        weeks_on.setText(LanguageController.getInstance().getMobileMsgByKey(AppConstant.weeks_on));

        check_mon.setText(LanguageController.getInstance().getMobileMsgByKey(AppConstant.check_mon));
        check_tues.setText(LanguageController.getInstance().getMobileMsgByKey(AppConstant.check_tues));
        check_wedns.setText(LanguageController.getInstance().getMobileMsgByKey(AppConstant.check_wedns));
        check_thurs.setText(LanguageController.getInstance().getMobileMsgByKey(AppConstant.check_thurs));
        check_friday.setText(LanguageController.getInstance().getMobileMsgByKey(AppConstant.check_friday));
        check_satur.setText(LanguageController.getInstance().getMobileMsgByKey(AppConstant.check_satur));
        check_sun.setText(LanguageController.getInstance().getMobileMsgByKey(AppConstant.check_sun));

        range_of_recurence.setText(LanguageController.getInstance().getMobileMsgByKey(AppConstant.range_of_recurence));
        radio_no_end_date.setText(LanguageController.getInstance().getMobileMsgByKey(AppConstant.radio_no_end_date));
        radio_end_after.setText(LanguageController.getInstance().getMobileMsgByKey(AppConstant.radio_end_after));
        radio_end_by.setText(LanguageController.getInstance().getMobileMsgByKey(AppConstant.radio_end_by));
        occurance.setText(LanguageController.getInstance().getMobileMsgByKey(AppConstant.occurance));
        schel_start.setText(LanguageController.getInstance().getMobileMsgByKey(AppConstant.schel_start));
        recuring_pattenr.setText(LanguageController.getInstance().getMobileMsgByKey(AppConstant.recuring_pattenr));
        submit_btn.setText(LanguageController.getInstance().getMobileMsgByKey(AppConstant.save_btn));
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.submit_btn:
                addWeeklyRecurSubmit();
                break;
            case R.id.every_recr_up_img:
                setCountForEvery(true);
                break;
            case R.id.every_recr_down_img:
                setCountForEvery(false);
                break;
            case R.id.end_after_up_img:
                setCountForEndAfter(true);
                break;
            case R.id.end_after_down_img:
                setCountForEndAfter(false);
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

    private void addWeeklyRecurSubmit() {
        if (selectedDays.size() == 0) {
            showDialog(LanguageController.getInstance().getMobileMsgByKey(AppConstant.select_week_days));
        } else {
            occurances = RECRMSG == 1 ? "" : end_after_edt.getText().toString();
            numOfWeeks = every_recr_edt.getText().toString();
            Intent intent = new Intent();
            intent.putExtra("recurType", "2");
            intent.putExtra("recurMsg", recur_job_days_msg.getText().toString());
            intent.putExtra("selectedDays", new Gson().toJson(selectedDays));
            intent.putExtra("daily_recur_pattern", new Gson().toJson
                    (new JobRecurModel(mode, startDate, endDate, occurances, numOfWeeks, endRecurMode, weekDays)));
            getActivity().setResult(Add_job_activity.ADDCUSTOMRECUR, intent);
            getActivity().finish();
        }
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

                callApiForRecurMsg();

            }

        }, StartRecrCheck);
        datePicker.show(getFragmentManager(), dateView);
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
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

        if (buttonView.getId() == R.id.radio_no_end_date) {
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
        } else if (buttonView.getId() == R.id.check_mon) {
            if (check_mon.isChecked()) {
                selectedDays.put("monday", true);
                weekDays.add("1");
            } else {
                selectedDays.remove("monday");
                weekDays.remove("1");
            }
            callApiForRecurMsg();
        } else if (buttonView.getId() == R.id.check_tues) {
            if (check_tues.isChecked()) {
                selectedDays.put("tuesday", true);
                weekDays.add("2");
            } else {
                selectedDays.remove("tuesday");
                weekDays.remove("2");
            }
            callApiForRecurMsg();
        } else if (buttonView.getId() == R.id.check_wedns) {
            if (check_wedns.isChecked()) {
                selectedDays.put("wednesday", true);
                weekDays.add("3");
            } else {
                selectedDays.remove("wednesday");
                weekDays.remove("3");
            }
            callApiForRecurMsg();
        } else if (buttonView.getId() == R.id.check_thurs) {
            if (check_thurs.isChecked()) {
                selectedDays.put("thursday", true);
                weekDays.add("4");
            } else {
                selectedDays.remove("thursday");
                weekDays.remove("4");
            }
            callApiForRecurMsg();
        } else if (buttonView.getId() == R.id.check_friday) {
            if (check_friday.isChecked()) {
                selectedDays.put("friday", true);
                weekDays.add("5");
            } else {
                selectedDays.remove("friday");
                weekDays.remove("5");

            }
            callApiForRecurMsg();
        } else if (buttonView.getId() == R.id.check_satur) {
            if (check_satur.isChecked()) {
                selectedDays.put("saturday", true);
                weekDays.add("6");

            } else {
                selectedDays.remove("saturday");
                weekDays.remove("6");

            }
            callApiForRecurMsg();
        } else if (buttonView.getId() == R.id.check_sun) {
            if (check_sun.isChecked()) {
                selectedDays.put("sunday", true);
                weekDays.add("7");

            } else {
                selectedDays.remove("sunday");
                weekDays.remove("7");

            }
            callApiForRecurMsg();
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
        } else if (charSequence.length() > 0) {
            recur_job_days_msg.setVisibility(View.VISIBLE);
            if (charSequence.hashCode() == every_recr_edt.getText().hashCode() || charSequence.hashCode() == end_after_edt.getText().hashCode()) {
                callApiForRecurMsg();
            }
        }


    }

    @Override
    public void afterTextChanged(Editable s) {

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

    private void callApiForRecurMsg() {

        if (RECRMSG == 3 && !AppUtility.dateGraterOrLess(date_start.getText().toString(), end_date_by.getText().toString())) {
            showDialog(LanguageController.getInstance().getMobileMsgByKey(AppConstant.date_validation));
        } else {

            String numberOfOcurrences = "";
            numberOfOcurrences = end_after_edt.getText().toString();
            if (RECRMSG == 3 && !end_date_by.getText().toString().equals("")) {
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

            if (selectedDays.size() > 0) {
                weeklyRecrPi.getDailyRecurMsg(new DailyMsgReqModel(mode, schdlStart, startDate, endDate,
                        every_recr_edt.getText().toString(), endRecurMode, weekDays, numberOfOcurrences));
            }
        }

    }


    private void setViewVisibility(int i) {
        recur_job_days_msg.setVisibility(View.GONE);
        if (i == 1) {
            end_after_layout.setBackgroundResource(R.drawable.layout_disable);
            end_date_by.setBackgroundResource(R.drawable.layout_disable);
            end_count_layout.setVisibility(View.INVISIBLE);
            end_after_edt.setVisibility(View.INVISIBLE);
            end_date_by.setClickable(false);
            RECRMSG = 1;
            end_after_edt.setText("");
            end_date_by.setText("");
            endRecurMode = "0";
        } else if (i == 2) {
            end_after_edt.addTextChangedListener(this);
            end_after_layout.setBackgroundResource(R.drawable.edittext_shap_qus);
            end_date_by.setBackgroundResource(R.drawable.layout_disable);
            end_count_layout.setVisibility(View.VISIBLE);
            end_after_edt.setVisibility(View.VISIBLE);
            end_date_by.setClickable(false);
            RECRMSG = 2;
            endRecurMode = "1";
            end_after_edt.setText("1");
            end_date_by.setText("");
        } else if (i == 3) {
            end_after_edt.removeTextChangedListener(this);
            end_after_edt.setText("");
            end_after_layout.setBackgroundResource(R.drawable.layout_disable);
            end_date_by.setBackgroundResource(R.drawable.edittext_shap_qus);
            end_count_layout.setVisibility(View.INVISIBLE);
            end_after_edt.setVisibility(View.INVISIBLE);
            end_date_by.setText("");
            end_date_by.setClickable(true);
            RECRMSG = 3;
            endRecurMode = "2";
            end_date_by.setOnClickListener(this);
        }
    }

}
