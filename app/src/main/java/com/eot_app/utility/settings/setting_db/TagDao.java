package com.eot_app.utility.settings.setting_db;

import static androidx.room.OnConflictStrategy.REPLACE;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import java.util.List;
import java.util.Set;

/**
 * Created by aplite_pc302 on 8/31/18.
 */
@Dao
public interface TagDao {
    @Insert(onConflict = REPLACE)
    void inserTags(List<TagData> workers);

    @Query("select * from TagData")
    List<TagData> getTagslist();

    @Query("SELECT COUNT(*) from TagData")
    int getCount();

    @Query("delete from TagData")
    void delete();

    @Query("select * from TagData where tagId in (:tagId)")
    List<TagData> tagDataList(Set<String> tagId);

    @Query("select * from TagData where tagId in (:tag_id)")
    TagData tagDataGet(String tag_id);

}
