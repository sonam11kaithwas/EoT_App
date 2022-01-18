package com.eot_app.nav_menu.appointment.addupdate;

import android.app.Application;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;

import com.eot_app.nav_menu.appointment.Keepar;
import com.eot_app.nav_menu.appointment.ServerResponse;
import com.eot_app.nav_menu.appointment.addupdate.model.AppointmentAddReq;
import com.eot_app.nav_menu.appointment.dbappointment.Appointment;
import com.eot_app.services.Service_apis;
import com.eot_app.utility.AppConstant;
import com.eot_app.utility.AppUtility;
import com.eot_app.utility.EotApp;
import com.eot_app.utility.db.AppDataBase;
import com.eot_app.utility.db.OfflineDataController;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.hypertrack.hyperlog.HyperLog;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class AppointmentViewModel extends AndroidViewModel implements ServerResponse {

    private final MutableLiveData<Boolean> finishActivity = new MutableLiveData<>();

    private final MutableLiveData<String> serverMessage = new MutableLiveData<>();

    public AppointmentViewModel(@NonNull Application application) {
        super(application);
    }

    public MutableLiveData<Boolean> getFinishActivity() {
        return finishActivity;
    }


    public MutableLiveData<String> getServerMessage() {
        return serverMessage;
    }


    public void addAppointment(AppointmentAddReq appointmentModel) {

        if (appointmentModel != null) {
            appointmentModel.setTempId(AppUtility.getTempIdFormat("Appointment"));
            String dateTime = AppUtility.getDateByFormat(AppConstant.DATE_TIME_FORMAT);
            Gson gson = new Gson();
            String addAppointmentReq = gson.toJson(appointmentModel);//2308  2739
            saveAppointmentTOLocalDB(appointmentModel);
            OfflineDataController.getInstance().addInOfflineDB(Service_apis.addAppointment, addAppointmentReq, dateTime);
            finishActivity.setValue(true);
            HyperLog.i("", "addAppointment(M) saved local DB and offline table");

        }

    }

    private void saveAppointmentTOLocalDB(AppointmentAddReq req) {
        Appointment newAppointment = new Appointment();
        newAppointment.setTempId(req.getTempId());
        newAppointment.setAppId(req.getTempId());
        newAppointment.setCltId(req.getCltId());
        newAppointment.setSiteId(req.getSiteId());
        newAppointment.setConId(req.getConId());
        newAppointment.setIsdelete("1");
        newAppointment.setNm(req.getNm());
        newAppointment.setEmail(req.getEmail());
        newAppointment.setMob1(req.getMob1());
        newAppointment.setDes(req.getDes());
        newAppointment.setCtry(req.getCtry());
        newAppointment.setState(req.getState());
        newAppointment.setAdr(req.getAdr());
        newAppointment.setCity(req.getCity());
        newAppointment.setZip(req.getZip());
        /*set keepars*/
        List<Keepar> keeparList = new ArrayList<>();
        if (req.getMemIds() != null && req.getMemIds().size() > 0) {
            for (String id : req.getMemIds()) {
                Keepar k = new Keepar();
                k.setUsrId(id);
                keeparList.add(k);
            }
        }

        newAppointment.setKpr(keeparList);

        newAppointment.setUpdateDate(AppUtility.getDateByMiliseconds());
        newAppointment.setSchdlStart(getTimeStampFromFormatedDate(req.getSchdlStart()));
        newAppointment.setSchdlFinish(getTimeStampFromFormatedDate(req.getSchdlFinish()));
        AppDataBase.getInMemoryDatabase(EotApp.getAppinstance()).appointmentModel().insertSingleAppointment(newAppointment);
    }


    @Override
    public void onSuccess(Object successObject, int requestCode) {
        Log.d("", successObject.toString());
        JsonObject jsonObject = (JsonObject) successObject;
        if (jsonObject.get("success").getAsBoolean()) {

        } else if (jsonObject.get("statusCode") != null && jsonObject.get("statusCode").getAsString().equals(AppConstant.SESSION_EXPIRE)) {
            //   expenseListView.onSessionExpire(LanguageController.getInstance().getServerMsgByKey(jsonObject.get("message").getAsString()));
        } else if (!jsonObject.get("success").getAsBoolean()) {
            serverMessage.postValue(jsonObject.get("message").getAsString());
            serverMessage.setValue(null);
        }
    }

    @Override
    public void onError(Object errorObject, int requestCode) {

    }


    private String getTimeStampFromFormatedDate(String schdlStart) {
        SimpleDateFormat gettingfmt = new SimpleDateFormat(AppUtility.dateTimeByAmPmFormate(
                "yyyy-MM-dd HH:mm:ss", "yyyy-MM-dd kk:mm:ss"), Locale.US);
        try {
            Date formated = gettingfmt.parse(schdlStart);
            String str = String.valueOf(formated.getTime() / 1000);
            return str;
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return null;
    }

}
