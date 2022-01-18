package com.eot_app.login_next.login_next_mvp;


public interface Login_Next_View {
    void setEmailEroor(String msg);

    void setPassEroor(String msg);

    void userLogin();

    void LoginSuccessFully();

    void setSaveLoginCrediantal(String email, String pass, boolean b);

    void upateForcefully();

    void updateNotForcefully();

    void userregiNotCompleted();
}
