package com.eot_app.nav_menu.client.clientlist.client_detail.site.sitelist.editsite.editsitedb;

import android.util.Log;

import com.eot_app.utility.AppConstant;
import com.eot_app.utility.Country;
import com.eot_app.utility.Currency;
import com.eot_app.utility.Currency_format;
import com.eot_app.utility.EotApp;
import com.eot_app.utility.Language;
import com.eot_app.utility.Server_location;
import com.eot_app.utility.States;
import com.eot_app.utility.Timezones;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by ubuntu on 26/6/18.
 */

public class SpinnerCountrySite {
    /**
     * Get country list from Json file
     */
    static public List<Country> clientCountryList() {
        ArrayList<Country> country = new ArrayList<>();
        try {
            InputStream is = EotApp.getAppinstance().getResources().getAssets().open(AppConstant.COUNTRYFILE);
            int size = is.available();
            byte[] data = new byte[size];
            is.read(data);
            is.close();
            String json = new String(data);

            JSONObject countries = new JSONObject(json);
            JSONArray cntry = countries.getJSONArray("countries");
            if (cntry != null) {
                for (int i = 0; i < cntry.length(); i++) {
                    country.add(new Country(cntry.getJSONObject(i).getString("id"), cntry.getJSONObject(i).getString("name")));
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException je) {
            Log.e("Error", je.getMessage());
            je.printStackTrace();
        }
        return country;
    }

    /**
     * Get State list from Json file by country ID
     */
    static public List<States> clientStatesList(String cntryId) {
        ArrayList<States> states = new ArrayList<>();
        try {
            states.clear();
            InputStream is = EotApp.getAppinstance().getResources().getAssets().open(AppConstant.STATEFILE);
            int size = is.available();
            byte[] data = new byte[size];
            is.read(data);
            is.close();
            String json = new String(data);
            JSONObject countries = new JSONObject(json);
            JSONArray jsinState = countries.getJSONArray("states");
            if (jsinState != null) {
                for (int i = 0; i < jsinState.length(); i++) {
                    JSONObject jstate = jsinState.getJSONObject(i);
                    if (jstate.getString("country_id").equals(cntryId)) {
                        states.add(new States(jstate.getString("id"), jstate.getString("name"), jstate.getString("country_id")));
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException je) {
            Log.e("Error", je.getMessage());
            je.printStackTrace();
        }
        return states;
    }

    /**
     * Fetch country name by id for set country name in input fields
     */
    public static String getCountryNameById(String ctryID) {
        List<Country> country = clientCountryList();
        for (Country ctry_model : country) {
            if (ctry_model.getId().equals(ctryID)) {
                return ctry_model.getName();
            }
        }
        return "";
    }

    public static String getStatenameById(String ctryId, String stateId) {
        List<States> states = clientStatesList(ctryId);
        for (States state_model : states) {
            if (state_model.getId().equals(stateId)) {
                return state_model.getName();
            }
        }
        return "";
    }

    /**
     * get country id by name
     */
    public static String getCountryId(String name) {
        List<Country> country = clientCountryList();
        for (Country ctry_model : country) {
            if (ctry_model.getName().equals(name)) {
                return ctry_model.getId();
            }
        }
        return "";
    }

    public static String getStateId(String name, String statename) {
        List<States> state = clientStatesList(name);
        for (States state_mdl : state) {
            if (state_mdl.getName().equals(statename)) {
                return state_mdl.getId();
            }
        }
        return String.valueOf(0);
    }

    /**
     * Get Language list from Json file by country ID
     */
    static public List<Language> clientLangugeList() {
        ArrayList<Language> languages = new ArrayList<>();
        try {
            InputStream is = EotApp.getAppinstance().getResources().getAssets().open(AppConstant.LANGUAGEFILE);
            int size = is.available();
            byte[] data = new byte[size];
            is.read(data);
            is.close();
            String json = new String(data);

            JSONObject countries = new JSONObject(json);
            JSONArray cntry = countries.getJSONArray("language");
            if (cntry != null) {
                for (int i = 0; i < cntry.length(); i++) {
                    languages.add(new Language(cntry.getJSONObject(i).getString("lngId"), cntry.getJSONObject(i).getString("nativeName")
                            , cntry.getJSONObject(i).getString("languageCode")));
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException je) {
            Log.e("Error", je.getMessage());
            je.printStackTrace();
        }
        return languages;
    }


    /**
     * Get Currency list from Json file by country ID
     */
    static public List<Currency> clientCurrencyList() {
        ArrayList<Currency> currencies = new ArrayList<>();
        try {
            InputStream is = EotApp.getAppinstance().getResources().getAssets().open(AppConstant.CURRENCYFILE);
            int size = is.available();
            byte[] data = new byte[size];
            is.read(data);
            is.close();
            String json = new String(data);

            JSONObject countries = new JSONObject(json);
            JSONArray cntry = countries.getJSONArray("data");
            if (cntry != null) {
                for (int i = 0; i < cntry.length(); i++) {
                    currencies.add(new Currency(cntry.getJSONObject(i).getString("text"), cntry.getJSONObject(i).getString("id")));
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException je) {
            Log.e("Error", je.getMessage());
            je.printStackTrace();
        }
        return currencies;
    }

    /**
     * Get Currenct-format list from Json file by country ID
     */
    static public List<Currency_format> clientCurrencyFormatList() {
        ArrayList<Currency_format> currenciesFormats = new ArrayList<>();
        try {
            InputStream is = EotApp.getAppinstance().getResources().getAssets().open(AppConstant.CURRENCYFORMATFILE);
            int size = is.available();
            byte[] data = new byte[size];
            is.read(data);
            is.close();
            String json = new String(data);

            JSONObject countries = new JSONObject(json);
            JSONArray cntry = countries.getJSONArray("currency_format");
            if (cntry != null) {
                for (int i = 0; i < cntry.length(); i++) {
                    currenciesFormats.add(new Currency_format(cntry.getJSONObject(i).getString("id"), cntry.getJSONObject(i).getString("text")));
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException je) {
            Log.e("Error", je.getMessage());
            je.printStackTrace();
        }
        return currenciesFormats;
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

    /**
     * get courrency id by name
     */
    public static String getCourrencyId(String name) {
        List<Currency> currency = clientCurrencyList();
        for (Currency curry_model : currency) {
            if (curry_model.getName().equals(name)) {
                return curry_model.getId();
            }
        }
        return "";
    }


    public static String getCourrencyNamebyId(String name) {
        List<Currency> currency = clientCurrencyList();
        for (Currency curry_model : currency) {
            if (curry_model.getId().equals(name)) {
                String[] currencyNm = (curry_model.getText()).split(" ");
                return currencyNm[0];
            }
        }
        return "";
    }


    /**
     * get TimeZone id by name
     */
    public static String getTimezoneId(String name) {
        List<Timezones> timezones = clientTimezonesList();
        for (Timezones timezones_model : timezones) {
            if (timezones_model.getName().equals(name)) {
                return timezones_model.getId();
            }
        }
        return "";
    }

    /*** * get courrency_format id by name
     */
    public static String getCourrencyFormatId(String name) {
        List<Currency_format> currency_formats = clientCurrencyFormatList();
        for (Currency_format curry_format_model : currency_formats) {
            if (curry_format_model.getName().equals(name)) {
                return curry_format_model.getId();
            }
        }
        return "";
    }

    /**
     * get language id by name
     */
    public static String getLanguageId(String name) {
        List<Language> languages = clientLangugeList();
        for (Language language_model : languages) {
            if (language_model.getName().equals(name)) {
                return language_model.getLngId();
            }
        }
        return "";
    }


    /**
     * Get Server-Location list from Json file by country ID
     */
    static public List<Server_location> clientServer_locationList() {
        ArrayList<Server_location> server_locations = new ArrayList<>();
        try {
            InputStream is = EotApp.getAppinstance().getResources().getAssets().open(AppConstant.SERVER_LOCATIONFILE);
            int size = is.available();
            byte[] data = new byte[size];
            is.read(data);
            is.close();
            String json = new String(data);

            JSONObject countries = new JSONObject(json);
            JSONArray cntry = countries.getJSONArray("Server_location");
            if (cntry != null) {
                for (int i = 0; i < cntry.length(); i++) {
                    server_locations.add(new Server_location(cntry.getJSONObject(i).getString("apiCode"), cntry.getJSONObject(i).getString("text")));
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException je) {
            Log.e("Error", je.getMessage());
            je.printStackTrace();
        }
        return server_locations;
    }

    /**
     * get Server location id by name
     */
    public static String getServerLocationId(String name) {
        List<Server_location> server_locations = clientServer_locationList();
        for (Server_location server_location_model : server_locations) {
            if (server_location_model.getName().equals(name)) {
                return server_location_model.getApiCode();
            }
        }
        return "";
    }


    /**
     * get All details for Country For Registration form
     *****/
    static public List<Country> getCountryDetailsList() {
        ArrayList<Country> country = new ArrayList<>();
        try {
            InputStream is = EotApp.getAppinstance().getResources().getAssets().open(AppConstant.COUNTRYFILE);
            int size = is.available();
            byte[] data = new byte[size];
            is.read(data);
            is.close();
            String json = new String(data);

            JSONObject countries = new JSONObject(json);
            JSONArray cntry = countries.getJSONArray("countries");
            if (cntry != null) {
                for (int i = 0; i < cntry.length(); i++) {
                    country.add(new Country(cntry.getJSONObject(i).getString("id"), cntry.getJSONObject(i).getString("name")
                            , cntry.getJSONObject(i).getString("timezone"), cntry.getJSONObject(i).getString("currency"),
                            cntry.getJSONObject(i).getString("languageCode"), cntry.getJSONObject(i).getString("currency_format")));
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException je) {
            Log.e("Error", je.getMessage());
            je.printStackTrace();
        }
        return country;
    }


    //**
//        * get language name by id
//     */
    public static String getLanguageName(String id) {
        List<Language> languages = clientLangugeList();
        for (Language language_model : languages) {
            if (language_model.getLngId().equals(id)) {
                return language_model.getName();
            }
        }
        return "";
    }


    /**
     * get courrency name by id
     */
    public static String getCourrencyName(String id) {
        List<Currency> currency = clientCurrencyList();
        for (Currency curry_model : currency) {
            if (curry_model.getId().equals(id)) {
                return curry_model.getName();
            }
        }
        return "";
    }

    /**
     * get TimeZone name by id
     */
    public static String getTimezoneName(String id) {
        List<Timezones> timezones = clientTimezonesList();
        for (Timezones timezones_model : timezones) {
            if (timezones_model.getId().equals(id)) {
                return timezones_model.getName();
            }
        }
        return "";
    }


    /**
     * get courrency_format name by id
     */
    public static String getCourrencyFormatName(String id) {
        List<Currency_format> currency_formats = clientCurrencyFormatList();
        for (Currency_format curry_format_model : currency_formats) {
            if (curry_format_model.getId().equals(id)) {
                return curry_format_model.getName();
            }
        }
        return "";
    }

    /**
     * get language id by languageCode
     */
    public static String getLanguageIDByLanuageCode(String languageCode) {
        List<Language> languages = clientLangugeList();
        for (Language language_model : languages) {
            if (language_model.getLanguageCode().equals(languageCode)) {
                return language_model.getLngId();
            }
        }
        return "";
    }
}
