package com.eot_app.nav_menu.jobs.job_detail.job_equipment.job_equ_remrk.job_equ_mvp;

import android.content.Context;
import android.text.TextUtils;
import android.util.Log;

import com.eot_app.activitylog.ActivityLogController;
import com.eot_app.nav_menu.audit.audit_list.equipment.remark.remark_mvp.RemarkRequest;
import com.eot_app.nav_menu.jobs.job_db.EquArrayModel;
import com.eot_app.nav_menu.jobs.job_db.Job;
import com.eot_app.nav_menu.jobs.job_detail.customform.cstm_form_model.CustomFormList_Res;
import com.eot_app.nav_menu.jobs.job_detail.customform.cstm_form_model.FormList_Model_Req;
import com.eot_app.services.ApiClient;
import com.eot_app.services.Service_apis;
import com.eot_app.utility.AppConstant;
import com.eot_app.utility.AppUtility;
import com.eot_app.utility.App_preference;
import com.eot_app.utility.EotApp;
import com.eot_app.utility.db.AppDataBase;
import com.eot_app.utility.language_support.LanguageController;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.reflect.TypeToken;
import com.hypertrack.hyperlog.HyperLog;

import java.io.File;
import java.lang.reflect.Type;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;

import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;

/**
 * Created by Sonam-11 on 22/9/20.
 */
public class JobEquRemark_PC implements JobEquRemark_PI {
    private final JobEquRemark_View jobEquimView;
    private final int updatelimit;
    private int count;
    private int updateindex;

    public JobEquRemark_PC(JobEquRemark_View jobEquimView) {
        this.jobEquimView = jobEquimView;
        this.updateindex = 0;
        this.updatelimit = AppConstant.LIMIT_MID;
    }

