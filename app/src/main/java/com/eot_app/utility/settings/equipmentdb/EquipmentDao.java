package com.eot_app.utility.settings.equipmentdb;

import static androidx.room.OnConflictStrategy.REPLACE;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

@Dao
public interface EquipmentDao {
    @Insert(onConflict = REPLACE)
    void insertEquipmentList(List<Equipment> equipmentList);


    @Query("select * from Equipment where equId=:equId")
    Equipment getEquipmentById(String equId);

    @Query("select * from Equipment where barcode=:barcode or sno LIKE (:serialno)")
    Equipment getEquipmentByBarcodeOrSerialNo(String barcode, String serialno);

    @Update
    void updateEquipment(Equipment model);


    @Query("delete from Equipment where isdelete = 0 ")
    void deleteEquipmentByIsDelete();

    @Query("delete from Equipment")
    void delete();
}
