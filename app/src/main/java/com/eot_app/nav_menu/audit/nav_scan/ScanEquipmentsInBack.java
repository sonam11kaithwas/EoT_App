package com.eot_app.nav_menu.audit.nav_scan;

import android.os.Handler;
import android.os.Looper;

import com.eot_app.nav_menu.audit.audit_db.AuditDao;
import com.eot_app.nav_menu.audit.audit_list.audit_mvp.model.AuditList_Res;
import com.eot_app.nav_menu.audit.audit_list.equipment.model.Equipment_Res;
import com.eot_app.nav_menu.jobs.job_db.EquArrayModel;
import com.eot_app.nav_menu.jobs.job_db.Job;
import com.eot_app.nav_menu.jobs.job_db.JobDao;
import com.eot_app.utility.EotApp;
import com.eot_app.utility.db.AppDataBase;
import com.eot_app.utility.settings.equipmentdb.Equipment;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ScanEquipmentsInBack { // extends AsyncTask<String, Void, Void> {

    private final List<AuditList_Res> auditEquipmetnList = new ArrayList<>();
    private final List<Job> jobEquipmentList = new ArrayList<>();
    private final OnSearchedEquipment onSearchedEquipment;
    private Equipment equipmentByIdOrSerialNo;
    private String searchKey = "";

    public ScanEquipmentsInBack(OnSearchedEquipment onSearchedEquipment, String searchKey) {//
        this.onSearchedEquipment = onSearchedEquipment;
        this.searchKey = searchKey;
    }

    public void filterEquipments() {
        try {

            ExecutorService executorService = Executors.newSingleThreadExecutor();

            executorService.execute(new Runnable() {
                @Override
                public void run() {

/*******    pre processs before background proccess*****/

                    new Handler(Looper.getMainLooper()).post(new Runnable() {
                        public void run() {
                            if (onSearchedEquipment != null)
                                onSearchedEquipment.showProgress(true);
                        }
                    });


                    /****** background proccess*******/


//                    String searchKey = "";//strings[0]; //search key may be serial no or barcode no
                    //job search
                    JobDao jobDao = AppDataBase.getInMemoryDatabase(EotApp.getAppinstance())
                            .jobModel();

                    List<Job> jobEquipmentByEquipmentId = jobDao.getJobEquipmentByEquipmentId("%" + searchKey + "%");
                    if (jobEquipmentByEquipmentId != null) {
                        jobEquipmentList.clear();
                        for (Job job : jobEquipmentByEquipmentId) {
                            List<EquArrayModel> equArray = job.getEquArray();
                            if (equArray != null) {
                                for (EquArrayModel equipment : equArray) {
                                    if (equipment.getSno() != null && equipment.getSno().equals(searchKey) ||
                                            equipment.getBarcode() != null && equipment.getBarcode().equals(searchKey)) {
                                        jobEquipmentList.add(job);
                                    }
                                }
                            }
                        }
                    }


                    //audit search
                    AuditDao auditDao = AppDataBase.getInMemoryDatabase(EotApp.getAppinstance())
                            .auditDao();

                    List<AuditList_Res> auditListId = auditDao.getAuditEquipmentByEquipmentId("%" + searchKey + "%");
                    if (auditListId != null) {
                        auditEquipmetnList.clear();
                        for (AuditList_Res audit : auditListId) {
                            List<Equipment_Res> equArray = audit.getEquArray();
                            if (equArray != null) {
                                for (Equipment_Res equipment : equArray) {
                                    if (equipment.getSno() != null && equipment.getSno().equals(searchKey) ||
                                            equipment.getBarcode() != null && equipment.getBarcode().equals(searchKey)) {
                                        auditEquipmetnList.add(audit);
                                    }
                                }
                            }
                        }
                    }


                    if (jobEquipmentList.size() == 0 && auditEquipmetnList.size() == 0) {
                        //search equipment in equipment main table to show details of equipment
                        equipmentByIdOrSerialNo = AppDataBase.getInMemoryDatabase(EotApp.getAppinstance()).equipmentDao().getEquipmentByBarcodeOrSerialNo(searchKey, searchKey);

                    }


                    /*****Post update UI processs after background proccess****/

                    new Handler(Looper.getMainLooper()).post(new Runnable() {
                        public void run() {
                            if (onSearchedEquipment != null) {
                                onSearchedEquipment.showProgress(false);
                                if (jobEquipmentList.size() != 0 || auditEquipmetnList.size() != 0)
                                    onSearchedEquipment.OnRecordFound(jobEquipmentList, auditEquipmetnList);
                                else onSearchedEquipment.onEquipmentFound(equipmentByIdOrSerialNo);
                            }
                        }
                    });


                }
            });


        } catch (Exception exception) {
            exception.getMessage();
        }
    }

//    @Override
//    protected void onPreExecute() {
//        super.onPreExecute();
//        if (onSearchedEquipment != null)
//            onSearchedEquipment.showProgress(true);
//    }
//
//    @Override
//    protected void onPostExecute(Void aVoid) {
//        super.onPostExecute(aVoid);
//        if (onSearchedEquipment != null) {
//            onSearchedEquipment.showProgress(false);
//            if (jobEquipmentList.size() != 0 || auditEquipmetnList.size() != 0)
//                onSearchedEquipment.OnRecordFound(jobEquipmentList, auditEquipmetnList);
//            else onSearchedEquipment.onEquipmentFound(equipmentByIdOrSerialNo);
//        }
//    }

    /*@Override
    protected Void doInBackground(String... strings) {
        String searchKey = strings[0]; //search key may be serial no or barcode no
        //job search
        JobDao jobDao = AppDataBase.getInMemoryDatabase(EotApp.getAppinstance())
                .jobModel();

        List<Job> jobEquipmentByEquipmentId = jobDao.getJobEquipmentByEquipmentId("%" + searchKey + "%");
        if (jobEquipmentByEquipmentId != null) {
            jobEquipmentList.clear();
            for (Job job : jobEquipmentByEquipmentId) {
                List<EquArrayModel> equArray = job.getEquArray();
                if (equArray != null) {
                    for (EquArrayModel equipment : equArray) {
                        if (equipment.getSno() != null && equipment.getSno().equals(searchKey) ||
                                equipment.getBarcode() != null && equipment.getBarcode().equals(searchKey)) {
                            jobEquipmentList.add(job);
                        }
                    }
                }
            }
        }


        //audit search
        AuditDao auditDao = AppDataBase.getInMemoryDatabase(EotApp.getAppinstance())
                .auditDao();

        List<AuditList_Res> auditListId = auditDao.getAuditEquipmentByEquipmentId("%" + searchKey + "%");
        if (auditListId != null) {
            auditEquipmetnList.clear();
            for (AuditList_Res audit : auditListId) {
                List<Equipment_Res> equArray = audit.getEquArray();
                if (equArray != null) {
                    for (Equipment_Res equipment : equArray) {
                        if (equipment.getSno() != null && equipment.getSno().equals(searchKey) ||
                                equipment.getBarcode() != null && equipment.getBarcode().equals(searchKey)) {
                            auditEquipmetnList.add(audit);
                        }
                    }
                }
            }
        }


        if (jobEquipmentList.size() == 0 && auditEquipmetnList.size() == 0) {
            //search equipment in equipment main table to show details of equipment
            equipmentByIdOrSerialNo = AppDataBase.getInMemoryDatabase(EotApp.getAppinstance()).equipmentDao().getEquipmentByBarcodeOrSerialNo(searchKey, searchKey);

        }


        return null;
    }
    */
    public interface OnSearchedEquipment {
        void OnRecordFound(List<Job> jobList, List<AuditList_Res> auditList);

        void onEquipmentFound(Equipment equipment);

        void showProgress(boolean b);

    }
}
