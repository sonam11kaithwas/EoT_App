package com.eot_app.nav_menu.jobs.job_detail.invoice.invoice_db.invebtry_items_dao;

import static androidx.room.OnConflictStrategy.REPLACE;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import com.eot_app.nav_menu.jobs.job_detail.invoice.inventry_pkg.Inventry_ReS_Model;

import java.util.List;

/**
 * Created by Sonam-11 on 29/5/20.
 */
@Dao
public interface invebtry_ItemDao {
    @Insert(onConflict = REPLACE)
    void insertInvebtryItems(List<Inventry_ReS_Model> itemDataList);

    @Query("select * from Inventry_ReS_Model order by inm ASC ")
    List<Inventry_ReS_Model> getInventryItemList();


    @Query("delete from Inventry_ReS_Model")
    void delete();
}
