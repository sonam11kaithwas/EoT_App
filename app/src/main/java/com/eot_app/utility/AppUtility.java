package com.eot_app.utility;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.location.LocationManager;
import android.media.ExifInterface;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.os.PowerManager;
import android.provider.Settings;
import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.AutoCompleteTextView;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;

import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.eot_app.R;
import com.eot_app.nav_menu.admin_fw_chat_pkg.sonam_user_user_chat_pkg.user_chat_controller.UserToUserChatController;
import com.eot_app.nav_menu.jobs.add_job.Add_job_activity;
import com.eot_app.nav_menu.jobs.job_detail.invoice.invoice_detail_pkg.inv_detail_model.Tax;
import com.eot_app.utility.db.AppDataBase;
import com.eot_app.utility.language_support.LanguageController;
import com.eot_app.utility.util_interfaces.Callback_AlertDialog;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.hypertrack.hyperlog.HyperLog;

import org.jsoup.helper.StringUtil;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.lang.reflect.Field;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.TimeZone;
import java.util.concurrent.Callable;

import okhttp3.RequestBody;

/**
 * Created by geet-pc on 18/5/18.
 */

public class AppUtility {
    //    public static String taxCalculationType = "0";
    private static final int UPLOAD_FILE = 1;
    public static int SELECT_CAMERA_PICTURE = 1001;
    public static String captureImagePath = "";
    private static int year, month, day, mHour, mMinute;
    private static String dateStr;
    private static String timeStr, timeEnd;
    private static Date endDate, startDate = null;
    private static ProgressDialog progressDialog;

