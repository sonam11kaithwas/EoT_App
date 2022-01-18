package com.eot_app.registration_form.company_model_pkg;

public class VerifyCompanyCode {

    private String email;
    private String code;
    private String pass;
    private String apiCode;

    public VerifyCompanyCode(String email, String code, String pass, String apiCode) {
        this.email = email;
        this.code = code;
        this.pass = pass;
        this.apiCode = apiCode;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getPass() {
        return pass;
    }

    public void setPass(String pass) {
        this.pass = pass;
    }

    public String getApiCode() {
        return apiCode;
    }

    public void setApiCode(String apiCode) {
        this.apiCode = apiCode;
    }


}
