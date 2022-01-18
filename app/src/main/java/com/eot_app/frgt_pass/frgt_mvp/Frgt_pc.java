package com.eot_app.frgt_pass.frgt_mvp;

import android.app.Dialog;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.eot_app.R;
import com.eot_app.frgt_pass.frgt_pass_model.FrgtEmail;
import com.eot_app.services.ApiClient;
import com.eot_app.services.Service_apis;
import com.eot_app.utility.AppConstant;
import com.eot_app.utility.AppUtility;
import com.eot_app.utility.EotApp;
import com.eot_app.utility.language_support.LanguageController;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;

import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by ubuntu on 17/7/18.
 */

public class Frgt_pc implements Frgt_pi {
    Frgt_View frgt_view;
    String userId, cc = "";
    ArrayList<String> comList;
    String email;

    public Frgt_pc(Frgt_View frgt_view) {
        this.frgt_view = frgt_view;
    }

    @Override
    public boolean frgtEmailCheck(String email) {
        if ((email.length() < 3)) {
            frgt_view.setFrgtEmail(LanguageController.getInstance().getMobileMsgByKey(LanguageController.getInstance().getMobileMsgByKey(AppConstant.inCorrect_user_name)));
            return false;
        }
        return true;
    }

    @Override
    public boolean keyCheck(String key) {//error msg key filed empty
        if (key.equals("")) {
            frgt_view.passwordError(LanguageController.getInstance().getMobileMsgByKey(AppConstant.key));
            return false;
        } else return true;
    }

    @Override
    public boolean passwordMatch(String pass1, String pass2) {
        if (pass1.equals("")) {
            frgt_view.setFrgtEmail(LanguageController.getInstance().getMobileMsgByKey(AppConstant.password_error));
            return false;
        } else if (!pass1.equals(pass2)) {
            frgt_view.passwordError(LanguageController.getInstance().getMobileMsgByKey(AppConstant.frgt_pass_match));
            return false;
        }
        return true;
    }

    @Override
    public void emailApi(String email) {//UserName/Email found or not
        this.email = email;
        HashMap<String, String> hm1 = new HashMap<>();
        hm1.put("username", email);
        hm1.put("cc", cc);
        String data = new Gson().toJson(hm1);

        if (AppUtility.isInternetConnected()) {
            AppUtility.progressBarShow((Context) frgt_view);
            ApiClient.getservices().service_Call_Without_Token(Service_apis.forgotPassword, AppUtility.getUserTimeZone(), AppUtility.getJsonObject(data))
                    .subscribeOn(Schedulers.io()).
                    observeOn(AndroidSchedulers.mainThread()).subscribe(new Observer<JsonObject>() {
                @Override
                public void onSubscribe(Disposable d) {

                }

                @Override
                public void onNext(JsonObject jsonObject) {
                    Log.e("", jsonObject.toString());
                    Toast.makeText(((Context) frgt_view), LanguageController.getInstance().getServerMsgByKey(jsonObject.get("message").getAsString()), Toast.LENGTH_SHORT).show();
                    Boolean rspn = Boolean.valueOf(String.valueOf(jsonObject.get("success")));
                    if (rspn) {
                        frgt_view.setKey();
                    } else {
                        compnaySelected((JsonArray) jsonObject.get("data"), String.valueOf(LanguageController.getInstance().getServerMsgByKey(jsonObject.get("message").getAsString())));
                    }
                }

                @Override
                public void onError(Throwable e) {
                    Log.e("error", e.getMessage());
                    AppUtility.progressBarDissMiss();
                }

                @Override
                public void onComplete() {
                    AppUtility.progressBarDissMiss();
                }
            });

        } else {
            EotApp.getAppinstance().showToastmsg(LanguageController.getInstance().getMobileMsgByKey(AppConstant.err_check_network));
        }
    }

