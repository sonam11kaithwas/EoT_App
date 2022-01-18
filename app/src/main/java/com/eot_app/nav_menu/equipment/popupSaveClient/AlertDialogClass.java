package com.eot_app.nav_menu.equipment.popupSaveClient;

import android.content.Context;

import com.eot_app.nav_menu.equipment.popupSaveClient.equipmentClinetsave.Equipment_Client_PC;
import com.eot_app.nav_menu.equipment.popupSaveClient.equipmentClinetsave.Equipment_Client_PI;
import com.eot_app.nav_menu.equipment.popupSaveClient.equipmentClinetsave.Equipment_Client_view;
import com.eot_app.utility.AppConstant;
import com.eot_app.utility.AppUtility;
import com.eot_app.utility.language_support.LanguageController;

import java.util.concurrent.Callable;

public class AlertDialogClass {
    private final Context context;
    private final Equipment_Client_PI equipment_client_pi;
    private final String jobId;
    private final String audId;
    private final String appId;


    public AlertDialogClass(Context context, Equipment_Client_view equipment_client_view, String jobId, String audId, String appId) {
        this.context = context;
        this.equipment_client_pi = new Equipment_Client_PC(equipment_client_view);
        this.jobId = jobId;
        this.audId = audId;
        this.appId = appId;


    }

    public void alertDialog() {


        AppUtility.alertDialog(context, LanguageController.getInstance().getMobileMsgByKey(AppConstant.do_you_want_save_client), LanguageController.getInstance().getMobileMsgByKey(AppConstant.are_you_sure), LanguageController.getInstance().getMobileMsgByKey(AppConstant.yes), LanguageController.getInstance().getMobileMsgByKey(AppConstant.cancel), new Callable<Boolean>() {
            @Override
            public Boolean call() throws Exception {
                equipment_client_pi.addClientForSaveUse(jobId, audId, appId);
                return null;
            }
        });


    }


}
