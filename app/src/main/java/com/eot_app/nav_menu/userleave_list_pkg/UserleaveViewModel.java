package com.eot_app.nav_menu.userleave_list_pkg;

import android.app.Application;
import android.content.Context;

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
import com.google.gson.reflect.TypeToken;
import com.hypertrack.hyperlog.HyperLog;

import java.lang.reflect.Type;
import java.util.List;

/**
 * Created by Sona-11 on 9/11/21.
 */
public class UserleaveViewModel extends AndroidViewModel implements ApiCalServerReqRes {
    MutableLiveData<List<UserLeaveResModel>> userLeaveStatusList = new MutableLiveData<>();
    private Context context;

    public UserleaveViewModel(@NonNull Application application) {
        super(application);
    }

    public MutableLiveData<List<UserLeaveResModel>> getUserLeaveStatusList() {
        return userLeaveStatusList;
    }

    public void setUserLeaveStatusList(MutableLiveData<List<UserLeaveResModel>> userLeaveStatusList) {
        this.userLeaveStatusList = userLeaveStatusList;
    }

    public void getUserLeaveList() {
        HyperLog.i("UserleaveViewModel", "getUserLeaveList(M) started");
        UserLeaveListReqModel usrleaveModel = new UserLeaveListReqModel();
        ApiRequestresponce requestor = new ApiRequestresponce(this, USERLEAVEReqCode);
        if (context != null) {
            AppUtility.progressBarShow(context);
            requestor.sendReqOnServerGetRes(Service_apis.getUserLeaveList, usrleaveModel);
        }

        HyperLog.i("UserleaveViewModel", "getUserLeaveList(M) Stop");
    }

    public Context getContext() {
        return context;
    }

    public void setContext(Context activity) {
        this.context = activity;
    }

    @Override
    public void onSuccess(Object successObject, int requestCode) {
        JsonObject jsonObject = (JsonObject) successObject;

        switch (requestCode) {
            case USERLEAVEReqCode:
                if (jsonObject.get("success").getAsBoolean()) {
                    System.out.println();

                    String str = new Gson().toJson(jsonObject.get("data").getAsJsonArray());
                    Type listType = new TypeToken<List<UserLeaveResModel>>() {
                    }.getType();
                    List<UserLeaveResModel> userLeavList = new Gson().fromJson(str, listType);

                    userLeaveStatusList.setValue(userLeavList);

                } else if (jsonObject.get("statusCode") != null && jsonObject.get("statusCode").getAsString().equals(AppConstant.SESSION_EXPIRE)) {
                    EotApp.getAppinstance().sessionExpired();
                } else {
                    EotApp.getAppinstance().showToastmsg(LanguageController.getInstance().getServerMsgByKey(jsonObject.get("message").getAsString()));
                }
                try {
                    AppUtility.progressBarDissMiss();
                } catch (Exception e) {
                    e.getMessage();
                }
        }
    }

    @Override
    public void onError(Throwable errorObject, int requestCode) {
        EotApp.getAppinstance().showToastmsg(errorObject.getMessage());
        try {
            AppUtility.progressBarDissMiss();
        } catch (Exception e) {
            e.getMessage();
        }
    }

}

