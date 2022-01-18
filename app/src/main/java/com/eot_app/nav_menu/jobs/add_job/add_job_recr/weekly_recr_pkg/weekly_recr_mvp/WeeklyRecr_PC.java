package com.eot_app.nav_menu.jobs.add_job.add_job_recr.weekly_recr_pkg.weekly_recr_mvp;

import android.util.Log;

import androidx.fragment.app.Fragment;

import com.eot_app.nav_menu.jobs.add_job.add_job_recr.daily_recr_pkg.daily_recur_model.DailyMsgReqModel;
import com.eot_app.nav_menu.jobs.add_job.add_job_recr.daily_recr_pkg.daily_recur_model.DailyMsgResModel;
import com.eot_app.services.ApiClient;
import com.eot_app.services.Service_apis;
import com.eot_app.utility.AppConstant;
import com.eot_app.utility.AppUtility;
import com.eot_app.utility.language_support.LanguageController;
import com.google.gson.Gson;
import com.google.gson.JsonObject;

import java.util.concurrent.Callable;

import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by Sona-11 on 22/3/21.
 */
public class WeeklyRecr_PC implements WeeklyRecr_PI {
    private final WeeklyRecr_View weeklyRecrView;

    public WeeklyRecr_PC(WeeklyRecr_View weeklyRecrView) {
        this.weeklyRecrView = weeklyRecrView;
    }

    @Override
    public void getDailyRecurMsg(DailyMsgReqModel dailyMsgReqModel) {

        if (AppUtility.isInternetConnected()) {

            ApiClient.getservices().eotServiceCall(Service_apis.weeklyJobRecurrenceResult, AppUtility.getApiHeaders(),
                    AppUtility.getJsonObject(new Gson().toJson(dailyMsgReqModel)))
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new Observer<JsonObject>() {
                        @Override
                        public void onSubscribe(Disposable d) {

                        }

                        @Override
                        public void onNext(JsonObject jsonObject) {
                            if (jsonObject.get("success").getAsBoolean()) {
                                String transVarModel = new Gson().toJson(jsonObject.get("transVar").getAsJsonObject());
                                DailyMsgResModel dailyMsgResModel = new Gson().fromJson(transVarModel, DailyMsgResModel.class);
                                Log.e("", "");
                                weeklyRecrView.showMsgOnView(dailyMsgResModel);
                            } else if (jsonObject.get("statusCode") != null && jsonObject.get("statusCode").getAsString().equals(AppConstant.SESSION_EXPIRE)) {
                                weeklyRecrView.onSessionExpired(LanguageController.getInstance().getServerMsgByKey(jsonObject.get("message").getAsString()));
                            } else {

                            }
                        }


                        @Override
                        public void onError(Throwable e) {
                            Log.e("TAG", e.getMessage());
                        }

                        @Override
                        public void onComplete() {
                        }
                    });


        } else {
            netWorkError();
        }
    }

    private void netWorkError() {
        AppUtility.alertDialog((((Fragment) weeklyRecrView).getActivity()), LanguageController.getInstance().getMobileMsgByKey(AppConstant.dialog_alert), LanguageController.getInstance().getMobileMsgByKey(AppConstant.err_check_network), LanguageController.getInstance().getMobileMsgByKey(AppConstant.ok), "", new Callable<Boolean>() {
            @Override
            public Boolean call() throws Exception {
                return null;
            }
        });
    }

}







/*   int everyOccr = (Integer.parseInt(every) - 1) * 7;

        Log.e("", "");
        strDate = AppUtility.conditionCheck(strDate, defaultJobDateTime);
        int temOccurance = 0;
        temOccurance = occurance;

        Calendar calendar = Calendar.getInstance();
        try {
            calendar.setTime(new SimpleDateFormat("dd-MMM-yyyy", Locale.getDefault()).parse(strDate));
        } catch (ParseException e) {
            e.printStackTrace();
        }


        for (; temOccurance > 0; ) {
            for (String day : SELECTED_DAYS) {
                switch (day) {
                    case "Monday":
                        if (calendar.get(Calendar.DAY_OF_WEEK) == Calendar.MONDAY) {
                            temOccurance = temOccurance - 1;


                        }
                        break;
                    case "Tuesday":
                        if (calendar.get(Calendar.DAY_OF_WEEK) == Calendar.TUESDAY) {
                            temOccurance = temOccurance - 1;
                        }
                        break;
                    case "Wednesday":
                        if (calendar.get(Calendar.DAY_OF_WEEK) == Calendar.WEDNESDAY) {
                            temOccurance = temOccurance - 1;

                        }
                        break;
                    case "Thrusday":
                        if (calendar.get(Calendar.DAY_OF_WEEK) == Calendar.THURSDAY) {
                            temOccurance = temOccurance - 1;

                        }
                        break;
                    case "Friday":
                        if (calendar.get(Calendar.DAY_OF_WEEK) == Calendar.FRIDAY) {
                            temOccurance = temOccurance - 1;
                        }
                        break;
                    case "Saturday":
                        if (calendar.get(Calendar.DAY_OF_WEEK) == Calendar.SATURDAY) {
                            temOccurance = temOccurance - 1;
                        }
                        break;
                    case "Sunday":
                        if (calendar.get(Calendar.DAY_OF_WEEK) == Calendar.SUNDAY) {
                            temOccurance = temOccurance - 1;
                        }
                        break;
                }
                if (temOccurance == 0)
                    break;
            }
            calendar.add(Calendar.DAY_OF_WEEK, 1);

        }
        everyOccr++;
        calendar.add(Calendar.DAY_OF_WEEK, everyOccr);

        SimpleDateFormat sdf2 = new SimpleDateFormat("dd-MMM-yyyy", Locale.getDefault());
        String output = sdf2.format(calendar.getTime());
        Log.e("output", "output" + output);
        weeklyRecrView.setEndDateByoccur(output);*/




