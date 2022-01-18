package com.eot_app.nav_menu.usr_time_sheet_pkg;

import android.app.Application;
import android.content.Context;
import android.os.Environment;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;

import com.eot_app.common_api_contr.ApiCalServerReqRes;
import com.eot_app.common_api_contr.ApiRequestresponce;
import com.eot_app.nav_menu.usr_time_sheet_pkg.timeshet_model.TimeSheetReqModel;
import com.eot_app.services.Service_apis;
import com.eot_app.utility.AppConstant;
import com.eot_app.utility.AppUtility;
import com.eot_app.utility.App_preference;
import com.eot_app.utility.EotApp;
import com.eot_app.utility.language_support.LanguageController;
import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.hypertrack.hyperlog.HyperLog;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Created by Sona-11 on 23/8/21.
 */
public class TimeSheetFragViewModel extends AndroidViewModel implements ApiCalServerReqRes {
    private final MutableLiveData<Boolean> finishActivity = new MutableLiveData<>();
    File apkStorage = null;
    File outputFile = null;
    String url = "";
    private String downloadUrl = "";
    private String downloadFileName = "";
    private Context context;

    public TimeSheetFragViewModel(@NonNull Application application) {
        super(application);
    }

    public MutableLiveData<Boolean> getFinishActivity() {
        return finishActivity;
    }

    public void generatetimeSheet(String fromDate, String todate) {
        HyperLog.i("TimeSheetFragViewModel", "generatetimeSheet(M) started");
        TimeSheetReqModel timeSheetReqModel = new TimeSheetReqModel(fromDate, todate);
        ApiRequestresponce requestor = new ApiRequestresponce(this, TimeSheetReqCode);
        if (context != null) {
            AppUtility.progressBarShow(context);
            requestor.sendReqOnServerGetRes(Service_apis.generateUserTimesheetPDF, timeSheetReqModel);
        }

        HyperLog.i("TimeSheetFragViewModel", "generatetimeSheet(M) Stop");
    }


    @Override
    public void onSuccess(Object successObject, int requestCode) {
        JsonObject jsonObject = (JsonObject) successObject;

        switch (requestCode) {
            case TimeSheetReqCode:
                if (jsonObject.get("success").getAsBoolean()) {
                    System.out.println();
                    JsonElement convert = jsonObject.get("data");

                    createFolderForPdfFile(convert);
                    EotApp.getAppinstance().showToastmsg(LanguageController.getInstance().getServerMsgByKey(jsonObject.get("message").toString()));
                    finishActivity.setValue(true);


                } else if (jsonObject.get("statusCode") != null && jsonObject.get("statusCode").getAsString().equals(AppConstant.SESSION_EXPIRE)) {
                    EotApp.getAppinstance().sessionExpired();
                } else {
                    finishActivity.setValue(true);
                    EotApp.getAppinstance().showToastmsg(LanguageController.getInstance().getServerMsgByKey(jsonObject.get("message").getAsString()));
                }
                try {
                    AppUtility.progressBarDissMiss();
                } catch (Exception e) {
                    e.getMessage();
                }
        }
    }

    private void createFolderForPdfFile(JsonElement jsonObject) {
        HyperLog.i("TimeSheetFragViewModel", "createFolderForPdfFile(M) started");
        url = new Gson().toJson(((JsonObject) jsonObject).get("path"));
        downloadUrl = url.replace("\"", "");
        Log.d("success", url);

        downloadFileName = downloadUrl.substring(downloadUrl.lastIndexOf('/'));//Create file name by picking download file name from URL
        Log.e("TAG", downloadFileName);

        ExecutorService service = Executors.newSingleThreadExecutor();
        service.execute(new Runnable() {
            @Override
            public void run() {

                try {

                    URL urls = new URL(App_preference.getSharedprefInstance().getBaseURL() + url.replaceAll("\"", ""));//Create Download URl
                    HttpURLConnection c = (HttpURLConnection) urls.openConnection();//Open Url Connection
                    c.setRequestMethod("GET");//Set Request Method to "GET" since we are grtting data
                    c.connect();//connect the URL Connection

                    //    If Connection response is not OK then show Logs
                    if (c.getResponseCode() != HttpURLConnection.HTTP_OK) {
                        Log.e("TAG", "Server returned HTTP " + c.getResponseCode() + " " + c.getResponseMessage());

                    }

                    // apkStorage = new File(context.getExternalFilesDir(ACTION_CREATE_DOCUMENT) + "/" + "EyeOnTask");
                    //     apkStorage = new File(context.getExternalFilesDir("") + "/" + "EyeOnTask");
                    apkStorage = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOCUMENTS) + File.separator + "EyeOnTask/Timesheet");

                    //If File is not present create directory
                    if (!apkStorage.exists()) {
                        apkStorage.mkdir();
                        Log.e("TAG", "Directory Created.");
                    }

                    outputFile = new File(apkStorage, downloadFileName);//Create Output file in Main File

                    try {
                        //Create New File if not present
                        if (!outputFile.exists()) {
                            //outputFile.createNewFile();
                            outputFile.getParentFile().mkdirs();


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
                    HyperLog.i("TimeSheetFragViewModel", "Download Error Exception" + e.getMessage());

                }


            }
        });
        HyperLog.i("TimeSheetFragViewModel", "createFolderForPdfFile(M) Stop");

    }


    @Override
    public void onError(Throwable errorObject, int requestCode) {
        EotApp.getAppinstance().showToastmsg(errorObject.getMessage());
        try {
            AppUtility.progressBarDissMiss();
        } catch (Exception e) {
            e.getMessage();
        }
    }

    public void setContext(Context activity) {
        this.context = activity;
    }
}
