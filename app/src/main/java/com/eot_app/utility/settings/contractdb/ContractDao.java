package com.eot_app.utility.settings.contractdb;

import static androidx.room.OnConflictStrategy.REPLACE;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import java.util.List;

@Dao
public interface ContractDao {
    @Insert(onConflict = REPLACE)
    void insertContractList(List<ContractRes> contractResList);

    @Query("select * from ContractRes where cltId=:cltId and endDate>= :currentdateTime order by  startDate DESC")
    List<ContractRes> getContractListById(String cltId, String currentdateTime);

    @Query("delete from ContractRes where isdelete = 0 ")
    void deleteContractByIsDelete();


    @Query("delete from ContractRes")
    void delete();
}
