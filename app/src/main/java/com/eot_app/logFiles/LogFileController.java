package com.eot_app.logFiles;

import android.Manifest;
import android.os.Environment;
import android.util.Log;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.OutputStreamWriter;

/**
 * Created by aplite_pc302 on 3/13/19.
 */

public class LogFileController {
    private static final int REQUEST_EXTERNAL_STORAGE = 1;
    private static final String[] PERMISSIONS_STORAGE = {
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE};
    private static LogFileController logFileControllerInstance = null;
    private final String FileName = "locationLogs.txt";
    private final String LogFilePath = "Eot_logs";

    private LogFileController() {

    }

    public static LogFileController getInstance() {
        if (logFileControllerInstance == null) {
            logFileControllerInstance = new LogFileController();
        }
        return logFileControllerInstance;
    }

    public void WriteLogFiles(String previous_lat, String previous_lon, String current_lat, String current_lon, String distance, String date_time, String isService_call, String onLocationChange) {

        StringBuffer sb = new StringBuffer();
        String extStorageDirectory = Environment
                .getExternalStorageDirectory().getAbsolutePath();

        File folder = new File(extStorageDirectory, LogFilePath);
        if (!folder.exists()) {
            folder.mkdir();
        }
        File file = new File(folder, FileName);
        try {
            if (file.exists()) {
                sb = getOldContentOfFile(file);

            } else {
                file.createNewFile();
            }
            sb.append(onLocationChange).append(",  ")
                    .append(previous_lat).append(",  ")
                    .append(previous_lon).append(", ")
                    .append(current_lat).append(",  ")
                    .append(current_lon).append(",  ")
                    .append(distance).append(",  ")
                    .append(date_time).append(",  ")
                    .append(isService_call).append("\n\n");

            FileOutputStream fOut = new FileOutputStream(file);
            OutputStreamWriter myOutWriter = new OutputStreamWriter(fOut);
            myOutWriter.append(sb);
            myOutWriter.close();
            fOut.close();

        } catch (Exception e) {
            Log.e("ERRR", "Could not create file", e);
        }
    }

    private StringBuffer getOldContentOfFile(File file) {

        StringBuffer text = new StringBuffer();
        try {
            BufferedReader br = new BufferedReader(new FileReader(file));
            String line;

            while ((line = br.readLine()) != null) {
                text.append(line);
                text.append('\n');
            }
            br.close();
        } catch (IOException e) {
            //You'll need to add proper error handling here
        }
        return text;
    }
}
