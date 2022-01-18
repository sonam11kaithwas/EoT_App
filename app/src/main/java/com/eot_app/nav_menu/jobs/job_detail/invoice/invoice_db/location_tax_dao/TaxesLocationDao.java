package com.eot_app.nav_menu.jobs.job_detail.invoice.invoice_db.location_tax_dao;

import static androidx.room.OnConflictStrategy.REPLACE;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import java.util.List;

/**
 * Created by Sona-11 on 19/8/21.
 */
@Dao
public interface TaxesLocationDao {
    @Insert(onConflict = REPLACE)
    void insertAllLocationList(List<TaxesLocation> taxesLocationList);

    @Query("select * from TaxesLocation order by location asc")
    List<TaxesLocation> getTaxLocationList();


    @Query("select * from TaxesLocation where locId=:locId order by location asc")
    List<TaxesLocation> getTaxLocationListByid(String locId);

    @Query("select * from TaxesLocation where locId=:locId order by location asc")
    TaxesLocation getTaxLocationByid(String locId);

    @Query("delete from TaxesLocation")
    void delete();

}
