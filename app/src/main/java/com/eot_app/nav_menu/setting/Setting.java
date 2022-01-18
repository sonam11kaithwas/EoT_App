package com.eot_app.nav_menu.setting;

import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;

import com.eot_app.BuildConfig;
import com.eot_app.R;
import com.eot_app.login_next.login_next_model.Right;
import com.eot_app.services.ApiClient;
import com.eot_app.services.Service_apis;
import com.eot_app.utility.AppConstant;
import com.eot_app.utility.AppUtility;
import com.eot_app.utility.App_preference;
import com.eot_app.utility.language_support.LanguageController;
import com.eot_app.utility.language_support.Language_Model;
import com.eot_app.utility.language_support.Language_Preference;
import com.eot_app.utility.settings.setting_db.ErrorLog;
import com.eot_app.utility.util_interfaces.Callback_AlertDialog;
import com.eot_app.utility.util_interfaces.MySpinnerAdapter;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.hypertrack.hyperlog.DeviceLogModel;
import com.hypertrack.hyperlog.HyperLog;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;

import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

public class Setting extends AppCompatActivity implements View.OnClickListener, AdapterView.OnItemSelectedListener {
    private final List<Language_Model> lan_list = App_preference.getSharedprefInstance().getLoginRes().getLanguageList();
    Spinner page_spinner, language_spinner;
    TextView title_language, text_page, tv_label_language, tv_label_view, site_name_show_txt;
    int languageSelectedPosition;
    List<ModuleCodeModel> allowedModules = new ArrayList<>();
    int defaultPageView;
    AppCompatButton button_report;
    View report_divider;
    LinearLayout ll_report_bug;
    private Context mContext;
    private TextView tv_report_msg;
    private Switch site_name_show_btn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);
        mContext = getApplicationContext();
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle(LanguageController.getInstance().getMobileMsgByKey(AppConstant.settings));


        initializelables();
        /**show and hide site name from list*/
        site_name_show_btn.setChecked(App_preference.getSharedprefInstance().getSiteNameShowInSetting());

        PageSet();
        setDefaultSelectedLng();
        setOptionInLanguageDropDown();

        site_name_show_btn.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                showHideSiteName();
            }
        });

        /***mahendra change's****/
