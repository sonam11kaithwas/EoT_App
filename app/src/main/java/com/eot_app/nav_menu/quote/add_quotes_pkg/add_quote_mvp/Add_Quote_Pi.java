package com.eot_app.nav_menu.quote.add_quotes_pkg.add_quote_mvp;

import com.eot_app.nav_menu.quote.add_quotes_pkg.model_pkg.Add_Quote_ReQ;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public interface Add_Quote_Pi {
    void getJobServices();

    void getClientList();

    void getCountryList();

    void getStateList(String countyId);

    void getContactList(String cltId);

    void getSilteList(String stId);

    void addQuotes(Add_Quote_ReQ requestModel);

    boolean requiredFileds(Set<String> jobType, String cltId, String adr, String countryId, String stateId, String mob, String alterNateMob, String email);

    boolean isValidCountry(String countryaaAaA);

    boolean isValidState(String state);

    String cntryId(String cntId);

    String statId(String state, String statename);

    void getActiveUserList();

    void getTermsConditions();

    void addQuoteWithDocuments(Add_Quote_ReQ add_quote_reQ, ArrayList links, List<String> fileNames);
}
