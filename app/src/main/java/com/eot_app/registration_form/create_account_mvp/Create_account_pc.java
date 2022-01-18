package com.eot_app.registration_form.create_account_mvp;

import android.content.Context;
import android.util.Log;
import android.util.Patterns;

import com.eot_app.nav_menu.client.clientlist.client_detail.site.sitelist.editsite.editsitedb.SpinnerCountrySite;
import com.eot_app.registration_form.company_model_pkg.Company;
import com.eot_app.registration_form.company_model_pkg.VerifyEmail;
import com.eot_app.services.ApiClient;
import com.eot_app.services.Service_apis;
import com.eot_app.utility.AppConstant;
import com.eot_app.utility.AppUtility;
import com.eot_app.utility.App_preference;
import com.eot_app.utility.EotApp;
import com.eot_app.utility.Server_location;
import com.eot_app.utility.language_support.LanguageController;
import com.google.gson.JsonObject;
import com.hypertrack.hyperlog.HyperLog;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Callable;

import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;


public class Create_account_pc implements Create_account_pi {
    private final Create_accountview create_accountview;
    private List<Server_location> server_locations;

    public Create_account_pc(Create_accountview create_accountview) {
        this.create_accountview = create_accountview;
    }


    @Override
    public boolean RequiredFields(String companyname, String email, String pass, String server_location) {
        if (companyname.equals("")) {
            create_accountview.errorMsg(AppConstant.err_compny_message1);
            return false;
        } else if (companyname.length() < 3) {
            create_accountview.errorMsg(AppConstant.compny_length_check1);
            return false;
        } else if (email.equals("")) {
            create_accountview.errorMsg(AppConstant.email_empty1);
            return false;
        } else if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            create_accountview.errorMsg(AppConstant.email_error1);
            return false;
        } else if (pass.equals("")) {
            create_accountview.errorMsg(AppConstant.empty_pass1);
            return false;
        } else if (pass.length() < 3) {
            create_accountview.errorMsg(AppConstant.pass_length_check1);
            return false;
        } else if (server_location.equals("")) {
            create_accountview.errorMsg(AppConstant.err_server_type1);
            return false;
        }
        return true;
    }

    @Override
    public void doRegistration(final Company company, String server_location) {
        if (AppUtility.isInternetConnected()) {

            ApiClient.resetClientforBaseurl();
            App_preference.getSharedprefInstance().setBaseURL(AppConstant.server(server_location));

            //  AppUtility.progressBarShow((Context) create_accountview);


            ApiClient.getservices().registration(Service_apis.addCompany, company.getName(),
                    company.getEmail(), company.getPass(), company.getPlanId())
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new Observer<JsonObject>() {
                        @Override
                        public void onSubscribe(Disposable d) {

                        }

                        @Override
                        public void onNext(JsonObject jsonObject) {

                            if (jsonObject.get("success").getAsBoolean()) {
                                //   EotApp.getAppinstance().showToastmsg(LanguageController.getInstance().getServerMsgByKey(jsonObject.get("message").getAsString()));
                                try {
                                    if (jsonObject != null) {


                                        String message = jsonObject.get("message").getAsString();
                                        String email = company.getEmail();
                                        create_accountview.verify_account_activity_open(email, message);


                                    }
                                } catch (Exception e) {
                                    e.printStackTrace();

                                }
                            } else if (jsonObject.get("statusCode") != null && jsonObject.get("statusCode").getAsString().equals(AppConstant.SESSION_EXPIRE)) {
                                EotApp.getAppinstance().sessionExpired();
                            } else {
                                if (LanguageController.getInstance().getServerMsgByKey(jsonObject.get("message").getAsString()).equals("invalid_domain")) {
                                    create_accountview.errorMsg(AppConstant.invalid_domain);
                                } else
                                    create_accountview.errorMsg(LanguageController.getInstance().getServerMsgByKey(jsonObject.get("message").getAsString()));

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
    public void verify_Email(VerifyEmail verifyEmail) {
        if (AppUtility.isInternetConnected()) {

            AppUtility.progressBarShow((Context) create_accountview);

            Map<String, String> verifyCode = new HashMap<>();
            verifyCode.put("email", verifyEmail.getEmail());

            ApiClient.resetClientforBaseurl();
            App_preference.getSharedprefInstance().setBaseURL(AppConstant.server(""));


            ApiClient.getservices().verifyemail(Service_apis.verifyEmail, verifyCode)
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

                                    Boolean success = jsonObject.get("success").getAsBoolean();
                                    create_accountview.email_Alreday_Existes(success);

                                } catch (Exception e) {
                                    e.printStackTrace();

                                }
                            } else if (jsonObject.get("statusCode") != null && jsonObject.get("statusCode").getAsString().equals(AppConstant.SESSION_EXPIRE)) {

                                EotApp.getAppinstance().sessionExpired();
                            } else {
                                create_accountview.errorMsg(AppConstant.email_add_alredy_taken1);
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
                            // Log.e("TAG", "");
                        }
                    });
        } else {
            networkError();
        }

    }

    @Override
    public void getServerLocationList() {
        server_locations = SpinnerCountrySite.clientServer_locationList();// clientCountryList("countries.json");
        create_accountview.setServerLocationList(server_locations);
    }

    @Override
    public String serverLocationId(String server_location) {
        String serverId = SpinnerCountrySite.getServerLocationId(server_location);
        return serverId;
    }


    private void networkError() {
        AppUtility.alertDialog(((Context) create_accountview), LanguageController.getInstance().
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
