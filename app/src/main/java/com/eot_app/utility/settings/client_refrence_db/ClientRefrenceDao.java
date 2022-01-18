package com.eot_app.utility.settings.client_refrence_db;

import static androidx.room.OnConflictStrategy.REPLACE;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import java.util.List;

/**
 * Created by Sona-11 on 13/9/21.
 */
@Dao
public interface ClientRefrenceDao {
    @Insert(onConflict = REPLACE)
    void insertClientRefrenceList(List<ClientRefrenceModel> account);


    @Query("select * from ClientRefrenceModel")
    List<ClientRefrenceModel> getRefrenceList();

    @Query("delete from ClientRefrenceModel")
    void delete();

    @Query("select * from ClientRefrenceModel where refId = :refId")
    ClientRefrenceModel getClientByRefrenceId(String refId);
}
