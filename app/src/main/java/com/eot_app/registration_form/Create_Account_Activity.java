package com.eot_app.registration_form;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.eot_app.R;
import com.eot_app.registration_form.company_model_pkg.Company;
import com.eot_app.registration_form.company_model_pkg.VerifyEmail;
import com.eot_app.registration_form.create_account_mvp.Create_account_pc;
import com.eot_app.registration_form.create_account_mvp.Create_account_pi;
import com.eot_app.registration_form.create_account_mvp.Create_accountview;
import com.eot_app.utility.AppConstant;
import com.eot_app.utility.AppUtility;
import com.eot_app.utility.Server_location;
import com.eot_app.utility.language_support.LanguageController;
import com.eot_app.utility.util_interfaces.MySpinnerAdapter;
import com.google.android.material.textfield.TextInputLayout;

import java.util.List;
import java.util.concurrent.Callable;

public class Create_Account_Activity extends AppCompatActivity implements AdapterView.OnItemSelectedListener, View.OnClickListener, Create_accountview {
    TextInputLayout campany_txt, email_txt, pass_txt, text_server;
    private EditText company_name, editText_email, editText_password, editText_server;
    private Spinner server_location_spinner;
    private String apiCode = "";
    private Create_account_pi create_account_pc;
    private List<Server_location> server_locationList;
    private Button register_btn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create__account);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Create your account");
        initializelables();
        editText_server.setText(server_locationList.get(0).getText());
    }

    private void initializelables() {
        campany_txt = findViewById(R.id.campany_txt);
        email_txt = findViewById(R.id.email_txt);
        pass_txt = findViewById(R.id.pass_txt);
        text_server = findViewById(R.id.text_server);
        register_btn = findViewById(R.id.register_btn);

        company_name = findViewById(R.id.company_name);
        editText_email = findViewById(R.id.editText_email);
        editText_password = findViewById(R.id.editText_password);
        editText_server = findViewById(R.id.editText_server);

        server_location_spinner = findViewById(R.id.server_location_spinner);
        editText_server.setOnClickListener(this);
        register_btn.setOnClickListener(this);

        create_account_pc = new Create_account_pc(this);
        create_account_pc.getServerLocationList();

        setHintEnable();
    }

    private void setHintEnable() {
        company_name.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int start, int before, int count) {
                if (charSequence.length() >= 1) {
                    campany_txt.setHintEnabled(true);
                } else if (charSequence.length() <= 0) {
                    campany_txt.setHintEnabled(false);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });


        editText_email.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int start, int before, int count) {
                if (charSequence.length() >= 1) {
                    email_txt.setHintEnabled(true);
                } else if (charSequence.length() <= 0) {
                    email_txt.setHintEnabled(false);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        editText_password.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int start, int before, int count) {
                if (charSequence.length() >= 1) {
                    pass_txt.setHintEnabled(true);
                } else if (charSequence.length() <= 0) {
                    pass_txt.setHintEnabled(false);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }


    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        switch (parent.getId()) {
            case R.id.server_location_spinner:
                Server_location server_location = server_locationList.get(position);
                editText_server.setText(server_location.getText());

                break;
        }
    }


    @Override
    public void onNothingSelected(AdapterView<?> parent) {
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.editText_server:
                server_location_spinner.performClick();
                break;
            case R.id.register_btn:
                String serverLocatinID;
                serverLocatinID = editText_server.getText().toString();
                apiCode = create_account_pc.serverLocationId(serverLocatinID);
                if (create_account_pc.RequiredFields(company_name.getText().toString(), editText_email.getText().toString(), editText_password.getText().toString(), editText_server.getText().toString())) {
                    verifyEmail();
                }
                break;
        }
    }

    private void registerAccount() {
        Company company = new Company(company_name.getText().toString(), editText_email.getText().toString(),
                editText_password.getText().toString(), "0");
        create_account_pc.doRegistration(company, editText_server.getText().toString());
    }


    private void verifyEmail() {
        VerifyEmail verifyEmail = new VerifyEmail(editText_email.getText().toString());
        create_account_pc.verify_Email(verifyEmail);
    }

    @Override
    public void email_Alreday_Existes(Boolean success) {
        if (success) {
            registerAccount();
        }
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
    public void set_company_Error(String title) {
        showErrorDialog(title);
    }

    @Override
    public void set_serverLocation_Error(String title) {
        showErrorDialog(title);
    }

    @Override
    public void setEmailError(String msg) {
        showErrorDialog(msg);
    }

    @Override
    public void setPassError(String msg) {
        showErrorDialog(msg);
    }

    @Override
    public void errorMsg(String error) {
        showErrorDialog(error);
    }

    @Override
    public void verify_account_activity_open(String email, String message) {
        Intent intent = new Intent(com.eot_app.registration_form.Create_Account_Activity.this, Varify_Account_Activity.class);
        intent.putExtra("email", email);
        intent.putExtra("message", message);
        intent.putExtra("apiCode", apiCode);
        intent.putExtra("pass", editText_password.getText().toString());
        startActivity(intent);
    }

    @Override
    public void setServerLocationList(List<Server_location> serverLocationList) {
        this.server_locationList = serverLocationList;
        setOptionInuServerLocationDropDownn(serverLocationList);
    }

    private void setOptionInuServerLocationDropDownn(@NonNull List<Server_location> server_locations) {
        int index = 0;
        String[] languageList = new String[server_locations.size()];
        AppUtility.spinnerPopUpWindow(server_location_spinner);
        for (Server_location language_model : server_locations) {
            languageList[index] = language_model.getText();
            index++;
        }
        server_location_spinner.setAdapter(new MySpinnerAdapter(com.eot_app.registration_form.Create_Account_Activity.this, languageList));
        server_location_spinner.setOnItemSelectedListener(this);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            onBackPressed();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }


}