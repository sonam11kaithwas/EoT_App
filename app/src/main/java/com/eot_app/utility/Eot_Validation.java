package com.eot_app.utility;

import com.eot_app.utility.language_support.LanguageController;

/**
 * Created by ubuntu on 9/6/18.
 */

public class Eot_Validation {

    static public String email_checker(String email) {
        if (email.isEmpty()) {
            return LanguageController.getInstance().getMobileMsgByKey(AppConstant.email_empty);
        } else if (!android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            return LanguageController.getInstance().getMobileMsgByKey(AppConstant.email_error);
        }

        return "";
    }


    static public String mob_checker(String mob) {   //mendatry mob
        if (mob.isEmpty()) {
            return LanguageController.getInstance().getMobileMsgByKey(AppConstant.c_e_mob);
        } else if (mob.length() < 8) {
            return LanguageController.getInstance().getMobileMsgByKey(AppConstant.err_mob_lent);
        }
        return "";
    }

    static public boolean mob_length(String no) {
        return no.length() <= 8;

    }
}
