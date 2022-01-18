package com.eot_app.login_next;

import android.app.Dialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.eot_app.R;
import com.eot_app.frgt_pass.AsteriskPasswordTransformationMethod;
import com.eot_app.frgt_pass.frgt_mvp.Frgt_View;
import com.eot_app.frgt_pass.frgt_mvp.Frgt_pc;
import com.eot_app.frgt_pass.frgt_pass_model.FrgtEmail;
import com.eot_app.login_next.login_next_model.Login_Next_Request_MOdel;
import com.eot_app.login_next.login_next_mvp.Login_Next_Pc;
import com.eot_app.login_next.login_next_mvp.Login_Next_Pi;
import com.eot_app.login_next.login_next_mvp.Login_Next_View;
import com.eot_app.registration_form.Almost_done_Activity;
import com.eot_app.registration_form.Create_Account_Activity;
import com.eot_app.services.GetKillEvent_ToDestryNotication;
import com.eot_app.utility.AppConstant;
import com.eot_app.utility.AppUtility;
import com.eot_app.utility.App_preference;
import com.eot_app.utility.language_support.LanguageController;
import com.eot_app.utility.settings.firstSync.FirstSyncActivity;
import com.eot_app.utility.util_interfaces.Callback_AlertDialog;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.InstanceIdResult;

import java.util.HashMap;
import java.util.concurrent.Callable;

