package com.eot_app.nav_menu.usr_time_sheet_pkg;

import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import com.eot_app.R;
import com.eot_app.databinding.FragmentTimeSheetBinding;
import com.eot_app.nav_menu.jobs.add_job.add_job_recr.dateTime_pkg.DateTimeCallBack;
import com.eot_app.nav_menu.jobs.add_job.add_job_recr.dateTime_pkg.DateTimeDiloag;
import com.eot_app.utility.AppConstant;
import com.eot_app.utility.AppUtility;
import com.eot_app.utility.language_support.LanguageController;

import java.util.concurrent.Callable;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link TimeSheetFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class TimeSheetFragment extends Fragment implements View.OnClickListener {
    public static final int TIMESHEET = 666;
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    TimeSheetFragViewModel timeSheetFragViewModel;
    FragmentTimeSheetBinding binding;

    boolean SHEETPERMMISSIONALLOW = false;
    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public TimeSheetFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment TimeSheetFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static TimeSheetFragment newInstance(String param1, String param2) {
        TimeSheetFragment fragment = new TimeSheetFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    private boolean hasPermissions(Context context, String... permissions) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && context != null && permissions != null) {
            for (String permission : permissions) {
                if (ActivityCompat.checkSelfPermission(context, permission) != PackageManager.PERMISSION_GRANTED) {
                    return false;
                }
            }
        }
        return true;
    }

    private void getDatePickerDialog(final String dateView) {
        DialogFragment datePicker = new DateTimeDiloag(new DateTimeCallBack() {
            @Override
            public void getDateTimeFromPicker(String currentDateString) {
                if (dateView.equals("time_from")) {
                    binding.timeFrom.setText(currentDateString);
                    binding.fromLable.setVisibility(View.VISIBLE);
                } else if (dateView.equals("time_to")) {
                    binding.timeTo.setText(currentDateString);
                    binding.toLable.setVisibility(View.VISIBLE);
                }
            }

        }, false);
        datePicker.show(getFragmentManager(), dateView);
    }

    public void setSHEETPERMMISSIONALLOW(boolean SHEETPERMMISSIONALLOW) {
        this.SHEETPERMMISSIONALLOW = SHEETPERMMISSIONALLOW;
    }

    private void requestPermissionAndContinue() {
        if (Build.VERSION.SDK_INT >= 23) {
            String[] PERMISSIONS = {android.Manifest.permission.WRITE_EXTERNAL_STORAGE};
            if (!hasPermissions(getActivity(), PERMISSIONS)) {
                ActivityCompat.requestPermissions(getActivity(), PERMISSIONS, TIMESHEET);
            } else {
                SHEETPERMMISSIONALLOW = true;
            }
        } else {
            SHEETPERMMISSIONALLOW = true;
        }
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        timeSheetFragViewModel = ViewModelProviders.of(getActivity()).get(TimeSheetFragViewModel.class);


        timeSheetFragViewModel.getFinishActivity().observe(this, new Observer<Boolean>() {
            @Override
            public void onChanged(@Nullable Boolean aBoolean) {
                if (aBoolean) {
                    binding.timeFrom.setText("");
                    binding.timeTo.setText("");
                    binding.fromLable.setVisibility(View.GONE);
                    binding.toLable.setVisibility(View.GONE);
                }
            }
        });

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_time_sheet, container, false);
        setUiLables();

        requestPermissionAndContinue();
        return binding.getRoot();
    }

    private void showAlertDialog(String msg) {
        AppUtility.error_Alert_Dialog(getActivity(), msg, LanguageController.getInstance().getMobileMsgByKey(AppConstant.ok)
                , new Callable<Boolean>() {
                    @Override
                    public Boolean call() throws Exception {
                        return null;
                    }
                });
    }

    private void clickListners() {
        binding.downloadTimeSheet.setOnClickListener(this);
        binding.llTimesheetTo.setOnClickListener(this);
        binding.linearFromdateLay.setOnClickListener(this);
    }

    private void setUiLables() {
        binding.downloadSheetHeader.setText(LanguageController.getInstance().getMobileMsgByKey(AppConstant.time_sheet_header));
        binding.timeSheetSubHeader.setText(LanguageController.getInstance().getMobileMsgByKey(AppConstant.time_sheet_sub_header));
        binding.timeFrom.setHint(LanguageController.getInstance().getMobileMsgByKey(AppConstant.time_sheet_from));
        binding.timeTo.setHint(LanguageController.getInstance().getMobileMsgByKey(AppConstant.time_sheet_to));
        binding.submitBtn.setText(LanguageController.getInstance().getMobileMsgByKey(AppConstant.time_sheet_header));
        binding.fromLable.setHint(LanguageController.getInstance().getMobileMsgByKey(AppConstant.time_sheet_from));
        binding.toLable.setHint(LanguageController.getInstance().getMobileMsgByKey(AppConstant.time_sheet_to));

        timeSheetFragViewModel.setContext(getActivity());
        clickListners();

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.download_timeSheet:
                if (binding.timeFrom.getText().toString().trim().equals("") && binding.timeTo.getText().toString().trim().equals("")) {
                    showAlertDialog(LanguageController.getInstance().getMobileMsgByKey(AppConstant.select_date_range));
                } else if (binding.timeFrom.getText().toString().trim().equals("")) {
                    showAlertDialog(LanguageController.getInstance().getMobileMsgByKey(AppConstant.from_date_error));
                } else if (binding.timeTo.getText().toString().trim().equals("")) {
                    showAlertDialog(LanguageController.getInstance().getMobileMsgByKey(AppConstant.to_date_error));
                } else if (!AppUtility.compareTwoDatesForTimeSheet(binding.timeFrom.getText().toString().trim(), binding.timeTo.getText().toString().trim(), "dd-MMM-yyyy")) {
                    showAlertDialog(LanguageController.getInstance().getMobileMsgByKey(AppConstant.time_sheet_date_error));
                } else {
                    if (SHEETPERMMISSIONALLOW)
                        timeSheetFragViewModel.generatetimeSheet(binding.timeFrom.getText().toString().trim(),
                                binding.timeTo.getText().toString().trim());
                    else requestPermissionAndContinue();
                }
                break;
            case R.id.linear_fromdate_lay:
                getDatePickerDialog("time_from");
                break;
            case R.id.ll_timesheet_to:
                getDatePickerDialog("time_to");

                break;
        }
    }


}