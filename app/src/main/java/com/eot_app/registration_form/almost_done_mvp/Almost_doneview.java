package com.eot_app.registration_form.almost_done_mvp;

import com.eot_app.utility.Country;
import com.eot_app.utility.Currency;
import com.eot_app.utility.Currency_format;
import com.eot_app.utility.Language;
import com.eot_app.utility.States;
import com.eot_app.utility.Timezones;

import java.util.List;

public interface Almost_doneview {

    void setCountryList(List<Country> countryList);


    void setCurrencyList(List<Currency> countryList);

    void seLanguageList(List<Language> languageList);

    void setCurrencyFormatList(List<Currency_format> currencyFormatList);

    void setTimezoneList(List<Timezones> timezoneList);

    void setStateList(List<States> statesList);

    void errorMsg(String error);

    void almostDoneSuccessFull();

}
