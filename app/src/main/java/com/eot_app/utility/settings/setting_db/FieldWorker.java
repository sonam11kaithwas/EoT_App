package com.eot_app.utility.settings.setting_db;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.Index;
import androidx.room.PrimaryKey;

import com.eot_app.utility.DropdownListBean;

/**
 * Created by aplite_pc302 on 7/3/18.
 */
@Entity(indices = {@Index(value = "usrId", unique = true)}) // check user first name is not repeat.
public class FieldWorker implements DropdownListBean {
    @PrimaryKey
    @NonNull
    private String usrId;
    private String fnm;
    private String lnm;
    private String email;
    private String mob1;
    private String img;
    private String lat;
    private String lng;
    private String extra;
    @Ignore
    private boolean select = false;

    public String getExtra() {
        return extra;
    }

    public void setExtra(String extra) {
        this.extra = extra;
    }

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

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getMob1() {
        return mob1;
    }

    public void setMob1(String mob1) {
        this.mob1 = mob1;
    }

    public String getImg() {
        return img;
    }

    public void setImg(String img) {
        this.img = img;
    }

    public String getLat() {
        return lat;
    }

    public void setLat(String lat) {
        this.lat = lat;
    }

    public String getLng() {
        return lng;
    }

    public void setLng(String lng) {
        this.lng = lng;
    }

    @Override
    public String getKey() {
        return getUsrId();
    }

    @Override
    public String getName() {
        return getFnm();
    }

    public boolean isSelect() {
        return select;
    }

    public void setSelect(boolean select) {
        this.select = select;
    }
}