    @Override
    public void keyApiCall(final FrgtEmail frgtkey) {//key match
        if (AppUtility.isInternetConnected()) {
            AppUtility.progressBarShow((Context) frgt_view);
            ApiClient.getservices().service_Call_Without_Token(Service_apis.forgotPasswordKey, AppUtility.getUserTimeZone(), AppUtility.jsonToStingConvrt(frgtkey)).subscribeOn(Schedulers.io()).
                    observeOn(AndroidSchedulers.mainThread()).subscribe(new Observer<JsonObject>() {
                @Override
                public void onSubscribe(Disposable d) {

                }

                @Override
                public void onNext(JsonObject jsonObject) {
                    Log.e("", jsonObject.toString());
                    boolean rspn = Boolean.valueOf(String.valueOf(jsonObject.get("success")));
                    Toast.makeText(EotApp.getAppinstance(), LanguageController.getInstance().getServerMsgByKey(jsonObject.get("message").getAsString()), Toast.LENGTH_SHORT).show();
                    if (rspn) {
                        userId = String.valueOf(jsonObject.get("usrId").getAsInt());
                        frgt_view.newPassViewSet();
                    }
                }

                @Override
                public void onError(Throwable e) {
                    Log.e("error", e.getMessage());
                    AppUtility.progressBarDissMiss();
                }

                @Override
                public void onComplete() {
                    AppUtility.progressBarDissMiss();
                }
            });
        } else {
            EotApp.getAppinstance().showToastmsg(LanguageController.getInstance().getMobileMsgByKey(AppConstant.err_check_network));
        }
    }


    @Override
    public void passChangeApiCall(String pass) {//craete new password
        FrgtEmail frgtEmail = new FrgtEmail(userId, pass, "");
        if (AppUtility.isInternetConnected()) {
            AppUtility.progressBarShow((Context) frgt_view);
            ApiClient.getservices().service_Call_Without_Token(Service_apis.forgotPasswordReset, AppUtility.getUserTimeZone(),
                    AppUtility.jsonToStingConvrt(frgtEmail)).subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread()).subscribe(new Observer<JsonObject>() {
                @Override
                public void onSubscribe(Disposable d) {

                }

                @Override
                public void onNext(JsonObject jsonObject) {
                    boolean rspn = Boolean.valueOf(String.valueOf(jsonObject.get("success")));
                    if (rspn) {
                        Toast.makeText(EotApp.getAppinstance(), LanguageController.getInstance().getServerMsgByKey(jsonObject.get("message").getAsString()), Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onError(Throwable e) {
                    Log.e("error", e.getMessage());
                }

                @Override
                public void onComplete() {
                    AppUtility.progressBarDissMiss();
                    frgt_view.frgtDialogFinish();
                }
            });
        } else {
            EotApp.getAppinstance().showToastmsg(LanguageController.getInstance().getMobileMsgByKey(AppConstant.err_check_network));
        }
    }


    // user register in multiple companies than show company list dialog
    @Override
    public void compnaySelected(JsonArray data, String message) {

        String convert = new Gson().toJson(data);
        Type listType = new TypeToken<ArrayList<HashMap>>() {
        }.getType();
        ArrayList<HashMap> dataList = new Gson().fromJson(convert, listType);
        if (data.size() > 0) {
            comList = new ArrayList<>();
            for (HashMap<String, String> item : dataList) {
                comList.add(item.get("comp_code"));
            }
        }
        final Dialog dialog = new Dialog((Context) frgt_view);
        dialog.setCancelable(false);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.radiobutton_dialog);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        dialog.setCancelable(false);
        RadioGroup rg = dialog.findViewById(R.id.radio_group);
        TextView txt_close = dialog.findViewById(R.id.btnClose);
        TextView txt_continue = dialog.findViewById(R.id.btn_continue);
        TextView msg = dialog.findViewById(R.id.msg);
        msg.setText(message);
        dialog.show();
        txt_close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                cc = "";
                dialog.dismiss();
            }
        });
        txt_continue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                emailApi(email);
                dialog.dismiss();
            }
        });

        if (comList != null) {
            for (int i = 0; i < comList.size(); i++) {
                RadioButton rb = new RadioButton((Context) frgt_view);
                String s = comList.get(i);
                rb.setTextAppearance((Context) frgt_view, R.style.style_thrid);
                rb.setText(s);
                rg.addView(rb);
            }
        } else {
            dialog.dismiss();
        }
        rg.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                int id = radioGroup.getCheckedRadioButtonId();
                RadioButton checked_btn = radioGroup.findViewById(id);
                cc = checked_btn.getText().toString();
            }
        });

    }
}
