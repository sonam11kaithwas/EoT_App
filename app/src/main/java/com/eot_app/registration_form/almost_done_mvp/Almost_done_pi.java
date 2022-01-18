package com.eot_app.registration_form.almost_done_mvp;

import com.eot_app.login_next.login_next_model.Login_Next_Request_MOdel;
import com.eot_app.registration_form.company_model_pkg.CompanyDefaultSetting;

public interface Almost_done_pi {

    boolean RequiredFields(String country, String state, String curency, String currencyFormat, String timezone);

    void getCountryList();

    void getLangugeList();

    void getCurrencyList();

    void getCurrencyFormatList();

    void getTimezoneList();

    void getStateList(String countyName);

    String cntryId(String cntId);

    String statId(String state, String statename);

    void set_CompanyDefault_Setting(CompanyDefaultSetting companyDefault_setting);

    String currencyId(String currency);

    String timeZoneId(String timezone);

    String currencyFormatId(String currency_format);

    String LangaugeId(String laguage);

    void UserLoginServiceCall(Login_Next_Request_MOdel login_next_request_mOdel);

    boolean isValidCountry(String countryaaAaA);

    boolean isValidState(String state);


    String currencyName(String currency);

    String timeZoneName(String timezone);

    String currencyFormatName(String currency_format);

    String LangaugeName(String laguage);

    //   String getCounrencyById(String currencyId);

}
