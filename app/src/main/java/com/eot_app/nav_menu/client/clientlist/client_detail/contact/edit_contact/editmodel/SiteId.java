package com.eot_app.nav_menu.client.clientlist.client_detail.contact.edit_contact.editmodel;

import java.io.Serializable;

/**
 * Created by Sonam-11 on 11/7/20.
 */
public class SiteId implements Serializable {
    private String siteId;
    private String snm;

    public SiteId(String siteId, String snm) {
        this.siteId = siteId;
        this.snm = snm;
    }

    public SiteId() {
    }

    public String getSiteId() {
        return siteId;
    }

    public void setSiteId(String siteId) {
        this.siteId = siteId;
    }

    public String getSnm() {
        return snm;
    }

    public void setSnm(String snm) {
        this.snm = snm;
    }
}
