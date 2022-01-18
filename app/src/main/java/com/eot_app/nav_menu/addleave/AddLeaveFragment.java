package com.eot_app.nav_menu.addleave;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.MenuItem;
import android.view.View;
import android.widget.DatePicker;
import android.widget.TimePicker;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import com.eot_app.R;
import com.eot_app.databinding.FragmentAddLeaveBinding;
import com.eot_app.utility.AppConstant;
import com.eot_app.utility.AppUtility;
import com.eot_app.utility.language_support.LanguageController;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.concurrent.Callable;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link AddLeaveFragment#} factory method to
 * create an instance of this fragment.
 */
public class AddLeaveFragment extends AppCompatActivity implements View.OnClickListener, TextWatcher {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    FragmentAddLeaveBinding binding;
    private String STARTSELCTEDATE = "", STARTSELCTETIME = " 12:00 am", ENDSELCTETIME = " 11:59 pm", ENDSELCTEDATE = "";
    private final DatePickerDialog.OnDateSetListener datePickerListener = new DatePickerDialog.OnDateSetListener() {
        @Override
        public void onDateSet(DatePicker view, int selectedYear, int selectedMonth, int selectedDay) {

            try {
                if (view.getTag().equals("time_from")) {
                    DateFormat formatter = new SimpleDateFormat("dd-MM-yyyy", Locale.US);//hh:mm:ss a
                    Date startDate = formatter.parse(selectedDay + "-" + (selectedMonth + 1) + "-" + selectedYear);
                    STARTSELCTEDATE = "";
                    STARTSELCTEDATE = " " + formatter.format(startDate);
                    binding.timeFrom.setText("");
                    binding.timeFrom.setText(STARTSELCTEDATE.concat(STARTSELCTETIME));
                    selectStartTime("START");


                } else if (view.getTag().equals("time_to")) {
                    DateFormat formatter = new SimpleDateFormat("dd-MM-yyyy", Locale.US);//hh:mm:ss a
                    Date startDate = formatter.parse(selectedDay + "-" + (selectedMonth + 1) + "-" + selectedYear);
                    ENDSELCTEDATE = "";
                    ENDSELCTEDATE = " " + formatter.format(startDate);
                    binding.timeTo.setText("");
                    binding.timeTo.setText(ENDSELCTEDATE.concat(ENDSELCTETIME));
                    selectStartTime("END");
                }
            } catch (ParseException e) {
                e.printStackTrace();
            }


        }
    };
    private AddLeaveViewModel addLeaveViewModel;
    private int year, month, day;

    public AddLeaveFragment() {
        // Required empty public constructor
    }


    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     * <p>
     * //     * @param param1 Parameter 1.
     * //     * @param param2 Parameter 2.
     *
     * @return A new instance of fragment AddLeaveFragment.
     */
    // TODO: Rename and change types and number of parameters
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTitle(LanguageController.getInstance().getMobileMsgByKey(AppConstant.add_leave));

        addLeaveViewModel = ViewModelProviders.of(this).get(AddLeaveViewModel.class);
        addLeaveViewModel.getFinishActivity().observe(this, new Observer<Boolean>() {
            @Override
            public void onChanged(@Nullable Boolean aBoolean) {
                if (aBoolean) {
                    try {
                        AppUtility.progressBarDissMiss();
                        if (aBoolean) {
                            setResult(RESULT_OK, new Intent());
                            finish();
                        }
                    } catch (Exception exception) {
                        exception.printStackTrace();
                    }
                    initilaleDateTimeset();
                    binding.notesEdt.setText("");
                    binding.reasonEdt.setText("");
                }
            }
        });


        addLeaveViewModel.getShowDialogs().observe(this, new Observer<String>() {
            @Override
            public void onChanged(String s) {
                AppUtility.progressBarDissMiss();
                showMyDialog(s);
            }
        });

        binding = DataBindingUtil.setContentView(this, R.layout.fragment_add_leave);