/* public void calcultEndDateByWeekDatsForEvery(ArrayList<String> SELECTED_DAYS, String startDate, int occurance, String defaultJobDateTime, String every) {


        startDate = AppUtility.conditionCheck(startDate, defaultJobDateTime);

        Calendar startCalendar = Calendar.getInstance();

        Calendar endcalendar = Calendar.getInstance();

        endcalendar.add(Calendar.DAY_OF_WEEK, occurance);

        try {
            startCalendar.setTime(new SimpleDateFormat("dd-MMM-yyyy", Locale.getDefault()).parse(startDate));
        } catch (ParseException e) {
            e.printStackTrace();
        }


        int temOccurance = 0;
        temOccurance = occurance;


        for (; temOccurance > 0; ) {

            for (String day : SELECTED_DAYS) {
                switch (day) {
                    case "Monday":
                        if (startCalendar.get(Calendar.DAY_OF_WEEK) == Calendar.MONDAY) {
                            temOccurance = temOccurance - 1;
                        }
                        break;
                    case "Tuesday":
                        if (startCalendar.get(Calendar.DAY_OF_WEEK) == Calendar.TUESDAY) {
                            temOccurance = temOccurance - 1;
                        }
                        break;
                    case "Wednesday":
                        if (startCalendar.get(Calendar.DAY_OF_WEEK) == Calendar.WEDNESDAY) {
                            temOccurance = temOccurance - 1;

                        }
                        break;
                    case "Thrusday":
                        if (startCalendar.get(Calendar.DAY_OF_WEEK) == Calendar.THURSDAY) {
                            temOccurance = temOccurance - 1;

                        }
                        break;
                    case "Friday":
                        if (startCalendar.get(Calendar.DAY_OF_WEEK) == Calendar.FRIDAY) {
                            temOccurance = temOccurance - 1;
                        }
                        break;
                    case "Saturday":
                        if (startCalendar.get(Calendar.DAY_OF_WEEK) == Calendar.SATURDAY) {
                            temOccurance = temOccurance - 1;
                        }
                        break;
                    case "Sunday":
                        if (startCalendar.get(Calendar.DAY_OF_WEEK) == Calendar.SUNDAY) {
                            temOccurance = temOccurance - 1;
                        }
                        break;
                }
                startCalendar.add(Calendar.DAY_OF_WEEK, 1);


                if (temOccurance == 0)
                    break;

            }
        }


        SimpleDateFormat sdf2 = new SimpleDateFormat("dd-MMM-yyyy", Locale.getDefault());
        String output = sdf2.format(startCalendar.getTime());
        Log.e("output", "output" + output);

    }

    /***this for Single WEEK ****/

//public void calcultEndDateByWeekDats(ArrayList<String> SELECTED_DAYS, String strDate,
//                                     int occurance, String defaultJobDateTime) {
//    Log.e("", "");
//    strDate = AppUtility.conditionCheck(strDate, defaultJobDateTime);
//    int temOccurance = 0;
//    temOccurance = occurance;
//
//    Calendar calendar = Calendar.getInstance();
//    try {
//        calendar.setTime(new SimpleDateFormat("dd-MMM-yyyy", Locale.getDefault()).parse(strDate));
//    } catch (ParseException e) {
//        e.printStackTrace();
//    }
//
//
//    for (; temOccurance > 0; ) {
//        for (String day : SELECTED_DAYS) {
//            switch (day) {
//                case "Monday":
//                    if (calendar.get(Calendar.DAY_OF_WEEK) == Calendar.MONDAY) {
//                        temOccurance = temOccurance - 1;
//                    }
//                    break;
//                case "Tuesday":
//                    if (calendar.get(Calendar.DAY_OF_WEEK) == Calendar.TUESDAY) {
//                        temOccurance = temOccurance - 1;
//                    }
//                    break;
//                case "Wednesday":
//                    if (calendar.get(Calendar.DAY_OF_WEEK) == Calendar.WEDNESDAY) {
//                        temOccurance = temOccurance - 1;
//                    }
//                    break;
//                case "Thrusday":
//                    if (calendar.get(Calendar.DAY_OF_WEEK) == Calendar.THURSDAY) {
//                        temOccurance = temOccurance - 1;
//                    }
//                    break;
//                case "Friday":
//                    if (calendar.get(Calendar.DAY_OF_WEEK) == Calendar.FRIDAY) {
//                        temOccurance = temOccurance - 1;
//                    }
//                    break;
//                case "Saturday":
//                    if (calendar.get(Calendar.DAY_OF_WEEK) == Calendar.SATURDAY) {
//                        temOccurance = temOccurance - 1;
//                    }
//                    break;
//                case "Sunday":
//                    if (calendar.get(Calendar.DAY_OF_WEEK) == Calendar.SUNDAY) {
//                        temOccurance = temOccurance - 1;
//                    }
//                    break;
//            }
//            if (temOccurance == 0)
//                break;
//        }
//        calendar.add(Calendar.DAY_OF_WEEK, 1);
//
//    }
//    int size = SELECTED_DAYS.size() + 1;
//    if (SELECTED_DAYS.size() == 1)
//        calendar.add(Calendar.DAY_OF_WEEK, -1);
//    else calendar.add(Calendar.DAY_OF_WEEK, -size);
//
//    SimpleDateFormat sdf2 = new SimpleDateFormat("dd-MMM-yyyy", Locale.getDefault());
//    String output = sdf2.format(calendar.getTime());
//    Log.e("output", "output" + output);
//    weeklyRecrView.setEndDateByoccur(output);
//
////}*/