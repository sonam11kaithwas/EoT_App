package com.eot_app.registration_form;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;

import androidx.appcompat.app.AppCompatActivity;

import com.eot_app.R;
import com.eot_app.custom_country_dp.CountrySelctListn;
import com.eot_app.login_next.Login2Activity;
import com.eot_app.login_next.login_next_model.Login_Next_Request_MOdel;
import com.eot_app.nav_menu.client.clientlist.client_detail.site.sitelist.editsite.editsitedb.SpinnerCountrySite;
import com.eot_app.nav_menu.jobs.add_job.adapters.FilterCountry;
import com.eot_app.nav_menu.jobs.add_job.adapters.FilterStates;
import com.eot_app.registration_form.adpters.FilterCurrency;
import com.eot_app.registration_form.adpters.FilterCurrency_Format;
import com.eot_app.registration_form.adpters.FilterLanguage;
import com.eot_app.registration_form.adpters.FilterTimeZone;
import com.eot_app.registration_form.almost_done_mvp.Almost_done_pc;
import com.eot_app.registration_form.almost_done_mvp.Almost_done_pi;
import com.eot_app.registration_form.almost_done_mvp.Almost_doneview;
import com.eot_app.registration_form.company_model_pkg.CompanyDefaultSetting;
import com.eot_app.utility.AppConstant;
import com.eot_app.utility.AppUtility;
import com.eot_app.utility.App_preference;
import com.eot_app.utility.Country;
import com.eot_app.utility.Currency;
import com.eot_app.utility.Currency_format;
import com.eot_app.utility.Language;
import com.eot_app.utility.States;
import com.eot_app.utility.Timezones;
import com.eot_app.utility.language_support.LanguageController;
import com.google.android.material.textfield.TextInputLayout;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;

