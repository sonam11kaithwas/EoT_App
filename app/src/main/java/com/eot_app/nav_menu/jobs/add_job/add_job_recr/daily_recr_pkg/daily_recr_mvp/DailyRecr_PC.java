package com.eot_app.nav_menu.jobs.add_job.add_job_recr.daily_recr_pkg.daily_recr_mvp;

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
//import android.util.Log;


/**
 * Created by Sona-11 on 15/3/21.
 */
public class DailyRecr_PC implements DailyRecr_PI {
    private final DailyRecr_View dailyRecrView;

    public DailyRecr_PC(DailyRecr_View dailyRecrView) {
        this.dailyRecrView = dailyRecrView;
    }

    @Override
    public void getDailyRecurMsg(DailyMsgReqModel dailyMsgReqModel) {

        if (AppUtility.isInternetConnected()) {

            ApiClient.getservices().eotServiceCall(Service_apis.dailyJobRecurrenceResult, AppUtility.getApiHeaders(),
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
                                dailyRecrView.showMsgOnView(dailyMsgResModel);
                            } else if (jsonObject.get("statusCode") != null && jsonObject.get("statusCode").getAsString().equals(AppConstant.SESSION_EXPIRE)) {
                                dailyRecrView.onSessionExpired(LanguageController.getInstance().getServerMsgByKey(jsonObject.get("message").getAsString()));
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
        AppUtility.alertDialog((((Fragment) dailyRecrView).getActivity()), LanguageController.getInstance().getMobileMsgByKey(AppConstant.dialog_alert), LanguageController.getInstance().getMobileMsgByKey(AppConstant.err_check_network), LanguageController.getInstance().getMobileMsgByKey(AppConstant.ok), "", new Callable<Boolean>() {
            @Override
            public Boolean call() throws Exception {
                return null;
            }
        });
    }

/*
    @Override
    public int calCulateWeekDay(String startDate, String endDate, String defaultJobDateTime) {

        startDate = AppUtility.conditionCheck(startDate, defaultJobDateTime);

        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd-MMM-yyyy", Locale.getDefault());//, Locale.US

        Calendar startCal = Calendar.getInstance();
        Calendar endCal = Calendar.getInstance();

        try {
            startCal.setTime(simpleDateFormat.parse(startDate));
            endCal.setTime(simpleDateFormat.parse(endDate));
        } catch (ParseException e) {
            e.printStackTrace();
        }

        int workDays = 0;

        //Return 0 if start and end are the same
        if (startCal.getTimeInMillis() == endCal.getTimeInMillis()) {
            return 0;
        }

        if (startCal.getTimeInMillis() > endCal.getTimeInMillis()) {
            try {
                startCal.setTime(simpleDateFormat.parse(endDate));
                endCal.setTime(simpleDateFormat.parse(startDate));

            } catch (ParseException e) {
                e.printStackTrace();
            }
        }

        do {
            //excluding start date
            startCal.add(Calendar.DAY_OF_MONTH, 1);
            if (startCal.get(Calendar.DAY_OF_WEEK) != Calendar.SATURDAY && startCal.get(Calendar.DAY_OF_WEEK) != Calendar.SUNDAY) {
                ++workDays;
            }
        } while (startCal.getTimeInMillis() < endCal.getTimeInMillis()); //excluding end date

        dailyRecrView.setOccuranceDays(workDays + "");


        return workDays;
    }

    *//***calculate END date by Everday & occurance & Start Date*****//*
    @Override
    public void getendDateForoccurance(String pickerDate, int occuranceDays, String defaultJobDateTime, boolean check) {
        pickerDate = AppUtility.conditionCheck(pickerDate, defaultJobDateTime);
        dailyRecrView.setEndAfterDate(AppUtility.getEndDateForRecr(pickerDate, occuranceDays, check));
    }


    *//*****//*





    @Override
    public void getEndDateForWeekRAdio(String pickerDate, int occuranceDays, String defaultJobDateTime) {
        pickerDate = AppUtility.conditionCheck(pickerDate, defaultJobDateTime);

        endDateCheckForSatSun(pickerDate, occuranceDays);

    }

    private void endDateCheckForSatSun(String startDate, int occuranceDays) {

        Calendar startCalender = Calendar.getInstance();
        try {
            startCalender.setTime(new SimpleDateFormat("dd-MMM-yyyy").parse(startDate));
        } catch (ParseException e) {
            e.printStackTrace();
        }
        Calendar endcalendar = Calendar.getInstance();

        endcalendar.add(Calendar.DAY_OF_WEEK, occuranceDays);

        while (startCalender.getTimeInMillis() <= endcalendar.getTimeInMillis()) {
            if (startCalender.get(Calendar.DAY_OF_WEEK) == Calendar.SATURDAY || startCalender.get(Calendar.DAY_OF_WEEK) == Calendar.SUNDAY) {
                endcalendar.add(Calendar.DAY_OF_WEEK, 1);
            }
            startCalender.add(Calendar.DAY_OF_WEEK, 1);
            if (startCalender.get(Calendar.DAY_OF_WEEK) == Calendar.SATURDAY || startCalender.get(Calendar.DAY_OF_WEEK) == Calendar.SUNDAY) {
                startCalender.add(Calendar.DAY_OF_WEEK, 1);
                endcalendar.add(Calendar.DAY_OF_WEEK, 1);
            }
        }
        SimpleDateFormat sdf2 = new SimpleDateFormat("dd-MMM-yyyy", Locale.getDefault());
        String output = sdf2.format(endcalendar.getTime());

        dailyRecrView.setEndAfterDate(output);

    }


    @Override
    public void calculateDaysFromStrToEnd(String strDate, String endDate, String defaultJobDate) {

        strDate = AppUtility.conditionCheck(strDate, defaultJobDate);

        SimpleDateFormat myFormat = new SimpleDateFormat("dd-MMM-yyyy");

        try {
            Date dateBefore = myFormat.parse(strDate);
            Date dateAfter = myFormat.parse(endDate);

            long difference = dateAfter.getTime() - dateBefore.getTime();
            String daysBetween = String.valueOf((difference / (1000 * 60 * 60 * 24)));
            *//* You can also convert the milliseconds to days using this method
     * float daysBetween =
     *         TimeUnit.DAYS.convert(difference, TimeUnit.MILLISECONDS)
     *//*
            int temp = Integer.parseInt(daysBetween) + 1;


            dailyRecrView.setOccuranceDays(temp + "");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void getendDateForEveryRecr(String strDate, String endDate, int everydays) {
        SimpleDateFormat myFormat = new SimpleDateFormat("dd-MMM-yyyy");
        try {
            Date dateBefore = myFormat.parse(strDate);
            Date dateAfter = myFormat.parse(endDate);
            long difference = dateAfter.getTime() - dateBefore.getTime();
            String daysBetween = String.valueOf((difference / (1000 * 60 * 60 * 24)));
            int daysMinus = Integer.parseInt(daysBetween) / everydays;
            *//* You can also convert the milliseconds to days using this method
     * float daysBetween =
     *         TimeUnit.DAYS.convert(difference, TimeUnit.MILLISECONDS)
     *//*
            System.out.println("Number of Days between dates: " + daysMinus);
            dailyRecrView.setOccuranceDays(daysMinus + "");
        } catch (Exception e) {
            e.printStackTrace();
        }

    }*/
}





