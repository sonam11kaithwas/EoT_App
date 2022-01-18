package com.eot_app.nav_menu.admin_fw_chat_pkg.chatmember_list_pkg.chatmember_mvp;

import android.util.Log;

import androidx.fragment.app.Fragment;

import com.eot_app.nav_menu.admin_fw_chat_pkg.sonam_user_user_chat_pkg.usertouser_model.UserChatListModelReq;
import com.eot_app.nav_menu.admin_fw_chat_pkg.sonam_user_user_chat_pkg.usertouser_model.UserChatModel;
import com.eot_app.services.ApiClient;
import com.eot_app.services.Service_apis;
import com.eot_app.utility.AppConstant;
import com.eot_app.utility.AppUtility;
import com.eot_app.utility.App_preference;
import com.eot_app.utility.EotApp;
import com.eot_app.utility.db.AppDataBase;
import com.eot_app.utility.language_support.LanguageController;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonSyntaxException;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.List;
import java.util.concurrent.Callable;

import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by Sonam-11 on 2020-03-06.
 */
public class ChatUsersList_PC implements ChatUsersList_PI {
    private final ChatUsersList_View singleChatView;
    private final int updatelimit;
    private int count;
    private boolean loadList;
    private int updateindex;
    private String searchuser = "";

    public ChatUsersList_PC(ChatUsersList_View singleChatView) {
        this.singleChatView = singleChatView;
        this.updatelimit = AppConstant.LIMIT_MID;
        this.updateindex = 0;
    }


    private void networkError() {
        AppUtility.alertDialog(((Fragment) singleChatView).getActivity(), LanguageController.getInstance().
                        getMobileMsgByKey(AppConstant.dialog_alert), LanguageController.getInstance().getMobileMsgByKey
                        (AppConstant.err_check_network), LanguageController.getInstance().getMobileMsgByKey(AppConstant.ok),
                "", new Callable<Boolean>() {
                    @Override
                    public Boolean call() throws Exception {
                        return null;
                    }
                });
    }

    @Override
    public void getChatUserList() {
        searchuser = "";
        callApiGetUserList();
    }

    @Override
    public void getChatUserListFromDB() {
        singleChatView.setChatUserS(AppDataBase.getInMemoryDatabase(EotApp.getAppinstance()).userChatModel().getChatUserlist());
    }

    @Override
    public void getUserBySearch(String search) {
        searchuser = search;
        callApiGetUserList();
    }

    @Override
    public void getChatUserByName(String search) {
        singleChatView.setChatUserS(AppDataBase.getInMemoryDatabase(EotApp.getAppinstance()).userChatModel().
                getSearchChatuserByNmae("%" + search + "%"));
    }

    @Override
    public void callApiGetUserList() {//final String searchCharUser
        Log.e("", "");
        if (AppUtility.isInternetConnected()) {
            AppUtility.progressBarShow(((Fragment) singleChatView).getActivity());
            UserChatListModelReq model = new UserChatListModelReq(Integer.valueOf(App_preference.getSharedprefInstance().getLoginRes().getCompId()),
                    updatelimit, updateindex
                    , App_preference.getSharedprefInstance().getUsersSyncTime());
            String data = new Gson().toJson(model);
            ApiClient.getservices().eotServiceCall(Service_apis.groupUserListForChat,
                    AppUtility.getApiHeaders(), AppUtility.getJsonObject(data))
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new Observer<JsonObject>() {
                        @Override
                        public void onSubscribe(Disposable d) {

                        }

                        @Override
                        public void onNext(JsonObject jsonObject) {
                            Log.e("", "");
                            try {
                                if (jsonObject.get("success").getAsBoolean()) {
                                    count = jsonObject.get("count").getAsInt();
                                    String jsonString = new Gson().toJson(jsonObject.get("data").getAsJsonArray());
                                    Type typeList = new TypeToken<List<UserChatModel>>() {
                                    }.getType();
                                    List<UserChatModel> chatList = new Gson().fromJson(jsonString, typeList);
                                    addRecordToDB(chatList);
                                } else if (jsonObject.get("statusCode") != null && jsonObject.get("statusCode").getAsString().equals(AppConstant.SESSION_EXPIRE)) {
                                    EotApp.getAppinstance().sessionExpired();
                                } else {
                                    //                                EotApp.getAppinstance().showToastmsg(LanguageController.getInstance().getServerMsgByKey(jsonObject.get("message").getAsString()));
                                }
                            } catch (JsonSyntaxException e) {
                                e.printStackTrace();
                            }
                        }

                        @Override
                        public void onError(Throwable e) {
                            Log.e("", "");
                            AppUtility.progressBarDissMiss();
                        }

                        @Override
                        public void onComplete() {
                            AppUtility.progressBarDissMiss();
                            Log.e("", "");
                            if ((updateindex + updatelimit) <= count) {
                                updateindex += updatelimit;
                                callApiGetUserList();
                            } else {
                                if (count != 0) {
                                    App_preference.getSharedprefInstance().setUsersSyncTime(AppUtility.getDateByFormat(AppConstant.DATE_TIME_FORMAT));
                                }
                                updateindex = 0;
                                count = 0;
                                singleChatView.setChatUserS(AppDataBase.getInMemoryDatabase(EotApp.getAppinstance()).userChatModel().getChatUserlist());

                            }
                        }
                    });
        } else {
            networkError();
            singleChatView.disableRefersh();
        }
    }

    private void addRecordToDB(List<UserChatModel> chatList) {
        AppDataBase.getInMemoryDatabase(EotApp.getAppinstance()).userChatModel().insertChatUserList(chatList);
    }
}
