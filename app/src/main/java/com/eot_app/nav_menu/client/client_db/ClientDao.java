package com.eot_app.nav_menu.client.client_db;

import static androidx.room.OnConflictStrategy.REPLACE;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

/**
 * Created by aplite_pc302 on 6/1/18.
 */
@Dao
public interface ClientDao {
    @Insert(onConflict = REPLACE)
    void insertUser(List<Client> client);

    @Insert(onConflict = REPLACE)
    void inserSingleClient(Client client);

    @Query("select * from Client order by LOWER(nm) ASC")
    List<Client> getClientList();

    @Query("select * from Client where cltId = :cltId LIMIT 1")
    Client getClientFromId(int cltId);

    @Update
    int update(Client... client);

    @Insert(onConflict = REPLACE)
    void insertNewClient(Client client);

    @Query("delete from Client")
    void delete();

    @Query("SELECT COUNT(*) from Client")
    int getTotalCount();

    @Query("SELECT * FROM Client WHERE nm LIKE :nmText")
    List<Client> getClientList(String nmText);

    @Query("delete from Client where isdelete = 0")
    void deleteClientByIsDelete();

    @Query("UPDATE Client SET cltId = :cltId WHERE cltId = :tempId and tempId = :tempId")
    void udpateClientByTempIdtoOriganalId(String cltId, String tempId);

    //    delete Client which is unused.
    @Query("delete from Client where cltId=tempId")
    void deleteUnusedTempClient();

    @Query("select * from Client where isactive=1 order by LOWER(nm) ASC")
    List<Client> getActiveClientList();

    /**
     * clent list get in desending order
     ***/
    @Query("select * from Client where isactive=1 order by LOWER(nm) DESC")
    List<Client> getActiveClientListInDescOrdr();


    @Query("select * from Client where cltId = :cltId")
    Client getClientById(String cltId);

    @Query("select  c.cltId,c.nm,c.pymtType,c.gstNo,c.tinNo,c.industry,c.note,c.isactive,c.accid,c.acctype,c.siteId,c.snm,c.adr,c.city,c.state,c.ctry,c.conId,c.cnm,c.email,cd.mob1,cd.mob2,c.isdelete,c.tempId,c.zip,c.lat,c.lng,c.extra,c.industryName,c.referral from client  as c left join contactdata  as cd on c.cltId==cd.cltId where cd.def=1  and (c.nm like:query or cd.mob1 like:query or cd.mob2 like:query) order by LOWER(nm) ASC")
    List<Client> getClientsWithMobile(String query);

}
