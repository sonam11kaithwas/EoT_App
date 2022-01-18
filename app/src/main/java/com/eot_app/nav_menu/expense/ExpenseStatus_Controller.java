package com.eot_app.nav_menu.expense;

import android.util.Log;

import com.eot_app.nav_menu.jobs.job_detail.detail.jobdetial_model.JobStatusModel;
import com.eot_app.utility.EotApp;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

public class ExpenseStatus_Controller {
    private static ExpenseStatus_Controller ourInstance;
    private List<JobStatusModel> expenseStatusList = new ArrayList<>();// = new ArrayList<>();
//    private JSONObject jsonObject;

    private ExpenseStatus_Controller() {
        getStatusList();
    }

    public static ExpenseStatus_Controller getInstance() {
        if (ourInstance == null) {
            ourInstance = new ExpenseStatus_Controller();
        }
        return ourInstance;
    }


    public void getStatusList() {
        expenseStatusList = new ArrayList<>();
        try {
            InputStream is = EotApp.getAppinstance().getAssets().open("expenses_status.json");
            int size = is.available();
            byte[] data = new byte[size];
            is.read(data);
            is.close();
            //   getJsonFileById();//get json for buttob Ids & job Status change by button
            String json = new String(data);
            JSONObject jsonObject = new JSONObject(json);
            JSONArray jsonArray = jsonObject.getJSONArray("expensestatus");
            is.close();
            if (jsonArray != null) {
                for (int i = 0; i < jsonArray.length(); i++) {
                    expenseStatusList.add(new JobStatusModel((String) jsonArray.getJSONObject(i).get("id"), jsonArray.getJSONObject(i).getString("name"), (String) jsonArray.getJSONObject(i).get("img")));
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException je) {
            Log.e("Error", je.getMessage());
            je.printStackTrace();
        }
    }

//    private void getJsonFileById() {
//        try {
//            InputStream is = EotApp.getAppinstance().getAssets().open("job_status_btn.json");
//            int size = is.available();
//            byte[] data = new byte[size];
//            is.read(data);
//            is.close();
//            String json = new String(data);
//            jsonObject = new JSONObject(json);
//            Log.e("", "" + jsonObject.toString());
//        } catch (Exception ex) {
//            ex.printStackTrace();
//        }
//    }


//    public JobStatusModel getStatusByButtonAction(String ids, int type) {
//        type--; //  see the josn file first and this btn id is the indexing of array in json format.
//        try {
//            JSONArray jsonArray = jsonObject.getJSONArray(ids);
//            String statusName="";
//            statusName=jsonArray.getJSONObject(type).getString("status_name");
//            return new JobStatusModel(jsonArray.getJSONObject(type).getString("status_no"), statusName, jsonArray.getJSONObject(type).getString("img"));
//        } catch (JSONException e) {
//            e.printStackTrace();
//        }
//        return null;
//    }


//    public JobStatusModel getExpenseStatusByName(String statusName) {
//        for (JobStatusModel jobStatusModel : expenseStatusList) {
//            if (jobStatusModel.getStatus_name().equals(statusName)) {
//                return jobStatusModel;
//            }
//        }
//        return null;
//    }

    public JobStatusModel getStatusObjectById(String statusId) {
        if (expenseStatusList != null) {
            for (JobStatusModel jobStatusModel : expenseStatusList) {
                if (jobStatusModel != null && jobStatusModel.getStatus_no().equals(statusId)) {
                    return jobStatusModel;
                }
            }
        }
        return null;
    }


}