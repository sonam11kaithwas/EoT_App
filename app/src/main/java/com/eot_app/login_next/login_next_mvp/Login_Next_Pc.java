package com.eot_app.login_next.login_next_mvp;

import android.app.Dialog;
import android.content.Context;
import android.content.pm.PackageInfo;
import android.graphics.drawable.ColorDrawable;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.eot_app.R;
import com.eot_app.login_next.login_next_model.Login_Next_Request_MOdel;
import com.eot_app.login_next.login_next_model.Login_Responce_Model;
import com.eot_app.login_next.login_next_model.ResLoginData;
import com.eot_app.login_next.login_next_model.ResMobileLogin;
import com.eot_app.nav_menu.jobs.job_controller.ChatController;
import com.eot_app.services.ApiClient;
import com.eot_app.services.Service_apis;
import com.eot_app.utility.AppConstant;
import com.eot_app.utility.AppUtility;
import com.eot_app.utility.App_preference;
import com.eot_app.utility.EotApp;
import com.eot_app.utility.db.AppDataBase;
import com.eot_app.utility.language_support.LanguageController;
import com.eot_app.utility.language_support.Language_Preference;
import com.eot_app.utility.util_interfaces.Callback_AlertDialog;
import com.google.gson.Gson;
import com.google.gson.JsonObject;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.TimeUnit;

import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

public class Login_Next_Pc implements Login_Next_Pi {
    Login_Next_View login_next_view;
    Login_Next_Request_MOdel login_next_request_mOdel;
    boolean remember_me;
    String comp_code = "";


    public Login_Next_Pc(Login_Next_View login_next_view) {
        this.login_next_view = login_next_view;
    }


    @Override
    public boolean checkEmailValidation(String email) {
        if ((email.length() < 3)) {
            login_next_view.setEmailEroor(AppConstant.userName);
            return false;
        }
        return true;
    }

    @Override
    public boolean checkPassValidation(String pass) {
        if (pass.isEmpty()) {
            login_next_view.setPassEroor(AppConstant.frgt_pass);
            return false;
        }
        return true;
    }


    @Override
    public void syncData(final HashMap<String, String> hashMap) {//Sync offline data when same user login
        String sp_user = App_preference.getSharedprefInstance().getLoginRes() != null ? App_preference.getSharedprefInstance().getLoginRes().getUsername() : "";
        if (!hashMap.get("email").equalsIgnoreCase(sp_user)) {
            if (AppDataBase.getInMemoryDatabase(EotApp.getAppinstance()).offlinemodel().getCountOfRow() > 0) {
                String existUser = "";
                if (App_preference.getSharedprefInstance() != null && App_preference.getSharedprefInstance().getLoginRes() != null && App_preference.getSharedprefInstance().getLoginRes().getEmail() != null) {
                    existUser = App_preference.getSharedprefInstance().getLoginRes().getEmail();
                }
                AppUtility.alertDialog2((Context) login_next_view, AppConstant.Sync_alert + "!", AppConstant.syn_data + existUser, "", AppConstant.Clear_data, new Callback_AlertDialog() {
                    @Override
                    public void onPossitiveCall() {

                    }

                    @Override
                    public void onNegativeCall() {
                        AppUtility.clearAllDataBase();
                        AppUtility.clearAllSettings();
                        get_Url(hashMap);
                    }
                });

            } else {
                AppUtility.clearAllDataBase();
                AppUtility.clearAllSettings();
//                    add this line after clear all data base
                App_preference.getSharedprefInstance().setFirstSyncState(0);
                get_Url(hashMap);
            }
        } else {
            if (App_preference.getSharedprefInstance().getFirstSyncState() == 4) {// if new user enter for setting
                App_preference.getSharedprefInstance().setFirstSyncState(0);
            }
            get_Url(hashMap);
        }
    }

