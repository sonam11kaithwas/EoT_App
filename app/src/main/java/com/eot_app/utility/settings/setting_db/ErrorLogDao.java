package com.eot_app.utility.settings.setting_db;

import static androidx.room.OnConflictStrategy.REPLACE;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

/**
 * Created by aplite_pc302 on 9/4/18.
 */
@Dao
public interface ErrorLogDao {
    @Insert(onConflict = REPLACE)
    void insertError(ErrorLog items);

    @Query("select * from ErrorLog limit 1")
    ErrorLog getSingleRecord();

    @Query("delete from ErrorLog")
    void delete();

    @Query("SELECT COUNT(*) from ErrorLog")
    int getNoOfRows();

    @Query("delete from ErrorLog where id =:id")
    int deleteById(int id);
}
