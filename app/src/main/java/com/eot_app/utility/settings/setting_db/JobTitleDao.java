package com.eot_app.utility.settings.setting_db;

import static androidx.room.OnConflictStrategy.REPLACE;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import java.util.List;

/**
 * Created by aplite_pc302 on 6/21/18.
 */
@Dao
public interface JobTitleDao {
    @Insert(onConflict = REPLACE)
    void inserTitle(List<JobTitle> titles);

    @Query("select * from JobTitle")
    List<JobTitle> getJobTitlelist();

    @Query("SELECT COUNT(*) from JobTitle")
    int getCount();

    @Query("SELECT title from JobTitle")
    List<String> getJobtitleNameList();

    @Query("delete from JobTitle")
    void delete();

    @Query("select * from JobTitle where jtId=:jtId")
    JobTitle getJobTitleByid(String jtId);
}