    @Override
    public void get_Url(HashMap<String, String> hashMap) {//get Base Url for particular Server
        ApiClient.resetClientforBaseurl();
        App_preference.getSharedprefInstance().setBaseURL(AppConstant.BASEURL);
        if (AppUtility.isInternetConnected()) {
            AppUtility.progressBarShow((Context) login_next_view);
            ApiClient.getservices().getRegionURL(Service_apis.getApiUrl, hashMap).subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new Observer<JsonObject>() {
                        @Override
                        public void onSubscribe(Disposable d) {
                        }

                        @Override
                        public void onNext(JsonObject responseBody) {
                            if (responseBody.get("success").getAsBoolean()) {
                                Gson gson = new Gson();
                                String jsonString = gson.toJson(responseBody.get("data"));
                                Login_Responce_Model login_responce_model = gson.fromJson(jsonString, Login_Responce_Model.class);
                                List<String> list = login_responce_model.getcCode();
                                if (login_responce_model.getcCode().size() > 1) {
                                    showRadioButtonDialog(list, "");
                                } else {
                                    App_preference.getSharedprefInstance().setCompCode(list.get(0));
                                    App_preference.getSharedprefInstance().setRegion(login_responce_model.getRegion());
                                }
                                App_preference.getSharedprefInstance().setEmailResponce(jsonString);
                                /*****Very**********************************************************************
                                 * imp
                                 * line
                                 * *****
                                 * ************************************************************/
                                App_preference.getSharedprefInstance().setBaseURL(login_responce_model.getApiurl());//update Base url
                                ApiClient.resetClientforBaseurl();
                                login_next_view.userLogin();
                            } else {
                                userNamePassNotFound(LanguageController.getInstance().getServerMsgByKey(LanguageController.getInstance().getServerMsgByKey(responseBody.get("message").getAsString())));
                            }
                        }

                        @Override
                        public void onError(Throwable e) {
                            AppUtility.progressBarDissMiss();
                            Log.e("Error", e.getMessage());
                        }

                        @Override
                        public void onComplete() {
                            AppUtility.progressBarDissMiss();
                        }
                    });
        } else
            userNamePassNotFound(LanguageController.getInstance().getMobileMsgByKey(AppConstant.err_check_network));
    }

    @Override
    public void UserLoginServiceCall(Login_Next_Request_MOdel login_next_request_model, boolean rememberme) {
        if (rememberme) {
            App_preference.getSharedprefInstance().setLoginCredentials(login_next_request_model.getEmail(), login_next_request_model.getPass());
        } else {
            App_preference.getSharedprefInstance().setLoginCredentials("", "");
        }
        String jsonString = new Gson().toJson(login_next_request_model);
        JsonObject jsonObject = AppUtility.getJsonObject(jsonString);
        if (AppUtility.isInternetConnected()) {

            AppUtility.progressBarShow((Context) login_next_view);

            ApiClient.getservices().userLogin(Service_apis.mobileLogin, jsonObject).subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new Observer<JsonObject>() {
                        @Override
                        public void onSubscribe(Disposable d) {
                        }

                        @Override
                        public void onNext(JsonObject responseBody) {
                            Log.e("", "");
                            if (responseBody.get("success").getAsBoolean()) {

                                Gson gson = new Gson();
                                String jsonString = gson.toJson(responseBody.getAsJsonArray("data").get(0));
                                Log.e("TAG>>>token missing", jsonString);

                                /***   clear language if different user find.   **/
                                if (App_preference.getSharedprefInstance().getLoginRes() == null) {
                                    LanguageController.getInstance().clearlanguageData();
                                }

                                ResMobileLogin resMobileLogin = gson.fromJson(jsonString, ResMobileLogin.class);
                                ResLoginData resLoginData = App_preference.getSharedprefInstance().getLoginRes();
                                if (resLoginData != null) {
                                    resLoginData.setMobileLoginData(resMobileLogin);
                                } else {
                                    resLoginData = new ResLoginData();
                                    resLoginData.setMobileLoginData(resMobileLogin);
                                }
                                String saveLoginData = gson.toJson(resLoginData);

                                App_preference.getSharedprefInstance().setLoginResponse(saveLoginData);

                                /**If User registration are not completed so reDirect On AlmoSt Page For again Fill information****/
                                if (resMobileLogin.getIsCompInfoFill().equals("1")) {
                                    Log.e("TAG : ", "HIII");
                                    login_next_view.LoginSuccessFully();
                                    /** set Authentication user login to fire base******/
                                    ChatController.getInstance().getFirebaseAuthentication(App_preference.getSharedprefInstance().getLoginRes().getUsrId(), App_preference.getSharedprefInstance().getRegion());
                                } else {
                                    login_next_view.userregiNotCompleted();
                                }


                            } else {
                                userNamePassNotFound(LanguageController.getInstance().getServerMsgByKey(responseBody.get("message").getAsString()));
                            }
                        }

                        @Override
                        public void onError(Throwable e) {
                            Log.e("Error", e.getMessage());
                            AppUtility.progressBarDissMiss();
                        }

                        @Override
                        public void onComplete() {
                            AppUtility.progressBarDissMiss();
                        }
                    });
        } else {
            userNamePassNotFound(LanguageController.getInstance().getMobileMsgByKey(AppConstant.network_error));
        }
    }

    private void checkForLanguageSettings() {
        float existing_version = Float.parseFloat(Language_Preference.getSharedprefInstance().getlanguageVersion());
        String existing_name = Language_Preference.getSharedprefInstance().getlanguageFilename();

        final String new_filename = App_preference.getSharedprefInstance().getLoginRes().getLanguage().getFileName();
        final float new_version = Float.parseFloat(App_preference.getSharedprefInstance().getLoginRes().getLanguage().getVersion());

        boolean isLanChangebyUser = Language_Preference.getSharedprefInstance().isUserChangeLang();
        String islock = App_preference.getSharedprefInstance().getLoginRes().getLanguage().getIsLock();
//the language file only download if user not change by it self or admin change the language
        if (!islock.equals("0") || !isLanChangebyUser || (!existing_name.equals(new_filename) && existing_version != new_version)) {
            String file_path = App_preference.getSharedprefInstance().getLoginRes().getLanguage().getFilePath();
            String download_url = file_path + new_filename + ".json";
            LanguageController.getInstance().downloadFile(download_url, new Callable<Boolean>() {
                @Override
                public Boolean call() throws Exception {
                    Language_Preference.getSharedprefInstance().setisUserChangeLang(false);
                    Language_Preference.getSharedprefInstance().setLanguageFilename(new_filename);
                    Language_Preference.getSharedprefInstance().setLanguageVersion(String.valueOf(new_version));
                    return null;
                }
            });
        }
    }


    private void checkForVersionUpdate() {
        try {
            float force_version = Float.valueOf(App_preference.getSharedprefInstance().getLoginRes().getForceupdate_version());
            float version = Float.valueOf(App_preference.getSharedprefInstance().getLoginRes().getVersion());

            PackageInfo pInfo = EotApp.getAppinstance().getPackageManager().getPackageInfo(EotApp.getAppinstance().getPackageName(), 0);
            float app_version = Float.valueOf(pInfo.versionName);
            if (force_version > app_version) {
                login_next_view.upateForcefully();
            } else if (version > app_version && check24HourComplete()) {
//                check 24 hours complete ?
                login_next_view.updateNotForcefully();
            } else {
                login_next_view.LoginSuccessFully();
            }
        } catch (Exception e) {
            e.printStackTrace();
            login_next_view.LoginSuccessFully();
        }
    }

    private boolean check24HourComplete() {
        String datevalue = App_preference.getSharedprefInstance().getIS_24HOURS();
        if (datevalue.equals("")) {
            App_preference.getSharedprefInstance().setIS_24HOURS(AppUtility.getDateByMiliseconds());
            return true;
        }
        Date last_date = new Date(Long.valueOf(datevalue)); // last save date on update check.
        Date current_date = new Date(Long.valueOf(AppUtility.getDateByMiliseconds())); // current date.
        long duration = current_date.getTime() - last_date.getTime();
        long diffInDays = TimeUnit.MILLISECONDS.toDays(duration);
        if (diffInDays >= 1) {
            App_preference.getSharedprefInstance().setIS_24HOURS(AppUtility.getDateByMiliseconds());
            return true;
        }
        return false;
    }

    @Override
    public void getsaveLoginCrediantal() {
        boolean checked = false;
        String[] login_data = App_preference.getSharedprefInstance().getLoginCredentials();
        if (login_data[0].length() > 0) {
            checked = true;
        }
        login_next_view.setSaveLoginCrediantal(login_data[0], login_data[1], checked);
    }


    @Override
    public void showRadioButtonDialog(List<String> list, String message) {//select company code
        final Dialog dialog = new Dialog((Context) login_next_view);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.radiobutton_dialog);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        RadioGroup rg = dialog.findViewById(R.id.radio_group);
        TextView txt_close = dialog.findViewById(R.id.btnClose);
        TextView txt_continue = dialog.findViewById(R.id.btn_continue);
        TextView msg = dialog.findViewById(R.id.msg);
        msg.setText(message);
        txt_close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
                login_next_request_mOdel = null;
                remember_me = false;
                comp_code = "";
            }
        });
        txt_continue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!App_preference.getSharedprefInstance().getCompCode().equals(comp_code)) {
                    AppUtility.clearAllDataBase();
                }
                App_preference.getSharedprefInstance().setCompCode(comp_code);
            }
        });

        for (int i = 0; i < list.size(); i++) {
            RadioButton rb = new RadioButton((Context) login_next_view);
            RadioGroup.LayoutParams lp = new RadioGroup.LayoutParams(RadioGroup.LayoutParams.MATCH_PARENT, RadioGroup.LayoutParams.MATCH_PARENT);
            rb.setLayoutParams(lp);// dynamically creating RadioButton and adding to RadioGroup.
            rb.setTextAppearance((Context) login_next_view, R.style.style_thrid);
            rb.setText(list.get(i));
            rg.addView(rb);
        }
        rg.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                int id = radioGroup.getCheckedRadioButtonId();
                RadioButton checked_btn = radioGroup.findViewById(id);
                comp_code = checked_btn.getText().toString();
            }
        });
        dialog.show();
    }


    private void userNamePassNotFound(String msg) {//error dialog
        AppUtility.alertDialog(((Context) login_next_view), EotApp.getAppinstance().getString(R.string.dialog_alert), msg, AppConstant.ok, "", new Callable<Boolean>() {
            @Override
            public Boolean call() throws Exception {
                return null;
            }
        });
    }
}
