package com.eot_app.nav_menu.appointment.dbappointment;

import static androidx.room.OnConflictStrategy.REPLACE;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.RoomWarnings;
import androidx.room.Update;

import com.eot_app.nav_menu.jobs.job_db.Job;
import com.eot_app.utility.settings.setting_db.Offlinetable;

import java.util.List;

@Dao
public interface AppointmentDao {
    @Insert(onConflict = REPLACE)
    void insertAppointments(List<Appointment> appointments);

    @Insert(onConflict = REPLACE)
    void insertSingleAppointment(Appointment appointment);


    @Query("select * from Appointment order by schdlStart DESC")
    List<Appointment> getAppointmentList();

    @Query("select * from Appointment where status in (:appointment_status) order by schdlStart DESC")
    List<Appointment> getAppointmentByStatus(List<String> appointment_status);

    @Query("select * from Appointment where appId =:appId")
    Appointment getAppointmentById(String appId);


    @Query("UPDATE Appointment SET quotId = :quotId,quotLabel=:quotelabel WHERE appId = :appId")
    void updateAppointment(String appId, String quotId, String quotelabel);

    @Query("UPDATE Appointment  SET cltId = :cltId,siteId = :siteId,conId = :conId  WHERE appId = :appId ")
    void updateAppointmentCltID(String appId, String cltId, String siteId, String conId);


    //    for delete job
    @Query("delete from Appointment")
    void delete();

    @Query("delete from Appointment where isdelete = 0 or status = '3' or status = '4' or status = '10'")
    void deleteAppointmentByIsDelete();

    @Query("delete from Appointment where appId=:appid")
    void deleteAppointmentById(String appid);

    @Query("delete from Appointment where tempId=:tempId")
    void deleteAppointmentByTempId(String tempId);

    /**
     * search job of particular date
     */

    /******* or schdlFinish >= :startTime AND schdlFinish >=:endTime or schdlStart >=:startTime  or schdlStart >=:endTime*********/

//    @Query("SELECT * FROM Job WHERE schdlStart BETWEEN :startTime AND :endTime")
//    List<Job> getJobsByDate(long startTime, long endTime);
    @Query("SELECT * FROM Job WHERE schdlStart BETWEEN :startTime AND :endTime or schdlFinish BETWEEN :startTime AND :endTime or  schdlStart <= :startTime and schdlStart<=:endTime and schdlFinish>=:startTime and schdlFinish>=:endTime")
    List<Job> getJobsByDate(long startTime, long endTime);


    /**
     * search appointment of particular date
     */
    @SuppressWarnings(RoomWarnings.CURSOR_MISMATCH)
    @Query("SELECT a.attachCount,a.schdlStart ,a.schdlFinish ,a.status, a.appId,a.tempId,a.adr,a.des,a.label,a.nm ,c.nm as conId FROM Appointment a LEFT JOIN Client c ON a.cltId=c.cltId WHERE a.isdelete=1 and a.schdlStart BETWEEN :startTime AND :endTime or schdlFinish BETWEEN :startTime AND :endTime or schdlStart<=:startTime and schdlStart<=:endTime and schdlFinish>=:startTime and schdlFinish>=:endTime")
    List<Appointment> getAppointmentByDate(long startTime, long endTime);
    

    /*//    @Query("SELECT * FROM Job WHERE schdlStart BETWEEN :startTime AND :endTime")
//    List<Job> getJobsByDate(long startTime, long endTime);*/

    /*    @Query("SELECT * FROM Job WHERE schdlStart BETWEEN :startTime AND :endTime or schdlFinish BETWEEN :startTime AND :endTime or  schdlStart <= :startTime and schdlStart<=:endTime and schdlFinish>=:startTime and schdlFinish>=:endTime")
    List<Job> getJobsByDate(long startTime, long endTime);*/


    @Query("select cd.mob1,cd.email,cd.conId,s.siteId,c.cltId, s.adr,s.city,s.ctry,s.state,s.zip from Client c left join Site_model s On c.cltId=s.cltID left join ContactData cd ON c.cltId=cd.cltId where c.cltId=:clientId and s.def=1 and cd.def=1 limit 1")
    ClientCompleteAddress getCompleteAddress(String clientId);

    @Query("SELECT * FROM offlinetable WHERE service_name=:apiname")
    List<Offlinetable> getOfflineData(String apiname);

    @Update
    void updateOfflineTable(Offlinetable offlinetable);

}