//        exportLogInFile();
//        openLogDialog();

    }

    private void showHideSiteName() {
        if (site_name_show_btn.isChecked()) {
            App_preference.getSharedprefInstance().setSiteNameShowInSetting(true);
            Intent intent = new Intent();
            intent.putExtra("isShowSite", true);
            setResult(RESULT_OK, intent);
        } else {
            App_preference.getSharedprefInstance().setSiteNameShowInSetting(false);
            Intent intent = new Intent();
            intent.putExtra("isShowSite", false);
            setResult(RESULT_OK, intent);
        }
    }

    private void initializelables() {

        site_name_show_btn = findViewById(R.id.site_name_show_btn);
        site_name_show_txt = findViewById(R.id.site_name_show_txt);
        site_name_show_txt.setText(LanguageController.getInstance().getMobileMsgByKey(AppConstant.site_name_show_permisson));

        button_report = findViewById(R.id.button_report);
        tv_report_msg = findViewById(R.id.tv_report_msg);
        ll_report_bug = findViewById(R.id.ll_report_bug);
        report_divider = findViewById(R.id.report_divider);

        button_report.setText(LanguageController.getInstance().getMobileMsgByKey(AppConstant.setting_report));
        tv_report_msg.setText(LanguageController.getInstance().getMobileMsgByKey(AppConstant.report_msg));

        if (App_preference.getSharedprefInstance().getLoginRes().getIsEmailLogEnable().equals("1")) {
            ll_report_bug.setVisibility(View.VISIBLE);
            report_divider.setVisibility(View.VISIBLE);
        } else {
            ll_report_bug.setVisibility(View.GONE);
            report_divider.setVisibility(View.GONE);
        }


        tv_label_language = findViewById(R.id.tv_label_language);
        tv_label_language.setText(LanguageController.getInstance().getMobileMsgByKey(AppConstant.app_language));

        tv_label_view = findViewById(R.id.tv_label_view);
        tv_label_view.setText(LanguageController.getInstance().getMobileMsgByKey(AppConstant.default_view));


        title_language = findViewById(R.id.title_language);
        title_language.setText(LanguageController.getInstance().getMobileMsgByKey(AppConstant.title_language));

        page_spinner = findViewById(R.id.page_spinner);
        language_spinner = findViewById(R.id.language_spinner);


        text_page = findViewById(R.id.text_page);
        title_language.setOnClickListener(this);
        text_page.setOnClickListener(this);
        tv_label_language.setOnClickListener(this);

        button_report.setOnClickListener(this);
    }


    private void setDefaultSelectedLng() {
        if (lan_list == null) return;
        int index = 0;
        for (Language_Model language_model : lan_list) {
            if (Language_Preference.getSharedprefInstance().getlanguageFilename().equals(language_model.getFileName())) {
                String lan_lable = language_model.getNativeName();
                title_language.setText(lan_lable);
                languageSelectedPosition = index;
                Log.d("mahi", languageSelectedPosition + "");
                break;
            }

            index++;
        }

    }

    public void PageSet() {
        AppUtility.spinnerPopUpWindow(page_spinner);
        allowedModules.clear();

        ModuleCodeModel mcm = new ModuleCodeModel(ModuleCode.JOB,
                LanguageController.getInstance().getMobileMsgByKey(AppConstant.jobs));

        allowedModules.add(mcm);
        List<Right> rights = App_preference.getSharedprefInstance().getLoginRes().getRights();
        if (rights != null && rights.size() > 0) {
            Right permissions = rights.get(0);
            if (permissions != null) {

                if (permissions.getIsAppointmentVisible() == 0) {
                    mcm = new ModuleCodeModel(ModuleCode.CALENDAR,
                            LanguageController.getInstance().getMobileMsgByKey(AppConstant.title_appointments));
                    allowedModules.add(mcm);
                }

                if (permissions.getIsAuditVisible() == 0) {
                    mcm = new ModuleCodeModel(ModuleCode.AUDIT,
                            LanguageController.getInstance().getMobileMsgByKey(AppConstant.audit_nav));
                    allowedModules.add(mcm);
                }

                if (permissions.getIsClientVisible() == 0) {
                    mcm = new ModuleCodeModel(ModuleCode.CLIENTS,
                            LanguageController.getInstance().getMobileMsgByKey(AppConstant.clients));
                    allowedModules.add(mcm);
                }

                if (permissions.getIsQuoteVisible() == 0) {
                    mcm = new ModuleCodeModel(ModuleCode.QUOTES,
                            LanguageController.getInstance().getMobileMsgByKey(AppConstant.quotes));
                    allowedModules.add(mcm);
                }
                if (App_preference.getSharedprefInstance().getLoginRes().getIsOnetoOneChatEnable().equals("1")) {
                    mcm = new ModuleCodeModel(ModuleCode.CHATS,
                            LanguageController.getInstance().getMobileMsgByKey(AppConstant.side_menu_title_chats));
                    allowedModules.add(mcm);
                }

                if (permissions.getIsExpenseVisible() == 0) {
                    mcm = new ModuleCodeModel(ModuleCode.EXPENSES,
                            LanguageController.getInstance().getMobileMsgByKey(AppConstant.title_expence));
                    allowedModules.add(mcm);
                }

            }
        }

        int defaultPageView = Language_Preference.getSharedprefInstance().getDefaultPageView();
        boolean foundAllowedDefaultModule = false;
        String name = allowedModules.get(0).getText();

        String[] moduleNames = new String[allowedModules.size()];
        int index = 0;
        for (ModuleCodeModel codeModel : allowedModules) {
            if (defaultPageView == codeModel.getCode()) {
                foundAllowedDefaultModule = true;
                name = codeModel.getText();
            }
            moduleNames[index] = codeModel.getText();
            index++;
        }

        //check default found in allowed module if not than permission revoke by the admin and set to the default calendar or  job list page view
        if (!foundAllowedDefaultModule) {
            if (rights != null && rights.size() > 0 && rights.get(0).getIsAppointmentVisible() == 0) {
                defaultPageView = ModuleCode.CALENDAR;
                name = LanguageController.getInstance().getMobileMsgByKey(AppConstant.title_appointments);
            } else {
                defaultPageView = ModuleCodeModel.JOB;
                name = allowedModules.get(0).getText();
            }
            Language_Preference.getSharedprefInstance().setDefaultPageView(defaultPageView);
        }


        //default selected settings
        text_page.setText(name);

        page_spinner.setAdapter(new MySpinnerAdapter(Setting.this, moduleNames));
        page_spinner.setOnItemSelectedListener(this);

    }

    public void setOptionInLanguageDropDown() {
        int index = 0;
        String[] languageList = new String[lan_list.size()];
        AppUtility.spinnerPopUpWindow(language_spinner);
        for (Language_Model language_model : lan_list) {
            languageList[index] = language_model.getNativeName();
            index++;
        }
        language_spinner.setAdapter(new MySpinnerAdapter(Setting.this, languageList));
        language_spinner.setOnItemSelectedListener(this);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home)
            onBackPressed();
        return true;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {

            case R.id.text_page:
                page_spinner.performClick();
                break;

            case R.id.title_language:
                language_spinner.performClick();
                break;

            case R.id.button_report:
                /****Very important ***/
                sendDeviceLog();
                break;
        }
    }


    public void changeLng(final Language_Model language_settings) {
        AppUtility.alertDialog2(this, LanguageController.getInstance().getMobileMsgByKey(AppConstant.confirmation), LanguageController.getInstance().getMobileMsgByKey(AppConstant.changeLanguage)
                , LanguageController.getInstance().getMobileMsgByKey(AppConstant.ok), LanguageController.getInstance().getMobileMsgByKey(AppConstant.cancel), new Callback_AlertDialog() {
                    @Override
                    public void onPossitiveCall() {
                        if (AppUtility.isInternetConnected()) {
                            // Language_Preference.getSharedprefInstance().setLanguageFilename(language_model.getFileName());
                            AppUtility.progressBarShow(Setting.this);
                            setLanguage(language_settings);
                        } else {
                            AppUtility.alertDialog(Setting.this, LanguageController.getInstance().getMobileMsgByKey(AppConstant.dialog_alert), LanguageController.getInstance().getMobileMsgByKey(AppConstant.err_check_network), LanguageController.getInstance().getMobileMsgByKey(AppConstant.ok), "", new Callable<Boolean>() {
                                @Override
                                public Boolean call() throws Exception {
                                    return null;
                                }
                            });
                        }
                    }

                    @Override
                    public void onNegativeCall() {

                    }
                });

    }

    private void setLanguage(final Language_Model language_model) {
        String lan_url = language_model.getFilePath() + language_model.getFileName() + ".json";
        LanguageController.getInstance().downloadFile(lan_url, new Callable<Boolean>() {
            @Override
            public Boolean call() throws Exception {
                AppUtility.progressBarDissMiss();
                Intent intent = new Intent();
                intent.putExtra("isChange", true);
                setResult(RESULT_OK, intent);
                finish();
                return null;
            }
        });
    }


    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        switch (parent.getId()) {
            case R.id.language_spinner:
                Language_Model language_model = lan_list.get(position);
                title_language.setText(language_model.getNativeName());
                if (language_spinner.getSelectedItemPosition() > -1 && languageSelectedPosition != language_spinner.getSelectedItemPosition()) {
                    changeLng(lan_list.get(language_spinner.getSelectedItemPosition()));
                }
                break;
            case R.id.page_spinner:
                text_page.setText(allowedModules.get(position).getText());
                if (page_spinner.getSelectedItemPosition() > -1) {
                    ModuleCodeModel codeModel = allowedModules.get(page_spinner.getSelectedItemPosition());
                    if (defaultPageView != codeModel.getCode())
                        Language_Preference.getSharedprefInstance().setDefaultPageView(codeModel.getCode());
                    else Language_Preference.getSharedprefInstance().setDefaultPageView(0);
                    //  finish();
                } /*else finish();*/
                break;
        }
    }


    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }


    private void sendDeviceLog() {

        if (AppUtility.isInternetConnected()) {
            AppUtility.progressBarShow(this);
            StringBuilder builder = new StringBuilder();
            builder.append(getDeviceConfigurations() + "<br><br>");
            List<DeviceLogModel> deviceLogs = HyperLog.getDeviceLogs(false);
            if (deviceLogs != null) {
                for (DeviceLogModel s : deviceLogs) {
                    builder.append("<br>" + s.getDeviceLog());
                }

                ErrorLog errorLog = new ErrorLog();
                errorLog.setApiUrl("Send from setting");
                errorLog.setRequestParam("[]");
                errorLog.setResponse(builder.toString());
                errorLog.setVersion(Build.MODEL + ", " + Build.VERSION.RELEASE
                        + " " + Build.VERSION_CODES.class.getFields()[Build.VERSION.SDK_INT].getName() + ", " + BuildConfig.VERSION_NAME);
                errorLog.setTime(System.currentTimeMillis() + "");

         /*       Intent intent = new Intent(Intent.ACTION_SEND);
                intent.setType("text/html");
                intent.putExtra(Intent.EXTRA_EMAIL, "emailaddress@emailaddress.com");
                intent.putExtra(Intent.EXTRA_SUBJECT, "Device Log");
                intent.putExtra(Intent.EXTRA_TEXT, builder.toString());
                startActivity(Intent.createChooser(intent, "Send Email"));*/
                ApiClient.getservices().eotServiceCall(Service_apis.errorLogMail, AppUtility.getApiHeaders(), AppUtility.getJsonObject(new Gson().toJson(errorLog)))
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(new Observer<JsonObject>() {
                            @Override
                            public void onSubscribe(Disposable d) {
                            }

                            @Override
                            public void onNext(JsonObject jsonObject) {
                                if (jsonObject.get("success").getAsBoolean()) {
                                    Log.d("loggg", jsonObject.toString());
                                    // HyperLog.deleteLogs();
                                    Toast.makeText(getApplicationContext(), "Log sent", Toast.LENGTH_LONG).show();
                                }
                            }

                            @Override
                            public void onError(Throwable e) {
                                Log.d("loggg", e.toString());

                            }

                            @Override
                            public void onComplete() {
                                AppUtility.progressBarDissMiss();
                            }
                        });
            }
        } else {
            AppUtility.alertDialog(this, LanguageController.getInstance().
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


    /**
     * get logged in device configuration for
     * better log understanding and bug fix
     */
    private String getDeviceConfigurations() {
        StringBuilder builder = new StringBuilder();
        builder.append(App_preference.getSharedprefInstance().getLoginRes().getUsername() + " ID:"
                + App_preference.getSharedprefInstance().getLoginRes().getUsrId());
        builder.append("\nCompany Id:" +
                App_preference.getSharedprefInstance().getLoginRes().getCompId());
        builder.append("\nDevice Name:" + Build.MANUFACTURER + " " + Build.BRAND + " " + Build.MODEL);
        builder.append("\nDevice Version:" + Build.VERSION.RELEASE
                + " " + Build.VERSION_CODES.class.getFields()[Build.VERSION.SDK_INT].getName()
                + " " + Build.ID);
        builder.append("\nApp Version:" + BuildConfig.VERSION_NAME);

        builder.append("\nToken:" +
                App_preference.getSharedprefInstance().getLoginRes().getToken());

        builder.append("\nBASE url:" +
                App_preference.getSharedprefInstance().getBaseURL());

        return builder.toString();

    }

    /**/

    private void openLogDialog() {
        AlertDialog.Builder
                builder = new AlertDialog.Builder(Setting.this, android.R.style.Theme_Material_Light_NoActionBar_Fullscreen);
        builder.setTitle("Logs");

        View view = LayoutInflater.from(Setting.this)
                .inflate(R.layout.dialog_logs, null, false);
        TextView textView = view.findViewById(R.id.tv_log);

        builder.setView(view);

        List<DeviceLogModel> deviceLogs = HyperLog.getDeviceLogs(false);
        if (deviceLogs != null)
            for (DeviceLogModel s : deviceLogs) {
                textView.append("\n" + s.getDeviceLog());
            }
        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }


    protected void exportLogInFile() {
        button_report.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {

                File file = HyperLog.getDeviceLogsInFile(Setting.this, false);
/*
                Intent intent = new Intent(Intent.ACTION_SEND);
                intent.setType("text/html");
                intent.putExtra(Intent.EXTRA_EMAIL, "emailaddress@emailaddress.com");
                intent.putExtra(Intent.EXTRA_SUBJECT, "Device Log");*/
                List<DeviceLogModel> deviceLogs = HyperLog.getDeviceLogs(false);
                StringBuilder builder = new StringBuilder();
                if (deviceLogs != null)
                    for (DeviceLogModel s : deviceLogs) {
                        builder.append("\n" + s.getDeviceLog());
                    }
             /*   intent.putExtra(Intent.EXTRA_TEXT, builder.toString());

                startActivity(Intent.createChooser(intent, "Send Email"));*/
                HyperLog.deleteLogs();
                Toast.makeText(getApplicationContext(), "Log exported", Toast.LENGTH_SHORT).show();
                return true;
            }
        });

    }
}