    /**
     * get Custom form List
     ******/
    @Override
    public void getCustomFormList(final Job equipmentRes, final ArrayList<String> jtId) {
        if (AppUtility.isInternetConnected()) {
            FormList_Model_Req formList_model = new FormList_Model_Req(updateindex, updatelimit,
                    equipmentRes.getJobId(), jtId, "1");
            String jsonObjectdata = new Gson().toJson(formList_model);
            JsonParser parser = new JsonParser();
            final JsonObject jsonObject = parser.parse(jsonObjectdata).getAsJsonObject();

            ApiClient.getservices().eotServiceCall(Service_apis.getFormList, AppUtility.getApiHeaders(), jsonObject)
                    .subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new Observer<JsonObject>() {
                        @Override
                        public void onSubscribe(Disposable d) {

                        }

                        @Override
                        public void onNext(JsonObject jsonObject) {
                            Log.e("Responce", jsonObject.toString());
                            if (jsonObject.get("success").getAsBoolean() && jsonObject.get("data").getAsJsonArray().size() > 0) {
                                if (jsonObject.get("count") != null && jsonObject.get("count").getAsInt() > 0) {
                                    count = jsonObject.get("count").getAsInt();
                                    String convert = new Gson().toJson(jsonObject.get("data").getAsJsonArray());
                                    Type listType = new TypeToken<List<CustomFormList_Res>>() {
                                    }.getType();
                                    ArrayList<CustomFormList_Res> formList = new Gson().fromJson(convert, listType);
                                    jobEquimView.setList(formList);
                                }
                            } else if (jsonObject.get("statusCode") != null && jsonObject.get("statusCode").getAsString().equals(AppConstant.SESSION_EXPIRE)) {
                                jobEquimView.onSessionExpire(LanguageController.getInstance().getServerMsgByKey(jsonObject.get("message").getAsString()));
                            } else if (jsonObject.get("count") != null && jsonObject.get("count").getAsString().equals("0")) {
                                jobEquimView.formNotFound();
                            }
                        }

                        @Override
                        public void onError(Throwable e) {
                            Log.e("Error", e.getMessage());
                        }

                        @Override
                        public void onComplete() {
                            if ((updateindex + updatelimit) <= count) {
                                updateindex = updatelimit;
                                getCustomFormList(equipmentRes, jtId);
                            }
                        }
                    });

        } else {
            networkError();
        }
    }

    @Override
    public void addNewRemark(final RemarkRequest remarkRequest, String file, List<MultipartBody.Part> docAns, ArrayList<String> docQueIdArrays,
                             List<MultipartBody.Part> signAns, ArrayList<String> signQueIdArrays) {
        if (AppUtility.isInternetConnected()) {
            HyperLog.i("", "JobEquRemark_PC: " + "addNewRemark:::: Start");

            String mimeType = "";
            MultipartBody.Part body = null;
            List<MultipartBody.Part> filesList = new ArrayList<>();
            if (!TextUtils.isEmpty(file)) {
                File file1 = new File(file);
                if (file1 != null) {
                    mimeType = URLConnection.guessContentTypeFromName(file1.getName());
                    if (mimeType == null) {
                        mimeType = file1.getName();
                    }
                    RequestBody requestFile = RequestBody.create(MediaType.parse(mimeType), file1);
                    // MultipartBody.Part is used to send also the actual file name
                    body = MultipartBody.Part.createFormData("ja[]", file1.getName()
                            //  + file.substring(file.lastIndexOf("."))
                            , requestFile);
                    filesList.add(body);
                }
            }
            RequestBody audId = RequestBody.create(MultipartBody.FORM, remarkRequest.getAudId());
            RequestBody equId = RequestBody.create(MultipartBody.FORM, remarkRequest.getEquId());
            RequestBody userId = RequestBody.create(MultipartBody.FORM, App_preference.getSharedprefInstance().getLoginRes().getUsrId());
            RequestBody remark = RequestBody.create(MultipartBody.FORM, remarkRequest.getRemark());
            RequestBody status = RequestBody.create(MultipartBody.FORM, remarkRequest.getStatus());
            RequestBody lat = RequestBody.create(MultipartBody.FORM, remarkRequest.getLat());
            RequestBody lng = RequestBody.create(MultipartBody.FORM, remarkRequest.getLng());
            RequestBody isJob = RequestBody.create(MultipartBody.FORM, remarkRequest.getIsJob());
            String str = new Gson().toJson(remarkRequest.getAnswerArray().getAnswer());
            RequestBody answerArray = RequestBody.create(MultipartBody.FORM, str);

            String signIdArrayStr = new Gson().toJson(signQueIdArrays);
            RequestBody signQueIdArray = RequestBody.create(MultipartBody.FORM, signIdArrayStr);

            String docIdArrayStr = new Gson().toJson(docQueIdArrays);
            RequestBody docQueIdArray = RequestBody.create(MultipartBody.FORM, docIdArrayStr);


            ActivityLogController
                    .saveActivity(ActivityLogController
                                    .AUDIT_MODULE,
                            ActivityLogController.AUDIT_EQUIP,
                            ActivityLogController.AUDIT_REMARK);
            AppUtility.progressBarShow((Context) jobEquimView);


            ApiClient.getservices().uploadAuditRemarkWithDocument(AppUtility.getApiHeaders(),
                    audId,
                    equId,
                    userId,
                    remark,
                    status,
                    lat,
                    lng,
                    isJob,
                    filesList,
                    docAns,
                    docQueIdArray,
                    answerArray,
                    signAns,
                    signQueIdArray)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new Observer<JsonObject>() {
                        @Override
                        public void onSubscribe(Disposable d) {

                        }

                        @Override
                        public void onNext(JsonObject jsonObject) {
                            try {
                                HyperLog.i("", "JobEquRemark_PC: " + "JsonObject:::: " + jsonObject.toString());

                                Log.d("mahi", jsonObject.toString());
                                if (jsonObject.get("success").getAsBoolean()) {
                                    String message = LanguageController.getInstance().getServerMsgByKey(jsonObject.get("message").getAsString());
                                    updateEquipmentStates(remarkRequest);
                                    jobEquimView.onRemarkUpdate(message);
                                } else if (jsonObject.get("statusCode") != null && jsonObject.get("statusCode").getAsString().equals(AppConstant.SESSION_EXPIRE)) {
                                    jobEquimView.onSessionExpire(LanguageController.getInstance().getServerMsgByKey(jsonObject.get("message").getAsString()));
                                } else {
                                    String message = LanguageController.getInstance().getServerMsgByKey(jsonObject.get("message").getAsString());
                                    jobEquimView.onErrorMsg(message);
                                }
                            } catch (Exception e) {
                                HyperLog.i("", "JobEquRemark_PC: " + "Exception:::: " + e.getMessage());
                                e.printStackTrace();
                            }

                        }

                        @Override
                        public void onError(Throwable e) {
                            Log.d("mahi", e.toString());
                            //  remark_view.onSessionExpire("");
                            HyperLog.i("", "JobEquRemark_PC: " + "onError:::: " + e.getMessage());

                            AppUtility.progressBarDissMiss();
                        }

                        @Override
                        public void onComplete() {
                            Log.d("mahi", "Completed");
                            AppUtility.progressBarDissMiss();
                        }
                    });
        } else {
            networkError();
        }

    }

    private void updateEquipmentStates(RemarkRequest remarkRequest) {
        Job job = AppDataBase.getInMemoryDatabase(EotApp.getAppinstance()).jobModel().getJobsById(remarkRequest.getAudId());
        if (job != null) {
            try {
                for (EquArrayModel equipment : job.getEquArray()) {
                    if (equipment.getEquId().equals(remarkRequest.getEquId())) {
                        equipment.setStatus(remarkRequest.getStatus());
                        equipment.setRemark(remarkRequest.getRemark());
                        AppDataBase.getInMemoryDatabase(EotApp.getAppinstance()).jobModel().updateJob(job);
                        break;
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private void networkError() {
        AppUtility.alertDialog(((Context) jobEquimView), LanguageController.getInstance().
                        getMobileMsgByKey(AppConstant.dialog_alert), LanguageController.getInstance().getMobileMsgByKey
                        (AppConstant.err_check_network), LanguageController.getInstance().getMobileMsgByKey(AppConstant.ok),
                "", new Callable<Boolean>() {
                    @Override
                    public Boolean call() throws Exception {
                        return null;
                    }
                });
    }
}
