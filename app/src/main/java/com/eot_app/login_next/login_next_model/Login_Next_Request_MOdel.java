package com.eot_app.login_next.login_next_model;

import android.os.Build;

import com.eot_app.BuildConfig;

public class Login_Next_Request_MOdel {
    private final String devName;
    private final String appVersion;
    private final String osVersion;
    public String email;
    private String cc;
    private String pass;
    private String devType;
    private String devId;


    public Login_Next_Request_MOdel(String email, String pass, String cc, String devId) {//String devType,
        this.email = email;
        this.pass = pass;
        this.devType = "1";
        this.cc = cc;//Jit8385
        this.devId = devId;
        this.devName = Build.MANUFACTURER + " " + Build.BRAND + " " + Build.MODEL;
        this.appVersion = BuildConfig.VERSION_NAME;
        this.osVersion = Build.VERSION.RELEASE
                + " " + Build.VERSION_CODES.class.getFields()[Build.VERSION.SDK_INT].getName()
                + " " + Build.ID;
    }

    public Login_Next_Request_MOdel(String email, String pass) {//String devType,
        this.email = email;
        this.pass = pass;
        this.devType = "1";
        this.cc = "";//Jit8385
        this.devId = "";
        this.devName = Build.MANUFACTURER + " " + Build.BRAND + " " + Build.MODEL;
        this.appVersion = BuildConfig.VERSION_NAME;
        this.osVersion = Build.VERSION.RELEASE
                + " " + Build.VERSION_CODES.class.getFields()[Build.VERSION.SDK_INT].getName()
                + " " + Build.ID;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPass() {
        return pass;
    }

    public void setPass(String pass) {
        this.pass = pass;
    }

    public String getDevType() {
        return devType;
    }

    public void setDevType(String devType) {
        this.devType = devType;
    }

    public String getCc() {
        return cc;
    }

    public void setCc(String cc) {
        this.cc = cc;
    }

    public String getDevId() {
        return devId;
    }

    public void setDevId(String devId) {
        this.devId = devId;
    }
}