        setUiLables();

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }


    private void showMyDialog(String msg) {
        AppUtility.alertDialog(this, "",
                msg,
                LanguageController.getInstance().getMobileMsgByKey(AppConstant.ok), "", new Callable<Boolean>() {
                    @Override
                    public Boolean call() throws Exception {
                        return null;
                    }
                });
    }

    private void selectStartTime(final String tag) {
        Calendar mcurrentTime = Calendar.getInstance();
        int hour = mcurrentTime.get(Calendar.HOUR_OF_DAY);
        int minute = mcurrentTime.get(Calendar.MINUTE);
        TimePickerDialog mTimePicker;
        mTimePicker = new TimePickerDialog(this, new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker timePicker, int selectedHour, int selectedMinute) {
                String stime = updateTimeS(selectedHour, selectedMinute);
                if (tag.equals("START")) {
                    STARTSELCTETIME = "";
                    STARTSELCTETIME = " " + stime;
                    binding.timeFrom.setText(STARTSELCTEDATE.concat(STARTSELCTETIME));
                } else if (tag.equals("END")) {
                    ENDSELCTETIME = "";
                    ENDSELCTETIME = " " + stime;
                    binding.timeTo.setText(ENDSELCTEDATE.concat(ENDSELCTETIME));
                }
            }
        }, hour, minute, false);//Yes 24 hour time
        mTimePicker.setTitle("Select Time");
        mTimePicker.show();
    }

    public String updateTimeS(int hours, int mins) {

        String timeSet = "";
        if (hours > 12) {
            hours -= 12;
            timeSet = "PM";
        } else if (hours == 0) {
            hours += 12;
            timeSet = "AM";
        } else if (hours == 12)
            timeSet = "PM";
        else
            timeSet = "AM";

        String minutes = "";
        if (mins < 10)
            minutes = "0" + mins;
        else
            minutes = String.valueOf(mins);

        String aTime = new StringBuilder().append(hours).append(':')
                .append(minutes).append(" ").append(timeSet).toString();
        return aTime;


    }


    private void setUiLables() {

        initilaleDateTimeset();

        AppUtility.setupUI(binding.parentLayout, this);


        binding.notesEdt.setText("");
        binding.reasonEdt.setText("");

        binding.notesEdt.setHint(LanguageController.getInstance().getMobileMsgByKey(AppConstant.notes));
        binding.reasonEdt.setHint(LanguageController.getInstance().getMobileMsgByKey(AppConstant.add_reason_leave));

        binding.submitButton.setText(LanguageController.getInstance().getMobileMsgByKey(AppConstant.add_leave));
        createListners();
    }

    @Override
    public void onPause() {

        super.onPause();
    }

    private void initilaleDateTimeset() {
        binding.timeFrom.setText(AppUtility.getDateByFormat("dd-MM-yyyy").concat(STARTSELCTETIME));
        binding.timeTo.setText(AppUtility.getDateByFormat("dd-MM-yyyy").concat(ENDSELCTETIME));
        emptyfields();
    }

    private void createListners() {
        binding.timeFrom.setOnClickListener(this);
        binding.timeTo.setOnClickListener(this);

        binding.addNotesLayout.getEditText().addTextChangedListener(this);
        binding.addReasonLayout.getEditText().addTextChangedListener(this);

        binding.submitButton.setOnClickListener(this);
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.time_from:
                selectStartDate("time_from");
                break;
            case R.id.time_to:
                selectStartDate("time_to");
                break;
            case R.id.submit_button:
                Date startDate = null, endDate = null;
                String s = "", e = "", datetiemform;
                datetiemform = "dd-MM-yyyy hh:mm a";
                try {
                    startDate = new SimpleDateFormat(datetiemform, Locale.getDefault()).parse(binding.timeFrom.getText().toString().trim());
                    endDate = new SimpleDateFormat(datetiemform, Locale.getDefault()).parse(binding.timeTo.getText().toString().trim());
                    s = new SimpleDateFormat("dd-MM-yyyy hh:mm:ss", Locale.getDefault()).format(startDate);
                    e = new SimpleDateFormat("dd-MM-yyyy hh:mm:ss", Locale.getDefault()).format(endDate);
                    e = AppUtility.getDate(e);
                } catch (Exception exception) {
                    exception.getMessage();
                }

                if (!AppUtility.compareTwoDatesForTimeSheet2(s, e, "dd-MM-yyyy hh:mm:ss")) {
                    showMyDialog(LanguageController.getInstance().getMobileMsgByKey(AppConstant.time_sheet_date_error));
                } else {
                    AppUtility.progressBarShow(this);
                    addLeaveViewModel.getLeaveApiCall(binding.reasonEdt.getText().toString().trim(), binding.notesEdt.getText().toString().trim()
                            , s, e);
                }
                break;
        }

    }

    private void selectStartDate(String tag) {
        final Calendar c = Calendar.getInstance();
        year = c.get(Calendar.YEAR);
        month = c.get(Calendar.MONTH);
        day = c.get(Calendar.DAY_OF_MONTH);
        DatePickerDialog datePickerDialog = new DatePickerDialog(this, datePickerListener, year, month, day);
        datePickerDialog.getDatePicker().setTag(tag);
        datePickerDialog.show();
    }


    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence charSequence, int start, int before, int count) {
        if (charSequence.length() >= 1) {
            if (charSequence.hashCode() == binding.notesEdt.getText().hashCode())
                binding.addNotesLayout.setHintEnabled(true);
            if (charSequence.hashCode() == binding.reasonEdt.getText().hashCode())
                binding.addReasonLayout.setHintEnabled(true);
        } else if (charSequence.length() <= 0) {
            if (charSequence.hashCode() == binding.notesEdt.getText().hashCode())
                binding.addNotesLayout.setHintEnabled(false);
            if (charSequence.hashCode() == binding.reasonEdt.getText().hashCode())
                binding.addReasonLayout.setHintEnabled(false);
        }

    }

    @Override
    public void afterTextChanged(Editable s) {

    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            onBackPressed();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void emptyfields() {
        binding.notesEdt.setText("");
        binding.reasonEdt.setText("");

        binding.notesEdt.setHint(LanguageController.getInstance().getMobileMsgByKey(AppConstant.notes));
        binding.reasonEdt.setHint(LanguageController.getInstance().getMobileMsgByKey(AppConstant.add_reason_leave));
        binding.addNotesLayout.setHintEnabled(false);
        binding.addReasonLayout.setHintEnabled(false);

    }
}