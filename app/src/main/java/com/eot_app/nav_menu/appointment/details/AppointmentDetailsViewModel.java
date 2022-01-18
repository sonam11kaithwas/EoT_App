package com.eot_app.nav_menu.appointment.details;

import android.app.Application;
import android.text.TextUtils;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;

import com.eot_app.nav_menu.appointment.Keepar;
import com.eot_app.nav_menu.appointment.Requestor;
import com.eot_app.nav_menu.appointment.ServerResponse;
import com.eot_app.nav_menu.appointment.addupdate.model.AppointmentAddReq;
import com.eot_app.nav_menu.appointment.addupdate.model.AppointmentUpdateReq;
import com.eot_app.nav_menu.appointment.dbappointment.Appointment;
import com.eot_app.nav_menu.appointment.details.documents.DocumentExportReq;
import com.eot_app.services.Service_apis;
import com.eot_app.utility.AppConstant;
import com.eot_app.utility.AppUtility;
import com.eot_app.utility.EotApp;
import com.eot_app.utility.db.AppDataBase;
import com.eot_app.utility.db.OfflineDataController;
import com.eot_app.utility.settings.setting_db.Offlinetable;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;

import java.io.File;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;

public class AppointmentDetailsViewModel extends AndroidViewModel implements ServerResponse {
    MutableLiveData<List<AppointmentAttachment>> liveAttachments = new MutableLiveData<>();
    MutableLiveData<Boolean> isUploading = new MutableLiveData<>();
    MutableLiveData<String> pdfPath = new MutableLiveData<>();
    Appointment liveAppointmentModel;
    MutableLiveData<AppointmentAttachment> localAttachement = new MutableLiveData<>();

    public AppointmentDetailsViewModel(@NonNull Application application) {
        super(application);
    }

    public MutableLiveData<String> getPdfPath() {
        return pdfPath;
    }

    public MutableLiveData<Boolean> getIsUploading() {
        return isUploading;
    }

    public void addAppointmentDocument(Appointment model, String file) {

        /**
         * appointment not sync yet we can modified the add appointment request
         * */
        if (liveAppointmentModel != null && liveAppointmentModel.getTempId().equals(liveAppointmentModel.getAppId())) {
            List<Offlinetable> offlineData = AppDataBase.getInMemoryDatabase(EotApp.getAppinstance())
                    .appointmentModel().getOfflineData(Service_apis.addAppointment);
            if (offlineData != null && offlineData.size() > 0) {
                for (Offlinetable tbl : offlineData) {
                    if (tbl.getParams().contains(liveAppointmentModel.getTempId())) {
                        String params = tbl.getParams();
                        AppointmentAddReq appointmentAddReq = new Gson().fromJson(params, AppointmentAddReq.class);
                        List<String> appDoc = appointmentAddReq.getAppDoc();
                        appDoc.add(file);
                        tbl.setParams(new Gson().toJson(appointmentAddReq));
                        AppDataBase.getInMemoryDatabase(EotApp.getAppinstance()).appointmentModel()
                                .updateOfflineTable(tbl);
                        fetchAppointmentDetails(model);
                        break;
                    }
                }
            }
        }
        /*appointment synced and add new update Request to offline table*/
        else if (liveAppointmentModel != null && !liveAppointmentModel.getTempId().equals(liveAppointmentModel.getAppId())) {
            if (!TextUtils.isEmpty(liveAppointmentModel.getAppId())) {
                List<Offlinetable> offlineData = AppDataBase.getInMemoryDatabase(EotApp.getAppinstance())
                        .appointmentModel().getOfflineData(Service_apis.updateAppointment);

                if (offlineData != null && offlineData.size() > 0) {
                    List<String> previousFiles = new ArrayList<>();
                    AppointmentUpdateReq appointmentUpdateReq = null;
                    Offlinetable tbl = null;
                    /*add all previous file path to new list*/
                    for (Offlinetable tbl1 : offlineData) {
                        if (tbl1.getParams().contains(liveAppointmentModel.getAppId())) {
                            tbl = tbl1;
                            String params = tbl.getParams();
                            appointmentUpdateReq = new Gson().fromJson(params, AppointmentUpdateReq.class);
                            if (appointmentUpdateReq != null && appointmentUpdateReq.getAppDoc() != null) {
                                previousFiles.addAll(appointmentUpdateReq.getAppDoc());
                            }
                            break;
                        }
                    }

                    //submit new update request
                    if (appointmentUpdateReq != null && tbl != null) {
                        if (file != null) {
                            List<String> newFiles = new ArrayList<>();
                            newFiles.add(file);
                            appointmentUpdateReq.setAppDoc(newFiles);
                        }
                        tbl.setParams(new Gson().toJson(appointmentUpdateReq));
                        String s = new Gson().toJson(appointmentUpdateReq);
                        String dateTime = AppUtility.getDateByFormat(AppConstant.DATE_TIME_FORMAT);
                        OfflineDataController.getInstance().addInOfflineDB(Service_apis.updateAppointment, s, dateTime);
                    }

                }
                /**
                 * NO previous update request found creating new one
                 * */
                else {
                    AppointmentUpdateReq updateReq = new AppointmentUpdateReq();
                    updateReq.setAppId(liveAppointmentModel.getAppId());
                    updateReq.setCltId(liveAppointmentModel.getCltId());
                    updateReq.setSiteId(liveAppointmentModel.getSiteId());
                    updateReq.setConId(liveAppointmentModel.getConId());
                    updateReq.setDes(liveAppointmentModel.getDes());
                    try {
                        Long stime = Long.parseLong(liveAppointmentModel.getSchdlStart());
                        Long etime = Long.parseLong(liveAppointmentModel.getSchdlFinish());
                        updateReq.setSchdlStart(AppUtility.getDateWithFormate(stime, "yyyy-MM-dd HH:mm:ss")
                                //AppUtility.getDateWithFormate(stime, AppUtility.dateTimeByAmPmFormate("yyyy-MM-dd HH:mm:ss","yyyy-MM-dd kk:mm:ss"))
                        );
                        updateReq.setSchdlFinish(AppUtility.getDateWithFormate(etime, "yyyy-MM-dd HH:mm:ss"
                                //        AppUtility.dateTimeByAmPmFormate("yyyy-MM-dd HH:mm:ss","yyyy-MM-dd kk:mm:ss")
                        ));

                    } catch (Exception ex) {

                    }

                    Set<String> set = new HashSet<String>();
                    if (liveAppointmentModel.getKpr() != null)
                        for (Keepar keepar : liveAppointmentModel.getKpr())
                            set.add(keepar.getUsrId());
                    updateReq.setMemIds(set);
                    updateReq.setCtry(liveAppointmentModel.getCtry());
                    updateReq.setState(liveAppointmentModel.getState());
                    updateReq.setAdr(liveAppointmentModel.getAdr());
                    updateReq.setZip(liveAppointmentModel.getZip());
                    updateReq.setMob1(liveAppointmentModel.getMob1());
                    updateReq.setNm(liveAppointmentModel.getNm());
                    updateReq.setEmail(liveAppointmentModel.getEmail());

                    List<String> files = new ArrayList<>();
                    files.add(file);
                    updateReq.setAppDoc(files);

                    String s = new Gson().toJson(updateReq);
                    String dateTime = AppUtility.getDateByFormat(AppConstant.DATE_TIME_FORMAT);
                    OfflineDataController.getInstance().addInOfflineDB(Service_apis.updateAppointment, s, dateTime);
                    //  fetchAppointmentDetails(liveAppointmentModel);
                }


            }
        }


    }


