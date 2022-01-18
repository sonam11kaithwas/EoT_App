package com.eot_app.nav_menu.jobs.job_db;

import static androidx.room.OnConflictStrategy.REPLACE;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.eot_app.nav_menu.jobs.job_detail.addinvoiveitem2pkg.model.InvoiceItemDataModel;
import com.eot_app.utility.settings.setting_db.TagData;

import java.util.List;
import java.util.Set;

/**
 * Created by aplite_pc302 on 6/1/18.
 */
@Dao
public interface JobDao {
    @Insert(onConflict = REPLACE)
    void inserJob(List<Job> job);

    @Query("select * from Job order by schdlStart DESC")
    List<Job> getJoblist();

    @Query("select * from Job where status in (:job_status) order by schdlStart DESC")
    List<Job> getJobsByStatus(List<String> job_status);

    @Query("select * from Job where jobId =:jobid")
    Job getJobsById(String jobid);

    @Query("SELECT COUNT(*) from Job")
    int getTotleCount();

    @Query("UPDATE Job SET status = :status_no,updateDate=:updateDate  WHERE jobId = :jobId")
    void updateJobByStatus(String jobId, String status_no, String updateDate);

    @Query("delete from Job")
    void delete();

    @Query("UPDATE Job SET status = :toStatus WHERE jobId != :jobId and status = :fromStatus")
    void setOtherToPending(String jobId, String fromStatus, String toStatus);

    //    for delete job
    @Query("delete from Job where isdelete = 0 or status = '3' or status = '4' or status = '10'")
    void deleteJobByIsDelete();

    @Query("delete from Job where jobId=:jobid")
    void deleteJobById(String jobid);

//    @Query("delete from Job where status != :s1 or status != :s2  or status != :s3  or status != :s4  or status != :s5  or status != :s6  or status != :s7  or status != :s8  or status != :s9  or status != :s10  or status != :s11 or status!= :s12")
//    void deleteJobStatusNot(String s1, String s2, String s3, String s4, String s5, String s6, String s7, String s8, String s9, String s10, String s11, String s12);

    @Insert(onConflict = REPLACE)
    void inserSingleJob(Job jobitem);


    @Query("delete from Job WHERE jobId = :tempId and tempId = :tempId")
    void udpateJobByTempIdtoOriganalId(String tempId);

    @Query("delete from Job where tempId=:tempId")
    void deleteJobByTempId(String tempId);

    //    delete job which is unused.
    @Query("delete from Job where jobId=tempId")
    void deleteUnusedTempJobs();

    @Query("select * from Job where tagData IN (:tagdata) order by schdlStart DESC")
    List<Job> getTagJobList(List<TagData> tagdata);

    @Query("select * from Job where status IN (:status)  and prty IN (:job_prty)  order by schdlStart DESC")
    List<Job> loadJobsFromStatusPrtY(Set<String> status, Set<String> job_prty);

    @Query("select * from Job where status IN (:status)  order by schdlStart DESC")
    List<Job> loadJobsFromStatus(Set<String> status);

    @Query("select * from Job where prty IN (:prty)  order by schdlStart DESC")
    List<Job> loadJobsFromPrty(Set<String> prty);

    @Query("select * from Job order by schdlStart DESC")
    List<Job> getJoblistByScheduleStart();

    @Query("SELECT * FROM Job WHERE nm LIKE (:search) or label LIKE (:search) or adr LIKE (:search) or city LIKE (:search) or status IN (:status)  and prty IN (:job_prty) order by schdlStart DESC")
    List<Job> getJobFilterByScheduleStart(String search, Set<String> status, Set<String> job_prty);

    @Query("SELECT * FROM Job WHERE nm LIKE (:search) or label LIKE (:search) or adr LIKE (:search) or city LIKE (:search) or  status IN (:status)  and prty IN (:job_prty) order by schdlStart DESC")
    List<Job> getJobFilterByDate(String search, Set<String> status, Set<String> job_prty);

    @Query("SELECT * FROM Job WHERE cnm LIKE (:search) or label LIKE (:search) order by schdlStart DESC")
    List<Job> getJobByNameOrLable(String search);


    //    temp logic
    @Query("SELECT * FROM Job WHERE nm LIKE (:search) or label LIKE (:search) or adr LIKE (:search) or city LIKE (:search) or status IN (:status) order by schdlStart DESC")
    List<Job> getJobFilterByScheduleStart_null_priority(String search, Set<String> status);

    @Query("SELECT * FROM Job WHERE nm LIKE (:search) or label LIKE (:search) or adr LIKE (:search) or city LIKE (:search) or  status IN (:status) order by schdlStart DESC")
    List<Job> getJobFilterByDate_null_priority(String search, Set<String> status);


    @Query("SELECT * FROM Job WHERE nm LIKE (:search) or label LIKE (:search) or adr LIKE (:search) or city LIKE (:search)  or prty IN (:job_prty) order by schdlStart DESC")
    List<Job> getJobFilterByScheduleStart_null_status(String search, Set<String> job_prty);

    @Query("SELECT * FROM Job WHERE nm LIKE (:search) or label LIKE (:search) or adr LIKE" +
            " (:search) or city LIKE (:search)  or prty IN (:job_prty) order by updateDate DESC")
    List<Job> getJobFilterByDate_null_status(String search, Set<String> job_prty);


    /*** add edit Complation ***/
    @Query("UPDATE Job SET complNote = :complNote,updateDate=:updateDate  WHERE jobId = :jobId")
    void updateJobComplation(String jobId, String complNote, String updateDate);

    /**
     * reschedule job update start and end time of job
     */
    @Query("UPDATE Job  SET schdlStart = :schdlStart, schdlFinish =:schdlFinish  WHERE jobId = :jobId ")
    void rescheduleJob(String schdlStart, String schdlFinish, String jobId);


    /**
     * sort by job lable
     ****/
    @Query("select * from Job order by  LOWER(label) DESC")
    List<Job> getJoblistByJobLable();

    @Query("UPDATE Job  SET itemData = :itemData WHERE jobId = :jobId ")
    void updateJobitems(String jobId, List<InvoiceItemDataModel> itemData);

    @Query("UPDATE Job  SET equArray = :equArray WHERE jobId = :jobId ")
    void updateJobequipArray(String jobId, List<EquArrayModel> equArray);

    /**
     * new query added for equipment on job
     */
    @Query("select *from Job WHERE equArray LIKE (:equipmentID)")
    List<Job> getJobEquipmentByEquipmentId(String equipmentID);

    @Update
    void updateJob(Job model);


    @Query("select *from Job WHERE equArray LIKE (:equipmentID)")
    Job getJobEquipmentByEquipmentIds(String equipmentID);


    @Query("UPDATE Job  SET cltId = :cltId WHERE jobId = :jobId ")
    void updateJobCltID(String jobId, String cltId);

    @Query("UPDATE Job SET signature=:signature WHERE jobId= :jobId")
    void updateSignaturePath(String signature, String jobId);

    @Query("UPDATE Job SET complNote=:complNote WHERE jobId= :jobId")
    void updateComplitionNotes(String complNote, String jobId);

////  @Query("SELECT * FROM user WHERE birthday = :targetDate")
//    @Query("delete from Job where status != '1' or status != '2' or status != '3' or status != '4' or status != '5' or status != '6' " +
//            "or status != '7' or status != '8' or status != '9' or status != '10' or status != '11' or status != '12'")
//    void deleteJOBStatusNotFound();
}
