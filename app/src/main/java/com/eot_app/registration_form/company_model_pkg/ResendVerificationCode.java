package com.eot_app.registration_form.company_model_pkg;

public class ResendVerificationCode {

    private String email;

    public ResendVerificationCode(String email) {
        this.email = email;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}

