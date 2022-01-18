package com.eot_app.nav_menu.client.clientlist.client_detail.contact.edit_contact.editmodel;

import java.util.HashSet;
import java.util.Set;

/**
 * Created by ubuntu on 8/6/18.
 */

public class ClientContactAddEdit_Model {

    private final String cltId;
    private final String cnm;
    private final String email;
    private final String mob1;
    private final String mob2;
    private final String fax;
    private final String twitter;
    private final String skype;
    private final int def;
    int checkd;
    String compId;
    private String tempId;
    private Set<String> siteId = new HashSet<>();

    public ClientContactAddEdit_Model(String compId, String cltId, String cnm, String email, String mob, String alternate, String fax, String skype, String twitter, int i, int checked, Set<String> siteIds) {
        this.compId = compId;
        this.cltId = cltId;
        this.cnm = cnm;
        this.email = email;
        this.mob1 = mob;
        this.mob2 = alternate;
        this.fax = fax;
        this.twitter = twitter;
        this.skype = skype;
        this.def = i;
        this.checkd = checked;
        this.siteId = siteIds;
    }

    public Set<String> getSiteId() {
        return siteId;
    }

    public String getTempId() {
        return tempId;
    }

    public void setTempId(String tempId) {
        this.tempId = tempId;
    }

    public int getCheckd() {
        return checkd;
    }

    public String getCompId() {
        return compId;
    }

    public String getCltId() {
        return cltId;
    }

    public String getCnm() {
        return cnm;
    }

    public String getEmail() {
        return email;
    }

    public String getMob1() {
        return mob1;
    }

    public String getMob2() {
        return mob2;
    }

    public String getFax() {
        return fax;
    }

    public String getTwitter() {
        return twitter;
    }

    public String getSkype() {
        return skype;
    }

    public int getDef() {
        return def;
    }
}

class ClientContactEdit_Model {
    private final String cltId;
    private final String conId;
    private final String cnm;
    private final String email;
    private final String mob1;
    private final String mob2;
    private final String fax;
    private final String twitter;
    private final String skype;
    private final int def;
    int checkd;
    String compId;
    private Set<String> siteId = new HashSet<>();


    public ClientContactEdit_Model(String cltId, String conId, String cnm, String email, String mob1, String mob2, String fax, String twitter, String skype, int def, int checkd, Set<String> siteId) {
        this.cltId = cltId;
        this.conId = conId;
        this.cnm = cnm;
        this.email = email;
        this.mob1 = mob1;
        this.mob2 = mob2;
        this.fax = fax;
        this.skype = skype;
        this.twitter = twitter;
        this.def = def;
        this.checkd = checkd;
        this.siteId = siteId;
    }


    public int getCheckd() {
        return checkd;
    }

    public String getConId() {
        return conId;
    }

    public String getCnm() {
        return cnm;
    }

    public String getEmail() {
        return email;
    }

    public String getMob1() {
        return mob1;
    }

    public String getMob2() {
        return mob2;
    }

    public String getFax() {
        return fax;
    }

    public String getTwitter() {
        return twitter;
    }

    public String getSkype() {
        return skype;
    }

    public int getDef() {
        return def;
    }

    public Set<String> getSiteId() {
        return siteId;
    }
}