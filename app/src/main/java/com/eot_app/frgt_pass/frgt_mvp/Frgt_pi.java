package com.eot_app.frgt_pass.frgt_mvp;

import com.eot_app.frgt_pass.frgt_pass_model.FrgtEmail;
import com.google.gson.JsonArray;

/**
 * Created by ubuntu on 17/7/18.
 */

public interface Frgt_pi {
    boolean frgtEmailCheck(String email);

    void emailApi(String email);

    void keyApiCall(FrgtEmail frgtkey);

    boolean passwordMatch(String pass1, String pass2);

    void passChangeApiCall(String pass);

    boolean keyCheck(String key);

    void compnaySelected(JsonArray data, String message);
}
