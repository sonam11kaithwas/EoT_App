package com.eot_app.registration_form.create_account_mvp;

import com.eot_app.registration_form.company_model_pkg.Company;
import com.eot_app.registration_form.company_model_pkg.VerifyEmail;

public interface Create_account_pi {

    boolean RequiredFields(String companyname, String email, String pass, String server_location);

    void doRegistration(Company company, String server_location);

    void verify_Email(VerifyEmail verifyEmail);

    void getServerLocationList();

    String serverLocationId(String server_location);


}
