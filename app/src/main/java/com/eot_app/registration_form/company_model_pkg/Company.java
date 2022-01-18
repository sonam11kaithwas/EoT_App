package com.eot_app.registration_form.company_model_pkg;

public class Company {
    private String name;
    private String email;
    private String pass;
    private String planId;

    public Company(String name, String email, String pass, String planId) {
        this.name = name;
        this.email = email;
        this.pass = pass;
        this.planId = planId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
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

    public String getPlanId() {
        return planId;
    }

    public void setPlanId(String planId) {
        this.planId = planId;
    }
}
