package com.eot_app.registration_form.almost_done_mvp;

import android.content.Context;
import android.util.Log;

import com.eot_app.login_next.login_next_model.Login_Next_Request_MOdel;
import com.eot_app.nav_menu.client.clientlist.client_detail.site.sitelist.editsite.editsitedb.SpinnerCountrySite;
import com.eot_app.registration_form.company_model_pkg.CompanyDefaultSetting;
import com.eot_app.services.ApiClient;
import com.eot_app.services.Service_apis;
import com.eot_app.utility.AppConstant;
import com.eot_app.utility.AppUtility;
import com.eot_app.utility.App_preference;
import com.eot_app.utility.Country;
import com.eot_app.utility.Currency;
import com.eot_app.utility.Currency_format;
import com.eot_app.utility.EotApp;
import com.eot_app.utility.Language;
import com.eot_app.utility.States;
import com.eot_app.utility.Timezones;
import com.eot_app.utility.language_support.LanguageController;
import com.google.gson.JsonObject;
import com.hypertrack.hyperlog.HyperLog;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Callable;

import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

public class Almost_done_pc implements Almost_done_pi {
    private final Almost_doneview almost_doneview;
    private List<States> statesList;
    private List<Country> countryList;
    private List<Currency> currencyList;
    private List<Currency_format> currencyFormatList;
    private List<Language> languageList;
    private List<Timezones> timezonesList;


    public Almost_done_pc(Almost_doneview almost_doneview) {
        this.almost_doneview = almost_doneview;
    }