public class Login2Activity extends AppCompatActivity implements View.OnClickListener, Login_Next_View, Frgt_View {
    Button next_btn, submit_button;
    LinearLayout email_lay, pass_lay, login_layout;
    EditText editText_email, editText_pass;
    TextView email_id_show, forgot_pw, use_anot_account;
    Login_Next_Request_MOdel request_mOdel;
    String email, pass;
    Login_Next_Pi login_next_pi;
    CheckBox rememberme;
    int state = 0;
    Dialog dialog;
    Frgt_pc frgt_pc;
    TextView back_txt, alredy_key_txt, head_txt, recover_txt, textView3, app_fw, login_header;
    Button sbmit_btn;
    String email_frgt;
    EditText frgt_ed_email, frgt_key_edt;
    TextInputLayout textInputLayout, textInputLayout2;
    TextView registeration_btn, tv_privacy_policy;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login2);
        if (!isTaskRoot()) {//current activity not finish it's open/lunch root(Top/Current) Activity
            final Intent intent = getIntent();
            final String intentAction = intent.getAction();
            if (intent.hasCategory(Intent.CATEGORY_LAUNCHER) && intentAction != null && intentAction.equals(Intent.ACTION_MAIN)) {
                finish();
                return;
            }
        }

        initializelables();
        inializeviews();

        login_layout.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                AppUtility.hideSoftKeyboard(Login2Activity.this);
                return false;
            }
        });
    }

    @Override
    public void userregiNotCompleted() {
        Intent intent = new Intent(Login2Activity.this, Almost_done_Activity.class);
        intent.putExtra("email", email);
        intent.putExtra("pass", pass);
        startActivity(intent);
        this.finish();
    }

    private void initializelables() {
        registeration_btn = findViewById(R.id.registeration_btn);
        next_btn = findViewById(R.id.next_btn);
        email_lay = findViewById(R.id.email_lay);
        pass_lay = findViewById(R.id.pass_lay);
        editText_email = findViewById(R.id.editText_email);

        email_id_show = findViewById(R.id.email_id_show);
        editText_pass = findViewById(R.id.editText_pass);

        submit_button = findViewById(R.id.submit_button);

        rememberme = findViewById(R.id.rememberme);

        login_layout = findViewById(R.id.login_layout);
        forgot_pw = findViewById(R.id.forgot_pw);

        use_anot_account = findViewById(R.id.use_anot_account);

        app_fw = findViewById(R.id.app_fw);
        login_header = findViewById(R.id.login_header);
        tv_privacy_policy = findViewById(R.id.tv_privacy_policy);
    }


    private void inializeviews() {
        registeration_btn.setOnClickListener(this);
        tv_privacy_policy.setOnClickListener(this);
        next_btn.setOnClickListener(this);
        use_anot_account.setOnClickListener(this);
        forgot_pw.setOnClickListener(this);
        submit_button.setOnClickListener(this);
        login_next_pi = new Login_Next_Pc(this);
        login_next_pi.getsaveLoginCrediantal();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.next_btn:

                next_btn.setEnabled(false);
                onNextButtonClick();
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        next_btn.setEnabled(true);
                    }
                }, 500);


                break;
            case R.id.submit_button:
                if (App_preference.getSharedprefInstance().getFirebaseDeviceToken().equals("")) {
                    FirebaseInstanceId.getInstance().getInstanceId().addOnSuccessListener(new OnSuccessListener<InstanceIdResult>() {
                        @Override
                        public void onSuccess(InstanceIdResult instanceIdResult) {
                            App_preference.getSharedprefInstance().setFirebaseDeviceToken(instanceIdResult.getToken());
                            pass = editText_pass.getText().toString();
                            if (login_next_pi.checkPassValidation(pass)) {
                                request_mOdel = new Login_Next_Request_MOdel(email, pass, App_preference.getSharedprefInstance().getCompCode(), instanceIdResult.getToken());
                                Log.e("MyDeviceToken", "" + instanceIdResult.getToken());
                                login_next_pi.UserLoginServiceCall(request_mOdel, rememberme.isChecked());
                            }
                        }
                    });
                } else {
                    pass = editText_pass.getText().toString();
                    if (login_next_pi.checkPassValidation(pass)) {
                        request_mOdel = new Login_Next_Request_MOdel(email, pass, App_preference.getSharedprefInstance().getCompCode(), App_preference.getSharedprefInstance().getFirebaseDeviceToken());
                        Log.e("MyDeviceToken", "" + App_preference.getSharedprefInstance().getFirebaseDeviceToken());
                        login_next_pi.UserLoginServiceCall(request_mOdel, rememberme.isChecked());
                    }

                }
                break;
            case R.id.use_anot_account://redirect on username screen
                email_lay.setVisibility(View.VISIBLE);
                pass_lay.setVisibility(View.GONE);
                editText_email.setText("");
                editText_pass.setText("");
                rememberme.setChecked(false);
                break;
            case R.id.forgot_pw://Forgot password
                state = 0;
                forgot_pw.setEnabled(false);
                frgtPass();
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        forgot_pw.setEnabled(true);
                    }
                }, 500);


                break;
            case R.id.back_txt:
                dialog.dismiss();
                break;
            case R.id.alredy_key_txt:
                CharSequence str = alredy_key_txt.getText();
                if (str.equals(AppConstant.fr_alr_key)) {
                    state = 1;
                    textInputLayout.setVisibility(View.VISIBLE);
                    textInputLayout2.setVisibility(View.VISIBLE);
                    head_txt.setText(R.string.pass_head_desc);
                    textInputLayout2.setHint("Key");
                    sbmit_btn.setText(AppConstant.fr_reset_key);

                    onClick(sbmit_btn);
                } else if (str.equals(AppConstant.fr_dont_key)) {
                    state = 0;
                    textInputLayout2.setVisibility(View.GONE);
                    textInputLayout.setVisibility(View.VISIBLE);
                    sbmit_btn.setText(R.string.submit);
                    head_txt.setText(R.string.pass_head_desc);
                    frgt_ed_email.setText("");
                    alredy_key_txt.setText(AppConstant.fr_alr_key);
                    onClick(sbmit_btn);
                }
                break;
            case R.id.registeration_btn:
                Intent intent = new Intent(Login2Activity.this, Create_Account_Activity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                startActivity(intent);
                break;

            case R.id.tv_privacy_policy:
                Intent webView = new Intent();
                webView.setAction(Intent.ACTION_VIEW);
                webView.setData(Uri.parse("https://www.eyeontask.com/policy.html"));
                startActivity(webView);
                break;
        }
    }


    public void onNextButtonClick() {
        email = editText_email.getText().toString();
        if (login_next_pi.checkEmailValidation(email)) {
            HashMap<String, String> hashMap = new HashMap<>();
            hashMap.put("email", email);
            login_next_pi.syncData(hashMap);
        }
    }

    @Override
    public void userLogin() {
        email_lay.setVisibility(View.GONE);
        pass_lay.setVisibility(View.VISIBLE);
        email_id_show.setText(App_preference.getSharedprefInstance().getEmailRespone().getEmail());
    }


    @Override
    public void setEmailEroor(String msg) {
        showErrorMsg(msg);
    }

    private void showErrorMsg(String msg) {
        AppUtility.error_Alert_Dialog(this, msg, LanguageController.getInstance().getMobileMsgByKey(AppConstant.ok)
                , new Callable<Boolean>() {
                    @Override
                    public Boolean call() throws Exception {
                        return null;
                    }
                });
    }

    @Override
    public void setPassEroor(String msg) {
        showErrorMsg(msg);
    }

    @Override
    public void LoginSuccessFully() {
        startService(new Intent(this, GetKillEvent_ToDestryNotication.class));
        startActivity(new Intent(Login2Activity.this, FirstSyncActivity.class).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
        finish();
    }

    @Override
    public void setSaveLoginCrediantal(String email, String pass, boolean b) {
        editText_email.setText(email);
        editText_pass.setText(pass);
        email_id_show.setText(email);
        this.email = email;
        if (b) {
            rememberme.setChecked(true);
            pass_lay.setVisibility(View.VISIBLE);
            email_lay.setVisibility(View.GONE);
        } else {
            rememberme.setChecked(false);
        }
    }

    @Override
    public void upateForcefully() {
        AppUtility.alertDialog(this, "Update!", AppConstant.updateAppMsg, AppConstant.update, "", new Callable<Boolean>() {
            @Override
            public Boolean call() throws Exception {
                final String appPackageName = getPackageName(); // getPackageName() from Context or Activity object
                try {
                    startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=" + appPackageName)));
                } catch (android.content.ActivityNotFoundException anfe) {
                    startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=" + appPackageName)));
                }
                return null;
            }
        });
    }

    //"Update!"
    @Override
    public void updateNotForcefully() {
        AppUtility.alertDialog2(this, "Update!", AppConstant.updateAppMsg, AppConstant.update, AppConstant.cancel, new Callback_AlertDialog() {
            @Override
            public void onPossitiveCall() {
                final String appPackageName = getPackageName(); // getPackageName() from Context or Activity object
                try {
                    startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=" + appPackageName)));
                } catch (android.content.ActivityNotFoundException anfe) {
                    startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=" + appPackageName)));
                }
            }

            @Override
            public void onNegativeCall() {
                LoginSuccessFully();
            }
        });
    }


    @Override
    public void setFrgtEmail(String msg) {
        showErrorMsg(msg);
    }


    @Override
    public void setKey() {  //call after get reset key
        state++;
        textInputLayout.setVisibility(View.GONE);
        textInputLayout2.setVisibility(View.VISIBLE);
        head_txt.setText(AppConstant.fr_set_key);
        alredy_key_txt.setText(R.string.dontkey);
        alredy_key_txt.setOnClickListener(this);
        sbmit_btn.setText(AppConstant.fr_reset_key);
    }

    @Override
    public void newPassViewSet() { //password conformation
        frgt_ed_email.requestFocus();
        state++;
        head_txt.setText("");
        head_txt.setVisibility(View.GONE);
        textInputLayout.setVisibility(View.VISIBLE);
        textInputLayout2.setVisibility(View.VISIBLE);
        textInputLayout.setHint(AppConstant.fr_pass);
        textInputLayout2.setHint(AppConstant.fr_cnfrm);
        frgt_ed_email.setText("");
        frgt_key_edt.setText("");
        back_txt.setGravity(Gravity.CENTER_VERTICAL);
        frgt_ed_email.setTransformationMethod(new AsteriskPasswordTransformationMethod());//text/password Show in Asterisks form
        frgt_key_edt.setTransformationMethod(new AsteriskPasswordTransformationMethod());
        alredy_key_txt.setVisibility(View.GONE);
    }

    @Override
    public void passwordError(String msg) {
        showErrorMsg(msg);

    }

    @Override
    public void frgtDialogFinish() {
        dialog.dismiss();
    }


    public void frgtPass() {
        dialog = new Dialog(this);
        dialog.setContentView(R.layout.frgtpass_layout);
        dialog.setCancelable(false);
        dialog.show();
        dialogView();
        frgt_pc = new Frgt_pc(Login2Activity.this);
        alredy_key_txt.setOnClickListener(this);
        back_txt.setOnClickListener(this);
        sbmit_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                email_frgt = frgt_ed_email.getText().toString();
                if (state == 0) {
                    if (frgt_pc.frgtEmailCheck(email_frgt)) {
                        frgt_pc.emailApi(email_frgt);
                    }
                } else if (state == 1) {
                    email_frgt = frgt_ed_email.getText().toString();
                    String key = frgt_key_edt.getText().toString();
                    FrgtEmail frgtKey = new FrgtEmail(email_frgt, key);
                    if (frgt_pc.frgtEmailCheck(email_frgt))
                        if (frgt_pc.keyCheck(key)) {
                            frgt_pc.keyApiCall(frgtKey);
                        }
                } else if (state == 2) {
                    back_txt.setGravity(Gravity.CENTER);
                    String pass = frgt_ed_email.getText().toString();
                    String cn_pass = frgt_key_edt.getText().toString();
                    if (frgt_pc.passwordMatch(pass, cn_pass)) {
                        frgt_pc.passChangeApiCall(pass);
                    }
                }
            }
        });
    }


    void dialogView() {

        recover_txt = dialog.findViewById(R.id.recover_txt);
        recover_txt.setText(R.string.forgetpassword);

        textView3 = dialog.findViewById(R.id.textView3);
        textView3.setText(R.string.pass_recov);

        head_txt = dialog.findViewById(R.id.head_txt);
        head_txt.setText(R.string.pass_head_desc);

        frgt_ed_email = dialog.findViewById(R.id.frgt_email_edt);
        frgt_ed_email.setHint(R.string.login_email);

        frgt_key_edt = dialog.findViewById(R.id.frgt_key_edt);
        frgt_key_edt.setHint(R.string.Passwordresetkey);

        sbmit_btn = dialog.findViewById(R.id.submit);
        sbmit_btn.setText(R.string.submit);


        back_txt = dialog.findViewById(R.id.back_txt);
        back_txt.setText(R.string.back_to_login);

        alredy_key_txt = dialog.findViewById(R.id.alredy_key_txt);
        alredy_key_txt.setText(AppConstant.fr_alr_key);

        textInputLayout = dialog.findViewById(R.id.textInputLayout);
        textInputLayout2 = dialog.findViewById(R.id.textInputLayout2);

        textInputLayout2.setVisibility(View.GONE);

        textInputLayout.getEditText().addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (charSequence.length() >= 1) {
                    if (charSequence.hashCode() == frgt_ed_email.getText().hashCode()) {
                        textInputLayout.setHintEnabled(true);
                    }
                }
                if (charSequence.length() <= 0) {
                    if (charSequence.hashCode() == frgt_ed_email.getText().hashCode()) {
                        textInputLayout.setHintEnabled(false);
                    }
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });


        textInputLayout2.getEditText().addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (charSequence.length() >= 1) {
                    if (charSequence.hashCode() == frgt_key_edt.getText().hashCode()) {
                        textInputLayout2.setHintEnabled(true);
                    }
                }
                if (charSequence.length() <= 0) {

                    if (charSequence.hashCode() == frgt_key_edt.getText().hashCode()) {
                        textInputLayout2.setHintEnabled(false);
                    }

                }

            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
    }

    @Override
    protected void onResume() {
        Log.e("onResume", "");
        AppConstant.location_checker_enable = true; // check for location update.
        super.onResume();
    }

    @Override
    protected void onPause() {
        Log.e("OnPause", "");
        super.onPause();
    }


}
