package com.eot_app.nav_menu.quote.filter_quotes_pkg;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import com.eot_app.R;
import com.eot_app.home_screens.MainActivity;
import com.eot_app.nav_menu.jobs.job_filter.FilterListAdapter;
import com.eot_app.nav_menu.quote.filter_quotes_pkg.quote_filter_model.QuoteFilter_State_Model;
import com.eot_app.nav_menu.quote.filter_quotes_pkg.quote_mvp.QuoteFilter_Pc;
import com.eot_app.nav_menu.quote.filter_quotes_pkg.quote_mvp.QuoteFilter_Pi;
import com.eot_app.nav_menu.quote.filter_quotes_pkg.quote_mvp.QuoteFilter_View;
import com.eot_app.nav_menu.quote.quotes_list_pkg.QuotesFilter;
import com.eot_app.utility.AppConstant;
import com.eot_app.utility.AppUtility;
import com.eot_app.utility.DropdownListBean;
import com.eot_app.utility.language_support.LanguageController;
import com.eot_app.utility.util_interfaces.MySpinnerAdapter;
import com.google.gson.Gson;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Set;

public class QuotesFilter_Activity extends AppCompatActivity implements View.OnClickListener, QuoteFilter_View {
    private final ArrayList<DropdownListBean> status_SElectes_list = new ArrayList<>();
    //private ArrayList<QuotesFilter> listBeans = new ArrayList<>();
    private final ArrayList<DropdownListBean> filterListData = new ArrayList<>();
    private final String Start_Date = "StartDate";
    private final String End_Date = "EndDate";
    private final List<String> statusIdList = new ArrayList<>();
    private final String[] dateArray = {"Today", "Yesterday", "Last 7 Days", "Last 30 Days", "This Month", "Last Month", "Custom Range"};
    String dtf = "", dtt = "", sDtf = "00:00:00", eDtf = "23:59:59";
    private Calendar cal;
    private TextView fil_status_hint, date_hint_txt;
    private TextView status_name, date_txt;
    private LinearLayout fil_status_layout, date_LinearLayout;
    private Spinner fil_status_spinner, date_spinner;
    private QuoteFilter_Pi quoteFilterPi;
    private Button filter_btn, reset_btn;
    private RelativeLayout dateLayout;
    private Calendar myCalendar;
    private TextView start_date, end_date;
    private final DatePickerDialog.OnDateSetListener datePickerListener = new DatePickerDialog.OnDateSetListener() {
        @Override
        public void onDateSet(DatePicker view, int selectedYear, int selectedMonth, int selectedDay) {
            String dateselect = "";
            try {
                DateFormat formatter = new SimpleDateFormat("dd-MM-yyy");//hh:mm:ss a
                Date startDate = formatter.parse(selectedDay + "-" + (selectedMonth + 1) + "-" + selectedYear);
                dateselect = formatter.format(startDate);
            } catch (ParseException e) {
                e.printStackTrace();
            }
            DateFormat dateFormat = new SimpleDateFormat(
                    AppUtility.dateTimeByAmPmFormate("hh:mm:ss a", "kk:mm:ss "), Locale.US);//append current time
            dateFormat.format(new Date());
            String tag = ((String) view.getTag());
            if (tag.equals(Start_Date)) {
                dtf = dateselect + " " + sDtf;
                start_date.setText(dateselect);
            } else if (tag.equals(End_Date)) {
                dtt = dateselect + " " + eDtf;
                end_date.setText(dateselect);
            }
        }
    };
    private String dateFliterNm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_filter);
        setTitle(LanguageController.getInstance().getMobileMsgByKey(AppConstant.filter));

        initializelables();

    }

    private void initializelables() {
        fil_status_spinner = findViewById(R.id.fil_status_spinner);
        fil_status_layout = findViewById(R.id.fil_status_layout);
        fil_status_hint = findViewById(R.id.fil_status_hint);
        status_name = findViewById(R.id.status_name);
        status_name.setHint(LanguageController.getInstance().getMobileMsgByKey(AppConstant.quote_status));

        date_LinearLayout = findViewById(R.id.date_LinearLayout);
        date_hint_txt = findViewById(R.id.date_hint_txt);

        date_txt = findViewById(R.id.date_txt);
        date_txt.setHint(LanguageController.getInstance().getMobileMsgByKey(AppConstant.quote_date));

        date_spinner = findViewById(R.id.date_spinner);

        dateLayout = findViewById(R.id.dateLayout);

        start_date = findViewById(R.id.start_date);
        start_date.setHint(LanguageController.getInstance().getMobileMsgByKey(AppConstant.date_form));

        end_date = findViewById(R.id.end_date);
        end_date.setHint(LanguageController.getInstance().getMobileMsgByKey(AppConstant.date_form));


        filter_btn = findViewById(R.id.filter_btn);
        filter_btn.setText(LanguageController.getInstance().getMobileMsgByKey(AppConstant.filter));

        reset_btn = findViewById(R.id.reset_btn);
        reset_btn.setText(LanguageController.getInstance().getMobileMsgByKey(AppConstant.reset));


        intializeViews();
    }

    private void intializeViews() {
        quoteFilterPi = new QuoteFilter_Pc(this);
        quoteFilterPi.getQuotesStatesList();
        setQuoteDateList();


        fil_status_layout.setOnClickListener(this);
        date_LinearLayout.setOnClickListener(this);
        filter_btn.setOnClickListener(this);
        reset_btn.setOnClickListener(this);

        start_date.setOnClickListener(this);
        end_date.setOnClickListener(this);

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.fil_status_layout:
                fil_status_spinner.performClick();
                break;
            case R.id.date_LinearLayout:
                date_spinner.performClick();
                break;
            case R.id.reset_btn:
                quoteFilterPi.getQuotesStatesList();
                resetButtonClearAll();
                break;
            case R.id.start_date:
                SelectStartDate("StartDate");
                break;
            case R.id.end_date:
                SelectStartDate("EndDate");
                break;
            case R.id.filter_btn:
                for (DropdownListBean list : status_SElectes_list) {
                    statusIdList.add(list.getKey());
                }

                QuotesFilter filter = new QuotesFilter("", dtf, dtt, statusIdList, dateFliterNm);// date_txt.getText().toString()


                //   if (filter != null) {
                if (!dtf.equals("") || !statusIdList.isEmpty()) {
                    Intent intent = new Intent();
                    // intent.putExtra("filterlist", filter);
                    String str = new Gson().toJson(filter);
                    intent.putExtra("filterlist", str);
                    setResult(MainActivity.FILTERQUOTESLIST, intent);
                    this.finish();
                } else {
                    quoteFilterPi.emptyFilterListDialog();
                }
                break;
        }
    }

    private void SelectStartDate(String tag) {
        myCalendar = Calendar.getInstance();
        int year = myCalendar.get(Calendar.YEAR);
        int month = myCalendar.get(Calendar.MONTH);
        int dayOfMonth = myCalendar.get(Calendar.DAY_OF_MONTH);
        DatePickerDialog datePickerDialog = new DatePickerDialog(this, datePickerListener, year, month, dayOfMonth);
        datePickerDialog.getDatePicker().setTag(tag);
        datePickerDialog.show();
    }

    private void setQuoteDateList() {
        AppUtility.spinnerPopUpWindow(date_spinner);
        date_spinner.setAdapter(new MySpinnerAdapter(this, dateArray));
        date_spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int pos, long l) {
                date_hint_txt.setVisibility(View.VISIBLE);
                date_hint_txt.setText(LanguageController.getInstance().getMobileMsgByKey(AppConstant.quote_date));
                date_txt.setText(dateArray[pos]);
                //dateFliterNm=date
                getDateForFilter(pos);
            }


            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
    }


    private void resetButtonClearAll() {
        status_SElectes_list.clear();
        dtt = dtf = "";

        status_name.setText("");
        status_name.setHint(LanguageController.getInstance().getMobileMsgByKey(AppConstant.quote_status));
        fil_status_hint.setVisibility(View.GONE);

        date_txt.setText("");
        date_txt.setHint(LanguageController.getInstance().getMobileMsgByKey(AppConstant.quote_date));
        date_hint_txt.setVisibility(View.GONE);

        dateLayout.setVisibility(View.GONE);//hide custom range
        start_date.setText("");
        end_date.setText("");


    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private void getDateForFilter(int pos) {
        dateFliterNm = date_txt.getText().toString();
        switch (pos) {
            case 0:
                dateLayout.setVisibility(View.GONE);
                getCurrentDate();
                break;
            case 1:
                dateLayout.setVisibility(View.GONE);
                // getYesterDayDate(-1);//get yesterday date
                getYestrerDayDate();
                break;
            case 2:
                dateLayout.setVisibility(View.GONE);
                getYesterDayDate(-6);//get last 7 days
                break;
            case 3:
                dateLayout.setVisibility(View.GONE);
                getYesterDayDate(-29);//get Last 30 Days
                break;
            case 4:
                dateLayout.setVisibility(View.GONE);
                getThisMonthDate();
                break;
            case 5:
                dateLayout.setVisibility(View.GONE);
                getLastMonthDate();
                break;
            case 6://get custom date from picker
                dateFliterNm = "";
                dateLayout.setVisibility(View.VISIBLE);
                break;
        }

    }

    private void getLastMonthDate() {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.US);
        cal = Calendar.getInstance();// add -1 month to current month

        cal.add(Calendar.MONTH, -1);// set DATE to 1, so first date of previous month
        cal.set(Calendar.DATE, 1);

        dtf = dateFormat.format(cal.getTime()) + " " + sDtf;

// set actual maximum date of previous month
        cal.set(Calendar.DATE, cal.getActualMaximum(Calendar.DAY_OF_MONTH));

        dtt = dateFormat.format(cal.getTime()) + " " + eDtf;
    }


    private void getThisMonthDate() {
//{"limit":10,"index":0,"status":[],"dtf":"2019-08-01 00:00:00","dtt":"2019-08-31 23:59:59"}:
        cal = Calendar.getInstance();
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.US);
        cal.set(Calendar.DAY_OF_MONTH, cal.getActualMinimum(Calendar.DAY_OF_MONTH));
        dtf = dateFormat.format(cal.getTime()) + " " + sDtf;
        Date today = new Date();
        cal.setTime(today);
        cal.add(Calendar.MONTH, 1);
        cal.set(Calendar.DAY_OF_MONTH, 1);
        cal.add(Calendar.DATE, -1);
        Date lastDayOfMonth = cal.getTime();
        dtt = dateFormat.format(lastDayOfMonth) + " " + eDtf;
    }

    private void getYestrerDayDate() {
        cal = Calendar.getInstance();
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.US);
        cal.add(Calendar.MONTH, 0);
        cal.add(Calendar.DATE, -1);
        dtt = dateFormat.format(cal.getTime()) + " " + eDtf;
        dtf = dateFormat.format(cal.getTime()) + " " + sDtf;
    }

    private void getYesterDayDate(int yesterDay) {
        cal = Calendar.getInstance();
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.US);
        cal.add(Calendar.MONTH, 0);
        dtt = dateFormat.format(cal.getTime()) + " " + eDtf;
        cal.add(Calendar.DATE, yesterDay);
        dtf = dateFormat.format(cal.getTime()) + " " + sDtf;
    }

    private void getCurrentDate() {//get Today date
        cal = Calendar.getInstance();
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd", Locale.US);
        dtt = df.format(cal.getTime());
        dtt = dtt + " " + eDtf;
        dtf = df.format(cal.getTime()) + " " + sDtf;
    }


    @Override
    public void setQuoteSStatesList(final ArrayList<QuoteFilter_State_Model> quoteStatusList) {
        AppUtility.spinnerPopUpWindow(fil_status_spinner);
        FilterListAdapter statusFilterAdapter = new
                FilterListAdapter(this, quoteStatusList, new FilterListAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(Set<String> data, Set<DropdownListBean> beanList) {
                if (data.size() >= 4)
                    status_name.setText(LanguageController.getInstance().getMobileMsgByKey(AppConstant.items_selected) + " " + data.size());
                else
                    status_name.setText(data.toString().replace("[", "").replace("]", ""));
                if (data.size() > 0) {
                    fil_status_hint.setVisibility(View.VISIBLE);
                    fil_status_hint.setText(LanguageController.getInstance().getMobileMsgByKey(AppConstant.quote_status));
                } else fil_status_hint.setVisibility(View.INVISIBLE);
                status_SElectes_list.clear();
                status_SElectes_list.addAll(beanList);

            }
        });
        fil_status_spinner.setAdapter(statusFilterAdapter);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                AppUtility.hideSoftKeyboard(this);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }
}
