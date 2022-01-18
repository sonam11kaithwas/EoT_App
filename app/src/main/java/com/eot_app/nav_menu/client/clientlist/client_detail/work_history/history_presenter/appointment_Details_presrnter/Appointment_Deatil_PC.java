package com.eot_app.nav_menu.client.clientlist.client_detail.work_history.history_presenter.appointment_Details_presrnter;

import android.content.Context;
import android.util.Log;

import com.eot_app.nav_menu.appointment.details.AppointmentAttachment;
import com.eot_app.nav_menu.client.clientlist.client_detail.work_history.model.AppointmentAttachmentReq;
import com.eot_app.services.ApiClient;
import com.eot_app.services.Service_apis;
import com.eot_app.utility.AppConstant;
import com.eot_app.utility.AppUtility;
import com.eot_app.utility.language_support.LanguageController;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.List;
import java.util.concurrent.Callable;

import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

public class Appointment_Deatil_PC implements Appointmnet_Deatils_PI {
    private final Appointment_Deatils_View appointment_deatils_view;


    public Appointment_Deatil_PC(Appointment_Deatils_View appointment_deatils_view) {
        this.appointment_deatils_view = appointment_deatils_view;

    }


    @Override
    public void getAppointmentAttachment(String aapId) {
        if (AppUtility.isInternetConnected()) {

            AppointmentAttachmentReq model = new AppointmentAttachmentReq(aapId);
            String data = new Gson().toJson(model);
            ApiClient.getservices().eotServiceCall(Service_apis.getAppointmentAttachment, AppUtility.getApiHeaders(), AppUtility.getJsonObject(data))
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new Observer<JsonObject>() {
                        @Override
                        public void onSubscribe(Disposable d) {

                        }

                        @Override
                        public void onNext(JsonObject jsonObject) {
                            if (jsonObject.get("success").getAsBoolean()) {
                                try {
                                    String convert = new Gson().toJson(jsonObject.get("data").getAsJsonArray());
                                    Type listType = new TypeToken<List<AppointmentAttachment>>() {
                                    }.getType();
                                    List<AppointmentAttachment> list = new Gson().fromJson(convert, listType);
                                    appointment_deatils_view.setAppointmentAttachment(list);
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                            } else if (jsonObject.get("statusCode") != null && jsonObject.get("statusCode").getAsString().equals(AppConstant.SESSION_EXPIRE)) {
                                appointment_deatils_view.sessionExpire(LanguageController.getInstance().getServerMsgByKey(jsonObject.get("message").getAsString()));
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
            netWork_erroR();
        }
    }


    private void netWork_erroR() {
        AppUtility.alertDialog(((Context) appointment_deatils_view), LanguageController.getInstance().getMobileMsgByKey(AppConstant.dialog_alert), LanguageController.getInstance().getMobileMsgByKey(AppConstant.err_check_network), LanguageController.getInstance().getMobileMsgByKey(AppConstant.ok), "", new Callable<Boolean>() {
            @Override
            public Boolean call() throws Exception {
                return null;
            }
        });
    }
}
