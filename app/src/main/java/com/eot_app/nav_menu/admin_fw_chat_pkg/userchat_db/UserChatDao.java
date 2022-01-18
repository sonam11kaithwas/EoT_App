package com.eot_app.nav_menu.admin_fw_chat_pkg.userchat_db;

import static androidx.room.OnConflictStrategy.REPLACE;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import com.eot_app.nav_menu.admin_fw_chat_pkg.sonam_user_user_chat_pkg.usertouser_model.UserChatModel;

import java.util.List;

/**
 * Created by Sonam-11 on 27/3/20.
 */
@Dao
public interface UserChatDao {
    @Insert(onConflict = REPLACE)
    void insertChatUserList(List<UserChatModel> userChatModel);

    @Query("select *from UserChatModel ORDER BY LOWER(fnm) ASC")
    List<UserChatModel> getChatUserlist();

    @Query("SELECT * FROM UserChatModel WHERE fnm LIKE (:nm) or lnm LIKE (:nm) order by lower(fnm) Asc")
    List<UserChatModel> getSearchChatuserByNmae(String nm);

    @Query("select * from UserChatModel where usrId =:usrId")
    UserChatModel getUserById(String usrId);

    @Query("UPDATE  UserChatModel SET readCount = :readCount WHERE usrId=:usrId")
    void updateUserCount(String usrId, int readCount);

    @Query("delete from UserChatModel")
    void delete();
}
