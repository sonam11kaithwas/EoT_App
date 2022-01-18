package com.eot_app.nav_menu.jobs.job_detail.feedback.feedback_mvp;

import android.util.Log;

import androidx.fragment.app.Fragment;

import com.eot_app.activitylog.ActivityLogController;
import com.eot_app.services.ApiClient;
import com.eot_app.utility.AppConstant;
import com.eot_app.utility.AppUtility;
import com.eot_app.utility.App_preference;
import com.eot_app.utility.EotApp;
import com.eot_app.utility.db.AppDataBase;
import com.eot_app.utility.language_support.LanguageController;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.net.URLConnection;
import java.util.concurrent.Callable;

import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;

public class Feedback_pc implements Feedback_pi {
    FeedBack_View feedBack_view;

    public Feedback_pc(FeedBack_View feedBack_view) {
        this.feedBack_view = feedBack_view;
    }

    @Override
    public void UploadSignToServer(File mypath, String DesNotes, String setSmilie, String mParam2) {
        String mimeType = "";

        MultipartBody.Part body = null;
        if (mypath != null) {
            mimeType = URLConnection.guessContentTypeFromName(mypath.getName());
            RequestBody requestFile = RequestBody.create(MediaType.parse(mimeType), mypath);
            // MultipartBody.Part is used to send also the actual file name
            body = MultipartBody.Part.createFormData("sign", mypath.getName(), requestFile);
        }
        RequestBody description = RequestBody.create(MultipartBody.FORM, DesNotes);

        RequestBody rating = RequestBody.create(MultipartBody.FORM, String.valueOf(setSmilie));

        RequestBody jobId = RequestBody.create(MultipartBody.FORM, mParam2);

        RequestBody userId = RequestBody.create(MultipartBody.FORM, App_preference.getSharedprefInstance().getLoginRes().getUsrId());

        //RequestBody token = RequestBody.create(okhttp3.MultipartBody.FORM, tokenstr);

        if (AppUtility.isInternetConnected()) {

            ActivityLogController.saveActivity(ActivityLogController.JOB_MODULE, ActivityLogController.JOB_FEEDBACK, ActivityLogController.JOB_MODULE);

            AppUtility.progressBarShow(((Fragment) feedBack_view).getActivity());
            ApiClient.getservices().uploadFileWithPartMap(AppUtility.getApiHeaders(), userId, jobId, description, rating, body)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new Observer<ResponseBody>() {
                        @Override
                        public void onSubscribe(Disposable d) {

                        }

                        @Override
                        public void onNext(ResponseBody responce) {
                            Log.e("Signature", responce.toString());
                            try {
                                JSONObject jsonObject = new JSONObject(responce.string());
                                boolean success = jsonObject.getBoolean("success");
                                if (success) {
                                    if (jsonObject.getJSONArray("data") != null && jsonObject.getJSONArray("data").length() > 0) {
                                        if (jsonObject.getJSONArray("data").getJSONObject(0).getString("status_code").equals("1")) {
                                            String jobId = jsonObject.getJSONArray("data").getJSONObject(0).getString("jobid");
                                            AppDataBase.getInMemoryDatabase(EotApp.getAppinstance()).jobModel().deleteJobById(jobId);
//                          for alert msg to remove job
                                            EotApp.getAppinstance().notifyObserver("removeFW", jobId, LanguageController.getInstance().getServerMsgByKey(jsonObject.getString("message")));
                                        }
                                    } else {
                                        String message = LanguageController.getInstance().getServerMsgByKey(jsonObject.getString("message"));
                                        EotApp.getAppinstance().showToastmsg(message);
                                        feedBack_view.onfeedbackSendSuccessfully();
                                    }
                                } else if (jsonObject.get("statusCode").equals(AppConstant.SESSION_EXPIRE)) {
                                    feedBack_view.onSessionExpire(LanguageController.getInstance().getServerMsgByKey(jsonObject.getString("message")));
                                } else {
                                    EotApp.getAppinstance().showToastmsg(LanguageController.getInstance().getServerMsgByKey(jsonObject.getString("message")));
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        }


                        @Override
                        public void onError(Throwable e) {
                            AppUtility.progressBarDissMiss();
                            Log.e("Sign error", e.getMessage());
                        }

                        @Override
                        public void onComplete() {
                            AppUtility.progressBarDissMiss();

                        }
                    });
        } else {
            networkDialog();
        }
    }


    private void networkDialog() {
        AppUtility.alertDialog((((Fragment) feedBack_view).getActivity()), LanguageController.getInstance().getMobileMsgByKey(AppConstant.dialog_alert), LanguageController.getInstance().getMobileMsgByKey(AppConstant.err_check_network), LanguageController.getInstance().getMobileMsgByKey(AppConstant.ok), "", new Callable<Boolean>() {
            @Override
            public Boolean call() throws Exception {
                return null;
            }
        });
    }
}
