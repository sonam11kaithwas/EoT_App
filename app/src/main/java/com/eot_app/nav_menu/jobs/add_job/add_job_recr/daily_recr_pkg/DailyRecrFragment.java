package com.eot_app.nav_menu.jobs.add_job.add_job_recr.daily_recr_pkg;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;

import com.eot_app.R;
import com.eot_app.nav_menu.jobs.add_job.Add_job_activity;
import com.eot_app.nav_menu.jobs.add_job.add_job_recr.daily_recr_pkg.daily_recr_mvp.DailyRecr_PC;
import com.eot_app.nav_menu.jobs.add_job.add_job_recr.daily_recr_pkg.daily_recr_mvp.DailyRecr_PI;
import com.eot_app.nav_menu.jobs.add_job.add_job_recr.daily_recr_pkg.daily_recr_mvp.DailyRecr_View;
import com.eot_app.nav_menu.jobs.add_job.add_job_recr.daily_recr_pkg.daily_recur_model.DailyMsgReqModel;
import com.eot_app.nav_menu.jobs.add_job.add_job_recr.daily_recr_pkg.daily_recur_model.DailyMsgResModel;
import com.eot_app.nav_menu.jobs.add_job.add_job_recr.daily_recr_pkg.daily_recur_model.JobRecurModel;
import com.eot_app.nav_menu.jobs.add_job.add_job_recr.dateTime_pkg.DateTimeCallBack;
import com.eot_app.nav_menu.jobs.add_job.add_job_recr.dateTime_pkg.DateTimeDiloag;
import com.eot_app.utility.AppConstant;
import com.eot_app.utility.AppUtility;
import com.eot_app.utility.EotApp;
import com.eot_app.utility.language_support.LanguageController;
import com.google.gson.Gson;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.concurrent.Callable;

