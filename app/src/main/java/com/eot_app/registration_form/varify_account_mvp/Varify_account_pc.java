package com.eot_app.registration_form.varify_account_mvp;

import android.content.Context;
import android.util.Log;

import com.eot_app.registration_form.company_model_pkg.ResendVerificationCode;
import com.eot_app.registration_form.company_model_pkg.VerifyCompanyCode;
import com.eot_app.services.ApiClient;
import com.eot_app.services.Service_apis;
import com.eot_app.utility.AppConstant;
import com.eot_app.utility.AppUtility;
import com.eot_app.utility.EotApp;
import com.eot_app.utility.language_support.LanguageController;
import com.google.gson.JsonObject;
import com.hypertrack.hyperlog.HyperLog;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.Callable;

import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

public class Varify_account_pc implements Varify_account_pi {

    private final Varify_accountview varify_accountview;

    public Varify_account_pc(Varify_accountview varify_accountview) {

        this.varify_accountview = varify_accountview;
    }

    @Override
    public void resend_Verification_Code(ResendVerificationCode resendVerificationCode) {
        if (AppUtility.isInternetConnected()) {

            AppUtility.progressBarShow((Context) varify_accountview);

            Map<String, String> verifyCode = new HashMap<>();
            verifyCode.put("email", resendVerificationCode.getEmail());


            ApiClient.getservices().resendVerificationCode(Service_apis.resendVerificationCode,
                    verifyCode)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new Observer<JsonObject>() {
                        @Override
                        public void onSubscribe(Disposable d) {

                        }

                        @Override
                        public void onNext(JsonObject jsonObject) {

                            if (jsonObject.get("success").getAsBoolean()) {
                                //  EotApp.getAppinstance().showToastmsg(LanguageController.getInstance().getServerMsgByKey(jsonObject.get("message").getAsString()));
                                try {

                                } catch (Exception e) {
                                    e.printStackTrace();

                                }
                            } else if (jsonObject.get("statusCode") != null && jsonObject.get("statusCode").getAsString().equals(AppConstant.SESSION_EXPIRE)) {

                                EotApp.getAppinstance().sessionExpired();
                            } else {
                                varify_accountview.errorMsg(LanguageController.getInstance().getServerMsgByKey(jsonObject.get("message").getAsString()));
                            }

                        }


                        @Override
                        public void onError(Throwable e) {
                            AppUtility.progressBarDissMiss();
                            HyperLog.i("", e.getMessage());
                        }

                        @Override
                        public void onComplete() {
                            AppUtility.progressBarDissMiss();
                            Log.e("TAG", "");
                        }
                    });
        } else {
            networkError();
        }

    }

    @Override
    public void verify_Company_Code(final VerifyCompanyCode verifyCompanyCode) {
        if (AppUtility.isInternetConnected()) {

            AppUtility.progressBarShow((Context) varify_accountview);

            Map<String, String> verifyCompCode = new HashMap<>();
            verifyCompCode.put("email", verifyCompanyCode.getEmail());
            verifyCompCode.put("code", verifyCompanyCode.getCode());
            verifyCompCode.put("pass", verifyCompanyCode.getPass());
            verifyCompCode.put("apiCode", verifyCompanyCode.getApiCode());


            ApiClient.getservices().verifyCompanyCode(Service_apis.verifyCompanyCode,
                    verifyCompCode
            ).subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new Observer<JsonObject>() {
                        @Override
                        public void onSubscribe(Disposable d) {

                        }

                        @Override
                        public void onNext(JsonObject jsonObject) {

                            if (jsonObject.get("success").getAsBoolean()) {
                                // EotApp.getAppinstance().showToastmsg(LanguageController.getInstance().getServerMsgByKey(jsonObject.get("message").getAsString()));
                                try {
                                    AppUtility.progressBarDissMiss();
                                    Boolean success = jsonObject.get("success").getAsBoolean();
                                    varify_accountview.varify_code(success);

                                } catch (Exception e) {
                                    e.printStackTrace();

                                }
                            } else if (jsonObject.get("statusCode") != null && jsonObject.get("statusCode").getAsString().equals(AppConstant.SESSION_EXPIRE)) {

                                EotApp.getAppinstance().sessionExpired();
                            } else {
                                varify_accountview.errorMsg(LanguageController.getInstance().getServerMsgByKey(jsonObject.get("message").getAsString()));
                                varify_accountview.varify_code(false);
                            }

                        }


                        @Override
                        public void onError(Throwable e) {
                            AppUtility.progressBarDissMiss();
                            HyperLog.i("", e.getMessage());
                        }

                        @Override
                        public void onComplete() {
                            AppUtility.progressBarDissMiss();
                            Log.e("TAG", "");
                        }
                    });
        } else {
            networkError();
        }
    }


    private void networkError() {
        AppUtility.alertDialog(((Context) varify_accountview), LanguageController.getInstance().
                        getMobileMsgByKey(AppConstant.dialog_alert), LanguageController.getInstance().getMobileMsgByKey
                        (AppConstant.err_check_network), LanguageController.getInstance().getMobileMsgByKey(AppConstant.ok),
                "", new Callable<Boolean>() {
                    @Override
                    public Boolean call() throws Exception {
                        return null;
                    }
                });
    }
}
