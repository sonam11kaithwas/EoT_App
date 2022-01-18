package com.eot_app.nav_menu.report;

import android.app.Activity;
import android.content.Context;
import android.os.Environment;
import android.util.Log;

import androidx.fragment.app.Fragment;

import com.eot_app.services.ApiClient;
import com.eot_app.services.Service_apis;
import com.eot_app.utility.AppConstant;
import com.eot_app.utility.AppUtility;
import com.eot_app.utility.App_preference;
import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

public class GetloginReport_Pc implements LoginReport_Pi {

    public static final String NOTIFICATION_CHANNEL_ID = "10001";
    private final static String default_notification_channel_id = "default";
    Context context;
    LoginReport_view loginReport_view;
    File apkStorage = null;
    File outputFile = null;
    String url = "";
    private String downloadUrl = "";
    private String downloadFileName = "";
    private Activity activity;

    public GetloginReport_Pc(LoginReport_view loginReport_view) {
        this.loginReport_view = loginReport_view;
    }

    @Override
    public void getLoginReport(GetReport_Req getReport_req) {
        if (AppUtility.isInternetConnected()) {

            AppUtility.progressBarShow(((Fragment) loginReport_view).getActivity());

            String data = new Gson().toJson(getReport_req);
            ApiClient.getservices().eotServiceCall(Service_apis.getLoginReport, AppUtility.getApiHeaders(), AppUtility.getJsonObject(data))
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new Observer<JsonObject>() {
                        @Override
                        public void onSubscribe(Disposable d) {

                        }

                        @Override
                        public void onNext(JsonObject jsonObject) {
                            if (jsonObject.get("success").getAsBoolean()) {
                                JsonElement convert = jsonObject.get("data");
                                url = new Gson().toJson(((JsonObject) convert).get("path"));
                                downloadUrl = url.replace("\"", "");
                                Log.d("success", url);
                                //Toast.makeText(context, "success", Toast.LENGTH_SHORT).show();
                            } else if (jsonObject.get("statusCode") != null && jsonObject.get("statusCode").getAsString().equals(AppConstant.SESSION_EXPIRE)) {
                                //  joblist_view.onSessionExpired(LanguageController.getInstance().getServerMsgByKey(jsonObject.get("message").getAsString()));
                            }
                        }


                        @Override
                        public void onError(Throwable e) {
                            Log.e("TAG", e.getMessage());
                            AppUtility.progressBarDissMiss();
                        }

                        @Override
                        public void onComplete() {
                            Log.e("TAG", "onComplete");
                            downloadReport(url);
                            AppUtility.progressBarDissMiss();
                            //  EotApp.getAppinstance().showToastmsg(LanguageController.getInstance().getMobileMsgByKey(AppConstant.download_res));
                        }
                    });
        } else {
            // joblist_view.setRefereshPullOff();
        }

    }

    private void downloadReport(final String convert) {


        downloadFileName = downloadUrl.substring(downloadUrl.lastIndexOf('/'));//Create file name by picking download file name from URL
        Log.e("TAG", downloadFileName);

        ExecutorService service = Executors.newSingleThreadExecutor();
        service.execute(new Runnable() {
            @Override
            public void run() {

                try {
                    URL url = new URL(App_preference.getSharedprefInstance().getBaseURL() + convert.replaceAll("\"", ""));//Create Download URl
                    HttpURLConnection c = (HttpURLConnection) url.openConnection();//Open Url Connection
                    c.setRequestMethod("GET");//Set Request Method to "GET" since we are grtting data
                    c.connect();//connect the URL Connection

                    //If Connection response is not OK then show Logs
                    if (c.getResponseCode() != HttpURLConnection.HTTP_OK) {
                        Log.e("TAG", "Server returned HTTP " + c.getResponseCode() + " " + c.getResponseMessage());

                    }


                    apkStorage = new File(Environment.getExternalStorageDirectory() + "/" + "EyeOnTask");
                    //If File is not present create directory
                    if (!apkStorage.exists()) {
                        apkStorage.mkdir();
                        Log.e("TAG", "Directory Created.");
                    }

                    outputFile = new File(apkStorage, downloadFileName);//Create Output file in Main File

                    try {
                        //Create New File if not present
                        if (!outputFile.exists()) {
                            outputFile.createNewFile();
                            Log.e("TAG", "File Created");
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                    FileOutputStream fos = new FileOutputStream(outputFile);//Get OutputStream for NewFile Location
                    InputStream is = c.getInputStream();//Get InputStream for connection

                    byte[] buffer = new byte[1024];//Set buffer type
                    int len1 = 0;//init length
                    while ((len1 = is.read(buffer)) != -1) {
                        fos.write(buffer, 0, len1);//Write new file
                    }

                    //Close all connection after doing task
                    fos.close();
                    is.close();

                } catch (Exception e) {

                    //Read exception if something went wrong
                    e.printStackTrace();
                    outputFile = null;
                    Log.e("TAG", "Download Error Exception " + e.getMessage());
                }


            }
        });
    }

}