public class DailyRecrFragment extends Fragment implements View.OnClickListener, CompoundButton.OnCheckedChangeListener, TextWatcher, DailyRecr_View {
    private int RECRMSG = 1;
    private ImageView every_recr_up_img, every_recr_down_img, end_after_up_img, end_after_down_img;
    private EditText every_recr_edt, end_after_edt;
    private RadioButton radio_everyDay, radio_weekDay, radio_no_end_date, radio_end_after, radio_end_by;
    private String defaultJobDateTime = "";
    private TextView date_start, end_date_by, recur_job_days_msg, recuring_pattenr, range_of_recurence, occurance, day_s, schel_start;
    private LinearLayout daily_recr_every_layout, end_after_layout, end_count_layout, every_img_layout;
    private RelativeLayout parentViewDailyRecr;
    /***This use for disable Previous date for Start Date picker***/
    private boolean StartRecrCheck;
    private DailyRecr_PI dailyRecrPi;
    private String schdlStart, mode = "1", endRecurMode = "0";
    private Button submit_btn;
    private String endDate = "", startDate = "", occurances = "";


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.daily_recur_layout, container, false);
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
        parentViewDailyRecr = view.findViewById(R.id.parentViewDailyRecr);


        daily_recr_every_layout = view.findViewById(R.id.daily_recr_every_layout);
        every_recr_edt = view.findViewById(R.id.every_recr_edt);
        every_recr_up_img = view.findViewById(R.id.every_recr_up_img);
        every_recr_down_img = view.findViewById(R.id.every_recr_down_img);


        radio_weekDay = view.findViewById(R.id.radio_weekDay);
        radio_everyDay = view.findViewById(R.id.radio_everyDay);

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
        every_img_layout = view.findViewById(R.id.every_img_layout);


        recur_job_days_msg = view.findViewById(R.id.recur_job_days_msg);
        recuring_pattenr = view.findViewById(R.id.recuring_pattenr);
        range_of_recurence = view.findViewById(R.id.range_of_recurence);
        occurance = view.findViewById(R.id.occurance);
        day_s = view.findViewById(R.id.day_s);
        schel_start = view.findViewById(R.id.schel_start);
        submit_btn = view.findViewById(R.id.submit_btn);


        //     week_mon_txt = view.findViewById(R.id.week_mon_txt);


        setOnclickListnerForView();

    }

    private void setDefaultValues() {
        date_start.setText(defaultJobDateTime);
        dailyRecrPi = new DailyRecr_PC(this);
        callApiForRecurMsg();
    }

    private void setOnclickListnerForView() {
        AppUtility.setupUI(parentViewDailyRecr, getActivity());

        every_recr_up_img.setOnClickListener(this);
        every_recr_down_img.setOnClickListener(this);

        end_after_up_img.setOnClickListener(this);
        end_after_down_img.setOnClickListener(this);

        date_start.setOnClickListener(this);

        radio_everyDay.setOnCheckedChangeListener(this);
        radio_weekDay.setOnCheckedChangeListener(this);

        radio_no_end_date.setOnCheckedChangeListener(this);
        radio_end_after.setOnCheckedChangeListener(this);
        radio_end_by.setOnCheckedChangeListener(this);

        every_recr_edt.addTextChangedListener(this);


        submit_btn.setOnClickListener(this);


        dailyRecrPi = new DailyRecr_PC(this);


        setTextViews();


    }

    private void setTextViews() {
        radio_everyDay.setText(LanguageController.getInstance().getMobileMsgByKey(AppConstant.radio_everyDay));

        recuring_pattenr.setText(LanguageController.getInstance().getMobileMsgByKey(AppConstant.recuring_pattenr));
        range_of_recurence.setText(LanguageController.getInstance().getMobileMsgByKey(AppConstant.range_of_recurence));
        radio_everyDay.setText(LanguageController.getInstance().getMobileMsgByKey(AppConstant.radio_everyDay));
        radio_weekDay.setText(LanguageController.getInstance().getMobileMsgByKey(AppConstant.radio_weekDay));
        schel_start.setText(LanguageController.getInstance().getMobileMsgByKey(AppConstant.schel_start));
        radio_no_end_date.setText(LanguageController.getInstance().getMobileMsgByKey(AppConstant.radio_no_end_date));
        radio_end_after.setText(LanguageController.getInstance().getMobileMsgByKey(AppConstant.radio_end_after));
        radio_end_by.setText(LanguageController.getInstance().getMobileMsgByKey(AppConstant.radio_end_by));
        occurance.setText(LanguageController.getInstance().getMobileMsgByKey(AppConstant.occurance));
        day_s.setText(LanguageController.getInstance().getMobileMsgByKey(AppConstant.day_s));
        submit_btn.setText(LanguageController.getInstance().getMobileMsgByKey(AppConstant.save_btn));
        setDefaultValues();
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.submit_btn:
                addDailyRecurForJob();
                break;
            case R.id.every_recr_up_img:
                setEVERY_DAYS_COUNTForEvery(true);
                break;
            case R.id.every_recr_down_img:
                setEVERY_DAYS_COUNTForEvery(false);
                break;
            case R.id.end_after_up_img:
                setEVERY_DAYS_COUNTForEndAfter(true);
                break;
            case R.id.end_after_down_img:
                setEVERY_DAYS_COUNTForEndAfter(false);
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


    private void addDailyRecurForJob() {
        if (RECRMSG == 3 && end_date_by.getText().toString().equals("")) {
            showDialog(LanguageController.getInstance().getMobileMsgByKey(AppConstant.select_end_date));
        } else {
            occurances = RECRMSG == 1 ? "" : end_after_edt.getText().toString();
            String numOfDays = mode.equals("1") ? every_recr_edt.getText().toString() : "";
            Intent intent = new Intent();
            intent.putExtra("recurType", "1");
            intent.putExtra("recurMsg", recur_job_days_msg.getText().toString());
            intent.putExtra("daily_recur_pattern", new Gson().toJson
                    (new JobRecurModel(mode, startDate, numOfDays, endDate, occurances, endRecurMode)));
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
                    checkIfDateWeekendDay(currentDateString);
                else
                    end_date_by.setText(currentDateString);

                callApiForRecurMsg();

            }

        }, StartRecrCheck);
        datePicker.show(getFragmentManager(), dateView);
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

        } else if (RECRMSG == 3 && dailyMsgResModel.getOccurences() != null) {
            end_after_edt.setVisibility(View.VISIBLE);
            end_after_edt.setText(dailyMsgResModel.getOccurences());
        }


        if (RECRMSG == 1) {
            recur_job_days_msg.setText(LanguageController.getInstance().getMobileMsgByKey(AppConstant.custom_recur_msg1) + " " + every_recr_edt.getText().toString() + " " + LanguageController.getInstance().getMobileMsgByKey(AppConstant.starting_on) + " " + dailyMsgResModel.getStart_date() + " " +
                    LanguageController.getInstance().getMobileMsgByKey(AppConstant.custom_recur_msg2) + " " + LanguageController.getInstance().getMobileMsgByKey(AppConstant.infinity));
        } else {
            recur_job_days_msg.setText(LanguageController.getInstance().getMobileMsgByKey(AppConstant.custom_recur_msg1) + " " +
                    " " + every_recr_edt.getText().toString() + " " + LanguageController.getInstance().getMobileMsgByKey(AppConstant.starting_on) + " " + dailyMsgResModel.getStart_date() + " " +
                    LanguageController.getInstance().getMobileMsgByKey(AppConstant.custom_recur_msg2) + " " + end_after_edt.getText().toString() + " " + LanguageController.getInstance().getMobileMsgByKey(AppConstant.custom_recur_msg3) + " " + dailyMsgResModel.getEnd_date());
        }
    }

    /***Selected picker Date If Sunday or Saturday so reduce date *****/
    private void checkIfDateWeekendDay(String currentDateString) {
        Calendar startCal = Calendar.getInstance();

        try {
            startCal.setTime(new SimpleDateFormat("dd-MMM-yyyy", Locale.getDefault()).parse(currentDateString));
        } catch (ParseException e) {
            e.printStackTrace();
        }

        if (startCal.get(Calendar.DAY_OF_WEEK) == Calendar.SATURDAY) {
            startCal.add(Calendar.DATE, -1);  // number of days to add, can also use Calendar.DAY_OF_MONTH in place of Calendar.DATE
        } else if (startCal.get(Calendar.DAY_OF_WEEK) == Calendar.SUNDAY) {
            startCal.add(Calendar.DATE, -2);  // number of days to add, can also use Calendar.DAY_OF_MONTH in place of Calendar.DATE
        }

        end_date_by.setText(new SimpleDateFormat("dd-MMM-yyyy", Locale.getDefault()).format(startCal.getTime()));

    }


    private void setEVERY_DAYS_COUNTForEndAfter(boolean b) {
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

    /****update Every day recur EVERY_DAYS_COUNT***/
    private void setEVERY_DAYS_COUNTForEvery(boolean updateEVERY_DAYS_COUNT) {
        int EVERYDAYSCOUNT = 0;
        if (!every_recr_edt.getText().toString().equals(""))
            EVERYDAYSCOUNT = Integer.parseInt(every_recr_edt.getText().toString());
        if (updateEVERY_DAYS_COUNT) {
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

        if (buttonView.getId() == R.id.radio_everyDay) {
            if (isChecked) {
                radio_weekDay.setChecked(false);
                radio_everyDay.setChecked(true);
                parentViewEnabledisable(true);
            } else {
                radio_weekDay.setChecked(true);
                radio_everyDay.setChecked(false);
            }
        } else if (buttonView.getId() == R.id.radio_weekDay) {

            if (isChecked) {
                radio_weekDay.setChecked(true);
                radio_everyDay.setChecked(false);
                parentViewEnabledisable(false);
            } else {
                radio_weekDay.setChecked(false);
                radio_everyDay.setChecked(true);
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

    private void setViewVisibility(int i) {
        recur_job_days_msg.setVisibility(View.GONE);
        if (i == 1) {
            end_after_edt.removeTextChangedListener(this);
            end_after_layout.setBackgroundResource(R.drawable.layout_disable);
            end_date_by.setBackgroundResource(R.drawable.layout_disable);
            end_count_layout.setVisibility(View.INVISIBLE);
            end_after_edt.setVisibility(View.INVISIBLE);
            end_date_by.setClickable(false);
            RECRMSG = 1;
            end_after_edt.setText("");
            end_date_by.setText("");
            endRecurMode = "0";
            callApiForRecurMsg();
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


    private void parentViewEnabledisable(boolean enableDisablechild) {
        if (enableDisablechild) {
            daily_recr_every_layout.setBackgroundResource(R.drawable.edittext_shap_qus);
            every_img_layout.setVisibility(View.VISIBLE);
            every_recr_edt.setVisibility(View.VISIBLE);
            mode = "1";
            callApiForRecurMsg();
        } else {
            daily_recr_every_layout.setBackgroundResource(R.drawable.layout_disable);
            every_img_layout.setVisibility(View.INVISIBLE);
            every_recr_edt.setVisibility(View.INVISIBLE);
            mode = "2";
            callApiForRecurMsg();
        }

    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int EVERY_DAYS_COUNTs, int after) {

    }

    @Override
    public void onTextChanged(CharSequence charSequence, int start, int before, int EVERY_DAYS_COUNTs) {
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

            occurances = (RECRMSG == 2) ? end_after_edt.getText().toString() : "";

            if (RECRMSG == 3 && !end_date_by.getText().toString().equals("")) {
                Date end_Date = null;
                try {
                    end_Date = new SimpleDateFormat("dd-MMM-yyyy", Locale.getDefault()).parse(end_date_by.getText().toString());
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                endDate = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(end_Date);
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

            dailyRecrPi.getDailyRecurMsg(new DailyMsgReqModel("", schdlStart, mode,
                    startDate, every_recr_edt.getText().toString(), endDate, occurances, endRecurMode));
        }
    }


}



