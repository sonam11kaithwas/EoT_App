package com.eot_app.registration_form;

import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.eot_app.R;
import com.eot_app.login_next.Login2Activity;
import com.eot_app.registration_form.company_model_pkg.ResendVerificationCode;
import com.eot_app.registration_form.company_model_pkg.VerifyCompanyCode;
import com.eot_app.registration_form.varify_account_mvp.Varify_account_pc;
import com.eot_app.registration_form.varify_account_mvp.Varify_account_pi;
import com.eot_app.registration_form.varify_account_mvp.Varify_accountview;
import com.eot_app.utility.AppConstant;
import com.eot_app.utility.AppUtility;
import com.eot_app.utility.language_support.LanguageController;
import com.google.android.material.textfield.TextInputLayout;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.concurrent.Callable;

public class Varify_Account_Activity extends AppCompatActivity implements View.OnClickListener, Varify_accountview {
    private TextView get_txt, resend_timer, resend_btn;
    private String email, pass, message, apiCode;
    private TextInputLayout verify_code;
    private Varify_account_pi varify_account_pc;
    private Button verify_btn;
    private EditText confirmation_code;
    private LinearLayout resend_linear;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_varify__account);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Welcome to EyeOnTask.com");
        Intent intent = getIntent();
        email = intent.getStringExtra("email");
        pass = intent.getStringExtra("pass");
        message = intent.getStringExtra("message");
        apiCode = intent.getStringExtra("apiCode");

        initializelables();
        setGet_txt();
        timmer();
        setHintEnable();
    }

    private void initializelables() {
        resend_linear = findViewById(R.id.resnd_linaer);
        confirmation_code = findViewById(R.id.confirmation_code);
        verify_btn = findViewById(R.id.verify_btn);
        resend_btn = findViewById(R.id.resend_btn);
        resend_timer = findViewById(R.id.resend_timer);
        get_txt = findViewById(R.id.get_txt);
        verify_code = findViewById(R.id.verify_code);

        resend_btn.setOnClickListener(this);
        verify_btn.setOnClickListener(this);

        varify_account_pc = new Varify_account_pc(this);

    }

    private void setHintEnable() {
        confirmation_code.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int start, int before, int count) {
                if (charSequence.length() >= 1) {
                    verify_code.setHintEnabled(true);
                } else if (charSequence.length() <= 0) {
                    verify_code.setHintEnabled(false);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }

    private void setGet_txt() {
        if (message.equals("success_reg_code_sent")) {

            get_txt.setText("You are successfully registered and verification code has been sent to " + email +
                    " .if you didn't get the email then please contact to EyeOnTask support team.");

        } else if (message.equals("comp_already_exist")) {
            get_txt.setText("This company already exists.");

        } else if (message.equals("already_reg_mail_sent")) {
            get_txt.setText("You are successfully registered and verification code has been sent to " + email +
                    " .if you didn't get the email then please contact to EyeOnTask support team.");

        }
    }


    private void timmer() {
        new CountDownTimer(30000, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                NumberFormat f = new DecimalFormat("00");

                long min = (millisUntilFinished / 60000) % 60;
                long sec = (millisUntilFinished / 1000) % 60;
                resend_timer.setText(f.format(min) + ":" + f.format(sec));
                resend_btn.setEnabled(false);
                resend_linear.setBackgroundResource(R.drawable.resend_btn);
                resend_btn.setTextColor(getResources().getColor(R.color.black));

            }

            public void onFinish() {
                resend_timer.setText("00:30");
                resend_btn.setEnabled(true);
                resend_linear.setBackgroundResource(0);
                resend_btn.setTextColor(getResources().getColor(R.color.colorPrimaryDark));
            }
        }.start();
    }


    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.resend_btn:
                timmer();
                resendVerificationCode();
                break;
            case R.id.verify_btn:
                verifyCompanyCode(email);
                break;
        }
    }

    private void resendVerificationCode() {
        ResendVerificationCode resendVerificationCode = new ResendVerificationCode(email);
        varify_account_pc.resend_Verification_Code(resendVerificationCode);
    }

    private void verifyCompanyCode(String email) {
        VerifyCompanyCode verifyCompanyCode = new VerifyCompanyCode(email, confirmation_code.getText().toString(), pass, apiCode);
        varify_account_pc.verify_Company_Code(verifyCompanyCode);
    }

    @Override
    public void errorMsg(String error) {
        showErrorDialog(error);
    }

    @Override
    public void varify_code(Boolean success) {
        if (success) {
            almostDone_Activity_open();
        } else {
            get_txt.setBackgroundResource(R.drawable.shape_verify_pinck);
            get_txt.setText("Your verification code is wrong,please re-enter.");
        }
    }


    private void almostDone_Activity_open() {
        Intent intent = new Intent(Varify_Account_Activity.this, Almost_done_Activity.class);
        intent.putExtra("email", email);
        intent.putExtra("pass", pass);
        startActivity(intent);
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
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {

            Intent intent = new Intent(Varify_Account_Activity.this, Login2Activity.class);
            startActivity(intent);
            this.finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        Toast.makeText(getApplicationContext(), "Please Enter Varification Code", Toast.LENGTH_SHORT).show();
      /*  Intent intent = new Intent(Varify_Account_Activity.this, Login2Activity.class);
        startActivity(intent);*/
    }

}