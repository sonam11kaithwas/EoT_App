package com.eot_app.registration_form.create_account_mvp;

import com.eot_app.utility.Server_location;

import java.util.List;

public interface Create_accountview {

    void set_company_Error(String title);

    void set_serverLocation_Error(String title);

    void setEmailError(String msg);

    void setPassError(String msg);

    void errorMsg(String error);

    void verify_account_activity_open(String email, String message);

    void setServerLocationList(List<Server_location> serverLocationList);

    void email_Alreday_Existes(Boolean success);
}
