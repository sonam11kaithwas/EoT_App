package com.eot_app.nav_menu.audit.audit_db;

import static androidx.room.OnConflictStrategy.REPLACE;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.eot_app.nav_menu.audit.audit_list.audit_mvp.model.AuditList_Res;
import com.eot_app.nav_menu.audit.audit_list.equipment.model.Equipment_Res;

import java.util.List;

/**
 * Created by Sonam-11 on 31/8/20.
 */
@Dao
public interface AuditDao {
    @Insert(onConflict = REPLACE)
    void inserAuditList(List<AuditList_Res> job);

    @Query("select * from AuditList_Res order by schdlStart DESC")
    List<AuditList_Res> getAuditsList();

    @Query("SELECT * FROM AuditList_Res WHERE label LIKE (:label)  order by lower(label) Asc")
    List<AuditList_Res> getAuditListBySearch(String label);

    @Query("select * from AuditList_Res where audId=:audId  ")
    AuditList_Res getAuditsEquipmentList(String audId);

    @Query("update AuditList_Res set equArray=:equArray  where audId=:audId ")
    void updateEquipmentList(List<Equipment_Res> equArray, String audId);

    @Update
    void updateAudit(AuditList_Res model);

    @Query("delete from AuditList_Res")
    void delete();

    @Query("delete from AuditList_Res where isdelete = 0 ")
    void deleteJobByIsDelete();

    /**
     * new query added for equipment on job
     */
    @Query("select *from AuditList_Res WHERE equArray LIKE (:equipmentID)")
    List<AuditList_Res> getAuditEquipmentByEquipmentId(String equipmentID);

    /**
     * search job of particular date
     */
    @Query("SELECT * FROM AuditList_Res WHERE schdlStart BETWEEN :startTime AND :endTime or schdlFinish BETWEEN :startTime AND :endTime or schdlStart<= :startTime and schdlStart<=:endTime and schdlFinish>=:startTime and schdlFinish>=:endTime")
    List<AuditList_Res> getAuditListByDate(long startTime, long endTime);


    @Query("delete from AuditList_Res where audId=:audId")
    void deletAuditById(String audId);

    @Query("delete from AuditList_Res where tempId=:tempId")
    void deleteAuditByTempId(String tempId);

    @Query("select * from AuditList_Res where audId =:audId")
    AuditList_Res getAuditById(String audId);

    @Insert(onConflict = REPLACE)
    void inserSingleAudit(AuditList_Res auditList_res);

    @Update
    void updateAuditList_Res(AuditList_Res model);

    @Query("update AuditList_Res set cltId = :cltId  where audId = :audId ")
    void updateAuditCltID(String audId, String cltId);

//    @Query("delete from AuditList_Res where status !='1' or status !='7'  or status !='8'  or status !='9'")
//    void deleteAuditStatusNot();
}
