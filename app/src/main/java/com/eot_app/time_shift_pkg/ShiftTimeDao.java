package com.eot_app.time_shift_pkg;

import static androidx.room.OnConflictStrategy.REPLACE;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import java.util.List;

/**
 * Created by Sona-11 on 15/11/21.
 */
@Dao
public interface ShiftTimeDao {
    @Insert(onConflict = REPLACE)
    void insertAllShiftTimeList(List<ShiftTimeReSModel> taxesLocationList);

    @Query("select * from ShiftTimeReSModel order by shiftNm asc")
    List<ShiftTimeReSModel> getShiftTimeList();

    @Query("delete from ShiftTimeReSModel")
    void delete();
}