public class Almost_done_Activity extends AppCompatActivity implements View.OnClickListener, CountrySelctListn,
        Almost_doneview {
    private EditText editText_city;
    private AutoCompleteTextView editText_country, editText_state, editText_tiem_zone, editText_language, editText_currency, editText_currency_format;
    private List<Country> countryList;
    private List<Currency> currencyList;
    private List<Currency_format> currencyFormatList;
    private List<Language> languageList;
    private List<Timezones> timezonesList;
    private Almost_done_pi almost_done_pc;
    private String ctry_id = "", state_id = "", currency_id = "", timezone_id = "", currency_format_id = "", language_id = "";
    private TextInputLayout Country_layout, state_layout, city_layout, currency_layout, currencyformat_layout, timezone_layout, language_layout;
    private Button save_btn;
    private String email, pass;
    private Login_Next_Request_MOdel request_mOdel;
    private RelativeLayout relative_main;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_almost_done);
        getSupportActionBar().setTitle("Almost Done");
        Intent intent = getIntent();
        email = intent.getStringExtra("email");
        pass = intent.getStringExtra("pass");
        initializelables();

        getToken();
    }

    private void initializelables() {
        relative_main = findViewById(R.id.relative_main);

        Country_layout = findViewById(R.id.country_layout);
        city_layout = findViewById(R.id.city_layout);
        state_layout = findViewById(R.id.state_layout);
        editText_country = findViewById(R.id.editText_country);
        editText_currency_format = findViewById(R.id.editText_currency_format);
        editText_state = findViewById(R.id.editText_state);


        editText_city = findViewById(R.id.editText_city);
        editText_currency = findViewById(R.id.editText_currency);
        editText_tiem_zone = findViewById(R.id.editText_tiem_zone);
        editText_language = findViewById(R.id.editText_language);


        currency_layout = findViewById(R.id.currency_layout);
        currencyformat_layout = findViewById(R.id.currencyformat_layout);
        timezone_layout = findViewById(R.id.timezone_layout);
        language_layout = findViewById(R.id.language_layout);


        save_btn = findViewById(R.id.save_btn);

        editText_country.setOnClickListener(this);
        editText_state.setOnClickListener(this);
        editText_currency.setOnClickListener(this);
        editText_tiem_zone.setOnClickListener(this);
        editText_language.setOnClickListener(this);
        editText_currency_format.setOnClickListener(this);
        save_btn.setOnClickListener(this);

        almost_done_pc = new Almost_done_pc(this);
        almost_done_pc.getCountryList();
        almost_done_pc.getCurrencyList();
        almost_done_pc.getCurrencyFormatList();
        almost_done_pc.getTimezoneList();
        almost_done_pc.getLangugeList();

        AppUtility.setupUI(relative_main, Almost_done_Activity.this);

        editText_city.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (charSequence.length() >= 1) {
                    city_layout.setHintEnabled(true);
                } else if (charSequence.length() <= 0) {
                    city_layout.setHintEnabled(false);
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

    }

    private void getToken() {
        request_mOdel = new Login_Next_Request_MOdel(email, pass);
        almost_done_pc.UserLoginServiceCall(request_mOdel);
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.editText_language:
                editText_language.showDropDown();
                break;
            case R.id.editText_currency:
                editText_currency.showDropDown();
                break;
            case R.id.editText_currency_format:
                editText_currency_format.showDropDown();
                break;
            case R.id.editText_tiem_zone:
                editText_tiem_zone.showDropDown();
                break;
            case R.id.editText_country:
                editText_country.showDropDown();
                break;
            case R.id.editText_state:
                if (!ctry_id.equals("")) {
                    editText_state.showDropDown();
                }
                break;
            case R.id.save_btn:
                String countryID, stateID, currencyID, currencyFormatID, timeZoneID, languageID;

                currencyID = editText_currency.getText().toString();
                currency_id = almost_done_pc.currencyId(currencyID);

                languageID = editText_language.getText().toString();
                language_id = almost_done_pc.LangaugeId(languageID);

                currencyFormatID = editText_currency_format.getText().toString();
                currency_format_id = almost_done_pc.currencyFormatId(currencyFormatID);

                timeZoneID = editText_tiem_zone.getText().toString();
                timezone_id = almost_done_pc.timeZoneId(timeZoneID);

                countryID = editText_country.getText().toString();
                stateID = editText_state.getText().toString();
                if (ctry_id.equals("")) {
                    ctry_id = almost_done_pc.cntryId(countryID);
                    state_id = almost_done_pc.statId(ctry_id, stateID);
                }

                if (almost_done_pc.RequiredFields(ctry_id, state_id, currency_id, currency_format_id, timezone_id)) {
                    setCompanyDefaultSetting();
                }
                break;
        }
    }


    private void setCompanyDefaultSetting() {
        if (editText_language.getText().toString().isEmpty()) {
            CompanyDefaultSetting companyDefaultSetting = new CompanyDefaultSetting(editText_city.getText().toString(),
                    "01:00", state_id, ctry_id, currency_id, timezone_id, "1", currency_format_id, "");
            almost_done_pc.set_CompanyDefault_Setting(companyDefaultSetting);
        } else {
            CompanyDefaultSetting companyDefaultSetting = new CompanyDefaultSetting(editText_city.getText().toString(),
                    "01:00", state_id, ctry_id, currency_id, timezone_id, language_id, currency_format_id, "");
            almost_done_pc.set_CompanyDefault_Setting(companyDefaultSetting);
        }
    }

    @Override
    public void setCountryList(List<Country> countryList) {
        this.countryList = countryList;
        final FilterCountry countryAdapter = new FilterCountry(this, R.layout.custom_adapter_item_layout, (ArrayList<Country>) countryList);
        editText_country.setAdapter(countryAdapter);
        editText_country.setThreshold(1);
        editText_country.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                ctry_id = ((Country) adapterView.getItemAtPosition(i)).getId();
                /**get state list for Country****/
                almost_done_pc.getStateList(ctry_id);

                Country countryModel = ((Country) adapterView.getItemAtPosition(i));
                setDefailtDataForAll(countryModel);

            }
        });

        editText_country.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (charSequence.length() >= 1) {
                    Country_layout.setHintEnabled(true);
                } else if (charSequence.length() <= 0) {
                    //ctry_id = state_id = currency_id = "";
                    ctry_id = "";
                    Country_layout.setHintEnabled(false);
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
    }

    /**
     * Fill selected Country Releated Data
     * ***
     *
     * @param countryModel
     */
    private void setDefailtDataForAll(Country countryModel) {
        currency_id = countryModel.getCurrency();
        currency_format_id = countryModel.getCurrency_format();
        timezone_id = countryModel.getTimezone();
        String langaugeCode = countryModel.getLanguageCode();
        language_id = SpinnerCountrySite.getLanguageIDByLanuageCode(langaugeCode);

        editText_currency.setText(almost_done_pc.currencyName(currency_id));
        editText_currency_format.setText(almost_done_pc.currencyFormatName(currency_format_id));
        editText_tiem_zone.setText(almost_done_pc.timeZoneName(timezone_id));
        editText_language.setText(almost_done_pc.LangaugeName(language_id));
        Log.e("", "");
    }


    @Override
    public void setCurrencyList(List<Currency> currencyList) {
        this.currencyList = currencyList;

        final FilterCurrency currencyadpter = new FilterCurrency(this, R.layout.custom_adapter_item_layout, (ArrayList<Currency>) currencyList);
        editText_currency.setAdapter(currencyadpter);
        editText_currency.setThreshold(1);
        editText_currency.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                currency_id = ((Currency) adapterView.getItemAtPosition(i)).getId();


            }
        });

        editText_currency.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (charSequence.length() >= 1) {
                    currency_layout.setHintEnabled(true);
                } else if (charSequence.length() <= 0) {
                    currency_layout.setHintEnabled(false);
                    currency_id = "";
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
    }

    @Override
    public void seLanguageList(List<Language> languageList) {
        this.languageList = languageList;

        final FilterLanguage languageadpter = new FilterLanguage(this, R.layout.custom_adapter_item_layout, (ArrayList<Language>) languageList);
        editText_language.setAdapter(languageadpter);
        editText_language.setThreshold(1);
        editText_language.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                language_id = ((Language) adapterView.getItemAtPosition(i)).getLngId();
            }
        });

        editText_language.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (charSequence.length() >= 1) {
                    language_layout.setHintEnabled(true);
                } else if (charSequence.length() <= 0) {
                    language_layout.setHintEnabled(false);
                    language_id = "";
                }


            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
    }


    @Override
    public void setCurrencyFormatList(List<Currency_format> currencyFormatList) {
        this.currencyFormatList = currencyFormatList;
        final FilterCurrency_Format currencyformatadpter = new FilterCurrency_Format(this, R.layout.custom_adapter_item_layout, (ArrayList<Currency_format>) currencyFormatList);
        editText_currency_format.setAdapter(currencyformatadpter);
        editText_currency_format.setThreshold(1);
        editText_currency_format.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                currency_format_id = ((Currency_format) adapterView.getItemAtPosition(i)).getId();
            }
        });

        editText_currency_format.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (charSequence.length() >= 1) {
                    currencyformat_layout.setHintEnabled(true);
                } else if (charSequence.length() <= 0) {
                    currencyformat_layout.setHintEnabled(false);
                    currency_format_id = "";
                }


            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
    }


    @Override
    public void setTimezoneList(List<Timezones> timezoneList) {
        this.timezonesList = timezoneList;
        final FilterTimeZone timeZoneadpter = new FilterTimeZone(this, R.layout.custom_adapter_item_layout, (ArrayList<Timezones>) timezoneList);
        editText_tiem_zone.setAdapter(timeZoneadpter);
        editText_tiem_zone.setThreshold(1);
        editText_tiem_zone.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                timezone_id = ((Timezones) adapterView.getItemAtPosition(i)).getId();
            }
        });

        editText_tiem_zone.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (charSequence.length() >= 1) {
                    timezone_layout.setHintEnabled(true);
                } else if (charSequence.length() <= 0) {
                    timezone_layout.setHintEnabled(false);
                }


            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
    }


    @Override
    public void setStateList(List<States> statesList) {
        FilterStates stateAdapter = new FilterStates(this, R.layout.custom_adapter_item_layout, (ArrayList<States>) statesList);
        setDefaultState(statesList.get(0));
        editText_state.setAdapter(stateAdapter);
        editText_state.setThreshold(0);
        editText_state.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                state_id = ((States) adapterView.getItemAtPosition(i)).getId();
            }
        });

        editText_state.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (charSequence.length() >= 1) {
                    state_layout.setHintEnabled(true);
                } else if (charSequence.length() <= 0) {
                    state_id = "";
                    state_layout.setHintEnabled(false);
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
    }

    private void setDefaultState(States states) {
        editText_state.setText(states.getName());
        state_id = states.getId();
    }

    @Override
    public void errorMsg(String error) {
        showErrorDialog(error);
    }


    @Override
    public void almostDoneSuccessFull() {
        App_preference.getSharedprefInstance().deleteRegistrationToekn();
        Intent intent = new Intent(Almost_done_Activity.this, Login2Activity.class);
        startActivity(intent);
        this.finish();
    }

    private void showErrorDialog(String msg) {
        AppUtility.error_Alert_Dialog(this, msg, LanguageController.getInstance().getMobileMsgByKey(AppConstant.ok)
                , new Callable<Boolean>() {
                    @Override
                    public Boolean call() throws Exception {
                        return null;
                    }
                });
    }

    @Override
    public void selectedCountry(Country country) {

    }

    /*    @Override
        public boolean onOptionsItemSelected(MenuItem item) {
            if (item.getItemId() == android.R.id.home) {
                Toast.makeText(getApplicationContext(), "Please Fill all the Information", Toast.LENGTH_SHORT).show();
                return true;
            }
            return super.onOptionsItemSelected(item);
        }*/
    @Override
    public void onBackPressed() {
        showErrorDialog("Please Fill all the Information");
        // Toast.makeText(getApplicationContext(), "Please Fill all the Information", Toast.LENGTH_SHORT).show();
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        App_preference.getSharedprefInstance().deleteRegistrationToekn();

    }


}
