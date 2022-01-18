package com.eot_app.nav_menu.client.add_client;

import static android.location.LocationManager.GPS_PROVIDER;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.IntentSender;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.eot_app.R;
import com.eot_app.home_screens.MainActivity;
import com.eot_app.nav_menu.client.add_client.addclient_model_pkg.AddClientModel;
import com.eot_app.nav_menu.client.add_client.addclient_mvp.AddClient_Pc;
import com.eot_app.nav_menu.client.add_client.addclient_mvp.AddClient_Pi;
import com.eot_app.nav_menu.client.add_client.addclient_mvp.AddClient_View;
import com.eot_app.nav_menu.client.add_client.clientadpter.ClientIndustryAdpter;
import com.eot_app.nav_menu.client.add_client.clientadpter.ClientRefrenceAdpter;
import com.eot_app.nav_menu.client.clientlist.client_detail.site.sitelist.editsite.editsitedb.SpinnerCountrySite;
import com.eot_app.nav_menu.jobs.add_job.adapters.FilterCountry;
import com.eot_app.nav_menu.jobs.add_job.adapters.FilterStates;
import com.eot_app.utility.AppConstant;
import com.eot_app.utility.AppUtility;
import com.eot_app.utility.App_preference;
import com.eot_app.utility.Country;
import com.eot_app.utility.CurrLatLngCntrlr;
import com.eot_app.utility.EotApp;
import com.eot_app.utility.States;
import com.eot_app.utility.db.AppDataBase;
import com.eot_app.utility.language_support.LanguageController;
import com.eot_app.utility.settings.client_refrence_db.ClientRefrenceModel;
import com.eot_app.utility.settings.clientindustry_db.ClientIndustryModel;
import com.eot_app.utility.util_interfaces.MySpinnerAdapter;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.common.api.ResolvableApiException;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationSettingsResponse;
import com.google.android.gms.location.LocationSettingsStatusCodes;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputLayout;
import com.hypertrack.hyperlog.HyperLog;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;

