package com.eot_app.frgt_pass.frgt_pass_model;

import com.google.gson.annotations.SerializedName;

/**
 * Created by ubuntu on 7/6/18.
 */

public class FrgtEmail {
    @SerializedName("username")
    String username;
    @SerializedName("key")
    String key;
    @SerializedName("usrId")
    String usrId;
    @SerializedName("pass")
    String pass;

    @SerializedName("cc")
    String cc;


    public FrgtEmail(String username, String key) {
        this.username = username;
        this.key = key;
    }

    public FrgtEmail(String usrId, String pass, String username) {
        this.username = username;
        this.pass = pass;
        this.usrId = usrId;
    }

    public String getUsername() {
        return username;
    }


    public void setUsername(String email) {
        this.username = email;
    }
}
