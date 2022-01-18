package com.eot_app.nav_menu.jobs.job_detail.invoice.invoice_db.tax_dao;

import static androidx.room.OnConflictStrategy.REPLACE;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import com.eot_app.nav_menu.jobs.job_detail.invoice.invoice_detail_pkg.inv_detail_model.Tax;

import java.util.List;

/**
 * Created by Sonam-11 on 29/5/20.
 */
@Dao
public interface Invoice_TaxDao {
    @Insert(onConflict = REPLACE)
    void insertInvoiceTaxes(List<Tax> itemDataList);

    @Query("select * from Tax order by label asc")
    List<Tax> getTaxList();

    @Query("select * from Tax where locId=:locId")
    List<Tax> getTaxListByLocId(String locId);

    @Query("delete from Tax")
    void delete();
}