    //*********** Show alert to Ignore battery optimization **************
    public static void showAlertForIgnoreBatteryOptimize(Context activityContext) {
        try {
            if (App_preference.getSharedprefInstance().getBatteryRequest() != 1) {
                if (Build.VERSION.SDK_INT >= 23) {
                    Intent intent = new Intent();
                    String packageName = activityContext.getPackageName();
                    PowerManager pm = (PowerManager) activityContext.getSystemService(Context.POWER_SERVICE);
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                        if (pm.isIgnoringBatteryOptimizations(packageName)) {
                            intent.setAction(Settings.ACTION_IGNORE_BATTERY_OPTIMIZATION_SETTINGS);
                        } else {
                            intent.setAction(Settings.ACTION_REQUEST_IGNORE_BATTERY_OPTIMIZATIONS);
                            intent.setData(Uri.parse("package:" + packageName));
                        }
                    }
                    App_preference.getSharedprefInstance().setBatteryRequest(1);
                    activityContext.startActivity(intent);
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static ArrayList<String> auditStatusList() {
        //1-new,8-on hold ,7-In progress,9-complete

        ArrayList<String> statusList = new ArrayList<>();
        statusList.add("1");
        statusList.add("8");
        statusList.add("7");
        statusList.add("9");
        return statusList;
    }

    public static ArrayList<String> getJobStatusList() {
        // {"1-new", "2", "3", "4", "5", "6", "7", "8", "9", "10", "11-multi", "12-on hold"};

        ArrayList<String> jobStatusList = new ArrayList<>();
        jobStatusList.add("1");
        jobStatusList.add("2");
        jobStatusList.add("3");
        jobStatusList.add("4");
        jobStatusList.add("5");
        jobStatusList.add("6");
        jobStatusList.add("7");
        jobStatusList.add("8");
        jobStatusList.add("9");
        jobStatusList.add("10");
        jobStatusList.add("11");
        jobStatusList.add("12");
        return jobStatusList;
    }

    public static String getDate(long milliSeconds, String formate) { //Convert milisecond to Date time for chat
        SimpleDateFormat formatter = new SimpleDateFormat(formate, Locale.US);
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(milliSeconds * 1000);
        return formatter.format(calendar.getTime());
    }

    public static boolean isGPSEnabled() {
        LocationManager locationManager = (LocationManager) EotApp.getAppinstance().getSystemService(Context.LOCATION_SERVICE);
        return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
    }

    /****get Yesterday Date*****/
    public static String getYesterDayDate(String formate, int decrease) {
        DateFormat dateFormat = new SimpleDateFormat(formate, Locale.US);
        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.DATE, decrease);
        return dateFormat.format(cal.getTime()); //your formatted date here
    }

    //start date,time must be grater than to end date time
    public static boolean compareTwoDates(String schdlStart, String schdlFinish) {
        Locale.getDefault().getDisplayLanguage();
        try {
            SimpleDateFormat gettingfmt = new SimpleDateFormat(
                    AppUtility.dateTimeByAmPmFormate("dd-MM-yyyy hh:mm a", "dd-MM-yyyy kk:mm"), Locale.US);//, Locale.US
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

    public static boolean compareTwoDatesForTimeSheet2(String fromDate, String toDate, String formate) {
        Locale.getDefault().getDisplayLanguage();
        try {
            SimpleDateFormat gettingfmt = new SimpleDateFormat(
                    formate, Locale.getDefault());//, Locale.US
            Date date = gettingfmt.parse(fromDate);
            date.getTime();

            Date date1 = gettingfmt.parse(toDate);
            date1.getTime();
            if (date1.getTime() > date.getTime()) {
                return true;
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    public static boolean compareTwoDatesForTimeSheet(String fromDate, String toDate, String formate) {
        Locale.getDefault().getDisplayLanguage();
        try {
            SimpleDateFormat gettingfmt = new SimpleDateFormat(
                    formate, Locale.getDefault());//, Locale.US
            Date date = gettingfmt.parse(fromDate);
            date.getTime();

            Date date1 = gettingfmt.parse(toDate);
            date1.getTime();
            if (date1.getTime() > date.getTime()) {
                return true;
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    public static boolean isInternetConnected() {
        ConnectivityManager cm = (ConnectivityManager) EotApp.getAppinstance().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = cm.getActiveNetworkInfo();
        return networkInfo != null && networkInfo.isConnected();
    }

    public static String getDate(long milliSeconds) { //Convert milisecond to Date time for chat
        try {
            SimpleDateFormat formatter = new SimpleDateFormat(
                    AppUtility.dateTimeByAmPmFormate("dd/MMM/yyyy hh:mm a", "dd/MMM/yyyy kk:mm"), Locale.US);
            Calendar calendar = Calendar.getInstance();
            calendar.setTimeInMillis(milliSeconds * 1000);
            return formatter.format(calendar.getTime());
        } catch (Exception ex) {
            ex.getMessage();
        }
        return "";
    }

    public static String getDateWithFormate(long milliSeconds, String formate) { //Convert milisecond to Date time for chat
        try {
            SimpleDateFormat formatter = new SimpleDateFormat(formate);
            Calendar calendar = Calendar.getInstance();
            calendar.setTimeInMillis(milliSeconds * 1000);
            return formatter.format(calendar.getTime());
        } catch (Exception ex) {
            ex.getMessage();
        }
        return "";
    }

    public static String getDateWithFormate2(long milliSeconds, String formate) { //Convert milisecond to Date time for chat
        SimpleDateFormat formatter = new SimpleDateFormat(formate, Locale.US);
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(milliSeconds);
        return formatter.format(calendar.getTime());
    }

    public static String getDateByMiliseconds() {
        String str = "";
        try {
            long timeInMilliSecond = new Date().getTime();
            str = String.valueOf(timeInMilliSecond / 1000);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return str;
    }

    public static String getDateByFormat(String format) { //get current date time
        /******not work default language so always set US langauge for this request*****/
        SimpleDateFormat gettingfmt = new SimpleDateFormat(format, Locale.US);
        String formated = gettingfmt.format(Calendar.getInstance().getTime());
        return formated;
    }


    public static String getCurrentDateByFormat(String format) { //get current date time
        /******not work default language so always set US langauge for this request*****/
        SimpleDateFormat gettingfmt = new SimpleDateFormat(format, Locale.getDefault());
        String formated = gettingfmt.format(Calendar.getInstance().getTime());
        return formated;
    }

    /**
     * <p>Checks if two dates are on the same day ignoring time.</p>
     *
     * @param date1 the first date, not altered, not null
     * @param date2 the second date, not altered, not null
     * @return true if they represent the same day
     * @throws IllegalArgumentException if either date is <code>null</code>
     */
    public static boolean isSameDay(Date date1, Date date2) {
        if (date1 == null || date2 == null) {
            throw new IllegalArgumentException("The dates must not be null");
        }
        Calendar cal1 = Calendar.getInstance();
        cal1.setTime(date1);
        Calendar cal2 = Calendar.getInstance();
        cal2.setTime(date2);
        return isSameDay(cal1, cal2);
    }

    /**
     * <p>Checks if two calendars represent the same day ignoring time.</p>
     *
     * @param cal1 the first calendar, not altered, not null
     * @param cal2 the second calendar, not altered, not null
     * @return true if they represent the same day
     * @throws IllegalArgumentException if either calendar is <code>null</code>
     */
    public static boolean isSameDay(Calendar cal1, Calendar cal2) {
        if (cal1 == null || cal2 == null) {
            throw new IllegalArgumentException("The dates must not be null");
        }
        return (cal1.get(Calendar.ERA) == cal2.get(Calendar.ERA) &&
                cal1.get(Calendar.YEAR) == cal2.get(Calendar.YEAR) &&
                cal1.get(Calendar.DAY_OF_YEAR) == cal2.get(Calendar.DAY_OF_YEAR));
    }

    /**
     * <p>Checks if a date is today.</p>
     *
     * @param date the date, not altered, not null.
     * @return true if the date is today.
     * @throws IllegalArgumentException if the date is <code>null</code>
     */
    public static boolean isToday(Date date) {
        return isSameDay(date, Calendar.getInstance().getTime());
    }

    /**
     * <p>Checks if the first date is after the second date ignoring time.</p>
     *
     * @param date1 the first date, not altered, not null
     * @param date2 the second date, not altered, not null
     * @return true if the first date day is after the second date day.
     * @throws IllegalArgumentException if the date is <code>null</code>
     */
    public static boolean isAfterDay(Date date1, Date date2) {
        if (date1 == null || date2 == null) {
            throw new IllegalArgumentException("The dates must not be null");
        }
        Calendar cal1 = Calendar.getInstance();
        cal1.setTime(date1);
        Calendar cal2 = Calendar.getInstance();
        cal2.setTime(date2);
        return isAfterDay(cal1, cal2);
    }

    /**
     * <p>Checks if the first calendar date is after the second calendar date ignoring time.</p>
     *
     * @param cal1 the first calendar, not altered, not null.
     * @param cal2 the second calendar, not altered, not null.
     * @return true if cal1 date is after cal2 date ignoring time.
     * @throws IllegalArgumentException if either of the calendars are <code>null</code>
     */
    public static boolean isAfterDay(Calendar cal1, Calendar cal2) {
        if (cal1 == null || cal2 == null) {
            throw new IllegalArgumentException("The dates must not be null");
        }
        if (cal1.get(Calendar.ERA) < cal2.get(Calendar.ERA)) return false;
        if (cal1.get(Calendar.ERA) > cal2.get(Calendar.ERA)) return true;
        if (cal1.get(Calendar.YEAR) < cal2.get(Calendar.YEAR)) return false;
        if (cal1.get(Calendar.YEAR) > cal2.get(Calendar.YEAR)) return true;
        return cal1.get(Calendar.DAY_OF_YEAR) > cal2.get(Calendar.DAY_OF_YEAR);
    }

    //formate a date
    public static String[] getFormatedTime(String updateDate) {
        try {
            String timeFormate =
                    AppUtility.dateTimeByAmPmFormate("EEE, d MMM yyyy/hh:mm/a", "EEE, d MMM yyyy/kk:mm");
            // Create a DateFormatter object for displaying date in specified format.
//            SimpleDateFormat formatter = new SimpleDateFormat(timeFormate, Locale.US);
            SimpleDateFormat formatter = new SimpleDateFormat(timeFormate, Locale.getDefault());


            // Create a calendar object that will convert the date and time value in milliseconds to date.
            Calendar calendar = Calendar.getInstance();
            calendar.setTimeInMillis(Long.valueOf(updateDate) * 1000);
            return formatter.format(calendar.getTime()).split("/");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    //formate a date
    public static String[] getFormatedTime2(String updateDate) {
        try {
            String timeFormate =
                    AppUtility.dateTimeByAmPmFormate("EEE, d MMM yyyy/hh:mm/a", "EEE, d MMM yyyy/kk:mm");
            // Create a DateFormatter object for displaying date in specified format.
            SimpleDateFormat formatter = new SimpleDateFormat(timeFormate, Locale.getDefault());

            Date endDate = null;
            try {
                endDate = formatter.parse(updateDate);
            } catch (Exception exception) {
                exception.getMessage();
            }

            // Create a calendar object that will convert the date and time value in milliseconds to date.
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(endDate);
            return formatter.format(calendar.getTime()).split("/");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /*****/
    //px to dp coversion logic
    public static int getintToPX(int i) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, i, EotApp.getAppinstance().getResources().getDisplayMetrics());
    }

    //get direct call
    public static void getCallOnNumber(Context context, String number) {
        try {
            Intent intent = new Intent();
            intent.setAction("android.intent.action.DIAL");
            Log.e("String_number length=", number.length() + "");
            intent.setData(Uri.parse("tel:" + number));
            context.startActivity(intent);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static File createImageFile(Context context) throws IOException {
        Calendar calendar = Calendar.getInstance();
        long imageFileName = calendar.getTime().getTime();

        File storageDir = context.getExternalFilesDir(Environment.DIRECTORY_PICTURES);// return path

        File directoryPath = new File(storageDir.getPath());
        File image = File.createTempFile(
                String.valueOf(imageFileName),  /* prefix */
                ".jpg",         /* suffix */
                directoryPath   /* directory */
        );
        captureImagePath = image.getAbsolutePath();
        return new File(image.getPath());
    }

    public static RequestBody getRequestBody(Object obj) {
        Gson gson = new Gson();
        String str = gson.toJson(obj);
        return RequestBody.create(okhttp3.MediaType.parse("application/json; charset=utf-8"), str);
    }

    //custom Alert Dialog
    public static void error_Alert_Dialog(Context context, String message, String positiveButton
            , final Callable<Boolean> function) {
        final AlertDialog.Builder alertDialog = new AlertDialog.Builder(context, R.style.AppCompatAlertDialogStyle);
        TextView error_msg;
        LayoutInflater inflater = ((Activity) context).getLayoutInflater();
        final View customLayout = inflater.inflate(R.layout.error_dialog_layout, null);
        alertDialog.setView(customLayout);
        alertDialog.setCancelable(false);
        error_msg = customLayout.findViewById(R.id.error_msg);
        error_msg.setText(message);


        alertDialog.setPositiveButton(positiveButton, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                try {
                    function.call();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
        alertDialog.show();
    }

    public static void alertDialog(Context context, String title, String message,
                                   String positiveButton, String negativeButton,
                                   final Callable<Boolean> function) {
        try {

            TextView dailog_title, dialog_msg;
            final AlertDialog.Builder alertDialog = new AlertDialog.Builder(context);

            LayoutInflater inflater = ((Activity) context).getLayoutInflater();// (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            final View customLayout = inflater.inflate(R.layout.dialog_layout, null);
            alertDialog.setView(customLayout);
            alertDialog.setCancelable(false);

            dailog_title = customLayout.findViewById(R.id.dai_title);
            dialog_msg = customLayout.findViewById(R.id.dia_msg);
            if (!title.equals("")) {
                dailog_title.setVisibility(View.VISIBLE);
                dailog_title.setText(title);
            }
            dialog_msg.setText(message);

            alertDialog.setPositiveButton(positiveButton, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    try {
                        function.call();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            });

            alertDialog.setNegativeButton(negativeButton, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                }
            });

            alertDialog.show();
        } catch (Exception e) {
            e.printStackTrace();
            EotApp.getAppinstance().showToastmsg("Something went wrong here.");
            //Toast.makeText(context, "Something went wrong here.", Toast.LENGTH_SHORT).show();
        }
    }

    public static void alertDialog2(Context context, String title, String message, String positiveButton, String negativeButton, final Callback_AlertDialog callback) {
        final AlertDialog.Builder alertDialog = new AlertDialog.Builder(context);

        TextView dailog_title, dialog_msg;

        LayoutInflater inflater = ((Activity) context).getLayoutInflater();// (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        final View customLayout = inflater.inflate(R.layout.dialog_layout, null);
        alertDialog.setView(customLayout);

        dailog_title = customLayout.findViewById(R.id.dai_title);
        dialog_msg = customLayout.findViewById(R.id.dia_msg);
        if (title.length() > 1) {
            dailog_title.setVisibility(View.VISIBLE);
        } else {
            dailog_title.setVisibility(View.GONE);
        }

        dailog_title.setText(title);
        dialog_msg.setText(message);

        alertDialog.setCancelable(false);
        //getWindow().setLayout(600, 400);

        alertDialog.setPositiveButton(positiveButton, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                //positive Button clicked
                //Do Something
                try {
                    callback.onPossitiveCall();
                    dialog.dismiss();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
        alertDialog.setNegativeButton(negativeButton, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                //negative button clicked
                callback.onNegativeCall();
                dialog.dismiss();
            }
        });
        alertDialog.show();
    }

    //InputDate picker logic
    public static DatePickerDialog.OnDateSetListener InputDateSet(final Context context, final Add_job_activity.DateTimeCallback callback, final String message) {
        DatePickerDialog.OnDateSetListener pickerListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int selectedYear, int selectedMonth, int selectedDay) {
                year = selectedYear;
                month = selectedMonth;
                day = selectedDay;
                try {
                    dateStr = day + "-" + (month + 1) + "-" + year;
                    DateFormat formatter = new SimpleDateFormat("dd-MM-yyyy", Locale.US);
                    startDate = formatter.parse(dateStr);
                    callback.setDateTime(formatter.format(startDate));
                } catch (ParseException e) {
                    e.printStackTrace();
                }
            }
        };
        return pickerListener;
    }

    public static DatePickerDialog.OnDateSetListener recurendDate(final Add_job_activity.DateTimeCallback callback) {
        DatePickerDialog.OnDateSetListener pickerListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int selectedYear, int selectedMonth, int selectedDay) {
                year = selectedYear;
                month = selectedMonth;
                day = selectedDay;
                try {
                    DateFormat formatter = new SimpleDateFormat("dd-MM-yyyy", Locale.getDefault());

                    Date startDate = formatter.parse(day + "-" + (month + 1) + "-" + year);
                    callback.setDateTime(new SimpleDateFormat("dd-MMM-yyyy", Locale.getDefault()).format(startDate));
                } catch (ParseException e) {
                    e.printStackTrace();
                }
            }
        };
        return pickerListener;
    }

    //compare a start and finish date logic
    public static DatePickerDialog.OnDateSetListener CompareInputOutputDate(final Context context, final Add_job_activity.DateTimeCallback callback,
                                                                            final String message) {
        final DatePickerDialog.OnDateSetListener pickerListener1 = new DatePickerDialog.OnDateSetListener() {

            // when dialog box is closed, below method will be called.
            @Override
            public void onDateSet(DatePicker view, int selectedYear,
                                  int selectedMonth, int selectedDay) {

                year = selectedYear;
                month = selectedMonth;
                day = selectedDay;

                try {
                    String dateEnd = day + "-" + (month + 1) + "-" + year;
                    DateFormat formatter = new SimpleDateFormat("dd-MM-yyyy", Locale.US);

                    try {
                        endDate = formatter.parse(dateEnd);
                        callback.setDateTime(formatter.format(endDate));
                    } catch (ParseException e) {
                        e.printStackTrace();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }

            }
        };
        return pickerListener1;
    }

    public static TimePickerDialog.OnTimeSetListener InputTimeSet(final Context context,
                                                                  final Add_job_activity.DateTimeCallback callback
            , final String message) {
        TimePickerDialog.OnTimeSetListener onTimeSetListener = new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                String stime = updateTime(hourOfDay, minute);
                timeStr = hourOfDay + ":" + minute;
                callback.setDateTime(stime);
            }
        };
        return onTimeSetListener;
    }

    static public String updateTime(int hours, int mins) {

        if (App_preference.getSharedprefInstance().getLoginRes().getIs24hrFormatEnable() != null
                && App_preference.getSharedprefInstance().getLoginRes().getIs24hrFormatEnable().equals("0")) {
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

        } else {


            String minutes = "";
            if (mins < 10)
                minutes = "0" + mins;
            else
                minutes = String.valueOf(mins);

            String aTime = new StringBuilder().append(hours).append(':')
                    .append(minutes).toString();
            return aTime;
        }

    }

    public static String dateTimeByAmPmFormate(String formate12Hr, String formate24Hr) {
        String formate = App_preference.getSharedprefInstance().getLoginRes().getIs24hrFormatEnable() != null
                && App_preference.getSharedprefInstance().getLoginRes().getIs24hrFormatEnable().equals("0") ?
                formate12Hr : formate24Hr;
        return formate;
    }

    public static TimePickerDialog.OnTimeSetListener OutPutTime(final Context context, final Add_job_activity.DateTimeCallback callback,
                                                                final String message) {
        TimePickerDialog.OnTimeSetListener onTimeSetListener1 = new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                String etime = updateTime(hourOfDay, minute);
                callback.setDateTime(etime);
            }
        };
        return onTimeSetListener1;
    }

    static boolean isTimeAfter(Date startTime, Date endTime) {
        return !endTime.before(startTime);
    }

    public static String getTempIdFormat(String head) {
        return head + "-" + App_preference.getSharedprefInstance().getLoginRes().getUsrId() + "-" + getDateByMiliseconds();
    }

    public static String getUserTimeZone() {
        return TimeZone.getDefault().getID();
    }

    public static void clearAllDataBase() {
        AppDataBase.getInMemoryDatabase(EotApp.getAppinstance()).appointmentModel().delete();
        AppDataBase.getInMemoryDatabase(EotApp.getAppinstance()).jobModel().delete();
        AppDataBase.getInMemoryDatabase(EotApp.getAppinstance()).clientModel().delete();
        AppDataBase.getInMemoryDatabase(EotApp.getAppinstance()).sitemodel().delete();
        AppDataBase.getInMemoryDatabase(EotApp.getAppinstance()).contactModel().delete();
        AppDataBase.getInMemoryDatabase(EotApp.getAppinstance()).userChatModel().delete();

        AppDataBase.getInMemoryDatabase(EotApp.getAppinstance()).invoiceTaxDao().delete();
        AppDataBase.getInMemoryDatabase(EotApp.getAppinstance()).invoiceItemDao().delete();
        AppDataBase.getInMemoryDatabase(EotApp.getAppinstance()).jobOfflineDao().delete();

        AppDataBase.getInMemoryDatabase(EotApp.getAppinstance()).offlinemodel().delete();
        AppDataBase.getInMemoryDatabase(EotApp.getAppinstance()).errorLogmodel().delete();

        AppDataBase.getInMemoryDatabase(EotApp.getAppinstance()).auditDao().delete();
        AppDataBase.getInMemoryDatabase(EotApp.getAppinstance()).equipmentDao().delete();
        AppDataBase.getInMemoryDatabase(EotApp.getAppinstance()).contractDao().delete();
        AppDataBase.getInMemoryDatabase(EotApp.getAppinstance()).taxesLocationDao().delete();
        AppDataBase.getInMemoryDatabase(EotApp.getAppinstance()).clientRefrenceDao().delete();
        AppDataBase.getInMemoryDatabase(EotApp.getAppinstance()).shiftTimeDao().delete();

        App_preference.getSharedprefInstance().clearSharedPreference();

        UserToUserChatController.getInstance().clearAllList();

    }

    public static void sendRequestToJoin(String email, String emailSubject, String title, Context context) {
        Intent emailIntent = new Intent(Intent.ACTION_SEND);
// set the type to 'email'
        emailIntent.setType("vnd.android.cursor.dir/email");
        String[] to = {email};
        emailIntent.putExtra(Intent.EXTRA_EMAIL, to);
// the attachment
        //emailIntent .putExtra(Intent.EXTRA_STREAM, path);
// the mail subject
        emailIntent.putExtra(Intent.EXTRA_SUBJECT, emailSubject);
        context.startActivity(Intent.createChooser(emailIntent, title));
    }

    public static void clearAllSettings() {
        AppDataBase.getInMemoryDatabase(EotApp.getAppinstance()).jobTitleModel().delete();
        AppDataBase.getInMemoryDatabase(EotApp.getAppinstance()).fieldWorkerModel().delete();
        AppDataBase.getInMemoryDatabase(EotApp.getAppinstance()).clientAccount().delete();
        AppDataBase.getInMemoryDatabase(EotApp.getAppinstance()).tagmodel().delete();
    }

    static public void progressBarShow(Context context) {
        progressDialog = new ProgressDialog(context, (R.style.progressbarstyle));
        progressDialog.setMessage(LanguageController.getInstance().getMobileMsgByKey(AppConstant.loading) + "...");
        progressDialog.setCancelable(false);
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progressDialog.setIndeterminate(true);
        progressDialog.show();
    }

    public static void progressBarDissMiss() {
        try {
            progressDialog.dismiss();
            progressDialog.setCancelable(false);
            progressDialog.getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void hideSoftKeyboard(Activity activity) {
        try {
            InputMethodManager inputMethodManager =
                    (InputMethodManager) activity.getSystemService(
                            Activity.INPUT_METHOD_SERVICE);
            inputMethodManager.hideSoftInputFromWindow(
                    activity.getCurrentFocus().getWindowToken(), 0);
        } catch (Exception e) {
            Log.e("TAG :", e.getMessage());
        }
    }

    public static void setupUI(View view, final Context context) {

        // Set up touch listener for non-text box views to hide keyboard.
        if (!(view instanceof EditText)) {
            view.setOnTouchListener(new View.OnTouchListener() {
                public boolean onTouch(View v, MotionEvent event) {
                    hideSoftKeyboard((Activity) context);
                    return false;
                }
            });
        }

        //If a layout container, iterate over children and seed recursion.
        if (view instanceof ViewGroup) {
            for (int i = 0; i < ((ViewGroup) view).getChildCount(); i++) {
                View innerView = ((ViewGroup) view).getChildAt(i);
                setupUI(innerView, context);
            }
        }
    }

    public static JsonObject getJsonObject(String params) {
        JsonParser parser = new JsonParser();
        JsonObject obj = parser.parse(params).getAsJsonObject();
        return obj;
    }

    public static void spinnerPopUpWindow(final Spinner spinner) {
        try {
            Field popup = Spinner.class.getDeclaredField("mPopup");
            popup.setAccessible(true);
            final android.widget.ListPopupWindow popupWindow = (android.widget.ListPopupWindow) popup.get(spinner);
            popupWindow.setHeight(getintToPX(140));
//            //popupWindow.setHeight(140);

            popupWindow.setBackgroundDrawable(EotApp.getAppinstance().getResources().getDrawable(android.R.drawable.dialog_holo_light_frame));


        } catch (NoClassDefFoundError | ClassCastException | NoSuchFieldException | IllegalAccessException e) {
            e.getMessage();
        }


    }

    public static void autocompletetextviewPopUpWindow(View spinner) {
        try {
            Field popup = AutoCompleteTextView.class.getDeclaredField("mPopup");
            popup.setAccessible(true);
            android.widget.ListPopupWindow popupWindow = (android.widget.ListPopupWindow) popup.get(spinner);
            popupWindow.setHeight(getintToPX(140));
            popupWindow.setBackgroundDrawable(EotApp.getAppinstance().getResources().getDrawable(android.R.drawable.dialog_holo_light_frame));
        } catch (NoClassDefFoundError | ClassCastException | NoSuchFieldException | IllegalAccessException e) {
        }
    }

    public static Map<String, String> getApiHeaders() {
        Map<String, String> headers = new HashMap<>();
        headers.put("User-Time-Zone", TimeZone.getDefault().getID());
        headers.put("Token", App_preference.getSharedprefInstance().getLoginRes().getToken());
        headers.put("Origin", getBaserUrlToOrigin());
        return headers;
    }

    public static Map<String, String> getResgistrationApiHeaders() {
        Map<String, String> headers = new HashMap<>();
        headers.put("User-Time-Zone", TimeZone.getDefault().getID());
        headers.put("Token", App_preference.getSharedprefInstance().getResgistartionToken());
        headers.put("Origin", getBaserUrlToOrigin());
        return headers;

    }

    private static String getBaserUrlToOrigin() {
        String[] parts = App_preference.getSharedprefInstance().getBaseURL().split("com");
        String part1 = parts[0] + "com";
        //  String part2 = parts[1];
        return part1;
    }

    public static JsonObject jsonToStingConvrt(Object object) {
        JsonObject jsonObject = null;
        String jsonObjectdata = new Gson().toJson(object);
        JsonParser parser = new JsonParser();
//        jsonObject = parser.parse(jsonObjectdata).getAsJsonObject();
        return parser.parse(jsonObjectdata).getAsJsonObject();
    }

    public static boolean askCameraTakePicture(Context context) {
        HyperLog.i("AppUtility", "askCameraTakePicture(M) Start");
        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.LOLLIPOP) {
            HyperLog.i("AppUtility", "LOLLIPOP grater");
            if (verifyPermissions(AppConstant.cameraPermissions)) {
                HyperLog.i("AppUtility", "Allow Permission dialog ......");
                return true;
            } else {
                HyperLog.i("AppUtility", "Ask Permission dialog ......");
                ActivityCompat.requestPermissions((Activity) context, AppConstant.cameraPermissions, 1);
            }
        } else {
            HyperLog.i("AppUtility", "LOLLIPOP Less than");
            return true;
        }
        HyperLog.i("AppUtility", "askCameraTakePicture(M) Stop");

        return false;
    }

    public static boolean askGalaryTakeImagePermiSsion(Context context) {
        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.LOLLIPOP) {
            if (verifyPermissions(AppConstant.galleryPermissions)) {
                return true;
            } else {
                ActivityCompat.requestPermissions((Activity) context, AppConstant.galleryPermissions, 1);
            }

        } else {
            return true;
        }
        return false;
    }

    public static boolean verifyPermissions(String[] grantResults) {
        for (String result : grantResults) {
            HyperLog.i("AppUtility", "verifyPermissions LOOP call");
            if (ActivityCompat.checkSelfPermission(EotApp.getAppinstance(), result) != PackageManager.PERMISSION_GRANTED) {
                HyperLog.i("AppUtility", "verifyPermissions exit permission");
                return false;
            }
        }
        return true;
    }

    public static boolean askStoragePerMission(Context context) {
        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.LOLLIPOP) {
            if (ContextCompat.checkSelfPermission(context,
                    Manifest.permission.READ_EXTERNAL_STORAGE)
                    != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions((Activity) context,
                        new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, UPLOAD_FILE);
            } else {
                ActivityCompat.requestPermissions((Activity) context, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, 1);
                return true;

            }
        } else {
            return true;
        }
        return false;

    }

    public static boolean isvalidPhoneNo(String mobile_number) {
        return StringUtil.isNumeric(mobile_number);
    }

    public static String getRoundoff_amount(String value) {
        String result = "0";
        String toRoundOf = "2";
        double amount = 0.0;


        try {
            amount = value.isEmpty() ? amount : Double.parseDouble(value);
            if (App_preference.getSharedprefInstance().getCompanySettingsDetails() != null) {
                toRoundOf = App_preference.getSharedprefInstance().getCompanySettingsDetails().getNumAfterDecimal();
            }

            if (amount == 0) {
                switch (toRoundOf) {
                    case "2":
                        return result + ".00";
                    case "3":
                        return result + ".000";
                    case "4":
                        return result + ".0000";
                    default:
                        return result;

                }

            } else {
                /**for me changes**/
                String decimalAmount = String.format("%." + toRoundOf + "f", amount);

                if (decimalAmount.contains(",")) {
                    return decimalAmount.replace(",", ".");
                } else {
                    return String.format("%." + toRoundOf + "f", amount);
                }

            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (amount == 0) {
            return result;
        } else {
            return String.format("%." + toRoundOf + "f", amount);
        }
    }

    public static boolean isValidMultipleEmail(String emailString) {
        boolean isEmailValid = false;
        final String EMAIL_PATTERN = "^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@" + "[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";

        if (emailString.isEmpty()) {
            return false;
        }
        String[] emailArray = emailString.split(",");

        for (int i = 0; i < emailArray.length; i++) {
            if (emailArray[i].matches(EMAIL_PATTERN)) {
                isEmailValid = true;
            } else {
                return false;
            }
        }
        return isEmailValid;
    }

    /**
     * calculate tax for job invoice item list
     */

    public static String getCalculatedAmount(String str_qty, String str_rate, String str_discount, List<Tax> tax, String taxCalculationType) {
        String result = "0.0";
        double amount = 0;
        try {
            double qty = 0, rate = 0, dis = 0;
            if (str_qty.isEmpty()) {
                str_qty = "0";
            }
            if (str_rate.isEmpty()) {
                str_rate = "0";
            }
            if (str_discount.isEmpty()) {
                str_discount = "0";
            }

            qty = Double.parseDouble(str_qty);
            rate = Double.parseDouble(str_rate);
            dis = Double.parseDouble(str_discount);
            float total_tax = 0;
            for (Tax item : tax) {
                String item_rate = item.getRate();
                if (!item_rate.isEmpty()) {
                    total_tax += Float.parseFloat(item_rate);
                }
            }
            if (taxCalculationType.equals("0")) {
                // amount = (qty * rate + qty * ((rate * total_tax) / 100)) - qty * ((rate * dis) / 100);
                double calculaterateDis = (rate * dis) / 100;
                double newRate = rate - calculaterateDis;
                double newAmt = (newRate * total_tax) / 100;
                amount = newAmt + newRate;
                amount = amount * qty;
            } else if (taxCalculationType.equals("1")) {
                double newRate = (rate - ((rate * dis) / 100));
                amount = qty * (newRate + ((newRate * total_tax) / 100));
            }
            result = String.valueOf(amount);
        } catch (Exception ex) {
            ex.getMessage();
        }
        return result;
    }

    /**
     * calculate tax for quotation item list
     */

    public static String getCalculatedAmountForQuotes(String str_qty, String str_rate, String str_discount, List<Tax> tax, String taxCalculationType) {
        String result = "0.0";
        double amount = 0;
        try {
            double qty = 0, rate = 0, dis = 0;
            if (str_qty.isEmpty()) {
                str_qty = "0";
            }
            if (str_rate.isEmpty()) {
                str_rate = "0";
            }
            if (str_discount.isEmpty()) {
                str_discount = "0";
            }

            qty = Double.parseDouble(str_qty);
            rate = Double.parseDouble(str_rate);
            dis = Double.parseDouble(str_discount);
            float total_tax = 0;
            for (Tax item : tax) {
                String item_rate = item.getRate();
                if (!item_rate.isEmpty()) {
                    total_tax += Float.parseFloat(item_rate);
                }
            }
            if (taxCalculationType.equals("0")) {
                //   amount = (qty * rate + qty * ((rate * total_tax) / 100)) - qty * ((rate * dis) / 100);
                double calculaterateDis = (rate * dis) / 100;
                double newRate = rate - calculaterateDis;
                double newAmt = (newRate * total_tax) / 100;
                amount = newAmt + newRate;
                amount = amount * qty;

            } else if (taxCalculationType.equals("1")) {
                double newRate = (rate - ((rate * dis) / 100));
                amount = qty * (newRate + ((newRate * total_tax) / 100));
            }
            result = String.valueOf(amount);
        } catch (Exception ex) {
            ex.getMessage();
        }
        return result;
    }

    /*resize image*/
    public static File scaleToActualAspectRatio(String captureImagePath, Float maxHeight, Float maxWidth) {
        try {

            Bitmap bitmap = BitmapFactory.decodeFile(captureImagePath);


            if (bitmap != null) {
                try {
                    float actualHeight = bitmap.getHeight();
                    float actualWidth = bitmap.getWidth();
// float maxHeight = 1024.0f;
// float maxWidth = 1024.0f;
                    float imgRatio = actualWidth / actualHeight;
                    float maxRatio = maxWidth / maxHeight;
                    if (actualHeight > maxHeight || actualWidth > maxWidth) {
                        if (imgRatio < maxRatio) {
//adjust width according to maxHeight
                            imgRatio = maxHeight / actualHeight;
                            actualWidth = imgRatio * actualWidth;
                            actualHeight = maxHeight;
                        } else if (imgRatio > maxRatio) {
//adjust height according to maxWidth
                            imgRatio = maxWidth / actualWidth;
                            actualHeight = imgRatio * actualHeight;
                            actualWidth = maxWidth;
                        } else {
                            actualHeight = maxHeight;
                            actualWidth = maxWidth;
                        }
                    }


                    bitmap = Bitmap.createScaledBitmap(bitmap, (int) actualWidth, (int) actualHeight, true);
                    if (!captureImagePath.equals("")) {
                        ExifInterface exif = new ExifInterface(captureImagePath);
                        int orientation = exif.getAttributeInt(ExifInterface.TAG_ORIENTATION, 1);
                        Matrix matrix = new Matrix();

                        switch (orientation) {
                            case 6:
                                matrix.postRotate(90f);
                                break;

                            case 3:
                                matrix.postRotate(180f);
                                break;
                            case 8:
                                matrix.postRotate(270f);
                                break;
                        }

                        bitmap = Bitmap.createBitmap(bitmap, 0, 0, (int) actualWidth, (int) actualHeight, matrix, true);

                        try {
                            FileOutputStream out = new FileOutputStream(captureImagePath);
                            bitmap.compress(Bitmap.CompressFormat.PNG, 100, out);
                            out.flush();
                            out.close();
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }


                } catch (Exception e) {
                    e.printStackTrace();
                }

            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return new File(captureImagePath);
    }

    public static String getDate(String pickerdate) {
        Date date = new Date(System.currentTimeMillis());
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MMM-yyyy", Locale.getDefault());
        String tempDate = dateFormat.format(date);
        boolean grater = dateGraterOrLess(pickerdate, tempDate);
        if (!grater) {
            return pickerdate;
        } else {
            return tempDate;
        }

    }

    //start date,time must be grater than to end date time
    public static boolean dateGraterOrLess(String schdlStart, String schdlFinish) {
        Locale.getDefault().getDisplayLanguage();
        try {
            SimpleDateFormat gettingfmt = new SimpleDateFormat("dd-MMM-yyyy", Locale.US);//, Locale.US
            Date date = gettingfmt.parse(schdlStart);
            date.getTime();

            Date date1 = gettingfmt.parse(schdlFinish);
            date1.getTime();
            if (date1.getTime() > date.getTime())
                return true;

        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    public static boolean compareCheckInOutTime() {
        if (App_preference.getSharedprefInstance().getLoginRes().getLastCheckIn() != null && !App_preference.getSharedprefInstance().getLoginRes().getLastCheckIn().equals("") && App_preference.getSharedprefInstance().getLoginRes().getShiftEndTime() != null && !App_preference.getSharedprefInstance().getLoginRes().getShiftEndTime().equals("")) {
            String templastCheckOutTime = AppUtility.getDateWithFormate(Long.parseLong(App_preference.getSharedprefInstance().getLoginRes().getLastCheckIn()), "dd-MM-yyyy");

            String endShiftTime = templastCheckOutTime + " " + App_preference.getSharedprefInstance().getLoginRes().getShiftEndTime();


            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd-MM-yyyy HH:mm");

            Date endDate = null;
            try {
                if (endShiftTime != null && !endShiftTime.equals(""))
                    endDate = simpleDateFormat.parse(endShiftTime);
            } catch (Exception exception) {
                exception.getMessage();
            }

            Calendar calendar = Calendar.getInstance();
            calendar.setTime(endDate);
            calendar.add(Calendar.HOUR, 10);
            String tempCheckOuttime = (simpleDateFormat.format(calendar.getTime()));


            String currentTime = AppUtility.getDateByFormat("dd-MM-yyyy HH:mm");

            try {
                Date tempCheckOutdate = simpleDateFormat.parse(tempCheckOuttime);

                Date checkIndate1 = simpleDateFormat.parse(currentTime);
                if (tempCheckOutdate.getTime() > checkIndate1.getTime()) {
                    System.out.println("Check Out");
                    return true;
                } else {
                    System.out.println("Check In");
                    return false;
                }

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return false;

    }

}

