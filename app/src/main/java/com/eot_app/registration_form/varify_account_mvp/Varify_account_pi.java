package com.eot_app.registration_form.varify_account_mvp;

import com.eot_app.registration_form.company_model_pkg.ResendVerificationCode;
import com.eot_app.registration_form.company_model_pkg.VerifyCompanyCode;

public interface Varify_account_pi {

    void resend_Verification_Code(ResendVerificationCode resendVerificationCode);

    void verify_Company_Code(VerifyCompanyCode verifyCompanyCode);


}
