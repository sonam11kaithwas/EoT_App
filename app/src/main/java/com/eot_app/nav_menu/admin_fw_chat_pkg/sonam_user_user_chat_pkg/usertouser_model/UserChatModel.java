package com.eot_app.nav_menu.admin_fw_chat_pkg.sonam_user_user_chat_pkg.usertouser_model;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.PrimaryKey;
import androidx.room.TypeConverters;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Sonam-11 on 2020-03-27.
 */
@Entity(tableName = "UserChatModel")
public class UserChatModel implements Serializable {
    @PrimaryKey
    @NonNull
    private String usrId;
    private String fnm;
    private String lnm;
    private String img;
    private int readCount = 0;
    private String isTeam;
    @TypeConverters(TeamMemrConverter.class)
    private List<TeamMemIdModel> teamMemId = new ArrayList<>();

    public UserChatModel() {

    }

    public String getIsTeam() {
        return isTeam;
    }

    public void setIsTeam(String isTeam) {
        this.isTeam = isTeam;
    }

    public List<TeamMemIdModel> getTeamMemId() {
        return teamMemId;
    }

    public void setTeamMemId(List<TeamMemIdModel> teamMemId) {
        this.teamMemId = teamMemId;
    }

    @NonNull
    public String getUsrId() {
        return usrId;
    }

    public void setUsrId(String usrId) {
        this.usrId = usrId;
    }

    public String getFnm() {
        return fnm;
    }

    public void setFnm(String fnm) {
        this.fnm = fnm;
    }

    public String getLnm() {
        return lnm;
    }

    public void setLnm(String lnm) {
        this.lnm = lnm;
    }

    public String getImg() {
        return img;
    }

    public void setImg(String img) {
        this.img = img;
    }

    public int getReadCount() {
        return readCount;
    }

    public void setReadCount(int readCount) {
        this.readCount = readCount;
    }


}
