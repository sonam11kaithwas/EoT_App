package com.eot_app.nav_menu.jobs.job_detail.chat.fire_Base_Model;

/**
 * Created by aplite_pc302 on 1/9/19.
 */

public class FirestoreFieldWorkerModel {
    private int unread = 0;
    private int cltunread;

    public int getUnread() {
        return unread;
    }

    public void setUnread(int unread) {
        this.unread = unread;
    }

    public int getCltunread() {
        return cltunread;
    }

    public void setCltunread(int cltunread) {
        this.cltunread = cltunread;
    }
}
