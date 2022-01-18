package com.eot_app.nav_menu.report;

import android.content.Context;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
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
import com.eot_app.databinding.FragmentReportBinding;
import com.eot_app.nav_menu.jobs.add_job.add_job_recr.dateTime_pkg.DateTimeCallBack;
import com.eot_app.nav_menu.jobs.add_job.add_job_recr.dateTime_pkg.DateTimeDiloag;
import com.eot_app.utility.AppConstant;
import com.eot_app.utility.AppUtility;
import com.eot_app.utility.language_support.LanguageController;

import java.util.concurrent.Callable;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ReportFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ReportFragment extends Fragment implements View.OnClickListener {

    public static final int REQUEST_CODE = 11; // Used to identify the result
    public static final int REPORT_TIMESHEET = 555;
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    FragmentReportBinding reportBinding;

    boolean PERMMISSIONALLOW = false;
    ReportViewModel reportViewModel;
    String selectedDate;
    String showDate;
    String fromDate1 = "", toDate1 = "";
    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private int year, month, day, mHour, mMinute;
    private boolean StartRecrCheck;
    private OnFragmentInteractionListener mListener;

    public ReportFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment ReportFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static ReportFragment newInstance(String param1, String param2) {
        ReportFragment fragment = new ReportFragment();
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


    // TODO: Rename and change types of parameters

    public void setPERMMISSIONALLOW(boolean PERMMISSIONALLOW) {
        this.PERMMISSIONALLOW = PERMMISSIONALLOW;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        reportViewModel = ViewModelProviders.of(getActivity()).get(ReportViewModel.class);
        reportViewModel.getFinishActivity().observe(this, new Observer<Boolean>() {
            @Override
            public void onChanged(@Nullable Boolean aBoolean) {
                if (aBoolean) {
                    reportBinding.timeFrom.setText("");
                    reportBinding.timeTo.setText("");
                }
            }
        });
    }

    private void clickListners() {
        reportBinding.downloadReport.setOnClickListener(this);
        reportBinding.llTimereportTo.setOnClickListener(this);
        reportBinding.fromReportLay.setOnClickListener(this);
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

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.download_report:
                if (reportBinding.timeFrom.getText().toString().trim().equals("") && reportBinding.timeTo.getText().toString().trim().equals("")) {
                    showAlertDialog(LanguageController.getInstance().getMobileMsgByKey(AppConstant.select_date_range));
                } else if (reportBinding.timeFrom.getText().toString().trim().equals("")) {
                    showAlertDialog(LanguageController.getInstance().getMobileMsgByKey(AppConstant.from_date_error));
                } else if (reportBinding.timeTo.getText().toString().trim().equals("")) {
                    showAlertDialog(LanguageController.getInstance().getMobileMsgByKey(AppConstant.to_date_error));
                } else if (!AppUtility.compareTwoDatesForTimeSheet(reportBinding.timeFrom.getText().toString().trim(), reportBinding.timeTo.getText().toString().trim(), "dd-MMM-yyyy")) {
                    showAlertDialog(LanguageController.getInstance().getMobileMsgByKey(AppConstant.time_report_date_error));
                } else {
                    Log.e("", "");
                    if (PERMMISSIONALLOW)
                        reportViewModel.generateReport(reportBinding.timeFrom.getText().toString().trim(), reportBinding.timeTo.getText().toString().trim());
                    else {
                        requestPermissionAndContinue();//("You have need internal storage permission for download Report");
                    }
                    Log.e("", "");
                }
                break;
            case R.id.from_report_lay:
                getDatePickerDialog("time_from");

                break;
            case R.id.ll_timereport_to:
                getDatePickerDialog("time_to");

                break;
        }
    }

    private void getDatePickerDialog(final String dateView) {
        DialogFragment datePicker = new DateTimeDiloag(new DateTimeCallBack() {
            @Override
            public void getDateTimeFromPicker(String currentDateString) {
                if (dateView.equals("time_from")) {
                    reportBinding.timeFrom.setText(currentDateString);
                    reportBinding.fromLable.setVisibility(View.VISIBLE);

                } else if (dateView.equals("time_to")) {
                    reportBinding.timeTo.setText(currentDateString);
                    reportBinding.toLable.setVisibility(View.VISIBLE);
                }
            }

        }, false);
        datePicker.show(getFragmentManager(), dateView);
    }


    private void setUiLables() {
        reportBinding.downloadReportHeader.setText(LanguageController.getInstance().getMobileMsgByKey(AppConstant.download_report));
        reportBinding.timeReportSubHeader.setText(LanguageController.getInstance().getMobileMsgByKey(AppConstant.download_report_lable_stext));
        reportBinding.timeFrom.setHint(LanguageController.getInstance().getMobileMsgByKey(AppConstant.report_from));
        reportBinding.timeTo.setHint(LanguageController.getInstance().getMobileMsgByKey(AppConstant.report_to));
        reportBinding.submitBtn.setText(LanguageController.getInstance().getMobileMsgByKey(AppConstant.download_report));
        reportBinding.fromLable.setHint(LanguageController.getInstance().getMobileMsgByKey(AppConstant.report_from));
        reportBinding.toLable.setHint(LanguageController.getInstance().getMobileMsgByKey(AppConstant.report_to));

        reportViewModel.setContext(getActivity());


        clickListners();
        requestPermissionAndContinue();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        reportBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_report, container, false);
        setUiLables();

        return reportBinding.getRoot();

        /*eotServices/UserController/generateCheckInOutPDF
           param=> {"dtf":"2021-07-08","dtt":"2021-08-06","usrId":["994"]}*/

        /*{"dtf":"01/08/2021","dtt":"10/08/2021","usrId":"967"}*/
    }


    private void requestPermissionAndContinue() {
        if (Build.VERSION.SDK_INT >= 23) {
            String[] PERMISSIONS = {android.Manifest.permission.WRITE_EXTERNAL_STORAGE};
            if (!hasPermissions(getActivity(), PERMISSIONS)) {
                ActivityCompat.requestPermissions(getActivity(), PERMISSIONS, REPORT_TIMESHEET);
            } else {
                PERMMISSIONALLOW = true;
            }
        } else {

            PERMMISSIONALLOW = true;
        }
    }


    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }

}