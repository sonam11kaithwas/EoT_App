package com.eot_app.login_next.login_next_model;

import java.util.ArrayList;
import java.util.List;

public class Login_Responce_Model {

    private List<String> cCode = null;
    private String apiurl;
    private String region;
    private String email;

    public String getRegion() {
        return region;
    }

    public void setRegion(String region) {
        this.region = region;
    }

    public String getApiurl() {
        return apiurl;
    }

    public void setApiurl(String apiurl) {
        this.apiurl = apiurl;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public List<String> getcCode() {
        return cCode;
    }

    public void setcCode(List<String> cCode) {
        this.cCode = cCode;
    }

    public void setcCode(ArrayList<String> cCode) {
        this.cCode = cCode;
    }
}