    public MutableLiveData<List<AppointmentAttachment>> getLiveAttachments() {
        return liveAttachments;
    }

    public void exportDocumentToPDF(DocumentExportReq req) {
        if (liveAppointmentModel != null && !liveAppointmentModel.getTempId().equals(liveAppointmentModel.getAppId()) && AppUtility.isInternetConnected()) {
            isUploading.setValue(true);
            Requestor requestor = new Requestor(this, ServerResponse.reqCode2);
            requestor.sendRequestToServer(Service_apis.generateJobDocumentPDF, req);
        }
    }


    public void fetchAppointmentDetails(Appointment appointment) {
        liveAppointmentModel = appointment;
        if (liveAppointmentModel != null && !liveAppointmentModel.getTempId().equals(liveAppointmentModel.getAppId()) && AppUtility.isInternetConnected()) {
            AppointementDetailsReq req = new AppointementDetailsReq();
            req.setAppId(liveAppointmentModel.getAppId());
            Requestor requestor = new Requestor(this, ServerResponse.reqCode);
            requestor.sendRequestToServer(Service_apis.getAppointmentDetail, req);
        }
    }

    @Override
    public void onSuccess(Object successObject, int requestCode) {
        JsonObject jsonObject = (JsonObject) successObject;
        switch (requestCode) {
            case ServerResponse.reqCode:
                if (jsonObject.get("success").getAsBoolean()) {
                    JsonObject data = jsonObject.get("data").getAsJsonObject();

                    if (data.has("attachments")) {
                        String jsonString = new Gson().toJson(data.get("attachments").getAsJsonArray());
                        Type typeList = new TypeToken<List<AppointmentAttachment>>() {
                        }.getType();
                        List<AppointmentAttachment> attachmentList = new Gson().fromJson(jsonString, typeList);
                        if (attachmentList != null) liveAttachments.setValue(attachmentList);
                    }

                } else if (jsonObject.get("statusCode") != null && jsonObject.get("statusCode").getAsString().equals(AppConstant.SESSION_EXPIRE)) {
                    //   expenseListView.onSessionExpire(LanguageController.getInstance().getServerMsgByKey(jsonObject.get("message").getAsString()));
                } else {
                    // expenseListView.finish();
                }
                break;

            case ServerResponse.reqCode2:
                isUploading.setValue(false);
                if (jsonObject.get("success").getAsBoolean()) {
                    JsonObject data = jsonObject.get("data").getAsJsonObject();
                    if (data != null) {
                        pdfPath.setValue(data.get("path").getAsString());
                    }

                }
                break;
        }


    }

    @Override
    public void onError(Object errorObject, int requestCode) {
        isUploading.setValue(false);
        Log.d("", errorObject.toString());
    }

    public MultipartBody.Part prepareFilePart(String partName, File file) {
        // create RequestBody instance from file
        if (file == null) return null;
        RequestBody requestFile =
                RequestBody.create(
                        MediaType.parse("multipart/form-data"),
                        file
                );
        // MultipartBody.Part is used to send also the actual file name
        return MultipartBody.Part.createFormData(partName, file.getName(), requestFile);
    }
}
