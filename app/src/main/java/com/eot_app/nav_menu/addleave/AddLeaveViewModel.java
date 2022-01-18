package com.eot_app.nav_menu.addleave;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;

import com.eot_app.common_api_contr.ApiCalServerReqRes;
import com.eot_app.common_api_contr.ApiRequestresponce;
import com.eot_app.services.Service_apis;
import com.eot_app.utility.AppConstant;
import com.eot_app.utility.AppUtility;
import com.eot_app.utility.EotApp;
import com.eot_app.utility.language_support.LanguageController;
import com.google.gson.Gson;
import com.google.gson.JsonObject;

/**
 * Created by Sona-11 on 28/10/21.
 */
public class AddLeaveViewModel extends AndroidViewModel implements ApiCalServerReqRes {
    private final MutableLiveData<Boolean> finishActivity = new MutableLiveData<>();
    private final MutableLiveData<String> showDialogs = new MutableLiveData<>();

    public AddLeaveViewModel(@NonNull Application application) {
        super(application);
    }

    public MutableLiveData<Boolean> getFinishActivity() {
        return finishActivity;
    }

    public MutableLiveData<String> getShowDialogs() {
        return showDialogs;
    }

    public void getLeaveApiCall(String reason, String note, String startDateTime, String finishDateTime) {
        LeaveReqModel leaveReqModel = new LeaveReqModel(reason, note, startDateTime, finishDateTime);
        ApiRequestresponce requestor = new ApiRequestresponce(this, LEAVEReqCode);
        requestor.sendReqOnServerGetRes(Service_apis.addLeave, leaveReqModel);
    }

    @Override
    public void onSuccess(Object successObject, int requestCode) {
        JsonObject jsonObject = (JsonObject) successObject;

        switch (requestCode) {
            case LEAVEReqCode:
                if (jsonObject.get("success").getAsBoolean()) {
                    EotApp.getAppinstance().showToastmsg(LanguageController.getInstance().getServerMsgByKey(jsonObject.get("message").getAsString()));
                    finishActivity.setValue(true);

                } else if (jsonObject.get("statusCode") != null && jsonObject.get("statusCode").getAsString().equals(AppConstant.SESSION_EXPIRE)) {
                    EotApp.getAppinstance().sessionExpired();
                } else {
                    String start = "", end = "", myMSg = null;
                    try {
                        String transVarModel = new Gson().toJson(jsonObject.get("transVar").getAsJsonObject());
                        LeaveReSModel dailyMsgResModel = new Gson().fromJson(transVarModel, LeaveReSModel.class);
                        myMSg = "";
                        end = "{{endDateTime}}";
                        start = "{{startDateTime}}";

                        if (LanguageController.getInstance().getServerMsgByKey(jsonObject.get("message").getAsString()).contains("{{startDateTime}}")) {
                            myMSg = LanguageController.getInstance().getServerMsgByKey(jsonObject.get("message").getAsString()).replace(start, dailyMsgResModel.getStartDateTime());

                        }
                        if (LanguageController.getInstance().getServerMsgByKey(jsonObject.get("message").getAsString()).contains("{{endDateTime}}")) {
                            myMSg = myMSg.replace(end, dailyMsgResModel.getEndDateTime());
                        }
                    } catch (Exception jsonSyntaxException) {
                        jsonSyntaxException.printStackTrace();
                    }
                    showMyDialog(myMSg);
                    break;
                }
        }
    }

    private void showMyDialog(String msg) {
        showDialogs.setValue(msg);
    }

    @Override
    public void onError(Throwable errorObject, int requestCode) {
        EotApp.getAppinstance().showToastmsg(errorObject.getMessage());
        finishActivity.setValue(true);

        try {
            AppUtility.progressBarDissMiss();
        } catch (Exception e) {
            e.getMessage();
        }
    }
}