public class AddClient extends AppCompatActivity implements AddClient_View, View.OnClickListener, Spinner.OnItemSelectedListener, TextWatcher {
    public static final int LOCATION_REQUEST = 1000;
    EditText c_name, c_email, c_mob, c_gst, c_tin, c_add, c_city, c_zip, c_notes, edt_lng, edt_lat;
    EditText c_contact_name, c_site_name;
    Button save_clnt;
    Spinner spinn_industry, c_account, referenceDp;
    AutoCompleteTextView client_cntry, client_state;
    AddClient_Pi addClient_pi;
    int accountId, reference = 0;
    int indusId;
    String cntryId;
    String stateId;
    TextInputLayout input_layout_clientname, input_layout_client_email, input_layout_clientmobile, layout_gst_no, layout_tin_no, client_adr_layout, client_lat_layout, client_lng_layout, client_city_layout, client_country_layout, client_state_layout, client_zip_layout, layout_client_notes;
    TextInputLayout client_contact_layout, client_site_layout;
    RelativeLayout relative_main;
    TextView hint_tv_ac, tv_spinner_account, tv_spinner_ind, tv_hint_indus, hint_tv_reference, tv_spinner_reference;
    LinearLayout linearLayout_account, indus_linearLayout, linearLayout_reference;
    String[] accountArray;
    LocationManager locationManager;
    private View lng_view, lat_view;
    private View lat_lng_view_lay;
    private TextView lat_lng_txt_lable;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_client);
        relative_main = findViewById(R.id.relative_main);
        AppUtility.setupUI(relative_main, AddClient.this);

        setTitle(LanguageController.getInstance().getMobileMsgByKey(AppConstant.add_client));
        initializelables();
        intializeTextInputLayout();
        intializeEditText();

        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);


        addClient_pi.accountItemSpinner();
        setIndustry();
        addClient_pi.clientCountryList();
        setCompanySettingAdrs();
        setRefrences();

    }

    private void setCompanySettingAdrs() {
        client_cntry.setText(SpinnerCountrySite.getCountryNameById(App_preference.getSharedprefInstance().getCompanySettingsDetails().getCtry()));
        addClient_pi.clientStatesList((App_preference.getSharedprefInstance().getCompanySettingsDetails().getCtry()));
        client_state_layout.setHintEnabled(true);
        client_state.setText(SpinnerCountrySite.getStatenameById(App_preference.getSharedprefInstance().getCompanySettingsDetails().getCtry(), App_preference.getSharedprefInstance().getCompanySettingsDetails().getState()));
        c_city.setText(App_preference.getSharedprefInstance().getCompanySettingsDetails().getCity());
        stateId = (App_preference.getSharedprefInstance().getCompanySettingsDetails().getState());
        cntryId = (App_preference.getSharedprefInstance().getCompanySettingsDetails().getCtry());

        try {
            if (App_preference.getSharedprefInstance().getLoginRes().getCtryCode() != null) {
                c_mob.setText(App_preference.getSharedprefInstance().getLoginRes().getCtryCode());
            }
        } catch (Exception e) {
            e.getMessage();
        }
    }


    private void getLocation() {
        try {
            if (ActivityCompat.checkSelfPermission(
                    AddClient.this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                    && ActivityCompat.checkSelfPermission(
                    AddClient.this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                        LOCATION_REQUEST);
            } else {
                Location locationGPS = null;
                try {
                    if (Build.VERSION.SDK_INT >= 30) {
                        locationGPS = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
                        HyperLog.i("AddClient", "getLocation-" + "above version 11");
                    } else {
                        locationGPS = locationManager.getLastKnownLocation(GPS_PROVIDER);
                        HyperLog.i("AddClient", "getLocation-" + "below version 10");
                    }
                } catch (Exception exception) {
                    getLatLngCntr();
                    exception.getMessage();
                    HyperLog.i("AddClient", "getLocation-" + exception.getMessage());

                }


                if (locationGPS != null) {
                    double lat = locationGPS.getLatitude();
                    double longi = locationGPS.getLongitude();
                    try {
                        DecimalFormat dFormat = new DecimalFormat("#.######");
                        lat = Double.valueOf(dFormat.format(lat));
                        longi = Double.valueOf(dFormat.format(longi));
                    } catch (Exception e) {
                        e.printStackTrace();
                        lat = locationGPS.getLatitude();
                        longi = locationGPS.getLongitude();
                    }

                    String latitude = String.valueOf(lat);
                    String longitude = String.valueOf(longi);
                    edt_lat.setText(latitude);
                    edt_lng.setText(longitude);
                } else {
                    HyperLog.i("AddClient", "getLocation-" + "Unable to find location.");
                    getLatLngCntr();
                }
            }

        } catch (Exception exception) {
            getLatLngCntr();
            HyperLog.i("AddClient", exception.getMessage());
            exception.getMessage();
        }
    }

    private void getLatLngCntr() {
        CurrLatLngCntrlr.getInstance().getCurrLatLng(new CurrLatLngCntrlr.GetLatLng() {
            @Override
            public void getLatLngs(String lat, String lng) {
                edt_lat.setText(lat);
                edt_lng.setText(lng);
            }
        });
    }


    public void displayLocationSettingsRequest() {
        LocationRequest locationRequest = LocationRequest.create().setPriority(LocationRequest.PRIORITY_LOW_POWER).setInterval(2000);

        LocationSettingsRequest.Builder builder = new LocationSettingsRequest.Builder()
                .addLocationRequest(locationRequest);
        Task<LocationSettingsResponse> result =
                LocationServices.getSettingsClient(this).checkLocationSettings(builder.build());


        result.addOnCompleteListener(new OnCompleteListener<LocationSettingsResponse>() {
            @Override
            public void onComplete(@NonNull Task<LocationSettingsResponse> task) {
                try {
                    LocationSettingsResponse response = task.getResult(ApiException.class);
                    // All location settings are satisfied. The client can initialize location
                    // requests here.
                    if (ContextCompat.checkSelfPermission(AddClient.this, Manifest.permission.ACCESS_FINE_LOCATION)
                            == PackageManager.PERMISSION_GRANTED) {
                        locationManager.requestLocationUpdates(GPS_PROVIDER, 2000, 100, new LocationListener() {
                            @Override
                            public void onLocationChanged(Location location) {
                                if (location != null) {
                                    double lat = location.getLatitude();
                                    double longi = location.getLongitude();
                                    String latitude = String.valueOf(lat);
                                    String longitude = String.valueOf(longi);
                                    edt_lat.setText(latitude);
                                    edt_lng.setText(longitude);
                                    locationManager.removeUpdates(this);
                                } else {
                                    HyperLog.i("EditSiteActivity", "requestLocationUpdates()" + "location Null");

                                }
                            }

                            @Override
                            public void onStatusChanged(String provider, int status, Bundle extras) {

                            }

                            @Override
                            public void onProviderEnabled(String provider) {

                            }

                            @Override
                            public void onProviderDisabled(String provider) {

                            }
                        });
                    }
                } catch (ApiException exception) {
                    switch (exception.getStatusCode()) {
                        case LocationSettingsStatusCodes.RESOLUTION_REQUIRED:
                            // Location settings are not satisfied. But could be fixed by showing the
                            // user a dialog.
                            try {
                                // Cast to a resolvable exception.
                                ResolvableApiException resolvable = (ResolvableApiException) exception;
                                // Show the dialog by calling startResolutionForResult(),
                                // and check the result in onActivityResult().
                                resolvable.startResolutionForResult(AddClient.this, 5000);
                                break;
                            } catch (IntentSender.SendIntentException e) {
                                // Ignore the error.
                            } catch (ClassCastException e) {
                                // Ignore, should be an impossible error.
                            }
                            break;
                        case LocationSettingsStatusCodes.SETTINGS_CHANGE_UNAVAILABLE:
                            // Location settings are not satisfied. However, we have no way to fix the
                            // settings so we won't show the dialog.

                            break;
                    }
                }
            }
        });
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        Log.e("permission", "permission");
        switch (requestCode) {
            case LOCATION_REQUEST: {
                //location for FW
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    getLocation();
                } else {
                    HyperLog.i("TAG", "Location Permission denied from user");
                    showErrorDialog(LanguageController.getInstance().getMobileMsgByKey(AppConstant.loc_permission_deny));
                }
                break;
            }
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    private void initializelables() {
        c_name = findViewById(R.id.c_name);
        c_name.setHint((LanguageController.getInstance().getMobileMsgByKey(AppConstant.client_name)) + " *");

        tv_spinner_account = findViewById(R.id.tv_spinner_account);
        tv_spinner_account.setHint(LanguageController.getInstance().getMobileMsgByKey(AppConstant.account_type));

        c_email = findViewById(R.id.c_email);
        c_email.setHint((LanguageController.getInstance().getMobileMsgByKey(AppConstant.client_email)));//+ " *"

        c_mob = findViewById(R.id.c_mob);
        c_mob.setHint((LanguageController.getInstance().getMobileMsgByKey(AppConstant.mob_no)));// add+ " *"

        c_gst = findViewById(R.id.c_gst);
        if (App_preference.getSharedprefInstance().getCompanySettingsDetails()
                .getGstLabel() != null)
            c_gst.setHint(App_preference.getSharedprefInstance().getCompanySettingsDetails()
                    .getGstLabel());

        c_tin = findViewById(R.id.c_tin);
        if (App_preference.getSharedprefInstance().getCompanySettingsDetails()
                .getTinLabel() != null)
            c_tin.setHint(App_preference.getSharedprefInstance().getCompanySettingsDetails()
                    .getTinLabel());

        tv_spinner_ind = findViewById(R.id.tv_spinner_ind);
        tv_spinner_ind.setHint(LanguageController.getInstance().getMobileMsgByKey(AppConstant.industry));

        c_add = findViewById(R.id.c_add);
        c_add.setHint((LanguageController.getInstance().getMobileMsgByKey(AppConstant.address)) + " *");

        client_cntry = findViewById(R.id.client_cntry);
        client_cntry.setHint((LanguageController.getInstance().getMobileMsgByKey(AppConstant.country)) + " *");

        client_state = findViewById(R.id.client_state);
        client_state.setHint((LanguageController.getInstance().getMobileMsgByKey(AppConstant.state)) + " *");

        c_city = findViewById(R.id.c_city);
        c_city.setHint(LanguageController.getInstance().getMobileMsgByKey(AppConstant.city));

        c_zip = findViewById(R.id.c_zip);
        c_zip.setHint(LanguageController.getInstance().getMobileMsgByKey(AppConstant.postal_code));

        c_notes = findViewById(R.id.c_notes);
        c_notes.setHint(LanguageController.getInstance().getMobileMsgByKey(AppConstant.notes));

        save_clnt = findViewById(R.id.save_clnt);
        save_clnt.setText(LanguageController.getInstance().getMobileMsgByKey(AppConstant.create_client));

        hint_tv_ac = findViewById(R.id.hint_tv_ac);
        tv_hint_indus = findViewById(R.id.tv_hint_indus);

        client_lat_layout = findViewById(R.id.client_lat_layout);
        edt_lat = findViewById(R.id.edt_lat);
        edt_lat.setHint(LanguageController.getInstance().getMobileMsgByKey(AppConstant.latitude));

        client_lng_layout = findViewById(R.id.client_lng_layout);
        edt_lng = findViewById(R.id.edt_lng);
        edt_lng.setHint(LanguageController.getInstance().getMobileMsgByKey(AppConstant.longitued));


        spinn_industry = findViewById(R.id.spinn_industry);

        lat_view = findViewById(R.id.lat_view);
        lng_view = findViewById(R.id.lng_view);
        //lat_lng_btn = findViewById(R.id.lat_lng_btn);
        lat_lng_view_lay = findViewById(R.id.lat_lng_view_lay);

        /** hide/show lat lng filed accroding to Admin permission*/
        if (App_preference.getSharedprefInstance().getCompanySettingsDetails().getIsJobLatLngEnable().equals("0")) {
            lng_view.setVisibility(View.GONE);
            lat_view.setVisibility(View.GONE);
            client_lat_layout.setVisibility(View.GONE);
            client_lng_layout.setVisibility(View.GONE);
            lat_lng_view_lay.setVisibility(View.GONE);
        }

        /**Reference View find***/
        linearLayout_reference = findViewById(R.id.linearLayout_reference);
        referenceDp = findViewById(R.id.referenceDp);
        hint_tv_reference = findViewById(R.id.hint_tv_reference);
        tv_spinner_reference = findViewById(R.id.tv_spinner_reference);

        hint_tv_reference.setHint((LanguageController.getInstance().getMobileMsgByKey(AppConstant.reference)));
        tv_spinner_reference.setHint(LanguageController.getInstance().getMobileMsgByKey(AppConstant.reference));


        c_contact_name = findViewById(R.id.c_contact_name);
        c_contact_name.setHint(LanguageController.getInstance().getMobileMsgByKey(AppConstant.contact_name) + " *");

        c_site_name = findViewById(R.id.c_site_name);
        c_site_name.setHint(LanguageController.getInstance().getMobileMsgByKey(AppConstant.site_name) + " *");

        lat_lng_txt_lable = findViewById(R.id.lat_lng_txt_lable);
        lat_lng_txt_lable.setText(LanguageController.getInstance().getMobileMsgByKey(AppConstant.get_current_lat_long));
        lat_lng_view_lay.setOnClickListener(this);

    }

    @Override
    public void setAccountType(ArrayList<String> accountList) {
        AppUtility.spinnerPopUpWindow(c_account);
        accountArray = accountList.toArray(new String[accountList.size()]);
        c_account.setAdapter(new MySpinnerAdapter(this, accountArray));
        c_account.setOnItemSelectedListener(this);
    }

    @Override
    public void setRefrences() {
        AppUtility.spinnerPopUpWindow(referenceDp);
        try {
            final List<ClientRefrenceModel> list = AppDataBase.getInMemoryDatabase(EotApp.getAppinstance()).clientRefrenceDao().getRefrenceList();


            referenceDp.setAdapter(new ClientRefrenceAdpter(this, list));

            referenceDp.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    hint_tv_reference.setVisibility(View.VISIBLE);
                    hint_tv_reference.setText(LanguageController.getInstance().getMobileMsgByKey(AppConstant.reference));
                    tv_spinner_reference.setText(list.get(position).getRefName());
                    reference = Integer.parseInt(list.get(position).getRefId());
                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {

                }
            });

        } catch (Exception exception) {
            exception.printStackTrace();
        }
    }

    @Override
    public void setIndustry() {
        final List<ClientIndustryModel> getIndustryList = AppDataBase.getInMemoryDatabase(this).clientIndustryDao().getIndustryList();
        AppUtility.spinnerPopUpWindow(spinn_industry);
        spinn_industry.setAdapter(new ClientIndustryAdpter(this, getIndustryList));

        spinn_industry.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                tv_hint_indus.setVisibility(View.VISIBLE);
                tv_hint_indus.setText(LanguageController.getInstance().getMobileMsgByKey(AppConstant.industry));
                tv_spinner_ind.setText(getIndustryList.get(position).getIndustryName());
                indusId = Integer.parseInt(getIndustryList.get(position).getIndustryId());
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }


    private void intializeEditText() {
        save_clnt.setOnClickListener(this);
        c_account = findViewById(R.id.c_account);


        linearLayout_account = findViewById(R.id.linearLayout_account);

        indus_linearLayout = findViewById(R.id.indus_linearLayout);


        linearLayout_account.setOnClickListener(this);
        indus_linearLayout.setOnClickListener(this);
        client_state.setOnClickListener(this);
        client_cntry.setOnClickListener(this);
        linearLayout_reference.setOnClickListener(this);


        c_name.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (TextUtils.isEmpty(s)) {
                    c_contact_name.setText("");
                    c_site_name.setText("");
                } else {
                    if (TextUtils.isEmpty(c_contact_name.getText().toString()))
                        c_contact_name.setText("Self");
                    if (TextUtils.isEmpty(c_site_name.getText().toString()))
                        c_site_name.setText("Self");
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });


        addClient_pi = new AddClient_Pc(this);

    }

    private void intializeTextInputLayout() {
        input_layout_clientname = findViewById(R.id.input_layout_clientname);
        input_layout_client_email = findViewById(R.id.input_layout_client_email);
        input_layout_clientmobile = findViewById(R.id.input_layout_clientmobile);
        layout_gst_no = findViewById(R.id.layout_gst_no);
        layout_tin_no = findViewById(R.id.layout_tin_no);
        client_adr_layout = findViewById(R.id.client_adr_layout);
        client_city_layout = findViewById(R.id.client_city_layout);
        client_country_layout = findViewById(R.id.client_country_layout);
        client_state_layout = findViewById(R.id.client_state_layout);
        client_zip_layout = findViewById(R.id.client_zip_layout);
        layout_client_notes = findViewById(R.id.layout_client_notes);

        client_contact_layout = findViewById(R.id.client_contact_layout);
        client_site_layout = findViewById(R.id.client_site_layout);


        input_layout_clientname.getEditText().addTextChangedListener(this);
        input_layout_client_email.getEditText().addTextChangedListener(this);
        input_layout_clientmobile.getEditText().addTextChangedListener(this);
        layout_gst_no.getEditText().addTextChangedListener(this);
        layout_tin_no.getEditText().addTextChangedListener(this);
        client_adr_layout.getEditText().addTextChangedListener(this);
        client_city_layout.getEditText().addTextChangedListener(this);
        client_country_layout.getEditText().addTextChangedListener(this);
        client_state_layout.getEditText().addTextChangedListener(this);
        client_zip_layout.getEditText().addTextChangedListener(this);
        layout_client_notes.getEditText().addTextChangedListener(this);
        client_lat_layout.getEditText().addTextChangedListener(this);
        client_lng_layout.getEditText().addTextChangedListener(this);
        client_contact_layout.getEditText().addTextChangedListener(this);
        client_site_layout.getEditText().addTextChangedListener(this);
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            onBackPressed();
            return true;
        }
        return super.onOptionsItemSelected(item);
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
    public void setNameError(String msg) {
        showErrorDialog(msg);
    }

    @Override
    public void setEmailError(String msg) {
        showErrorDialog(msg);
    }

    @Override
    public void setMobError(String msg) {
        showErrorDialog(msg);
    }

    @Override
    public void setAddError(String msg) {
        showErrorDialog(msg);
    }

    @Override
    public void setCountryError(String msg) {
        showErrorDialog(msg);
    }

    @Override
    public void setStateError(String msg) {
        showErrorDialog(msg);
    }

    @Override
    public void onAddNewClient(String cltId) {
        if (!cltId.equals("")) {
            Intent intent = new Intent();
            intent.putExtra("cltId", cltId);
            setResult(MainActivity.ClientADD, intent);
        }
        finish();
    }

    @Override
    public void setCountryList(List<Country> countryList) {
        AppUtility.autocompletetextviewPopUpWindow(client_cntry);
        FilterCountry countryAdapter = new FilterCountry(this, R.layout.custom_adapter_item_layout, (ArrayList<Country>) countryList);
        client_cntry.setAdapter(countryAdapter);
        client_cntry.setThreshold(1);
        client_cntry.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                cntryId = ((Country) adapterView.getItemAtPosition(i)).getId();
                addClient_pi.clientStatesList(cntryId);
            }
        });
        client_cntry.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                client_state.setText("");
                stateId = "";
                if (charSequence.length() >= 1) {
                    client_country_layout.setHintEnabled(true);
                } else if (charSequence.length() <= 0) {
                    client_country_layout.setHintEnabled(false);
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
    }

    @Override
    public void setStateList(List<States> stateList) {
        AppUtility.autocompletetextviewPopUpWindow(client_state);
        FilterStates stateAdapter = new FilterStates(this, R.layout.custom_adapter_item_layout, (ArrayList<States>) stateList);
        client_state.setAdapter(stateAdapter);

        client_state.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                stateId = (((States) adapterView.getItemAtPosition(i)).getId());
            }
        });
        client_state.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (charSequence.length() >= 1) {
                    client_state_layout.setHintEnabled(true);
                } else if (charSequence.length() <= 0) {
                    client_state_layout.setHintEnabled(false);
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.lat_lng_view_lay:
                getLocation();
                break;
            case R.id.linearLayout_reference:
                referenceDp.performClick();
                break;
            case R.id.linearLayout_account:
                if (accountArray.length > 0)
                    c_account.performClick();
                break;
            case R.id.indus_linearLayout:
                spinn_industry.performClick();
                break;
            case R.id.save_clnt: {
                String nm, cemail, cmob, cadd, gst_no, tno, note, adr, city, state, country, zip, mob;

                String cname, sitename;

                cname = c_contact_name.getText().toString().trim();
                sitename = c_site_name.getText().toString().trim();


                nm = c_name.getText().toString().trim();
                cemail = c_email.getText().toString().trim();
                cmob = c_mob.getText().toString().trim();
                cadd = c_add.getText().toString().trim();
                gst_no = c_gst.getText().toString().trim();
                tno = c_tin.getText().toString().trim();
                note = c_notes.getText().toString().trim();
                adr = c_add.getText().toString().trim();
                city = c_city.getText().toString().trim();
                zip = c_zip.getText().toString().trim();
                mob = c_mob.getText().toString().trim();
                state = client_state.getText().toString().trim();
                country = client_cntry.getText().toString().trim();
                String tempId = AppUtility.getTempIdFormat("Client");
                if (addClient_pi.addClientValidation(nm, cname, cemail, sitename, cadd, country, state, mob)) {// cmob,
                    AddClientModel addClientModel = new AddClientModel(tempId, App_preference.getSharedprefInstance().getLoginRes().getCompId(),
                            nm, accountId, cemail, mob, gst_no, tno, indusId, adr, cntryId, stateId, city, zip, note, "", sitename, "",
                            cname
                            , edt_lat.getText().toString(), edt_lng.getText().toString(), tv_spinner_account.getText().toString()
                            , reference + "");
                    addClient_pi.addClientCall(addClientModel);
                }
            }
            break;
        }
    }

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

        switch (adapterView.getId()) {
            case R.id.c_account:
                hint_tv_ac.setVisibility(View.VISIBLE);
                hint_tv_ac.setText(LanguageController.getInstance().getMobileMsgByKey(AppConstant.account_type));
                String aid = accountArray[i];
                tv_spinner_account.setText(aid);
                accountId = addClient_pi.accountItemSelect(aid);
                break;
//            case R.id.referenceDp:
//                hint_tv_reference.setVisibility(View.VISIBLE);
//                hint_tv_reference.setText(LanguageController.getInstance().getMobileMsgByKey(AppConstant.reference));
////                tv_spinner_reference.setText(AppConstant.clientReferenceList[i]);
//                reference = i + 1;
//                break;
        }
    }


    public void onNothingSelected(AdapterView<?> adapterView) {
    }


    @Override
    public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

    }

    @Override
    public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

        if (charSequence.length() >= 1) {
            if (charSequence.hashCode() == c_name.getText().hashCode())
                input_layout_clientname.setHintEnabled(true);
            if (charSequence.hashCode() == c_email.getText().hashCode())
                input_layout_client_email.setHintEnabled(true);
            if (charSequence.hashCode() == c_mob.getText().hashCode())
                input_layout_clientmobile.setHintEnabled(true);
            if (charSequence.hashCode() == c_gst.getText().hashCode())
                layout_gst_no.setHintEnabled(true);
            if (charSequence.hashCode() == c_tin.getText().hashCode())
                layout_tin_no.setHintEnabled(true);
            if (charSequence.hashCode() == c_add.getText().hashCode())
                client_adr_layout.setHintEnabled(true);
            if (charSequence.hashCode() == edt_lat.getText().hashCode())
                client_lat_layout.setHintEnabled(true);
            if (charSequence.hashCode() == edt_lng.getText().hashCode())
                client_lng_layout.setHintEnabled(true);
            if (charSequence.hashCode() == c_city.getText().hashCode())
                client_city_layout.setHintEnabled(true);
            if (charSequence.hashCode() == c_zip.getText().hashCode())
                client_zip_layout.setHintEnabled(true);
            if (charSequence.hashCode() == c_notes.getText().hashCode())
                layout_client_notes.setHintEnabled(true);
            if (charSequence.hashCode() == c_contact_name.getText().hashCode())
                client_contact_layout.setHintEnabled(true);
            if (charSequence.hashCode() == c_site_name.getText().hashCode())
                client_site_layout.setHintEnabled(true);
        } else if (charSequence.length() <= 0) {
            if (charSequence.hashCode() == c_name.getText().hashCode())
                input_layout_clientname.setHintEnabled(false);
            if (charSequence.hashCode() == c_email.getText().hashCode())
                input_layout_client_email.setHintEnabled(false);
            if (charSequence.hashCode() == c_mob.getText().hashCode())
                input_layout_clientmobile.setHintEnabled(false);
            if (charSequence.hashCode() == c_gst.getText().hashCode())
                layout_gst_no.setHintEnabled(false);
            if (charSequence.hashCode() == c_tin.getText().hashCode())
                layout_tin_no.setHintEnabled(false);
            if (charSequence.hashCode() == c_add.getText().hashCode())
                client_adr_layout.setHintEnabled(false);
            if (charSequence.hashCode() == edt_lat.getText().hashCode())
                client_lat_layout.setHintEnabled(false);
            if (charSequence.hashCode() == edt_lng.getText().hashCode())
                client_lng_layout.setHintEnabled(false);
            if (charSequence.hashCode() == c_city.getText().hashCode())
                client_city_layout.setHintEnabled(false);
            if (charSequence.hashCode() == c_zip.getText().hashCode())
                client_zip_layout.setHintEnabled(false);
            if (charSequence.hashCode() == c_notes.getText().hashCode())
                layout_client_notes.setHintEnabled(false);
            if (charSequence.hashCode() == c_contact_name.getText().hashCode())
                client_contact_layout.setHintEnabled(false);
            if (charSequence.hashCode() == c_site_name.getText().hashCode())
                client_site_layout.setHintEnabled(false);
        }

    }

    @Override
    public void afterTextChanged(Editable editable) {

    }
}
