package com.eot_app.nav_menu.audit.nav_scan;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;

import com.eot_app.nav_menu.appointment.Requestor;
import com.eot_app.nav_menu.appointment.list.AppointmentListViewModel;
import com.eot_app.services.Service_apis;
import com.eot_app.utility.AppConstant;
import com.google.gson.JsonObject;

import java.util.HashMap;

public class UploadBarcodeViewModel extends AppointmentListViewModel {
    MutableLiveData<Boolean> isUploadingBarcode = new MutableLiveData<>();
    MutableLiveData<String> uploadErrorMessage = new MutableLiveData<>();
    MutableLiveData<String> uploadMessage = new MutableLiveData<>();
    private String equipmentId, equipmentCode;

    public UploadBarcodeViewModel(@NonNull Application application) {
        super(application);
    }

    public MutableLiveData<String> getUploadErrorMessage() {
        return uploadErrorMessage;
    }

    public MutableLiveData<String> getUploadMessage() {
        return uploadMessage;
    }

    public MutableLiveData<Boolean> getIsUploadingBarcode() {
        return isUploadingBarcode;
    }

    public void uploadBarcode(String equipmentId, String barcodeNO) {
        isUploadingBarcode.setValue(true);

        this.equipmentCode = barcodeNO;
        this.equipmentId = equipmentId;

        HashMap<String, String> hashMap = new HashMap<>();
        hashMap.put("equId", equipmentId);
        hashMap.put("barCode", barcodeNO);
        Requestor requestor = new Requestor(this, reqCode4);
        requestor.sendRequestToServer(Service_apis.generateBarcodeWithGiveCode, hashMap);
    }

    @Override
    public void onSuccess(Object successObject, int requestCode) {
        super.onSuccess(successObject, requestCode);
        isUploadingBarcode.setValue(false);
        if (requestCode == reqCode4) {
            JsonObject jsonObject = (JsonObject) successObject;
            if (jsonObject != null) {
                if (jsonObject.get("success").getAsBoolean()) {
                    uploadMessage.setValue(jsonObject.get("message").getAsString());
                    uploadMessage.setValue(null);
                    refreshListFromServer();
                } else if (jsonObject.get("statusCode") != null && jsonObject.get("statusCode").getAsString().equals(AppConstant.SESSION_EXPIRE)) {

                } else if (!jsonObject.get("success").getAsBoolean()) {
                    uploadErrorMessage.setValue(jsonObject.get("message").getAsString());
                    uploadErrorMessage.setValue(null);
                }
            }

        }
    }

    @Override
    public void onError(Object errorObject, int requestCode) {
        super.onError(errorObject, requestCode);
        isUploadingBarcode.setValue(false);
    }
}
