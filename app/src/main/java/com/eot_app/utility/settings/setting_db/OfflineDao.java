package com.eot_app.utility.settings.setting_db;

import static androidx.room.OnConflictStrategy.REPLACE;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

/**
 * Created by aplite_pc302 on 6/25/18.
 */
@Dao
public interface OfflineDao {
    @Insert(onConflict = REPLACE)
    void insertOffline(Offlinetable offlinetable);

    @Query("select * from Offlinetable where service_name =:servicename")
    List<Offlinetable> getAllLocations(String servicename);


    @Delete
    void deleteAllLocation(List<Offlinetable> offlinetables);

    @Query("select * from Offlinetable limit 1")
    Offlinetable getSingleRecord();

    @Query("SELECT COUNT(*) from offlinetable")
    int getCountOfRow();

    @Query("delete from Offlinetable")
    void delete();

    @Query("DELETE FROM Offlinetable WHERE id = :id")
    int deleteFromId(int id);

    @Query("UPDATE Offlinetable SET count = :update_count WHERE id = :id")
    void updateCountById(int id, int update_count);

    @Query("DELETE FROM Offlinetable WHERE params like :jobId")
    int deleteFromSearchJobID(String jobId);

    @Query("select * from Offlinetable where service_name like :serviceName")
    List<Offlinetable> getOfflinetablesById(String serviceName);

    /***/
    @Query("select * from Offlinetable")
    List<Offlinetable> getList();


    @Update
    void update(Offlinetable... offlinetablesupdate);

}
