package com.eot_app.utility.settings.clientaccount_db;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.Index;
import androidx.room.PrimaryKey;

import com.eot_app.utility.DropdownListBean;

/**
 * Created by ubuntu on 23/6/18.
 */
@Entity(indices = {@Index(value = "accId", unique = true)})
public class ClientAccountType implements DropdownListBean {

    @PrimaryKey
    @NonNull
    private String accId;
    private String compId;
    private String type;
    private String extra;

    public String getExtra() {
        return extra;
    }

    public void setExtra(String extra) {
        this.extra = extra;
    }

    //    public ClientAccountType(String accId, String compId, String type) {
//        this.accId = accId;
//        this.compId = compId;
//        this.type = type;
//    }

    public String getAccId() {
        return accId;
    }

    public void setAccId(String accId) {
        this.accId = accId;
    }

    public String getCompId() {
        return compId;
    }

    public void setCompId(String compId) {
        this.compId = compId;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    @Override
    public String getKey() {
        return accId;
    }

    @Override
    public String getName() {
        return type;
    }
}