    /**
     * Get Currenct list from Json file by country ID
     */
    static public List<Timezones> clientTimezonesList() {
        ArrayList<Timezones> timezones = new ArrayList<>();
        try {
            InputStream is = EotApp.getAppinstance().getResources().getAssets().open(AppConstant.TIMEZONEFILE);
            int size = is.available();
            byte[] data = new byte[size];
            is.read(data);
            is.close();
            String json = new String(data);

            JSONObject countries = new JSONObject(json);
            JSONArray cntry = countries.getJSONArray("timezones");
            if (cntry != null) {
                for (int i = 0; i < cntry.length(); i++) {
                    timezones.add(new Timezones(cntry.getJSONObject(i).getString("id"), cntry.getJSONObject(i).getString("text")));
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException je) {
            Log.e("Error", je.getMessage());
            je.printStackTrace();
        }
        return timezones;
    }

    @Override
    public boolean isValidCountry(String country) {
        for (Country ctry : countryList) {
            if (ctry.getId().equalsIgnoreCase(country))
                return true;
        }
        return false;
    }

    @Override
    public boolean isValidState(String state) {
        for (States item : statesList) {
            if (item.getId().equalsIgnoreCase(state))
                return true;
        }
        return false;
    }

    @Override
    public boolean RequiredFields(String country, String state, String curency, String currencyFormat, String timezone) {
        if (!isValidCountry(country)) {
            almost_doneview.errorMsg(AppConstant.please_select_country_first1);
            return false;
        } else if (!isValidState(state)) {
            almost_doneview.errorMsg(AppConstant.state_error1);
            return false;
        } else if (currencyFormat.isEmpty()) {
            almost_doneview.errorMsg(AppConstant.err_currenyformat_type1);
            return false;
        } else if (timezone.isEmpty()) {
            almost_doneview.errorMsg(AppConstant.err_timezone_type1);
            return false;
        } else {
            return true;
        }
    }


    @Override
    public void getCountryList() {
        countryList = SpinnerCountrySite.getCountryDetailsList();// clientCountryList("countries.json");
        almost_doneview.setCountryList(countryList);
    }

    @Override
    public void getLangugeList() {
        languageList = SpinnerCountrySite.clientLangugeList();// clientCountryList("countries.json");
        almost_doneview.seLanguageList(languageList);
    }

    @Override
    public void getCurrencyList() {
        currencyList = SpinnerCountrySite.clientCurrencyList();// clientCountryList("countries.json");
        almost_doneview.setCurrencyList(currencyList);
    }

    @Override
    public void getCurrencyFormatList() {
        currencyFormatList = SpinnerCountrySite.clientCurrencyFormatList();// clientCountryList("countries.json");
        almost_doneview.setCurrencyFormatList(currencyFormatList);
    }

    @Override
    public void getTimezoneList() {
        timezonesList = SpinnerCountrySite.clientTimezonesList();// clientCountryList("countries.json");
        almost_doneview.setTimezoneList(timezonesList);
    }

    @Override
    public void getStateList(String countyId) {
        statesList = SpinnerCountrySite.clientStatesList(countyId);//clientStatesList("states.json", countyId);
        almost_doneview.setStateList(statesList);
    }

    @Override
    public String cntryId(String country) {
        String cId = SpinnerCountrySite.getCountryId(country);
        statesList = SpinnerCountrySite.clientStatesList(cId);
        return cId;
    }

    @Override
    public String statId(String state, String statename) {
        String sId = SpinnerCountrySite.getStateId(state, statename);
        return sId;
    }


    @Override
    public void set_CompanyDefault_Setting(CompanyDefaultSetting companyDefault_setting) {
        if (AppUtility.isInternetConnected()) {
            AppUtility.progressBarShow((Context) almost_doneview);
            Map<String, String> compCode = new HashMap<>();
            compCode.put("city", companyDefault_setting.getCity());
            compCode.put("duration", companyDefault_setting.getDuration());
            compCode.put("state", companyDefault_setting.getState());
            compCode.put("ctry", companyDefault_setting.getCtry());
            compCode.put("cur", companyDefault_setting.getCur());
            compCode.put("timezone", companyDefault_setting.getTimezone());
            compCode.put("lngId", companyDefault_setting.getLngId());
            compCode.put("localId", companyDefault_setting.getLocalId());
            compCode.put("mob", companyDefault_setting.getMob());

            ApiClient.getservices().setAlmostDone(AppUtility.getResgistrationApiHeaders(), Service_apis.setCompanyDefaultSetting,
                    compCode)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new Observer<JsonObject>() {
                        @Override
                        public void onSubscribe(Disposable d) {

                        }

                        @Override
                        public void onNext(JsonObject jsonObject) {
                            if (jsonObject.get("success").getAsBoolean()) {
                                EotApp.getAppinstance().showToastmsg(AppConstant.almost_done_message1);
                                try {
                                    if (jsonObject != null) {
                                        almost_doneview.almostDoneSuccessFull();
                                    }
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                            } else if (jsonObject.get("statusCode") != null && jsonObject.get("statusCode").getAsString().equals(AppConstant.SESSION_EXPIRE)) {
                                EotApp.getAppinstance().sessionExpired();
                            } else {
                                almost_doneview.errorMsg(LanguageController.getInstance().getServerMsgByKey(jsonObject.get("message").getAsString()));
                            }

                        }


                        @Override
                        public void onError(Throwable e) {
                            AppUtility.progressBarDissMiss();
                            HyperLog.i("", e.getMessage());
                            almost_doneview.errorMsg(e.getMessage());
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
    public String currencyId(String currency) {
        String currynId = SpinnerCountrySite.getCourrencyId(currency);
        return currynId;
    }

    @Override
    public String timeZoneId(String timezone) {
        String timezoneId = SpinnerCountrySite.getTimezoneId(timezone);
        return timezoneId;
    }

    @Override
    public String currencyFormatId(String currency_format) {
        String currency_formatId = SpinnerCountrySite.getCourrencyFormatId(currency_format);
        return currency_formatId;
    }

    @Override
    public String LangaugeId(String laguage) {
        String languageId = SpinnerCountrySite.getLanguageId(laguage);
        return languageId;
    }

    @Override
    public void UserLoginServiceCall(Login_Next_Request_MOdel login_next_request_model) {

        if (AppUtility.isInternetConnected()) {

            //   AppUtility.progressBarShow((Context) almost_doneview);


            Map<String, String> login = new HashMap<>();
            login.put("email", login_next_request_model.getEmail());
            login.put("cc", login_next_request_model.getCc());
            login.put("pass", login_next_request_model.getPass());
            login.put("devType", login_next_request_model.getDevType());
            login.put("devId", login_next_request_model.getDevId());


            ApiClient.getservices().login(Service_apis.login, login).subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new Observer<JsonObject>() {
                        @Override
                        public void onSubscribe(Disposable d) {
                        }

                        @Override
                        public void onNext(JsonObject responseBody) {
                            if (responseBody.get("success").getAsBoolean()) {

                                String token = responseBody.getAsJsonArray("data").get(0).getAsJsonObject().get("token").getAsString();
                                App_preference.getSharedprefInstance().setResgistrationToken(token);

                            }
                        }

                        @Override
                        public void onError(Throwable e) {
                            Log.e("Error", e.getMessage());
                            //AppUtility.progressBarDissMiss();
                        }

                        @Override
                        public void onComplete() {
                            // AppUtility.progressBarDissMiss();
                        }
                    });
        }
    }


    private void networkError() {
        AppUtility.alertDialog(((Context) almost_doneview), LanguageController.getInstance().
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
    public String currencyName(String currency) {
        String currynName = SpinnerCountrySite.getCourrencyName(currency);
        return currynName;
    }

    @Override
    public String timeZoneName(String timezone) {
        String timezoneName = SpinnerCountrySite.getTimezoneName(timezone);
        return timezoneName;
    }

    @Override
    public String currencyFormatName(String currency_format) {
        String currency_formatName = SpinnerCountrySite.getCourrencyFormatName(currency_format);
        return currency_formatName;
    }

    @Override
    public String LangaugeName(String laguage) {
        String languageName = SpinnerCountrySite.getLanguageName(laguage);
        return languageName;
    }

}
