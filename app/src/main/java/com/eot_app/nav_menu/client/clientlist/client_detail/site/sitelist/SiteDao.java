package com.eot_app.nav_menu.client.clientlist.client_detail.site.sitelist;

import static androidx.room.OnConflictStrategy.REPLACE;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

/**
 * Created by ubuntu on 8/6/18.
 */

@Dao
public interface SiteDao {
    @Insert(onConflict = REPLACE)
    void insertSiteList(List<Site_model> site_model);

    @Insert(onConflict = REPLACE)
    void insertSingleSiteList(Site_model site_model);


    @Query("select *from Site_model ORDER BY LOWER(snm) ASC")
    List<Site_model> getSitelist();

    @Query("select *from Site_model where cltId= :cltId order by LOWER(snm) ASC")
    List<Site_model> getSitesFromCltId(int cltId);

    @Insert(onConflict = REPLACE)
    void insertNewSite(Site_model site_model);

    @Update
    int update(Site_model... site_model);

    @Query("select * from Site_model where siteId = :site_id")
    Site_model getSiteFromSiteId(String site_id);

    @Query("delete from Site_model")
    void delete();

    @Query("SELECT * FROM Site_model WHERE snm LIKE :snmText and cltId=:cltId")
    List<Site_model> getSitelist(String snmText, String cltId);

    @Query("update Site_model set def = 0 where siteId != :siteId and cltId = :cltId")
    void updateDefault(String cltId, String siteId);

    @Query("select * from Site_model where def=1 and cltId = :cltId")
    Site_model getDefaultFromCltId(String cltId);

    @Query("delete from Site_model where isdelete = 0")
    void deleteSiteByIsDelete();

    @Query("SELECT * FROM Site_model WHERE def=1 and cltId=:cltId order by siteId DESC limit 1")
    Site_model getLastUpdateDefault(String cltId);

    @Query("SELECT COUNT(*) from Site_model")
    int getTotalCount();

    @Query("UPDATE Site_model SET siteId = :siteId WHERE siteId = :tempId and tempId = :tempId")
    void updateSiteByTempIdtoOriganalId(String siteId, String tempId);

    //    delete Site which is unused.
    @Query("delete from Site_model where siteId=tempId")
    void deleteUnusedTempSite();

    @Query("select *from Site_model where cltId= :cltId order by LOWER(snm) ASC")
    List<Site_model> getSitesByCltId(String cltId);

    @Query("select * from Site_model where cltId = :cltId")
    Site_model getSiteByClientId(String cltId);

}

