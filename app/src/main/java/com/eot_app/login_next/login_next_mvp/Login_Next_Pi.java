package com.eot_app.login_next.login_next_mvp;

import com.eot_app.login_next.login_next_model.Login_Next_Request_MOdel;

import java.util.HashMap;
import java.util.List;

public interface Login_Next_Pi {
    boolean checkEmailValidation(String email);

    boolean checkPassValidation(String pass);

    void get_Url(HashMap<String, String> login_next_request_mOdel);

    void UserLoginServiceCall(Login_Next_Request_MOdel login_next_request_mOdel, boolean checked);//, boolean checked

    void getsaveLoginCrediantal();

    void showRadioButtonDialog(final List<String> list, String message);

    void syncData(HashMap<String, String> hashMap);
}
